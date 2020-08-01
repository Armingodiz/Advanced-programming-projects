package GUI;
/**
 * this class create a panel
 * and put needed components in it
 * the have method to return this panel as
 * part 1 panel
 *
 * @author Armin Goodarzi
 */

import ApplicationRunner.Request;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Part1 {
    private JPanel panel;
    private JPanel output;
    private ArrayList<JMenu> jMenus;
    private ArrayList<String> customs;
    private int lastJMenue;
    private String folderName;
    private JFrame mainFrame;
    private JMenuBar list;
    private Request currentOpenRequest;
    private MainFrame app;
    private ArrayList<Request> requests;
    private ArrayList<Folder> lists;
    private JMenuBar menuBar;
    private boolean searchMood;

    /**
     * @param mainFrame the main frame
     * @param app       the app
     */
    public Part1(JFrame mainFrame, MainFrame app) throws IOException {
        //creating empty panels and a default open request
        searchMood = false;
        requests = new ArrayList<>();
        this.app = app;
        currentOpenRequest = new Request();
        lastJMenue = 7;
        this.mainFrame = mainFrame;
        customs = new ArrayList<>();
        lists = new ArrayList<>();
        panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(layout);
        output = new JPanel();
        output.setLayout(new BorderLayout());
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding insomnia menubar
        JMenuBar insomn = new JMenuBar();
        Color color = new Color(107, 10, 200, 222);
        JMenu insomnia = new JMenu("INSOMNIA                                                                         ");
        insomn.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));
        insomnia.setBorder(BorderFactory.createLineBorder(color, 2, true));
        insomnia.setBackground(color);
        insomnia.setForeground(color);
        insomn.add(insomnia);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 20;
        gbc.gridwidth = 6;
        insomn.setBorder(BorderFactory.createTitledBorder(""));
        panel.add(insomn, gbc);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding environment menubar
        menuBar = new JMenuBar();
        menuBar.setLayout(new GridLayout(0, 1));
        JMenu environment = new JMenu("NO ENVIRONMENT");
        JMenu general = new JMenu("GENERAL");
        JMenuItem noEnvironment = new JMenuItem("Manage Environments");
        general.add(noEnvironment);
        JMenu active = new JMenu("ACTIVE ENVIRONMENT");
        JMenuItem no = new JMenuItem("NO ENVIRONMENT");
        environment.add(general);
        environment.add(active);
        active.add(no);
        menuBar.add(environment);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipady = 10;
        gbc.gridwidth = 3;
        menuBar.setBorder(BorderFactory.createTitledBorder(""));
        panel.add(menuBar, gbc);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding cookies button
        JButton cookies = new JButton("Cookies");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        cookies.setBorder(BorderFactory.createTitledBorder(""));
        panel.add(cookies, gbc);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding filter textfield to search between requests
        JTextField filter = new JTextField("SEARCH");
        //filter.addFocusListener(new com.company.FocusListenerText(filter, "Filter"));
        filter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                if (searchMood) {
                    panel.remove(5);
                    gbc.fill = GridBagConstraints.HORIZONTAL;
                    gbc.gridx = 0;
                    gbc.gridy = 3;
                    gbc.gridwidth = 5;
                    panel.add(getSearchResult(filter.getText()), gbc);
                    panel.setVisible(false);
                    panel.setVisible(true);
                    filter.requestFocus();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                insertUpdate(documentEvent);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {

            }
        });
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        filter.setBorder(BorderFactory.createTitledBorder(""));
        panel.add(filter, gbc);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding menubar for adding folder and request
        JMenuBar add = new JMenuBar();
        JMenu search = searchControler();
        JMenu add2 = new JMenu("\u2295 " + "\u25BC");
        add.add(search);
        add.add(add2);
        newRequestAction newRequestAction = new newRequestAction();
        JMenuItem newreq = new JMenuItem("NEW REQUEST");
        newreq.setMnemonic(KeyEvent.VK_R);
        newreq.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        newreq.addActionListener(newRequestAction);


        JMenuItem newfol = new JMenuItem("NEW FOLDER");
        newfol.setMnemonic(KeyEvent.VK_F);
        newfol.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        newfol.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                newFolder(new Folder("@@@@@", "@@@@@"));
            }
        });
        add2.add(newreq);
        add2.add(newfol);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 5;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add.setBorder(BorderFactory.createTitledBorder(""));
        panel.add(add, gbc);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding menu to part 1 as category for requests
        jMenus = new ArrayList<JMenu>();
        list = new JMenuBar();
        JMenu get = new JMenu("GET");
        get.setForeground(color);
        JMenu post = new JMenu("POST");
        post.setForeground(Color.GREEN);
        Color orange = new Color(255, 75, 5, 150);
        JMenu put = new JMenu("PUT");
        put.setForeground(orange);
        JMenu patch = new JMenu("PATCH");
        patch.setForeground(Color.YELLOW);
        JMenu delete = new JMenu("DELETE");
        delete.setForeground(Color.RED);
        JMenu options = new JMenu("OPTIONS");
        options.setForeground(Color.CYAN);
        JMenu head = new JMenu("HEAD");
        head.setForeground(Color.BLUE);
        JMenu customMethod = new JMenu("Custom Method");
        customMethod.setForeground(Color.GRAY);
        jMenus.add(get);
        jMenus.add(post);
        jMenus.add(put);
        jMenus.add(patch);
        jMenus.add(delete);
        jMenus.add(options);
        jMenus.add(head);
        jMenus.add(customMethod);
        for (JMenu temp : jMenus) {
            list.add(temp);
        }
        list.setLayout(new GridLayout(0, 1));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        panel.add(list, gbc);
        loadRequests();
        loadLists();
    }
    //##################################################################################################################

    /**
     * this method first put our main panel in
     * north part of output panel then return
     * the out put panel
     *
     * @return part1 panel
     */
    public JPanel getPanel() {
        output.add(panel, BorderLayout.NORTH);
        return output;
    }

    /**
     * @return array list of custom types that user already created
     */
    public ArrayList<String> getCustoms() {
        return customs;
    }

    //##################################################################################################################

    /**
     * this class a action listener for new request action
     * it get needed information using newRequestFrame
     * then create a request with given informations
     * and put it in right category or right folder
     * based on constructor input
     */
    private class newRequestAction implements ActionListener {
        Folder folder;

        newRequestAction() {
            folder = new Folder("empty%%%", "empty%%%");
        }

        newRequestAction(Folder folder) {
            this.folder = folder;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            newRequestFrame newRequestFrame = new newRequestFrame(customs);
            JFrame frame = newRequestFrame.addNewRequestFrame();
            newRequestFrame.getCreate().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String name = newRequestFrame.getNameField().getText();
                    String type = newRequestFrame.getType();
                    String body = newRequestFrame.getBodyType();
                    System.out.println(name + "  " + type + "  " + body);
                    Request requestPicture = null;
                    try {
                        requestPicture = new Request();
                        requestPicture.setName(name);
                        requestPicture.setMethod(type);
                        requestPicture.setBodyType(body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    requests.add(requestPicture);
                    requestPicture.add(requestPictureOptions(requestPicture));
                    requestPicture.setText(requestPicture.getName());
                    JLabel tip = new JLabel("NAME : " + name + "      TYPE : " + type + "    BODY : " + body);
                    requestPicture.add(tip);
                    if (folder.getName().equals("empty%%%")) {
                        if (isConfirmedType(type)) {
                            for (JMenu temp : jMenus) {
                                if (temp.getText().equals(type))
                                    temp.add(requestPicture);
                            }
                        } else {
                            customs.add(type);
                            jMenus.get(7).add(requestPicture);
                        }
                    } else {
                        folder.addRequest(requestPicture);
                        folder.add(requestPicture);
                    }
                }
            });
        }
    }
    //##################################################################################################################

    /**
     * this method get a type name and check
     * if it has its own category or not
     *
     * @param type input string
     * @return true or false
     */
    public boolean isConfirmedType(String type) {
        String[] confirmedTypes = {"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"};
        for (String current : confirmedTypes) {
            if (type.equals(current))
                return true;
        }
        return false;
    }

    //##################################################################################################################

    /**
     * this method show a JDialog and get needed informations from
     * user then check if we should put it in a folder or not
     * then we make a menu for this folder options and finally
     * put folder in right place
     *
     * @param folder menu we want to add folder to it
     */
    public void newFolder(Folder folder) {
        JDialog newFolder = new JDialog(mainFrame, "New Folder");
        JTextField name = new JTextField("Name");
        JButton create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                folderName = name.getText();
                JMenuBar folderIcon = new JMenuBar();
                Folder fol = new Folder("F " + folderName + "                          ", folderName);
                lists.add(fol);
                folderIcon.add(fol);
                if (folder.getName().equals("@@@@@")) {
                    JMenu opt = optionsForFolders(fol, folderIcon, new JMenu());
                    folderIcon.add(opt);
                    list.add(folderIcon);
                } else {//putting folder in a folder
                    Folder c = new Folder(fol.getText(), fol.getName());
                    fol.setText("Contents");
                    c.add(fol);
                    JMenu opt = optionsForFolders(fol, folderIcon, c);
                    c.add(opt);
                    folder.add(c);
                }
                newFolder.dispose();
            }
        });
        JPanel folders = new JPanel();
        BoxLayout boxlayout2 = new BoxLayout(folders, BoxLayout.Y_AXIS);
        JPanel empty = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(0);
        empty.setLayout(layout);
        JLabel emp = new JLabel("                                                                                                      ");
        empty.add(emp);
        empty.add(create);
        folders.setLayout(boxlayout2);
        folders.add(name);
        folders.add(empty);
        newFolder.add(folders);
        newFolder.setSize(400, 100);
        newFolder.setLocation(400, 400);
        newFolder.setVisible(true);
    }

    //##################################################################################################################

    /**
     * this method creates a menu for a folder
     * and add needed action listeners to it
     *
     * @param folder    the folder we want add options to it
     * @param removeTo  the folder we want to add options to it
     * @param removeTo2 the folder we want to add options to it
     * @return a menu
     */
    public JMenu optionsForFolders(Folder folder, JMenuBar removeTo, JMenu removeTo2) {
        JMenu popupMenu = new JMenu("    >");
        JMenuItem newf = new JMenuItem("New Folder");
        newf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                newFolder(folder);
            }
        });
        JMenuItem newr = new JMenuItem("New Request");
        newr.addActionListener(new newRequestAction(folder));
        JMenuItem del = new JMenuItem("Delete");
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                removeTo.setVisible(false);
                removeTo2.setVisible(false);
            }
        });
        JMenuItem cop = new JMenuItem("Duplicate");
        popupMenu.add(newf);
        popupMenu.add(newr);
        popupMenu.add(del);
        popupMenu.add(cop);
        return popupMenu;
    }
    //##################################################################################################################

    /**
     * this method creates a menu and add needed items and
     * action listeners to it and finally add it to its request
     *
     * @param addTo the request we want to add options to it
     * @return a menu
     */
    public JMenu requestPictureOptions(Request addTo) {
        JMenu requestOptions = new JMenu("Options");
        JMenuItem pick = new JMenuItem("Chose");
        requestOptions.add(pick);
        pick.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                app.getPart2().saveInformationOfRequest();
                currentOpenRequest = addTo;
                app.getPart2().setCurrentOpenRequest(addTo);
                app.getPart3().setCurrentOpenRequest(addTo);
                app.getPart3().updateRequestHeaders();
            }
        });
        JMenuItem duplicate = new JMenuItem("Duplicate");
        requestOptions.add(duplicate);
        JMenuItem generateCode = new JMenuItem("Generate Code");
        requestOptions.add(generateCode);
        JMenuItem copyCurl = new JMenuItem("Copy as CURL");
        requestOptions.add(copyCurl);
        JMenuItem delete = new JMenuItem("Delete");
        requestOptions.add(delete);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                addTo.setVisible(false);
            }
        });
        JMenuItem settings = new JMenuItem("Settings");
        requestOptions.add(settings);
        return requestOptions;
    }

    //##################################################################################################################

    /**
     * @return current open request
     */
    public Request getCurrentOpenRequest() {
        return currentOpenRequest;
    }

    /**
     * @param currentOpenRequest set new current open request
     */
    public void setCurrentOpenRequest(Request currentOpenRequest) {
        this.currentOpenRequest = currentOpenRequest;
    }

    //##################################################################################################################

    /**
     * this method get a string and search between requests
     * and return list of request that have inputted string
     * in them
     *
     * @param input the inputted string
     * @return requests that have inputted string in them
     */
    public JMenuBar getSearchResult(String input) {
        JMenuBar newBar = new JMenuBar();
        newBar.setLayout(new GridLayout(0, 1));
        for (Request temp : requests) {
            if (temp.getName().contains(input) && input.length() != 0) {
                System.out.println(temp.getName());
                newBar.add(temp);
            }
        }
        return newBar;
    }

    //##################################################################################################################

    /**
     * creating and returning a menu to determine
     * if we should search or not
     *
     * @return menu
     */
    public JMenu searchControler() {
        JMenu search = new JMenu("Search");
        JRadioButton on = new JRadioButton("SEARCHING MOOD ON");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        on.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.remove(list);
                panel.add(new JMenuBar(), gbc);
                panel.setVisible(false);
                panel.setVisible(true);
                searchMood = true;
            }
        });
        JRadioButton off = new JRadioButton("SEARCHING MOOD OFF", true);
        off.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                panel.remove(5);
                panel.add(list, gbc);
                panel.setVisible(false);
                panel.setVisible(true);
                searchMood = false;
            }
        });
        ButtonGroup bg = new ButtonGroup();
        bg.add(off);
        bg.add(on);
        search.add(on);
        search.add(off);
        return search;
    }

    //##################################################################################################################

    /**
     * this class reade requests from file and put them in part 1
     */
    public void loadRequests() {
        File file = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\saveedReqs\\nameOfRequests.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String name = "";
            while ((name = reader.readLine()) != null) {
                FileInputStream fileIn = new FileInputStream(new File(name));
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Request request;
                request = (Request) in.readObject();
                requests.add(request);
                request.removeAll();
                request.add(requestPictureOptions(request));
                for (JMenu temp : jMenus) {
                    if (temp.getText().equals(request.getMethod()))
                        temp.add(request);
                }
                in.close();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //##################################################################################################################

    /**
     * this class read folders object and put them in part 1
     * also with their requests
     */
    public void loadLists() {
        File file = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\Lists\\nameOfLists.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String name = "";
            while ((name = reader.readLine()) != null) {
                FileInputStream fileIn = new FileInputStream(new File(name));
                ObjectInputStream in = new ObjectInputStream(fileIn);
                Folder folder;
                folder = (Folder) in.readObject();
                folder.removeAll();
                for (Request temp : folder.getRequests()) {
                    temp.removeAll();
                    temp.add(requestPictureOptions(temp));
                    folder.add(temp);
                }
                folder.setText("F " + folder.getName() + "                          ");
                JMenuBar folderIcon = new JMenuBar();
                lists.add(folder);
                folderIcon.add(folder);
                JMenu opt = optionsForFolders(folder, folderIcon, new JMenu());
                folderIcon.add(opt);
                list.add(folderIcon);
                in.close();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //##################################################################################################################

    /**
     * this class save all lists in files and their name in a text file
     *
     * @throws IOException
     */
    public void saveLists() throws IOException {
        int counter = 0;
        File nameReq = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\Lists\\nameOfLists.txt");
        nameReq.delete();
        nameReq.createNewFile();
        for (Folder temp : lists) {
            File file = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\Lists\\" + temp.getName());
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(temp);
                objectOutputStream.close();
                BufferedWriter writer = new BufferedWriter(new FileWriter(nameReq, true));
                if (counter != 0)
                    writer.newLine();
                writer.write(file.getPath());
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        }
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
     * @return list of folders
     */
    public ArrayList<Folder> getLists() {
        return lists;
    }

    /**
     *
     * @param lists list of requests
     */
    public void setLists(ArrayList<Folder> lists) {
        this.lists = lists;
    }

    /**
     *
     * @return list of types of method
     */
    public ArrayList<JMenu> getjMenus() {
        return jMenus;
    }

    /**
     *
     * @return list
     */
    public JMenuBar getList() {
        return list;
    }
}
