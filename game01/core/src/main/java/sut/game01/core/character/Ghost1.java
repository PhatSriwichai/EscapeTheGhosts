package sut.game01.core.character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Layer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.GameScreen;
import sut.game01.core.TestScreen;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

/**
 * Created by GGGCOM on 17/5/2559.
 */
public class Ghost1 {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private float x;
    private float y;
    private Body body;
    private boolean contacted;
    private int contactCheck;
    private Body other;
    private Clock clock;
    private int groupIndex;
    private World world;
    private boolean checkDestroy = false;

    public enum State{
        WALK, ATTK, RWALK, RATTK
    };
    private State state;
    private int e = 0;
    private int offset = 0;
    private char direction;

    public Ghost1(final World world, final float x_px, final float y_px, final int groupIndex, final char direction){
        this.world = world;
        this.groupIndex = groupIndex;
        this.direction = direction;

        if(direction == 'L')        state = State.WALK;
        else if(direction == 'R')   state = State.RWALK;

        sprite = SpriteLoader.getSprite("images/sprites/ghost2.json");
        sprite.addCallback(new Callback<Sprite>(){
            @Override
            public void onSuccess(Sprite result){
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x, y + 13f);
                body = initPhysicsBody(world, x_px,
                         y_px);

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
        e += delta;
        if(checkDestroy == true){
            sprite.layer().setVisible(false);
            world.destroyBody(body);
            checkDestroy = false;
        }
        if(e > 150){
            switch(state) {
                case WALK: offset = 0; break;
                case ATTK: offset = 4;
                            if(spriteIndex == 6){
                                state = State.WALK;
                            }
                            break;
                case RWALK: offset = 8; break;
                case RATTK: offset = 12;
                    if(spriteIndex == 15){
                        state = State.RWALK;
                    }
                    break;

            }
            //body.applyForce(new Vec2(10.0f, 0.0f), body.getPosition());
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            //sprite.layer().setTranslation(body.getPosition().x / GameScreen.M_PER_PIXEL,
            //        body.getPosition().y / GameScreen.M_PER_PIXEL);
            e=0;
        }

    }

    public void paint(Clock clock){
        this.clock = clock;
        if(!hasLoaded) return;

        //sprite.layer().setRotation(body.getAngle());

        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL - 7),
                body.getPosition().y / GameScreen.M_PER_PIXEL);
        walk(direction);

    }


    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(90 * TestScreen.M_PER_PIXEL/2,
                sprite.layer().height()*TestScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.2f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        fixtureDef.filter.groupIndex = groupIndex;

        body.createFixture(fixtureDef);

        //body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);

        return body;
    }

    public Body getBody(){
        return this.body;
    }

    public void contact(Contact contact, String c){
        contacted = true;
        contactCheck = 0;
        if(c == "Bomb"){
            if(direction == 'L') body.applyLinearImpulse(new Vec2(100f, 50f), body.getPosition());
            else if(direction == 'R') body.applyLinearImpulse(new Vec2(-100f, 50f), body.getPosition());
            checkDestroy = true;
        }
        if(c == "Hero"){
            if(direction == 'L') state = State.ATTK;
            else if(direction == 'R') state = State.RATTK;
        }
        if(contact.getFixtureA().getBody()==body){
            other = contact.getFixtureB().getBody();
        }else{
            other = contact.getFixtureA().getBody();
        }
    }

    public void walk(char direction){
        if(direction == 'L') body.applyForce(new Vec2(-30.0f, 0.0f), body.getPosition());
        else if(direction == 'R') body.applyForce(new Vec2(+30.0f, 0.0f), body.getPosition());

    }
}
