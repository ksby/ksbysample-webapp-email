package ksbysample.webapp.email.web.mailsend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mailsend")
public class MailsendController {

    @RequestMapping
    public String index() {
        return "mailsend/mailsend";
    }

    @RequestMapping("/send")
    public String send() {
        return "redirect:/mailsend";
    }

}
