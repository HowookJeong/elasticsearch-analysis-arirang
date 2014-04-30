package org.elasticsearch.index.analysis;

import java.io.IOException;

import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

public class ArirangAnalyzerProvider extends AbstractIndexAnalyzerProvider<KoreanAnalyzer> {

    private final KoreanAnalyzer analyzer;

    @Inject
    public ArirangAnalyzerProvider(Index index, @IndexSettings Settings indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) throws IOException {
        super(index, indexSettings, name, settings);

        analyzer = new KoreanAnalyzer(Lucene.VERSION.LUCENE_47);
    }

    @Override
    public KoreanAnalyzer get() {
        return this.analyzer;
    }
}