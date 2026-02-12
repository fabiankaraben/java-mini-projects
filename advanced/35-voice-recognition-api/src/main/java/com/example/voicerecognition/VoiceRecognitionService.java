package com.example.voicerecognition;

import org.vosk.Model;
import org.vosk.Recognizer;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

@Service
public class VoiceRecognitionService {

    private Model model;

    @PostConstruct
    public void init() throws IOException {
        // We assume the model is located at "model" directory in the working directory
        // In Docker, we will mount or copy the model there.
        // For local development, the user needs to download it.
        try {
            this.model = new Model("model");
        } catch (IOException e) {
            System.err.println("Could not load Vosk model from 'model' directory. " +
                    "Make sure you have downloaded a model from https://alphacephei.com/vosk/models " +
                    "and extracted it to a 'model' folder in the project root.");
            throw e;
        }
    }

    public String recognize(InputStream audioStream) throws IOException {
        try (Recognizer recognizer = new Recognizer(model, 16000)) {
            BufferedInputStream bis = new BufferedInputStream(audioStream);
            byte[] b = new byte[4096];
            int len;
            StringBuilder result = new StringBuilder();
            
            while ((len = bis.read(b)) >= 0) {
                if (recognizer.acceptWaveForm(b, len)) {
                    // We can accumulate partial results if needed, but for now we just wait for final
                    // result.append(recognizer.getResult());
                } else {
                    // result.append(recognizer.getPartialResult());
                }
            }
            
            // getFinalResult() returns a JSON string like { "text" : "..." }
            return recognizer.getFinalResult();
        }
    }
}
