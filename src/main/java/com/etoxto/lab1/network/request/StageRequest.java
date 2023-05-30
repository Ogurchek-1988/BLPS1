package com.etoxto.lab1.network.request;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class StageRequest {
    String name;
    LocalDate date;
}
