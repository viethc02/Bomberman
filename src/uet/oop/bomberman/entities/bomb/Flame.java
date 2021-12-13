package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Player2;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;

public class Flame extends Entity {

    protected Board _board;
    protected int _direction;
    protected int xOrigin, yOrigin;
    protected FlameSegment[] _flameSegments = new FlameSegment[0];
    private final int _radius;

    /**
     * @param x         is coordinate X of Flame
     * @param y         is coordinate Y of Flame
     * @param direction is direction of Flame
     * @param radius    is max direction of Flame
     */
    public Flame(int x, int y, int direction, int radius, Board board) {
        xOrigin = x;
        yOrigin = y;
        _x = x;
        _y = y;
        _direction = direction;
        _radius = radius;
        _board = board;
        createFlameSegments();
    }

    /**
     * create FlameSegment, a segment is a coordinate.
     */
    private void createFlameSegments() {
        /// calculate Flame corresponding segment
        _flameSegments = new FlameSegment[calculatePermitedDistance()];

        boolean last = false;

        int x = (int) _x;
        int y = (int) _y;
        for (int i = 0; i < _flameSegments.length; i++) {
            last = i == _flameSegments.length - 1;

            switch (_direction) {
                case 0:
                    y--;
                    break;
                case 1:
                    x++;
                    break;
                case 2:
                    y++;
                    break;
                case 3:
                    x--;
                    break;
            }
            _flameSegments[i] = new FlameSegment(x, y, _direction, last);
        }
    }

    /**
     * calculate Flame.
     */
    private int calculatePermitedDistance() {
        int radius = 0;
        int x = (int) _x;
        int y = (int) _y;
        while (radius < _radius) {
            if (_direction == 0) y--;
            if (_direction == 1) x++;
            if (_direction == 2) y++;
            if (_direction == 3) x--;

            Entity a = _board.getEntity(x, y, null);

            if (a instanceof Bomb) ++radius; //explosion has to be below the bom

            if (a.collide(this) == false) //cannot pass thru
                break;

            ++radius;
        }
        return radius;
    }

    public FlameSegment flameSegmentAt(int x, int y) {
        for (int i = 0; i < _flameSegments.length; i++) {
            if (_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
                return _flameSegments[i];
        }
        return null;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Screen screen) {
        for (int i = 0; i < _flameSegments.length; i++) {
            _flameSegments[i].render(screen);
        }
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Bomber) ((Bomber) e).kill();
        if (e instanceof Player2) ((Player2) e).kill();
        if (e instanceof Enemy) ((Enemy) e).kill();
        return true;
    }
}
