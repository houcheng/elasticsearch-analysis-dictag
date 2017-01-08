package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class DictionaryTagTokenizerFactory extends AbstractTokenizerFactory {

    private final String typeString;
    private final String path;
    private final String url;
    private final DictionaryTagType tagType;

    public DictionaryTagTokenizerFactory(IndexSettings indexSettings, Environment env, String name, Settings settings) {
        super(indexSettings, name, settings);

        typeString = settings.get("tag_type", "");
        path = settings.get("file_path", "");
        url = settings.get("file_url", "");

        tagType = DictionaryTagType.getTagType(typeString);
    }

    @Override
    public Tokenizer create() {
        return new DictionaryTagTokenizer(tagType, path, url);
    }
}

