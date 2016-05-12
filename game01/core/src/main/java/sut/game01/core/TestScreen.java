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
import sut.game01.core.character.Hero;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class TestScreen extends Screen {
  private final ScreenStack ss;
  private Image bgImage;
  private ImageLayer bgLayer;
  private Image backButton;
  private ImageLayer backLayer;
  private Hero hero;

    public static float M_PER_PIXEL = 1/26.666667f;
    private static int width = 24;
    private static int height = 18;
    private boolean showDebugDraw = true;
    private DebugDrawBox2D debugDraw;

    private World world;
    private HashMap<Object, String> bodies;
    private List<Hero> heroMap;
    private List<Bomb> bombList;

    private int i = 0;
    private int core = 0;
    private Bomb bomb;


 

  public TestScreen(final ScreenStack ss){
  	//===============================================================
    this.ss = ss;
      Vec2 gravity = new Vec2(0.0f,100.0f);
      world = new World(gravity);
      world.setWarmStarting(true);
      world.setAutoClearForces(true);

      bodies = new HashMap<Object, String>();
      heroMap = new ArrayList<Hero>();
      bombList = new ArrayList<Bomb>();


    bgImage = assets().getImage("images/bg.png");
    bgLayer = graphics().createImageLayer(bgImage);
    //graphics().rootLayer().add(bgLayer);

    backButton = assets().getImage("images/backButton.png");
    backLayer = graphics().createImageLayer(backButton);
    //graphics().rootLayer().add(backLayer);
    backLayer.setTranslation(10,10);

    backLayer.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.remove(ss.top());
      }
    });
    //==============================================================

      hero = new Hero(world,150f,150f);
      bodies.put(hero.getBody(), "hero_1");
      bombList.add(new Bomb(world, 200f, 300f));
      bombList.add(new Bomb(world, 300f, 300f));
      bombList.add(new Bomb(world, 400f, 300f));


  }

  @Override
  public void wasShown() {
      super.wasShown();
      //this.layer.add(bgLayer);
      this.layer.add(backLayer);

      Body ground = world.createBody(new BodyDef());
      EdgeShape groundShape = new EdgeShape();
      groundShape.set(new Vec2(0,height), new Vec2(width, height));
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

              if(contact.getFixtureA().getBody() == hero.getBody()||
                      contact.getFixtureB().getBody() == hero.getBody()){
                  hero.contact(contact);
              }
              for(Bomb bomb: bombList){
                  if(contact.getFixtureA().getBody()==bomb.getBody()||
                          contact.getFixtureB().getBody() == bomb.getBody()){
                      bomb.contact(contact);
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

          }
      });


      this.layer.add(hero.layer());
      for(Bomb b: bombList){
         this.layer.add(b.layer());
      }


  }

    @Override
    public void update(int delta){
  		super.update(delta);
  		hero.update(delta);
        for(Bomb b: bombList){
            b.update(delta);
        }

        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        hero.paint(clock);

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
}
