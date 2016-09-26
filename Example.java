import java.util.*;
import leetcode.Maze.Coordinate;
// Example of using Maze class and its functions
public class Example {

	public static void main(String[] args) {
// 		structure of  maze		
		int[][] multi = new int[][]{
			  { 1, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
			  { 1, 0, 0, 0, 0, 1, 0, 1, 0, 0 },
			  { 0, 1, 1, 0, 1, 1, 1, 1, 1, 0 },
			  { 0, 0, 1, 0, 0, 1, 0, 0, 0, 0 },
			  { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0 }
			};
		
//	    construct a maze object . (1,0) is marked with start.(8,3) is marked with finish 
	    Maze m=new Maze(1,0,8,3,multi);
	    
//      All paths from (1,0) to (8,3):	
	    List<List<Maze.Coordinate>> paths=m.findAllPaths();
//		result:   
//	    (1,0)->(1,1)->(2,1)->(3,1)->(3,2)->(3,3)->(3,4)->(4,4)->(5,4)->(6,4)->(6,3)->(7,3)->(8,3)
//	    (1,0)->(1,1)->(2,1)->(3,1)->(3,2)->(3,3)->(4,3)->(4,4)->(5,4)->(6,4)->(6,3)->(7,3)->(8,3)
//	    (1,0)->(1,1)->(2,1)->(3,1)->(3,0)->(4,0)->(5,0)->(6,0)->(7,0)->(8,0)->(8,1)->(9,1)->(9,2)->(9,3)->(8,3)
//	    (1,0)->(1,1)->(2,1)->(3,1)->(3,0)->(4,0)->(5,0)->(6,0)->(7,0)->(8,0)->(9,0)->(9,1)->(9,2)->(9,3)->(8,3)
//	    (1,0)->(1,1)->(2,1)->(3,1)->(4,1)->(4,0)->(5,0)->(6,0)->(7,0)->(8,0)->(8,1)->(9,1)->(9,2)->(9,3)->(8,3)
//	    (1,0)->(1,1)->(2,1)->(3,1)->(4,1)->(4,0)->(5,0)->(6,0)->(7,0)->(8,0)->(9,0)->(9,1)->(9,2)->(9,3)->(8,3)
	    
	    
//      The shortest path from (1,0) to (8,3):	
	    List<Maze.Coordinate> shortestpath=m.findShortestPath();
//      result:
//	    (1,0)->(1,1)->(2,1)->(3,1)->(3,2)->(3,3)->(3,4)->(4,4)->(5,4)->(6,4)->(6,3)->(7,3)->(8,3)
	    
	    
	    
//      wish to visit (7,3) ,(6,4),(8,1),(6,1) during the way from (1,0) to (8,3)
	    List<Maze.Coordinate> cs=new ArrayList<>();	   
	    cs.add((new Coordinate(7,3)));
	    cs.add((new Coordinate(6,4)));
	    cs.add((new Coordinate(8,1)));
	    cs.add((new Coordinate(6,1)));
//      the order of these positions which a user should follow so that he can walk through minimum distance from (1,0) to (8,3)	    
	    List<Maze.Coordinate> csr=m.findShortestRouteAcross(cs);
//	    result:
//	    (1,0)->(6,1)->(8,1)->(7,3)->(6,4)->(8,3)	
		
	}
}


