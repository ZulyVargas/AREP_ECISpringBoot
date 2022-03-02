package edu.escuelaing.arep.httpServer;

import edu.escuelaing.arep.anotaciones.Component;
import edu.escuelaing.arep.anotaciones.Service;
import edu.escuelaing.arep.anotaciones.RequestMapping;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private File path;

    /**
     * Inicialize the server and searchs the path.
     */
    private ECISpringBoot() {
        String packageName;
        //Dejarlo como ruta de carpertas
        packageName = ECISpringBoot.class.getPackage().getName().replace(".","/");
        System.out.println("package encontrado " +  packageName );
        this.path = new File("./src/main/java/"+packageName);

    }

    /**
     * Gives a ECISpringBootInstance.
     * @return _instance - ECISpringBootInstance
     */
    public static ECISpringBoot getInstance() {
        return _instance;
    }


    public void start(){
        loadComponents();
    }


    //Método usado en la primera versión del proyecto solo RequestMapping
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


    /**
     * Shearchs the component in the classes with @Component.
     */
    //Buscar la clase, cargarla y meterla en la estructura de datos
    //Para buscar decirle la posición, que busque recursivamente los .class
    private void loadComponents() {
        List<String> componentList = getComponentList(path);
        //Cargas uno a uno los servicios definidos
        //String[] componentList = searchComponentList(path);
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

    private List<String> getComponentList(File file) {
        List<String> components = new ArrayList<>();
        if (file.isDirectory()){
            for (File root : file.listFiles()){
                components.addAll(getComponentList(root));
            }
        }
        else{
            if (file.getName().endsWith("java")){
                String path = generatePath(file.getPath());
                Class varClass = null;
                try{
                    varClass = Class.forName(path);
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (varClass.isAnnotationPresent(Component.class)){
                    components.add(path);
                }
            }
        }
        return components;
    }

    /**
     * Creates a classpath.
     * @param path - new path to convert.
     * @return path - new path convert to classpath.
     */
    private String generatePath(String path){
        return path.replace(".java","").replace("\\",".").replace("..src.main.java.","");
    }

    private void loadServices(String componentname) throws InvocationTargetException, IllegalAccessException {
        try {
            Class c = Class.forName(componentname);
            Method[] declaredMethods = c.getDeclaredMethods();
            for (Method m : declaredMethods) {
                if (m.isAnnotationPresent(Service.class)) {
                    // Si la anotacion esta presente saque el valor, lo mete a los servicios
                    Service a = m.getAnnotation(Service.class);
                    services.put( a.value() , m);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Invokes the server with the gives name.
     * @param serviceName
     * @return service - Services.
     * @throws InvocationTargetException -  InvocationTargetException
     * @throws IllegalAccessException - IllegalAccessException
     */
        public String invokeService(String serviceName) throws InvocationTargetException, IllegalAccessException {
            System.out.println("Tratando de invocar : " + serviceName);
            return  services.get(serviceName).invoke(null).toString();
        }



    private String[] searchComponentList() {
        return new String[]{"examples"};

    }
}
