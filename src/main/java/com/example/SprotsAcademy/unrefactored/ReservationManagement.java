package com.example.SprotsAcademy.unrefactored;
import com.example.SprotsAcademy.model.Athlete;
import com.example.SprotsAcademy.model.TrainingProgram;
import com.example.SprotsAcademy.model.TrainingProgramReservation;
import javafx.application.Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.example.SprotsAcademy.Database.getReservations;

public class ReservationManagement extends Application{
    private Stage stage;

    List<TrainingProgramReservation> reservations;
    TableView<TrainingProgramReservation> tableView = new TableView<>();

    public ReservationManagement() {
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("here");
        reservations = getReservations();
        System.out.println("here2");

        if (reservations == null) {
            System.out.println("Error: Failed to retrieve reservations.");
            reservations = new ArrayList<>();
        }


        this.stage = primaryStage;
        BorderPane borderPane = new BorderPane();

        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.setPadding(new Insets(5));
        navigationBar.setSpacing(50); // Add spacing between buttons
        navigationBar.setMinWidth(300);


        Button mainMenuBtn = new Button("Back\n");
        mainMenuBtn.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        mainMenuBtn.setOnMouseEntered(e -> mainMenuBtn.setStyle("-fx-background-color: RED;"));
        mainMenuBtn.setOnMouseExited(e -> mainMenuBtn.setStyle("-fx-background-color: SILVER;"));;

        Button newReserve = new Button("Register a new reservation\n");
        newReserve.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        newReserve.setOnMouseEntered(e -> newReserve.setStyle("-fx-background-color: gray;"));
        newReserve.setOnMouseExited(e -> newReserve.setStyle("-fx-background-color: LIGHTGREEN;"));

        Button cancelReserve = new Button("Cancel reservation\n");
        cancelReserve.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        cancelReserve.setOnMouseEntered(e -> cancelReserve.setStyle("-fx-background-color: gray;"));
        cancelReserve.setOnMouseExited(e -> cancelReserve.setStyle("-fx-background-color: GOLD;"));



        // Set button width to be equal and align center
        mainMenuBtn.setMaxWidth(Double.MAX_VALUE);
        newReserve.setMaxWidth(Double.MAX_VALUE);
        cancelReserve.setMaxWidth(Double.MAX_VALUE);

        mainMenuBtn.setMinWidth(100);
        newReserve.setMinWidth(100);
        cancelReserve.setMinWidth(100);


        // Add buttons to the navigation bar
        navigationBar.getChildren().addAll(newReserve, cancelReserve, mainMenuBtn);

        Label welcome = new Label("Reservation Management");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        welcome.setAlignment(Pos.CENTER);
        VBox menubar = new VBox(welcome, navigationBar);
        menubar.setAlignment(Pos.CENTER);
        menubar.setSpacing(20);

        // Create the athlete table
        tableView = new TableView<>();
        tableView.setMaxWidth(600);
        tableView.setMaxHeight(400);
        setupTable();



        Label tableNameLabel = new Label("List of Reservations");
        tableNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");


        VBox tableContainer = new VBox(tableNameLabel,tableView);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setPadding(new Insets(10));



        // Add the navigation bar and table to a new VBox
        VBox mainContainer = new VBox(menubar, tableContainer);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(20);
        borderPane.setCenter(mainContainer);

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        // Create scene
        Scene scene = new Scene(borderPane, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setTitle("Reservation Management");
        stage.show();


        mainMenuBtn.setOnAction(event -> {
            try {
                MainMenu display = new MainMenu();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        cancelReserve.setOnAction(event -> {
            try {
                CancelReservation display = new CancelReservation();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        newReserve.setOnAction(event -> {
            try {
                AddReservation display = new AddReservation();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }



    private void setInitialBackground(Button button) {
        button.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: lightblue;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: lightgray;"));
    }


    private void setupTable() {
        // Define columns
        TableColumn<TrainingProgramReservation, String> sportColumn = new TableColumn<>("Sport");
        sportColumn.setMinWidth(200);
        sportColumn.setCellValueFactory(cellData -> {
            TrainingProgram trainingProgram = cellData.getValue().getTrainingProgram();
            if (trainingProgram != null) {
                return new SimpleStringProperty(trainingProgram.getSport());
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<TrainingProgramReservation, String> athleteNameColumn = new TableColumn<>("Athlete Name");
        athleteNameColumn.setMinWidth(200);
        athleteNameColumn.setCellValueFactory(cellData -> {
            Athlete athlete = cellData.getValue().getAthlete();
            if (athlete != null) {
                return new SimpleStringProperty(athlete.getName());
            } else {
                return new SimpleStringProperty("");
            }
        });

        TableColumn<TrainingProgramReservation, String> dateColumn = new TableColumn<>("Training Date");
        dateColumn.setMinWidth(200);
        dateColumn.setCellValueFactory(cellData -> {
            LocalDate trainingDate = cellData.getValue().getTrainingProgramDate();
            if (trainingDate != null) {
                return new SimpleStringProperty(trainingDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            } else {
                return new SimpleStringProperty("");
            }
        });

        // Add columns to table
        tableView.getColumns().addAll(sportColumn, athleteNameColumn, dateColumn);

        // Set items (reservations) to the table
        ObservableList<TrainingProgramReservation> reservationData = FXCollections.observableArrayList(reservations);
        tableView.setItems(reservationData);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
