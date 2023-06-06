package com.etoxto.lab1.controller;

import com.etoxto.lab1.network.request.StageRequest;
import com.etoxto.lab1.network.request.TaskRequest;
import com.etoxto.lab1.service.ManagerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {

    private final ManagerService managerService;
    @Autowired
    public ManagerController(ManagerService managerService){
        this.managerService = managerService;
    }


    @PostMapping("/createTask/{name}")
    public ResponseEntity<?> createTask(@PathVariable String name,
                                 @RequestBody TaskRequest dto) {
        return managerService.createTask(name, dto.getStageRequest1(), dto.getStageRequest2(), dto.getStageRequest3());
    }

    @PostMapping("/stage1/{name}")
    public ResponseEntity<?> assignStage1(@PathVariable String name,
                                          @RequestBody StageRequest dto) {
        return managerService.assignStage1(name, dto);
    }

    @PostMapping("/stage2/{name}")
    public ResponseEntity<?> assignStage2(@PathVariable String name,
                                          @RequestBody StageRequest dto) {
        return managerService.assignStage2(name, dto);
    }

    @PostMapping("/stage3/{name}")
    public ResponseEntity<?> assignStage3(@PathVariable String name,
                                          @RequestBody StageRequest dto) {
        return managerService.assignStage3(name, dto);
    }
}
