package com.App;
/**
 * this class holds information of a request
 * response such as count of data received and etc.
 */

import java.io.*;
import java.util.HashMap;

public class RequestResponse implements Serializable {
    private Request request;
    private HashMap<String, String> headers;
    private String status;
    private long time;
    private int dataReceived;
    private File responseBytes;

    /**
     * constructor
     */
    public RequestResponse() {
        headers = new HashMap<>();
    }

    /**
     * @param key   key of header
     * @param value value of header
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * @return request
     */
    public Request getRequest() {
        return request;
    }

    /**
     * @param request request that response is for that
     */
    public void setRequest(Request request) {
        this.request = request;
    }

    /**
     * @return headers hash map
     */
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers set headers of response
     */
    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return response message
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status response message
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return time toked for request
     */
    public long getTime() {

        return time;
    }

    /**
     * @param time time toked
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return count of data
     */
    public int getDataReceived() {
        return dataReceived;
    }

    /**
     * @param dataReceived count of data received
     */
    public void setDataReceived(int dataReceived) {
        this.dataReceived = dataReceived;
    }

    /**
     * @return response bytes file
     */
    public File getResponseBytes() {
        return responseBytes;
    }

    /**
     * @param responseBytes file for response bytes
     */
    public void setResponseBytes(File responseBytes) {
        this.responseBytes = responseBytes;
    }

    /**
     * this class print information of response
     *
     * @throws IOException for file stuff
     */
    public void printResponse() throws IOException {
        System.out.println(time);
        System.out.println(status);
        System.out.println(dataReceived + " bytes");
        for (String temp : headers.keySet()) {
            System.out.println("key :" + temp + " value : " + headers.get(temp));
        }
        if (responseBytes != null) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(new FileInputStream(responseBytes)));
            String line;
            int x;
            String response = "";
            while ((line = in.readLine()) != null) {
                response = response + line + "\n";
            }
            System.out.println(" TEXT OF RESPONSE : ##@!@#$#@");
            System.out.println(response);
        }
    }
}
