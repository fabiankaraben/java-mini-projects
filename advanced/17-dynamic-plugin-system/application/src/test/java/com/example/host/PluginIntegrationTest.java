package com.example.host;

import com.example.plugin.api.GreetingExtensionPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
class PluginIntegrationTest {

    @TempDir
    static Path pluginsDir;

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public PluginManager pluginManager() {
            return new DefaultPluginManager(pluginsDir);
        }
    }

    @Autowired
    private PluginManager pluginManager;

    @Autowired
    private PluginController pluginController;

    @Test
    void testPluginLoadingAndExecution() throws Exception {
        // 1. Create a dummy plugin jar
        createDummyPluginJar(pluginsDir.resolve("dummy-plugin.jar"));

        // 2. Load plugins via controller
        pluginController.loadPlugins();

        // 3. Verify plugin is loaded
        assertThat(pluginManager.getPlugins()).hasSize(1);
        assertThat(pluginManager.getPlugins().get(0).getPluginId()).isEqualTo("dummy-plugin");

        // 4. Verify extension execution
        List<String> greetings = pluginController.getGreetings();
        assertThat(greetings).contains("Hello from Dummy Plugin!");
    }

    private void createDummyPluginJar(Path jarPath) throws Exception {
        // Compile a simple extension class
        String source = "package com.example.plugin.dummy;" +
                "import com.example.plugin.api.GreetingExtensionPoint;" +
                "import org.pf4j.Extension;" +
                "@Extension " +
                "public class DummyGreeting implements GreetingExtensionPoint {" +
                "    public String getGreeting() { return \"Hello from Dummy Plugin!\"; }" +
                "}";

        Path sourceFile = pluginsDir.resolve("DummyGreeting.java");
        Files.writeString(sourceFile, source);

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // Setup classpath to include the current classpath (which includes plugin-api)
        String classpath = System.getProperty("java.class.path");
        
        int result = compiler.run(null, null, null, 
                "-cp", classpath, 
                "-d", pluginsDir.toString(), 
                sourceFile.toString());
        
        if (result != 0) {
            throw new RuntimeException("Compilation failed");
        }

        // Create Manifest
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifest.getMainAttributes().put(new Attributes.Name("Plugin-Id"), "dummy-plugin");
        manifest.getMainAttributes().put(new Attributes.Name("Plugin-Version"), "0.0.1");
        manifest.getMainAttributes().put(new Attributes.Name("Plugin-Provider"), "Test Provider");
        // We don't need Plugin-Class if we rely on Extension annotation index or just classpath scanning?
        // PF4J usually requires an index or a Plugin class, OR it scans classes if configured.
        // DefaultPluginManager scans for extensions if no index is present but we need to ensure it scans.
        // However, PF4J usually requires a Plugin class OR we can just omit it if we only have extensions.
        // Let's rely on standard behaviour. DefaultPluginManager uses JarPluginLoader.
        
        // Create Jar
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(jarPath.toFile()), manifest)) {
            // Add compiled class
            Path classFile = pluginsDir.resolve("com/example/plugin/dummy/DummyGreeting.class");
            JarEntry entry = new JarEntry("com/example/plugin/dummy/DummyGreeting.class");
            jos.putNextEntry(entry);
            Files.copy(classFile, jos);
            jos.closeEntry();
            
            // Add extensions.index if needed? 
            // PF4J AnnotationProcessor usually generates META-INF/extensions.idx.
            // Since we manually compiled, we didn't run the processor.
            // We need to add META-INF/extensions.idx manually for PF4J to find it efficiently, 
            // or configure it to scan (which is slower and might not be default).
            // Let's add the index.
            JarEntry indexEntry = new JarEntry("META-INF/extensions.idx");
            jos.putNextEntry(indexEntry);
            jos.write("com.example.plugin.dummy.DummyGreeting\n".getBytes());
            jos.closeEntry();
        }
    }
}
