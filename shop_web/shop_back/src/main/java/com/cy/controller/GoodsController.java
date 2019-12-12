package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cy.entity.Goods;
import com.cy.entity.GoodsImages;
import com.cy.entity.ResultData;
import com.cy.service.IGoodsService;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("goods")
public class GoodsController {

//    private String uploadPath = "C:" + File.separator + "Users" + File.separator + "hs" + File.separator + "Pictures" + File.separator + "IDEAimg";
//  private String uploadPath = "C:/Users/hs/Pictures/IDEAimg";

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Reference
    private IGoodsService goodsService;

    /**
     * 查询所有商品
     *
     * @return
     */
    @RequestMapping("goodslist")
    public String getGoodsList() {
//        int i = static.1.9.1.9.1.9.1/0;

        return "goodslist";
    }

    /**
     * 查询商品并分页
     *
     * @return
     */
    @RequestMapping("getGoodsPage")
    public String getGoodsPage(ModelMap map, Page<Goods> page) {
        IPage<Goods> goodsPage = goodsService.pagegetGoodsPage(page);


//        List list = new ArrayList();
        List<GoodsImages> goodsImages = goodsService.selGoodsImages();
        //将图片查出来并添加进去
        List<Goods> records = goodsPage.getRecords();
        for (Goods r : records) {
            for (GoodsImages i : goodsImages) {
                if (i.getGid().equals(r.getId()) && i.getIsfengmian() == 1) {
                    r.setFmurl(i.getUrl());
                }
            }
        }
        goodsPage.setRecords(records);
        if (goodsPage.getSize() == 0) {
            goodsPage.setSize(10);
        }
        System.out.println(goodsPage);
//        map.put("records",records);
        //参数处理正常
        map.put("page", goodsPage);
        map.put("url", "goods/getGoodsPage");
        map.put("limit", "[1,3,5,10]");
        return "goodslist";
    }

    @RequestMapping("uploader")
    @ResponseBody
    public ResultData<String> uploader(MultipartFile file) {
        StorePath storePath = null;
        try {
            //会返回一个路径
            storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(
                    file.getInputStream(),
                    file.getSize(),
                    "JPG",
                    null
            );

            System.out.println("nigx地址" + storePath.getFullPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


//        System.out.println("接收到文件" + file.getOriginalFilename());
//        //准备文件名称
//        String filename = UUID.randomUUID().toString();
////        String path = uploadPath + File.separator + filename;
//        String path = uploadPath + "/" + filename;
//
//        try (
//                InputStream in = file.getInputStream();
//                OutputStream out = new FileOutputStream(path)
//        ) {
//            IOUtils.copy(in, out);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return new ResultData().setCode(ResultData.ResultCodeList.OK).setData(storePath.getFullPath());
//        return new ResultData().setCode(ResultData.ResultCodeList.OK).setData(path);
    }


//    @RequestMapping("showimg")
//    public void showimg(String imgPath, HttpServletResponse response) {
//        try (
//                InputStream in = new FileInputStream(imgPath);
//                OutputStream out = response.getOutputStream();
//        ) {
////            将本地图片拷贝到浏览器
//            IOUtils.copy(in, out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    @RequestMapping(value = "ajax")
    @ResponseBody
    public ResultData<String> ajaxDemo() {
//        int i = static.1.9.1.9.1.9.1/0;
        return new ResultData<String>().setCode("200");
    }


    @RequestMapping("insert")
    public String insert(Goods goods) {
        System.out.println("insert接收到的值: " + goods);
        Integer i = goodsService.addGoods(goods);
        System.out.println(i);
        return "redirect:/goods/getGoodsPage";
    }

}
