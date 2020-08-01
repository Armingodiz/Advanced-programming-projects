package com.App;
/**
 * this class holds information for a list
 * such as its name and list of its requests
 */

import java.io.File;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class List implements Serializable {
   private String listName;
   private ArrayList<Request> requests;

    /**
     * constructor
     * @param listName name of list
     */
    List(String listName) {
        this.listName = listName;
        requests = new ArrayList<>();
    }

    /**
     *
     * @return name of list
     */
    public String getListName() {
        return listName;
    }

    /**
     *
     * @param listName name of list
     */
    public void setListName(String listName) {
        this.listName = listName;
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
     * @param requests list of requests
     */
    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    /**
     *
     * @param request request to add to requests list
     */
    public void addRequest(Request request) {
        requests.add(request);
    }
}

