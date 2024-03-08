package signup_and_signin;
import javax.swing.*;

import Quackstagram.Header;
import Quackstagram.InstagramProfileUI;
import Quackstagram.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class SignInUI extends UI {

    //REF added more constants
    private static final Color SIGN_IN_BUTTON_COLOR = new Color(255, 90, 95);
    private static final Color BUTTON_TEXT_COLOR = Color.BLACK;
    private static final String DEFAULT_USERNAME = "Username";
    private static final String DEFAULT_PASSWORD = "Password";
    private static final String CREDENTIALS_FILE_PATH = "resources/data/credentials.txt";
    private static final String USERS_FILE_PATH = "resources/data/users.txt";

    private JTextField txtUsername;
    private JTextField txtPassword;
    private JButton btnSignIn, btnRegisterNow;
    private JLabel lblPhoto;
    private User newUser;
    

    public SignInUI() {
        setTitle("Quackstagram - Register");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        initializeUI();
    }

    //REF
    public void initializeUI() {
        // Header with the Register label
        JPanel headerPanel = new Header("Quackstagram 🐥");
        JPanel fieldsPanel = createTextFieldsPanel();
        JPanel registerPanel = createRegisterPanel();
        JPanel buttonPanel = createButtonPanel();

        // Adding components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(registerPanel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createPicturePanel() {
        lblPhoto = new JLabel();
        lblPhoto.setPreferredSize(new Dimension(80, 80));
        lblPhoto.setHorizontalAlignment(JLabel.CENTER);
        lblPhoto.setVerticalAlignment(JLabel.CENTER);
        lblPhoto.setIcon(new ImageIcon(new ImageIcon("resources/img/logos/DACS.png").getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        JPanel photoPanel = new JPanel(); // Use a panel to center the photo label
        photoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);
        return photoPanel;
    }

    private JPanel createTextFieldsPanel() {
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = new JTextField(DEFAULT_USERNAME);
        txtPassword = new JTextField(DEFAULT_PASSWORD);
        txtUsername.setForeground(Color.GRAY);
        txtPassword.setForeground(Color.GRAY);

        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(createPicturePanel());
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(Box.createVerticalStrut(10));
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(Box.createVerticalStrut(10));

        return fieldsPanel;
    }
    
    private JPanel createRegisterPanel() {
        btnSignIn = new JButton("Sign-In");
        btnSignIn.addActionListener(this::onSignInClicked);
        btnSignIn.setBackground(SIGN_IN_BUTTON_COLOR);
        btnSignIn.setForeground(BUTTON_TEXT_COLOR);
        btnSignIn.setFocusPainted(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFont(new Font("Arial", Font.BOLD, 14));
        JPanel registerPanel = new JPanel(new BorderLayout()); // Panel to contain the register button
        registerPanel.setBackground(Color.WHITE); // Background for the panel
        registerPanel.add(btnSignIn, BorderLayout.CENTER);

        return registerPanel;
    }

    private JPanel createButtonPanel() {
        btnRegisterNow = new JButton("No Account? Register Now");
        btnRegisterNow.addActionListener(this::onRegisterNowClicked);
        btnRegisterNow.setBackground(Color.WHITE); // Set a different color for distinction
        btnRegisterNow.setForeground(Color.BLACK);
        btnRegisterNow.setFocusPainted(false);
        btnRegisterNow.setBorderPainted(false);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // Grid layout with 1 row, 2 columns
        buttonPanel.setBackground(Color.white);
        buttonPanel.add(btnSignIn);
        buttonPanel.add(btnRegisterNow);

        return buttonPanel;
    }

    //REF
   private void onSignInClicked(ActionEvent event) {
        String enteredUsername = txtUsername.getText();
        String enteredPassword = txtPassword.getText();
        System.out.println(enteredUsername+" <-> "+enteredPassword);
        if (verifyCredentials(enteredUsername, enteredPassword)) {
            System.out.println("It worked");
            closeFrame();
            openFrame(new InstagramProfileUI(newUser));
        } else {
            System.out.println("It Didn't");
        }
    }

    //REF
    private void onRegisterNowClicked(ActionEvent event) {
        closeFrame();
        openFrame(new SignUpUI());
    }

    private boolean verifyCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(":");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                String bio = credentials[2];
                // Create User object and save information
            newUser = new User(username, bio, password); // Assuming User constructor takes these parameters
            saveUserInformation(newUser);
        
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

   private void saveUserInformation(User user) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH, false))) {
            writer.write(user.toString());  // Implement a suitable toString method in User class
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //REF
    private void closeFrame() {
        dispose();
    }

    //REF
    private void openFrame(JFrame frame) {
        SwingUtilities.invokeLater(() -> {
            frame.setVisible(true);
        });
    }
}
