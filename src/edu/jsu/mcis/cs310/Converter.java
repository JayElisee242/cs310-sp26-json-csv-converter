package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.*;
import java.util.*;


public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
            //Handed the raw CSV test to OpenCSV so it could read and parse the data 
            //This then return the data into a structured format that i would use to reshape into JSON
            CSVReader reader = new CSVReaderBuilder(new StringReader(csvString)).build();
            List<String[]> csvData = reader.readAll();
            
            //This separates the first row (which describes the structure of the table) from the other rows.
            //The other rows would be looked at as data
            String[] headers = csvData.get(0);
            List<String[]> dataRows = csvData.subList(1, csvData.size());
            
            //Created an object that acts as a container that will later hold the organize CSV data 
            JsonObject json = new JsonObject();
            
            //Created a JSON array that be populated with the column names from the CSV header row that I separated using the previous block of code
            JsonArray colHeadings = new JsonArray();
            for (String header : headers) {
                 colHeadings.add(header);        
            }
            json.put("ColHeadings", colHeadings);
            
            
        //Created a JSON array for the episode ID's
        JsonArray prodNums = new JsonArray();
        
        //Created a JSON array for the episode data 
        JsonArray data = new JsonArray();
        
        //Created a for loop that goes through each CSV data row at a time, exracts the episode ID.
        //Then converts the remaining fields into JSON format.
        //Also keeps the intergrity of the data.
        for (String[] row : dataRows) {
            // This if statment will filter out empty rows/rows filled with whitespace
            if (row.length > 0 && row[0] != null && !row[0].trim().isEmpty()) {
                prodNums.add(row[0]);
                JsonArray rowArray = new JsonArray();
                for (int i = 1; i < row.length; ++i) {
                    if (headers[i].equals("Season") || headers[i].equals("Episode")) {
                        rowArray.add(Integer.valueOf(row[i]));
                    } else {
                        rowArray.add(row[i]);
                    }
                }
                data.add(rowArray);
            }
        }
        //This adds the episodes ID's and data record to the json object and them serialize it.
        json.put("ProdNums", prodNums);
        json.put("Data", data);
        
        result = Jsoner.serialize(json);
        
    }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            
        // This will parse the raw json text/string into a JSONObject.**Deserializing the json text**
        JsonObject jsonObject = (JsonObject) Jsoner.deserialize(jsonString);

        //This pulls the headings, episode ID and the episode data arrays from JSONObject.
        JsonArray colHeadings = (JsonArray) jsonObject.get("ColHeadings");
        JsonArray prodNums = (JsonArray) jsonObject.get("ProdNums");
        JsonArray data = (JsonArray) jsonObject.get("Data");

        //Used StringWriter an in memory text buffer that acts like a blank page where the CSV will be written.
        //Using the CSVWritter class this will handle CSV formating rules automatictly.commas, quotes, etc.
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.DEFAULT_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, "\n");

        //This will convert JSON coumn heading into a java string array.
        //Then writes it as the first row of the CSV file.
        String[] headers = colHeadings.toArray(new String[0]);
        csvWriter.writeNext(headers);

        //This for loop rebuilds each CSV data row by attaching the episode ID with the corresponding data fields from JSON.
        for (int i = 0; i < prodNums.size(); i++) {
            List<String> rowList = new ArrayList<>();
            rowList.add(prodNums.get(i).toString());

            JsonArray rowData = (JsonArray) data.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                Object item = rowData.get(j);
                String header = headers[j + 1];
                
                if (header.equals("Episode") && item instanceof Number && ((Number)item).intValue() < 10) {
                    rowList.add(String.format("%02d", ((Number)item).intValue()));
                } else {
                    rowList.add(item.toString());
                }
            }
            //This writes one complete CSV row.
            csvWriter.writeNext(rowList.toArray(new String[0]));
        }

        //This finanlizes and cleans the output.
        //Removes any trailing newline 
        csvWriter.close();
        result = stringWriter.toString().replace("\r\n", "\n");
        if (result.endsWith("\n")) {
            result = result.substring(0, result.length() - 1);
        }
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
