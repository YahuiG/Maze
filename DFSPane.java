package report4.maze;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JPanel;

public class DFSPane extends JPanel
{
	public static final int EAST = 0;
	public static final int SOUTH = 1;
	public static final int WEST = 2;
	public static final int NORTH = 3;
	public static final int ROWS = Maze.ROWS;
	public static final int COLS = Maze.COLS;
	
	private Maze maze;
	private Cell[] cells;
	private Stack<Integer> path;
	private ArrayList<Integer> temp;
	private int current=0;
	
	public DFSPane()
	{
		maze = new Maze();
		cells = new Cell[ROWS*COLS];
		maze.createMaze(maze.getRoots(), maze.getCells());
		cells = maze.getCells();
		path = new Stack<Integer>();
		temp = new ArrayList<Integer>();
		visit(cells,0,ROWS*COLS-1);
		
		new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				int size = getMazePath().size();
				for(int i=0; i< size; i++)
				{
					current =getMazePath().get(i);
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DFSPane.this.repaint();
				}
			}
		}).start();
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(ROWS*WIDTH,COLS*WIDTH);
	}
	
	public void paintComponent(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		for(int i =0; i< Maze.ROWS*Maze.COLS; i++)
		{
			maze.getCells()[i].drawCell(g);
		}
		maze.getMouses()[current].drawMouse(g);
	}
	
	// 起点从(0,0)开始，终点到(ROWS-,COLS-1)为止
	public void visit(Cell[] cells,int c1,int c2)
	{
		path.push(c1);
		cells[c1].visited =true;
		while(!path.isEmpty())
		{
			int c = path.peek();      // 访问栈顶元素
			cells[c].visited = true;  // 将单元格标记为访问
			temp.add(c);             // 将访问的元素存进列表中
			
			if(path.peek() == c2)
				break;
			
			for(int i=0; i< 4; i++)
			{
				// 有墙，continue
		    	if(cells[c].walls[i] == true)
		    		continue;
		    	
		    	// 无墙压入栈
		    	switch(i)
		    	{
		    	case EAST:
		    		if(c%COLS != COLS-1 && cells[c+1].visited == false)
		    		{
		    			path.push(c+1);
		    		}
		    		break;
		    	case SOUTH:
		    		if((c-c%COLS)/COLS != (ROWS-1) && cells[c+COLS].visited == false)
		    		{
		    			path.push(c+COLS);
		    		}	
		    		break;
		    	case WEST:
		    		if(c%COLS != 0 && cells[c-1].visited == false)
		    		{
		    			path.push(c-1);
		    		}
		    			
		    		break;
		    	case NORTH:
		    		if((c-c%COLS)/COLS != 0 && cells[c-COLS].visited == false)
		    		{
		    			path.push(c-COLS);
		    		}
		    		break;
		    	}	
			}
		
			if(path.peek() == c)
	    		path.pop();
		}
	}
	
	public ArrayList<Integer> getMazePath()
	{
		return temp;
	}
}
