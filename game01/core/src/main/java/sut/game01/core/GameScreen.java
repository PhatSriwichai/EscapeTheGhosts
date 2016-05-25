package sut.game01.core;


import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.character.Bomb;
import sut.game01.core.character.Ghost1;
import sut.game01.core.character.Hero;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameScreen extends Screen {
    private ScreenStack ss;
    private Image bgImage;
    private ImageLayer bgLayer;
    private Image backButton;
    private ImageLayer backLayer;
    private Image heartImage;
    private ImageLayer heart;
    private ImageLayer heart2;
    private ImageLayer heart3;
    private Image heroProfileImage;
    private ImageLayer heroProfile;
    //private Layer layer1 = new GameScreen().layer;

    private GroupLayer groupBomb = graphics().createGroupLayer();
    private GroupLayer ghostLayer1 = graphics().createGroupLayer();
    private GroupLayer ghostLayer2 = graphics().createGroupLayer();
    private Hero hero;
    private Ghost1 ghost1;
    private Ghost1 ghost2;

    public static float M_PER_PIXEL = 1/26.666667f;
    private static int width = 24;
    private static int height = 18;
    private boolean showDebugDraw = true;
    private DebugDrawBox2D debugDraw;

    private static World world;
    private HashMap<Object, String> bodies;
    private List<Hero> heroMap;
    private List<Ghost1> ghostList1;
    private static List<Bomb> bombList;

    private int i = 0;
    private int e=0;
    private int g = 0;
    private int core = 0;
    private int heartCount = 3;
    private int hCount = 0;
    private Bomb bomb;




    public GameScreen(final ScreenStack ss){
        //===============================================================
        this.ss = ss;
        Vec2 gravity = new Vec2(0.0f,100.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        bodies = new HashMap<Object, String>();
        heroMap = new ArrayList<Hero>();
        bombList = new ArrayList<Bomb>();
        ghostList1 = new ArrayList<Ghost1>();



        bgImage = assets().getImage("images/background/bg1.png");
        bgLayer = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bgLayer);

        backButton = assets().getImage("images/backButton.png");
        backLayer = graphics().createImageLayer(backButton);
        //graphics().rootLayer().add(backLayer);
        backLayer.setTranslation(500,10);

        heartImage = assets().getImage("images/item/heart.png");
        heart = graphics().createImageLayer(heartImage);
        heart2 = graphics().createImageLayer(heartImage);
        heart3 = graphics().createImageLayer(heartImage);
        heart.setTranslation(80,25);
        heart2.setTranslation(130,25);
        heart3.setTranslation(180,25);

        heroProfileImage = assets().getImage("images/item/hero.png");
        heroProfile = graphics().createImageLayer(heroProfileImage);
        heroProfile.setTranslation(5, 10);

        backLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
            }
        });

        //==============================================================

        hero = new Hero(world,100f,100f);
        ghost1 = new Ghost1(world,400f,100f, -2);
        ghost2 = new Ghost1(world,500f,100f, -2);
        bodies.put(hero.getBody(), "hero_1");

        //ghostList1.add(new Ghost1(world,500f,100f));


        //bombList.add(new Bomb(world, 200f, 300f));
       // bombList.add(new Bomb(world, 300f, 300f));
        //bombList.add(new Bomb(world, 400f, 300f));


    }
    public GameScreen(){

    }

    @Override
    public void wasShown() {
        super.wasShown();
        //this.layer.add(bgLayer);
        this.layer.add(backLayer);
        this.layer.add(heroProfile);
        this.layer.add(heart);
        this.layer.add(heart2);
        this.layer.add(heart3);


        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0,height-2), new Vec2(width, height-2));
        ground.createFixture(groundShape, 0.0f);

        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width / M_PER_PIXEL),
                    (int)(height / M_PER_PIXEL)
            );
            layer.add(graphics().createImageLayer(image));
            debugDraw = new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFlags(DebugDraw.e_shapeBit |
                    DebugDraw.e_jointBit |
                    DebugDraw.e_aabbBit);
            debugDraw.setCamera(0, 0, 1f / M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                if(a == hero.getBody()|| b == hero.getBody()){
                    //hero.contact(contact);
                }
                    for(Bomb bomb: bombList) {
                        if ((a == bomb.getBody() && b == ghost1.getBody()) || (b == bomb.getBody() && a == ghost1.getBody())) {
                            ghost1.contact(contact, "Bomb");
                        }
                    }
                    if((a == hero.getBody()&& b == ghost1.getBody()) || (b == hero.getBody()&& a == ghost1.getBody())){
                        ghost1.contact(contact, "Hero");
                        hero.contact(contact);
                        heartCount--;
                        checkHeart(heartCount);
                    }
                    for(Bomb bomb: bombList) {
                        if ((a == bomb.getBody() && b == ghost2.getBody()) || (b == bomb.getBody() && a == ghost2.getBody())) {
                            ghost2.contact(contact, "Bomb");
                        }
                    }
                if((a == hero.getBody()&& b == ghost2.getBody()) || (b == hero.getBody()&& a == ghost2.getBody())){
                    ghost2.contact(contact, "Hero");
                }


                for(Bomb bomb: bombList){
                    if(a==bomb.getBody()|| b == bomb.getBody() ){
                        //bomb.contact(contact, hero);
                        core++;
                        //bomb.layer().setVisible(false);
                    }
                }

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                if((a == hero.getBody()&& b == ghost1.getBody()) || (b == hero.getBody()&& a == ghost1.getBody())){
                    hCount++;
                    if(hCount > 1000){
                        ghost1.contact(contact, "Hero");
                        hero.contact(contact);
                        heartCount--;
                        checkHeart(heartCount);
                        hCount = 0;
                    }

                }

            }
        });


        this.layer.add(hero.layer());
        ghostLayer1.add(ghost1.layer());
        ghostLayer2.add(ghost2.layer());
        this.layer.add(ghostLayer1);
        this.layer.add(ghostLayer2);
        this.layer.add(groupBomb);
        for(Bomb b: bombList){
            //this.layer.add(b.layer());
        }
    }

    @Override
    public void update(int delta){
        super.update(delta);
        hero.update(delta);
        ghost1.update(delta);
        ghost2.update(delta);


        for(Bomb b: bombList){
            b.update(delta);
        }

        for(Bomb b: bombList){
            //this.layer.add(b.layer());
            groupBomb.add(b.layer());
        }

        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        hero.paint(clock);
        ghost1.paint(clock);
        ghost2.paint(clock);

        for(Bomb b: bombList){
            b.paint(clock);
        }
        //for(Hero h: heroMap){
        //System.out.println("paint");
        // h.paint(clock);
        //}
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();

            debugDraw.getCanvas().setFillColor(Color.rgb(255,255,255));
            debugDraw.getCanvas().drawText("Score = "+String.valueOf(core),100,100);
        }


    }
    public void addBomb(Bomb b){
        bombList.add(b);
    }

    public void checkHeart(int count){
        switch(count){
            case 2: heart3.setVisible(false); break;
            case 1: heart2.setVisible(false); break;
            case 0: heart.setVisible(false);
                ss.push(new GameOverScreen(ss));
                break;
        }

    }
}
