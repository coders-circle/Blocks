package com.toggle.blocks;

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
 */
public class HookSystem extends com.toggle.katana2d.System {
    private GameState mGameState;
    public final static float HOOK_LENGTH = 200;

    public HookSystem(GameState gameState) {
        super(new Class[]{Hook.class, PhysicsBody.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {
        for (Entity entity: mEntities) {
            Hook hook = entity.get(Hook.class);
            Body body = entity.get(PhysicsBody.class).body;

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
            // and no active block (which just started to fall)
            // and the hook is almost straight (less than 4 degrees).

            if (hook.getAttachedBlock() == null &&
                    mGameState.activeBlock == null &&
                    Math.abs(body.getAngle()) < Math.toRadians(4)) {
                Vector2 point = new Vector2(body.getWorldPoint(hook.getHookPoint())).scl(
                        PhysicsSystem.PIXELS_PER_METER);
                mGameState.blockCreator.createBlock(point.x, point.y);
            }

            // Touch to remove the block joint.

            TouchInputData inputData = mGameState.game.getTouchInputData();
            if (inputData.pointers.size() > 0 && hook.getAttachedBlock() != null) {
                mGameState.activeBlock = hook.getAttachedBlock();
                hook.removeBlock();
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
                new float[]{0.8f, 0.8f, 0.8f, 1}, 2.0f, HOOK_LENGTH);
        hookTexture.originY = 0;
        entity.add(new Sprite(hookTexture));
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