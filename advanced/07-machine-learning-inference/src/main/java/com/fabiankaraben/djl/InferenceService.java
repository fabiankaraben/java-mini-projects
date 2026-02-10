package com.fabiankaraben.djl;

import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface InferenceService {
    List<Classifications.Classification> predict(InputStream imageStream) throws IOException, TranslateException;
}
