package com.jluque.spring.processor;

import com.jluque.spring.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonaItemProcessor implements ItemProcessor<Persona, Persona> {
    public static final Logger log = LoggerFactory.getLogger(PersonaItemProcessor.class);

    @Override
    public Persona process(Persona item) throws Exception {
        return Persona.builder()
                .nombre(item.getNombre())
                .apellido(item.getApellido())
                .direccion(item.getDireccion())
                .telefono("+54 9 115" + String.format("%7s", item.getTelefono()).replace(' ', '0'))
                .build();
    }
}
