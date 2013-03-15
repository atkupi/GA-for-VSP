/*
*author: Aleksander Kupicha
*/
package fitness;

import java.util.List; 
import java.util.ArrayList;

import data.DataConnector;
//import data.DataConnector;

import java.io.*;

public class Costs {
	int 			trips;
	int 			depots;
	
	int[][] 		tripCostsForDepot;
	int[][] 		connectionCosts;
	
	DataConnector 	connection;
	
	public Costs(boolean pausiert){
		p("COSTS");
		connection = new DataConnector();
		
		trips = connection.numberRows("GA_TRIPS");
		depots = connection.numberRows("REC_BTF");
		p("trips und depots: "+ trips+","+depots);
		
		//Fahrtenkosten nach Typ/Depot
		tripCostsForDepot = new int[depots][trips];
		for(int i=0;i<depots;i++){
			for(int j=0;j<trips;j++){
				tripCostsForDepot[i][j]=1;		//hier: alle gleich
			}
		}
		
		//Verbindungskosten
		p("START GENERATING CONNECTIONCOSTS");
		int[][] dummy = new int[depots+trips][depots+trips];
		
				/**[i][j] : Kosten von i nach j
				// i,j = 0,...,depots-1 stellen Depots dar
				// i,j = depots,...,depots+trips-1 stellen Servicefahrten dar.
				
			//Zunächst die reinen Verbindungsfahrten **/
		
		p("START FOR LOOP");
		for(int i=depots;i<depots+trips;i++){
			int kk=0;
			for(int j=depots;j<depots+trips;j++){
				if(kk++ % 25==0) {
					connection.close();
					connection.open();
				}
				
				dummy[i][j] = (int) Math.round(connection.getConnectionDistance(i-depots+1, j-depots+1));
			
			
					//clear screen
					for (int ii=0; ii<60; ii++) {
					    // scroll down one line
					    p("");
					}
					p(kk);
					p("Load connectionCosts (trips): "+Math.round(100*(i+1)/(depots+trips))+"% done");
					p("Load connectionCosts (rest): ");
			}
		}
		if(pausiert) pause();
			/** Einsetz- und Aussetzfahrten **/
	
		for(int i=0;i<depots;i++){
			for(int j=depots;j<depots+trips;j++){
				//p(j);
				dummy[i][j] = (int) Math.round(connection.getEinsetzDistance(i+1, j-depots+1));
				dummy[j][i] = (int) Math.round(connection.getAussetzDistance( j-depots+1, i+1));
			}
					//clear screen
					for (int ii=0; ii<60; ii++) {
					    // scroll down one line
					    p("");
					}
					p("Load connectionCosts (trips): 100%");
					p("Load connectionCosts (rest): "+Math.round(100*i/(depots-1))+"% done");
		}
		
		
		connectionCosts = dummy.clone();
		
		if(pausiert){
			print();
			pause();
		}
		
	}
	
     public Costs(int t, int d, int alt) {
		trips	= t;
		depots	= d;
		
		tripCostsForDepot 	= new int[][] {	{0,0,0,0},
											{1,1,1,1},
											{3,3,3,3}};
											
		connectionCosts 	= new int[][] { {0,0,0,	0,0,0,0},
											{0,0,0,	3,3,3,3},	//Einsetzkosten
											{0,0,0,	5,5,4,4},
											{0,1,3,	0,0,3,2},
											{0,1,3,	0,0,2,3},
											{0,1,2,	3,2,0,0},
											{0,1,2,	2,3,0,0}};
    }

