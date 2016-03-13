package com.toggle.blocks;

import com.toggle.katana2d.*;

public class ScoreSystem extends com.toggle.katana2d.System {
    private GameState mGameState;

    public ScoreSystem(GameState gameState) {
        super(new Class[]{Dummy.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {

    }
}
