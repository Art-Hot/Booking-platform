package online.automationintesting.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import online.automationintesting.config.ProjectConfig;
import org.aeonbits.owner.ConfigFactory;

import java.time.Duration;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selectors.byXpath;

public class MainPage {

    private static final ProjectConfig config = ConfigFactory.create(ProjectConfig.class);

    private final SelenideElement nameInput = $("#name");
    private final SelenideElement emailInput = $("#email");
    private final SelenideElement phoneInput = $("#phone");
    private final SelenideElement subjectInput = $("#subject");
    private final SelenideElement messageTextarea = $("#description");
    private final SelenideElement submitButton = $(".d-grid button.btn.btn-primary");
    private final SelenideElement adminLink = $("a[href='/admin']");

    private final SelenideElement successMessageHeading = $(byXpath("(//h3 | //h4)[contains(normalize-space(), 'Thanks for getting in touch')]"));
    public MainPage openPage() {
        open(config.baseUrl());
        return this;
    }

    public MainPage fillContactForm(String name, String email, String phone,
                                    String subject, String message) {
        nameInput.setValue(name);
        emailInput.setValue(email);
        phoneInput.setValue(phone);
        subjectInput.setValue(subject);
        messageTextarea.setValue(message);
        return this;
    }

    public MainPage submitContactForm() {
        submitButton.shouldBe(Condition.enabled).click(usingJavaScript());
        return this;
    }

    public MainPage checkSuccessMessageIsVisible() {
        successMessageHeading.shouldHave(Condition.text("Thanks for getting in touch"));
        return this;
    }

    public boolean isSuccessMessageDisplayed() {
        return successMessageHeading.has(Condition.text("Thanks for getting in touch"));
    }

    public AdminPage goToAdminPage() {
        adminLink.click();
        try {
            $("#username").shouldBe(visible, Duration.ofSeconds(5));
        } catch (Exception e) {
            if ($(withText("Logout")).exists()) {
                $(withText("Logout")).click();
                $("#username").shouldBe(visible, Duration.ofSeconds(5));
            } else {
                throw new AssertionError("Не удалось найти ни форму логина, ни кнопку выхода после клика на Admin");
            }
        }
        return new AdminPage();
    }

}
