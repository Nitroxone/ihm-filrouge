package graphics;

import core.Game;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    private static final int ROWS = 3;
    private static final int COLS = 4;
    private static final int GRID_HEIGHT = 3;
    private static final int GRID_WIDTH = 4;

    @Override
    public void start(Stage primaryStage) throws Exception{

        Game game = new Game();
        game.generateCards(6);
        game.buildDeck();
        game.shufflePairs();
        System.out.println(game.getCards());
        System.out.println(game.getPairs());

        GridPane root = new GridPane();
        root.setPadding(new Insets(10,10,10,10));
        root.setHgap(COLS*2);
        root.setVgap(ROWS*2);

        int colorLooper = 0;
        int colorLooperInsider = 0;

        for(int rowCount = 0; rowCount < ROWS; rowCount++) {
            for(int colCount = 0; colCount < COLS; colCount++) {
                if(colorLooper == 6) {
                    colorLooper = 0;
                }
                Rectangle rec = new Rectangle();
                rec.setWidth(140);
                rec.setHeight(200);
                //rec.setFill(Color.AQUA);
                rec.setFill(game.getPairs().get(colorLooper).getContent()[0].getColor());
                GridPane.setRowIndex(rec,rowCount);
                GridPane.setColumnIndex(rec,colCount);
                root.getChildren().addAll(rec);
                colorLooper++;
                if(colorLooperInsider == 1) {
                    colorLooperInsider = 0;
                } else {
                    colorLooperInsider++;
                }
            }
        }

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
