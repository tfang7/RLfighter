
public enum StateType {
	STANDING(0),
	CROUCHING(1),
	BLOCKING(2),
	ATTACK_BLOCKED(3),
	ATTACK_VULN(4),
	JUMPING(5);
	
	private final int val;
	
	private StateType(int val){
		this.val = val;
	}
	
	public int id()
	{
		return this.val;
	}
	
}
