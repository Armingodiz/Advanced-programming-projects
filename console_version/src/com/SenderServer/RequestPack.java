package com.SenderServer;

import com.App.List;
import com.App.Request;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class create a pack which contains
 * a list of requests and a list of lists
 */
public class RequestPack implements Serializable {
    ArrayList<Request> requests;
    ArrayList<List> lists;

    /**
     * constructor
     */
    public RequestPack() {
        requests = new ArrayList<>();
        lists = new ArrayList<>();
    }

    /**
     * adding request to pack
     * @param request request to add
     */
    public void addRequest(Request request) {
        requests.add(request);
    }

    /**
     * adding list to pack
     * @param list list to add
     */
    public void addList(List list) {
        lists.add(list);
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
     * @return list of lists
     */
    public ArrayList<List> getLists() {
        return lists;
    }

    /**
     *
     * @param lists list of lists
     */
    public void setLists(ArrayList<List> lists) {
        this.lists = lists;
    }
}
