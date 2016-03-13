package com.toggle.blocks;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.toggle.katana2d.*;
import com.toggle.katana2d.physics.PhysicsBody;
import com.toggle.katana2d.physics.PhysicsSystem;

/**
 * The main game scene.
 */
public class GameScene extends Scene implements BlockCreator {

    private PhysicsSystem mPhysicsSystem;
    private Hook mHook;
    private GameState mGameState;

    @Override
    public void onInit() {

        // Add the systems:

        mPhysicsSystem = new PhysicsSystem();
        mGameState = new GameState(mGame, mPhysicsSystem.getWorld(), this);
        GLRenderer renderer = mGame.getRenderer();

        mSystems.add(new RenderSystem(mGame.getRenderer()));
        mSystems.add(mPhysicsSystem);
        mSystems.add(new HookSystem(mGameState));
        mSystems.add(new CameraSystem(mGameState));
        mSystems.add(new ScoreSystem(mGameState));
        mSystems.add(new GameEnvironment(mGameState));
        mSystems.add(new BlockSystem(mGameState));


        // Add the entities:

        // Ceiling entity.
        Entity ceiling = new Entity();
        ceiling.add(new Transformation(renderer.width/2, 0, 0));
        ceiling.add(new Sprite(
                renderer.addTexture(new float[]{0,0,0,1}, renderer.width, 8f)
        ));
        ceiling.add(new PhysicsBody(mPhysicsSystem.getWorld(),
                BodyDef.BodyType.StaticBody, ceiling, new PhysicsBody.Properties(0.0f)));
        addEntity(ceiling);

        // Ground entity.
        Entity ground = new Entity();
        ground.add(new Transformation(renderer.width / 2, renderer.height-64, 0));
        ground.add(new Sprite(
                renderer.addTexture(R.drawable.ground, renderer.width, 128f)
        ));
        ground.add(new PhysicsBody(mPhysicsSystem.getWorld(),
        BodyDef.BodyType.StaticBody, ground, new PhysicsBody.Properties(0.0f)));
        addEntity(ground);

        // Hook entity.
        Entity hook = new Entity();
        HookSystem.initHookEntity(mGameState, hook);
        mHook = hook.get(Hook.class);
        addEntity(hook);


        // Create a block texture to be used for every block generated.
        mGame.textureManager.add("BlockTexture",
                renderer.addTexture(R.drawable.block, 80, 100));
        //addTexture(R.drawable.my_image, width, height)
    }

    /**
     * Create a new block at given position.
     * @param x X-coordinate of position to create new block at.
     * @param y Y-coordinate of position to create new block at.
     */
    public void createBlock(float x, float y) {
        Entity block = new Entity();
        BlockSystem.initBlockEntity(mGameState, block, x, y);
        addEntity(block);

        mHook.attachBlock(block);
    }
}
