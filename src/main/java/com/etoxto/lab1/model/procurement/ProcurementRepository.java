package com.etoxto.lab1.model.procurement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcurementRepository extends JpaRepository<Procurement, Long> {
    Procurement getProcurementByNameAndAssignedType(String name, AssignedType type);
    @Modifying
    @Query("update Procurement proc set proc.status = :status where proc.id = :id")
    void setStatusById(@Param("id") Long id, @Param("status") ProcurementStatus status);
}
