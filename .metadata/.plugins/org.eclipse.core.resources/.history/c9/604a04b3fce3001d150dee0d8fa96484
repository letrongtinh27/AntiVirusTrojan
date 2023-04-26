package virusanalyzer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Stack;

public class VirusAnalyzer {
	public static ArrayList<String> virusDefinitions = new ArrayList<>();
	public static ArrayList<String> virusNames = new ArrayList<>();
	public static ArrayList<String> virusTypes = new ArrayList<>();
	public static VirusHandler virusHandler = new VirusHandler();
	public static boolean check = virusHandler.readVirusDefinition();
	private static int scanCount = 1;
	

	public static ArrayList<Object[]> scanFolder(File folder) {
        ArrayList<Object[]> listVirus = new ArrayList<>();
        Stack<File> stack = new Stack<>();
        if(folder != null) {
        stack.push(folder);
        while (!stack.isEmpty()) {
            File currentFolder = stack.pop();
            if(currentFolder != null) {
            File[] files = currentFolder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    Object[] isVirus = checkVirus(file);
                    if (isVirus != null) {
                        listVirus.add(isVirus);
                        
                    }
                } else if (file.isDirectory()) {
                    stack.push(file);
                	}
            	}
            }
        }
        }
        writeVirusToFile(listVirus);
        return listVirus;
    }

	public static Object[] checkVirus(File file) {
		if(file != null) {
			AnalyzingLogic logic = new AnalyzingLogic();
	    	try {   
		         String fileChecksum = logic.md5Generator(file.toString()); 
		         if(check) {
		        	 int index = logic.analyze(fileChecksum, virusDefinitions);
		        	 if(index != -1) {
		        		return new Object[]{file.getAbsolutePath(), virusTypes.get(index).toString(), virusNames.get(index).toString()}; 
		        	 }
		            }
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public static void writeVirusToFile(ArrayList<Object[]> listVirus) {
		 String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File outFile = new File("Virus/virus_" + dateStr + "_scan" + scanCount + ".txt");
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(outFile))){
			if(listVirus != null) {
				for (Object[] objects : listVirus) {
					String outputString = String.join(",", Arrays.stream(objects)
	                        .map(Object::toString)
	                        .toArray(CharSequence[]::new)) + "\n";
					bw.write(outputString);
				}
				bw.close();
			}
		} catch (Exception e) {}
		scanCount++;
	}
	
	public static ArrayList<Object[]> readVirusFromFile(File file) {
		ArrayList<Object[]> listVirus = new ArrayList<>();
		if(file.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))){
				String line;
				while((line=br.readLine()) != null) {
					String[] values = line.split(",");
					Object[] virus = new Object[values.length];
					for(int i = 0; i < values.length; i++) {
						virus[i] = values[i];
					}
					listVirus.add(virus);
				}
			} catch (Exception e) {}
		}
		return listVirus;
	}
}
