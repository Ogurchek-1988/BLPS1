package com.etoxto.lab1.model.procurement;

import com.etoxto.lab1.model.stage.Stage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Procurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Stage stage1;

    @OneToOne(cascade = CascadeType.ALL)
    private Stage stage2;

    @OneToOne(cascade = CascadeType.ALL)
    private Stage stage3;

    @Enumerated(EnumType.STRING)
    private AssignedType assignedType;

    @Enumerated(EnumType.STRING)
    private ProcurementStatus status;

    public Procurement(String name,
                       Stage stage1,
                       Stage stage2,
                       Stage stage3,
                       AssignedType assignedType,
                       ProcurementStatus status) {
        this.name = name;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.stage3 = stage3;
        this.assignedType = assignedType;
        this.status = status;
    }

    public Procurement(String name,
                       Stage stage1,
                       Stage stage2,
                       Stage stage3,
                       AssignedType assignedType) {
        this.name = name;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.stage3 = stage3;
        this.assignedType = assignedType;
    }
}
