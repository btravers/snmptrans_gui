package com.zenika.snmptrans.gui.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zenika.snmptrans.gui.model.SnmpProcess;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface ConfRepository {

    Collection<Map<String, Object>> findAllServers();

    SnmpProcess get(String host, int port) throws IOException;

    void save(SnmpProcess snmpProcess) throws JsonProcessingException;

    void delete(String host, int port);

    void update(SnmpProcess snmpProcess) throws JsonProcessingException;
}
