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
        Entity background = new Entity();
        Texture bgTex = mGame.getRenderer().addTexture(R.drawable.sky,
                640, 200, 1, 10);   // Image is 640 x 2000 and 2000 = 200*10.
        background.add(new Background(bgTex, 80, 0, -(2000-mGame.getRenderer().height)));
        addEntity(background);

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
