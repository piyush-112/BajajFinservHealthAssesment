package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class DestinationHashGenerator {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar DestinationHashGenerator.jar 240340120131 C:\Users\piyus\Desktop\BajajFinservHealthAssesment/example.json");
            return;
        }

        String prnNumber = args[0].toLowerCase().trim(); 
        String jsonFilePath = args[1];

        try {
            // Step 2: Parse the JSON file
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(jsonFilePath)).getAsJsonObject();
            String destinationValue = findDestinationValue(jsonObject); // traversing the JSON

            if (destinationValue == null) {
                System.out.println("No 'destination' key found in the JSON file.");
                return;
            }

            //Generating a random string
            String randomString = generateRandomString(8);

            //generate the MD5 hash
            String concatenatedString = prnNumber + destinationValue + randomString;
            String hash = generateMD5Hash(concatenatedString);

            //print the output
            String output = hash + ";" + randomString;
            System.out.println(output);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    private static String findDestinationValue(JsonObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            JsonElement element = jsonObject.get(key);
            if (key.equals("destination")) {
                return element.getAsString();
            } else if (element.isJsonObject()) {
                String result = findDestinationValue(element.getAsJsonObject());
                if (result != null) return result;
            }
        }
        return null;
    }

    // Function to generate a random alphanumeric string of given length
    private static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }

    // Function to generate MD5 hash of the input string
    private static String generateMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();

            for (byte b : messageDigest) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
