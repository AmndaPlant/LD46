package com.amnda.ludumdare.screens;

import com.amnda.ludumdare.LudumDare;
import com.amnda.ludumdare.crop.Crop;
import com.amnda.ludumdare.crop.Seeds;
import com.amnda.ludumdare.sprites.Player;
import com.amnda.ludumdare.utils.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {
    private LudumDare game;

    // Camera Variables
    private OrthographicCamera cam;
    private FitViewport port;

    // Player variables
    private Player player;
    private TextureRegion currentPlayerFrame;
    private Sprite currentPlayerSprite;
    private PlayerController controller;
    private boolean sleep;

    // Map variables
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private MapLoader loader;
    private int mapWidth;
    private int mapHeight;
    private MapObject object;

    // Clock Variables
    private GameTimeClock clock;
    private Timer_ timer;
    private int currentDays;
    private ShapeRenderer shapeRenderer;

    // UI Variables
    private static Integer money;
    private static Label timeLabel;
    private static Label timeStringLabel;
    private static Label scoreLabel;
    private static Label scoreStringLabel;
    private static Label corporateLabel;
    private static Label corporateScoreLabel;
    private static int corporateScore;
    private static Label daysLeftLabel;
    private static Label daysLeftNum;
    private Viewport uiPort;
    public Stage stage;
    private String time;
    private Skin skin;
    private Dialog bedMenu;
    private boolean showStage;
    private BitmapFont font;
    private static int daysLeft;
    private Label win;
    private Label lose;

    // Item Variables
    public Items.ItemType currentType;
    public Items currentItem;
    public Texture bucketTexture;
    public Items bucket;
    public Items tomatoSeed;
    public Items potatoSeed;
    public Items carrotSeed;
    public Items cornSeed;
    public Items artSeed;
    public Items pepperSeed;
    public Items gourdSeed;
    public int intType;

    // Inventory variables
    private Array<Items> items;
    private Texture box;
    private Texture border;

    // Crop Variables
    public Array<Crop> crops;
    public int numCrops;
    private TextureRegion mouseFrame;
    public Texture mouseCrop;
    private TextureRegion[][] textures;
    public Array<Seeds> seeds;

    private Music music;

    public GameScreen(final LudumDare game) {
        this.game = game;

        cam = new OrthographicCamera();
        port = new FitViewport(LudumDare.V_WIDTH, LudumDare.V_HEIGHT, cam);
        cam.setToOrtho(false, port.getWorldWidth(), port.getWorldWidth());
        cam.zoom = 0.5f;

        seeds = new Array<Seeds>();

        map = new TmxMapLoader().load("outside.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(cam);
        loader = new MapLoader(this);
        mapWidth = map.getProperties().get("width", Integer.class) * 32;
        mapHeight = map.getProperties().get("height", Integer.class) * 32;

        player = new Player();
        player.startingPosition(loader.getPlayerSpawn().x, loader.getPlayerSpawn().y);
        currentPlayerSprite = player.getFrameSprite();
        controller = new PlayerController(this, player);
        shapeRenderer = new ShapeRenderer();
        sleep = false;

        timer = new Timer_();
        timer.startNew(60, true, true);
        timer.setStartTime(0,12, 0, 0);
        clock = new GameTimeClock(timer);
        currentDays = 0;
        daysLeft = 30;

        time = timer.getFormattedTimeOfDay();

        crops = new Array<Crop>();
        numCrops = 0;
        intType = 0;
        mouseCrop = new Texture("veggies.png");

        textures = TextureRegion.split(mouseCrop, 32, 32);
        tomatoSeed = new Items(textures[0][0], Items.ItemType.SEED, Items.Item.TOMATO);
        potatoSeed = new Items(textures[0][1], Items.ItemType.SEED, Items.Item.POTATO);
        carrotSeed = new Items(textures[0][2], Items.ItemType.SEED, Items.Item.CARROT);
        cornSeed = new Items(textures[0][3], Items.ItemType.SEED, Items.Item.CORN);
        gourdSeed = new Items(textures[0][4], Items.ItemType.SEED, Items.Item.GOURD);
        artSeed = new Items(textures[0][5], Items.ItemType.SEED, Items.Item.ARTICHOKE);
        pepperSeed = new Items(textures[0][6], Items.ItemType.SEED, Items.Item.PEPPER);
        bucketTexture = new Texture("bucket.png");
        bucket = new Items(bucketTexture, Items.ItemType.TOOL, Items.Item.BUCKET);

        items = new Array<Items>(8);
        items.add(bucket);
        items.add(tomatoSeed);
        items.add(potatoSeed);
        items.add(carrotSeed);
        items.add(cornSeed);
        items.add(gourdSeed);
        items.add(artSeed);
        items.add(pepperSeed);
        setMouseCrop(bucket);

        for (int i = 0; i < 20; i++) {
            addSeeds(Items.Item.CARROT);
        }

        box = new Texture("box.png");
        border = new Texture("border.png");

        uiPort = new FitViewport(LudumDare.V_WIDTH, LudumDare.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(uiPort, game.getBatch());
        Table winTable = new Table();
        Table loseTable = new Table();
        Table table = new Table();
        winTable.center();
        winTable.setFillParent(true);
        loseTable.center();
        loseTable.setFillParent(true);
        table.top();
        table.setFillParent(true);
        money = 100;
        corporateScore = 100;
        timeLabel = new Label(time, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeStringLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("$%d", money), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreStringLabel = new Label("Money", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        corporateLabel = new Label("Evil Corporate Money", new Label.LabelStyle(new BitmapFont(), Color.RED));
        corporateScoreLabel = new Label(String.format("$%d", corporateScore), new Label.LabelStyle(new BitmapFont(), Color.RED));
        daysLeftLabel = new Label("Days Left", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        daysLeftNum = new Label(String.format("%d", daysLeft), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lose = new Label("You lose! The evil corporate wins!", new Label.LabelStyle(new BitmapFont(), Color.RED));
        win = new Label("You win! The evil corporate doesn't get your land!", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        win.setVisible(false);
        win.setFontScale(3);
        lose.setVisible(false);
        lose.setFontScale(3);
        timeStringLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        scoreStringLabel.setFontScale(2);
        corporateLabel.setFontScale(2);
        corporateScoreLabel.setFontScale(2);
        daysLeftLabel.setFontScale(2);
        daysLeftNum.setFontScale(2);
        table.add(scoreStringLabel).padRight(100f);
        table.add(corporateLabel).padRight(100f);
        table.add(timeStringLabel).padRight(100f);
        table.add(daysLeftLabel);
        table.row();
        table.add(scoreLabel).padRight(100f);
        table.add(corporateScoreLabel).padRight(100f);
        table.add(timeLabel).padRight(100f);
        table.add(daysLeftNum);
        loseTable.add(lose);
        winTable.add(win);
        stage.addActor(table);
        stage.addActor(loseTable);
        stage.addActor(winTable);
        font = new BitmapFont();

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        bedMenu = new Dialog("", skin, "dialog") {
            protected void result(Object object) {
                if (object.equals(true)) {
                    sleep = true;
                }
                Timer.schedule(new Timer.Task()  {
                    @Override
                    public void run() {
                        bedMenu.remove();
                        showStage = false;
                    }
                }, 0);
            }
        };
        bedMenu.text("Would you like to sleep?");
        bedMenu.button("Yes", true);
        bedMenu.button("No", false);

        InputMultiplexer inputMultiplexer = new InputMultiplexer ();
        inputMultiplexer.addProcessor(controller);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        music = LudumDare.assets.get("bgm.wav", Music.class);
        music.setLooping(true);
        music.setVolume(.2f);
        music.play();
    }

    public void setMouseCrop(Items item) {
        mouseFrame = item.getTextureRegion();
        currentItem = item;
        currentType = item.getType();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (daysLeft == 0) {
            if (corporateScore > money) {
                lose.setVisible(true);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        music.stop();
                        game.setScreen(new MainMenu(game));
                        dispose();
                    }
                }, 7);
            }
            if (corporateScore < money) {
                win.setVisible(true);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        music.stop();
                        game.setScreen(new MainMenu(game));
                        dispose();
                    }
                }, 7);
            }
        }

        if (currentPlayerSprite.getX() + (cam.viewportWidth / 2 * cam.zoom) < mapWidth &&
            currentPlayerSprite.getX() - (cam.viewportWidth / 2 * cam.zoom) > 0)
            cam.position.x = player.getPlayerCentreX();
        if (currentPlayerSprite.getY() + (cam.viewportHeight / 2 * cam.zoom) < mapHeight &&
                currentPlayerSprite.getY() - (cam.viewportHeight / 2 * cam.zoom) > 0)
            cam.position.y = player.getPlayerCentreY();
        if (!isCollision(player.getBoundingBox()))
            player.setCurrentToNext();

        if ((object = isTeleport(player.getBoundingBox())) != null ) {
            map.dispose();
            map = new TmxMapLoader().load(object.getName() + ".tmx");
            renderer.setMap(map);
            mapWidth = map.getProperties().get("width", Integer.class) * 32;
            mapHeight = map.getProperties().get("height", Integer.class) * 32;
            for (MapObject spawn : map.getLayers().get("Player_Spawn").getObjects()) {
                Rectangle rectangle = ((RectangleMapObject) spawn).getRectangle();
                player.setCurrentPosition(rectangle.x, rectangle.y);
                cam.position.set(rectangle.x, rectangle.y, 0);
            }
        }

        if (isBed(player.getBoundingBox())) {
            if (!showStage) {
                showStage = true;
                bedMenu.show(stage);
            }

            if (sleep) {
                timer.setDaysPassed(currentDays + 1);
                timer.setStartTime(currentDays + 1, 7, 0, 0);
                for (MapObject spawn : map.getLayers().get("Player_Spawn").getObjects()) {
                    Rectangle rect = ((RectangleMapObject) spawn).getRectangle();
                    player.setCurrentPosition(rect.x, rect.y);
                    cam.position.set(rect.x, rect.y, 0);
                }
            }
        }

        sleep = false;

             cam.update();
        player.update(delta);
        renderer.setView(cam);
        renderer.render();

        clock.act(delta);
        if (timer.getDaysPassed() != currentDays) {
            for (Crop crop : crops) {
                crop.addDay();
            }
            currentDays = timer.getDaysPassed();
            daysLeft--;
            daysLeftNum.setText(String.format("%d", daysLeft));
            corporateScore += 50;
            corporateScoreLabel.setText(String.format("$%d", corporateScore));
        }
        time = timer.getFormattedTimeOfDay();
        timeLabel.setText(time);

        controller.update(delta);
        game.getBatch().setProjectionMatrix(cam.combined);
        currentPlayerFrame = player.getCurrentFrame();

        game.getBatch().begin();
        for (int i = 0; i < numCrops; i++) {
            if (crops.get(i).isWatered())
                game.getBatch().setColor(Color.BROWN);
            game.getBatch().draw(textures[0][7], crops.get(i).getFrameSprite().getX(), crops.get(i).getFrameSprite().getY() - 6);
            game.getBatch().setColor(Color.WHITE);
            game.getBatch().draw(crops.get(i).getCurrentFrame(), crops.get(i).getFrameSprite().getX(), crops.get(i).getFrameSprite().getY());
            if (crops.get(i).isDead()) {
                crops.removeIndex(i);
                numCrops--;
            }
        }
        game.getBatch().draw(currentPlayerFrame, currentPlayerSprite.getX(), currentPlayerSprite.getY());
        game.getBatch().end();

        shapeRenderer.setProjectionMatrix(cam.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(clock.getAmbientLighting());
        Matrix4 mat = cam.combined.cpy();
        shapeRenderer.setProjectionMatrix(mat);
        mat.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.rect(cam.position.x - port.getWorldWidth() / 2, cam.position.y - port.getWorldHeight() / 2, port.getWorldWidth(), port.getWorldHeight());
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        game.getBatch().setProjectionMatrix(mat);
        shapeRenderer.setColor(Color.WHITE);

        game.getBatch().setProjectionMatrix(cam.combined);
        game.getBatch().begin();
        for (int i = 0; i < 8; i++) {
            game.getBatch().draw(box, (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)), cam.position.y - (cam.viewportHeight / 2 * cam.zoom));
            game.getBatch().draw(items.get(i).getTextureRegion(), (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)), cam.position.y - (cam.viewportHeight / 2 * cam.zoom));
            if (items.get(i).getType() == Items.ItemType.SEED)
                font.draw(game.getBatch(), String.format("%d", items.get(i).getNum()), (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)-6), cam.position.y - (cam.viewportHeight / 2 * cam.zoom)+12);
            if (items.get(i).getItem() == currentItem.getItem())
                game.getBatch().draw(border, (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)), cam.position.y - (cam.viewportHeight / 2 * cam.zoom));
        }
        game.getBatch().end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        port.update(width, height);
        uiPort.update(width, height);
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
        stage.dispose();
        bucketTexture.dispose();
        map.dispose();
        skin.dispose();
        font.dispose();
        mouseCrop.dispose();
        box.dispose();
        border.dispose();
        music.dispose();
    }

    public TiledMap getMap() {
        return map;
    }

    public Timer_ getTimer() {
        return timer;
    }

    public Array<Items> getItems() {
        return items;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public Array<Crop> getCrops() {
        return crops;
    }

    public int getCurrentDays() {
        return currentDays;
    }

    public Array<Seeds> getSeeds() {
        return seeds;
    }

    public boolean isCollision(Rectangle boundingBox) {
        MapLayer objectLayer  = map.getLayers().get("Collision");
        return checkCollision(boundingBox, objectLayer);
    }

    private boolean isBed(Rectangle boundingBox) {
        if (map.getProperties().get("name").equals("inside")) {
            MapLayer objectLayer = map.getLayers().get("Bed");
            return checkCollision(boundingBox, objectLayer);
        } else {
            return false;
        }
    }

    public MapObject isTeleport(Rectangle boundingBox) {
        MapLayer objectLayer = map.getLayers().get("Teleport");
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (boundingBox.overlaps(rect))
                    return object;
            }
        }

        return null;
    }

    public boolean checkCollision(Rectangle boundingBox, MapLayer objectLayer) {
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (boundingBox.overlaps(rect))
                    return true;
            }
        }

        return false;
    }

    public void removeSeeds(Items.Item item) {
        switch (item) {
            case TOMATO:
                tomatoSeed.remove();
                break;
            case POTATO:
                potatoSeed.remove();
                break;
            case CARROT:
                carrotSeed.remove();
                break;
            case CORN:
                cornSeed.remove();
                break;
            case ARTICHOKE:
                artSeed.remove();
                break;
            case GOURD:
                gourdSeed.remove();
                break;
            case PEPPER:
                pepperSeed.remove();
                break;
        }
    }

    public void addSeeds(Items.Item item) {
        switch (item) {
            case TOMATO:
                tomatoSeed.add();
                break;
            case POTATO:
                potatoSeed.add();
                break;
            case CARROT:
                carrotSeed.add();
                break;
            case CORN:
                cornSeed.add();
                break;
            case ARTICHOKE:
                artSeed.add();
                break;
            case GOURD:
                gourdSeed.add();
                break;
            case PEPPER:
                pepperSeed.add();
                break;
        }
    }

    public void buySeeds(Seeds seed) {
        if (seed.getPrice() <= money) {
            money -= seed.getPrice();
            addSeeds(seed.getItem());
        }

        scoreLabel.setText(String.format("$%d", money));
    }

    public void addCrop(Crop crop) {
        crops.add(crop);
    }

    public void addMoney(int price) {
        money += price;
        scoreLabel.setText(String.format("$%d", money));
    }
}
