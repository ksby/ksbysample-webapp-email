package ksbysample.webapp.email.web.mailsend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mailsend")
public class MailsendController {

    @Autowired
    private MailsendService mailsendService;

    @Autowired
    private MailsendFormValidator mailsendFormValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(mailsendFormValidator);
    }

    @RequestMapping
    public String index(MailsendForm mailsendForm
            , Model model) {
        return "mailsend/mailsend";
    }

    @RequestMapping("/send")
    public String send(@Validated MailsendForm mailsendForm
            , BindingResult bindingResult
            , Model model) {
        if (bindingResult.hasErrors()) {
            return "mailsend/mailsend";
        }

        // 入力されたデータをDBに保存した後、メールを送信する
        mailsendService.saveAndSendEmail(mailsendForm);

        return "redirect:/mailsend";
    }

}
