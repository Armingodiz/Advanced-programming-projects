package GUI;

import ApplicationRunner.Request;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * this class is for holding and showing each
 * response information.
 *
 * @author Armin Goodarzi
 */
public class ResponseInformation {
    private JPanel panel;
    private JTextField status_code;
    private JTextField time;
    private JTextField data;
    private MainFrame mainFrame;
    private ArrayList<Request> requests;

    /**
     * creating a new panel and put needed components to
     * show information of each response
     */
    ResponseInformation(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        panel = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
        panel.setPreferredSize(new Dimension(50, 43));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Color color23 = new Color(224, 221, 76);
        status_code = new JTextField("status code");
        status_code.setFocusable(false);
        status_code.setBackground(color23);
        status_code.setForeground(Color.WHITE);
        status_code.setFont(new Font("Arial", Font.BOLD, 11));
        status_code.setHorizontalAlignment(SwingConstants.CENTER);
        status_code.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        status_code.setPreferredSize(new Dimension(status_code.getText().length() * 15, 30));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Color color22 = new Color(224, 12, 115);
        time = new JTextField("Time 0");
        time.setFocusable(false);
        time.setBackground(color22);
        time.setForeground(Color.WHITE);
        time.setFont(new Font("Arial", Font.BOLD, 11));
        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        time.setPreferredSize(new Dimension(time.getText().length() * 20, 30));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        data = new JTextField("data 0kb");
        data.setFocusable(false);
        data.setBackground(Color.BLUE);
        data.setForeground(Color.WHITE);
        data.setFont(new Font("Arial", Font.BOLD, 11));
        data.setHorizontalAlignment(SwingConstants.CENTER);
        data.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        data.setPreferredSize(new Dimension(data.getText().length() * 10, 30));
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("save" + "\u25BC");
        JMenuItem saveResponse = new JMenuItem("Save Response of request .");
        JMenuItem saveRequest = new JMenuItem("Save request .");
        JMenuItem show = new JMenuItem("Show list .");
        saveResponse.addActionListener(new SaveWindow());
        saveRequest.addActionListener(new SaveRequestAction());
        show.addActionListener(new Show());
        menu.add(saveResponse);
        menu.add(saveRequest);
        menu.add(show);
        menuBar.add(menu);
        menuBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        JLabel space = new JLabel("                    ");
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        panel.add(status_code);
        panel.add(time);
        panel.add(data);
        panel.add(space);
        panel.add(menuBar);
    }
//##################################################################################################################

    /**
     * this method put created panel
     * in north of another panel then
     * return that.
     *
     * @return panel to show information
     */
    public JPanel getPanel() {
        JPanel output = new JPanel();
        output.setLayout(new BorderLayout());
        output.add(panel, BorderLayout.NORTH);
        return output;
    }

    /**
     * @return data show field
     */
    public JTextField getData() {
        return data;
    }

    /**
     * @param data show field
     */
    public void setData(JTextField data) {
        this.data = data;
    }

    /**
     * @return time show field
     */
    public JTextField getTime() {
        return time;
    }

    /**
     * @param time time show field
     */
    public void setTime(JTextField time) {
        this.time = time;
    }

    /**
     * @return response message show field
     */
    public JTextField getStatus_code() {
        return status_code;
    }

    /**
     * @param status_code response message show field
     */
    public void setStatus_code(JTextField status_code) {
        this.status_code = status_code;
    }

    /**
     * this class is a action listener for saving request response
     */
    private class SaveAction implements ActionListener {
        JTextField nameOFFile;
        JDialog window;

        SaveAction(JDialog window, JTextField name) {
            this.nameOFFile = name;
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("####################################################################################################" +
                    "##############################################################################################################" +
                    "################################################################################################################");
            File file = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\savedResponses\\" + nameOFFile.getText());
            mainFrame.getPart2().getCurrentOpenRequest().getResponseOutput().renameTo(file);
            window.dispose();
        }
    }

    /**
     * this class is for showing a jdialog to chose a name for file we want to save response of request in it
     */
    private class SaveWindow implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JDialog getFilename = new JDialog(mainFrame.getFrame2(), "SAVE IN :");
            JTextField name = new JTextField("Name");
            name.addFocusListener(new FocusListenerText(name, "NAME"));
            JButton save = new JButton("Save");
            save.addActionListener(new SaveAction(getFilename, name));
            JPanel fileChoser = new JPanel();
            BoxLayout boxlayout2 = new BoxLayout(fileChoser, BoxLayout.Y_AXIS);
            JPanel empty = new JPanel();
            FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
            layout.setHgap(0);
            empty.setLayout(layout);
            JLabel emp = new JLabel("                                                                                                         ");
            empty.add(emp);
            empty.add(save);
            fileChoser.setLayout(boxlayout2);
            fileChoser.add(name);
            fileChoser.add(empty);
            getFilename.add(fileChoser);
            getFilename.setSize(400, 100);
            getFilename.setLocation(1100, 100);
            getFilename.setVisible(true);
        }
    }

    /**
     * this class is a action listener for saving current open request in a file
     */
    private class SaveRequestAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String fileName = "";
            if (mainFrame.getPart2().getCurrentOpenRequest().getName().equals("default")) {
                Random random = new Random();
                fileName = "request" + random.nextInt(10000);
            } else
                fileName = mainFrame.getPart2().getCurrentOpenRequest().getName();
            File file = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\saveedReqs\\" + fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(mainFrame.getPart2().getCurrentOpenRequest());
                objectOutputStream.close();
                File nameReq = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\saveedReqs\\nameOfRequests.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(nameReq, true));
                writer.newLine();
                writer.write(file.getPath());
                writer.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * this class is a action listener to show saved requests in console
     */
    private class Show implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            File file = new File("C:\\Users\\User\\Desktop\\mian term\\phase 4\\GUI_version\\saveedReqs\\nameOfRequests.txt");
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String name = "";
                while ((name = reader.readLine()) != null) {
                    FileInputStream fileIn = new FileInputStream(new File(name));
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    Request request;
                    request = (Request) in.readObject();
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
                    request.printRequest();
                    System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
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
    }

}

