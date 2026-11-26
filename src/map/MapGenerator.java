package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import event.EventType;

public class MapGenerator {
	
	private static final int MIN_NODES = 2;
	private static final int MAX_NODES = 5;
	private static final int MAX_NODE_CONNECTIONS = 3;
	private static final int DEFAULT_DEPTH = 15;

	private final int depth;
	private final Random rng = new Random();
	
	private Digraph graph;
	private EventType[] types;
	private List<List<Integer>> levels;
	private int[] levelOf;
	private int[] indexOf;
	
	public MapGenerator() {
		this(DEFAULT_DEPTH);
	}
	public MapGenerator(int depth) {
		if (depth < 2) {
			throw new IllegalArgumentException("Depth must be at least 2");
		}
		this.depth = depth;
	}
	
	public void generate() {
		buildGraph();
		determineLevels();
		assignTypes();
	}
	
	private void buildGraph() {
		List<List<Integer>> scaffold = new ArrayList<>();
		int total = 0;
		for (int l = 0; l <= depth; l++) {
			int count = (l == depth) ? 1 : MIN_NODES + rng.nextInt(MAX_NODES - MIN_NODES + 1);
			List<Integer> level = new ArrayList<>();
			for (int k = 0; k < count; k++)
				level.add(total++);
			scaffold.add(level);
		}
		graph = new Digraph(total);
		for (int l = 0; l < depth; l++)
			connect(scaffold.get(l), scaffold.get(l + 1));
	}
	
	private void connect(List<Integer> lower, List<Integer> upper) {
		int n = lower.size();
		int m = upper.size();
		int extra = MAX_NODE_CONNECTIONS - 1;
		int i = 0;
		int j = 0;
		int nodeConnections = 0;
		
		if (m - 1 > extra * n) {
			throw new IllegalStateException("Can't connect node");
		}
		
		graph.addEdge(lower.get(i), upper.get(j));
		
		while (i < n - 1 || j < m - 1) {
			boolean canStepUpper = j < m - 1 && nodeConnections < extra;
			boolean canStepLower = i < n - 1 && (m - 1 - j) <= extra * (n - 1 - i);
			boolean stepLower;
			
			if (canStepLower && canStepUpper) 
				stepLower = rng.nextBoolean();
	        else if (canStepLower)
	        	stepLower = true;
	        else if (canStepUpper)
	        	stepLower = false;
	        else throw new IllegalStateException("No valid move while connecting levels");

	        if (stepLower) {
	            i++;
	            nodeConnections = 0;
	        } else {
	            j++;
	            nodeConnections++;
	        }
	        graph.addEdge(lower.get(i), upper.get(j));
		}
	}
	
	private void determineLevels() {
		BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(graph, getStartNodes());
		
		levels = new ArrayList<>();
		levelOf = new int[graph.V()];
		indexOf = new int[graph.V()];
		
		for(int v = 0; v < graph.V(); v++) {
			int level = bfs.distTo(v);
			
			while (levels.size() <= level) 
				levels.add(new ArrayList<>());
			
			levelOf[v] = level;
			indexOf[v] = levels.get(level).size();
			levels.get(level).add(v);
		}
	}
	
	private void assignTypes() {
		types = new EventType[graph.V()];
		
		for (int v = 0; v < graph.V(); v++) {
			if (isBoss(v))
				types[v] = EventType.BOSS;
			else if (leadsToBoss(v))
				types[v] = EventType.REST;
			else
				types[v] = roll();
		}
	}
	
	private boolean leadsToBoss(int v) {
		for (int w : graph.adj(v)) {
			if (isBoss(w))
				return true;
		}
		return false;
	}
	
	private EventType roll() {
		int r = rng.nextInt(100);
		if (r < 40) return EventType.MINION;
		if (r < 55) return EventType.ELITE;
		if (r < 65) return EventType.SHOP;
		if (r < 90) return EventType.MYSTERY;
		return EventType.REST;
	}
	
	public int getDepth() {
		return levels.size() - 1;
	}
	
	public int levelOf(int v) {
		return levelOf[v];
	}
	
	public int indexOf(int v) {
		return indexOf[v];
	}
	
	public int levelSize(int level) {
		return levels.get(level).size();
	}
	
	public EventType getType(int v) {
		return types[v];
	}
	
	public boolean isBoss(int v) {
		return graph.outdegree(v) == 0;
	}
	
	public int bossNode() {
		for (int v = 0; v < graph.V(); v++) {
			if (isBoss(v))
				return v;
		}
		return -1;
	}
	
	public List<Integer> getStartNodes() {
		List<Integer> starts = new ArrayList<>();
		for (int v = 0; v < graph.V(); v++) {
			if (graph.indegree(v) == 0)
				starts.add(v);
		}
		return starts;
	}
	
	public List<Integer> getNextNodes(int v) {
		List<Integer> next = new ArrayList<>();
		for (int w : graph.adj(v)) 
			next.add(w);
		return next;
	}
}
