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

import java.util.*;

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
    private Image pauseImage;
    private ImageLayer pause;
    //private Layer layer1 = new GameScreen().layer;

    private GroupLayer groupBomb = graphics().createGroupLayer();
    private GroupLayer ghostLayer1 = graphics().createGroupLayer();
    private GroupLayer ghostLayer2 = graphics().createGroupLayer();
    private Hero hero;
    private Ghost1 ghost1;
    private Ghost1 ghost2;
    private GamePauseScreen gamePause;

    public static float M_PER_PIXEL = 1/26.666667f;
    private static int width = 24;
    private static int height = 18;
    private boolean showDebugDraw = true;
    private DebugDrawBox2D debugDraw;

    private static World world;
    private HashMap<Object, String> bodies;
    private HashMap<Bomb, String> bombHash;
    private List<Hero> heroMap;
    private List<Ghost1> ghostList1;
    private static List<Bomb> bombList;

    private Queue que;

    private int i = 0;
    private int e=0;
    private int g = 0;
    private int core = 0;
    private int heartCount = 3;
    private int hCount = 0;
    private Bomb bomb;
    private int bombIndex = 0;
    private int ghostTime = 0;


    public GameScreen(final ScreenStack ss){
        //===============================================================
        this.ss = ss;
        Vec2 gravity = new Vec2(0.0f,100.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        que = new LinkedList();

        bodies = new HashMap<Object, String>();
        bombHash = new HashMap<Bomb, String>();
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
        heart.setTranslation(85,0);
        heart2.setTranslation(140,0);
        heart3.setTranslation(195,0);

        heroProfileImage = assets().getImage("images/item/hero1.png");
        heroProfile = graphics().createImageLayer(heroProfileImage);
        heroProfile.setTranslation(5, 20);

        pauseImage = assets().getImage("images/button/pause.png");
        pause = graphics().createImageLayer(pauseImage);
        pause.setTranslation(300f,10f);

        backLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
            }
        });

        gamePause = new GamePauseScreen(ss, bgImage);

        pause.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(gamePause);
            }
        });

        //==============================================================
        hero = new Hero(world,100f,100f);
        bodies.put(hero.getBody(), "hero_1");


    }
    public GameScreen(){ }

    @Override
    public void wasShown() {
        super.wasShown();
        //this.layer.add(bgLayer);
        this.layer.add(backLayer);
        this.layer.add(heroProfile);
        this.layer.add(heart);
        this.layer.add(heart2);
        this.layer.add(heart3);
        this.layer.add(pause);


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
                    for(Ghost1 g:ghostList1){
                        if((a == hero.getBody()&& b == g.getBody()) || (b == hero.getBody()&& a == g.getBody())){
                            g.contact(contact, "Hero");
                            hero.contact(contact);
                            heartCount--;
                            checkHeart(heartCount);
                        }
                        for(Bomb bomb: bombList) {
                            if ((a == bomb.getBody() && b == g.getBody()) || (b == bomb.getBody() && a == g.getBody())) {
                                g.contact(contact, "Bomb");
                            }
                        }
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

                for(Ghost1 g:ghostList1){
                    if((a == hero.getBody()&& b == g.getBody()) || (b == hero.getBody()&& a == g.getBody())){
                        hCount++;
                        if(hCount > 1000){
                            g.contact(contact, "Hero");
                            hero.contact(contact);
                            heartCount--;
                            checkHeart(heartCount);
                            hCount = 0;
                        }

                    }
                }


            }
        });


        this.layer.add(hero.layer());
        ghostLayer1.add(ghost1.layer());
        ghostLayer2.add(ghost2.layer());
        for(Ghost1 g:ghostList1){
            this.layer.add(g.layer());
        }
        for(Bomb b: bombList){
            //this.layer.add(b.layer());
        }
    }

    @Override
    public void update(int delta){
        super.update(delta);
        hero.update(delta);
        //ghost1.update(delta);
        //ghost2.update(delta);

        for(Ghost1 g:ghostList1){
            g.update(delta);
            this.layer.add(g.layer());
        }


        for(Bomb b: bombList){
            b.update(delta);
        }

        for(Bomb b: bombList){
            //this.layer.add(b.layer());
            this.layer.add(b.layer());
        }
        ghostTime++;
        if(ghostTime > 300){
            ghostList1.add(new Ghost1(world,400f,400f, -2));
            ghostTime = 0;
        }
        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        hero.paint(clock);
        for(Ghost1 g:ghostList1){
            g.paint(clock);
        }

        for(Bomb b: bombList){
            b.paint(clock);
        }

        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();

            debugDraw.getCanvas().setFillColor(Color.rgb(255,255,255));
            debugDraw.getCanvas().drawText("Score = "+String.valueOf(core),100,100);
        }


    }
    public void addBomb(Bomb b){
       bombList.add(b);
        //que.add(b);
    }

    public void removeBomb(){
        //world.destroyBody(bombList.get(bombIndex++).getBody());
        //bombList.remove(bombList.get(0));

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
