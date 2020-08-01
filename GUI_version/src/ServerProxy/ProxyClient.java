package ServerProxy;



import ApplicationRunner.Request;
import ApplicationRunner.RequestResponse;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * this class create a client socket to
 * receive a request pack from another application
 */
public class ProxyClient {
    private String ip;
    private int port;
    private RequestResponse requestResponse;
    private Request request;

    /**
     * constructor
     *
     * @param ip   ip
     * @param port port
     */
    public ProxyClient(String ip, int port,Request request) {
        this.ip = ip;
        this.port = port;
        requestResponse = new RequestResponse();
        this.request=request;
    }

    /**
     * this method send a request to server , receive the response
     * and
     * return it.
     *
     * @return received request pack
     */
    public RequestResponse run() {
        try (Socket client = new Socket(ip, port)) {
            System.out.println("Connected to server.");
            OutputStream out = client.getOutputStream();
            ObjectOutputStream writer = new ObjectOutputStream(out);
            writer.writeObject(request);
            System.out.println("REQUEST  Sent . ");
            InputStream in = client.getInputStream();
            ObjectInputStream reader = new ObjectInputStream(in);
            requestResponse=(RequestResponse) reader.readObject();
            requestResponse.printResponse();
            System.out.println("RESPONSE RECEIVED .");
            client.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        System.out.println("CLIENT " + " has done its job .");
        return requestResponse;
    }

}
