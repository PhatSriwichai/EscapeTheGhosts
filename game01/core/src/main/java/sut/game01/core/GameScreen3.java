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
import sut.game01.core.character.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.io.*;
import java.util.*;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameScreen3 extends Screen {
    private ScreenStack ss;
    private Image bgImage;
    private ImageLayer bgLayer;
    private Image heartImage;
    private ImageLayer heart;
    private ImageLayer heart2;
    private ImageLayer heart3;
    private Image heroProfileImage;
    private ImageLayer heroProfile;
    private Image pauseImage;
    private ImageLayer pause;
    private Image ghostProfileImage;
    private ImageLayer ghostProfile;

    //private Layer layer1 = new GameScreen().layer;

    private Image number;
    private List<ImageLayer> numList;
    private List<ImageLayer> numList2;
    private List<ImageLayer> maxList;

    private GroupLayer groupBomb = graphics().createGroupLayer();
    private GroupLayer ghostLayer1 = graphics().createGroupLayer();
    private GroupLayer ghostLayer2 = graphics().createGroupLayer();
    private Hero hero;
    private Ghost1 ghost1;
    private Ghost1 ghost2;

    private GamePauseScreen gamePause;

    public static float M_PER_PIXEL = 1/26.666667f;
    private static int width = 36;
    private static int height = 18;
    private boolean showDebugDraw = true;
    private DebugDrawBox2D debugDraw;

    private static World world;
    private HashMap<Object, String> bodies;
    private HashMap<Bomb, String> bombHash;
    private List<Hero> heroMap;
    private List<Ghost1> ghostList1;
    private List<GhostRun> ghostRun;
    private List<Boss> boss;
    private static List<Bomb> bombList;

    private Queue que;

    private int i = 0;
    private int e=0;
    private int g = 0;
    private int core = 0;
    private int heartCount = 3;
    private int hCount = 0;
    private Bomb bomb;
    private int bombIndex = 0;
    private int ghostTime = 0;
    private int ghostTimeR = 0;
    private int killCount = 0;
    private int killMax = 5;
    private int ghostCount = 0;
    private int bossTime = 0;

    private float x = 0.0f;
    private float y = 0.0f;

    private boolean checkPoint = true;
    private boolean pauseCheck = true;


    public GameScreen3(final ScreenStack ss){
        //===============================================================
        this.ss = ss;
        Vec2 gravity = new Vec2(0.0f,100.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);
        numList = new ArrayList<ImageLayer>();
        numList2 = new ArrayList<ImageLayer>();
        maxList = new ArrayList<ImageLayer>();
        que = new LinkedList();

        bodies = new HashMap<Object, String>();
        bombHash = new HashMap<Bomb, String>();
        heroMap = new ArrayList<Hero>();
        bombList = new ArrayList<Bomb>();
        ghostList1 = new ArrayList<Ghost1>();
        ghostRun = new ArrayList<GhostRun>();
        boss = new ArrayList<Boss>();

        bgImage = assets().getImage("images/background/bg1.png");
        bgLayer = graphics().createImageLayer(bgImage);
        bgLayer.setTranslation(200,0);
        graphics().rootLayer().add(bgLayer);

        heartImage = assets().getImage("images/item/heart.png");
        heart = graphics().createImageLayer(heartImage);
        heart2 = graphics().createImageLayer(heartImage);
        heart3 = graphics().createImageLayer(heartImage);
        heart.setTranslation(85,0);
        heart2.setTranslation(140,0);
        heart3.setTranslation(195,0);

        heroProfileImage = assets().getImage("images/item/hero1.png");
        heroProfile = graphics().createImageLayer(heroProfileImage);
        heroProfile.setTranslation(5, 20);
        ghostProfileImage = assets().getImage("images/item/ghost.png");
        ghostProfile = graphics().createImageLayer(ghostProfileImage);
        ghostProfile.setTranslation(545, 5);

        pauseImage = assets().getImage("images/button/pause.png");
        pause = graphics().createImageLayer(pauseImage);
        pause.setTranslation(300f,10f);

        number = assets().getImage("images/number/small/zero.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/one.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/two.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/three.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/four.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/five.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/six.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/seven.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/eight.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));
        number = assets().getImage("images/number/small/nine.png");
        numList.add(graphics().createImageLayer(number));
        numList2.add(graphics().createImageLayer(number));
        maxList.add(graphics().createImageLayer(number));

        number = assets().getImage("images/number/small/slash.png");
        maxList.add(graphics().createImageLayer(number));

        //numList.get(0).setTranslation(500f,10f);
        gamePause = new GamePauseScreen(ss, bgImage);

        pause.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.push(gamePause);
                //pauseCheck = !pauseCheck;
            }
        });

        //==============================================================
        hero = new Hero(world,12,14,3);
        bodies.put(hero.getBody(), "hero_1");


    }
    public GameScreen3(){ }

    @Override
    public void wasShown() {
        super.wasShown();
        //this.layer.add(bgLayer);
        this.layer.add(heroProfile);
        this.layer.add(heart);
        this.layer.add(heart2);
        this.layer.add(heart3);
        this.layer.add(pause);
        this.layer.add(ghostProfile);

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(-width,height-2), new Vec2(width*2, height-2));
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

                if(a == hero.getBody()|| b == hero.getBody()){
                    //hero.contact(contact);
                }
                /*for(Ghost1 g:ghostList1){
                    if((a == hero.getBody()&& b == g.getBody()) || (b == hero.getBody()&& a == g.getBody())){
                        g.contact(contact, "Hero");
                        hero.contact(contact);
                        heartCount--;
                        checkHeart(heartCount);
                    }
                    for(Bomb bomb: bombList) {
                        if ((a == bomb.getBody() && b == g.getBody()) || (b == bomb.getBody() && a == g.getBody())) {
                            g.contact(contact, "Bomb");
                            //killCount++;
                            //checkPoint = true;
                            // checkNumber();
                        }
                    }
                }
                for(GhostRun g:ghostRun){
                    if((a == hero.getBody()&& b == g.getBody()) || (b == hero.getBody()&& a == g.getBody())){
                        g.contact(contact, "Hero");
                        hero.contact(contact);
                        heartCount--;
                        checkHeart(heartCount);
                    }
                    for(Bomb bomb: bombList) {
                        if ((a == bomb.getBody() && b == g.getBody()) || (b == bomb.getBody() && a == g.getBody())) {
                            g.contact(contact, "Bomb");
                            //killCount++;
                            //checkPoint = true;
                            // checkNumber();
                        }
                    }
                }*/
                for(Boss g:boss){
                    if((a == hero.getBody()&& b == g.getBody()) || (b == hero.getBody()&& a == g.getBody())){
                        g.contact(contact, "Hero");
                        hero.contact(contact);
                        heartCount--;
                        checkHeart(heartCount);
                    }
                    for(Bomb bomb: bombList) {
                        if ((a == bomb.getBody() && b == g.getBody()) || (b == bomb.getBody() && a == g.getBody())) {
                            g.contact(contact, "Bomb");
                            //killCount++;
                            //checkPoint = true;
                            // checkNumber();
                        }
                    }
                }
                for(Bomb bomb: bombList){
                    if(a==bomb.getBody()|| b == bomb.getBody() ){
                        //bomb.contact(contact, hero);
                        core++;
                        //bomb.layer().setVisible(false);
                    }
                }

            }

            @Override
            public void endContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();
                /*for(Ghost1 g:ghostList1){
                    for(Bomb bomb: bombList) {
                        if ((a == bomb.getBody() && b == g.getBody()) || (b == bomb.getBody() && a == g.getBody())) {
                            killCount++;
                            checkPoint = true;
                            checkNumber();
                        }
                    }
                }
                for(GhostRun g:ghostRun){
                    for(Bomb bomb: bombList) {
                        if ((a == bomb.getBody() && b == g.getBody()) || (b == bomb.getBody() && a == g.getBody())) {
                            killCount++;
                            checkPoint = true;
                            checkNumber();
                        }
                    }
                }*/
                for(Boss g:boss){
                    for(Bomb bomb: bombList) {
                        if ((a == bomb.getBody() && b == g.getBody()) || (b == bomb.getBody() && a == g.getBody())) {
                            if(g.bombCount >= 3){
                                killCount++;
                                checkPoint = true;
                                checkNumber();
                            }

                        }
                    }
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                /*for(Ghost1 g:ghostList1){
                    if((a == hero.getBody()&& b == g.getBody()) || (b == hero.getBody()&& a == g.getBody())){
                        hCount++;
                        if(hCount > 1000){
                            g.contact(contact, "Hero");
                            hero.contact(contact);
                            heartCount--;
                            checkHeart(heartCount);
                            hCount = 0;
                        }
                    }
                }
                for(GhostRun g:ghostRun){
                    if((a == hero.getBody()&& b == g.getBody()) || (b == hero.getBody()&& a == g.getBody())){
                        hCount++;
                        if(hCount > 1000){
                            g.contact(contact, "Hero");
                            hero.contact(contact);
                            heartCount--;
                            checkHeart(heartCount);
                            hCount = 0;
                        }
                    }
                }*/
                for(Boss g:boss){
                    if((a == hero.getBody()&& b == g.getBody()) || (b == hero.getBody()&& a == g.getBody())){
                        hCount++;
                        if(hCount > 1000){
                            g.contact(contact, "Hero");
                            hero.contact(contact);
                            heartCount--;
                            checkHeart(heartCount);
                            hCount = 0;
                        }
                    }
                }
            }
        });


        this.layer.add(hero.layer());
        /*for(Ghost1 g:ghostList1){
            this.layer.add(g.layer());
        }
        for(GhostRun g:ghostRun){
            this.layer.add(g.layer());
        }*/
        for(Boss g:boss){
            this.layer.add(g.layer());
        }
        for(ImageLayer l: numList) {
            this.layer.add(l);
            l.setVisible(false);
        }
        for(ImageLayer l: numList2)  {
            this.layer.add(l);
            l.setVisible(false);
        }
        numList.get(0).setTranslation(380f,15f);
        numList2.get(0).setTranslation(415f,15f);
        numList.get(0).setVisible(true);
        numList2.get(0).setVisible(true);
        addKillMax();
    }

    @Override
    public void update(int delta){
        super.update(delta);
        if(pauseCheck == true){
            hero.update(delta);

            /*for(Ghost1 g:ghostList1){
                g.update(delta);
                this.layer.add(g.layer());
            }
            for(GhostRun g:ghostRun){
                g.update(delta);
                this.layer.add(g.layer());
            }*/
            for(Boss g:boss){
                g.update(delta);
                this.layer.add(g.layer());
            }
            for(Bomb b: bombList){
                b.update(delta);
            }
            for(Bomb b: bombList){
                //this.layer.add(b.layer());
                this.layer.add(b.layer());
            }
            ghostTimeR++;
            ghostTime++;
            bossTime++;
            //if(ghostCount < killMax){
            /*if(ghostTime > 50){
                int randomNum = 0 + (int)(Math.random() * 2);
                //System.out.println(randomNum);
                if(randomNum == 1){
                    //ghostList1.add(new Ghost1(world,hero.getBody().getPosition().x+7, hero.getBody().getPosition().y, -2, 'L'));
                }else if(randomNum == 0){
                    //ghostList1.add(new Ghost1(world,hero.getBody().getPosition().x-7, hero.getBody().getPosition().y, -2, 'R'));
                }
                ghostTime = 0;
                ghostCount++;
            }
            if(ghostTimeR > 100){
                int randomNum = 0 + (int)(Math.random() * 2);
                //System.out.println(randomNum);
                if(randomNum == 1){
                    //ghostRun.add(new GhostRun(world,hero.getBody().getPosition().x+10, hero.getBody().getPosition().y, -2, 'L'));
                }else if(randomNum == 0){
                    //ghostRun.add(new GhostRun(world,hero.getBody().getPosition().x-10, hero.getBody().getPosition().y, -2, 'R'));
                }
                ghostTimeR = 0;
                ghostCount++;
            }*/
            if(ghostCount <= killMax){
                if(bossTime > 100){
                    int randomNum = 0 + (int)(Math.random() * 2);
                    //System.out.println(randomNum);
                    if(randomNum == 1){
                        boss.add(new Boss(world,hero.getBody().getPosition().x+10, hero.getBody().getPosition().y, -2, 'L'));
                    }else if(randomNum == 0){
                        boss.add(new Boss(world,hero.getBody().getPosition().x-10, hero.getBody().getPosition().y, -2, 'R'));
                    }
                    bossTime = 0;
                    ghostCount++;
                }
            }

            //}else
            if(killCount >= killMax){
                /*try {

                    String content = Integer.toString(killCount)+"\n";

                    File file = new File("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");

                    // if file doesnt exists, then create it
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    //Writer output;

                    //FileReader inputFile = new FileReader("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");
                    FileInputStream inputFile = new FileInputStream(file);
                    //BufferedReader bufferReader = new BufferedReader(inputFile);
                    //System.out.println(inputFile.read());

                    List<Integer> line = new ArrayList();
                    String lineRead = new String("0");

                    while (inputFile.read() != -1)   {
                        line.add(inputFile.read());

                    }
                    line.add(killCount);

                    //FileWriter fw = new FileWriter("C:/cygwin64/home/GGGCOM/games/28052016/EscapeTheGhosts/game01/assets/src/main/resources/assets/content/maxKill.txt");
                    //BufferedWriter output = new BufferedWriter(fw);
                    FileOutputStream output = new FileOutputStream(file);
                    //Writer w = new OutputStreamWriter(output, "UTF-8");
                    for(int l:line){
                        output.write(l);
                        System.out.println(l);
                        //output.write(10);
                        //output.;
                    }
                    output.close();
                    //output.newLine();
                    //output.append('\n');
                    //output.flush();
                    //bufferReader.close();
                    //output.close();

                    System.out.println("Done");

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                ss.push(new GameWinScreen(ss, 3));
            }
            bgLayer.setTranslation(hero.bg_x, hero.bg_y);
        }
        world.step(0.033f, 10, 10);
    }

    @Override
    public void paint(Clock clock) {
        super.paint(clock);
        if(pauseCheck == true){

            hero.paint(clock);
            /*for(Ghost1 g:ghostList1){
                g.paint(clock);
            }
            for(GhostRun g:ghostRun){
                g.paint(clock);
            }*/
            for(Boss g:boss){
                g.paint(clock);
            }
            for(Bomb b: bombList){
                b.paint(clock);
            }

            if(showDebugDraw){
                debugDraw.getCanvas().clear();
                world.drawDebugData();

                debugDraw.getCanvas().setFillColor(Color.rgb(0,128,128));
                debugDraw.getCanvas().drawText("Score = "+String.valueOf(core),100,100);
            }
        }
    }

    public void addBomb(Bomb b){
        bombList.add(b);
    }

    public void checkHeart(int count){
        switch(count){
            case 2: heart3.setVisible(false); break;
            case 1: heart2.setVisible(false); break;
            case 0: heart.setVisible(false);
                ss.push(new GameOverScreen(ss, 3));
                break;
        }
    }

    public void checkNumber(){
        int front, back;

        front = killCount/10;
        back = killCount%10;

        for(ImageLayer l: numList)  l.setVisible(false);
        for(ImageLayer l: numList2)  l.setVisible(false);

        numList.get(front).setTranslation(385f,15f);
        numList2.get(back).setTranslation(415f,15f);
        //this.layer.add(numList.get(front));
        //this.layer.add(numList2.get(back));
        numList.get(front).setVisible(true);
        numList2.get(back).setVisible(true);
        checkPoint = false;
    }

    public void addKillMax(){
        int front;
        int back;

        front = killMax/10;
        back = killMax%10;
        maxList.get(10).setTranslation(445f,15f);
        maxList.get(front).setTranslation(475f,15f);
        maxList.get(back).setTranslation(495f,15f);
        this.layer.add(maxList.get(10));
        this.layer.add(maxList.get(front));
        this.layer.add(maxList.get(back));
    }
}
