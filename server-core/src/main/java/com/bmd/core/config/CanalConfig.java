package com.bmd.core.config;

import com.bmd.core.service.BmdProductService;
import com.bmd.core.utils.CanalClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class CanalConfig implements ApplicationRunner {
    @Autowired
    private com.bmd.es.dao.listDao listDao;
    @Autowired
    private BmdProductService bmdProductService;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                CanalClient canalClient = new CanalClient(listDao, bmdProductService, restHighLevelClient);
                canalClient.canal();
            }
        }).start();
    }

}
