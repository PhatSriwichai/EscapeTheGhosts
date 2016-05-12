package sut.game01.core;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class HomeScreen extends UIScreen {

  private final GameScreen gameScreen;
  private final ScreenStack ss;
  private final TestScreen test;
  
  private Image bgImage;
  private ImageLayer bgLayer;
  private Image startButton;
  private ImageLayer startLayer;

  private Image optionButton;
  private ImageLayer optionLayer;

  private Image creditButton;
  private ImageLayer creditLayer;


  

  public HomeScreen(final ScreenStack ss){
    this.ss = ss;
    test = new TestScreen(ss);
    gameScreen = new GameScreen(ss);

    bgImage = assets().getImage("images/homeBackground3.png");
    bgLayer = graphics().createImageLayer(bgImage);
    //graphics().rootLayer().add(bgLayer);

    startButton = assets().getImage("images/playGameButton.png");
    startLayer = graphics().createImageLayer(startButton);
    //graphics().rootLayer().add(startLayer);
    startLayer.setTranslation(245,140);

    optionButton = assets().getImage("images/optionButton.png");
    optionLayer = graphics().createImageLayer(optionButton);
    //graphics().rootLayer().add(startLayer);
    optionLayer.setTranslation(245,220);

    creditButton = assets().getImage("images/creditButton.png");
    creditLayer = graphics().createImageLayer(creditButton);
    //graphics().rootLayer().add(startLayer);
    creditLayer.setTranslation(245,300);


      startLayer.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event){
        ss.push(gameScreen);
      }
    });

      
}

  @Override
  public void wasShown() {
      super.wasShown();

      this.layer.add(bgLayer);
      this.layer.add(startLayer);
      this.layer.add(optionLayer);
      this.layer.add(creditLayer);
  }
}

