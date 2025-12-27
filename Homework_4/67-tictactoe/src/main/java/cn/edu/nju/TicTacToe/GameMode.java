package cn.edu.nju.TicTacToe;

public abstract class GameMode{
    public int putChess(char[][] cells, Player nextPlay, String move){return 0;}

    public Result check(char[][] cells){return null;}

    public int putChess(char[][] cells, Player currentPlayer, String chessPos, String toBeRemoved){return 0;}
}
