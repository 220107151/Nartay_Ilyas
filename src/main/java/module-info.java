module com.example.nartay_ilyas_sudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nartay_ilyas_sudoku to javafx.fxml;
    exports com.example.nartay_ilyas_sudoku;
}