import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;
import java.io.*;

/**
 * Code to test an <tt>GraphSearchEngine</tt> implementation.
 */
public class GraphSearchEngineTester {
	private final GraphSearchEngine searchEngine = new GraphSearchEngineImpl();

	/*
	 * checks that the names in the path matches the names in correctNames array
	 */
	void checkCorrectNamesInPath(List<Node> shortestPath, String[] correctNames) {
		int idx = 0;
		for (Node node : shortestPath) {
			assertEquals(correctNames[idx++], node.getName());
		}
	}

	/*
	 * makes the graph using the data from the tsv file
	 */
	private IMDBGraph makeGraph() {
		final IMDBGraph graph;
		try {
			graph = new IMDBGraphImpl(IMDBGraphImpl.IMDB_DIRECTORY + "/testActors.tsv",
					IMDBGraphImpl.IMDB_DIRECTORY + "/testMovies.tsv");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			assertTrue(false);
			return null;
		}
		return graph;
	}

	@Test
	@Timeout(5)
	void testShortestPath1() {
		IMDBGraph graph = makeGraph();

		final Node actor1 = graph.getActor("Kris");
		final Node actor2 = graph.getActor("Sandy");

		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
		assertEquals(5, shortestPath.size());

		final String[] correctNames = { "Kris", "Blah2", "Sara", "Blah3", "Sandy" };
		checkCorrectNamesInPath(shortestPath, correctNames);

	}

	@Test
	/*
	 * tests the path between the same node
	 */
	void testSameNode() {
		IMDBGraph graph = makeGraph();

		final Node actor1 = graph.getActor("Uyen");
		final Node actor2 = graph.getActor("Uyen");

		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
		assertEquals(1, shortestPath.size());

		final String[] correctNames = { "Uyen" };
		checkCorrectNamesInPath(shortestPath, correctNames);
	}

	@Test
	/*
	 * tests for no path between 2 nodes
	 */
	void testNoPath() {
		IMDBGraph graph = makeGraph();

		final Node actor1 = graph.getActor("Uyen2");
		final Node actor2 = graph.getActor("Kris");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);

		assertNull(shortestPath);
	}

	@Test
	/*
	 * tests to make sure there is no path if the start and end nodes are not actors
	 */
	void testNotAnActor() {
		IMDBGraph graph = makeGraph();

		final Node actor1 = graph.getActor("Uyen3");
		final Node actor2 = graph.getActor("aa");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);

		assertNull(shortestPath);
	}

	@Test
	/*
	 * test to make sure there is no path if it starts out as null
	 */
	void testStartingNodeNull() {
		IMDBGraph graph = makeGraph();

		final Node actor1 = graph.getActor(null);
		final Node actor2 = graph.getActor("aa");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);

		assertNull(shortestPath);
	}

	@Test
	/*
	 * tests for a path between two nodes with surrounding neighbors
	 */
	void testTwoMiddleNodes() {
		IMDBGraph graph = makeGraph();

		final Node actor1 = graph.getActor("Sara");
		final Node actor2 = graph.getActor("Sandy");

		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actor2);
		assertEquals(3, shortestPath.size());

		final String[] correctNames = { "Sara", "Blah3", "Sandy" };
		checkCorrectNamesInPath(shortestPath, correctNames);
	}

	@Test
	/*
	 * tests for a path between two movie nodes
	 */
	void testTwoMovies() {
		IMDBGraph graph = makeGraph();

		final Node movie1 = graph.getMovie("Blah1");
		final Node movie2 = graph.getMovie("Blah3");
		final List<Node> shortestPath = searchEngine.findShortestPath(movie1, movie2);
		assertEquals(5, shortestPath.size());
		final String[] correctNames = { "Blah1", "Kris", "Blah2", "Sara", "Blah3" };
		checkCorrectNamesInPath(shortestPath, correctNames);
	}

	@Test
	/*
	 * tests for a path between a movie and actor
	 */
	void testPathBetweenMovieAndActor() {
		IMDBGraph graph = makeGraph();

		final Node movie1 = graph.getMovie("Blah1");
		final Node actor1 = graph.getActor("Sandy");

		final List<Node> shortestPath = searchEngine.findShortestPath(movie1, actor1);
		assertEquals(6, shortestPath.size());

		final String[] correctNames = { "Blah1", "Kris", "Blah2", "Sara", "Blah3", "Sandy" };
		checkCorrectNamesInPath(shortestPath, correctNames);
	}

}
