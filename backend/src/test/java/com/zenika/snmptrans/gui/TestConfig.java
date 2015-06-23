package com.zenika.snmptrans.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Configuration
@Import(AppConfig.class)
public class TestConfig {

    private static final Logger logger = LoggerFactory.getLogger(TestConfig.class);

    @Bean
    @Primary
    public Client client(ObjectMapper mapper) {
        ImmutableSettings.Builder elasticsearchSettings = ImmutableSettings.settingsBuilder()
                .put("http.enabled", "false")
                .put("path.data", "target/elasticsearch-data");

        Node node = NodeBuilder.nodeBuilder().local(true).settings(elasticsearchSettings).node();
        Client client = node.client();

        try {
            client.admin().indices().create(new CreateIndexRequest(AppConfig.INDEX))
                    .actionGet();

            InputStream confMapping = getClass().getResourceAsStream("/conf_mapping.json");

            client.admin().indices().preparePutMapping(AppConfig.INDEX)
                    .setType(AppConfig.CONF_TYPE)
                    .setSource(mapper.readValue(confMapping, Map.class))
                    .execute().actionGet();
        } catch (ElasticsearchException e) {
            logger.info(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return client;
    }
}
