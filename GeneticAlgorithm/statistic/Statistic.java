package statistic;

import population.Population;
import java.util.Arrays;

public class Statistic {
	
	public float 		START_TIME;
	public float 		END_TIME;
	public float[] 		time;
	
	public int[] 		highest;
	public int[] 		lowest;
	public double[] 	average;
	public double[]		median;
	
	public Statistic(int iterations){
		START_TIME 	= System.currentTimeMillis();
		
		time 		= new float[iterations+1];
		highest		= new int[iterations+1];
		lowest		= new int[iterations+1];
		average		= new double[iterations+1];
		median		= new double[iterations+1];
	}
	
	public void apply(Population p){
		
		time[p.generation] 		= System.currentTimeMillis();
		highest[p.generation] 	= getHighest(p.fitness);
		lowest[p.generation] 	= getLowest(p.fitness);
		average[p.generation]	= calcAverage(p.fitness);
		median[p.generation]	= calcMedian(p.fitness);
		 
	}
	
	public int getHighest(int[] data){
		int[] inorder = new int[data.length];
		inorder = data.clone();
		Arrays.sort(inorder);
		
		return inorder[data.length-1];
	}
	
	public int getLowest(int[] data){
		int[] inorder = new int[data.length];
		inorder = data.clone();
		Arrays.sort(inorder);
		
		return inorder[1];
	}
	
	public double calcAverage(int[] data){
		//System.out.println(Arrays.toString(data));
		double average = 0;
		
		for(int i = 0;i<data.length;i++){
			average += data[i];
		}
		return average/(data.length-1);
	}
	
	public double calcMedian(int[] data){
		int[] inorder = data.clone();
		Arrays.sort(inorder);

		if(data.length%2 == 0){
			return inorder[(int) Math.floor(data.length/2)];
		} else {
			return (inorder[(int) data.length/2] + inorder[(int) data.length/2 - 1])/2;
		}
	}
}
