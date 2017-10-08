
package snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Pedro Henrique
 */
public class SnakeGame extends Application {
    
 
    public enum Direction{ //pre defined states!
        UP, DOWN, LEFT, RIGHT
    }

    

    public static final int BLOCK_SIZE = 20;//size of 1 block
    public static final int APP_W = 20*BLOCK_SIZE; // application width
    public static final int APP_H = 15*BLOCK_SIZE; // application height
  
   
    private Direction direction = Direction.RIGHT; // default direction
    private boolean moved = false; // moving (don't allows moving in different directions at the same time
    private boolean running = false; // is our application running

//    private Path p = Paths.get("." + File.separator +"LeaderBoard.txt");
//    private File leaderBoard = new File("sample/LeaderBoard.txt");

    private Timeline timeline = new Timeline(); // our animation

    private ObservableList<Node> snake; // we will display and iterate over it nad our snake body, we will iterate over it.

    private Parent createContent() throws Exception {
       
        Pane root = new  Pane();
        root.setPrefSize(APP_W, APP_H); // setting pane's size
        double getRootTX= root.getTranslateX();
        double getRootTY = root.getTranslateY();

        Group snakeBody = new Group(); // we get children from the group and assign them to our snake list below
        snake = snakeBody.getChildren();// snake list

        
        Rectangle food = new Rectangle(BLOCK_SIZE,BLOCK_SIZE);
        food.setFill(Color.BLUE);
        food.setTranslateX((int)(Math.random() * (APP_W - BLOCK_SIZE)/*in order to stay within screen*/)/ BLOCK_SIZE * BLOCK_SIZE);
        food.setTranslateY((int)(Math.random() * (APP_H - BLOCK_SIZE))/ BLOCK_SIZE * BLOCK_SIZE); // setting x, and y of food to random value);
        
        KeyFrame frame = new KeyFrame(/* KeyFrame is like a single frame in animation!!!*/ Duration.seconds(0.2 /*To increase difficulty lower the value*/), event -> {
            if(!running)
                return; //if not running just simple return

            boolean toRemove = snake.size() > 1; // at least two blocks in the snake body;
            Node tail = toRemove ? snake.remove(snake.size()-1) : snake.get(0); //

            double tailX = tail.getTranslateX();
            double tailY = tail.getTranslateY();

            switch (direction) {
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - BLOCK_SIZE);
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY()+BLOCK_SIZE);
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX()-BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX()+BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
            }

            moved = true; // we can now physically move, we have changed duration

            if(toRemove)
                snake.add(0, tail); // we put tail in front -- the zeroth element

            //collision detection collision with itself!
            for(Node rect : snake) {
                if(rect != tail && tail.getTranslateX() == rect.getTranslateX() && tail.getTranslateY() == rect.getTranslateY()) { // tail name is little confusing, cause it must be a head now!!!
           
                    restartGame();
                    break;
                }
            }

            if (tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
                food.setTranslateX((int)(Math.random() * (APP_W - BLOCK_SIZE)/*in order to stay within screen*/)/ BLOCK_SIZE * BLOCK_SIZE);
                food.setTranslateY((int)(Math.random() * (APP_H - BLOCK_SIZE))/ BLOCK_SIZE * BLOCK_SIZE); // setting x, and y of food to random value);
                Rectangle rect= new Rectangle(BLOCK_SIZE,BLOCK_SIZE);
                rect.setTranslateX(tailX);
                rect.setTranslateX(tailY);
                
                snake.add(rect); //adding rectangle to snake
            }
        });

        timeline.getKeyFrames().addAll(frame); // add frame to the timeline KeyFrames
        timeline.setCycleCount(Timeline.INDEFINITE); // it will always run same frame(there is any one frame to run

        root.getChildren().addAll(food, snakeBody);

        return root;
    }

 

    private void restartGame() {
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

 
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        Scene scene = new Scene(createContent());
       
        scene.setOnKeyPressed(event ->{
            if(moved){
                switch (event.getCode()) {
                case W:
                    if (direction != Direction.DOWN)
                        direction = Direction.UP;
                    break;
                case S:
                    if (direction != Direction.UP)
                        direction = Direction.DOWN;
                    break;
                case A:
                    if(direction != Direction.RIGHT)
                        direction = Direction.LEFT;
                    break;
                case D:
                    if(direction != Direction.LEFT)
                        direction = Direction.RIGHT;
                    break;
            }
            }
            moved = false;
        });
          
         
          
        
         primaryStage.setTitle("Snake game");
         primaryStage.setScene(scene);
         primaryStage.show();
         startGame();        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
