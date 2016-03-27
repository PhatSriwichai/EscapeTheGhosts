package sut.game01.core;

import static playn.core.PlayN.*;

import react.UnitSlot;

import playn.core.*;
import playn.core.Image;
import playn.core.ImageLayer;
import tripleplay.game.*;

public class HomeScreen extends UIScreen {

  private final TestScreen testScreen;
  private final ScreenStack ss;
  
  private Image bgImage;
  private ImageLayer bgLayer;
  private Image startButton;
  private ImageLayer startLayer;

  

  public HomeScreen(final ScreenStack ss){
    this.ss = ss;
    testScreen = new TestScreen(ss);

    bgImage = assets().getImage("images/homeBackground3.png");
    bgLayer = graphics().createImageLayer(bgImage);
    //graphics().rootLayer().add(bgLayer);

    startButton = assets().getImage("images/startButton.png");
    startLayer = graphics().createImageLayer(startButton);
    //graphics().rootLayer().add(startLayer);
    startLayer.setTranslation(220,140);


      startLayer.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.push(testScreen);
      }
    });
      
  }

  @Override
  public void wasShown() {
      super.wasShown();

      this.layer.add(bgLayer);
      this.layer.add(startLayer);

      
  }
}
