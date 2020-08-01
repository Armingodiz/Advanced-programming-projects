package com.SenderServer;




import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * this class create a server to send a request pack through it
 */
public class SenderServer extends Thread {
    private int port;
    private RequestPack requestPack;

    /**
     * constructor
     * @param port port
     * @param requestPack request pack to send
     */
    public SenderServer(int port,RequestPack requestPack) {
        this.port = port;
        this.requestPack=requestPack;
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
                pool.execute(new ClientHandler(connectionSocket, count,requestPack));
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

    /**
     *
     * @param connectionSocket connection
     * @param clientNum number of client
     * @param requestPack request pack to send
     * @throws IOException for connection stuff
     */
    public ClientHandler(Socket connectionSocket, int clientNum,RequestPack requestPack) throws IOException {
        this.connectionSocket = connectionSocket;
        this.clientNum = clientNum;
        this.requestPack=requestPack;

    }

    /**
     * this method send request pack by writing
     * request pack object to client output stream
     */
    @Override
    public void run() {
        try {
            OutputStream out = connectionSocket.getOutputStream();
            ObjectOutputStream writer = new ObjectOutputStream(out);
            writer.writeObject(requestPack);
            System.out.println("REQUEST Pack SENT to CLIENT " + clientNum);
            Thread.sleep(2000);
            System.out.print("Request Response sent.\nClosing client ... ");
        } catch (IOException | InterruptedException e) {
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
