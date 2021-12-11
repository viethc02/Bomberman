package uet.oop.bomberman.entities;

import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

/**
 * Lớp đại diện cho tất cả thực thể trong game (Bomber, Enemy, Wall, Brick,...)
 */
public abstract class Entity {

    protected double _x, _y;
    protected boolean _removed = false;
    protected Sprite _sprite;

    /**
     * Phương thức này được gọi liên tục trong vòng lặp game,
     * mục đích để xử lý sự kiện và cập nhật trạng thái Entity
     */
    public abstract void update();

    /**
     * Phương thức này được gọi liên tục trong vòng lặp game,
     * mục đích để cập nhật hình ảnh của entity theo trạng thái
     */
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

    /**
     * Phương thức này được gọi để xử lý khi hai entity va chạm vào nhau
     *
     * @param e
     * @return
     */
    public abstract boolean collide(Entity e);
    //xu li 2 entity va cham

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
