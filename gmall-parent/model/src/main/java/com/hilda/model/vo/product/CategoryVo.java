package com.hilda.model.vo.product;

import com.hilda.model.bean.base.BaseEntity;

import java.util.List;
import java.util.Objects;

public class CategoryVo extends BaseEntity {

    private Long categoryId;

    private String categoryName;

    private List<CategoryVo> categoryChild;

    public CategoryVo() {
    }

    public CategoryVo(Long categoryId, String categoryName, List<CategoryVo> categoryChild) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryChild = categoryChild;
    }

    @Override
    public String toString() {
        return "CategoryVo{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryChild=" + categoryChild +
                '}';
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<CategoryVo> getCategoryChild() {
        return categoryChild;
    }

    public void setCategoryChild(List<CategoryVo> categoryChild) {
        this.categoryChild = categoryChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CategoryVo that = (CategoryVo) o;
        return Objects.equals(categoryId, that.categoryId) && Objects.equals(categoryName, that.categoryName) && Objects.equals(categoryChild, that.categoryChild);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), categoryId, categoryName, categoryChild);
    }
}
