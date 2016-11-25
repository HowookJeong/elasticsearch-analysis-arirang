package org.elasticsearch.rest.action.analisys;

import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

import java.io.IOException;

/**
 * Created by jeonghoug on 2016. 3. 16..
 */
public class ArirangAnalyzerRestAction extends BaseRestHandler {
  @Inject
  public ArirangAnalyzerRestAction(Settings settings, RestController controller) {
    super(settings);

    controller.registerHandler(RestRequest.Method.GET, "/_arirang_dictionary_reload", this);
  }

  @Override
  protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient client) throws IOException {
    try {
      DictionaryUtil.loadDictionary();
    } catch (MorphException me) {
      return channel -> channel.sendResponse(new BytesRestResponse(RestStatus.NOT_ACCEPTABLE, "Failed which reload arirang analyzer dictionary!!"));
    } finally {
    }

    return channel -> channel.sendResponse(new BytesRestResponse(RestStatus.OK, "Reloaded arirang analyzer dictionary!!"));
  }
}
