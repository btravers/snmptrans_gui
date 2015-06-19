package com.zenika.snmptrans.gui.service;

import com.zenika.snmptrans.gui.model.Query;
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
    public void push(SnmpProcess snmpProcess) throws IOException {
        if (snmpProcess.getId() == null) {
            SnmpProcess existingSnmpProcess = this.confRepository.get(snmpProcess.getServer().getHost(), snmpProcess.getServer().getPort());

            if (existingSnmpProcess == null) {
                this.confRepository.save(snmpProcess);
            } else {
                for (Query query : snmpProcess.getQueries()){
                    existingSnmpProcess.getQueries().add(query);
                }
                this.confRepository.update(existingSnmpProcess);
            }
        } else {
            this.confRepository.update(snmpProcess);
        }
    }

    @Override
    public void delete(String host, int port) {
        this.confRepository.delete(host, port);
    }
}
