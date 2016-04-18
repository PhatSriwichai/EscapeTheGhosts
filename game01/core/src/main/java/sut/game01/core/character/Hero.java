package sut.game01.core.character;

import sut.game01.core.sprite.*;
import playn.core.util.*;
import playn.core.*;
import sut.game01.core.*;
import tripleplay.game.*;
import playn.core.Layer;

public class Hero{
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private float x;
    private float y;

    public enum State{
        IDLE, IDLE2, IDLE3, RUN, RUN2, RUN3, ATTK, ATTK2, ATTK3
    };

    private State state = State.IDLE;

    private int e = 0;
    private int offset = 0;

    public Hero(final float x, final float y){
        this.x = x;
        this.y = y;
          
        sprite = SpriteLoader.getSprite("images/sprites/hero.json");
        sprite.addCallback(new Callback<Sprite>(){
            @Override
            public void onSuccess(Sprite result){
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() /2f,
                                         sprite.height() /2f);
                sprite.layer().setTranslation(x, y +13f);
                hasLoaded = true;


            }

            @Override
            public void onFailure(Throwable cause){
                //PlayN.log().error("Error loading image!", cause);
            }

        });

      

    }
    public Layer layer(){
        return sprite.layer();
    }

    public void update(int delta){
        if(hasLoaded == false) return;

        PlayN.keyboard().setListener(new Keyboard.Adapter(){
            @Override
            public void onKeyDown(Keyboard.Event event){
                if(event.key() == Key.SPACE){
                    switch(state){
                        case IDLE: state = State.RUN; break;
                        case RUN: state = State.ATTK; break;
                        case ATTK: state = State.IDLE; break;
                    }
                }
                if(event.key() == Key.A){
                    x -= 2/2+20;
                }
                
            }
        });

        e += delta;
        if(e > 150){
            switch(state){
                case IDLE: offset = 0;
                           break;
                case IDLE2: offset = 4;
                           break;
                case IDLE3: offset = 8;
                           break;
                case RUN: offset = 12;
                            break;
                case RUN2: offset = 16;
                            break;
                case RUN3: offset = 20;
                            break;
                case ATTK: offset = 24;
                            break;
                case ATTK2: offset = 26;
                            break;
                case ATTK3: offset = 28;

            }
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            sprite.layer().setTranslation(x, y +13f);
            e=0;
        }
    }
}