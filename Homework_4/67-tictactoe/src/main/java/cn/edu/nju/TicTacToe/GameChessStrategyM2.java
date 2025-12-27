package cn.edu.nju.TicTacToe;

public class GameChessStrategyM2 extends GameMode
{
    /**
     * 落子方法，可根据需要自行调整传入参数
     * @param cells  棋盘对应的char二维数组
     * @param currentPlayer  当前落子的玩家
     * @param chessPos  下棋位置的字符串表示
     */

    private String[] memo = new String[10];
    private int currentChess;

    public GameChessStrategyM2()
    {
        currentChess = 0;
    }
    @Override
    public int putChess(char[][] cells, Player currentPlayer, String chessPos)
    {
        currentChess++;
        String toDel = memo[currentChess%10];
        memo[currentChess%10] = chessPos;
        if (currentChess > 10)
        {
            int k = toDel.charAt(1) - '1';
            int l = toDel.charAt(0) - 'A';
            cells[k][l] = '_';
        }
        int i = chessPos.charAt(1) - '1';
        int j = chessPos.charAt(0) - 'A';
        if (cells[i][j] != '_')
        {
            return -1;
        }
        cells[i][j] = currentPlayer == Player.X ? 'X' : 'O';
        return 0;
    }
}
