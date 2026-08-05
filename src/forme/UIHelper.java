package forme;

import javax.swing.JOptionPane;
import java.awt.Component;

public class UIHelper {
    
    private UIHelper() {
    }
    
    public static void showError(Component parent, String message) {
        showError(parent, message, null);
    }
    
    public static void showError(Component parent, String message, Exception ex) {
        String fullMessage = message;
        if (ex != null && ex.getMessage() != null && !ex.getMessage().isEmpty() && !sadrziRazlog(message)) {
            fullMessage += "\nRazlog: " + ex.getMessage();
        }
        JOptionPane.showMessageDialog(parent, fullMessage, "GREŠKA", JOptionPane.ERROR_MESSAGE);
    }

    public static void showOperationError(Component parent, String operationMessage, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append(osigurajTacku(operationMessage));

        if (reason != null && !reason.trim().isEmpty()) {
            sb.append("\nRazlog: ").append(reason.trim());
        }

        JOptionPane.showMessageDialog(parent, sb.toString(), "GREŠKA", JOptionPane.ERROR_MESSAGE);
    }

    public static void showOperationError(Component parent, String operationMessage, Exception ex) {
        String reason = null;
        if (ex != null) {
            reason = ex.getMessage();
        }
        showOperationError(parent, operationMessage, reason);
    }
    
    public static void showInfo(Component parent, String message) {
        showInfo(parent, message, "Informacija");
    }
    
    public static void showInfo(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static int confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "Potvrda brisanja", JOptionPane.YES_NO_OPTION);
    }

    private static boolean sadrziRazlog(String message) {
        return message != null && message.contains("\nRazlog:");
    }

    private static String osigurajTacku(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Došlo je do greške.";
        }
        String trimmed = message.trim();
        return trimmed.endsWith(".") ? trimmed : trimmed + ".";
    }
}
