package com.uca.parcialfinalncapas.utils.enums;

import lombok.Getter;

@Getter
public enum State {
    OPEN("Abierto"),
    IN_PROGRESS("En Proceso"),
    CLOSED("Cerrado");

    private final String description;

    State(String description) {
        this.description = description;
    }
}
