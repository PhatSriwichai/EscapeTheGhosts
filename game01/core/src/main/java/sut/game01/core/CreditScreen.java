package sut.game01.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by GGGCOM on 29/5/2559.
 */
public class CreditScreen extends Screen {
    private final ScreenStack ss;

    private Image bgImage;
    private ImageLayer bg;

    private Image profileImage;
    private ImageLayer profile;
    private Image mainImage;
    private ImageLayer main;
    private Image nameImage;
    private ImageLayer name;

    public CreditScreen(final ScreenStack ss){
        this.ss = ss;
        bgImage = assets().getImage("images/profile/bg.png");
        bg = graphics().createImageLayer(bgImage);
        //graphics().rootLayer().add(bg);

        profileImage = assets().getImage("images/profile/phat.png");
        profile = graphics().createImageLayer(profileImage);
        profile.setTranslation(260f,100f);

        mainImage = assets().getImage("images/button/mainButtonSmall.png");
        main = graphics().createImageLayer(mainImage);
        main.setTranslation(550f, 430f);

        nameImage = assets().getImage("images/profile/name.png");
        name = graphics().createImageLayer(nameImage);
        name.setTranslation(0f, 200f);

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
        this.layer.add(profile);
        this.layer.add(name);
        this.layer.add(main);
    }


}
