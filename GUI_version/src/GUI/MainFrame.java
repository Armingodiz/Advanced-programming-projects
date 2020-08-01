package GUI;
/**
 * this class manage all tree parts of app
 * and connect them to each other and also
 * has a menubar for settings and some other
 * options .
 */

import ApplicationRunner.Request;
import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainFrame {
    private JFrame frame2;
    private JPanel part1;
    private JPanel part2;
    private JPanel part3;
    private JMenuItem exit;
    private ArrayList<JRadioButton> thems;
    private boolean systemTray;
    private boolean fullScreen;
    private boolean followRed;
    private String startingThem;
    private Part1 part11;
    private Part2 part22;
    private ResponsePanel response;

    /**
     * frame2 is the main frame that we will show it to user
     * this constructor creat 3 parts and put them in frame2
     * then add menubar and also add needed action listeners to
     * each item of menubar
     *
     * @throws IOException for opening and closing file
     */
    public MainFrame() throws IOException {
        /*
        making mainframe and adding three
        parts to it and also a menubar
         */
        frame2 = new JFrame();
        frame2.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    saveSettings();
                    getPart1().saveLists();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.exit(0);
            }
        });
        LoadSettings();//reading default settings from a text file
        part11 = new Part1(frame2, this);
        part22 = new Part2(frame2, this);
        response = new ResponsePanel(frame2, this);
        part1 = part11.getPanel();
        part2 = part22.getPanel();
        part3 = response.getResponse();
        JMenuBar menuBar = new JMenuBar();
        LafManager.install();
        part1.setBorder(BorderFactory.createTitledBorder(""));
        part2.setBorder(BorderFactory.createTitledBorder(""));
        part3.setBorder(BorderFactory.createTitledBorder(""));
        frame2.setLayout(new BorderLayout());
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, part1, part2);
        sp.setResizeWeight(0.2);
        sp.setDividerSize(1);
        JSplitPane sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, part3);
        sp2.setResizeWeight(0.8);
        sp2.setDividerSize(1);
        frame2.add(sp2, BorderLayout.CENTER);
        frame2.setJMenuBar(menuBar);
        /*
        settings and showing mainframe
        */
        frame2.setSize(300, 300);
        frame2.setLocation(100, 100);
        frame2.setVisible(true);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*
        adding menu to menubar
        */
        JMenu menu1 = new JMenu("Application");
        JMenu menu2 = new JMenu("Edit");
        JMenu menu3 = new JMenu("View");
        JMenu menu4 = new JMenu("Window");
        JMenu menu5 = new JMenu("Tools");
        JMenu menu6 = new JMenu("Help");
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);
        menuBar.add(menu4);
        menuBar.add(menu5);
        menuBar.add(menu6);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
        making two menu item for application menu
        */
        JMenuItem options = new JMenuItem("Options");
        options.setMnemonic(KeyEvent.VK_O);
        options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        JPopupMenu options1 = new JPopupMenu("Options");
        options1.add(getCheckBoxes());//adding a panel for options
        determineFirstTheme();//make current them item chosen
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                options1.show(frame2, 50, 50);
            }
        });
        exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            /*
            this action listener works based on system tray value
             */
            public void actionPerformed(ActionEvent actionEvent) {
                Test1 x;
                if (systemTray)
                    x = new Test1();
                else {
                    try {
                        saveSettings();
                        getPart1().saveLists();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
        menu1.add(options);
        menu1.add(exit);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        /*
        adding two menu item for view menu
        * */
        JCheckBox toggleFull = new JCheckBox("Toggle Full Screen");
        toggleFull.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (toggleFull.isSelected()) {
                    frame2.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    fullScreen = true;
                } else {
                    frame2.setExtendedState(JFrame.NORMAL);
                    fullScreen = false;
                }
            }
        });
        if (fullScreen)//for default setting
            toggleFull.doClick();
        JMenuItem toggleSidebar = new JMenuItem("Toggle Sidebar ");
        toggleSidebar.setMnemonic(KeyEvent.VK_T);
        toggleSidebar.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        toggleSidebar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (part1.isVisible()) {
                    part1.setVisible(false);
                } else {
                    sp.setDividerLocation(-2);
                    part1.setVisible(true);


                }
            }
        });
        menu3.add(toggleFull);
        menu3.add(toggleSidebar);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //adding help and about items to help menu
        JMenuItem help = new JMenuItem("Help");
        help.setMnemonic(KeyEvent.VK_H);
        help.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        JMenuItem about = new JMenuItem("About");
        about.setMnemonic(KeyEvent.VK_A);
        about.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                About about1 = new About();
            }
        });
        menu6.add(help);
        menu6.add(about);
    }     //end of constructor
