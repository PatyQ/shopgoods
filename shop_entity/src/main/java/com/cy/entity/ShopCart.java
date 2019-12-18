package com.cy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("shopcart")
public class ShopCart extends BaseEntity implements Serializable {

    private Integer uid;
    private Integer gid;
    private Integer number;
    private BigDecimal cartPrice;//商品小计

    @TableField(exist = false)
    private Goods goods;
}
