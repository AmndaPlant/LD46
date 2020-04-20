package com.amnda.ludumdare.utils;

import com.amnda.ludumdare.crop.Seeds;
import com.amnda.ludumdare.screens.GameScreen;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MapLoader {
    private Vector2 playerSpawn;
    private TiledMap map;

    public MapLoader(GameScreen screen) {
        map = screen.getMap();
        playerSpawn = new Vector2();

        for (MapObject object : map.getLayers().get("Player_Spawn").getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            playerSpawn.set(rect.x, rect.y);
        }

        for (MapObject object : map.getLayers().get("Seeds").getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                screen.getSeeds().add(new Seeds(object.getName(), rect));
            }
        }
    }

    public Vector2 getPlayerSpawn() {
        return playerSpawn;
    }
}
