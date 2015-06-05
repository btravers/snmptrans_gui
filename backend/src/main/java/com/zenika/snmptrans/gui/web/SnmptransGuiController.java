package com.zenika.snmptrans.gui.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zenika.snmptrans.gui.model.SnmpProcess;
import com.zenika.snmptrans.gui.service.SnmptransGuiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

@Controller
public class SnmptransGuiController {

    private static final Logger logger = LoggerFactory.getLogger(SnmptransGuiController.class);

    @Autowired
    private SnmptransGuiService snmptransGuiService;

    @Autowired
    private ObjectMapper mapper;

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/snmpprocess", method = RequestMethod.GET)
    @ResponseBody
    public SnmpProcess get(@RequestParam(value = "host", required = true) String host,
                           @RequestParam(value = "port", required = true) int port) {
        try {
            return this.snmptransGuiService.get(host, port);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/snmpprocess/all", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Map<String, Object>> findAllServers() {
        return this.snmptransGuiService.findAllServers();
    }

    @RequestMapping(value = "/snmpprocess", method = RequestMethod.POST)
    @ResponseBody
    public void push(@Valid @RequestBody SnmpProcess snmpProcess) {
        try {
            this.snmptransGuiService.push(snmpProcess);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/snmpprocess", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@RequestParam(value = "host", required = true) String host,
                       @RequestParam(value = "port", required = true) int port) {
        this.snmptransGuiService.delete(host, port);
    }

    @RequestMapping(value = "/snmpprocess/download", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<InputStreamResource> download(@RequestParam(value = "host", required = true) String host,
                                                        @RequestParam(value = "port", required = true) int port) {
        try {
            SnmpProcess snmpProcess = this.snmptransGuiService.get(host, port);

            byte[] content = this.mapper.writeValueAsBytes(snmpProcess);

            HttpHeaders respHeaders = new HttpHeaders();
            respHeaders.setContentType(MediaType.APPLICATION_JSON);
            respHeaders.setContentLength(content.length);
            respHeaders.setContentDispositionFormData("attachment", "server_"
                    + host + ":" + port + ".json");

            return new ResponseEntity<>(
                    new InputStreamResource(new ByteArrayInputStream(content)),
                    respHeaders, HttpStatus.OK);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(value = "/snmpprocess/upload", method = RequestMethod.POST)
    @ResponseBody
    public void upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String confFile = new String(bytes);

                SnmpProcess snmpProcess = mapper.readValue(confFile, SnmpProcess.class);

                this.snmptransGuiService.push(snmpProcess);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
