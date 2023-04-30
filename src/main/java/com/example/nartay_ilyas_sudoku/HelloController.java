package com.example.nartay_ilyas_sudoku;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.util.RandomAccess;
import java.util.Scanner;


public class HelloController {
    @FXML Button start,showans;
    private String[][] task;
    @FXML
    Label time,BestTime;
    private int secondsPassed = 0;
    private Timeline timeline;
    @FXML
    RadioButton easy, hard, med;
    @FXML
    TextField arr11, arr12, arr13, arr14, arr15, arr16, arr17, arr18, arr19, arr21, arr22, arr23, arr24, arr25, arr26, arr27, arr28, arr29, arr31, arr32, arr33, arr34, arr35, arr36, arr37, arr38, arr39, arr41, arr42, arr43, arr44, arr45, arr46, arr47, arr48, arr49, arr51, arr52, arr53, arr54, arr55, arr56, arr57, arr58, arr59, arr61, arr62, arr63, arr64, arr65, arr66, arr67, arr68, arr69, arr71, arr72, arr73, arr74, arr75, arr76, arr77, arr78, arr79, arr81, arr82, arr83, arr84, arr85, arr86, arr87, arr88, arr89, arr91, arr92, arr93, arr94, arr95, arr96, arr97, arr98, arr99;

    public void initialize() throws FileNotFoundException {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    Field field = getClass().getDeclaredField("arr" + (i + 1) + "" + (j + 1));
                    TextField textField = (TextField) field.get(this);
                    setDigitTextField(textField);
                } catch (NoSuchFieldException e) {
                    System.out.println("jok var" + (i + 1) + "" + (j + 1));
                } catch (IllegalAccessException e) {
                    System.out.println("xz che eto" + (i + 1) + "" + (j + 1));
                }
            }
        }
        ToggleGroup s = new ToggleGroup();
        hard.setToggleGroup(s);
        med.setToggleGroup(s);
        easy.setToggleGroup(s);

        Scanner sc = new Scanner(new File("src/main/java/com/example/nartay_ilyas_sudoku/Record"));
        int rec = sc.nextInt();
        String format = "Best time:" + String.format("%02d:%02d:%02d", rec/60/60, rec/60 % 60, rec % 60 %60);
        BestTime.setText(format);
    }

    private void setDigitTextField(TextField textField) {
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), null,
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("\\d?")) {
                        return change;
                    }
                    return null;
                });
        textField.setTextFormatter(formatter);
    }
    SudokuGenerator g = new SudokuGenerator();

    @FXML
    void letstart() throws FileNotFoundException {
        if(start.getText().equals("Start")){
        int level = 0;
        if (easy.isSelected()) level = 1;
        else if (med.isSelected()) level = 2;
        else if (hard.isSelected()) level = 3;
       task = g.generate(level);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    Field field = getClass().getDeclaredField("arr" + (i + 1) + "" + (j + 1));
                    TextField textField = (TextField) field.get(this);
                    textField.setText(task[i][j]);
                    if (!task[i][j].equals("")) {
                        textField.setEditable(false);
                        textField.setStyle("-fx-text-fill: blue;");
                    }
                } catch (NoSuchFieldException e) {
                    System.out.println("jok var" + (i + 1) + "" + (j + 1));
                } catch (IllegalAccessException e) {
                    System.out.println("xz che eto" + (i + 1) + "" + (j + 1));
                }

            }
        }


         timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                secondsPassed++;
                String format = String.format("%02d:%02d:%02d", secondsPassed/60/60, secondsPassed/60 % 60, secondsPassed % 60 %60);
                time.setText(format);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        start.setText("Check");
        showans.setVisible(false);
    }
    else if(start.getText().equals("Check")){
        String[][] ans = g.getSolution();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    Field field = getClass().getDeclaredField("arr" + (i + 1) + "" + (j + 1));
                    TextField textField = (TextField) field.get(this);
                    if(!textField.getText().equals(ans[i][j])) {
                        showans.setVisible(true);
                        showErrorDialog("Wrong answer");
                    }
                } catch (NoSuchFieldException e) {
                    System.out.println("jok var" + (i + 1) + "" + (j + 1));
                } catch (IllegalAccessException e) {
                    System.out.println("xz che eto" + (i + 1) + "" + (j + 1));
                }

            }
        }
        Scanner sc = new Scanner(new File("com/example/nartay_ilyas_sudoku/Record "));
        if(sc.nextInt() >  secondsPassed){
           try(PrintWriter pw = new PrintWriter("com/example/nartay_ilyas_sudoku/Record ")) {
               pw.write(secondsPassed);
               String format = "Best time:" + String.format("%02d:%02d:%02d", secondsPassed/60/60, secondsPassed/60 % 60, secondsPassed % 60 %60);
               time.setText(format);
           }
        }
        timeline.stop();
        secondsPassed = 0;
        time.setText("00:00:00");
    }
}
@FXML
void showans(){
    String[][] ans = g.getSolution();
    for (int i = 0; i < 9; i++) {
        System.out.print("[");
        for (int j = 0; j < 8; j++) {
            System.out.print(ans[i][j]+ " , ");
        }
        System.out.println(ans[i][8]+"]");
    }

    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            try {
                Field field = getClass().getDeclaredField("arr" + (i + 1) + "" + (j + 1));
                TextField textField = (TextField) field.get(this);
                textField.setText(ans[i][j]);
                textField.setEditable(false);
            } catch (NoSuchFieldException e) {
                System.out.println("jok var" + (i + 1) + "" + (j + 1));
            } catch (IllegalAccessException e) {
                System.out.println("xz che eto" + (i + 1) + "" + (j + 1));
            }

        }
    }
    timeline.stop();
    secondsPassed = 0;
    time.setText("00:00:00");
    start.setText("Start");
}


    void showErrorDialog(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
        throw new RuntimeException("to stop code");
    }

}

