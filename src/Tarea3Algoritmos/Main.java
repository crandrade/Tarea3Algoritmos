package Tarea3Algoritmos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;



public class Main {
	static protected char[] keys;
	
	@SuppressWarnings("resource")
	static public char [] init(String filename) throws FileNotFoundException{
		String line = "";
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		line = scanner.nextLine();
		while (scanner.hasNextLine()) {
		       line += scanner.nextLine();
		}
		//line = line.trim().replaceAll("[^0-9 ]", "");
		return line.toCharArray();
	}
	static public void destroy(char[] text){
		text = null;
		System.gc();
	}
	
	static public int [] generateIntArray(boolean o, int [] chain, int l){
		int [] patron = new int [10000];
		int max = Integer.MAX_VALUE;
		if(o){ // o == true
			int k = (int)Math.pow(2,l);
			for(int j = 0; j< 10000; j++){
				int i = ThreadLocalRandom.current().nextInt(0, k);
				patron[j] = chain[i];
			}
		}
		else{ //o == false
			for(int j = 0; j< 10000; j++){
				patron[j] = ThreadLocalRandom.current().nextInt(0, max);
			}
		}
		return patron;
	}
	
	static public boolean acceptableError(SummaryStatistics summary, int iterations){
		if(iterations > 1){
			return (((summary.getStandardDeviation()/summary.getMean()) <= 0.05)
					&& (((2*summary.getStandardDeviation())/Math.sqrt(iterations))>=0.95));
		}
		else return false;
	}
	
	static public void main(String [] args) throws IOException{
		Option iter = new Option("iterations", "Number of iterations");
		iter.setArgs(1);
		iter.setRequired(true);
		Option power = new Option("i", "Log (Min lenght of patron)");
		power.setArgs(1);
		power.setRequired(true);
		Option power2 = new Option("I", "Log (Max lenght of patron)");
		power2.setArgs(1);
		power2.setRequired(true);

		Options options = new Options();
		options.addOption(iter);
		options.addOption(power);
		options.addOption(power2);

		
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("***ERROR: " + e.getClass() + ": " + e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options );
			return;
		}

		String dir = new SimpleDateFormat("YYYYMMDD HH:mm:ss.S").format(new Date())+" output.txt";
		System.err.println("Opening file at  "+dir);
		File fDir = new File(dir);
		PrintWriter printer = new PrintWriter(new FileWriter(fDir,true));
		
