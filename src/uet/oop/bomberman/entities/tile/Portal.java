package uet.oop.bomberman.entities.tile;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.BomberAI;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.sound.Sound;

public class Portal extends Tile {
    protected Board _board;

    public Portal(int x, int y, Board board, Sprite sprite) {
        super(x, y, sprite);
        _board = board;
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Bomber) {

            if (!_board.detectNoEnemies())
                return false;

            if (e.getXTile() == getX() && e.getYTile() == getY()) {
                if (_board.detectNoEnemies()) {
                    _board.nextLevel();
                    Sound.play("res/sound/CRYST_UP.wav");
                }
            }

            return true;
        }

        return true;
    }

    @Override
    public void render(Screen screen) {
        int x = Coordinates.tileToPixel(_x);
        int y = Coordinates.tileToPixel(_y);

        if (_board.detectNoEnemies()) {
            _sprite = Sprite.movingSprite(Sprite.portal_close, Sprite.portal_open, 0, 2);
            screen.renderEntityWithBelowSprite(x, y, this, Sprite.portal_open);
        } else {
            screen.renderEntity(x, y, this);
        }
    }
}
