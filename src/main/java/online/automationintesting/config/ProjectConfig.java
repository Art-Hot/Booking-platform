package online.automationintesting.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:config.properties"})
public interface ProjectConfig extends Config {

    @Key("base.url")
    @DefaultValue("https://automationintesting.online")
    String baseUrl();

    @Key("api.base.url")
    @DefaultValue("https://automationintesting.online/api")
    String apiBaseUrl();

    @Key("admin.username")
    @DefaultValue("admin")
    String adminUsername();

    @Key("admin.password")
    @DefaultValue("password")
    String adminPassword();

    @Key("browser")
    @DefaultValue("chrome")
    String browser();

    @Key("browser.size")
    @DefaultValue("1920x1080")
    String browserSize();

    @Key("timeout")
    @DefaultValue("10000")
    long timeout();
}