import java.io.*;
import java.util.Arrays;
import java.util.Random;
import population.Population;
import fitness.Costs;
import statistic.Statistic;

public class GA {
	
	static long 	BEGIN;
	static long 	END;
	
	final Random 	rand;
	Statistic		statistic;
	
	Population		population;
	int				individuals;

	int				trips;
	int				depots;
	int				depot_capacity;
	Costs 			costs;
	
	final int 		maxIteration;
	final int 		mutationPercent;
	final int 		crossoverPercent;
	
	final int 		min_start_time;
	final int 		max_start_time;
	
	public GA(){
		BEGIN 				= System.currentTimeMillis();
		rand 				= new Random();
		
		min_start_time		= 10000;
		max_start_time		= 40000;
		
		maxIteration 		= 1000;
		mutationPercent 	= 10;
		crossoverPercent	= 60;
		
		individuals 		= 20; //individuals 		= population.individuals;
		depot_capacity		= 150;
	
		population			= new Population(individuals, depot_capacity, min_start_time, max_start_time );
		//population 			= new Population(5, 28, 3, new int[] {0,9,9,9});
							//Individuen, Fahrten, Depots, DepoKapazitäten
		
		costs 				= new Costs(true);
		
		statistic			= new Statistic(maxIteration);	
		
		
	}

	public static void main(String[] args){
		
		System.out.println("Hello World I am a GA");
		GA test = new GA();
		
		test.run();
	}

	public void run(){
		p("RUN");
		if(population!=null){
			for(int g = 0;g < maxIteration;g++){
				p("GENERATION: "+g);
				for(int i = 0;i < individuals;i++){
					int f = costs.apply(population.getIndidividual(i));
					//p(f);
					population.setFitness(i,f);
				}
				
				if(g%10 == 0) {
					p("\n GENERATION: "+g);
			 		population.printPopulation(); //Matrix();
				}
				//statistic.apply(population);
				
				
				
				/*
				System.out.println("hieghest"+statistic.highest[g]);
				System.out.println("lowest"+statistic.lowest[g]);
				System.out.println("average"+statistic.average[g]);
				System.out.println("median"+statistic.median[g]);
				*/
				
				population.nextGeneration();
				
			}/**/
			for(int i = 0;i < individuals;i++){
				int f = costs.apply(population.getIndidividual(i));
				//p(f);
				population.setFitness(i,f);
			}
			System.out.println("GENERATION: "+maxIteration);
	 		population.printPopulation(); //Matrix();
	 		population.printTime();
		}else System.out.println("Population wurde nicht inititert");
		
	}

	public static void p(Object s){
		System.out.println(s);
	}

	public void pause(){
		try{
			p("Pause.");
			System.in.read();
		}catch(IOException e){
			System.err.println("pause() > Fehler: SQLException");
			e.printStackTrace();
		}
	}
}


