package dl4j.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

public class BuildPredictionModel {
	
	private static int rngSeed = 123;

	public List<Integer> hiddenLayers;
	public WeightInit weightInit;
	public Activation activation;
	public OptimizationAlgorithm optimizor;
	public boolean l2Regularization;
	public LossFunction lossFunction;
	public int inputs;
	public int outputs;
	public Activation outputActivation;
	public double learningRate;
	public int batchSize;
	
	public MultiLayerNetwork network;
	
	public BuildPredictionModel(int inputs, int outputs, List<Integer> hiddenLayers, WeightInit weightInit, Activation activation, 
			Activation outputActivation, OptimizationAlgorithm optimizor, boolean l2Regularization, LossFunction lossFunction, 
			double learningRate, int batchSize) {
		super();
		this.inputs = inputs;
		this.outputs = outputs;
		this.hiddenLayers = hiddenLayers;
		this.weightInit = weightInit;
		this.activation = activation;
		this.optimizor = optimizor;
		this.l2Regularization = l2Regularization;
		this.lossFunction = lossFunction;
		this.outputActivation = outputActivation;
		this.learningRate = learningRate;
		this.batchSize = batchSize;
	}
	
	public void init(){
		
        ListBuilder conf = new NeuralNetConfiguration.Builder()
            .seed(rngSeed) //include a random seed for reproducibility
            .optimizationAlgo(optimizor) // use stochastic gradient descent as an optimization algorithm
            .iterations(1)
            .activation(activation)
            .weightInit(weightInit)
            .learningRate(learningRate) //specify the learning rate
            .updater(Updater.NESTEROVS).momentum(0.98) //specify the rate of change of the learning rate.
            //.regularization(l2Regularization).l2(learningRate * 0.005) // regularize learning model
            .list();
        
        // Input layer
    	conf.layer(0, new DenseLayer.Builder()
                .nIn(inputs)
                .nOut(hiddenLayers.isEmpty() ? outputs : hiddenLayers.get(0))
                .build());
    	
    	// Hidden/output layers
        for(int i = 0; i < hiddenLayers.size(); i++){
        	if (i+1 == hiddenLayers.size()){
        		conf.layer(i+1, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD) //create output layer
                        .activation(Activation.SOFTMAX)
                        .nIn(hiddenLayers.get(i))
                        .nOut(outputs)
                        .build());
        	} else {
	        	conf.layer(i+1, new DenseLayer.Builder()
	                    .nIn(hiddenLayers.get(i))
	                    .nOut(hiddenLayers.get(i+1))
	                    .build());
        	}
        }
        
		MultiLayerConfiguration multiConf = conf.pretrain(false).backprop(true) //use backpropagation to adjust weights
				.build();

        network = new MultiLayerNetwork(multiConf);
        network.init();
        
	}

	public void save() throws IOException{
		//Save the model
		String filename = this.toString();
        File locationToSave = new File(filename);      //Where to save the network. Note: the file is in .zip format - can be opened externally
        boolean saveUpdater = true;                                             //Updater: i.e., the state for Momentum, RMSProp, Adagrad etc. Save this if you want to train your network more in the future
        ModelSerializer.writeModel(network, locationToSave, saveUpdater);
	}
	
	public void load() throws IOException{
		
		String filename = this.toString();
		
		//Load the model
		network = ModelSerializer.restoreMultiLayerNetwork(filename);

	}
	
	@Override
	public String toString() {
		String name = "model["+inputs;
		for(int i : hiddenLayers){
			name += "-" + i;
		}
		name += "-" + outputs+"]";
		name += "_" + weightInit;
		name += "_" + activation;
		name += "_" + outputActivation;
		name += "_" + optimizor;
		name += l2Regularization ? "_l2" : "";
		name += "_" + lossFunction;
		return name;
	}
	
	
	
}
