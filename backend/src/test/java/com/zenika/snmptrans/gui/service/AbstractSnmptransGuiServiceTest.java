package com.zenika.snmptrans.gui.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.snmptrans.gui.model.OutputWriter;
import com.zenika.snmptrans.gui.model.Query;
import com.zenika.snmptrans.gui.model.Server;
import com.zenika.snmptrans.gui.model.SnmpProcess;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.MapEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

public abstract class AbstractSnmptransGuiServiceTest {

    @Autowired
    protected SnmptransGuiService snmptransGuiService;

    @Autowired
    protected ObjectMapper mapper;

    protected abstract void flushChanges();

    /**
     * Load some data in the repository:
     *  - /test/document1.json
     *  - /test/document2.json
     */
    @Before
    public abstract void setUp();

    /**
     * If your repository allows transactional actions, you do not need to remove data.
     * Otherwise clean all data in your repository.
     */
    @After
    public abstract void tearDown();

    @Test
    public void shouldFindAllServers() {
        Collection<Map<String, Object>> list = this.snmptransGuiService.findAllServers();
        Assertions.assertThat(list.size()).isEqualTo(2);

        Collection<Map<String, Object>> expectedList = new ArrayList<>();

        Map<String, Object> expectedServer = new HashMap<>();
        expectedServer.put("host", "localhost");
        expectedServer.put("port", 161);
        expectedList.add(expectedServer);

        expectedServer = new HashMap<>();
        expectedServer.put("host", "192.168.0.1");
        expectedServer.put("port", 161);
        expectedList.add(expectedServer);

        Assertions.assertThat(list).containsAll(expectedList);
    }

    @Test
    public void shouldGetSnmpProcess() throws IOException {
        SnmpProcess snmpProcess = this.snmptransGuiService.get("localhost", 161);
        Assertions.assertThat(snmpProcess).isNotNull();

        Assertions.assertThat(snmpProcess.getId()).isNotNull();
        Assertions.assertThat(snmpProcess.getServer().getDescription()).isEqualTo("Ubuntu 14.04LTS");
        Assertions.assertThat(snmpProcess.getServer().getHost()).isEqualTo("localhost");
        Assertions.assertThat(snmpProcess.getServer().getPort()).isEqualTo(161);
        Assertions.assertThat(snmpProcess.getServer().getVersion()).isEqualTo(Server.SnmpVersion.V2c);
        Assertions.assertThat(snmpProcess.getServer().getCommunity()).isEqualTo("public");

        Assertions.assertThat(snmpProcess.getQueries().size()).isEqualTo(1);
        Query query = snmpProcess.getQueries().iterator().next();
        Assertions.assertThat(query.getObj()).isEqualTo("1.3.6.1.4.1.2021.11");
        Assertions.assertThat(query.getTypeName()).isEqualTo("2");
        Assertions.assertThat(query.getResultAlias()).isEqualTo("cpu");
        Assertions.assertThat(query.getAttr().size()).isEqualTo(6);
        Assertions.assertThat(query.getAttr()).containsAll(Arrays.asList("9", "50", "10", "52", "11", "53"));

        Assertions.assertThat(snmpProcess.getWriters().size()).isEqualTo(1);
        OutputWriter writer = snmpProcess.getWriters().iterator().next();
        Assertions.assertThat(writer.getAtClass()).isEqualTo("com.zenika.snmptrans.model.output.BluefloodWriter");
        Assertions.assertThat(writer.getSettings()).containsExactly(MapEntry.entry("port", 19000), MapEntry.entry("host", "localhost"));
    }

    @Test
    public void shouldNotFindSnmpProcess() throws IOException {
        SnmpProcess snmpProcess = this.snmptransGuiService.get("test", 161);
        Assertions.assertThat(snmpProcess).isNull();
    }

