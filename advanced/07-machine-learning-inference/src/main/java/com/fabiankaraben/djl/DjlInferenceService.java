package com.fabiankaraben.djl;

import ai.djl.Application;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.TranslateException;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class DjlInferenceService implements InferenceService {

    private ZooModel<Image, Classifications> model;

    @PostConstruct
    public void init() throws ModelNotFoundException, MalformedModelException, IOException {
        // Define the criteria to load a ResNet50 model trained on ImageNet
        Criteria<Image, Classifications> criteria = Criteria.builder()
                .optApplication(Application.CV.IMAGE_CLASSIFICATION)
                .setTypes(Image.class, Classifications.class)
                .optFilter("arch", "resnet50")
                .optFilter("layers", "50")
                .optEngine("PyTorch") 
                .build();

        this.model = criteria.loadModel();
    }

    public List<Classifications.Classification> predict(InputStream imageStream) throws IOException, TranslateException {
        Image image = ImageFactory.getInstance().fromInputStream(imageStream);

        try (Predictor<Image, Classifications> predictor = model.newPredictor()) {
            Classifications classifications = predictor.predict(image);
            return classifications.items();
        }
    }

    @PreDestroy
    public void cleanup() {
        if (model != null) {
            model.close();
        }
    }
}
