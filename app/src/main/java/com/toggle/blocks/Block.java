package com.toggle.blocks;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.toggle.katana2d.Component;
import com.toggle.katana2d.Entity;
import com.toggle.katana2d.physics.ContactListener;
import com.toggle.katana2d.physics.PhysicsBody;

/**
 * Block component stores states and properties of a block.
 */
public class Block implements Component {

    public Fixture bottomSensor;

    public Joint bottomJoint;

    // Collision state:

    // The body in collision.
    public Body collidingBody;
    // The collision count with that body.
    // This value is useful in endContact-listener to find when to reset collision state.
    public int collideCount = 0;

    /**
     * Initialize the block with contact listener that handles joint creation.
     * @param body PhysicsBody of the entity containing this Block component, whose contactListener
     *             will be created by this method.
     */
    public void initialize(PhysicsBody body) {

        body.contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact, Fixture me, Fixture other) {

                // If there is no bottom joint yet but the sensor
                // is touching a body, set the colliding state.

                if (me.isSensor()) {

                    Entity entity = (Entity)me.getUserData();
                    Entity otherEntity = (Entity)other.getUserData();

//                    if (!otherEntity.has(Block.class))
//                        return;

                    Block block = entity.get(Block.class);
                    Body otherBody = otherEntity.get(PhysicsBody.class).body;

                    if (block.bottomSensor == me) {

                        if (block.bottomJoint == null) {

                            if (block.collidingBody != otherBody)
                                block.collideCount = 0;
                            block.collidingBody = otherBody;
                            block.collideCount++;
                        }

                    }
                }
            }

            @Override
            public void endContact(Contact contact, Fixture me, Fixture other) {

                // Similar to beginContact, used to decrement the collision count with a body.

                if (me.isSensor()) {

                    Entity entity = (Entity)me.getUserData();
                    Entity otherEntity = (Entity)other.getUserData();

//                    if (!otherEntity.has(Block.class))
//                        return;

                    Block block = entity.get(Block.class);
                    Body otherBody = otherEntity.get(PhysicsBody.class).body;

                    if (block.collidingBody == otherBody) {
                        block.collideCount--;
                        if (block.collideCount == 0)
                            collidingBody = null;
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Fixture me, Fixture other) {
            }

            @Override
            public void postSolve(Contact contact, Fixture me, Fixture other) {
            }
        };
    }
}
