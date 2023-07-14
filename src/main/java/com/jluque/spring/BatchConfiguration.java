package com.jluque.spring;

import com.jluque.spring.listener.JobListener;
import com.jluque.spring.model.Persona;
import com.jluque.spring.processor.PersonaItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    public static final Logger log = LoggerFactory.getLogger(BatchConfiguration.class);

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;

    @Bean
    public Job processJob(JobListener listener) {
        log.info("> processJob...");
        return jobBuilderFactory
                .get("processJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(executeStep())
                .end()
                .build();
    }

    @Bean
    public Step executeStep() {
        log.info("> > executeStep...");
        return stepBuilderFactory.get("executeStep")
                .<Persona, Persona>chunk(10)
                .reader(executeReader())
                .processor(executeProcessor())
                .writer(executeWriter())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Persona> executeReader() {
        log.info("> > > executeReader...");
        return new JdbcCursorItemReaderBuilder<Persona>()
                .dataSource(this.dataSource)
                .name("fooReader")
                .sql("SELECT nombre, apellido, direccion, telefono FROM persona")
                .rowMapper((rs, rowNum) -> Persona.builder()
                        .nombre(rs.getString("nombre"))
                        .apellido(rs.getString("apellido"))
                        .direccion(rs.getString("direccion"))
                        .telefono(rs.getString("telefono"))
                        .build())
                .build();
    }

    @Bean
    public PersonaItemProcessor executeProcessor(){
        log.info("> > > executeProccesor...");
        return new PersonaItemProcessor();
    }

    @Bean
    public FlatFileItemWriter<Persona> executeWriter() {
        log.info("> > > executeWriter...");
        return new FlatFileItemWriterBuilder<Persona>()
                .name("executeWriter")
                .resource(new FileSystemResource("C:\\Users\\Julio\\w\\batch-reader\\src\\main\\resources\\csv_output.csv"))
                .delimited().delimiter(";")
                .names(new String[]{"nombre", "apellido", "direccion", "telefono"})
                .build();
    }

}
