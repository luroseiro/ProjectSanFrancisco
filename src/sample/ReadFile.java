package sample;

import java.io.*;

public class ReadFile {
    private String path;

    public ReadFile(String filePath) {
        path = filePath;
    }

    public String[] OpenFile() throws IOException {
        BufferedReader textReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "ISO-8859-1"));

        int numberLines = readLines();
        String[] textData = new String[numberLines];

        for (int i = 0; i < numberLines; i++) {
            textData[i] = textReader.readLine();
        }

        textReader.close();
        return textData;
    }

    int readLines() throws IOException {
        FileReader fileToRead = new FileReader(path);
        BufferedReader bF = new BufferedReader(fileToRead);

        String aLine;
        int numberLines = 0;

        while ((aLine = bF.readLine()) != null) {
            numberLines++;
        }
        bF.close();

        return numberLines;
    }
}