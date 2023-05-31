package com.etoxto.lab1.service;

import com.etoxto.lab1.model.procurement.Procurement;
import com.etoxto.lab1.model.procurement.ProcurementRepository;
import com.etoxto.lab1.model.procurement.AssignedType;
import com.etoxto.lab1.model.procurement.ProcurementStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcurementServiceImpl implements ProcurementService{

    private final ProcurementRepository procurementRepository;

    @Autowired
    private ProcurementServiceImpl(ProcurementRepository procurementRepository) {
        this.procurementRepository = procurementRepository;
    }

    @Override
    public Procurement save(Procurement procurement) {
        return procurementRepository.save(procurement);
    }

    @Override
    public Procurement getProcurementByNameAndAssignedType(String name, AssignedType type) {
        return procurementRepository.getProcurementByNameAndAssignedType(name, type);
    }

    @Override
    public void delete(Long id) {
        procurementRepository.deleteById(id);
    }

    @Override
    public Procurement setStatus(Long id, ProcurementStatus status) {
        return procurementRepository.setStatusById(id, status);
    }
}
