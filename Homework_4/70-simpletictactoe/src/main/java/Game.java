public class Game {
    
    //游戏主方法playGame
    //输入为对弈双方轮番落子的位置，以英文逗号为间隔，X先走
    public Result playGame(String s){
        board theBoard = new board();
        String[] sequence = s.split(",");
        int player = 0;
        for (String i : sequence)
        {
            theBoard.makeAStep(i, player % 2);
            theBoard.printBoard();
            player++;
        }
		return theBoard.showTheWinner();
    }

    class board
    {
        String [][]board = {{" ","A","B","C"}, {"1","_","_","_"},{"2","_","_","_"},{"3","_","_","_"}};

        public board(){}

        public void printBoard()
        {
            for (int i = 0; i < 4; ++i)
            {
                for (int j = 0; j < 4; ++j)
                {
                    if (j != 3)
                    {
                        System.out.print(this.board[i][j] + " ");
                    } else
                    {
                        System.out.print(this.board[i][j]);
                    }
                }
                System.out.println();
            }
        }

        public void makeAStep(String s, int player)
        {
            int i = 0;
            int j = 0;
            switch (s.charAt(0))
            {
                case 'A': j = 1; break;
                case 'B': j = 2; break;
                case 'C': j = 3; break;
                default: System.out.printf("Unknown step %c.\n", s.charAt(0));
            }
            switch (s.charAt(1))
            {
                case '1': i = 1; break;
                case '2': i = 2; break;
                case '3': i = 3; break;
                default: System.out.printf("Unknown step %c.\n", s.charAt(1));
            }
            if (player == 0)
            {
                this.board[i][j] = "X";
            } else
            {
                this.board[i][j] = "O";
            }
        }

        public Result showTheWinner()
        {
            for (String[] i : this.board)
            {
                if (i[1].equals(i[2]) && i[2].equals(i[3]))
                {
                    if (i[1].equals("X"))
                    {
                        return Result.X_WIN;
                    } else
                    {
                        return Result.O_WIN;
                    }
                }
            }
            for (int j = 0; j < 4; ++j)
            {
               if (this.board[1][j].equals(this.board[2][j]) && this.board[2][j].equals(this.board[3][j]))
               {
                   if (this.board[1][j].equals("X"))
                   {
                       return Result.X_WIN;
                   } else
                   {
                       return Result.O_WIN;
                   }
               }
            }
            if (this.board[1][1].equals(this.board[2][2]) && this.board[2][2].equals(this.board[3][3]))
            {
                if (this.board[1][1].equals("X"))
                {
                    return Result.X_WIN;
                } else
                {
                    return Result.O_WIN;
                }
            }
            if (this.board[1][3].equals(this.board[2][2]) && this.board[2][2].equals(this.board[3][1]))
            {
                if (this.board[1][3].equals("X"))
                {
                    return Result.X_WIN;
                } else
                {
                    return Result.O_WIN;
                }
            }
            for (String[] i : this.board)
            {
                for (String j : i)
                {
                    if (j.equals("_"))
                    {
                        return Result.GAMING;
                    }
                }
            }
            return Result.DRAW;
        }

    }


    public static void main(String[] args){
        Game game = new Game();
        Result result = game.playGame("B2,C2,C1,A3,B3,B1,A2,B2,C3,A1,A3,B3,C2,B1,B2,A2,A1,C1,C3");
        System.out.println(result);
    }
}
