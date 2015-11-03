package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.junit.Test;

import static org.elasticsearch.common.settings.Settings.settingsBuilder;

/**
 * Created by hwjeong on 2015. 11. 3..
 */
public class ArirangAnalyzerTest {

  @Test
  public void testArirangAnalyzer() throws Exception {
    Index index = new Index("test");
    Settings settings = settingsBuilder()
        .put("index.analysis.analyzer", "arirang_analyzer")
        .put("index.analysis.analyzer.arirang_analyzer.type", "org.elasticsearch.index.analysis.ArirangAnalyzerProvider")
        .put("index.analysis.analyzer.arirang_analyzer.tokenizer", "arirang_tokenizer")
        .put("index.analysis.analyzer.arirang_analyzer.filter","trim", "lowercase", "arirang_filter")
        .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
        .build();

    ArirangAnalyzerProvider analyzerProvider = new ArirangAnalyzerProvider(index, settings, null, null, settings);
    KoreanAnalyzer analyzer = analyzerProvider.get();
    TokenStream stream = analyzer.tokenStream(null, "한국 엘라스틱서치 사용자 그룹의 HENRY 입니다.");

    CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);

    try {
      stream.reset();

      while (stream.incrementToken()) {
        System.out.println(termAtt.toString());
      }

      stream.end();
    } finally {
      stream.close();
    }
  }
}
