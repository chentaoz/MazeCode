import java.util.*;

/*
 * Each Maze object represents a Rally Health office(mazelike ). 
 * It mainly includes three properties:
 * 	1)start		: start coordinate of a way in the maze.
 * 	2)end		: destination coordinate of a way in the maze.
 *  3)mazeBody	: the main structure of the maze. it is 2D integers array and in each ceil, it is either 0 or 1;
 *  	-1  represents an obstacle ,like a desk, coffee machine etc.
 *  	-0  represents a unit which we can walk through. 
 * It mainly includes three functions :
 * 	1)findAllPaths()		:Return all paths from the start position to finish position :.
 *  2)findShortestPath()	:Return the shortest path from start position to finish position.
 *  3)findShortestRouteAcross(List<Coordinate> coordinates):Return an Ordered list of positions which a user wishes to visit during the way from start to the destination 
 *    so that the user can walk through these position from start to destination with least distance  
 */

public class Maze{

	
	private Coordinate start;
	private Coordinate end;
	private int [][] mazeBody;
	
	public Maze(int startCoordinateX,int startCoordinateY,int endCoordinateX,int endCoordinateY,int [][] mazeBody){
		start=new Coordinate(startCoordinateX,startCoordinateY);
		end=new Coordinate(endCoordinateX,endCoordinateY);
		this.mazeBody=mazeBody;
	}
	
	
	private List<List<Coordinate>> allPaths=new ArrayList<>();
	
	public List<List<Coordinate>> findAllPaths(){
		//If allPaths was calculated , don't need to calculate again
		if(allPaths.size()>0) return allPaths;
		
		List<Coordinate> path=new ArrayList<Coordinate>();
		HashSet<String> visitedSet=new HashSet<String>();
		depthFirstSearch(path,visitedSet,start);		
		return allPaths;
	}
	
	
	public List<Coordinate> findShortestPath(){
		//If allPaths was not calculated ,then do it
		if(allPaths.size()==0)  findAllPaths();
		
		List<Coordinate> shortest=null;
		for(List path:allPaths){
			if(shortest==null || path.size()<shortest.size()) shortest=path;
		}
		return shortest;
	}
	/*
	 * @Param:list of positions which user wish to visit during the way.
	 * @Return :Ordered list of positions and user can walk through least distance by this order.
	 * 
	 */
	public List<Coordinate> findShortestRouteAcross(List<Coordinate> coordinates){
		shortesDistanceRoute=null;
		shortesDistanceAcross=Integer.MAX_VALUE;
		
		List<Coordinate> path= new ArrayList<>();
		path.add(start);
		
		HashSet<String> currentCoordinatesSet=new HashSet<>();
		//Get an array which contains all-pair shortest distances between two positions, 
		//and based on these pairs of shortest distances to order all positions which a user should walk through from start to end.
		int [][]shortestDistanceArray = AllPairShortestPaths();
		dfsAcrossCoordinates(path,0,currentCoordinatesSet,coordinates,shortestDistanceArray);
		
		return shortesDistanceRoute;
	}
	
	private int shortesDistanceAcross=Integer.MAX_VALUE;
	private List<Coordinate> shortesDistanceRoute=null;
	
	// helper function: depth-first search to find shorts route across targeted positions
	private void dfsAcrossCoordinates(List<Coordinate> path,int distance,HashSet<String> currentCoordinatesSet,List<Coordinate> coordinateList,int [][] shortestDistanceArray){
		
		if(path.size()==coordinateList.size()+1){
			Coordinate lastPos=path.get(path.size()-1);
			int indexOflastPos=positionToIndex(lastPos.X,lastPos.Y);
			int indexOfCurrentPos=positionToIndex(end.X,end.Y);
			distance=addDistances(distance,shortestDistanceArray[indexOflastPos][indexOfCurrentPos]);

			if(shortesDistanceAcross>distance) {
				shortesDistanceRoute= new ArrayList<Coordinate>(path);
				shortesDistanceRoute.add(end);
				shortesDistanceAcross=distance;
			}
			return;
		}
		
		for(Coordinate c:coordinateList){
			if(!currentCoordinatesSet.contains(c.toString())){
			
				Coordinate lastPos=path.get(path.size()-1);
				int indexOflastPos=positionToIndex(lastPos.X,lastPos.Y);
				int indexOfcurrentPos=positionToIndex(c.X,c.Y);
				int shortestDistanceFromLastPosToCurrent=shortestDistanceArray[indexOflastPos][indexOfcurrentPos];

				path.add(c);
				currentCoordinatesSet.add(c.toString());
				dfsAcrossCoordinates(path,addDistances(distance,shortestDistanceFromLastPosToCurrent),currentCoordinatesSet,coordinateList,shortestDistanceArray);
				path.remove(path.size()-1);
				currentCoordinatesSet.remove(c.toString());
			}
		}
	}
	
