public class King extends ConcretePiece{
    ConcretePlayer concretePlayer;

    public King(ConcretePlayer concretePlayer) {
        this.concretePlayer = concretePlayer;

    }

    @Override
    public Player getOwner() {
        return concretePlayer;
    }

    @Override
    public String getType() {
        return "♔";
    }
}
