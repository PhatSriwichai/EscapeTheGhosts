package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.GameScreen;
import sut.game01.core.TestScreen;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

import java.util.ArrayList;
import java.util.List;

public class Hero{
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private float x;
    private float y;
    private Body body;
    private boolean contacted;
    private int contactCheck;
    private Body other;
    private List<Bomb> bombList;
    private World world;
    public GameScreen game = new GameScreen();
    private Clock clock;

    public static enum State{
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

    public Hero(final World world, final float x_px, final float y_px){
    //public Hero(final World world){
        this.world = world;
        this.x = x_px;
        this.y = y_px;
        bombList = new ArrayList<Bomb>();
          
        sprite = SpriteLoader.getSprite("images/sprites/hero.json");
        sprite.addCallback(new Callback<Sprite>(){
            @Override
            public void onSuccess(Sprite result){
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x, y + 13f);
                body = initPhysicsBody(world, TestScreen.M_PER_PIXEL * x_px,
                                       TestScreen.M_PER_PIXEL * y_px);

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
                    //direction = Direction.LEFT;
                    if(state == State.RIDLE || state == State.RIDLE2)
                        state = State.IDLE;
                    else
                        state = State.RUN;
                    body.applyLinearImpulse(new Vec2(-20.0f,0), body.getPosition());
                    sprite.layer().setTranslation(body.getPosition().x / TestScreen.M_PER_PIXEL -10,
                            body.getPosition().y / TestScreen.M_PER_PIXEL);
                }
                if (event.key() == Key.D) {
                    //direction = Direction.RIGHT;
                    if(state == State.IDLE || state == State.IDLE2)
                        state = State.RIDLE;
                    else
                        state = State.RRUN;
                    body.applyLinearImpulse(new Vec2(20.0f,0), body.getPosition());
                }
                if(event.key() == Key.SPACE){
                    //jump();
                    //body.setActive(false);
                    Bomb b;
                    switch(state){
                        case IDLE: state = State.ATTK;
                            b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL -50,body.getPosition().y/GameScreen.M_PER_PIXEL, 'L');
                            game.addBomb(b);
                            break;
                        case IDLE2: state = State.ATTK;
                            b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL -50,body.getPosition().y/GameScreen.M_PER_PIXEL, 'L');
                            game.addBomb(b);
                            break;
                        case RUN: state = State.ATTK;
                            b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL -50,body.getPosition().y/GameScreen.M_PER_PIXEL, 'L');
                            game.addBomb(b);
                            break;
                        case RUN2: state = State.ATTK;
                            b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL -50,body.getPosition().y/GameScreen.M_PER_PIXEL, 'L');
                            game.addBomb(b);
                            break;
                        case RIDLE: state = State.RATTK;
                            b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL +50,body.getPosition().y/GameScreen.M_PER_PIXEL, 'R');
                            game.addBomb(b);
                            break;
                        case RIDLE2: state = State.RATTK;
                            b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL +50,body.getPosition().y/GameScreen.M_PER_PIXEL, 'R');
                            game.addBomb(b);
                            break;
                        case RRUN: state = State.RATTK;
                            b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL +50,body.getPosition().y/GameScreen.M_PER_PIXEL, 'R');
                            game.addBomb(b);
                            break;
                        case RRUN2: state = State.RATTK;
                            b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL +50,body.getPosition().y/GameScreen.M_PER_PIXEL, 'R');
                            game.addBomb(b);
                            break;
                    }

                    //b = new Bomb(world, body.getPosition().x/GameScreen.M_PER_PIXEL +15,body.getPosition().y/GameScreen.M_PER_PIXEL);
                    //game.addBomb(b);

                    //body.setActive(true);

                    //bombList.add(new Bomb(world, body.getPosition().x, body.getPosition().y));
                    //for(Bomb b: bombList){
                        //game.addBomb(b);
                    //}

                }

            }

        });

        d1 += delta;
        checkEyes();

        e += delta;
        if(e > 120){
            switch(state){
                case IDLE: offset = 0;
                           break;
                case IDLE2: offset = 4;
                           break;
                case IDLE3: offset = 8;
                           break;
                case RUN: offset = 12;
                            if(spriteIndex == 15)
                                state = State.IDLE;
                            break;
                case RUN2: offset = 16;
                            if(spriteIndex == 19)
                                state = State.IDLE;
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
                            if(spriteIndex == 51)
                                state = State.RIDLE;
                            break;
                case RRUN2: offset = 52;
                            if(spriteIndex == 55)
                                state = State.RIDLE;
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
            sprite.layer().setTranslation(body.getPosition().x / TestScreen.M_PER_PIXEL,
                    body.getPosition().y / TestScreen.M_PER_PIXEL);
            e=0;
        }
    }

    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(70 * TestScreen.M_PER_PIXEL/2,
                sprite.layer().height()*TestScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        fixtureDef.filter.groupIndex = 1;
        body.createFixture(fixtureDef);

        //body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);

        return body;
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

    public void paint(Clock clock){
        this.clock = clock;
        if(!hasLoaded) return;

        //sprite.layer().setRotation(body.getAngle());

        sprite.layer().setTranslation(
                (body.getPosition().x / TestScreen.M_PER_PIXEL + 13),
                body.getPosition().y / TestScreen.M_PER_PIXEL);

    }

    public void setX(float x){
        this.x = x;
    }
    public float getX(){
        return this.x;
    }
    public void setY(float y){
        this.y = y;
    }
    public float getY(){
        return this.y;
    }

    public Body getBody(){
        return this.body;
    }

    public void contact(Contact contact){
        contacted = true;
        contactCheck = 0;

        if(state == State.ATTK || state == State.ATTK2){
            //state = State.IDLE;
        }
        if(contact.getFixtureA().getBody()==body){
            other = contact.getFixtureB().getBody();
        }else{
            other = contact.getFixtureA().getBody();
        }
    }
    public void jump(){
        body.applyForce(new Vec2(-10f, -6000f), body.getPosition());
    }



}