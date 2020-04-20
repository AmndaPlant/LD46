package com.amnda.ludumdare.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class GameTimeClock extends Actor {
    public static final float NIGHT_TIME = 19; // 7:00 PM
    public static final float DARK_TO_DAWN_TIME = 1; // Hours of transition from dark to dawn
    public static final float DAWN_TIME = 7; // 7:00 AM
    private static final float DAWN_TO_LIGHT_TIME = 2.15f; // Hours of transition from dawn to light
    private static final float DAY_TIME = 10; // 10:00 AM
    private static final float LIGHT_TO_DUSK_TIME = 0.75f; // Hours of transition from light to dusk
    private static final float DUSK_TIME = 18; // 6:00 PM
    private static final float DUSK_TO_DARK_TIME = 1; // Hours of transition from dusk to dark

    private Timer_ worldTime = null;
    private double rotation = 0;
    private Sprite sprite;
    private Color ambient = new Color();
    private Label text = null;

    private static final Color DAWN_COLOUR = new Color(0, 0, 0.75f, 0.2f);
    private static final Color DARK_COLOUR = new Color(0, 0,0, 0.6f);
    private static final Color LIGHT_COLOUR = new Color(1, 1, 1, 0);
    private static final Color DUSK_COLOUR = new Color(1, 0.3f, 0.67f, 0.1f);

    public GameTimeClock(Timer_ timer) {
        this.worldTime = timer;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        worldTime.tick();

        float degPerHour = 360 / 24;
        double time = worldTime.getElapsedInHours();
        rotation = time * (-degPerHour);

        Color current = new Color();
        Color lerp = null;
        double amt = 0;
        if (time >= NIGHT_TIME) {
            current.set(DARK_COLOUR);
        } else if (time < DAWN_TIME) {
            current.set(DARK_COLOUR);
            if (time >= (DAWN_TIME - DARK_TO_DAWN_TIME)) {
                lerp = DAWN_COLOUR;
                amt = (time - (DAWN_TIME - DARK_TO_DAWN_TIME)) / DARK_TO_DAWN_TIME;
            }
        } else if (time >= DAWN_TIME && time < DAY_TIME) {
            current.set(DAWN_COLOUR);
            if (time >= (DAY_TIME - DAWN_TO_LIGHT_TIME)) {
                lerp = LIGHT_COLOUR;
                amt = (time - (DAY_TIME - DAWN_TO_LIGHT_TIME)) / DAWN_TO_LIGHT_TIME;
            }
        } else if (time >= DAY_TIME && time < DUSK_TIME) {
            current.set(LIGHT_COLOUR);
            if (time >= (DUSK_TIME - LIGHT_TO_DUSK_TIME)) {
                lerp = DUSK_COLOUR;
                amt = (time - (DUSK_TIME - LIGHT_TO_DUSK_TIME)) / LIGHT_TO_DUSK_TIME;
            }
        } else if (time >= DUSK_TIME && time < NIGHT_TIME) {
            current.set(DUSK_COLOUR);
            if (time >= (NIGHT_TIME - DUSK_TO_DARK_TIME)) {
                lerp = DARK_COLOUR;
                amt = (time - (NIGHT_TIME - DUSK_TO_DARK_TIME)) / DUSK_TO_DARK_TIME;
            }
        }

        ambient.set(current);
        if (lerp != null) {
            ambient.lerp(lerp, (float) amt);
        }
    }

    public Color getAmbientLighting() {
        return ambient;
    }
}
