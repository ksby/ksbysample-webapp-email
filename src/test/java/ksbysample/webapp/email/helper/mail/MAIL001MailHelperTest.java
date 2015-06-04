package ksbysample.webapp.email.helper.mail;

import ksbysample.webapp.email.Application;
import ksbysample.webapp.email.config.Constant;
import ksbysample.webapp.email.web.mailsend.MailsendForm;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.yaml.snakeyaml.Yaml;

import javax.mail.internet.*;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class MAIL001MailHelperTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class テキストメール生成のテスト {

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

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class HTMLメール生成のテスト {

        private final MailsendForm mailsendFormSimple
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("/ksbysample/webapp/email/web/mailsend/mailsendForm_simple.yml"));
        private final MailsendForm mailsendFormMinimum
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("/ksbysample/webapp/email/web/mailsend/mailsendForm_minimum.yml"));

        @Autowired
        private MAIL001MailHelper mail001MailHelper;

        @Test
        public void MailsendFormの全てに値がセットされている場合() throws Exception {
            MimeMessage message = mail001MailHelper.createHtmlMessage(mailsendFormSimple);

            // from, Subject
            InternetAddress address = (InternetAddress) message.getFrom()[0];
            assertThat(address.getAddress(), is(mailsendFormSimple.getFromAddr()));
            assertThat(message.getSubject(), is(mailsendFormSimple.getSubject()));

            // メール本文
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader((String) message.getContent())));
            Constant constant = Constant.getInstance();
            assertThat(document, hasXPath("//*[@id=\"name\"]", equalTo(mailsendFormSimple.getName())));
            assertThat(document, hasXPath("//*[@id=\"sex\"]", equalTo(constant.SEX_MAP.get(mailsendFormSimple.getSex()))));
            assertThat(document, hasXPath("//*[@id=\"type\"]", equalTo(constant.TYPE_MAP.get(mailsendFormSimple.getType()))));
            assertThat(document, hasXPath("//*[@id=\"item\"]"
                    , equalTo(
                    mailsendFormSimple.getItem().stream()
                            .map(constant.ITEM_MAP::get)
                            .collect(Collectors.joining(", ")))));
            assertThat(document, hasXPath("//*[@id=\"naiyo\"]", equalTo(mailsendFormSimple.getNaiyo())));
        }

        @Test
        public void MailsendFormの必須項目のみ値がセットされている場合() throws Exception {
            MimeMessage message = mail001MailHelper.createHtmlMessage(mailsendFormMinimum);

            // from, Subject
            InternetAddress address = (InternetAddress) message.getFrom()[0];
            assertThat(address.getAddress(), is(mailsendFormMinimum.getFromAddr()));
            assertThat(message.getSubject(), is(mailsendFormMinimum.getSubject()));

            // メール本文
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader((String) message.getContent())));
            Constant constant = Constant.getInstance();
            assertThat(document, hasXPath("//*[@id=\"name\"]", equalTo("")));
            assertThat(document, hasXPath("//*[@id=\"sex\"]", equalTo("")));
            assertThat(document, hasXPath("//*[@id=\"type\"]", equalTo("")));
            assertThat(document, hasXPath("//*[@id=\"item\"]", equalTo("")));
            assertThat(document, hasXPath("//*[@id=\"naiyo\"]", equalTo("")));
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 添付ファイル付テキストメール生成のテスト {

        private final MailsendForm mailsendFormAttached
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("/ksbysample/webapp/email/web/mailsend/mailsendForm_attached.yml"));

        @Autowired
        private MAIL001MailHelper mail001MailHelper;

        @Value("${mailsend.attached.file}")
        private String attachedFilePropertyValue;

        @Test
        public void MailsendFormの全てに値がセットされている場合() throws Exception {
            MimeMessage mimeMessage = mail001MailHelper.createAttachedMessage(mailsendFormAttached);
            assertThat(mimeMessage.getContent(), instanceOf(MimeMultipart.class));
            
            MimeMultipart mimeMultipart = (MimeMultipart) mimeMessage.getContent();
            assertThat(mimeMultipart.getCount(), is(2));

            MimeBodyPart bodyPart = (MimeBodyPart) mimeMultipart.getBodyPart(1);
            File file = new File(this.attachedFilePropertyValue);
            assertThat(MimeUtility.decodeText(bodyPart.getFileName()), is(file.getName()));
        }

    }

}
