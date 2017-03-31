import enumerate.Action;
import enumerate.State;

public class ReinforcementLearning {
	public double[][] qTable;
	private int stateSize, actionSize;	
	
	public Action[] actions;
	public RLState[] states;
	
	
	private Action[] actionAir;
	private Action[] actionGround;
	
	public static void main(){
		
	}
	//main RL class
	//todo: figure out states
	//todo: figure out actions
	public ReinforcementLearning(){
		this.stateSize = enumerate.State.values().length;//StateType.values().length;
		//this.actionSize = aSize;
		setActions();
		qTable = new double[this.stateSize][this.actionSize];		
		initQ(qTable);
		printQ();
	}
	
	public void printQ(){
		if (this.qTable == null) return;
		String finalTable = "";
		for (int i = 0; i < this.stateSize; i++){
			for (int j = 0; j < this.actionSize; j++){
				finalTable += (" " + qTable[i][j]);
			}
			finalTable+=("\n");
		}
		System.out.println(finalTable);
	}
	
	public void setActions()
	{
	    actionAir =
	            new Action[] {Action.AIR_GUARD, Action.AIR_A, Action.AIR_B, Action.AIR_DA, Action.AIR_DB,
	                Action.AIR_FA, Action.AIR_FB, Action.AIR_UA, Action.AIR_UB, Action.AIR_D_DF_FA,
	                Action.AIR_D_DF_FB, Action.AIR_F_D_DFA, Action.AIR_F_D_DFB, Action.AIR_D_DB_BA,
	                Action.AIR_D_DB_BB};
        actionGround =
	            new Action[] {Action.STAND_D_DB_BA, Action.BACK_STEP, Action.FORWARD_WALK, Action.DASH,
	                Action.JUMP, Action.FOR_JUMP, Action.BACK_JUMP, Action.STAND_GUARD,
	                Action.CROUCH_GUARD, Action.THROW_A, Action.THROW_B, Action.STAND_A, Action.STAND_B,
	                Action.CROUCH_A, Action.CROUCH_B, Action.STAND_FA, Action.STAND_FB, Action.CROUCH_FA,
	                Action.CROUCH_FB, Action.STAND_D_DF_FA, Action.STAND_D_DF_FB, Action.STAND_F_D_DFA,
	                Action.STAND_F_D_DFB, Action.STAND_D_DB_BB};
        
		int aLength = actionAir.length;
		int gLength = actionGround.length;
		int totalLength = aLength + gLength;
		this.actionSize = totalLength;
		System.out.println("A Length: " + aLength + "," + "G length" + gLength + ", total" + totalLength);
        actions = new Action[aLength + gLength];
        //compile action list;
        for (int i = 0; i < aLength; i++)
        {
        	actions[i] = actionAir[i];
        }
       
        for (int j = aLength; j < totalLength; j++)
        {
        	actions[j] = actionGround[j - aLength];
        }
        
        /*  for (int k = 0; k < totalLength; k++)
        {
        	System.out.println(actions[k]);
        }
         */
    	states = new RLState[stateSize];
    	

    	for (int i = 0; i < stateSize; i++){
    			
			states[i] = new RLState(enumerate.State.values()[i], this.stateSize, this.actionSize);
			System.out.println("i " + i + ":" + enumerate.State.values()[i]);
    	}
		
	}
	public void initQ(double[][] table){
		for (int i = 0; i < this.stateSize; i++){
			//this might be one off, too tired to check right now
			for (int j = 0; j < this.actionSize; j++){
				if (states[i].type != State.AIR && j < actionAir.length){
					table[i][j] = -1.0d;
				}
				else if (states[i].type == State.AIR && j >= actionAir.length)
				{
					table[i][j] = -1.0d;
				}
				else
				{
					table[i][j] = 0.0d;
				}
			}
		}
	}
	
	public void init(){
		
	}
	
	
}
