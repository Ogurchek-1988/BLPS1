package com.etoxto.lab1.service;

import com.etoxto.lab1.model.managerStages.ManagerStages;
import com.etoxto.lab1.model.managerStages.ManagerStagesRepository;
import com.etoxto.lab1.model.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ManagerStagesServiceImpl implements ManagerStagesService{
    private final ManagerStagesRepository managerStagesRepository;

    @Autowired
    public ManagerStagesServiceImpl(ManagerStagesRepository managerStagesRepository){
        this.managerStagesRepository = managerStagesRepository;
    }

    @Override
    public ManagerStages getByName(String name) {
        return managerStagesRepository.getManagerStagesByName(name);
    }

    @Override
    public ManagerStages save(ManagerStages managerStages) {
        return managerStagesRepository.save(managerStages);
    }

    @Override
    public boolean existByName(String name) {
        return managerStagesRepository.existsManagerStagesByName(name);
    }

    @Override
    public ManagerStages setStage1(Long id, Stage stage) {
        return managerStagesRepository.setStage1ById(id, stage);
    }

    @Override
    public ManagerStages setStage2(Long id, Stage stage) {
        return managerStagesRepository.setStage2ById(id, stage);
    }

    @Override
    public ManagerStages setStage3(Long id, Stage stage) {
        return managerStagesRepository.setStage3ById(id, stage);
    }
}
