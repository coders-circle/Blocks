package com.toggle.blocks;

import com.toggle.katana2d.Entity;

/**
 * BlockCreator is used to create new blocks at given position.
 */
public interface BlockCreator {
    Entity createBlock(float x, float y);
}
