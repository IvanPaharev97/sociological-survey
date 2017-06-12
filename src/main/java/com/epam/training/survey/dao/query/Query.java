package com.epam.training.survey.dao.query;

import java.util.ArrayList;
import java.util.List;

import com.epam.training.survey.dao.query.constant.QueryPart;

public class Query {
    private List<String> whereEqualsPredicates;
    private List<String> joinTables;
    private String queryStart;
    private String createOrUpdateBody;
    private StringBuilder stringBuilder;

    public Query(String queryType, String tableName) {
        super();
        this.whereEqualsPredicates = new ArrayList<>();
        this.joinTables = new ArrayList<>();
        this.queryStart = queryType + tableName;
    }
    
    public Query setCreateOrUpdateBody(String createOrUpdateBody) {
        this.createOrUpdateBody = createOrUpdateBody;
        return this;
    }
    
    public Query joinTable(String join) {
        joinTables.add(join);
        return this;
    }
    
    public Query whereEquals(String property) {
        if (!whereEqualsPredicates.isEmpty()) {
            int lastIndex = whereEqualsPredicates.size() - 1;
            whereEqualsPredicates.set(lastIndex, whereEqualsPredicates.get(lastIndex) + QueryPart.AND);
        }
        whereEqualsPredicates.add(String.format(QueryPart.EQUALS, property));
        return this;
    }
    
    public String getResultQuery() {
        stringBuilder = new StringBuilder(queryStart);
        if (createOrUpdateBody != null) {
            stringBuilder.append(createOrUpdateBody);
        }
        if (!joinTables.isEmpty()) {
            for (String join : joinTables) {
                stringBuilder.append(join);
            }
        }
        if (!whereEqualsPredicates.isEmpty()) {
            stringBuilder.append(QueryPart.WHERE_START);
            for (String whereEquals : whereEqualsPredicates) {
                stringBuilder.append(whereEquals);
            }
        }
        stringBuilder.append(QueryPart.QUERY_END);
        return stringBuilder.toString();
    }
    
}

