package com.toggle.blocks;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.toggle.katana2d.*;
import com.toggle.katana2d.physics.PhysicsBody;
import com.toggle.katana2d.physics.PhysicsSystem;

public class HangAndFallSystem extends com.toggle.katana2d.System {

    private Game mGame;
    private BlockCreator mBlockCreator;
    private Texture mBlockTexture;

    public interface BlockCreator {
        void createBlock(float x, float y);
    }

    public HangAndFallSystem(Game game, Texture blockTexture, BlockCreator blockCreator) {
        super(new Class[]{Rope.class, Hanger.class});
        mBlockCreator = blockCreator;
        mGame = game;
        mBlockTexture = blockTexture;
    }

    @Override
    public void update(float dt) {
        TouchInputData inputData = mGame.getTouchInputData();
        for (Entity entity: mEntities) {
            Rope rope = entity.get(Rope.class);
            Hanger hanger = entity.get(Hanger.class);

            if (hanger.generateNext) {
                GLRenderer renderer = mGame.getRenderer();
                Body finalBody = rope.segments.get(rope.segments.size() - 1);
                Vector2 position = new Vector2(finalBody.getPosition())
                        .scl(PhysicsSystem.PIXELS_PER_METER);

                Vector2 displacement = new Vector2(position).sub(renderer.width / 2 - 128, 128);

                if (displacement.len2() < 32*32) {
                    hanger.generateNext = false;
                    mBlockCreator.createBlock(position.x, position.y);
                }
            }
            else if (inputData.pointers.size()>0) {
                rope.removeEndBody();
                hanger.generateNext = true;
            }

        }
    }
}
