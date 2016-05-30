package sut.game01.core;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by GGGCOM on 21/5/2559.
 */
public class LevelScreen extends Screen {
    private ScreenStack ss;
    private GameScreen gameScreen;
    private GameScreen2 gameScreen2;
    private GameScreen3 gameScreen3;
    private Image bgImage;
    private ImageLayer bg;
    private Image selectImage;
    private ImageLayer select;
    private Image levelImage1;
    private ImageLayer level1;
    private Image levelImage2;
    private ImageLayer level2;
    private Image levelImage3;
    private ImageLayer level3;
    private ImageLayer backLayer;
    private Image backButton;

    public LevelScreen(final ScreenStack ss) {
        this.ss = ss;
        gameScreen = new GameScreen(ss);
        gameScreen2 = new GameScreen2(ss);
        gameScreen3 = new GameScreen3(ss);
        bgImage = assets().getImage("images/homeBackground2.png");
        bg = graphics().createImageLayer(bgImage);

        backButton = assets().getImage("images/backButton.png");
        backLayer = graphics().createImageLayer(backButton);

        backLayer.setTranslation(10, 10);

        selectImage = assets().getImage("images/levelSelect/levelSelect.png");
        select = graphics().createImageLayer(selectImage);
        select.setTranslation(50f, 50f);

        levelImage1 = assets().getImage("images/levelSelect/level1.png");
        level1 = graphics().createImageLayer(levelImage1);
        level1.setTranslation(100f, 150f);

        levelImage2 = assets().getImage("images/levelSelect/level2.png");
        level2 = graphics().createImageLayer(levelImage2);
        level2.setTranslation(250f, 150f);

        levelImage3 = assets().getImage("images/levelSelect/level3.png");
        level3 = graphics().createImageLayer(levelImage3);
        level3.setTranslation(400f, 150f);

        level1.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(gameScreen);
            }
        });
        level2.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(gameScreen2);
            }
        });
        level3.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(gameScreen3);
            }
        });

        backLayer.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });
    }

    @Override
    public void wasShown() {
        super.wasShown();

        this.layer.add(bg);
        this.layer.add(select);
        this.layer.add(level1);
        this.layer.add(level2);
        this.layer.add(level3);
        this.layer.add(backLayer);
    }
}
