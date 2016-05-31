package sut.game01.core;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameOverScreen extends Screen {
    private final ScreenStack ss;
    private Image bgImage;
    private ImageLayer bg;
    private Image backButton;
    private ImageLayer back;
    private Image overImage;
    private ImageLayer over;
    private Image mainImage;
    private ImageLayer main;

    private int screenIndex;

    public GameOverScreen(final ScreenStack ss, final int screenIndex){
        this.ss = ss;

        bgImage = assets().getImage("images/background/overBg.jpg");
        bg = graphics().createImageLayer(bgImage);

        backButton = assets().getImage("images/backButton.png");
        back = graphics().createImageLayer(backButton);
        back.setTranslation(10f, 10f);

        overImage = assets().getImage("images/pauseZone/gameOver.png");
        over = graphics().createImageLayer(overImage);
        over.setTranslation(30f, 30f);
        mainImage = assets().getImage("images/pauseZone/mainButtonSmall.png");
        main = graphics().createImageLayer(mainImage);
        main.setTranslation(550f, 430f);

        back.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                switch(screenIndex){
                    case 1: ss.push(new GameScreen(ss)); break;
                    case 2: ss.push(new GameScreen2(ss)); break;
                    case 3: ss.push(new GameScreen3(ss)); break;
                }
            }
        });
        main.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new HomeScreen(ss));
            }
        });

    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(back);
        this.layer.add(over);
        this.layer.add(main);
    }
}

