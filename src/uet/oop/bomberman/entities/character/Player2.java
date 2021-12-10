package uet.oop.bomberman.entities.character;

import java.util.ArrayList;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.enemy.ai.AI;
import uet.oop.bomberman.entities.character.enemy.ai.AILow;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.KeyBoardSpe;
import uet.oop.bomberman.input.KeyBoardSpe2;
import uet.oop.bomberman.input.Keyboard;

import java.util.Iterator;
import java.util.List;

import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.sound.Sound;

public class Player2 extends Character {

    private List<Bomb> _bombs;
    protected KeyBoardSpe2 _input;
    public static List<Item> _items = new ArrayList<Item>();//xu li Item
    public static boolean _alive = true;
    protected int _finalAnimation = 30;
    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs2 = 0;

    public Player2(int x, int y, Board board) {
        super(x, y, board);
        _timeAfter = 20;
        _bombs = _board.getBombs2();
        _input = _board.getInput2();
        _sprite = Sprite.player2_right;
    }

    @Override
    public void update() {
        clearBombs();
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs2 < -7500) _timeBetweenPutBombs2 = 0;
        else _timeBetweenPutBombs2--;

        animate();

        calculateMove();

        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        //calculateXOffset();

        if (_alive)
            chooseSprite();
        else
            _sprite = Sprite.player2_dead1;

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     *
     */
    private void detectPlaceBomb() {
        // TODO: kiểm tra xem phím điều khiển đặt bom có được gõ và giá trị _timeBetweenPutBombs, Game.getBombRate() có thỏa mãn hay không
        // TODO:  Game.getBombRate() sẽ trả về số lượng bom có thể đặt liên tiếp tại thời điểm hiện tại
        // TODO: _timeBetweenPutBombs dùng để ngăn chặn Bomber đặt 2 Bomb cùng tại 1 vị trí trong 1 khoảng thời gian quá ngắn
        // TODO: nếu 3 điều kiện trên thỏa mãn thì thực hiện đặt bom bằng placeBomb()
        // TODO: sau khi đặt, nhớ giảm số lượng Bomb Rate và reset _timeBetweenPutBombs về 0
        if (_input.space && Game.getBombRate2() > 0 && _timeBetweenPutBombs2 < 0) {

            int xt = Coordinates.pixelToTile(_x + _sprite.getSize() / 2);
            int yt = Coordinates.pixelToTile((_y + _sprite.getSize() / 2) - _sprite.getSize()); //subtract half player height and minus 1 y position

            placeBomb(xt, yt);
            Game.addBombRate2(-1);

            _timeBetweenPutBombs2 = 30;
        }
    }

    protected void placeBomb(int x, int y) {
        // TODO: thực hiện tạo đối tượng bom, đặt vào vị trí (x, y)
        Bomb b = new Bomb(x, y, _board);
        _board.addBomb2(b);
        Sound.play("res/sound/BOM_SET.wav");
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate2(1);
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
            _board.endGamePvP();
        }
    }

    @Override
    protected void calculateMove() {
        // TODO: xử lý nhận tín hiệu điều khiển hướng đi từ _input và gọi move() để thực hiện di chuyển
        // TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
        int xa = 0, ya = 0;
        if (_input.up) ya--;
        if (_input.down) ya++;
        if (_input.left) xa--;
        if (_input.right) xa++;
        if (xa != 0 || ya != 0) {
            move(xa * Game.getBomberSpeed2(), ya * Game.getBomberSpeed2());
            _moving = true;
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        // TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không
        for (int c = 0; c < 4; c++) { //colision detection for each corner of the player
            double xt = ((_x + x) + c % 2 * 9) / Game.TILES_SIZE; //divide with tiles size to pass to tile coordinate
            double yt = ((_y + y) + c / 2 * 10 - 13) / Game.TILES_SIZE; //these values are the best from multiple tests

            Entity a = _board.getEntity(xt, yt, this);

            if (!a.collide(this))
                return false;
        }

        return true;
        //return false;
    }

    @Override
    public void move(double xa, double ya) {
        // TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không và thực hiện thay đổi tọa độ _x, _y
        // TODO: nhớ cập nhật giá trị _direction sau khi di chuyển
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
        // TODO: xử lý va chạm với Flame
        // TODO: xử lý va chạm với Enemy
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

    //sprite
    private void chooseSprite() {
        switch (_direction) {
            case 0:
                _sprite = Sprite.player2_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_up_1, Sprite.player2_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player2_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_right_1, Sprite.player2_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player2_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_down_1, Sprite.player2_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player2_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_left_1, Sprite.player2_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player2_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player2_right_1, Sprite.player2_right_2, _animate, 20);
                }
                break;
        }
    }
}
