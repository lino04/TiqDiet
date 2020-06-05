package TiqDiet_class;

import java.util.ArrayList;

public class Ingredients {

    String ingredients_name;
    String ingredients_quantity;
    String ingredients_measure;
    int x = 0;

    ArrayList<Ingredients> ingredients_list = new ArrayList<Ingredients>();

    public void New_ingredients(String name, String quantity, String measure){
        ingredients_name = name;
        ingredients_quantity = quantity;
        ingredients_measure = measure;
    }

    public void New_ingredients(String name, String measure){
        ingredients_name = name;
        ingredients_measure = measure;
    }

    public String get_ingredients_name(){
        return ingredients_name;
    }

    public String get_ingredients_quantity(){
        return ingredients_quantity;
    }

    public String get_ingredients_measure(){
        return  ingredients_measure;
    }

    public void Creat_ingredients_list(Ingredients ingredient){
        String name = ingredient.get_ingredients_name();
        String measure =ingredient.get_ingredients_measure();
        Ingredients temp_ingredient = new Ingredients();
        boolean exist = false;

        for (int i = 0; i < ingredients_list.size(); i++) {

            if(name.equals(ingredients_list.get(i).get_ingredients_name())){
                if(measure.equals(ingredients_list.get(i).get_ingredients_measure())){
                    exist = true;
                }
            }
        }
        if(exist == false){
            ingredients_list.add(ingredient);
        }
    }

    public ArrayList<Ingredients> get_Ingredients_list(){
        return  ingredients_list;
    }
}
