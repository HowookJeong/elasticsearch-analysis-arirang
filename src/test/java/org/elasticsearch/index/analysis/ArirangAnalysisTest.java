package org.elasticsearch.index.analysis;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.plugin.analysis.arirang.AnalysisArirangPlugin;
import org.elasticsearch.test.ESIntegTestCase;
import org.elasticsearch.test.ESTestCase;
import org.elasticsearch.test.ESTestCase.TestAnalysis;
import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import static org.elasticsearch.test.ESTestCase.createTestAnalysis;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Created by hwjeong on 2015. 11. 3..
 */
public class ArirangAnalysisTest extends ESTestCase {

  @Test
  public void test() {
    System.out.println("hello world");
  }

  @Ignore
  public void testArirangAnalysis() throws IOException {
    final TestAnalysis analysisService = createTestAnalysis(new Index("test", "_na_"), Settings.EMPTY, new AnalysisArirangPlugin());
    TokenizerFactory tokenizerFactory = analysisService.tokenizer.get("arirang_tokenizer");
    assertThat(tokenizerFactory, instanceOf(ArirangTokenizerFactory.class));
  }
}
