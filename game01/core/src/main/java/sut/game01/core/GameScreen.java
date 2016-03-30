package sut.game01.core;

import static playn.core.PlayN.*;

import playn.core.*;
import playn.core.Image;
import playn.core.ImageLayer;
import tripleplay.game.*;

public class GameScreen extends UIScreen {
  private final ScreenStack ss;
  private Image bgImage;
  private ImageLayer bgLayer;
  private Image backButton;
  private ImageLayer backLayer;

 

  public GameScreen(final ScreenStack ss){
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

   
      
  }

  @Override
  public void wasShown() {
      super.wasShown();
      this.layer.add(bgLayer);
       this.layer.add(backLayer);

  }

  
}
