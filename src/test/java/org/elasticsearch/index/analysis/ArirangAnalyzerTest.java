package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.ko.KoreanFilter;
import org.apache.lucene.analysis.ko.KoreanTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.elasticsearch.Version;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.inject.Injector;
import org.elasticsearch.common.inject.ModulesBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsModule;
import org.elasticsearch.env.Environment;
import org.elasticsearch.env.EnvironmentModule;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.IndexNameModule;
import org.elasticsearch.index.settings.IndexSettingsModule;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.elasticsearch.plugin.analysis.arirang.AnalysisArirangPlugin;
import org.junit.Test;

import java.io.StringReader;

import static org.elasticsearch.common.settings.Settings.settingsBuilder;

/**
 * Created by hwjeong on 2015. 11. 3..
 */
public class ArirangAnalyzerTest {

//  public String query = "이것은 루씬한국어 형태소 분석기 플러그인 입니다.";
  public String query = "한국 엘라스틱서치 사용자 그룹의 HENRY 입니다.";

  @Test
  public void testArirangAnalyzerNamedAnalyzer() throws Exception {
    System.out.println("####### testArirangAnalyzerNamedAnalyzer #######");

    Index index = new Index("test");
    Settings settings = settingsBuilder()
        .put("path.home", "/tmp")
        .put("index.analysis.analyzer.arirang.type", "arirang_analyzer")
        .put("index.analysis.analyzer.arirang.tokenizer", "arirang_tokenizer")
        .putArray("index.analysis.analyzer.arirang.filter", "uppercase", "trim", "arirang_ft")
        .put("index.analysis.filter.arirang_ft.type", "arirang_filter")
        .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
        .build();

    Injector parentInjector = new ModulesBuilder().add(new SettingsModule(settings),
        new EnvironmentModule(new Environment(settings)))
        .createInjector();

    AnalysisModule analysisModule = new AnalysisModule(settings, parentInjector.getInstance(IndicesAnalysisService.class));
    new AnalysisArirangPlugin().onModule(analysisModule);

    Injector injector = new ModulesBuilder().add(
        new IndexSettingsModule(index, settings),
        new IndexNameModule(index),
        analysisModule)
        .createChildInjector(parentInjector);

    AnalysisService analysisService = injector.getInstance(AnalysisService.class);
    NamedAnalyzer namedAnalyzer = analysisService.analyzer("arirang");

    TokenStream stream = namedAnalyzer.tokenStream(null, query);

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

  @Test
  public void testArirangAnalyzerTokenFilter() throws Exception {
    System.out.println("####### testArirangAnalyzerTokenFilter #######");

    Index index = new Index("test");
    Settings settings = settingsBuilder()
        .put("path.home", "/tmp")
        .put("index.analysis.analyzer.arirang.type", "custom")
        .put("index.analysis.analyzer.arirang.tokenizer", "arirang_tokenizer")
        .putArray("index.analysis.analyzer.arirang.filter", "lowercase", "arirang_ft")
        .put("index.analysis.filter.arirang_ft.type", "arirang_filter")
        .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
        .build();

    Injector parentInjector = new ModulesBuilder().add(new SettingsModule(settings),
        new EnvironmentModule(new Environment(settings)))
        .createInjector();

    AnalysisModule analysisModule = new AnalysisModule(settings, parentInjector.getInstance(IndicesAnalysisService.class));
    new AnalysisArirangPlugin().onModule(analysisModule);

    Injector injector = new ModulesBuilder().add(
        new IndexSettingsModule(index, settings),
        new IndexNameModule(index),
        analysisModule)
        .createChildInjector(parentInjector);

    AnalysisService analysisService = injector.getInstance(AnalysisService.class);
    TokenFilterFactory tokenFilter = analysisService.tokenFilter("arirang_ft");
    Tokenizer tokenizer = (new ArirangTokenizerFactory(index,settings,null,settings)).create();

    tokenizer.setReader(new StringReader(query));
    TokenStream filterStream = tokenFilter.create(tokenizer);

    CharTermAttribute termAtt = filterStream.addAttribute(CharTermAttribute.class);

    try {
      filterStream.reset();

      while (filterStream.incrementToken()) {
        System.out.println(termAtt.toString());
      }

      filterStream.end();
    } finally {
      filterStream.close();
    }
  }

  @Test
  public void testArirangAnalyzerProvider() throws Exception {
    System.out.println("####### testArirangAnalyzerProvider #######");

    Index index = new Index("test");
    Settings settings = settingsBuilder()
        .put("path.home", "/tmp")
        .put("index.analysis.analyzer.arirang.type", "custom")
        .put("index.analysis.analyzer.arirang.tokenizer", "arirang_tokenizer")
        .putArray("index.analysis.analyzer.arirang.filter", "arirang_ft")
        .put("index.analysis.filter.arirang_ft.type", "arirang_filter")
        .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
        .build();

    ArirangAnalyzerProvider analyzerProvider = new ArirangAnalyzerProvider(index, settings, null, null, settings);
    KoreanAnalyzer koreanAnalyzer = analyzerProvider.get(); // analyzer 만 적용.
    TokenStream tokenStream = koreanAnalyzer.tokenStream(null, query);

    CharTermAttribute termAtt = tokenStream.addAttribute(CharTermAttribute.class);

    try {
      tokenStream.reset();

      while (tokenStream.incrementToken()) {
        System.out.println(termAtt.toString());
      }

      tokenStream.end();
    } finally {
      tokenStream.close();
    }
  }

  @Test
  public void testKoreanAnalyzer() throws Exception {
    System.out.println("####### testKoreanAnalyzer #######");

    KoreanAnalyzer koreanAnalyzer = new KoreanAnalyzer(); // analyzer 적용.
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
