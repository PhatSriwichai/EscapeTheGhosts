package sut.game01.core.character;


import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Layer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.TestScreen;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;

public class Bomb {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private float x;
    private float y;
    private Body body;
    private boolean contacted;
    private int contactCheck;
    private Body other;
    private char direction;
    private int checkBoom = 0;


    public enum State{
        IDLE, BOOM
    };
    private State state = State.IDLE;
    private int offset = 0;
    private int e = 0;

    public Bomb(final World world, final float x_px, final float y_px, final char direction){
        this.x = x_px;
        this.y = y_px;
        this.direction = direction;

        sprite = SpriteLoader.getSprite("images/sprites/bomb.json");
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

    public void update(int delta) {
        if (hasLoaded == false) return;
        checkBoom += delta;
        e += delta;
        if (e > 150) {
            switch (state) {
                case IDLE: offset = 0;
                    if(checkBoom >= 450){
                        state = State.BOOM;
                        checkBoom = 0;
                    }
                    break;
                case BOOM: offset = 6;
                    if(spriteIndex == 11){
                        state = State.IDLE;
                        sprite.layer().setVisible(false);
                        body.setActive(false);
                    }
                    break;
            }
            spriteIndex = offset + ((spriteIndex + 1) % 6);
            sprite.setSprite(spriteIndex);
            sprite.layer().setTranslation(body.getPosition().x / TestScreen.M_PER_PIXEL,
                    body.getPosition().y / TestScreen.M_PER_PIXEL);
            e = 0;
        }
    }

    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(x, y);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 * TestScreen.M_PER_PIXEL/2,
                50*TestScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.1f;
        fixtureDef.friction = 0.8f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        //body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);
        if(direction == 'L')
            body.applyForce(new Vec2(500f,0f), body.getPosition());
        else if(direction == 'R')
            body.applyForce(new Vec2(-500f,0f), body.getPosition());
        return body;
    }

    public void paint(Clock clock){
        if(!hasLoaded) return;

        //sprite.layer().setRotation(body.getAngle());

        sprite.layer().setTranslation(
                (body.getPosition().x / TestScreen.M_PER_PIXEL),
                body.getPosition().y / TestScreen.M_PER_PIXEL);

    }

    public Body getBody(){
        return this.body;
    }

    public void contact(Contact contact, Hero hero){
        contacted = true;
        contactCheck = 0;

        if(contact.getFixtureA().getBody()==body){
            other = contact.getFixtureB().getBody();
        }else{
            other = contact.getFixtureA().getBody();
        }
    }

    public void force(){
        body.applyForce(new Vec2(10f,0f), body.getPosition());
    }

}
