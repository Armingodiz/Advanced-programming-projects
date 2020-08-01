package com.App;
/**
 * this class is a request sender that
 * send a request and do actions to its response based on
 * request setting such as isShowHeaders and etc.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

public class RequestSender implements Runnable {
    private Request request;
    private HttpURLConnection connection;
    private int dataRec;
    private long time;
    private RequestResponse requestResponse;
    private boolean showResponse;

    /**
     * constructor
     */
    public RequestSender() {
        dataRec = 0;
        time = System.currentTimeMillis();
        requestResponse = new RequestResponse();
        showResponse = true;
    }

    public void setShowResponse(boolean showResponse) {
        this.showResponse = showResponse;
    }

    /**
     * @param request request to send
     */
    public void setRequest(Request request) {
        this.request = request;
        requestResponse.setRequest(request);
    }

    /**
     * this method create a httpURLConnection with request url
     * then call correct method to send request .
     */
    @Override
    public void run() {
        request.printRequest();
        String method = request.getMethod();
        String bodyType = request.getBodyType();
        try {
            connection = (HttpURLConnection) request.getUrl().openConnection();
            if (request.isFollowRedirect())
                HttpURLConnection.setFollowRedirects(true);
            putHeaders();
            if (method.equals("GET"))
                sendGet(method);
            else if (method.equals("DELETE"))
                sendGet(method);
            else {
                switch (bodyType) {
                    case "json":
                        json(method);
                        break;
                    case "formData":
                        formData(method);
                        break;
                    case "upload":
                        upload(method);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method put headers of a request in it
     */
    public void putHeaders() {
        for (String temp : request.getHeaders().keySet()) {
            connection.setRequestProperty(temp, request.getHeaders().get(temp));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this method sends a ger request or a delete request
     * and also check follow redirection and
     * save response boolean of request
     *
     * @param method method of request
     * @throws IOException for connection and file stuff
     */
    public void sendGet(String method) throws IOException {
        connection.setRequestMethod(method);
        //add request header
        connection.setRequestProperty(
                "Content-Type", "application/x-www-form-urlencoded");
        int responseCode = connection.getResponseCode();
        checkFollowRedirect(responseCode);
        System.out.println("\nSending " + method + " request to URL : " + connection.getURL().toString());
        System.out.println("Response Code : " + responseCode);
        StringBuilder responseText = new StringBuilder();
        InputStream output = null;
        if (request.getResponseOutput() != null) {
            byte[] responseBytes = connection.getInputStream().readAllBytes();
            OutputStream outputStream = new FileOutputStream(request.getResponseOutput());
            outputStream.write(responseBytes);
            outputStream.flush();
            outputStream.close();
            output = new FileInputStream(request.getResponseOutput());
            requestResponse.setResponseBytes(request.getResponseOutput());
        } else {
            try {
                output = connection.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(output));
            String line;
            while ((line = in.readLine()) != null) {
                if (showResponse)
                    System.out.println(line);
                dataRec += line.getBytes().length;
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        printHeaders();
        printMoreInformation();
    }
    

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this method sends a put request or a post request
     * and also check follow redirection and
     * save response boolean of request
     * it sends a binary uploaded file with request
     *
     * @param method method of request
     */
    public void upload(String method) {
        try {
            long time = System.currentTimeMillis();
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/octet-stream");
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(request.getUploadedFile()));
            bufferedOutputStream.write(fileInputStream.readAllBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            InputStream out = saveResponse();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(out);
            if (showResponse) {
                System.out.println(new String(bufferedInputStream.readAllBytes()));
                System.out.println(connection.getResponseCode());
                System.out.println(connection.getHeaderFields());
                checkFollowRedirect(connection.getResponseCode());
            }
            printHeaders();
            printMoreInformation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this method sends a post request or a put request
     * and also check follow redirection and
     * save response boolean of request
     * it also sends a json object with request
     *
     * @param method method of request
     * @throws JSONException for json
     * @throws IOException   for file and connection stuff
     */
    public void json(String method) throws JSONException, IOException {
        long time = System.currentTimeMillis();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod(method);
        JSONObject jsonObject = new JSONObject();
        for (String temp : request.getJsonData().keySet()) {
            jsonObject.put(temp, request.getJsonData().get(temp));
        }
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(jsonObject.toString());
        wr.flush();
//display what returns the POST request
        StringBuilder sb = new StringBuilder();
        int HttpResult = connection.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            InputStream out = saveResponse();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(out, "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            if (showResponse)
                System.out.println("" + sb.toString());
        } else
            checkFollowRedirect(HttpResult);
        printHeaders();
        printMoreInformation();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this method puts multi parts information in connection
     * output to send them with request
     *
     * @param formBody             data
     * @param boundary             boundary string
     * @param bufferedOutputStream output of connection
     * @throws IOException for connection stuff
     */
    public void sendFormDataMultiple(HashMap<String, String> formBody, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
        for (String key : formBody.keySet()) {
            bufferedOutputStream.write(("--" + boundary + "\r\n").getBytes());
            if (key.contains("file")) {
                bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(formBody.get(key))).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                try {
                    BufferedInputStream tempBufferedInputStream = new BufferedInputStream(new FileInputStream(new File(formBody.get(key))));
                    byte[] filesBytes = tempBufferedInputStream.readAllBytes();
                    bufferedOutputStream.write(filesBytes);
                    bufferedOutputStream.write("\r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bufferedOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
                bufferedOutputStream.write((formBody.get(key) + "\r\n").getBytes());
            }
        }
        bufferedOutputStream.write(("--" + boundary + "--\r\n").getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this method sends a post request or a put request
     * and also check follow redirection and
     * save response boolean of request
     * it also send a map of data with request
     *
     * @param method method of request
     */
    public void formData(String method) {
        long time = System.currentTimeMillis();
        HashMap<String, String> fooBody = request.getFormData();
        if (fooBody == null) {
            fooBody = new HashMap<>();
            fooBody.put("empty", "empty");
        }
        try {
            String boundary = System.currentTimeMillis() + "";
            connection.setRequestMethod(method);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            BufferedOutputStream request2 = new BufferedOutputStream(connection.getOutputStream());
            sendFormDataMultiple(fooBody, boundary, request2);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
            String response = "";
            if (request.getResponseOutput() != null) {
                byte[] responseBytes = connection.getInputStream().readAllBytes();
                OutputStream outputStream = new FileOutputStream(request.getResponseOutput());
                outputStream.write(responseBytes);
                outputStream.flush();
                outputStream.close();
                InputStream x = new FileInputStream(request.getResponseOutput());
                requestResponse.setResponseBytes(request.getResponseOutput());
                response = new String(x.readAllBytes());
                x.close();
            } else
                response = new String(bufferedInputStream.readAllBytes());
            checkFollowRedirect(connection.getResponseCode());
            if (showResponse)
                System.out.println(response);
            printHeaders();
            printMoreInformation();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this method save response of a request in request output file
     * and also create a input stream and return it
     *
     * @return a inputStream
     * @throws IOException file stuff
     */
    private InputStream saveResponse() throws IOException {
        InputStream out = null;
        if (request.getResponseOutput() != null) {
            byte[] responseBytes = connection.getInputStream().readAllBytes();
            OutputStream outputStream = new FileOutputStream(request.getResponseOutput());
            outputStream.write(responseBytes);
            outputStream.flush();
            outputStream.close();
            out = new FileInputStream(request.getResponseOutput());
            requestResponse.setResponseBytes(request.getResponseOutput());
        } else {
            out = connection.getInputStream();
        }
        return out;
    }

    /**
     * this method prints time , mount of received data and request status
     *
     * @throws IOException for connection stuff
     */
    public void printMoreInformation() throws IOException {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        time = System.currentTimeMillis() - time;
        if (showResponse) {
            System.out.println("TIME : " + time + " MILLI SECONDS");
            System.out.println("STATUS CODE : " + connection.getResponseCode() + " " + connection.getResponseMessage());
            System.out.println("Data RECEIVED : " + dataRec + " Bytes .");
        }
        requestResponse.setTime(time);
        requestResponse.setStatus(connection.getResponseCode() + " " + connection.getResponseMessage());
        requestResponse.setDataReceived(dataRec);
        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    }

    /**
     * this method print headers of a request if it was needed
     */
    public void printHeaders() {
        if (request.isShowHeaders()) {
            System.out.println(" HEADERS : ");
            for (int i = 0; i < 1000; i++) {
                if (connection.getHeaderField(i) == null)
                    break;
                if (showResponse)
                    System.out.println(connection.getHeaderFieldKey(i) + " = " + connection.getHeaderField(i));
                requestResponse.addHeader(connection.getHeaderFieldKey(i), connection.getHeaderField(i));
            }
        }
    }

    /**
     * this method is for following redirection by
     * creating new connection to correct address
     *
     * @param statusCode response code
     * @throws IOException for connection stuff
     */
    public void checkFollowRedirect(int statusCode) throws IOException {
        boolean redirect = false;
        if (request.isFollowRedirect()) {
            // normally, 3xx is redirect
            if (statusCode != HttpURLConnection.HTTP_OK) {
                if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                        || statusCode == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
        }
        if (redirect) {
            // get redirect url from "location" header field
            String newUrl = connection.getHeaderField("Location");
            // get the cookie if need, for login
            String cookies = connection.getHeaderField("Set-Cookie");
            // open the new connnection again
            System.out.println(newUrl);
            connection = (HttpURLConnection) new URL(newUrl).openConnection();
            connection.setRequestProperty("Cookie", cookies);
            connection.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            connection.addRequestProperty("User-Agent", "Mozilla");
            connection.addRequestProperty("Referer", "google.com");
            System.out.println("Redirect to URL : " + newUrl);
        }
    }

    public RequestResponse getRequestResponse() {
        return requestResponse;
    }

    public void setRequestResponse(RequestResponse requestResponse) {
        this.requestResponse = requestResponse;
    }
}

