package Test_TiqDiet;

import GUI.GUI_setup;
import TiqDiet_class.Dish;
import TiqDiet_class.Excel_operation;
import TiqDiet_class.Ingredients;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Product_verification {

    WebDriver chrome;
    private ArrayList<Dish> input_dish = new ArrayList<Dish>();
    private ArrayList<Ingredients> product_list = new ArrayList<Ingredients>();
    private ArrayList<Ingredients> failed_product_list = new ArrayList<Ingredients>();
    GUI_setup gui_input;
    boolean englis;
    boolean data_base;
    double progres;
    @BeforeMethod
    public void start(){

        creat_dish_list();
        englis = gui_input.englishCheckBox.isSelected();
        data_base = gui_input.tiqDietEnglishCoFIDCheckBox.isSelected();

        gui_input.add_out_put("In file: "+product_list.size()+" products to verification");
        gui_input.add_out_put("Start verification");

        WebDriverManager.chromedriver().setup();
        chrome = new ChromeDriver();
        chrome.manage().window().maximize();
        chrome.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        progres = gui_input.progres;
    }

    @Test
    public void execut() throws InterruptedException {

        if(product_list.size() > 0) {

            chrome.get("https://dev.tiqdiet.com/");

            //Login
            boolean login = false;
            while (login == false) {
                chrome.findElement(By.xpath("//input[@id='loginform-email-input']")).sendKeys(gui_input.login_TextField.getText());
                chrome.findElement(By.xpath("//input[@id='loginform-password-input']")).sendKeys(gui_input.password_Field.getText());
                chrome.findElement(By.xpath("//button[@id='loginform-submit-button']")).click();


                try {
                    chrome.findElement(By.xpath("//div[@id='avatar-holder' and @class='avatar-holder']"));
                    login = true;
                } catch (Throwable e) {
                    try {
                        if (chrome.findElement(By.xpath("//p[@id='loginform-error-content']")).isDisplayed()) {
                            gui_input.add_out_put(chrome.findElement(By.xpath("//p[@id='loginform-error-content']")).getText());
                            break;
                        } else {
                            chrome.findElement(By.xpath("//input[@id='loginform-email-input']")).clear();
                            chrome.findElement(By.xpath("//input[@id='loginform-password-input']")).clear();
                        }
                    } catch (Throwable ee) {
                        login = false;
                    }
                }
            }

            if (englis == true || data_base == true) {
                chrome.findElement(By.xpath("//div[@id='avatar-holder' and @class='avatar-holder']")).click();
                chrome.findElement(By.xpath("//a[@id='settings']/child::div/child::div[@class='valign description']")).click();

                if (englis == true) {
                    WebElement language = chrome.findElement(By.xpath("//button[@id='app-options-language']/parent::div"));
                    language.click();
                    List<WebElement> language_option = language.findElements(By.xpath("//div[@class='item']"));
                    for (int i = 0; i < language_option.size(); i++) {
                        if (language_option.get(i).getText().equals("English")) {
                            language_option.get(i).click();
                            break;
                        }
                    }
                    gui_input.add_out_put("Language change correct");
                }

                if (data_base == true) {
                    chrome.findElement(By.xpath("//input[@type='checkbox' and @value='false']")).click();
                    gui_input.add_out_put("TiqDiet English (CoFID) selected correct");
                }

                chrome.findElement(By.xpath("//button[@id='action-type' and @name='accept']")).click();
            }
            progres = progres + 5;
            gui_input.set_progres(progres);

            Thread.sleep(1000);

            //Go to product
            chrome.findElement(By.xpath("//a[@id='meals-and-diets']/child::div")).click();
            List<WebElement> button_dish = chrome.findElements(By.xpath("//div[@class='btn-group switcher col-sm-12']/child::button"));
            button_dish.get(0).click();

            double temp_progres = 40.0/(double) product_list.size();

            for(int i = 0; i < product_list.size(); i++) {
                Ingredients check_product = product_list.get(i);

                //seach product
                chrome.findElement(By.xpath("//input[@id='table-search']")).clear();
                chrome.findElement(By.xpath("//input[@id='table-search']")).sendKeys(check_product.get_ingredients_name());
                Thread.sleep(1000);


                boolean product_exist = false;
                List<WebElement> product_carts = chrome.findElements(By.xpath("//div[@class='card-container']"));
                if(product_carts.size() > 0) {
                    for (int j = 0; j < product_carts.size(); j++) {
                        if (product_carts.get(j).findElement(By.xpath("//div[@class='meal-name meal-name-index-" + j + "']")).getText().equals(check_product.get_ingredients_name())) {
                            List<WebElement> measure_name = product_carts.get(j).findElements(By.cssSelector("span"));
                            for (int k = 0; k < measure_name.size(); k++) {
                                String measure = measure_name.get(k).getText().substring(0, measure_name.get(k).getText().length() - 1);
                                if (measure.equals(check_product.get_ingredients_measure())) {
                                    product_exist = true;
                                }
                            }
                        }
                    }
                }
                if (!product_exist) {
                    gui_input.add_out_put("Product: "+check_product.get_ingredients_name()+" and measure: "+check_product.get_ingredients_measure()+" not find :(");
                    failed_product_list.add(check_product);
                }
                progres = progres + temp_progres;
                gui_input.set_progres(progres);
            }
        }
    }

    @AfterMethod
    public void end() throws IOException {
        chrome.close();
        gui_input.add_out_put("End verification");
        gui_input.progressBar1.setValue(50);
        Excel_operation out_put = new Excel_operation();
        out_put.Get_failed_product_file(failed_product_list);
        gui_input.check_product_file.setText(out_put.file_path);

    }

    public Product_verification(ArrayList<Dish> input_file_dish, GUI_setup gui) throws IOException, InterruptedException {
        input_dish = input_file_dish;
        gui_input = gui;
        start();
        execut();
        end();
    }

    public void close_chrome(){
        chrome.close();
    }

    public void creat_dish_list(){
        Ingredients temp_check_product = new Ingredients();

        for(int i = 0; i < input_dish.size(); i++){
            Dish check_product_list = new Dish();
            check_product_list = input_dish.get(i);
            for(int j = 0; j < check_product_list.get_ingredients_list_size(); j++){
                Ingredients check_product = check_product_list.get_ingredients(j);
                temp_check_product.Creat_ingredients_list(check_product);
            }
        }

        product_list = temp_check_product.get_Ingredients_list();
    }
}
