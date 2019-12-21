package com.cy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetils extends BaseEntity {

    private Integer oid;
    private Integer gid;
    private String subject;
    private BigDecimal price;
    private Integer number;
    private String fmurl;
    private BigDecimal detilsPrice;
}
