package com.example.search.service;

import com.example.search.DTO.User;
import com.example.search.util.DSLUtil;
import com.example.search.util.JsonUtils;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


/**
 * @author hyy
 */
@Service
@Slf4j
public class SearchService {
    @Autowired
    private JestClient jestClient;

    public void save(User user) {
        try {
            DocumentResult execute = jestClient.execute(new Index.Builder(user).index("hyy").type("user").id(user.getId().toString()).build());
            log.info(execute.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void del(Long id) {
        try {
            DocumentResult execute = jestClient.execute(new Delete.Builder(id.toString()).index("hyy").type("user").build());
            log.info(execute.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(User user) {
        try {
            DocumentResult execute = jestClient.execute(new Update.Builder(JsonUtils.toESUpdateString(user)).index("hyy").type("user").id(user.getId().toString()).build());
            log.info(execute.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User get(User user) {
        try {
            JestResult jestResult = jestClient.execute(new Get.Builder("hyy", user.getId().toString()).type("user").build());
            return jestResult.getSourceAsObject(User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> search(User user) {
       // String query = dslString(user);
        String query = DSLUtil.getDSLString(user);
        try {
            SearchResult execute = jestClient.execute(new Search.Builder(query).addIndex("hyy").addType("user").build());
//            List<SearchResult.Hit<User, Void>> hits = execute.getHits(User.class);
//            List<User> collect = hits.stream().map(h -> h.source).collect(Collectors.toList());
            log.info(execute.toString());
            return execute.getSourceAsObjectList(User.class,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String dslString(User user) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(10);
        searchSourceBuilder.version(true);
        searchSourceBuilder.sort("age", SortOrder.DESC);
//        match 和term的区别是，match 查询的时候，elasticsearch会根据你给定的字段提供合适的分析器，而term查询时不会有分析器分析的过程
//        boolQueryBuilder.filter(QueryBuilders.matchQuery("name",user.getName()));
//        prefix前缀匹配查询
//        boolQueryBuilder.filter(QueryBuilders.prefixQuery("name","h"));
//        范围查找
//        RangeQueryBuilder age = QueryBuilders.rangeQuery("age");
//        age.gte(20);
//        age.lte(30);
//        boolQueryBuilder.filter(age);
//        通配符查找
//        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("name","hy*"));
//        模糊查找(一直不生效)
//        boolQueryBuilder.filter(QueryBuilders.fuzzyQuery("name","y"));

        if(user.getAge() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("age",user.getAge()));

        }
       if(user.getId() != null) {
           boolQueryBuilder.filter(QueryBuilders.termQuery("id",user.getId()));
       }
       if(user.getName() != null) {
           // 模糊匹配
           boolQueryBuilder.filter(QueryBuilders.wildcardQuery("name","*" + user.getName() + "*"));
       }

        return searchSourceBuilder.toString();
    }
}
