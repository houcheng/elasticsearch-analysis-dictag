package org.elasticsearch.index.analysis;

public enum DictionaryTagType {
    FILE_DATE,
    FILE_SIZE,
    FILE_NUMBER_OF_LINE;

    public static DictionaryTagType getTagType(String name) {
        if ("size".equals(name)) {
            return FILE_SIZE;
        } else if ("line_no".equals(name)) {
            return FILE_NUMBER_OF_LINE;
        } else {
            return FILE_DATE;
        }
    }
}
