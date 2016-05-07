package sut.game01.core.character;

import sut.game01.core.sprite.*;
import playn.core.util.*;
import playn.core.*;
import sut.game01.core.*;
import tripleplay.game.*;
import playn.core.Layer;

public class Zealot{
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;

    public enum State{
        IDLE, RUN, ATTK
    };

    private State state = State.IDLE;

    private int e = 0;
    private int offset = 0;

    public Zealot(final float x, final float y){

          PlayN.keyboard().setListener(new Keyboard.Adapter(){
            @Override
            public void onKeyUp(Keyboard.Event event){
                if(event.key() == Key.SPACE){
                    switch(state){
                        case IDLE: state = State.RUN; break;
                        case RUN: state = State.ATTK; break;
                        case ATTK: state = State.IDLE; break;
                    }
                    
                }
            }
        });
          
        sprite = SpriteLoader.getSprite("images/spriteSheet1.json");
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

        e += delta;
        if(e > 150){
            switch(state){
                case IDLE: offset = 0;
                           break;
                case RUN: offset = 4;
                            break;
                case ATTK: offset = 8;
                            if(spriteIndex == 10){
                                state = State.IDLE;
                            }
            }
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            e=0;
        }
    }
}