package ghost;

import java.util.*;
import java.io.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Settings {
    
    private int playerLives;
    private int speed;
    private int frightenedLength;
    private int[] ghostModes;
    private String mapName;

    public int getPlayerLives() {
        return this.playerLives;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getFrightenedLength() {
        return this.frightenedLength;
    }

    public int[] getGhostModes() {
        return this.ghostModes;
    }

    public String getMapName() {
        return this.mapName;
    }

    /**
     * Processes and stores game settings from the configuration file for later use in setting
     * up the game.
     * 
     * @param filename the game configuration file.
     */
    public void parseConfigFile(String filename) {

        if (filename == null) {
            System.out.println("Error: File does not exist.");
            return;

        } else {
            JSONParser jsonParser = new JSONParser();
            
            try {
                FileReader reader = new FileReader(filename);
                Object obj = jsonParser.parse(reader);
                JSONObject description = (JSONObject)obj;

                // Retrieves player lives, movement speed.
                this.playerLives = ((Long)description.get("lives")).intValue();
                this.speed = ((Long)description.get("speed")).intValue();
                
                // Retrieves ghost frightened mode, and ghost mode durations.
                this.frightenedLength = ((Long)description.get("frightenedLength")).intValue();

                JSONArray jsonArray = (JSONArray)description.get("modeLengths");
                this.ghostModes = new int[jsonArray.size()];
                for (int i = 0; i < jsonArray.size(); i ++) {
                    this.ghostModes[i] = ((Long)jsonArray.get(i)).intValue();
                }

                // Retrieves the name of the map layout that is to be used in game.
                this.mapName = (String)description.get("map");

            } catch (FileNotFoundException e) {
                System.out.println("Error: File does not exist.");

            } catch (IOException e) {
                System.out.println("Error: Invalid file configuration.");

            } catch (ParseException e) {
                System.out.println("Error: Invalid file configuration.");
            }
        }
    }
}
