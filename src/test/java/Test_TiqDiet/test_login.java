package Test_TiqDiet;

import GUI.GUI_setup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class test_login {
    WebDriver chrome;
    GUI_setup get_data;

    @BeforeMethod
    public void start() throws IOException {
        get_data = new GUI_setup();
        System.setProperty("webdriver.chrome.driver", "driver/83/chromedriver.exe");
        chrome = new ChromeDriver();
        chrome.manage().window().maximize();
        chrome.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void Verification() throws InterruptedException {

        chrome.get("https://dev.tiqdiet.com/");
        chrome.findElement(By.xpath("//input[@id='loginform-email-input']")).sendKeys(get_data.login_TextField.getText());
        chrome.findElement(By.xpath("//input[@id='loginform-password-input']")).sendKeys(get_data.password_Field.getText());
        chrome.findElement(By.xpath("//button[@id='loginform-submit-button']")).click();

    }

    @AfterMethod
    public void end() throws IOException {
        chrome.close();
    }

    public test_login() throws IOException, InterruptedException {
        start();
        Verification();
        end();
    }


}
