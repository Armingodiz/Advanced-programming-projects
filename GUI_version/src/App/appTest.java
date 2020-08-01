package App;

import GUI.MainFrame;

import java.io.IOException;

public class appTest {
    public static void main(String[] args) throws IOException {
        // write your code here
        try {
            MainFrame mainFrame=new MainFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
