package ksbysample.webapp.email.web.mailsend;

import ksbysample.webapp.email.dao.EmailDao;
import ksbysample.webapp.email.dao.EmailItemDao;
import ksbysample.webapp.email.entity.Email;
import ksbysample.webapp.email.entity.EmailItem;
import ksbysample.webapp.email.helper.mail.MAIL001MailHelper;
import ksbysample.webapp.email.service.EmailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailsendService {

    @Autowired
    private EmailDao emailDao;

    @Autowired
    private EmailItemDao emailItemDao;

    @Autowired
    private MAIL001MailHelper mail001MailHelper;

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
        EmailItem emailItem = new EmailItem();
        for (String item : mailsendForm.getItem()) {
            emailItem.setEmailItemId(null);
            emailItem.setEmailId(email.getEmailId());
            emailItem.setItem(item);
            emailItemDao.insert(emailItem);
        }
    }

    public void sendEmail(MailsendForm mailsendForm) {
        SimpleMailMessage mailMessage = mail001MailHelper.createMessage(mailsendForm);
        emailService.sendSimpleMail(mailMessage);
    }

}
