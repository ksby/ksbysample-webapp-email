package ksbysample.webapp.email.web.mailsend;

import ksbysample.webapp.email.dao.EmailDao;
import ksbysample.webapp.email.dao.EmailItemDao;
import ksbysample.webapp.email.entity.Email;
import ksbysample.webapp.email.entity.EmailItem;
import ksbysample.webapp.email.service.EmailService;
import ksbysample.webapp.email.util.VelocityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailsendService {

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private EmailItemDao emailItemDao;

    @Autowired
    private VelocityUtils velocityUtils;

    @Autowired
    private EmailService emailService;

    public void saveAndSendEmail(MailsendForm mailsendForm) {
        // 入力されたデータを email, email_item テーブルに保存する
        saveEmail(mailsendForm);

        // メールを送信する
        sendEmail(mailsendForm);
    }

    public void saveEmail(MailsendForm mailsendForm) {
        // email テーブルに保存する
        Email email = new Email();
        BeanUtils.copyProperties(mailsendForm, email);
        emailDao.insert(email);

        // email_item テーブルに保存する
        for (String item : mailsendForm.getItem()) {
            EmailItem emailItem = new EmailItem();
            emailItem.setEmailId(email.getEmailId());
            emailItem.setItem(item);
            emailItemDao.insert(emailItem);
        }
    }

    public void sendEmail(MailsendForm mailsendForm) {
        SimpleMailMessage mailMessage = MAIL001MailBuilder.build()
                .setForm(mailsendForm)
                .setVelocityUtils(velocityUtils)
                .setTemplateLocation("mail/MAIL001/MAIL001-body.vm")
                .create();
        emailService.sendSimpleMail(mailMessage);
    }

}
