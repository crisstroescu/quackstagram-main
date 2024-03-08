import javax.swing.SwingUtilities;

import signup_and_signin.SignInUI;

public class Main {
      public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SignInUI frame = new SignInUI();
            frame.setVisible(true);
        });
    }

}
