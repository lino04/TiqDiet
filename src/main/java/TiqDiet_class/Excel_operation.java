package TiqDiet_class;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class Excel_operation {

    ArrayList<Dish> dish_list = new ArrayList<Dish>();
    String failed_product;
    public String file_path;

    public void Read_file(String file_path) throws IOException {

        //FileInputStream add_dish = new FileInputStream(new File("dish_data/add_dish.xlsx"));
        FileInputStream add_dish = new FileInputStream(new File(file_path));
        XSSFWorkbook wb = new XSSFWorkbook(add_dish);
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        Dish new_dish = null;
        int temp_i = 0;
        int row_int = 0;


        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            boolean get_name = false;
            boolean get_ingredients = false;
            boolean get_description = false;
            boolean get_category = false;
            boolean get_preper_data = false;
            String ingredients_name = null;
            String ingredients_quantity = null;
            String ingredients_measure;

            while(cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                switch(cell.getColumnIndex()){
                    case 0:
                        switch (cell.getCellType()){
                            case Cell.CELL_TYPE_STRING:
                                get_ingredients = false;
                                switch (cell.getStringCellValue()){
                                    case "Nazwa dania":
                                        get_name = true;
                                        get_description = false;
                                        get_category = false;
                                        get_preper_data = false;
                                        new_dish = new Dish();
                                        break;
                                    case "Opis przygotowania:":
                                        get_name = false;
                                        get_description = true;
                                        get_category = false;
                                        get_preper_data = false;
                                        break;
                                    case "Cechy dania do określenia kategorii:":
                                        get_name = false;
                                        get_description = false;
                                        get_category = true;
                                        get_preper_data = false;
                                        break;
                                    case "Inne informacje:":
                                        get_name = false;
                                        get_description = false;
                                        get_category = false;
                                        get_preper_data = true;
                                        break;
                                    case "Komentarz:":
                                        temp_i++;
                                        dish_list.add(new_dish);
                                        break;
                                }
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                get_ingredients = true;
                                get_name = false;
                                get_description = false;
                                get_category = false;
                                get_preper_data = false;
                                break;
                        }
                        break;
                    case 1:
                        if(get_name == true){
                            new_dish.New_dish_name(cell.getStringCellValue());
                        }
                        if(get_ingredients == true){
                            ingredients_name = cell.getStringCellValue();
                        }
                        if(get_description == true){
                            new_dish.New_dish_description(cell.getStringCellValue());
                        }
                        if(get_category == true){
                            if(cell.getStringCellValue().equals("")){
                                new_dish.New_dish_category("empty");
                                //System.out.println("empty"+cell.getRowIndex());
                            }else {
                                new_dish.New_dish_category(cell.getStringCellValue());
                            }
                        }
                        if(get_preper_data == true){
                            new_dish.New_dish_prepering(cell.getStringCellValue());
                        }
                        break;
                    case 2:
                        if(get_ingredients == true){
                            ingredients_quantity = String.valueOf(cell.getNumericCellValue());
                        }
                        break;
                    default:
                        if(get_ingredients == true){
                            if(!cell.getStringCellValue().equals("")){
                                ingredients_measure = cell.getStringCellValue();
                                new_dish.Add_dish_ingredients(ingredients_name, ingredients_quantity, ingredients_measure);
                            }
                        }
                        break;
                }
            }
        }

    }

    public ArrayList<Dish> get_dish_list(){
        return dish_list;
    }

    public void get_error_file_dish(ArrayList<Dish> failed_dish_list) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Failed_dish");
        int row_count = 0;

        for(int i = 0; i < failed_dish_list.size(); i++){

            Dish fialed_dish = new Dish();
            fialed_dish = failed_dish_list.get(i);

            XSSFRow row = sheet.createRow(row_count);

            row.createCell(0).setCellValue("Nazwa dania:");
            row.createCell(1).setCellValue(fialed_dish.get_dish_name());

            row_count++;

            row = sheet.createRow(row_count);
            row.createCell(0).setCellValue("Lp");
            row.createCell(1).setCellValue("Nazwa składnika/produktu");
            row.createCell(2).setCellValue("Ilość");
            row.createCell(3).setCellValue("Miara domowa");

            row_count++;
            Ingredients ingredient = new Ingredients();
            for(int j = 0; j < fialed_dish.get_ingredients_list_size(); j++){
                row = sheet.createRow(row_count);
                ingredient = fialed_dish.get_ingredients(j);
                row.createCell(0).setCellValue(j+1);
                row.createCell(1).setCellValue(ingredient.get_ingredients_name());
                row.createCell(2).setCellValue(ingredient.get_ingredients_quantity());
                row.createCell(3).setCellValue(ingredient.get_ingredients_measure());
                row_count++;
            }

            row = sheet.createRow(row_count);
            row.createCell(0).setCellValue("Opis przygotowania:");
            row.createCell(1).setCellValue(fialed_dish.get_dish_description());

            row_count++;
            row = sheet.createRow(row_count);
            row.createCell(0).setCellValue("Cechy dania do określenia kategorii:");

            String category = null;
            for(int j = 0; j < fialed_dish.get_dish_category_list_size(); j++){
                category = fialed_dish.get_dish_category(j) + " ,";
            }

            row.createCell(1).setCellValue(category);

            row_count++;

            row = sheet.createRow(row_count);
            row.createCell(0).setCellValue("Inne informacje:");
            row.createCell(1).setCellValue("Servings: "+fialed_dish.get_dish_serving()+" / Preparation time: "+fialed_dish.get_dish_preparation()+" minutes");

            row_count++;
            row = sheet.createRow(row_count);
            row.createCell(0).setCellValue("Komentarz:");

            row_count++;
            row = sheet.createRow(row_count);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        String file_name = "failed_dish_"+dtf.format(now)+".xlsx";

        FileOutputStream get_failed_file = new FileOutputStream(new File("dish_data/"+file_name));
        wb.write(get_failed_file);
        get_failed_file.close();
        wb.close();
        file_path = "dish_data/"+file_name;
        //Desktop.getDesktop().open(new File("dish_data/"+file_name));
    }

    public void Get_failed_product_file(ArrayList<Ingredients> ingredinet) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet("Failed_product");

        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("Nazwa składnika/produktu");
        row.createCell(1).setCellValue("Miara domowa");

        for(int i = 0; i < ingredinet.size(); i++){

            String name = ingredinet.get(i).get_ingredients_name();
            String measure = ingredinet.get(i).get_ingredients_measure();
            row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(name);
            row.createCell(1).setCellValue(measure);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
        LocalDateTime now = LocalDateTime.now();
        failed_product = "failed_product_"+dtf.format(now)+".xlsx";
        File out_file = new File("dish_data/"+failed_product);
        FileOutputStream get_failed_file = new FileOutputStream(out_file);
        wb.write(get_failed_file);
        file_path = out_file.getPath();
        get_failed_file.close();
        wb.close();


    }

    public ArrayList<Ingredients> get_fialed_product(String file_path) throws IOException {

        ArrayList<Ingredients> fialed_product_list = new ArrayList<Ingredients>();
        FileInputStream add_product = new FileInputStream(new File(file_path));
        XSSFWorkbook wb = new XSSFWorkbook(add_product);
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        String name = null;
        String measure = null;

        while(rowIterator.hasNext()) {
            Ingredients ingredient = new Ingredients();
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();

            if(row.getRowNum() > 0){

                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();

                    switch(cell.getColumnIndex()){
                        case 0:
                            name = cell.getStringCellValue();
                            break;
                        case 1:
                            measure = cell.getStringCellValue();
                            break;
                    }
                }

                ingredient.New_ingredients(name, measure);
                fialed_product_list.add(ingredient);
            }
        }

        return fialed_product_list;
    }
}
