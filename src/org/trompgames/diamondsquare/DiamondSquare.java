package org.trompgames.diamondsquare;

import java.util.Random;

import org.trompgames.isometric.IsoFrame;

public class DiamondSquare {

	
	//http://stackoverflow.com/questions/892811/drawing-isometric-game-worlds
	
	public static void main(String[] args) {
		
		Location loc1 = new Location(0, 0);
		Location loc2 = new Location(10, 10);
		
		Location lerped = Location.lerp(loc1, loc2, 0.5);
		//System.out.println("Lerp: " + lerped);
		DiamondSquare square = new DiamondSquare(3, (int) (Math.random()*1000));
		square.generate();
		
		//System.out.println(square.exelTest());
			

		for(int i = 0; i < 20; i++){
			//System.out.println("Rand: " + (int) (1.0 * square.seedRandom()*255));
		}
		
		
		
		
		
		
		System.out.println(square.toString());
		
		for(int i = 0; i < 10; i++){
			//System.out.println("I: " + square.seedRandom());
		}
		
		
		new IsoFrame(500, 500, square);
		
		
	}
	
	private int[][] map;
	private int width;
	private int height;
	private Random rand;
	private int mapSeed;
	
	public DiamondSquare(int size, int seed){
		this.width = (int) Math.pow(2, size) + 1;
		this.height = (int) Math.pow(2, size) + 1;
		rand = new Random();
		rand.setSeed(seed);
		map = new int[width][height];	
		
		
		mapSeed = (int) (rand.nextDouble()*255);
		map[0][0] = (int) (rand.nextDouble()*255);
		map[0][height-1] = (int) (rand.nextDouble()*255);
		map[width-1][0] = (int) (rand.nextDouble()*255);
		map[width-1][height-1] = (int) (rand.nextDouble()*255);
	}
	
	public int[][] getMap(){
		return map;
	}
	
	public double seedRandom(){
		return rand.nextDouble();
	}
	
	public String exelTest(){
		String s = "";
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				s += "" + x + " " + map[x][y] + " " + y + "\n";
			}
		}	
		return s;
	}
	

	
	public boolean isFull(){
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				if(map[x][y] == 0) return false;
			}
		}		
		return true;
	}
	
	//int h = 2;
	
	public double randHeight(double h){	
		//return (float) (h + (int) ((this.seedRandom() * 2 * this.h) - this.h));
		
		
		return (h + this.seedRandom());
	}
	

	
	public void generate(){
		int i = 1;
		while(!isFull()){
			step(i, (int) (mapSeed));
			i++;
		}
		
		//for(int i = 1; i <= 2; i++){
		//	step(i, i);
		//}		
	}
	
	public int rand(int avg){
		//return avg;
		return (int) (avg + this.seedRandom()*6);	
	}
	
	
	public void step(int step, int h){		
		Location minLoc = new Location(0,0);
		Location maxLoc = new Location(1.0 * ((width-1)/step), 1.0 * ((height-1)/step));
		
		Location p1 = minLoc;
		Location p2 = new Location(0, maxLoc.getY());
		Location p3 = new Location(maxLoc.getX(), 0);
		Location p4 = maxLoc;		
		
		int dist = (int) (maxLoc.getX() - minLoc.getX());
		
		for(int x = 0; x <= width; x += maxLoc.getX()){
			for(int y = 0; y <= height; y += maxLoc.getY()){
				p1 = new Location(x, y);
				p2 = new Location(x, y+dist);
				p3 = new Location(x+dist, y);
				p4 = new Location(x+dist, y+dist);
				if(p1.getX() >= width || p2.getX() >= width || p3.getX() >= width || p4.getX() >= width
						|| p1.getY() >= height || p2.getY() >= height || p3.getY() >= height || p4.getY() >= height){
				
					continue;
				}			
								
				Location mid = new Location(p1.getX() + p2.getX() + p3.getX() + p4.getX(), p1.getY() + p2.getY() + p3.getY() + p4.getY());
				mid = mid.multiply(0.25);				
				
				int avg = map[(int) p1.getX()][(int) p1.getY()] + map[(int) p2.getX()][(int) p2.getY()] + map[(int) p3.getX()][(int) p3.getY()] + map[(int) p4.getX()][(int) p4.getY()];
				avg /= 4;
				map[(int) mid.getX()][(int) mid.getY()] = rand(avg);			
				
				
				
				
				p1 = new Location(mid.getX()-dist/2, mid.getY());
				p2 = new Location(mid.getX()+dist/2, mid.getY());
				p3 = new Location(mid.getX(), mid.getY()+dist/2);
				p4 = new Location(mid.getX(), mid.getY()-dist/2);
				
				if(map[(int) p1.getX()][(int) p1.getY()] == 0) map[(int) p1.getX()][(int) p1.getY()] = rand(getHeight(p1, dist/2));		
				if(map[(int) p2.getX()][(int) p2.getY()] == 0) map[(int) p2.getX()][(int) p2.getY()] = rand(getHeight(p2, dist/2));			
				if(map[(int) p3.getX()][(int) p3.getY()] == 0) map[(int) p3.getX()][(int) p3.getY()] = rand(getHeight(p3, dist/2));			
				if(map[(int) p4.getX()][(int) p4.getY()] == 0) map[(int) p4.getX()][(int) p4.getY()] = rand(getHeight(p4, dist/2));			

				
			}			
		}	
	}
	
	
	public int getHeight(Location loc, int dist){
		int totHeight = 0;
		int found = 0;
		
		Location p1 = loc.add(dist, 0);
		Location p2 = loc.add(-dist, 0);
		Location p3 = loc.add(0, dist);
		Location p4 = loc.add(0, -dist);

		if(isInMap(p1) && map[(int) p1.getX()][(int) p1.getY()] != 0){
			totHeight += map[(int) p1.getX()][(int) p1.getY()];
			found++;
		}if(isInMap(p2) && map[(int) p2.getX()][(int) p2.getY()] != 0){
			totHeight += map[(int) p2.getX()][(int) p2.getY()];
			found++;
		}if(isInMap(p3) && map[(int) p3.getX()][(int) p3.getY()] != 0){
			totHeight += map[(int) p3.getX()][(int) p3.getY()];
			found++;
		}if(isInMap(p4) && map[(int) p4.getX()][(int) p4.getY()] != 0){
			totHeight += map[(int) p4.getX()][(int) p4.getY()];
			found++;
		}
		if(found == 0) return 0;
		return totHeight / found;		
	}
	
	
	public boolean isInMap(Location loc){
		int x = (int) loc.getX();
		int y = (int) loc.getY();
		
		if(x < 0 || y < 0 || x >= width || y >= height) return false;
		return true;
	}
	
	
	@Override
	public String toString(){
		String s = "";	
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				s += map[x][y] + " ";
			}
			s += "\n";
		}		
		return s;		
	}
	
	public String toString(int scale){
		String s = "";	
		for(int y = 0; y < height; y++){
			String last = "";
			for(int x = 0; x < width; x++){
				for(int i = 0; i < scale; i++)
					last += map[x][y] + " ";
			}
			for(int i = 0; i < scale; i++){
				s += last + "\n";
			}
		}		
		return s;		
	}
	
	
	
	
	
	
}
