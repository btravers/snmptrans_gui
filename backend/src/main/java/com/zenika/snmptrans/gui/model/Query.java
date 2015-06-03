package com.zenika.snmptrans.gui.model;

import javax.validation.constraints.NotNull;

public class Query {

    private String description;

    @NotNull
    private String oid;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

}
