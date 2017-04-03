import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

import commandcenter.CommandCenter;
import enumerate.Action;
import enumerate.State;
import structs.CharacterData;
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
	private int oldEnemyHealth;
	
	
	double alpha, gamma;
	
	
	int stateIndex;
	Action next;
	boolean cost;
	private CharacterData character;
	private ReinforcementLearning RL;
	
	private State lastState;
	boolean roundStarted;
	private int lastAction;
	File file;
	String fp;
	PrintWriter pw;
	BufferedReader br;
	Random rnd;
	int r = -100;
	int roundNumber = 0;
	//-n 10 --c1 ZEN --c2 ZEN --a1 FightingAI --a2 FightingAI --fastmode

	@Override
	public int initialize(GameData gameData, boolean playerNumber) {

        //int sSize = State.values().length;
		//int aSize = actionAir.length + actionGround.length;
		oldEnemyHealth = -1;

        RL = new ReinforcementLearning();

		//System.out.println("Scanning from file");
     //   System.out.println(RL.printQ());
        
        
        rnd = new Random();

        roundStarted = false;
        alpha = 0.6d;
        gamma = 0.5d;
        
		player = playerNumber;
		inputKey = new Key();
		frameData = new FrameData();
		cc = new CommandCenter();
		
		myScore = 0;
		opponentScore = 0;
		
		//init qtable
		
		
		//check for qtable file
		rnd = new Random();	
		fp = "data/aiData/qData/tableData.txt";
		file = new File(fp);
		if (file.exists()){
			try {
				Scanner scan = new Scanner(file);
				if (scan.hasNextDouble()){
					for (int i = 0; i < 4; i++){
						for (int j = 0; j < 40; j++){
							if (scan.hasNextDouble())
								RL.qTable[i][j] = scan.nextDouble();
						}
					}
				}
				//Initialized with q table:
				System.out.println(RL.printQ());
				scan.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else{
			//file.createNewFile();
			System.out.println("created a new file");

			
			
			
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
/*		System.out.println("remaining time" + frameData.getRemainingTimeMilliseconds() );
		if (frameData.getRemainingTimeMilliseconds() <= 1000){
			System.out.println("Round over");
		}*/
		
		if (roundStarted == false && frameData.getRemainingTimeMilliseconds() > 58000)
		{
			roundStarted = true;
		}
		if (roundStarted == true && frameData.getRemainingTimeMilliseconds() < 2000)
		{
			
			//RL.printQ();
			double average = RL.averageQ();
			
			roundNumber++;
			System.out.println(roundNumber + " " + average);
			try {
				BufferedWriter chartDataOut = new BufferedWriter(new FileWriter("data/aiData/qData/chartData.txt", true));
				chartDataOut.write(roundNumber + " " + average + '\n');
				chartDataOut.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			roundStarted = false;
			try(  PrintWriter out = new PrintWriter( fp )  ){
			    out.println( RL.printQ() );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (canProcessing())
		{
			character = cc.getMyCharacter();
			stateIndex = character.getState().ordinal();
		}

		this.frameData = frameData;
		cc.setFrameData(this.frameData, player); 

	}

	@Override
	public void processing() {

		//RL.printQ();
		// TODO Auto-generated method stub
		if(canProcessing())
		{
			qLearn();
		}
	}
	public void qLearn(){
		//getCurrentState(this.frameData);
		if (cc.getSkillFlag())
		{
			//CHECK LAST ACTION HERE
			inputKey = cc.getSkillKey();
			//this is q
			lastState = character.getState();
			if (r >= 0) lastAction = r;
		//	System.out.println(character.getAction() + "<" + character.getLastCombo());
		//	System.out.println("Last action was" + lastAction + "<" + "LastState" + lastState);
		}
		else 
		{
			inputKey.empty();
			cc.skillCancel();
			
			r = rnd.nextInt(RL.actions.length);
			
			while (RL.qTable[stateIndex][r] < 0 
				   && (r < 0 && r > RL.actions.length) 
				   && character.energy + RL.energyCosts[r] < 0)
			{
				r = rnd.nextInt();
			}
			//this is q'
			next = RL.actions[r];
			if (r < RL.actionAir.length){
				if (stateIndex != State.AIR.ordinal())
					stateIndex = State.AIR.ordinal();
			}
			else{
				if (stateIndex == State.AIR.ordinal())
					stateIndex = State.STAND.ordinal();
			}
		/*	lastState = character.getState();
			lastAction = character.getAction();*/
/*			
			if(oldEnemyHealth != - 1)
			{
				RL.states[stateIndex].rewards[r] -= cc.getEnemyHP() - oldEnemyHealth;
			}
			*/
			oldEnemyHealth = cc.getEnemyHP();
			if (lastState != null && lastAction >= 0)
			{
				 //RL.states[stateIndex].rewards[r] + (gamma * maxQ(character.getState()));
				double t = computeQ(lastState, State.values()[stateIndex], lastAction, r);
				//System.out.println("new q value: " + t);
				RL.qTable[stateIndex][r] = t;
				//System.out.println("new q value: " + q );
			}
			cc.commandCall(next.name());
		}
		
	}
	@Override
	public Key input()
	{
		return inputKey;
	}
	//this function is not finished yet
	public double computeQ(State sLast, State sNext, int aLast, int aNext )
	{
		//System.out.println("Total Length " + RL.actions.length  + "," + alpha + "; Gamma:" + gamma);
		double q,qMax, qNext,reward;	
		
		//Index out of range bug somewhere in this function, possible from reward function
		qMax = maxQ(sNext);
		
		q = RL.qTable[sLast.ordinal()][aLast];
		reward = RL.qTable[sNext.ordinal()][aNext];
		//System.out.println("(" + sLast.ordinal()+"," + aLast +")" + "q: " + q);
	    qNext = RL.qTable[sNext.ordinal()][aNext];
		//rNext = states[sNext.ordinal()].rewards[aNext];
		
	    //System.out.println("(" + sNext.ordinal()+"," + aNext +")" + "Qn: " + qNext );
		return q + alpha * (reward + gamma * qMax - q);
	}
	public double maxQ(State s)
	{
		double max = 0;
		int bestIndex = 0;
		for (int i = 0; i < RL.actions.length; i++)
		{
			if (RL.qTable[s.ordinal()][i] > max)
			{
				max = RL.qTable[s.ordinal()][i];
				bestIndex = i;
			}
		}
		return max;
	}
	public void close(){
		try {
			System.out.println("myScore:"+myScore);
			System.out.println("opScore:"+opponentScore);
			
			if(myScore < opponentScore){
				System.out.println("lose");
				
				pw = new PrintWriter(new BufferedWriter (new FileWriter(file)));
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
	public int getActionIndex(Action a){
		Action[] actList = RL.actions;
		for (int i = 0; i < actList.length; i++)
		{
			if (actList[i] == a)
				return i;
		}
		return 0;
	}
	private int calculateMyScore(int p1Hp,int p2Hp,boolean playerNumber)
	{
		int score = 0;
		
		if(playerNumber)
		{
			if(p2Hp != 0 || p1Hp != 0)
			{
				score = 100 * p2Hp / (p1Hp + p2Hp);
			}
			else{
				score = 500;
			}
		}
		else
		{
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
		
		State s = cc.getMyCharacter().getState();
		
		
		
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
	
	@Override
	public String getCharacter() {
		// TODO Auto-generated method stub
		return CHARACTER_ZEN;
	}
	
}