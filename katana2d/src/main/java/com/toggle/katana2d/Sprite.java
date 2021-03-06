package com.toggle.katana2d;


// Sprite component that stores sprite-sheet data and a reference to GLSprite
public class Sprite implements Component{
    public Texture texture;
    public float[] mixColor = new float[] {1,1,1,1};
    public SpriteSheetData spriteSheetData;
    public float scaleX = 1, scaleY = 1;
    public boolean visible = true;
    public boolean postDrawn = false;
    public float z = 0;

    public Sprite(Texture texture) {
        this.texture = texture;
    }

    public Sprite(Texture texture, SpriteSheetData spriteSheetData) {
        this.texture = texture;
        this.spriteSheetData = spriteSheetData;
    }

    public Sprite(Texture texture, int numCols, int numRows) {
        this(texture, numCols, numRows, numCols*numRows);
    }

    public Sprite(Texture texture, int numCols, int numRows, int numImages) {
        this.texture = texture;
        spriteSheetData = new SpriteSheetData();
        spriteSheetData.numRows = numRows;
        spriteSheetData.numCols = numCols;
        spriteSheetData.numImages = numImages;

        spriteSheetData.imgWidth = 1f/numCols;
        spriteSheetData.imgHeight = 1f/numRows;
    }

    public Sprite(Texture texture, int numCols, int numRows, int numImages, int index,
                  float animationSpeed) {
        this(texture, numCols, numRows, numImages);
        spriteSheetData.index = index;
        spriteSheetData.animationSpeed = animationSpeed;
    }

    public Sprite(Texture texture, int numCols, int numRows, int numImages, int index,
                  float animationSpeed, float offsetX, float offsetY, float hSpacing,
                  float vSpacing) {
        spriteSheetData.index = index;
        spriteSheetData.animationSpeed = animationSpeed;
        spriteSheetData.offsetX = offsetX;
        spriteSheetData.offsetY = offsetY;
        spriteSheetData.hSpacing = hSpacing;
        spriteSheetData.vSpacing = vSpacing;
    }

    public void reset() {
        if (spriteSheetData != null) {
            spriteSheetData.index = 0;
            spriteSheetData.timePassed = 0;
        }
    }

    public static class SpriteSheetData {
        // all these are normalized (i.e. in [0, 1] range) with respect to texture size
        public float offsetX = 0, offsetY = 0, imgWidth, imgHeight, hSpacing = 0, vSpacing = 0;

        public int numRows = 1, numCols = 1;
        public int numImages = -1;              // If -1 then numImages = numRows x numCols

        public float animationSpeed = 12; // in FPS
        public boolean loop = true;       // loop animation?
        public float timePassed = 0;     // time that has elapsed since last frame

        public int index = 0;	// the index of image to draw next

        public AnimationListener listener;
    }

    public interface AnimationListener {
        void onComplete();
    }

    public void changeSprite(Texture sprite, SpriteSheetData newSpriteSheetData) {
        if (sprite != null)
            texture = sprite;

        if (spriteSheetData == newSpriteSheetData || newSpriteSheetData == null)
            return;

        if (spriteSheetData != null && spriteSheetData.animationSpeed > 0) {
            spriteSheetData.index = 0;
            spriteSheetData.timePassed = 0;
        }
        spriteSheetData = newSpriteSheetData;
    }

    public void animate(double dt) {
        SpriteSheetData ssd = spriteSheetData;
        // animate a sprite sheet by advancing the image index when required time has elapsed
        if (ssd != null && ssd.animationSpeed > 0) {
            if (ssd.numImages < 0)
                ssd.numImages = ssd.numRows * ssd.numCols;

            ssd.timePassed += (float) dt;
            if (ssd.timePassed >= 1.0/ssd.animationSpeed) {
                ssd.timePassed = 0;
                ssd.index++;

                if (ssd.index >= ssd.numImages) {
                    if (ssd.listener != null)
                        ssd.listener.onComplete();
                    if (ssd.loop)
                        ssd.index = 0;
                    else
                        ssd.index--;
                }
            }
        }
    }

    public void draw(GLRenderer renderer, float x, float y, float angle) {
        if (texture == null || !visible)
            return;

        float[] color = texture.color;
        texture.color = new float[] {
                texture.color[0]*mixColor[0],
                texture.color[1]*mixColor[1], texture.color[2]*mixColor[2],
                texture.color[3]*mixColor[3]
        };
        if (spriteSheetData == null)
            texture.draw(renderer, x, y, -z, angle, scaleX, scaleY);
        else {
            Sprite.SpriteSheetData ssd = spriteSheetData;

            int col = ssd.index % ssd.numCols;
            int row = ssd.index / ssd.numCols;

            float clipX = (ssd.imgWidth + ssd.hSpacing) * col + ssd.offsetX;
            float clipY = (ssd.imgHeight + ssd.vSpacing) * row + ssd.offsetY;

            texture.draw(renderer, x, y, -z, angle, scaleX, scaleY, clipX, clipY,
                    ssd.imgWidth, ssd.imgHeight);
        }
        texture.color = color;
    }
}
