package cn.edu.nju.TicTacToe;
public class Board {
	/**
	 * 成员变量的初始化代码请修改，请灵活选择初始化方式
	 * 必要时可添加成员变量
	 */
	protected char[][] cells;
	protected GameMode chessStrategy;
	protected GameMode winStrategy;
	//protected int currentChess = 0;
	public Player player = Player.X;
	protected int size;
	protected String gameMode;
	//protected String[] toDel = new String[10];

	/**
	 * 请修改构造方法，并添加合适的构造方法
	 */
	public Board(int a, String s){
		cells = new char[a][a];
		for(int i=0; i<a; i++){
			for(int j=0; j<a; j++){
				cells[i][j] = '_';
			}
		}
		gameMode = s;
		size = a;
		switch (gameMode.charAt(0))
		{
			case '0': chessStrategy = new GameChessStrategyM1();break;
			case '1': chessStrategy = new GameChessStrategyM2();break;
		}
		switch (gameMode.charAt(1))
		{
			case '0': winStrategy = new GameWinStrategy_HVD();break;
			case '1': winStrategy = new GameWinStrategy_HV();break;
		}



	}

	/*public Board(int boardSize, GameChessStrategy chessStrategy, GameWinStrategy_HVD winStrategy){
		cells = new char[boardSize][boardSize];
		for(int i=0; i<boardSize; i++){
			for(int j=0; j<boardSize; j++){
				cells[i][j] = '_';
			}
		}

		this.chessStrategy = chessStrategy;
		this.winStrategy = winStrategy;
	}*/

	/**
	 * @param move 下棋的位置
	 * @return 落棋之后的结果
	 */
	public Result nextMove(String move)
	{
		int check = 0;
		/*switch (gameMode.charAt(0))
		{
			case '0':
				check = chessStrategy.putChess(cells, nextPlay(), move);
				break;
			case '1':
				String del = toDel[currentChess % 10];
				toDel[currentChess % 10] = move;
				currentChess++;
				check = chessStrategy.putChess(cells, nextPlay(), move);
				break;
		}*/
		check = chessStrategy.putChess(cells, nextPlay(), move);
		if (check == -1)
		{
			return Result.ERROR;
		} else
		{
			return winStrategy.check(cells);
		}
	}
	
	/**
	 * @return 下一个落棋的玩家
	 */
	protected Player nextPlay(){
		Player res = player;
		player = player == Player.X ? Player.O : Player.X;
		return res;
	}
	
	/**
	 * 棋盘的输出方法，根据需要进行修改
	 */
	public void print(){
		System.out.print(" ");
		for (int i=0; i<size;i++)
		{
			System.out.print(' ');
			System.out.print(Character.toChars('A' + i));
		}
		System.out.println();
		for(int i=0 ;i<size; i++){
			System.out.print(i+1);
			for(int j=0; j<size; j++){
				System.out.print(" "+cells[i][j]);
			}
			System.out.println();
		}
	}
}