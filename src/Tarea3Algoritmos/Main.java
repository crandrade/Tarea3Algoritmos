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
		@SuppressWarnings("resource")
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
		int max = Integer.MAX_VALUE;
		if (o) { // o == true
			int k = (int) Math.pow(2, l);
			for (int j = 0; j < 10000; j++) {
				int i = ThreadLocalRandom.current().nextInt(0, k);
				patron[j] = chain[i];
			}
		} else { // o == false
			for (int j = 0; j < 10000; j++) {
				patron[j] = - ThreadLocalRandom.current().nextInt(0, max);
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

		int l = 10; // minimal measure
		int L = 25; // maximal measure
		int max_it = 100;
		if (cmd.hasOption("iterations")) {
			int nn = Integer.parseInt(cmd.getOptionValue(iter.getOpt()));
			if (nn >= 1 && nn <= 10000)
				max_it = nn;
		}
		if (cmd.hasOption("i")) {
			int nn = Integer.parseInt(cmd.getOptionValue(power.getOpt()));
			if (nn >= 10 && nn <= 25)
				l = nn;
		}
		if (cmd.hasOption("I")) {
			int nn = Integer.parseInt(cmd.getOptionValue(power2.getOpt()));
			if (nn >= l && nn <= 25)
				L = nn;
		}
		boolean random = false;
		boolean extracted = true;
		/*
		 * Medir Ocupación: Al entrar 2^20, 2^21, 2^22, 2^23, 2^24, 2^25 Al
		 * salir 2^24, 2^23, 2^22, 2^21, 2^20
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
		 * Medir IO for i in [20, 25] Luego de insertar 2^i Tras realizar 10000
		 * búsquedas "exitosas" Tras realizar 10000 búsquedas "infructuosas" for
		 * i in [25, 21] Luego de borrar entre i e i-1 Luego de borrar todo
		 */
		SummaryStatistics ABB_Time_insert[] = new SummaryStatistics[6];
		SummaryStatistics ABB_Time_successfulSearch[] = new SummaryStatistics[6];
		SummaryStatistics ABB_Time_unfavorableSearch[] = new SummaryStatistics[6];
		SummaryStatistics ABB_Time_deleting[] = new SummaryStatistics[5];
		SummaryStatistics ABB_Time_erased = new SummaryStatistics(); // resetear
		SummaryStatistics AVL_Time_insert[] = new SummaryStatistics[6];
		SummaryStatistics AVL_Time_successfulSearch[] = new SummaryStatistics[6];
		SummaryStatistics AVL_Time_unfavorableSearch[] = new SummaryStatistics[6];
		SummaryStatistics AVL_Time_deleting[] = new SummaryStatistics[5];
		SummaryStatistics AVL_Time_erased = new SummaryStatistics(); // resetear
		SummaryStatistics SPL_Time_insert[] = new SummaryStatistics[6];
		SummaryStatistics SPL_Time_successfulSearch[] = new SummaryStatistics[6];
		SummaryStatistics SPL_Time_unfavorableSearch[] = new SummaryStatistics[6];
		SummaryStatistics SPL_Time_deleting[] = new SummaryStatistics[5];
		SummaryStatistics SPL_Time_erased = new SummaryStatistics(); // resetear
		SummaryStatistics VEB_Time_insert[] = new SummaryStatistics[6];
		SummaryStatistics VEB_Time_successfulSearch[] = new SummaryStatistics[6];
		SummaryStatistics VEB_Time_unfavorableSearch[] = new SummaryStatistics[6];
		SummaryStatistics VEB_Time_deleting[] = new SummaryStatistics[5];
		SummaryStatistics VEB_Time_erased = new SummaryStatistics(); // resetear
		/* SOMEHOW */

		// test real DNA
		printer.println("Iteracion\tNombre\tPromedio\tDesviacion\tError(2sigma/raiz(n)*promedio)");
		int bhelper_low = 0, bhelper_high = 0, exthelper_low = 0, exthelper_high = 0, lin1helper_low = 0, lin1helper_high = 0, lin2helper_low = 0, lin2helper_high = 0;
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
				System.err.print("2^" + i);
				max = (int) Math.pow(2, i);
				for (int j = actual; j < max; j++) {
					abb.insert(keys_and_values[j][0],keys_and_values[j][1]);
					avl.insert(keys_and_values[j][0],keys_and_values[j][1]);
					spl.insert(keys_and_values[j][0],keys_and_values[j][1]);
					veb.insert(keys_and_values[j][0],keys_and_values[j][1]);
				}
				System.err.print(".");
				// measure Occupation
				ABB_OccIn[i - l].addValue(abb.size());
				AVL_OccIn[i - l].addValue(avl.size());
				SPL_OccIn[i - l].addValue(spl.size());
				VEB_OccIn[i - l].addValue(veb.size());
				// measure IO
				bhelper_high = abb.getIOs();
				exthelper_high = avl.getIOs();
				lin1helper_high = spl.getIOs();
				lin2helper_high = veb.getIOs();
				ABB_Time_insert[i - l].addValue(bhelper_high + bhelper_low);
				AVL_Time_insert[i - l].addValue(exthelper_high + exthelper_low);
				SPL_Time_insert[i - l].addValue(lin1helper_high + lin1helper_low);
				VEB_Time_insert[i - l].addValue(lin2helper_high + lin2helper_low);
				abb.resetIOs();
				avl.resetIOs();
				spl.resetIOs();
				veb.resetIOs();
				System.err.print(",");
				// end measure IO
				actual = max;
				// find successful
				int[] patron = generateIntArray(extracted, keys_and_values, l);
				for (int iterations = 0; iterations < 10000; iterations++) {
					abb.find(patron[iterations]);
					avl.find(patron[iterations]);
					spl.find(patron[iterations]);
					veb.find(patron[iterations]);
				}
				System.err.print(".");
				// measure IOs
				ABB_Time_successfulSearch[i - l].addValue(abb.getIOs());
				AVL_Time_successfulSearch[i - l].addValue(avl.getIOs());
				SPL_Time_successfulSearch[i - l].addValue(spl.getIOs());
				VEB_Time_successfulSearch[i - l].addValue(veb.getIOs());
				abb.resetIOs();
				avl.resetIOs();
				spl.resetIOs();
				veb.resetIOs();
				System.err.print("m");
				// end measure IOs
				// find unfavorable
				patron = generateIntArray(random, keys_and_values, l);
				for (int iterations = 0; iterations < 10000; iterations++) {
					abb.find(patron[iterations]);
					avl.find(patron[iterations]);
					spl.find(patron[iterations]);
					veb.find(patron[iterations]);
				}
				System.err.print(".");
				// measure IOs
				ABB_Time_unfavorableSearch[i - l].addValue(abb.getIOs());
				AVL_Time_unfavorableSearch[i - l].addValue(avl.getIOs());
				SPL_Time_unfavorableSearch[i - l].addValue(spl.getIOs());
				VEB_Time_unfavorableSearch[i - l].addValue(veb.getIOs());
				abb.resetIOs();
				avl.resetIOs();
				spl.resetIOs();
				veb.resetIOs();
				System.err.print(",");
				// migrate helpers => low accumulates all inserting IOs
				bhelper_low += bhelper_high;
				exthelper_low += exthelper_high;
				lin1helper_low += lin1helper_high;
				lin2helper_low += lin2helper_high;
			}
			System.err.print("\nErasing >>");
			actual = (int) Math.pow(2, L);
			int min = 0;
			bhelper_low = bhelper_high = 0;
			exthelper_low = exthelper_high = 0;
			lin1helper_low = lin1helper_high = 0;
			lin2helper_low = lin2helper_high = 0;

			// randomize erasing
			List<int[]> a = Arrays.asList(keys_and_values);
			Collections.shuffle(a);
			keys_and_values = (int[][]) a.toArray();

			System.gc(); // clean old keys_and_values
			for (int i = L; i > l; i--) {
				System.err.print("2^" + i + "->2^" + (i - 1) + " >> ");
				min = (int) Math.pow(2, i - 1);
				for (int j = actual - 1; j >= min; j--) {
					abb.delete(keys_and_values[i][0]);
					avl.delete(keys_and_values[i][0]);
					spl.delete(keys_and_values[i][0]);
					veb.delete(keys_and_values[i][0]);
				}
				System.err.print(".");
				actual = min;
				// measure Occupation
				ABB_OccOut[i - l - 1].addValue(abb.size());
				AVL_OccOut[i - l - 1].addValue(avl.size());
				SPL_OccOut[i - l - 1].addValue(spl.size());
				VEB_OccOut[i - l - 1].addValue(veb.size());
				// measure IOs
				ABB_Time_deleting[i - l - 1].addValue(abb.getIOs());
				AVL_Time_deleting[i - l - 1].addValue(avl.getIOs());
				SPL_Time_deleting[i - l - 1].addValue(spl.getIOs());
				VEB_Time_deleting[i - l - 1].addValue(veb.getIOs());
				System.err.print(",");
			}
			for (int i = actual - 1; i >= 0; i--) {
				abb.delete(keys_and_values[i]);
				avl.delete(keys_and_values[i]);
				spl.delete(keys_and_values[i]);
				veb.delete(keys_and_values[i]);
			}
			// measure
			ABB_Time_erased.addValue(abb.getIOs());
			AVL_Time_erased.addValue(avl.getIOs());
			SPL_Time_erased.addValue(spl.getIOs());
			VEB_Time_erased.addValue(veb.getIOs());
			// end
			destroy(keys_and_values);
			/*
			 * calculate error if reasonable, quit
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
			// TODO
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
			printer.println("IO in");
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
			printer.println("IO out");
			// TODO
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
			printer.println("IO successful search");
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
			printer.println("IO unfavorable search");
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
		/* print to file */
	}
}
