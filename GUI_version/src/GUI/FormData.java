package GUI;
/**
 * this class creates and hold information of a panel
 * that handel adding form data informations
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class FormData extends JPanel {
    private MainFrame mainFrame;
    private JPanel formDataPanel;
    private JButton addBut3;

    /**
     * constructor that create the panel
     * and fill it with needed components
     *
     * @param mainFrame
     */
    FormData(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        formDataPanel = new JPanel();
        BoxLayout boxlayout2 = new BoxLayout(formDataPanel, BoxLayout.Y_AXIS);
        formDataPanel.setLayout(boxlayout2);
        JPanel temp4 = new JPanel();
        temp4.setLayout(new GridLayout(1, 1));
        addBut3 = new JButton("ADD HEADER");
        temp4.add(addBut3);
        formDataPanel.add(temp4);
        addBut3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                formDataPanel.remove(temp4);
                JPanel temp3 = mainFrame.getPart2().addHeader(1, true, "", "", true);
                mainFrame.getPart2().addFormDataPanel(temp3);
                formDataPanel.add(temp3);
                formDataPanel.add(temp4);
            }
        });
        this.setLayout(new BorderLayout());
        this.add(formDataPanel, BorderLayout.NORTH);
    }

    /**
     * @return form data panel
     */
    public JPanel getFormDataPanel() {
        return formDataPanel;
    }

    /**
     * @return add button
     */
    public JButton getAddBut3() {
        return addBut3;
    }

    /**
     * @param addBut3 add button
     */
    public void setAddBut3(JButton addBut3) {
        this.addBut3 = addBut3;
    }
}
