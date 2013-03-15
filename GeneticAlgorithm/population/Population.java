
package population;

import java.io.IOException;
import java.util.*;

import reviser.Reviser;
import data.DataConnector;

import java.lang.reflect.Array;
public class Population{
	
	int[][] 		population;
	public int 		individuals;
	int				trips;
	int 			genes;
	public int 		generation;
	
	Random			rand;
		
	int 			depots;
	int[][]			usedVperD;
	int[]			VperD;
	public int[]	fitness;
	int				sumFitness;
	int[] 			approved;

	DataConnector 	connection;
	
	Reviser			R;
	
	//NEUER VERSUCH VOLLER ELAN :D
	public Population(int i, int depot_capacity, int min_start_time, int max_start_time){
		rand 				= new Random();
		generation 			= 0;
		individuals		 	= i;
		fitness 			= new int[i];
		sumFitness 			= 0;
		approved 			= new int[i];
		
		//Depotanzahl & Fahrtenanzahl auslesen
		connection = new DataConnector();
		connection.initializeGATrips(min_start_time,max_start_time,2);
		
		trips = connection.numberRows("GA_TRIPS");
		genes = trips*2;
		depots = connection.numberRows("REC_BTF");

		R					= new Reviser(trips);
		
		population 			= new int[individuals][genes];
		int[][] dummy		= new int[individuals][genes];
		VperD				= new int[depots];
		for(int j = 0;j<depots;j++) VperD[j]=depot_capacity;
		
		
		//RandomPopulation generieren
		int startIndex = 0;
		for(int k=0;k<individuals;k++){
			
			for(int l=startIndex;l<genes;l=l+2){
				int depot 			= (int) Math.floor(rand.nextDouble()*depots)+1;
				dummy[k][l]	= depot;
				
				int vehikel 		= (int) Math.floor(rand.nextDouble()*VperD[depot-1])+1;
				dummy[k][l+1]		= vehikel;
			}
			
					//clear screen
					for (int ii=0; ii<60; ii++) {
					    // scroll down one line
					    p("");
					}
					p("Load Population (random): "+Math.round(100*(k+1)/(individuals))+"% done");
					
			int check_reply = R.checkTimeConstraint(dummy[k],startIndex);
			//p("check_reply = "+check_reply);
			if(check_reply >=0) {
				//p("NEU");
				startIndex = check_reply;
				//p("startIndex = "+startIndex);
				
				k--;
				
			}else if(check_reply == -1) startIndex = 0;
		}
		
		/*CHECK Population again 
		for(int j=0;j<individuals;j++){
			if(R.checkTime(dummy[j])) p("Ind "+j+" ist korrekt.");
			else p("FEHELER BEI I "+j);
		}
		*/
		
		population = dummy.clone();
		
		printPopulation();	
		
		//pause();
	}
	
	public Population(int i, int depot_capacity){
		rand 				= new Random();
		generation 			= 0;
		individuals		 	= i;
		fitness 			= new int[i];
		sumFitness 			= 0;
		approved 			= new int[i];
		
		//Depotanzahl & Fahrtenanzahl auslesen
		connection = new DataConnector();
		
		trips = connection.numberRows("GA_TRIPS");
		genes = trips*2;
		depots = connection.numberRows("REC_BTF");

		R					= new Reviser(trips);
		
		population 			= new int[individuals][genes];
		int[][] dummy		= new int[individuals][genes];
		VperD				= new int[depots];
		for(int j = 0;j<depots;j++) VperD[j]=depot_capacity;
		
		/*RandomPopulation generieren*/
		//p("Populationgeneration");
		int startIndex = 0;
		for(int k=0;k<individuals;k++){
			
			for(int l=startIndex;l<genes;l=l+2){
				int depot 			= (int) Math.floor(rand.nextDouble()*depots)+1;
				dummy[k][l]	= depot;
				
				int vehikel 		= (int) Math.floor(rand.nextDouble()*VperD[depot-1])+1;
				dummy[k][l+1]		= vehikel;
			}
			
					//clear screen
					for (int ii=0; ii<60; ii++) {
					    // scroll down one line
					    p("");
					}
					p("Load Population (random): "+Math.round(100*(k+1)/(individuals))+"% done");
					
			int check_reply = R.checkTimeConstraint(dummy[k],startIndex);
			//p("check_reply = "+check_reply);
			if(check_reply >=0) {
				//p("NEU");
				startIndex = check_reply;
				//p("startIndex = "+startIndex);
				k--;
				
			}else if(check_reply == -1) startIndex = 0;
		}
		
		/*CHECK Population again 
		for(int j=0;j<individuals;j++){
			if(R.checkTime(dummy[j])) p("Ind "+j+" ist korrekt.");
			else p("FEHELER BEI I "+j);
		}
		*/
		
		population = dummy.clone();
	}
	
	
	//________________________________
	
