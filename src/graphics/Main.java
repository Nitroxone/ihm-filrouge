package graphics;

import core.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    private static Game game = null;
    private static int pairsAmount = 4;
    private int rows = 2;
    private final int COLS = 4;
    private static Label scoreCounter = new Label("0");
    private static Label gameStatus = new Label("No game");

    @Override
    public void start(Stage primaryStage) throws Exception {

        // DECLARING : NON-STATIC LABELS AND BUTTONS
        Label gameTitle = new Label("Memory Project");
        Button buttonNewGame = new Button("New game");
        Button buttonStopGame = new Button("Leave game");
        Label statusTitle = new Label("Game status");
        Label scoreTitle = new Label("Score");

        // STYLE-SETTING FOR STATIC LABELS
        scoreCounter.setStyle("-fx-font-size:40;");
        gameStatus.setStyle("-fx-font-size:20;");

        // DECLARING : PANES
        BorderPane root = new BorderPane();
        GridPane branch = new GridPane();
        BorderPane topBox = new BorderPane();
        VBox topLeftBox = new VBox();
        VBox topRightBox = new VBox();
        VBox topCenterBox = new VBox();

        buttonNewGame.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                boolean generate = false;
                Alert newGameSetup = new Alert(Alert.AlertType.CONFIRMATION);
                newGameSetup.setTitle("Select pairs amount");
                newGameSetup.setHeaderText("Select the desired amount of pairs.");
                newGameSetup.setContentText("Select one of those options.");

                ButtonType pickFourPairs = new ButtonType("4 pairs");
                ButtonType pickSixPairs = new ButtonType("6 pairs");
                ButtonType pickEightPairs = new ButtonType("8 pairs");
                ButtonType cancelNewGameSetup = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                newGameSetup.getButtonTypes().setAll(pickFourPairs,pickSixPairs,pickEightPairs,cancelNewGameSetup);

                Optional<ButtonType> userChoice = newGameSetup.showAndWait();

                // pairsAmount RECEIVES A VALUE FROM USER'S PICK
                if(userChoice.get() == pickFourPairs) {
                    pairsAmount = 4;
                    generate = true;
                } else if(userChoice.get() == pickSixPairs) {
                    pairsAmount = 6;
                    generate = true;
                } else if(userChoice.get() == pickEightPairs) {
                    pairsAmount = 8;
                    generate = true;
                }

                // GENERATING A NEW GAME
                if(generate) {
                    // CREATING A NEW EMPTY CARD IN ORDER TO RESET THE COLORS (CHECK JAVADOC FOR MORE INFORMATION)
                    Card resetter = new Card(true);

                    // RESETTING THE COLORS, THEN THE SCORE
                    resetter.resetColors();
                    resetScore();

                    // CREATING A NEW GAME
                    game = new Game();
                    game.generateCards(pairsAmount);
                    game.buildDeck();
                    game.shufflePairs();

                    // CALCULATING THE GridPane ROWCOUNT
                    rows = pairsAmount / 2;

                    // REMOVING THE PREVIOUS CARDS FROM THE GridPane
                    branch.getChildren().clear();

                    // GENERATING A NEW GRID ACCORDING TO THE SETTINGS ABOVE
                    fillNewGrid(pairsAmount, rows, COLS, game, branch);

                    // CHANGING THE GAME STATUS
                    gameStatus.setText("Ongoing");
                }
            }
        });
        buttonStopGame.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // IF THERE IS AN ONGOING GAME, THEN LEAVE IT, AND RESET THE SCORE AS WELL AS THE GAME STATUS
                // IF THERE IS NO ONGOING GAME, THEN EXIT THE PROGRAM
                if(game != null) {
                    game = null;
                    branch.getChildren().clear();
                    gameStatus.setText("No game");
                    resetScore();
                } else {
                    primaryStage.close();
                    System.exit(0);
                }
            }
        });

        // SETTING THE PANES
        root.setMinSize(400,150);
        branch.setPadding(new Insets(10));
        branch.setHgap(COLS *2);
        branch.setVgap(rows*2);
        branch.setAlignment(Pos.CENTER);

        topLeftBox.setAlignment(Pos.TOP_LEFT);
        topRightBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setRight(topRightBox);
        topBox.setLeft(topLeftBox);
        topBox.setCenter(topCenterBox);

        topCenterBox.setSpacing(10);
        topCenterBox.setAlignment(Pos.CENTER);
        topCenterBox.setPadding(new Insets(20));
        topCenterBox.getChildren().addAll(gameTitle,buttonNewGame, buttonStopGame);

        topLeftBox.setSpacing(10);
        topLeftBox.setAlignment(Pos.CENTER_LEFT);
        topLeftBox.setPadding(new Insets(20));
        topLeftBox.getChildren().addAll(scoreTitle,scoreCounter);

        topRightBox.setSpacing(10);
        topRightBox.setAlignment(Pos.CENTER_RIGHT);
        topRightBox.setPadding(new Insets(20));
        topRightBox.getChildren().addAll(statusTitle,gameStatus);

        root.setCenter(branch);
        root.setTop(topBox);
        Scene scene = new Scene(root,Double.MAX_VALUE,Double.MAX_VALUE);

        stageConfig(primaryStage, scene);
    }

    /**
     * Resets the current score, and the score display Label.
     */
    private void resetScore() {
        scoreCounter.setText("0");
        if(game != null) {
            game.resetScore();
        }
    }

    /**
     * Configures the stage with the declared parameters.
     * @param primaryStage the stage to be configured
     * @param scene the associated scene
     */
    private void stageConfig(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("Memory");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * Generates a new grid of cards, by iterating via <i>for</i> loops over <b>rows</b> and <b>cols</b>.
     *
     * @param pairsAmount the amount of pairs generated
     * @param rows the grid rows
     * @param cols the grid columns
     * @param game the game
     * @param branch the pane the grid is drawn on
     */
    private void fillNewGrid(int pairsAmount, int rows, int cols, Game game, GridPane branch) {
        // THE colorLooper VARIABLE IS USED TO SAFELY ITERATE OVER EACH PAIR, ENSURING A RANDOM AND UNPREDICTABLE LAYOUT OF THE CARDS ON THE GRID.
        int colorLooper = 0;
        for(int rowCount = 0; rowCount < rows; rowCount++) {
            for(int colCount = 0; colCount < cols; colCount++) {
                if(colorLooper == pairsAmount) {
                    // ONCE THE FIRST SET OF CARDS (THE FIRST CARD FOR EACH PAIR) HAS BEEN GENERATED, RESET THE colorLooper AND SHUFFLE THE CARDS
                    colorLooper = 0;
                    game.shufflePairs();
                }
                // CREATING A NEW RECTANGLE AND SETTING ITS STYLE, THEN SETTING ITS POSITION ON THE GRID
                Rectangle rec = new Rectangle();
                setRectangleStyle(rec);
                GridPane.setRowIndex(rec,rowCount);
                GridPane.setColumnIndex(rec,colCount);

                // ASSIGNING COORDINATES TO EACH CARD :
                if("EMPTY".equals(game.getPairs().get(colorLooper).getContent()[0].printPos())) {
                    //NO POSITION FOUND FOR CARD 1 OF THE PAIR. ASSIGNING NEW POSITION TO CARD 1
                    game.getPairs().get(colorLooper).getContent()[0].setPos(rowCount, colCount);
                    // ADDING A NEW LISTENER TO THE CARD
                    rec.addEventFilter(MouseEvent.MOUSE_PRESSED, new CardFilter(rec, game.getPairs().get(colorLooper).getContent()[0], game));
                } else {
                    //POSITION FOUND FOR CARD 1. ASSIGNING NEW POSITION TO CARD 2
                    game.getPairs().get(colorLooper).getContent()[1].setPos(rowCount, colCount);
                    // ADDING A NEW LISTENER TO THE CARD
                    rec.addEventFilter(MouseEvent.MOUSE_PRESSED, new CardFilter(rec, game.getPairs().get(colorLooper).getContent()[1], game));
                }
                // ADDING THE RECTANGLE TO THE BRANCH
                branch.getChildren().addAll(rec);
                colorLooper++;
            }
        }
    }

    /**
     * Sets a rectangle to specific parameters.
     * @param rec the rectangle to be set
     */
    private void setRectangleStyle(Rectangle rec) {
        rec.setWidth(100);
        rec.setHeight(160);
        rec.setFill(Color.BLACK);
        rec.setStyle("-fx-background-radius: 10 10 10 10");
    }

    /**
     * Checks if the game is over by looking to any non-found card on the grid.
     * @param game the game to be checked
     * @return <b>true</b> if the game is over, <b>false</b> otherwise
     */
    public static boolean isGameOver(Game game) {
        for (Pair pair : game.getPairs()) {
            for (Card card : pair.getContent()) {
                if(!card.isFound()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Updates the game score with the specified amount.
     * @param amount the amount to be appended to the score
     */
    public static void updateScore(int amount) {
        System.out.println("SCORE UPDATED WITH " + amount);
        game.appendScore(amount);
        scoreCounter.setText(String.valueOf(game.getScore()));
        if(game.getScore() == pairsAmount * 4) {
            scoreCounter.setText(scoreCounter.getText() + ", PERFECT GUESS !");
        }
    }

    public static class CardFilter implements EventHandler<MouseEvent> {
        Rectangle rec;
        Card card;
        Game game;
        Timer timer = new Timer();
        static Card[] matchCards = new Card[2];
        static Rectangle[] matchRecs = new Rectangle[2];

        CardFilter(Rectangle rec, Card card, Game game) {
            this.rec = rec;
            this.card = card;
            this.game = game;
            Arrays.fill(matchCards,null);
        }

        @Override
        public void handle(MouseEvent event) {
            if(!card.isFound()) {
                rec.setFill(card.getColor());
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            if (matchCards[0] == null) {
                                matchCards[0] = card;
                                matchRecs[0] = rec;
                            } else if (matchCards[1] == null && !(matchCards[0].equals(card))) {
                                matchCards[1] = card;
                                matchRecs[1] = rec;
                                if (isMatching()) {
                                    findPair();
                                } else {
                                    fillBlack();
                                }
                                clearMatchCards();
                                if(isGameOver(game)) {
                                    gameOver();
                                }
                            }
                        });
                    }
                }, 500);
            }
        }

        private void gameOver() {
            gameStatus.setText("Finished!");
            Alert gameOverPopup = new Alert(Alert.AlertType.INFORMATION);
            gameOverPopup.setTitle("Game Over !");
            gameOverPopup.setHeaderText("Game Over !");
            gameOverPopup.setContentText("Your score : " + game.getScore());
            gameOverPopup.showAndWait();
        }

        private void findPair() {
            matchCards[0].find();
            matchCards[1].find();
            updateScore(4);
        }

        public boolean isMatching() {
            return matchCards[0].getColor().equals(matchCards[1].getColor());
        }

        public void clearMatchCards() {
            matchCards[0] = null;
            matchCards[1] = null;
        }

        private void fillBlack() {
            matchRecs[0].setFill(Color.BLACK);
            matchRecs[1].setFill(Color.BLACK);
            updateScore(-2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
