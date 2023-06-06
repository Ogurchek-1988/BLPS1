package com.etoxto.lab1.controller;

import com.etoxto.lab1.model.managerStages.ManagerStages;
import com.etoxto.lab1.model.procurement.AssignedType;
import com.etoxto.lab1.model.procurement.Procurement;
import com.etoxto.lab1.model.stage.Stage;
import com.etoxto.lab1.network.request.StageRequest;
import com.etoxto.lab1.network.respons.ResponseWrapper;
import com.etoxto.lab1.service.ManagerStagesService;
import com.etoxto.lab1.service.ProcurementService;
import com.etoxto.lab1.service.StageService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/v1/manager")
public class ManagerController {
    private final StageService stageService;
    private final ProcurementService procurementService;
    private final ManagerStagesService managerStagesService;
    @Autowired
    public ManagerController(StageService stageService, ProcurementService procurementService, ManagerStagesService managerStagesService){
        this.stageService = stageService;
        this.procurementService = procurementService;
        this.managerStagesService = managerStagesService;
    }

    @Transactional
    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(@RequestBody String name,
                                 @RequestBody StageRequest dtoStage1,
                                 @RequestBody StageRequest dtoStage2,
                                 @RequestBody StageRequest dtoStage3) {
        try {
            var stage1 = stageService.save(new Stage(dtoStage1.getName(), dtoStage2.getDate()));
            var stage2 = stageService.save(new Stage(dtoStage2.getName(), dtoStage2.getDate()));
            var stage3 = stageService.save(new Stage(dtoStage3.getName(), dtoStage3.getDate()));
            var procurement = procurementService.save(new Procurement(name, stage1, stage2, stage3, AssignedType.MANAGER));
            return ResponseEntity.ok().body(new ResponseWrapper("Manager created task - " + procurement.getName()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    @PostMapping("/stage1/{name}")
    public ResponseEntity<?> assignStage1(@PathVariable String name,
                                          @RequestBody StageRequest dto) {
        try {
            var stage = stageService.save(new Stage(dto.getName(), dto.getDate()));
            if (managerStagesService.existByName(name)) {
                ManagerStages managerStages = managerStagesService.getByName(name);
                managerStages.setStage1(stage);
                managerStagesService.save(managerStages);
                return ResponseEntity.ok().body(new ResponseWrapper("Stage1 Added"));
            }
            managerStagesService.save(new ManagerStages(name, stage, null, null));
            return ResponseEntity.ok().body(new ResponseWrapper("Stage1 Added"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    @PostMapping("/stage2/{name}")
    public ResponseEntity<?> assignStage2(@PathVariable String name,
                                          @RequestBody StageRequest dto) {
        try {
            var stage = stageService.save(new Stage(dto.getName(), dto.getDate()));
            if (managerStagesService.existByName(name)) {
                ManagerStages managerStages = managerStagesService.getByName(name);
                managerStages.setStage2(stage);
                managerStagesService.save(managerStages);
                return ResponseEntity.ok().body(new ResponseWrapper("Stage2 Added"));
            }
            managerStagesService.save(new ManagerStages(name, null, stage, null));
            return ResponseEntity.ok().body(new ResponseWrapper("Stage2 Added"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    @PostMapping("/stage3/{name}")
    public ResponseEntity<?> assignStage3(@PathVariable String name,
                                          @RequestBody StageRequest dto) {
        try {
            var stage = stageService.save(new Stage(dto.getName(), dto.getDate()));
            if (managerStagesService.existByName(name)) {
                ManagerStages managerStages = managerStagesService.getByName(name);
                managerStages.setStage3(stage);
                managerStagesService.save(managerStages);
                return ResponseEntity.ok().body(new ResponseWrapper("Stage3 Added"));
            }
            managerStagesService.save(new ManagerStages(name, null, null, stage));
            return ResponseEntity.ok().body(new ResponseWrapper("Stage3 Added"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }
}
