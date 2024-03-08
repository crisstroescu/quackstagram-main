package signup_and_signin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import Quackstagram.Header;
import Quackstagram.UI;

import java.awt.image.BufferedImage;


public class SignUpUI extends UI {
    
    private static final String CREDENTIALS_FILE_PATH = "resources/data/credentials.txt";
    private static final String PROFILE_PHOTO_STORAGE_PATH = "resources/img/storage/profile/";
    private static final String DEFAULT_PROFILE_PHOTO = "resources/img/logos/DACS.png";

    private JTextField txtUsername, txtPassword, txtBio;
    private JButton btnRegister, btnUploadPhoto, btnSignIn;
    private JLabel lblPhoto;

    public SignUpUI() {
        initializeWindow();
        initializeUI();
    }

    private void initializeWindow() {
        setTitle("Quackstagram - Register");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
    }

    public void initializeUI() {
        add(new Header("Quackstagram 🐥"), BorderLayout.NORTH);
        add(createFieldsPanel(), BorderLayout.CENTER);
        add(createRegisterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createFieldsPanel() {
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));

        txtUsername = createTextField("Username");
        txtPassword = createTextField("Password");
        txtBio = createTextField("Bio");

        fieldsPanel.add(createVerticalStrut());
        fieldsPanel.add(createPhotoPanel());
        fieldsPanel.add(createVerticalStrut());
        fieldsPanel.add(txtUsername);
        fieldsPanel.add(createVerticalStrut());
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(createVerticalStrut());
        fieldsPanel.add(txtBio);
        fieldsPanel.add(createPhotoUploadButton());
        return fieldsPanel;
    }

    private Component createVerticalStrut() {
        return Box.createVerticalStrut(10);
    }

    private JTextField createTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setForeground(Color.GRAY);
        return textField;
    }

    private JPanel createPhotoPanel() {
        lblPhoto = new JLabel(new ImageIcon(new ImageIcon(DEFAULT_PROFILE_PHOTO).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        JPanel photoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        photoPanel.add(lblPhoto);
        return photoPanel;
    }

    private JButton createPhotoUploadButton() {
        btnUploadPhoto = new JButton("Upload Photo");
        btnUploadPhoto.addActionListener(this::handleProfilePictureUpload);
        return btnUploadPhoto;
    }

    private JPanel createRegisterPanel() {
        JPanel registerPanel = new JPanel(new BorderLayout());
        registerPanel.setBackground(Color.WHITE);

        btnRegister = new JButton("Register");
        btnRegister.addActionListener(this::onRegisterClicked);
        styleButton(btnRegister, new Color(255, 90, 95), Color.BLACK);

        btnSignIn = new JButton("Already have an account? Sign In");
        btnSignIn.addActionListener(e -> openSignInUI());
        styleButton(btnSignIn, Color.WHITE, Color.BLUE);

        registerPanel.add(btnRegister, BorderLayout.CENTER);
        registerPanel.add(btnSignIn, BorderLayout.PAGE_END);
        return registerPanel;
    }

    private void styleButton(JButton button, Color backgroundColor, Color textColor) {
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void handleProfilePictureUpload(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(this);
    
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            saveProfilePicture(selectedFile, txtUsername.getText());
        }
    }

    private void saveProfilePicture(File file, String username) {
        try {
            BufferedImage image = ImageIO.read(file);
            File outputFile = new File(PROFILE_PHOTO_STORAGE_PATH + username + ".png");
            ImageIO.write(image, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving profile picture.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRegisterClicked(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        String bio = txtBio.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (doesUsernameExist(username)) {
            JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            saveCredentials(username, password, bio);
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            openSignInUI();
        }
    }

    private boolean doesUsernameExist(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(CREDENTIALS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.split(":")[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveCredentials(String username, String password, String bio) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CREDENTIALS_FILE_PATH, true))) {
            writer.write(username + ":" + password + ":" + bio);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openSignInUI() {
        this.dispose(); 
        SwingUtilities.invokeLater(() -> {
            SignInUI signInFrame = new SignInUI(); 
            signInFrame.setVisible(true);
        });
    }  
}
