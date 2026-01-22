package edu.jsu.mcis.cs310;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputData {
    
    private final String csvString, jsonString;
    
    public InputData() {
        
        String csvString = null, jsonString = null;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        try {
            
            // open file input streams
            
            StringBuilder csvBuffer = new StringBuilder();
            StringBuilder jsonBuffer = new StringBuilder();
            
            BufferedReader csvReader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("resources" + File.separator + "input.csv")));
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("resources" + File.separator + "input.json")));
            
            String line;
            
            // read CSV data
            
            while((line = csvReader.readLine()) != null) {
                csvBuffer.append(line).append('\n');
            }
        
            csvString = csvBuffer.toString().trim();
            
            // read JSON data
            
            while((line = jsonReader.readLine()) != null) {
                jsonBuffer.append(line).append('\n');
            }
        
            jsonString = jsonBuffer.toString().trim();
            
            // close file input streams
            
            csvReader.close();
            jsonReader.close();
            
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        
        this.csvString = csvString;
        this.jsonString = jsonString;
        
    }

    public String getCsvString() {
        return csvString;
    }

    public String getJsonString() {
        return jsonString;
    }
    
}
