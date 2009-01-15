package org.teacake.monolith.apk;

public class Options 
{
	public Options()
	{
		this.setMusicEnabled(true);
		this.setSoundEnabled(true);
		
		this.setGame(new MonolithGameData());
		this.setGameType(game.getGameType());
		this.startingLevel = this.getFirstLevel();
		this.difficultyLevel = Options.DIFFICULTY_NORMAL;
		this.currentSelectedOption = 0;//Options.ALLOPTIONS[0];
		this.previousSelectedOption = 1;
		status = Options.STATUS_SELECTING;
		this.currentSelectedOptionTime = System.currentTimeMillis();
		this.previousSelectedOptionTime = this.currentSelectedOptionTime;
		this.currentSelectedOption = 1;
	}
	private int status;
	private boolean enabledmusic;
	private int difficultyLevel;
	
	public void setDifficultyLevel(int difficultyLevel)
	{
		this.difficultyLevel = difficultyLevel;
	}
	public int getDifficultyLevel()
	{
		return this.difficultyLevel;
	}
	public void setMusicEnabled(boolean flag)
	{
		this.enabledmusic = flag;
	}
	public boolean isMusicEnabled()
	{
		return this.enabledmusic;
	}
	private boolean enabledsound;
	public void setSoundEnabled(boolean flag)
	{
		this.enabledsound = flag;
	}
	public boolean isSoundEnabled()
	{
		return this.enabledsound;
	}
	public void setGame(Game game)
	{
		this.game = game;
	}
	private Game game;
	private int gameType;
	public void setGameType(int gameType)
	{
		this.gameType = gameType;
	}
	public int getGameType()
	{
		return this.gameType;
	}
	private int currentLevelIndex;
	private int startingLevel;
	public void setStartingLevel(int startingLevel)
	{
		this.startingLevel = startingLevel;
	}
	public int getStartingLevel()
	{
		return this.startingLevel;
	}
	public int getFirstLevel()
	{
		return this.game.getLevels()[0];
	}
	public void setFirstLevel()
	{
		this.currentLevelIndex=0;
		this.startingLevel = this.game.getLevels()[currentLevelIndex];
	}
	public void setNextLevel()
	{
		if(this.currentLevelIndex<this.game.getLevels().length)
		{
			this.startingLevel = this.game.getLevels()[this.currentLevelIndex++];
		}
		else
		{
			this.currentLevelIndex = 0;
			this.startingLevel = this.game.getLevels()[0];
		}
	}
	public void setPreviousLevel()
	{
		if(this.currentLevelIndex>0)
		{
			this.startingLevel = this.game.getLevels()[--currentLevelIndex];
		}
		else
		{
			this.currentLevelIndex = this.game.getLevels().length-1;
			this.startingLevel = this.game.getLevels()[this.currentLevelIndex];
		}
	}
	public int getCurrentSelectedOption()
	{
		return this.currentSelectedOption;
	}
	public int getPreviousSelectedOption()
	{
		return this.previousSelectedOption;
	}
	public void nextOption()
	{
		
		this.previousSelectedOption = currentSelectedOption;
		this.previousSelectedOptionTime = currentSelectedOptionTime;
		if(currentSelectedOption<ALLOPTIONS.length-1)
		{
			
			currentSelectedOption++;
		}
		else
		{
		
			currentSelectedOption =0;
		}
		currentSelectedOptionTime = System.currentTimeMillis();
	}
	public void previousOption()
	{
		this.previousSelectedOption = currentSelectedOption;
		this.previousSelectedOptionTime = currentSelectedOptionTime;
		if(currentSelectedOption>0)
		{
			currentSelectedOption--;
		}
		else
		{
			currentSelectedOption = ALLOPTIONS.length-1;
		}
		currentSelectedOptionTime = System.currentTimeMillis();
	}
	public float interpolatePosition(int milliseconds)
	{
		long now = System.currentTimeMillis();
		float retval = 0.0f;
		if(now-this.previousSelectedOptionTime>milliseconds)
		{
			retval = 1.0f;
		}
		else
		{
			retval = ((float)(System.currentTimeMillis()-this.previousSelectedOptionTime))/(float)milliseconds;
		}
		if(this.currentSelectedOption>this.previousSelectedOption)
		{
			return retval;
		}
		else
		{
			return 1.0f-retval;
		}
	}
	public void setNextValue()
	{
		switch(currentSelectedOption)
		{
		case OPTION_BACK:
			
			break;
		case OPTION_GAMETYPE:
			if(this.gameType==Monolith.GAME_MONOLITH)
			{
				gameType=Monolith.GAME_CLASSIC;
			}
			else
			{
				gameType=Monolith.GAME_MONOLITH;
			}
			break;
		case OPTION_DIFFICULTY:
			if(this.difficultyLevel==DIFFICULTY_NORMAL)
			{
				this.difficultyLevel=DIFFICULTY_EXPERT;
			}
			else
			{
				this.difficultyLevel=DIFFICULTY_NORMAL;
			}
			break;
		case OPTION_STARTING_LEVEL:
			this.setNextLevel();
			break;
		case OPTION_MUSIC:
			this.setMusicEnabled(!this.enabledmusic);
			break;
		case OPTION_SOUND:
			this.setSoundEnabled(!this.enabledsound);
			break;
		case OPTION_OK:
			this.status = Options.STATUS_OK;
			break;
			
		}
	}
	public void setPreviousValue()
	{
		switch(currentSelectedOption)
		{
		case OPTION_BACK:
			status = Options.STATUS_BACK;
			break;
		case OPTION_GAMETYPE:
			if(this.gameType==Monolith.GAME_MONOLITH)
			{
				gameType=Monolith.GAME_CLASSIC;
			}
			else
			{
				gameType=Monolith.GAME_MONOLITH;
			}
			break;
		case OPTION_DIFFICULTY:
			if(this.difficultyLevel==DIFFICULTY_NORMAL)
			{
				this.difficultyLevel=DIFFICULTY_EXPERT;
			}
			else
			{
				this.difficultyLevel=DIFFICULTY_NORMAL;
			}
			break;
		case OPTION_STARTING_LEVEL:
			this.setPreviousLevel();
			break;
		case OPTION_MUSIC:
			this.setMusicEnabled(!this.enabledmusic);
			break;
		case OPTION_SOUND:
			this.setSoundEnabled(!this.enabledsound);
			break;
		case OPTION_OK:
			break;
			
		}		
	}
	public static final int DIFFICULTY_NORMAL = 0;
	public static final int DIFFICULTY_EXPERT = 1;
	private int currentSelectedOption;
	private long currentSelectedOptionTime;
	private int previousSelectedOption;
	private long previousSelectedOptionTime;
	public static final int OPTION_BACK = 0;
	public static final int OPTION_GAMETYPE = 1;
	public static final int OPTION_DIFFICULTY = 2;
	public static final int OPTION_STARTING_LEVEL = 3;
	public static final int OPTION_MUSIC = 4;
	public static final int OPTION_SOUND = 5;
	public static final int OPTION_OK = 6;
	public static final int STATUS_SELECTING =0;
	public static final int STATUS_OK = 1;
	public static final int STATUS_BACK = 2;
	public static int[] ALLOPTIONS = {
										OPTION_BACK,
										OPTION_GAMETYPE,
										OPTION_DIFFICULTY,
										OPTION_STARTING_LEVEL,
										OPTION_MUSIC,
										OPTION_SOUND,
										OPTION_OK
										}; 
}
