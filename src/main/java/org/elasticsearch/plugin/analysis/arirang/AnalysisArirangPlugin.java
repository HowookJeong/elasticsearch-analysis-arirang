package org.elasticsearch.plugin.analysis.arirang;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.ArirangAnalysisBinderProcessor;
import org.elasticsearch.plugins.Plugin;


public class AnalysisArirangPlugin extends Plugin {

    @Override
    public String name() {
        return "analysis-arirang";
    }

    @Override
    public String description() {
        return "Korean Analyzer";
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new ArirangAnalysisBinderProcessor());
    }
}