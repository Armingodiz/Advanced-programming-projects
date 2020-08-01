package ApplicationRunner;
/**
 * this class holds needed information for a request
 */

import GUI.Header;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Request extends JMenu {
    private URL url;
    private String name;
    private String method;
    private String bodyType;//json , formData , upload
    private String[] requestDetails;
    private File responseOutput;
    private File uploadedFile;
    ArrayList<Header> headers;
    ArrayList<Header> queryHeaders;
    private ArrayList<Header> formData;
    private JSONObject jsonObject;
    private String bodyText;

    /**
     * constructor for default request
     */
    public Request() throws IOException {
        name = "default";
        method = "GET";
        bodyType = "formData";
        url=new URL("http://apapi.haditabatabaei.ir/docs");
        headers = new ArrayList<Header>();
        queryHeaders = new ArrayList<Header>();
        formData = new ArrayList<>();
        Random random = new Random();
        responseOutput = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\trash\\Request" + random.nextInt(100));
        responseOutput.createNewFile();
    }

    /**
     * @param bodyText text in body of a request
     */
    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    /**
     * @return text of request in body
     */
    public String getBodyText() {
        return bodyText;
    }

    /**
     *
     * @param header header to add
     */
    public void addFormData(Header header) {
        formData.add(header);
    }

    /**
     * @param header header we want to add to this request
     */
    public void addHeader(Header header) {
        headers.add(header);
    }

    /**
     * @param header query header we want to add to this request
     */
    public void addQueryHeader(Header header) {
        queryHeaders.add(header);
    }

    /**
     * @return list of header
     */
    public ArrayList<Header> getHeaders() {
        return headers;
    }

    /**
     * @return list of query headers
     */
    public ArrayList<Header> getQueryHeaders() {
        return queryHeaders;
    }

    /**
     * @return command line of request
     */
    public String[] getRequestDetails() {
        return requestDetails;
    }

    /**
     * @param requestDetails command line of request
     */
    public void setRequestDetails(String[] requestDetails) {
        this.requestDetails = requestDetails;
    }

    /**
     * print this request
     */
    public void printRequest() {
        System.out.println("NAME OF REQUEST : " + name +"  URL : " + url.toString() + "  METHOD : " + method + " BODY TYPE : " + bodyType);
        System.out.println("HEADERS : ");
        for (Header temp : headers) {
            System.out.println("KEY :" + temp.getName() + "  Value : " + temp.getValue() + " ENABLITY : " + temp.isEnable());
        }
        System.out.println("QUERY HEADERS : ");
        for (Header temp : queryHeaders) {
            System.out.println("KEY :" + temp.getName() + "  Value : " + temp.getValue() + " ENABLITY : " + temp.isEnable());
        }
        System.out.println("FORM DATA : ");
        for (Header temp : formData) {
            System.out.println("KEY :" + temp.getName() + "  Value : " + temp.getValue() + " ENABLITY : " + temp.isEnable());
        }
        System.out.println("JSON DATA : ");
        if (jsonObject != null)
            System.out.println(jsonObject.toString());
        System.out.println("Uploaded File :");
        if (uploadedFile != null)
            System.out.println(uploadedFile.getName());

    }

    /**
     * @param map given hash map
     */
    public void printMap(HashMap<String, String> map) {
        for (String temp : map.keySet()) {
            System.out.println("KEY : " + temp + " VALUE : " + map.get(temp));
        }
    }

    /**
     * @return body message of a request
     */
    public String getBodyType() {
        return bodyType;
    }

    /**
     * @param bodyType body type message of a request
     */
    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }


    /**
     * @return json data of a request
     */
    public JSONObject getJsonData() {
        return jsonObject;
    }

    /**
     * @param jsonData of a request
     */
    public void setJsonData(JSONObject jsonData) {
        jsonObject = jsonData;
    }

    /**
     * @return data of a request
     */
    public ArrayList<Header> getFormData() {
        return formData;
    }

    /**
     * @param formData data of a request
     */
    public void setFormData(ArrayList<Header> formData) {
        this.formData = formData;
    }

    /**
     * @return binary file of request
     */
    public File getUploadedFile() {
        return uploadedFile;
    }

    /**
     * @param uploadedFile binary file of request
     */
    public void setUploadedFile(File uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * @return file to save response in it
     */
    public File getResponseOutput() {
        return responseOutput;
    }

    /**
     * @param responseOutput file to save response in it
     */
    public void setResponseOutput(File responseOutput) {
        this.responseOutput = responseOutput;
    }

    /**
     * @return method of request
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method method of request
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return url of request
     */
    public URL getUrl() {
        return url;
    }

    /**
     * @param url of request
     */
    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * this method clear all array list of request
     */
    public void clearRequestOldData() {
        headers.clear();
        queryHeaders.clear();
        formData.clear();
    }

    /**
     *
     * @return name of request
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     *
     * @param name name of request
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }
}
