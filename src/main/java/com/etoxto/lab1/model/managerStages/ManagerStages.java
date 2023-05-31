package com.etoxto.lab1.model.managerStages;

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
public class ManagerStages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    private Stage stage1;

    @OneToOne
    private Stage stage2;

    @OneToOne
    private Stage stage3;

    public ManagerStages(String name,
                         Stage stage1,
                         Stage stage2,
                         Stage stage3) {
        this.name = name;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.stage3 = stage3;
    }
}
