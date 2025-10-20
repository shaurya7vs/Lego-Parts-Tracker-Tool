package persistence;

import model.LegoInventory;
import model.LegoPiece;
import model.MasterBuilder;
import model.Build;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads MasterBuilder data from JSON file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads MasterBuilder from file and returns it;
    // throws IOException if an error occurs reading data from file
    public MasterBuilder read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseMasterBuilder(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses MasterBuilder from JSON object and returns it
    private MasterBuilder parseMasterBuilder(JSONObject jsonObject) {
        String name = jsonObject.getString("userName");
        MasterBuilder masterBuilder = new MasterBuilder(name);
        addInventories(masterBuilder, jsonObject);
        addBuilds(masterBuilder, jsonObject);
        return masterBuilder;
    }

    // MODIFIES: masterBuilder
    // EFFECTS: parses inventories from JSON object and adds them to masterBuilder
    private void addInventories(MasterBuilder masterBuilder, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("inventories");
        for (Object json : jsonArray) {
            JSONObject nextInventory = (JSONObject) json;
            addInventory(masterBuilder, nextInventory);
        }
    }

    // MODIFIES: masterBuilder
    // EFFECTS: parses inventory from JSON object and adds it to masterBuilder
    private void addInventory(MasterBuilder masterBuilder, JSONObject jsonObject) {
        String name = jsonObject.getString("collectionName");
        LegoInventory inventory = new LegoInventory(name);
        addPieces(inventory, jsonObject);
        masterBuilder.addInventory(inventory);
    }

    // MODIFIES: inventory
    // EFFECTS: parses Lego pieces from JSON object and adds them to inventory
    private void addPieces(LegoInventory inventory, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("pieces");
        for (Object json : jsonArray) {
            JSONObject nextPiece = (JSONObject) json;
            addPiece(inventory, nextPiece);
        }
    }

    // MODIFIES: inventory
    // EFFECTS: parses Lego piece from JSON object and adds it to inventory
    private void addPiece(LegoInventory inventory, JSONObject jsonObject) {
        String partType = jsonObject.getString("partType");
        String color = jsonObject.getString("color");
        String dimensions = jsonObject.getString("dimensions");
        int quantity = jsonObject.getInt("quantity");
        LegoPiece piece = new LegoPiece(partType, color, dimensions, quantity);
        inventory.addPiece(piece);
    }

    // MODIFIES: masterBuilder
    // EFFECTS: parses builds from JSON object and adds them to masterBuilder
    private void addBuilds(MasterBuilder masterBuilder, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("builds");
        for (Object json : jsonArray) {
            JSONObject nextBuild = (JSONObject) json;
            addBuild(masterBuilder, nextBuild);
        }
    }

    // MODIFIES: masterBuilder
    // EFFECTS: parses build from JSON object and adds it to masterBuilder
    private void addBuild(MasterBuilder masterBuilder, JSONObject jsonObject) {
        int buildId = jsonObject.getInt("buildId");
        Build build = new Build(buildId);
        addRequiredPieces(build, jsonObject);
        masterBuilder.addBuild(build);
    }

    // MODIFIES: build
    // EFFECTS: parses required Lego pieces from JSON object and adds them to build
    private void addRequiredPieces(Build build, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("requiredPieces");
        for (Object json : jsonArray) {
            JSONObject nextPiece = (JSONObject) json;
            addRequiredPiece(build, nextPiece);
        }
    }

    // MODIFIES: build
    // EFFECTS: parses required Lego piece from JSON object and adds it to build
    private void addRequiredPiece(Build build, JSONObject jsonObject) {
        String partType = jsonObject.getString("partType");
        String color = jsonObject.getString("color");
        String dimensions = jsonObject.getString("dimensions");
        int quantity = jsonObject.getInt("quantity");
        LegoPiece piece = new LegoPiece(partType, color, dimensions, quantity);
        build.addRequiredPiece(piece);
    }
}