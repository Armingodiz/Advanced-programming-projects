package GUI;
/**
 * this class creat a panel which has needed components
 * for part 3 and have methods to create this panel
 * and finally a method to return it .
 */


import ApplicationRunner.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ResponsePanel {
    private JPanel headerPanel;
    private JFrame mainFrame;
    private JPanel response;
    private JPanel header;
    private Request currentOpenRequest;
    private MainFrame app;
    private ArrayList<JPanel> headers;
    private JButton copy;
    private JTextPane responseBody;
    private JPanel preview;
    private ResponseInformation responseInformation;
    private JTextPane morInfo;

    /**
     * constructor create empty panels and fill them using methods
     * we have in this class
     *
     * @param mainFrame its the main frame we show it to user
     * @param app       its the class that has all 3 parts and connect them and manage them
     */
    public ResponsePanel(JFrame mainFrame, MainFrame app) throws IOException {
        headers = new ArrayList<>();
        this.app = app;
        currentOpenRequest = new Request();
        this.mainFrame = mainFrame;
        response = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        response.setLayout(layout);
        JTabbedPane tp = new JTabbedPane();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //making response body panel and adding its combo box
        JPanel body = new JPanel();
        body.setLayout(new BorderLayout());
        String[] bodyTypes = {"Raw", "Preview"};
        JComboBox responseBodies = new JComboBox(bodyTypes);
        body.add(responseBodies, BorderLayout.NORTH);
        responseBody = new JTextPane();
        preview = new JPanel();
        JScrollPane scrollPane = new JScrollPane(responseBody);
        responseBodies.addActionListener(new ResponseTypeChoosing(body, scrollPane, preview, responseBodies));
        tp.add(body, "BODY");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //making header panel panel
        header = new JPanel();
        header.setLayout(new BorderLayout());
        headerPanel = creatHeader();
        header.add(headerPanel, BorderLayout.NORTH);
        tp.add(header, "HEADER");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //creating explanation tap
        JPanel exp = new JPanel();
        exp.setLayout(new BorderLayout());
        morInfo = new JTextPane();
        morInfo.setEditable(false);
        morInfo.setText("* Preparing request to \n" +
                "* Using libcurl/7.57.0-DEV OpenSSL/1.0.2o zlib/1.2.11 libssh2/1.7.0_DEV\n" +
                "* Disable timeout\n" +
                "* Enable automatic URL encoding\n" +
                "* Enable SSL validation\n" +
                "* Enable cookie sending with jar of 0 cookies\n" +
                "* <url> malformed\n" +
                "* Closing connection -1");
        exp.add(morInfo);
        tp.add(exp, "Information");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding taps and response information panel to main panel
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 40;
        gbc.gridheight = 1;
        gbc.ipadx = 400;
        gbc.ipady = 0;
        responseInformation = new ResponseInformation(app);
        JPanel top = responseInformation.getPanel();
        response.add(top, gbc);


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 40;
        gbc.gridheight = 1;
        gbc.ipadx = 400;
        gbc.ipady = 650;
        response.add(tp, gbc);
    }

    //##################################################################################################################

    /**
     * this method put main panel to north of another panel and then
     * return that panel as response panel
     *
     * @return response panel
     */
    public JPanel getResponse() {
        JPanel output = new JPanel();
        output.setLayout(new BorderLayout());
        output.add(response, BorderLayout.NORTH);
        return output;
    }

    //##################################################################################################################

    /**
     * this method create a header panel
     *
     * @return header panel
     */
    public JPanel creatHeader() {
        JPanel header = new JPanel();
        BoxLayout boxlayout = new BoxLayout(header, BoxLayout.Y_AXIS);
        header.setLayout(boxlayout);
        JPanel temp = addHeader(0, "", "");
        copy = new JButton("Copy to Clipboard");
        header.add(copy);
        header.add(temp);
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Clipboard clipboard = getSystemClipboard();
                String copied = "";
                for (JPanel temp : headers) {
                    JTextField name = (JTextField) temp.getComponent(0);
                    JTextField value = (JTextField) temp.getComponent(1);
                    copied = copied + "\n" + name.getText() + "   " + value.getText();
                }
                clipboard.setContents(new StringSelection(copied), null);
            }
        });
        return header;
    }

    //##################################################################################################################

    /**
     * this method makes a header as a panel with given information
     *
     * @param type       if type==0 it makes a label for name and value
     * @param inputName  name of header
     * @param inputValue value of header
     * @return a header as a panel
     */
    public JPanel addHeader(int type, String inputName, String inputValue) {
        JPanel head = new JPanel();
        JTextField name = new JTextField();
        JTextField value = new JTextField();
        name.setPreferredSize(new Dimension(150, 30));
        value.setPreferredSize(new Dimension(150, 30));
        name.setEditable(false);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        head.setLayout(layout);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        name.setEditable(false);
        value.setEditable(false);
        if (type == 0) {
            name.setText("                            NAME ");
            value.setText("                           VALUE");
        } else {
            name.setText(inputName);
            value.setText(inputValue);
        }
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        // gbc.gridwidth = 1;
        gbc.ipadx = 150;
        head.add(name, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 1;
        gbc.gridy = 0;
        //gbc.gridwidth = 1;
        gbc.ipadx = 150;
        head.add(value, gbc);

        return head;
    }

    //##################################################################################################################

    /**
     * this method create a body panel with textPane
     *
     * @return body panel
     */
    public JPanel creatBody() {
        JPanel body = new JPanel();
        JTextPane answer = new JTextPane();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        body.setLayout(layout);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipadx = 300;
        gbc.ipady = 600;
        body.add(answer, gbc);
        return body;
    }

    //##################################################################################################################

    /**
     * @return current open request in part 3
     */
    public Request getCurrentOpenRequest() {
        return currentOpenRequest;
    }

    /**
     * @param currentOpenRequest current open request in part 3
     */
    public void setCurrentOpenRequest(Request currentOpenRequest) {
        this.currentOpenRequest = currentOpenRequest;
    }

    //##################################################################################################################

    /**
     * @return a clipBoard
     */
    private static Clipboard getSystemClipboard() {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        return defaultToolkit.getSystemClipboard();
    }

    //##################################################################################################################

    /**
     * this method update putted headers in headers panel
     * based on headers that current open request has
     */
    public void updateRequestHeaders() {
        headerPanel.setVisible(false);
        resetHeaderPanel();
        // headerPanel.remove(copy);
        for (Header temp : currentOpenRequest.getHeaders()) {
            System.out.println("###############################################33");
            System.out.println(temp.getName() + "        " + temp.getValue());
            JPanel current = addHeader(1, temp.getName(), temp.getValue());
            headerPanel.add(current);
            headers.add(current);
        }
        //  header.add(copy);
        headerPanel.setVisible(true);
    }

    //##################################################################################################################

    /**
     * this method will empty the header panel to make it ready to put
     * new headers
     */
    public void resetHeaderPanel() {
        int i = 0;
        for (Component temp : headerPanel.getComponents()) {
            //   headerPanel.remove(i+2);
            System.out.println("header  removed");
            i++;
        }
        for (int j = 0; j < headers.size(); j++) {
            // headerPanel.remove(j);
            System.out.println("@@@@@@@@@@@@@@@@@2  " + j);
        }
        for (JPanel current : headers) {
            // headerPanel.remove(current);
        }
        System.out.println("#################################3   " + i);
        headerPanel.removeAll();
        headers.clear();
    }

    /**
     * @return copy button
     */
    public JButton getCopy() {
        return copy;
    }

    /**
     * @return header panel
     */
    public JPanel getHeaderPanel() {
        return headerPanel;
    }

    /**
     * @param headerPanel header panel
     */
    public void setHeaderPanel(JPanel headerPanel) {
        this.headerPanel = headerPanel;
    }

    /**
     * @param copy copy button
     */
    public void setCopy(JButton copy) {
        this.copy = copy;
    }

    /**
     * @return pane of response
     */
    public JTextPane getResponseBody() {
        return responseBody;
    }

    /**
     * @param responseBody pane of response
     */
    public void setResponseBody(JTextPane responseBody) {
        this.responseBody = responseBody;
    }

    /**
     * @return preview panel
     */
    public JPanel getPreview() {
        return preview;
    }

    /**
     * @param preview preview panel
     */
    public void setPreview(JPanel preview) {
        this.preview = preview;
    }

    /**
     * @return response information part
     */
    public ResponseInformation getResponseInformation() {
        return responseInformation;
    }

    /**
     * @param responseInformation response information part
     */
    public void setResponseInformation(ResponseInformation responseInformation) {
        this.responseInformation = responseInformation;
    }

    /**
     * add headers of request
     *
     * @param key   key of header
     * @param value value of header
     */
    public void addHeadersOfRequest(String key, String value) {
        headers.add(addHeader(1, key, value));
        headerPanel.add(addHeader(1, key, value));
    }

    /**
     * @return information text pane
     */
    public JTextPane getMorInfo() {
        return morInfo;
    }
}