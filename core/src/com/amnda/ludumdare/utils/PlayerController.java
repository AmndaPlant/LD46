package com.amnda.ludumdare.utils;

import com.amnda.ludumdare.LudumDare;
import com.amnda.ludumdare.crop.Crop;
import com.amnda.ludumdare.screens.GameScreen;
import com.amnda.ludumdare.sprites.Player;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;

public class PlayerController implements InputProcessor {
    private Player player;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private GameScreen screen;

    public PlayerController(GameScreen screen, Player player) {
        this.player = player;
        this.screen = screen;
        left = false;
        right = false;
        up = false;
        down = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            left = true;
        }
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            up = true;
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            down = true;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            right = true;
        }
        if (keycode == Input.Keys.EQUALS)
            screen.getTimer().setTimeRatio(9000);
        if (keycode == Input.Keys.MINUS)
            screen.getTimer().setTimeRatio(Timer_.GAMETIME);
        if (keycode == Input.Keys.NUM_1)
            screen.setMouseCrop(screen.getItems().get(0));
        if (keycode == Input.Keys.NUM_2)
            screen.setMouseCrop(screen.getItems().get(1));
        if (keycode == Input.Keys.NUM_3)
            screen.setMouseCrop(screen.getItems().get(2));
        if (keycode == Input.Keys.NUM_4)
            screen.setMouseCrop(screen.getItems().get(3));
        if (keycode == Input.Keys.NUM_5)
            screen.setMouseCrop(screen.getItems().get(4));
        if (keycode == Input.Keys.NUM_6)
            screen.setMouseCrop(screen.getItems().get(5));
        if (keycode == Input.Keys.NUM_7)
            screen.setMouseCrop(screen.getItems().get(6));
        if (keycode == Input.Keys.NUM_8)
            screen.setMouseCrop(screen.getItems().get(7));
        if (keycode == Input.Keys.NUM_0) {
            screen.getTimer().setDaysPassed(screen.getCurrentDays() + 1);
            screen.getTimer().setStartTime(screen.getCurrentDays() + 1, 7, 0, 0);
        }
        if (keycode == Input.Keys.M)
            screen.addMoney(1000);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            left = false;
        }
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            up = false;
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            down = false;
        }
        if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            right = false;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = screen.getCam().unproject(new Vector3(screenX, screenY, 0));
        if (Math.abs(player.getPlayerCentreX() - coords.x) < 50 && Math.abs(player.getPlayerCentreY() - coords.y) < 50) {
            for (int i = 0; i < screen.getSeeds().size; i++) {
                if (screen.getSeeds().get(i).getBoundingRect().contains(coords.x, coords.y)) {
                    screen.buySeeds(screen.getSeeds().get(i));
                    LudumDare.assets.get("seeds.wav", Sound.class).play(2);
                }
            }

            for (int i = 0; i < screen.numCrops; i++) {
                if (screen.getCrops().get(i).getFrameSprite().getBoundingRectangle().contains(coords.x, coords.y)) {
                    if (screen.currentItem.getItem() == Items.Item.BUCKET) {
                        screen.getCrops().get(i).setWatered(true);
                        LudumDare.assets.get("water.wav", Sound.class).play(2);
                    }
                    if (screen.getCrops().get(i).getGrowthStage() == 3) {
                        screen.addMoney(screen.getCrops().get(i).getPrice());
                        screen.getCrops().removeIndex(i);
                        LudumDare.assets.get("plant.wav", Sound.class).play(2);
                        screen.numCrops--;
                        return false;
                    } else {
                        return false;
                    }
                }
            }

            if (screen.currentType == Items.ItemType.SEED) {
                TiledMapTileLayer ground = (TiledMapTileLayer) screen.getMap().getLayers().get("Tile Layer 1");
                TiledMapTileLayer.Cell cell = ground.getCell(Math.round(coords.x / 32), Math.round(coords.y / 32));
                if (cell != null) {
                    Object grass = cell.getTile().getProperties().get("grass");
                    if (grass != null && screen.currentItem.getNum() > 0) {
                        for (Crop crop : screen.getCrops()) {
                            if (crop.getBoundingRectangle().contains(coords.x, coords.y))
                                return false;
                        }
                        Crop crop = new Crop(screen.currentItem.getItem(), coords.x, coords.y);
                        screen.addCrop(crop);
                        screen.removeSeeds(screen.currentItem.getItem());
                        screen.numCrops++;
                        LudumDare.assets.get("plant.wav", Sound.class).play(2);
                    }
                }
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        screen.intType += amount;
        if (screen.intType > screen.getItems().size - 1)
            screen.intType = 0;
        if (screen.intType < 0)
            screen.intType = screen.getItems().size-  1;
        screen.setMouseCrop(screen.getItems().get(screen.intType));

        return false;
    }

    public void update(float delta) {
        processInput(delta);
    }

    private void processInput(float delta) {
        if (up) {
            player.move(Player.Direction.UP, delta);
            player.setState(Player.State.WALKING);
            player.setDirection(Player.Direction.UP);
        } else if (down) {
            player.move(Player.Direction.DOWN, delta);
            player.setState(Player.State.WALKING);
            player.setDirection(Player.Direction.DOWN);
        } else if (left) {
            player.move(Player.Direction.LEFT, delta);
            player.setState(Player.State.WALKING);
            player.setDirection(Player.Direction.LEFT);
        } else if (right) {
            player.move(Player.Direction.RIGHT, delta);
            player.setState(Player.State.WALKING);
            player.setDirection(Player.Direction.RIGHT);
        } else {
            player.setState(Player.State.IDLE);
            player.setDirection(player.getDirection());
        }
    }
}
