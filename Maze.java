package report4.maze;

import java.util.Random;

/* ��������Թ��㷨
 * ���ÿ����Ԫ������ͨ��ÿ����Ԫ��һ�õ����������������߶�Ϊ1����Ϊcells[i] = -1(��ʾ�߶�Ϊ1��root)��
 * ��ô�漴ѡ��һ��ǽ������cells[0]��cells[1]֮���ǽ���ж����ǵ������Ƿ���ͬ��
 * ���������ͬ����ô����û����ͨ,��ô���ǽ�������������ϲ����������߶�С���ǿ��������߶ȴ����������
 * ������ǵ������ֱ�Ϊcells[i],cells[j], ��j������i���ϣ���cells[j] = i, ��cells[j]������������������iΪ��������һ���֡�
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
	private Mouse[] mouses;  // ֻ��Ϊ�˳�ʼ���� x/y ������

	public Maze()
	{
	    roots = new int[ROWS*COLS];
		cells = new Cell[ROWS*COLS];
		mouses = new Mouse[ROWS*COLS];
		
		int zeroX= 30,zeroY = 30;  // ��ʼ��
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
	
	// ��ʼ���Թ�
	public void initMaze(int roots[], Cell cells[])
	{
		// ��ʼ�������Թ���Ԫ�����
		for(int i=0; i<ROWS*COLS; i++)
		{
			for(int k=0; k<DIRECTION_NUM; k++)
				cells[i].walls[k] = true;
		}
		
		// �����ϽǺ����½ǵ�ǽ�Ƶ�������ʼ��
		cells[0].walls[WEST] = false;
		cells[ROWS*COLS-1].walls[EAST] = false;
		
		for(int i=0; i<ROWS*COLS; i++)
		{
			roots[i] = -1;
		}
	}
	
	// ��������Թ�
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
				if((c1-c1%COLS)/COLS == (ROWS-1)) c2 =-1;  // Ϊ�˵õ�����
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
			
			// ȫ����ͨ���˳�
			if(isConnect(roots,0,CELL_NUM-1)) break;
		}
	}
	
	// �ж�������Ԫ��ʱ������
	public boolean isConnect(final int roots[],int c1, int c2)
	{
		while(roots[c1] >= 0) c1 = roots[c1];
		while(roots[c2] >= 0) c2 = roots[c2];
		if(c1 == c2) 
			return true;
		else
			return false;
	}
	
	// �������������Ԫ��û��������romove ����֮���ǽ
	public void removeDoors(int roots[],int c1, int c2)
	{
		while(roots[c1] >= 0) c1 = roots[c1];
		while(roots[c2] >= 0) c2 = roots[c2];
		
		// �����c2����c1�����ô��c1ճ����c2��
		if(roots[c1] > roots[c2])
			roots[c1] = c2;
		else
		{
			if(roots[c1] == roots[c2])
				roots[c1]--;
			roots[c2] =c1;
		}
	}
	
	
	// ��õ�Ԫ��
	public int[] getRoots()
	{
		return roots;
	}
	
	// �õ�maze��ά���飬�����������Ϣ���ƶ�ά����
	public Cell[] getCells()
	{
		return cells;
	}
	
	public Mouse[] getMouses()
	{
		return mouses;
	}
	
}
