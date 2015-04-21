package ksbysample.webapp.email.web.mailsend;

import ksbysample.webapp.email.config.Constant;
import ksbysample.webapp.email.util.VelocityUtils;
import org.springframework.mail.SimpleMailMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MAIL001MailBuilder {

    private MailsendForm mailsendForm;

    private VelocityUtils velocityUtils;

    private String templateLocation;

    public static MAIL001MailBuilder build() {
        return new MAIL001MailBuilder();
    }

    public MAIL001MailBuilder setForm(MailsendForm mailsendForm) {
        this.mailsendForm = mailsendForm;
        return this;
    }

    public MAIL001MailBuilder setVelocityUtils(VelocityUtils velocityUtils) {
        this.velocityUtils = velocityUtils;
        return this;
    }

    public MAIL001MailBuilder setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
        return this;
    }

    public SimpleMailMessage create() {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailsendForm.getFrom());
        mailMessage.setTo(mailsendForm.getTo());
        mailMessage.setSubject(mailsendForm.getSubject());

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
        mailMessage.setText(velocityUtils.merge(this.templateLocation, model));

        return mailMessage;
    }

}
