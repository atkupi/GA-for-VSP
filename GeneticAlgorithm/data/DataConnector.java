package data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import graph.Edge;
import graph.Graph;
import graph.Node;


public class DataConnector {
	private static final String	BASIS_VERSION	= "1";
	private static final String	DB_NAME			= "jdbc:odbc:ÖPNV5";
	private static final String	DB_DRIVER		= "sun.jdbc.odbc.JdbcOdbcDriver";
	private static final String	USER_NAME		= "";
	private static final String	PASSWORD		= "";

	private Connection			connection		= null;

	public DataConnector() {
		open();
	}

	public void open() {
		try {
			Class.forName(DB_DRIVER).newInstance();
			connection = DriverManager.getConnection(DB_NAME, USER_NAME,
					PASSWORD);
		} catch (final InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		if (connection != null) try {
			connection.close();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Graph getNetwork() {
		final Graph graph = new Graph();
		try {
			final Statement stmt = connection.createStatement();
			ResultSet rslt;

			rslt = stmt
					.executeQuery("SELECT ORT_NR, ONR_TYP_NR, SEL_ZIEL, SEL_ZIEL_TYP, SEL_LAENGE FROM REC_SEL WHERE BASIS_VERSION="
							+ BASIS_VERSION);

			while (rslt.next()) {
				final int ort_nr = rslt.getInt(1);
				final int onr_typ_nr = rslt.getInt(2);
				final int sel_ziel = rslt.getInt(3);
				final int sel_ziel_typ = rslt.getInt(4);
				final int sel_laenge = rslt.getInt(5);

				Node source = graph.getNode(ort_nr, onr_typ_nr);
				if (source == null) source = new Node(ort_nr, onr_typ_nr);

				Node destination = graph.getNode(sel_ziel, sel_ziel_typ);
				if (destination == null)
					destination = new Node(sel_ziel, sel_ziel_typ);

				Edge track = graph.getTrack(source, destination);
				if (track == null)
					track = new Edge(graph.getNextEdgeId(), source,
							destination, sel_laenge);

				graph.addNode(source);
				graph.addNode(destination);
				graph.addEdge(track);
			}
		} catch (final SQLException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return graph;
	}

	
	public static void main(String[] args){
		DataConnector test = new DataConnector(); 
		p(Arrays.toString(test.getColumnAsArray("GA_TRIPS", "START_TIME")));
		
	}
	
	public int[][] giveTableAsArray(String table){
		try{
			ResultSet rsltTable = select("SELECT * FROM "+table+" ORDER BY NR ASC");
			int rows = 0;  
			while ( rsltTable.next() )  
			{  
			    // Process the row.  
			    rows++;  
			} 
			
			//get column count
			ResultSetMetaData rsmd = rsltTable.getMetaData();
			int columns = rsmd.getColumnCount();
			//p(columns);
			int[][] t = new int[rows][columns];
			
			rsltTable = select("SELECT * FROM "+table);
			
			for(int i=0;i<rows;i++){
				rsltTable.next();
				for(int j=1;j<=columns;j++){
					//p(j);
					t[i][j-1]=rsltTable.getInt(j);
				}
			}
			
			return t;
			
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return null;
		}
	}

	public String[][] TableAsArray(String select_query){
		try{
			ResultSet rsltTable = select(select_query);
			int rows = 0;  
			while (rsltTable.next()){  
			    // Process the row.  
			    rows++;  
			} 
			
			//get column count
			ResultSetMetaData rsmd = rsltTable.getMetaData();
			int columns = rsmd.getColumnCount();
			//p(columns);
			String[][] t = new String[rows][columns];
			
			rsltTable = select(select_query);
			
			for(int i=0;i<rows;i++){
				rsltTable.next();
				for(int j=1;j<=columns;j++){
					//p(j);
					t[i][j-1]=rsltTable.getString(j);
				}
			}
			rsltTable.close();
			close();
			open();
			return t;
			
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return null;
		}
	}
	
	public void insertDistances(){
		close();
		open();
		for(int i=1;i<=28;i++){
			
			
			insert("INSERT INTO GA_DISTANCES (NR, DIST, TO_1, TO_2, TO_3, TO_4, TO_5, TO_6, TO_7, " +
					"TO_8, TO_9, TO_10, TO_11, TO_12, TO_13, TO_14, TO_15, TO_16, TO_17, TO_18, " +
					"TO_19, TO_20, TO_21, TO_22, TO_23, TO_24, TO_25, TO_26, TO_27, TO_28) " +
					"VALUES " +
					"("+
					i+","+
					Math.round(distance(i))+","+
					Math.round(distance(i,1))+", "+Math.round(distance(i,2))+", "+Math.round(distance(i,3))+", "+Math.round(distance(i,4))+", "+
					Math.round(distance(i,5))+", "+Math.round(distance(i,6))+", "+Math.round(distance(i,7))+", "+Math.round(distance(i,8))+", "+
					Math.round(distance(i,9))+", "+Math.round(distance(i,10))+", "+Math.round(distance(i,11))+", "+Math.round(distance(i,12))+", "+
					Math.round(distance(i,13))+", "+Math.round(distance(i,14))+", "+Math.round(distance(i,15))+", "+Math.round(distance(i,16))+", "+
					Math.round(distance(i,17))+", "+Math.round(distance(i,18))+", "+Math.round(distance(i,19))+", "+Math.round(distance(i,20))+", "+
					Math.round(distance(i,21))+", "+Math.round(distance(i,22))+", "+Math.round(distance(i,23))+", "+Math.round(distance(i,24))+", "+
					Math.round(distance(i,25))+", "+Math.round(distance(i,26))+", "+Math.round(distance(i,27))+", "+Math.round(distance(i,28))+")");
				
			insert("INSERT INTO GA_DISTANCES_BTF (NR, FROM_BTF_1, FROM_BTF_2, FROM_BTF_3, " +
					"TO_BTF_1, TO_BTF_2, TO_BTF_3) VALUES (" +
					i+", "+
					Math.round(einsetzDistance(1, i))+", "+Math.round(einsetzDistance(2, i))+", "+Math.round(einsetzDistance(3, i))+", "+
					Math.round(aussetzDistance(i, 1))+", "+Math.round(aussetzDistance(i, 2))+", "+Math.round(aussetzDistance(i, 3))+
					")");
		}	
			p(Math.round(einsetzDistance(1, 28)));
			p(Math.round(einsetzDistance(2, 28)));
			p(Math.round(einsetzDistance(3, 28)));
			p(Math.round(aussetzDistance(28, 1)));
			p(Math.round(aussetzDistance(28, 2)));
			p(Math.round(aussetzDistance(28, 3))); 
		
		
		
	}
	
	//Entfernungberechnungen
	public double einsetzDistance(int btf, int trip){
		close();
		open();
		
		try{
			ResultSet rsltTrip = select("SELECT LI_NR, STR_LI_VAR " +
					"FROM GA_TRIPS WHERE NR="+trip);
			rsltTrip.next();
			int li = rsltTrip.getInt(1);
			String str_li_var = rsltTrip.getString(2);
			rsltTrip.close();
			return distance(btf, li, str_li_var);
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return 0;
		}
	}

	public double aussetzDistance(int trip, int btf){
		close();
		open();
		
		try{
			ResultSet rsltTrip = select("SELECT LI_NR, STR_LI_VAR " +
					"FROM GA_TRIPS WHERE NR="+trip);
			rsltTrip.next();
			int li = rsltTrip.getInt(1);
			String str_li_var = rsltTrip.getString(2);
			rsltTrip.close();
			return distance(li, str_li_var, btf);
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return 0;
		}
	}
	
	//Methode zur Berechnung der ENtfernung einer Aussetzfahrt
	public double distance(int LI, String STR_LI_VAR, int BTF){
		try{
			ResultSet rsltEnd = select("SELECT ZIEL_ORT_NR FROM LID_VERLAUF_MOD WHERE " +
					"STR_LI_VAR='"+STR_LI_VAR+"' AND LI_NR="+LI);
			rsltEnd.next();
			int end_ort_nr = rsltEnd.getInt(1);
			
			rsltEnd = select("SELECT ORT_NAME FROM REC_ORT WHERE ORT_NR="+end_ort_nr+
					" AND ONR_TYP_NR=1 AND BASIS_VERSION=1");
			rsltEnd.next();
			String end_ort = rsltEnd.getString(1);
			
			rsltEnd = select("SELECT ORT_POS_X, ORT_POS_Y FROM REC_ORT WHERE " +
					"ORT_NAME='"+end_ort+"' AND ORT_POS_X>0 AND BASIS_VERSION=1");
			rsltEnd.next();
			
			
			double end_ort_x = rsltEnd.getDouble(1);
			double end_ort_y = rsltEnd.getDouble(2);

			rsltEnd.close();
			
			ResultSet rsltBtf = select("SELECT X_KOOR, Y_KOOR FROM REC_BTF WHERE " +
					"BTF_NR="+BTF);
			rsltBtf.next();
			int btf_x = rsltBtf.getInt(1);
			int btf_y = rsltBtf.getInt(2);
			
			rsltBtf.close();
			
			return euklidDistance(end_ort_x,end_ort_y,btf_x,btf_y);
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return 0;
		}
	}
	
	
	public double distance(int trip){
		try{
			ResultSet rsltStart = select("SELECT LI_NR, STR_LI_VAR FROM GA_TRIPS WHERE " +
					"NR="+trip);
			rsltStart.next();
			int li_nr = rsltStart.getInt(1);
			String str_li_nr = rsltStart.getString(2);
			
			rsltStart = select("SELECT START_ORT_NR, ZIEL_ORT_NR FROM LID_VERLAUF_MOD " +
					"WHERE LI_NR="+li_nr+" AND STR_LI_VAR='"+str_li_nr+"'");
			rsltStart.next();
			int start_ort = rsltStart.getInt(1);
			int end_ort = rsltStart.getInt(2);
			rsltStart.close();
			
			//p(start_ort+";"+end_ort);
			
			Graph G = getNetwork();
			Graph t = null;
			
			t=G.getSubgraph((int) start_ort, (int) end_ort);
			if(t==null) p("?????");
			return t.giveTrackLength();
		
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return 0;
		}
	}
	
	//für eine Einsetzfahrt
	public double distance(int BTF, int LI, String STR_LI_VAR){
		try{
			ResultSet rsltStart = select("SELECT START_ORT_NR FROM LID_VERLAUF_MOD WHERE " +
					"STR_LI_VAR='"+STR_LI_VAR+"' AND LI_NR="+LI);
			rsltStart.next();
			int start_ort_nr = rsltStart.getInt(1);
			
			rsltStart = select("SELECT ORT_NAME FROM REC_ORT WHERE ORT_NR="+start_ort_nr+
					" AND ONR_TYP_NR=1 AND BASIS_VERSION=1");
			rsltStart.next();
			String start_ort = rsltStart.getString(1);
			//p(start_ort+";"+BTF);
			rsltStart = select("SELECT ORT_POS_X, ORT_POS_Y FROM REC_ORT WHERE " +
					"ORT_NAME='"+start_ort+"' AND ORT_POS_X>0 AND BASIS_VERSION=1");
			rsltStart.next();
			double start_ort_x = rsltStart.getDouble(1);
			double start_ort_y = rsltStart.getDouble(2);
			
			rsltStart.close();
			
			ResultSet rsltBtf = select("SELECT X_KOOR, Y_KOOR FROM REC_BTF WHERE " +
					"BTF_NR="+BTF);
			rsltBtf.next();
			int btf_x = rsltBtf.getInt(1);
			int btf_y = rsltBtf.getInt(2);
			
			rsltBtf.close();
			
			return euklidDistance(start_ort_x,start_ort_y,btf_x,btf_y);
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return 0;
		}
	}
	
	public double distance(int trip1, int trip2){
		close();
		open();
		try{
			//p(trip1+";"+trip2);
			
			String order = "DESC";
			if(trip1<trip2) order = "ASC";
			
				
			ResultSet rsltTrips = select("SELECT LI_NR, STR_LI_VAR " +
					"FROM GA_TRIPS WHERE NR="+trip1+" OR NR="+trip2+
					" ORDER BY NR "+order);
			rsltTrips.next();
			int li1 = rsltTrips.getInt(1);
			String str_li_var1 = rsltTrips.getString(2);
			
			
			if(trip1!=trip2){
				rsltTrips.next();
				int li2 = rsltTrips.getInt(1);
				String str_li_var2 = rsltTrips.getString(2);
				
				rsltTrips.close();
				//p(li1+";"+ str_li_var1+";"+ li2+";"+ str_li_var2);
				
				return distance(li1, str_li_var1, li2, str_li_var2);
			}else{
				rsltTrips.close();
				return distance(trip1);
			}
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return 0;
		}
	}
	
	//Methode zur Berechnung der Entfernung einer Verbindungsfahrt EndeFahrt1-StartFahrt2 nach Koordinaten
	public double distance(int LI_BEFORE, String STR_LI_VAR_BEFORE, int LI_AFTER, String STR_LI_VAR_AFTER) {
		try{
			
			//p(LI_BEFORE+";"+STR_LI_VAR_BEFORE+";"+LI_AFTER+";"+STR_LI_VAR_AFTER);
			
			
			ResultSet rsltTripBefore = select("SELECT ZIEL_ORT_NR FROM LID_VERLAUF_MOD " +
					"WHERE LI_NR="+ LI_BEFORE+" AND STR_LI_VAR='"+STR_LI_VAR_BEFORE+"'");
			
			ResultSet rsltTripAfter = select("SELECT START_ORT_NR FROM LID_VERLAUF_MOD " +
					"WHERE LI_NR="+ LI_AFTER+" AND STR_LI_VAR='"+STR_LI_VAR_AFTER+"'");
			
			rsltTripBefore.next();
			rsltTripAfter.next();
			
			
			int start_ort = rsltTripBefore.getInt(1);
			int ziel_ort = rsltTripAfter.getInt(1);
			//p(start_ort+";"+ziel_ort);
			rsltTripBefore.close();
			rsltTripAfter.close();
			
			ResultSet rsltStart = select("SELECT ORT_NAME FROM REC_ORT WHERE ONR_TYP_NR=1" +
					" AND ORT_NR="+start_ort+" AND BASIS_VERSION=1");
			rsltStart.next();
			String Start_ort = rsltStart.getString(1);
			
			rsltStart.close();
			
			ResultSet rsltZiel = select("SELECT ORT_NAME FROM REC_ORT WHERE ONR_TYP_NR=1" +
					" AND ORT_NR="+ziel_ort+" AND BASIS_VERSION=1");
			rsltZiel.next();
			String Ziel_ort = rsltZiel.getString(1);
			rsltZiel.close();
			//p(Start_ort+";"+Ziel_ort+";");
			if(!Start_ort.equals(Ziel_ort)) {
				
				ResultSet rslt = select("SELECT ORT_POS_X, ORT_POS_Y FROM REC_ORT WHERE " +
						"(ORT_NAME='"+Start_ort+"' OR ORT_NAME='"+Ziel_ort+"') AND " +
								"ORT_POS_X>0"+" AND BASIS_VERSION=1");
				
				rslt.next();
				double start_x = rslt.getInt(1);
				double start_y = rslt.getInt(2);
				
				rslt.next();
				double ziel_x = rslt.getInt(1);
				double ziel_y = rslt.getInt(2);
				//p("----"+start_x+","+ziel_x);
				rslt.close();
				return euklidDistance(start_x, start_y, ziel_x, ziel_y);
			}else {
				
				return 0;
			}
		
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return 0;
		}
	}
		
	public double euklidDistance(double x1, double y1, double x2, double y2){
		return Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
	}

	
	public ResultSet select(String query){
	
		final ResultSet rslt;
		try {
			final Statement stmt = connection.createStatement();
			rslt = stmt.executeQuery(query);

			

		} catch (final SQLException e) {
			System.err
					.println("LichtOMat.readLineData() > Fehler: SQLException");
			e.printStackTrace();
			return null;
		}

		return rslt;
	}
	
	public void insert(String query){
		try {
			final Statement stmt = connection.createStatement();
			int rslt = stmt.executeUpdate(query);


		} catch (final SQLException e) {
			System.err
					.println("LichtOMat.readLineData() > Fehler: SQLException");
			e.printStackTrace();
			
		}

	}
	
	
	//--------OFF TO A FRESH NEW START :D
	
	//.....HELPER....
	public int[] getCoordinates(int station_ref){
		try{
			
			ResultSet rslt = select("SELECT ORT_POS_X, ORT_POS_Y FROM REC_ORT " +
					"WHERE BASIS_VERSION=1 AND ORT_NR="+ station_ref);
			
			if(rslt.next()){
				
				int[] r = new int[] {rslt.getInt(1), rslt.getInt(2)};
				rslt.close();
				return r;
			}else{
				rslt = select("SELECT X_KOOR, Y_KOOR FROM REC_BTF " +
						"WHERE ORT_REF_ORT="+ station_ref);
				if(rslt.next()){
					int[] r = new int[] {rslt.getInt(1), rslt.getInt(2)};
					rslt.close();
					return r;
				}else {
					p("ERROR: Station_Ref "+station_ref+" is no Station or BTF");
					return null;
				}
				
			}
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return null;
		}
	}
	
	public int getStartStation(int line, String variant){
		try{
			close();
			open();
		
			ResultSet rslt = select("SELECT ORT_NR FROM LID_VERLAUF " +
					"WHERE BASIS_VERSION=1 AND LI_NR="+ line+" AND STR_LI_VAR='"+variant+"' " +
							"ORDER BY LI_LFD_NR ASC");
			
			rslt.next();
			int ort_nr = rslt.getInt(1);
			
			int r = getREF_ORT(ort_nr);
		
			rslt.close();
			return r;
			
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();

			p("Fehler (StartStation): " + line+", "+variant);
			return 0;
		}
	}
	
	public int getEndStation(int line, String variant){
		try{
			close();
			open();
			
			ResultSet rslt = select("SELECT ORT_NR FROM LID_VERLAUF " +
					"WHERE BASIS_VERSION=1 AND LI_NR="+ line+" AND STR_LI_VAR='"+variant+"' " +
							"ORDER BY LI_LFD_NR DESC");
			
			rslt.next();
			int ort_nr =rslt.getInt(1);
			//p("OrtNR: "+ort_nr);
			int r = getREF_ORT(ort_nr);
			rslt.close();
			return r;
			
			
		}catch(SQLException e){
			
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();

			p("Fehler (EndStation): " + line+", "+variant);
			return 0;
		}
	}
	
	public int getREF_ORT(int ort_nr){
		try{
			ResultSet rslt = select("SELECT ORT_REF_ORT FROM REC_ORT " +
					"WHERE BASIS_VERSION=1 AND ORT_NR="+ort_nr);
			
			rslt.next();
			int r = rslt.getInt(1);
			
			rslt.close();
			return r;
			
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			p("Fehler ORT_NR (getREF): " + ort_nr);
			return 0;
		}
	}
	
	public int getBTF_REF_ORT(int nr){
		try{
			ResultSet rslt = select("SELECT ORT_REF_ORT FROM REC_BTF " +
					"WHERE BTF_NR="+nr);
			
			rslt.next();
			int r = rslt.getInt(1);
			
			rslt.close();
			return r;
			
			
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			p("Fehler BTF_NR (getBTF_REF): " + nr);
			p("So viele BTF existieren nicht");
			return -1;
		}
	}
	
	public double getDistance(int start_ref, int end_ref){
		int[] start_xy = new int[2];
		int[] end_xy = new int[2];
		
		start_xy = getCoordinates(start_ref);
		end_xy = getCoordinates(end_ref);

		return euklidDistance(start_xy[0], start_xy[1], end_xy[0], end_xy[1]);			
	}
	
	public int getGAStartStation(int trip){
		try{
			ResultSet rslt = select("SELECT START_ORT_REF FROM GA_TRIPS " +
					"ORDER BY START_TIME, LI_NR, STR_LI_VAR");
			for(int i=0;i<trip;i++) rslt.next();
			return rslt.getInt(1);
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getGAEndStation(int trip){
		try{
			ResultSet rslt = select("SELECT END_ORT_REF FROM GA_TRIPS " +
					"ORDER BY START_TIME, LI_NR, STR_LI_VAR");
			for(int i=0;i<trip;i++) rslt.next();
			return rslt.getInt(1);
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return -1;
		}
	}
	
	public double getConnectionDistance(int trip_a, int trip_b){
		if(trip_a==trip_b){
			return getDistance(getGAStartStation(trip_a), getGAEndStation(trip_a));
		}else{
			int vstart_ref 	= getGAEndStation(trip_a);
			int vend_ref	=getGAStartStation(trip_b);
			
			//p(vstart_ref+", "+vend_ref);
			
			int[] start_xy = new int[2];
			int[] end_xy = new int[2];
			
			start_xy = getCoordinates(vstart_ref);
			end_xy = getCoordinates(vend_ref);
			
			/*
			if(trip_b+trip_a %25==0){
				close();
				open();
			}
			*/
			
			return euklidDistance(start_xy[0], start_xy[1], end_xy[0], end_xy[1]);			
		}
		
	}
	
	public double getEinsetzDistance(int btf, int trip) {
		close();
		open();
		return getDistance(getBTF_REF_ORT(btf), getGAStartStation(trip));
	}
	
	public double getAussetzDistance(int trip, int btf) {
		return getDistance(getGAEndStation(trip), getBTF_REF_ORT(btf));
	}
	
	public int numberRows(String table){
		try{
			
			ResultSet rslt = select("SELECT * FROM "+ table);
			int number = 0;
			
			while(rslt.next()) number++;
		
			return number;		
		}catch(SQLException e){
			System.err
			.println("insertTrip > Fehler: SQLException");
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public static void p(Object s){
		System.out.println(s);
	}

	//..............
	
	public int[] getColumnAsArray(String table, String column){
		try{
			ResultSet rslt = select("SELECT "+column+" FROM "+table+
					" ORDER BY START_TIME, LI_NR, STR_LI_VAR");
			List<Integer> entry = new ArrayList<Integer>();
			
			while(rslt.next()) entry.add(rslt.getInt(1));
			
			int[] int_column = new int[entry.size()];
			int i = 0;
			for(Integer integer : entry) int_column[i++]=integer;
			return int_column;
		}catch(SQLException e){
			System.err
			.println("getColumnAsArray > Fehler: SQLException");
			e.printStackTrace();
			return null;
		}
	}
	
	public void initializeTrips(int start_time, int end_time, int variant_max){			
	
			String[][] table = TableAsArray("SELECT FRT_START, LI_NR, STR_LI_VAR " +
					"FROM CATS_FRT_POOL WHERE ID=14 AND " +
					"FRT_START>"+start_time+" " +
					"AND FRT_START<"+end_time+" AND LI_KU_NR<="+variant_max +
					" ORDER BY FRT_START, LI_NR, STR_LI_VAR");
			
			
			close();
			open();
			
			for(int i=0;i<table.length;i++){
				int time = Integer.valueOf(table[i][0]);
				int li = Integer.valueOf(table[i][1]);
				String str_li_var = table[i][2];
				int start_ref = getStartStation(li, str_li_var);
				int end_ref = getEndStation(li, str_li_var);
				double distance = getDistance(start_ref, end_ref);
				int v_typ = 0;
				if(li==3 || li==4 || li==5 || li==12 || li==13 || li==15 || li==16 || li==17 || li==18 || li==19) {
					v_typ=2;
				}else v_typ = 1; 		//Typaufteilung Hoch-/Niedrflur
				
				
				insert("INSERT INTO GA_REC_TRIPS " +
						"(NR, LI_NR, STR_LI_VAR, V_TYP, START_TIME, "+
						"END_TIME, "+
						"START_ORT_REF, END_ORT_REF, DIST)" +
						" VALUES (" +
						(i+1)+", "+li+", '"+str_li_var+"', "+ v_typ +", "+time+", "+
						Math.round(time+distance*2280/13608)+", "+//geschwindigkeit anhand Linie9: Königsforst(18715) - Sülz(13217) in 38min = 2280sec, Distanz=13608m
						start_ref+", "+end_ref+", "+distance+")");
				
				//clear screen
				for (int ii=0; ii<60; ii++) {
				    // scroll down one line
				    p("");
				}
				p("table.length="+table.length+"  ,  i="+i);
				p(Math.round(100*i/table.length)+"% done");
				
				
				//TODO: LETZTE FAHRT WIRD NICHT EINGELSESN
			}
			
		
	}

	public void initializeGATrips(int start_time, int end_time, int variant_max){
		
		
		insert("DELETE FROM GA_TRIPS");
		
		close();
		open();
		String[][] table = TableAsArray("SELECT NR, LI_NR, STR_LI_VAR, V_TYP, " +
				"START_TIME, END_TIME, START_ORT_REF, END_ORT_REF, DIST " + 
				"FROM GA_REC_TRIPS "+
				"WHERE START_TIME>="+start_time+" AND START_TIME<="+end_time+
				" ORDER BY START_TIME, LI_NR, STR_LI_VAR");
		
		//" AND STR_LI_VAR<="+variant_max+
		for(int i=0;i<table.length;i++){
			insert("INSERT INTO GA_TRIPS (NR, LI_NR, STR_LI_VAR, V_TYP, " +
				"START_TIME, END_TIME, START_ORT_REF, END_ORT_REF, DIST) " +
				"VALUES (" +
				Integer.valueOf(table[i][0])+", "+
				Integer.valueOf(table[i][1])+", '"+
				table[i][2]+"', "+
				Integer.valueOf(table[i][3])+", "+
				Integer.valueOf(table[i][4])+", "+
				Integer.valueOf(table[i][5])+", "+
				Integer.valueOf(table[i][6])+", "+
				Integer.valueOf(table[i][7])+", "+
				Integer.valueOf(table[i][8])+")");
			
			if(i%100==0) {
				close();
				open();
			}
			
			for (int ii=0; ii<60; ii++) {
			    // scroll down one line
			    p("");
			}
			p("Generate Trips for GA: "+Math.round(100*i/(table.length-1))+"% done");
		}
		pause();
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
