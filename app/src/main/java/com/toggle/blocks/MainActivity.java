package com.toggle.blocks;

import com.toggle.katana2d.Game;
import com.toggle.katana2d.GameActivity;

public class MainActivity extends GameActivity {

    @Override
    public void onGamePreStart() {
        Game game = getGame();
        game.getRenderer().setBackgroundColor(100.0f / 255, 149.0f / 255, 237.0f / 255);

        GameScene gameScene = new GameScene();
        game.setActiveScene(game.addScene(gameScene));
    }

    @Override
    public void onGameStart() {

    }
}
