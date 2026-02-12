package com.example.transcoding;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ws.schild.jave.Encoder;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TranscodingService {

    private final Map<String, TranscodingJob> jobs = new ConcurrentHashMap<>();

    public TranscodingJob createJob(String id, String originalFilename) {
        TranscodingJob job = new TranscodingJob(id, originalFilename);
        jobs.put(id, job);
        return job;
    }

    public TranscodingJob getJob(String id) {
        return jobs.get(id);
    }

    @Async
    public void transcode(String jobId, File source, File target) {
        TranscodingJob job = jobs.get(jobId);
        if (job == null) {
            return;
        }

        job.setStatus(JobStatus.PROCESSING);
        job.setOutputFilename(target.getName());

        try {
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac");
            audio.setBitRate(128000);
            audio.setChannels(2);
            audio.setSamplingRate(44100);

            VideoAttributes video = new VideoAttributes();
            video.setCodec("h264");
            video.setBitRate(160000);
            video.setFrameRate(30);

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setOutputFormat("mp4");
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            Encoder encoder = getEncoder();
            encoder.encode(new MultimediaObject(source), target, attrs);

            job.setStatus(JobStatus.COMPLETED);
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            job.setMessage(e.getMessage());
            e.printStackTrace();
        }
    }

    protected Encoder getEncoder() {
        return new Encoder();
    }
}
