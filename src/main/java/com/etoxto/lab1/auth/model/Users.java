package com.etoxto.lab1.auth.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement
public class Users {
    private List<User> user;

    public Users(){
        user = new ArrayList<>();
    }
}
