package com.uca.parcialfinalncapas.utils.enums;

import lombok.Getter;

@Getter
public enum Rol {
    USER("USER"),
    TECH("TECH");

    private final String value;

    Rol(String value) {
        this.value = value;
    }
}
