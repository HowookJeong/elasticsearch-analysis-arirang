package org.elasticsearch.plugin.analysis.arirang;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.ArirangAnalysisBinderProcessor;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.action.analysis.ArirangAnalyzerRestModule;

import java.util.ArrayList;
import java.util.Collection;


public class AnalysisArirangPlugin extends Plugin {

    @Override
    public String name() {
        return "analysis-arirang";
    }

    @Override
    public String description() {
        return "Korean Analyzer";
    }

    @Override
    public Collection<Module> nodeModules() {
        Collection<Module> modules = new ArrayList<>();
        modules.add(new ArirangAnalyzerRestModule());

        return modules;
    }

    public void onModule(AnalysisModule module) {
        module.addProcessor(new ArirangAnalysisBinderProcessor());
    }
}