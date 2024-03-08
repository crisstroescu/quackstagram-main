package Quackstagram;
import javax.imageio.ImageIO;
import javax.swing.*;

import signup_and_signin.User;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

public class ExploreUI extends UI {

    private static final int IMAGE_SIZE = WIDTH / 3; // Size for each image in the grid

    public ExploreUI() {
        setTitle("Explore");
        setSize(WIDTH, HEIGHT);
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        initializeUI();
    }

    public void initializeUI() {
        clearContentPane();
        setLayout(new BorderLayout());

        JPanel headerPanel = new Header(" Explore 🐥");
        JPanel mainContentPanel = createMainContentPanel();
        JPanel navigationPanel = new Navigation(this);

        addPanelsToFrame(headerPanel, mainContentPanel, navigationPanel);
        revalidateAndRepaint();
    }
    
    private void addPanelsToFrame(JPanel headerPanel, JPanel mainContentPanel, JPanel navigationPanel) {
        add(headerPanel, BorderLayout.NORTH);
        add(mainContentPanel, BorderLayout.CENTER);
        add(navigationPanel, BorderLayout.SOUTH);
    }

    private JPanel createMainContentPanel() {
        JPanel searchPanel = createSearchPanel();
        JPanel imageGridPanel = createImageGridPanel();

        JScrollPane scrollPane = createScrollPane(imageGridPanel);

        JPanel mainContentPanel = createMainContentContainer(searchPanel, scrollPane);
        return mainContentPanel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(" Search Users");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getPreferredSize().height)); // Limit the height
        return searchPanel;
    }

    //TO DO
    private JPanel createImageGridPanel() {
        JPanel imageGridPanel = new JPanel(new GridLayout(0, 3, 2, 2)); // 3 columns, auto rows
        File imageDir = new File("resources/img/uploaded");
        if (imageDir.exists() && imageDir.isDirectory()) {
            File[] imageFiles = imageDir.listFiles((dir, name) -> name.matches(".*\\.(png|jpg|jpeg)"));
            if (imageFiles != null) {
                for (File imageFile : imageFiles) {
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageFile.getPath()).getImage().getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, Image.SCALE_SMOOTH));
                    JLabel imageLabel = new JLabel(imageIcon);
                    imageLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            displayImage(imageFile.getPath()); // Call method to display the clicked image
                        }
                    });
                    imageGridPanel.add(imageLabel);
                }
            }
        }
        return imageGridPanel;
    }

    private JScrollPane createScrollPane(JPanel imageGridPanel) {
        JScrollPane scrollPane = new JScrollPane(imageGridPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }

    private JPanel createMainContentContainer(JPanel searchPanel, JScrollPane scrollPane) {
        JPanel mainContentPanel = new JPanel();
        mainContentPanel.setLayout(new BoxLayout(mainContentPanel, BoxLayout.Y_AXIS));
        mainContentPanel.add(searchPanel);
        mainContentPanel.add(scrollPane); // This will stretch to take up remaining space
        return mainContentPanel;
    }

    private void displayImage(String imagePath) {
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        // Add the header and navigation panels back
        add(new Header(" Explore 🐥"), BorderLayout.NORTH);
        add(new Navigation(this), BorderLayout.SOUTH);


        //JPanel imageViewerPanel = new JPanel(new BorderLayout());

        // Extract image ID from the imagePath
        String imageId = new File(imagePath).getName().split("\\.")[0];
        
        // Read image details
        String username = "";
        String bio = "";
        String timestampString = "";
        int likes = 0;
        Path detailsPath = Paths.get("resources/img", "image_details.txt");
        try (Stream<String> lines = Files.lines(detailsPath)) {
            String details = lines.filter(line -> line.contains("ImageID: " + imageId)).findFirst().orElse("");
            if (!details.isEmpty()) {
                String[] parts = details.split(", ");
                username = parts[1].split(": ")[1];
                bio = parts[2].split(": ")[1];
                System.out.println(bio+"this is where you get an error "+parts[3]);
                timestampString = parts[3].split(": ")[1];
                likes = Integer.parseInt(parts[4].split(": ")[1]);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // Handle exception
        }
        // Calculate time since posting
        String timeSincePosting = "Unknown";
        if (!timestampString.isEmpty()) {
            LocalDateTime timestamp = LocalDateTime.parse(timestampString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime now = LocalDateTime.now();
            long days = ChronoUnit.DAYS.between(timestamp, now);
            timeSincePosting = days + " day" + (days != 1 ? "s" : "") + " ago";
        }

        // Top panel for username and time since posting
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton usernameLabel = new JButton(username);
        JLabel timeLabel = new JLabel(timeSincePosting);
        timeLabel.setHorizontalAlignment(JLabel.RIGHT);
        topPanel.add(usernameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);

        // Prepare the image for display
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            ImageIcon imageIcon = new ImageIcon(originalImage);
            imageLabel.setIcon(imageIcon);
        } catch (IOException ex) {
            imageLabel.setText("Image not found");
        }

        // Bottom panel for bio and likes
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JTextArea bioTextArea = new JTextArea(bio);
        bioTextArea.setEditable(false);
        JLabel likesLabel = new JLabel("Likes: " + likes);
        bottomPanel.add(bioTextArea, BorderLayout.CENTER);
        bottomPanel.add(likesLabel, BorderLayout.SOUTH);

        // Adding the components to the frame
        add(topPanel, BorderLayout.NORTH);
        add(imageLabel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Re-add the header and navigation panels
        add(new Header(" Explore 🐥"), BorderLayout.NORTH);
        add(new Navigation(this), BorderLayout.SOUTH);

        // Panel for the back button
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back");

        // Make the button take up the full width
        backButton.setPreferredSize(new Dimension(WIDTH-20, backButton.getPreferredSize().height));

        backButtonPanel.add(backButton);

        backButton.addActionListener(e -> {
            getContentPane().removeAll();
            add(new Header(" Explore 🐥"), BorderLayout.NORTH);
        add(createMainContentPanel(), BorderLayout.CENTER);
            add(new Navigation(this), BorderLayout.SOUTH);
        revalidate();
            repaint();
        });
    
        final String finalUsername = username;

        usernameLabel.addActionListener(e -> {
            User user = new User(finalUsername); // Assuming User class has a constructor that takes a username
            InstagramProfileUI profileUI = new InstagramProfileUI(user);
            profileUI.setVisible(true);
            dispose(); // Close the current frame
        });

        // Container panel for image and details
        JPanel containerPanel = new JPanel(new BorderLayout());

        containerPanel.add(topPanel, BorderLayout.NORTH);
        containerPanel.add(imageLabel, BorderLayout.CENTER);
        containerPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Add the container panel and back button panel to the frame
        add(backButtonPanel, BorderLayout.NORTH);
        add(containerPanel, BorderLayout.CENTER);

        revalidateAndRepaint();
    }

    //REF it was repeating
    private void clearContentPane() {
        getContentPane().removeAll();
    }

    //REF
    private void revalidateAndRepaint() {
        revalidate();
        repaint();
    }
}
