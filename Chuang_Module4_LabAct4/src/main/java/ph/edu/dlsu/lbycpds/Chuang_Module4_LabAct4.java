package ph.edu.dlsu.lbycpds;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.InputStream;

public class Chuang_Module4_LabAct4 extends Application {
    private MyQueue quizQueue = new MyQueue(10);
    private MultipleChoice activeQuestion;

    private TextField questionField = new TextField();
    private TextField choiceAField = new TextField();
    private TextField choiceBField = new TextField();
    private TextField choiceCField = new TextField();
    private TextField choiceDField = new TextField();
    private TextField answerKeyField = new TextField();
    private Label notificationLabel = new Label();

    private Label questionViewLabel = new Label();
    private Button optionButtonA = new Button();
    private Button optionButtonB = new Button();
    private Button optionButtonC = new Button();
    private Button optionButtonD = new Button();
    private Label feedbackStatusLabel = new Label();

    private ImageView statusImageView = new ImageView();
    private Image successGraphic;
    private Image failureGraphic;

    private Stage primaryWindow;
    private VBox inputPane;
    private VBox reviewPane;

    @Override
    public void start(Stage stage) {
        this.primaryWindow = stage;
        stage.setTitle("Interactive Quiz Portal");

        loadAssets();
        buildInputLayout();
        buildReviewLayout();
        applyCustomPinkTheme();

        stage.setScene(new Scene(inputPane, 720, 520));
        stage.show();
    }

    private void loadAssets() {
        try {
            InputStream successStream = getClass().getResourceAsStream("images/GoodJob.png");
            if (successStream != null) {
                successGraphic = new Image(successStream);
            }
            InputStream failStream = getClass().getResourceAsStream("images/TryAgain.png");
            if (failStream != null) {
                failureGraphic = new Image(failStream);
            }
        } catch (Exception e) {
            System.out.println("Notice - graphics failed to load: " + e.getMessage());
        }
    }

    private void applyCustomPinkTheme() {
        String customBackground = "-fx-background-color: #2D1A27;";
        String fieldStyle = "-fx-background-color: #4A283B; -fx-text-fill: #ffffff; -fx-border-color: #8C5370;";
        String buttonStyle = "-fx-background-color: #4A283B; -fx-text-fill: #ffffff; -fx-border-color: #8C5370; -fx-background-radius: 20; -fx-border-radius: 20; -fx-cursor: hand;";
        String textWhite = "-fx-text-fill: #ffffff;";

        inputPane.setStyle(customBackground);
        reviewPane.setStyle(customBackground);

        styleAllLabels(inputPane, textWhite);
        styleAllLabels(reviewPane, textWhite);

        if (!inputPane.getChildren().isEmpty() && inputPane.getChildren().get(0) instanceof Label) {
            ((Label) inputPane.getChildren().get(0)).setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #ff758f;");
        }

        TextField[] fields = {questionField, choiceAField, choiceBField, choiceCField, choiceDField, answerKeyField};
        for (TextField tf : fields) {
            tf.setStyle(fieldStyle);
        }

        questionViewLabel.setStyle("-fx-border-color: #8C5370; -fx-background-radius: 15; -fx-border-radius: 15; -fx-padding: 12px; -fx-font-size: 14px; -fx-background-color: #4A283B; -fx-text-fill: #ffffff;");

        optionButtonA.setStyle(buttonStyle);
        optionButtonB.setStyle(buttonStyle);
        optionButtonC.setStyle(buttonStyle);
        optionButtonD.setStyle(buttonStyle);

        for (javafx.scene.Node node : inputPane.getChildren()) {
            if (node instanceof HBox) {
                for (javafx.scene.Node subNode : ((HBox) node).getChildren()) {
                    if (subNode instanceof Button) {
                        subNode.setStyle(buttonStyle);
                    }
                }
            }
        }
    }

    private void styleAllLabels(javafx.scene.Parent parent, String style) {
        for (javafx.scene.Node n : parent.getChildrenUnmodifiable()) {
            if (n instanceof Label) {
                ((Label) n).setStyle(style);
            } else if (n instanceof javafx.scene.Parent) {
                styleAllLabels((javafx.scene.Parent) n, style);
            }
        }
    }

    private void buildInputLayout() {
        inputPane = new VBox(12);
        inputPane.setPadding(new Insets(20));

        Label headerTitle = new Label("Quiz Bank Setup (Minimum 5 items)");
        headerTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #ff758f;");

        questionField.setPrefHeight(55);
        VBox promptGroup = new VBox(3, new Label("Question Statement:"), questionField);

        HBox rowA = new HBox(8, new Label("Option A:"), choiceAField);
        HBox rowB = new HBox(8, new Label("Option B:"), choiceBField);
        HBox rowC = new HBox(8, new Label("Option C:"), choiceCField);
        HBox rowD = new HBox(8, new Label("Option D:"), choiceDField);
        HBox rowKey = new HBox(8, new Label("Correct Letter:"), answerKeyField);

        HBox.setHgrow(choiceAField, Priority.ALWAYS);
        HBox.setHgrow(choiceBField, Priority.ALWAYS);
        HBox.setHgrow(choiceCField, Priority.ALWAYS);
        HBox.setHgrow(choiceDField, Priority.ALWAYS);
        HBox.setHgrow(answerKeyField, Priority.ALWAYS);

        HBox actionButtons = new HBox(12);
        Button insertBtn = new Button("Add Question");
        Button launchBtn = new Button("Launch Quiz");
        actionButtons.getChildren().addAll(insertBtn, launchBtn);

        insertBtn.setOnAction(e -> handleAddRecord());

        launchBtn.setOnAction(e -> {
            if (quizQueue.size() < 5) {
                notificationLabel.setText("WARNING: YOU NEED AT LEAST 5 QUESTIONS TO START THE QUIZ!");
                notificationLabel.setStyle("-fx-text-fill: #ff5252; -fx-font-weight: bold;");
            } else {
                primaryWindow.setScene(new Scene(reviewPane, 720, 600));
                fetchNextQuizItem();
            }
        });

        inputPane.getChildren().addAll(headerTitle, promptGroup, rowA, rowB, rowC, rowD, rowKey, actionButtons, notificationLabel);
    }

