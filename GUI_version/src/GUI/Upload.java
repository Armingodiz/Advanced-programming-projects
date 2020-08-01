package GUI;
/**
 * this class is for creating and holding needed data for upload panel in part 2
 */

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Upload extends JPanel {
    private JTextField fileNAme;
    private JButton addFile;
    private JButton removeFile;
    private MainFrame mainFrame;

    /**
     * constructor
     * and also create upload panel and put needed components in it .
     * @param mainFrame main application
     */
    Upload(MainFrame mainFrame) {
        this.mainFrame=mainFrame;
        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(layout);
        fileNAme = new JTextField("NO FILE SELECTED ");
        fileNAme.setEditable(false);
        addFile = new JButton("Choose File ");
        addFile.addActionListener(new FileChooser());
        removeFile = new JButton("Reset File ");
        removeFile.addActionListener(new FileReseter());
        removeFile.setEnabled(false);


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.ipady = 20;
        gbc.ipadx = 20;
        gbc.gridwidth = 8;
        fileNAme.setBorder(BorderFactory.createTitledBorder("SELECTED FILE :"));
        panel.add(fileNAme, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 6;
        gbc.gridy = 1;
        gbc.ipady = 20;
        gbc.gridwidth = 1;
        panel.add(addFile, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.ipady = 20;
        gbc.gridwidth = 1;
        panel.add(removeFile, gbc);

        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.NORTH);
    }

    /**
     * this class is a action listener for choosing a file to upload it .
     */
    private class FileChooser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String actionCommand = actionEvent.getActionCommand();
            if (actionCommand.equals("save")) {
                // create an object of JFileChooser class
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                // invoke the showsSaveDialog function to show the save dialog
                int r = fileChooser.showSaveDialog(null);
                // if the user selects a file
                if (r == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected file
                    fileNAme.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    mainFrame.getPart2().getCurrentOpenRequest().setUploadedFile(new File(fileNAme.getText()));
                    mainFrame.getPart2().getCurrentOpenRequest().setBodyType("upload");
                    removeFile.setEnabled(true);
                }
            }

            // if the user presses the open dialog show the open dialog
            else {
                // create an object of JFileChooser class
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                // invoke the showsOpenDialog function to show the save dialog
                int r = fileChooser.showOpenDialog(null);

                // if the user selects a file
                if (r == JFileChooser.APPROVE_OPTION) {
                    // set the label to the path of the selected file
                    fileNAme.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    mainFrame.getPart2().getCurrentOpenRequest().setUploadedFile(new File(fileNAme.getText()));
                    mainFrame.getPart2().getCurrentOpenRequest().setBodyType("upload");
                    removeFile.setEnabled(true);
                }
            }
        }
    }

    /**
     * this action listener is for resetting the chosen file
     */
    private class FileReseter implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            fileNAme.setText("NO FILE SELECTED ");
            mainFrame.getPart2().getCurrentOpenRequest().setUploadedFile(null);
            removeFile.setEnabled(false);
        }
    }

    /**
     *
     * @return path of file
     */
    public JTextField getFileNAme() {
        return fileNAme;
    }

    /**
     *
     * @param fileNAme path of file
     */
    public void setFileNAme(JTextField fileNAme) {
        this.fileNAme = fileNAme;
    }

    /**
     *
     * @return remove button
     */
    public JButton getRemoveFile() {
        return removeFile;
    }

    /**
     *
     * @param removeFile remove button
     */
    public void setRemoveFile(JButton removeFile) {
        this.removeFile = removeFile;
    }
}
