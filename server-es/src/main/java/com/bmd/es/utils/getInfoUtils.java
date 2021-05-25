package com.bmd.es.utils;


import com.bmd.common.result.R;
import com.bmd.es.dao.listDao;
import com.bmd.es.po.Tutu;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class getInfoUtils {
    public static R getItems(listDao listDao, String keyword, Integer page, Integer pageSize) {
        try {
            List<Tutu> tutus = new ArrayList<>();
            HashMap<String, Object> map = new HashMap<>();
            if ("".equals(keyword)) {
                //  关键字为空
                Page<Tutu> search = listDao.search(QueryBuilders.matchAllQuery(), PageRequest.of(page, pageSize));
                tutus = search.getContent();
                map.put("total", search.getTotalElements());
            } else {
                //  带关键字查寻
                Page<Tutu> search = listDao.search(QueryBuilders.queryStringQuery(keyword).field("pname").field("pdesc"), PageRequest.of(page, pageSize));
                tutus = search.getContent();
                map.put("total", search.getTotalElements());
            }
            map.put("list", tutus);
            return R.ok().date(map);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message("查询失败");
        }
    }

    public static R getTips(RestHighLevelClient restHighLevelClient, String index, String key, String keyword) {
        try {
            //  创建搜索对象
            SearchRequest request = new SearchRequest(index);
            //  高亮对象
            HighlightBuilder highlightBuilder = new HighlightBuilder()
//                    .preTags("<span style=\"color:red\">")
//                    .postTags("</span>")
                    .preTags("")
                    .postTags("")
                    .field(key);
            //  构造搜索条件
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder
                    .query(QueryBuilders.queryStringQuery(keyword).field(key))
                    .from(0)
                    .size(6)        //  只给六条建议
                    .highlighter(highlightBuilder);
            request.source(sourceBuilder);
            //  执行搜索结果
            SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = search.getHits().getHits();
            ArrayList<Map<String, String>> list = new ArrayList<>();
            for (SearchHit hit : hits) {
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                HighlightField highlightField = highlightFields.get(key);
                HashMap<String, String> map = new HashMap<>();
                map.put("value", highlightField.fragments()[0].toString());
                list.add(map);
            }
            return R.ok().data("list", list);
        } catch (IOException e) {
            e.printStackTrace();
            return R.error().message("查询失败");
        }
    }
}
