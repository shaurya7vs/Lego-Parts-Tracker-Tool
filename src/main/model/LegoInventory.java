package model;

/*
 * Represents a Lego part inventory.
 * 
 * Stores a collection of Lego pieces with a nam of the collection and total number of pieces.
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.Writable;

public class LegoInventory implements Writable {
    private String collectionName;  // name of collection of LegoPiece
    private List<LegoPiece> pieceList; // List of Lego pieces in inventory

    /*
     * Requires: collectionName must not be null or empty.
     * Modifies: this
     * Effects: Initializes an empty Lego inventory with the given name.
     */
    public LegoInventory(String collectionName) {
        this.collectionName = collectionName;
        this.pieceList = new ArrayList<>();
    }

    /*
     * Returns the name of the Lego collection.
     */
    public String getCollectionName() {
        return collectionName;
    }

    /*
     * Returns the total number of pieces in the inventory.
     */
    public int getTotalPieces() {
        int total = 0;
        for (LegoPiece piece : pieceList) {
            total += piece.getQuantity();
        }
        return total;
    }

    /*
     * Requires: piece must not be null.
     * Modifies: this
     * Effects: Adds a Lego piece to the inventory.
     */
    public void addPiece(LegoPiece piece) {
        pieceList.add(piece);
        
        EventLog.getInstance().logEvent(new Event("Added " + piece.getQuantity() + "x " + piece.getColor()
                + " " + piece.getPartType() + " (" + piece.getDimensions() + ") to inventory "
                + this.collectionName));
    }

    /*
     * Requires: piece must not be null.
     * Modifies: this
     * Effects: Removes a Lego piece from the inventory.
     */
    public void removePiece(LegoPiece piece) {
        pieceList.remove(piece);

        EventLog.getInstance().logEvent(new Event("Removed " + piece.getQuantity() + "x " + piece.getColor()
                + " " + piece.getPartType() + " (" + piece.getDimensions() + ") from inventory "
                + this.collectionName));
    }

    /*
     * Returns the list of Lego pieces in the inventory.
     */
    public List<LegoPiece> getPieces() {
        return pieceList;
    }

    /*
     * EFFECTS: returns this LegoInventory as a JSON object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("collectionName", collectionName);
        json.put("pieces", piecesToJson());
        return json;
    }

    /*
     * EFFECTS: returns pieces in this inventory as a JSON array
     */
    private JSONArray piecesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (LegoPiece piece : pieceList) {
            jsonArray.put(piece.toJson());
        }
        return jsonArray;
    }
}