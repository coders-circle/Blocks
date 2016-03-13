package com.toggle.blocks;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.toggle.katana2d.*;
import com.toggle.katana2d.physics.PhysicsBody;


/**
 * BlockSystem is responsible for handling the blocks.
 * Major job is to handle state of
 * the actively falling block and the stack of blocks on the ground.
 */
public class BlockSystem extends com.toggle.katana2d.System {
    private GameState mGameState;

    public BlockSystem(GameState gameState) {
        super(new Class[]{Block.class, PhysicsBody.class, Transformation.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {
        for (Entity entity: mEntities) {

            Block block = entity.get(Block.class);
            Body body = entity.get(PhysicsBody.class).body;
            Transformation transformation = entity.get(Transformation.class);

            if (mGameState.activeBlock == entity) {
                // If an active block has fallen for some height (say half of screen),
                // set active block to null
                // so that next block can then be generated.

                if (transformation.y > mGameState.game.getRenderer().height/2) {
                    mGameState.activeBlock = null;
                }
            }

        }
    }

    /**
     * Initialize a block entity with components.
     * @param gameState Global game data.
     * @param entity The block entity to be filled with components.
     * @param x X-Coordinate of position of the block.
     * @param y Y-Coordinate of position of hte block.
     */
    public static void initBlockEntity(GameState gameState, Entity entity, float x, float y) {
        entity.add(new Transformation(x, y + 10, 0));
        entity.add(new Sprite(gameState.game.textureManager.get("BlockTexture")));
        entity.add(new PhysicsBody(gameState.world, BodyDef.BodyType.DynamicBody,
                entity, new PhysicsBody.Properties(0.2f, 0.8f, 0.0f, false, false)));
        entity.get(PhysicsBody.class).body.setAngularDamping(50f);
        entity.add(new Block());
    }
}
