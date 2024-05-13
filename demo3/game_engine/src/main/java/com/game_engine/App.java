package com.game_engine;

import java.util.ArrayList;
import java.util.Collections;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    private static final int TARGET_FRAME_RATE = 60; // Adjust this to your desired frame rate
    private long lastFrameTime = 0;
    private long nanoPerFrame = 1000000000 / TARGET_FRAME_RATE;
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("3D_WORLD");
        World wm=new World();

        Scene scene = new Scene(wm.group, Color.BLACK);

        // Handle keyboard input

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);


        // ...

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Calculate time elapsed since the last frame

                // Update game logic
                wm.update();
                wm.updateFrameRate(now);

                // Update last frame time
                lastFrameTime = now;
            }
        };

        

        stage.show();

        // Start the game loop
        gameLoop.start();
    }


}
