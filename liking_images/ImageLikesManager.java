package liking_images;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ImageLikesManager {

    private final String likesFilePath = "resources/data/likes.txt";

    // Method to like an image
    public void likeImage(String username, String imageID) throws IOException {
        Map<String, Set<String>> likesMap = readLikes();
        if (!likesMap.containsKey(imageID)) {
            likesMap.put(imageID, new HashSet<>());
        }
        Set<String> users = likesMap.get(imageID);
        if (users.add(username)) { // Only add and save if the user hasn't already liked the image
            saveLikes(likesMap);
        }
    }

    // Method to read likes from file
    private Map<String, Set<String>> readLikes() throws IOException {
        Map<String, Set<String>> likesMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(likesFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                String imageID = parts[0];
                Set<String> users = Arrays.stream(parts[1].split(",")).collect(Collectors.toSet());
                likesMap.put(imageID, users);
            }
        }
        return likesMap;
    }

    // Method to save likes to file
    private void saveLikes(Map<String, Set<String>> likesMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(likesFilePath, false))) {
            for (Map.Entry<String, Set<String>> entry : likesMap.entrySet()) {
                String line = entry.getKey() + ":" + String.join(",", entry.getValue());
                writer.write(line);
                writer.newLine();

                //no catch function
            }
        }
    }

    // Method to count likes of an image
    public int countLikes(String imageID) throws IOException {
        Map<String, Set<String>> likesMap = readLikes();
        int count = 0;
        if (likesMap.containsKey(imageID)) {
            Set<String> users = likesMap.get(imageID);
            count = users.size();
        }
        return count;
    }

    // Method to record the like in notifications.txt
    public void recordLike(String currentUser, String imageId) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String imageOwner = "";
        
        Path detailsPath = Paths.get("resources/img", "image_details.txt");
        try (BufferedReader reader = Files.newBufferedReader(detailsPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("ImageID: " + imageId)) {
                    String[] parts = line.split(", ");
                    imageOwner = parts[1].split(": ")[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String notification = String.format("%s; %s; %s; %s\n", imageOwner, currentUser, imageId, timestamp);
        try (BufferedWriter notificationWriter = Files.newBufferedWriter(Paths.get("resources/data", "notifications.txt"), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            notificationWriter.write(notification);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
