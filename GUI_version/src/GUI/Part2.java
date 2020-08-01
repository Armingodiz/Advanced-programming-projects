package GUI;
/**
 * this class create a panel
 * and put needed components in it
 * the have method to return this panel as
 * part 1 panel
 *
 * @author Armin Goodarzi
 */


import App.ReqSender;
import ApplicationRunner.Request;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.util.HashMap;

public class Part2 {
    private JFrame mainFrame;
    private JPanel panel;
    private JPanel output;
    private JPanel headerPanel;
    private JPanel query;
    private String type;
    private JMenu get;
    private ArrayList<String> customs;
    private newRequestFrame fnewType;
    private Request currentOpenRequest;
    private ArrayList<JPanel> puttedHeaders;
    private ArrayList<JPanel> puttedQueryHeaders;
    private ArrayList<JPanel> puttedFormDataHeaders;
    private JTextPane paneBody;
    private MainFrame app;
    private JButton addBut;
    private JButton addBut2;
    private JTextField ur;
    private JComboBox bodyTypes;
    private String[] bodies;
    private ArrayList<JMenuItem> items;
    private JMenu typeChoosing;
    private JTextField textArea;
    private FormData formDataPanel;
    private Upload uploadPanel;
    private JPanel urlPanel;
    private JMenuBar menuBar3;


