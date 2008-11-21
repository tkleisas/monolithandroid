package org.teacake.monolith.apk;

	public class SimpleGameData implements Game
	{
		public static final int STATUS_STARTUP=0;
		public static final int STATUS_PLAYING=1;
		public static final int STATUS_PAUSE=2;
		public static final int STATUS_GAME_OVER=3;
		public static final int STATUS_EVOLVING=4;
		public SimpleGameData()
		{
			Block.enableMonolithBlocks = false;
			this.score =0;
			this.lines =0;
			this.level =1;
			this.timer = 1000;
			this.timerEnabled = false;
			this.newLevel = 1;
			this.startingLevel = 1;
			this.gridMaxWidth = 10;
			this.gridMaxHeight = 20;
			this.grid = new int[gridMaxWidth][gridMaxHeight];
			for(int y=0;y<gridMaxHeight;y++)
			{
				for(int x=0;x<gridMaxWidth;x++)
				{
					this.grid[x][y]=-1;
				}
			}
			this.clearedLines = new int[gridMaxHeight];
			for(int y=0;y<gridMaxHeight;y++)
			{
				this.clearedLines[y]=0;
			}
		}
			
		public void clearGrid()
		{
			for(int x=0;x<gridMaxWidth;x++)
			{
				for(int y=0;y<gridMaxHeight;y++)
				{
					this.grid[x][y]=-1;
				}
			}
			for(int y=0;y<gridMaxHeight;y++)
			{
				this.clearedLines[y]=0;
			}
		}
		public void initGame(int theStartingLevel)
		{
			this.score = 0;
			this.lines = 0;
			this.level = startingLevel;
			this.newLevel = startingLevel;
			this.startingLevel = theStartingLevel;
			Block newblock = new Block();
			this.currentBlock  = newblock;
			Block newnextblock = new Block();
			this.nextBlock = newnextblock;
			switch(theStartingLevel)
			{
			case 1:
				this.timer = 500;
				break;
			case 2:
				this.timer = 450;
				break;
			case 3:
				this.timer = 400;
				break;
			case 4:
				this.timer = 350;
				break;
			case 5:
				this.timer = 300;
				break;
			case 6:
				this.timer = 250;
				break;
			case 7:
				this.timer = 200;
				break;
			case 8:
				this.timer = 166;
				break;
			case 9:
				this.timer = 133;
				break;
			case 10:
				this.timer = 100;
				break;
			}			
		}
		public void setStatus(int status)
		{
			this.status = status;
		}
		public int getStatus()
		{
			return this.status;
		}
		public int getGridValue(int x, int y)
		{
			return grid[x][y];
		}
		public int getPreviousGridValue(int x, int y)
		{
			return grid[x][y];
		}
		public void setGridValue(int x, int y, int value)
		{
			grid[x][y]=value;
		}		
		public void gameLoop()
		{
			if(this.moveBlockDown())
			{
				this.clearCompleteLines();
				if(this.newLevel!=this.level)
				{
					level=newLevel;
				}
			}
			else
			{
				
				for (int i=0;i<4;i++)
				{
					if (this.grid[this.nextBlock.subblocks[0].xpos+this.nextBlock.xPos][this.currentBlock.subblocks[0].ypos+this.nextBlock.yPos]!=-1)
					{
						this.setStatus(STATUS_GAME_OVER);
						
						return;
					}
				}
				this.currentBlock=this.nextBlock;
				Block bl = new Block();
				
				this.nextBlock= bl;

			}			
		}
		public void moveBlockLeft()
		{
			if(
					(this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos>0) &&
					(this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos>0) &&
					(this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos>0) &&
					(this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos>0)
				)
				{
					if(
						(this.grid[this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos-1][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos]==-1) &&
						(this.grid[this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos-1][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos]==-1) &&
						(this.grid[this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos-1][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos]==-1) &&
						(this.grid[this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos-1][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos]==-1) 
					)
					{
						this.currentBlock.xPos--;
					}
				}			
		}
		public void moveBlockRight()
		{
			if(
					(this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos<gridMaxWidth-1) &&
					(this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos<gridMaxWidth-1) &&
					(this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos<gridMaxWidth-1) &&
					(this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos<gridMaxWidth-1)
				)
				{
					if(
						(this.grid[this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos+1][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos]==-1) &&
						(this.grid[this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos+1][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos]==-1) &&
						(this.grid[this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos+1][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos]==-1) &&
						(this.grid[this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos+1][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos]==-1) 
					)
					{
						this.currentBlock.xPos++;
					}
				}			
		}
		public boolean moveBlockDown()
		{
			for(int i=0;i<4;i++)
			{
				if (this.currentBlock.subblocks[i].ypos+this.currentBlock.yPos>gridMaxHeight+2)
				{
					this.grid[this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos]=this.currentBlock.color;
					this.grid[this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos]=this.currentBlock.color;
					this.grid[this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos]=this.currentBlock.color;
					this.grid[this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos]=this.currentBlock.color;
			
			

					return false;

				}
			}
			if(this.currentBlock.height+this.currentBlock.yPos<gridMaxHeight)
			{
				if(
					(grid[currentBlock.subblocks[0].xpos+currentBlock.xPos][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos+1]==-1) &&
					(grid[currentBlock.subblocks[1].xpos+currentBlock.xPos][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos+1]==-1) &&
					(grid[currentBlock.subblocks[2].xpos+currentBlock.xPos][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos+1]==-1) &&
					(grid[currentBlock.subblocks[3].xpos+currentBlock.xPos][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos+1]==-1) 
				)
				{
					this.currentBlock.yPos++;
					
					return true;
				}
			}
			score++;
			this.grid[this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos]=this.currentBlock.color;
			this.grid[this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos]=this.currentBlock.color;
			this.grid[this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos]=this.currentBlock.color;
			this.grid[this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos]=this.currentBlock.color;
			return false;
		}
		
		public boolean canMoveBlockDown()
		{
			for(int i=0;i<4;i++)
			{
				if (this.currentBlock.subblocks[i].ypos+this.currentBlock.yPos>gridMaxHeight+2)
				{
					return false;

				}
			}
			if(this.currentBlock.height+this.currentBlock.yPos<gridMaxHeight)
			{
				if(
					(grid[currentBlock.subblocks[0].xpos+currentBlock.xPos][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos+1]==-1) &&
					(grid[currentBlock.subblocks[1].xpos+currentBlock.xPos][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos+1]==-1) &&
					(grid[currentBlock.subblocks[2].xpos+currentBlock.xPos][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos+1]==-1) &&
					(grid[currentBlock.subblocks[3].xpos+currentBlock.xPos][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos+1]==-1) 
				)
				{
					return true;
				}
				
			}
			return false;
			
		}		
		public void setTimer(int time)
		{
			this.timer = time;
		}
		private int[][] grid;
		private int[] clearedLines;
		private int status;
		public int score;
		public int lines;
		public int level;
		public int timer;
		public Block currentBlock;
		public Block nextBlock;
		boolean timerEnabled;
		public void clearCompleteLines()
		{
			int currentline =gridMaxHeight-1;
			int linescleared = 0;
			this.newLevel=this.level;
			while(currentline>0)
			{
				boolean linecomplete=true;
				for (int x=0;x<gridMaxWidth;x++)
				{
					if(grid[x][currentline]==-1)
					{
						linecomplete = false;
					}
				}
				if (linecomplete)
				{
					for (int y=currentline;y>0;y--)
					{
						for (int x=0;x<gridMaxWidth;x++)
						{
							if (y>0)
							{
								grid[x][y]=grid[x][y-1];
							}
							else
							{
								grid[x][y]=-1;
							}
						}
					}
					linescleared++;
				}
				else
				{
					currentline--;
				}
			}
			switch(linescleared)
			{
			case 0:
				break;
			case 1:
				score = score + 25;
				break;
			case 2:
				score = score + 75;
				break;

			case 3:
				score = score + 100;
				break;
			case 4:
				score = score + 200;
				break;
			default:
				break;

			}
			lines = lines+linescleared;
			if (lines>20 && lines<=40)
			{
				this.newLevel = 2;
				timer = 900;
				return;
			}
			if (lines>40 && lines<=60)
			{
				this.newLevel = 2;
				timer = 800;
				return;
			}
			if (lines>60 && lines<=80)
			{
				this.newLevel = 3;
				timer = 700;
				return;
			}
			if (lines>80 && lines<=100)
			{
				this.newLevel = 4;
				timer = 600;
				return;
			}
			if (lines>100 && lines<=120)
			{
				this.newLevel = 5;
				timer = 500;
				return;
			}
			if (lines>120 && lines<=140)
			{
				this.newLevel = 6;
				timer = 400;
				return;
			}
			if (lines>140 && lines<=160)
			{
				this.newLevel = 7;
				timer = 300;
				return;
			}
			if (lines>160 && lines<=180)
			{
				this.newLevel = 8;
				timer = 200;
				return;
			}
			if (lines>180 && lines<=200)
			{
				this.newLevel = 9;
				timer = 100;
				return;
			}
			if (lines>200)
			{
				this.newLevel = 10;
				timer = 50;
				return;
			}			
		}
		public void flagCompletedLines()
		{
			for(int y=0;y<gridMaxHeight;y++)
			{

				boolean linecleared=true;
				for(int x=0;x<gridMaxWidth;x++)
				{
					if(this.grid[x][y]==-1)
					{
						linecleared = false;
						break;
					}
				}
				if(linecleared)
				{
					this.clearedLines[y]=1;
				}
				else
				{
					this.clearedLines[y]=0;
				}
			}			
		}
		public int getClearedLineCount()
		{
			int cleared=0;
			for(int line=0;line<gridMaxHeight;line++)
			{
				if(this.clearedLines[line]!=0)
				{
					cleared++;
				}
			}
			return cleared;			
		}
		public void rotateCurrentBlockClockwise()
		{
			int currentorientation = this.currentBlock.orientation;
			this.currentBlock.rotateClockwise();
			this.currentBlock.recalcBlockOrientation();
			for(int i=0;i<4;i++)
			{
				if (this.currentBlock.xPos+this.currentBlock.subblocks[i].xpos>=this.gridMaxWidth)
				{
					this.currentBlock.orientation = currentorientation;
					this.currentBlock.recalcBlockOrientation();
					return;
				}
				if (this.currentBlock.yPos+this.currentBlock.subblocks[i].ypos>= this.gridMaxHeight)
				{
					this.currentBlock.orientation = currentorientation;
					this.currentBlock.recalcBlockOrientation();
					return;

				}
				if (grid[this.currentBlock.subblocks[i].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[i].ypos+this.currentBlock.yPos]!=-1)
				{
					this.currentBlock.orientation = currentorientation;
					this.currentBlock.recalcBlockOrientation();
					return;

				}
			}			
		}
		public void setScore(int score)
		{
			this.score = score;
		}
		public int getScore()
		{
			return this.score;
		}
		public void setLevel(int level)
		{
			this.level = level;
		}
		public int getLevel()
		{
			return this.level;
		}
		public void setLines(int lines)
		{
			this.lines = lines;
			
		}
		public int getLines()
		{
			return this.lines;
		}
		public void setTimerEnabled(boolean flag)
		{
			this.timerEnabled = flag;
		}
		public boolean isTimerEnabled()
		{
			return this.timerEnabled;
		}
		public Block getCurrentBlock()
		{
			return this.currentBlock;
		}
		public Block getNextBlock()
		{
			return this.nextBlock;
		}
		public int getTimer()
		{
			return this.timer;
		}
		public int getEnergy()
		{
			return 0;
		}
		public void setEnergy(int energy)
		{
			//do nothing;
		}
		public int[] getClearedLines()
		{
			return this.clearedLines;
		}
		int newLevel;
		int startingLevel;
		private int step;
		public int getCurrentStep()
		{
			return step;
		}
		public void setStep(int step)
		{
			this.step = step;
		}
		
		private boolean blockPlaced;
		public boolean isBlockPlaced()
		{
			return this.blockPlaced;
		}
		public int gridMaxHeight;
		public int gridMaxWidth;
		public static void main(String[] args) {
			// TODO Auto-generated method stub

		}			

	}




