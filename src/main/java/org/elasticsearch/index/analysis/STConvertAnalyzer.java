package org.elasticsearch.index.analysis;
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
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.settings.Settings;


public final class STConvertAnalyzer extends Analyzer {


    private String delimiter;
    private String type;
    private Boolean keepBoth=false;


    public STConvertAnalyzer(Settings settings) {
        type = settings.get("convert_type", "t2s");
        delimiter = settings.get("delimiter", ",");
        String keepBothStr = settings.get("keep_both", "false");
        if(keepBothStr.equals("true")){
            keepBoth=true;
        }
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        STConvertType convertType= STConvertType.traditional2simple;
        if(type.equals("s2t")){
            convertType = STConvertType.simple2traditional;
        }
        return  new TokenStreamComponents(new STConvertTokenizer(convertType, delimiter,keepBoth));
    }

}
