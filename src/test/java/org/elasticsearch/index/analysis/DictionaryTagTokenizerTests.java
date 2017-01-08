package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.analysis.DictionaryTagType.*;

// es plugin gradle, the test class name must ends with Tests
// https://github.com/elastic/elasticsearch/issues/13930
public class DictionaryTagTokenizerTests {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    private File temporaryFile;

    private StringReader stringReader = new StringReader("Test document");
    private DictionaryTagTokenizer tokenizer;

    @Before
    public void setup() throws IOException {
        temporaryFile = temporaryFolder.newFile("testFile.dic");

        try (PrintWriter out =new PrintWriter(temporaryFile)) {
            out.println("word1");
            out.println("word2");
            out.println("word3");
        }
    }

    @Test
    public void testGetDictionarySize() throws IOException {
        createTokenizer(FILE_SIZE);

        tokenizer.reset();

        assertThat(tokenizer.incrementToken()).isTrue();
        CharTermAttribute terms = tokenizer.getAttribute(CharTermAttribute.class);
        assertThat(terms.toString()).isEqualTo(String.valueOf(temporaryFile.length()));

        assertThat(tokenizer.incrementToken()).isFalse();

        tokenizer.end();
    }

    @Test
    public void testGetDictionaryDate() throws IOException {
        createTokenizer(FILE_DATE);

        tokenizer.reset();

        assertThat(tokenizer.incrementToken()).isTrue();
        CharTermAttribute terms = tokenizer.getAttribute(CharTermAttribute.class);
        assertThat(terms.toString()).isEqualTo(String.valueOf(temporaryFile.lastModified()));

        assertThat(tokenizer.incrementToken()).isFalse();

        tokenizer.end();
    }

    @Test
    public void testGetDictionaryLineNumber() throws IOException {
        createTokenizer(FILE_NUMBER_OF_LINE);

        tokenizer.reset();

        assertThat(tokenizer.incrementToken()).isTrue();
        CharTermAttribute terms = tokenizer.getAttribute(CharTermAttribute.class);
        assertThat(terms.toString()).isEqualTo("3");

        assertThat(tokenizer.incrementToken()).isFalse();

        tokenizer.end();
    }

    private void createTokenizer(DictionaryTagType tagType) {
        tokenizer = new DictionaryTagTokenizer(tagType, temporaryFile.getAbsolutePath().toString(), "");
        tokenizer.setReader(stringReader);
    }

}