package reviser;

import java.util.Arrays;

import data.DataConnector;

public class Reviser {

	DataConnector connection;
	
	int trips;
	
	int[] start_time;
	int[] end_time;
	
	public Reviser(int t){
		trips = t;
		
		start_time = new int[trips];
		end_time = new int[trips];
		
		connection = new DataConnector();
		
		start_time = connection.getColumnAsArray("GA_TRIPS", "START_TIME").clone();
		end_time = connection.getColumnAsArray("GA_TRIPS", "END_TIME").clone();
	}
	
	public boolean checkTime(int[] I){ //I sollte ein trips*2 Array sein
		for(int i=0;i<I.length;i=i+2){
			//p(i);
			int current_depot = I[i];
			int current_vehicle = I[i+1];
			int current_end_time = end_time[i/2];
			
			/*search next trip of current vehicle*/
			int j = i+2;
			while(j<I.length){
				
				
				if(current_depot == I[j] && current_vehicle == I[j+1]) break;
				j=j+2;
			}
			//p("j: "+j);
			/* bisher verbindungszeit noch nicht bedacht*/
			if(j<I.length){
				if (current_end_time > start_time[j/2] && j<I.length) {
					//p("FALSE");
					return false;
				}
			}
		}
		//p("TRUE");
		return true;
	}

	/* checkTimeConstraint
	 * gives 0 if constraint is held, otherwise i 
	 * the place where it is uncorrect the first time
	 */
	public int checkTimeConstraint(int[] I, int start_index){ //I sollte ein trips*2 Array sein
		for(int i=start_index;i<I.length;i=i+2){
			//p(i);
			int current_depot = I[i];
			int current_vehicle = I[i+1];
			int current_start_time = start_time[i/2];
			
			/*search next trip of current vehicle*/
			int j = i-2;
			while(j>=0){
				//p("in Schleife: j = "+j);
				
				if(current_depot == I[j] && 
						current_vehicle == I[j+1]) break;
				j=j-2;
			}

			/* bisher verbindungszeit noch nicht bedacht*/
			if(j>=0){
				if (current_start_time < end_time[j/2]) {
					//p("FALSE");
					return i;
				}
			}
		}
		//p("TRUE");
		return -1;
	}
	
	public static void main(String[] args){
		Reviser R = new Reviser(8);
		DataConnector c = new DataConnector();
		p(Arrays.toString(c.getColumnAsArray("GA_TRIPS", "START_TIME")));
		p(Arrays.toString(c.getColumnAsArray("GA_TRIPS", "END_TIME")));
		R.checkTime(new int[] 	{1,1,
									1,12,
									1,13,
									1,14,
									1,11,
									1,16,
									1,19,
									1,18});
	}
	
	public void printTimes(){
		p("Starting at \t "+Arrays.toString(start_time));
		p("Ending at \t "+Arrays.toString(end_time));
	}
	
	public static void p(Object s){
		System.out.println(s);
	}
}
