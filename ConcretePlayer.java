public class ConcretePlayer implements  Player{

    boolean isPlayerOne;
     int wins;
    public ConcretePlayer() {
    wins=0;
    }

    @Override

    public boolean isPlayerOne() {
        return this.isPlayerOne;
    }

    @Override
    public int getWins() {

        return wins;
    }
    public void newWin() {
        this.wins++;

    }
    public int resetWin() {
        return wins=0;
    }
    public void setPlayer(boolean isPlayerOne) {
        this.isPlayerOne=isPlayerOne;
    }
}
