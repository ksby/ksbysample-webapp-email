package ksbysample.webapp.email.web.mailsend;

import ksbysample.webapp.email.Application;
import ksbysample.webapp.email.test.MailServerResource;
import ksbysample.webapp.email.test.MockMvcResource;
import ksbysample.webapp.email.test.TestDataResource;
import ksbysample.webapp.email.test.TestHelper;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.yaml.snakeyaml.Yaml;

import static ksbysample.webapp.email.test.ErrorsResultMatchers.errors;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(Enclosed.class)
public class MailsendControllerTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 初期表示のテスト {

        @Rule
        @Autowired
        public MockMvcResource mvc;
        
        @Test
        public void メール送信画面を表示する() throws Exception {
            // メール送信画面が表示されることを確認する
            mvc.nonauth.perform(get("/mailsend"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsend/mailsend"));
        }
        
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 入力チェックエラーのテスト {

        private final MailsendForm mailsendFormEmpty
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_empty.yml"));
        private final MailsendForm mailsendFormMaxPatternerr
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_max_patternerr.yml"));
        private final MailsendForm mailsendFormFv01
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_fv01.yml"));
        private final MailsendForm mailsendFormFv02
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_fv02.yml"));

        @Rule
        @Autowired
        public MockMvcResource mvc;

        @Test
        public void データ未入力時には入力チェックエラー() throws Exception {
            // NotBlank のテスト
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/send", this.mailsendFormEmpty))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsend/mailsend"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(3))
                    .andExpect(errors().hasFieldError("mailsendForm", "fromAddr", "NotBlank"))
                    .andExpect(errors().hasFieldError("mailsendForm", "toAddr", "NotBlank"))
                    .andExpect(errors().hasFieldError("mailsendForm", "subject", "NotBlank"));
        }

        @Test
        public void 最大文字数オーバー_パターンエラー時には入力チェックエラー() throws Exception {
            // Email/Size/Pattern のテスト
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/send", this.mailsendFormMaxPatternerr))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsend/mailsend"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(7))
                    .andExpect(errors().hasFieldError("mailsendForm", "fromAddr", "Email"))
                    .andExpect(errors().hasFieldError("mailsendForm", "toAddr", "Email"))
                    .andExpect(errors().hasFieldError("mailsendForm", "subject", "Size"))
                    .andExpect(errors().hasFieldError("mailsendForm", "name", "Size"))
                    .andExpect(errors().hasFieldError("mailsendForm", "sex", "Pattern"))
                    .andExpect(errors().hasFieldError("mailsendForm", "type", "Pattern"))
                    .andExpect(errors().hasFieldError("mailsendForm", "naiyo", "Size"));
        }

        @Test
        public void 項目が資料請求_商品に関する苦情の場合に商品が何も選択されていない場合には入力チェックエラー() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/send", this.mailsendFormFv01))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsend/mailsend"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(errors().hasGlobalError("mailsendForm", "mailsendForm.item.noSelect"));
        }

        @Test
        public void 項目がその他の場合に内容に何も入力されていない場合には入力チェックエラー() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/send", this.mailsendFormFv02))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsend/mailsend"))
                    .andExpect(model().hasErrors())
                    .andExpect(model().errorCount(1))
                    .andExpect(errors().hasGlobalError("mailsendForm", "mailsendForm.naiyo.noText"));
        }
        
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 正常処理時のテスト {

        private final MailsendForm mailsendFormMinimum
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_minimum.yml"));
        private final MailsendForm mailsendFormMin
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_min.yml"));
        private final MailsendForm mailsendFormMax
                = (MailsendForm) new Yaml().load(getClass().getResourceAsStream("mailsendForm_max.yml"));

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public MailServerResource mailServer;

        @Rule
        @Autowired
        public MockMvcResource mvc;

        @Test
        public void 最小値空ありで送信ボタンをクリックした場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/send", this.mailsendFormMinimum))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/mailsend"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().errorCount(0));
        }

        @Test
        public void 最小値で送信ボタンをクリックした場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/send", this.mailsendFormMin))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/mailsend"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().errorCount(0));
        }

        @Test
        public void 最大値で送信ボタンをクリックした場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/send", this.mailsendFormMax))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/mailsend"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().errorCount(0));
        }

        @Test
        public void 最小値空ありでHTML送信ボタンをクリックした場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/sendhtml", this.mailsendFormMinimum))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/mailsend"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().errorCount(0));
        }

        @Test
        public void 最小値でHTML送信ボタンをクリックした場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/sendhtml", this.mailsendFormMin))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/mailsend"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().errorCount(0));
        }

        @Test
        public void 最大値でHTML送信ボタンをクリックした場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsend/sendhtml", this.mailsendFormMax))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/mailsend"))
                    .andExpect(model().hasNoErrors())
                    .andExpect(model().errorCount(0));
        }
        
    }

}
