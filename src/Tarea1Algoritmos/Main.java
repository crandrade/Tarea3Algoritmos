package Tarea1Algoritmos;

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
	static protected char[] binarytext;
	static protected char[] realDNA;
	static protected char[] fakeDNA;
	static protected char[] plaintext;
	static protected char[] faketext;
	
	
	@SuppressWarnings("resource")
	static public char [] init(String filename) throws FileNotFoundException{
		String line = "";
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		line = scanner.nextLine();
		while (scanner.hasNextLine()) {
		       line += scanner.nextLine();
		}
		//line = line.trim().replaceAll("[^A-Za-z ]", " ");
		return line.toCharArray();
	}
	static public void destroy(char[] text){
		text = null;
		System.gc();
	}
	
	static public char[] generatePatron(boolean o, char[] text, int l){
		char [] patron = new char[l];
		if(o){
			for(int i=0; i<l; i++){
				patron[i] = (char) ThreadLocalRandom.current().nextInt(0,2);
			}
		}
		else{
			int i = ThreadLocalRandom.current().nextInt(0, text.length-l+1);
			patron = Arrays.copyOfRange(text, i, i+l);
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
		Option binary = new Option("b", "Binary input file");
		binary.setArgs(0);
		Option rDNA = new Option("rd", "Real DNA input file");
		rDNA.setArgs(0);
		Option fDNA = new Option("fd", "Fake DNA input file");
		fDNA.setArgs(0);
		Option rText = new Option("pt", "Real text input file");
		rText.setArgs(0);
		Option fText = new Option("ft", "Fake text input file");
		fText.setArgs(0);

		Options options = new Options();
		options.addOption(iter);
		options.addOption(power);
		options.addOption(power2);
		options.addOption(binary);
		options.addOption(rDNA);
		options.addOption(fDNA);
		options.addOption(rText);
		options.addOption(fText);
		
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
		SummaryStatistics BFsum;
		SummaryStatistics BFtime;
		SummaryStatistics KMPsum;
		SummaryStatistics KMPtime;
		SummaryStatistics BMHsum;
		SummaryStatistics BMHtime;
		
		
		// test binary
		if (cmd.hasOption("b")) {
			binarytext = init("binary.txt");
			printer.println("Binary Text");
			GenericTextSearch brute = new BruteForceSearch(binarytext);
			GenericTextSearch kmp = new KnuthMorrisPrattSearch(binarytext);
			GenericTextSearch bmh = new BoyerMooreHorspoolSearch(binarytext);
			printer.println("Binary:");
			for(int i=l; i<=L; i++){
				System.err.println("2^"+i);
				printer.println("2^"+i);
				BFsum = new SummaryStatistics();
				BFtime = new SummaryStatistics();
				KMPsum = new SummaryStatistics();
				KMPtime = new SummaryStatistics();
				BMHsum = new SummaryStatistics();
				BMHtime = new SummaryStatistics();
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
			destroy(binarytext);
			printer.println();
		}
		// test real DNA
		if (cmd.hasOption("rd")) {
			realDNA = init("realDNA.txt");
			printer.println("Real DNA Text");
			GenericTextSearch brute = new BruteForceSearch(realDNA);
			GenericTextSearch kmp = new KnuthMorrisPrattSearch(realDNA);
			GenericTextSearch bmh = new BoyerMooreHorspoolSearch(realDNA);
			for(int i=l; i<=L; i++){
				System.err.println("2^"+i);
				printer.println("2^"+i);
				BFsum = new SummaryStatistics();
				BFtime = new SummaryStatistics();
				KMPsum = new SummaryStatistics();
				KMPtime = new SummaryStatistics();
				BMHsum = new SummaryStatistics();
				BMHtime = new SummaryStatistics();
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(extracted, 
							realDNA, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BFsum.addValue((double)(brute.search(patron)));
					BFtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados realDNA BF");
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
					char [] patron = generatePatron(extracted, 
							realDNA, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					KMPsum.addValue((double)(kmp.search(patron)));
					KMPtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados realDNA KMP");
						System.err.println("Promedio tiempo: "+(KMPtime.getMean()/1000)+" seg.");
						System.err.println("DEstandar tiempo: "+(KMPtime.getStandardDeviation()/1000)+" seg.");
						System.err.println("Error tiempo: "+((2*KMPtime.getStandardDeviation())/
								Math.sqrt(iterations)*1000)+" seg.");
						System.err.println("Promedio comps: "+(KMPsum.getMean())+" comps.");
						System.err.println("DEstandar comps: "+(KMPsum.getStandardDeviation())+" comps.");
						System.err.println("Error comps: "+((2*KMPsum.getStandardDeviation())/
								Math.sqrt(iterations))+" comps.");
					}
					if(acceptableError(BFsum, iterations) 
							&& acceptableError(BFtime, iterations) || iterations >= max_it){
						printer.println("KMP Iterations: "+iterations);
						printer.println("KMPtime:\t"+KMPtime.getMean()+"\t"+KMPtime.getVariance()+"\t"+KMPtime.getStandardDeviation());
						printer.println("KMPsum:\t"+KMPsum.getMean()+"\t"+KMPsum.getVariance()+"\t"+KMPsum.getStandardDeviation());
						break;
					}
				}
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(extracted, 
							realDNA, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BMHsum.addValue((double)(bmh.search(patron)));
					BMHtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados realDNA BMH");
						System.err.println("Promedio tiempo: "+(BMHtime.getMean()/1000)+" seg.");
						System.err.println("DEstandar tiempo: "+(BMHtime.getStandardDeviation()/1000)+" seg.");
						System.err.println("Error tiempo: "+((2*BMHtime.getStandardDeviation())/
								Math.sqrt(iterations)*1000)+" seg.");
						System.err.println("Promedio comps: "+(BMHsum.getMean())+" comps.");
						System.err.println("DEstandar comps: "+(BMHsum.getStandardDeviation())+" comps.");
						System.err.println("Error comps: "+((2*BMHsum.getStandardDeviation())/
								Math.sqrt(iterations))+" comps.");
					}
					if(acceptableError(BFsum, iterations) 
							&& acceptableError(BFtime, iterations) || iterations >= max_it){
						printer.println("BMH Iterations: "+iterations);
						printer.println("BMHtime:\t"+BMHtime.getMean()+"\t"+BMHtime.getVariance()+"\t"+BMHtime.getStandardDeviation());
						printer.println("BMHsum:\t"+BMHsum.getMean()+"\t"+BMHsum.getVariance()+"\t"+BMHsum.getStandardDeviation());
						break;
					}
				}
				
			}
			destroy(realDNA);
		}
		
		// test fake DNA
		if (cmd.hasOption("fd")) {
			fakeDNA = init("fakeDNA.txt");
			printer.println("Fake DNA Text");
			GenericTextSearch brute = new BruteForceSearch(fakeDNA);
			GenericTextSearch kmp = new KnuthMorrisPrattSearch(fakeDNA);
			GenericTextSearch bmh = new BoyerMooreHorspoolSearch(fakeDNA);
			for(int i=l; i<=L; i++){
				System.err.println("2^"+i);
				printer.println("2^"+i);
				BFsum = new SummaryStatistics();
				BFtime = new SummaryStatistics();
				KMPsum = new SummaryStatistics();
				KMPtime = new SummaryStatistics();
				BMHsum = new SummaryStatistics();
				BMHtime = new SummaryStatistics();
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(extracted, 
							fakeDNA, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BFsum.addValue((double)(brute.search(patron)));
					BFtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados fakeDNA BF");
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
					char [] patron = generatePatron(extracted, 
							fakeDNA, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					KMPsum.addValue((double)(kmp.search(patron)));
					KMPtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados fakeDNA KMP");
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
						printer.println("KMPsum:\t"+KMPsum.getMean()+"\t"+KMPsum.getVariance()+"\t"+KMPsum.getStandardDeviation());
						break;
					}
				}
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(extracted, 
							fakeDNA, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BMHsum.addValue((double)(bmh.search(patron)));
					BMHtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados fakeDNA BMH");
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
						printer.println("BMHsum:\t"+BMHsum.getMean()+"\t"+BMHsum.getVariance()+"\t"+BMHsum.getStandardDeviation());
						break;
					}
				}
				
			}
			destroy(fakeDNA);
		}
		// test plain text
		if (cmd.hasOption("pt")) {
			plaintext = init("plainText.txt");
			printer.println("Plain Text");			
			GenericTextSearch brute = new BruteForceSearch(plaintext);
			GenericTextSearch kmp = new KnuthMorrisPrattSearch(plaintext);
			GenericTextSearch bmh = new BoyerMooreHorspoolSearch(plaintext);
			for(int i=l; i<=L; i++){
				System.err.println("2^"+i);
				printer.println("2^"+i);
				BFsum = new SummaryStatistics();
				BFtime = new SummaryStatistics();
				KMPsum = new SummaryStatistics();
				KMPtime = new SummaryStatistics();
				BMHsum = new SummaryStatistics();
				BMHtime = new SummaryStatistics();
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(extracted, 
							plaintext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BFsum.addValue((double)(brute.search(patron)));
					BFtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados plaintext BF");
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
					char [] patron = generatePatron(extracted, 
							plaintext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					KMPsum.addValue((double)(kmp.search(patron)));
					KMPtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados plaintext KMP");
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
						printer.println("KMPsum:\t"+KMPsum.getMean()+"\t"+KMPsum.getVariance()+"\t"+KMPsum.getStandardDeviation());
						break;
					}
				}
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(extracted, 
							plaintext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BMHsum.addValue((double)(bmh.search(patron)));
					BMHtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados realText BMH");
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
						printer.println("BMHsum:\t"+BMHsum.getMean()+"\t"+BMHsum.getVariance()+"\t"+BMHsum.getStandardDeviation());
						break;
					}
				}
				
			}
			destroy(plaintext);
		}
		
		// test fake Text
		if (cmd.hasOption("ft")) {
			faketext = init("fakeText.txt");
			printer.println("Fake Text");
			GenericTextSearch brute = new BruteForceSearch(faketext);
			GenericTextSearch kmp = new KnuthMorrisPrattSearch(faketext);
			GenericTextSearch bmh = new BoyerMooreHorspoolSearch(faketext);
			for(int i=l; i<=L; i++){
				System.err.println("2^"+i);
				printer.println("2^"+i);
				BFsum = new SummaryStatistics();
				BFtime = new SummaryStatistics();
				KMPsum = new SummaryStatistics();
				KMPtime = new SummaryStatistics();
				BMHsum = new SummaryStatistics();
				BMHtime = new SummaryStatistics();
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(extracted, 
							faketext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BFsum.addValue((double)(brute.search(patron)));
					BFtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados faketext BF");
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
					char [] patron = generatePatron(extracted, 
							faketext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					KMPsum.addValue((double)(kmp.search(patron)));
					KMPtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados faketext KMP");
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
						printer.println("KMPsum:\t"+KMPsum.getMean()+"\t"+KMPsum.getVariance()+"\t"+KMPsum.getStandardDeviation());
						break;
					}
				}
				for(int iterations=1; true; iterations++){
					char [] patron = generatePatron(extracted, 
							faketext, (int)Math.pow(2, i));
					t = System.currentTimeMillis();
					BMHsum.addValue((double)(bmh.search(patron)));
					BMHtime.addValue((double)(System.currentTimeMillis() - t));
					if(iterations%10000 == 0){
						System.out.println(""+iterations);
						System.err.println("Resultados faketext BMH");
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
						printer.println("BMHsum:\t"+BMHsum.getMean()+"\t"+BMHsum.getVariance()+"\t"+BMHsum.getStandardDeviation());
						break;
					}
				}
			}
			destroy(faketext);
			printer.close();
		}
	}
}
