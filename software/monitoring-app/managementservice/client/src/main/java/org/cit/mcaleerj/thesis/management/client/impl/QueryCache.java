package org.cit.mcaleerj.thesis.management.client.impl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Basic query cache.
 */
class QueryCache {

  private final String queryBasePath;

  private Map<String, String> cache = new HashMap<>();

  /**
   * Constructor.
   * @param queryBasePath
   */
  QueryCache(final String queryBasePath) {
    this.queryBasePath = queryBasePath + File.separatorChar;
  }

  /**
   * Returns query with the given name.
   * @param queryName query name
   * @return query string
   * @throws IOException
   */
  public String getQuery(final String queryName) throws IOException {
    String query = this.cache.get(queryName);
    if(!StringUtils.isBlank(query)) {
      return query;
    }
    final String queryPath = this.queryBasePath + queryName;

    query = IOUtils.toString(QueryCache.class.getResourceAsStream(queryPath), "UTF-8");
    this.cache.put(queryName, query);
    return query;
  }

}
