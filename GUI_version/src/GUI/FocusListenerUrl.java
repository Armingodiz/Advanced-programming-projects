package GUI;
/**
 * this claSS IS FOCUS LISTENER FOR URL TEXT IN APP THAT
 * PUT A DEFAULT TEXT IN A TEXT WE ADD THIS LISTENER TO
 * IT.and it replace inputed new text to url text in a
 * change url text.
 *
 * @AUTHOR ARMIN GOODARZI
 */

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FocusListenerUrl implements FocusListener {
    private JTextField change;
    private JTextField text;
    private String defaultText;

    /**
     *
     * @param text the text we add listener to it
     * @param defaultText a default text to put it when text is empty
     * @param change the text we want to change it when we change url text
     */
    FocusListenerUrl(JTextField text, String defaultText, JTextField change) {
        this.text = text;
        this.defaultText = defaultText;
        this.change = change;
    }

    /**
     *
     * @param focusEvent when it is focused
     *                   this class check and if our text had a default text
     *                   when we focus on it
     *                   it will make the text empty
     */
    @Override
    public void focusGained(FocusEvent focusEvent) {
        if (text.getText().equals(defaultText))
            text.setText("");
    }

    /**
     *
     * @param focusEvent when it is not focused
     *                   this class check and if text was empty
     *                   after loosing focus it will put a default text
     *                   in text and if not replace change text contents
     */
    @Override
    public void focusLost(FocusEvent focusEvent) {
        String p = text.getText().replaceAll("\\s", "");
        if (p.equals(""))
            text.setText(defaultText);
        else
            change.setText(text.getText());
    }
}
