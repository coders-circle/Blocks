package com.toggle.blocks;

import com.toggle.katana2d.*;

public class BlockSystem extends com.toggle.katana2d.System {
    private GameState mGameState;

    public BlockSystem(GameState gameState) {
        super(new Class[]{Block.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {
        for (Entity entity: mEntities) {
            Block block = entity.get(Block.class);


        }
    }
}
