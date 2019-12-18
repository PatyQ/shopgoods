package com.cy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("addredd")
public class Address extends BaseEntity {

    private Integer uid;
    private String person;
    private String address;
    private String phone;
    private String code;
    private Integer isdefault = 0;
}
