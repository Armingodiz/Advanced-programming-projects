package GUI;
/**
 * this class shows a new frame for creating new request
 * take needed information from user then create the request
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class newRequestFrame {
    private ArrayList<String> customs;//array list of types that user has been created them
    private String type;//type of request
    private String name;//name of request
    private String bodyType;//type of body for request
    private JFrame newRequset;//frame that we will show it
    private JTextField nameField;
    private JMenuBar menuBar;//for types of request
    private JMenu types;
    private JMenuBar body;//for types to body
    private JMenu bodies;
    private JButton create;
    private JPanel up;
    private int output;
    private int clicked;

    /**
     * @param customs array list of types that user has been created them
     */
    public newRequestFrame(ArrayList<String> customs) {
        this.customs = customs;
        output = 0;
        clicked = 0;
    }

    /**
     * @return a frame with needed panels for getting needed information from user
     */
    public JFrame addNewRequestFrame() {
        //putting default information and creating frame ,nameField and menubar
        type = "GET";
        name = "My Request";
        bodyType = "NO BODY";
        newRequset = new JFrame("New Request");
        nameField = new JTextField(name);
        menuBar = new JMenuBar();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding menubar for choosing type of request
        ArrayList<JMenuItem> items = new ArrayList<JMenuItem>();
        types = new JMenu(type + "                       \u25BC");
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
            types.add(temp);
            temp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    if (temp.getText().equals("CUSTOM METHOD")) {
                        newRequset.setVisible(false);
                        nameField.setPreferredSize(new Dimension(450, 60));
                        up.add(body);
                        newType(types, customs, newRequset, 1);//new frame for creating custom type
                    } else {
                        if (temp.getText().contains("P")) {
                            nameField.setPreferredSize(new Dimension(450, 60));
                            up.add(body);//adding body type menubar for needed types
                        }
                        String space = "";
                        for (int i = 0; i < 19 - temp.getText().length(); i++)
                            space = space + " ";
                        types.setText(temp.getText() + space + "\u25BC");//replacing chosen type for menu name
                        type = temp.getText();
                    }
                }
            });
        }
        menuBar.add(types);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //creating bodies menubar for needed types
        body = new JMenuBar();
        body.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        body.setPreferredSize(new Dimension(120, 55));
        bodies = new JMenu("  No Body" + "            \u25BC");
        ArrayList<JMenuItem> bodyTypes = new ArrayList<>();
        JMenuItem json = new JMenuItem("json");
        bodyTypes.add(json);
        JMenuItem form = new JMenuItem("formData");
        bodyTypes.add(form);
        JMenuItem upload = new JMenuItem("upload");
        bodyTypes.add(upload);
        for (JMenuItem temp : bodyTypes) {
            bodies.add(temp);
            temp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    bodies.setText(temp.getText() + "                   \u25BC");//replacing name of bodies with chosen type
                    bodyType = temp.getText();
                }
            });
        }
        body.add(bodies);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding creat button
        create = new JButton("Create");
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                output = 1;
                newRequset.dispose();
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //frame setting
        newRequset.setLayout(new BorderLayout());
        newRequset.add(cratePanel(), BorderLayout.CENTER);
        newRequset.setVisible(true);
        newRequset.setSize(800, 200);
        newRequset.setLocation(200, 200);
        // newRequset.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return newRequset;
    }

    //##################################################################################################################
    //getter methods

    /**
     * @return type of new request
     */
    public String getType() {
        return type;
    }

    /**
     * @return name of new request
     */
    public String getName() {
        return name;
    }

    /**
     * @return type of body for new request
     */
    public String getBodyType() {
        return bodyType;
    }

    /**
     * @return creat button
     */
    public JButton getCreate() {
        return create;
    }

    /**
     * @return array list of custom types that user has been created
     */
    public ArrayList<String> getCustoms() {
        return customs;
    }

    //##################################################################################################################

    /**
     * this class put needed components for getting information in a panel
     * and then return it
     *
     * @return a panel that we add it to frame for getting information
     */
    public JPanel cratePanel() {
        JPanel panel = new JPanel();
        up = new JPanel();
        JPanel down = new JPanel();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(50, 43));
        nameField.setBorder(BorderFactory.createTitledBorder("Name"));
        nameField.setPreferredSize(new Dimension(580, 60));
        menuBar.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
        menuBar.setPreferredSize(new Dimension(120, 55));
        up.setLayout(layout);
        up.add(nameField);
        up.add(menuBar);
        up.setBorder(BorderFactory.createTitledBorder(""));

        JTextField tip = new JTextField("*Tip: paste Curl command into URL afterwards to import it");
        tip.setEditable(false);
        FlowLayout layout1 = new FlowLayout(FlowLayout.LEFT);
        JLabel empty = new JLabel("                                                                                                          ");
        //empty.setEditable(false);
        layout1.setHgap(2);
        down.setLayout(layout1);
        down.add(tip);
        down.add(empty);
        down.add(create);
        down.setBorder(BorderFactory.createTitledBorder(""));
        panel.add(up);
        panel.add(down);
        return panel;
    }

    //##################################################################################################################

    /**
     * this class show a new frame for user to make a new custom type
     * we give this method a frame to open it whe we close this frame
     *
     * @param inputTypes      menu of types
     * @param inputCustoms    array list of types that user already created
     * @param inputNewRequest new request frame
     * @param part            this show in which part we are creating new type
     */
    public void newType(JMenu inputTypes, ArrayList<String> inputCustoms, JFrame inputNewRequest, int part) {
        JPanel newUp = new JPanel();
        JPanel newDown = new JPanel();
        JFrame custom = new JFrame("HTTP Method");
        custom.setLayout(new BorderLayout());
        JPanel newCustom = new JPanel();
        custom.add(newCustom, BorderLayout.CENTER);
        JTextField newName = new JTextField("GET");
        newName.addFocusListener(new FocusListenerText(newName, "GET"));
        JTextField guid = new JTextField("*Common examples are LINK, UNLINK, FIND, PURGE");
        guid.setEditable(false);
        JButton done = new JButton("DONE");
        done.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                inputTypes.setText(newName.getText());
                type = newName.getText();
                custom.dispose();
                if (part == 1)
                    inputNewRequest.setVisible(true);
            }
        });
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT);
        layout.setHgap(10);
        newCustom.setLayout(layout);
        newCustom.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        newCustom.setPreferredSize(new Dimension(50, 43));
        newCustom.setLayout(layout);
        newUp.setLayout(layout);
        newUp.setBorder(BorderFactory.createTitledBorder(""));

        newName.setBorder(BorderFactory.createTitledBorder("Name"));
        newName.setPreferredSize(new Dimension(580, 60));
        newUp.add(newName);
        newCustom.add(newUp);

        if (inputCustoms.size() != 0) {
            custom.setSize(660, 300);
            JPanel middle = new JPanel();
            FlowLayout layout1 = new FlowLayout(FlowLayout.LEFT);
            layout1.setHgap(1);
            middle.setLayout(layout1);
            //middle.setBorder(BorderFactory.createTitledBorder(""));
            for (String temp : inputCustoms) {
                JPanel x = new JPanel();
                FlowLayout layout2 = new FlowLayout(FlowLayout.LEFT);
                layout2.setHgap(0);
                x.setLayout(layout2);
                JButton x1 = new JButton(temp);
                JButton x2 = new JButton("-");
                x1.setFont(new Font("Arial", Font.BOLD, 10));
                x1.setFont(new Font("Arial", Font.BOLD, 10));
                x.setPreferredSize(new Dimension(120, 50));
                x1.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        inputTypes.setText(temp);
                        newName.setText(temp);
                        type = temp;
                    }
                });
                x2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        clicked++;
                        if (clicked % 2 == 0) {
                            x.setVisible(false);
                        } else
                            x2.setText("!");
                    }
                });
                x1.setPreferredSize(new Dimension(65, 40));
                x2.setPreferredSize(new Dimension(30, 40));
                x.add(x1);
                x.add(x2);
                middle.add(x);
            }
            newCustom.add(middle);
        } else
            custom.setSize(660, 200);

        FlowLayout layout1 = new FlowLayout(FlowLayout.LEFT);
        JLabel empty2 = new JLabel("                                                                        ");
        layout1.setHgap(2);
        newDown.setLayout(layout1);
        newDown.add(guid);
        newDown.add(empty2);
        newDown.add(done);
        newDown.setBorder(BorderFactory.createTitledBorder(""));
        newCustom.add(newDown);

        custom.setVisible(true);
        custom.setLocation(300, 300);
    }

    /**
     *
     * @return name Field
     */
    public JTextField getNameField() {
        return nameField;
    }
}
