package virusanalyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class VirusAnalyzer {
	public static ArrayList<String> virusDefinitions = new ArrayList<>();
	public static ArrayList<String> virusNames = new ArrayList<>();
	public static ArrayList<String> virusTypes = new ArrayList<>();
	public static VirusHandler virusHandler = new VirusHandler();
	public static boolean check = virusHandler.readVirusDefinition();
	private static int scanCount = 1;

	public static ArrayList<Object[]> infectedFiles = new ArrayList<>(); // 20130340
	public static File fileSession; // 20130340

	public static ArrayList<Object[]> scanFolder(File folder) {
		ArrayList<Object[]> listVirus = new ArrayList<>();
		Stack<File> stack = new Stack<>();
		stack.push(folder);
		while (!stack.isEmpty()) {
			File currentFolder = stack.pop();
			if (currentFolder.isDirectory()) {
				File[] files = currentFolder.listFiles();
				if (files != null) {
					for (File file : files) {
						if (file.isDirectory()) {
							stack.push(file);
						} else {
							Object[] isVirus = checkVirus(file);
							if (isVirus != null) {
								listVirus.add(isVirus);
							}
						}
					}
				}
			}
		}
		if (listVirus.size() > 0) {
			writeVirusToFile(listVirus);
			infectedFiles = listVirus;
		}
		return listVirus;
	}

	public static Object[] checkVirus(File file) {
		if (file != null) {
			AnalyzingLogic logic = new AnalyzingLogic();
			String fileChecksum = "";
			try {
				fileChecksum = logic.md5Generator(file.toString());
				if (check) {
					int index = logic.analyze(fileChecksum, virusDefinitions);
					if (index != -1) {
						return new Object[] { file.getAbsolutePath(), virusTypes.get(index).toString(),
								virusNames.get(index).toString() };
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 20130340
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			boolean deleted = file.delete();
			if (deleted) {
				System.out.println(infectedFiles);
				for (Object[] o : infectedFiles) {
					for (Object s : o) {
						if (s.equals(filePath)) {
							infectedFiles.remove(o);
							System.out.println(updateFile());
							return updateFile();
						}
					}
				}
			}
		}
		return false;
	}

	// 20130340
	public static boolean updateFile() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileSession))) {
			if (infectedFiles != null) {
				for (Object[] objects : infectedFiles) {
					String outputString = String.join(",",
							Arrays.stream(objects).map(Object::toString).toArray(CharSequence[]::new)) + "\n";
					bw.write(outputString);
				}
				bw.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	// 20130340
	public static ArrayList<Object[]> findFile(String keyWords) {
		ArrayList<Object[]> rs = new ArrayList<>();
		for (Object[] o : infectedFiles) {
			ArrayList<String> array = new ArrayList<>();
			for (Object obj : o) {
				array.add((String) obj);
			}
			for (String s : array) {
				if (s.contains(keyWords)) {
					rs.add(o);
				}
			}
		}
		return rs;
	}

	public static void writeVirusToFile(ArrayList<Object[]> listVirus) {
		String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File outFile = new File("Virus/virus_" + dateStr + "_scan" + scanCount + ".txt");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))) {
			if (listVirus != null) {
				for (Object[] objects : listVirus) {
					String outputString = String.join(",",
							Arrays.stream(objects).map(Object::toString).toArray(CharSequence[]::new)) + "\n";
					bw.write(outputString);
				}
				bw.close();
			}
		} catch (Exception e) {
		}
		scanCount++;
		fileSession = outFile;
	}

	public static ArrayList<Object[]> readVirusFromFile(File file) {
		ArrayList<Object[]> listVirus = new ArrayList<>();
		if (file != null && file.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",");
					Object[] virus = new Object[values.length];
					for (int i = 0; i < values.length; i++) {
						virus[i] = values[i];
					}
					listVirus.add(virus);
				}
			} catch (Exception e) {
			}
		}
		infectedFiles = listVirus; // 20130340
		fileSession = file; // 20130340
		return listVirus;
	}

}
