package model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

/*
 * Represents a Lego build with an ID and a list of required Lego pieces.
 */
public class Build implements Writable {
    private int buildId; // Unique identifier for the build
    private List<LegoPiece> requiredPieces; // List of Lego pieces needed for the build

    /*
     * Requires: buildId must be a positive integer.
     * Modifies: this
     * Effects: Initializes a build with the given ID and an empty list of required pieces.
     */
    public Build(int buildId) {
        this.buildId = buildId;
        this.requiredPieces = new ArrayList<>();
    }

    /*
     * Returns the ID of the build.
     */
    public int getBuildId() {
        return buildId;
    }

    /*
     * Returns the list of required Lego pieces for the build.
     */
    public List<LegoPiece> getRequiredPieces() {
        return requiredPieces;

    }

    /*
     * Requires: piece must not be null.
     * Modifies: this
     * Effects: Adds a Lego piece to the list of required pieces.
     */
    public void addRequiredPiece(LegoPiece piece) {
        requiredPieces.add(piece);

        EventLog.getInstance().logEvent(new Event("Added " + piece.getQuantity() + "x " + piece.getColor()
                + " " + piece.getPartType() + " (" + piece.getDimensions() + ") to build "
                + this.buildId));
    }

    /*
     * EFFECTS: returns this Build as a JSON object
     */
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("buildId", buildId);
        json.put("requiredPieces", requiredPiecesToJson());
        return json;
    }

    /*
     * EFFECTS: returns required pieces for this Build as a JSON array
     */
    private JSONArray requiredPiecesToJson() {
        JSONArray jsonArray = new JSONArray();
        for (LegoPiece piece : requiredPieces) {
            jsonArray.put(piece.toJson());
        }
        return jsonArray;
    }
}
