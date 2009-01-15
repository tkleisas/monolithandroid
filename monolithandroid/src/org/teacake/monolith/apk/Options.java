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
		this.currentSelectedOption = Options.ALLOPTIONS[0];
	}
	
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
			this.startingLevel = this.game.getLevels()[currentLevelIndex--];
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
	public void nextOption()
	{
		if(currentSelectedOption<ALLOPTIONS.length-1)
		{
			currentSelectedOption++;
		}
		else
		{
			currentSelectedOption =0;
		}
	}
	public void previousOption()
	{
		if(currentSelectedOption>0)
		{
			currentSelectedOption--;
		}
		else
		{
			currentSelectedOption = ALLOPTIONS.length-1;
		}
	}
	public void setNextValue()
	{
		switch(currentSelectedOption)
		{
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
		case OPTION_END:
			break;
			
		}
	}
	public void setPreviousValue()
	{
		switch(currentSelectedOption)
		{
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
		case OPTION_END:
			break;
			
		}		
	}
	public static final int DIFFICULTY_NORMAL = 0;
	public static final int DIFFICULTY_EXPERT = 1;
	private int currentSelectedOption;
	public static final int OPTION_GAMETYPE = 0;
	public static final int OPTION_DIFFICULTY = 1;
	public static final int OPTION_STARTING_LEVEL = 2;
	public static final int OPTION_MUSIC = 3;
	public static final int OPTION_SOUND = 4;
	public static final int OPTION_END = 5;
	public static int[] ALLOPTIONS = {
										OPTION_GAMETYPE,
										OPTION_DIFFICULTY,
										OPTION_STARTING_LEVEL,
										OPTION_MUSIC,
										OPTION_SOUND,
										OPTION_END
										}; 
}
