package sut.game01.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenSpace;
import tripleplay.game.ScreenStack;

public class GamePauseScreen extends Screen {
    private final ScreenStack ss;
    private Image bgImage;
    private ImageLayer bg;
    private Image pauseImage;
    private ImageLayer pauseZone;
    private Image resumeImage;
    private ImageLayer resume;
    private Image settingImage;
    private ImageLayer setting;
    private Image mainImage;
    private ImageLayer main;

    private HomeScreen home;

    public GamePauseScreen(final ScreenStack ss, Image bgImage){
        this.ss = ss;
        this.bgImage = bgImage;
        //home = new HomeScreen(ss);
        bg = graphics().createImageLayer(bgImage);

        pauseImage = assets().getImage("images/pauseZone/pauseZone.png");
        pauseZone = graphics().createImageLayer(pauseImage);
        pauseZone.setTranslation(60f,20f);
        resumeImage = assets().getImage("images/pauseZone/resumeButton.png");
        resume = graphics().createImageLayer(resumeImage);
        resume.setTranslation(90f, 260f);
        settingImage = assets().getImage("images/pauseZone/settingButton.png");
        setting = graphics().createImageLayer(settingImage);
        setting.setTranslation(230f, 260f);
        mainImage = assets().getImage("images/pauseZone/mainButton.png");
        main = graphics().createImageLayer(mainImage);
        main.setTranslation(360f, 260f);

        resume.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });

        //
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
        this.layer.add(pauseZone);
        this.layer.add(resume);
        this.layer.add(setting);
        this.layer.add(main);

    }
}
