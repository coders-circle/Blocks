package com.toggle.blocks;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.toggle.katana2d.*;
import com.toggle.katana2d.physics.PhysicsBody;
import com.toggle.katana2d.physics.PhysicsSystem;

public class GameScene extends Scene {

    private Texture blockTexture;
    private PhysicsSystem physicsSystem;

    private GameState mGameState = new GameState();


    @Override
    public void onInit() {
        GLRenderer renderer = mGame.getRenderer();
        physicsSystem = new PhysicsSystem();

        // Add the systems
        mSystems.add(new RenderSystem(mGame.getRenderer()));
        mSystems.add(physicsSystem);
        mSystems.add(new HookSystem(mGameState));
        mSystems.add(new CameraSystem(mGameState));
        mSystems.add(new ScoreSystem(mGameState));
        mSystems.add(new GameEnvironment(mGameState));
        mSystems.add(new BlockSystem(mGameState));


        // Block entity
        blockTexture = renderer.addTexture(new float[]{0.2f, 0.2f, 0.2f, 1}, 90, 60);
        createBlock(renderer.width/2-128, 128f);

    }

    public void createBlock(float x, float y) {
        Entity block = new Entity();
        block.add(new Transformation(x, y + 10, 0));
        block.add(new Sprite(blockTexture));
        block.add(new PhysicsBody(physicsSystem.getWorld(), BodyDef.BodyType.DynamicBody,
                block, new PhysicsBody.Properties(0.2f, 0.8f, 0.0f, false, false)));
        block.get(PhysicsBody.class).body.setAngularDamping(50f);
        addEntity(block);
    }
}
