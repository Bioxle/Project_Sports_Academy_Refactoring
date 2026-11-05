package com.example.SprotsAcademy;

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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.SprotsAcademy.Database.*;

public class CancelReservation extends Application {
    private Stage stage;
    List<TrainingProgramReservation> reservations;
    TableView<TrainingProgramReservation> tableView = new TableView<>();

    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        reservations = getReservations();
        // Create the athlete table

        // Create the athlete table
        tableView = new TableView<>();
        tableView.setMaxWidth(600);
        tableView.setMaxHeight(400);
        setupTable();

        Label tableLabel = new Label("Select a Reserve to Cancel:");
        tableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

        VBox tableContainer = new VBox(tableLabel, tableView);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setSpacing(5);






        // Payment Button
        Button cancelReserve = new Button("Cancel Reservation");
        cancelReserve.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        cancelReserve.setOnMouseEntered(e -> cancelReserve.setStyle("-fx-background-color: LIMEGREEN"));
        cancelReserve.setOnMouseExited(e -> cancelReserve.setStyle("-fx-background-color: SILVER;"));
        cancelReserve.setOnAction(e -> {
            cancelSelectedReservation();
        });
        // Back Button
        Button backButton = new Button("Back");
        backButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: RED;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: SILVER;"));

        backButton.setOnAction(e -> {
            try {
                // Navigate back to previous screen (if applicable)
                // Example: Navigate back to AthleteManagement screen
                ReservationManagement display = new ReservationManagement();
                display.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // Add all elements to the grid

        HBox buttonBoxes = new HBox(cancelReserve,backButton);
        buttonBoxes.setAlignment(Pos.CENTER);
        buttonBoxes.setSpacing(10);

        VBox mainContainer = new VBox(tableContainer, buttonBoxes);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setSpacing(20);






        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        // Create scene
        Scene scene = new Scene(mainContainer, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setTitle("Athlete Management");
        stage.show();
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


    private void cancelSelectedReservation() {
        TrainingProgramReservation selectedReservation = tableView.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            System.out.println("before:");
            for(TrainingProgramReservation reservation:reservations){
                System.out.println(reservation.getUniqueCode());
            }
            System.out.println("----------------\nafter:");

            reservations.remove(selectedReservation);

            for(TrainingProgramReservation reservation:reservations){
                System.out.println(reservation.getUniqueCode());
            }
            System.out.println("----------------------------");
            tableView.getItems().remove(selectedReservation);


            saveReservationsToExcel(reservations);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Subscription Canceled");
            alert.setHeaderText(null);
            alert.setContentText("Subscription has been successfully canceled.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a subscription to cancel.");
            alert.showAndWait();
        }
    }


}
