package com.etoxto.lab1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/admin")
public class AdminManagerController {

    @GetMapping("/jopa")
    ResponseEntity<String> getJopa() {
        log.info("JOPA");
        return new ResponseEntity<>("jopa", HttpStatus.OK);
    }


}
