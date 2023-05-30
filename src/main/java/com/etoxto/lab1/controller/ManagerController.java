package com.etoxto.lab1.controller;

import com.etoxto.lab1.model.stage.Stage;
import com.etoxto.lab1.model.stage.StageRepository;
import com.etoxto.lab1.network.request.StageRequest;
import com.etoxto.lab1.service.StageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {
    private final StageService stageService;
    @Autowired
    public ManagerController(StageService stageService){
        this.stageService = stageService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStage(@RequestBody StageRequest dto) {
        log.info("Creating STAGE");
        var stage = stageService.save(new Stage(dto.getName(), dto.getDate()));
        return ResponseEntity.ok().body(stage.getName());
    }
}
