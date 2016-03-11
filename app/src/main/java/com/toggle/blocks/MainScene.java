package com.toggle.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.toggle.katana2d.*;
import com.toggle.katana2d.physics.PhysicsBody;
import com.toggle.katana2d.physics.PhysicsSystem;

import java.util.ArrayList;
import java.util.List;

public class MainScene extends Scene implements HangAndFallSystem.BlockCreator {

    private Rope ropeData;
    private Texture blockTexture;
    private PhysicsSystem physicsSystem;

    @Override
    public void onInit() {
        GLRenderer renderer = mGame.getRenderer();
        physicsSystem = new PhysicsSystem();

        // Add the systems
        mSystems.add(new RenderSystem(mGame.getRenderer()));
        mSystems.add(physicsSystem);
        mSystems.add(new RopeSystem(physicsSystem.getWorld(), renderer));
        mSystems.add(new HangAndFallSystem(mGame, blockTexture, this));

        // Ceiling entity needed to hang the rope
        Entity ceiling = new Entity();
        ceiling.add(new Transformation(renderer.width/2, 0, 0));
        ceiling.add(new Sprite(
                renderer.addTexture(new float[]{0,0,0,1}, renderer.width, 8f)
        ));
        ceiling.add(new PhysicsBody(physicsSystem.getWorld(),
                BodyDef.BodyType.StaticBody, ceiling, new PhysicsBody.Properties(0.0f)));
        addEntity(ceiling);

        // Rope entity

        Entity rope = new Entity();

        List<Vector2> ropePath = new ArrayList<>();

        ropePath.add(new Vector2(renderer.width / 2, 0));
        ropePath.add(new Vector2(renderer.width / 2 - 128, 128f));

        ropeData = new Rope(ropePath, Rope.STANDARD_SEGMENT_THICKNESS,
                Rope.STANDARD_SEGMENT_LENGTH, ceiling);
        ropeData.segmentSprite = new Sprite(
                renderer.addTexture(new float[]{1,1,0,1}, Rope.STANDARD_SEGMENT_LENGTH,
                        Rope.STANDARD_SEGMENT_THICKNESS)
        );
        ropeData.segmentSprite.z = 2;

        rope.add(ropeData);
        rope.add(new Hanger());
        addEntity(rope);

        // Ground
        Entity ground = new Entity();
        ground.add(new Transformation(renderer.width / 2, renderer.height-64, 0));
        ground.add(new Sprite(
                renderer.addTexture(new float[]{0.4f, 0.4f, 0.4f, 1}, renderer.width, 128f)
        ));
        ground.add(new PhysicsBody(physicsSystem.getWorld(),
                BodyDef.BodyType.StaticBody, ground, new PhysicsBody.Properties(0.0f)));
        addEntity(ground);


        // Block entity
        blockTexture = renderer.addTexture(new float[]{0.2f, 0.2f, 0.2f, 1}, 90, 60);
        createBlock(renderer.width/2-128, 128f);

    }

    @Override
    public void createBlock(float x, float y) {
        Entity block = new Entity();
        block.add(new Transformation(x, y + 10, 0));
        block.add(new Sprite(blockTexture));
        block.add(new PhysicsBody(physicsSystem.getWorld(), BodyDef.BodyType.DynamicBody,
                block, new PhysicsBody.Properties(1.0f)));
        block.get(PhysicsBody.class).body.setAngularDamping(20);
        addEntity(block);

        ropeData.setEndBody(block);
    }
}
