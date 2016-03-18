package com.toggle.blocks;

import com.toggle.katana2d.*;

/**
 * Camera system handles the camera.
 * Major job includes scrolling the viewport, along with the hook as block falls.
 */
public class CameraSystem extends com.toggle.katana2d.System {
    private GameState mGameState;

    public CameraSystem(GameState gameState) {
        super(new Class[]{Dummy.class});
        mGameState = gameState;
    }

    @Override
    public void update(float dt) {
        Viewport viewport = mGameState.game.getRenderer().getCamera();

        viewport.y += mGameState.cameraSpeedY * dt;
    }
}
