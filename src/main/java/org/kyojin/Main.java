package org.kyojin;

import org.kyojin.validation.ValidationResult;
import org.kyojin.validation.Validator;

public class Main {
    public static void main(String[] args) {

        Validator validator = new Validator();

        ValidationResult result = validator.check("123568777777", "test").rules("required|string|min:5|max:10");

        System.out.println(result.isPassed() + result.message());
    }
}