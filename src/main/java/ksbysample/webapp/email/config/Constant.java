package ksbysample.webapp.email.config;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

@Component
public class Constant {

    private static final Constant instance = (Constant) new Yaml().load(Constant.class.getResourceAsStream("/constant.yml"));

    public static Constant getInstance() {
        return instance;
    }

    // 性別
    public Map<String, String> SEX_MAP;

    // 問い合わせ項目
    public Map<String, String> TYPE_MAP;

    // 商品
    public Map<String, String> ITEM_MAP;

}
