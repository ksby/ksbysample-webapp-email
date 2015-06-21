package ksbysample.webapp.email.domain;

import ksbysample.webapp.email.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class InquiryTypeTest {

    @Test
    public void test() {
        for (InquiryType inquiryType : InquiryType.values()) {
            System.out.println(inquiryType.getText());
        }
    }
    
}