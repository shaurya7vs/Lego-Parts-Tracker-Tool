package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents a Lego part with specific attributes.
// Each Lego piece has a type, color, dimensions, and quantity.

public class LegoPiece implements Writable {
    private String partType; // e.g., "Brick", "Slope", "Plate"
    private String color; // e.g., "Red", "Blue", "Transparent"
    private String dimensions; // e.g., "2x4", "1x2"
    private int quantity; // Number of pieces in inventory

    /*
     * Requires: partType, color, dimensions must not be null or empty.
     *           quantity must be non-negative.
     * Modifies: this
     * Effects: Initializes a LegoPiece with the given attributes.
     */
    public LegoPiece(String partType, String color, String dimensions, int quantity) {
        this.partType = partType;
        this.color = color;
        this.dimensions = dimensions;
        this.quantity = quantity;
    }

    /*
     * Returns the type of the Lego piece.
     */
    public String getPartType() {
        return partType;
    }

    /*
     * Returns the color of the Lego piece.
     */
    public String getColor() {
        return color;
    }

    /*
     * Returns the dimensions of the Lego piece.
     */
    public String getDimensions() {
        return dimensions;
    }

    /*
     * Returns the quantity of the Lego piece.
     */
    public int getQuantity() {
        return quantity;
    }

    /*
     * Requires: quantity must be non-negative.
     * Modifies: this
     * Effects: Sets the quantity of the Lego piece.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;

        EventLog.getInstance().logEvent(new Event("Set " + quantity + "x as quantity for " + this.getColor()
                + " " + this.getPartType() + " (" + this.getDimensions() + ")  piece in inventory."));
    }

    /*
     * Requires: quantity must be non-negative.
     * Modifies: this
     * Effects: adds quantity to the existing quantity of the Lego piece.
     */
    public void addQuantity(int quantity) {
        this.quantity += quantity;

        EventLog.getInstance().logEvent(new Event("Added " + quantity + "x " + this.getColor()
                + " " + this.getPartType() + " (" + this.getDimensions() + ") to inventory."));
    }

    /*
     * Requires: quantity must be non-negative. quantity removed must be less
     *           than existing quantity of the Lego piece.
     * Modifies: this
     * Effects: removes quantity from the existing quantity of the Lego piece.
     *          else leaves original quantity as is.
     */
    public void removeQuantity(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
        }

        EventLog.getInstance().logEvent(new Event("Removed " + quantity + "x " + this.getColor()
                + " " + this.getPartType() + " (" + this.getDimensions() + ") from inventory."));
    }

    /*
     * EFFECTS: returns this LegoPiece as a JSON object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("partType", partType);
        json.put("color", color);
        json.put("dimensions", dimensions);
        json.put("quantity", quantity);
        return json;
    }
}
