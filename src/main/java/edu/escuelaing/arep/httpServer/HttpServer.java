package edu.escuelaing.arep.httpServer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a HttpServer.
 * @author Zuly Vargas.
 */
public class HttpServer {

    private ECISpringBoot eciSpringBoot;
    boolean running = true;
    private static final String headerOk =  "HTTP/1.1 200 OK\r\n"
            + "Content-Type: text/html\r\n"
            + "\r\n";

    /**
     * Create a new instance with a new EciSptingBoot.
     * @param eciSpringBoot EciSpringBoot a actualizar.
     */
    public HttpServer(ECISpringBoot eciSpringBoot){
        this.eciSpringBoot = eciSpringBoot;

    }

    /**
     * Return the port where the server is listening.
     * @return Port - port number.
     */
    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set
    }

    /**
     * Start httpServer.
     * @throws IOException -IOException.
     * @throws InvocationTargetException - InvocationTargetException
     * @throws IllegalAccessException - IllegalAccessException.
     */
    public void start() throws IOException, InvocationTargetException, IllegalAccessException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(getPort()); //SOCKET DE SERVIDOR, PUERTO 35000
        } catch (IOException e) {
            System.err.println("Could not listen on port:." + getPort());
            System.exit(1);
        }

        Socket clientSocket = null; //Cliente socket
        running = true;
        while (running) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept(); //Se pone a aceptar conexiones
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            //PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); //Flujo de salida
            makeRequest(clientSocket);
            clientSocket.close();

            }
        serverSocket.close();
        }


    /**
     * Performs the process of reading the input request.
     * @param clientSocket - socket del cliente.
     */
    private void makeRequest(Socket clientSocket) throws IOException, InvocationTargetException, IllegalAccessException {
        BufferedReader in = new BufferedReader( //Flujo de entrada
                new InputStreamReader(
                        clientSocket.getInputStream()));
        String inputLine, outputLine;
        //Map<String, String> solicitudes = new HashMap<>();

        //String file ="";
        boolean requestReady = false;
        ArrayList<String> request = new ArrayList<>();
        while ((inputLine = in.readLine()) != null) {
            System.out.println("Received: " + inputLine); //Mensaje de lo que recibió
            request.add(inputLine);
            if (!in.ready()) {
                break;
            }
        }
        makeResponse(request, new PrintWriter(clientSocket.getOutputStream(), true), clientSocket.getOutputStream());
        in.close();
    }

    //App servicio

    /**
     * Returns what was found by the Ecispringboot.
     * @param uriApp -uri of the method.
     * @param out - out of te method.
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
   private static void invokeService(String uriApp, PrintWriter out) throws IllegalAccessException,InvocationTargetException {
        String response = ECISpringBoot.getInstance().invokeService(uriApp);
        out.println(headerOk + response);
    }

    /**
     * Creates the response to display and calls the method that checks if it is image or page.
     * @param req - Request.
     * @param out - Out text.
     * @param outputStream - Out bytes.
     */
    private void makeResponse(ArrayList<String> req, PrintWriter out, OutputStream outputStream) throws InvocationTargetException, IllegalAccessException {
        String file = req.get(0).split(" ")[1];
        //URI uri = file.getTheuri();
        String appUri= file.replace("/opciones/", "");
        if (file.startsWith("/opciones")) {
            invokeService(appUri,out);
        } else {
            getFile(appUri, out, outputStream);
        }
        out.close();

    }

    /**
     * Output according to file type.
     * @param path -  Address of the requested resource.
     * @param out  - Out text.
     * @param outputStream - Out bytes.
     */
    private void getFile(String path, PrintWriter out, OutputStream outputStream) {
        if (path.contains("html")) {
            page(path, out);
        } else if (path.contains("png")) {
            image(path, outputStream); //Necesita convertir a bytes para poder mostrar
        }
    }

    /**
     * Converts the image to bytes.
     * @param path - Path of the img with .png.
     * @param outputStream - To convert to Bytes.
     */
    private void image(String path, OutputStream outputStream) {
        //Path pathImage = Paths.get("target/classes/view" + path);
        try{
            //toma la imagen de la carpeta
            BufferedImage image = ImageIO.read(new File(System.getProperty("user.dir")+"/src/main/resources/view/hello.png"));
            ByteArrayOutputStream ArrBytes = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(outputStream);
            ImageIO.write(image, "PNG", ArrBytes);
            out.writeBytes("HTTP/1.1 200 OK \r\n"
                    + "Content-Type: image/png \r\n"
                    + "\r\n");
            out.write(ArrBytes.toByteArray());
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the contents of the file to display it.
     * @param path - Path of the file with .html .
     * @param out - Text.
     */
    private void page(String path, PrintWriter out) {
        Path file = Paths.get("target/classes/view" + path);
        System.out.println(file);
        try (InputStream in = Files.newInputStream(file);
             BufferedReader reader
                     = new BufferedReader(new InputStreamReader(in))) {

            String header = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "\r\n";
            out.println(header);
            String line = null;
            while ((line = reader.readLine()) != null) {
                out.println(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Generates an entry splitting the received request.
     * @param inputLine - Request to be generated.
     */
    private String[] generarEntrada(String inputLine) {
        System.out.println("ENTRY SPLIT : " + inputLine);
        return inputLine.split(":");
    }
}



