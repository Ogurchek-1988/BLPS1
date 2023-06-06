package com.etoxto.lab1.service;

import com.etoxto.lab1.model.managerStages.ManagerStages;
import com.etoxto.lab1.model.managerStages.ManagerStagesRepository;
import com.etoxto.lab1.model.procurement.AssignedType;
import com.etoxto.lab1.model.procurement.Procurement;
import com.etoxto.lab1.model.procurement.ProcurementRepository;
import com.etoxto.lab1.model.stage.Stage;
import com.etoxto.lab1.model.stage.StageRepository;
import com.etoxto.lab1.network.request.StageRequest;
import com.etoxto.lab1.network.respons.ResponseWrapper;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class ManagerService {

    StageRepository stageRepository;

    ProcurementRepository procurementRepository;

    ManagerStagesRepository managerStagesRepository;

    @Transactional
    public ResponseEntity<?> createTask(String name,
                                        StageRequest dtoStage1,
                                        StageRequest dtoStage2,
                                        StageRequest dtoStage3) {
        try {
            var stage1 = stageRepository.save(new Stage(dtoStage1.getName(), LocalDate.parse(dtoStage1.getDate())));
            var stage2 = stageRepository.save(new Stage(dtoStage2.getName(), LocalDate.parse(dtoStage2.getDate())));
            var stage3 = stageRepository.save(new Stage(dtoStage3.getName(), LocalDate.parse(dtoStage3.getDate())));
            var procurement = procurementRepository.save(new Procurement(name, stage1, stage2, stage3, AssignedType.MANAGER));
            return ResponseEntity.ok().body(new ResponseWrapper("Manager created task - " + procurement.getName()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    public ResponseEntity<?> assignStage1(String name,
                                          StageRequest dto) {
        try {
            var stage = stageRepository.save(new Stage(dto.getName(), LocalDate.parse(dto.getDate())));
            if (managerStagesRepository.existsManagerStagesByName(name)) {
                ManagerStages managerStages = managerStagesRepository.getManagerStagesByName(name);
                managerStages.setStage1(stage);
                managerStagesRepository.save(managerStages);
                return ResponseEntity.ok().body(new ResponseWrapper("Stage1 Added"));
            }
            managerStagesRepository.save(new ManagerStages(name, stage, null, null));
            return ResponseEntity.ok().body(new ResponseWrapper("Stage1 Added"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    public ResponseEntity<?> assignStage2(@PathVariable String name,
                                          @RequestBody StageRequest dto) {
        try {
            var stage = stageRepository.save(new Stage(dto.getName(), LocalDate.parse(dto.getDate())));
            if (managerStagesRepository.existsManagerStagesByName(name)) {
                ManagerStages managerStages = managerStagesRepository.getManagerStagesByName(name);
                managerStages.setStage2(stage);
                managerStagesRepository.save(managerStages);
                return ResponseEntity.ok().body(new ResponseWrapper("Stage2 Added"));
            }
            managerStagesRepository.save(new ManagerStages(name, null, stage, null));
            return ResponseEntity.ok().body(new ResponseWrapper("Stage2 Added"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    public ResponseEntity<?> assignStage3(@PathVariable String name,
                                          @RequestBody StageRequest dto) {
        try {
            var stage = stageRepository.save(new Stage(dto.getName(), LocalDate.parse(dto.getDate())));
            if (managerStagesRepository.existsManagerStagesByName(name)) {
                ManagerStages managerStages = managerStagesRepository.getManagerStagesByName(name);
                managerStages.setStage3(stage);
                managerStagesRepository.save(managerStages);
                return ResponseEntity.ok().body(new ResponseWrapper("Stage3 Added"));
            }
            managerStagesRepository.save(new ManagerStages(name, null, null, stage));
            return ResponseEntity.ok().body(new ResponseWrapper("Stage3 Added"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }
}
