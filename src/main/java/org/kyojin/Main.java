package org.kyojin;

import jakarta.persistence.Persistence;
import org.kyojin.util.EnvLoader;
import org.kyojin.validation.ValidationResult;
import org.kyojin.validation.Validator;

public class Main {
    public static void main(String[] args) {

//        Validator validator = new Validator();
//
//        ValidationResult result = validator.check("shfishdif@gm.com", "email").rules("required|email|min:5|max:20");
//
//        System.out.println(result.isPassed() + result.message());

        EnvLoader.load();
        Persistence.createEntityManagerFactory("tal3demPU");
        System.out.println("EntityManagerFactory created successfully!");
    }
}