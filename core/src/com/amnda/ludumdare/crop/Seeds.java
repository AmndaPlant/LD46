package com.amnda.ludumdare.crop;

import com.amnda.ludumdare.utils.Items;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Seeds {
    //private Texture texture;
    private Rectangle boundingRect;
    private Items.Item item;
    private int price;

    public Seeds(String string, Rectangle rectangle) {
        //this.texture = new Texture("seeds/" + string + "s.png");
        this.boundingRect = new Rectangle(rectangle.x, rectangle.y, 32, 32);
        setSeedPrice(string);
    }

    public void setSeedPrice(String string) {
        if (string.equals("gourd")) {
            this.price = 4;
            this.item = Items.Item.GOURD;
        } else if (string.equals("corn")) {
            this.price = 5;
            this.item = Items.Item.CORN;
        } else if (string.equals("carrot")) {
            this.price = 2;
            this.item = Items.Item.CARROT;
        } else if (string.equals("artichoke")) {
            this.price = 8;
            this.item = Items.Item.ARTICHOKE;
        } else if (string.equals("tomato")) {
            this.price = 4;
            this.item = Items.Item.TOMATO;
        } else if (string.equals("potato")) {
            this.price = 5;
            this.item = Items.Item.POTATO;
        } else if (string.equals("pepper")) {
            this.price = 7;
            this.item = Items.Item.PEPPER;
        }
    }

    public int getPrice() {
        return price;
    }

//    public Texture getTexture() {
//        return texture;
//    }

    public Rectangle getBoundingRect() {
        return boundingRect;
    }

    public Items.Item getItem() {
        return item;
    }
}
