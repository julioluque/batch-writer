package com.jluque.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BatchWriterApplication {


    private static final Logger log = LoggerFactory.getLogger(BatchWriterApplication.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public static void main(String[] args) {
        SpringApplication.run(BatchWriterApplication.class, args);
    }

    public void run(String... args) throws Exception {
        log.info("INICIANDO EJECUCION...");
        long start = System.currentTimeMillis();


        // Creates a FileReader
        FileReader file = new FileReader("C:\\Users\\Julio\\w\\batch-writer\\src\\main\\resources\\sample_10k.csv");

        // Creates a BufferedReader
        BufferedReader reader = new BufferedReader(file);

//        BufferedReader reader = new BufferedReader(new FileReader("sample_10k.csv"));
        String row = null;

        while ((row = reader.readLine()) != null) {
            Object[] data = row.split(",");
            jdbcTemplate.update("INSERT INTO persona (nombre, apellido, direccion, telefono) VALUES (?, ?, ?, ?)", data);
        }

        long total = jdbcTemplate.queryForObject("SELECT count(1) FROM persona", Long.class);
        long end = System.currentTimeMillis();
        long diff = end - start;
        reader.close();

        log.info("Tiempo de ejecucion: {}", TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
        log.info("Registros : {}", total);

    }
}
