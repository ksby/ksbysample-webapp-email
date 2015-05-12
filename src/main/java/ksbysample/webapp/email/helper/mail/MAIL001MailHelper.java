package ksbysample.webapp.email.helper.mail;

import ksbysample.webapp.email.config.Constant;
import ksbysample.webapp.email.util.VelocityUtils;
import ksbysample.webapp.email.web.mailsend.MailsendForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MAIL001MailHelper {

    private final String templateLocation = "mail/MAIL001/MAIL001-body.vm";

    @Autowired
    private VelocityUtils velocityUtils;

    public SimpleMailMessage createMessage(MailsendForm mailsendForm) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailsendForm.getFromAddr());
        mailMessage.setTo(mailsendForm.getToAddr());
        mailMessage.setSubject(mailsendForm.getSubject());
        mailMessage.setText(generateTextUsingVelocity(mailsendForm));
        return mailMessage;
    }

    private String generateTextUsingVelocity(MailsendForm mailsendForm) {
        Constant constant = Constant.getInstance();
        Map<String, Object> model = new HashMap<>();
        model.put("name", mailsendForm.getName());
        model.put("sex", constant.SEX_MAP.get(mailsendForm.getSex()));
        model.put("type", constant.TYPE_MAP.get(mailsendForm.getType()));

        String itemList = mailsendForm.getItem().stream()
                .map(constant.ITEM_MAP::get)
                .collect(Collectors.joining(", "));
        model.put("item", itemList);

        model.put("naiyo", mailsendForm.getNaiyo());
        return velocityUtils.merge(this.templateLocation, model);
    }

}
