package Test_TiqDiet;

import GUI.GUI_setup;
import TiqDiet_class.Dish;
import TiqDiet_class.Excel_operation;
import TiqDiet_class.Ingredients;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Add_dish {

    WebDriver chrome;
    ArrayList<Dish> input_dish_list= new ArrayList<Dish>();
    ArrayList<Dish> passed_dish_list = new ArrayList<Dish>();
    ArrayList<Dish> failed_dish_list = new ArrayList<Dish>();
    ArrayList<Ingredients> failed_product_list = new ArrayList<Ingredients>();
    GUI_setup gui_input;
    boolean englis;
    boolean data_base;
    int all_dish;

    @BeforeMethod
    public void start(){

        check_dish_product();
        gui_input.add_out_put(failed_dish_list.size()+" dish hasn't product in database");

        System.setProperty("webdriver.chrome.driver", "driver/83/chromedriver.exe");
        chrome = new ChromeDriver();
        chrome.manage().window().maximize();
        chrome.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
    }

    @Test
    public void execut(){
        gui_input.add_out_put("Start add "+input_dish_list.size()+" dish");

    }

    @AfterMethod
    public void end() throws IOException {
        chrome.close();
        gui_input.add_out_put("All dish in file: "+all_dish);
        gui_input.add_out_put("Passed add dish: "+passed_dish_list.size());
        gui_input.add_out_put("Failed add dish: "+failed_dish_list.size());
        Excel_operation out_put = new Excel_operation();
        out_put.get_error_file_dish(failed_dish_list);
        gui_input.file_path = out_put.file_path;
        gui_input.add_out_put("File path: "+out_put.file_path);
    }

    public Add_dish(ArrayList<Dish> input_file_dish, GUI_setup gui, ArrayList<Ingredients> failed_product) throws IOException {
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
