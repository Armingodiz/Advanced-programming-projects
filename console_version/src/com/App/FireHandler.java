package com.App;
/**
 * this class handle fire command and send chosen request
 */

import java.io.*;
import java.net.HttpURLConnection;
import java.net.http.HttpRequest;
import java.util.ArrayList;

public class FireHandler implements Runnable {
    private Request currentRequest;
    private Application application;
    private String[] input;

    /**
     * constructor
     * @param application application fire handler is connected to
     * @param input command line
     */
    FireHandler(Application application, String[] input) {
        this.application = application;
        this.input = input;
    }

    /**
     * method run to send requests
     */
    @Override
    public void run() {
        fireCommand(input);
    }

    /**
     * this method recognize which requests from which lists
     * must be sent and the call the fire method and give it needed informations
     * @param inputCommand command line
     */
    public void fireCommand(String[] inputCommand) {
        String listName = "default";
        int startingPoint = 1;
        ArrayList<Integer> numberOfRequests = new ArrayList<>();
        if (inputCommand[1] == null)
            System.out.println("NO CHOSEN REQUEST .");
        if (!isNumeric(inputCommand[1])) {
            listName = inputCommand[1];
            startingPoint = 2;
        }
        for (int i = startingPoint; i < inputCommand.length; i++) {
            int reqNum = Integer.parseInt(inputCommand[i]);
            numberOfRequests.add(reqNum);
        }
        fire(listName, numberOfRequests);
    }

    /**
     * determine if inputted string is a number
     * @param str inputted string
     * @return boolean
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * this method send chosen requests
     * @param listName name of list
     * @param numberOfRequest number of requests
     */
    public void fire(String listName, ArrayList<Integer> numberOfRequest) {
        RequestSender requestSender = new RequestSender();
        if (listName.equals("default")) {
            for (Integer temp : numberOfRequest) {
                Request req = application.getRequests().get(temp - 1);
                requestSender.setRequest(req);
                requestSender.run();
            }
        } else {
            for (List temp : application.getLists()) {
                if (temp.getListName().equals(listName)) {
                    ArrayList<Request> requestsList = temp.getRequests();
                    for (Integer temp2 : numberOfRequest) {
                        Request req = requestsList.get(temp2 - 1);
                        requestSender.setRequest(req);
                        requestSender.run();
                    }
                }
            }
        }
    }

}

