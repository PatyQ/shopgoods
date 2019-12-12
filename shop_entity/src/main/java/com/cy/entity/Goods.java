package com.cy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors
@TableName("goods")
public class Goods extends BaseEntity implements Serializable {

    private String subject;
    private String info;
    private BigDecimal price;
    private Integer save;

    @TableField(exist = false)
    private String fmurl;

    @TableField(exist = false)
    private List<String> otherurl;

}
