package dl4j.preprocessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import bwapi.UnitRepository;

public class BuildDataSetCreator {
	
	public static void main(String[] args) throws IOException{
		create(0.80);
	}
	
	public static void create(double split) throws IOException{
		List<String> samples = new ArrayList<String>();
		File folder = new File("data/games/");
		System.out.println(folder.listFiles().length + " files.");
		int i = 0;
		for (final File file : folder.listFiles()) {
			System.out.println("Reading file " + i + " " + file.getName());
			BufferedReader br = new BufferedReader(new FileReader(file));
			for (String line = br.readLine(); line != null; line = br.readLine()) {
				String buildName = line.split(":")[0];
				String state = line.split(":")[1];
				if (state.charAt(state.length()-1) == ','){
					state = state.substring(0,state.length()-2);
				}
				if (UnitRepository.protossIdxByName.containsKey(buildName)){
					int idx = UnitRepository.protossIdxByName.get(buildName);
					String sample = idx + "," + state;
					samples.add(sample);
				}
			}
			br.close();
			i++;
	    }

		System.out.println("Saving data set of " + samples.size() + " samples.");
		PrintWriter trainWriter = new PrintWriter("data/samples/train.csv", "UTF-8");
		PrintWriter testWriter = new PrintWriter("data/samples/test.csv", "UTF-8");

		for(int s = 0; s < samples.size(); s++){
			if (s % 1000 == 0){
				System.out.println(s);
			}
			if (s < samples.size()*split){
				trainWriter.println(samples.get(s));
			} else {
				testWriter.println(samples.get(s));
			}
		}
		
		trainWriter.close();
	    testWriter.close();
		
	}

}
