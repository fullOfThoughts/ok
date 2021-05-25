package com.bmd.es.dao;


import com.bmd.es.po.Tutu;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface listDao extends ElasticsearchRepository<Tutu,Long> {
}
