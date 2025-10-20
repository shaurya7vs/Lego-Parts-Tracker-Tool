package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/*
 * Represents the main application for tracking Lego inventories and builds.
 */
public class LegoTrackerApp {
    private MasterBuilder user;
    private Scanner scanner;
    private static final String JSON_STORE = "./data/legoTracker.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    /*
     * Modifies: this
     * Effects: Initializes scanner, prompts user for name, and starts the main loop.
     */
    public LegoTrackerApp() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runLegoTracker();
    }

    /*
     * Modifies: this
     * Effects: Displays menu and processes user choices until exit option is selected.
     */
    @SuppressWarnings("methodlength")
    private void runLegoTracker() {
        scanner = new Scanner(System.in);
        System.out.println("-----------------------------------------------\n" 
                            + "\tWelcome to the Lego Tracker App\n"
                            + "-----------------------------------------------");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        user = new MasterBuilder(name);

        boolean running = true;
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    addInventory();
                    break;
                case 2:
                    System.out.print("\nChoose an inventory to add pieces to: ");
                    String invName = scanner.nextLine().trim();
                    addPiecesToInventory(user.getAnInventory(invName));
                    break;
                case 3:
                    addBuild();
                    break;
                case 4:
                    System.out.print("\nEnter Build ID to add pieces to: ");
                    int buildId = scanner.nextInt();
                    scanner.nextLine();
                    addPiecesToBuild(buildId);
                    break;
                case 5:
                    printBuildStatus();
                    break;
                case 6:
                    viewInventory();
                    break;
                case 7:
                    System.out.print("\nEnter Build ID to attempt building: ");
                    int attemptBuildId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    
                    Build build = user.getABuild(attemptBuildId);
                    if (build == null) {
                        System.out.println("\nNo build found with that ID.");
                    }
                    attemptBuild(build);
                    break;
                case 8:
                    saveLegoTracker();
                    break;
                case 9:
                    loadLegoTracker();
                    break;
                case 10:
                    System.out.println("Thank you for using the Lego Tracker App!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }

    /*
     * Effects: Prints available menu options to console.
     */
    private void displayMenu() {
        System.out.println("\nLego Tracker App Menu:");
        System.out.println("1. Add Inventory");
        System.out.println("2. Add Pieces to Inventory");
        System.out.println("3. Add Build");
        System.out.println("4. Add Pieces to Build");
        System.out.println("5. Check Build Status");
        System.out.println("6. View Inventory");
        System.out.println("7. Build!");
        System.out.println("8. Save");
        System.out.println("9. Load");        
        System.out.println("10. Exit");
        System.out.print("Enter your choice: ");
    }

    /*
     * Modifies: user
     * Effects: Creates a new inventory and adds it to user's collection.
     */
    private void addInventory() {
        System.out.print("\nEnter inventory name: ");
        String name = scanner.nextLine();
        LegoInventory inventory = new LegoInventory(name);
        user.addInventory(inventory);
        System.out.println("Inventory added successfully!");
    }

    /*
     * Modifies: user
     * Effects: Creates a new build and adds it to user's list.
     */
    private void addBuild() {
        System.out.print("\nEnter Build ID: ");
        int buildId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        Build build = new Build(buildId);
        user.addBuild(build);
        System.out.println("\n----Enter pieces needed for the build----");
        addPiecesToBuild(buildId);
        System.out.println("Build added successfully!");
    }

    /*
     * Modifies: build
     * Effects: Adds specified pieces to a build and checks if it can be completed.
     */
    @SuppressWarnings("methodlength")
    private void addPiecesToBuild(int buildId) {
        Build build = user.getABuild(buildId);

        if (build == null) {
            System.out.println("No build found with that ID.");
            return;
        }

        boolean addingPieces = true;
        while (addingPieces) {
            System.out.print("\nEnter part type (e.g., Brick, Plate): ");
            String partType = scanner.nextLine();
            System.out.print("Enter color: ");
            String color = scanner.nextLine();
            System.out.print("Enter dimensions (e.g., 2x4, 1x2): ");
            String dimensions = scanner.nextLine();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            LegoPiece piece = new LegoPiece(partType, color, dimensions, quantity);
            build.addRequiredPiece(piece);
            System.out.println("\nPiece added to build successfully!");

            System.out.print("\nWould you like to add another piece? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("yes")) {
                addingPieces = false;
            }
        }

        boolean canBuild = user.isBuildable(build);
        System.out.println("\nBuild ID: " + build.getBuildId() + " - Status: "
                + (canBuild ? "Buildable" : "Not Buildable"));
    }

    /*
     * Modifies: inventory
     * Effects: Adds new pieces to the specified inventory or updates quantities if they already exist.
     */
    @SuppressWarnings("methodlength")
    private void addPiecesToInventory(LegoInventory inventory) {
        if (!user.getInventories().contains(inventory)) {
            System.out.println("\nThat inventory does not exist.");
            return;
        }

        boolean addingPieces = true;
        while (addingPieces) {
            System.out.print("\nEnter part type (e.g., Brick, Plate): ");
            String partType = scanner.nextLine();
            System.out.print("Enter color: ");
            String color = scanner.nextLine();
            System.out.print("Enter dimensions (e.g., 2x4, 1x2): ");
            String dimensions = scanner.nextLine();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            // If no existing piece was found, add a new one
            if (!containsPiece(inventory, partType, color, dimensions, quantity)) {
                LegoPiece newPiece = new LegoPiece(partType, color, dimensions, quantity);
                inventory.addPiece(newPiece);
                System.out.println("\nNew piece added successfully!");
            }
            
            // Prompts for adding a new piece or exiting
            System.out.print("\nWould you like to add another piece? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("yes")) {
                addingPieces = false;
            }
        }
    }

    /*
     * Modifies: inventory
     * Effects: Updates the quantity of an existing piece if found, otherwise does nothing.
     */
    private boolean containsPiece(LegoInventory inventory, String partType, 
                String color, String dimensions, int quantity) {
        boolean pieceExists = false;

        // Check if the piece already exists in the inventory
        for (LegoPiece piece : inventory.getPieces()) {
            if (piece.getPartType().equalsIgnoreCase(partType)
                    && piece.getColor().equalsIgnoreCase(color)
                    && piece.getDimensions().equals(dimensions)) {
                // Update existing piece quantity
                piece.setQuantity(piece.getQuantity() + quantity);
                pieceExists = true;
                System.out.println("\nPieces updated successfully!");
                break; // Stop searching once we update an existing piece
            }
        }
        return pieceExists;
    }

    /*
     * Effects: Prints each build's ID and status (Built or Not Built).
     */
    private void printBuildStatus() {    
        for (Build build : user.getBuilds()) {
            String status = "";
            if (user.isBuildable(build) == true) {
                status = "Buildable";
            } else {
                status = "Not Buildable";
            }
            System.out.println("\nBuild ID: " + build.getBuildId() + " - Status: " + status);
        }
    }

    /*
     * Effects: Prints each inventory name and all its contained pieces.
     */
    private void viewInventory() {
        for (LegoInventory inventory : user.getInventories()) {
            System.out.println("\nInventory: " + inventory.getCollectionName());
            for (LegoPiece piece : inventory.getPieces()) {
                System.out.println("- " + piece.getQuantity() + "x " + piece.getColor() + " "
                        + piece.getPartType() + " (" + piece.getDimensions() + ")");
            }
        }
    }

    /*
     * Requires: buildId corresponds to an existing build.
     * Modifies: user inventories
     * Effects: If the build is buildable, subtracts the required pieces from the user's inventory
     *          and prints the result. Otherwise, notifies the user that the build cannot be completed.
     */
    @SuppressWarnings("methodlength")
    public void attemptBuild(Build build) {
        if (!user.isBuildable(build)) {
            System.out.println("\nNot enough pieces to complete this build.");
            return;
        }

        // Remove used pieces from inventory
        for (LegoPiece requiredPiece : build.getRequiredPieces()) {
            int remainingQuantity = requiredPiece.getQuantity();
            for (LegoInventory inventory : user.getInventories()) {
                List<LegoPiece> pieces = inventory.getPieces();
                for (int i = 0; i < pieces.size() && remainingQuantity > 0; i++) {
                    LegoPiece ownedPiece = pieces.get(i);
                    if (ownedPiece.getPartType().equals(requiredPiece.getPartType())
                            && ownedPiece.getColor().equals(requiredPiece.getColor())
                            && ownedPiece.getDimensions().equals(requiredPiece.getDimensions())) {
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
        System.out.println("\nBuild successfully completed!");
    }

    /*
     * MODIFIES: this
     * EFFECTS: Saves the Lego Tracker to file
     */
    private void saveLegoTracker() {
        try {
            jsonWriter.open();
            jsonWriter.write(user);
            jsonWriter.close();
            System.out.println("Lego Tracker saved to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    /* 
     * MODIFIES: this
     * EFFECTS: Loads the Lego Tracker from file
     */
    private void loadLegoTracker() {
        try {
            user = jsonReader.read();
            System.out.println("Lego Tracker loaded from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}