package com.etoxto.lab1.network.request;

import com.etoxto.lab1.model.procurement.AssignedType;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProcurementRequest {
    String name;
    AssignedType assignedType;
}
