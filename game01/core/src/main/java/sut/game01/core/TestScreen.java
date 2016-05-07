package sut.game01.core;



import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import sut.game01.core.character.Hero;
import tripleplay.entity.World;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

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

    private World world;


 

  public TestScreen(final ScreenStack ss){
  	//===============================================================
    this.ss = ss;

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

     hero = new Hero(560f, 400f);

  
  }

  @Override
  public void wasShown() {
      super.wasShown();
      this.layer.add(bgLayer);
      this.layer.add(backLayer);

     
      this.layer.add(hero.layer());
  }

    @Override
  public void update(int delta){
  		super.update(delta);
  		hero.update(delta);
  }

  
}
