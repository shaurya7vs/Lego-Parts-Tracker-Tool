package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/*
 *  GUI screen for Lego Tracker App with menu options as buttons.
 */
public class LegoTrackerGUI extends JFrame {
    private MasterBuilder user;
    private JButton[] optionButtons;
    private static final String JSON_STORE = "./data/legoTracker.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String[] OPTIONS = {
            "1. Add Inventory",
            "2. Add Pieces to Inventory",
            "3. Remove Pieces from Inventory",
            "4. Add Build",
            "5. Add Pieces to Build",
            "6. Check Build Status",
            "7. View Inventory",
            "8. Build!",
            "9. Save",
            "10. Load",
            "11. Exit"
    };


    // EFFECTS: Initializes the GUI window with menu buttons and creates a default MasterBuilder
    @SuppressWarnings("methodlength")
    public LegoTrackerGUI() {
        showSplashScreen();

        String userName = JOptionPane.showInputDialog(this, "Enter your name:", "Welcome to Lego Tracker App",
                JOptionPane.PLAIN_MESSAGE);
        
        user = new MasterBuilder(userName);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(userName + "'s Lego Tracker App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(OPTIONS.length, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        optionButtons = new JButton[OPTIONS.length];
        for (int i = 0; i < OPTIONS.length; i++) {
            optionButtons[i] = new JButton(OPTIONS[i]);
            int finalI = i;
            optionButtons[i].addActionListener(e -> handleOptionSelected(finalI + 1));
            buttonPanel.add(optionButtons[i]);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @SuppressWarnings("methodlength")
    // EFFECTS: Displays splash screen for 3 seconds
    private void showSplashScreen() {
        JWindow splash = new JWindow();

        ImageIcon backgroundIcon = new ImageIcon("./data/lego_splash_background.jpg");
        JLabel background = new JLabel(backgroundIcon);
        background.setLayout(new BorderLayout());

        JLabel title = new JLabel("Lego Tracker App", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.BLACK);
        title.setBorder(BorderFactory.createEmptyBorder(70, 0, 0, 0));
        background.add(title, BorderLayout.NORTH);

        JLabel subtitle = new JLabel("Designed by: Shaurya Vardhan Shastri", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.BOLD, 12));
        subtitle.setForeground(Color.BLACK);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 70, 0));
        background.add(subtitle, BorderLayout.SOUTH);

        // progress bar
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setForeground(Color.GREEN);
        progressBar.setPreferredSize(new Dimension(300, 20));

        // housing for progress bar with border aligning to center it
        JPanel progressPanel = new JPanel();
        progressPanel.setOpaque(false); // Keep background transparent to show image
        progressPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        progressPanel.add(progressBar);

        background.add(progressPanel, BorderLayout.CENTER);

        splash.getContentPane().add(background);
        splash.setSize(backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight());
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        // moving progress bar every 0.3 seconds
        for (int i = 0; i <= 100; i++) {
            progressBar.setValue(i);
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        splash.dispose();
    }

    @SuppressWarnings("methodlength")
    // EFFECTS: Handles user menu selections
    private void handleOptionSelected(int optionNumber) {
        switch (optionNumber) {
            case 1:
                addInventory();
                break;
            case 2:
                addPieceToInventory();
                break;
            case 3:
                removePieceFromInventory();
                break;
            case 4:
                addBuild();
                break;
            case 5:
                addPieceToBuild();
                break;
            case 6:
                showBuildStatus();
                break;
            case 7:
                viewInventory();
                break;
            case 8:
                attemptBuild();
                break;
            case 9:
                saveLegoTracker();
                break;
            case 10:
                loadLegoTracker();
                break;
            case 11:
                showEventLogThenExit();
                break;
            default: JOptionPane.showMessageDialog(this,
                    "Option not yet implemented.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // MODIFIES: user
    // EFFECTS: Prompts user to add a new inventory
    private void addInventory() {
        String name = JOptionPane.showInputDialog(this, "Enter inventory name:");
        if (name != null && !name.trim().isEmpty()) {
            user.addInventory(new LegoInventory(name.trim()));
            JOptionPane.showMessageDialog(this, "Inventory added.");
        }
    }

    // MODIFIES: user
    // EFFECTS: Prompts user to add a new build
    private void addBuild() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Build ID:"));
        Build build = new Build(id);
        user.addBuild(build);

        int addMore;
        do {
            String type = JOptionPane.showInputDialog(this, "Enter required part type:");
            String color = JOptionPane.showInputDialog(this, "Enter required color:");
            String dim = JOptionPane.showInputDialog(this, "Enter dimensions (e.g., 2x4):");
            int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity needed:"));

            LegoPiece piece = new LegoPiece(type, color, dim, quantity);
            build.addRequiredPiece(piece);

            addMore = JOptionPane.showConfirmDialog(this, "Add another piece to build?",
            "Continue", JOptionPane.YES_NO_OPTION);
        } while (addMore == JOptionPane.YES_OPTION);

        JOptionPane.showMessageDialog(this, "Build added.");
    }

    // MODIFIES: user
    // EFFECTS: Prompts user to add a piece to an existing inventory
    private void addPieceToInventory() {
        String invName = JOptionPane.showInputDialog(this, "Enter inventory name:");
        LegoInventory inventory = user.getAnInventory(invName);
        if (inventory == null) {
            JOptionPane.showMessageDialog(this, "Inventory not found.");
            return;
        }

        String type = JOptionPane.showInputDialog(this, "Enter part type (e.g., Brick):");
        String color = JOptionPane.showInputDialog(this, "Enter color:");
        String dim = JOptionPane.showInputDialog(this, "Enter dimensions (e.g., 2x4):");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity:"));

        LegoPiece newPiece = new LegoPiece(type, color, dim, quantity);

        // checking to see if piece already exists in the inventory
        for (LegoPiece piece : inventory.getPieces()) {
            if (piece.getPartType().equalsIgnoreCase(newPiece.getPartType()) 
                    && piece.getColor().equalsIgnoreCase(newPiece.getColor())
                    && piece.getDimensions().equalsIgnoreCase(newPiece.getDimensions())) {
                piece.addQuantity(newPiece.getQuantity());
                return;
            }
        }
        
        inventory.addPiece(newPiece);

        JOptionPane.showMessageDialog(this, "Piece added to inventory.");
    }

    // MODIFIES: user
    // EFFECTS: Prompts user to remove a piece to an existing inventory
    @SuppressWarnings("methodlength")
    private void removePieceFromInventory() {
        String invName = JOptionPane.showInputDialog(this, "Enter inventory name:");
        LegoInventory inventory = user.getAnInventory(invName);
        if (inventory == null) {
            JOptionPane.showMessageDialog(this, "Inventory not found.");
            return;
        }

        String type = JOptionPane.showInputDialog(this, "Enter part type (e.g., Brick):");
        String color = JOptionPane.showInputDialog(this, "Enter color:");
        String dim = JOptionPane.showInputDialog(this, "Enter dimensions (e.g., 2x4):");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity:"));

        LegoPiece newPiece = new LegoPiece(type, color, dim, quantity);

        // checking to see if piece already exists in the inventory
        for (LegoPiece piece : inventory.getPieces()) {
            if (piece.getPartType().equalsIgnoreCase(newPiece.getPartType()) 
                    && piece.getColor().equalsIgnoreCase(newPiece.getColor())
                    && piece.getDimensions().equalsIgnoreCase(newPiece.getDimensions())) {
                if (piece.getQuantity() - quantity <= 0) {
                    inventory.removePiece(piece);
                } else {
                    piece.removeQuantity(quantity);
                }

                JOptionPane.showMessageDialog(this, "Pieces removed from inventory.");
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Piece does not exist in inventory.");
    }

    // MODIFIES: user
    // EFFECTS: Prompts user to add a piece to an existing build
    private void addPieceToBuild() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Build ID to update:"));
        Build build = user.getABuild(id);
        if (build == null) {
            JOptionPane.showMessageDialog(this, "Build not found.");
            return;
        }

        int addMore;
        do {
            String type = JOptionPane.showInputDialog(this, "Enter required part type:");
            String color = JOptionPane.showInputDialog(this, "Enter required color:");
            String dim = JOptionPane.showInputDialog(this, "Enter dimensions (e.g., 2x4):");
            int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity needed:"));

            LegoPiece piece = new LegoPiece(type, color, dim, quantity);
            build.addRequiredPiece(piece);

            addMore = JOptionPane.showConfirmDialog(this, "Add another piece to build?",
            "Continue", JOptionPane.YES_NO_OPTION);
        } while (addMore == JOptionPane.YES_OPTION);

        JOptionPane.showMessageDialog(this, "Pieces added to Build ID: " + id);
    }

    // EFFECTS: Shows build status for all builds
    private void showBuildStatus() {
        StringBuilder sb = new StringBuilder();
        for (Build b : user.getBuilds()) {
            boolean complete = user.isBuildable(b);
            sb.append("Build ID: ").append(b.getBuildId())
                    .append(" - ").append(complete ? "Buildable" : "Not Buildable").append("\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Build Status", JOptionPane.INFORMATION_MESSAGE);
    }

    // EFFECTS: Displays all inventories and their contents
    private void viewInventory() {
        StringBuilder sb = new StringBuilder();
        for (LegoInventory inv : user.getInventories()) {
            sb.append("\nInventory: ").append(inv.getCollectionName()).append("\n");
            for (LegoPiece p : inv.getPieces()) {
                sb.append("  - ").append(p.getQuantity()).append("x ")
                        .append(p.getColor()).append(" ")
                        .append(p.getPartType()).append(" (")
                        .append(p.getDimensions()).append(")\n");
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString(), "Inventories", JOptionPane.INFORMATION_MESSAGE);
    }

    // MODIFIES: user inventories
    // EFFECTS: attempts to complete a build and removes used pieces if buildable
    @SuppressWarnings("methodlength")
    private void attemptBuild() {
        int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Build ID to attempt:"));
        Build build = user.getABuild(id);
        
        if (build == null) {
            JOptionPane.showMessageDialog(this, "Build not found.");
            return;
        }

        if (!user.isBuildable(build)) {
            JOptionPane.showMessageDialog(this, "Not enough pieces to complete this build.");
            return;
        }

        for (LegoPiece requiredPiece : build.getRequiredPieces()) {
            int remainingQuantity = requiredPiece.getQuantity();
            for (LegoInventory inventory : user.getInventories()) {
                List<LegoPiece> pieces = inventory.getPieces();
                for (int i = 0; i < pieces.size() && remainingQuantity > 0; i++) {
                    LegoPiece ownedPiece = pieces.get(i);
                    if (ownedPiece.getPartType().equalsIgnoreCase(requiredPiece.getPartType())
                            && ownedPiece.getColor().equalsIgnoreCase(requiredPiece.getColor())
                            && ownedPiece.getDimensions().equalsIgnoreCase(requiredPiece.getDimensions())) {

                        int usedQuantity = Math.min(ownedPiece.getQuantity(), remainingQuantity);
                        ownedPiece.setQuantity(ownedPiece.getQuantity() - usedQuantity);
                        remainingQuantity -= usedQuantity;

                        if (ownedPiece.getQuantity() == 0) {
                            pieces.remove(i);
                            i--;
                        }
                    }
                }
            }
        }
        JOptionPane.showMessageDialog(this, "Build successfully completed!");
    }

    // MODIFIES: this
    // EFFECTS: Saves the current user state to JSON file
    private void saveLegoTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(user);
            jsonWriter.close();
            JOptionPane.showMessageDialog(this, "Lego Tracker saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads user data from JSON file
    private void loadLegoTracker() {
        try {
            user = jsonReader.read();
            JOptionPane.showMessageDialog(this, "Lego Tracker loaded from " + JSON_STORE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: Shows the event log in a window and exits the application after it is closed
    private void showEventLogThenExit() {
        for (model.Event event : model.EventLog.getInstance()) {
            System.out.println(event.toString());
        }

        System.exit(0);
    }
}