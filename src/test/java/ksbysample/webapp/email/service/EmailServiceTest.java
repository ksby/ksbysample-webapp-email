package ksbysample.webapp.email.service;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetup;
import ksbysample.webapp.email.Application;
import ksbysample.webapp.email.test.MailServerResource;
import ksbysample.webapp.email.test.MailServerWiserResource;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.subethamail.wiser.WiserMessage;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class EmailServiceTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class Wiserを利用してメール送信する場合 {

        @Rule
        @Autowired
        public MailServerWiserResource wiser;

        @Autowired
        private EmailService emailService;

        @Test
        public void testSendSimpleMail() throws Exception {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("test@sample.com");
            message.setTo("xxx@yyy.zzz");
            message.setSubject("テスト");
            message.setText("これはテストです");
            emailService.sendSimpleMail(message);

            List<WiserMessage> receiveMessages = wiser.getWiser().getMessages();
            assertThat(receiveMessages.size(), is(1));
            MimeMessage receiveMessage = receiveMessages.iterator().next().getMimeMessage();
            assertThat(receiveMessage.getFrom()[0], is(new InternetAddress("test@sample.com")));
            assertThat(receiveMessage.getAllRecipients()[0], is(new InternetAddress("xxx@yyy.zzz")));
            assertThat(receiveMessage.getSubject(), is("テスト"));
            assertThat(receiveMessage.getContent(), is("これはテストです"));
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class GreenMailを利用してメール送信する場合 {

        @Rule
        public GreenMailRule greenMail = new GreenMailRule(new ServerSetup(25, "localhost", ServerSetup.PROTOCOL_SMTP));

        @Autowired
        private EmailService emailService;

        @Test
        public void testSendSimpleMail() throws Exception {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("test@sample.com");
            message.setTo("xxx@yyy.zzz");
            message.setSubject("テスト");
            message.setText("これはテストです");
            emailService.sendSimpleMail(message);

            MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
            assertThat(receivedMessages.length, is(1));
            assertThat(receivedMessages[0].getFrom()[0], is(new InternetAddress("test@sample.com")));
            assertThat(receivedMessages[0].getAllRecipients()[0], is(new InternetAddress("xxx@yyy.zzz")));
            assertThat(receivedMessages[0].getSubject(), is("テスト"));
            // GreenMailUtil.getBody を使用すると取得した日本語のメール本文が
            // "44GT44KM44Gv44OG44K544OI44Gn44GZ"
            // でした。デコードが必要？
            //assertThat(GreenMailUtil.getBody(receivedMessages[0]), is("これはテストです"));
            assertThat(receivedMessages[0].getContent(), is("これはテストです"));
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class MailServerResourceを利用してメール送信する場合 {

        @Rule
        @Autowired
        public MailServerResource mailServer;

        @Autowired
        private EmailService emailService;

        @Test
        public void testSendSimpleMail() throws Exception {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("test@sample.com");
            message.setTo("xxx@yyy.zzz");
            message.setSubject("テスト");
            message.setText("これはテストです");
            emailService.sendSimpleMail(message);

            assertThat(mailServer.getMessagesCount(), is(1));
            MimeMessage receiveMessage = mailServer.getFirstMessage();
            assertThat(receiveMessage.getFrom()[0], is(new InternetAddress("test@sample.com")));
            assertThat(receiveMessage.getAllRecipients()[0], is(new InternetAddress("xxx@yyy.zzz")));
            assertThat(receiveMessage.getSubject(), is("テスト"));
            assertThat(receiveMessage.getContent(), is("これはテストです"));
        }

    }

}
