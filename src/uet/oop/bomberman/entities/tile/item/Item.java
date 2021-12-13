package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

public abstract class Item extends Tile {
    protected int _duration = -1; //time of item(forever)
    protected boolean _active = false;
    protected int _level;

    public Item(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    public abstract boolean collide(Entity e);
}
