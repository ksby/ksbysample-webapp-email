package ksbysample.webapp.email.web.mailsearch;

import ksbysample.webapp.email.entity.Email;
import ksbysample.webapp.email.util.SelectOptionsUtils;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailsearchService {

    @Autowired
    private MailsearchDao mailsearchDao;
    
    public Page<Email> searchEmail(MailsearchForm mailsearchForm, Pageable pageable) {
        SelectOptions options = SelectOptionsUtils.get(pageable, true);
        List<Email> emailList = mailsearchDao.selectCondition(mailsearchForm, options);
        long count = options.getCount();

        Page<Email> page = new PageImpl<Email>(emailList, pageable, count);
        return page;
    }

}
