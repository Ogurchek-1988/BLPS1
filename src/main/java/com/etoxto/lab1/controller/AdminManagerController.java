package com.etoxto.lab1.controller;

import com.etoxto.lab1.network.request.AdditionallyRequest;
import com.etoxto.lab1.network.request.StageRequest;
import com.etoxto.lab1.network.request.TaskRequest;
import com.etoxto.lab1.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/admin")
public class AdminManagerController {

    private final AdminManagerService adminManagerService;

    @Autowired
    public AdminManagerController(AdminManagerService adminManagerService) {
        this.adminManagerService = adminManagerService;
    }

    @PostMapping("/createTask/{name}")
    public ResponseEntity<?> createTask(@PathVariable String name,
                                 @RequestBody TaskRequest dto) {
        return adminManagerService.createTask(name, dto.getStageRequest1(), dto.getStageRequest2(), dto.getStageRequest3());
    }

    @PostMapping("/checkTask/{name}")
    public ResponseEntity<?> checkTask(@PathVariable String name) {
        return adminManagerService.checkTask(name);
    }

    @PostMapping("/createIssue/{name}")
    ResponseEntity<?> createIssue(@PathVariable String name,
                                  @RequestBody AdditionallyRequest additionallyRequest) {
        return adminManagerService.createIssue(name, additionallyRequest);
    }

    @PostMapping("/extendTime/{name}")
    public ResponseEntity<?> extendTime(@PathVariable String name,
                                 @RequestBody int days) {
        return adminManagerService.extendTime(name, days);
    }

    @PostMapping("/closeIssue/{name}")
    ResponseEntity<?> closeIssue(@PathVariable String name) {
        return adminManagerService.closeIssue(name);
    }
}
