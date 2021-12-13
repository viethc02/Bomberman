package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.graphics.Screen;

public abstract class Character extends AnimatedEntitiy {

    public int _timeAfter = 40;
    protected Board _board;
    protected int _direction = -1;
    protected boolean _alive = true;
    protected boolean _moving = false;


    public Character(int x, int y, Board board) {
        _x = x;
        _y = y;
        _board = board;
        //System.out.println(_board._characters.size());
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);

    protected abstract void calculateMove();

    protected abstract void move(double xa, double ya);

    /**
     * kill entity
     */
    public abstract void kill();

    /**
     * handle after killing
     */
    protected abstract void afterKill();

    protected abstract boolean canMove(double x, double y);

    protected double getXMessage() {
        return (_x * Game.SCALE) + (_sprite.SIZE / 2 * Game.SCALE);
    }

    protected double getYMessage() {
        return (_y * Game.SCALE) - (_sprite.SIZE / 2 * Game.SCALE);
    }

}
