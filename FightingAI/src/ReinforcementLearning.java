public class ReinforcementLearning {
	public double[][] qTable;
	private int stateSize, actionSize;
	
	public static void main(){
		
	}
	//main RL class
	//todo: figure out states
	//todo: figure out actions
	public ReinforcementLearning(int sSize,int aSize){
		this.stateSize = sSize;
		this.actionSize = aSize;
		qTable = new double[this.stateSize][this.actionSize];
		
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
