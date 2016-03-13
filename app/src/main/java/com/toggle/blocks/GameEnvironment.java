package com.toggle.blocks;

import com.toggle.katana2d.*;

public class GameEnvironment extends com.toggle.katana2d.System {
    private GameState mGameState;

    public GameEnvironment(GameState gameState) {
        super(new Class[]{Dummy.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {

    }
}
