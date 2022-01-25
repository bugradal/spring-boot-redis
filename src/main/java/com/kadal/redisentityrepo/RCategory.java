package com.kadal.redisentityrepo;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@RedisHash("category")
@Data
public class RCategory {

    @Id
    private String id;
    @Indexed
    private Integer cid;
    @Indexed
    private String title;

}