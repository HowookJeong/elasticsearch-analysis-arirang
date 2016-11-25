package org.elasticsearch.plugin.analysis.arirang;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.index.analysis.ArirangTokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;


public class AnalysisArirangPlugin extends Plugin implements AnalysisPlugin {

    @Override
    public Map<String, AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        return singletonMap("arirang_filter", ArirangTokenFilterFactory::new);
    }

    @Override
    public Map<String, AnalysisProvider<TokenizerFactory>> getTokenizers() {
        Map<String, AnalysisProvider<TokenizerFactory>> extra = new HashMap<>();
        extra.put("arirang_tokenizer", ArirangTokenizerFactory::new);

        return extra;
    }

    @Override
    public Map<String, AnalysisProvider<AnalyzerProvider<? extends Analyzer>>> getAnalyzers() {
        return singletonMap("arirang_analyzer", ArirangAnalyzerProvider::new);
    }
}