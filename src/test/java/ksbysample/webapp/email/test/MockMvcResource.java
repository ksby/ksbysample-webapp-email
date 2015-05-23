package ksbysample.webapp.email.test;

import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Component
public class MockMvcResource extends ExternalResource {

    @Autowired
    private WebApplicationContext context;

    public MockMvc nonauth;

    @Override
    protected void before() throws Throwable {
        this.nonauth = MockMvcBuilders.webAppContextSetup(this.context)
                .build();
    }

}
