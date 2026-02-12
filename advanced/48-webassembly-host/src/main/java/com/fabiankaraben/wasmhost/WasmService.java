package com.fabiankaraben.wasmhost;

import com.dylibso.chicory.runtime.ExportFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Module;
import com.dylibso.chicory.wasm.types.Value;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class WasmService {

    private Instance instance;
    private ExportFunction addFunction;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("add.wasm");
        try (InputStream is = resource.getInputStream()) {
            Module module = Module.builder(is).build();
            this.instance = module.instantiate();
            this.addFunction = instance.export("add");
        }
    }

    public int add(int a, int b) {
        Value[] args = new Value[]{Value.i32(a), Value.i32(b)};
        Value[] results = addFunction.apply(args);
        return results[0].asInt();
    }
}