    //3 BTF, für jede Linie zwei Fahrten; BW02 & FW01; das sind 28 fahrten
	public Costs(int ALT) {
		
		depots = 3;
		trips = 28;
		connectionCosts = new int[depots+trips+1][depots+trips+1];
		tripCostsForDepot = new int[depots+1][trips];
		
		connection = new DataConnector();
		
		//von Depot abhängige Fahrtkosten
		
		for(int i=0;i<=depots;i++){
			for(int j=0;j<trips;j++){
				tripCostsForDepot[i][j]=1;
			}
		}
		
		//connectionCost einlesen aus Tabellen 'GA_DISTANCES' & 'GA_DISTANCES_BTF'
		
		
		int[][] verbindungskosten = connection.giveTableAsArray("GA_DISTANCES");
		int[][] btf_kosten = connection.giveTableAsArray("GA_DISTANCES_BTF");
		
		if(trips!=verbindungskosten.length) p("Fehler: Anzahl Trips");
		//p(verbindungskosten.length+";"+verbindungskosten[0].length);
		for(int i=0;i<trips;i++){
			for(int j=0;j<trips;j++){
				//p(i+";"+j);
				connectionCosts[depots+i+1][depots+j+1] = verbindungskosten[i][j+3];
				
			}
		}
		//ensetz-/aussetzkosten einlesen aus 'GA_DISTANCES_BTF'
		for(int i=0;i<trips;i++){
			for(int j=0;j<depots;j++){
				connectionCosts[j+1][depots+i+1] = btf_kosten[i][j+2];
				
				connectionCosts[depots+i+1][j+1] = btf_kosten[i][j+5];
			}
		}
		/*
		p(connectionCosts[1][4]);
		p(connectionCosts[1][4]);
		p(connectionCosts[1][4]);
		
		p(connectionCosts[0][0]+";"+connectionCosts[0][1]+";"+connectionCosts[0][2]+";"+connectionCosts[0][3]+";"+connectionCosts[0][4]+";"+connectionCosts[0][5]);
		p(connectionCosts[1][0]+";"+connectionCosts[1][1]+";"+connectionCosts[1][2]+";"+connectionCosts[1][3]+";"+connectionCosts[1][4]+";"+connectionCosts[1][5]);
		p(connectionCosts[2][0]+";"+connectionCosts[2][1]+";"+connectionCosts[2][2]+";"+connectionCosts[2][3]+";"+connectionCosts[2][4]+";"+connectionCosts[2][5]);
		p(connectionCosts[3][0]+";"+connectionCosts[3][1]+";"+connectionCosts[3][2]+";"+connectionCosts[3][3]+";"+connectionCosts[3][4]+";"+connectionCosts[3][5]);
		p(connectionCosts[4][0]+";"+connectionCosts[4][1]+";"+connectionCosts[4][2]+";"+connectionCosts[4][3]+";"+connectionCosts[4][4]+";"+connectionCosts[4][5]);
		p(connectionCosts[depots+1][depots+1]);
		p(connectionCosts[depots+trips-1][depots+trips-1]);

		p(connectionCosts[depots+trips][depots+trips]);
		*/
		
		
	
		
    }


public static void main(String[] args){

	Costs c = new Costs(false);

	p(c.apply(new int[] {1,1, 1,7, 1,2, 2,8, 2,1, 3,3, 3,1, 1,4}));
	p(c.apply(new int[] {2,6, 1,7, 1,2, 2,8, 2,1, 3,3, 3,8, 1,4}));
}

	
/*Methoden 
*/
	public int apply(int[] I){
		if (I.length == 2*trips) { 
			int cost = 0;
			List<int[]> belegteVehikel = new ArrayList<int[]>();

			for(int i = 0;i < trips;i++){		//jede zu fahrende Fahrt
				
				int[] vehikel = new int[] {I[2*i],I[2*i+1]};	//Prüfe ob akt. Vehikel
				if (!belegteVehikel.contains(vehikel)){			//noch im Depot ist 
					belegteVehikel.add(vehikel);				//
					/*
					p("---");
					
					p(connectionCosts[1][4]);
					p("---");
					p("vehikel:"+vehikel[0]+","+depots);
					*/
					cost += connectionCosts[vehikel[0]-1][depots+i];	//Einsetzfahrtkosten
				}
				
				cost += tripCostsForDepot[vehikel[0]-1][i];	//Fahrtkosten
				
				int j=i+1;
				while(j < trips){
					if(I[2*j] == I[2*i] && 					//Suche nächste Fahrt des akt.
							I[2*j+1] == I[2*i+1]){			//Vehikels
						break;
					} else j++;
				}
				
				if (j < trips){												//hat das akt.Vehikel eine FOlgefahrt werden
					cost += connectionCosts[depots+i][depots+j];		//Verbindungskosten addiert,
				} else cost += connectionCosts[depots+i][vehikel[0]-1];		//ansonsten Aussetzkosten
																	
			}
			return cost;													
		}else {
			System.out.println("Fehler: Individuum passt nicht zu konfigurierter FitnessFunktion");
			return -1;
		}
	}
	
	
	
/*---------HELPER------------- */
	public void print(){
		for(int i = 0;i < depots+trips;i++){
			//p(i);
			for(int j = 0;j < depots+trips;j++){
				System.out.print(connectionCosts[i][j]+"\t");
			}
			System.out.println();
		}	
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



