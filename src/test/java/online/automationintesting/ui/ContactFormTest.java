package online.automationintesting.ui;

import online.automationintesting.pages.AdminPage;
import online.automationintesting.pages.MainPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UI тесты: контактная форма и админ-панель")
public class ContactFormTest extends BaseUITest {

    @Test
    @DisplayName("Успешная отправка контактной формы")
    void shouldSubmitContactFormSuccessfully() {
        MainPage mainPage = new MainPage().openPage();

        mainPage.fillContactForm(
                "Иван Березин",
                "ivan@test.com",
                "79991234567",
                "Тестовое сообщение",
                "Это тестовое сообщение для проверки формы обратной связи."
        );

        mainPage.submitContactForm();

        mainPage.checkSuccessMessageIsVisible();
    }

    @Test
    @DisplayName("Валидация: отправка пустой формы")
    void shouldShowValidationErrorsForEmptyForm() {
        MainPage mainPage = new MainPage().openPage();
        mainPage.submitContactForm();

        assertThat(mainPage.isSuccessMessageDisplayed())
                .as("Форма не должна отправляться с пустыми полями")
                .isFalse();
    }

    @Test
    @DisplayName("Сообщение отображается в админ-панели")
    void shouldShowMessageInAdminPanel() {
        String uniqueSubject = "Тест_" + System.currentTimeMillis();

        MainPage mainPage = new MainPage().openPage();
        mainPage.fillContactForm(
                "Софья Сидорова",
                "sofia@test.com",
                "79997654321",
                uniqueSubject,
                "Сообщение для проверки отображения в админке."
        );
        mainPage.submitContactForm();

        mainPage.checkSuccessMessageIsVisible();

        AdminPage adminPage = mainPage.goToAdminPage();
        adminPage.login();
        adminPage.ensureInsideAdminPanel();

        adminPage.goToMessages();

        assertThat(adminPage.isMessageDisplayed(uniqueSubject))
                .as("Сообщение с темой '" + uniqueSubject + "' должно отображаться в админ-панели")
                .isTrue();
    }

    @Test
    @DisplayName("Вход в админ-панель с неверными учётными данными")
    void shouldNotLoginWithWrongCredentials() {
        MainPage mainPage = new MainPage().openPage();
        AdminPage adminPage = mainPage.goToAdminPage();

        adminPage.loginWithInvalidCredentials("wrong_user", "wrong_password");

        assertThat(adminPage.isLoggedIn())
                .as("Вход с неверными данными не должен выполняться")
                .isFalse();
    }

}