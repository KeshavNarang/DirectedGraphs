import java.io.*;
import java.util.*;

public class Tester
{
	public static void main (String [] args) throws IOException
	{
		BufferedReader lineReader = new BufferedReader (new FileReader (new File ("edges.txt")));
		String line = lineReader.readLine();
		
		int numberVertices = Integer.parseInt(line);
		DirectedGraph input = new DirectedGraph (numberVertices);
		
		while ((line = lineReader.readLine()) != null)
		{
			StringTokenizer tokenReader = new StringTokenizer (line);
			
			int startVertex = Integer.parseInt(tokenReader.nextToken());
			int endVertex = Integer.parseInt(tokenReader.nextToken());
			
			input.addEdge(startVertex, endVertex);
		}
		
		Kosaraju solution = new Kosaraju (input);
		
		System.out.println("There are: " + solution.numberStrongComponents + " strongly connected components in the graph.");
		for (int vertex = 0; vertex < numberVertices; vertex++)
		{
			System.out.println(vertex + " has an ID of : " + solution.ID(vertex));
		}
	}
}

class Graph
{
	public int numberVertices;
	public int numberEdges;
	public LinkedList <Integer> [] adjacentVertices;
			
	@SuppressWarnings("unchecked")
	public Graph (int numberVertices)
	{
		this.numberVertices = numberVertices;
		this.numberEdges = 0;

		adjacentVertices = (LinkedList <Integer> []) new LinkedList [numberVertices];		
		for (int vertex = 0; vertex < numberVertices; vertex++)
		{
			adjacentVertices[vertex] = new LinkedList <Integer> ();
		}
	}
	
	public void addEdge (int start, int end)
	{
		adjacentVertices[start].add(end);
		adjacentVertices[end].add(start);
		numberEdges++;
	}
	
	public Iterable <Integer> adjacentVertices (int vertex)
	{
		return adjacentVertices[vertex];
	}
	
	public String toString ()
	{
		String output = "";
		
		for (int index = 0; index < numberVertices; index++)
		{
			output += index + " [ ";
			
			for (int neighbor : adjacentVertices(index))
			{
				output += neighbor + " ";
			}
			
			output += "]";
			output += "\n";
		}
		
		return output;
	}
}

class DepthFirstOrder
{
	public boolean [] visited;
	public Stack <Integer> reversePost;
	
	public DepthFirstOrder (DirectedGraph graph)
	{
		visited = new boolean [graph.numberVertices];
		reversePost = new Stack <Integer> ();
		
		for (int vertex = 0; vertex < graph.numberVertices; vertex++)
		{
			if (!visited[vertex])
			{
				DFS(graph, vertex);
			}
		}
	}
	
	public void DFS (DirectedGraph graph, int vertex)
	{
		visited[vertex] = true;
		for (int neighbor : graph.adjacentVertices(vertex))
		{
			if (!visited[neighbor])
			{
				DFS (graph, neighbor);
			}
		}
		reversePost.push(vertex);
	}
	
	public Iterable <Integer> reversePost ()
	{
		return reversePost;
	}
}

class Kosaraju
{	
	public boolean [] visited; 
	public int [] ID;
	
	public int numberStrongComponents;
	
	public Kosaraju (DirectedGraph graph)
	{		
		visited = new boolean [graph.numberVertices];
		ID = new int [graph.numberVertices];
		
		DepthFirstOrder order = new DepthFirstOrder(graph.reverse());
		for (int vertex : order.reversePost())
		{
			if (!visited[vertex])
			{
				DFS(graph, vertex);
				numberStrongComponents++;
			}
		}
	}
	
	public void DFS (DirectedGraph graph, int vertex)
	{
		visited[vertex] = true;
		ID[vertex] = numberStrongComponents;
		
		for (int neighbor : graph.adjacentVertices(vertex))
		{
			if (!visited[neighbor])
			{
				DFS (graph, neighbor);
			}
		}
	}
	
	public int ID (int vertex)
	{
		return ID[vertex];
	}
	
	public boolean stronglyConnected (int one, int two)
	{
		return ID[one] == ID[two];
	}
}