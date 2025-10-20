package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

/*
 * Represents a MasterBuilder, a user who manages Lego inventories and builds.
 */
public class MasterBuilder implements Writable {
    private String userName; // Name of the user
    private List<LegoInventory> inventories; // List of Lego inventories owned by the user
    private List<Build> builds; // List of builds the user wants to make

    /*
     * Requires: userName must not be null or empty.
     * Modifies: this
     * Effects: Initializes a MasterBuilder with the given name and empty lists for inventories and builds.
     */
    public MasterBuilder(String userName) {
        this.userName = userName;
        this.inventories = new ArrayList<>();
        this.builds = new ArrayList<>();
    }

    /*
     * Requires: build is not null
     * Effects: Checks if the available pieces in the user's inventories are sufficient to 
     *          fully complete the given build. Returns true if all required pieces are available 
     *          in the necessary quantities, otherwise returns false.
     */
    public boolean isBuildable(Build build) {
        for (LegoPiece requiredPiece : build.getRequiredPieces()) {
            int totalAvailable = 0;
            for (LegoInventory inventory : inventories) {
                for (LegoPiece ownedPiece : inventory.getPieces()) {

                    String pt = requiredPiece.getPartType().toLowerCase();
                    String pc = requiredPiece.getColor().toLowerCase();
                    String pd = requiredPiece.getDimensions();
                    Boolean partType = ownedPiece.getPartType().toLowerCase().equals(pt);
                    Boolean partColor = ownedPiece.getColor().toLowerCase().equals(pc);
                    Boolean partDimension = ownedPiece.getDimensions().equals(pd);
                    
                    if (partType && partColor && partDimension) {
                        totalAvailable += ownedPiece.getQuantity();
                    }
                }
            }
            if (totalAvailable < requiredPiece.getQuantity()) {
                return false; // if not enough pieces for this requirement
            }
        }
        return true; // if all conditions pass then build can be made
    }

    /*
     * Requires: inventory must not be null.
     * Modifies: this
     * Effects: Adds a Lego inventory to the user's list.
     */
    public void addInventory(LegoInventory inventory) {
        inventories.add(inventory);

        EventLog.getInstance().logEvent(new Event("Added inventory: " + inventory.getCollectionName()
                + " to " + userName + "'s list of inventories."));
    }

    /*
     * Requires: build must not be null.
     * Modifies: this
     * Effects: Adds a build to the user's list.
     */
    public void addBuild(Build build) {
        builds.add(build);

        EventLog.getInstance().logEvent(new Event("Added build: " + build.getBuildId()
                + " to " + userName + "'s list of builds."));
    }

    /*
     * Returns the user's name.
     */
    public String getUserName() {
        return userName;
    }

    /*
     * Returns the list of Lego inventories owned by the user.
     */
    public List<LegoInventory> getInventories() {
        return inventories;
    }

    /*
     * Returns the inventory with specified name from list of inventories
     */
    public LegoInventory getAnInventory(String name) {
        for (LegoInventory inv : inventories) {
            if (name.equals(inv.getCollectionName())) {
                return inv;
            }
        }
        return null;
    }

    /*
     * Returns the list of builds the user wants to make.
     */
    public List<Build> getBuilds() {
        return builds;
    }

    /*
     * Returns a build the user wants to make.
     */
    public Build getABuild(int id) {
        for (Build b : builds) {
            if (id == b.getBuildId()) {
                return b;
            }
        }
        return null;
    }

    /*
     * EFFECTS: returns this MasterBuilder as a JSON object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("userName", userName);
        json.put("inventories", inventoriesToJson());
        json.put("builds", buildsToJson());
        return json;
    }

    /* 
     * EFFECTS: returns inventories in this MasterBuilder as a JSON array
     */
    private JSONArray inventoriesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (LegoInventory inventory : inventories) {
            jsonArray.put(inventory.toJson());
        }
        return jsonArray;
    }

    /*
     * EFFECTS: returns builds in this MasterBuilder as a JSON array
     */
    private JSONArray buildsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Build build : builds) {
            jsonArray.put(build.toJson());
        }
        return jsonArray;
    }
}