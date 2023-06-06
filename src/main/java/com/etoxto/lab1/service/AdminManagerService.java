package com.etoxto.lab1.service;

import com.etoxto.lab1.model.issue.*;
import com.etoxto.lab1.model.managerStages.ManagerStages;
import com.etoxto.lab1.model.managerStages.ManagerStagesRepository;
import com.etoxto.lab1.model.procurement.AssignedType;
import com.etoxto.lab1.model.procurement.Procurement;
import com.etoxto.lab1.model.procurement.ProcurementRepository;
import com.etoxto.lab1.model.procurement.ProcurementStatus;
import com.etoxto.lab1.model.stage.Stage;
import com.etoxto.lab1.model.stage.StageRepository;
import com.etoxto.lab1.network.request.AdditionallyRequest;
import com.etoxto.lab1.network.request.StageRequest;
import com.etoxto.lab1.network.respons.ResponseWrapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class AdminManagerService {

    StageRepository stageRepository;
    ProcurementRepository procurementRepository;
    IssueRepository issueRepository;
    ManagerStagesRepository managerStagesRepository;

    @Autowired
    public AdminManagerService(StageRepository stageRepository, ProcurementRepository procurementRepository,
                               IssueRepository issueRepository, ManagerStagesRepository managerStagesRepository) {
        this.stageRepository = stageRepository;
        this.procurementRepository = procurementRepository;
        this.issueRepository = issueRepository;
        this.managerStagesRepository = managerStagesRepository;
    }

    @Transactional
    public ResponseEntity<?> createTask(String name,
                                        StageRequest dtoStage1,
                                        StageRequest dtoStage2,
                                        StageRequest dtoStage3) {
        try {
            if (procurementRepository.getProcurementByNameAndAssignedType(name, AssignedType.ADMIN) == null) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                var stage1 = stageRepository.save(new Stage(dtoStage1.getName(), LocalDate.parse(dtoStage1.getDate(), df)));
                var stage2 = stageRepository.save(new Stage(dtoStage2.getName(), LocalDate.parse(dtoStage2.getDate(), df)));
                var stage3 = stageRepository.save(new Stage(dtoStage3.getName(), LocalDate.parse(dtoStage3.getDate(), df)));
                var procurement = procurementRepository.save(new Procurement(name, stage1, stage2, stage3, AssignedType.ADMIN, ProcurementStatus.PROCESSING));
                return ResponseEntity.ok().body(new ResponseWrapper("Admin created task - " + procurement.getName()));
            } else {
                return ResponseEntity.ok().body(new ResponseWrapper("соси жопу такое уже существует"));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    public ResponseEntity<?> checkTask(@PathVariable String name) {
        try {
            Procurement adminProcurement = procurementRepository.getProcurementByNameAndAssignedType(name, AssignedType.ADMIN);
            if (adminProcurement == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("Task " + name + " does not exist"));
            } else if (adminProcurement.getStatus() == ProcurementStatus.REVIEWED || adminProcurement.getStatus() == ProcurementStatus.ASSIGNED) {
                return ResponseEntity.ok().body(new ResponseWrapper("Task " + name + " is reviewed or assigned"));
            }
            Procurement managerProcurement = procurementRepository.getProcurementByNameAndAssignedType(name, AssignedType.MANAGER);
            if (managerProcurement == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("Managers have not set time for task " + name));
            }
            if (managerProcurement.getStage1().getDate().isAfter(adminProcurement.getStage1().getDate()) ||
                    managerProcurement.getStage2().getDate().isAfter(adminProcurement.getStage2().getDate()) ||
                    managerProcurement.getStage3().getDate().isAfter(adminProcurement.getStage3().getDate())){
                procurementRepository.deleteById(managerProcurement.getId());
                procurementRepository.deleteById(adminProcurement.getId());
                return ResponseEntity.ok().body(new ResponseWrapper("Managers time is not right"));
            }
            procurementRepository.deleteById(managerProcurement.getId());
            procurementRepository.setStatusById(adminProcurement.getId(), ProcurementStatus.REVIEWED);
            return ResponseEntity.ok().body(new ResponseWrapper("Task " + adminProcurement.getName() + " is reviewed"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    public ResponseEntity<?> createIssue(String name,
                                  AdditionallyRequest additionallyRequest) {
        try {
            Procurement procurement = procurementRepository.getProcurementByNameAndAssignedType(name, AssignedType.ADMIN);
            if (procurement == null || procurement.getStatus() != ProcurementStatus.REVIEWED) {
                return ResponseEntity.ok().body(new ResponseWrapper("Task " + name + " does not exist or not reviewed"));
            }
            if (additionallyRequest.getAdditionally() == Additionally.LIMITTIME_STAGE1) {
                procurement.getStage1().setDate(procurement.getStage1().getDate().minusDays(1));
            } else if (additionallyRequest.getAdditionally() == Additionally.LIMITTIME_STAGE2) {
                procurement.getStage2().setDate(procurement.getStage2().getDate().minusDays(1));
            } else if (additionallyRequest.getAdditionally() == Additionally.LIMITTIME_STAGE3) {
                procurement.getStage3().setDate(procurement.getStage3().getDate().minusDays(1));
            }
            Issue issue = issueRepository.save(new Issue(procurement, ExtendedTime.NO, IssueStatus.ACTIVE, additionallyRequest.getAdditionally()));
            return ResponseEntity.ok().body(new ResponseWrapper("Issue " + issue.getProcurement().getName() + " created"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    public ResponseEntity<?> checkIssue(String name) {
        try {
            Issue issue = issueRepository.getIssueByProcurementName(name);
            if (issue == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("This issue does not exist"));
            }
            ManagerStages managerStages = managerStagesRepository.getManagerStagesByName(name);
            if (managerStages == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("ManagerStages does not exist"));
            } else if (managerStages.getStage1() == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("Stage1 does not exist"));
            } else if (managerStages.getStage2() == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("Stage2 does not exist"));
            } else if (managerStages.getStage3() == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("Stage3 does not exist"));
            }
            if (managerStages.getStage1().getDate().isAfter(issue.getProcurement().getStage1().getDate()) ||
                    managerStages.getStage2().getDate().isAfter(issue.getProcurement().getStage2().getDate()) ||
                    managerStages.getStage3().getDate().isAfter(issue.getProcurement().getStage3().getDate())) {
                return ResponseEntity.ok().body(new ResponseWrapper("Task " + name + " просрочено время"));
            }
            return ResponseEntity.ok().body(new ResponseWrapper("Task " + name + " все ок"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    @Transactional
    public ResponseEntity<?> extendTime(String name, int days) {
        try {
            Issue issue = issueRepository.getIssueByProcurementName(name);
            if (issue == null) {
                return ResponseEntity.internalServerError().body(new ResponseWrapper("This issue does not exist"));
            } else if (issue.getExtendedTime() == ExtendedTime.YES) {
                issue.setIssueStatus(IssueStatus.FAILED);
                issueRepository.save(issue);
                return ResponseEntity.ok().body(new ResponseWrapper("For this issue already extended time, you failed this task"));
            }
            ManagerStages managerStages = managerStagesRepository.getManagerStagesByName(name);
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
                    issueRepository.save(issue);
                    return ResponseEntity.ok().body(new ResponseWrapper("Error limit exceeded, you failed this task"));
                }
            }

            if (managerStages.getStage3().getDate().isAfter(issue.getProcurement().getStage3().getDate())) {
                if (count == 0) {
                    msg = "stage3";
                    issue.getProcurement().getStage3().setDate(issue.getProcurement().getStage3().getDate().plusDays(days));
                } else {
                    issue.setIssueStatus(IssueStatus.FAILED);
                    issueRepository.save(issue);
                    return ResponseEntity.ok().body(new ResponseWrapper("Error limit exceeded, you failed this task"));
                }
            }

            issue.setExtendedTime(ExtendedTime.YES);
            Issue newIssue = issueRepository.save(issue);
            return ResponseEntity.ok().body(new ResponseWrapper("For issue " + newIssue.getProcurement().getName() + " time is extended by " + days + " days for " + msg));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }

    public ResponseEntity<?> closeIssue(String name) {
        try {
            Issue issue = issueRepository.getIssueByProcurementName(name);
            if (issue == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("This issue does not exist"));
            }
            ManagerStages managerStages = managerStagesRepository.getManagerStagesByName(name);
            if (managerStages == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("ManagerStages does not exist"));
            } else if (managerStages.getStage1() == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("Stage1 does not exist"));
            } else if (managerStages.getStage2() == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("Stage2 does not exist"));
            } else if (managerStages.getStage3() == null) {
                return ResponseEntity.ok().body(new ResponseWrapper("Stage3 does not exist"));
            }
            if (managerStages.getStage1().getDate().isAfter(issue.getProcurement().getStage1().getDate()) ||
                    managerStages.getStage2().getDate().isAfter(issue.getProcurement().getStage2().getDate()) ||
                    managerStages.getStage3().getDate().isAfter(issue.getProcurement().getStage3().getDate())) {
                issue.setIssueStatus(IssueStatus.FAILED);
                issueRepository.save(issue);
                return ResponseEntity.ok().body(new ResponseWrapper("Task " + name + " failed"));
            }
            issue.setIssueStatus(IssueStatus.SUCCESSFULLY);
            issueRepository.save(issue);
            return ResponseEntity.ok().body(new ResponseWrapper("Task" + name + "successfully"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new ResponseWrapper("Something went wrong"));
        }
    }
}
