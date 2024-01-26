import java.util.ArrayList;
import java.util.Comparator;

public class Square implements Comparable<Square> {

    private Position pos;
    private ArrayList<String> ids;
    public Square(Position pos) {
        this.pos = pos;

    }
    public Square(int x, int y) {

        this.ids=new ArrayList<>();
        this.pos=new Position(x,y);
    }

    @Override
    public String toString() {
        return "("+getX()+", "+getY()+")"+getSize()+" pieces";
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public void setIds(ArrayList<String> ids) {
        this.ids = ids;
    }
    public int getX() {
        return pos.getX();
    }
    public int getY() {
        return pos.getY();
    }
    public int getSize() {
        return this.ids.size();
    }


    @Override
    public int compareTo(Square o) {
        return 0;
    }
    public static Comparator<Square> sortBySizeAndXY = Comparator
            .comparingInt(Square::getSize)
            .reversed()
            .thenComparing(Square::getX)
            .thenComparing(Square::getY);


}
