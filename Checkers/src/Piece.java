import java.awt.Color;

public class Piece
{
	protected boolean king;
	
	protected Color color;
	
	public Piece()
	{
		king = false;
		color = null;
	}
	
	public Piece(Color team, boolean king)
	{
		this.color = team;
		this.king = king;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public boolean move()
	{
		return false;
	}
	
	public void setColor(Color c)
	{
		this.color = c;
	}
	
	public Piece clone()
	{
		return new Piece(color, king);
	}
	
	public void king()
	{
		king = true;
	}
	
	public boolean isKing()
	{
		return king;
	}
}