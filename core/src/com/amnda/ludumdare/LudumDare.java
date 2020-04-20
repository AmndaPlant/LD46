package com.amnda.ludumdare;

import com.amnda.ludumdare.screens.MainMenu;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LudumDare extends Game {
	private SpriteBatch batch;

	public static final int V_WIDTH = 1280;
	public static final int V_HEIGHT = 720;
	public static AssetManager assets;

	@Override
	public void create() {
		batch = new SpriteBatch();
		assets = new AssetManager();
		assets.load("water.wav", Sound.class);
		assets.load("plant.wav", Sound.class);
		assets.load("seeds.wav", Sound.class);
		assets.load("bgm.wav", Music.class);
		assets.finishLoading();
		setScreen(new MainMenu(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		super.dispose();
		batch.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}