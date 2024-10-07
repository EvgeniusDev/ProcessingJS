package com.litesoft.processingjs.utils.validator;

import java.util.List;

public class CompositeValidator {
    private List<Validator> validators;
    
    public CompositeValidator add(Validator validator) {
        validators.add(validator);
        return this;
    }
    
    public String validate(String input) {
        for (Validator validator : validators) {
            String error = validator.validate(input);
            if (!error.isEmpty()) {
                return error;
            }
        }
        
        return "";
    }
}