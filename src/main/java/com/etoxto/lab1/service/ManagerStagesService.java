package com.etoxto.lab1.service;

import com.etoxto.lab1.model.managerStages.ManagerStages;
import com.etoxto.lab1.model.stage.Stage;

public interface ManagerStagesService {
    ManagerStages getByName(String name);
    ManagerStages save(ManagerStages managerStages);
    boolean existByName(String name);
    ManagerStages setStage1(Long id, Stage stage);
    ManagerStages setStage2(Long id, Stage stage);
    ManagerStages setStage3(Long id, Stage stage);
}
