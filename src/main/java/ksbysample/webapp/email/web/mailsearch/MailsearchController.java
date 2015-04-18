package ksbysample.webapp.email.web.mailsearch;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mailsearch")
public class MailsearchController {

    @RequestMapping
    public String index() {
        return "mailsearch/mailsearch";
    }

}
