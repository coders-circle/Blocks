package com.toggle.blocks;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.toggle.katana2d.*;
import com.toggle.katana2d.physics.PhysicsBody;
import com.toggle.katana2d.physics.PhysicsSystem;

/**
 * The main game scene.
 */
public class GameScene extends Scene implements BlockCreator {

    private Hook mHook;
    private GameState mGameState;

    @Override
    public void onInit() {

        // Add the systems:

        PhysicsSystem physicsSystem = new PhysicsSystem();
        mGameState = new GameState(mGame, physicsSystem.getWorld(), this);
        GLRenderer renderer = mGame.getRenderer();

        mSystems.add(new RenderSystem(mGame.getRenderer()));
        mSystems.add(physicsSystem);
        mSystems.add(new HookSystem(mGameState));
        mSystems.add(new CameraSystem(mGameState));
        mSystems.add(new ScoreSystem(mGameState));
        mSystems.add(new GameEnvironment(mGameState));
        mSystems.add(new BlockSystem(mGameState));
        mSystems.add(new BackgroundSystem(mGame.getRenderer()));

        // Set background color (to appear after sky disappears) to black.
        mGame.getRenderer().setBackgroundColor(0, 0, 0);

        // Add the entities:

        // Sky background.
        Entity sky = new Entity();
        Texture skyTex = mGame.getRenderer().addTexture(R.drawable.sky,
                640, 200, 1, 10);   // Image is 640 x 2000 and 2000 = 200*10.
        sky.add(new Background(skyTex, 100, 0, -(2000 - mGame.getRenderer().height)));
        addEntity(sky);

        // Mountains
        Entity mountain0 = new Entity();
        Texture mountainTex0 = mGame.getRenderer().addTexture(R.drawable.m0,
                500, 1000, 2, 1);
        mountain0.add(new Background(mountainTex0, 90, 0, -(mGame.getRenderer().height-800)));
        addEntity(mountain0);

        Entity mountain1 = new Entity();
        Texture mountainTex1 = mGame.getRenderer().addTexture(R.drawable.m1,
                250, 1000, 4, 1);
        mountain1.add(new Background(mountainTex1, 85, 0, -(mGame.getRenderer().height-800)));
        addEntity(mountain1);

        Entity mountain2 = new Entity();
        Texture mountainTex2 = mGame.getRenderer().addTexture(R.drawable.m2,
                500, 1000, 2, 1);
        mountain2.add(new Background(mountainTex2, 80, 0, -(mGame.getRenderer().height-800)));
        addEntity(mountain2);

        // Ground entity.
        Entity ground = new Entity();
        ground.add(new Transformation(renderer.width / 2, renderer.height-10, 0));
        ground.add(new Sprite(
                renderer.addTexture(R.drawable.ground, renderer.width, 100f)
        ));
        //ground.get(Sprite.class).texture.originY = 0.75f;
        ground.add(new PhysicsBody(physicsSystem.getWorld(),
                BodyDef.BodyType.StaticBody, ground,
                renderer.width, 100f,
                new PhysicsBody.Properties(0.0f)));
        addEntity(ground);

        // Hook entity.
        Entity hook = new Entity();
        HookSystem.initHookEntity(mGameState, hook);
        mHook = hook.get(Hook.class);
        addEntity(hook);

        // Create a block texture to be used for every block generated.
        mGame.textureManager.add("BlockTexture",
                renderer.addTexture(R.drawable.block, 100, 60));
        //mGame.textureManager.get("BlockTexture").originY = 0.6f;
    }

    /**
     * Create a new block at given position.
     * @param x X-coordinate of position to create new block at.
     * @param y Y-coordinate of position to create new block at.
     */
    public Entity createBlock(float x, float y) {
        Entity block = new Entity();
        BlockSystem.initBlockEntity(mGameState, block, x, y);
        addEntity(block);

        mHook.attachBlock(block);
        return block;
    }
}
