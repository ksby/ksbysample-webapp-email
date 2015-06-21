package ksbysample.webapp.email.web.mailsend;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import ksbysample.webapp.email.Application;
import ksbysample.webapp.email.config.Constant;
import ksbysample.webapp.email.domain.InquiryType;
import ksbysample.webapp.email.test.MailServerResource;
import ksbysample.webapp.email.test.TableDataAssert;
import ksbysample.webapp.email.test.TestDataResource;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.yaml.snakeyaml.Yaml;

import javax.mail.internet.*;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class MailsendServiceTest {

    // MailsendFormの全てに値がセットされているテストデータ
    private final MailsendForm mailsendFormSimple
            = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_simple.yml"));
    // MailsendFormの必須項目のみ値がセットされているテストデータ
    private final MailsendForm mailsendFormMinimum
            = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_minimum.yml"));
    // MailsendFormの全てに値がセットされている添付ファイル付メール用のテストデータ
    private final MailsendForm mailsendFormAttached
            = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_attached.yml"));

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

    @Value("${mailsend.attached.file}")
    private String attachedFilePropertyValue;

    @Test
    public void mailsendFormSimpleでテキストメールを送信する場合() throws Exception {
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
    public void mailsendFormMinimumでテキストメールを送信する場合() throws Exception {
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

    @Test
    public void mailsendFormSimpleでHTMLメールを送信する場合() throws Exception {
        mailsendService.saveAndSendHtmlEmail(mailsendFormSimple);

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

        // メール本文
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader((String) receiveMessage.getContent())));
        Constant constant = Constant.getInstance();
        assertThat(document, hasXPath("//*[@id=\"name\"]", equalTo(mailsendFormSimple.getName())));
        assertThat(document, hasXPath("//*[@id=\"sex\"]", equalTo(constant.SEX_MAP.get(mailsendFormSimple.getSex()))));
        assertThat(document, hasXPath("//*[@id=\"type\"]", equalTo(InquiryType.getText(mailsendFormSimple.getType()))));
        assertThat(document, hasXPath("//*[@id=\"item\"]"
                , equalTo(
                mailsendFormSimple.getItem().stream()
                        .map(constant.ITEM_MAP::get)
                        .collect(Collectors.joining(", ")))));
        assertThat(document, hasXPath("//*[@id=\"naiyo\"]", equalTo(mailsendFormSimple.getNaiyo())));
    }

    @Test
    public void mailsendFormMinimumでHTMLメールを送信する場合() throws Exception {
        mailsendService.saveAndSendHtmlEmail(mailsendFormMinimum);

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

        // メール本文
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader((String) receiveMessage.getContent())));
        Constant constant = Constant.getInstance();
        assertThat(document, hasXPath("//*[@id=\"name\"]", equalTo("")));
        assertThat(document, hasXPath("//*[@id=\"sex\"]", equalTo("")));
        assertThat(document, hasXPath("//*[@id=\"type\"]", equalTo("")));
        assertThat(document, hasXPath("//*[@id=\"item\"]", equalTo("")));
        assertThat(document, hasXPath("//*[@id=\"naiyo\"]", equalTo("")));
    }

    @Test
    public void mailsendFormAttachedで添付ファイル付テキストメールを送信する場合() throws Exception {
        mailsendService.saveAndSendEmail(mailsendFormAttached);

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

        assertThat(receiveMessage.getContent(), instanceOf(MimeMultipart.class));
        MimeMultipart mimeMultipart = (MimeMultipart) receiveMessage.getContent();
        assertThat(mimeMultipart.getCount(), is(2));

        // テキストメールの確認
        String mailsendFormSimpleMail
                = Files.toString(new File(getClass().getResource("mailsendForm_simple_mail.txt").toURI()), StandardCharsets.UTF_8);
        MimeBodyPart bodyPart = (MimeBodyPart) mimeMultipart.getBodyPart(0);
        MimeMultipart part = (MimeMultipart) bodyPart.getContent();
        String text = null;
        try (Reader reader = new InputStreamReader(part.getBodyPart(0).getInputStream())) {
            text = CharStreams.toString(reader);
        }
        assertThat(text, is(mailsendFormSimpleMail));

        // 添付ファイルの確認
        bodyPart = (MimeBodyPart) mimeMultipart.getBodyPart(1);
        File file = new File(this.attachedFilePropertyValue);
        assertThat(MimeUtility.decodeText(bodyPart.getFileName()), is(file.getName()));
    }

}
