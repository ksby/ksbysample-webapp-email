package ksbysample.webapp.email.util;

import ksbysample.webapp.email.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class VelocityUtilsTest {

    private final String templateLocation = "ksbysample/webapp/email/util/VelocityUtilsTest.vm";

    @Autowired
    private VelocityUtils velocityUtils;

    @Test
    public void testMerge() throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("testdata", "VelocityUtils のテストです。");
        String testString = velocityUtils.merge(templateLocation, model);
        assertThat(testString, is("テスト: VelocityUtils のテストです。"));
    }
}
