package com.etoxto.lab1.model.procurement;

import com.etoxto.lab1.model.stage.Stage;
import jakarta.persistence.*;

@Entity
public class Procurement {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Stage stage1;

    @OneToOne
    private Stage stage2;

    @OneToOne
    private Stage stage3;

    @Enumerated(EnumType.STRING)
    private ProcurementStatus status;
}
