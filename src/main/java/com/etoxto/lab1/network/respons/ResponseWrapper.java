package com.etoxto.lab1.network.respons;

import lombok.Data;

@Data
public class ResponseWrapper {
    String message;

    public ResponseWrapper(String message) {
        this.message = message;
    }
}
