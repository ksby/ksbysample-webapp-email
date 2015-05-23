package ksbysample.webapp.email.helper.mail;

import ksbysample.webapp.email.Application;
import ksbysample.webapp.email.web.mailsend.MailsendForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.yaml.snakeyaml.Yaml;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MAIL001MailHelperTest {

    private final MailsendForm mailsendFormSimple
            = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("/ksbysample/webapp/email/web/mailsend/mailsendForm_simple.yml"));
    private final MailsendForm mailsendFormMinimum
            = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("/ksbysample/webapp/email/web/mailsend/mailsendForm_minimum.yml"));

    @Autowired
    private MAIL001MailHelper mail001MailHelper;

    @Test
    public void MailsendFormの全てに値がセットされている場合() throws Exception {
        SimpleMailMessage message = mail001MailHelper.createMessage(mailsendFormSimple);
        assertThat(message.getFrom(), is(mailsendFormSimple.getFromAddr()));
    }

    @Test
    public void MailsendFormの必須項目のみ値がセットされている場合() throws Exception {
        SimpleMailMessage message = mail001MailHelper.createMessage(mailsendFormMinimum);
        assertThat(message.getFrom(), is(mailsendFormMinimum.getFromAddr()));
    }

}
