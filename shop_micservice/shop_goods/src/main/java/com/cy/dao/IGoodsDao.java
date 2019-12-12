package com.cy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.entity.Goods;
import org.apache.ibatis.annotations.Param;

public interface IGoodsDao extends BaseMapper<Goods> {

    IPage<Goods> selectPageVo(Page<Goods> page);

    Goods selGoodsById(@Param("id") Integer id);
}
