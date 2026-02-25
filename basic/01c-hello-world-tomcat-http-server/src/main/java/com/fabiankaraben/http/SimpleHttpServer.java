package com.fabiankaraben.http;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class SimpleHttpServer {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(SimpleHttpServer.class.getName());
    private static final int PORT = 8080;
    private Tomcat tomcat;

    public void start() throws Exception {
        tomcat = new Tomcat();
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.setPort(PORT);
        tomcat.getConnector(); // Trigger creation of default connector

        Context context = tomcat.addContext("", new File(".").getAbsolutePath());
        Tomcat.addServlet(context, "HelloHandler", new HelloHandler());
        context.addServletMappingDecoded("/*", "HelloHandler");

        tomcat.start();
        logger.info("Server started on port " + PORT);
    }

    public void stop() throws Exception {
        if (tomcat != null) {
            tomcat.stop();
            tomcat.destroy();
            logger.info("Server stopped");
        }
    }

    public static void main(String[] args) {
        SimpleHttpServer httpServer = new SimpleHttpServer();
        try {
            httpServer.start();

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Stopping server...");
                try {
                    httpServer.stop();
                } catch (Exception e) {
                    logger.severe("Error stopping server: " + e.getMessage());
                }
            }));

            httpServer.tomcat.getServer().await();

        } catch (Exception e) {
            logger.severe("Failed to start server: " + e.getMessage());
        }
    }

    public int getPort() {
        if (tomcat != null && tomcat.getConnector() != null) {
            return tomcat.getConnector().getLocalPort();
        }
        return PORT;
    }
}
