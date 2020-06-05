package GUI;

import Test_TiqDiet.Add_dish;
import Test_TiqDiet.Product_verification;
import TiqDiet_class.Dish;
import TiqDiet_class.Excel_operation;
import TiqDiet_class.Ingredients;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

public class GUI_setup extends JFrame{
    private JPanel mainPanel;
    private JButton addDishFileButton;
    public JTextField dish_fileTextField;
    private JTextArea out_put_area;
    private JButton startButton;
    public JTextField login_TextField;
    public JCheckBox englishCheckBox;
    public JCheckBox tiqDietEnglishCoFIDCheckBox;
    public JCheckBox checkProduct;
    public JCheckBox fileExistCheckBox;
    public JTextField check_product_file;
    public JProgressBar progressBar1;
    private JLabel login_label;
    private JLabel password_label;
    public JPasswordField password_Field;
    private JScrollPane out_put_scroll;
    public String out_put = "";
    Task task;
    public double progres = 0;

    Excel_operation input_file = new Excel_operation();
    public ArrayList<Dish> input_dish_list = new ArrayList<Dish>();
    public ArrayList<Ingredients> failed_product_list = new ArrayList<Ingredients>();

    class Task extends SwingWorker<Void, Void>{

        GUI_setup x;
        public Task(GUI_setup gui_setup) {
            x = gui_setup;
        }

        @Override
        protected Void doInBackground() throws Exception {
            out_put = "";
            add_out_put("Start read file: "+ dish_fileTextField.getText());
            input_file.Read_file(dish_fileTextField.getText());
            input_dish_list = input_file.get_dish_list();
            add_out_put("End read file: "+ dish_fileTextField.getText());
            add_out_put("In file find: "+input_dish_list.size() + " dish to add");
            progressBar1.setValue((int) (progres+5));

            if(checkProduct.isSelected()){
                if(fileExistCheckBox.isSelected()){
                    add_out_put("Start read file: "+ check_product_file.getText());
                    failed_product_list = input_file.get_fialed_product(check_product_file.getText());
                    add_out_put("End read file: "+ check_product_file.getText());
                    add_out_put("In file find: "+failed_product_list.size() + " products is not add to Tiq-Database");
                }else{
                    add_out_put("Start product verification");
                    Product_verification prod_very = new Product_verification(input_dish_list,x);
                    failed_product_list = input_file.get_fialed_product(check_product_file.getText());
                    add_out_put(failed_product_list.size() + " products is not add to Tiq-Database");
                }
            }else{
                progressBar1.setValue((int)(progres+5));
            }

            Add_dish add_dish = new Add_dish(input_dish_list, x, failed_product_list);


            add_out_put("Done!!");

            startButton.setEnabled(true);
            login_TextField.setEnabled(true);
            password_Field.setEnabled(true);
            addDishFileButton.setEnabled(true);
            englishCheckBox.setEnabled(true);
            tiqDietEnglishCoFIDCheckBox.setEnabled(true);
            checkProduct.setEnabled(true);
            fileExistCheckBox.setEnabled(true);
            return null;
        }
    }

    public void set_progres(double pro){
        progressBar1.setValue((int) pro);
    }

    public void close_lisner(){
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public void set_start_GUI(){
        dish_fileTextField.setEditable(false);
        out_put_area.setEditable(false);
        fileExistCheckBox.setVisible(false);
        check_product_file.setEditable(false);
        check_product_file.setVisible(false);
        progressBar1.setVisible(false);
        login_TextField.setVisible(false);
        password_Field.setVisible(false);
        englishCheckBox.setVisible(false);
        tiqDietEnglishCoFIDCheckBox.setVisible(false);
        checkProduct.setVisible(false);
        login_label.setVisible(false);
        password_label.setVisible(false);
        startButton.setVisible(false);

    }

    public void add_file_dish(JFrame gui){
        addDishFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser select_file_dish = new JFileChooser();
                int i = select_file_dish.showOpenDialog(gui);
                if(i == 0){
                    File file_dish = select_file_dish.getSelectedFile();
                    String file_path = file_dish.getPath();
                    dish_fileTextField.setText(file_path);
                    if(!dish_fileTextField.equals("")){
                        login_label.setVisible(true);
                        login_TextField.setVisible(true);
                        password_label.setVisible(true);
                        password_Field.setVisible(true);
                        checkProduct.setVisible(true);
                        tiqDietEnglishCoFIDCheckBox.setVisible(true);
                        englishCheckBox.setVisible(true);
                        startButton.setVisible(true);
                    }
                    refresh();
                }
            }
        });
    }

    public void add_product_file(JFrame gui){
        checkProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(checkProduct.isSelected()){
                    fileExistCheckBox.setVisible(true);
                    check_product_file.setVisible(true);
                    fileExistCheckBox.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(fileExistCheckBox.isSelected()){
                                JFileChooser select_file_dish = new JFileChooser();
                                int i = select_file_dish.showOpenDialog(gui);
                                if(i == 0){
                                    File file_dish = select_file_dish.getSelectedFile();
                                    String file_path = file_dish.getPath();
                                    check_product_file.setText(file_path);
                                }
                            }else{
                                check_product_file.setText("");
                                fileExistCheckBox.setSelected(true);

                            }
                            refresh();
                        }
                    });
                }else{
                    fileExistCheckBox.setVisible(false);
                    check_product_file.setVisible(false);
                    check_product_file.setText("");
                }
                refresh();
            }
        });
    }

    public void start_scrept(JFrame gui){
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!login_TextField.getText().equals("") && !password_Field.getText().equals("")){
                    out_put = out_put + "Read input dish file: " +dish_fileTextField.getText()+ "\r\n";

                    startButton.setEnabled(false);
                    progressBar1.setVisible(true);
                    login_TextField.setEnabled(false);
                    password_Field.setEnabled(false);
                    addDishFileButton.setEnabled(false);
                    englishCheckBox.setEnabled(false);
                    tiqDietEnglishCoFIDCheckBox.setEnabled(false);
                    checkProduct.setEnabled(false);
                    fileExistCheckBox.setEnabled(false);
                    out_put_area.setText(out_put);
                    progressBar1.setValue(0);
                    progressBar1.setStringPainted(true);
                    refresh();
                    try {
                        run_scrept();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }

                if(login_TextField.getText().equals("") && password_Field.getText().equals("")){
                    JOptionPane.showMessageDialog(gui,
                            "Login and Password is empty",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                }

                if(!login_TextField.getText().equals("") && password_Field.getText().equals("")){
                    JOptionPane.showMessageDialog(gui,
                            "Password is empty",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                }

                if(login_TextField.getText().equals("") && !password_Field.getText().equals("")){
                    JOptionPane.showMessageDialog(gui,
                            "Login is empty",
                            "Inane error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public void run_scrept() throws Exception {
        task = new Task(this);
        task.execute();
    }

    public void add_out_put(String text){
        out_put = out_put +text+ "\r\n";
        out_put_area.setText(out_put);
        refresh();
    }

    public void refresh(){
        revalidate();
        repaint();
    }

    public void Start_GUI(){
        set_start_GUI();
        close_lisner();

        add_file_dish(this);
        add_product_file(this);
        start_scrept(this);

        setSize(600,600);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
        getFrames();
    }

}
