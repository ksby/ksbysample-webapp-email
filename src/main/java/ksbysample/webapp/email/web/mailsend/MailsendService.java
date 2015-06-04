package ksbysample.webapp.email.web.mailsend;

import ksbysample.webapp.email.dao.EmailDao;
import ksbysample.webapp.email.dao.EmailItemDao;
import ksbysample.webapp.email.entity.Email;
import ksbysample.webapp.email.entity.EmailItem;
import ksbysample.webapp.email.helper.mail.MAIL001MailHelper;
import ksbysample.webapp.email.service.EmailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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

    public void saveAndSendEmail(MailsendForm mailsendForm) throws MessagingException {
        // 入力されたデータを email, email_item テーブルに保存する
        saveEmail(mailsendForm);

        // メールを送信する
        if (StringUtils.equals(mailsendForm.getAttachedFile(), "1")) {
            sendAttachedEmail(mailsendForm);
        }
        else {
            sendEmail(mailsendForm);
        }
    }

    public void saveAndSendHtmlEmail(MailsendForm mailsendForm) throws MessagingException {
        // 入力されたデータを email, email_item テーブルに保存する
        saveEmail(mailsendForm);

        // HTMLメールを送信する
        sendHtmlEmail(mailsendForm);
    }

    private void saveEmail(MailsendForm mailsendForm) {
        // email テーブルに保存する
        Email email = new Email();
        BeanUtils.copyProperties(mailsendForm, email);
        emailDao.insert(email);

        // email_item テーブルに保存する
        EmailItem emailItem = new EmailItem();
        if (mailsendForm.getItem() != null) {
            for (String item : mailsendForm.getItem()) {
                emailItem.setEmailItemId(null);
                emailItem.setEmailId(email.getEmailId());
                emailItem.setItem(item);
                emailItemDao.insert(emailItem);
            }
        }
    }

    private void sendEmail(MailsendForm mailsendForm) {
        SimpleMailMessage message = mail001MailHelper.createMessage(mailsendForm);
        emailService.sendSimpleMail(message);
    }

    private void sendHtmlEmail(MailsendForm mailsendForm) throws MessagingException {
        MimeMessage message = mail001MailHelper.createHtmlMessage(mailsendForm);
        emailService.sendMail(message);
    }
    
    private void sendAttachedEmail(MailsendForm mailsendForm) throws MessagingException {
        MimeMessage message = mail001MailHelper.createAttachedMessage(mailsendForm);
        emailService.sendMail(message);
    }

}
