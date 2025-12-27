package cn.edu.nju.TicTacToe;
/**
 * 横竖斜方式获胜对应的类，应该考虑到可扩展性，当有新的获胜模式出现时更易于添加
 * hint：采用接口的方式，接口与实现相分离
 * @author Xin Feng & Qiu Liu
 *
 */
public class GameWinStrategy_HVD extends GameMode {
	/**
	 * 根据需要修改获胜的方法
	 * @param cells  棋盘对应的char二维数组
	 * @return  检测结果
	 */
	@Override
	public Result check(char[][] cells)
	{
		int size = cells[0].length;
		char winChar = 0;
		for(int i=0; i<size; i++)
		{
			for (int j = 0; j < size - 2; j++)
				if (cells[i][j] != '_' &&
						cells[i][j] == cells[i][j + 1] && cells[i][j + 1] == cells[i][j + 2])
				{
					winChar = cells[i][j];
					break;
				}
		}
		
		for(int j=0; winChar == 0 && j<size; j++)
		{
			for (int i = 0; i < size - 2; i++)
			{
				if (cells[i][j] != '_' &&
						cells[i][j] == cells[i+1][j] && cells[i+1][j] == cells[i+2][j])
				{
					winChar = cells[i][j];
					break;
				}
			}
		}

		/*for (int i = 0; i < size; ++i)
		{
			for (int j = i; j < size -2; ++j)
			{
				if (cells[j-i][j] != '_' &&
						cells[j-i+1][j+1] == cells[j-i][j] && cells[j-i+1][j+1] == cells[j-i+2][j+2])
				{
					winChar = cells[j-i][j];
					break;
				}
				if (cells[j][j-i] != '_'&&
						cells[j+1][j-i+1] == cells[j][j-i] && cells[j+1][j-i+1] == cells[j+2][j-i+2])
				{
					winChar = cells[j][j-i];
					break;
				}
				if (cells[size-j-1][j-i]!= '_' &&
						cells[size-j-1][j-i]==cells[size-j-2][j-i+1] && cells[size-j-2][j-i+1]==cells[size-j-3][j-i+2])
				{
					winChar = cells[size-j-1][j-i];
					break;
				}
				if (cells[j-i][size-j-1]!='_'&&
						cells[j-i][size-j-1]==cells[j-i+1][size-j-2] && cells[j-i+1][size-j-2] == cells[j-i+2][size-j-3])
				{
					winChar = cells[j-i][size-j-1];
					break;
				}
			}
		}*/

		for (int i = 0; i < 2 * size - 1; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if ((i - j - 2) >= 0 && (i - j + 2) < size)
				{
					if (cells[i-j][j]!='_' && cells[i-j-1][j+1] == cells[i-j][j] && cells[i-j-1][j+1] == cells[i-j-2][j+2])
					{
						winChar = cells[i-j][j];
						break;
					}
				}
			}
		}
		for (int i = 1 - size; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				if ((i + j - 2) >= 0 && (i + j + 2) < size)
				{
					if (cells[i+j][j] != '_' && cells[i+j+1][j+1] == cells[i+j][j] && cells[i+j+1][j+1] == cells[i+j+2][j+2])
					{
						winChar = cells[i+j][j];
						break;
					}
				}
			}
		}

		
		switch(winChar){
			case 'X': return Result.X_WIN;
			case 'O': return Result.O_WIN;
			default: break;
		}
		
		for(int i = 0; i < size; ++i)
		{
			for(int j = 0; j < size; ++j)
			{
				if(cells[i][j] == '_')
					return Result.GAMING;
			}
		}
		
		return Result.DRAW;
	}
}