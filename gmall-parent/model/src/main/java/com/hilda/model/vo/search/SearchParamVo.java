package com.hilda.model.vo.search;

import com.hilda.model.bean.base.BaseEntity;

import java.util.Arrays;
import java.util.Objects;

public class SearchParamVo extends BaseEntity {

    private String keyword;
    private Long category1Id;
    private Long category2Id;
    private Long category3Id;
    private String trademark;
    private String[] props;
    private String order;
    private Integer pageNo;

    public SearchParamVo() {
    }

    public SearchParamVo(String keyword, Long category1Id, Long category2Id, Long category3Id, String trademark, String[] props, String order, Integer pageNo) {
        this.keyword = keyword;
        this.category1Id = category1Id;
        this.category2Id = category2Id;
        this.category3Id = category3Id;
        this.trademark = trademark;
        this.props = props;
        this.order = order;
        this.pageNo = pageNo;
    }

    @Override
    public String toString() {
        return "SearchParamVo{" +
                "keyword='" + keyword + '\'' +
                ", category1Id=" + category1Id +
                ", category2Id=" + category2Id +
                ", category3Id=" + category3Id +
                ", trademark='" + trademark + '\'' +
                ", props=" + Arrays.toString(props) +
                ", order='" + order + '\'' +
                ", pageNo=" + pageNo +
                '}';
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCategory1Id() {
        return category1Id;
    }

    public void setCategory1Id(Long category1Id) {
        this.category1Id = category1Id;
    }

    public Long getCategory2Id() {
        return category2Id;
    }

    public void setCategory2Id(Long category2Id) {
        this.category2Id = category2Id;
    }

    public Long getCategory3Id() {
        return category3Id;
    }

    public void setCategory3Id(Long category3Id) {
        this.category3Id = category3Id;
    }

    public String getTrademark() {
        return trademark;
    }

    public void setTrademark(String trademark) {
        this.trademark = trademark;
    }

    public String[] getProps() {
        return props;
    }

    public void setProps(String[] props) {
        this.props = props;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SearchParamVo that = (SearchParamVo) o;
        return Objects.equals(keyword, that.keyword) && Objects.equals(category1Id, that.category1Id) && Objects.equals(category2Id, that.category2Id) && Objects.equals(category3Id, that.category3Id) && Objects.equals(trademark, that.trademark) && Arrays.equals(props, that.props) && Objects.equals(order, that.order) && Objects.equals(pageNo, that.pageNo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), keyword, category1Id, category2Id, category3Id, trademark, order, pageNo);
        result = 31 * result + Arrays.hashCode(props);
        return result;
    }
}
