package com.amnda.ludumdare.screens;

import com.amnda.ludumdare.LudumDare;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainMenu implements Screen {
    private FitViewport port;
    private Stage mainStage;
    private Stage tutorialStage;
    private LudumDare game;
    private Skin skin;
    private Texture background;
    private Table table;

    public MainMenu(final LudumDare game) {
        this.game = game;
        port = new FitViewport(LudumDare.V_WIDTH, LudumDare.V_HEIGHT, new OrthographicCamera());
        mainStage = new Stage(port, game.getBatch());
        tutorialStage = new Stage(port, game.getBatch());
        Gdx.input.setInputProcessor(mainStage);
        background = new Texture("background.png");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        final TextButton startGame = new TextButton("New Game", skin);
        final TextButton tutorialButton = new TextButton("Tutorial", skin);
        final TextButton backButton = new TextButton("Back", skin);
        startGame.getLabel().setFontScale(1.5f);
        tutorialButton.getLabel().setFontScale(1.5f);
        backButton.getLabel().setFontScale(1.5f);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        font.font.getData().setScale(1);
        table = new Table();
        mainStage.addActor(table);
        table.center();
        table.setFillParent(true);

        table.add(startGame).padBottom(50f).padTop(100f).width(250).height(75);
        table.row();
        table.add(tutorialButton).width(250).height(75);

        final Table tutorial = new Table();
        tutorialStage.addActor(tutorial);
        tutorial.center();
        tutorial.setFillParent(true);
        final Label playAgainLabel = new Label("" +
                "You are competing with an evil corporation over your land!\n" +
                "Raise more money than them in 30 days or they'll buy out your land!\n" +
                "How to play:\n" +
                "Move around using the arrow keys or WASD\n" +
                "Cycle crop seeds and tools using the mouse wheel or number keys\n" +
                "Crops need to be watered everyday to grow, otherwise they'll die\n" +
                "Click on full grown crops to harvest for money\n" +
                "Click on crop crates to buy more seeds\n" +
                "Sleep in your bed to advance a day\n" +
                "", font);
        tutorial.add(playAgainLabel).padBottom(50);
        tutorial.row();
        tutorial.add(backButton).width(250).height(75);
        tutorial.setVisible(false);
        tutorialButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                table.setVisible(false);
                tutorial.setVisible(true);
                Gdx.input.setInputProcessor(tutorialStage);
                return true;
            }
        });
        backButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                table.setVisible(true);
                tutorial.setVisible(false);
                Gdx.input.setInputProcessor(mainStage);
                return true;
            }
        });


        startGame.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
                dispose();
                return true;
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, port.getWorldWidth(), port.getWorldHeight());
        game.getBatch().end();

        mainStage.act(delta);
        mainStage.draw();
        tutorialStage.act(delta);
        tutorialStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        port.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        skin.dispose();
        mainStage.dispose();
        background.dispose();
        tutorialStage.dispose();
    }
}