//######################################################################################################################

    /**
     * @return systemTray value
     */
    public boolean isSystemTray() {
        return systemTray;
    }
//######################################################################################################################

    /**
     * @param systemTray systemTray value
     */
    public void setSystemTray(boolean systemTray) {
        this.systemTray = systemTray;
    }
//######################################################################################################################

    /**
     * this method put check boxes and JRADIOBUTTONS in a panel and add needed action listeners
     * then return that panel
     *
     * @return a panel full of check boxes for follow redirect and system tray and also JRadio buttons for thems
     */
    public JPanel getCheckBoxes() {
        JPanel checkboxes = new JPanel();
        checkboxes.setLayout(new GridLayout(7, 1));
        JCheckBox followRedirect = new JCheckBox("follow redirect ");
        followRedirect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JCheckBox cb = (JCheckBox) actionEvent.getSource();
                if (cb.isSelected()) {
                    followRed = true;
                } else {
                    followRed = false;
                }
            }
        });
        if (followRed)
            followRedirect.doClick();//for default setting
        checkboxes.add(followRedirect);


        JCheckBox systemTray = new JCheckBox("System Tray");
        systemTray.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JCheckBox cb = (JCheckBox) actionEvent.getSource();
                if (cb.isSelected()) {
                    setSystemTray(true);
                } else {
                    setSystemTray(false);
                }
            }
        });
        if (isSystemTray())
            systemTray.doClick();//for default setting
        checkboxes.add(systemTray);


        thems = new ArrayList<>();
        JRadioButton solarizedLight = new JRadioButton("Solarized Light");
        JRadioButton highContrastDark = new JRadioButton("HighContrast Dark Theme");
        JRadioButton highContrastLight = new JRadioButton("HighContrast Light");
        JRadioButton darcula = new JRadioButton("DARK");
        JRadioButton oneDark = new JRadioButton("NAVY BLUE");
        ButtonGroup bg = new ButtonGroup();
        thems.add(solarizedLight);
        thems.add(highContrastDark);
        thems.add(highContrastLight);
        thems.add(darcula);
        thems.add(oneDark);
        ThemesHandler themesHandler = new ThemesHandler();
        for (JRadioButton temp : thems) {
            bg.add(temp);
            checkboxes.add(temp);
            temp.addActionListener(themesHandler);//action for changing look and feel
        }
        return checkboxes;
    }
//######################################################################################################################

    /**
     * this class show a frame with information of author
     * of this application
     */
    private class About extends JFrame {
        About() {
            this.setLayout(new GridLayout(3, 1));
            JLabel nameAndLastName = new JLabel("Producer : Armin Godarzi");
            JLabel e_mail = new JLabel("G-Mail : armingodarzi@gmail.com");
            JLabel id = new JLabel("ID : 9831055");
            nameAndLastName.setBorder(BorderFactory.createTitledBorder(""));
            e_mail.setBorder(BorderFactory.createTitledBorder(""));
            id.setBorder(BorderFactory.createTitledBorder(""));
            this.add(nameAndLastName);
            this.add(e_mail);
            this.add(id);
            this.setVisible(true);
            this.setSize(200, 100);
            this.setLocation(300, 30);
        }
    }

    //##################################################################################################################

    /**
     * this class provide systemTray option for main frame
     * and add a action listener to exit item in application menu for
     * hiding frame in system Tray
     */
    private class Test1 {
        TrayIcon trayIcon;
        SystemTray tray;
        int trays;

        Test1() {
            trays = 0;
            System.out.println("creating instance");
            if (SystemTray.isSupported()) {
                System.out.println("system tray supported");
                tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().getImage("/media/faisal/DukeImg/Duke256.png");
                ActionListener exitListener = new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Exiting....");
                        System.exit(0);
                    }
                };
                PopupMenu popup = new PopupMenu();
                MenuItem defaultItem = new MenuItem("Exit");
                defaultItem.addActionListener(exitListener);
                popup.add(defaultItem);
                defaultItem = new MenuItem("Open");
                defaultItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        frame2.setVisible(true);
                        frame2.setExtendedState(JFrame.NORMAL);
                    }
                });

                popup.add(defaultItem);

                trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
                trayIcon.setImageAutoSize(true);

            } else {
                System.out.println("system tray not supported");
            }

            exit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        if (trays == 0) {
                            tray.add(trayIcon);
                            trays++;
                        }
                        frame2.setVisible(false);
                        System.out.println("added to SystemTray");
                    } catch (AWTException ex) {
                        System.out.println("unable to add to tray");
                    }
                }
            });

            frame2.setIconImage(Toolkit.getDefaultToolkit().getImage("Duke256.png"));
        }
    }

    //##################################################################################################################

    /**
     * this class provide a action listener for changing them
     * which change the app them based on chosen them
     * in option popup menu
     */
    public class ThemesHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String them = actionEvent.getActionCommand();
            startingThem = them;
            if (them.equals("HighContrast Dark Theme"))
                LafManager.install(new HighContrastDarkTheme());
            else if (them.equals("HighContrast Light"))
                LafManager.install(new HighContrastLightTheme());
            else if (them.equals("DARK"))
                LafManager.install(new DarculaTheme());
            else if (them.equals("NAVY BLUE"))
                LafManager.install(new OneDarkTheme());
            else
                LafManager.install(new SolarizedLightTheme());
        }
    }

    //##################################################################################################################
