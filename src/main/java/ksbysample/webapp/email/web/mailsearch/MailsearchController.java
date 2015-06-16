package ksbysample.webapp.email.web.mailsearch;

import ksbysample.webapp.email.entity.Email;
import ksbysample.webapp.email.helper.PagenationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mailsearch")
public class MailsearchController {

    private static final int DEFAULT_PAGEABLE_SIZE = 2;

    @Autowired
    private MailsearchService mailsearchService;

    @RequestMapping
    public String index(MailsearchForm mailsearchForm
            , @PageableDefault(size = DEFAULT_PAGEABLE_SIZE, page = 0) Pageable pageable
            , Model model) {
        Page<Email> page = mailsearchService.searchEmail(mailsearchForm, pageable);
        PagenationHelper ph = new PagenationHelper(page);

        model.addAttribute("page", page);
        model.addAttribute("ph", ph);

        return "mailsearch/mailsearch";
    }

}
