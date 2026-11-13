package com.example.SprotsAcademy.unrefactored;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainMenu extends Application {
    private Stage stage;

    public MainMenu() {
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        BorderPane borderPane = new BorderPane();

        HBox navigationBar = new HBox();
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.setPadding(new Insets(5));
        navigationBar.setSpacing(50); // Add spacing between buttons
        navigationBar.setMinWidth(300);

        Button athleteManagementBtn = new Button("Athlete Management\n");
        athleteManagementBtn.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        athleteManagementBtn.setOnMouseEntered(e -> athleteManagementBtn.setStyle("-fx-background-color: gray;"));
        athleteManagementBtn.setOnMouseExited(e -> athleteManagementBtn.setStyle("-fx-background-color: LIGHTGREEN;"));
        Button subscriptionManagementBtn = new Button("Subscription management\n");
        subscriptionManagementBtn.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
        subscriptionManagementBtn.setOnMouseEntered(e -> subscriptionManagementBtn.setStyle("-fx-background-color: gray;"));
        subscriptionManagementBtn.setOnMouseExited(e -> subscriptionManagementBtn.setStyle("-fx-background-color: GOLD;"));
        Button reservationManagementBtn = new Button("Reservation management\n");
        reservationManagementBtn.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        reservationManagementBtn.setOnMouseEntered(e -> reservationManagementBtn.setStyle("-fx-background-color: gray;"));
        reservationManagementBtn.setOnMouseExited(e -> reservationManagementBtn.setStyle("-fx-background-color: LIGHTBLUE;"));

        // Set button width to be equal and align center
        athleteManagementBtn.setMaxWidth(Double.MAX_VALUE);
        subscriptionManagementBtn.setMaxWidth(Double.MAX_VALUE);
        reservationManagementBtn.setMaxWidth(Double.MAX_VALUE);
        athleteManagementBtn.setMinWidth(150);
        subscriptionManagementBtn.setMinWidth(150);
        reservationManagementBtn.setMinWidth(150);

        // Add buttons to the navigation bar
        navigationBar.getChildren().addAll(athleteManagementBtn, subscriptionManagementBtn, reservationManagementBtn);

        Label welcome = new Label("Management of Sports Academies");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        welcome.setAlignment(Pos.CENTER);
        welcome.setFont(Font.font(20));

        // Load the image
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/sportsManagement.png")));
        imageView.setFitHeight(200);  // Adjust as necessary
        imageView.setPreserveRatio(true);


        VBox heading = new VBox(imageView,welcome);
        heading.setAlignment(Pos.CENTER);
        heading.setSpacing(0);


        VBox menubar = new VBox(heading, navigationBar);
        menubar.setAlignment(Pos.CENTER);
        menubar.setSpacing(25);

        // Add menubar to the BorderPane
        borderPane.setCenter(menubar);

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        // Create scene
        Scene scene = new Scene(borderPane, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.setTitle("Main Menu");
        stage.show();

        athleteManagementBtn.setOnAction(event -> {
            try {
                AthleteManagement display = new AthleteManagement();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        reservationManagementBtn.setOnAction(event -> {
            try {
                ReservationManagement display = new ReservationManagement();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        subscriptionManagementBtn.setOnAction(event -> {
            try {
                SubscriptionManagement display = new SubscriptionManagement();
                display.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
