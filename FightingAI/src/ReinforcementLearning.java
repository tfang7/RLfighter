public class ReinforcementLearning {
	public double[][] qTable;
	private int stateSize, actionSize;	

	public static void main(){
		
	}
	//main RL class
	//todo: figure out states
	//todo: figure out actions
	public ReinforcementLearning(int aSize){
		this.stateSize = StateType.values().length;
		this.actionSize = aSize;
		qTable = new double[this.stateSize][this.actionSize];		
		initQ(qTable);
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
	public void initQ(double[][] table){
		for (int i = 0; i < this.stateSize; i++){
			for (int j = 0; j < this.actionSize; j++){
				table[i][j] = 0.0d;
			}
		}
	}
	
	public void init(){
		
	}
	
	
}
