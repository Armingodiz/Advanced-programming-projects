package GUI;

import ApplicationRunner.Request;
import ApplicationRunner.RequestResponse;
import SenderServer.ClientGetter;
import SenderServer.RequestPack;
import SenderServer.SenderServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * this class create a panel for sending or receiving all
 * requests in a app .
 */
public class SendOrGetPanel extends JPanel {
    private MainFrame app;
    private JTextField ipField;
    private JTextField portField;
    private JRadioButton getBut;
    private JRadioButton sendBut;
    private JTextPane statusPane;
    private JButton goBut;

    /**
     * constructor
     * creating needed components and
     * adding them to panel
     *
     * @param app the main application
     */
    SendOrGetPanel(MainFrame app) {
        this.app = app;
        statusPane = new JTextPane();
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
        // panel.setPreferredSize(new Dimension(50, 43));
        /////////////////////////////////////////////////////////////////////////////
        ipField = new JTextField();
        ipField.setText("localhost");
        ipField.addFocusListener(new FocusListenerText(ipField, "ip"));
        ipField.setFont(new Font("Arial", Font.BOLD, 11));
        ipField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        ipField.setPreferredSize(new Dimension(150, 30));
        panel.add(ipField);
        /////////////////////////////////////////////////////////////////////////////
        portField = new JTextField();
        portField.setText("port");
        portField.addFocusListener(new FocusListenerText(portField, "port"));
        portField.setFont(new Font("Arial", Font.BOLD, 11));
        portField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        portField.setPreferredSize(new Dimension(150, 30));
        panel.add(portField);
        //////////////////////////////////////////////////////////////////////////////
        JLabel empty = new JLabel("      ");
        panel.add(empty);
        //////////////////////////////////////////////////////////////////////////////
        getBut = new JRadioButton("GET REQUESTS");
        getBut.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        getBut.setFont(new Font("Arial", Font.BOLD, 8));
        getBut.setPreferredSize(new Dimension(100, 30));
        getBut.setBackground(Color.RED);
        getBut.setForeground(Color.RED);
        ///////////////
        sendBut = new JRadioButton("SEND REQUESTS");
        sendBut.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        sendBut.setFont(new Font("Arial", Font.BOLD, 8));
        sendBut.setPreferredSize(new Dimension(100, 30));
        sendBut.setBackground(Color.GREEN);
        sendBut.setForeground(Color.GREEN);
        ButtonGroup group = new ButtonGroup();
        group.add(sendBut);
        group.add(getBut);
        panel.add(getBut);
        panel.add(sendBut);
        //////////////////////////////////////////////////////////////////////////////
        JLabel empty2 = new JLabel("      ");
        panel.add(empty2);
        /////////////////////////////////////////////////////////////////////////////
        goBut = new JButton("GO");
        goBut.addActionListener(new goButton());
        goBut.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        goBut.setFont(new Font("Arial", Font.BOLD, 8));
        goBut.setPreferredSize(new Dimension(60, 30));
        goBut.setBackground(Color.BLUE);
        goBut.setForeground(Color.BLUE);
        panel.add(goBut);
        //////////////////////////////////////////////////////////////////////////////
        statusPane.setEditable(false);
        statusPane.setBackground(new Color(0x5D85DF));
        statusPane.setText("                                                                                            STATUS" + "\n");
        panel.setBackground(Color.gray);
        JScrollPane scrollPane = new JScrollPane(statusPane);
        this.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1, true));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1, true));
        this.add(panel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * this class is a action listener that create a
     * server to send requests or create a client
     * to get requests based on chosen option .
     */
    private class goButton implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            RequestPack requestPack = new RequestPack();
            if (getBut.isSelected()) {//getting requests
                ClientGetter clientGetter = new ClientGetter(ipField.getText(), Integer.parseInt(portField.getText()), requestPack, statusPane);
                requestPack = clientGetter.run();
                statusPane.setText(statusPane.getText() + requestPack.packToString());
                JMenuBar folderIcon = new JMenuBar();
                JMenu addedRequests = new JMenu("REQUESTS FROM PROXY  " + "\u25BC");
                for (Request temp : requestPack.getAllRequests()) {
                    app.getPart1().getRequests().add(temp);
                    temp.removeAll();
                    temp.add(app.getPart1().requestPictureOptions(temp));
                    addedRequests.add(temp);
                }
                folderIcon.add(addedRequests);
                app.getPart1().getList().add(folderIcon);
            } else {//sending all requests
                for (Request temp : app.getPart1().getRequests())
                    requestPack.addRequest(temp);
                for (Folder temp : app.getPart1().getLists()) {
                    System.out.println(temp.getName() + "    ADDED");
                    requestPack.addFolder(temp);
                }
                SenderServer senderServer = new SenderServer(Integer.parseInt(portField.getText()), requestPack, statusPane);
                senderServer.start();
                System.out.println("FUCK");
            }
        }
    }
}