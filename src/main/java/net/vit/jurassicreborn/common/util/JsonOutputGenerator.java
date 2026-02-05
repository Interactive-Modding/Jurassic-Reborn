package net.vit.jurassicreborn.common.util;

import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class JsonOutputGenerator {
    public static void doJsonProcessing(HashMap<JsonObject, String> jsons){
        StringBuilder file = new StringBuilder();
        for(JsonObject object : jsons.keySet()) {

            file.append("NAME: ").append(object.remove("name").getAsString()).append(" FILE: ").append(object.toString()).append("\n SPLIT \n");
        }

        Path path = Path.of(FileUtils.getUserDirectoryPath(), "json_output.txt");

        System.out.println("JSON PATH: " + path.toString());


        try {
            Files.write(path, file.toString().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
