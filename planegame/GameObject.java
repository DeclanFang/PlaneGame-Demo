package com.declan.planegame;

import java.awt.*;

/**
 * Father class of all the game objects
 * @author Declan
 */

public class GameObject {
    Image img;
    double x, y;
    int speed;
    int width, height;

    public void drawSelf(Graphics g) {
        g.drawImage(img, (int)x, (int)y, null);
    }

    public GameObject() {
    }

    //return the rectangle of the object in case we utilize the detection of the crash
    public Rectangle getRect() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
