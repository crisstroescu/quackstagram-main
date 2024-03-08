package Quackstagram;
import javax.swing.*;

public abstract class UI extends JFrame {
    //magic numbers -> give clear names for the variables
    public static final int WIDTH = 300;
    public static final int HEIGHT = 500;

    public abstract void initializeUI();
}
