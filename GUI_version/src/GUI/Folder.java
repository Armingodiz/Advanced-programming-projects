package GUI;
/**
 * this class is for holding information of a folder
 * such as list of requests .
 */

import ApplicationRunner.Request;

import javax.swing.*;
import java.util.ArrayList;

public class Folder extends JMenu {
    private ArrayList<Request> requests;
    private String name;

    /**
     *
     * @param text text of folder
     * @param name name of folder
     */
    Folder(String text,String name) {
        super(text);
        this.name=name;
        requests = new ArrayList<>();
    }

    /**
     *
     * @param request request to add
     */
    public void addRequest(Request request) {
        requests.add(request);
    }

    /**
     *
     * @return list of requests
     */
    public ArrayList<Request> getRequests() {
        return requests;
    }

    /**
     *
     * @param requests requests of folder
     */
    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    /**
     *
     * @return name of folder
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name of folder
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
}
