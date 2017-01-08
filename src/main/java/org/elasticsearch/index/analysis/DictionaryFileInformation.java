package org.elasticsearch.index.analysis;

import java.io.*;

import static org.elasticsearch.index.analysis.DictionaryTagType.FILE_NUMBER_OF_LINE;
import static org.elasticsearch.index.analysis.DictionaryTagType.FILE_SIZE;

/**
 * Gets dictionary file information, with 5 minutes cache.
 */
public class DictionaryFileInformation {
    private final DictionaryTagType tagType;
    private final String path;
    public DictionaryFileInformation(String path, DictionaryTagType tagType) {
        this.path = path;
        this.tagType = tagType;
    }

    public String getTag() {
        if (tagType == FILE_SIZE) {
            return getFileLength();
        } else if (tagType == FILE_NUMBER_OF_LINE) {
            return getFileLineOfNumber();
        }
        return getFileDate();

    }

    private String getFileLineOfNumber() {
        int lineNumber = 0;
        try {
            BufferedReader reader = reader = new BufferedReader(new FileReader(path));
            while (reader.readLine() != null) lineNumber++;
            reader.close();
        } catch (FileNotFoundException e) {
            return "0";
        } catch (IOException e) {
            return "0";
        }
        return String.valueOf(lineNumber);
    }

    private String getFileDate() {
        File file = new File(path);
        return String.valueOf(file.lastModified());
    }

    private String getFileLength() {
        File file = new File(path);
        return String.valueOf(file.length());
    }
}
