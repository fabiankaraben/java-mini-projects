package com.example.videothumbnail;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
public class FFmpegInvoker {

    @Value("${ffmpeg.path:/usr/bin/ffmpeg}")
    private String ffmpegPath;

    @Value("${ffprobe.path:/usr/bin/ffprobe}")
    private String ffprobePath;

    public void generateThumbnail(Path videoPath, Path thumbnailPath) throws IOException {
        FFmpeg ffmpeg = new FFmpeg(ffmpegPath);
        FFprobe ffprobe = new FFprobe(ffprobePath);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(videoPath.toString())
                .overrideOutputFiles(true)
                .addOutput(thumbnailPath.toString())
                .setFrames(1)
                .setVideoCodec("png")
                .setVideoFrameRate(1)
                .setVideoFilter("select='gte(n\\,10)',scale=320:-1")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
        executor.createJob(builder).run();
    }
}
