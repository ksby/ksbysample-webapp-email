package ksbysample.webapp.email.web.mailsend;

import lombok.Data;

import java.util.List;

@Data
public class MailsendForm {

    private String from;

    private String to;

    private String subject;

    private String name;

    private String sex;

    private String type;

    private List<String> item;

    private String naiyo;

}
