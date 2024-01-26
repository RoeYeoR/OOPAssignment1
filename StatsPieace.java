import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StatsPieace implements Comparable<StatsPieace> {

    public static Comparator<StatsPieace> sortByKillsAndId = Comparator
            .comparingInt(StatsPieace::getKills)
            .reversed()
            .thenComparing(StatsPieace::getId, new AlphanumericComparator());

    public static Comparator<StatsPieace> sortBySquaresAndId = Comparator
            .comparingInt(StatsPieace::getSquares)
            .reversed()
            .thenComparing(StatsPieace::getId, new AlphanumericComparator());

    public static Comparator<StatsPieace> sortByStepsAndId = Comparator
            .comparingInt(StatsPieace::getSteps)
            .thenComparing(StatsPieace::getId, new AlphanumericComparator());
    private static class AlphanumericComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            String[] parts1 = s1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            String[] parts2 = s2.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");

            for (int i = 0; i < Math.min(parts1.length, parts2.length); i++) {
                if (Character.isDigit(parts1[i].charAt(0)) && Character.isDigit(parts2[i].charAt(0))) {
                    int num1 = Integer.parseInt(parts1[i]);
                    int num2 = Integer.parseInt(parts2[i]);
                    if (num1 != num2) {
                        return Integer.compare(num1, num2);
                    }
                } else {
                    int lexOrder = parts1[i].compareTo(parts2[i]);
                    if (lexOrder != 0) {
                        return lexOrder;
                    }
                }
            }

            return Integer.compare(parts1.length, parts2.length);
        }
    }


    private String id;
    private int kills;
    private int squares;
    private int steps;

    public void addSquares(int squares) {
        this.squares+=squares;
    }

    public int getSquares() {
        return this.squares;
    }

    private ArrayList<Position> positions=new ArrayList<Position>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Position> getPos() {
        return positions;
    }

    public void setPos(ArrayList<Position> positions) {
        this.positions = positions;
    }

    public int getKills() {
        return kills;
    }
    public void increaseKills() {
         kills++;
    }

    public void addKills(int kills) {
        this.kills +=kills;
    }
    public void removeKills(int kills) {
        this.kills -=kills;
    }

    public void increaseSteps() {
        this.steps++;
    }
    public int getSteps() {
        return this.steps;
    }

    public StatsPieace(String id, Position pos) {

        this.id = id;
        this.kills=0;
        this.positions.add(pos);
        this.steps=0;
    }


    public String toStringMoves() {
        String text=id+": [";

        for (int i=0;i<positions.size();i++)
        {
            if(!positions.get(i).isSame(new Position(-1,-1)))
            {
                text += positions.get(i).toString();
                if (!(i + 1 == positions.size()))
                    text += ", ";
            }
        }
        if(positions.getLast().isSame(new Position(-1,-1)))
            text=text.substring(0,text.length()-2);

        return text+"]";
    }
    public String toStringKills() {

        return id+": "+kills+" kills";
    }
    public String toStringSquares() {

        return id+": "+squares+" squares";
    }



    public int compare(String s1, String s2) {
        String[] parts1 = s1.split("(?<=\\D)(?=\\d)"); // Split at the boundary between non-digit and digit
        String[] parts2 = s2.split("(?<=\\D)(?=\\d)");

        int result = parts1[0].compareTo(parts2[0]); // Compare non-digit parts

        if (result == 0) {
            int num1 = Integer.parseInt(parts1[1]);
            int num2 = Integer.parseInt(parts2[1]);
            result = Integer.compare(num2, num1); // Compare numeric parts in reverse order
        }

        return result;
    }

    @Override
    public int compareTo(StatsPieace o) {
        return Integer.compare(o.getKills(), this.getKills());
    }

}
