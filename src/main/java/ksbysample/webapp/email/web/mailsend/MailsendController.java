package ksbysample.webapp.email.web.mailsend;

import ksbysample.webapp.email.service.EmailService;
import ksbysample.webapp.email.util.VelocityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mailsend")
public class MailsendController {

    @Autowired
    private VelocityUtils velocityUtils;

    @Autowired
    private EmailService emailService;

    @RequestMapping
    public String index(MailsendForm mailsendForm
            , Model model) {
        return "mailsend/mailsend";
    }

    @RequestMapping("/send")
    public String send(MailsendForm mailsendForm
            , Model model) {
        // メールを送信する
        SimpleMailMessage mailMessage = MAIL001MailBuilder.build()
                .setForm(mailsendForm)
                .setVelocityUtils(velocityUtils)
                .setTemplateLocation("mail/MAIL001/MAIL001-body.vm")
                .create();
        emailService.sendSimpleMail(mailMessage);

        return "redirect:/mailsend";
    }

}
