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
    private boolean d = false;
    private int d1 = 0;
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

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override
            public void onKeyDown(Keyboard.Event event) {
                if(event.key() == Key.A) {
                    direction = Direction.LEFT;
                    switch (state) {
                        case RIDLE: state = State.IDLE;     break;
                        case RIDLE2:state = State.IDLE2;    break;
                        case RRUN:  state = State.RUN;      break;
                        case RRUN2: state = State.RUN2;     break;
                        case RATTK: state = State.ATTK;     break;
                    }
                    while(event.key() == Key.A)
                        x -= 10;
                }
                if (event.key() == Key.D) {
                    direction = Direction.RIGHT;
                    switch (state) {
                        case IDLE:  state = State.RIDLE;    break;
                        case IDLE2: state = State.RIDLE2;   break;
                        case RUN:   state = State.RRUN;     break;
                        case RUN2:  state = State.RRUN2;    break;
                        case ATTK:  state = State.RATTK;    break;
                    }
                    x += 10;
                }
                if (event.key() == Key.SPACE) {
                    spriteIndex = 0;
                    switch (state) {
                        case IDLE:
                            state = State.RUN;
                            break;
                        case RUN:
                            state = State.ATTK;
                            break;
                        case ATTK:
                            state = State.IDLE;
                            break;
                        case RIDLE:
                            state = State.RRUN;
                            break;
                        case RRUN:
                            state = State.RATTK;
                            break;
                        case RATTK:
                            state = State.RIDLE;
                            break;
                    }

                }
            }
        });

        d1 += delta;
        checkEyes();

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

    private void checkEyes(){
        if(d){
            switch(state) {
                case IDLE2: state = State.IDLE;
                    switch(offset){
                        case 4: offset = 1;
                            break;
                        case 5: offset = 2;
                            break;
                        case 6: offset = 3;
                            break;
                        case 7: offset = 0;
                            break;
                    }
                    break;

                case RIDLE2: state = State.RIDLE;
                    switch(offset){
                        case 40: offset = 39;
                            break;
                        case 41: offset = 36;
                            break;
                        case 42: offset = 37;
                            break;
                        case 43: offset = 38;
                            break;
                    }
                    break;

                case RUN2:
                    state = State.RUN;
                    switch (offset) {
                        case 16: offset = 13;
                            break;
                        case 17: offset = 14;
                            break;
                        case 18: offset = 15;
                            break;
                        case 19: offset = 12;
                            break;
                    }
                    break;

                case RRUN2:
                    state = State.RRUN;
                    switch (offset) {
                        case 52: offset = 49;
                            break;
                        case 53: offset = 50;
                            break;
                        case 54: offset = 51;
                            break;
                        case 55: offset = 48;
                            break;
                    }
                    break;
            }
            d = false;
        }


        if(d1 > 450){
            switch(state){
                case IDLE:
                    state = State.IDLE2;
                    switch (offset) {
                        case 0: offset = 5;
                            break;
                        case 1: offset = 6;
                            break;
                        case 2: offset = 7;
                            break;
                        case 3: offset = 4;
                            break;
                    }
                    break;

                case RIDLE:
                    state = State.RIDLE2;
                    switch (offset) {
                        case 36: offset = 41;
                            break;
                        case 37: offset = 42;
                            break;
                        case 38: offset = 43;
                            break;
                        case 39: offset = 40;
                            break;
                    }
                    break;

                case RUN:
                    state = State.RUN2;
                    switch (offset) {
                        case 12: offset = 17;
                            break;
                        case 13: offset = 18;
                            break;
                        case 14: offset = 19;
                            break;
                        case 15: offset = 16;
                            break;
                    }
                    break;

                case RRUN:
                    state = State.RRUN2;
                    switch (offset) {
                        case 48: offset = 53;
                            break;
                        case 49: offset = 54;
                            break;
                        case 50: offset = 55;
                            break;
                        case 51: offset = 52;
                            break;
                    }
                    break;
            }
            d1 = 0;
            d = true;
        }
    }

}