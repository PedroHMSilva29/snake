/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author 41528840
 */
public class SnakeGame extends Application {
    
    
    
    @Override
    public void start(Stage primaryStage) {
        Button btnJogar = new Button();
        btnJogar.setTranslateX(-100);
        btnJogar.setText("JOGAR");
        btnJogar.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                //Jogar
            }
        });
        
         Button btnTutorial = new Button();
         btnTutorial.setTranslateX(200);
         btnTutorial.setText("TUTORIAL");
        btnTutorial.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                //Tutorial
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btnJogar);
        root.getChildren().add(btnTutorial);
        
        Scene scene = new Scene(root, 800, 500);
        
        primaryStage.setTitle("Snake game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
