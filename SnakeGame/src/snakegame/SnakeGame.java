package snakegame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author Pedro Henrique
 */
public class SnakeGame extends Application {

    public enum Direction { //pre defined states!
        UP, DOWN, LEFT, RIGHT
    }

    public static final int BLOCK_SIZE = 25;//size of 1 block
    public static final int APP_W = 20 * BLOCK_SIZE; // application width
    public static final int APP_H = 15 * BLOCK_SIZE; // application height

    public static int score = 0;
    public static int number1 = 1;
    public static int number2 = 1;
    public static int answer = 2;

    private Direction direction = Direction.RIGHT; // default direction
    private boolean moved = false; // moving (don't allows moving in different directions at the same time
    private boolean running = false; // is our application running
    Stage thestage;
    private Timeline timeline = new Timeline(); // our animation

    private ObservableList<Node> snake; // we will display and iterate over it nad our snake body, we will iterate over it.

    private Parent createContent() throws Exception {

        Pane root = new Pane();
        root.setPrefSize(APP_W, APP_H); // setting pane's size
        root.setBackground(new Background(new BackgroundFill(Color.web("#eee"), CornerRadii.EMPTY, Insets.EMPTY)));

        //root.set
        Group snakeBody = new Group(); // we get children from the group and assign them to our snake list below
        snake = snakeBody.getChildren();// snake list

        Rectangle scoreBar = new Rectangle(APP_W, 25);
        scoreBar.setFill(Color.web("#D3D3D3"));
        Text scoreText = new Text("Pontuação: " + score);
        scoreText.setTextOrigin(VPos.TOP);
        scoreText.setLayoutX(APP_W - 100);

        Text question = new Text("Quanto é " + number1 + " + " + number2 + " ? ");
        question.setTextOrigin(VPos.TOP);
        question.setLayoutX(20);

        Rectangle food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        food.setFill(Color.BLUE);
        int tx = (int) (Math.random() * (APP_W/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
        int ty = (int) (Math.random() * (APP_H/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
        food.setTranslateX(tx);
        food.setTranslateY(ty); // setting x, and y of food to random value);
        int foodNumber = answer;
        Text foodText = new Text(Integer.toString(foodNumber));// setting the number of the food
        foodText.setTextOrigin(VPos.TOP);
        foodText.setLayoutX(tx + 5);
        foodText.setLayoutY(ty);

        // Wrong Answer
        Rectangle poison = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        poison.setFill(Color.BLUE);
        tx = (int) (Math.random() * (APP_W/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
        ty = (int) (Math.random() * (APP_H/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
        poison.setTranslateX(tx);
        poison.setTranslateY(ty); // setting x, and y of poison to random value);
        int poisonNumber = (int) (Math.random() * 30);
        Text poisonText = new Text(Integer.toString(poisonNumber));// setting the number of the food
        poisonText.setTextOrigin(VPos.TOP);
        poisonText.setLayoutX(tx + 5);
        poisonText.setLayoutY(ty);

        KeyFrame frame = new KeyFrame(/* KeyFrame is like a single frame in animation!!!*/Duration.seconds(0.20 /*To increase difficulty lower the value*/), event -> {
            if (!running) {
                return; //if not running just simple return
            }
            System.out.println("size:" + snake.size());
            boolean toRemove = snake.size() > 1; // at least two blocks in the snake body;
            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0); //

            double tailX = tail.getTranslateX();
            double tailY = tail.getTranslateY();

            switch (direction) {
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - BLOCK_SIZE);
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() + BLOCK_SIZE);
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX() - BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX() + BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
            }

            moved = true; // we can now physically move, we have changed duration

            if (toRemove) {
                snake.add(0, tail); // we put tail in front -- the zeroth element
            }
            //collision detection collision with itself!
            for (Node rect : snake) {
                if (rect != tail && tail.getTranslateX() == rect.getTranslateX()
                        && tail.getTranslateY() == rect.getTranslateY()) {
                    try {
                        // tail name is little confusing, cause it must be a head now!!!
                        restartGame();
                    } catch (Exception ex) {
                        Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }

            if (tail.getTranslateX() < 0 || tail.getTranslateX() > APP_W
                    || tail.getTranslateY() < 0 || tail.getTranslateY() > APP_H) {
                try {
                    restartGame();
                } catch (Exception ex) {
                    Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (tail.getTranslateX() == food.getTranslateX()
                    && tail.getTranslateY() == food.getTranslateY()) {
                int fx = (int) (Math.random() * (APP_W /*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
                int fy = (int) (Math.random() * (APP_H /*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;

                Rectangle rect = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
                rect.setTranslateX(tailX);
                rect.setTranslateX(tailY);

                snake.add(rect);
                buildQuestion();
                question.setText("Quanto é " + number1 + " + " + number2 + " ? ");
                foodText.setText(Integer.toString(answer));
                foodText.setLayoutX(fx + 5);
                foodText.setLayoutY(fy);

                food.setTranslateX(fx);
                food.setTranslateY(fy); // setting x, and y of food to random value);

                fx = (int) (Math.random() * (APP_W/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
                fy = (int) (Math.random() * (APP_H/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
                poison.setTranslateX(fx);
                poison.setTranslateY(fy); // setting x, and y of poison to random value);
                poisonText.setText(Integer.toString((int)(Math.random() * 30)));
                poisonText.setLayoutX(fx + 5);
                poisonText.setLayoutY(fy);

                score++;
                scoreText.setText(Integer.toString(score));

                // snake.add(rect); //adding rectangle to snake
            }
        });

        timeline.getKeyFrames().addAll(frame); // add frame to the timeline KeyFrames
        timeline.setCycleCount(Timeline.INDEFINITE); // it will always run same frame(there is any one frame to run

        root.getChildren().addAll(scoreBar, question, scoreText, food, snakeBody, foodText, poison, poisonText);

        return root;
    }

    private void restartGame() throws Exception {

        stopGame();
        startGame();
    }

    private void stopGame() {
        running = false;
        timeline.stop();
        snake.clear(); // clear the elements within snake list

    }

    private void startGame() {
        direction = Direction.RIGHT;
        Rectangle head = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        snake.add(head);
        timeline.play();
        running = true;
    }

    public void buildQuestion() {
        number1 = (int) (Math.random() * 30);
        number2 = (int) (Math.random() * 30);
        answer = number1 + number2;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        thestage = primaryStage;
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(event -> {
            if (!moved) {
                return;
            }
            switch (event.getCode()) {
                case W:
                    if (direction != Direction.DOWN) {
                        direction = Direction.UP;
                    }
                    break;
                case S:
                    if (direction != Direction.UP) {
                        direction = Direction.DOWN;
                    }
                    break;
                case A:
                    if (direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    }
                    break;
                case D:
                    if (direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                    break;
            }
            moved = false;
        });
        Button btn = new Button();
        btn.setText("Jogar");
        btn.setTranslateX(-100);
        btn.setTranslateY(180);
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                primaryStage.setTitle("Snake game");
                primaryStage.setResizable(false);
                primaryStage.setScene(scene);
                primaryStage.show();
                startGame();
            }
        });

        Button btnTutorial = new Button();
        btnTutorial.setText("Tutorial");
        btnTutorial.setTranslateX(100);
        btnTutorial.setTranslateY(180);
        btnTutorial.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //tutorial   
            }
        });

        StackPane root = new StackPane();
        root.setId("pane");
        root.getChildren().add(btn);
        root.getChildren().add(btnTutorial);

        Scene scene2 = new Scene(root, 900, 500);
        scene2.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        primaryStage.setTitle("Menu Snack Game");
        primaryStage.setScene(scene2);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
