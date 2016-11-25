package org.elasticsearch.index.analysis;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.plugin.analysis.arirang.AnalysisArirangPlugin;
import org.hamcrest.MatcherAssert;

import java.io.IOException;

import static org.elasticsearch.test.ESTestCase.createAnalysisService;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by hwjeong on 2015. 11. 3..
 */
public class ArirangAnalysisTest {

  public void testArirangAnalysis() throws IOException {
    final AnalysisService analysisService = createAnalysisService(new Index("test", "_na_"), Settings.EMPTY,
      new AnalysisArirangPlugin());
    TokenizerFactory tokenizerFactory = analysisService.tokenizer("arirang_tokenizer");
    MatcherAssert.assertThat(tokenizerFactory, instanceOf(ArirangTokenizerFactory.class));
  }
}
