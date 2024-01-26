import java.io.Serializable;

public class Position  {


  private int x,y;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;

    }

    public boolean isSame(Position p)
    {
        if(p!=null)
            return this.x== p.getX()&&this.y==p.getY();
        return false;
    }

    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                ')';
    }
}
