package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.IndexSettings;

import java.io.IOException;

public class ArirangAnalyzerProvider extends AbstractIndexAnalyzerProvider<KoreanAnalyzer> {

    private final KoreanAnalyzer analyzer;

    public ArirangAnalyzerProvider(IndexSettings indexSettings, Environment env, String name, Settings settings) throws IOException {
        super(indexSettings, name, settings);

        analyzer = new KoreanAnalyzer();
    }

    @Override
    public KoreanAnalyzer get() {
        return this.analyzer;
    }
}