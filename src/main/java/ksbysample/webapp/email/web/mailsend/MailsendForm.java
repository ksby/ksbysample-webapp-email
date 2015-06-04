package ksbysample.webapp.email.web.mailsend;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.List;

@Data
public class MailsendForm {

    @NotBlank
    @Email
    private String fromAddr;

    @NotBlank
    @Email
    private String toAddr;

    @NotBlank
    @Size(max = 128, message = "{error.size.max}")
    private String subject;

    @Size(max = 32, message = "{error.size.max}")
    private String name;

    @Pattern(regexp = "^(|1|2)$", message = "{mailsendForm.sex.pattern}")
    private String sex;

    @Pattern(regexp = "^(|1|2|3)$", message = "{mailsendForm.type.pattern}")
    private String type;

    private List<String> item;

    @Size(max = 2000, message = "{error.size.max}")
    private String naiyo;

    private String attachedFile;

}
