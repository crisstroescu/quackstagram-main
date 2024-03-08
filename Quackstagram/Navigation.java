package Quackstagram;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import liking_images.QuakstagramHomeUI;
import signup_and_signin.User;


public class Navigation extends JPanel {
    private static final int NAV_ICON_SIZE = 20; // Corrected static size for bottom icons

    public Navigation(JFrame frame) {
        setBackground(new Color(249, 249, 249));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        add(createIconButton("resources/img/icons/home.png", "home", frame));
        add(Box.createHorizontalGlue());
        add(createIconButton("resources/img/icons/search.png","explore", frame));
        add(Box.createHorizontalGlue());
        add(createIconButton("resources/img/icons/add.png","add", frame));
        add(Box.createHorizontalGlue());
        add(createIconButton("resources/img/icons/heart.png","notification", frame));
        add(Box.createHorizontalGlue());
        add(createIconButton("resources/img/icons/profile.png", "profile", frame));
    }

    private JButton createIconButton(String iconPath, String buttonType, JFrame frame) {
        ImageIcon iconOriginal = new ImageIcon(iconPath);
        Image iconScaled = iconOriginal.getImage().getScaledInstance(NAV_ICON_SIZE, NAV_ICON_SIZE, Image.SCALE_SMOOTH);
        JButton button = new JButton(new ImageIcon(iconScaled));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setContentAreaFilled(false);
 
        // Define actions based on button type
        if ("home".equals(buttonType)) {
            button.addActionListener(e -> openHomeUI(frame));
        } else if ("profile".equals(buttonType)) {
            button.addActionListener(e -> openProfileUI(frame));
        } else if ("notification".equals(buttonType)) {
            button.addActionListener(e -> notificationsUI(frame));
        } else if ("explore".equals(buttonType)) {
            button.addActionListener(e -> exploreUI(frame));
        } else if ("add".equals(buttonType)) {
            button.addActionListener(e -> ImageUploadUI(frame));
        }
        return button;       
    }

    private void notificationsUI(JFrame frame) {
        // Open InstagramProfileUI frame
        frame.dispose();
        NotificationsUI notificationsUI = new NotificationsUI();
        notificationsUI.setVisible(true);
    }

    private void ImageUploadUI(JFrame frame) {
        // Open InstagramProfileUI frame
        frame.dispose();
        ImageUploadUI upload = new ImageUploadUI();
        upload.setVisible(true);
    }
 
    private void openHomeUI(JFrame frame) {
        // Open InstagramProfileUI frame
        frame.dispose();
        String loggedInUsername = "";
 
        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User(loggedInUsername);
        QuakstagramHomeUI homeUI = new QuakstagramHomeUI(user);
        homeUI.setVisible(true);
    }
 
    private void exploreUI(JFrame frame) {
        // Open InstagramProfileUI frame
        frame.dispose();
        ExploreUI explore = new ExploreUI();
        explore.setVisible(true);
    }

    private void openProfileUI(JFrame frame) {
        // Open InstagramProfileUI frame
        frame.dispose();
        String loggedInUsername = "";
 
        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = new User(loggedInUsername);
        InstagramProfileUI profileUI = new InstagramProfileUI(user);
        profileUI.setVisible(true);
    }
}
