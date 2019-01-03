public class Location
{
	public int y;
	public int x;
	
	public Location(int y, int x)
	{
		this.y = y;
		this.x = x;
	}
	
	public Location clone()
	{
		return new Location(y, x);
	}
}