import java.io.*;
import java.util.*;

public class Juicer
{
	private class Recipe
	{
		private TreeSet<String> components;
		private boolean alreadyMade = false;
		
		public Recipe()
		{
			components = new TreeSet<String>();
		}
		
		public void addComponent(String component)
		{
			components.add(component);
			foundComponents.add(component);
		}
		
		public String toString()
		{
			return components.toString();
		}
	};
	
	private TreeSet<Recipe> recipes;
	private int cleaningCounter = 0;
	private ArrayList<Recipe> sortedRecipes= new ArrayList<Recipe>();
	
	public static LinkedHashSet<String> foundComponents = new LinkedHashSet<String>();
	
	public Juicer()
	{
		recipes = new TreeSet<Recipe>(new Comparator<Recipe>()
		{
			public int compare(Recipe recipe1, Recipe recipe2)
			{
				int firstSize = recipe1.components.size();
				int secondSize = recipe2.components.size();
				if(recipe1.components.equals(recipe2.components))
					return 0;
				if(firstSize == secondSize)
					return -1;
				return (firstSize - secondSize);
			}	
		});
		cleaningCounter = 0;
	}
	
	public void addRecipes(String filename) throws IOException
	{
		FileReader fr = new FileReader(filename);
		BufferedReader bf = new BufferedReader(fr);
		
		String aLine;
 		while((aLine = bf.readLine()) != null)
		{
			StringTokenizer st = new StringTokenizer(aLine, " .,\"();-?!");
			Recipe temporary = new Recipe();
			while(st.hasMoreTokens())
			{
				temporary.addComponent(st.nextToken());
			}
			recipes.add(temporary);
		}
		bf.close();
	};
	
	public void sortRecipes()
	{
		sortedRecipes = new ArrayList<Recipe>();
		Recipe ifNotFound = new Recipe();
		Iterator<Recipe> it = recipes.iterator();
		boolean somethingFound = true;
		it.next();
		for(Recipe firstRecipe : recipes)
		{
			if(firstRecipe.alreadyMade == false)
			{
				somethingFound = false;
				ifNotFound = firstRecipe;
				for(Iterator<Recipe> it2 = recipes.iterator(); it2.hasNext();)
				{
					Recipe secondRecipe = it2.next();
					if(secondRecipe.components.size() > firstRecipe.components.size())
					{
						if(secondRecipe.alreadyMade == false)
						{
							if(secondRecipe.components.containsAll(firstRecipe.components))
							{
								if(firstRecipe.alreadyMade == false)
									sortedRecipes.add(firstRecipe);
								sortedRecipes.add(secondRecipe);
								firstRecipe.alreadyMade = true;
								secondRecipe.alreadyMade = true;
								firstRecipe = secondRecipe;
								somethingFound = true;
							}
						}
					}
				}
				cleaningCounter++;
				if(somethingFound == false)
				{
					sortedRecipes.add(ifNotFound);
					ifNotFound.alreadyMade = true;
					cleaningCounter++;
				}
			}
		}
	}
	
	public ArrayList<ArrayList<String>> getRecipes()
	{
		ArrayList<ArrayList<String>> newRecipes = new ArrayList<ArrayList<String>>();
		for(Recipe r : recipes)
		{
			ArrayList<String> temp = new ArrayList<String>(r.components);
			newRecipes.add(temp);
		}
		return newRecipes;
	}
	
	public int minimalNumberOfCleanings()
	{
		return cleaningCounter;
	}
}
