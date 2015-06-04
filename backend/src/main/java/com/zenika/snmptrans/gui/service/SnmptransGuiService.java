package com.zenika.snmptrans.gui.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zenika.snmptrans.gui.model.SnmpProcess;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface SnmptransGuiService {

    Collection<Map<String, Object>> findAllServers();

    SnmpProcess get(String host, int port) throws IOException;

    void push(SnmpProcess snmpProcess) throws IOException;

    void delete(String host, int port);

}
