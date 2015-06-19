package com.zenika.snmptrans.gui.service;

import com.zenika.snmptrans.service.SnmpTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SnmpTransformerRunner {

    @Autowired
    private SnmpTransformer snmpTransformer;

    @PostConstruct
    public void init() {
        Thread thread = new Thread(snmpTransformer);
        thread.start();
    }
}
