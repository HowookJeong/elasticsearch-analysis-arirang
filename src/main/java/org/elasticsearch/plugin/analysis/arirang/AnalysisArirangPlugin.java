package org.elasticsearch.plugin.analysis.arirang;

import java.util.List;
import java.util.function.Supplier;
import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.node.DiscoveryNodes;
import org.elasticsearch.common.settings.ClusterSettings;
import org.elasticsearch.common.settings.IndexScopedSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.index.analysis.ArirangTokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule.AnalysisProvider;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;

import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestHandler;
import org.elasticsearch.rest.action.analysis.ArirangAnalyzerRestAction;

import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;


public class AnalysisArirangPlugin extends Plugin implements AnalysisPlugin, ActionPlugin {

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

    @Override
    public List<RestHandler> getRestHandlers(final Settings settings,
      final RestController restController,
      final ClusterSettings clusterSettings,
      final IndexScopedSettings indexScopedSettings,
      final SettingsFilter settingsFilter,
      final IndexNameExpressionResolver indexNameExpressionResolver,
      final Supplier<DiscoveryNodes> nodesInCluster) {

        return singletonList(new ArirangAnalyzerRestAction(settings, restController));
    }
}