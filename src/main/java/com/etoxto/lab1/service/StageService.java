package com.etoxto.lab1.service;

import com.etoxto.lab1.model.stage.Stage;

public interface StageService {
    Stage save(Stage stage);

    Stage getByName(String name);
}
