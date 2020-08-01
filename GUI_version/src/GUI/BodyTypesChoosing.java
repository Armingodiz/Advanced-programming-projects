package GUI;
/**
 * this class is a action listener for comboBox in bodyTap in part2
 * it put a panel based on chosen body Type from combo box
 *
 * @AUTHOR ARMIN GOODARZI
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BodyTypesChoosing implements ActionListener {
    private JPanel body;
    private JScrollPane json;
    private JPanel formData;
    private JPanel upload;
    private JComboBox combo;
    private MainFrame mainFrame;

    /**
     * @param body     it is body tab
     * @param json     a scroll panel with text line number for json body type
     * @param formData a panel for form data body type
     * @param upload   a panel for upload body type
     * @param combo    a comboBox for choosing body Type
     */
    BodyTypesChoosing(JPanel body, JScrollPane json, JPanel formData, JPanel upload, JComboBox combo, MainFrame mainFrame) {
        this.body = body;
        this.json = json;
        this.formData = formData;
        this.upload = upload;
        this.combo = combo;
        body.add(json, BorderLayout.CENTER);//default bodyType
        this.mainFrame = mainFrame;
    }

    /**
     * @param actionEvent chosen bodyType
     *                    this method put a body type based on chosen item in comboBox
     *                    and put it in body tab panel
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        body.remove(1);//removing current body
        body.setVisible(false);
        if (combo.getItemAt(combo.getSelectedIndex()).equals("json")) {
            body.add(json, BorderLayout.CENTER);
            mainFrame.getPart2().getCurrentOpenRequest().setBodyType("json");
        } else if (combo.getItemAt(combo.getSelectedIndex()).equals("formData")) {
            mainFrame.getPart2().getCurrentOpenRequest().setBodyType("formData");
            body.add(formData, BorderLayout.CENTER);
        } else {
            body.add(upload, BorderLayout.CENTER);
            mainFrame.getPart2().getCurrentOpenRequest().setBodyType("upload");
        }
        body.setVisible(true);
    }
}