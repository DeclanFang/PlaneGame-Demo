package com.declan.planegame;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

/**
 * Main frame of plane game
 * @author Declan
 */

public class GameFrame extends Frame {

    Image planeImg = GameUtil.getImage("images/plane.png");
    Image bg = GameUtil.getImage("images/bg.jpg");

    Plane plane = new Plane(planeImg, 250, 250);
    Shell[] shells = new Shell[30];

    Explode explode;

    Date startTime = new Date();
    Date endTime;
    int period;

    @Override
    public void paint(Graphics g) {
        Color c = g.getColor();
        g.drawImage(bg,0,0,null);

        plane.drawSelf(g);

        for(int i = 0; i < shells.length; i ++) {
            shells[i].draw(g);

            boolean crash = shells[i].getRect().intersects(plane.getRect());

            if(crash) {
                plane.live = false;
                if(explode == null) {
                    explode = new Explode(plane.x, plane.y);
                    endTime = new Date();
                    period = (int)((endTime.getTime() - startTime.getTime()) / 1000);
                }
                explode.draw(g);
            }

            if(!plane.live) {
                g.setColor(Color.WHITE);
                Font f = new Font("Times", Font.BOLD, 30);
                g.setFont(f);
                g.drawString("Times: " + period + "s", (int)plane.x, (int)plane.y);
            }
        }
        g.setColor(c);
    }

    //keep repainting the window
    class PaintThread extends Thread {

        @Override
        public void run() {
            while(true) {
                repaint();

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            plane.addDirection(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            plane.cancelDirection(e);
        }

    }

    //Initialize the window
    public void launchFrame() {
        this.setTitle("Plane Game");
        this.setVisible(true);
        this.setSize(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);
        this.setLocation(300,300);

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        new PaintThread().start();  //start the thread of repaint
        addKeyListener(new KeyMonitor());

        for(int i = 0; i < shells.length; i ++) {
            shells[i] = new Shell();
        }
    }

    public static void main(String[] args) {
        GameFrame frame = new GameFrame();
        frame.launchFrame();
    }

    private Image offScreenImage = null;

    public void update(Graphics g) {
        if(offScreenImage == null)
            offScreenImage = this.createImage(Constant.GAME_WIDTH,Constant.GAME_HEIGHT);

        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }
}
