# Spring Boot Batch

## POC - EJEMPLO BASE PREVIO A MIGRAR A VERSION 3

Usa la memoria para manipular los metadatos del proceso spring batch.

### Caracteristicas

- writer escribe en base de datos
- csv to db  
- Java 8
- Spring 2
- Maven
- output logs en consola


### input
 
- resources/sample_1k.csv
- resources/sample_10k.csv

Ejemplo
```
Nombre 1;Apellido 1;Direccion 1;Telefono 1
Nombre 2;Apellido 2;Direccion 2;Telefono 2
Nombre 3;Apellido 3;Direccion 3;Telefono 3
Nombre 4;Apellido 4;Direccion 4;Telefono 4
Nombre 5;Apellido 5;Direccion 5;Telefono 5
Nombre 6;Apellido 6;Direccion 6;Telefono 6
Nombre 7;Apellido 7;Direccion 7;Telefono 7
Nombre 8;Apellido 8;Direccion 8;Telefono 8
Nombre 9;Apellido 9;Direccion 9;Telefono 9
Nombre 10;Apellido 10;Direccion 10;Telefono 10
Nombre 11;Apellido 11;Direccion 11;Telefono 11
```

### output

- Tabla PERSONA de base de datos.


### Model

```
CREATE TABLE persona(
id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
nombre VARCHAR(30),
apellido VARCHAR(30),
direccion VARCHAR(30),
telefono VARCHAR(30)
);
```

### Ejecutables

```
    mvn clean package
```

