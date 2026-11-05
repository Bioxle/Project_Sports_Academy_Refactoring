package com.example.SprotsAcademy;

import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.example.SprotsAcademy.model.Athlete;

import java.time.LocalDate;

import static com.example.SprotsAcademy.Database.registerAthlete;

public class AthleteRegistration extends Application {
    private Stage stage;
    public AthleteRegistration(){}

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Label messageLabel = new Label("");

        stage.setTitle("Registration");

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // Headline Label
        Label headlineLabel = new Label("Athlete Registration");
        headlineLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        headlineLabel.setAlignment(Pos.CENTER);

        // Name Label - constraints use (column, row)
        Label nameLabel = new Label("Name:");
        GridPane.setConstraints(nameLabel, 0, 0);

        // Name Input
        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter your name");
        GridPane.setConstraints(nameInput, 1, 0);

        // Surname Label
        Label surnameLabel = new Label("Surname:");
        GridPane.setConstraints(surnameLabel, 0, 1);

        // Surname Input
        TextField surnameInput = new TextField();
        surnameInput.setPromptText("Enter your surname");
        GridPane.setConstraints(surnameInput, 1, 1);

        // Contact Information Label
        Label contactLabel = new Label("Contact Info:");
        GridPane.setConstraints(contactLabel, 0, 2);

        // Contact Information Input
        TextField contactInput = new TextField();
        contactInput.setPromptText("Enter your contact info");
        GridPane.setConstraints(contactInput, 1, 2);

        // Date of Birth Label
        Label dobLabel = new Label("Date of Birth:");
        GridPane.setConstraints(dobLabel, 0, 3);

        // Date of Birth Input
        DatePicker dobInput = new DatePicker();
        GridPane.setConstraints(dobInput, 1, 3);

        // Email Label
        Label sheetLabel = new Label("Sheet:");
        GridPane.setConstraints(sheetLabel, 0, 4);

        // Email Input
        TextField sheetInput = new TextField();
        sheetInput.setPromptText("Male/Female/Mixed");
        GridPane.setConstraints(sheetInput, 1, 4);

        // Professional Label
        Label professionalLabel = new Label("Professional:");
        GridPane.setConstraints(professionalLabel, 0, 5);

        // Professional Input
        ToggleGroup professionalGroup = new ToggleGroup();
        RadioButton yes = new RadioButton("Yes");
        yes.setToggleGroup(professionalGroup);
        RadioButton no = new RadioButton("No");
        no.setToggleGroup(professionalGroup);
        HBox professionBox = new HBox(10, yes, no);
        GridPane.setConstraints(professionBox, 1, 5);

        // Experience Level Label
        Label expLevel = new Label("Experience Level:");
        GridPane.setConstraints(expLevel, 0, 6);
        TextField expLevelField = new TextField();
        expLevelField.setPromptText("1-5");
        GridPane.setConstraints(expLevelField, 1, 6);

        // Register Button
        Button registerButton = new Button("Register");
        registerButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        registerButton.setOnMouseEntered(e -> registerButton.setStyle("-fx-background-color: LIMEGREEN"));
        registerButton.setOnMouseExited(e -> registerButton.setStyle("-fx-background-color: SILVER;"));
        registerButton.setOnAction(e -> {
            // Retrieve all input values

            String name = nameInput.getText().trim();
            String surname = surnameInput.getText().trim();
            String contact = contactInput.getText().trim();
            LocalDate dob = dobInput.getValue();
            String sheet = sheetInput.getText().trim(); // Assuming 'emailInput' is meant for 'Sheet'
            boolean isProfessional = yes.isSelected();
            String experienceLevel = expLevelField.getText().trim();


            if (name.isEmpty() || surname.isEmpty() || contact.isEmpty() || dob == null ||
                    sheet.isEmpty() || experienceLevel.isEmpty()) {
                messageLabel.setText("Please fill out all fields.");
                messageLabel.setTextFill(Color.RED);
                return; // Exit method without creating athlete if any field is empty
            }
            else{
                messageLabel.setText("");
            }


            Athlete athlete1 = new Athlete(name, surname,sheet, dob, contact, isProfessional, Integer.parseInt(experienceLevel));


            // Example: Print all data (you can modify this part to suit your needs)
            System.out.println("serial: " + athlete1.getSerialNumber());
            System.out.println("Name: " + name);
            System.out.println("Surname: " + surname);
            System.out.println("Contact: " + contact);
            System.out.println("Date of Birth: " + dob);
            System.out.println("Sheet: " + sheet);
            System.out.println("Professional: " + (isProfessional ? "Yes" : "No"));
            System.out.println("Experience Level: " + experienceLevel);

//             You can also call your 'registerAthlete' method here with the retrieved data

            System.out.println("data processing to save");
            boolean success = registerAthlete(athlete1);
            System.out.println("register function done");


        });

        // Back Button (assuming you have navigation logic)
        Button backButton = new Button("Back");
        backButton.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
        backButton.setOnMouseEntered(e -> backButton.setStyle("-fx-background-color: RED;"));
        backButton.setOnMouseExited(e -> backButton.setStyle("-fx-background-color: SILVER;"));

        backButton.setOnAction(e -> {
            try {
                AthleteManagement display = new AthleteManagement();
                display.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox buttonBoxes = new HBox(registerButton, backButton);
        buttonBoxes.setSpacing(10);
        buttonBoxes.setAlignment(Pos.CENTER);

        GridPane.setConstraints(buttonBoxes, 0, 9);
        GridPane.setColumnSpan(buttonBoxes, 2);

        // Add all elements to the grid
        grid.getChildren().addAll(
                nameLabel, nameInput,
                surnameLabel, surnameInput,
                contactLabel, contactInput,
                dobLabel, dobInput,
                sheetLabel, sheetInput,
                professionalLabel, professionBox,
                expLevel, expLevelField,
                buttonBoxes
        );

        grid.setAlignment(Pos.CENTER);
        VBox vbox = new VBox(10, headlineLabel, grid, messageLabel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        // Create a scene with the VBox
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
        Scene scene = new Scene(vbox, screenWidth, screenHeight);

        stage.setScene(scene);
        stage.show();
    }


    private void clearFields(TextField nameInput, TextField surnameInput, TextField contactInput,
                             DatePicker dobInput, TextField emailInput, ToggleGroup professionalGroup,
                             TextField expLevelField) {
        nameInput.clear();
        surnameInput.clear();
        contactInput.clear();
        dobInput.getEditor().clear();
        emailInput.clear();
        professionalGroup.selectToggle(null);
        expLevelField.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
