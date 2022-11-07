package com.hilda.model.vo.product;

import java.util.Objects;

public class SkuSaleAttrJsonValueVo {

    private Long skuId;
    private String jsonValue;

    public SkuSaleAttrJsonValueVo() {
    }

    public SkuSaleAttrJsonValueVo(Long skuId, String jsonValue) {
        this.skuId = skuId;
        this.jsonValue = jsonValue;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkuSaleAttrJsonValueVo that = (SkuSaleAttrJsonValueVo) o;
        return Objects.equals(skuId, that.skuId) && Objects.equals(jsonValue, that.jsonValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuId, jsonValue);
    }
}
