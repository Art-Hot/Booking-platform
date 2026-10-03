package online.automationintesting.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.ex.ConditionNotMetError;
import com.codeborne.selenide.ex.ElementNotFound;
import online.automationintesting.config.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class AdminPage {

    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton   = $("#doLogin");

    private final SelenideElement messagesLink  = $("a[href='/admin/message']");
    private final SelenideElement bookingsLink  = $("a[href='/admin/booking']");
    private final SelenideElement messageList   = $(".messages-list");

    public AdminPage() {
        $("#username").shouldBe(visible, Duration.ofSeconds(10));
    }

    public AdminPage login(String username, String password) {
        System.out.println("Вход: " + username);

        usernameInput.shouldBe(visible).setValue(username);
        passwordInput.shouldBe(visible).setValue(password);
        loginButton.click();

        System.out.println("✅ Вход выполнен успешно");
        return this;
    }

    public AdminPage loginWithInvalidCredentials(String username, String password) {
        System.out.println(" Негативный вход: " + username);

        usernameInput.shouldBe(visible).setValue(username);
        passwordInput.shouldBe(visible).setValue(password);
        loginButton.click();

        // Жесткая проверка: ошибка ОБЯЗАНА появиться
        $(".alert-danger, .error")
                .shouldBe(Condition.visible, Duration.ofSeconds(5));

        System.out.println("❌ Ошибка входа подтверждена: " + $(".alert-danger, .error").text());
        return this;
    }

    public AdminPage login() {
        return login(config.adminUsername(), config.adminPassword());
    }

    public AdminPage ensureInsideAdminPanel() {
        $("a[href='/admin/rooms']").shouldBe(visible, Duration.ofSeconds(10));
        return this;
    }

    public AdminPage goToMessages() {
        messagesLink.shouldBe(visible, Duration.ofSeconds(10)).click();

        Selenide.webdriver().shouldHave(urlContaining("/admin/message"));
        $("body").shouldNot(text("Loading..."), Duration.ofSeconds(10));
        return this;
    }

    public boolean isLoggedIn() {
        return bookingsLink.isDisplayed();
    }
    public boolean isMessageDisplayed(String messageText) {
        try {
            String xpath = "//*[contains(., '" + messageText + "')]";
            $(By.xpath(xpath)).shouldBe(Condition.visible, Duration.ofSeconds(10));
            return true;
        } catch (ElementNotFound | ConditionNotMetError e) {
            return false;
        }
    }
}
