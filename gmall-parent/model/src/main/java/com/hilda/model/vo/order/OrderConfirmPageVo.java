package com.hilda.model.vo.order;

import com.hilda.model.bean.base.BaseEntity;
import com.hilda.model.bean.order.OrderDetail;
import com.hilda.model.bean.user.UserAddress;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class OrderConfirmPageVo extends BaseEntity {

    private List<OrderDetail> detailArrayList;
    private Integer totalNum;
    private BigDecimal totalAmount;
    private List<UserAddress> userAddressList;
    private Long tradeNo;

    public OrderConfirmPageVo() {
    }

    public OrderConfirmPageVo(List<OrderDetail> detailArrayList, Integer totalNum, BigDecimal totalAmount, List<UserAddress> userAddressList, Long tradeNo) {
        this.detailArrayList = detailArrayList;
        this.totalNum = totalNum;
        this.totalAmount = totalAmount;
        this.userAddressList = userAddressList;
        this.tradeNo = tradeNo;
    }

    @Override
    public String toString() {
        return "OrderComfirmPageVo{" +
                "detailArrayList=" + detailArrayList +
                ", totalNum=" + totalNum +
                ", totalAmount=" + totalAmount +
                ", userAddressList=" + userAddressList +
                ", tradeNo=" + tradeNo +
                '}';
    }

    public List<OrderDetail> getDetailArrayList() {
        return detailArrayList;
    }

    public void setDetailArrayList(List<OrderDetail> detailArrayList) {
        this.detailArrayList = detailArrayList;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<UserAddress> getUserAddressList() {
        return userAddressList;
    }

    public void setUserAddressList(List<UserAddress> userAddressList) {
        this.userAddressList = userAddressList;
    }

    public Long getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(Long tradeNo) {
        this.tradeNo = tradeNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderConfirmPageVo that = (OrderConfirmPageVo) o;
        return Objects.equals(detailArrayList, that.detailArrayList) && Objects.equals(totalNum, that.totalNum) && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(userAddressList, that.userAddressList) && Objects.equals(tradeNo, that.tradeNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), detailArrayList, totalNum, totalAmount, userAddressList, tradeNo);
    }
}