	public Population(int i, int g, int d, int[] VpD){
		generation 			= 0;
		genes 				= g;
		individuals		 	= i;
		depots				= d;
		
		rand 				= new Random();
		population 			= new int[i+1][2*g];

		usedVperD 			= new int[i+1][d+1];
		VperD				= VpD.clone();
		fitness 			= new int[i+1];
		sumFitness 			= 0;
		approved 			= new int[i+1];
		
		
		for(int k=1;k<=i;k++){
			for(int l=0;l<2*g;l=l+2){
				int depot 			= (int) Math.floor(rand.nextDouble()*d)+1;
				population[k][l]	= depot;
				
				int vehikel 		= (int) Math.floor(rand.nextDouble()*VperD[depot])+1;
				population[k][l+1]	= vehikel;//vielleicht vereinfachen
			}
		}
	}

	
	public static void main(String[] args){
		
		//Population test = new Population(5, 9, 15000, 15800);
		//doCrossover(3,0);
		}

	
/*Methden*/
	public void nextGeneration(){
		/*//check popultaion
		for(int i=0;i<individuals;i++){
			if(!R.checkTime(population[i])) p("IND: "+i+ " ist nicht korrekt");
		}
		p("CHECK IST ABGESCHLOSSEN. POPULATION KORREKT. nextGen kann angeagnegn werden.");
		pause();
		
		p("NEXT GEN.");
		*/
		
		
		
		if(0.5 < 1 && 0.5 > 0){
			int[] order = getOrder(fitness).clone();
			
			generation++;
			
			/*Elitism, hier die besten zwei*/
			for(int i=0;i<2;i++){
				population[order[individuals-1-i]] = population[order[i]].clone();
			}
			
			/*Crossover*/
			int amount_crossover = 2; //(int) Math.floor(rand.nextDouble()*)
			doCrossover(amount_crossover,0);
			
			
			/*Mutation*/
			int amount_mutations = 2; //(int) Math.floor(rand.nextDouble()*individuals/3)+1;
			doMutation(amount_mutations);
			
						
		}else System.out.println("Fehler: Falsche Prozentangabe");		
	}
	
	public void doMutation(int times){
		for(int i = 0;i < times;i++){
			int pos = (int) Math.floor(rand.nextDouble()*individuals);
			//p("mutation at "+pos);
			population[pos] = mutate(pos,1).clone();
		}
	}
	
	public void doCrossover(int times, int selection_type){
		//selection_type == 1 entspricht RankSelection; funktioniert noch nicht 
		int[][] indexedFitness = indexArray(fitness);
		int[][] dummy = new int[2][2*trips];
		for(int i=0;i<times;i++){
		
			int pos1 = selection_type==0 ? 
					randomValueSelection(indexedFitness):randomRankSelection(fitness.length);
			indexedFitness[pos1][1] = -1;						//Auswahl markieren
			int pos2 = selection_type==0 ? 
					randomValueSelection(indexedFitness):randomRankSelection(fitness.length);
			indexedFitness[pos2][1] = -1;
			
			//p("crossover at "+pos1+";"+pos2);
			dummy = twoPointCrossover(pos1,pos2).clone();
			//p("CROSSOVER");
			int zaehler = 0;
			while(!R.checkTime(dummy[0]) || !R.checkTime(dummy[1])) {
				//p("NEU CROSSOVER");
				dummy = twoPointCrossover(pos1,pos2).clone();
				if(zaehler++ > 1000) p("Zu häufige Wiederholung von Crossover. Vielleicht zu wenig Fahrzeuge?");
			}
			
			population[pos1] = dummy[0].clone();
			population[pos2] = dummy[1].clone();
		}		
	}
	
