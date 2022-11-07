package com.hilda.model.vo.item;

import com.hilda.model.bean.base.BaseEntity;
import com.hilda.model.bean.product.BaseCategoryView;
import com.hilda.model.bean.product.SkuInfo;
import com.hilda.model.bean.product.SpuSaleAttr;

import java.util.List;
import java.util.Objects;

public class SkuDetailVo extends BaseEntity {

    private BaseCategoryView categoryView;
    private SkuInfo skuInfo;
    private List<SpuSaleAttr> spuSaleAttrList;
    private String valuesSkuJson;

    public SkuDetailVo() {
    }

    public SkuDetailVo(BaseCategoryView categoryView, SkuInfo skuInfo, List<SpuSaleAttr> spuSaleAttrList, String valuesSkuJson) {
        this.categoryView = categoryView;
        this.skuInfo = skuInfo;
        this.spuSaleAttrList = spuSaleAttrList;
        this.valuesSkuJson = valuesSkuJson;
    }

    @Override
    public String toString() {
        return "ItemDetailVo{" +
                "categoryView=" + categoryView +
                ", skuInfo=" + skuInfo +
                ", spuSaleAttrList=" + spuSaleAttrList +
                ", valuesSkuJson='" + valuesSkuJson + '\'' +
                '}';
    }

    public BaseCategoryView getCategoryView() {
        return categoryView;
    }

    public void setCategoryView(BaseCategoryView categoryView) {
        this.categoryView = categoryView;
    }

    public SkuInfo getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(SkuInfo skuInfo) {
        this.skuInfo = skuInfo;
    }

    public List<SpuSaleAttr> getSpuSaleAttrList() {
        return spuSaleAttrList;
    }

    public void setSpuSaleAttrList(List<SpuSaleAttr> spuSaleAttrList) {
        this.spuSaleAttrList = spuSaleAttrList;
    }

    public String getValuesSkuJson() {
        return valuesSkuJson;
    }

    public void setValuesSkuJson(String valuesSkuJson) {
        this.valuesSkuJson = valuesSkuJson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SkuDetailVo that = (SkuDetailVo) o;
        return Objects.equals(categoryView, that.categoryView) && Objects.equals(skuInfo, that.skuInfo) && Objects.equals(spuSaleAttrList, that.spuSaleAttrList) && Objects.equals(valuesSkuJson, that.valuesSkuJson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), categoryView, skuInfo, spuSaleAttrList, valuesSkuJson);
    }
}
