package online.automationintesting.ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import online.automationintesting.config.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.*;

public class BaseUITest {

    protected static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    @BeforeAll
    static void setUp() {
        Configuration.browser = config.browser();
        Configuration.browserSize = config.browserSize();
        Configuration.timeout = config.timeout();
        Configuration.headless = true;

        Configuration.screenshots = true;
        Configuration.savePageSource = true;
        Configuration.pageLoadStrategy = "eager";
    }

    @AfterEach
    void cleanState() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.clearBrowserCookies();
            Selenide.clearBrowserLocalStorage();
        }
    }

    @AfterAll
    static void closeBrowser() {
        Selenide.closeWebDriver();
    }
}