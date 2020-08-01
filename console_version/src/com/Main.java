package com;


import com.App.Application;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Application application = new Application();
        do {
            application.run();
        } while (true);
    }
}
