import java.awt.Color;
import java.util.ArrayList;

public class Tile
{
	private Piece piece;
	private Color color;
	private Location location;
	private Board board;
	
	public Tile(Color color, Location loc, Board container)
	{
		this.color = color;
		this.location = loc;
		this.board = container;
	}
	
	public void setPiece(Piece p)
	{
		this.piece = p;
	}
	
	public Location getLocation()
	{
		return location.clone();
	}
	
	public void kingPiece()
	{
		if (piece != null)
		{
			piece.king();
		}
	}
	
	public Piece getPiece()
	{
		if (piece != null)
		{
			return piece.clone();
		}
		else
		{
			return null;
		}
	}
	
	public Color getColor()
	{
		return color;
	}

	public boolean hasPiece()
	{
		return ((getPiece() == null) ? false : true);
	}
	
	public boolean isAdjacentTo(Tile other)
	{
		if (getAdjacentTiles().contains(other))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public ArrayList<Tile> getAdjacentTiles()
	{
		ArrayList<Tile> adjList = new ArrayList<>();
		Location loc = this.getLocation();
		int y = loc.y;
		int x = loc.x;
		
		Tile t;
		if ((t = board.tileAt(new Location(y + 1, x + 1))) != null)
		{
			adjList.add(t);
		}
		if ((t = board.tileAt(new Location(y + 1, x - 1))) != null)
		{
			adjList.add(t);
		}
		if ((t = board.tileAt(new Location(y - 1, x + 1))) != null)
		{
			adjList.add(t);
		}
		if ((t = board.tileAt(new Location(y - 1, x - 1))) != null)
		{
			adjList.add(t);
		}
		
		return adjList;
	}
}
