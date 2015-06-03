package com.zenika.snmptrans.gui.web;

import com.zenika.snmptrans.gui.model.SnmpProcess;
import com.zenika.snmptrans.gui.service.SnmptransGuiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class SnmptransGuiController {

    private static final Logger logger = LoggerFactory.getLogger(SnmptransGuiController.class);

    @Autowired
    private SnmptransGuiService snmptransGuiService;

    @RequestMapping("/")
    public String index() {
        return "index.html";
    }

    @RequestMapping(value = "/server", method = RequestMethod.GET)
    @ResponseBody
    public SnmpProcess get(@RequestParam(value = "host", required = true) String host,
                           @RequestParam(value = "port", required = true) int port) {
        try {
            return this.snmptransGuiService.get(host, port);
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            return null;
        }
    }
}