	public static int randomRankSelection(int quantity){
		
		double prob_step = 100*2/(quantity*(quantity+1));
		//p("prob_step. "+prob_step);
		double g = Math.random();
		//p("g: "+g);
		double prob = prob_step;
		double increment = prob_step;
		//p("prob: "+prob);
		int i = quantity-1;
		while(g*100 >= prob && i>0){
			i--;
			increment+=prob_step;
			prob += increment;
		}
		return i;
	}
	
	public static int randomValueSelection(int[][] value){
		//p("\n RandomSelektion");
		
		boolean available_spot = false;
		int max = 0;
		for(int j=0;j<value.length;j++) if(max<=value[j][0] && value[j][1]>=0){
			max=value[j][0];
			available_spot = true;
		}
		if(available_spot){
			int[] flipped_value = new int[value.length];
			for(int j=0;j<flipped_value.length;j++) {
				flipped_value[j] = max - value[j][0] +1;
				//System.out.print(flipped_value[j]+",");
				
			}
			//p("");
			
			//int[] normal_value = normalizeArray(value).clone();
					
			int sum = 0;
			for(int j=0;j<value.length;j++) if(value[j][1]>=0) sum+=flipped_value[j];
			//p("sum: "+sum);
			int i = 0;
			int f = 0;
			while(i<value.length){
				if(value[i][1]>=0){
					f = flipped_value[i];
					break;
				}
				i++;
			}
			double g = Math.random();
			//p("g: "+g);
			while(g >= (double) f/sum){
			
				i++;
				if(value[i][1]>=0) f = f + flipped_value[i];
			}
			//p("value[i][1]"+value[i][1]);
			return value[i][1];
		}else{
			p("Fehler in RandomValueSelection: Alle Individuen ausgewählt. Keins mehr übrig.");
			return -1;
		}
	}
	
	public static int[][] indexArray(int[] array){
		int[][] indexedArray = new int[array.length][2];
		for(int i=0;i<array.length;i++){
			indexedArray[i][0] = array[i];
			indexedArray[i][1] = i;
		}
		return indexedArray;
	}
	
	public static int[] getOrder(int[] f){
		Integer[][] a = new Integer[f.length][2];
		for(int i=0;i<f.length;i++){
			a[i][0] = i;
			a[i][1] = f[i];
		}
		
		Arrays.sort(a, new Comparator<Integer[]>() {
		    @Override
		    public int compare(Integer[] int1, Integer[] int2) {
		        Integer spot1 = int1[1];
		        Integer spot2 = int2[1];
		        return spot1.compareTo(spot2);
		    }
		});
		
		int[] b = new int[a.length];
		for(int i=0;i<a.length;i++){
			b[i] = a[i][0];
		}
		return b;
	}
	/*-----------------*/
	

