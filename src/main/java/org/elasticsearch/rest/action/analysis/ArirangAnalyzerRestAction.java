package org.elasticsearch.rest.action.analysis;

import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

/**
 * Created by jeonghoug on 2016. 3. 16..
 */
public class ArirangAnalyzerRestAction extends BaseRestHandler {
  ESLogger LOG = Loggers.getLogger(ArirangAnalyzerRestAction.class);

  @Inject
  public ArirangAnalyzerRestAction(Settings settings, RestController controller, Client client) {
    super(settings, controller, client);

    controller.registerHandler(RestRequest.Method.GET, "/_arirang_dictionary_reload", this);
  }

  @Override
  public void handleRequest(RestRequest request, RestChannel channel, Client client) {
    try {
      LOG.debug("Reloaded arirang analyzer dictionary!!");

      DictionaryUtil.loadDictionary();
    } catch (MorphException me) {
      channel.sendResponse(new BytesRestResponse(RestStatus.NOT_ACCEPTABLE, "Failed which reload arirang analyzer dictionary!!"));
    } finally {
    }

    channel.sendResponse(new BytesRestResponse(RestStatus.OK, "Reloaded arirang analyzer dictionary!!"));
  }
}
