package sut.game01.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by GGGCOM on 29/5/2559.
 */
public class ResetScreen extends Screen {
    private final ScreenStack ss;

    private List<ImageLayer> numList;
    private List<ImageLayer> numList2;
    private List<ImageLayer> numList3;
    private Image number;

    private Image bgImage;
    private ImageLayer bg;

    private Image gameWin;
    private ImageLayer winTem;
    private Image resumeImage;
    private ImageLayer resume;
    private Image resetImage;
    private ImageLayer reset;
    private int totalKill = 0;
    private int scoreKill = 0;
    private int screenIndex;
    private boolean checkShow = false;

    public ResetScreen(final ScreenStack ss){
        this.ss = ss;
        bgImage = assets().getImage("images/homeBackground3.png");
        bg = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bg);

        gameWin = assets().getImage("images/levelSelect/yourScore.png");
        winTem = graphics().createImageLayer(gameWin);
        winTem.setTranslation(60f,20f);
        resumeImage = assets().getImage("images/button/back.png");
        resume = graphics().createImageLayer(resumeImage);
        resume.setTranslation(90f, 260f);
        resetImage = assets().getImage("images/button/reset.png");
        reset = graphics().createImageLayer(resetImage);
        reset.setTranslation(300f, 260f);

        numList = new ArrayList<ImageLayer>();
        numList2 = new ArrayList<ImageLayer>();
        numList3 = new ArrayList<ImageLayer>();

        number = assets().getImage("images/number/big/zero.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/one.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/two.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/three.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/four.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/five.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/six.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/seven.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/eight.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/big/nine.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        numList3.add(graphics().createImageLayer(number));


        resume.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
               ss.remove(ss.top());
            }
        });

        //
        reset.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                resetScore();
                //checkNumber(totalKill);
                ss.remove(ss.top());
            }
        });

        try{
            //Create object of FileReader
            //FileReader inputFile = new FileReader("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");
            //File file = new File("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");
            File file2 = new File("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/totalKill.txt");
            if (!file2.exists()) {
                file2.createNewFile();
            }
            FileInputStream inputFile2 = new FileInputStream(file2);
            totalKill = inputFile2.read();
            inputFile2.close();
            checkNumber(totalKill);

        }catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }




    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(winTem);
        this.layer.add(resume);
        this.layer.add(reset);

        for(ImageLayer l: numList) {
            this.layer.add(l);
            l.setVisible(false);
        }
        for(ImageLayer l: numList2)  {
            this.layer.add(l);
            l.setVisible(false);
        }
        for(ImageLayer l: numList3)  {
            this.layer.add(l);
            l.setVisible(false);
        }
        checkNumber(totalKill);

        //checkNumber(totalKill+scoreKill);
    }

    public void checkNumber(int num){
        int front, back, mid;
        int cpNum = num;

        front = cpNum/100; cpNum = cpNum%100;
        mid = cpNum/10;
        back = cpNum%10;

        for(ImageLayer l: numList)  l.setVisible(false);
        for(ImageLayer l: numList2)  l.setVisible(false);
        for(ImageLayer l: numList3)  l.setVisible(false);

        numList.get(back).setTranslation(315f,150f);
        numList2.get(mid).setTranslation(245,150f);
        numList3.get(front).setTranslation(175,150f);

        numList.get(back).setVisible(true);
        numList2.get(mid).setVisible(true);
        numList3.get(front).setVisible(true);

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

            //Close the buffer reader

        }catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }
    }

}
