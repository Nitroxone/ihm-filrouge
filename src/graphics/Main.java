package graphics;

import core.Card;
import core.Game;
import core.Pair;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        int pairsAmount = 8; // EVEN NUMBERS ONLY !
        int rows,cols,gridHeight,gridWidth;
        rows = pairsAmount / 2;
        cols = 4;
        gridHeight = rows;
        gridWidth = cols;

        Game game = new Game();
        game.generateCards(pairsAmount);
        game.buildDeck();
        game.shufflePairs();
        System.out.println(game.getCards());
        System.out.println(game.getPairs());

        BorderPane root = new BorderPane();
        GridPane branch = new GridPane();
        branch.setPadding(new Insets(10,10,10,10));
        branch.setHgap(cols*2);
        branch.setVgap(rows*2);

        int colorLooper = 0;

        for(int rowCount = 0; rowCount < rows; rowCount++) {
            for(int colCount = 0; colCount < cols; colCount++) {
                if(colorLooper == pairsAmount) {
                    colorLooper = 0;
                    game.shufflePairs();
                }
                Rectangle rec = new Rectangle();
                rec.setWidth(120);
                rec.setHeight(180);
                rec.setFill(game.getPairs().get(colorLooper).getContent()[0].getColor());
                GridPane.setRowIndex(rec,rowCount);
                GridPane.setColumnIndex(rec,colCount);
                // ASSIGNATION DES COORDONNÉES À CHAQUE CARTE
                if(game.getPairs().get(colorLooper).getContent()[0].printPos().equals("EMPTY")) {
                    System.out.println("Aucune position sauvegardée pour la carte 1. Assignation.");
                    game.getPairs().get(colorLooper).getContent()[0].setPos(rowCount, colCount);
                    rec.addEventFilter(MouseEvent.MOUSE_PRESSED, new CardFilter(rec,game.getPairs().get(colorLooper).getContent()[0]));
                    System.out.println(game.getPairs().get(colorLooper).getContent()[0].printPos());
                } else {
                    System.out.println("Position sauvegardée pour la carte 1. Assignation pour la carte 2.");
                    game.getPairs().get(colorLooper).getContent()[1].setPos(rowCount, colCount);
                    rec.addEventFilter(MouseEvent.MOUSE_PRESSED, new CardFilter(rec,game.getPairs().get(colorLooper).getContent()[1]));
                    System.out.println(game.getPairs().get(colorLooper).getContent()[1].printPos());
                }
                branch.getChildren().addAll(rec);
                colorLooper++;
            }
        }

        for(Pair pair : game.getPairs()) {
            for(Card card : pair.getContent()) {
                System.out.println(card.printPos());
            }
        }

        root.setCenter(branch);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Memory");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    static class CardFilter implements EventHandler<MouseEvent> {
        Rectangle rec;
        Card card;
        static Card[] matchCards = new Card[2];
        static Rectangle[] matchRecs = new Rectangle[2];

        CardFilter(Rectangle rec, Card card) {
            this.rec = rec;
            this.card = card;
            Arrays.fill(matchCards,null);
        }

        @Override
        public void handle(MouseEvent event) {
            rec.setStyle("-fx-fill:black;");

            System.out.println("Card color : " + card.getColor());
            System.out.println("Card pos : " + card.printPos());

            if(!card.isFound()) {
                if (matchCards[0] == null) {
                    matchCards[0] = card;
                    matchRecs[0] = rec;
                } else if (matchCards[1] == null) {
                    matchCards[1] = card;
                    matchRecs[1] = rec;
                    if (isMatching()) {
                        findPair();
                    }
                    clearMatchCards();
                }
            }

            System.out.println(Arrays.toString(matchCards));
        }

        private void findPair() {
            matchRecs[0].setStyle("-fx-fill:white;");
            matchRecs[1].setStyle("-fx-fill:white;");
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
