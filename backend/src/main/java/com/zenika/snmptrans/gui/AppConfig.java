package com.zenika.snmptrans.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Configuration
@Import(com.zenika.snmptrans.AppConfig.class)
@ComponentScan({"com.zenika.back.service", "com.zenika.back.repository"})
public class AppConfig {
    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    public static final String INDEX = ".snmptrans";
    public static final String CONF_TYPE = "conf";

    @Value("${elasticsearch.host:}")
    private String host;

    @Value("${elasticsearch.path:}")
    private String path;

    @Value("${java.io.tmpdir}")
    private String tmpdir;

    @Value("${elasticsearch.cluster.name:elasticsearch}")
    private String clusterName;

    @Bean
    public static PropertyPlaceholderConfigurer configurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setIgnoreResourceNotFound(true);
        ppc.setSearchSystemEnvironment(true);
        ppc.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return ppc;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean(name = "client")
    public Client client() {
        logger.info("Elasticsearch client instantiation for prod");
        Client client = null;
        if (this.host.isEmpty()) {
            if (this.path.isEmpty()) {
                this.path = this.tmpdir;
            }
            Node node = NodeBuilder
                    .nodeBuilder()
                    .clusterName(this.clusterName)
                    .settings(
                            ImmutableSettings
                                    .settingsBuilder()
                                    .put("path.home",
                                            this.path + "/elasticsearch")
                                    .put("path.data",
                                            this.path + "/elasticsearch/data")
                                    .put("path.work",
                                            this.path + "/elasticsearch/work")
                                    .put("path.logs",
                                            this.path + "/elasticsearch/logs")
                                    .build()).node();
            client = node.client();
        } else {
            client = new TransportClient();
            String[] host = this.host.split(":");
            TransportAddress address = new InetSocketTransportAddress(host[0],
                    Integer.parseInt(host[1]));
            ((TransportClient) client).addTransportAddress(address);
        }

        try {
            client.admin().indices().create(new CreateIndexRequest(INDEX))
                    .actionGet();

            InputStream confMapping = getClass().getResourceAsStream("/conf_mapping.json");

            ObjectMapper mapper = this.objectMapper();

            client.admin().indices().preparePutMapping(INDEX)
                    .setType(CONF_TYPE)
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
