package bchen11.tankwar;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

class Missile {

    private static final int SPEED = 12;

    private int x;

    private int y;

    private final boolean enemy;

    private final Direction direction;

    private boolean live = true;

    boolean isLive() {
        return live;
    }

    private void setLive(boolean live) {
        this.live = live;
    }

    Missile(int x, int y, boolean enemy, Direction direction) {
        this.x = x;
        this.y = y;
        this.enemy = enemy;
        this.direction = direction;
    }
    
    private Image getImage() {
        return direction.getImage("missile");
    }

    private void move() {
        x += direction.xFactor * SPEED;
        y += direction.yFactor * SPEED;
    }

    void draw(Graphics g) {
        move();
        // prevent missile to penetrate out of window
        if (x < 0 || x > GameClient.WIDTH || y < 0 || y > GameClient.HEIGHT) {
            this.live = false;
            return;
        }

        // prevent missile to penetrate through wall
        Rectangle rectangle = this.getRectangle();
        for (Wall wall : GameClient.getInstance().getWalls()) {
            if (rectangle.intersects(wall.getRectangle())) {
                this.setLive(false);
                return;
            }
        }


        if (enemy) { // if the missile hit player tank
            Tank playerTank = GameClient.getInstance().getPlayerTank();
            if (rectangle.intersects(playerTank.getRectangleForHitDetection())) {
                if(playerTank.isCheatMode()){
                    if(playerTank.getHp() != 100) {
                        playerTank.setHp(100);
                        playerTank.setLive(true);
                    }
                }else {
                    addExplosion();
                    playerTank.setHp(playerTank.getHp() - 20);
                    if (playerTank.getHp() <= 0) {
                        playerTank.setLive(false);
                    }
                }
                this.setLive(false);
            }
        } else { // if the missile hit the enemy tank
            for (Tank tank : GameClient.getInstance().getEnemyTanks()) {
                if (rectangle.intersects(tank.getRectangleForHitDetection())) {
                    addExplosion();
                    tank.setLive(false);
                    this.setLive(false);
                    break;
                }
            }
        }
        g.drawImage(getImage(), x, y, null);
    }



    private void addExplosion() {
        GameClient.getInstance().addExplosion(new Explosion(x, y));
        Tools.playAudio("explode.wav");
    }

    private Rectangle getRectangle() {
        return new Rectangle(x, y, getImage().getWidth(null), getImage().getHeight(null));
    }
}