		int l=2;
		if (cmd.hasOption("i")) {
			int nn = Integer.parseInt(cmd.getOptionValue(power.getOpt()));
			if(nn>1 && nn<8)
			l=nn;
		}
		System.err.println(l);
		int L=7;
		if (cmd.hasOption("I")) {
			int nn = Integer.parseInt(cmd.getOptionValue(power2.getOpt()));
			if(nn>1 && nn<8 && nn>=l)
			L=nn;
		}
		System.err.println(L);
		int max_it=50000;
		if (cmd.hasOption("iterations")) {
			int nn = Integer.parseInt(cmd.getOptionValue(iter.getOpt()));
			if(nn>100 && nn<100000)
			max_it=nn;
		}		
		boolean random = true;
		boolean extracted = false;
		long t=0;
		SummaryStatistics ABBsize;
		SummaryStatistics ABBtime;
		SummaryStatistics AVLsize;
		SummaryStatistics AVLtime;
		SummaryStatistics SPLsize;
		SummaryStatistics SPLtime;
		SummaryStatistics VEBsize;
		SummaryStatistics VEBtime;
		
		
		// test binary
		if (cmd.hasOption("b")) {
			binarytext = init("binary.txt");
			printer.println("Binary Text");
			GenericTree abb = new ABB();
			GenericTree avl = new AVL();
			GenericTree spl = new SplayTree();
			//GenericTree veb = new VanEmdeBoas();
			GenericTree veb = new DummyVanEmdeBoas();
			printer.println("Binary:");
			for(int i=l; i<=L; i++){
				System.err.println("2^"+i);
				printer.println("2^"+i);
				ABBsize = new SummaryStatistics();
				ABBtime = new SummaryStatistics();
				AVLsize = new SummaryStatistics();
				AVLtime = new SummaryStatistics();
				SPLsize = new SummaryStatistics();
				SPLtime = new SummaryStatistics();
				VEBsize = new SummaryStatistics();
				VEBtime = new SummaryStatistics();
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(random, 
							binarytext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BFsum.addValue((double)(brute.search(patron)));
					BFtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados binary BF");
						System.err.println("Promedio tiempo: "+(BFtime.getMean()/1000)+" seg.");
						System.err.println("DEstandar tiempo: "+(BFtime.getStandardDeviation()/1000)+" seg.");
						System.err.println("Error tiempo: "+((2*BFtime.getStandardDeviation())/
								Math.sqrt(iterations)*1000)+" seg.");
						System.err.println("Promedio comps: "+(BFsum.getMean())+" comps.");
						System.err.println("DEstandar comps: "+(BFsum.getStandardDeviation())+" comps.");
						System.err.println("Error comps: "+((2*BFsum.getStandardDeviation())/
								Math.sqrt(iterations))+" comps.");
					}
						if(acceptableError(BFsum, iterations) 
							&& acceptableError(BFtime, iterations) || iterations >= max_it){
						printer.println("BF Iterations: "+iterations);
						printer.println("BFtime:\t"+BFtime.getMean()+"\t"+BFtime.getVariance()+"\t"+BFtime.getStandardDeviation());
						printer.println("BFsum:\t"+BFsum.getMean()+"\t"+BFsum.getVariance()+"\t"+BFsum.getStandardDeviation());
						break;
					}
				}
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(random, 
							binarytext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					KMPsum.addValue((double)(kmp.search(patron)));
					KMPtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados binary KMP");
						System.err.println("Promedio tiempo: "+(KMPtime.getMean()/1000)+" seg.");
						System.err.println("DEstandar tiempo: "+(KMPtime.getStandardDeviation()/1000)+" seg.");
						System.err.println("Error tiempo: "+((2*KMPtime.getStandardDeviation())/
								Math.sqrt(iterations)*1000)+" seg.");
						System.err.println("Promedio comps: "+(KMPsum.getMean())+" comps.");
						System.err.println("DEstandar comps: "+(KMPsum.getStandardDeviation())+" comps.");
						System.err.println("Error comps: "+((2*KMPsum.getStandardDeviation())/
								Math.sqrt(iterations))+" comps.");
					}
					if(acceptableError(KMPsum, iterations) 
							&& acceptableError(KMPtime, iterations) || iterations >= max_it){
						printer.println("KMP Iterations: "+iterations);
						printer.println("KMPtime:\t"+KMPtime.getMean()+"\t"+KMPtime.getVariance()+"\t"+KMPtime.getStandardDeviation());
						printer.println("KMPsum:\t"+KMPsum.getMean()+"\t"+KMPtime.getVariance()+"\t"+KMPtime.getStandardDeviation());
						break;
					}
				}
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(random, 
							binarytext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BMHsum.addValue((double)(bmh.search(patron)));
					BMHtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados binary BMH");
						System.err.println("Promedio tiempo: "+(BMHtime.getMean()/1000)+" seg.");
						System.err.println("DEstandar tiempo: "+(BMHtime.getStandardDeviation()/1000)+" seg.");
						System.err.println("Error tiempo: "+((2*BMHtime.getStandardDeviation())/
								Math.sqrt(iterations)*1000)+" seg.");
						System.err.println("Promedio comps: "+(BMHsum.getMean())+" comps.");
						System.err.println("DEstandar comps: "+(BMHsum.getStandardDeviation())+" comps.");
						System.err.println("Error comps: "+((2*BMHsum.getStandardDeviation())/
								Math.sqrt(iterations))+" comps.");
					}
					if(acceptableError(BMHsum, iterations) 
							&& acceptableError(BMHtime, iterations) || iterations >= max_it){
						printer.println("BMH Iterations: "+iterations);
						printer.println("BMHtime:\t"+BMHtime.getMean()+"\t"+BMHtime.getVariance()+"\t"+BMHtime.getStandardDeviation());
						printer.println("BMHsum:\t"+BMHsum.getMean()+"\t"+BMHtime.getVariance()+"\t"+BMHtime.getStandardDeviation());
						break;
					}
				}
				
			}
			destroy();
			printer.println();
		}
	}
}
