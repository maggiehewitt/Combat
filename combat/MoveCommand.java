package combat;

public abstract class MoveCommand {
    private PlayerManager player;

    abstract void move();

    public PlayerManager getPlayer() {
        return this.player;
    }

    public void setPlayer(PlayerManager player) {
        this.player = player;
    }
}
