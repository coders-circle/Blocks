package com.toggle.blocks;

import com.toggle.katana2d.Game;
import com.toggle.katana2d.GameActivity;

/**
 * Activity that holds the game.
 */
public class MainActivity extends GameActivity {

    @Override
    public void onGamePreStart() {
    }

    @Override
    public void onGameStart() {
        Game game = getGame();
        game.getRenderer().setBackgroundColor(100.0f / 255, 149.0f / 255, 237.0f / 255);

        GameScene gameScene = new GameScene();
        game.setActiveScene(game.addScene(gameScene));
    }
}
