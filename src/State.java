
public class State {
	public StateType type;
	public int stateSize;
	public double[] rewards;
	public double[] actionList;
	
	public State(StateType t, int sSize, int aSize)
	{
		this.type = t;
		this.stateSize = sSize;
		this.rewards = new double[aSize];
	}
	
	

}
