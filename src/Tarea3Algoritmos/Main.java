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
	static protected int [][] keys_and_values;

	@SuppressWarnings("resource")
	static public int[][] init(String filename, int k) throws FileNotFoundException{
		int i = (int)Math.pow(2, k);
		String line = "";
		String [] phase = new String[2];
		int [][] result = new int[i][2];
		File file = new File(filename);
		Scanner scanner = new Scanner(file);
		int j=0;
		while (scanner.hasNextLine() && j<i) {
		       line= scanner.nextLine();
		       if(j%1000000 == 0){
		    	   System.err.print(",");
		       }
		       phase = line.split("\t");
		       result[j][0] = Integer.parseInt(phase[0]);
		       result[j++][1] = Integer.parseInt(phase[1]);
		}
		return result;
	}

	static public void destroy(int [][] text) {
		text = null;
		System.gc();
	}

	static public int[] generateIntArray(boolean o, int[][] chain, int l) {
		int[] patron = new int[10000];
		//int max = Integer.MAX_VALUE;
		int k = (int) Math.pow(2, l);
		if(o){
			for (int j = 0; j < 10000; j++) {
				int i = ThreadLocalRandom.current().nextInt(0, k);
				patron[j] = chain[i][0];
			}
		}
		else{
			for (int j = 0; j < 10000; j++) {
				patron[j] = ThreadLocalRandom.current().nextInt(0, 12000000);
			}
		}
		return patron;
	}

	static public boolean acceptableError(SummaryStatistics summary,
			int iterations) {
		if (iterations > 1) {
			return (((summary.getStandardDeviation() / summary.getMean()) <= 0.05) && (((2 * summary
					.getStandardDeviation()) / Math.sqrt(iterations)) >= 0.95));
		} else
			return false;
	}

	public static void main(String[] args) throws IOException {
		Option iter = new Option("iterations", "Number of iterations");
		iter.setArgs(1);
		iter.setRequired(true);
		Option power = new Option("i", "Log (Min lenght of file (lines))");
		power.setArgs(1);
		power.setRequired(true);
		Option power2 = new Option("I", "Log (Max lenght of file (lines)");
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
			System.err.println("***ERROR: " + e.getClass() + ": "
					+ e.getMessage());
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("parameters:", options);
			return;
		}

		String dir = new SimpleDateFormat("yyyyMMdd HH:mm:ss")
				.format(new Date()) + " output.txt";
		System.err.println("Opening file at  " + dir);
		File fDir = new File(dir);
		PrintWriter printer = new PrintWriter(new FileWriter(fDir, true));

		int l = 5; // minimal measure
		int L = 15; // maximal measure
		int max_it = 100;
		if (cmd.hasOption("iterations")) {
			int nn = Integer.parseInt(cmd.getOptionValue(iter.getOpt()));
			if (nn >= 1 && nn <= 10000)
				max_it = nn;
		}
		if (cmd.hasOption("i")) {
			int nn = Integer.parseInt(cmd.getOptionValue(power.getOpt()));
			if (nn >= 10 && nn <= 15)
				l = nn;
		}
		if (cmd.hasOption("I")) {
			int nn = Integer.parseInt(cmd.getOptionValue(power2.getOpt()));
			if (nn >= l && nn <= 15)
				L = nn;
		}
		boolean random = false;
		boolean extracted = true;
		/*
		 * Medir Ocupación: 
		 * Al entrar [2^l, 2^L] 
		 * Al salir 2^(L-1) 2^l
		 */

		SummaryStatistics ABB_OccIn[] = new SummaryStatistics[6];
		SummaryStatistics ABB_OccOut[] = new SummaryStatistics[5];
		SummaryStatistics AVL_OccIn[] = new SummaryStatistics[6];
		SummaryStatistics AVL_OccOut[] = new SummaryStatistics[5];
		SummaryStatistics SPL_OccIn[] = new SummaryStatistics[6];
		SummaryStatistics SPL_OccOut[] = new SummaryStatistics[5];
		SummaryStatistics VEB_OccIn[] = new SummaryStatistics[6];
		SummaryStatistics VEB_OccOut[] = new SummaryStatistics[5];

		/*
		 * Medir tiempo for i in [l, L] Luego de insertar 2^i 
		 * Tras realizar 10000 búsquedas "exitosas" 
		 * Tras realizar 10000 búsquedas "infructuosas" 
		 * for i in [L, l+1] 
		 * Luego de borrar entre i e i-1 
		 * Luego de borrar todo
		 */
		SummaryStatistics ABB_Time_insert[] = new SummaryStatistics[6];
		SummaryStatistics ABB_Time_successfulSearch[] = new SummaryStatistics[6];
		SummaryStatistics ABB_Time_unfavorableSearch[] = new SummaryStatistics[6];
		SummaryStatistics ABB_Time_deleting[] = new SummaryStatistics[5];
		SummaryStatistics ABB_Time_erased = new SummaryStatistics(); // reset
		SummaryStatistics AVL_Time_insert[] = new SummaryStatistics[6];
		SummaryStatistics AVL_Time_successfulSearch[] = new SummaryStatistics[6];
		SummaryStatistics AVL_Time_unfavorableSearch[] = new SummaryStatistics[6];
		SummaryStatistics AVL_Time_deleting[] = new SummaryStatistics[5];
		SummaryStatistics AVL_Time_erased = new SummaryStatistics(); // reset
		SummaryStatistics SPL_Time_insert[] = new SummaryStatistics[6];
		SummaryStatistics SPL_Time_successfulSearch[] = new SummaryStatistics[6];
		SummaryStatistics SPL_Time_unfavorableSearch[] = new SummaryStatistics[6];
		SummaryStatistics SPL_Time_deleting[] = new SummaryStatistics[5];
		SummaryStatistics SPL_Time_erased = new SummaryStatistics(); // reset
		SummaryStatistics VEB_Time_insert[] = new SummaryStatistics[6];
		SummaryStatistics VEB_Time_successfulSearch[] = new SummaryStatistics[6];
		SummaryStatistics VEB_Time_unfavorableSearch[] = new SummaryStatistics[6];
		SummaryStatistics VEB_Time_deleting[] = new SummaryStatistics[5];
		SummaryStatistics VEB_Time_erased = new SummaryStatistics(); // reset
		/* SOMEHOW */
		long timer_in;
		// test real DNA
		printer.println("Iteracion\tNombre\tPromedio\tDesviacion\tError(2sigma/raiz(n)*promedio)");
		for (int i = 0; i < 6; i++) {
			ABB_OccIn[i] = new SummaryStatistics();
			AVL_OccIn[i] = new SummaryStatistics();
			SPL_OccIn[i] = new SummaryStatistics();
			VEB_OccIn[i] = new SummaryStatistics();
			ABB_Time_insert[i] = new SummaryStatistics();
			ABB_Time_successfulSearch[i] = new SummaryStatistics();
			ABB_Time_unfavorableSearch[i] = new SummaryStatistics();
			AVL_Time_insert[i] = new SummaryStatistics();
			AVL_Time_successfulSearch[i] = new SummaryStatistics();
			AVL_Time_unfavorableSearch[i] = new SummaryStatistics();
			SPL_Time_insert[i] = new SummaryStatistics();
			SPL_Time_successfulSearch[i] = new SummaryStatistics();
			SPL_Time_unfavorableSearch[i] = new SummaryStatistics();
			VEB_Time_insert[i] = new SummaryStatistics();
			VEB_Time_successfulSearch[i] = new SummaryStatistics();
			VEB_Time_unfavorableSearch[i] = new SummaryStatistics();
		}
		for (int i = 0; i < 5; i++) {
			ABB_OccOut[i] = new SummaryStatistics();
			AVL_OccOut[i] = new SummaryStatistics();
			SPL_OccOut[i] = new SummaryStatistics();
			VEB_OccOut[i] = new SummaryStatistics();
			ABB_Time_deleting[i] = new SummaryStatistics();
			AVL_Time_deleting[i] = new SummaryStatistics();
			SPL_Time_deleting[i] = new SummaryStatistics();
			VEB_Time_deleting[i] = new SummaryStatistics();
		}
		for (int r = 0; r < max_it; r++) {
			printer.println("Test " + r);
			System.err.print("Setting up Test " + r);
			keys_and_values = init("tarea3info" + r + ".txt", L);
			GenericTree abb = new ABB();
			GenericTree avl = new AVL();
			GenericTree spl = new SplayTree();
			GenericTree veb = new DummyVanEmdeBoas();
			System.err.print(".");
			int actual = 0;
			int max = 0;
			System.err.print("\nFilling >>");
			for (int i = l; i <= L; i++) {
				//-----------------------
				// insertions
				//-----------------------
				System.err.print("2^" + i);
				max = (int) Math.pow(2, i);
				timer_in = System.nanoTime();
				for (int j = actual; j < max; j++) {
					abb.insert(keys_and_values[j][0],keys_and_values[j][1]);
				}
				ABB_Time_insert[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int j = actual; j < max; j++) {
					avl.insert(keys_and_values[j][0],keys_and_values[j][1]);
				}
				AVL_Time_insert[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int j = actual; j < max; j++) {
					spl.insert(keys_and_values[j][0],keys_and_values[j][1]);
				}
				SPL_Time_insert[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int j = actual; j < max; j++) {
					veb.insert(keys_and_values[j][0],keys_and_values[j][1]);
				}
				VEB_Time_insert[i - l].addValue(System.nanoTime() - timer_in);
				System.err.print(".");
				// measure Occupation
				ABB_OccIn[i - l].addValue(abb.size());
				AVL_OccIn[i - l].addValue(avl.size());
				SPL_OccIn[i - l].addValue(spl.size());
				VEB_OccIn[i - l].addValue(veb.size());
				System.err.print(",");
				actual = max;
				//-----------------------
				// find successful
				//-----------------------
				int[] patron = generateIntArray(extracted, keys_and_values, l);
				timer_in = System.nanoTime();
				for (int iterations = 0; iterations < 10000; iterations++) {
					abb.find(patron[iterations]);
				}
				ABB_Time_successfulSearch[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int iterations = 0; iterations < 10000; iterations++) {
					avl.find(patron[iterations]);
				}
				AVL_Time_successfulSearch[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int iterations = 0; iterations < 10000; iterations++) {
					spl.find(patron[iterations]);
				}
				SPL_Time_successfulSearch[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int iterations = 0; iterations < 10000; iterations++) {
					veb.find(patron[iterations]);
				}
				VEB_Time_successfulSearch[i - l].addValue(System.nanoTime() - timer_in);
				System.err.print(".");
				//-----------------------
				// find unfavorable
				//-----------------------
				patron = generateIntArray(random, keys_and_values, l);
				timer_in = System.nanoTime();
				for (int iterations = 0; iterations < 10000; iterations++) {
					abb.find(patron[iterations]);
				}
				ABB_Time_unfavorableSearch[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int iterations = 0; iterations < 10000; iterations++) {
					avl.find(patron[iterations]);
				}
				AVL_Time_unfavorableSearch[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int iterations = 0; iterations < 10000; iterations++) {
					spl.find(patron[iterations]);
				}
				SPL_Time_unfavorableSearch[i - l].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int iterations = 0; iterations < 10000; iterations++) {
					veb.find(patron[iterations]);
				}
				VEB_Time_unfavorableSearch[i - l].addValue(System.nanoTime() - timer_in);
				System.err.print(".");
				System.err.print(",");
				// migrate helpers => low accumulates all inserting IOs
			}
			System.err.print("\nErasing >>");
			actual = (int) Math.pow(2, L);
			int min = 0;
			// randomize erasing
			List<int[]> a = Arrays.asList(keys_and_values);
			Collections.shuffle(a);
			keys_and_values = (int[][]) a.toArray();

			System.gc(); // clean old keys_and_values
			//-----------------------
			// erasing
			//-----------------------
			for (int i = L; i > l; i--) {
				System.err.print("2^" + i + "->2^" + (i - 1) + " >> ");
				min = (int) Math.pow(2, i - 1);
				timer_in = System.nanoTime();
				for (int j = actual - 1; j >= min; j--) {
					abb.delete(keys_and_values[i][0]);
				}
				ABB_Time_deleting[i - l - 1].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
//				for (int j = actual - 1; j >= min; j--) {
//					avl.delete(keys_and_values[i][0]);
//				}
				AVL_Time_deleting[i - l - 1].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int j = actual - 1; j >= min; j--) {
					spl.delete(keys_and_values[i][0]);
				}
				SPL_Time_deleting[i - l - 1].addValue(System.nanoTime() - timer_in);
				timer_in = System.nanoTime();
				for (int j = actual - 1; j >= min; j--) {
					veb.delete(keys_and_values[i][0]);
				}
				VEB_Time_deleting[i - l - 1].addValue(System.nanoTime() - timer_in);
				System.err.print(".");
				actual = min;
				// measure Occupation
				ABB_OccOut[i - l - 1].addValue(abb.size());
				AVL_OccOut[i - l - 1].addValue(avl.size());
				SPL_OccOut[i - l - 1].addValue(spl.size());
				VEB_OccOut[i - l - 1].addValue(veb.size());
				System.err.print(",");
			}
			//-----------------------
			// final erasing
			//-----------------------
			timer_in = System.nanoTime();
			for (int i = actual - 1; i >= 0; i--) {
				abb.delete(keys_and_values[i][0]);
			}
			ABB_Time_erased.addValue(System.nanoTime() - timer_in);
			timer_in = System.nanoTime();
