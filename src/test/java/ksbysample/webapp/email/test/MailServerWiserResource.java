package ksbysample.webapp.email.test;

import org.junit.rules.ExternalResource;
import org.springframework.stereotype.Component;
import org.subethamail.wiser.Wiser;

@Component
public class MailServerWiserResource extends ExternalResource {

    private Wiser wiser;

    @Override
    protected void before() {
        this.wiser = new Wiser(); 
        this.wiser.setHostname("localhost");
        this.wiser.setPort(25);
        this.wiser.start();
    }

    @Override
    protected void after() {
        this.wiser.stop();
        this.wiser = null;
    }

    public Wiser getWiser() {
        return this.wiser;
    }

}
