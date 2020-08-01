package GUI;

import ApplicationRunner.RequestResponse;
import ServerProxy.ProxyClient;
import ServerProxy.ProxyServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * this class create a panel to send a request to server
 * and get back its answer .
 */
public class ProxyPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField ipField;
    private JTextField portField;
    private RequestResponse requestResponse;
    private JButton showResponse;
    private JTextPane statusPane;

    /**
     * constructor
     * also create needed components and add them to panel
     * @param mainFrame the main application class
     */
    ProxyPanel(MainFrame mainFrame) {
        statusPane=new JTextPane();
        this.mainFrame=mainFrame;
        requestResponse=null;
        JPanel panel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
        // panel.setPreferredSize(new Dimension(50, 43));
        /////////////////////////////////////////////////////////////////////////////
        ipField = new JTextField();
        ipField.setText("ip");
        ipField.addFocusListener(new FocusListenerText(ipField, "ip"));
        ipField.setFont(new Font("Arial", Font.BOLD, 11));
        ipField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        ipField.setPreferredSize(new Dimension(190, 30));
        panel.add(ipField);
        /////////////////////////////////////////////////////////////////////////////
        portField = new JTextField();
        portField.setText("port");
        portField.addFocusListener(new FocusListenerText(portField, "port"));
        portField.setFont(new Font("Arial", Font.BOLD, 11));
        portField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        portField.setPreferredSize(new Dimension(190, 30));
        panel.add(portField);
        //////////////////////////////////////////////////////////////////////////////
        JLabel empty = new JLabel("             ");
        panel.add(empty);
        //////////////////////////////////////////////////////////////////////////////
        JButton sendToProxy = new JButton("Send to proxy");
        sendToProxy.addActionListener(new sendToProxy());
        sendToProxy.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        sendToProxy.setFont(new Font("Arial", Font.BOLD, 8));
        sendToProxy.setPreferredSize(new Dimension(70, 30));
        sendToProxy.setBackground(Color.RED);
        sendToProxy.setForeground(Color.RED);
        panel.add(sendToProxy);
        //////////////////////////////////////////////////////////////////////////////
        JLabel empty2 = new JLabel("      ");
        panel.add(empty2);
        /////////////////////////////////////////////////////////////////////////////
        showResponse = new JButton("SHOW RESPONSE");
        showResponse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    statusPane.setText(requestResponse.printResponse());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        showResponse.setBackground(Color.GREEN);
        showResponse.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        showResponse.setFont(new Font("Arial", Font.BOLD, 8));
        showResponse.setPreferredSize(new Dimension(90, 30));
        showResponse.setForeground(Color.GREEN);
        showResponse.setEnabled(false);
        panel.add(showResponse);
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
     *
     * @return ip field
     */
    public JTextField getIpField() {
        return ipField;
    }

    /**
     *
     * @param ipField set ip field
     */
    public void setIpField(JTextField ipField) {
        this.ipField = ipField;
    }

    /**
     *
     * @return port field
     */
    public JTextField getPortField() {
        return portField;
    }

    /**
     *
     * @param portField port field
     */
    public void setPortField(JTextField portField) {
        this.portField = portField;
    }

    /**
     * this action listener send a request to server and get back the answer.
     */
    private class sendToProxy implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProxyServer server = new ProxyServer(Integer.parseInt(portField.getText()),mainFrame);
            server.start();
            ProxyClient client=new ProxyClient(ipField.getText(),Integer.parseInt(portField.getText()),mainFrame.getPart2().getCurrentOpenRequest());
            requestResponse=client.run();
            if (requestResponse!=null)
                showResponse.setEnabled(true);
        }
    }
}
