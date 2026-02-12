package com.example.chaos.agent;

import com.example.chaos.config.ChaosConfig;
import com.example.chaos.config.ChaosLoader;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.util.Random;

public class ChaosAgent {
    private static ChaosConfig config;
    private static final Random random = new Random();

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("[ChaosAgent] Starting Chaos Engineering Agent...");
        config = ChaosLoader.loadConfig();

        if (config == null || config.getTargets() == null || config.getTargets().isEmpty()) {
            System.out.println("[ChaosAgent] No targets configured. Exiting setup.");
            return;
        }

        AgentBuilder agentBuilder = new AgentBuilder.Default()
            .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
            .disableClassFormatChanges();

        for (ChaosConfig.Target target : config.getTargets()) {
            System.out.println("[ChaosAgent] Instrumenting " + target.getClassName() + "#" + target.getMethodName());
            agentBuilder = agentBuilder.type(ElementMatchers.named(target.getClassName()))
                .transform((builder, typeDescription, classLoader, module, protectionDomain) ->
                    builder.visit(Advice.to(ChaosAdvice.class).on(ElementMatchers.named(target.getMethodName())))
                );
        }

        agentBuilder.installOn(inst);
        System.out.println("[ChaosAgent] Agent installed successfully.");
    }
    
    public static ChaosConfig getConfig() {
        return config;
    }
    
    public static Random getRandom() {
        return random;
    }
}
