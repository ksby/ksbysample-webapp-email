package ksbysample.webapp.email.util;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Map;

@Component
public class VelocityUtils {

    @Autowired
    private VelocityEngine velocityEngine;

    @Value("${spring.velocity.charset}")
    private String charset;

    public String merge(String templateLocation, Map<String, Object> model) {
        return VelocityEngineUtils.mergeTemplateIntoString(this.velocityEngine, templateLocation, charset, model);
    }

}
