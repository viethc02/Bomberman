package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.enemy.EnemyOther;
import uet.oop.bomberman.entities.character.enemy.ai.*;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.KeyBoardSpe;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.sound.Sound;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BomberAI extends Bomber {
    public static List<Item> _items = new ArrayList<Item>();
    private static java.util.List<Bomb> _bombs;
    protected final double rest;
    protected final double MAX_STEPS;
    public boolean ai_system = true;
    protected AI _ai;
    protected double _steps;
    protected int _timeBetweenPutBombs = 0;

    public BomberAI(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs1();
        _sprite = Sprite.player_right;
        MAX_STEPS = Game.TILES_SIZE / Game.getBomberSpeed();
        rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
        _steps = MAX_STEPS;
    }

    public static List<Bomb> get_bombs() {
        return _bombs;
    }

    public Enemy getEnemy() {
        for (Character c : Board._characters) {
            if (c instanceof Enemy) {
                return (Enemy) c;
            }
        }
        return null;
    }

    public Portal getPortal() {
        for (Entity c : _board._entities) {
            if (c instanceof LayeredEntity) {
                LayeredEntity d = (LayeredEntity) c;
                for (int i = 0; i < d._entities.size(); i++) {
                    if (d._entities.get(i) instanceof Portal) {
                        return (Portal) d._entities.get(i);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void update() {
        clearBombs();

        _ai = new AIHardBomber(this, getEnemy(), _board, getPortal());
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();

        calculateMove();

        //detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    @Override
    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * check and place bomb.
     */
    public void detectPlaceBomb() {
        if (Game.getBombRate() > 0 && _timeBetweenPutBombs < 0) {

            int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
            int yt = Coordinates.pixelToTile((_y + _sprite.getSize() / 2) - _sprite.getSize()); //subtract half player height and minus 1 y position

            placeBomb(xt, yt);
            Game.addBombRate(-1);

            _timeBetweenPutBombs = 30;
        }
    }

    @Override
    protected void placeBomb(int x, int y) {
        Bomb b = new Bomb(x, y, _board);
        _board.addBomb1(b);
        Sound.play("res/sound/BOM_SET.wav");
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    @Override
    public void kill() {
        if (!_alive) return;
        _alive = false;
        Sound.play("res/sound/endgame3.wav");
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) --_timeAfter;
        else {
            _board.endGame();
        }
    }

    @Override
    protected void calculateMove() {
        int xa = 0, ya = 0;
        if (_steps <= 0) {
            _direction = _ai.calculateDirection();
            _steps = MAX_STEPS;
            //System.out.println(_direction);
        }

        if (_direction == 0) ya--;
        if (_direction == 2) ya++;
        if (_direction == 3) xa--;
        if (_direction == 1) xa++;
        if (_direction == 5) {
            detectPlaceBomb();
        }

        if (canMove(xa, ya)) {
            _steps -= 1 + rest;
            move(xa * Game.getBomberSpeed(), ya * Game.getBomberSpeed());
            _moving = true;
        } else {
            _steps = 0;
            _moving = false;
        }

    }

    @Override
    public boolean canMove(double x, double y) {
        for (int c = 0; c < 4; c++) { //colision detection for each corner of the player
            double xt = ((_x + x) + c % 2 * 9) / Game.TILES_SIZE; //divide with tiles size to pass to tile coordinate
            double yt = ((_y + y) + c / 2 * 10 - 15) / Game.TILES_SIZE; //these values are the best from multiple tests

            Entity a = _board.getEntity(xt, yt, this);

            if (!a.collide(this)) {
                if (a instanceof LayeredEntity) {
                    if (_board.getShow() != 2)
                        detectPlaceBomb();
                }
                return false;
            }
        }

        return true;
        //return false;
    }

    @Override
    public void move(double xa, double ya) {
        if (xa > 0) _direction = 1;
        if (xa < 0) _direction = 3;
        if (ya > 0) _direction = 2;
        if (ya < 0) _direction = 0;

        if (canMove(0, ya)) { //separate the moves for the player can slide when is colliding
            _y += ya;
        }

        if (canMove(xa, 0)) {
            _x += xa;
        }
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Flame) {
            this.kill();
            return false;
        }
        if (e instanceof Enemy) {
            this.kill();
            return true;
        }
        if (e instanceof LayeredEntity) return (e.collide(this));
        return true;
    }

    protected void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }
}
