package org.elasticsearch.index.analysis;


public class ArirangAnalysisBinderProcessor  extends AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processAnalyzers(AnalyzersBindings analyzersBindings) {
        analyzersBindings.processAnalyzer("arirang_analyzer", ArirangAnalyzerProvider.class);
    }

    @Override
    public void processTokenizers(TokenizersBindings tokenizersBindings) {
        tokenizersBindings.processTokenizer("arirang_tokenizer", ArirangTokenizerFactory.class);
    }

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
        tokenFiltersBindings.processTokenFilter("arirang_filter", ArirangTokenFilterFactory.class);
    }
}