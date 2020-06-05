package TiqDiet_class;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dish {

    String dish_name;
    String dish_description;
    ArrayList<String> dish_category_list = new ArrayList<String>();
    ArrayList<Ingredients> dish_ingeredients_list = new ArrayList<Ingredients>();
    String serving;
    String preparation;
    Ingredients new_ingredients;

    public void New_dish_name(String name){
        dish_name = name;
    }

    public void New_dish_description(String description){
        dish_description = description;
    }

    public void Add_dish_ingredients(String ingredients, String quantity, String measure){
        new_ingredients = new Ingredients();
        new_ingredients.New_ingredients(ingredients,quantity,measure);
        dish_ingeredients_list.add(new_ingredients);
    }

    public void New_dish_category(String category){
        String temp = "";
        int x = 0;
        for(int i = 0; i < category.length(); i++){
            if(',' == category.charAt(i)){
                temp = category.substring(x,i);
                dish_category_list.add(temp);
                x = i + 2;
            }
        }
        if(x < category.length()){
            temp = category.substring(x);
            dish_category_list.add(temp);
        }
    }

    public void New_dish_prepering(String prepering){
        Pattern pattern_int = Pattern.compile("\\d+");
        Matcher matcher_int;
        String temp = "";
        int x = 0;
        for(int i = 0; i < prepering.length(); i++){
            if(prepering.charAt(i) == '/'){
                temp = prepering.substring(x,i);
                x = i + 1;
                if(temp.indexOf("Serving") >= 0){
                    matcher_int = pattern_int.matcher(temp);
                    while(matcher_int.find()){
                        serving = matcher_int.group();
                    }
                }
            }
        }
        temp = prepering.substring(x);
        if(temp.indexOf("Preparation") >= 0){
            matcher_int = pattern_int.matcher(temp);
            while(matcher_int.find()){
                preparation = matcher_int.group();
            }
        }
    }

    public String get_dish_name(){
        return dish_name;
    }

    public String get_dish_description(){
        return dish_description;
    }

    public int get_ingredients_list_size(){
        return dish_ingeredients_list.size();
    }

    public Ingredients get_ingredients(int index){
        new_ingredients = dish_ingeredients_list.get(index);
        return new_ingredients;
    }

    public String get_dish_ingredients_name(){
        return new_ingredients.get_ingredients_name();
    }

    public String get_dish_ingredients_quantity(){
        return new_ingredients.get_ingredients_quantity();
    }

    public String get_ingredients_measure(){
        return new_ingredients.get_ingredients_measure();
    }

    public String get_dish_serving(){
        return serving;
    }

    public String get_dish_preparation(){
        return preparation;
    }

    public int get_dish_category_list_size(){
        return dish_category_list.size();
    }

    public String get_dish_category(int index){
        return dish_category_list.get(index);
    }
}
