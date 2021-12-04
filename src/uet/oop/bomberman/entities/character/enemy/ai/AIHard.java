package uet.oop.bomberman.entities.character.enemy.ai;

import javafx.util.Pair;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;

import java.util.LinkedList;
import java.util.Queue;

public class AIHard extends AI {
    Bomber _bomber;
    Enemy _e;
    Board _board;

    public AIHard(Bomber bomber, Enemy e, Board board) {
        _bomber = bomber;
        _e = e;
        _board = board;
    }

    @Override
    public int calculateDirection() {
        // TODO: cài đặt thuật toán tìm đường đi
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

        p = new Pair<>(yEnemy, xEnemy);

        dx[yEnemy][xEnemy] = true;
        q.add(p);

        while (q.size() > 0) {
            p = q.element();

            q.remove();

            int y = p.getKey();
            int x = p.getValue();

            if (x == xPlayer && y == yPlayer) {

                int tmpX = xPlayer;
                int tmpY = yPlayer;

                while (tmpX != xEnemy || tmpY != yEnemy) {

                    p = tr[tmpY][tmpX];

                    if (p.getKey() == yEnemy && p.getValue() == xEnemy) {
                        if (tmpX < xEnemy)
                            return 3;
                        else if (tmpX > xEnemy)
                            return 1;
                        else if (tmpY < yEnemy)
                            return 0;
                        else if (tmpY > yEnemy)
                            return 2;
                    }

                    tmpY = p.getKey();
                    tmpX = p.getValue();
                }

            }

            p = new Pair<>(y, x);

            for (int i = 0; i <= 3; i++) {
                int newX = x + c[i];
                int newY = y + d[i];

                if (newX < 0 || newX >= _board.getWidth() ||
                        newY < 0 || newY >= _board.getHeight()) {
                    continue;
                }

                Entity a = _board.getEntityAt(8, 2);
                System.out.println(a.getClass());
                if (/*a.collide(_e) && */!dx[newY][newX]) {
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
