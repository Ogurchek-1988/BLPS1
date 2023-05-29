package com.etoxto.lab1.auth.model;

import com.etoxto.lab1.utils.XMLUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private final XMLUtils xmlUtil;
    private final String xmlPath = "./auth.xml";

    public UserRepositoryImpl(XMLUtils xmlUtil) {
        this.xmlUtil = xmlUtil;
    }

    @Override
    public User findByUsername(String username) {
        Users users = (Users)xmlUtil.getEntity(Users.class, "userList", xmlPath);
        if (users==null || users.getUser()==null) return null;
        List<User> userEntities = users.getUser();
        for (User cur: userEntities) {
            if (cur.getUsername().equals(username)) return cur;
        }
        return null;
    }

    @Override
    public void save(User user) {
        Users users = (Users)xmlUtil.getEntity(Users.class, "users", xmlPath);
        if (users==null) users = new Users();
        users.getUser().add(user);
        xmlUtil.saveEntity(users.getUser(), xmlPath);
    }
}
