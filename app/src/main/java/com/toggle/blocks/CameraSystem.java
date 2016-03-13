package com.toggle.blocks;

import com.toggle.katana2d.*;

public class CameraSystem extends com.toggle.katana2d.System {
    private GameState mGameState;

    public CameraSystem(GameState gameState) {
        super(new Class[]{Camera.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {
        for (Entity entity: mEntities) {
            Camera camera = entity.get(Camera.class);


        }
    }
}
