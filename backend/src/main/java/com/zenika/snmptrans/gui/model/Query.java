package com.zenika.snmptrans.gui.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Collection;

public class Query {

    @NotNull
    private String obj;
    private String resultAlias;
    @NotEmpty
    private Collection<Attribute> attr;
    private String typeName;

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getResultAlias() {
        return resultAlias;
    }

    public void setResultAlias(String resultAlias) {
        this.resultAlias = resultAlias;
    }

    public Collection<Attribute> getAttr() {
        return attr;
    }

    public void setAttr(Collection<Attribute> attr) {
        this.attr = attr;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
