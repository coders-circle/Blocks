package com.toggle.blocks;

import com.badlogic.gdx.physics.box2d.World;
import com.toggle.katana2d.Entity;
import com.toggle.katana2d.Game;

/**
 * GameState stores global game data used by different systems.
 * The FINAL data are constant global objects like Game, Box2D World and BlockCreator.
 * Other data represent current state of the game like actively falling block,
 * environmental states etc.
 */
public class GameState {

    // Constant objects used by different systems:
    public final Game game;
    public final World world;
    public final BlockCreator blockCreator;

    // Current state of the game represented by different objects and properties:
    public float cameraSpeedY = 0;
    public long blocks = 0;
    public Entity topBlock;
    public Entity hangingBlock = null;
    public Entity fallingBlock = null;

    // Current topmost block height.
    public int currentHeight;

    // Maximum height achieved till now.
    public int score;

    /**
     * Construct a new GameState object.
     * @param game The Game object for which object represent states.
     * @param world The Box2D World where the game bodies will be created and destroyed.
     * @param blockCreator The block creator that create new block entities.
     */
    public GameState(Game game, World world, BlockCreator blockCreator) {
        this.game = game;
        this.world = world;
        this.blockCreator = blockCreator;
    }
}
