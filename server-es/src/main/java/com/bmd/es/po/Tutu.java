package com.bmd.es.po;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Document(indexName = "tutu",indexStoreType = "tutu")
@ToString
public class Tutu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    @Field(type = FieldType.Keyword)
    private String uname;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String pname;
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String desc;
    @Field(type = FieldType.Keyword)
    private String url;

}
