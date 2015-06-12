package ksbysample.webapp.email.web.mailsearch;

import ksbysample.webapp.email.Application;
import ksbysample.webapp.email.entity.Email;
import ksbysample.webapp.email.test.TestDataLoader;
import ksbysample.webapp.email.test.TestDataLoaderResource;
import ksbysample.webapp.email.test.TestDataResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MailsearchDaoTest {

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Rule
    @Autowired
    public TestDataLoaderResource testDataLoaderResource;

    @Autowired
    private MailsearchDao mailsearchDao;

    @Test
    @TestDataLoader("src/test/resources/ksbysample/webapp/email/web/mailsearch/testdata")
    public void testSelectCondition() throws Exception {
        MailsearchForm mailsearchForm = new MailsearchForm();
        List<Email> emailList = mailsearchDao.selectCondition(mailsearchForm);
        // emailList.stream().forEach(s -> System.out.println(s.getToAddr()));
        assertThat(emailList.size(), is(5));

        mailsearchForm = new MailsearchForm();
        mailsearchForm.setToAddr("t");
        mailsearchForm.setSubject("スト");
        mailsearchForm.setName("花");
        mailsearchForm.setType(Arrays.asList("3"));
        emailList = mailsearchDao.selectCondition(mailsearchForm);
        assertThat(emailList.size(), is(1));

        mailsearchForm = new MailsearchForm();
        mailsearchForm.setName("太郎");
        emailList = mailsearchDao.selectCondition(mailsearchForm);
        assertThat(emailList.size(), is(3));
    }

}