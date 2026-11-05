package com.example.SprotsAcademy;

import com.example.SprotsAcademy.model.Subscription;
import javafx.application.Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.SprotsAcademy.Database.*;

public class CancelSubscription extends Application {
    private Stage stage;
    private TableView<Subscription> tableView;
    List<Subscription> subscriptions;

    public void start(Stage primaryStage) {
        subscriptions = getSubscriptions();
        this.stage = primaryStage;
        tableView = new TableView<>();
        tableView.setMaxWidth(600);
        tableView.setMaxHeight(400);
        setupTable();

        Label tableLabel = new Label("Select a Subscription to Cancel:");
        tableLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

        VBox tableContainer = new VBox(tableLabel, tableView);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setSpacing(5);





        // Payment Button
        Button cancelSubscription= new Button("Cancel Subscription");
        cancelSubscription.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        cancelSubscription.setOnMouseEntered(e -> cancelSubscription.setStyle("-fx-background-color: LIMEGREEN"));
        cancelSubscription.setOnMouseExited(e -> cancelSubscription.setStyle("-fx-background-color: SILVER;"));
        cancelSubscription.setOnAction(e -> {
            cancelSelectedSubscription();
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
                SubscriptionManagement display = new SubscriptionManagement();
                display.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        // Add all elements to the grid

        HBox buttonBoxes = new HBox(cancelSubscription,backButton);
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
        // Create athleteColumn
        TableColumn<Subscription, String> athleteColumn = new TableColumn<>("Athlete Name");
        athleteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAthlete().getName()));
        athleteColumn.setMinWidth(150);

        // Create trainingProgramColumn
        TableColumn<Subscription, String> trainingProgramColumn = new TableColumn<>("Training Program");
        trainingProgramColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Subscription, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Subscription, String> param) {
                return new SimpleStringProperty(param.getValue().getTrainingProgram().getSport());
            }
        });
        trainingProgramColumn.setMinWidth(150);

        // Create startDateColumn
        TableColumn<Subscription, String> startDateColumn = new TableColumn<>("Start Date");
        startDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Subscription, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Subscription, String> param) {
                LocalDate startDate = param.getValue().getStartDate();
                return new SimpleStringProperty(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });
        startDateColumn.setMinWidth(150);

        // Create endDateColumn
        TableColumn<Subscription, String> endDateColumn = new TableColumn<>("End Date");
        endDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Subscription, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Subscription, String> param) {
                LocalDate endDate = param.getValue().getEndDate();
                return new SimpleStringProperty(endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
        });
        endDateColumn.setMinWidth(150);

        // Add columns to the TableView
        tableView.getColumns().addAll(athleteColumn, trainingProgramColumn, startDateColumn, endDateColumn);

        // Add data to the TableView
        tableView.getItems().addAll(subscriptions);
    }
    private void cancelSelectedSubscription() {
        Subscription selectedSubscription = tableView.getSelectionModel().getSelectedItem();
        if (selectedSubscription != null) {
            System.out.println("before:");
            for(Subscription subscription:subscriptions){
                System.out.println(subscription.getSerialNumber());
            }
            System.out.println("----------------\nafter:");

            subscriptions.remove(selectedSubscription);

            for(Subscription subscription:subscriptions){
                System.out.println(subscription.getSerialNumber());
            }
            System.out.println("----------------------------");
            tableView.getItems().remove(selectedSubscription);


            saveSubscriptionsToExcel(subscriptions);

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
