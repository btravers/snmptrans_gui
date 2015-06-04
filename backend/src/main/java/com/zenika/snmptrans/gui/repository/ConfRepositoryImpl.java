package com.zenika.snmptrans.gui.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.snmptrans.gui.AppConfig;
import com.zenika.snmptrans.gui.model.SnmpProcess;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class ConfRepositoryImpl implements ConfRepository {

    @Autowired
    private Client client;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Collection<Map<String, Object>> findAllServers() {
        SearchResponse response = this.client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.CONF_TYPE)
                .setQuery(QueryBuilders.matchAllQuery())
                .addAggregation(
                        AggregationBuilders.terms("hosts").field("server.host").order(Terms.Order.term(true)).size(0)
                                .subAggregation(AggregationBuilders.terms("ports").field("server.port").order(Terms.Order.term(true)).size(0)))
                .execute().actionGet();

        List<Map<String, Object>> result = new ArrayList<>();

        Terms hosts = response.getAggregations().get("hosts");
        for (Terms.Bucket hostBucket : hosts.getBuckets()) {
            String host = hostBucket.getKey();
            Terms ports = hostBucket.getAggregations().get("ports");
            for (Terms.Bucket portBucket : ports.getBuckets()) {
                Map<String, Object> res = new HashMap<>();
                res.put("host", host);
                res.put("port", Integer.parseInt(portBucket.getKey()));
                result.add(res);
            }
        }

        return result;
    }

    @Override
    public SnmpProcess get(String host, int port) throws IOException {
        SearchResponse searchResponse = this.client.prepareSearch(AppConfig.INDEX)
                .setTypes(AppConfig.CONF_TYPE)
                .setQuery(QueryBuilders.matchAllQuery())
                .setPostFilter(
                        FilterBuilders
                                .boolFilter()
                                .must(FilterBuilders.termFilter("server.host", host))
                                .must(FilterBuilders.termFilter("server.port", port)))
                .execute().actionGet();

        if (searchResponse.getHits().getHits().length == 1) {
            SearchHit hit = searchResponse.getHits().getHits()[0];
            SnmpProcess snmpProcess = mapper.readValue(hit.getSourceAsString(), SnmpProcess.class);
            snmpProcess.setId(hit.getId());

            return snmpProcess;
        } else if (searchResponse.getHits().getHits().length == 0) {
            return null;
        } else {
            // TODO throw exception
            return null;
        }
    }

    @Override
    public void save(SnmpProcess snmpProcess) throws JsonProcessingException {
        String json = mapper.writeValueAsString(snmpProcess);

        this.client.prepareIndex(AppConfig.INDEX, AppConfig.CONF_TYPE)
                .setRefresh(true)
                .setSource(json).execute()
                .actionGet();
    }

    @Override
    public void delete(String host, int port) {
        this.client.prepareDeleteByQuery(AppConfig.INDEX)
                .setTypes(AppConfig.CONF_TYPE)
                .setQuery(
                        QueryBuilders
                                .boolQuery()
                                .must(QueryBuilders.termQuery("server.host",
                                        host))
                                .must(QueryBuilders.termQuery("server.port",
                                        port))).execute().actionGet();
    }

    @Override
    public void update(SnmpProcess snmpProcess) throws JsonProcessingException {
        String json = mapper.writeValueAsString(snmpProcess);

        this.client.prepareUpdate(AppConfig.INDEX, AppConfig.CONF_TYPE, snmpProcess.getId())
                .setRefresh(true)
                .setDoc(json)
                .execute().actionGet();
    }
}
