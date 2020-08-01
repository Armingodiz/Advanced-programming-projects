package GUI;
/**
 * this claSS IS FOCUS LISTENER FOR TEXT IN APP THAT
 * PUT A DEFAULT TEXT IN A TEXT WE ADD THIS LISTENER TO
 * IT.
 * @AUTHOR ARMIN GOODARZI
 */

import javax.swing.*;
import java.awt.event.FocusEvent;

public class FocusListenerText implements java.awt.event.FocusListener {

  private   JTextField text;
  private   String defaultText;

    /**
     *
     * @param text THE TEXT we add listener to it
     * @param defaultText a default text to put in text when it is empty
     */
    FocusListenerText(JTextField text,String defaultText){
        this.text=text;
        this.defaultText=defaultText;
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
     *                   in text
     */
    @Override
    public void focusLost(FocusEvent focusEvent) {
        String p= text.getText().replaceAll("\\s", "");
        if (p.equals(""))
            text.setText(defaultText);
    }
}
