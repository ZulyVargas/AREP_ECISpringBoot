package edu.escuelaing.arep.controlador;
import edu.escuelaing.arep.anotaciones.Component;
import edu.escuelaing.arep.anotaciones.RequestMapping;

@Component
public class HelloController {

    @RequestMapping(value = "/index")
    static public String index() {
        return "Greetings from Spring Boot! (This means that for now it works!!! )";
    }

    @RequestMapping(value = "/lottery")
    static public String lottery() {
        return "Number to play the lottery: 816";
    }

    @RequestMapping(value = "/song")
    static public String song() {
        return "Song of the day: Something Comforting - Porter Robinson";
    }


}