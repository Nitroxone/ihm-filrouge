package graphics;

import core.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    private Game game = null;
    private int pairsAmount = 4;
    private int rows = 2;
    private int cols = 4;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Label gameTitle = new Label("Memory Project");
        Button buttonNewGame = new Button("New game");
        Button buttonStopGame = new Button("Leave game");

        BorderPane root = new BorderPane();
        GridPane branch = new GridPane();
        VBox topBox = new VBox();

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

                if(generate) {
                    Card resetter = new Card(true);
                    resetter.resetColors();
                    game = new Game();
                    game.generateCards(pairsAmount);
                    game.buildDeck();
                    game.shufflePairs();
                    /*System.out.println(game.getCards());
                    System.out.println(game.getPairs());*/

                    rows = pairsAmount / 2;

                    branch.getChildren().clear();
                    fillNewGrid(pairsAmount, rows, cols, game, branch);
                }
            }
        });

        root.setMinSize(400,150);
        branch.setPadding(new Insets(10));
        topBox.setPadding(new Insets(20));
        branch.setHgap(cols*2);
        branch.setVgap(rows*2);
        topBox.setSpacing(10);
        topBox.setAlignment(Pos.CENTER);
        branch.setAlignment(Pos.CENTER);
        topBox.getChildren().addAll(gameTitle,buttonNewGame, buttonStopGame);

        //fillNewGrid(pairsAmount, rows, cols, game, branch);
        //printAllCardsPos(game);

        root.setCenter(branch);
        root.setTop(topBox);
        Scene scene = new Scene(root,Double.MAX_VALUE,Double.MAX_VALUE);

        primaryStage.setTitle("Memory");
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void fillNewGrid(int pairsAmount, int rows, int cols, Game game, GridPane branch) {
        int colorLooper = 0;
        for(int rowCount = 0; rowCount < rows; rowCount++) {
            for(int colCount = 0; colCount < cols; colCount++) {
                if(colorLooper == pairsAmount) {
                    colorLooper = 0;
                    game.shufflePairs();
                }
                Rectangle rec = new Rectangle();
                rec.setWidth(100);
                rec.setHeight(160);
                rec.setFill(Color.BLACK);
                GridPane.setRowIndex(rec,rowCount);
                GridPane.setColumnIndex(rec,colCount);
                // ASSIGNATION DES COORDONNÉES À CHAQUE CARTE
                if(game.getPairs().get(colorLooper).getContent()[0].printPos().equals("EMPTY")) {
                    //Aucune position sauvegardée pour la carte 1. Assignation.
                    game.getPairs().get(colorLooper).getContent()[0].setPos(rowCount, colCount);
                    rec.addEventFilter(MouseEvent.MOUSE_PRESSED, new CardFilter(rec, game.getPairs().get(colorLooper).getContent()[0], game));
                } else {
                    //Position sauvegardée pour la carte 1. Assignation pour la carte 2.
                    game.getPairs().get(colorLooper).getContent()[1].setPos(rowCount, colCount);
                    rec.addEventFilter(MouseEvent.MOUSE_PRESSED, new CardFilter(rec, game.getPairs().get(colorLooper).getContent()[1], game));
                }
                branch.getChildren().addAll(rec);
                colorLooper++;
            }
        }
    }

    private void printAllCardsPos(Game game) {
        for(Pair pair : game.getPairs()) {
            for(Card card : pair.getContent()) {
                System.out.println(card.printPos());
            }
        }
    }

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
            System.out.println("Card color : " + card.getColor());
            System.out.println("Card pos : " + card.printPos());

            if(!card.isFound()) {
                rec.setFill(card.getColor());
                System.out.println("REVEALED");
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
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
                                System.out.println("Game is finished !");
                            }
                        }
                    }
                }, 500);
            }

            System.out.println(Arrays.toString(matchCards));
        }

        private void findPair() {
            matchCards[0].find();
            matchCards[1].find();
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
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
