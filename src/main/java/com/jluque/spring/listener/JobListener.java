package com.jluque.spring.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Nos indica el estado del job, cuando arranca, cuando para
 */
@Component
public class JobListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobListener.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("------> beforeJob");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("------> afterJob");

        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("Finalizo el JOb. Verificar");
            Date start = jobExecution.getStartTime();
            Date end = jobExecution.getEndTime();

            long diff = end.getTime() - start.getTime();
            long total = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM persona", Long.class);

            log.info("Tiempo de ejecucion: {}", TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
            log.info("Registros: {}", total);
        }

    }
}
