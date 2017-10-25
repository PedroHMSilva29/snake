package snakegame;

import java.io.IOException;
import java.net.MalformedURLException;
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
import javafx.scene.control.Label;
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
import java.net.URL;
import javax.swing.*;
import javax.sound.sampled.*;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;

/**
 *
 * @author Pedro Henrique
 */
public class SnakeGame extends Application {

    public enum Direction { //pre defined states!
        UP, DOWN, LEFT, RIGHT
    }
    final URL resource = getClass().getResource("beep-02.wav");
    final AudioClip clip = new AudioClip(resource.toString());
    
    final URL resource3 = getClass().getResource("20281__koops__apple-crunch-18.wav");
    final AudioClip clip3 = new AudioClip(resource3.toString());
    
    final URL resource2 = getClass().getResource("404743__owlstorm__retro-video-game-sfx-fail.wav");
    final AudioClip clip2 = new AudioClip(resource2.toString());
            
            
    public static final int BLOCK_SIZE = 25;//size of 1 block
    public static final int APP_W = 40 * BLOCK_SIZE; // application width
    public static final int APP_H = 20 * BLOCK_SIZE; // application height
    
    Stage window;
    Scene sMenu, sTutorial,sJogo;
    
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
        buildQuestion();
        Pane root = new Pane();
        root.setPrefSize(APP_W, APP_H); // setting pane's size
        root.setBackground(new Background(new BackgroundFill(Color.web("#eee"), CornerRadii.EMPTY, Insets.EMPTY)));

        //root.set
        Group snakeBody = new Group(); // we get children from the group and assign them to our snake list below
        snake = snakeBody.getChildren();// snake list

        Rectangle scoreBar = new Rectangle(APP_W, 25);
        scoreBar.setFill(Color.web("#D3D3D3"));
        Text scoreText = new Text("Pontuação: " + score);
        scoreText.setText("Pontuação: " + score);
        scoreText.setTextOrigin(VPos.TOP);
        scoreText.setLayoutX(APP_W - 100);

        Text question = new Text("Quanto é " + number1 + " + " + number2 + " ? ");
        question.setTextOrigin(VPos.TOP);
        question.setLayoutX(20);

