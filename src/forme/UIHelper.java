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
        if (ex != null && ex.getMessage() != null && !ex.getMessage().isEmpty()) {
            fullMessage += "\nRazlog: " + ex.getMessage();
        }
        JOptionPane.showMessageDialog(parent, fullMessage, "GRESKA", JOptionPane.ERROR_MESSAGE);
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
}