	public void nextGeneration(int ALT){
		if(0.5 < 1 && 0.5 > 0){
			int[][] selection = divide().clone();
			if(generation%1000 == 0 ){
				p("\t highest \t"+Arrays.toString(selection[0]));
				p("\t lowest \t"+Arrays.toString(selection[1]));
			}
			generation++;
			
			for(int i = 0;i < selection[0].length;i++){
					population[selection[0][i]]=population[selection[1][i]].clone();
			}
			int j = 0;
			while(j+1 < selection[0].length){
				//crossover(selection[1][j],selection[1][j+1],3);
				twoPointCrossover(selection[1][j],selection[1][j+1]);
				j = j+2;
			}
			
			//mutieren
			for(int i = 0;i < 1;i++){
				mutate((int) Math.floor(rand.nextDouble()*individuals)+1,1);
			}
			
		}else System.out.println("Fehler: Falsche Prozentangabe");		
	}
	
	
	public int[][] divide(){
		int[][] elite 	= new int[2][(int) Math.floor(0.5*individuals)];
		/*
		elite[0]   		= selectReallyBest(elite[0].length);
		elite[1]		= selectReallyWorst(elite[0].length);
		*/
		
		int[] inorder 	= new int[individuals+1];
		inorder			= fitness.clone();
		Arrays.sort(inorder);
		//p("inorder:"+Arrays.toString(inorder));
		int i=0, j=0;
		int max = inorder[individuals-elite[0].length+1];
		int min = inorder[elite[0].length];
		//p(min+";"+max);
		for(int k = 1;k <= individuals;k++){
			
			if(max <= fitness[k] && i<elite[0].length) {
				elite[0][i] = k;
				i++;
			}else{
				if(min >= fitness[k] && fitness[k]>0 && j<elite[1].length) {
					elite[1][j] = k;
					j++;
				}
			}	
		}
		return elite;
	}	
	
	public int[] selectBest(int amount){
		int[] elite = new int[amount];
		
		for(int nr = 0;nr < amount;nr++){
			int candidate = selectBest();
			
			elite[nr] = candidate;
			for(int pre = 0;pre < nr;pre++){
				if(elite[pre] == candidate){
					nr--;
					break;
				}
			}
		}
		return elite;
	}	
	
	public static int[] normalizeArray(int[] f){ //alle Einträge -Minimum+1
		int[] normal = new int[f.length];
		int min = Integer.MAX_VALUE;
		int j =0;
		for(int e : f){
			if(e<min) min=e;
			normal[j++]=e;
		}
		for(int i=0;i<normal.length;i++) {
			normal[i]=normal[i]-min+1;
		}
		
		return normal;
	}
	
	public int selectWorst(){
		int i = 1;

		int[] normal_fitness = normalizeArray(fitness);
		
		double g = rand.nextDouble();
		int f = normal_fitness[0]-normal_fitness[i];
		while(g>((double) f/((individuals-1)*normal_fitness[0]))){
			i++;
			f=f+normal_fitness[0]-normal_fitness[i];
		}
		return i;
	}
	
	public int selectBest(){
		int[] normal_fitness = normalizeArray(fitness);
		int i = 1;
		int f = normal_fitness[i];

		double g = rand.nextDouble();
		while(g >= (double) f/normal_fitness[0]){
		
			i++;
			f = f + normal_fitness[i];
		}
		return i;
	}
	
	/*
	public int[] selectReallyBest(int amount){
		
		int[] inorder 	= new int[individuals+1];
		inorder			= fitness.clone();
		Arrays.sort(inorder); 						//nun aufsteigend geordnet
		p("geordnetes Array:"+Arrays.toString(inorder));
		int[] best = new int[amount];
		int i=0;
		int max 		= inorder[individuals-amount];
		p("max:"+max);
		for(int j = 1;j <= individuals;j++){
			
			if(max < fitness[j]) {
				best[i] = j;
				if(i < amount-1){
					i++;
				}else break;
			}	
		}
		p("best: "+Arrays.toString(best));
		return best;
	}
	
	public int selectReallyBest(){
		return selectReallyBest(1)[0];
	}
	
public int[] selectReallyWorst(int amount){
		
		int[] inorder = new int[individuals+1];
		inorder=fitness.clone();
		Arrays.sort(inorder); 					//nun aufsteigend geordnet
		
		int[] worst = new int[amount];
		int i		= 0;
		int min 	= inorder[amount];
		for(int j = 1;j <= individuals;j++){
			
			if(min > fitness[j] && fitness[j] != 0) {
				worst[i] = j;
				if(i < amount-1){
					i++;
				}else break;
			}	
		}
		return worst;
	}

	public int selectReallyWorst(){
		return selectReallyWorst(1)[0];
	}
	*/
	
	public void onePointCrossover(int i, int j){
		int point 	= (int) Math.floor(rand.nextDouble()*genes);
		int[] store = new int[2*genes];
				
		for(int k = 0;k < 2*point;k++){
			store[k]		= population[i][k];
			population[i][k]= population[j][k];
			population[j][k]= store[k];
		}
	}

