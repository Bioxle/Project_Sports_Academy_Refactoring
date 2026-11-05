package com.example.SprotsAcademy;

import com.example.SprotsAcademy.model.Subscription;
import javafx.application.Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.SprotsAcademy.Database.getSubscriptions;

public class SubscriptionManagement extends Application{
    private Stage stage;
    List<Subscription> subscriptions;

    TableView<Subscription> tableView = new TableView<>();
    public SubscriptionManagement() {
    }

    @Override
    public void start(Stage primaryStage) {
        subscriptions = getSubscriptions();
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

        Button newSubscription = new Button("Create New Subscription\n");
        newSubscription.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        newSubscription.setOnMouseEntered(e -> newSubscription.setStyle("-fx-background-color: gray;"));
        newSubscription.setOnMouseExited(e -> newSubscription.setStyle("-fx-background-color: LIGHTGREEN;"));

        Button cancelSubscription = new Button("Cancel subscription\n");
        cancelSubscription.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        cancelSubscription.setOnMouseEntered(e -> cancelSubscription.setStyle("-fx-background-color: gray;"));
        cancelSubscription.setOnMouseExited(e -> cancelSubscription.setStyle("-fx-background-color: GOLD;"));





        // Set button width to be equal and align center
        mainMenuBtn.setMaxWidth(Double.MAX_VALUE);
        newSubscription.setMaxWidth(Double.MAX_VALUE);
        cancelSubscription.setMaxWidth(Double.MAX_VALUE);

        mainMenuBtn.setMinWidth(100);
        newSubscription.setMinWidth(100);
        cancelSubscription.setMinWidth(100);


        // Add buttons to the navigation bar
        navigationBar.getChildren().addAll(newSubscription, cancelSubscription,mainMenuBtn);

        Label welcome = new Label("Subscription Management");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        welcome.setAlignment(Pos.CENTER);
        welcome.setFont(Font.font(20));
        VBox menubar = new VBox(welcome, navigationBar);
        menubar.setAlignment(Pos.CENTER);
        menubar.setSpacing(20);

        // Create the athlete table
        tableView = new TableView<>();
        tableView.setMaxWidth(600);
        tableView.setMaxHeight(400);
        setupTable();

        Label tableNameLabel = new Label("List of Subscriptions");
        tableNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: normal;");

        VBox tableContainer = new VBox(tableNameLabel, tableView);
        tableContainer.setAlignment(Pos.CENTER);
        tableContainer.setSpacing(5);
        tableContainer.setPadding(new Insets(5));


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
        stage.setTitle("Athlete Management");
        stage.show();

        // Set button actions


        mainMenuBtn.setOnAction(event -> {
            try {
                MainMenu display = new MainMenu();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        newSubscription.setOnAction(event -> {
            try {
                AddSubscription display = new AddSubscription();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        cancelSubscription.setOnAction(event -> {
            try {
                CancelSubscription display = new CancelSubscription();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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

    private void setInitialBackground(Button button) {
        button.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: lightblue;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: lightgray;"));
    }




    public static void main(String[] args) {
        launch(args);
    }
}
