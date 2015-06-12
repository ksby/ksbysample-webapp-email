package ksbysample.webapp.email.web.mailsearch;

import ksbysample.webapp.email.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/mailsearch")
public class MailsearchController {

    @Autowired
    private MailsearchDao mailsearchDao;
    
    @RequestMapping
    public String index(MailsearchForm mailsearchForm, Model model) {
        List<Email> emailList = mailsearchDao.selectCondition(mailsearchForm);
        model.addAttribute("emailList", emailList);

        return "mailsearch/mailsearch";
    }
    
}
