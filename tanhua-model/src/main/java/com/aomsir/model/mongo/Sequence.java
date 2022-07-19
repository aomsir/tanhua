package com.aomsir.model.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: Aomsir
 * @Date: 2022/7/19
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "sequence")
public class Sequence {
    private ObjectId id;

    private long seqId;  //自增序列

    private String collName;   // 集合名称
}
