
public class State {
	public int x, y;
	public int stateSize;
	public double[] rewards;
	public double[] actionList;
	
	public State(int x,int y, int sSize, int aSize)
	{
		this.x = x;
		this.y = y;
		this.stateSize = sSize;
		this.rewards = new double[aSize];
	}
	
	

}