    @Test
    public void shouldCreateSnmpProcess() throws IOException {
        SnmpProcess snmpProcess = this.snmptransGuiService.get("192.168.0.2", 161);
        Assertions.assertThat(snmpProcess).isNull();

        snmpProcess = mapper.readValue(getClass().getResourceAsStream("/document3.json"), SnmpProcess.class);
        this.snmptransGuiService.push(snmpProcess);
        this.flushChanges();

        snmpProcess = this.snmptransGuiService.get("192.168.0.2", 161);
        Assertions.assertThat(snmpProcess).isNotNull();

        Assertions.assertThat(snmpProcess.getId()).isNotNull();
        Assertions.assertThat(snmpProcess.getServer().getDescription()).isEqualTo("Ubuntu 14.04LTS");
        Assertions.assertThat(snmpProcess.getServer().getHost()).isEqualTo("192.168.0.2");
        Assertions.assertThat(snmpProcess.getServer().getPort()).isEqualTo(161);
        Assertions.assertThat(snmpProcess.getServer().getVersion()).isEqualTo(Server.SnmpVersion.V2c);
        Assertions.assertThat(snmpProcess.getServer().getCommunity()).isEqualTo("public");

        Assertions.assertThat(snmpProcess.getQueries().size()).isEqualTo(1);
        Query query = snmpProcess.getQueries().iterator().next();
        Assertions.assertThat(query.getObj()).isEqualTo("1.3.6.1.4.1.2021.11");
        Assertions.assertThat(query.getTypeName()).isEqualTo("2");
        Assertions.assertThat(query.getResultAlias()).isEqualTo("cpu");
        Assertions.assertThat(query.getAttr().size()).isEqualTo(6);
        Assertions.assertThat(query.getAttr()).containsAll(Arrays.asList("9", "50", "10", "52", "11", "53"));

        Assertions.assertThat(snmpProcess.getWriters().size()).isEqualTo(1);
        OutputWriter writer = snmpProcess.getWriters().iterator().next();
        Assertions.assertThat(writer.getAtClass()).isEqualTo("com.zenika.snmptrans.model.output.BluefloodWriter");
        Assertions.assertThat(writer.getSettings()).containsExactly(MapEntry.entry("port", 19000), MapEntry.entry("host", "localhost"));
    }

    @Test
    public void shouldpdateSnmpProcess() throws IOException {
        SnmpProcess snmpProcess = this.snmptransGuiService.get("localhost", 161);
        Assertions.assertThat(snmpProcess).isNotNull();
        snmpProcess.getServer().setHost("127.0.0.1");
        this.snmptransGuiService.push(snmpProcess);
        this.flushChanges();

        snmpProcess = this.snmptransGuiService.get("localhost", 161);
        Assertions.assertThat(snmpProcess).isNull();
        snmpProcess = this.snmptransGuiService.get("127.0.0.1", 161);
        Assertions.assertThat(snmpProcess).isNotNull();

        Assertions.assertThat(snmpProcess.getId()).isNotNull();
        Assertions.assertThat(snmpProcess.getServer().getDescription()).isEqualTo("Ubuntu 14.04LTS");
        Assertions.assertThat(snmpProcess.getServer().getHost()).isEqualTo("127.0.0.1");
        Assertions.assertThat(snmpProcess.getServer().getPort()).isEqualTo(161);
        Assertions.assertThat(snmpProcess.getServer().getVersion()).isEqualTo(Server.SnmpVersion.V2c);
        Assertions.assertThat(snmpProcess.getServer().getCommunity()).isEqualTo("public");

        Assertions.assertThat(snmpProcess.getQueries().size()).isEqualTo(1);
        Query query = snmpProcess.getQueries().iterator().next();
        Assertions.assertThat(query.getObj()).isEqualTo("1.3.6.1.4.1.2021.11");
        Assertions.assertThat(query.getTypeName()).isEqualTo("2");
        Assertions.assertThat(query.getResultAlias()).isEqualTo("cpu");
        Assertions.assertThat(query.getAttr().size()).isEqualTo(6);
        Assertions.assertThat(query.getAttr()).containsAll(Arrays.asList("9", "50", "10", "52", "11", "53"));

        Assertions.assertThat(snmpProcess.getWriters().size()).isEqualTo(1);
        OutputWriter writer = snmpProcess.getWriters().iterator().next();
        Assertions.assertThat(writer.getAtClass()).isEqualTo("com.zenika.snmptrans.model.output.BluefloodWriter");
        Assertions.assertThat(writer.getSettings()).containsExactly(MapEntry.entry("port", 19000), MapEntry.entry("host", "localhost"));
    }

    @Test
    public void shouldDelete() throws IOException {
        SnmpProcess snmpProcess = this.snmptransGuiService.get("localhost", 161);
        Assertions.assertThat(snmpProcess).isNotNull();

        this.snmptransGuiService.delete("localhost", 161);
        this.flushChanges();

        snmpProcess = this.snmptransGuiService.get("localhost", 161);
        Assertions.assertThat(snmpProcess).isNull();
    }

}
