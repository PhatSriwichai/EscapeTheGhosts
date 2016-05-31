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
public class GameEndScreen extends Screen {
    private final ScreenStack ss;

    private Image bgImage;
    private ImageLayer bg;

    private Image gameWin;
    private ImageLayer winTem;
    private Image mainImage;
    private ImageLayer main;
    private int totalKill = 0;
    private int scoreKill = 0;

    public GameEndScreen(final ScreenStack ss){
        this.ss = ss;
        bgImage = assets().getImage("images/background/endBg.png");
        bg = graphics().createImageLayer(bgImage);
        graphics().rootLayer().add(bg);

        gameWin = assets().getImage("images/gameEnd.png");
        winTem = graphics().createImageLayer(gameWin);
        winTem.setTranslation(0f,0f);

        mainImage = assets().getImage("images/button/mainButtonSmall.png");
        main = graphics().createImageLayer(mainImage);
        main.setTranslation(550f, 430f);




        //
        main.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event) {
                ss.push(new HomeScreen(ss));
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
            FileInputStream inputFile2 = new FileInputStream(file2);

            scoreKill = inputFile.read();
            totalKill = inputFile2.read();
            //Variable to hold the one line data

            inputFile.close();
            inputFile2.close();
            // Read file line by line and print on the console
            //while (inputFile.read() != -1)   {
            //}
            //for(int l: line){
            //if(l != 255 && l != -1){

            //}
            //}
            System.out.print("total = ");
            System.out.println(scoreKill+totalKill);
            FileOutputStream output = new FileOutputStream(file2);
            //Writer w = new OutputStreamWriter(output, "UTF-8");
            output.write(totalKill+scoreKill);
            output.close();



            //Close the buffer reader

        }catch(Exception e){
            System.out.println("Error while reading file line by line:" + e.getMessage());
        }


    }

    @Override
    public void wasShown() {
        super.wasShown();
        this.layer.add(winTem);
        this.layer.add(main);
    }


}