	public int[][] twoPointCrossover(int i, int j){
		int point1 	= (int) Math.floor(rand.nextDouble()*trips);
		int point2 	= (int) Math.floor(rand.nextDouble()*trips);
		int[][] children = new int[2][2*trips];
				
		int a		= Math.min(point1,point2);
		int b		= Math.max(point1,point2);
		
		for(int k = 0;k<2*a;k++){
			children[0][k] = population[i][k];
			children[1][k] = population[j][k];
		}
		for(int k = 2*a;k<2*b;k++){
			children[0][k] = population[j][k];
			children[1][k] = population[i][k];
		}
		for(int k = 2*b;k<2*trips;k++){
			children[0][k] = population[i][k];
			children[1][k] = population[j][k];
		}
		return children;
		
		/* ehemals
		for(int k = 2*a;k <= 2*b+1;k++){
			
			
			store[k]		 = population[i][k];
			population[i][k] = population[j][k];
			population[j][k] = store[k];
		}*/
	}
	
	/*
	public void crossover(int I1, int I2, int anzahl_points){
		int[] points = new int[anzahl_points];
		for(int i=0;i<anzahl_points;i++){
			points[i] = (int) Math.floor(rand.nextDouble()*genes);
		}
		Arrays.sort(points);
		int[] store = new int[2*genes];
		p(Arrays.toString(points));
		int zeiger = 0;
		for(int k = 0;k < 2*genes;k=k+2){
			p("zeiger:"+zeiger);
			p("k:"+k);
			if(2*points[zeiger]<=k && k<2*points[zeiger+1]){
				p("zwischen");
				store[k]		 = population[I1][k];
				population[I1][k] = population[I2][k];
				population[I2][k] = store[k];
			}
			if(k==2*points[zeiger+1]) zeiger=zeiger+2;
		}
	}
	*/
	
	public int[] mutate(int i, int times){
		int[] child = new int[2*trips];
		int zaehler = 0;
		for(int j = 1;j <= times;j++){
			
			child = population[i].clone();
			int position = (int) Math.floor(rand.nextDouble()*trips);
			int newDepot = (int) Math.floor(depots*rand.nextDouble())+1;
			int newVehicle = (int) Math.floor(VperD[newDepot-1]*rand.nextDouble())+1;
			//p("MUTATION AT pos: "+position);
			//position ändern
			child[2*position]	=	newDepot;
			child[2*position+1]	=	newVehicle;
			if(!R.checkTime(child)) {
				//p("NEU");
				j--;
			}
			if(zaehler++>1000) p("Zu häufige Wiederholung von Crossover. Vielleicht zu wenig Fahrzeuge?");
			if(!R.checkTime(population[i])) p("POPULATION[I] ist nicht korrekt");
		
		}
		return child;
	}
	
/*	
	public void mutateVonly(int i, int p){
	
		for(int j=1;j<=p;j++){
			int position = (int) Math.floor(rand.nextDouble()*fahrten);
			System.out.println("position: "+position);
			int depot = population[i][2*position];
			System.out.println("depot: "+depot);
			int newVehicle = (int) Math.floor((usedVperD[i][depot]+1)*rand.nextDouble())+1;
			System.out.println("newVehicle: "+newVehicle);
			int oldVehicle = population[i][2*position+1];
			System.out.println("oldVehicle: "+oldVehicle);
			
			//position ändern
			population[i][2*position+1]=newVehicle;
			if(newVehicle==usedVperD[i][depot]+1){
				usedVperD[i][depot]++;
			} else if(newVehicle>usedVperD[i][depot]+1) System.out.println("Fehler bei usedVperD[]");
			
			//Vehikelordnung ändern, falls oldVehicle sonst nirgends benutzt wird
			boolean only=true;
			for(int k=0;k<2*fahrten;k=k+2){
				if(population[i][k]==depot && population[i][k+1]==oldVehicle) {
					only=false;
					break;
				}
			}	
			if(only==true){
				System.out.println("only = true");
				for(int k=0;k<2*fahrten;k=k+2){
					if(population[i][k]==depot && oldVehicle<population[i][k+1]) {
						population[i][k+1]--;
					}
				}
				usedVperD[i][depot]--;
			}		
		}
	}
*/	
	
