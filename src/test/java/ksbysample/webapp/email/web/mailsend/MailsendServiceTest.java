package ksbysample.webapp.email.web.mailsend;

import com.google.common.io.Files;
import ksbysample.webapp.email.Application;
import ksbysample.webapp.email.test.MailServerResource;
import ksbysample.webapp.email.test.TableDataAssert;
import ksbysample.webapp.email.test.TestDataResource;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.yaml.snakeyaml.Yaml;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.sql.DataSource;
import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MailsendServiceTest {

    private final MailsendForm mailsendFormSimple
            = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_simple.yml"));
    private final MailsendForm mailsendFormMinimum
            = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_minimum.yml"));

    @Rule
    @Autowired
    public TestDataResource testDataResource;

    @Rule
    @Autowired
    public MailServerResource mailServer;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MailsendService mailsendService;

    @Test
    public void MailsendFormの全てに値がセットされている場合() throws Exception {
        mailsendService.saveAndSendEmail(mailsendFormSimple);

        // email, email_item テーブルに保存されているか確認する
        IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/email/web/mailsend/testdata/simple"));
        TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
        tableDataAssert.assertEquals("email", new String[]{"email_id"});
        tableDataAssert.assertEquals("email_item", new String[]{"email_item_id", "email_id"});

        // メールが送信されているか確認する
        assertThat(mailServer.getMessagesCount(), is(1));
        MimeMessage receiveMessage = mailServer.getFirstMessage();
        assertThat(receiveMessage.getFrom()[0], is(new InternetAddress("test@sample.com")));
        assertThat(receiveMessage.getAllRecipients()[0], is(new InternetAddress("xxx@yyy.zzz")));
        assertThat(receiveMessage.getSubject(), is("テスト"));
        String mailsendFormSimpleMail
                = Files.toString(new File(getClass().getResource("mailsendForm_simple_mail.txt").toURI()), StandardCharsets.UTF_8);
        assertThat(receiveMessage.getContent(), is(mailsendFormSimpleMail));
    }

    @Test
    public void MailsendFormの必須項目のみ値がセットされている場合() throws Exception {
        mailsendService.saveAndSendEmail(mailsendFormMinimum);

        // email, email_item テーブルに保存されているか確認する
        IDataSet dataSet = new CsvDataSet(new File("src/test/resources/ksbysample/webapp/email/web/mailsend/testdata/minimum"));
        TableDataAssert tableDataAssert = new TableDataAssert(dataSet, dataSource);
        tableDataAssert.assertEquals("email", new String[]{"email_id"});
        tableDataAssert.assertEquals("email_item", new String[]{"email_item_id", "email_id"});

        // メールが送信されているか確認する
        assertThat(mailServer.getMessagesCount(), is(1));
        MimeMessage receiveMessage = mailServer.getFirstMessage();
        assertThat(receiveMessage.getFrom()[0], is(new InternetAddress("test@sample.com")));
        assertThat(receiveMessage.getAllRecipients()[0], is(new InternetAddress("xxx@yyy.zzz")));
        assertThat(receiveMessage.getSubject(), is("テスト"));
        String mailsendFormMinimumMail
                = Files.toString(new File(getClass().getResource("mailsendForm_minimum_mail.txt").toURI()), StandardCharsets.UTF_8);
        assertThat(receiveMessage.getContent(), is(mailsendFormMinimumMail));
    }

}
