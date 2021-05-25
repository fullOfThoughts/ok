package com.bmd.core;


import cn.hutool.core.util.IdUtil;
import com.bmd.common.result.R;
import com.bmd.core.service.BmdPictureService;
import com.bmd.core.service.BmdProductService;
import com.bmd.core.service.BmdUserService;
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class esTest {
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private BmdPictureService bmdPictureService;
    @Autowired
    private BmdProductService bmdProductService;
    @Autowired
    private BmdUserService bmdUserService;
    @Autowired
    private com.bmd.es.dao.listDao listDao;


    @Test
    public void sss() throws IOException {
//        //  构建搜索对象
//        SearchRequest searchRequest = new SearchRequest("list");
//        //  构造搜索条件
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder
//                .query(QueryBuilders.termQuery("pname", "奥特"));
//        searchRequest.types("list").source(searchSourceBuilder);
//        //  执行搜索
//        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//        //  结果处理
//        SearchHit[] hits = search.getHits().getHits();
//        for (SearchHit hit : hits) {
//            Map<String, DocumentField> fields = hit.getFields();
//            System.out.println(fields);
//        }
//        getOneKey("list", "pname", "奥特");
//            getTotal();

    }

    public R getTips(String index, String key, String keyword) {
        try {
            //  创建搜索对象
            SearchRequest request = new SearchRequest(index);
            //  高亮对象
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .preTags("<span style=\"color:red\">")
                    .postTags("</span>")
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

    @Test
    public void getTotal() {
//        SearchRequest request = new SearchRequest(index);
//        SearchSourceBuilder query = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
//        request.source(query);
//        SearchResponse search = restHighLevelClient.search(request, RequestOptions.DEFAULT);
//        SearchHit[] hits = search.getHits().getHits();
//        for (SearchHit hit : hits) {
//            Map<String, DocumentField> fields = hit.getFields();
//            Set<String> strings = fields.keySet();
//        }
//        Iterable<Tutu> search1 = listDao.search(QueryBuilders.matchAllQuery());
//        for (Tutu tutu : search1) {
//            System.out.println(tutu);
//        }
        listDao.deleteAll();
    }

    @Test
    public void assa() {
//        Page<Tutu> search = listDao.search(QueryBuilders.matchAllQuery(), PageRequest.of(1, 2));
//        long totalElements = search.getTotalElements();
//        System.out.println(totalElements);
//        for (Tutu tutu : search) {
//            System.out.println(tutu);
//        }
//        getItems("", 1, 3);
        Tutu tutu = new Tutu();
        tutu.setId(IdUtil.getSnowflake(1, 1).nextId());
        tutu.setPname("你好2hao");
        tutu.setUname("qs");
        tutu.setDesc("12121212");
        tutu.setUrl("https://baomidou.oss-cn-hangzhou.aliyuncs.com/product/2021/04/27/e8143495422b4fe9bbea44831406866c.jpg");
        listDao.save(tutu);
    }

    public R getItems(String keyword, Integer page, Integer pageSize) {
        try {
            List<Tutu> tutus = new ArrayList<>();
            if ("".equals(keyword)) {
                //  关键字为空
                Page<Tutu> search = listDao.search(QueryBuilders.matchAllQuery(), PageRequest.of(page, pageSize));
                tutus = search.getContent();
            } else {
                //  带关键字查寻
                Page<Tutu> search = listDao.search(QueryBuilders.queryStringQuery(keyword).field("pname").field("pdesc"), PageRequest.of(page, pageSize));
                tutus = search.getContent();

            }
            return R.ok().data("list", tutus);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error().message("查询失败");
        }
    }



    @Test
    public void aa() {
//        BmdPicture bmdPicture = new BmdPicture();
//        bmdPicture.setMapperId("qw");
//        bmdPicture.setPid("1212");
//        bmdPicture.setU(true);
//        bmdPicture.setUrl("wqwqwqwq");
//        bmdPictureService.save(bmdPicture);
//        bmdPictureService.removeById(1387039424759582722L);
//        BmdProduct bmdProduct = new BmdProduct();
//        bmdProduct.setPcategory(1);
//        bmdProduct.setPname("lasd");
//        Long a = IdUtil.getSnowflake(1,1).nextId();
//        bmdProduct.setId(a);
//        System.out.println(a);
//        bmdProduct.setUid("assa");
//        bmdProduct.setPcontact("asdasd");
//        bmdProductService.save(bmdProduct);
//        bmdProductService.removeById(1387050701162680320L);
    }
}
