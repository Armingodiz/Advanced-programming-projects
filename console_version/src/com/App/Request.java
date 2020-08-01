package com.App;
/**
 * this class holds needed information for a request
 */

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Request implements Serializable {
    private URL url;
    private String name;
    private String method;
    private String bodyType;//json , formData , upload
    private String[] requestDetails;
    private boolean showHeaders;
    private boolean followRedirect;
    private File responseOutput;
    private File uploadedFile;
    private HashMap<String, String> headers;
    private HashMap<String, String> formData;
    private HashMap<String, String> jsonData;
    private HashMap<String, String> queryHeaders;
    private boolean usingProxy;
    private String ip;
    private int port;

    /**
     * constructor for default request
     */
    public Request() {
        method = "GET";
        bodyType = "formData";
        showHeaders = false;
        followRedirect = false;
        headers = new HashMap<>();
        formData = new HashMap<>();
        jsonData = new HashMap<>();
        queryHeaders=new HashMap<>();
        usingProxy=false;
    }

    /**
     *
     * @return query headers
     */
    public HashMap<String, String> getQueryHeaders() {
        return queryHeaders;
    }

    /**
     *
     * @param queryHeaders query headers
     */
    public void setQueryHeaders(HashMap<String, String> queryHeaders) {
        this.queryHeaders = queryHeaders;
    }

    /**
     *
     * @return command line of request
     */
    public String[] getRequestDetails() {
        return requestDetails;
    }

    /**
     *
     * @param requestDetails command line of request
     */
    public void setRequestDetails(String[] requestDetails) {
        this.requestDetails = requestDetails;
    }

    /**
     * print this request
     */
    public void printRequest() {
        System.out.println("URL : " + url.toString() + " METHOD : " + method + "  BODY TYPE : " + bodyType + "  SHOWING HEADERS : " + showHeaders + "  FOLLOW REDIRECT : " + followRedirect);
        System.out.println("HEADERS : ");
        printMap(headers);
        System.out.println("FORM DATA : ");
        printMap(formData);
        System.out.println("JSON DATA : ");
        printMap(jsonData);

    }

    /**
     *
     * @param map  given hash map
     */
    public void printMap(HashMap<String, String> map) {
        for (String temp : map.keySet()) {
            System.out.println("KEY : " + temp + " VALUE : " + map.get(temp));
        }
    }

    /**
     *
     * @return body message of a request
     */
    public String getBodyType() {
        return bodyType;
    }

    /**
     *
     * @param bodyType body type message of a request
     */
    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    /**
     *
     * @return headers of request
     */
    public HashMap<String, String> getHeaders() {
        return headers;
    }

    /**
     *
     * @param headers headers of request
     */
    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    /**
     *
     * @return json data of a request
     */
    public HashMap<String, String> getJsonData() {
        return jsonData;
    }

    /**
     *
     * @param jsonData of a request
     */
    public void setJsonData(HashMap<String, String> jsonData) {
        this.jsonData = jsonData;
    }

    /**
     *
     * @return data of a request
     */
    public HashMap<String, String> getFormData() {
        return formData;
    }

    /**
     *
     * @param formData data of a request
     */
    public void setFormData(HashMap<String, String> formData) {
        this.formData = formData;
    }

    /**
     *
     * @return binary file of request
     */
    public File getUploadedFile() {
        return uploadedFile;
    }

    /**
     *
     * @param uploadedFile binary file of request
     */
    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     *
     * @return file to save response in it
     */
    public File getResponseOutput() {
        return responseOutput;
    }

    /**
     *
     * @param responseOutput file to save response in it
     */
    public void setResponseOutput(File responseOutput) {
        this.responseOutput = responseOutput;
    }

    /**
     *
     * @return a boolean to show if request is following redirect or not
     */
    public boolean isFollowRedirect() {
        return followRedirect;
    }

    /**
     *
     * @param followRedirect a boolean to show if request is following redirect or not
     */
    public void setFollowRedirect(boolean followRedirect) {
        this.followRedirect = followRedirect;
    }

    /**
     *
     * @return a boolean to show if headers must be shown or not
     */
    public boolean isShowHeaders() {
        return showHeaders;
    }

    /**
     *
     * @param showHeaders a boolean to show if headers must be shown or not
     */
    public void setShowHeaders(boolean showHeaders) {
        this.showHeaders = showHeaders;
    }

    /**
     *
     * @return method of request
     */
    public String getMethod() {
        return method;
    }

    /**
     *
     * @param method method of request
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     *
     * @return url of request
     */
    public URL getUrl() {
        return url;
    }

    /**
     *
     * @param url of request
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isUsingProxy() {
        return usingProxy;
    }

    public void setUsingProxy(boolean usingProxy) {
        this.usingProxy = usingProxy;
    }
}
