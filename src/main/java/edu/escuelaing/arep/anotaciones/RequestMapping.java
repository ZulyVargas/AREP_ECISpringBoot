package edu.escuelaing.arep.anotaciones;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    String value();
}

//Crear una instancia EciSpringBoot , arrancar servidor web para recibir mensajes
//Metodos invoke y