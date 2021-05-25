package com.bmd.core.utils;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmd.core.pojo.po.BmdProduct;
import com.bmd.core.service.BmdProductService;
import com.bmd.es.dao.listDao;
import com.bmd.es.po.Tutu;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;


public class CanalClient {
    private listDao listDao;
    private BmdProductService bmdProductService;
    private RestHighLevelClient restHighLevelClient;

    public CanalClient(com.bmd.es.dao.listDao listDao, BmdProductService bmdProductService, RestHighLevelClient restHighLevelClient) {
        this.listDao = listDao;
        this.bmdProductService = bmdProductService;
        this.restHighLevelClient = restHighLevelClient;
    }

    public void canal() {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.29.122",
                11111), "test", "canal", "Canal@123456");
        int batchSize = 1000;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(1000 * 6);       //  刷新间隔
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    printEntry(message.getEntries());
                }
                connector.ack(batchId); // 提交确认
            }
        } finally {
            connector.disconnect();
        }
    }

    private void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage = null;

            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            EventType eventType = rowChage.getEventType();

            try {
                //  product
                if (entry.getHeader().getTableName().equals("bmd_product")) {
                    System.out.println("insert");
                    for (RowData rowData : rowChage.getRowDatasList()) {
                        //  delete
                        if (eventType == EventType.DELETE) {
                            for (Column column : rowData.getBeforeColumnsList()) {
                                if (column.getName().equals("id")) {
                                    listDao.deleteById(Long.valueOf(column.getValue()));
                                }
                            }
                            //  insert
                        } else if (eventType == EventType.INSERT) {
                            Tutu tutu = new Tutu();
                            for (Column column : rowData.getAfterColumnsList()) {
                                if (column.getName().equals("uid")) {
                                    tutu.setUname(column.getValue());
                                }
                                if (column.getName().equals("pname")) {
                                    tutu.setPname(column.getValue());
                                }
                                if (column.getName().equals("pdesc")) {
                                    tutu.setDesc(column.getValue());
                                }
                                if (column.getName().equals("id")) {
                                    tutu.setId(Long.valueOf(column.getValue()));
                                }
                            }
                            listDao.save(tutu);
                        } else {
                            //  逻辑删除
                            for (Column column : rowData.getBeforeColumnsList()) {
                                if (column.getName().equals("id")) {
                                    listDao.deleteById(Long.valueOf(column.getValue()));
                                }
                            }
                        }
                    }
                }
                //  picture
                if (entry.getHeader().getTableName().equals("bmd_picture")) {
                    for (RowData rowData : rowChage.getRowDatasList()) {
                        //  insert
                        if (eventType == EventType.INSERT) {

                            String uname = "";
                            String pname = "";
                            String url = "";
                            Long id = 0L;
                            Boolean u = false;
                            for (Column column : rowData.getAfterColumnsList()) {
                                if (column.getName().equals("mapper_id")) {
                                    uname = column.getValue();
                                }
                                if (column.getName().equals("pid")) {
                                    pname = column.getValue();
                                }
                                if (column.getName().equals("url")) {
                                    url = column.getValue();
                                }
                                if (column.getName().equals("is_u")) {
                                    if (column.getValue().equals("1")) {
                                        u = true;
                                    } else {
                                        u = false;
                                    }
                                }
                            }
                            if (!u) {
                                //  get id
                                QueryWrapper<BmdProduct> wrapper = new QueryWrapper<>();
                                wrapper.eq("uid", uname);
                                wrapper.eq("pname", pname);
                                wrapper.select("id");
                                Map<String, Object> map = bmdProductService.getMap(wrapper);
                                if (map==null) return;
                                id = (Long) map.get("id");
                                //  update url
                                UpdateRequest updateRequest = new UpdateRequest("tutu", "tutu", id.toString());
                                updateRequest.doc("{\"url\":\"" + url + "\"}", XContentType.JSON);
                                restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}