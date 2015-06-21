package ksbysample.webapp.email.web.mailsend;

import ksbysample.webapp.email.config.Constant;
import ksbysample.webapp.email.domain.InquiryType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MailsendFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(MailsendForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MailsendForm mailsendForm = (MailsendForm)target;
        Constant constant = Constant.getInstance();

        // 「項目」が「資料請求」「商品に関する苦情」の場合には「商品」が何も選択されていない場合にはエラー
        if (StringUtils.equals(mailsendForm.getType(), InquiryType.SHIRYO_SEIKYU.getValue())
                || StringUtils.equals(mailsendForm.getType(), InquiryType.SHOHIN_NI_KANSURU_KUJO.getValue())) {
            if ((mailsendForm.getItem() == null) || (mailsendForm.getItem().size() == 0)) {
                errors.reject("mailsendForm.item.noSelect");
            }
        }

        // 「項目」が「その他」の場合には「内容」に何も入力されていない場合にはエラー
        if (StringUtils.equals(mailsendForm.getType(), InquiryType.SONOTA.getValue())) {
            if (mailsendForm.getNaiyo().length() == 0) {
                errors.reject("mailsendForm.naiyo.noText");
            }
        }
    }

}
