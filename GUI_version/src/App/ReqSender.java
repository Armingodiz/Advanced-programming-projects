package App;
/**
 * this class is to work with swing worker and send requests
 */

import ApplicationRunner.Request;
import ApplicationRunner.RequestResponse;
import ApplicationRunner.RequestSender;
import GUI.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ReqSender extends SwingWorker<String, Object> {
    JTextPane responseText;
    JPanel preview;
    Request request;
    MainFrame mainFrame;
    RequestResponse requestResponse;

    /**
     *
     * @param responseText a pane to put string form of response in it
     * @param preview a panel to show picks and html pages in
     * @param request request we want to send
     * @param mainFrame application class
     */
    public ReqSender(JTextPane responseText, JPanel preview, Request request, MainFrame mainFrame) {
        this.responseText = responseText;
        this.preview = preview;
        this.request = request;
        this.mainFrame = mainFrame;
    }

    /**
     *
     * @return nothing
     * @throws Exception for connection stuff
     */
    @Override
    protected String doInBackground() throws Exception {
        RequestSender requestSender = new RequestSender(mainFrame);
        requestSender.setRequest(request);
        requestSender.run();
        for (int i = 0; i < 3; i++) {
            //Thread.sleep(1000);
            //  JLabel imgLabel = new JLabel(new ImageIcon("farzad.jpg"));
            // preview.add(imgLabel);
        }
        return null;
    }


}
