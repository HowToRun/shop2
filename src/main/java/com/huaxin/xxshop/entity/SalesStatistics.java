/**
 * Copyright (C), 2015 - 2020 , 百分点信息科技有限公司
 *
 * @fileName: SalesStatistics
 * @author: huyih
 * @date: 2020/3/25 16:28
 * @description:
 * @history: <author>          <date>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.huaxin.xxshop.entity;

public class SalesStatistics {
    private String goodsid;
    private String name;
    private Integer saleNum;
    private Integer orderNum;
    private Double price2;
    private Double saleMoney;

    @Override
    public String toString() {
        return "SalesStatistics{" +
                "goodsid='" + goodsid + '\'' +
                ", name='" + name + '\'' +
                ", saleNum=" + saleNum +
                ", orderNum=" + orderNum +
                ", price2=" + price2 +
                ", saleMoney=" + saleMoney +
                '}';
    }

    public String getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(String goodsid) {
        this.goodsid = goodsid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Double getPrice2() {
        return price2;
    }

    public void setPrice2(Double price2) {
        this.price2 = price2;
    }

    public Double getSaleMoney() {
        return saleMoney;
    }

    public void setSaleMoney(Double saleMoney) {
        this.saleMoney = saleMoney;
    }
}
