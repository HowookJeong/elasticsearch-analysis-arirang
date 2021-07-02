package org.elasticsearch.rest.action.analysis;

import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.ko.utils.DictionaryUtil;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.List;
import org.elasticsearch.rest.action.analysis.arirang.HanguelJamoMorphTokenizer;
import org.elasticsearch.rest.action.analysis.arirang.HanguelJamoType;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestRequest.Method.POST;

/**
 * Created by henry on 2018.8.28
 */
public class ArirangAnalyzerRestAction extends BaseRestHandler {

  @Override
  public String getName() {
    return "arirang_reload_action";
  }

  @Override
  public List<Route> routes() {
    return unmodifiableList(asList(
        new Route(GET, "/_arirang/reload"),
        new Route(POST, "/_arirang/reload"),
        new Route(GET, "/_arirang/jamo"),
        new Route(POST, "/_arirang/jamo")));
  }

  @Override
  protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient client)
      throws IOException {
    if (restRequest.rawPath().equalsIgnoreCase("/_arirang/jamo")) {
      return doArirangJamoTokenizer(restRequest, client);
    } else {
      return doArirangDictionaryReload(restRequest, client);
    }
  }

  protected RestChannelConsumer doArirangJamoTokenizer(RestRequest restRequest, NodeClient client)
      throws IOException {
    String jamoToken = "";

    try {
      jamoToken = HanguelJamoMorphTokenizer.getInstance().tokenizer(
          restRequest.param("text", ""),
          restRequest.param("token", HanguelJamoType.JAMO.getName())
      );
    } catch (Exception e) {
      return channel -> channel.sendResponse(
          new BytesRestResponse(RestStatus.NOT_ACCEPTABLE, "Failed which jamo tokenizer!!"));
    } finally {
    }

    final String finalToken = jamoToken;

    return channel -> channel.sendResponse(new BytesRestResponse(RestStatus.OK, finalToken));
  }


  protected RestChannelConsumer doArirangDictionaryReload(RestRequest restRequest,
      NodeClient client) throws IOException {
    try {
      DictionaryUtil.loadDictionary();
    } catch (MorphException me) {
      return channel -> channel.sendResponse(new BytesRestResponse(RestStatus.NOT_ACCEPTABLE,
          "Failed which reload arirang analyzer dictionary!!"));
    } finally {
    }

    return channel -> channel.sendResponse(
        new BytesRestResponse(RestStatus.OK, "Reloaded arirang analyzer dictionary!!"));
  }
}
