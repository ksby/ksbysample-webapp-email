package ksbysample.webapp.email.web.mailsearch;

import lombok.Data;

import java.util.List;

@Data
public class MailsearchForm {

    private String toAddr;

    private String subject;

    private String name;

    private List<String> type;

}
