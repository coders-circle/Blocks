package com.toggle.blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.toggle.katana2d.Component;
import com.toggle.katana2d.Entity;
import com.toggle.katana2d.Sprite;
import com.toggle.katana2d.physics.PhysicsBody;

import java.util.ArrayList;
import java.util.List;

// A Rope contains series of segments, each joined using a RevoluteJoint.
// The first segment is joined to some other body (startBody)
// and the last segment can be optionally joined to endBody (can be set to null).
public class Rope implements Component {

    public static final float STANDARD_SEGMENT_LENGTH = 12f;
    public static final float STANDARD_SEGMENT_THICKNESS = 6f;
    Sprite segmentSprite;

    // Create a rope along given path using small rope segments of
    // given length and thickness
    public Rope(List<Vector2> path, float thickness, float segmentLength, Entity startBody) {
        this.thickness = thickness;
        this.segmentLength = segmentLength;
        this.startBody = startBody;
        this.path = path;
    }

    final Entity startBody;
    Entity endBody;
    final List<Vector2> path;    // path for the rope segment

    // Segments data
    int numSegments;
    final float thickness;
    final float segmentLength;

    // List of box2d bodies representing each segment
    final List<Body> segments = new ArrayList<>();

    Joint finalRjoint, finalDjoint;

    public void removeSegment(int i) {
        Body body = segments.get(i);
        body.getWorld().destroyBody(body);
        segments.remove(body);
    }

    public void setEndBody(Entity end) {
        if (endBody != null)
            removeEndBody();

        endBody = end;

        Body ebd = end.get(PhysicsBody.class).body;

        RevoluteJointDef rJointDef = new RevoluteJointDef();
        DistanceJointDef dJointDef = new DistanceJointDef();
        rJointDef.collideConnected = false;
        dJointDef.collideConnected = false;

        Body link = segments.get(segments.size()-1);

        if (ebd != null && link != null) {
            rJointDef.initialize(link, ebd, link.getWorldCenter());
            finalRjoint = link.getWorld().createJoint(rJointDef);

            dJointDef.initialize(link, ebd, link.getWorldCenter(), ebd.getWorldCenter());
            finalDjoint = link.getWorld().createJoint(dJointDef);
        }
    }

    public void removeEndBody() {
        if (endBody == null)
            return;

        Body ebd = endBody.get(PhysicsBody.class).body;
        ebd.getWorld().destroyJoint(finalDjoint);
        ebd.getWorld().destroyJoint(finalRjoint);
        endBody = null;
    }
}
