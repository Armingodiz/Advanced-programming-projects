package GUI;
/**
 * this class is a action listener for comboBox in Response in part3
 * it put a panel based on chosen body Type from combo box
 *
 * @AUTHOR ARMIN GOODARZI
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResponseTypeChoosing implements ActionListener {
    private JPanel body;
    private JScrollPane raw;
    private JPanel preview;
    private JComboBox combo;

    /**
     * @param body    the main panel
     * @param raw     raw type
     * @param preview preview type
     * @param combo   a comboBox to add to panel
     */
    ResponseTypeChoosing(JPanel body, JScrollPane raw, JPanel preview, JComboBox combo) {
        this.body = body;
        this.raw = raw;
        this.preview = preview;
        this.combo = combo;
        preview.add(new JLabel("preview"));
        body.add(raw, BorderLayout.CENTER);
    }

    /**
     * this method will replace chosen bodyType with current one
     *
     * @param actionEvent choosing a body type from combo box
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        body.remove(1);
        body.setVisible(false);
        if (combo.getItemAt(combo.getSelectedIndex()).equals("Raw")) {
            body.add(raw, BorderLayout.CENTER);
        } else
            body.add(preview, BorderLayout.CENTER);
        body.setVisible(true);
    }
}
