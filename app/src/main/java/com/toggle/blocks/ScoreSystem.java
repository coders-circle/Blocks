package com.toggle.blocks;

import android.graphics.Typeface;

import com.toggle.katana2d.*;

public class ScoreSystem extends com.toggle.katana2d.System {

    private GameState mGameState;
    private Font mFont;

    public ScoreSystem(GameState gameState) {
        super(new Class[]{Dummy.class});
        mGameState = gameState;

        mFont = new Font(mGameState.game.getRenderer(),
                Typeface.DEFAULT, 25);
    }

    @Override
    public void update(float dt) {

        // Calculate score based on topmost block.
        int limit = mGameState.game.getRenderer().height - 70;

        if (mGameState.topBlock == null)
            mGameState.currentHeight = 0;
        else {
            float posY = mGameState.topBlock.get(Transformation.class).y;

            if (posY > limit)
                mGameState.currentHeight = 0;
            else
                mGameState.currentHeight = (int)(limit-posY);
        }

        mGameState.score = Math.max(mGameState.score, mGameState.currentHeight);
    }

    @Override
    public void draw() {

        // Draw the score.

        GLRenderer renderer = mGameState.game.getRenderer();
        String displayText ="Height: " + mGameState.currentHeight +
                "\nScore: " + mGameState.score;
        Rectangle boundary = mFont.calculateBoundary(displayText, 0, 0, 0, 1, 1);

        mFont.draw(displayText, renderer.width-boundary.right-32,
                renderer.getCamera().y + 16, 0, 1, 1);
    }
}
