package com.amnda.ludumdare.crop;

import com.amnda.ludumdare.utils.Items;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Crop extends Sprite {
    private int growthStage;
    private int growthStageDuration;
    private int daysOld;
    private int price;
    private int daysNotWatered;
    private boolean isWatered;
    private boolean isDead;
    private float centreX;
    private float centreY;
    private Vector2 pos;
    private Texture texture;
    private Sprite frameSprite;
    private TextureRegion currentFrame;
    private TextureRegion[][] textureFrames;
    private Array<TextureRegion> cropFrames;

    public Crop(Items.Item item, float x, float y) {
        pos = new Vector2(x, y);
        growthStage = 0;
        daysOld = 0;
        isWatered = false;
        daysNotWatered = 0;
        isDead = false;
        centreX  = x - 8;
        centreY = y - 8;
        texture = new Texture("veggies.png");
        textureFrames = TextureRegion.split(texture, 32, 32);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, 32, 16);
        frameSprite.setX(Math.round(centreX / 32) * 32);
        frameSprite.setY(Math.round(centreY / 16) * 16);
        loadInfo(item);
    }

    private void loadInfo(Items.Item item) {
        switch (item) {
            case TOMATO:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 0; i < 4; i++) {
                    cropFrames.insert(i, textureFrames[i + 1][0]);
                }
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 2;
                this.price = 10;
                break;
            case POTATO:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 0; i < 4; i++) {
                    cropFrames.insert(i, textureFrames[i + 1][1]);
                }
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 3;
                this.price = 20;
                break;
            case CARROT:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 0; i < 4; i++) {
                    cropFrames.insert(i, textureFrames[i + 1][2]);
                }
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 2;
                this.price = 8;
                break;
            case CORN:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 0; i < 4; i++) {
                    cropFrames.insert(i, textureFrames[i + 1][3]);
                }
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 3;
                this.price = 20;
                break;
            case GOURD:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 0; i < 4; i++) {
                    cropFrames.insert(i, textureFrames[i + 1][4]);
                }
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 3;
                this.price = 19;
                break;
            case ARTICHOKE:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 0; i < 4; i++) {
                    cropFrames.insert(i, textureFrames[i + 1][5]);
                }
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 5;
                this.price = 45;
                break;
            case PEPPER:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 0; i < 4; i++) {
                    cropFrames.insert(i, textureFrames[i + 1][6]);
                }
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 4;
                this.price = 30;
                break;
        }
    }

    public void addDay() {
        if (isWatered) {
            daysNotWatered = 0;
            daysOld++;
        }
        if (!isWatered && growthStage != 3) {
            daysNotWatered++;
        }
        if (daysNotWatered == 2)
            isDead = true;
        isWatered = false;
        checkGrowth();
    }

    private void checkGrowth() {
        if (daysOld % growthStageDuration == 0 && growthStage != 3) {
            growthStage++;
            this.currentFrame = cropFrames.get(growthStage);
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Sprite getFrameSprite() {
        return frameSprite;
    }

    public int getPrice() {
        return price;
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public void setWatered(boolean watered) {
        isWatered = watered;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isWatered() {
        return isWatered;
    }
}
