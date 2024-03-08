package Quackstagram;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import signup_and_signin.User;

public class ProfileHeader {

    private User currentUser;
    private static final int PROFILE_IMAGE_SIZE = 80; // Adjusted size for the profile image to match UI
    private String loggedInUsername;

    // #1
    public JPanel createHeaderPanel(User currentUser) {
        // Header Panel
        this.currentUser = currentUser;
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(Color.GRAY);
        JPanel topHeaderPanel = createTopHeaderPanel(); // Contains: profile image, bio and stats
        headerPanel.add(topHeaderPanel);
        JPanel nameAndBioPanel = createNameAndBioPanel(); // Contains: profile name and profile bio
        headerPanel.add(nameAndBioPanel);   
        return headerPanel;

    }

    // #2
    private JLabel createProfileImage(){
        ImageIcon profileIcon = new ImageIcon(new ImageIcon("resources/img/storage/profile/"+currentUser.getUsername()+".png").getImage().getScaledInstance(PROFILE_IMAGE_SIZE, PROFILE_IMAGE_SIZE, Image.SCALE_SMOOTH));
        JLabel profileImage = new JLabel(profileIcon);
        profileImage.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return profileImage;
    }

    // #3
    private JPanel createStatsPanel(){
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        statsPanel.setBackground(new Color(249, 249, 249));
        System.out.println("Number of posts for this user"+currentUser.getPostsCount());
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getPostsCount()) , "Posts"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowersCount()), "Followers"));
        statsPanel.add(createStatLabel(Integer.toString(currentUser.getFollowingCount()), "Following"));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 10, 0)); // Add some vertical padding
        return statsPanel;
    }

    // #4
    private JPanel createTopHeaderPanel(){
        JPanel topHeaderPanel = new JPanel(new BorderLayout(10, 0));
        topHeaderPanel.setBackground(new Color(249, 249, 249));
        JPanel statsFollowPanel = new JPanel();
        statsFollowPanel.setLayout(new BoxLayout(statsFollowPanel, BoxLayout.Y_AXIS));
        statsFollowPanel.add(createStatsPanel());
        statsFollowPanel.add(createFollowButton());
        topHeaderPanel.add(statsFollowPanel, BorderLayout.CENTER);
        topHeaderPanel.add(createProfileImage(), BorderLayout.WEST);
        return topHeaderPanel;
    }

    // #5
    private JButton createFollowButton(){
        JButton followButton;
            if (isCurrentUser()) {
                followButton = new JButton("Edit Profile");
            } else {
                followButton = new JButton("Follow");

                // Check if the current user is already being followed by the logged-in user
                Path followingFilePath = Paths.get("resources/data", "following.txt");
                try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(":");
                        if (parts[0].trim().equals(loggedInUsername)) {
                            String[] followedUsers = parts[1].split(";");
                            for (String followedUser : followedUsers) {
                                if (followedUser.trim().equals(currentUser.getUsername())) {
                                    followButton.setText("Following");
                                    break;
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                followButton.addActionListener(e -> {
                    handleFollowAction(currentUser.getUsername());
                    followButton.setText("Following");
                });
            }
    
        followButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        followButton.setFont(new Font("Arial", Font.BOLD, 12));
        followButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, followButton.getMinimumSize().height)); // Make the button fill the horizontal space
        followButton.setBackground(new Color(225, 228, 232)); // A soft, appealing color that complements the UI
        followButton.setForeground(Color.BLACK);
        followButton.setOpaque(true);
        followButton.setBorderPainted(false);
        followButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Add some vertical padding
        return followButton;
    }

    // #6
    private JPanel createNameAndBioPanel(){
        JPanel profileNameAndBioPanel = new JPanel();
        profileNameAndBioPanel.setLayout(new BorderLayout());
        profileNameAndBioPanel.setBackground(new Color(249, 249, 249));
        profileNameAndBioPanel.add(createProfileNamePanel(), BorderLayout.NORTH);
        profileNameAndBioPanel.add(createProfileBioPanel(), BorderLayout.CENTER);
        return profileNameAndBioPanel;
    }

    // #7
    private JLabel createProfileNamePanel(){
        JLabel profileNameLabel = new JLabel(currentUser.getUsername());
        profileNameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        profileNameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // Padding on the sides
        return profileNameLabel;
    }

    // #8
    private JTextArea createProfileBioPanel(){
        JTextArea profileBio = new JTextArea(currentUser.getBio());
        System.out.println("This is the bio "+currentUser.getUsername());
        profileBio.setEditable(false);
        profileBio.setFont(new Font("Arial", Font.PLAIN, 12));
        profileBio.setBackground(new Color(249, 249, 249));
        profileBio.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10)); // Padding on the sides
        return profileBio;
    }

    // #9
    private boolean isCurrentUser(){
        boolean isCurrentUser = false;
        String loggedInUsername = "";

        // Read the logged-in user's username from users.txt
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("resources/data", "users.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                loggedInUsername = line.split(":")[0].trim();
                isCurrentUser = loggedInUsername.equals(currentUser.getUsername());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.loggedInUsername = loggedInUsername;
        return isCurrentUser;
    }

    private JLabel createStatLabel(String number, String text) {
        JLabel label = new JLabel("<html><div style='text-align: center;'>" + number + "<br/>" + text + "</div></html>", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
        return label;
    }
    private void handleFollowAction(String usernameToFollow) {
        Path followingFilePath = Paths.get("resources/data", "following.txt");
        Path usersFilePath = Paths.get("resources/data", "users.txt");
        String currentUserUsername = "";

        try {
            // Read the current user's username from users.txt
            try (BufferedReader reader = Files.newBufferedReader(usersFilePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(":");
                    currentUserUsername = parts[0];
                }
            }

            System.out.println("Real user is "+currentUserUsername);
            // If currentUserUsername is not empty, process following.txt
            if (!currentUserUsername.isEmpty()) {
                boolean found = false;
                StringBuilder newContent = new StringBuilder();

                // Read and process following.txt
                if (Files.exists(followingFilePath)) {
                    try (BufferedReader reader = Files.newBufferedReader(followingFilePath)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            String[] parts = line.split(":");
                            if (parts[0].trim().equals(currentUserUsername)) {
                                found = true;
                                if (!line.contains(usernameToFollow)) {
                                    line = line.concat(line.endsWith(":") ? "" : "; ").concat(usernameToFollow);
                                }
                            }
                            newContent.append(line).append("\n");
                        }
                    }
                }

                // If the current user was not found in following.txt, add them
                if (!found) {
                    newContent.append(currentUserUsername).append(": ").append(usernameToFollow).append("\n");
                }

                // Write the updated content back to following.txt
                try (BufferedWriter writer = Files.newBufferedWriter(followingFilePath)) {
                    writer.write(newContent.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}
