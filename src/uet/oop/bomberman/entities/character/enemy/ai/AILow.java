package uet.oop.bomberman.entities.character.enemy.ai;

public class AILow extends AI {

    @Override
    public int calculateDirection() {
        return random.nextInt(4);
    }

}
