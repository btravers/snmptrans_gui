package com.zenika.snmptrans.gui.service;

import com.zenika.snmptrans.service.SnmpTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class SnmpTransformerRunner {

    @Autowired
    private SnmpTransformer snmpTransformer;

    private Thread thread;

    @PostConstruct
    public void setup() {
        thread = new Thread(snmpTransformer);
        thread.start();
    }

    @PreDestroy
    public void tearDown() {
        thread.interrupt();
    }
}
