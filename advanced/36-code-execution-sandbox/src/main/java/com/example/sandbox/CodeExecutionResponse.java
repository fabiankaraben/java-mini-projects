package com.example.sandbox;

public class CodeExecutionResponse {
    private String stdout;
    private String stderr;
    private int exitCode;
    private boolean timeout;

    public CodeExecutionResponse(String stdout, String stderr, int exitCode, boolean timeout) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCode = exitCode;
        this.timeout = timeout;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }

    public int getExitCode() {
        return exitCode;
    }

    public boolean isTimeout() {
        return timeout;
    }
}
