package ServerProxy;


import ApplicationRunner.Request;
import ApplicationRunner.RequestResponse;
import ApplicationRunner.RequestSender;
import GUI.MainFrame;
import SenderServer.RequestPack;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this class create a server to receive request and send back the response
 */
public class ProxyServer extends Thread {
    private int port;
private MainFrame app;

    /**
     * constructor
     *
     * @param port port
     */
    public ProxyServer(int port,MainFrame app) {
        this.port = port;
        this.app=app;
    }


    /**
     * creating a thread pool to
     * handle connecting multi clients to it
     */
    @Override
    public void run() {
        ExecutorService pool = Executors.newCachedThreadPool();
        int count = 0;
        try (ServerSocket welcomingSocket = new ServerSocket(port)) {
            System.out.print("Server started.\nWaiting for a client ... ");
            while (count < 3) {
                Socket connectionSocket = welcomingSocket.accept();
                count++;
                System.out.println("client accepted!");
                pool.execute(new ClientHandler(connectionSocket, count,app));

            }
            pool.shutdown();
            System.out.print("done.\nClosing server ... ");
        } catch (IOException ex) {
            System.err.println(ex);
        }
        System.out.println("done.");
    }
}

/**
 * this class creates a server to send request
 */
class ClientHandler implements Runnable {

    private Socket connectionSocket;
    private int clientNum;
    private RequestPack requestPack;
    private MainFrame app;

    /**
     * @param connectionSocket connection
     * @param clientNum        number of client
     * @throws IOException for connection stuff
     */
    public ClientHandler(Socket connectionSocket, int clientNum,MainFrame app) throws IOException {
        this.connectionSocket = connectionSocket;
        this.clientNum = clientNum;
        this.app=app;
    }

    /**
     * this method receive a request and
     * send back the response to connected client
     */
    @Override
    public void run() {
        try {
            InputStream in = connectionSocket.getInputStream();
            ObjectInputStream reader = new ObjectInputStream(in);
            Request request = (Request) reader.readObject();
            System.out.println("REQUEST RECEIVED FROM CLIENT");
            RequestSender sender = new RequestSender(app);
            sender.setRequest(request);
            sender.setChangeResponsePart(false);
            sender.run();
            RequestResponse response = sender.getRequestResponse();
            System.out.println("REQUEST IS DONE .");
            OutputStream out = connectionSocket.getOutputStream();
            ObjectOutputStream writer = new ObjectOutputStream(out);
            writer.writeObject(response);
            System.out.println("RESPONSE SENT to CLIENT " + clientNum);
            System.out.print("Request Response sent.\nClosing client ... ");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                connectionSocket.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }
}
