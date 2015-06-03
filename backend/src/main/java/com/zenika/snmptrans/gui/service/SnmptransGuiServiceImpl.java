package com.zenika.snmptrans.gui.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zenika.snmptrans.gui.model.SnmpProcess;
import com.zenika.snmptrans.gui.repository.ConfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Service
public class SnmptransGuiServiceImpl implements SnmptransGuiService {

    @Autowired
    private ConfRepository confRepository;

    @Override
    public Collection<Map<String, Object>> findAllServers() {
        return this.confRepository.findAllServers();
    }

    @Override
    public SnmpProcess get(String host, int port) throws IOException {
        return this.confRepository.get(host, port);
    }

    @Override
    public void push(SnmpProcess snmpProcess) throws JsonProcessingException {
        if (snmpProcess.getId() == null) {
            this.confRepository.save(snmpProcess);
        } else {
            this.confRepository.update(snmpProcess);
        }
    }

    @Override
    public void delete(String host, int port) {
        this.delete(host, port);
    }
}
