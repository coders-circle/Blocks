package com.toggle.blocks;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.toggle.katana2d.*;
import com.toggle.katana2d.physics.PhysicsBody;
import com.toggle.katana2d.physics.PhysicsSystem;


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
        Entity topBlock = null;
        float top = 999999;

        for (Entity entity: mEntities) {

             Block block = entity.get(Block.class);
             Body body = entity.get(PhysicsBody.class).body;
            Transformation transformation = entity.get(Transformation.class);

            if (mGameState.fallingBlock == entity) {

                // If a block has fallen for some height,
                // set falling block to null
                // so that next block can then be generated.

                GLRenderer renderer = mGameState.game.getRenderer();
                if (transformation.y > renderer.getCamera().y + renderer.height/2) {
                    mGameState.fallingBlock = null;
                }
            }

            if (mGameState.hangingBlock != entity) {
                // Find out the topmost block.
                if (transformation.y < top) {
                    topBlock = entity;
                    top = transformation.y;
                }
            }

            // Check if its bottom sensor is colliding with some other body
            // and if so, check if the bodies are almost linear and create joint if so.

            if (block.collidingBody != null) {
                if (Math.abs(
                        block.collidingBody.getAngle() - body.getAngle()) < Math.toRadians(0.8f)) {

                    if (block.bottomJoint == null) {

                            WeldJointDef jointDef = new WeldJointDef();
                            jointDef.initialize(body, block.collidingBody,
                                    body.getWorldCenter());

//                            DistanceJointDef jointDef = new DistanceJointDef();
//                            jointDef.initialize(body, block.collidingBody,
//                                    body.getWorldCenter(), block.collidingBody.getWorldCenter());

                            jointDef.collideConnected = true;

                            block.bottomJoint = body.getWorld().createJoint(jointDef);
                    }

                }
            }

            // Destroy the joint when there's too much tension.

            if (block.bottomJoint != null &&
                    block.bottomJoint.getReactionTorque(1/dt) > 500) {
                body.getWorld().destroyJoint(block.bottomJoint);
                block.bottomJoint = null;
            }
        }

        mGameState.topBlock = topBlock;
    }

    /**
     * Initialize a block entity with components.
     * @param gameState Global game data.
     * @param entity The block entity to be filled with components.
     * @param x X-Coordinate of position of the block.
     * @param y Y-Coordinate of position of hte block.
     */
    public static void initBlockEntity(GameState gameState, Entity entity, float x, float y) {
        entity.add(new Transformation(x, y + 30, 0));
        entity.add(new Sprite(gameState.game.textureManager.get("BlockTexture")));
        entity.add(new PhysicsBody(gameState.world, BodyDef.BodyType.DynamicBody,
                entity, 100, 60,
                new PhysicsBody.Properties(0.2f, 0.8f, 0.0f, false, false)));
        entity.get(PhysicsBody.class).body.setAngularDamping(50f);
        entity.add(new Block());

        // Add some sensors at the top and bottom of the block.

        PhysicsBody body = entity.get(PhysicsBody.class);
        Block block = entity.get(Block.class);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0, 0, new Vector2(0, 30*PhysicsSystem.METERS_PER_PIXEL), 0);
        block.bottomSensor = body.createSensor(shape);

        block.initialize(body);

    }
}
