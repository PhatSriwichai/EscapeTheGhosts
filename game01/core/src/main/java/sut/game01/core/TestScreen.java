package sut.game01.core;


import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.character.Hero;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.*;

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
    //private Map<String, Hero> heroMap;
    private List<Hero> heroMap;
    private int i = 0;

 

  public TestScreen(final ScreenStack ss){
  	//===============================================================
    this.ss = ss;
      Vec2 gravity = new Vec2(0.0f,10.0f);
      world = new World(gravity);
      world.setWarmStarting(true);
      world.setAutoClearForces(true);

      //heroMap = new HashMap<String, Hero>();
      heroMap = new ArrayList<Hero>();


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

     hero = new Hero(world,320f, 200f);

  
  }

  @Override
  public void wasShown() {
      super.wasShown();
      this.layer.add(bgLayer);
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
      mouse().setListener(new Mouse.Adapter(){
          @Override
          public void onMouseUp(Mouse.ButtonEvent event) {
              Hero he = new Hero(world, (float)event.x(), (float)event.y());
              //Hero he = new Hero(world, 100f, 100f);
              //heroMap.put("hero_" + i++, he);
              heroMap.add(he);
          }
      });
      this.layer.add(hero.layer());

      for(Hero h: heroMap){
          System.out.println("add");
          this.layer.add(h.layer());
      }

  }

    @Override
  public void update(int delta){
  		super.update(delta);
  		hero.update(delta);
        for(Hero h: heroMap){
            //System.out.println("update");
            this.layer.add(h.layer());
            h.update(delta);
        }
        world.step(0.033f, 10, 10);
  }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        hero.paint(clock);
        for(Hero h: heroMap){
            //System.out.println("paint");
            h.paint(clock);
        }
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            world.drawDebugData();
        }
    }
}
