package com.etoxto.lab1.utils;

import com.etoxto.lab1.auth.model.User;
import com.thoughtworks.xstream.XStream;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class XMLUtils {
    private final XStream xstream;
    public XMLUtils(){
        this.xstream = new XStream();
    }
    public <T> Object getEntity(Class<T> convertClass, String aliasName, String xmlPath) {
        xstream.alias(aliasName, convertClass);
        try {
            File file = new File(xmlPath);
            file.createNewFile();
            FileReader reader = new FileReader(file);
            if (reader.read() > 0) {
                JAXBContext jaxbContext = JAXBContext.newInstance(convertClass);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                return jaxbUnmarshaller.unmarshal(file);
            }
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveEntity(List<?> saveEntity, String xmlPath) {
        xstream.alias("user", User.class);
        xstream.alias("users", List.class);
        try {
            xstream.toXML(saveEntity, new FileWriter(xmlPath, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
