package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ko.KoreanFilter;
import org.apache.lucene.analysis.ko.KoreanTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.plugin.analysis.arirang.AnalysisArirangPlugin;

import java.io.StringReader;

import static org.elasticsearch.test.ESTestCase.createAnalysisService;

/**
 * Created by hwjeong on 2015. 11. 3..
 */
public class ArirangAnalyzerTest {

//  public String query = "이것은 루씬한국어 형태소 분석기 플러그인 입니다.";
  public String query = "[한국 엘라스틱서치 사용자 그룹의 HENRY 입니다.";

  public void testArirangAnalyzerNamedAnalyzer() throws Exception {
    System.out.println("####### testArirangAnalyzerNamedAnalyzer #######");
    final AnalysisService analysisService = createAnalysisService(new Index("test", "_na_"), Settings.EMPTY,
      new AnalysisArirangPlugin());

    NamedAnalyzer namedAnalyzer = analysisService.analyzer("arirang");

    TokenStream tokenStream = namedAnalyzer.tokenStream(null, query);

    CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
    TypeAttribute typeAttr = tokenStream.addAttribute(TypeAttribute.class);

    try {
      tokenStream.reset();

      while (tokenStream.incrementToken()) {
        System.out.println(termAtt.toString() + " [" + typeAttr.type() + "]");
      }

      tokenStream.end();
    } finally {
      tokenStream.close();
    }
  }

  public void testArirangAnalyzerTokenFilter() throws Exception {
    System.out.println("####### testArirangAnalyzerTokenFilter #######");
    final AnalysisService analysisService = createAnalysisService(new Index("test", "_na_"), Settings.EMPTY,
      new AnalysisArirangPlugin());

    TokenizerFactory tokenizerFactory = analysisService.tokenizer("arirang_tokenizer");
    TokenFilterFactory tokenFilter = analysisService.tokenFilter("arirang_filter");
    Tokenizer tokenizer = tokenizerFactory.create();

    tokenizer.setReader(new StringReader(query));
    TokenStream tokenStream = tokenFilter.create(tokenizer);

    CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
    TypeAttribute typeAttr = tokenStream.addAttribute(TypeAttribute.class);

    try {
      tokenStream.reset();

      while (tokenStream.incrementToken()) {
        System.out.println(termAtt.toString() + " [" + typeAttr.type() + "]");
      }

      tokenStream.end();
    } finally {
      tokenStream.close();
    }
  }

  public void testArirangCustomAnalyzer() throws Exception {
    System.out.println("####### testArirangCustomAnalyzer #######");

    final AnalysisService analysisService = createAnalysisService(new Index("test", "_na_"), Settings.EMPTY,
      new AnalysisArirangPlugin());

    TokenizerFactory tokenizerFactory = analysisService.tokenizer("arirang_tokenizer");
    TokenFilterFactory tokenFilter = analysisService.tokenFilter("arirang_filter");
    Tokenizer tokenizer = tokenizerFactory.create();


    TokenFilterFactory lowerCaseTokenFilterFactory = analysisService.tokenFilter("lowercase");
    TokenStream tokenStream;

    tokenizer.setReader(new StringReader(query));

    tokenStream = tokenFilter.create(tokenizer);
    tokenStream = lowerCaseTokenFilterFactory.create(tokenStream);

    CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
    TypeAttribute typeAttr = tokenStream.addAttribute(TypeAttribute.class);

    try {
      tokenStream.reset();

      while (tokenStream.incrementToken()) {
        System.out.println(termAtt.toString() + " [" + typeAttr.type() + "]");
      }

      tokenStream.end();
    } finally {
      tokenStream.close();
    }
  }

  public void testKoreanAnalyzer() throws Exception {
    System.out.println("####### testKoreanAnalyzer #######");

    // http://localhost:9200/test/_analyze?tokenizer=arirang_tokenizer&filters=arirang_filter&text=한국 엘라스틱서치 사용자 그룹의 HENRY 입니다.&pretty=1
    KoreanTokenizer koreanTokenizer = new KoreanTokenizer();  // tokenizer 적용.
    koreanTokenizer.setReader(new StringReader(query));

    TokenStream tokenStream = new KoreanFilter(koreanTokenizer); // filter 적용.

    CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);
    TypeAttribute typeAttr = tokenStream.addAttribute(TypeAttribute.class);

    try {
      tokenStream.reset();

      while (tokenStream.incrementToken()) {
        System.out.println(termAtt.toString() + " [" + typeAttr.type() + "]");
      }

      tokenStream.end();
    } finally {
      tokenStream.close();
    }
  }
}
