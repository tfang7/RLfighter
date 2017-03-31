import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import commandcenter.CommandCenter;
import enumerate.Action;
import enumerate.State;
import structs.FrameData;
import structs.GameData;
import structs.Key;
import gameInterface.AIInterface;

/*
 * SAMPLE RANDOM AI
 * 
 * 
 * 
 * 
 * 
 */
public class FightingAI implements AIInterface {

	private Key inputKey;
	private boolean player;
	private FrameData frameData;
	private CommandCenter cc;
	private int myScore;
	private int opponentScore;
	private RLState[] states;
	private Action[] actionAir;
	private Action[] actionGround;
	private Action[] allActions;
	
	private ReinforcementLearning RL;
	
	File file;
	PrintWriter pw;
	BufferedReader br;
	
	private boolean switchFlag;
	
	Random rnd;
	
	@Override
	public int initialize(GameData gameData, boolean playerNumber) {

		
        
        int sSize = enumerate.State.values().length;
		//int aSize = actionAir.length + actionGround.length;
		
        RL = new ReinforcementLearning();
	//	states = new RLState[sSize];

	/*	for (int i = 0; i < sSize; i++){
			states[i] = new RLState(enumerate.State.values()[i], sSize, aSize);
			//System.out.println("made a new state class for " + enumerate.State.values()[i]);
		}*/

        
        
		player = playerNumber;
		inputKey = new Key();
		frameData = new FrameData();
		cc = new CommandCenter();
		
		myScore = 0;
		opponentScore = 0;
		
		//init qtable
		
		
		//init reward table
		rnd = new Random();	
		
		file = new File("data/aiData/Switch/signal.txt");
		
		try {
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			System.out.println("signal:" + line);
			if(line.equals("1"))switchFlag = true;
			else switchFlag = false;
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		return 0;
	}
	
    public boolean canProcessing() {
	    return !frameData.getEmptyFlag() && frameData.getRemainingTimeMilliseconds() > 0;
	  }
  
	@Override
	public void getInformation(FrameData frameData) {
		// TODO Auto-generated method stub
		// calculates and saves scores

/*		if(frameData.getRemainingTimeMilliseconds()>=0 ){
			
			System.out.println("P1:" + this.frameData.getP1().getHp());
			System.out.println("P2:" + this.frameData.getP2().getHp());
			myScore += calculateMyScore(this.frameData.getP1().getHp(),this.frameData.getP2().getHp(),player);
			opponentScore += calculateOpponentScore(this.frameData.getP1().getHp(),this.frameData.getP2().getHp(),player);
		}
*/
		this.frameData = frameData;
		cc.setFrameData(this.frameData, player); 

	}

	@Override
	public void processing() {
		//RL.printQ();
		// TODO Auto-generated method stub
		if(canProcessing())
		{
			//getCurrentState(this.frameData);
			
			if (cc.getSkillFlag())
			{
				inputKey = cc.getSkillKey();
			}
			else 
			{
				inputKey.empty();
				cc.skillCancel();
				cc.commandCall(Action.STAND_D_DF_FA.name());
				
			}
		}
		
	}

	@Override
	public Key input() {
		// TODO Auto-generated method stub
		return inputKey;
	}

	public void close(){
		try {
			System.out.println("myScore:"+myScore);
			System.out.println("opScore:"+opponentScore);
			
			if(myScore < opponentScore){
				System.out.println("lose");
				pw = new PrintWriter(new BufferedWriter (new FileWriter(file)));
				if(switchFlag){
					pw.println("0");
				}
				else{
					pw.println("1");
				}
				pw.close();
			}
			else{
				System.out.println("win");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private int calculateMyScore(int p1Hp,int p2Hp,boolean playerNumber){
		int score = 0;
		
		if(playerNumber){
			if(p2Hp != 0 || p1Hp != 0)
			{
				score = 100 * p2Hp / (p1Hp + p2Hp);
			}
			else{
				score = 500;
			}
		}
		else{
			if(p2Hp != 0 || p1Hp != 0)
			{
				score = 100 * p1Hp / (p1Hp + p2Hp);
			}
			else{
				score = 500;
			}
		}
		return score;
	}
	//FOR DEBUGGING
	private void getCurrentState(FrameData frameData)
	{
		//StateType out = StateType.STANDING;
		FrameData currentFrame = frameData;
		if (currentFrame == null) currentFrame = this.frameData;
		
		int hp = cc.getMyHP();
		int energy = cc.getMyEnergy();
		int dist = cc.getDistanceX();
		
		enumerate.State s = cc.getMyCharacter().getState();
		
		
		
		/*for (enumerate.State st : enumerate.State.values()){
			System.out.println("State: " + st);
			
		}*/
		//System.out.println("current state: " + s.toString());
		//System.out.println("current hp: " + hp);
		//System.out.println("current energy: " + energy);
		//System.out.println("current dist: " + dist);
		
		//return out;
	}
	private int calculateOpponentScore(int p1Hp,int p2Hp,boolean playerNumber){
		int score = 0;
		if(playerNumber){
			if(p2Hp != 0 || p1Hp != 0)
			{
				score = 1000 * p1Hp / (p1Hp + p2Hp);
			}
			else{
				score = 500;
			}
		}
		else{
			if(p2Hp != 0 || p1Hp != 0)
			{
				score = 1000 * p2Hp / (p1Hp + p2Hp);
			}
			else{
				score = 500;
			}
		}
		return score;
	}
	
	private void randomProcessing(){
		

		System.out.println("Dist to enemy " + cc.getDistanceX());
		if (cc.getDistanceX() < 100){
			cc.commandCall("2 3 6 _ B");

		}
	
		/*
		inputKey.A = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.B = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.C = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.U = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.D = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.L = (rnd.nextInt(10) > 4) ? true : false;
		inputKey.R = (rnd.nextInt(10) > 4) ? true : false;*/
	//	for (Action a: Action.)
	}
	
/*	private void copyProcessing(){
		if(!frameData.getEmptyFlag()){
			if(frameData.getRemainingTime()>0){
				// gets the opponent's Key.
				
				inputKey = frameData.getKeyData().getOpponentKey(player);
				
				inputKey.R = inputKey.R ? false : true;
				inputKey.L = inputKey.L ? false : true;
			}
		}
	}*/

	@Override
	public String getCharacter() {
		// TODO Auto-generated method stub
		return CHARACTER_ZEN;
	}
	
}