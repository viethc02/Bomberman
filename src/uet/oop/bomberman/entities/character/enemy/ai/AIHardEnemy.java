package uet.oop.bomberman.entities.character.enemy.ai;

import javafx.util.Pair;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.character.enemy.EnemyOther;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.level.FileLevelLoader;

import java.util.LinkedList;
import java.util.Queue;

public class AIHardEnemy extends AI {
    Bomber _bomber;
    Enemy _e;
    Board _board;

    public AIHardEnemy(Bomber bomber, Enemy e, Board board) {
        _bomber = bomber;
        _e = e;
        _board = board;
    }

    public boolean checkPos(int posX, int posY) {
        if (posX >= 0 && posY >= 0 && posY < _board.getHeight() && posY < _board.getWidth()) {
            Entity entity = _board.getEntityAt(posX, posY);
            if (entity instanceof Grass) {
                return true;
            } else if (entity instanceof LayeredEntity) {
                LayeredEntity le = (LayeredEntity) entity;
                for (int i = 0; i < le._entities.size(); i++) {
                    if (le._entities.get(i) instanceof Brick) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public int calculateDirection() {
        int xEnemy = _e.getXTile();
        int yEnemy = _e.getYTile();

        int xPlayer = _bomber.getXTile();
        int yPlayer = _bomber.getYTile();

        boolean[][] dx = new boolean[110][110];

        Pair<Integer, Integer>[][] tr = new Pair[110][110];

        int[] c = {0, 0, -1, 1};
        int[] d = {1, -1, 0, 0};

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();

        Pair<Integer, Integer> p;

        p = new Pair<>(yPlayer, xPlayer);

        dx[yPlayer][xPlayer] = true;

        q.add(p);

        while (q.size() > 0) {
            p = q.element();

            q.remove();

            int y = p.getKey();
            int x = p.getValue();

            if (x == xEnemy && y == yEnemy) {

                int tmpX = xEnemy;
                int tmpY = yEnemy;

                p = tr[tmpY][tmpX];

                if (p == null)
                    return random.nextInt(4);
                if (p.getValue() < xEnemy)
                    return 3;
                else if (p.getValue() > xEnemy)
                    return 1;
                else if (p.getKey() < yEnemy)
                    return 0;
                else if (p.getKey() > yEnemy)
                    return 2;
            }

            p = new Pair<>(y, x);

            for (int i = 0; i <= 3; i++) {
                int newX = x + c[i];
                int newY = y + d[i];

                if (newX < 0 || newX >= _board.getWidth() ||
                        newY < 0 || newY >= _board.getHeight()) {
                    continue;
                }


                if (checkPos(newX, newY) && !dx[newY][newX]) {
                    dx[newY][newX] = true;
                    tr[newY][newX] = p;

                    Pair<Integer, Integer> pp = new Pair<>(newY, newX);

                    q.add(pp);
                }
            }
        }
        return random.nextInt(4);
    }
}
