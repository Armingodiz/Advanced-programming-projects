package SenderServer;


import ApplicationRunner.Request;
import GUI.Folder;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class create a pack which contains
 * a list of requests and a list of lists
 */
public class RequestPack implements Serializable {
    ArrayList<Request> requests;
    ArrayList<Folder> folders;

    /**
     * constructor
     */
    public RequestPack() {
        requests = new ArrayList<>();
        folders = new ArrayList<>();
    }

    /**
     * adding request to pack
     *
     * @param request request to add
     */
    public void addRequest(Request request) {
        requests.add(request);
    }

    /**
     * @return list of requests
     */
    public ArrayList<Request> getRequests() {
        return requests;
    }

    /**
     * @param requests list of requests
     */
    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    /**
     *
     * @param folder folder to add
     */
    public void addFolder(Folder folder) {
        folders.add(folder);
    }

    /**
     *
     * @return folders in pack
     */
    public ArrayList<Folder> getFolders() {
        return folders;
    }

    /**
     *
     * @param folders folders in pack
     */
    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
    }

    /**
     *
     * @return a string show all requests and lists to pack
     */
    public String packToString() {
        String pack = "";
        pack = pack + "REQUESTS IN PACK : " + "\n";
        for (Request temp : requests) {
            pack = pack + "NAME OF REQUEST : " + temp.getName() + " URL : " + temp.getUrl().toString() + "\n";
        }
        pack = pack + "LISTS IN PACK : " + "\n";
        for (Folder temp : folders) {
            pack = pack + "NAME OF LIST : " + temp.getName() + "\n";
        }
        return pack;
    }

    /**
     *
     * @return all requests even requests in folders
     */
    public ArrayList<Request> getAllRequests(){
        ArrayList<Request> all=requests;
        for (Folder temp:folders) {
            all.addAll(temp.getRequests());
        }
        return all;
    }
}
