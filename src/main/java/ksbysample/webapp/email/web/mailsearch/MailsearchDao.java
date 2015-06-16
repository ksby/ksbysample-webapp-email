package ksbysample.webapp.email.web.mailsearch;

import ksbysample.webapp.email.annotation.dao.ComponentAndAutowiredDomaConfig;
import ksbysample.webapp.email.entity.Email;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

@Dao
@ComponentAndAutowiredDomaConfig
public interface MailsearchDao {

    @Select
    List<Email> selectCondition(MailsearchForm mailsearchForm);

    @Select
    List<Email> selectCondition(MailsearchForm mailsearchForm, SelectOptions options);

}
