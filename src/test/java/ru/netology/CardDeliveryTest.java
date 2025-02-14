package ru.netology;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {
    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


    @BeforeEach
    void openBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        Selenide.open("http://localhost:9999");
        $("[data-test-id='date'] [placeholder='Дата встречи']").sendKeys(Keys.SHIFT,Keys.HOME, Keys.BACK_SPACE);
    }

    @Test
    void shouldValidValues() {
        $("[data-test-id='city'] [placeholder='Город']").val("Нижний Новгород");
        $("[data-test-id='date'] [placeholder='Дата встречи']").val(generateDate(3));
        $("[data-test-id='name'] [name='name']").val("Иван Иванов");
        $("[data-test-id='phone'] [name='phone']").val("+12312341234");
        $("[data-test-id='agreement'] [class='checkbox__box']").click();
        $(".button").click();
        $("[data-test-id='notification'] [class='notification__title']").shouldBe(text("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id='notification'] [class='notification__content']").shouldBe(text("Встреча успешно забронирована на " + generateDate(3)));
    }

    @Test
    void shouldInvalidValueOfCity() {
        $("[data-test-id='city'] [placeholder='Город']").val("Кстово");
        $("[data-test-id='date'] [placeholder='Дата встречи']").val(generateDate(3));
        $("[data-test-id='name'] [name='name']").val("Иван Иванов");
        $("[data-test-id='phone'] [name='phone']").val("+12312341234");
        $("[data-test-id='agreement'] [class='checkbox__box']").click();
        $(".button").click();
        $("[data-test-id='city'].input_invalid [class='input__sub']").shouldHave(ownText("Доставка в выбранный город недоступна"));
    }


    @Test
    void shouldSelectedInListOfCity() {
        $("[data-test-id='city'] [placeholder='Город']").val("Ни");
        ElementsCollection listOfCities = $$("[class='popup__container'] span");
        listOfCities.findBy(text("Нижний Новгород")).click();
        $("[data-test-id='date'] [placeholder='Дата встречи']").val(generateDate(3));
        $("[data-test-id='name'] [name='name']").val("Иван Иванов");
        $("[data-test-id='phone'] [name='phone']").val("+12312341234");
        $("[data-test-id='agreement'] [class='checkbox__box']").click();
        $(".button").click();
        $("[data-test-id='notification'] [class='notification__title']").shouldBe(text("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id='notification'] [class='notification__content']").shouldBe(text("Встреча успешно забронирована на " + generateDate(3)));
    }
}