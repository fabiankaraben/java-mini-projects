package com.example.chaos.config;

import java.util.List;

public class ChaosConfig {
    private List<Target> targets;

    public List<Target> getTargets() {
        return targets;
    }

    public void setTargets(List<Target> targets) {
        this.targets = targets;
    }

    public static class Target {
        private String className;
        private String methodName;
        private String failureType; // LATENCY, EXCEPTION
        private long latencyMs;
        private String exceptionClass;
        private double rate; // 0.0 to 1.0

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getFailureType() {
            return failureType;
        }

        public void setFailureType(String failureType) {
            this.failureType = failureType;
        }

        public long getLatencyMs() {
            return latencyMs;
        }

        public void setLatencyMs(long latencyMs) {
            this.latencyMs = latencyMs;
        }

        public String getExceptionClass() {
            return exceptionClass;
        }

        public void setExceptionClass(String exceptionClass) {
            this.exceptionClass = exceptionClass;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }
    }
}
