package com.App;
/**
 * this class scan a commandLine the base on given command line does
 * the needed actions.
 */

import com.SenderServer.ClientGetter;


import com.SenderServer.RequestPack;
import com.SenderServer.SenderServer;
import com.ServerProxy.ProxyClient;
import com.ServerProxy.ProxyServer;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Application {
    private ArrayList<Request> requests;
    private ArrayList<List> lists;
    private HashMap<String, String> queries;
    private RequestPack requestPack;
    private RequestResponse response;

    /**
     * constructor
     *
     * @throws IOException for file
     */
    public Application() throws IOException {
        requests = new ArrayList<>();
        lists = new ArrayList<>();
        queries = new HashMap<>();
        loadLists();
        loadInformation();
    }

    /**
     * this method scan a line of command then turn it to
     * array of string then check the first string and call
     * the methods base on that .
     *
     * @throws IOException for stream
     */
    public void run() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        String[] inputCommand = input.split(" ");
        findQueryHeaders(inputCommand);
        switch (inputCommand[0]) {
            case "list":
                listCommand(inputCommand);
                break;
            case "fire":
                FireHandler fireHandler = new FireHandler(this, inputCommand);
                Thread thread1 = new Thread(fireHandler);
                thread1.start();
                break;
            case "create":
                createCommand(inputCommand);
                break;
            case "exit":
                saveLists();
                System.exit(0);
                break;
            case "--help":
                help();
                break;
            case "--send":
                sendAll(inputCommand);
                break;
            case "--get":
                get(inputCommand);
                break;
            case "--Presult":
                System.out.println("RESULT OF USING PROXY :");
                response.printResponse();
                break;
            default:
                Request request = createRequest(inputCommand, 1);
                if (request.isUsingProxy()) {
                    response = sendToProxy(request);
                } else {
                    RequestSender requestSender = new RequestSender();
                    requestSender.setRequest(request);
                    Thread thread2 = new Thread(requestSender);
                    thread2.start();
                }
                break;
        }
    }

    /**
     * this method make query string
     *
     * @return query string
     */
    public String addQueriesToURL() {
        String queryText = "";
        int end = queries.size();
        int counter = 0;
        for (String temp : queries.keySet()) {
            if (counter == 0)
                queryText = temp + "=" + queries.get(temp);
            else
                queryText = queryText + temp + "=" + queries.get(temp);
            if (counter < end - 1)
                queryText = queryText + "&";
            counter++;
        }
        return queryText;
    }

    /**
     * this method check to be sure given url is http or https and nothing else .
     *
     * @param link link for url
     * @return created url
     * @throws MalformedURLException for urls that have no protocol
     */
    public URL createValidUrl(String link) throws MalformedURLException {
        boolean flag = true;
        URL url = null;
        url = new URL(link);
        if ("http".equals(url.getProtocol()) || "https".equals(url.getProtocol())) {
            return url;
        } else {
            System.out.println("UNSUPPORTED PROTOCOL!");
            return null;
        }
    }

    /**
     * this method set query headers
     *
     * @param commands command line
     */
    public void findQueryHeaders(String[] commands) {
        String headers = null;
        for (String temp : commands) {
            if (temp.equals("--Qheaders") || temp.equals("-QH"))
                headers = commands[temp.indexOf(temp) + 2];
        }
        if (headers != null) {
            String x = String.valueOf(headers.charAt(0));
            headers = headers.replaceAll(x, "");
            String[] headersPart = headers.split(";");
            for (String temp : headersPart) {
                System.out.println(temp);
                String[] query = temp.split(":");
                queries.put(query[0], query[1]);
            }
        }
    }

    /**
     * this method check the next command after list command
     * and call show list method with proper input
     * a default value string to show list of saved requests
     * and a name of list string to show requests of that list
     *
     * @param inputCommand array of string for commands
     */
    public void listCommand(String[] inputCommand) {
        if (inputCommand.length == 1)
            showList("default");
        else if (inputCommand[1] == null)
            showList("default");
        else
            showList(inputCommand[1]);
    }

    /**
     * @return list of requests
     */
    public ArrayList<Request> getRequests() {
        return requests;
    }

    /**
     * @return list of lists
     */
    public ArrayList<List> getLists() {
        return lists;
    }

    /**
     * this method create a txt file for a list
     * and also check the name for list to wont be null
     *
     * @param inputCommand list of commands
     * @throws IOException for opening and creating file
     */
    public void createCommand(String[] inputCommand) throws IOException {
        if (inputCommand[1] == null)
            System.out.println("NO INPUT NAME FOR LIST !!");
        else {
            createList(inputCommand[1]);
            File file = new File("LIST_" + inputCommand[1] + ".txt");
            file.createNewFile();
        }
    }

    /**
     * this method crate a request with url and default fields
     * then change it fields based on further commands given
     * in command line .
     *
     * @param inputCommand list of commands
     * @param saveOrNO     a boolean value . this variable is to determine if this request is crating by command line and
     *                     must be saved if needed or no .
     * @return created request
     * @throws IOException for files
     */
    public Request createRequest(String[] inputCommand, int saveOrNO) throws IOException {
        Request request = new Request();
        request.setRequestDetails(inputCommand);
        String link = inputCommand[0];
        if (queries.size() != 0)
            link = link + addQueriesToURL();
        URL url = createValidUrl(link);
        if (url == null) {
            System.out.println("This URL is not supported !");
            System.exit(0);
        } else
            request.setUrl(url);
        if (queries.size() != 0)
            request.setQueryHeaders(queries);
        String savePAth = "default";
        boolean mustSave = false;
        for (int i = 1; i < inputCommand.length; i++) {
            switch (inputCommand[i]) {
                case "-M":
                case "--method":
                    if (inputCommand.length > i + 1)
                        request.setMethod(getValidMethod(inputCommand[i + 1]));
                    break;
                case "-H":
                case "--headers":
                    if (inputCommand.length == i + 1) {
                        System.out.println("no inputted header !!");
                    } else {
                        if (inputCommand[i + 1].contains("-"))
                            System.out.println("no inputted header !!");
                        else
                            request.setHeaders(makingHeadersHashMap(inputCommand[i + 1]));
                    }
                    break;
                case "-i":
                    request.setShowHeaders(true);
                    break;
                case "-h":
                case "--help":
                    help();
                    break;
                case "-f":
                    request.setFollowRedirect(true);
                    break;
                case "-O":
                case "--output":
                    if (inputCommand.length == i + 1)
                        request.setResponseOutput(fileForSavingResponse("empty"));
                    else if (inputCommand[i + 1].contains("-"))
                        request.setResponseOutput(fileForSavingResponse("empty"));
                    else
                        request.setResponseOutput(fileForSavingResponse(inputCommand[i + 1]));
                    break;
                case "-S":
                case "--save":
                    mustSave = true;
                    if (inputCommand.length > i + 1) {
                        if (inputCommand[i + 1].contains("-") || inputCommand[i + 1] == null)
                            savePAth = "default";
                        else
                            savePAth = inputCommand[i + 1];
                    }
                    break;
                case "--upload":
                    if (inputCommand.length == i + 1) {
                        System.out.println("no entry for upload command !!");
                    } else if (inputCommand[i + 1].contains("-"))
                        System.out.println("no entry for upload command !!");
                    else {
                        request.setUploadedFile(new File(inputCommand[i + 1]));
                        request.setBodyType("upload");
                    }
                    break;
                case "-d":
                case "--data":
                    if (inputCommand.length == i + 1)
                        System.out.println("no inputted data !!");
                    else if (inputCommand[i + 1].contains("-")) {
                        System.out.println("no inputted data !!");
                    } else
                        request.setFormData(makeFormData(inputCommand[i + 1]));
                    break;
                case "-j":
                case "-json":
                    if (inputCommand.length == i + 1)
                        System.out.println("no inputted data for json !!");
                    else if (inputCommand[i + 1].contains("-")) {
                        System.out.println("no inputted data for json !!");
                    } else {
                        request.setJsonData(makeJsonData(inputCommand[i + 1]));
                        request.setBodyType("json");
                    }
                    break;
                case "--proxy":
                    request.setUsingProxy(true);
                    request.setIp(inputCommand[i + 2]);
                    request.setPort(Integer.parseInt(inputCommand[i + 4]));
                    break;
                default:
                    break;
            }
        }
        if (mustSave && saveOrNO == 1) {
            save(savePAth, inputCommand);
        }
        return request;
    }

    /**
     * this method check the given string and return it
     * if it was valid as a method for connection and return
     * null if it was not.
     *
     * @param method given string in command line as a method
     * @return a valid method or null
     */
    public String getValidMethod(String method) {
        if (method.equals("GET") || method.equals("POST") || method.equals("DELETE") || method.equals("PUT"))
            return method;
        System.out.println("inputted method is not supported !!");
        System.exit(0);
        return null;
    }

    /**
     * this method create a file with given name or
     * a random name to save response of request in it
     *
     * @param input name for file
     * @return a file to save response in it
     * @throws IOException for file stuff
     */
    public File fileForSavingResponse(String input) throws IOException {
        Random random = new Random();
        if (input.contains("-") || input.equals("empty")) {
            Date date = new Date();
            File file = new File("output_[" + date.getDate() + random.nextInt(1000) + "]");
            file.createNewFile();
            return file;
        } else {
            File file = null;
            if (input.contains(".")) {
                file = new File(input);
                file.createNewFile();
            } else {
                file = new File(input);
                file.createNewFile();
            }
            return file;
        }
    }

    /**
     * this method creates a hash map for data
     * and return it
     *
     * @param input command line
     * @return a hash map as data
     */
    public HashMap<String, String> makeFormData(String input) {
        HashMap<String, String> formData = new HashMap<>();
        input = input.replaceAll(String.valueOf(input.charAt(0)), "");
        String[] keyAndValues = input.split("&");
        for (String temp : keyAndValues) {
            String[] data = temp.split("=");
            formData.put(data[0], data[1]);
        }
        return formData;
    }

    /**
     * this method creates a hash map for json data
     * and return it
     *
     * @param input command line
     * @return a hash map as json data
     */
    public HashMap<String, String> makeJsonData(String input) {
        HashMap<String, String> jsonData = new HashMap<>();
        char[] res = input.toCharArray();
        String result = "";
        for (int i = 2; i < input.length() - 2; i++) {
            result = result + res[i];
        }
        String[] keyAndValues = result.split(",");
        for (String temp : keyAndValues) {
            String[] data = temp.split(":");
            jsonData.put(data[0], data[1]);
        }
        return jsonData;
    }

    /**
     * this method creates map of headers
     * based on command line and return it
     *
     * @param input command line
     * @return a hash map as headers
     */
    public HashMap<String, String> makingHeadersHashMap(String input) {
        HashMap<String, String> headers = new HashMap<>();
        input = input.replaceAll(String.valueOf(input.charAt(0)), "");
        String[] keyAndValues = input.split(";");
        for (String temp : keyAndValues) {
            String[] data = temp.split(":");
            headers.put(data[0], data[1]);
        }
        return headers;
    }

    /**
     * this method print all commands for user
     */
    public void help() {
        System.out.println("--method or -M is for setting method for a request and the default method is GET . (other methods are {POST,DELETE,PUT} ). ");
        System.out.println("--headers or -H is for adding headers to request . ");
        System.out.println("--Qheaders or -QH is for adding query headers to request . ");
        System.out.println("-i determines if responses headers must be shown or not and it is false as default .");
        System.out.println("--help or -H is for showing you this list to understand how to use this application .");
        System.out.println("-f is for setting follow redirect true for request to send another request to correct address .");
        System.out.println("-O or --output is for saving response body of request in a file with given name ,if you dont enter name for it default name is current date . ");
        System.out.println("-S or --save is for saving current request with its settings in a file and you can use it again in future .");
        System.out.println("-list shows list of saved requests in system .");
        System.out.println("-S or --save listName : save current request in given list .");
        System.out.println("fire : after using -list you can send each request by writing fire and request number in list in front of fire word . ");
        System.out.println("fire listName : after using -list listName you can send each request by writing  name of the list and request number in that list in front of fire word . ");
        System.out.println("create list : it makes a list to save requests in it with given name .");
        System.out.println("### message body types : ");
        System.out.println("-d or --data is for message body and its form is form_data .");
        System.out.println("-json is for creating a json object and put it in request.");
        System.out.println("--upload is for getting system a absolute address and add request the file which is in given address . ");
    }

    /**
     * this method load lists and add them to
     * lists array list
     *
     * @throws IOException for file stuff
     */
    public void loadLists() throws IOException {
        FileInputStream fileIn = new FileInputStream("nameOfLists.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileIn));
        String name = "";
        while ((name = reader.readLine()) != null) {
            FileInputStream fileIn2 = new FileInputStream("LIST_" + name + ".txt");
            BufferedReader reader2 = new BufferedReader(new InputStreamReader(fileIn2));
            List readen = new List(name);
            String requestDetails = "";
            while ((requestDetails = reader2.readLine()) != null) {
                readen.addRequest(createRequest(makeArgsForm(requestDetails), 0));
            }
            lists.add(readen);
            reader2.close();
        }
        reader.close();
    }

    /**
     * this method save all lists and their names
     *
     * @throws IOException for file stuff
     */
    public void saveLists() throws IOException {
        BufferedWriter writerName = new BufferedWriter(new FileWriter("nameOfLists.txt"));
        for (List temp : lists) {
            writerName.write(temp.getListName());
            writerName.newLine();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("LIST_" + temp.getListName() + ".txt"));
                for (Request request : temp.getRequests()) {
                    writer.write(makeCommandForm(request.getRequestDetails()));
                    writer.newLine();
                }
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writerName.close();
    }

    /**
     * this method read requests file and load the requests
     */
    public void loadInformation() {
        try {
            FileInputStream fileIn = new FileInputStream("request.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileIn));
            requests.clear();
            String req = "";
            while ((req = reader.readLine()) != null) {
                requests.add(createRequest(makeArgsForm(req), 0));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            System.out.println("READING IS DONE .");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param nameOfList name of list to save request in it
     * @param request    the request we want to save it
     * @throws IOException for file stuff
     */
    public void save(String nameOfList, String[] request) throws IOException {
        if (nameOfList.equals("default")) {
            FileOutputStream fileOutputStream = new FileOutputStream("request.txt", true);
            try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream))) {
                bufferedWriter.write(makeCommandForm(request));
                bufferedWriter.newLine();
            }
        } else {
            boolean flag = false;
            List chosenList = null;
            for (List temp : lists) {
                if (temp.getListName().equals(nameOfList)) {
                    chosenList = temp;
                    chosenList.addRequest(createRequest(request, 0));
                    flag = true;
                }
            }
            if (!flag)
                System.out.println("WE COULD NOT FOUND THE LIST !!");
        }
    }

    /**
     * this method turn array of strings to a command line
     *
     * @param string the array of string we want to turn it to a command line
     * @return a command line
     */
    public String makeCommandForm(String[] string) {
        String commandForm = string[0];
        for (int i = 1; i < string.length; i++)
            commandForm = commandForm + " " + string[i];
        return commandForm;
    }

    /**
     * this method turn a command line to array of string
     *
     * @param string a array of string created from command line
     * @return array of string
     */
    public String[] makeArgsForm(String string) {
        return string.split(" ");
    }

    /**
     * this method print list of all requests or if it had
     * a list name as input it prints list of that list requests
     *
     * @param listName name of list
     */
    public void showList(String listName) {
        if (listName.equals("default")) {
            int i = 1;
            for (Request temp : requests) {
                System.out.print(i + " :");
                temp.printRequest();
                i++;
            }
        } else {
            List chosen = null;
            for (List temp : lists) {
                if (temp.getListName().equals(listName))
                    chosen = temp;
            }
            if (chosen != null) {
                int i = 1;
                for (Request temp : chosen.getRequests()) {
                    System.out.print(i + " :");
                    temp.printRequest();
                }
            }
        }
    }

    /**
     * this method create a list
     *
     * @param listName name of the list
     */
    public void createList(String listName) {
        lists.add(new List(listName));
    }

    /**
     * this method sends all request and lists through server
     * by creating a server and waiting for clients to connect to it and snd send
     * request pack to them
     *
     * @param commands command line
     */
    public void sendAll(String[] commands) {
        RequestPack requestPack = new RequestPack();
        if (requests.size() != 0)
            requestPack.getRequests().addAll(requests);
        if (lists.size() != 0)
            requestPack.getLists().addAll(lists);
        SenderServer senderServer = new SenderServer(Integer.parseInt(commands[4]), requestPack);
        senderServer.start();
    }

    /**
     * this method creates a client to connect to server with inputted
     * port and receive a request pack and save the requests and list
     * of that .
     *
     * @param commands command line
     */
    public void get(String[] commands) {
        String ip = commands[2];
        int port = Integer.parseInt(commands[4]);
        RequestPack receivedPack = new RequestPack();
        ClientGetter client = new ClientGetter(ip, port, receivedPack);
        receivedPack = client.run();
        if (receivedPack.getLists().size() != 0)
            lists.addAll(receivedPack.getLists());
        if (receivedPack.getRequests().size() != 0) {
            requests.addAll(receivedPack.getRequests());
        }
        for (Request temp : receivedPack.getRequests()) {
            try {
                save("default", temp.getRequestDetails());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RequestResponse sendToProxy(Request request) {
        RequestResponse response = new RequestResponse();
        ProxyServer server = new ProxyServer(request.getPort());
        server.start();
        ProxyClient client = new ProxyClient(request.getIp(), request.getPort(), request);
        response = client.run();
        return response;
    }
}