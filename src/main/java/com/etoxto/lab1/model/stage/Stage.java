package com.etoxto.lab1.model.stage;

import com.etoxto.lab1.model.procurement.Procurement;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Stage {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private LocalDate date;
}
