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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Add_dish {

    WebDriver chrome;
    ArrayList<Dish> input_dish_list= new ArrayList<Dish>();
    ArrayList<Dish> passed_dish_list = new ArrayList<Dish>();
    ArrayList<Dish> failed_dish_list = new ArrayList<Dish>();
    ArrayList<Ingredients> failed_product_list = new ArrayList<Ingredients>();
    public Excel_operation out_put;
    GUI_setup gui_input;
    boolean englis;
    boolean data_base;
    int all_dish;
    double progres;

    @BeforeMethod
    public void start() {
        WebDriverManager.chromedriver().setup();
        chrome = new ChromeDriver();
        check_dish_product();
        gui_input.add_out_put(failed_dish_list.size()+" dish hasn't product in database");
        all_dish = input_dish_list.size();

        chrome.manage().window().maximize();
        chrome.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        progres = gui_input.progres;
    }

    @Test
    public void execut() throws InterruptedException {
        gui_input.add_out_put("Start add "+input_dish_list.size()+" dish");
        if(input_dish_list.size() > 0) {

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
            if (login) {

                //change database and language
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
                button_dish.get(1).click();

                double temp_progres = (100.0 - progres) / (double) input_dish_list.size();
                chrome.findElement(By.xpath("//button[@id='add-meal']")).click();

                for (int i = 0; i < input_dish_list.size(); i++) {
                    Dish new_dish = input_dish_list.get(i);

                    try {
                        chrome.findElement(By.xpath("//input[@class='form-control']")).sendKeys(new_dish.get_dish_name());

                        for (int j = 0; j < new_dish.get_ingredients_list_size() - 1; j++) {

                            chrome.findElement(By.xpath("//div[@class='row add-another-row']/child::button[@class='tiq-button']")).click();
                        }

                        List<WebElement> ingredient_name_input = chrome.findElements(By.xpath("//input[@id='add-ingredients-name']"));
                        List<WebElement> ingredient_measure = chrome.findElements(By.xpath("//button[@id='add-homeMeasure']/child::span[@class='dropdown-name']"));
                        List<WebElement> ingredient_quantity = chrome.findElements(By.xpath("//input[@type='text' and @id='add-quantity']"));

                        for (int j = 0; j < new_dish.get_ingredients_list_size(); j++) {
                            Ingredients add_ingredients = new_dish.get_ingredients(j);
                            ingredient_name_input.get(j).sendKeys(add_ingredients.get_ingredients_name());
                            Thread.sleep(5000);
                            WebElement list = chrome.findElement(By.xpath("//ul[@id='react-autowhatever-1' and @role='listbox']"));
                            List<WebElement> option = list.findElements(By.tagName("li"));

                            for (int k = 0; k < option.size(); k++) {
                                if (option.get(k).getText().equals(add_ingredients.get_ingredients_name())) {
                                    option.get(k).click();
                                    break;
                                }
                            }

                            if (!ingredient_measure.get(j).getText().equals(add_ingredients.get_ingredients_measure())) {
                                ingredient_measure.get(j).click();
                                Thread.sleep(5000);
                                WebElement list_measure = chrome.findElement(By.xpath("//div[@class='dropdown tiq-dropdown col-sm-9 open']"));
                                List<WebElement> measure_option = list_measure.findElements(By.tagName("li"));
                                for (int k = 0; k < measure_option.size(); k++) {
                                    if (measure_option.get(k).getText().indexOf(add_ingredients.get_ingredients_measure()) >= 0) {
                                        measure_option.get(k).click();
                                        break;
                                    }
                                }
                            }

                            ingredient_quantity.get(j).clear();
                            ingredient_quantity.get(j).sendKeys(add_ingredients.get_ingredients_quantity());
                        }

                        chrome.findElement(By.xpath("//input[@type='text' and @id='portions']")).sendKeys(new_dish.get_dish_serving());
                        chrome.findElement(By.xpath("//input[@type='text' and @class='form-control prep-time-input' and @placeholder='min']")).sendKeys(new_dish.get_dish_preparation());
                        chrome.findElement(By.xpath("//textarea[@type='text']")).sendKeys(new_dish.get_dish_description());

                        List<WebElement> category_options = chrome.findElements(By.xpath("//div[@class='categories-wrapper']/child::div[@class='inline-flex flex-center category-tag']"));

                        for (int j = 0; j < category_options.size(); j++) {
                            for (int k = 0; k < new_dish.get_dish_category_list_size(); k++) {
                                String category = new_dish.get_dish_category(k);
                                if (category.equals(category_options.get(j).getText())) {
                                    category_options.get(j).click();
                                }
                            }
                        }

                        if (i + 1 < input_dish_list.size()) {
                            chrome.findElement(By.xpath("//div[@id='save-add-next-meal' and @class='add-next']")).click();
                        } else {
                            chrome.findElement(By.xpath("//button[@id='save-meal' and @class='tiq-button']")).click();
                        }

                        gui_input.add_out_put(new_dish.get_dish_name() + " dish added correct");
                        passed_dish_list.add(new_dish);
                    } catch (Throwable e) {
                        gui_input.add_out_put(new_dish.get_dish_name() + " dish not added - failed");
                        failed_dish_list.add(new_dish);
                        chrome.findElement(By.xpath("//button[@class='exitProfile']")).click();
                        chrome.findElement(By.xpath("//button[@id='add-meal']")).click();
                    }

                    progres = progres + temp_progres;
                    gui_input.set_progres(progres);
                }
            }else{
                failed_dish_list = input_dish_list;
            }
        }
    }

    @AfterMethod
    public void end() throws IOException {
        chrome.close();
        gui_input.set_progres(100.0);
        gui_input.add_out_put("All dish in file: "+all_dish);
        gui_input.add_out_put("Passed add dish: "+passed_dish_list.size());
        gui_input.add_out_put("Failed add dish: "+failed_dish_list.size());
        out_put = new Excel_operation();
        out_put.get_error_file_dish(failed_dish_list);
        gui_input.file_path = out_put.file_path;
        gui_input.add_out_put("File path: "+out_put.file_path);
    }

   public Add_dish(ArrayList<Dish> input_file_dish, GUI_setup gui, ArrayList<Ingredients> failed_product) throws IOException, InterruptedException {
        input_dish_list = input_file_dish;
        failed_product_list = failed_product;
        gui_input = gui;
        start();
        execut();
        end();
    }

    public void check_dish_product(){

        Dish check_dish_product = new Dish();
        for(int i = 0; i < input_dish_list.size();i++){
            check_dish_product = input_dish_list.get(i);
            boolean product_exist = true;

            for(int j = 0; j < check_dish_product.get_ingredients_list_size(); j++){
                Ingredients temp_product = new Ingredients();
                temp_product = check_dish_product.get_ingredients(j);

                for (int k = 0; k < failed_product_list.size(); k++){
                    if(temp_product.get_ingredients_name().equals(failed_product_list.get(k).get_ingredients_name())){
                        if(temp_product.get_ingredients_measure().equals(failed_product_list.get(k).get_ingredients_measure())){
                            product_exist = false;
                        }
                    }
                }
            }
            if(product_exist == false){
                failed_dish_list.add(check_dish_product);
            }
        }

        for(int i  = 0; i < failed_dish_list.size(); i++){
            input_dish_list.remove(failed_dish_list.get(i));
        }
    }


}
