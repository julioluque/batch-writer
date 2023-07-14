package com.jluque.spring;

import com.jluque.spring.model.Persona;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public JdbcCursorItemReader<Persona> executeReader() {
        log.info("executeReader");
        JdbcCursorItemReader<Persona> reader = new JdbcCursorItemReader<Persona>();

        reader.setDataSource(dataSource);
        reader.setSql("SELECT nombre, apellido, direccion, telefono FROM persona");
        reader.setRowMapper(new RowMapper<Persona>() {
            @Override
            public Persona mapRow(ResultSet rs, int rowNum) throws SQLException {
                Persona persona = new Persona();
                persona.setNombre(rs.getString("nombre"));
                persona.setApellido(rs.getString("apellido"));
                persona.setDireccion(rs.getString("direccion"));
                persona.setTelefono(rs.getString("telefono"));
                return persona;
            }
        });
        return reader;
    }

    @Bean
    public FlatFileItemWriter<Persona> executeWriter() {
        return new FlatFileItemWriterBuilder<Persona>()
                .name("executeWriter")
                .resource(new FileSystemResource("C:\\Users\\Julio\\w\\batch-reader\\src\\main\\resources\\csv_output.csv"))
                .delimited().delimiter(";")
                .names(new String[]{"nombre", "apellido", "direccion", "telefono"})
                .build();
    }

    @Bean
    public Step executeStep() {
        return stepBuilderFactory.get("executeStep")
                .<Persona, Persona>chunk(10)
                .reader(executeReader())
//                .processor(processor())
                .writer(executeWriter())
                .build();
    }

    @Bean
    public Job processJob() {
        return jobBuilderFactory
                .get("processJob")
                .incrementer(new RunIdIncrementer())
                .flow(executeStep())
                .end()
                .build();
    }
}