    private void buildReviewLayout() {
        reviewPane = new VBox(18);
        reviewPane.setPadding(new Insets(25));
        reviewPane.setAlignment(Pos.CENTER);

        Label testHeader = new Label("Choose the correct choice below:");
        testHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");

        questionViewLabel.setWrapText(true);
        questionViewLabel.setMinHeight(90);
        questionViewLabel.setMaxWidth(540);

        GridPane optionsGrid = new GridPane();
        optionsGrid.setHgap(12);
        optionsGrid.setVgap(12);
        optionsGrid.setAlignment(Pos.CENTER);

        optionButtonA.setPrefWidth(240);
        optionButtonB.setPrefWidth(240);
        optionButtonC.setPrefWidth(240);
        optionButtonD.setPrefWidth(240);

        optionsGrid.add(optionButtonA, 0, 0);
        optionsGrid.add(optionButtonC, 1, 0);
        optionsGrid.add(optionButtonB, 0, 1);
        optionsGrid.add(optionButtonD, 1, 1);

        optionButtonA.setOnAction(e -> processAnswer("A"));
        optionButtonB.setOnAction(e -> processAnswer("B"));
        optionButtonC.setOnAction(e -> processAnswer("C"));
        optionButtonD.setOnAction(e -> processAnswer("D"));

        statusImageView.setFitWidth(90);
        statusImageView.setFitHeight(90);
        statusImageView.setPreserveRatio(true);
        statusImageView.setVisible(false);

        reviewPane.getChildren().addAll(testHeader, questionViewLabel, optionsGrid, feedbackStatusLabel, statusImageView);
    }

    private void handleAddRecord() {
        try {
            if (quizQueue.isFull()) {
                notificationLabel.setText("Queue storage limit reached!");
                return;
            }
            MultipleChoice record = new MultipleChoice(
                    questionField.getText(),
                    choiceAField.getText(),
                    choiceBField.getText(),
                    choiceCField.getText(),
                    choiceDField.getText(),
                    answerKeyField.getText()
            );
            quizQueue.offer(record);
            notificationLabel.setText("Successfully saved! Total queued: " + quizQueue.size());
            notificationLabel.setStyle("-fx-text-fill: #ffffff;");

            questionField.clear();
            choiceAField.clear();
            choiceBField.clear();
            choiceCField.clear();
            choiceDField.clear();
            answerKeyField.clear();
        } catch (Exception err) {
            notificationLabel.setText("Input Error: " + err.getMessage());
        }
    }

    private void fetchNextQuizItem() {
        if (quizQueue.isEmpty()) {
            questionViewLabel.setText("Great job! All quiz items have been answered correctly!");
            optionButtonA.setVisible(false);
            optionButtonB.setVisible(false);
            optionButtonC.setVisible(false);
            optionButtonD.setVisible(false);
            feedbackStatusLabel.setText("Session Finished.");
            statusImageView.setVisible(false);
            return;
        }

        activeQuestion = (MultipleChoice) quizQueue.front();
        questionViewLabel.setText(activeQuestion.getPrompt());
        optionButtonA.setText("A. " + activeQuestion.getOpt1());
        optionButtonB.setText("B. " + activeQuestion.getOpt2());
        optionButtonC.setText("C. " + activeQuestion.getOpt3());
        optionButtonD.setText("D. " + activeQuestion.getOpt4());
        feedbackStatusLabel.setText("");
        statusImageView.setVisible(false);
    }

    private void processAnswer(String userChoice) {
        if (activeQuestion.getKey().equalsIgnoreCase(userChoice)) {
            quizQueue.poll();
                feedbackStatusLabel.setText("Correct! This item has been cleared from the queue.");
            feedbackStatusLabel.setStyle("-fx-text-fill: #76ff03; -fx-font-weight: bold;");
            if (successGraphic != null) {
                statusImageView.setImage(successGraphic);
                statusImageView.setVisible(true);
            }
        } else {
            MultipleChoice cyclingItem = (MultipleChoice) quizQueue.poll();
            quizQueue.offer(cyclingItem);
            feedbackStatusLabel.setText("Incorrect! This item has been sent back to the queue.");
            feedbackStatusLabel.setStyle("-fx-text-fill: #ff5252; -fx-font-weight: bold;");
            if (failureGraphic != null) {
                statusImageView.setImage(failureGraphic);
                statusImageView.setVisible(true);
            }
        }

        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.2));
        delay.setOnFinished(ev -> fetchNextQuizItem());
        delay.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}