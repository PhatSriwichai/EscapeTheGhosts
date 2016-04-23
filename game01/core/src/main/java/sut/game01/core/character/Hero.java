package sut.game01.core.character;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

public class Hero{
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private float x;
    private float y;

    public enum State{
        IDLE, IDLE2, IDLE3, RUN, RUN2, RUN3, ATTK, ATTK2, ATTK3,
        RIDLE, RIDLE2, RIDLE3, RRUN, RRUN2, RRUN3, RATTK, RATTK2, RATTK3
    };

    public enum Direction{
        LEFT, RIGHT
    };

    private State state = State.IDLE;
    private Direction direction = Direction.RIGHT;

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
                if(event.key() == Key.A){
                   direction = Direction.LEFT;
                    switch(state){
                        case RIDLE: state = State.IDLE; break;
                        case RRUN: state = State.RUN; break;
                        case RATTK: state = State.ATTK; break;
                    }
                }
                if(event.key() == Key.D){
                    direction = Direction.RIGHT;
                    switch(state){
                        case IDLE: state = State.RIDLE; break;
                        case RUN: state = State.RRUN; break;
                        case ATTK: state = State.RATTK; break;
                    }
                }
                if(event.key() == Key.SPACE){
                    spriteIndex = 0;
                    switch(state){
                        case IDLE: state = State.RUN; break;
                        case RUN: state = State.ATTK; break;
                        case ATTK: state = State.IDLE; break;
                        case RIDLE: state = State.RRUN; break;
                        case RRUN: state = State.RATTK; break;
                        case RATTK: state = State.RIDLE; break;
                    }

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
                            if(spriteIndex == 26)
                                state = State.IDLE;
                            break;
                case ATTK2: offset = 28;
                            break;
                case ATTK3: offset = 32;
                            break;
                case RIDLE: offset = 36;
                            break;
                case RIDLE2: offset = 40;
                            break;
                case RIDLE3: offset = 44;
                            break;
                case RRUN: offset = 48;
                            break;
                case RRUN2: offset = 52;
                            break;
                case RRUN3: offset = 56;
                            break;
                case RATTK: offset = 60;
                            if(spriteIndex == 62)
                                state = State.RIDLE;
                            break;
                case RATTK2: offset = 64;
                            break;
                case RATTK3: offset = 68;

            }
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            sprite.layer().setTranslation(x, y +13f);
            e=0;
        }
    }
}