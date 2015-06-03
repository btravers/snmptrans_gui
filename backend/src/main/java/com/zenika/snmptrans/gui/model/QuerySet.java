package com.zenika.snmptrans.gui.model;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.Collection;

public class QuerySet {

    private String description;

    @Valid
    @NotEmpty
    private Collection<Query> queries;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Query> getQueries() {
        return queries;
    }

    public void setQueries(Collection<Query> queries) {
        this.queries = queries;
    }

}
