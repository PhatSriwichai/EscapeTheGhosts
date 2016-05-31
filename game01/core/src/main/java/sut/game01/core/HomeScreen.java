package sut.game01.core;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class HomeScreen extends Screen {

  private final LevelScreen levelScreen;
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
  public static boolean showDebugDraw = false;


  

  public HomeScreen(final ScreenStack ss){
    this.ss = ss;
    test = new TestScreen(ss);
    levelScreen = new LevelScreen(ss);

    bgImage = assets().getImage("images/homeBackground3.png");
    bgLayer = graphics().createImageLayer(bgImage);
    //graphics().rootLayer().add(bgLayer);

    startButton = assets().getImage("images/playGameButton.png");
    startLayer = graphics().createImageLayer(startButton);
    //graphics().rootLayer().add(startLayer);
    startLayer.setTranslation(245,140);

    optionButton = assets().getImage("images/button/score.png");
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
        ss.push(levelScreen);
      }
    });
    optionLayer.addListener(new Mouse.LayerAdapter(){
      @Override
      public void onMouseUp(Mouse.ButtonEvent event) {
        ss.push(new ResetScreen(ss));
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

  public void resetScore(){
    try{
      //Create object of FileReader
      //FileReader inputFile = new FileReader("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");
      //File file = new File("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");
      File file2 = new File("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/totalKill.txt");
      if (!file2.exists()) {
        file2.createNewFile();
      }

      FileOutputStream output = new FileOutputStream(file2);
      //Writer w = new OutputStreamWriter(output, "UTF-8");
      output.write(0);
      output.close();

      ss.push(new GameWinScreen(ss, 0));


      //Close the buffer reader

    }catch(Exception e){
      System.out.println("Error while reading file line by line:" + e.getMessage());
    }
  }
}

