package sut.game01.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by GGGCOM on 29/5/2559.
 */
public class GameWinScreen extends Screen {
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
    private Image settingImage;
    private ImageLayer setting;
    private Image mainImage;
    private ImageLayer main;
    private Image totalImage;
    private ImageLayer total;
    private Image nextImage;
    private ImageLayer next;
    private int totalKill = 0;
    private int screenIndex;

    public GameWinScreen(final ScreenStack ss, final int screenIndex){
        this.ss = ss;
        this.screenIndex = screenIndex;
        bgImage = assets().getImage("images/background/bg1.png");
        bg = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bg);

        gameWin = assets().getImage("images/winZone/winZone.png");
        winTem = graphics().createImageLayer(gameWin);
        winTem.setTranslation(60f,20f);
        resumeImage = assets().getImage("images/pauseZone/resumeButton.png");
        resume = graphics().createImageLayer(resumeImage);
        resume.setTranslation(90f, 260f);
        settingImage = assets().getImage("images/pauseZone/settingButton.png");
        setting = graphics().createImageLayer(settingImage);
        setting.setTranslation(250f, 260f);
        mainImage = assets().getImage("images/pauseZone/mainButtonSmall.png");
        main = graphics().createImageLayer(mainImage);
        main.setTranslation(550f, 430f);
        nextImage = assets().getImage("images/pauseZone/nextButton.png");
        next = graphics().createImageLayer(nextImage);
        next.setTranslation(410f, 260f);


        totalImage = assets().getImage("images/winZone/total.png");
        total = graphics().createImageLayer(totalImage);
        total.setTranslation(80f,130f);

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
                switch (screenIndex){
                    case 1: ss.push(new GameScreen(ss));
                    case 2: ss.push(new GameScreen2(ss));
                    case 3: ss.push(new GameScreen3(ss));
                }
            }
        });

        //
        main.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new HomeScreen(ss));
            }
        });
        next.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                switch (screenIndex){
                    case 1: ss.push(new GameScreen2(ss));
                    case 2: ss.push(new GameScreen3(ss));
                }

            }
        });


        try{
            //Create object of FileReader
            //FileReader inputFile = new FileReader("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");
            File file = new File("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");
            File file2 = new File("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/totalKill.txt");
            if (!file2.exists()) {
                file2.createNewFile();
            }
            //Instantiate the BufferedReader Class
            //BufferedReader bufferReader = new BufferedReader(inputFile);
            FileInputStream inputFile = new FileInputStream(file);
            List<Integer> line = new ArrayList();
            //Variable to hold the one line data


            // Read file line by line and print on the console
            while (inputFile.read() != -1)   {
                    line.add(inputFile.read());
            }
            for(int l: line){
                if(l != 255 && l != -1){
                    totalKill += l;
                }
            }

            FileOutputStream output = new FileOutputStream(file);
            //Writer w = new OutputStreamWriter(output, "UTF-8");
            output.write(totalKill);



            //Close the buffer reader

        }catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }

    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(winTem);
        this.layer.add(resume);
        this.layer.add(setting);
        this.layer.add(main);
        this.layer.add(total);
        this.layer.add(next);

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

        numList.get(back).setTranslation(415f,150f);
        numList2.get(mid).setTranslation(345,150f);
        numList3.get(front).setTranslation(275,150f);

        numList.get(back).setVisible(true);
        numList2.get(mid).setVisible(true);
        numList3.get(front).setVisible(true);

    }

}
