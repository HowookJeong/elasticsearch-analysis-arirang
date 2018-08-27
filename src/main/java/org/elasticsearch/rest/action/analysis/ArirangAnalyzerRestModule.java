package org.elasticsearch.rest.action.analysis;

import org.elasticsearch.common.inject.AbstractModule;

/**
 * Created by jeonghoug on 2016. 3. 16..
 */
public class ArirangAnalyzerRestModule  extends AbstractModule {

  @Override
  protected void configure() {
    // TODO Auto-generated method stub
    bind(ArirangAnalyzerRestAction.class).asEagerSingleton();
  }
}