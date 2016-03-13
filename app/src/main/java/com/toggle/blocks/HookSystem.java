package com.toggle.blocks;

import com.toggle.katana2d.*;

public class HookSystem extends com.toggle.katana2d.System {
    private GameState mGameState;

    public HookSystem(GameState gameState) {
        super(new Class[]{Hook.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {
        for (Entity entity: mEntities) {
            Hook hook = entity.get(Hook.class);


        }
    }
}
