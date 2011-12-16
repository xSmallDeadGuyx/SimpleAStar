package org.xsmalldeadguyx.astar;

import java.util.ArrayList;
import java.util.List;

/*
Self-explanatory. Comes with 2 methods you can use, one for integer positions and another for nodes as positions. paths return are lists of nodes, but using my class should be very simple :)
*/


public class Pathfinder {
	
	public static boolean canCutCorners = true;
	
	private static Node end;
	private static int[][] gScore;
	private static int[][] hScore;
	private static int[][] fScore;
	private static Node[][] cameFrom;
	private static boolean[][] walls;
	
	public static Node toNode(int i, int j) {
		return new Node(i, j);
	}
	
	public static List<Node> generate(int startX, int startY, int endX, int endY, boolean[][] mapWalls) {
		return generate(toNode(startX, startY), toNode(endX, endY), mapWalls);
	}
	
	public static List<Node> generate(Node start, Node finish, boolean[][] mapWalls) {
		List<Node> openNodes = new ArrayList<Node>();
		List<Node> closedNodes = new ArrayList<Node>();
		walls = mapWalls;
		end = finish;
		gScore = new int[walls.length][walls[0].length];
		fScore = new int[walls.length][walls[0].length];
		hScore = new int[walls.length][walls[0].length];
		cameFrom = new Node[walls.length][walls[0].length];
		openNodes.add(start);
		gScore[start.x][start.y] = 0;
		hScore[start.x][start.y] = calculateHeuristic(start);
		fScore[start.x][start.y] = hScore[start.x][start.y];
		
		while(openNodes.size() > 0) {
			Node current = getLowestNodeIn(openNodes);
			if(current == null) break;
			if(current.equals(end)) return reconstructPath(current);
			System.out.println(current.x + ", " + current.y);
			
			openNodes.remove(current);
			closedNodes.add(current);
			
			List<Node> neighbors = getNeighborNodes(current);
			for(Node n : neighbors) {
				
				if(closedNodes.contains(n)) continue;
				
				int tempGscore = gScore[current.x][current.y] + distanceBetween(n, current);
				
				boolean proceed = false;
				if(!openNodes.contains(n)) {
					openNodes.add(n);
					proceed = true;
				}
				else if(tempGscore < gScore[n.x][n.y]) proceed = true;
				
				if(proceed) {
					cameFrom[n.x][n.y] = current;
			        gScore[n.x][n.y] = tempGscore;
			        hScore[n.x][n.y] = calculateHeuristic(n);
			        fScore[n.x][n.y] = gScore[n.x][n.y] + hScore[n.x][n.y];
				}
			}
		}
		return new ArrayList<Node>();
	}

	private static List<Node> reconstructPath(Node n) {
		if(cameFrom[n.x][n.y] != null) {
			List<Node> path = reconstructPath(cameFrom[n.x][n.y]);
			path.add(n);
			return path;
		}
		else {
			List<Node> path = new ArrayList<Node>();
			path.add(n);
			return path;
		}
	}

	private static List<Node> getNeighborNodes(Node n) {
		List<Node> found = new ArrayList<Node>();
		if(!walls[n.x + 1][n.y]) found.add(toNode(n.x + 1, n.y));
		if(!walls[n.x - 1][n.y]) found.add(toNode(n.x - 1, n.y));
		if(!walls[n.x][n.y + 1]) found.add(toNode(n.x, n.y + 1));
		if(!walls[n.x][n.y - 1]) found.add(toNode(n.x, n.y - 1));
		if(canCutCorners) {
			if(!walls[n.x + 1][n.y + 1] && (!walls[n.x + 1][n.y] || !walls[n.x][n.y + 1])) found.add(toNode(n.x + 1, n.y + 1));
			if(!walls[n.x - 1][n.y + 1] && (!walls[n.x - 1][n.y] || !walls[n.x][n.y + 1])) found.add(toNode(n.x - 1, n.y + 1));
			if(!walls[n.x - 1][n.y - 1] && (!walls[n.x - 1][n.y] || !walls[n.x][n.y - 1])) found.add(toNode(n.x - 1, n.y - 1));
			if(!walls[n.x + 1][n.y - 1] && (!walls[n.x + 1][n.y] || !walls[n.x][n.y - 1])) found.add(toNode(n.x + 1, n.y - 1));
		}
		else {
			if(!walls[n.x + 1][n.y + 1] && (!walls[n.x + 1][n.y] && !walls[n.x][n.y + 1])) found.add(toNode(n.x + 1, n.y + 1));
			if(!walls[n.x - 1][n.y + 1] && (!walls[n.x - 1][n.y] && !walls[n.x][n.y + 1])) found.add(toNode(n.x - 1, n.y + 1));
			if(!walls[n.x - 1][n.y - 1] && (!walls[n.x - 1][n.y] && !walls[n.x][n.y - 1])) found.add(toNode(n.x - 1, n.y - 1));
			if(!walls[n.x + 1][n.y - 1] && (!walls[n.x + 1][n.y] && !walls[n.x][n.y - 1])) found.add(toNode(n.x + 1, n.y - 1));
		}
		return found;
	}

	private static Node getLowestNodeIn(List<Node> nodes) {
		int lowest = -1;
		Node found = null;
		for(Node n : nodes) {
			int dist = cameFrom[n.x][n.y] == null ? -1 : gScore[cameFrom[n.x][n.y].x][cameFrom[n.x][n.y].y] + distanceBetween(n, cameFrom[n.x][n.y]) + calculateHeuristic(n);
			if(dist <= lowest || lowest == -1) {
				lowest = dist;
				found = n;
			}
		}
		return found;
	}

	private static int distanceBetween(Node n1, Node n2) {
		return (int) Math.round(10 * Math.sqrt(Math.pow(n1.x - n2.x, 2) + Math.pow(n1.y - n2.y, 2)));
	}

	private static int calculateHeuristic(Node start) {
		return 10 * (Math.abs(start.x - end.x) + Math.abs(start.y - end.y));
	}
}
