import enumerate.State;

public class RLState {
	public enumerate.State type;
	public int stateSize;
	public double[] rewards;
	public double[] actionList;
	
	public RLState(State t, int sSize, int aSize)
	{
		this.type = t;
		this.stateSize = sSize;
		this.rewards = new double[aSize];
		init();
	}
	
	private void init(){
		for (int i = 0; i < rewards.length; i++)
		{
			rewards[i] = 0.0d;
		}
	}
	
	

}
