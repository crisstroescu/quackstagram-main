package Quackstagram;
import javax.swing.*;
import java.awt.*;


public class Header extends JPanel {
    private static final int WIDTH = 300;
    
    public Header(String name) {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        setBackground(new Color(51, 51, 51)); // Set a darker background for the header
        JLabel textLabel = new JLabel(name);
        textLabel.setFont(new Font("Arial", Font.BOLD, 16));
        textLabel.setForeground(Color.WHITE); // Set the text color to white
        add(textLabel);
        setPreferredSize(new Dimension(WIDTH, 40)); // Give the header a fixed height
    }
}