    /**
     * in constructor we create a main panel and add
     * needed components using other methods in part2
     * class and finally we have a method to return
     * part2 panel.
     *
     * @param mainFrame main frame in app
     * @param app       the app
     */
    public Part2(JFrame mainFrame, MainFrame app) throws IOException {
        //creating empty panels and set a default request as current open request
        puttedQueryHeaders = new ArrayList<>();
        this.app = app;
        currentOpenRequest = new Request();
        currentOpenRequest.add(app.getPart1().requestPictureOptions(currentOpenRequest));
        currentOpenRequest.setText(currentOpenRequest.getName());
        this.mainFrame = mainFrame;
        customs = new ArrayList<>();
        fnewType = new newRequestFrame(customs);
        panel = new JPanel();
        output = new JPanel();
        puttedHeaders = new ArrayList<>();
        puttedFormDataHeaders = new ArrayList<>();
        formDataPanel = new FormData(app);
        uploadPanel = new Upload(app);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //creating header tap
        headerPanel = new JPanel();
        BoxLayout boxlayout = new BoxLayout(headerPanel, BoxLayout.Y_AXIS);
        headerPanel.setLayout(boxlayout);
        JPanel temp2 = new JPanel();
        temp2.setLayout(new GridLayout(1, 1));
        addBut = new JButton("ADD HEADER");
        temp2.add(addBut);
        headerPanel.add(temp2);
        addBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                headerPanel.remove(temp2);
                JPanel temp3 = addHeader(1, true, "", "", true);
                puttedHeaders.add(temp3);
                headerPanel.add(temp3);
                headerPanel.add(temp2);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //creating query tap
        query = new JPanel();
        JPanel url = new JPanel();
        BoxLayout boxlayout2 = new BoxLayout(query, BoxLayout.Y_AXIS);
        query.setLayout(boxlayout2);
        urlPanel = createUrl();
        query.add(urlPanel);
        JPanel temp4 = new JPanel();
        temp4.setLayout(new GridLayout(1, 1));
        addBut2 = new JButton("ADD HEADER");
        query.add(temp4);
        temp4.add(addBut2);
        query.add(temp4);
        addBut2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                query.remove(temp4);
                JPanel temp3 = addHeader(1, true, "", "", true);
                query.add(temp3);
                puttedQueryHeaders.add(temp3);
                query.add(temp4);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(layout);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        menuBar3 = new JMenuBar();
        get = typeChoosing("GET          " + "\u25BC");
        menuBar3.add(get);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 25;
        gbc.gridwidth = 1;
        gbc.ipadx = 20;
        menuBar3.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        panel.add(menuBar3, gbc);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        textArea = new JTextField("https://api.myproduct.com/v1/users");
        String defaultUrl = "https://api.myproduct.com/v1/users";
        textArea.addFocusListener(new FocusListenerUrl(textArea, defaultUrl, ur));
        textArea.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (textArea.getText().equals(defaultUrl))
                    ur.setText(textArea.getText());
            }
        });
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.ipady = 25;
        gbc.gridwidth = 8;
        gbc.ipadx = 540;
        textArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));
        panel.add(textArea, gbc);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JButton send = new JButton("  send      > ");
        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    prepareRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ReqSender reqSender = new ReqSender(app.getPart3().getResponseBody(), app.getPart3().getPreview(), currentOpenRequest, app);
                reqSender.execute();
            }
        });
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 9;
        gbc.gridy = 0;
        gbc.ipady = 25;
        gbc.gridwidth = 1;
        gbc.ipadx = 20;
        send.setBorder(BorderFactory.createLineBorder(Color.GRAY, 3, true));
        panel.add(send, gbc);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //creating tap
        JTabbedPane tp = new JTabbedPane();
        JPanel p1 = new JPanel();
        p1.setLayout(new BorderLayout());
        p1.add(creatBearer(), BorderLayout.NORTH);
        JPanel p2 = new JPanel();
        p2.setLayout(new BorderLayout());
        p2.add(query, BorderLayout.NORTH);
        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        p3.add(headerPanel, BorderLayout.NORTH);
        JPanel p4 = new JPanel();
        tp.add(creatJson(), " Body");
        tp.add(p1, " Auth");
        tp.add(p2, "Query");
        tp.add(p3, "Header");
        tp.add(new ProxyPanel(app), "proxy");
        tp.add(new SendOrGetPanel(app), "SendOrGet");
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipady = 630;
        gbc.gridwidth = 10;
        gbc.ipadx = 40;
        panel.add(tp, gbc);
    }
    //##################################################################################################################

    /**
     * this method first add our main panel to
     * north part of output panel then return
     * output panel
     *
     * @return part2 panel
     */
    public JPanel getPanel() {
        output.setLayout(new BorderLayout());
        output.add(panel, BorderLayout.NORTH);
        return output;
    }
    //##################################################################################################################

    /**
     * this method creates a header in form of a panel using given informations
     *
     * @param type       this variable determine a header is a normal header or a query header
     * @param auto       this variable determine if we should creat a header with default information or not
     * @param inputName  input name for header
     * @param inputValue value for header
     * @param enability  check if header is enable or not
     * @return created header in form of a header
     */
    public JPanel addHeader(int type, boolean auto, String inputName, String inputValue, boolean enability) {
        JPanel head = new JPanel();
        JTextField name;
        JTextField value;
        JCheckBox check = new JCheckBox();
        ;
        if (auto) {
            name = new JTextField("NAME");
            name.addFocusListener(new FocusListenerText(name, "NAME"));
            value = new JTextField("VALUE");
            value.addFocusListener(new FocusListenerText(value, "VALUE"));
            check.setSelected(true);
        } else {
            name = new JTextField(inputName);
            value = new JTextField(inputValue);
            check.setSelected(enability);
        }
        check.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    name.setEnabled(true);
                    value.setEnabled(true);
                    name.setEditable(true);
                    value.setEditable(true);
                } else {
                    name.setEnabled(false);
                    value.setEnabled(false);
                    name.setEditable(false);
                    value.setEditable(false);
                }
            }
        });
        JButton delete = new JButton("\u2715");
        delete.addActionListener(new ActionListener() {
            JButton _lastButtonPressed;

            public void actionPerformed(ActionEvent actionEvent) {
                JButton buttonPressed = (JButton) actionEvent.getSource();

                if (_lastButtonPressed == buttonPressed)
                    head.setVisible(false);
                else
                    delete.setText("!");
                _lastButtonPressed = buttonPressed;
            }
        });
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        head.setLayout(layout);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.ipadx = 310;
        head.add(name, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.ipadx = 310;
        head.add(value, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 8;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.ipadx = 10;
        head.add(new JLabel(), gbc);


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 12;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.ipadx = 50;
        head.add(check, gbc);

        if (type == 2) {//for query headers
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("\u25BC");
            JMenuItem item1 = new JMenuItem("Text");
            JMenuItem item2 = new JMenuItem("Text(Multi-line)");
            menu.add(item1);
            menu.add(item2);
            menuBar.add(menu);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 13;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.ipadx = 10;
            head.add(menuBar, gbc);


            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 14;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.ipadx = 1;
            head.add(delete, gbc);


        } else {
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 13;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.ipadx = 1;
            head.add(delete, gbc);
        }


        return head;
    }

    //##################################################################################################################

    /**
     * this method creates a url panel with  a url text field
     * and a button to copy all contents in url text field
     * and then remove created panel as url panel
     *
     * @return a panel
     */
    public JPanel createUrl() {
        JPanel url = new JPanel();
        ur = new JTextField("...");
        ur.setEditable(false);
        JButton copy = new JButton("C");
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Clipboard clipboard = getSystemClipboard();
                clipboard.setContents(new StringSelection(ur.getText()), null);
            }
        });
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        url.setLayout(layout);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 20;
        gbc.ipady = 30;
        gbc.ipadx = 500;
        url.add(ur, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 20;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.ipady = 20;
        gbc.ipadx = 10;
        url.add(copy, gbc);

        url.setBorder(BorderFactory.createTitledBorder("URL Review"));
        return url;
    }

    //##################################################################################################################

    /**
     * this method crates a bearer panel
     * put needed components to it and
     * finally return it
     *
     * @return panel
     */
    public JPanel creatBearer() {
        JPanel bearer = new JPanel();
        JTextField token = new JTextField("TOKEN   :");
        JTextField prefix = new JTextField("PREFIX   :");
        JCheckBox enabled = new JCheckBox("ENABLED");
        enabled.setSelected(true);
        enabled.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    token.setEnabled(true);
                    prefix.setEnabled(true);
                    token.setEditable(true);
                    prefix.setEditable(true);
                } else {
                    token.setEnabled(false);
                    prefix.setEnabled(false);
                    token.setEditable(false);
                    prefix.setEditable(false);
                }
            }
        });
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bearer.setLayout(layout);

        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 540;
        bearer.add(token, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bearer.add(prefix, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        bearer.add(enabled, gbc);

        return bearer;
    }
    //##################################################################################################################

    /**
     * this method create a json body panel
     * using textPane and finally return it
     *
     * @return panel
     */
    public JPanel creatJson() {
        JPanel json = new JPanel();
        bodies = new String[]{"json", "formData", "upload"};
        bodyTypes = new JComboBox(bodies);
        json.setLayout(new BorderLayout());
        paneBody = new JTextPane();
        JScrollPane scrollPane = new JScrollPane(paneBody);
        TextLineNumber textLineNumber = new TextLineNumber(paneBody);
        scrollPane.setRowHeaderView(textLineNumber);
        json.add(bodyTypes, BorderLayout.NORTH);

        bodyTypes.addActionListener(new BodyTypesChoosing(json, scrollPane, formDataPanel, uploadPanel, bodyTypes, app));
        // json.add(scrollPane, BorderLayout.CENTER);
        return json;
    }

    //##################################################################################################################

    /**
     * this method creates a menu for choosing
     * type of request and return it
     *
     * @param name current name of menu
     * @return menu
     */
    public JMenu typeChoosing(String name) {
        typeChoosing = new JMenu(name);
        items = new ArrayList<JMenuItem>();
        JMenuItem get = new JMenuItem("GET");
        get.setForeground(Color.pink);
        JMenuItem post = new JMenuItem("POST");
        post.setForeground(Color.GREEN);
        Color orange = new Color(255, 75, 5, 150);
        JMenuItem put = new JMenuItem("PUT");
        put.setForeground(orange);
        JMenuItem patch = new JMenuItem("PATCH");
        patch.setForeground(Color.YELLOW);
        JMenuItem delete = new JMenuItem("DELETE");
        delete.setForeground(Color.RED);
        JMenuItem options = new JMenuItem("OPTIONS");
        options.setForeground(Color.CYAN);
        JMenuItem head = new JMenuItem("HEAD");
        head.setForeground(Color.BLUE);
        JMenuItem customMethod = new JMenuItem("CUSTOM METHOD");
        customMethod.setForeground(Color.GRAY);
        items.add(get);
        items.add(post);
        items.add(put);
        items.add(patch);
        items.add(delete);
        items.add(options);
        items.add(head);
        items.add(customMethod);
        for (JMenuItem temp : items) {
            typeChoosing.add(temp);
            temp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (temp.getText().equals("CUSTOM METHOD")) {
                        JFrame noUse = new JFrame();
                        fnewType.newType(typeChoosing, customs, noUse, 2);
                        currentOpenRequest.setMethod(typeChoosing.getText());
                    } else {
                        typeChoosing.setText(temp.getText() + "   \u25BC");
                        typeChoosing.setForeground(getColor(temp.getText()));
                        currentOpenRequest.setMethod(temp.getText());
                    }
                }
            });
        }
        return typeChoosing;
    }

    //##################################################################################################################

    /**
     * @return current open request
     */
    public Request getCurrentOpenRequest() {
        return currentOpenRequest;
    }

    /**
     * this method also change gui parts based on new setted request
     *
     * @param currentOpenRequest current open request
     */
    public void setCurrentOpenRequest(Request currentOpenRequest) {
        this.currentOpenRequest = currentOpenRequest;
        textArea.setText(currentOpenRequest.getUrl().toString());
        puttedHeaders.clear();
        puttedQueryHeaders.clear();
        puttedFormDataHeaders.clear();
        setHeadersOFPuttedRequest(currentOpenRequest.getHeaders(), puttedHeaders, headerPanel, addBut);
        setHeadersOFPuttedRequest(currentOpenRequest.getQueryHeaders(), puttedQueryHeaders, query, addBut2);
        setHeadersOFPuttedRequest(currentOpenRequest.getFormData(), puttedFormDataHeaders, formDataPanel.getFormDataPanel(), formDataPanel.getAddBut3());
        for (JMenuItem temp : items) {
            if (temp.getText().equals(currentOpenRequest.getMethod())) {
                temp.setSelected(true);
                temp.doClick();
            }
        }
        for (int i = 0; i < 3; i++)
            if (currentOpenRequest.getBodyType().equals(bodyTypes.getItemAt(i)))
                bodyTypes.setSelectedIndex(i);
        if (currentOpenRequest.getJsonData() != null)
            paneBody.setText(currentOpenRequest.getJsonData().toString());
        if (currentOpenRequest.getUploadedFile() != null)
            uploadPanel.getFileNAme().setText(currentOpenRequest.getUploadedFile().getPath());
        ur.setText(currentOpenRequest.getUrl().toString());
    }

    //##################################################################################################################

    /**
     * this method save changes in current open request
     * body , headers and etc.
     * at the end it reset the part 2 to be cleared from
     * previous open request
     */
    public void saveInformationOfRequest() {
        currentOpenRequest.clearRequestOldData();
        for (JPanel temp : puttedHeaders) {
            JTextField n = (JTextField) temp.getComponent(0);
            JTextField v = (JTextField) temp.getComponent(1);
            JCheckBox e = (JCheckBox) temp.getComponent(3);
            Header header = new Header(e.isSelected(), n.getText(), v.getText());
            currentOpenRequest.addHeader(header);
        }
        for (JPanel temp : puttedQueryHeaders) {
            JTextField n = (JTextField) temp.getComponent(0);
            JTextField v = (JTextField) temp.getComponent(1);
            JCheckBox e = (JCheckBox) temp.getComponent(3);
            Header header = new Header(e.isSelected(), n.getText(), v.getText());
            currentOpenRequest.addQueryHeader(header);
        }
        for (JPanel temp : puttedFormDataHeaders) {
            JTextField n = (JTextField) temp.getComponent(0);
            JTextField v = (JTextField) temp.getComponent(1);
            JCheckBox e = (JCheckBox) temp.getComponent(3);
            Header header = new Header(e.isSelected(), n.getText(), v.getText());
            currentOpenRequest.addFormData(header);
        }
        resetPart();
    }

    //##################################################################################################################

    /**
     * this method  reset the part 2 to be cleared from
     * previous open request
     */
    public void resetPart() {
        for (JPanel temp : puttedHeaders) {
            headerPanel.remove(temp);
        }
        for (JPanel temp : puttedQueryHeaders) {
            query.remove(temp);
        }
        paneBody.setText("");
        puttedHeaders.clear();
        puttedQueryHeaders.clear();
    }
    //##################################################################################################################

    /**
     * @return clipboard
     */
    private static Clipboard getSystemClipboard() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        return defaultToolkit.getSystemClipboard();
    }

    public String getURL() {
        return textArea.getText();
    }

    public void prepareRequest() throws IOException {
        saveInformationOfRequest();
        currentOpenRequest.setUrl(new URL(getURL()));
        try {
            if (currentOpenRequest.getBodyText() != null) {
                JSONObject jsonObject = new JSONObject(currentOpenRequest.getBodyText());
                currentOpenRequest.setJsonData(jsonObject);
            }
        } catch (JSONException e) {
            System.out.println("INPUTTED FORMAT FOR JSON IS INVALID !");
        }
    }

    /**
     * @param formData form data header panel
     */
    public void addFormDataPanel(JPanel formData) {
        puttedFormDataHeaders.add(formData);
    }
