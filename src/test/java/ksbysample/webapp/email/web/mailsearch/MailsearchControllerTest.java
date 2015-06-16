package ksbysample.webapp.email.web.mailsearch;

import ksbysample.webapp.email.Application;
import ksbysample.webapp.email.test.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.yaml.snakeyaml.Yaml;

import static ksbysample.webapp.email.test.CustomModelResultMatchers.modelEx;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(Enclosed.class)
public class MailsearchControllerTest {

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 初期表示のテスト {

        @Rule
        @Autowired
        public MockMvcResource mvc;

        @Test
        public void 送信済メール検索画面を表示する() throws Exception {
            // 送信済メール検索画面が表示されることを確認する
            mvc.nonauth.perform(get("/mailsearch"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsearch/mailsearch"))
                    .andExpect(modelEx().property("page.number", is(0)));
        }

    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 検索条件は何も入力しないで検索する場合 {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public TestDataLoaderResource testDataLoaderResource;

        @Rule
        @Autowired
        public MockMvcResource mvc;

        // テストデータ
        private MailsearchForm mailsearchFormEmpty
                = (MailsearchForm) new Yaml().load(getClass().getResourceAsStream("mailsearchForm_empty.yml"));

        @Test
        @TestDataLoader("src/test/resources/ksbysample/webapp/email/web/mailsearch/testdata")
        public void 検索ボタンをクリックすると１ページ目が表示される() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsearch", this.mailsearchFormEmpty))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsearch/mailsearch"))
                    .andExpect(modelEx().property("page.number", is(0)))
                    .andExpect(modelEx().property("page.size", is(2)))
                    .andExpect(modelEx().property("page.totalPages", is(3)))
                    .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                    .andExpect(modelEx().property("ph.hiddenPrev", is(true)))
                    .andExpect(modelEx().property("ph.hiddenNext", is(false)));
        }

        @Test
        @TestDataLoader("src/test/resources/ksbysample/webapp/email/web/mailsearch/testdata")
        public void 検索条件はそのままで２ページ目へ() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsearch?page=1&size=2", this.mailsearchFormEmpty))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsearch/mailsearch"))
                    .andExpect(modelEx().property("page.number", is(1)))
                    .andExpect(modelEx().property("page.size", is(2)))
                    .andExpect(modelEx().property("page.totalPages", is(3)))
                    .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                    .andExpect(modelEx().property("ph.hiddenPrev", is(false)))
                    .andExpect(modelEx().property("ph.hiddenNext", is(false)));
        }
        
    }

    @RunWith(SpringJUnit4ClassRunner.class)
    @SpringApplicationConfiguration(classes = Application.class)
    @WebAppConfiguration
    public static class 検索条件を入力して検索する場合 {

        @Rule
        @Autowired
        public TestDataResource testDataResource;

        @Rule
        @Autowired
        public TestDataLoaderResource testDataLoaderResource;

        @Rule
        @Autowired
        public MockMvcResource mvc;

        // テストデータ
        private MailsearchForm mailsearchFormToAddr
                = (MailsearchForm) new Yaml().load(getClass().getResourceAsStream("mailsearchForm_toAddr.yml"));
        private MailsearchForm mailsearchFormSubject
                = (MailsearchForm) new Yaml().load(getClass().getResourceAsStream("mailsearchForm_subject.yml"));
        private MailsearchForm mailsearchFormName
                = (MailsearchForm) new Yaml().load(getClass().getResourceAsStream("mailsearchForm_name.yml"));
        private MailsearchForm mailsearchFormType
                = (MailsearchForm) new Yaml().load(getClass().getResourceAsStream("mailsearchForm_type.yml"));

        @Test
        @TestDataLoader("src/test/resources/ksbysample/webapp/email/web/mailsearch/testdata")
        public void toAddrのみで検索して３件ヒットする場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsearch", this.mailsearchFormToAddr))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsearch/mailsearch"))
                    .andExpect(modelEx().property("page.number", is(0)))
                    .andExpect(modelEx().property("page.size", is(2)))
                    .andExpect(modelEx().property("page.totalPages", is(2)))
                    .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                    .andExpect(modelEx().property("ph.hiddenPrev", is(true)))
                    .andExpect(modelEx().property("ph.hiddenNext", is(false)));
        }

        @Test
        @TestDataLoader("src/test/resources/ksbysample/webapp/email/web/mailsearch/testdata")
        public void subjectのみで検索して５件ヒットする場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsearch", this.mailsearchFormSubject))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsearch/mailsearch"))
                    .andExpect(modelEx().property("page.number", is(0)))
                    .andExpect(modelEx().property("page.size", is(2)))
                    .andExpect(modelEx().property("page.totalPages", is(3)))
                    .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                    .andExpect(modelEx().property("ph.hiddenPrev", is(true)))
                    .andExpect(modelEx().property("ph.hiddenNext", is(false)));
        }

        @Test
        @TestDataLoader("src/test/resources/ksbysample/webapp/email/web/mailsearch/testdata")
        public void nameのみで検索して１件ヒットする場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsearch", this.mailsearchFormName))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsearch/mailsearch"))
                    .andExpect(modelEx().property("page.number", is(0)))
                    .andExpect(modelEx().property("page.size", is(2)))
                    .andExpect(modelEx().property("page.totalPages", is(1)))
                    .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                    .andExpect(modelEx().property("ph.hiddenPrev", is(true)))
                    .andExpect(modelEx().property("ph.hiddenNext", is(true)));
        }

        @Test
        @TestDataLoader("src/test/resources/ksbysample/webapp/email/web/mailsearch/testdata")
        public void typeのみで検索して２件ヒットする場合() throws Exception {
            mvc.nonauth.perform(TestHelper.postForm("/mailsearch", this.mailsearchFormType))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/html;charset=UTF-8"))
                    .andExpect(view().name("mailsearch/mailsearch"))
                    .andExpect(modelEx().property("page.number", is(0)))
                    .andExpect(modelEx().property("page.size", is(2)))
                    .andExpect(modelEx().property("page.totalPages", is(1)))
                    .andExpect(modelEx().property("ph.page1PageValue", is(0)))
                    .andExpect(modelEx().property("ph.hiddenPrev", is(true)))
                    .andExpect(modelEx().property("ph.hiddenNext", is(true)));
        }

    }

}