package Test_TiqDiet;

import GUI.GUI_setup;
import TiqDiet_class.Dish;
import TiqDiet_class.Excel_operation;
import TiqDiet_class.Ingredients;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Product_verification {

    WebDriver chrome;
    private ArrayList<Dish> input_dish = new ArrayList<Dish>();
    private ArrayList<Ingredients> product_list = new ArrayList<Ingredients>();
    private ArrayList<Ingredients> failed_product_list = new ArrayList<Ingredients>();
    GUI_setup gui_input;
    boolean englis;
    boolean data_base;
    double progres = gui_input.progres;
    @BeforeMethod
    public void start(){

        creat_dish_list();
        englis = gui_input.englishCheckBox.isSelected();
        data_base = gui_input.tiqDietEnglishCoFIDCheckBox.isSelected();

        gui_input.add_out_put("In file: "+product_list.size()+" products to verification");
        gui_input.add_out_put("Start verification");
        System.setProperty("webdriver.chrome.driver", "driver/83/chromedriver.exe");
        chrome = new ChromeDriver();
        chrome.manage().window().maximize();
        chrome.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
    }

    @Test
    public void execut(){
        double temp_progres = 45/product_list.size();

        if(product_list.size() > 0){

            try {
                chrome.get("https://dev.tiqdiet.com/");

                //Login
                boolean login = false;
                while (login == false) {
                    chrome.findElement(By.xpath("//input[@id='loginform-email-input']")).sendKeys("uattestreport@gmail.com");
                    chrome.findElement(By.xpath("//input[@id='loginform-password-input']")).sendKeys("Haslo123!@");
                    chrome.findElement(By.xpath("//button[@id='loginform-submit-button']")).click();
                    try {
                        chrome.findElement(By.xpath("//div[@id='avatar-holder' and @class='avatar-holder']"));
                        login = true;
                    } catch (Throwable e) {
                        login = false;
                    }
                }

                if (englis == true || data_base == true) {

                    chrome.findElement(By.xpath("//div[@id='avatar-holder' and @class='avatar-holder']")).click();
                    chrome.findElement(By.xpath("//div[@class='valign description' and text()='Ustawienia']")).click();


                    if (englis == true) {

                    }

                    if (data_base == true) {
                        chrome.findElement(By.xpath("//input[@type='checkbox' and @value='false']")).click();
                    }

                    chrome.findElement(By.xpath("//button[@id='action-type' and @name='accept']")).click();

                }
            }catch (Throwable e){

                failed_product_list = product_list;
                gui_input.add_out_put("Can't verification product - all product add to failed file");

            }

        }

        progres = progres + temp_progres;
        gui_input.set_progres(progres);


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

    public Product_verification(ArrayList<Dish> input_file_dish, GUI_setup gui) throws IOException {
        input_dish = input_file_dish;
        gui_input = gui;
        start();
        execut();
        end();
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
