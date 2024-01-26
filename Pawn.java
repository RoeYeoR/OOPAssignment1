public  class Pawn extends ConcretePiece{

    ConcretePlayer concretePlayer;

    public Pawn(ConcretePlayer concretePlayer) {
        this.concretePlayer = concretePlayer;
    }

    @Override
    public Player getOwner() {
        return concretePlayer;
    }

    @Override
    public String getType() {
        if (this.concretePlayer.isPlayerOne())
            return "♙";
        return "♟";
    }
}
