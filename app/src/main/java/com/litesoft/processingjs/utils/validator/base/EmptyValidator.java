package com.litesoft.processingjs.utils.validator.base;

import com.litesoft.processingjs.utils.validator.Validator;

public class EmptyValidator implements Validator {
    
    @Override
    public String validate(String input) {
        return input.isEmpty() ? "Заполните поле" : "";
    }
}