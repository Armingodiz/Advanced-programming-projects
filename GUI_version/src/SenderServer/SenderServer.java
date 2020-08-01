package SenderServer;


import javax.swing.*;
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
    private JTextPane statusPane;

    /**
     * constructor
     *
     * @param port        port
     * @param requestPack request pack to send
     */
    public SenderServer(int port, RequestPack requestPack, JTextPane statusPane) {
        this.port = port;
        this.requestPack = requestPack;
        this.statusPane = statusPane;
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
            statusPane.setText("Server started.\nWaiting for a client ... ");
            statusPane.setText(statusPane.getText() + "\n\n" + requestPack.packToString());
            Socket connectionSocket = welcomingSocket.accept();
            count++;
            System.out.println("client accepted!");
            pool.execute(new ClientHandler(connectionSocket, count, requestPack, statusPane));
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
    private JTextPane statusPane;

    /**
     * @param connectionSocket connection
     * @param clientNum        number of client
     * @param requestPack      request pack to send
     * @throws IOException for connection stuff
     */
    public ClientHandler(Socket connectionSocket, int clientNum, RequestPack requestPack, JTextPane statusPane) throws IOException {
        this.connectionSocket = connectionSocket;
        this.clientNum = clientNum;
        this.requestPack = requestPack;
        this.statusPane = statusPane;
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
            statusPane.setText(statusPane.getText() + "\n\n" + "REQUEST Pack SENT to CLIENT " + clientNum);
            statusPane.setText(statusPane.getText() + "\n" + "Request Response sent.\nClosing client ... ");
        } catch (IOException e) {
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
