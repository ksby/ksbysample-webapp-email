package ksbysample.webapp.email.test;

import org.junit.rules.ExternalResource;
import org.springframework.stereotype.Component;
import org.subethamail.wiser.Wiser;

@Component
public class MailServerWiserResource extends ExternalResource {

    private Wiser wiser = new Wiser();

    @Override
    protected void before() {
        this.wiser.setHostname("localhost");
        this.wiser.setPort(25);
        this.wiser.start();
    }

    @Override
    protected void after() {
        this.wiser.stop();
    }

    public Wiser getWiser() {
        return this.wiser;
    }

}
