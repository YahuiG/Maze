package report4.maze;

import java.util.Random;

/* 随机生成迷宫算法
 * 起初每个单元都不连通，每个单元是一棵单独的树的树根，高度为1，记为cells[i] = -1(表示高度为1的root)。
 * 那么随即选中一面墙，比如cells[0]和cells[1]之间的墙，判断它们的树根是否相同，
 * 如果树根不同，那么它们没有连通,那么拆掉墙，将这两棵树合并起来，将高度小的那棵树并到高度大的树的树上
 * 如果它们的树根分别为cells[i],cells[j], 将j树并到i树上，则cells[j] = i, 即cells[j]不再是树根，而是以i为根的树的一部分。
 */
public class Maze 
{
	public static final int ROWS = 60;
	public static final int COLS = 60;
	public static final int DIRECTION_NUM = 4;
	public static final int EAST = 0;
	public static final int SOUTH = 1;
	public static final int WEST = 2;
	public static final int NORTH = 3;
	public static final int WIDTH =10;
	
	private int[] roots;
	private Cell[] cells;
	private Mouse[] mouses;  // 只是为了初始化其 x/y 的坐标

	public Maze()
	{
	    roots = new int[ROWS*COLS];
		cells = new Cell[ROWS*COLS];
		mouses = new Mouse[ROWS*COLS];
		
		int zeroX= 30,zeroY = 30;  // 起始点
		for(int i=0; i< ROWS*COLS; i++)
		{
			int x = zeroX + (i%COLS)*WIDTH;
	        int y = zeroY + ((i-i%COLS)/COLS)*WIDTH;
			Cell cell = new Cell(x, y);
			Mouse mouse = new Mouse(x, y);
			cells[i] = cell;
			mouses[i] = mouse;
		}
	}
	
	// 初始化迷宫
	public void initMaze(int roots[], Cell cells[])
	{
		// 初始化所有迷宫单元格的门
		for(int i=0; i<ROWS*COLS; i++)
		{
			for(int k=0; k<DIRECTION_NUM; k++)
				cells[i].walls[k] = true;
		}
		
		// 将左上角和右下角的墙推倒，即起始点
		cells[0].walls[WEST] = false;
		cells[ROWS*COLS-1].walls[EAST] = false;
		
		for(int i=0; i<ROWS*COLS; i++)
		{
			roots[i] = -1;
		}
	}
	
	// 随机生成迷宫
	public void createMaze(int roots[], Cell cells[])
	{
		int direction, c1;
		int c2 = -1;
		int CELL_NUM = ROWS*COLS;
		initMaze(roots,cells);
		Random rand = new Random();
		while(true)
		{
			c1 = rand.nextInt(CELL_NUM);
			direction = rand.nextInt(DIRECTION_NUM);
			switch(direction)
			{
			case EAST:
			{
				if(c1%COLS == COLS-1) c2 = -1;
				else c2 = c1+1;
				break;
			}
			case SOUTH:
			{
				if((c1-c1%COLS)/COLS == (ROWS-1)) c2 =-1;  // 为了得到整数
				else c2 = c1+COLS;
				break;
			}
			case WEST:
			{
				if(c1%COLS == 0) c2 = -1;
				else c2 = c1-1;
				break;
			}
			case NORTH:
			{
				if((c1-c1%COLS)/COLS == 0) c2 =-1;
				else c2 = c1-COLS;
				break;
			}
			default:
				System.out.println("Romdon number is error!!!");
				System.exit(0);
				break;
			}
			
			if(c2 < 0) continue;
			
			if(isConnect(roots,c1,c2)) continue;
			else 
			{
				removeDoors(roots,c1,c2);
				cells[c1].walls[direction] = false;
				cells[c2].walls[(direction+2)%DIRECTION_NUM] = false;
			}
			
			// 全部连通后退出
			if(isConnect(roots,0,CELL_NUM-1)) break;
		}
	}
	
	// 判断两个单元格时候相连
	public boolean isConnect(final int roots[],int c1, int c2)
	{
		while(roots[c1] >= 0) c1 = roots[c1];
		while(roots[c2] >= 0) c2 = roots[c2];
		if(c1 == c2) 
			return true;
		else
			return false;
	}
	
	// 如果相邻两个单元格没有相连，romove 他们之间的墙
	public void removeDoors(int roots[],int c1, int c2)
	{
		while(roots[c1] >= 0) c1 = roots[c1];
		while(roots[c2] >= 0) c2 = roots[c2];
		
		// 如果树c2比树c1还深，那么树c1粘附到c2上
		if(roots[c1] > roots[c2])
			roots[c1] = c2;
		else
		{
			if(roots[c1] == roots[c2])
				roots[c1]--;
			roots[c2] =c1;
		}
	}
	
	
	// 获得单元格
	public int[] getRoots()
	{
		return roots;
	}
	
	// 得到maze二维数组，利用里面的信息绘制二维数组
	public Cell[] getCells()
	{
		return cells;
	}
	
	public Mouse[] getMouses()
	{
		return mouses;
	}
	
}
