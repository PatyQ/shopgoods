package com.cy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
//自动映射表名goods_images
public class GoodsImages extends BaseEntity implements Serializable {

    private Integer gid;
    private String info;
    private String url;
    private Integer isfengmian = 0;//0-非封面 1-封面

}
