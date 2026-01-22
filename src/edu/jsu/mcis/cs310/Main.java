package edu.jsu.mcis.cs310;

import java.io.StringReader;
import java.util.List;
import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;

public class Main {
    
    public static void main(String[] args) {
        
        try {
            
            // read input data
            
            InputData input = new InputData();
            
            // test conversion methods

            String csvString = Converter.jsonToCsv(input.getJsonString());
            String jsonString = Converter.csvToJson(input.getCsvString());
            
            // convert CSV to JSON; print results to console

            System.out.println("CONVERSION RESULTS (CSV to JSON)");
            System.out.println("================================");

            System.out.println(jsonString);

            // convert JSON to CSV; print results to console

            System.out.println("\nCONVERSION RESULTS (JSON to CSV)");
            System.out.println("================================");

            System.out.println(csvString);
            
            // parse output to CSV/JSON objects

            CSVReader csvReader = new CSVReader(new StringReader(csvString));
            List<String[]> csv = csvReader.readAll();
            JsonObject json = Jsoner.deserialize(jsonString, new JsonObject());
            
            System.out.println("\nJSON/CSV Objects");
            System.out.println("================");
            
            System.out.println("Total CSV Rows (should be 80): " + csv.size());
            System.out.println("Total JSON Elements (should be 3): " + json.size() + "\n");
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    
}
