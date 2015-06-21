package ksbysample.webapp.email.domain;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum InquiryType {

    SHIRYO_SEIKYU("1", "資料請求")
    , SHOHIN_NI_KANSURU_KUJO("2", "商品に関する苦情")
    , SONOTA("3", "その他");

    private final String value;
    private final String text;
    
    private InquiryType(String value, String text) {
        this.value = value;
        this.text = text;
    }
    
    public static String getText(String value) {
        String result = "";
        for (InquiryType inquiryType : InquiryType.values()) {
            if (StringUtils.equals(inquiryType.getValue(), value)) {
                result = inquiryType.getText();
            }
        }

        return result;
    }
    
}
