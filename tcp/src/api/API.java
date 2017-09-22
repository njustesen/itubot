package api;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import api.ActionSelection;

public class API {
	
	private MultiLayerNetwork model;
	
	public static void main(String[] args) throws IOException{
		API api = new API("model.zip");
		
		int port = Integer.parseInt(args[0]);
		
		TcpServer server = new TcpServer(port) {
			public String handleRequest(String request) {
				String[] arr = request.split("\\[")[1].split("\\]")[0].split(",");
				double[] state = new double[arr.length];
				for (int i = 0; i < arr.length; i++){
					state[i] = Double.parseDouble(arr[i].trim());
				}
				return api.getBuild(state, ActionSelection.PROBALISTIC);
			};
		};
		
	}
	
	public API(String filename) throws IOException {
		super();
		File file = new File(filename);
		System.out.println("Model path=" + file.getAbsolutePath());
		this.model = ModelSerializer.restoreMultiLayerNetwork(file.getAbsolutePath());
	}

	public String getBuild(double[] stateArray, ActionSelection selection){
		
		INDArray in = Nd4j.create(stateArray);
		INDArray out = model.output(in);
		
		int bestIdx = -1;
		if (selection == ActionSelection.GREEDY){
			double bestPred = Integer.MIN_VALUE;
			for (int i = 0; i < out.length(); i++){
				if (out.getDouble(i) > bestPred){
					bestPred = out.getDouble(i);
					bestIdx = i;					
				}
			}
		} else if (selection == ActionSelection.PROBALISTIC){
			double index = new Random().nextDouble();
	        double sum = 0;
	        int i=0;
	        while(sum < index) {
	             sum = sum + out.getDouble(i++);
	        }
	        bestIdx = Math.max(0,i-1);
		}
		
		String buildName = ""+bestIdx;
		
		return buildName;
	}
	
}
