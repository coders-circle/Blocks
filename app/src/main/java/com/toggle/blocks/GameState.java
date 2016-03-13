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

    // Actively falling block. When null, the hook may generate another block.
    public Entity activeBlock = null;

    public GameState(Game game, World world, BlockCreator blockCreator) {
        this.game = game;
        this.world = world;
        this.blockCreator = blockCreator;
    }
}
