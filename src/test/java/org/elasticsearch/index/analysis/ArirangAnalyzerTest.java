package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
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

  @Test
  public void testArirangAnalyzerNamedAnalyzer() throws Exception {
    System.out.println("####### testArirangAnalyzerNamedAnalyzer #######");

    Index index = new Index("test");
    Settings settings = settingsBuilder()
        .put("path.home", "/tmp")
        .put("index.analysis.analyzer.arirang_analyzer.type", "custom")
        .put("index.analysis.analyzer.arirang_analyzer.tokenizer", "arirang_tokenizer")
        .putArray("index.analysis.analyzer.arirang_analyzer.filter", "lowercase", "trim", "arirang_filter")
        .put("index.analysis.filter.arirang_filter.type", "arirang_filter")
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
    NamedAnalyzer namedAnalyzer = analysisService.analyzer("arirang_analyzer");

    TokenStream stream = namedAnalyzer.tokenStream(null, "한국 엘라스틱서치 사용자 그룹의 HENRY 입니다.");

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
        .put("index.analysis.analyzer.arirang_analyzer.type", "custom")
        .put("index.analysis.analyzer.arirang_analyzer.tokenizer", "arirang_tokenizer")
        .putArray("index.analysis.analyzer.arirang_analyzer.filter", "lowercase", "arirang_filter")
        .put("index.analysis.filter.arirang_filter.type", "arirang_filter")
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
    TokenFilterFactory tokenFilter = analysisService.tokenFilter("arirang_filter");
    Tokenizer tokenizer = (new ArirangTokenizerFactory(index,settings,null,settings)).create();
    String source = "한국 엘라스틱서치 사용자 그룹의 HENRY 입니다.";

    tokenizer.setReader(new StringReader(source));
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
        .put("index.analysis.analyzer.arirang_analyzer.type", "custom")
        .put("index.analysis.analyzer.arirang_analyzer.tokenizer", "arirang_tokenizer")
        .putArray("index.analysis.analyzer.arirang_analyzer.filter", "arirang_filter")
        .put("index.analysis.filter.arirang_filter.type", "arirang_filter")
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

  @Test
  public void testKoreanAnalyzer() throws Exception {
    KoreanAnalyzer analyzer = new KoreanAnalyzer();
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
