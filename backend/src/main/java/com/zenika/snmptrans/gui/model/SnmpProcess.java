package com.zenika.snmptrans.gui.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

public class SnmpProcess {

    private String id;

    @Valid
    @NotEmpty
    private Collection<OutputWriter> writers;

    @Valid
    @NotNull
    private Server server;

    @Valid
    @NotEmpty
    private Collection<QuerySet> querySets;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Collection<OutputWriter> getWriters() {
        return writers;
    }

    public void setWriters(Collection<OutputWriter> writers) {
        this.writers = writers;
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Collection<QuerySet> getQuerySets() {
        return querySets;
    }

    public void setQuerySets(Collection<QuerySet> querySets) {
        this.querySets = querySets;
    }

}