//			for (int i = actual - 1; i >= 0; i--) {
//				avl.delete(keys_and_values[i][0]);
//			}
			AVL_Time_erased.addValue(System.nanoTime() - timer_in);
			timer_in = System.nanoTime();
			for (int i = actual - 1; i >= 0; i--) {
				spl.delete(keys_and_values[i][0]);
			}
			SPL_Time_erased.addValue(System.nanoTime() - timer_in);
			timer_in = System.nanoTime();
			for (int i = actual - 1; i >= 0; i--) {
				veb.delete(keys_and_values[i][0]);
			}
			VEB_Time_erased.addValue(System.nanoTime() - timer_in);
			destroy(keys_and_values);
			/*
			 * print results in file
			 */
			double R = Math.sqrt(r + 1);
			printer.println("Occupation In");
			for (int k = 0; k < 6; k++) {
				printer.println(r
						+ "\t"
						+ "ABB_OccIn(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ ABB_OccIn[k].getMean()
						+ "\t"
						+ ABB_OccIn[k].getStandardDeviation()
						+ "\t"
						+ (2 * ABB_OccIn[k].getStandardDeviation() / (R * ABB_OccIn[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "AVL_OccIn(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ AVL_OccIn[k].getMean()
						+ "\t"
						+ AVL_OccIn[k].getStandardDeviation()
						+ "\t"
						+ (2 * AVL_OccIn[k].getStandardDeviation() / (R * AVL_OccIn[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "SPL_OccIn(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ SPL_OccIn[k].getMean()
						+ "\t"
						+ SPL_OccIn[k].getStandardDeviation()
						+ "\t"
						+ (2 * SPL_OccIn[k].getStandardDeviation() / (R * SPL_OccIn[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "VEB_OccIn(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ VEB_OccIn[k].getMean()
						+ "\t"
						+ VEB_OccIn[k].getStandardDeviation()
						+ "\t"
						+ (2 * VEB_OccIn[k].getStandardDeviation() / (R * VEB_OccIn[k]
								.getMean())));
			}
			printer.println("Occupation Out");
			for (int k = 4; k >= 0; k--) {
				printer.println(r
						+ "\t"
						+ "ABB_OccOut(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ ABB_OccIn[k].getMean()
						+ "\t"
						+ ABB_OccOut[k].getStandardDeviation()
						+ "\t"
						+ (2 * ABB_OccOut[k].getStandardDeviation() / (R * ABB_OccOut[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "AVL_OccOut(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ AVL_OccOut[k].getMean()
						+ "\t"
						+ AVL_OccOut[k].getStandardDeviation()
						+ "\t"
						+ (2 * AVL_OccOut[k].getStandardDeviation() / (R * AVL_OccOut[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "SPL_OccOut(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ SPL_OccOut[k].getMean()
						+ "\t"
						+ SPL_OccOut[k].getStandardDeviation()
						+ "\t"
						+ (2 * SPL_OccOut[k].getStandardDeviation() / (R * SPL_OccOut[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "VEB_OccOut(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ VEB_OccOut[k].getMean()
						+ "\t"
						+ VEB_OccOut[k].getStandardDeviation()
						+ "\t"
						+ (2 * VEB_OccOut[k].getStandardDeviation() / (R * VEB_OccOut[k]
								.getMean())));
			}
			printer.println("TIME in");
			for (int k = 0; k < 6; k++) {
				printer.println(r
						+ "\t"
						+ "ABB_Time_insert(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ ABB_Time_insert[k].getMean()
						+ "\t"
						+ ABB_Time_insert[k].getStandardDeviation()
						+ "\t"
						+ (2 * ABB_Time_insert[k].getStandardDeviation() / (R * ABB_Time_insert[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "AVL_Time_insert(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ AVL_Time_insert[k].getMean()
						+ "\t"
						+ AVL_Time_insert[k].getStandardDeviation()
						+ "\t"
						+ (2 * AVL_Time_insert[k].getStandardDeviation() / (R * AVL_Time_insert[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "SPL_Time_insert(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ SPL_Time_insert[k].getMean()
						+ "\t"
						+ SPL_Time_insert[k].getStandardDeviation()
						+ "\t"
						+ (2 * SPL_Time_insert[k].getStandardDeviation() / (R * SPL_Time_insert[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "VEB_Time_insert(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ VEB_Time_insert[k].getMean()
						+ "\t"
						+ VEB_Time_insert[k].getStandardDeviation()
						+ "\t"
						+ (2 * VEB_Time_insert[k].getStandardDeviation() / (R * VEB_Time_insert[k]
								.getMean())));
			}
			printer.println("TIME out");
			for (int k = 4; k >= 0; k--) {
				printer.println(r
						+ "\t"
						+ "ABB_Time_deleting(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ ABB_Time_deleting[k].getMean()
						+ "\t"
						+ ABB_Time_deleting[k].getStandardDeviation()
						+ "\t"
						+ (2 * ABB_Time_deleting[k].getStandardDeviation() / (R * ABB_Time_deleting[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "AVL_Time_deleting(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ AVL_Time_deleting[k].getMean()
						+ "\t"
						+ AVL_Time_deleting[k].getStandardDeviation()
						+ "\t"
						+ (2 * AVL_Time_deleting[k].getStandardDeviation() / (R * AVL_Time_deleting[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "SPL_Time_deleting(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ SPL_Time_deleting[k].getMean()
						+ "\t"
						+ SPL_Time_deleting[k].getStandardDeviation()
						+ "\t"
						+ (2 * SPL_Time_deleting[k].getStandardDeviation() / (R * SPL_Time_deleting[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "VEB_Time_deleting(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ VEB_Time_deleting[k].getMean()
						+ "\t"
						+ VEB_Time_deleting[k].getStandardDeviation()
						+ "\t"
						+ (2 * VEB_Time_deleting[k].getStandardDeviation() / (R * VEB_Time_deleting[k]
								.getMean())));
			}
			printer.println(r
					+ "\t"
					+ "ABB_Time_erased"
					+ "\t"
					+ ABB_Time_erased.getMean()
					+ "\t"
					+ ABB_Time_erased.getStandardDeviation()
					+ "\t"
					+ (2 * ABB_Time_erased.getStandardDeviation() / (R * ABB_Time_erased
							.getMean())));
			printer.println(r
					+ "\t"
					+ "AVL_Time_erased"
					+ "\t"
					+ AVL_Time_erased.getMean()
					+ "\t"
					+ AVL_Time_erased.getStandardDeviation()
					+ "\t"
					+ (2 * AVL_Time_erased.getStandardDeviation() / (R * AVL_Time_erased
							.getMean())));
			printer.println(r
					+ "\t"
					+ "SPL_Time_erased"
					+ "\t"
					+ SPL_Time_erased.getMean()
					+ "\t"
					+ SPL_Time_erased.getStandardDeviation()
					+ "\t"
					+ (2 * SPL_Time_erased.getStandardDeviation() / (R * SPL_Time_erased
							.getMean())));
			printer.println(r
					+ "\t"
					+ "VEB_Time_erased"
					+ "\t"
					+ VEB_Time_erased.getMean()
					+ "\t"
					+ VEB_Time_erased.getStandardDeviation()
					+ "\t"
					+ (2 * VEB_Time_erased.getStandardDeviation() / (R * VEB_Time_erased
							.getMean())));
			printer.println("TIME successful search");
			for (int k = 0; k < 6; k++) {
				printer.println(r
						+ "\t"
						+ "ABB_Time_successfulSearch(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ ABB_Time_successfulSearch[k].getMean()
						+ "\t"
						+ ABB_Time_successfulSearch[k].getStandardDeviation()
						+ "\t"
						+ (2 * ABB_Time_successfulSearch[k]
								.getStandardDeviation() / (R * ABB_Time_successfulSearch[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "AVL_Time_successfulSearch(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ AVL_Time_successfulSearch[k].getMean()
						+ "\t"
						+ AVL_Time_successfulSearch[k].getStandardDeviation()
						+ "\t"
						+ (2 * AVL_Time_successfulSearch[k]
								.getStandardDeviation() / (R * AVL_Time_successfulSearch[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "SPL_Time_insert(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ SPL_Time_successfulSearch[k].getMean()
						+ "\t"
						+ SPL_Time_successfulSearch[k]
								.getStandardDeviation()
						+ "\t"
						+ (2 * SPL_Time_successfulSearch[k]
								.getStandardDeviation() / (R * SPL_Time_successfulSearch[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "VEB_Time_insert(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ VEB_Time_successfulSearch[k].getMean()
						+ "\t"
						+ VEB_Time_successfulSearch[k]
								.getStandardDeviation()
						+ "\t"
						+ (2 * VEB_Time_successfulSearch[k]
								.getStandardDeviation() / (R * VEB_Time_successfulSearch[k]
								.getMean())));
			}
			printer.println("TIME unfavorable search");
			for (int k = 0; k < 6; k++) {
				printer.println(r
						+ "\t"
						+ "ABB_Time_unfavorableSearch(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ ABB_Time_unfavorableSearch[k].getMean()
						+ "\t"
						+ ABB_Time_unfavorableSearch[k].getStandardDeviation()
						+ "\t"
						+ (2 * ABB_Time_unfavorableSearch[k]
								.getStandardDeviation() / (R * ABB_Time_unfavorableSearch[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "AVL_Time_unfavorableSearch(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ AVL_Time_unfavorableSearch[k].getMean()
						+ "\t"
						+ AVL_Time_unfavorableSearch[k]
								.getStandardDeviation()
						+ "\t"
						+ (2 * AVL_Time_unfavorableSearch[k]
								.getStandardDeviation() / (R * AVL_Time_unfavorableSearch[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "SPL_Time_unfavorableSearch(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ SPL_Time_unfavorableSearch[k].getMean()
						+ "\t"
						+ SPL_Time_unfavorableSearch[k]
								.getStandardDeviation()
						+ "\t"
						+ (2 * SPL_Time_unfavorableSearch[k]
								.getStandardDeviation() / (R * SPL_Time_unfavorableSearch[k]
								.getMean())));
				printer.println(r
						+ "\t"
						+ "VEB_Time_unfavorableSearch(2^"
						+ (k + l)
						+ ")"
						+ "\t"
						+ VEB_Time_unfavorableSearch[k].getMean()
						+ "\t"
						+ VEB_Time_unfavorableSearch[k]
								.getStandardDeviation()
						+ "\t"
						+ (2 * VEB_Time_unfavorableSearch[k]
								.getStandardDeviation() / (R * VEB_Time_unfavorableSearch[k]
								.getMean())));
			}
		}
		printer.close();
		/* print to file */
	}
}
