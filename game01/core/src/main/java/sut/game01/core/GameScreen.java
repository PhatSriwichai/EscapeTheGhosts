package sut.game01.core;

import playn.core.*;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import sut.game01.core.character.*;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameScreen extends UIScreen {
    private final ScreenStack ss;
    private Image bgImage;
    private ImageLayer bgLayer;
    private Hero hero;


 

    public GameScreen(final ScreenStack ss){
        this.ss = ss;
        hero = new Hero(300,330);

        bgImage = assets().getImage("images/background/bg1.png");
        bgLayer = graphics().createImageLayer(bgImage);
        //graphics().rootLayer().add(bgLayer);

        PlayN.keyboard().setListener(new Keyboard.Adapter(){
            @Override
            public void onKeyUp(Keyboard.Event event) {
                if(event.key() == Key.ESCAPE){
                    ss.remove(ss.top());
                }
            }
        });
      
    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(hero.layer());

    }

    @Override
    public void update(int delta) {
        super.update(delta);
        hero.update(delta);
    }
}
