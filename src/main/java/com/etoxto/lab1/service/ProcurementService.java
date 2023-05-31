package com.etoxto.lab1.service;

import com.etoxto.lab1.model.procurement.Procurement;
import com.etoxto.lab1.model.procurement.AssignedType;
import com.etoxto.lab1.model.procurement.ProcurementStatus;

public interface ProcurementService {
    Procurement save(Procurement procurement);
    Procurement getProcurementByNameAndAssignedType(String name, AssignedType status);
    void delete(Long id);
    Procurement setStatus(Long id, ProcurementStatus status);
}
