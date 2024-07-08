import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

/**
 * Implements the GraphSearchEngine interface.
 */
public class GraphSearchEngineImpl implements GraphSearchEngine {
	
	private HashMap<Node, Node> visited;       // keeps track of the nodes visited
	private ArrayList<Node> lookAtNeighbors;   // keeps track of the nodes to visit (its a queue)
	
	public GraphSearchEngineImpl() {
		visited = new HashMap<>();
		lookAtNeighbors = new ArrayList<>();
	}

	/*
	 * creates the shortest path between two nodes
	 * @param start is the starting node, end is the ending node
	 */
	public List<Node> findShortestPath(Node start, Node end) {
		// checks if starting or ending nodes are valid nodes
		if (start == null || end == null) {
			return null;
		}
		
		if (start.equals(end)) {
			return makeCorrectRoute(visited, end, start);
		} else {
			addToLookAtNeighbors(start, visited, lookAtNeighbors);

			while (!lookAtNeighbors.isEmpty()) {
				Node current = lookAtNeighbors.get(0);

				if (current.equals(end)) {
					return makeCorrectRoute(visited, end, start);
				} else {
					addToLookAtNeighbors(current, visited, lookAtNeighbors);
				}
				lookAtNeighbors.remove(0);
			}
		}
		return null;
	}
	
	/*
	 * adds to the array list lookAtNeighbors
	 * @param start is the node to get the neighbors from
	 *        visited is the hash map of visited nodes
	 *        lookAtNeighbors is the queue of nodes to look through
	 */
	public void addToLookAtNeighbors(Node start, HashMap<Node, Node> visited, ArrayList<Node> lookAtNeighbors) {
		for (Node neighbor : start.getNeighbors()) {
			if (!visited.containsKey(neighbor)) {
				lookAtNeighbors.add(neighbor);
				visited.put(neighbor, start);
			}
		}
	}

	/*
	 * builds the correct path from the list of visited map
	 * @param visited is a map of all visited nodes end is the ending node start is
	 * the starting node
	 */
	public ArrayList<Node> makeCorrectRoute(HashMap<Node, Node> visited, Node end, Node start) {
		ArrayList<Node> path = new ArrayList<>();
		Node current = end;
		path.add(0, current);

		while (current != null && !current.equals(start)) {
			current = visited.get(current);
			path.add(0, current);
		}
		return path;

	}

}
