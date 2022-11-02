package com.hilda.model.vo.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hilda.model.bean.product.SpuImage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class SpuInfoVo {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("spuName")
    private String spuName;
    @JsonProperty("description")
    private String description;
    @JsonProperty("category3Id")
    private Long category3Id;
    @JsonProperty("spuImageList")
    private List<SpuImage> spuImageList;
    @JsonProperty("spuSaleAttrList")
    private List<SpuSaleAttrListDTO> spuSaleAttrList;
    @JsonProperty("tmId")
    private Long tmId;

    @NoArgsConstructor
    @Data
    public static class SpuSaleAttrListDTO {
        @JsonProperty("baseSaleAttrId")
        private Long baseSaleAttrId;
        @JsonProperty("saleAttrName")
        private String saleAttrName;
        @JsonProperty("spuSaleAttrValueList")
        private List<SpuSaleAttrValueListDTO> spuSaleAttrValueList;

        @NoArgsConstructor
        @Data
        public static class SpuSaleAttrValueListDTO {
            @JsonProperty("baseSaleAttrId")
            private Long baseSaleAttrId;
            @JsonProperty("saleAttrValueName")
            private String saleAttrValueName;
        }
    }
}
