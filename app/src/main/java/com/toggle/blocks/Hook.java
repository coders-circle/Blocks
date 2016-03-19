package com.toggle.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.toggle.katana2d.Component;
import com.toggle.katana2d.Entity;
import com.toggle.katana2d.physics.PhysicsBody;
import com.toggle.katana2d.physics.PhysicsSystem;

/**
 * Hook component stores the currently attached block entity and the joints
 * that connect the block with the hook.
 */
public class Hook implements Component {
    // The block entity.
    private Entity mBlock = null;

    // The joints that connect the block.
    private Joint mDistanceJoint = null;
    private Joint mRevoluteJoint = null;

    // The hook entity.
    private Entity mEntity;

    // The point where the block is joint to the hook.
    private Vector2 mHookPoint;

    /**
     * Construct the hook.
     * @param entity The entity that contains this Hook component.
     * @param hookPoint The point where blocks are attached in local coordinates of the hook.
     */
    public Hook(Entity entity, Vector2 hookPoint) {
        mEntity = entity;
        mHookPoint = new Vector2(hookPoint).scl(PhysicsSystem.METERS_PER_PIXEL);
    }

    public Entity getAttachedBlock() { return mBlock; }
    public float getHookLength() { return mHookPoint.len(); }
    public Vector2 getHookPoint() { return mHookPoint; }

    /**
     * Attach a block to the hook.
     * @param block Block entity to attach.
     */
    public void attachBlock(Entity block) {
        removeBlock();
        mBlock = block;

        if (mBlock != null) {
            Body hookBody = mEntity.get(PhysicsBody.class).body;
            Body blockBody = block.get(PhysicsBody.class).body;

            DistanceJointDef dJointDef = new DistanceJointDef();
            dJointDef.initialize(hookBody, blockBody,
                    hookBody.getWorldPoint(mHookPoint),
                    blockBody.getWorldPoint(new Vector2(0, -45*PhysicsSystem.METERS_PER_PIXEL)));
            dJointDef.collideConnected = false;

            RevoluteJointDef rJointDef = new RevoluteJointDef();
            rJointDef.initialize(hookBody, blockBody, hookBody.getWorldCenter());
            rJointDef.collideConnected = false;

            mDistanceJoint =  hookBody.getWorld().createJoint(dJointDef);
            mRevoluteJoint = hookBody.getWorld().createJoint(rJointDef);
        }
    }


    /**
     * Remove an attached block from the hook.
     */
    public void removeBlock() {
        if (mDistanceJoint != null) {
            Body hookBody = mEntity.get(PhysicsBody.class).body;
            hookBody.getWorld().destroyJoint(mDistanceJoint);
            hookBody.getWorld().destroyJoint(mRevoluteJoint);
            mDistanceJoint = null;
            mRevoluteJoint = null;
            mBlock = null;
        }
    }
}
