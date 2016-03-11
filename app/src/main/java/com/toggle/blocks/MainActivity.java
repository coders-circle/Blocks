package com.toggle.blocks;

import com.toggle.katana2d.Game;
import com.toggle.katana2d.GameActivity;

public class MainActivity extends GameActivity {

    @Override
    public void onGamePreStart() {
        Game game = getGame();
        game.getRenderer().setBackgroundColor(100.0f / 255, 149.0f / 255, 237.0f / 255);

        MainScene mainScene = new MainScene();
        game.setActiveScene(game.addScene(mainScene));
    }

    @Override
    public void onGameStart() {

    }
}
