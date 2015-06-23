package com.zenika.snmptrans.gui.service;

import com.zenika.snmptrans.gui.AppConfig;
import com.zenika.snmptrans.gui.TestConfig;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ElasticSearchSnmptransGuiServiceSerivceTest extends AbstractSnmptransGuiServiceTest {

    @Autowired
    private Client client;

    @Override
    protected void flushChanges() {
        this.client.admin().indices().flush(new FlushRequest(AppConfig.INDEX)).actionGet();
    }

    @Override
    public void setUp() {
        try {
            this.client.prepareIndex(AppConfig.INDEX, AppConfig.CONF_TYPE)
                    .setSource(IOUtils.toString(getClass().getResourceAsStream("/document1.json")))
                    .execute().actionGet();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.client.prepareIndex(AppConfig.INDEX, AppConfig.CONF_TYPE)
                    .setSource(IOUtils.toString(getClass().getResourceAsStream("/document2.json")))
                    .execute().actionGet();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.flushChanges();
    }

    @Override
    public void tearDown() {
        this.client.prepareDeleteByQuery(AppConfig.INDEX)
                .setTypes(AppConfig.CONF_TYPE)
                .setQuery(QueryBuilders.matchAllQuery())
                .execute().actionGet();
    }
}
