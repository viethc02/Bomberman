package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Player2;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.sound.Sound;

public class BombItem extends Item {

    public BombItem(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Bomber) {

            Sound.play("res/sound/Item.wav");
            Game.addBombRate(1);
            remove();
        }
        if (e instanceof Player2) {

            Sound.play("res/sound/Item.wav");
            Game.addBombRate2(1);
            remove();
        }
        return false;
    }

}
