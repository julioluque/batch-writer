package com.jluque.spring.processor;

import com.jluque.spring.model.Persona;
import org.springframework.batch.item.ItemProcessor;

public class PersonaItemProcessor implements ItemProcessor<Persona, Persona> {

    @Override
    public Persona process(Persona item) throws Exception {
        return Persona.builder()
                .nombre(item.getNombre().toUpperCase().split(" ")[0])
                .apellido(item.getApellido().toLowerCase().split(" ")[0])
                .direccion(item.getDireccion())
                .telefono(item.getTelefono().replace("Telefono ", ""))
                .build();
    }
}
