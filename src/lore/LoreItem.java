package lore;

public class LoreItem
{
	private String name;
	private String description;
	
	public LoreItem()
	{
		name = "Untitled";
	}
	
	public LoreItem(String name)
	{
		this.name = name;
	}
	
	public LoreItem(String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
}
