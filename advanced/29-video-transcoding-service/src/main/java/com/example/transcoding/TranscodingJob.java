package com.example.transcoding;

public class TranscodingJob {
    private String id;
    private String originalFilename;
    private String outputFilename;
    private JobStatus status;
    private String message;

    public TranscodingJob(String id, String originalFilename) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.status = JobStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
