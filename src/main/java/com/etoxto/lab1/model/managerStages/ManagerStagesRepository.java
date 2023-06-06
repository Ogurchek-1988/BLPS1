package com.etoxto.lab1.model.managerStages;

import com.etoxto.lab1.model.stage.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ManagerStagesRepository extends JpaRepository<ManagerStages, Long> {
    ManagerStages getManagerStagesByName(String name);
    boolean existsManagerStagesByName(String name);
    @Modifying
    @Query("update ManagerStages ms set ms.stage1 = :stage where ms.id = :id")
    ManagerStages setStage1ById(@Param("id") Long id, @Param("stage") Stage stage);
    @Modifying
    @Query("update ManagerStages ms set ms.stage2 = :stage where ms.id = :id")
    ManagerStages setStage2ById(@Param("id") Long id, @Param("stage") Stage stage);
    @Modifying
    @Query("update ManagerStages ms set ms.stage3 = :stage where ms.id = :id")
    ManagerStages setStage3ById(@Param("id") Long id, @Param("stage") Stage stage);
}
