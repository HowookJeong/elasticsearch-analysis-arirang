package org.elasticsearch.index.analysis;

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
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static org.elasticsearch.common.settings.Settings.Builder.EMPTY_SETTINGS;
import static org.elasticsearch.common.settings.Settings.settingsBuilder;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by hwjeong on 2015. 11. 3..
 */
//public class ArirangAnalysisTest extends ESTestCase {
public class ArirangAnalysisTest {

  @Test
  public void testArirangAnalysis() {
    Index index = new Index("test");
    Settings settings = settingsBuilder()
        .put("path.home", "/Users/hwjeong/temp/tmp")
        .put(IndexMetaData.SETTING_VERSION_CREATED, Version.CURRENT)
        .build();
    Injector parentInjector = new ModulesBuilder().add(new SettingsModule(EMPTY_SETTINGS), new EnvironmentModule(new Environment(settings))).createInjector();
    Injector injector = new ModulesBuilder().add(
        new IndexSettingsModule(index, settings),
        new IndexNameModule(index),
        new AnalysisModule(EMPTY_SETTINGS, parentInjector.getInstance(IndicesAnalysisService.class)).addProcessor(new ArirangAnalysisBinderProcessor()))
        .createChildInjector(parentInjector);

    AnalysisService analysisService = injector.getInstance(AnalysisService.class);

    TokenizerFactory tokenizerFactory = analysisService.tokenizer("arirang_tokenizer");
    MatcherAssert.assertThat(tokenizerFactory, instanceOf(ArirangTokenizerFactory.class));
  }
}
