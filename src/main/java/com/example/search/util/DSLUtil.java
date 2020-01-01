package com.example.search.util;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.lang.reflect.Field;

/**
 * @author hyy
 */
public class DSLUtil {
    public static String getDSLString(Object o) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        searchSourceBuilder.query(boolQueryBuilder);

        Field[] declaredFields = o.getClass().getDeclaredFields();
        try{
            for (Field field : declaredFields) {
                String name = field.getName();
                Object o1 = field.get(o);
                if(o1 != null) {
                    boolQueryBuilder.filter(QueryBuilders.termQuery(name,o1.toString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchSourceBuilder.toString();
    }
}