//getter methods for 3 parts of mainframe

    /**
     * @return part 1 panel
     */
    public Part1 getPart1() {
        return part11;
    }

    /**
     * @return part 2
     */
    public Part2 getPart2() {
        return part22;
    }

    /**
     * @return part 3
     */
    public ResponsePanel getPart3() {
        return response;
    }

    //##################################################################################################################

    /**
     * reading needed information from a file
     *
     * @throws IOException for file
     */
    public void LoadSettings() throws IOException {
        File startingSettings = new File("startingSettings.txt");
        FileInputStream inputStream = new FileInputStream(startingSettings);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String current;
        int currentLine = 1;
        while ((current = reader.readLine()) != null) {
            switch (currentLine) {
                case 1:
                    if (current.equals("true"))
                        systemTray = true;
                    else
                        systemTray = false;
                    break;
                case 2:
                    startingThem = current;
                    break;
                case 3:
                    if (current.equals("true"))
                        followRed = true;
                    else
                        followRed = false;
                    break;
                default:
                    if (current.equals("true"))
                        fullScreen = true;
                    else
                        fullScreen = false;
                    break;
            }
            currentLine++;
        }
        //setting starting them based on starting setting
        String them = startingThem;
        if (them.equals("HighContrast Dark Theme"))
            LafManager.install(new HighContrastDarkTheme());
        else if (them.equals("HighContrast Light"))
            LafManager.install(new HighContrastLightTheme());
        else if (them.equals("DARK"))
            LafManager.install(new DarculaTheme());
        else if (them.equals("NAVY BLUE"))
            LafManager.install(new OneDarkTheme());
        else
            LafManager.install(new SolarizedLightTheme());
    }

    //##################################################################################################################

    /**
     * this class set starting them checkbox selected
     */
    public void determineFirstTheme() {
        for (JRadioButton temp : thems) {
            if (temp.getText().equals(startingThem))
                temp.setSelected(true);
        }
    }

    //##################################################################################################################

    /**
     * this method first empty the starting settings file
     * then write current settings to file .
     *
     * @throws IOException for file
     */
    public void saveSettings() throws IOException {
        File startingSettings = new File("startingSettings.txt");
        PrintWriter wr = new PrintWriter(startingSettings);
        wr.close();
        FileOutputStream input = new FileOutputStream(startingSettings);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(input));

        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0:
                    writer.write(String.valueOf(systemTray));
                    break;
                case 1:
                    writer.write(startingThem);
                    break;
                case 2:
                    writer.write(String.valueOf(followRed));
                    break;
                case 3:
                    writer.write(String.valueOf(fullScreen));
                    break;
            }
            writer.newLine();
        }
        writer.close();
    }

    /**
     * @return is application follow redirect or no
     */
    public boolean isFollowRed() {
        return followRed;
    }

    /**
     * @param followRed is application follow redirect or no
     */
    public void setFollowRed(boolean followRed) {
        this.followRed = followRed;
    }

    /**
     *
     * @return frame
     */
    public JFrame getFrame2() {
        return frame2;
    }
}
