package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import org.junit.*;
import static org.junit.Assert.*;

public class JsonToCsvConverterTest {

    private String csvString, jsonString;
    private JsonObject json;
    
    @Before
    public void setUp() {

        InputData input = new InputData();
        
        csvString = input.getCsvString();
        jsonString = input.getJsonString();
        
        json = Jsoner.deserialize(jsonString, new JsonObject());
        
    }
        
    @Test
    public void testCsvToJson() {
        
        try {
            
            String testJsonString = Converter.csvToJson(csvString);
            JsonObject testJsonObject = Jsoner.deserialize(testJsonString, new JsonObject());
            
            assertEquals(json, testJsonObject);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void testJsonToCsv() {
        
        try {
            
            String testCsvString = Converter.jsonToCsv(jsonString);
            
            assertEquals(csvString, testCsvString);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void testCsvToJsonToCsv() {
        
        try {
            
            String testJsonString = Converter.csvToJson(csvString);
            String testCsvString = Converter.jsonToCsv(testJsonString);
            
            assertEquals(csvString, testCsvString);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    @Test
    public void testJsonToCsvToJson() {
        
        try {
            
            String testCsvString = Converter.jsonToCsv(jsonString);
            String testJsonString = Converter.csvToJson(testCsvString);
            
            JsonObject testJsonObject = Jsoner.deserialize(testJsonString, new JsonObject());
            
            assertEquals(json, testJsonObject);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}