package graphics;

import core.Card;
import core.Game;
import core.Pair;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        int pairsAmount = 4; // EVEN NUMBERS ONLY !
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
        int colorLooperInsider = 0;

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
                // ASSIGNATION DES COORDONNÉES À CHAQUE CARTES
                if(game.getPairs().get(colorLooper).getContent()[0].printPos().equals("EMPTY")) {
                    System.out.println("Aucune position sauvegardée pour la carte 1. Assignation.");
                    game.getPairs().get(colorLooper).getContent()[0].setPos(rowCount, colCount);
                    System.out.println(game.getPairs().get(colorLooper).getContent()[0].printPos());
                } else {
                    System.out.println("Position sauvegardée pour la carte 1. Assignation pour la carte 2.");
                    game.getPairs().get(colorLooper).getContent()[1].setPos(rowCount, colCount);
                    System.out.println(game.getPairs().get(colorLooper).getContent()[1].printPos());
                }
                branch.getChildren().addAll(rec);
                colorLooper++;
                if(colorLooperInsider == 1) {
                    colorLooperInsider = 0;
                } else {
                    colorLooperInsider++;
                }
            }
        }

        /*for(Pair pair : game.getPairs()) {
            for(Card card : pair.getContent()) {
                System.out.println(card.printPos());
            }
        }*/

        System.out.println("Affichage de la position des deux cartes d'une même paire :");
        System.out.println(game.getPairs().get(3).getContent()[0].printPos());
        System.out.println(game.getPairs().get(3).getContent()[1].printPos());

        root.setCenter(branch);
        Scene scene = new Scene(root);

        primaryStage.setTitle("Memory");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
