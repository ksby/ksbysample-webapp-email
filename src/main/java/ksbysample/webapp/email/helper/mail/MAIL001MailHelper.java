package ksbysample.webapp.email.helper.mail;

import ksbysample.webapp.email.config.Constant;
import ksbysample.webapp.email.domain.InquiryType;
import ksbysample.webapp.email.util.VelocityUtils;
import ksbysample.webapp.email.web.mailsend.MailsendForm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MAIL001MailHelper {

    private final String TEMPLATE_LOCATION_TEXTMAIL = "mail/MAIL001/MAIL001-body.vm";
    // templateEngine.processに渡すThymeleafのテンプレートファイルは拡張子.html付けないこと
    private final String TEMPLATE_LOCATION_HTMLMAIL = "mail/MAIL001/MAIL001-HTML-body";

    @Autowired
    private VelocityUtils velocityUtils;

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${mailsend.attached.file}")
    private String attachedFilePropertyValue;
    
    public SimpleMailMessage createMessage(MailsendForm mailsendForm) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailsendForm.getFromAddr());
        mailMessage.setTo(mailsendForm.getToAddr());
        mailMessage.setSubject(mailsendForm.getSubject());
        mailMessage.setText(generateTextUsingVelocity(mailsendForm));
        return mailMessage;
    }

    public MimeMessage createHtmlMessage(MailsendForm mailsendForm) throws MessagingException {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        message.setFrom(mailsendForm.getFromAddr());
        message.setTo(mailsendForm.getToAddr());
        message.setSubject(mailsendForm.getSubject());
        message.setText(generateTextUsingThymeleaf(mailsendForm), true);
        return message.getMimeMessage();
    }

    public MimeMessage createAttachedMessage(MailsendForm mailsendForm) throws MessagingException {
        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setFrom(mailsendForm.getFromAddr());
        message.setTo(mailsendForm.getToAddr());
        message.setSubject(mailsendForm.getSubject());
        message.setText(generateTextUsingVelocity(mailsendForm), false);

        // ファイルを添付する
        if (StringUtils.isNotEmpty(this.attachedFilePropertyValue)) {
            File file = new File(this.attachedFilePropertyValue);
            if (file.exists()) {
                String fileName = file.getName();
                message.addAttachment(fileName, file);
            }
        }
        
        return message.getMimeMessage();
    }
    
    private String generateTextUsingVelocity(MailsendForm mailsendForm) {
        Constant constant = Constant.getInstance();
        Map<String, Object> model = new HashMap<>();
        model.put("name", mailsendForm.getName());
        model.put("sex", constant.SEX_MAP.get(mailsendForm.getSex()));
        model.put("type", InquiryType.getText(mailsendForm.getType()));

        String itemList = null;
        if (mailsendForm.getItem() != null) {
            itemList = mailsendForm.getItem().stream()
                    .map(constant.ITEM_MAP::get)
                    .collect(Collectors.joining(", "));
        }
        model.put("item", itemList);

        model.put("naiyo", mailsendForm.getNaiyo());
        return velocityUtils.merge(this.TEMPLATE_LOCATION_TEXTMAIL, model);
    }

    private String generateTextUsingThymeleaf(MailsendForm mailsendForm) {
        Constant constant = Constant.getInstance();
        Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("name", mailsendForm.getName());
        ctx.setVariable("sex", constant.SEX_MAP.get(mailsendForm.getSex()));
        ctx.setVariable("type", InquiryType.getText(mailsendForm.getType()));

        String itemList = null;
        if (mailsendForm.getItem() != null) {
            itemList = mailsendForm.getItem().stream()
                    .map(constant.ITEM_MAP::get)
                    .collect(Collectors.joining(", "));
        }
        ctx.setVariable("item", itemList);

        ctx.setVariable("naiyo", mailsendForm.getNaiyo());
        
        return this.templateEngine.process(this.TEMPLATE_LOCATION_HTMLMAIL, ctx);
    }
    
}
