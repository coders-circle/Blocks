package com.toggle.blocks;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.toggle.katana2d.*;
import com.toggle.katana2d.physics.PhysicsBody;
import com.toggle.katana2d.physics.PhysicsSystem;

/**
 * HookSystem is responsible for the behaviour of the hook.
 * Major job includes:
 *  - Moving with pendulum motion.
 *  - Throwing a block when touch feedback is provided.
 *  - Generating next block when there is no actively falling block.
 *  - Scrolling the hook.
 */
public class HookSystem extends com.toggle.katana2d.System {
    private GameState mGameState;
    public final static float HOOK_LENGTH = 250;

    public HookSystem(GameState gameState) {
        super(new Class[]{Hook.class, PhysicsBody.class, Transformation.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {
        for (Entity entity: mEntities) {
            Hook hook = entity.get(Hook.class);
            Body body = entity.get(PhysicsBody.class).body;
            Transformation transformation = entity.get(Transformation.class);

            mGameState.hangingBlock = hook.getAttachedBlock();

            // Simple pendulum motion.
            // Angular acceleration = - g/R sin(theta)

            float g = body.getWorld().getGravity().y;
            float R = hook.getHookLength();
            float theta = body.getAngle();
            float acceleration = - g / R * (float)Math.sin(theta);

            if (acceleration == 0)
                body.setAngularVelocity(1f);                    // initial velocity
            else
                body.setAngularVelocity(body.getAngularVelocity()+acceleration*dt);


            // Generate next block when there is no block in the hook
            // and no active block
            // and the hook is almost straight (less than 4 degrees).

            if (hook.getAttachedBlock() == null &&
                    mGameState.fallingBlock == null &&
                    Math.abs(body.getAngle()) < Math.toRadians(4)) {
                Vector2 point = new Vector2(body.getWorldPoint(hook.getHookPoint())).scl(
                        PhysicsSystem.PIXELS_PER_METER);
                mGameState.hangingBlock = mGameState.blockCreator.createBlock(point.x, point.y);
                mGameState.blocks++;
            }

            // Touch to remove the block joint.

            TouchInputData inputData = mGameState.game.getTouchInputData();
            if (inputData.pointers.size() > 0 && hook.getAttachedBlock() != null) {
                mGameState.hangingBlock = null;
                mGameState.fallingBlock = hook.getAttachedBlock();
                hook.removeBlock();
            }

            // Scroll the hook to maintain distance to the last block.

            boolean scrolling = false;
            if (mGameState.topBlock != null) {
                float topBlockY = mGameState.topBlock.get(Transformation.class).y;
                float myY = transformation.y;
                float scrHeight = mGameState.game.getRenderer().height;

                // If too less distance, set upward velocity.
                if (topBlockY - myY < 3*scrHeight/4) {
                    body.setLinearVelocity(0, -32*PhysicsSystem.METERS_PER_PIXEL);
                    mGameState.cameraSpeedY = -32;
                    scrolling = true;
                }
                // If too much distance, set downward velocity.
                else if (myY < 0 && topBlockY - myY > 3*scrHeight/4) {
                    body.setLinearVelocity(0, 32*PhysicsSystem.METERS_PER_PIXEL);
                    mGameState.cameraSpeedY = 32;
                    scrolling = true;
                }
            }

            if (!scrolling) {
                body.setLinearVelocity(0, 0);
                mGameState.cameraSpeedY = 0;
            }
        }
    }

    /**
     * Initialize a hook entity with components.
     * @param gameState Global game data.
     * @param entity The hook entity to be filled with components.
     */
    public static void initHookEntity(GameState gameState, Entity entity) {
        GLRenderer renderer = gameState.game.getRenderer();

        // Hook component.
        entity.add(new Hook(entity, new Vector2(0, HOOK_LENGTH)));

        // Sprite and Transformation components.
        Texture hookTexture = renderer.addTexture(
                new float[]{0.7f, 0.7f, 0.0f, 1}, 5f, HOOK_LENGTH);
        hookTexture.originY = 0;
        entity.add(new Sprite(hookTexture));
        entity.get(Sprite.class).z = -0.2f;
        entity.add(new Transformation(renderer.width / 2, 0, 0));

        // Physics body component.
        PolygonShape hookShape = new PolygonShape();
        hookShape.setAsBox(
                10f / 2 * PhysicsSystem.METERS_PER_PIXEL,
                HOOK_LENGTH / 2 * PhysicsSystem.METERS_PER_PIXEL,
                new Vector2(0, -HOOK_LENGTH / 2 * PhysicsSystem.METERS_PER_PIXEL), 0
        );
        entity.add(new PhysicsBody(gameState.world, BodyDef.BodyType.KinematicBody,
                entity, hookShape, new PhysicsBody.Properties(1.0f)));
    }
}