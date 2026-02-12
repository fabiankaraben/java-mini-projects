package com.example.chaos.agent;

import com.example.chaos.config.ChaosConfig;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Method;
import java.util.Optional;

public class ChaosAdvice {

    @Advice.OnMethodEnter
    public static void enter(@Advice.Origin Method method) throws Exception {
        ChaosConfig config = ChaosAgent.getConfig();
        if (config == null) return;

        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        Optional<ChaosConfig.Target> targetOpt = config.getTargets().stream()
            .filter(t -> t.getClassName().equals(className) && t.getMethodName().equals(methodName))
            .findFirst();

        if (targetOpt.isPresent()) {
            ChaosConfig.Target target = targetOpt.get();
            if (ChaosAgent.getRandom().nextDouble() < target.getRate()) {
                System.out.println("[ChaosAgent] Injecting failure: " + target.getFailureType() + " into " + className + "#" + methodName);
                
                if ("LATENCY".equalsIgnoreCase(target.getFailureType())) {
                    Thread.sleep(target.getLatencyMs());
                } else if ("EXCEPTION".equalsIgnoreCase(target.getFailureType())) {
                    Class<?> clazz = Class.forName(target.getExceptionClass());
                    throw (Exception) clazz.getDeclaredConstructor(String.class)
                        .newInstance("Chaos Monkey injected exception!");
                }
            }
        }
    }
}
