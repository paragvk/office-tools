import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane; 

// A test comment

/**
 * Prevents office communicator going yello (away)
 */
public class MouseMover {

    private static boolean doMove = true;

    private static final long INTERVAL = 20000;

    private static final int MAX_MOVE_DISTANCE = 400;

    private static MenuItem resumePauseItem;

    private static TrayIcon trayIcon;

    private static Image defaultSysIcon;
    private static Image pausedSysIcon;

    public static void main(String... args) throws Exception {
        try {
            createTrayIcon();
            startMoving();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Didn't work. Sorry.");
        }
    }

    private static void startMoving() throws AWTException, InterruptedException {
        Random random = new Random();
        while (true) {
            if (doMove) {
                new Robot().mouseMove(random.nextInt(MAX_MOVE_DISTANCE),
                        random.nextInt(MAX_MOVE_DISTANCE));
            }
            Thread.sleep(INTERVAL);
        }
    }

    private static void createTrayIcon() throws AWTException {
        if (SystemTray.isSupported()) {

            final PopupMenu popup = new PopupMenu();
            defaultSysIcon = (new ImageIcon(
                    MouseMover.class.getResource("MouseMover.png"),
                    "Mouse mover default tray icon")).getImage();
            pausedSysIcon = (new ImageIcon(
                    MouseMover.class.getResource("MouseMoverPaused.png"),
                    "Mouse mover paused tray icon")).getImage();
            trayIcon = new TrayIcon(defaultSysIcon);
            final SystemTray tray = SystemTray.getSystemTray();

            // Create a pop-up menu components
            resumePauseItem = new MenuItem("Pause");
            MenuItem aboutItem = new MenuItem("About");
            MenuItem exitItem = new MenuItem("Exit");

            // Add components to pop-up menu
            popup.add(aboutItem);
            popup.addSeparator();
            popup.add(resumePauseItem);
            popup.add(exitItem);

            trayIcon.setPopupMenu(popup);
            trayIcon.setToolTip("I move mouse");

            aboutItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "zno385");
                }
            });

            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            resumePauseItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resumePauseItem.setLabel(doMove ? "Resume" : "Pause");
                    doMove = !doMove;
                    trayIcon.setToolTip(doMove ? "I move mouse"
                            : "I move mouse (Paused)");
                    trayIcon.setImage(doMove ? defaultSysIcon : pausedSysIcon);
                }
            });

            tray.add(trayIcon);
            trayIcon.displayMessage("Okay!",
                    "I ll move the mouse for you every " + (INTERVAL / 1000)
                            + " seconds",
                    MessageType.INFO);
        }

    }
}
