package main.java.application;

import main.java.panel.LargePanel;
import main.java.panel.SmallPanel;

public class Application {

    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 600;

    public static void main(String[] args) {

        LargePanel mainPanel = new LargePanel(CANVAS_WIDTH, CANVAS_HEIGHT);
        SmallPanel subPanel = new SmallPanel(CANVAS_WIDTH, CANVAS_HEIGHT/2);
    }


}
