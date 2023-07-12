package com.jluque.spring.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Persona {

    private String nombre;
    private String apellido;
    private String direccion;
    private String telefono;

}