	// helper function: find the all pair shortest path .
	private int[][] AllPairShortestPaths(){
		int [][] distances=new int[(mazeBody[0].length)*(mazeBody.length)][(mazeBody[0].length)*(mazeBody.length)];
		//initialize the paths array,used to record the distances between two position in mazeBody .  
		for(int i=0;i<distances.length;i++){
			for(int j=0;j<distances.length;j++){
				Coordinate pos_i=indexToPosition(i);
				Coordinate pos_j=indexToPosition(j);
				
				if(pos_i.isNeighber(pos_j)&& mazeBody[pos_i.Y][pos_i.X]==0 && mazeBody[pos_j.Y][pos_j.X]==0)
					distances[i][j]=1;				
				else if(pos_i.equals(pos_j) && mazeBody[pos_i.Y][pos_i.X]==0)
					distances[i][j]=0;	
				else distances[i][j]=Integer.MAX_VALUE;				
			}
		}
		
		// DP: all-pairs shortest paths
		for(int k=0;k<distances.length;k++){
			for(int i=0;i<distances.length;i++){
				for(int j=0;j<distances[0].length;j++){
					distances[i][j]=Math.min(addDistances(distances[i][k],distances[k][j]), distances[i][j]);
				}
			}
		}
			
		return distances;
	}

	// helper function:mapping  an index in array used to calculate all-pair shortest path to a position in mazeBody
	private int positionToIndex(int X,int Y){
		return Y*mazeBody[0].length+X;
	}
	// helper function:mapping a position in mazeBody to an index in array used to calculate all-pair shortest path. 
	private Coordinate indexToPosition(int index){
		return new Coordinate(index%mazeBody[0].length,index/mazeBody[0].length);
	}
	
	
	//helper function: depth first search to find to paths from start to end
	private void depthFirstSearch(List<Coordinate> path ,HashSet<String> visitedSet,Coordinate currentCoordinate){
		
		if(currentCoordinate.Y>=mazeBody.length || currentCoordinate.X>=mazeBody[0].length || currentCoordinate.X<0||currentCoordinate.Y<0) return;
		if(visitedSet.contains(currentCoordinate.toString()) || mazeBody[currentCoordinate.Y][currentCoordinate.X]==1) return ;
		if(currentCoordinate.equals(end)){
			path.add(currentCoordinate);
			allPaths.add(new ArrayList<Coordinate>(path));
			path.remove(path.size()-1);
			return;
		}
		
		visitedSet.add(currentCoordinate.toString());
		path.add(currentCoordinate);
		
		depthFirstSearch(path,visitedSet,new Coordinate(currentCoordinate.X,currentCoordinate.Y+1));
		depthFirstSearch(path,visitedSet,new Coordinate(currentCoordinate.X,currentCoordinate.Y-1));
		depthFirstSearch(path,visitedSet,new Coordinate(currentCoordinate.X+1,currentCoordinate.Y));
		depthFirstSearch(path,visitedSet,new Coordinate(currentCoordinate.X-1,currentCoordinate.Y));
		
		visitedSet.remove(currentCoordinate.toString());
		path.remove(path.size()-1);	
	}
	

	public Coordinate getStart() {
		return start;
	}

	public void setStart(int X,int Y) {
		this.start = new Coordinate(X,Y);	
		allPaths.removeAll(allPaths);
	}

	public Coordinate getEnd() {
		return end;
	}

	public void setEnd(int X,int Y) {
		this.end = new Coordinate(X,Y);;
		allPaths.removeAll(allPaths);
	}

	public int[][] getMazeBody() {
		return mazeBody;
	}

	public void setMazeBody(int[][] mazeBody) {
		this.mazeBody = mazeBody;
		allPaths.removeAll(allPaths);
	}
	
	public int getShortesDistanceAcross(){
		return shortesDistanceAcross;
	}
	
	//This function is to correct the sum when d1+d2 is greater than max value of integer, the result would be negative value, 
	private int addDistances(int d1,int d2){
		if(d1+d2<0) return Integer.MAX_VALUE;
		return d1+d2;
	}
	
	public static class Coordinate {
		public int X;
		public int Y;
		public Coordinate(int x,int y){
			X=x;
			Y=y;
		}
		public String toString(){
			return X+","+Y;
		}
		public boolean equals(Coordinate c){
			if(X==c.X&& Y==c.Y) return true;
			else return false;
		}
		public boolean isNeighber(Coordinate c){
			if(X==c.X && Math.abs(Y-c.Y)==1) return true;
			if(Y==c.Y && Math.abs(X-c.X)==1) return true;
			return false;
		}
	}
	
}