        Rectangle food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        food.setFill(Color.web("#fa6b6b"));
        int tx = (int) (Math.random() * (APP_W/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
        int ty = 25 + (int) (Math.random() * (APP_H - 25/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
        food.setTranslateX(tx);
        food.setTranslateY(ty); // setting x, and y of food to random value);
        int foodNumber = answer;
        Text foodText = new Text(Integer.toString(foodNumber));// setting the number of the food
        foodText.setFill(Color.web("#fff"));
        foodText.setTextOrigin(VPos.TOP);
        foodText.setLayoutX(tx + 5);
        foodText.setLayoutY(ty);

        // Wrong Answer
        Rectangle poison = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        poison.setFill(Color.web("#fa6b6b"));
        tx = (int) (Math.random() * (APP_W/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
        ty =  25 + (int) (Math.random() * (APP_H - 25/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
        
        poison.setTranslateX(tx);
        poison.setTranslateY(ty); // setting x, and y of poison to random value);
        int poisonNumber;
        
        do{
        poisonNumber = (int) (Math.random() * 30);
        }while(poisonNumber ==foodNumber || poisonNumber==0);
        
        Text poisonText = new Text(Integer.toString(poisonNumber));// setting the number of the food
        poisonText.setFill(Color.web("#fff"));
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
                    clip2.play(1.0);
                    score = 0;
                    scoreText.setText("Pontuação: " + score);
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
                score = 0;
                clip2.play(1.0);
                scoreText.setText("Pontuação: " + score);
                try {
                    restartGame();
                } catch (Exception ex) {
                    Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
            if (tail.getTranslateX() == poison.getTranslateX()
                    && tail.getTranslateY() == poison.getTranslateY()) {
                clip2.play(1.0);
                score = 0;
                scoreText.setText("Pontuação: " + score);
                try {
                    restartGame();
                } catch (Exception ex) {
                    Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
            }

            if (tail.getTranslateX() == food.getTranslateX()
                    && tail.getTranslateY() == food.getTranslateY()) {
                
                // Carrega o arquivo de áudio (não funciona com .mp3, só .wav) 
                
                clip3.play(2.0);
       // URL oUrl;
               // try {
                   // oUrl = new URL("http://www.soundjay.com/button/beep-02.wav");
                  //  Clip oClip = AudioSystem.getClip();
                 //   AudioInputStream oStream = AudioSystem.getAudioInputStream(oUrl);
                  //  oClip.open(oStream);
                  //  oClip.loop(0); // Toca uma vez
               // } catch (MalformedURLException ex) {
               //     Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
              //  } catch (LineUnavailableException ex) {
               //     Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
              //  } catch (UnsupportedAudioFileException ex) {
               //     Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
              //  } catch (IOException ex) {
                //    Logger.getLogger(SnakeGame.class.getName()).log(Level.SEVERE, null, ex);
                //}
       

                int fx = (int) (Math.random() * (APP_W /*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
                int fy = 25 + (int) (Math.random() * (APP_H - 25/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;

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
                fy = 25 + (int) (Math.random() * (APP_H - 25/*-BLOCK_SIZE*/)) / BLOCK_SIZE * BLOCK_SIZE;
                poison.setTranslateX(fx);
                poison.setTranslateY(fy); // setting x, and y of poison to random value);
                int poisonNumber2;
                do{
                    poisonNumber2 = (int) (Math.random() * 30);
                }while(poisonNumber2 ==answer || poisonNumber2 ==0);
                
                poisonText.setText(Integer.toString(poisonNumber2));
                poisonText.setLayoutX(fx + 5);
                poisonText.setLayoutY(fy);

                score++;
                scoreText.setText("Pontuação: " + score);

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
        
        do{
            number1 = (int) (Math.random() * 30);
        }while(number1 == 0);
            
        do{
            number2 = (int) (Math.random() * 30);
        }while(number2 == 0);
        
        answer = number1 + number2;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
         window = primaryStage;
         sJogo = new Scene(createContent());
   
         sJogo.setOnKeyPressed(event ->{
            if(!moved)
                return;
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
                moved = false;
        });
         Button btnJogar = new Button();
         btnJogar.setText("Jogar");
         btnJogar.setId("btnJogar");
         btnJogar.setTranslateX(-100);
         btnJogar.setTranslateY(180);
         btnJogar.setOnAction(new EventHandler<ActionEvent>() {
             
            
              @Override
             public void handle(ActionEvent event) {
                window.setResizable(false);
                window.setScene(sJogo);
                window.show();
                startGame();
             }
         });
       
         Button btnTutorial = new Button("Tutorial");
         btnTutorial.setId("btnTutorial");
         btnTutorial.setTranslateX(100);
         btnTutorial.setTranslateY(180);
         btnTutorial.setOnAction(e -> window.setScene(sTutorial)); 
              
         StackPane root =new StackPane();
         root.setId("pane");
         root.getChildren().add(btnTutorial);
         root.getChildren().add(btnJogar);
 
         sMenu = new Scene(root, 900,500);
         sMenu.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
         
         Button btnBack = new Button("Voltar");
         btnBack.setId("btnBack");
         btnBack.setTranslateX(0);
         btnBack.setTranslateY(185);
         btnBack.setOnAction(e-> window.setScene(sMenu)); 
         
         StackPane tutorialPane = new StackPane();
         //Label lregra01 = new Label("label samples 1");
         //lregra01.setTranslateY(100);
         
         //Label lregra02 = new Label("label samples 2");
         //lregra01.setTranslateY(-100);
          
         tutorialPane.setId("paneTuto");
         //tutorialPane.getChildren().addAll(btnBack,lregra01,lregra02);
         tutorialPane.getChildren().addAll(btnBack);
         sTutorial = new Scene(tutorialPane,900,500);
         sTutorial.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
         
         window.setScene(sMenu);
         window.setTitle("Menu do jogo");	
         window.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
