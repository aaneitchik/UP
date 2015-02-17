import java.io.IOException;

public class Test
{
	public static void main(String[] args) throws IOException
	{
		Juicer myJuicer = new Juicer();
		myJuicer.addRecipes("input.txt");
		
		myJuicer.sortRecipes();
		
		TaskManager.printComponentsByInputOrder(Juicer.foundComponents);
		TaskManager.printComponentsBySymbolsCodes(myJuicer);
		TaskManager.printMinimalNumberOfCleanings(myJuicer);
	}	
}
