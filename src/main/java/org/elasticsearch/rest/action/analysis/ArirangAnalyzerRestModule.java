package org.elasticsearch.rest.action.analysis;

import org.elasticsearch.common.inject.AbstractModule;

/**
 * Created by henry on 2018.8.28
 */
public class ArirangAnalyzerRestModule  extends AbstractModule {

  @Override
  protected void configure() {
    // TODO Auto-generated method stub
    bind(ArirangAnalyzerRestAction.class).asEagerSingleton();
  }
}