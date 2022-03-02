package edu.escuelaing.arep.httpServer;

import edu.escuelaing.arep.anotaciones.Service;
import edu.escuelaing.arep.anotaciones.RequestMapping;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *  @author Zuly Vargas.
 *
 */
public class ECISpringBoot {
    // Una URL y un método
    private Map<String, Method> services = new HashMap();
    private static ECISpringBoot _instance = new ECISpringBoot();


    private ECISpringBoot() {
    }

    public static ECISpringBoot getInstance() {
        return _instance;
    }

    public void start(String[] args) throws Exception {
        int passed = 0, failed=0;
        for (Method m : Class.forName(args[0]).getMethods()) {
            if (m.isAnnotationPresent(RequestMapping.class)) {
                System.out.println("Encontró RequestMapping");
                try {
                    //m.invoke(null);
                    //Valor , método
                    services.put(m.getAnnotation(RequestMapping.class).value(),m);
                    passed++;
                } catch (Throwable ex) {
                    ex.printStackTrace();
                    System.out.printf("Something  failed: ", m, ex.getCause());
                    failed++;
                }}

        }
    }


    /*
    //Buscar la clase, cargarla y meterla en la estructura de datos
    //Para buscar decirle la posición, que busque recursivamente los .class
    private void loadComponents() {

        //Cargas uno a uno los servicios definidos
        String[] componentList = searchComponentList();
        for (String componentname : componentList) {
            try {
                loadServices(componentname);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String loadServices(String componentname) throws InvocationTargetException, IllegalAccessException {
        try {
            Class c = Class.forName(componentname);
            Method[] declaredMethods = c.getDeclaredMethods();
            for (Method m : declaredMethods) {
                if (m.isAnnotationPresent(Service.class)) {
                    // Si la anotacion esta presente saque el valor, lo mete a los servicios
                    Service a = m.getAnnotation(Service.class);
                    services.put("", m);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return componentname;
    }
    */

        //Busque que el servicio exista
        //
        public String invokeService(String serviceName) throws InvocationTargetException, IllegalAccessException {
            System.out.println("Tratando de invocar : " + serviceName);
            return  services.get(serviceName).invoke(null).toString();
        }



    private String[] searchComponentList() {
        return new String[]{"examples"};

    }
}
