package org.elasticsearch.index.analysis;

import static org.hamcrest.Matchers.instanceOf;

import java.io.IOException;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.plugin.analysis.arirang.AnalysisArirangPlugin;
import org.elasticsearch.test.ESTestCase;

/**
 * Created by hwjeong on 2015. 11. 3..
 */
public class ArirangAnalysisTest extends ESTestCase {

  public void testArirangAnalysis() throws IOException {

    TestAnalysis analysis = createTestAnalysis();
    TokenizerFactory tokenizerFactory = analysis.tokenizer.get("arirang_tokenizer");

    assertThat(tokenizerFactory, instanceOf(ArirangTokenizerFactory.class));
  }

  private static TestAnalysis createTestAnalysis() throws IOException {
    return createTestAnalysis(new Index("test", "_na_"), Settings.EMPTY, new AnalysisArirangPlugin());
  }
}