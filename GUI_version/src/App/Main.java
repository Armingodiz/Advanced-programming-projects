package App;

import GUI.MainFrame;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        try {
            MainFrame mainFrame=new MainFrame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
