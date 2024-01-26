import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameLogic implements PlayableLogic {

    private ConcretePlayer player1,player2;

    private ArrayList<StatsPieace> redPawns=new ArrayList<StatsPieace>();
    private ArrayList<StatsPieace> bluePawns=new ArrayList<StatsPieace>();
    private boolean isSecondPlayerTurn;
    private ArrayList<String> arrIDs;
    private ArrayList<ArrayList<String>> kills;

    private StatsPieace kingPos;
    private boolean isVictory;
    final Position edge1=new Position(0,0),edge2=new Position(10,0),edge3=new Position(0,10),edge4=new Position(10,10);
    private Square [][] board=new Square[11][11];

    public GameLogic() {
        isSecondPlayerTurn = true;
         player1 = new ConcretePlayer();
        player1.setPlayer(true);
        player2 = new ConcretePlayer();
        player2.setPlayer(false);
        isVictory=false;
        arrIDs=new ArrayList<String>();
        kills=new ArrayList<ArrayList<String>>();

        initSquareArr();
        setPositions();

    }

    @Override
    public boolean move(Position a, Position b) {
        int kills=0;
        if(isEdge(b))
        {
            if(!a.isSame(kingPos.getPos().getLast()))
                return false;

        }
        if(isLegalMove(a,b))
        {
            if (!isSecondPlayerTurn)
            {
                if (a.isSame(kingPos.getPos().getLast()))
                {
                    boolean found= false;
                        if(board[b.getX()][b.getY()].getIds().size()==0)
                        {
                            board[b.getX()][b.getY()].getIds().add(kingPos.getId());
                        }
                        else {
                            for (int j = 0; j < board[b.getX()][b.getY()].getIds().size(); j++) {
                                if (board[b.getX()][b.getY()].getIds().get(j).equals(kingPos.getId())) {

                                    found = true;
                                }
                            }
                            if (!found) {
                                board[b.getX()][b.getY()].getIds().add(kingPos.getId());

                            }
                        }
                    kingPos.getPos().add(new Position(b.getX(), b.getY()));
                    isKingWinner();
                    kingPos.addSquares(calculateSquares(a,b));
                    arrIDs.add(kingPos.getId());
                    kingPos.increaseSteps();
                    checkIfWinner();

                    changeTurn();
                    return true;
                } else {
                    for (int i = 0; i < bluePawns.size(); i++)
                    {
                        boolean found= false;
                        if (a.isSame(bluePawns.get(i).getPos().getLast())) {
                            if(board[b.getX()][b.getY()].getIds().size()==0)
                            {
                                board[b.getX()][b.getY()].getIds().add(bluePawns.get(i).getId());
                            }
                            else
                            {
                                for(int j=0;j<board[b.getX()][b.getY()].getIds().size();j++)
                                {
                                    if(board[b.getX()][b.getY()].getIds().get(j).equals(bluePawns.get(i).getId()))
                                    {

                                        found=true;
                                    }
                                }
                                if(!found)
                                {
                                    board[b.getX()][b.getY()].getIds().add(bluePawns.get(i).getId());

                                }
                            }


                            bluePawns.get(i).getPos().add(new Position(b.getX(), b.getY()));
                            kills=Eat(b);
                            arrIDs.add(bluePawns.get(i).getId());

                            bluePawns.get(i).addKills(kills);
                            bluePawns.get(i).increaseSteps();
                            bluePawns.get(i).addSquares(calculateSquares(a,b));
                            checkIfWinner();


                            changeTurn();
                            return true;


                        }
                    }
                }

            } else
            {
                for (int i = 0; i < redPawns.size(); i++)
                {
                    boolean found= false;

                    if (a.isSame(redPawns.get(i).getPos().getLast()))
                    {
                        redPawns.get(i).getPos().add(new Position(b.getX(), b.getY()));
                        if(board[b.getX()][b.getY()]==null)
                        {
                            board[b.getX()][b.getY()].getIds().add(redPawns.get(i).getId());
                        }
                        else
                        {
                            for(int j=0;j<board[b.getX()][b.getY()].getIds().size();j++)
                            {
                                if(board[b.getX()][b.getY()].getIds().get(j).equals(redPawns.get(i).getId()))
                                {
                                    found=true;
                                }
                            }
                            if(!found)
                            {
                                board[b.getX()][b.getY()].getIds().add(redPawns.get(i).getId());

                            }
                        }
                        kills=Eat(b);
                        arrIDs.add(redPawns.get(i).getId());

                        redPawns.get(i).addKills(kills);
                        redPawns.get(i).increaseSteps();
                        redPawns.get(i).addSquares(calculateSquares(a,b));

                        checkIfWinner();

                        changeTurn();
                        return true;


                    }
                }

            }

        }
        return false;

    }

    private int calculateSquares(Position a, Position b) {
        int squares=0;
        if(a.getX()==b.getX())
            squares=Math.abs(a.getY()-b.getY());
        else
            squares=Math.abs(a.getX()-b.getX());
        return squares;
    }

    @Override
    public Piece getPieceAtPosition(Position position) {

        for (int i = 0; i < bluePawns.size(); i++) {
            if (position.isSame(bluePawns.get(i).getPos().getLast())) {

                return new Pawn(player1);
            }
        }
        for (int i = 0; i < redPawns.size(); i++) {
            if (position.isSame(redPawns.get(i).getPos().getLast())) {

                return new Pawn(player2);
            }
        }
        if (position.isSame(kingPos.getPos().getLast())) {

            return new King(player1);
        }

        return null;
    }

    public Piece initPlayersPieces(Position position) {

        for (int i = 0; i < bluePawns.size(); i++) {
            if (position.isSame(bluePawns.get(i).getPos().getLast())) {
                ConcretePlayer concretePlayer = new ConcretePlayer();
                concretePlayer.setPlayer(true);
                return new Pawn(concretePlayer);
            }
        }
        for (int i = 0; i < redPawns.size(); i++) {
            if (position.isSame(redPawns.get(i).getPos().getLast())) {
                ConcretePlayer concretePlayer = new ConcretePlayer();
                concretePlayer.setPlayer(false);
                return new Pawn(concretePlayer);
            }
        }
        if (position.isSame(kingPos.getPos().getLast())) {
            ConcretePlayer concretePlayer = new ConcretePlayer();
            concretePlayer.setPlayer(true);
            return new King(concretePlayer);
        }

        return null;
    }
    @Override
    public Player getFirstPlayer() {
        return player1;

    }

    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    @Override
    public boolean isGameFinished() {

        return isVictory;

    }

    @Override
    public boolean isSecondPlayerTurn() {
        return isSecondPlayerTurn;
    }

    public void changeTurn() {
        isSecondPlayerTurn = !isSecondPlayerTurn;
    }

    @Override
    public void reset() {

        if(!isVictory)
        {
            player2.resetWin();
            player1.resetWin();
        }
        isSecondPlayerTurn=true;
        isVictory=false;
        setPositions();
    }

    @Override
    public void undoLastMove() {

        if(arrIDs.size()>0) {
            StatsPieace lastPiece = findByID(arrIDs.getLast());
            Position lastpos = lastPiece.getPos().getLast();
            for(int i=0;i<kills.getLast().size();i++)
            {
                removeLastMove(kills.getLast().get(i));
                removeOneKill(lastPiece.getId());

            }
            kills.removeLast();
            removeLastMove(lastPiece.getId());
            arrIDs.removeLast();
            isSecondPlayerTurn = !isSecondPlayerTurn;
        }



    }

    @Override
    public int getBoardSize() {
        return 11;
    }

    public boolean isLegalMove(Position a, Position b) {
        if (a.getX() == b.getX() || a.getY() == b.getY()) {
            if (a.getX() == b.getX()) {

                    int min = Math.min(a.getY(),b.getY());
                    int max = Math.max(a.getY(),b.getY());
                if(a.getY()>b.getY())
                {
                    max--;
                }
                else
                {
                    min++;
                }

                for (int j = min; j <= max; j++) {
                    Position current = new Position(a.getX(), j);
                    if (current.isSame(kingPos.getPos().getLast())) {
                        return false;
                    }

                    for (int i = 0; i < bluePawns.size(); i++) {
                        if (current.isSame(bluePawns.get(i).getPos().getLast())) {
                            return false;
                        }
                    }
                    for (int i = 0; i < redPawns.size(); i++) {
                        if (current.isSame(redPawns.get(i).getPos().getLast())) {
                            return false;
                        }
                    }
                }
                return true;
            }
            if (a.getY() == b.getY()) {

                int min = Math.min(a.getX(), b.getX());
                int max = Math.max(a.getX(), b.getX());

                if(a.getX()>b.getX())
                {
                    max--;
                }
                else
                {
                    min++;
                }

                for (int j = min; j <= max; j++) {
                    Position current = new Position(j, a.getY());
                    if (current.isSame(kingPos.getPos().getLast()))
                    {
                        return false;
                    }

                    for (int i = 0; i < bluePawns.size(); i++) {
                        if (current.isSame(bluePawns.get(i).getPos().getLast())) {
                            return false;
                        }
                    }
                    for (int i = 0; i < redPawns.size(); i++) {
                        if (current.isSame(redPawns.get(i).getPos().getLast())) {
                            return false;
                        }
                    }
                }
                return true;

            }
        }
        return false;

    }
    public int Eat(Position d) {
        int counter=0;
        ArrayList<String> eats=new ArrayList<String>();
        if(isSecondPlayerTurn)//if red player
        {

            for(int i=0;i<bluePawns.size();i++)
            {
                if(d.getY()+1==bluePawns.get(i).getPos().getLast().getY()&&d.getX()==bluePawns.get(i).getPos().getLast().getX())//up
                {

                    for(int j=0;j<redPawns.size();j++)
                    {

                        if (((d.getY()+2  == redPawns.get(j).getPos().getLast().getY() && d.getX() == redPawns.get(j).getPos().getLast().getX())||(isEdge(new Position(d.getX(),d.getY()+2)))))
                        {
                            bluePawns.get(i).getPos().add(new Position(-1,-1));
                            eats.add(bluePawns.get(i).getId());
                            counter++;
                            break;


                        }
                    }
                    if(d.getY()+2>=getBoardSize()) {
                        bluePawns.get(i).getPos().add(new Position(-1, -1));
                        eats.add(bluePawns.get(i).getId());

                        counter++;
                    }

                }
                if(d.getY()-1==bluePawns.get(i).getPos().getLast().getY()&&d.getX()==bluePawns.get(i).getPos().getLast().getX())//down
                {

                    for(int j=0;j<redPawns.size();j++)
                    {
                        if (((d.getY()-2  == redPawns.get(j).getPos().getLast().getY() && d.getX() == redPawns.get(j).getPos().getLast().getX())||(isEdge(new Position(d.getX(),d.getY()-2)))))
                        {
                            bluePawns.get(i).getPos().add(new Position(-1,-1));
                            eats.add(bluePawns.get(i).getId());

                            counter++;
                            break;

                        }
                    }
                    if(d.getY()-2<0) {
                        bluePawns.get(i).getPos().add(new Position(-1, -1));
                        eats.add(bluePawns.get(i).getId());

                        counter++;
                    }
                    }

                if(d.getY()==bluePawns.get(i).getPos().getLast().getY()&&d.getX()+1==bluePawns.get(i).getPos().getLast().getX())//right
                {

                    for(int j=0;j<redPawns.size();j++)
                    {
                        if (((d.getY()  == redPawns.get(j).getPos().getLast().getY() && d.getX()+2 == redPawns.get(j).getPos().getLast().getX())||(isEdge(new Position(d.getX()+2,d.getY())))))
                        {
                            bluePawns.get(i).getPos().add(new Position(-1,-1));
                            eats.add(bluePawns.get(i).getId());

                            counter++;
                            break;

                        }
                    }
                    if(d.getX()+2>=getBoardSize()) {
                        bluePawns.get(i).getPos().add(new Position(-1, -1));
                        eats.add(bluePawns.get(i).getId());

                        counter++;
                    }
                }
                if(d.getY()==bluePawns.get(i).getPos().getLast().getY()&&d.getX()-1==bluePawns.get(i).getPos().getLast().getX())//left
                {

                    for(int j=0;j<redPawns.size();j++)
                    {
                        if (((d.getY()  == redPawns.get(j).getPos().getLast().getY() && d.getX()-2 == redPawns.get(j).getPos().getLast().getX())||(isEdge(new Position(d.getX()-2,d.getY())))))
                        {
                            bluePawns.get(i).getPos().add(new Position(-1,-1));
                            eats.add(bluePawns.get(i).getId());

                            counter++;
                            break;

                        }
                    }
                    if(d.getY()-2<0) {
                        bluePawns.get(i).getPos().add(new Position(-1, -1));
                        eats.add(bluePawns.get(i).getId());

                        counter++;
                    }
                }
            }
        }
        else//if red player
        {
            for(int i=0;i<redPawns.size();i++)
            {
                if(d.getY()+1==redPawns.get(i).getPos().getLast().getY()&&d.getX()==redPawns.get(i).getPos().getLast().getX())//up
                {
                    for(int j=0;j<bluePawns.size();j++)
                    {
                        if (((d.getY()+2  == bluePawns.get(j).getPos().getLast().getY() && d.getX() == bluePawns.get(j).getPos().getLast().getX())||(isEdge(new Position(d.getX(),d.getY()+2)))))
                        {
                            redPawns.get(i).getPos().add(new Position(-1,-1));
                            eats.add(redPawns.get(i).getId());

                            counter++;
                            break;

                        }
                    }
                    if(d.getY()+2>=getBoardSize()) {
                        redPawns.get(i).getPos().add(new Position(-1, -1));
                        eats.add(redPawns.get(i).getId());

                        counter++;
                    }
                }
                if(d.getY()-1==redPawns.get(i).getPos().getLast().getY()&&d.getX()==redPawns.get(i).getPos().getLast().getX())//down

                {
                    for(int j=0;j<bluePawns.size();j++)
                    {
                        if (((d.getY()-2  == bluePawns.get(j).getPos().getLast().getY() && d.getX() == bluePawns.get(j).getPos().getLast().getX())||(isEdge(new Position(d.getX(),d.getY()-2)))))
                        {
                            redPawns.get(i).getPos().add(new Position(-1,-1));
                            eats.add(redPawns.get(i).getId());

                            counter++;
                            break;

                        }
                    }
                    if(d.getY()-2<0) {
                        redPawns.get(i).getPos().add(new Position(-1, -1));
                        eats.add(redPawns.get(i).getId());

                        counter++;
                    }
                }

                if(d.getY()==redPawns.get(i).getPos().getLast().getY()&&d.getX()+1==redPawns.get(i).getPos().getLast().getX())//right
                {
                    for(int j=0;j<bluePawns.size();j++)
                    {
                        if (((d.getY()  == bluePawns.get(j).getPos().getLast().getY() && d.getX()+2 == bluePawns.get(j).getPos().getLast().getX())||(isEdge(new Position(d.getX()+2,d.getY())))))
                        {
                            redPawns.get(i).getPos().add(new Position(-1,-1));
                            eats.add(redPawns.get(i).getId());

                            counter++;
                            break;


                        }
                    }
                    if(d.getX()+2>=getBoardSize()) {
                        redPawns.get(i).getPos().add(new Position(-1, -1));
                        eats.add(redPawns.get(i).getId());

                        counter++;
                    }
                }
                if(d.getY()==redPawns.get(i).getPos().getLast().getY()&&d.getX()-1==redPawns.get(i).getPos().getLast().getX())//left
                {
                    for(int j=0;j<bluePawns.size();j++)
                    {
                        if (((d.getY()  == bluePawns.get(j).getPos().getLast().getY() && d.getX()-2 == bluePawns.get(j).getPos().getLast().getX())||(isEdge(new Position(d.getX()-2,d.getY())))))
                        {
                            redPawns.get(i).getPos().add(new Position(-1,-1));
                            eats.add(redPawns.get(i).getId());

                            counter++;
                            break;


                        }
                    }
                    if(d.getX()-2<0) {
                        redPawns.get(i).getPos().add(new Position(-1, -1));
                        eats.add(redPawns.get(i).getId());

                        counter++;
                    }
                }
            }
        }

        kills.add(eats);
    return counter;
    }
    public boolean isEdge(Position p)
    {
        if(p.isSame(edge1)||p.isSame(edge2)||p.isSame(edge3)||p.isSame(edge4))
        {
            return true;
        }
        return false;
    }
    public boolean isKingDefeated()
    {
        int count=0;
        for(int i=0;i<redPawns.size();i++)
        {

            if(kingPos.getPos().getLast().getY()+1==redPawns.get(i).getPos().getLast().getY()&&kingPos.getPos().getLast().getX()==redPawns.get(i).getPos().getLast().getX())//up
            {
                count++;

            }
            if(kingPos.getPos().getLast().getY()-1==redPawns.get(i).getPos().getLast().getY()&&kingPos.getPos().getLast().getX()==redPawns.get(i).getPos().getLast().getX())//down
            {
                count++;
            }
            if(kingPos.getPos().getLast().getY()==redPawns.get(i).getPos().getLast().getY()&&kingPos.getPos().getLast().getX()+1==redPawns.get(i).getPos().getLast().getX())//right
            {
                count++;
            }
            if(kingPos.getPos().getLast().getY()==redPawns.get(i).getPos().getLast().getY()&&kingPos.getPos().getLast().getX()-1==redPawns.get(i).getPos().getLast().getX())//left
            {
                count++;
            }


        }
        if(kingPos.getPos().getLast().getY()+1>=getBoardSize()||kingPos.getPos().getLast().getY()-1<0||kingPos.getPos().getLast().getX()-1<0||kingPos.getPos().getLast().getX()+1>=getBoardSize())//left
        {
            count++;
        }
        if(count==4) {
            kingPos.getPos().add(new Position(-1,-1));
            return true;
        }
        return false;
    }
    public boolean isKingWinner()
    {
        return isEdge(kingPos.getPos().getLast());
    }
    public void setPositions()
    {
        redPawns.clear();
        bluePawns.clear();
        kingPos=new StatsPieace("K7",new Position(5, 5));
        board[5][5].getIds().add("K7");

        redPawns.add(new StatsPieace("A1",new Position(3, 0)));
        redPawns.add(new StatsPieace("A2",new Position(4, 0)));
        redPawns.add(new StatsPieace("A3",new Position(5, 0)));
        redPawns.add(new StatsPieace("A4",new Position(6, 0)));
        redPawns.add(new StatsPieace("A5",new Position(7, 0)));
        redPawns.add(new StatsPieace("A6",new Position(5, 1)));
        redPawns.add(new StatsPieace("A7",new Position(0, 3)));
        redPawns.add(new StatsPieace("A8",new Position(10, 3)));
        redPawns.add(new StatsPieace("A9",new Position(0, 4)));
        redPawns.add(new StatsPieace("A10",new Position(10, 4)));
        redPawns.add(new StatsPieace("A11",new Position(0, 5)));
        redPawns.add(new StatsPieace("A12",new Position(1, 5)));
        redPawns.add(new StatsPieace("A13",new Position(9, 5)));
        redPawns.add(new StatsPieace("A14",new Position(10, 5)));
        redPawns.add(new StatsPieace("A15",new Position(0, 6)));
        redPawns.add(new StatsPieace("A16",new Position(10, 6)));
        redPawns.add(new StatsPieace("A17",new Position(0, 7)));
        redPawns.add(new StatsPieace("A18",new Position(10, 7)));
        redPawns.add(new StatsPieace("A19",new Position(5, 9)));
        redPawns.add(new StatsPieace("A20",new Position(3, 10)));
        redPawns.add(new StatsPieace("A21",new Position(4, 10)));
        redPawns.add(new StatsPieace("A22",new Position(5, 10)));
        redPawns.add(new StatsPieace("A23",new Position(6, 10)));
        redPawns.add(new StatsPieace("A24",new Position(7, 10)));
        board[3][0].getIds().add("A1");
        board[4][0].getIds().add("A2");
        board[5][0].getIds().add("A3");
        board[6][0].getIds().add("A4");
        board[7][0].getIds().add("A5");
        board[5][1].getIds().add("A6");
        board[0][3].getIds().add("A7");
        board[10][3].getIds().add("A8");
        board[0][4].getIds().add("A9");
        board[10][4].getIds().add("A10");
        board[0][5].getIds().add("A11");
        board[1][5].getIds().add("A12");
        board[9][5].getIds().add("A13");
        board[10][5].getIds().add("A14");
        board[0][6].getIds().add("A15");
        board[10][6].getIds().add("A16");
        board[0][7].getIds().add("A17");
        board[10][7].getIds().add("A18");
        board[5][9].getIds().add("A19");
        board[3][10].getIds().add("A20");
        board[4][10].getIds().add("A21");
        board[5][10].getIds().add("A22");
        board[6][10].getIds().add("A23");
        board[7][10].getIds().add("A24");



        bluePawns.add(new StatsPieace("D1",new Position(5, 3)));
        bluePawns.add(new StatsPieace("D2",new Position(4, 4)));
        bluePawns.add(new StatsPieace("D3",new Position(5, 4)));
        bluePawns.add(new StatsPieace("D4",new Position(6, 4)));
        bluePawns.add(new StatsPieace("D5",new Position(3, 5)));
        bluePawns.add(new StatsPieace("D6",new Position(4, 5)));
        bluePawns.add(new StatsPieace("D8",new Position(6, 5)));
        bluePawns.add(new StatsPieace("D9",new Position(7, 5)));
        bluePawns.add(new StatsPieace("D10",new Position(4, 6)));
        bluePawns.add(new StatsPieace("D11",new Position(5, 6)));
        bluePawns.add(new StatsPieace("D12",new Position(6, 6)));
        bluePawns.add(new StatsPieace("D13",new Position(5, 7)));
        board[5][3].getIds().add("D1");
        board[4][4].getIds().add("D2");
        board[5][4].getIds().add("D3");
        board[6][4].getIds().add("D4");
        board[3][5].getIds().add("D5");
        board[4][5].getIds().add("D6");
        board[6][5].getIds().add("D8");
        board[7][5].getIds().add("D9");
        board[4][6].getIds().add("D10");
        board[5][6].getIds().add("D11");
        board[6][6].getIds().add("D12");
        board[5][7].getIds().add("D13");



    }
    public void printMoves(boolean kingDefeated) {
        List<StatsPieace> blueList =  new ArrayList<StatsPieace>();
        List<StatsPieace> redList = new ArrayList<StatsPieace>();
        for(int i=0;i<redPawns.size();i++)
        {
            if(redPawns.get(i).getSteps()>=1)
                redList.add(redPawns.get(i));
        }
        for(int i=0;i<bluePawns.size();i++)
        {   if(bluePawns.get(i).getSteps()>=1)
            blueList.add(bluePawns.get(i));
        }

        if(kingPos.getPos().getLast().isSame(new Position(-1,-1)))
            kingPos.getPos().removeLast();

        Collections.sort(blueList, StatsPieace.sortByStepsAndId);
        Collections.sort(redList, StatsPieace.sortByStepsAndId);
        boolean flag=true;
        for(int a=0;a<blueList.size();a++)
        {
        if(blueList.get(a).getSteps()>=kingPos.getSteps()) {
            if (blueList.get(a).getSteps() > kingPos.getSteps()) {
                if (kingPos.getSteps() >= 1 && flag) {
                    blueList.add(a, kingPos);
                    flag = false;
                }
            }
            if ((Integer.parseInt(blueList.get(a).getId().substring(1)) > 7) && flag) {
                if (kingPos.getSteps() >= 1 && flag) {
                    blueList.add(a, kingPos);
                    flag = false;
                }
            }
        }
        }
        if(flag)
        {
            if (kingPos.getSteps() >= 1)
            {
                blueList.add(kingPos);
            }
        }

        if(kingDefeated)
        {
                for (int i = 0; i < redList.size(); i++)
                {
                        System.out.println(redList.get(i).toStringMoves());
                }

                for (int i = 0; i < blueList.size(); i++)
                {
                        System.out.println(blueList.get(i).toStringMoves());

                }

            }

        else
        {

                for (int i = 0; i < blueList.size(); i++) {
                        System.out.println(blueList.get(i).toStringMoves());

                }
                for (int i = 0; i < redList.size(); i++) {
                        System.out.println(redList.get(i).toStringMoves());

                }


            }
    if(redList.size()>0||blueList.size()>0)
        printStars();
    }

    public void printKills(boolean kingDefeated) {

        List<StatsPieace> blueList =  new ArrayList<StatsPieace>();
        List<StatsPieace> redList = new ArrayList<StatsPieace>();

        if(kingPos.getKills()>=1)
            blueList.add(kingPos);

        for(int i=0;i<redPawns.size();i++)
        {       if(redPawns.get(i).getKills()>=1)
                redList.add(redPawns.get(i));
        }
        for(int i=0;i<bluePawns.size();i++)
        {       if(bluePawns.get(i).getKills()>=1)
                blueList.add(bluePawns.get(i));
        }


        Collections.sort(blueList, StatsPieace.sortByKillsAndId);
        Collections.sort(redList, StatsPieace.sortByKillsAndId);

        List<StatsPieace> squaresPieaces = new ArrayList<StatsPieace>();
        int i=0;
        int j=0;
        int blueID=0;
        int redID=0;
        while(i<redList.size()&&j<blueList.size())
        {
            if (blueList.get(j).getKills() < redList.get(i).getKills())//blue
            {
                squaresPieaces.add(redList.get(i));
                i++;

            }
            else if (blueList.get(j).getKills() > redList.get(i).getKills())//blue
            {
                squaresPieaces.add(blueList.get(j));

                j++;

            }
            else //blue
            {
                blueID=Integer.parseInt((blueList.get(j).getId().substring(1)));
                redID=Integer.parseInt((redList.get(i).getId().substring(1)));

                if(blueID<redID)
                {
                    squaresPieaces.add(blueList.get(j));
                    j++;
                } else if (blueID>redID) {
                    squaresPieaces.add(redList.get(i));
                    i++;
                }
                else {


                    if (kingDefeated) {
                        squaresPieaces.add(redList.get(i));
                        i++;

                    } else {
                        squaresPieaces.add(blueList.get(j));
                        j++;

                    }
                }
            }
        }


        for(i=i;i<redList.size();i++)
        {
            squaresPieaces.add(redList.get(i));

        }

        for(j=j;j<blueList.size();j++)
        {
            squaresPieaces.add(blueList.get(j));
        }



            for (int a = 0; a < squaresPieaces.size(); a++)
            {

                System.out.println(squaresPieaces.get(a).toStringKills());
            }




        if(squaresPieaces.size()>0)
            printStars();
        }
    public void printStars() {

            System.out.println("***************************************************************************");
    }

    public void printSquares(boolean kingDefeated) {
        // Specify the file name
        boolean isAllEqual=true;

        List<StatsPieace> blueList =  new ArrayList<StatsPieace>();


        List<StatsPieace> redList = new ArrayList<StatsPieace>();
        for(int i=0;i<redPawns.size();i++)
        {
            if(redPawns.get(i).getSquares()!=0)
                redList.add(redPawns.get(i));
        }
        for(int i=0;i<bluePawns.size();i++)
        {
            if(bluePawns.get(i).getSquares()!=0)
                blueList.add(bluePawns.get(i));
        }

        Collections.sort(blueList, StatsPieace.sortBySquaresAndId);
        Collections.sort(redList, StatsPieace.sortBySquaresAndId);
        boolean flag=true;
        for(int a=0;a<blueList.size();a++)
        {
            if(blueList.get(a).getSquares()<=kingPos.getSquares())
            {
                if(blueList.get(a).getSquares()<kingPos.getSquares())
                {
                    if(kingPos.getSquares()>=1&&flag) {
                        blueList.add(a, kingPos);
                        flag = false;
                    }
                }
                if((Integer.parseInt(blueList.get(a).getId().substring(1))>7)&&flag)
                {
                    if(kingPos.getSquares()>=1&&flag) {
                        blueList.add(a, kingPos);
                        flag = false;
                    }
                }

            }

        }


        List<StatsPieace> squaresPieaces = new ArrayList<StatsPieace>();
        int i=0;
        int j=0;
        int blueID=0;
        int redID=0;

        while(i<redList.size()&&j<blueList.size())
        {
            if (blueList.get(j).getSquares() < redList.get(i).getSquares())//blue
            {
                squaresPieaces.add(redList.get(i));
                i++;

            }
            else if (blueList.get(j).getSquares() > redList.get(i).getSquares())//blue
            {
                squaresPieaces.add(blueList.get(j));
                j++;

            }

            else //blue
            {
                blueID=Integer.parseInt((blueList.get(j).getId().substring(1)));
                redID=Integer.parseInt((redList.get(i).getId().substring(1)));

                if(blueID<redID)
                {
                    squaresPieaces.add(blueList.get(j));
                    j++;
                } else if (blueID>redID) {
                    squaresPieaces.add(redList.get(i));
                    i++;
                }
                else {


                    if (kingDefeated) {
                        squaresPieaces.add(redList.get(i));
                        i++;

                    } else {
                        squaresPieaces.add(blueList.get(j));
                        j++;

                    }
                }
            }
        }


            for(i=i;i<redList.size();i++)
            {
                squaresPieaces.add(redList.get(i));

            }

            for(j=j;j<blueList.size();j++)
            {
            squaresPieaces.add(blueList.get(j));
            }



            for (int a = 0; a < squaresPieaces.size(); a++)
            {
                System.out.println(squaresPieaces.get(a).toStringSquares());
            }


        if(squaresPieaces.size()>0)
            printStars();

    }

    public void printPieces() {
        List<Square> listSquares=new ArrayList<>();

        for(int i=0;i<board.length;i++)
        {
            for(int j=0;j<board.length;j++)
            {
                if(board[i][j].getIds().size()>=2)
                {
                    listSquares.add(board[i][j]);
                }
            }
        }

        Collections.sort(listSquares, Square.sortBySizeAndXY);


            String text="";
            for (int i = 0; i < listSquares.size(); i++)
            {
                System.out.println(listSquares.get(i).toString());

            }
        if(listSquares.size()>0)
            printStars();

    }
    public void initSquareArr()
    {
        for(int i=0;i<board.length;i++)
        {
            for(int j=0;j<board.length;j++)
            {
                board[i][j]=new Square(i,j);
            }
        }
    }
    public boolean checkIfWinner()
    {
        if(isKingDefeated()) {
            player2.newWin();
            printMoves(true);
            printKills(true);
            printSquares(true);
            printPieces();

            isVictory=true;
            return true;
        }
        if (isKingWinner())
        {
            player1.newWin();

            printMoves(false);
            printKills(false);
            printSquares(false);
            printPieces();

            isVictory=true;
            return true;
        }
        return false;

    }

    public StatsPieace findByID(String id)
    {

        for(int i=0;i<bluePawns.size();i++)
        {
            if(bluePawns.get(i).getId()==id)
            {
                return bluePawns.get(i);
            }
        }
        for(int j=0;j<redPawns.size();j++)
        {
            if(redPawns.get(j).getId()==id)
            {
                return redPawns.get(j);
            }
        }
        return null;

    }
    public void removeLastMove(String id)
    {

        for(int i=0;i<bluePawns.size();i++)
        {
            if(bluePawns.get(i).getId()==id)
            {
                 bluePawns.get(i).getPos().removeLast();
            }
        }
        for(int j=0;j<redPawns.size();j++)
        {
            if(redPawns.get(j).getId()==id)
            {
                redPawns.get(j).getPos().removeLast();
            }
        }

    }
    public void removeOneKill(String id)
    {

        for(int i=0;i<bluePawns.size();i++)
        {
            if(bluePawns.get(i).getId()==id)
            {
                bluePawns.get(i).removeKills(1);
            }
        }
        for(int j=0;j<redPawns.size();j++)
        {
            if(redPawns.get(j).getId()==id)
            {
                redPawns.get(j).removeKills(1);
            }
        }

    }


























}