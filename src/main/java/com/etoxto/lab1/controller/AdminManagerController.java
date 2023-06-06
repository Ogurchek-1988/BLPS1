package com.etoxto.lab1.controller;

import com.etoxto.lab1.model.issue.Additionally;
import com.etoxto.lab1.model.issue.ExtendedTime;
import com.etoxto.lab1.model.issue.Issue;
import com.etoxto.lab1.model.issue.IssueStatus;
import com.etoxto.lab1.model.managerStages.ManagerStages;
import com.etoxto.lab1.model.procurement.Procurement;
import com.etoxto.lab1.model.procurement.AssignedType;
import com.etoxto.lab1.model.procurement.ProcurementStatus;
import com.etoxto.lab1.model.stage.Stage;
import com.etoxto.lab1.network.request.AdditionallyRequest;
import com.etoxto.lab1.network.request.StageRequest;
import com.etoxto.lab1.network.respons.ResponseWrapper;
import com.etoxto.lab1.service.IssueService;
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
@RequestMapping("api/v1/admin")
public class AdminManagerController {

    private final StageService stageService;
    private final ProcurementService procurementService;
    private final IssueService issueService;
    private final ManagerStagesService managerStagesService;

    @Autowired
    public AdminManagerController(StageService stageService, ProcurementService procurementService, IssueService issueService, ManagerStagesService managerStagesService) {
        this.stageService = stageService;
        this.procurementService = procurementService;
        this.issueService = issueService;
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
            var procurement = procurementService.save(new Procurement(name, stage1, stage2, stage3, AssignedType.ADMIN, ProcurementStatus.PROCESSING));
            return ResponseEntity.ok().body(new ResponseWrapper("Admin created task - " + procurement.getName()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    @PostMapping("/checkTask/{name}")
    public ResponseEntity<?> checkTask(@PathVariable String name) {
        try {
            Procurement adminProcurement = procurementService.getProcurementByNameAndAssignedType(name, AssignedType.ADMIN);
            if (adminProcurement == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("Task " + name + " does not exist"));
            } else if (adminProcurement.getStatus() == ProcurementStatus.REVIEWED || adminProcurement.getStatus() == ProcurementStatus.ASSIGNED) {
                return ResponseEntity.ok().body(new ResponseWrapper("Task " + name + " is reviewed or assigned"));
            }
            Procurement managerProcurement = procurementService.getProcurementByNameAndAssignedType(name, AssignedType.MANAGER);
            if (managerProcurement == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("Managers have not set time for task " + name));
            }
            if (managerProcurement.getStage1().getDate().isAfter(adminProcurement.getStage1().getDate()) ||
                    managerProcurement.getStage2().getDate().isAfter(adminProcurement.getStage2().getDate()) ||
                    managerProcurement.getStage3().getDate().isAfter(adminProcurement.getStage3().getDate())){
                return ResponseEntity.internalServerError().body(new ResponseWrapper("Managers time is not right"));
            }
            procurementService.delete(managerProcurement.getId());
            Procurement proc = procurementService.setStatus(adminProcurement.getId(), ProcurementStatus.REVIEWED);
            return ResponseEntity.ok().body(new ResponseWrapper("Task " + proc.getName() + " is reviewed"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @PostMapping("/createIssue/{name}")
    ResponseEntity<?> createIssue(@PathVariable String name,
                                  @RequestBody AdditionallyRequest additionallyRequest) {
        try {
            Procurement procurement = procurementService.getProcurementByNameAndAssignedType(name, AssignedType.ADMIN);
            if (procurement == null || procurement.getStatus() != ProcurementStatus.REVIEWED) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("Task " + name + " does not exist or not reviewed"));
            }
            if (additionallyRequest.getAdditionally() == Additionally.LIMITTIME_STAGE1) {
                procurement.getStage1().setDate(procurement.getStage1().getDate().minusDays(1));
            } else if (additionallyRequest.getAdditionally() == Additionally.LIMITTIME_STAGE2) {
                procurement.getStage2().setDate(procurement.getStage2().getDate().minusDays(1));
            } else if (additionallyRequest.getAdditionally() == Additionally.LIMITTIME_STAGE3) {
                procurement.getStage3().setDate(procurement.getStage3().getDate().minusDays(1));
            }
            Issue issue = issueService.save(new Issue(procurement, ExtendedTime.NO, IssueStatus.ACTIVE, additionallyRequest.getAdditionally()));
            return ResponseEntity.ok().body(new ResponseWrapper("Issue " + issue.getProcurement().getName() + " created"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    @PostMapping("/extendTime/{name}")
    public ResponseEntity<?> extendTime(@PathVariable String name,
                                 @RequestBody int days) {
        try {
            Issue issue = issueService.getIssueByProcurementName(name);
            if (issue == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("This issue does not exist"));
            } else if (issue.getExtendedTime() == ExtendedTime.YES) {
                issue.setIssueStatus(IssueStatus.FAILED);
                issueService.update(issue);
                return ResponseEntity.internalServerError().body(new ResponseWrapper("For this issue already extended time, you failed this task"));
            }
            ManagerStages managerStages = managerStagesService.getByName(name);
            if (managerStages == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("This manager stages does not exists"));
            }
            int count = 0;
            String msg = "";

            if (managerStages.getStage1().getDate().isAfter(issue.getProcurement().getStage1().getDate())) {
                count = count + 1;
                msg = "stage1";
                issue.getProcurement().getStage1().setDate(issue.getProcurement().getStage1().getDate().plusDays(days));
            }

            if (managerStages.getStage2().getDate().isAfter(issue.getProcurement().getStage2().getDate())) {
                if (count == 0) {
                    count = count + 1;
                    msg = "stage2";
                    issue.getProcurement().getStage2().setDate(issue.getProcurement().getStage2().getDate().plusDays(days));
                } else {
                    issue.setIssueStatus(IssueStatus.FAILED);
                    issueService.update(issue);
                    return ResponseEntity.ok().body(new ResponseWrapper("Error limit exceeded, you failed this task"));
                }
            }

            if (managerStages.getStage3().getDate().isAfter(issue.getProcurement().getStage3().getDate())) {
                if (count == 0) {
                    msg = "stage3";
                    issue.getProcurement().getStage3().setDate(issue.getProcurement().getStage3().getDate().plusDays(days));
                } else {
                    issue.setIssueStatus(IssueStatus.FAILED);
                    issueService.update(issue);
                    return ResponseEntity.ok().body(new ResponseWrapper("Error limit exceeded, you failed this task"));
                }
            }

            issue.setExtendedTime(ExtendedTime.YES);
            Issue newIssue = issueService.update(issue);
            return ResponseEntity.ok().body(new ResponseWrapper("For issue " + newIssue.getProcurement().getName() + " time is extended by " + days + " days for " + msg));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @PostMapping("/closeIssue/{name}")
    ResponseEntity<?> closeIssue(@PathVariable String name) {
        try {
            Issue issue = issueService.getIssueByProcurementName(name);
            if (issue == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("This issue does not exist"));
            }
            ManagerStages managerStages = managerStagesService.getByName(name);
            if (managerStages == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("ManagerStages does not exist"));
            } else if (managerStages.getStage1() == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("Stage1 does not exist"));
            } else if (managerStages.getStage2() == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("Stage2 does not exist"));
            } else if (managerStages.getStage3() == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("Stage3 does not exist"));
            }
            if (managerStages.getStage1().getDate().isAfter(issue.getProcurement().getStage1().getDate()) ||
                    managerStages.getStage2().getDate().isAfter(issue.getProcurement().getStage2().getDate()) ||
                    managerStages.getStage3().getDate().isAfter(issue.getProcurement().getStage3().getDate())) {
                issue.setIssueStatus(IssueStatus.FAILED);
                issueService.update(issue);
                return ResponseEntity.ok().body(new ResponseWrapper("Task " + name + " failed"));
            }
            issue.setIssueStatus(IssueStatus.SUCCESSFULLY);
            issueService.update(issue);
            return ResponseEntity.ok().body(new ResponseWrapper("Task" + name + "successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }
}
