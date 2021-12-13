package uet.oop.bomberman.entities;

import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

public abstract class Entity {

    protected double _x, _y;
    protected boolean _removed = false;
    protected Sprite _sprite;

    public abstract void update();

    public abstract void render(Screen screen);

    public void remove() {
        _removed = true;
    }

    public boolean isRemoved() {
        return _removed;
    }

    public Sprite getSprite() {
        return _sprite;
    }

    public abstract boolean collide(Entity e);

    public double getX() {
        return _x;
    }

    public double getY() {
        return _y;
    }

    public int getXTile() {
        return (int)Math.ceil(Coordinates.pixelToTile(_x + _sprite.SIZE / 2));
    }

    public int getYTile() {
        return (int)Math.ceil(Coordinates.pixelToTile(_y - _sprite.SIZE / 2));
    }
}
