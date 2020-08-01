package ApplicationRunner;
/**
 * this class is a request sender that
 * send a request and do actions to its response based on
 * request setting such as isShowHeaders and etc.
 */

import GUI.Header;
import GUI.MainFrame;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class RequestSender implements Runnable {
    private Request request;
    private HttpURLConnection connection;
    private int dataRec;
    private long time;
    private MainFrame mainFrame;
    private String response;
    private RequestResponse requestResponse;
    private boolean changeResponsePart;

    /**
     * constructor
     */
    public RequestSender(MainFrame mainFrame) throws IOException {
        changeResponsePart = true;
        mainFrame.getPart3().getResponseInformation().getTime().setText("Time 0");
        mainFrame.getPart3().getResponseInformation().getData().setText("0 KB");
        this.mainFrame = mainFrame;
        dataRec = 0;
        time = System.currentTimeMillis();
        requestResponse = new RequestResponse();
    }

    /**
     *
     * @param changeResponsePart to show we must change response part or not
     */
    public void setChangeResponsePart(boolean changeResponsePart) {
        this.changeResponsePart = changeResponsePart;
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
        String method = request.getMethod();
        String bodyType = request.getBodyType();
        try {
            if (request.getQueryHeaders().size() != 0)
                request.setUrl(new URL(request.getUrl().toString() + addQueriesHeaders()));
            connection = (HttpURLConnection) request.getUrl().openConnection();
            if (mainFrame.isFollowRed()) {
                HttpURLConnection.setFollowRedirects(true);
                connection.setInstanceFollowRedirects(true);
            } else {
                HttpURLConnection.setFollowRedirects(false);
                connection.setInstanceFollowRedirects(false);
            }
            putHeaders();
            if (changeResponsePart)
                mainFrame.getPart3().getResponseBody().setText("SENDING " + method + " REQUEST TO " + request.getUrl().toString() + " FOLLOW REDIRECTION : " + mainFrame.isFollowRed() + "\n");
            System.out.print("HELPER----> @@@@@@@@@@@@@@@@@@@@@@@@ ");
            request.printRequest();
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
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
            requestResponse.printResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method put headers of a request in it
     */
    public void putHeaders() {
        for (Header temp : request.getHeaders()) {
            if (temp.isEnable()) {
                connection.setRequestProperty(temp.getName(), temp.getValue());
            }
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
        if (changeResponsePart)
            mainFrame.getPart3().getResponseInformation().getStatus_code().setText(responseCode + connection.getResponseMessage());
        checkFollowRedirect(responseCode);
        System.out.println("\nSending " + method + " request to URL : " + connection.getURL().toString());
        System.out.println("Response Code : " + responseCode);
        StringBuilder responseText = new StringBuilder();
        InputStream output = saveResponse();
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(output));
            String line;
            int x;
            response = "";
            while ((line = in.readLine()) != null) {
                response = response + line + "\n";
                //System.out.println(line);
               /* char[] res = line.toCharArray();
                for (int i = 0; i < line.length(); i++) {
                    response = response + String.valueOf(res[i]);
                    if (i % 80 == 0)
                        response = response + "\n";
                }
                response = response + "\n";*/
                dataRec += line.getBytes().length;
                // Thread.sleep(2000);
            }
            System.out.println(response);
            if (changeResponsePart)
                mainFrame.getPart3().getResponseBody().setText(response);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        printMoreInformation();
        printHeaders();
        showPreview();
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
            if (changeResponsePart)
                mainFrame.getPart3().getResponseInformation().getStatus_code().setText(connection.getResponseMessage() + connection.getResponseMessage());
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
            BufferedInputStream fileInputStream = new BufferedInputStream(new FileInputStream(request.getUploadedFile()));
            bufferedOutputStream.write(fileInputStream.readAllBytes());
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            fileInputStream.close();
            InputStream out = saveResponse();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(out);
            String response = new String(bufferedInputStream.readAllBytes());
            System.out.println(response);
            if (changeResponsePart)
                mainFrame.getPart3().getResponseBody().setText(response);
            System.out.println(connection.getResponseCode());
            System.out.println(connection.getHeaderFields());
            checkFollowRedirect(connection.getResponseCode());
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
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestMethod(method);
        /*JSONObject jsonObject = new JSONObject();
        for (String temp : request.getJsonData().keySet()) {
            jsonObject.put(temp, request.getJsonData().get(temp));
        }*/
        OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(request.getJsonData().toString());
        wr.flush();
//display what returns the POST request
        StringBuilder sb = new StringBuilder();
        int HttpResult = connection.getResponseCode();
        if (changeResponsePart)
            mainFrame.getPart3().getResponseInformation().getStatus_code().setText(HttpResult + connection.getResponseMessage());
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            InputStream out = saveResponse();
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(out, "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            System.out.println("" + sb.toString());
            if (changeResponsePart)
                mainFrame.getPart3().getResponseBody().setText(sb.toString());
        } else
            checkFollowRedirect(HttpResult);
        System.out.println(connection.getResponseMessage());
        printMoreInformation();
        printHeaders();
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
        HashMap<String, String> fooBody = new HashMap<>();
        for (Header temp : request.getFormData()) {
            fooBody.put(temp.getName(), temp.getValue());
        }
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
            if (changeResponsePart)
                mainFrame.getPart3().getResponseInformation().getStatus_code().setText(connection.getResponseCode() + connection.getResponseMessage());
            String response = "SENDING " + method + " REQUEST TO " + request.getUrl().toString() + " FOLLOW REDIRECTION : " + mainFrame.isFollowRed() + "\n";
            InputStream inputStream = saveResponse();
            response = response + new String(inputStream.readAllBytes());
            inputStream.close();
            System.out.println(response);
            if (changeResponsePart)
                mainFrame.getPart3().getResponseBody().setText(response);
            checkFollowRedirect(connection.getResponseCode());
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
        byte[] responseBytes = connection.getInputStream().readAllBytes();
        OutputStream outputStream = new FileOutputStream(request.getResponseOutput());
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();
        requestResponse.setResponseBytes(request.getResponseOutput());
        InputStream out = new FileInputStream(request.getResponseOutput());
        return out;
    }

    /**
     * this method prints time , mount of received data and request status
     *
     * @throws IOException for connection stuff
     */
    public void printMoreInformation() throws IOException {
        time = System.currentTimeMillis() - time;
        determineStatusColor();
        float dataGot = (float) dataRec / 1000;
        requestResponse.setTime(time);
        requestResponse.setStatus(connection.getResponseCode() + " " + connection.getResponseMessage());
        requestResponse.setDataReceived(dataRec);
        String moreInformation = "NAME OF REQUEST : " + request.getName() + "        METHOD : " + request.getMethod() + " \n" +
                "URL :        " + request.getUrl().toString() + "\n" + "Status :     " + connection.getResponseCode() + " " + connection.getResponseMessage() + "\n" +
                "TIME :       " + String.valueOf(time) + " ms" + "\n" + "DATA RECEIVED :            " + String.valueOf(dataGot) + " KB";
        if (changeResponsePart) {
            mainFrame.getPart3().getResponseInformation().getTime().setText("Time " + String.valueOf(time) + " ms");
            mainFrame.getPart3().getResponseInformation().getData().setText(String.valueOf(dataGot) + " KB");
            mainFrame.getPart3().getResponseInformation().getStatus_code().setText(connection.getResponseCode() + " " + connection.getResponseMessage());
            mainFrame.getPart3().getMorInfo().setText(moreInformation);
        }
    }

    /**
     * this method print headers of a request if it was needed
     */
    public void printHeaders() {
        System.out.println(" HEADERS : ");
        if (changeResponsePart) {
            mainFrame.getPart3().resetHeaderPanel();
            mainFrame.getPart3().addHeadersOfRequest("                                  NAME      ", "                                    VALUE      ");
        }
        for (int i = 0; i < 1000; i++) {
            if (connection.getHeaderField(i) == null)
                break;
            System.out.println(connection.getHeaderFieldKey(i) + " = " + connection.getHeaderField(i));
            if (changeResponsePart)
                mainFrame.getPart3().addHeadersOfRequest(connection.getHeaderFieldKey(i), connection.getHeaderField(i));
            requestResponse.addHeader(connection.getHeaderFieldKey(i), connection.getHeaderField(i));
        }
        if (changeResponsePart)
            mainFrame.getPart3().getHeaderPanel().add(mainFrame.getPart3().getCopy());
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
        if (mainFrame.isFollowRed()) {
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

    /**
     * this class show preview if it was a pick or html page
     *
     * @throws IOException for connection stuff
     */

    public void showPreview() throws IOException {
        if (changeResponsePart) {
            if (connection.getHeaderField("Content-Type").contains("text/html")) {
                mainFrame.getPart3().getPreview().removeAll();
                JTextPane editorPane = new JTextPane();
                editorPane.setContentType("text/html");
                editorPane.setPage(request.getUrl());
                mainFrame.getPart3().getPreview().add(editorPane);
            } else if (connection.getHeaderField("Content-Type").contains("image/png")) {
                JLabel imgLabel = new JLabel(new ImageIcon(request.getResponseOutput().getPath()));
                mainFrame.getPart3().getPreview().removeAll();
                mainFrame.getPart3().getPreview().add(imgLabel);
            }
        }
    }

    /**
     * this method determine what must be add to url to case query
     * effects we want .
     *
     * @return a string ass query string
     */
    public String addQueriesHeaders() {
        String queryEffect = "?";
        int i = 0;
        for (Header temp : request.getQueryHeaders()) {
            queryEffect = queryEffect + temp.getName() + "=" + temp.getValue();
            i++;
            if (i < request.getQueryHeaders().size())
                queryEffect = queryEffect + "&";
        }
        request.getQueryHeaders().clear();
        return queryEffect;
    }

    /**
     * @return response of a request
     */
    public RequestResponse getRequestResponse() {
        return requestResponse;
    }

    /**
     * @param requestResponse response of request
     */
    public void setRequestResponse(RequestResponse requestResponse) {
        this.requestResponse = requestResponse;
    }

    /**
     * this method choose a color for status part color
     * based on response code.
     */
    public void determineStatusColor() throws IOException {
        if (changeResponsePart) {
            Color color1 = new Color(224, 110, 32);
            Color color2 = new Color(10, 224, 36);
            Color color3 = new Color(224, 10, 25);
            if (connection.getResponseCode() / 100 == 2)
                mainFrame.getPart3().getResponseInformation().getStatus_code().setBackground(color2);
            else if (connection.getResponseCode() / 100 == 3)
                mainFrame.getPart3().getResponseInformation().getStatus_code().setBackground(color1);
            else
                mainFrame.getPart3().getResponseInformation().getStatus_code().setBackground(color3);
        }
    }

}

