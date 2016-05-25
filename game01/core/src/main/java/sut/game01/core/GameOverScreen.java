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

    public GameOverScreen(final ScreenStack ss){
        this.ss = ss;

        bgImage = assets().getImage("images/bg.png");
        bg = graphics().createImageLayer(bgImage);

        backButton = assets().getImage("images/backButton.png");
        back = graphics().createImageLayer(backButton);
        back.setTranslation(10f, 10f);

        back.addListener(new Mouse.LayerAdapter() {
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new GameScreen(ss));
            }
        });

    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bg);
        this.layer.add(back);
    }
}

