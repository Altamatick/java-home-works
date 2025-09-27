package app;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {

    private final static String BASE_PATH = "files/";

    public String writeFile(String fileName, String fileContent) {
        try(FileWriter fw = new FileWriter(BASE_PATH + fileName)) {
            fw.write(fileContent);
            return "Success.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String readFile(String fileName) {
        try (FileReader reader = new FileReader(BASE_PATH + fileName)) {
            int sym;
            StringBuilder stringBuilder = new StringBuilder();
            while ((sym = reader.read()) != -1) {
                stringBuilder.append((char) sym);
            }
            return stringBuilder.toString();
        } catch (IOException ex) {
            return ex.getMessage();
        }
    }
}
