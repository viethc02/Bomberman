package uet.oop.bomberman.entities.character.enemy.ai;

import javafx.util.Pair;
import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.BomberAI;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.character.enemy.EnemyOther;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.level.FileLevelLoader;

import java.util.LinkedList;
import java.util.Queue;

public class AIHardBomber extends AI {
    Bomber _bomber;
    Enemy _e;
    Board _board;
    Portal _portal;

    public AIHardBomber(Bomber bomber, Enemy e, Board board, Portal portal) {
        _bomber = bomber;
        _e = e;
        _board = board;
        _portal = portal;
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

    public int handlePortal() {
        int xPortal = (int) _portal.getX();
        int yPortal = (int) _portal.getY();

        int xPlayer = _bomber.getXTile();
        int yPlayer = _bomber.getYTile();

        int xBomb = 0, yBomb = 0;
        if (_board.getBomb1At(xPlayer, yPlayer) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }

        if (_board.getBomb1At(xPlayer + 1, yPlayer) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }

        if (_board.getBomb1At(xPlayer + 2, yPlayer) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }
        if (_board.getBomb1At(xPlayer - 1, yPlayer) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }
        if (_board.getBomb1At(xPlayer - 2, yPlayer) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }

        if (_board.getBomb1At(xPlayer, yPlayer + 1) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }

        if (_board.getBomb1At(xPlayer, yPlayer + 2) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }

        if (_board.getBomb1At(xPlayer, yPlayer - 1) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }

        if (_board.getBomb1At(xPlayer, yPlayer - 2) != null) {
            xBomb = xPlayer;
            yBomb = yPlayer;
        }

        if (_board.getBombs1().size() != 0) {
            return avoidBomb(xBomb, yBomb, xPlayer, yPlayer);
        }

        if ((xPlayer == xPortal + 1 && yPlayer == yPortal) ||
                (xPlayer == xPortal - 1 && yPlayer == yPortal) ||
                (xPlayer == xPortal && yPlayer == yPortal + 1) ||
                (xPlayer == xPortal && yPlayer == yPortal - 1)) {
            LayeredEntity e = (LayeredEntity) _board.getEntityAt(xPortal, yPortal);
            boolean kt = false;
            for (int i = 0; i < e._entities.size(); i++) {
                if (e._entities.get(i) instanceof Brick) {
                    kt = true;
                }
            }
            if (kt == true) {
                return 5;
            }
        }

        if (_board.getBomb1At(xPlayer + 3, yPlayer) != null ||
                _board.getBomb1At(xPlayer, yPlayer + 3) != null ||
                _board.getBomb1At(xPlayer, yPlayer - 3) != null ||
                _board.getBomb1At(xPlayer - 3, yPlayer) != null) {
            return 4;
        }

        boolean[][] dx = new boolean[110][110];

        Pair<Integer, Integer>[][] tr = new Pair[110][110];

        int[] c = {0, 0, -1, 1};
        int[] d = {1, -1, 0, 0};

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();

        Pair<Integer, Integer> p;

        p = new Pair<>(yPortal, xPortal);

        dx[yPortal][xPortal] = true;

        q.add(p);

        while (q.size() > 0) {
            p = q.element();

            q.remove();

            int y = p.getKey();
            int x = p.getValue();

            if (x == xPlayer && y == yPlayer) {

                int tmpX = xPlayer;
                int tmpY = yPlayer;
                p = tr[tmpY][tmpX];
                if (p == null)
                    return random.nextInt(4);

                if (p.getValue() < xPlayer)
                    return 3;
                else if (p.getValue() > xPlayer)
                    return 1;
                else if (p.getKey() < yPlayer)
                    return 0;
                else if (p.getKey() > yPlayer)
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

                if (checkPos(newX, newY) &&
                        !dx[newY][newX]) {
                    dx[newY][newX] = true;
                    tr[newY][newX] = p;

                    Pair<Integer, Integer> pp = new Pair<>(newY, newX);

                    q.add(pp);
                }
            }
        }
        return random.nextInt(4);
    }

    public int avoidBomb(int xBomb, int yBomb, int xPlayer, int yPlayer) {

        int xTarget = xBomb;
        int yTarget = yBomb;

        if (xTarget - 3 >= 0 && checkPos(xTarget - 3, yTarget)
                && checkPos(xTarget - 3, yTarget)) {
            xTarget -= 3;
        } else if (yTarget - 3 >= 0 && checkPos(xTarget, yTarget - 3)) {
            yTarget -= 3;
        } else if (xTarget - 4 >= 0 && checkPos(xTarget - 4, yTarget)) {
            xTarget -= 4;
        } else if (xTarget - 5 >= 0 && checkPos(xTarget - 5, yTarget)) {
            xTarget -= 5;
        } else if (xTarget - 1 >= 0 && yTarget - 1 >= 0
                && checkPos(xTarget - 1, yTarget - 1)) {
            xTarget -= 1;
            yTarget -= 1;
        } else if (xTarget - 1 >= 0 && yTarget + 1 < _board.getHeight() &&
                checkPos(xTarget - 1, yTarget + 1)) {
            xTarget -= 1;
            yTarget += 1;
        } else if (xTarget + 1 < _board.getWidth() && yTarget - 1 >= 0 &&
                checkPos(xTarget + 1, yTarget - 1)) {
            xTarget += 1;
            yTarget -= 1;
        } else if (xTarget + 1 < _board.getWidth() && yTarget + 1 < _board.getHeight() &&
                checkPos(xTarget + 1, yTarget + 1)) {
            xTarget += 1;
            yTarget += 1;
        } else if (xTarget - 2 >= 0 && yTarget + 1 < _board.getHeight() &&
                checkPos(xTarget - 2, yTarget + 1)) {
            xTarget -= 2;
            yTarget += 1;
        } else if (xTarget - 2 >= 0 && yTarget - 1 >= 0 &&
                checkPos(xTarget - 2, yTarget - 1)) {
            xTarget -= 2;
            yTarget -= 1;
        } else if (xTarget + 2 < _board.getWidth() && yTarget + 1 < _board.getHeight() &&
                checkPos(xTarget + 2, yTarget + 1)) {
            xTarget += 2;
            yTarget += 1;
        } else if (xTarget + 2 < _board.getWidth() && yTarget - 1 >= 0 &&
                checkPos(xTarget + 2, yTarget - 1)) {
            xTarget += 2;
            yTarget -= 1;
        }

        if ((xPlayer == xBomb - 3 && yPlayer == yBomb) || (xPlayer == xBomb && yPlayer == yBomb - 3)
                || (xPlayer == xBomb - 1 && yPlayer == yBomb - 1) || (xPlayer == xBomb - 1 && yPlayer == yBomb + 1)
                || (xPlayer == xBomb - 2 && yPlayer == yBomb + 1) || (xPlayer == xBomb - 2 && yPlayer == yBomb - 1)
                || (xPlayer == xBomb + 1 && yPlayer == yBomb + 1) || (xPlayer == xBomb + 1 && yPlayer == yBomb - 1)
                || (xPlayer == xBomb + 2 && yPlayer == yBomb + 1) || (xPlayer == xBomb + 2 && yPlayer == yBomb - 1)) {
            return 4;
        }

        /*System.out.println(xBomb + " " + xPlayer + " " + xTarget + " ");
        System.out.println(yBomb + " " + yPlayer + " " + yTarget + " ");*/

        boolean[][] dx = new boolean[110][110];

        Pair<Integer, Integer>[][] tr = new Pair[110][110];

        int[] c = {0, 0, -1, 1};
        int[] d = {1, -1, 0, 0};

        Queue<Pair<Integer, Integer>> q = new LinkedList<>();

        Pair<Integer, Integer> p;

        p = new Pair<>(yTarget, xTarget);

        dx[yTarget][xTarget] = true;

        q.add(p);

        while (q.size() > 0) {
            p = q.element();

            q.remove();

            int y = p.getKey();
            int x = p.getValue();

            if (x == xPlayer && y == yPlayer) {

                int tmpX = xPlayer;
                int tmpY = yPlayer;
                p = tr[tmpY][tmpX];
                if (p == null)
                    return random.nextInt(4);

                if (p.getValue() < xPlayer)
                    return 3;
                else if (p.getValue() > xPlayer)
                    return 1;
                else if (p.getKey() < yPlayer)
                    return 0;
                else if (p.getKey() > yPlayer)
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

    public int handleEnemy() {
        int xEnemy = _e.getXTile();
        int yEnemy = _e.getYTile();

        int xPlayer = _bomber.getXTile();
        int yPlayer = _bomber.getYTile();

        int xBomb = 0, yBomb = 0;

        if (_board.getBombs1().size() != 0) {
            xBomb = (int) _board.getBombs1().get(0).getX();
            yBomb = (int) _board.getBombs1().get(0).getY();
        }

        if (_board.getBombs1().size() != 0) {
            return avoidBomb(xBomb, yBomb, xPlayer, yPlayer);
        }

        if (_board.getBomb1At(xPlayer + 3, yPlayer) != null ||
                _board.getBomb1At(xPlayer, yPlayer + 3) != null ||
                _board.getBomb1At(xPlayer, yPlayer - 3) != null ||
                _board.getBomb1At(xPlayer - 3, yPlayer) != null) {
            return 4;
        }

        if ((xPlayer + 1 < _board.getWidth() && _board.getCharacterAtExcluding(xPlayer + 1, yPlayer, _bomber) instanceof Enemy) ||
                (xPlayer - 1 >= 0 && _board.getCharacterAtExcluding(xPlayer - 1, yPlayer, _bomber) instanceof Enemy) ||
                (yPlayer + 1 < _board.getHeight() && _board.getCharacterAtExcluding(xPlayer, yPlayer + 1, _bomber) instanceof Enemy) ||
                (yPlayer - 1 >= 0 && _board.getCharacterAtExcluding(xPlayer, yPlayer - 1, _bomber) instanceof Enemy)) {
            return 5;
        }

        if ((xPlayer + 2 < _board.getWidth() && _board.getCharacterAtExcluding(xPlayer + 2, yPlayer, _bomber) instanceof Enemy) ||
                (xPlayer - 2 >= 0 && _board.getCharacterAtExcluding(xPlayer - 2, yPlayer, _bomber) instanceof Enemy) ||
                (yPlayer + 2 < _board.getHeight() && _board.getCharacterAtExcluding(xPlayer, yPlayer + 2, _bomber) instanceof Enemy) ||
                (yPlayer - 2 >= 0 && _board.getCharacterAtExcluding(xPlayer, yPlayer - 2, _bomber) instanceof Enemy)) {
            return 5;
        }
        if ((xPlayer + 1 < _board.getWidth() && yPlayer + 1 < _board.getWidth() &&
                _board.getCharacterAtExcluding(xPlayer + 1, yPlayer + 1, _bomber) instanceof Enemy) ||
                (xPlayer - 1 >= 0 && yPlayer - 1 >= 0 &&
                        _board.getCharacterAtExcluding(xPlayer - 1, yPlayer - 1, _bomber) instanceof Enemy) ||
                (yPlayer + 1 < _board.getHeight() && xPlayer + 1 < _board.getWidth() &&
                        _board.getCharacterAtExcluding(xPlayer + 1, yPlayer - 1, _bomber) instanceof Enemy) ||
                (yPlayer - 1 >= 0 && xPlayer + 1 < _board.getWidth() &&
                        _board.getCharacterAtExcluding(xPlayer - 1, yPlayer + 1, _bomber) instanceof Enemy)) {
            return 5;
        }

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
                p = tr[tmpY][tmpX];
                if (p == null)
                    return random.nextInt(4);

                if (p.getValue() < xPlayer)
                    return 3;
                else if (p.getValue() > xPlayer)
                    return 1;
                else if (p.getKey() < yPlayer)
                    return 0;
                else if (p.getKey() > yPlayer)
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

                if (FileLevelLoader.get_map()[newY][newX] != '#' && !dx[newY][newX]) {
                    dx[newY][newX] = true;
                    tr[newY][newX] = p;

                    Pair<Integer, Integer> pp = new Pair<>(newY, newX);

                    q.add(pp);
                }
            }
        }
        return random.nextInt(4);
    }

    @Override
    public int calculateDirection() {
        if (_e != null) {
            return handleEnemy();
        }
        return handlePortal();
    }
}
