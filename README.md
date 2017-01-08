Dictionary Tag plugin for Elasticsearch
=======================================

Dictionary Tag is an elasticsearch analyzer for generating dictionary's version information as
field for indexing. With the dictionary version information on each record(document) the administrator
can determine whether to re-index each record based on dictionary's version.

Currently, the plugin only supports elasticsearch version 5.1.1.

    ----------------------------------------------------
    | Dictionary Tag Analysis Plugin| Elasticsearch    |
    ----------------------------------------------------
    | 1.0.0                         | 5.1.1            |
    ----------------------------------------------------

The plugin includes the analyzer: `dictag` and tokenizer: `dictag`,

Configuration of the analyzer or tokenizer:

- tag_type: "size", "line_no", "date". The default is "date"
- file_path: path to the dictionary file.

### Test filter

Create the test index

```
curl -XPUT http://localhost:9200/my_index
```


Create the mapping and analyzer in my_index

```
PUT /my_index
{
  "settings": {
    "analysis": {
      "analyzer": {
        "my_analyzer": {
          "type": "dictag",
          "tag_type": "date",
          "file_path": "plugins/dictag/plugin-descriptor.properties"
        }
      }
    }
  }
}

POST /my_index/document/_mapping
{
   "document": {
      "properties": {
         "title": {
            "type": "text",
            "store": true,
            "fields": {
               "dictag": {
                  "type": "text",
                  "analyzer": "my_analyzer",
               }
            }
         }
      }
   }
}

```


Analyzer test, it returns the file date as expected:

```
curl -XGET 'localhost:9200/my_index/_analyze?analyzer=my_analyzer' -d 'Dictionary tag test'.

It returned:
{"tokens":[{"token":"1483899356000","start_offset":0,"end_offset":13,"type":"word","position":0}]}%
```

### Scenario of finding documents analyzed with old dictionary

Add documents and update dictionary's date:

```
PUT 'localhost:9200/my_index/document/1' -d '
{
  "title": "Title 1"
}'

touch plugins/dictag/plugin-descriptor.properties

curl -XPUT 'localhost:9200/my_index/document/2' -d '
{
  "title": "Title 2 uses newer dictionary"
}'
```

Call the analyzer to get the latest dictionary's tag information:

```
curl -XGET 'localhost:9200/my_index/_analyze?analyzer=my_analyzer' -d 'Dictionary tag test'

It returned:
{"tokens":[{"token":"1483900769000","start_offset":0,"end_offset":13,"type":"word","position":0}]}
```

Based on dictionary tag, find documents that parsed by older dictionary:

```
POST /my_index/document/_search
{
   "query": {
      "bool": {
         "must_not": [
            {
               "term": {
                  "title.dictag": "1483900769000"
               }
            }
         ]
      }
   }
}
```



