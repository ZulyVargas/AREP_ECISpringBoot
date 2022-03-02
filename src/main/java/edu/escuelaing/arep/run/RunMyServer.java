package edu.escuelaing.arep.run;




import edu.escuelaing.arep.httpServer.ECISpringBoot;
import edu.escuelaing.arep.httpServer.HttpServer;

/**
 * Class to start the WEB SERVER.
 */

public class RunMyServer {
    public static void main(String[] args){
        String[] args1=new String[1];
        args1[0]="edu.escuelaing.arep.controlador.HelloController";
        try {
            ECISpringBoot eciSpringBoot = ECISpringBoot.getInstance();
            eciSpringBoot.start(args1);
            HttpServer server = new HttpServer(eciSpringBoot);
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
