package com.hilda.model.vo.search;

import com.hilda.model.bean.search.Goods;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

// 总的数据
@Data
public class SearchResponseVo implements Serializable {

    private SearchParamVo searchParamVo;
    private String trademarkParam;
    private List<AttrVo> propsParamList;
    private List<TrademarkVo> trademarkList;
    private List<AttrListVo> attrsList;
    private List<Goods> goodsList;
    private OrderMapVo orderMap;
    private Integer pageNo;
    private String urlParam;
    private Integer totalPages;

    @Data
    public static class AttrVo{
        Long attrId;
        String attrName;
        String attrValue;
    }

    @Data
    public static class TrademarkVo{
        Long tmId;
        String tmLogoUrl;
        String tmName;
    }

    @Data
    public static class AttrListVo{
        Long attrId;
        String attrName;
        List<String> attrValueList;
    }

    @Data
    public static class OrderMapVo{
        String type;
        String sort;
    }

}