	public int[] getIndidividual(int i){
		if (population != null) {
			int[] I = new int[genes];
			I		= population[i].clone();
			return I;
		}else{
			System.out.println("Fehler: Keine Population vorhanden.");
			return null;
		}
	}

	public int[] getFitness(){
		if (population != null) {
			int[] F = new int[individuals];
			F		= fitness.clone();
			return F;
		}else{
			System.out.println("Fehler: Keine Population vorhanden.");
			return null;
		}
	}
	
	public void setFitness(int i, int f){
		fitness[i] = f;
	}
	
	public void setFitness(int[] f){
		if(f.length == individuals){
			for(int j = 0;j < individuals;j++){
				fitness[j] = f[j];
			}
		}else{
			System.out.println("Fehler: Anzahl FItnesswerte != anzahlIndividuen");
		}
	}
	
	public void printI(int i){
		if (population != null) {
			System.out.print("Depot:  ");
			for(int k = 0;k < 2*genes;k=k+2){
				System.out.print(population[i][k]);
				System.out.print(", ");
			}
			System.out.println();
			System.out.print("Vehikel:");
			for(int k = 1;k < 2*genes;k=k+2){
				System.out.print(population[i][k]);
				System.out.print(", ");
			}
			System.out.println();
		
			System.out.print("Fitness:"+fitness[i]);
		
		}else{
			System.out.println("Fehler: Keine Population vorhanden.");
		}
		
	}
			
	public void printP(){
		if (population!=null) {
			for(int i=1;i<=individuals;i++){
				System.out.println("Umlauf Nr."+i);
						
				System.out.println();
				System.out.println("mit");
				System.out.print("Depot: ");
				for(int k=0;k<2*genes;k=k+2){
					System.out.print(population[i][k]);
					System.out.print(", ");
				}
				System.out.println();
				System.out.print("Vehikel:");
				for(int k=1;k<2*genes;k=k+2){
					System.out.print(population[i][k]);
					System.out.print(", ");
				}
				System.out.println();
				System.out.println();
			}
			
			System.out.println("Fitness: ");
			for(int i = 1;i <= individuals;i++){
				System.out.print(fitness[i]+", ");
			}
			System.out.println();
		}else{
			System.out.println("Fehler: Keine Population vorhanden.");
		}
	}

	public void printPopulation(){
		if (population!=null) {
			//int[] order = getOrder(fitness);			//nach Fitness geordnet
			for(int i=0;i<individuals;i++){
				System.out.println("Individuum \t Nr."+i+"\t Fitness: "+fitness[i]);//order[i]+"\t Fitness: "+fitness[order[i]]);
				
				System.out.print("D: \t");
				for(int k=0;k<genes;k=k+2){
					System.out.print(population[i][k]);//order[i]][k]);
					System.out.print("\t");
				}
				System.out.println();
				System.out.print("V: \t");
				for(int k=0;k<genes;k=k+2){
					System.out.print(population[i][k+1]); //order[i]][k+1]);
					System.out.print("\t");
				}
				System.out.println();
			}
			System.out.println();
						
		}else{
			System.out.println("Fehler: Keine Population vorhanden.");
		}
	}
	
	public void printMatrix(){
		System.out.print("Fitness \t");
		for(int i=1;i<=genes;i++){
			System.out.print("(  "+i+" )");
		}
		System.out.println();
		System.out.println();
		for(int i=0;i<individuals;i++){
			System.out.print("  "+fitness[i]+"\t \t");
			System.out.println(Arrays.toString(population[i]));
		}
		int F =0;
		for(int i=0;i<individuals;i++){
			F+=fitness[i];
		}
		System.out.println("FitnessGesamt: "+F);
		System.out.println("-------------------------------------------");
		
	}
	
	public void printTime(){
		R.printTimes();
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
