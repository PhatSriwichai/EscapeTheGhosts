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

    public enum State{
        WALK, ATTK
    };
    private State state = State.WALK;
    private int e = 0;
    private int offset = 0;

    public Ghost1(final World world, final float x_px, final float y_px){
        sprite = SpriteLoader.getSprite("images/sprites/ghost2.json");
        sprite.addCallback(new Callback<Sprite>(){
            @Override
            public void onSuccess(Sprite result){
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x, y + 13f);
                body = initPhysicsBody(world, GameScreen.M_PER_PIXEL * x_px,
                        GameScreen.M_PER_PIXEL * y_px);

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
        if(e > 150){
            switch(state) {
                case WALK: offset = 0; break;
                case ATTK: offset = 4;
                            if(spriteIndex == 6){
                                state = State.WALK;
                            }
                            break;
            }
            //body.applyForce(new Vec2(10.0f, 0.0f), body.getPosition());
            spriteIndex = offset + ((spriteIndex + 1)%4);
            sprite.setSprite(spriteIndex);
            sprite.layer().setTranslation(body.getPosition().x / GameScreen.M_PER_PIXEL,
                    body.getPosition().y / GameScreen.M_PER_PIXEL);
            e=0;
        }
        body.applyForce(new Vec2(-70.0f, 0.0f), body.getPosition());
    }

    public void paint(Clock clock){
        this.clock = clock;
        if(!hasLoaded) return;

        //sprite.layer().setRotation(body.getAngle());

        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL - 7),
                body.getPosition().y / GameScreen.M_PER_PIXEL);

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
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
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
            body.applyLinearImpulse(new Vec2(100f, 50f), body.getPosition());
        }
        if(c == "Hero"){
            state = State.ATTK;
        }
        if(contact.getFixtureA().getBody()==body){
            other = contact.getFixtureB().getBody();
        }else{
            other = contact.getFixtureA().getBody();
        }
    }
}
