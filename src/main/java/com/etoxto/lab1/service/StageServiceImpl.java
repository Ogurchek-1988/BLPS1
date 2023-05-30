package com.etoxto.lab1.service;

import com.etoxto.lab1.model.stage.Stage;
import com.etoxto.lab1.model.stage.StageRepository;
import com.etoxto.lab1.network.request.StageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;

    @Autowired
    public StageServiceImpl(StageRepository stageRepository) {
        this.stageRepository = stageRepository;
    }

    @Override
    public Stage save(Stage stage) {
        return stageRepository.save(stage);
    }
}
