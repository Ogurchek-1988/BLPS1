package com.etoxto.lab1.network.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TaskRequest {
    StageRequest stageRequest1;
    StageRequest stageRequest2;
    StageRequest stageRequest3;
}