//######################################################################################################################

    /**
     * this method add each type of header such as query headers and etc to right
     * place for them in different parts of gui.
     *
     * @param headers    list of headers to set
     * @param panels     the list of panel formed headers
     * @param panelToPut the panel we want to add header to it
     * @param addButton  the add button we must add to end of the list
     */
    public void setHeadersOFPuttedRequest(ArrayList<Header> headers, ArrayList<JPanel> panels, JPanel panelToPut, JButton addButton) {
        panelToPut.removeAll();
        for (Header temp : headers) {
            JPanel header = addHeader(1, false, temp.getName(), temp.getValue(), temp.isEnable());
            panels.add(header);
            panelToPut.add(header);
        }
        if (panelToPut.equals(query))
            panelToPut.add(urlPanel);
        JPanel temp = new JPanel();
        temp.setLayout(new GridLayout(1, 1));
        temp.add(addButton);
        panelToPut.add(temp);
    }

    /**
     * this method returns a color for panel
     * @param method method
     * @return color for panel
     */
    public Color getColor(String method) {
        if (method.equals("GET"))
            return new Color(224, 0, 132, 150);
        else if (method.equals("POST"))
            return new Color(44, 255, 0, 150);
        else if (method.equals("PUT"))
            return new Color(255, 75, 5, 150);
        else if (method.equals("DELETE"))
            return new Color(222, 11, 32, 150);
        else
            return new Color(214, 224, 82, 150);
    }
}
