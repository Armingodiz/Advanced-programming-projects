package com.SenderServer;


import com.App.Request;
import com.App.RequestResponse;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

/**
 *  this class create a client socket to
 *  receive a request pack from another application
 */
public class ClientGetter  {
    private String ip;
    private int port;
    private RequestPack requestPack;

    /**
     * constructor
     * @param ip ip
     * @param port port
     * @param requestPack request pack
     */
    public ClientGetter(String ip, int port,RequestPack requestPack) {
        this.ip = ip;
        this.port = port;
        this.requestPack=requestPack;
    }

    /**
     * this method receive a request pack from server and
     * return it.
     * @return received request pack
     */
    public RequestPack run() {
        try (Socket client = new Socket(ip, port)) {
            System.out.println("Connected to server.");
            InputStream in = client.getInputStream();
            ObjectInputStream reader = new ObjectInputStream(in);
            requestPack= (RequestPack) reader.readObject();
            System.out.println("REQUEST Pack  RECEIVED . ");
            // reader.close();
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("CLIENT " +  " has done its job .");
        for (Request temp:requestPack.getRequests()) {
            temp.printRequest();
        }
        return requestPack;
    }

}
