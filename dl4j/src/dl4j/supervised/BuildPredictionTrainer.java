package dl4j.supervised;

import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.api.storage.StatsStorage;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.ui.api.UIServer;
import org.deeplearning4j.ui.stats.StatsListener;
import org.deeplearning4j.ui.storage.InMemoryStatsStorage;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bwapi.Race;
import bwapi.UnitRepository;
import dl4j.model.BuildPredictionModel;

public class BuildPredictionTrainer {

    private static Logger log = LoggerFactory.getLogger(BuildPredictionTrainer.class);
    
    private static String filenameTrain = "data/samples/train.csv";
    private static String filenameTest = "data/samples/test.csv";
    
    private static Race ownRace = Race.PROTOSS;
    private static Race oppRace = Race.TERRAN;
    
    public static void main(String[] args) throws IOException, InterruptedException{
    	
    	int inputs = getInputSize();
    	int outputs = getOutputSize();
    	
    	BuildPredictionModel model = new BuildPredictionModel(
    			inputs, 
    			outputs, 
    			Arrays.asList(128,128,128,128), 
    			WeightInit.XAVIER, 
    			Activation.RELU, 
    			Activation.SOFTMAX, 
    			OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT, 
    			false, 
    			//LossFunction.NEGATIVELOGLIKELIHOOD, 
    			LossFunction.MCXENT,
    			0.0015,
    			64);
    	System.out.println(UnitRepository.protoss.size());
    	model.init();
    	
    	train(model, 5);
    	
    }

	public static void train(BuildPredictionModel model, int epochs) throws IOException, InterruptedException{

    	log.info("Loading data....");
    	//Load the training data:
        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new File(filenameTrain)));
        DataSetIterator trainIter = new RecordReaderDataSetIterator(rr,model.batchSize,0,model.outputs);

        //Load the test/evaluation data:
        RecordReader rrTest = new CSVRecordReader();
        rrTest.initialize(new FileSplit(new File(filenameTest)));
        DataSetIterator testIter = new RecordReaderDataSetIterator(rrTest,model.batchSize,0,model.outputs);
        // DataSet set = testIter.next();
        // System.out.println(set);
        /*
        //Initialize the user interface backend
    	UIServer uiServer = UIServer.getInstance();
    	
        //Configure where the network information (gradients, score vs. time etc) is to be stored. Here: store in memory.
        StatsStorage statsStorage = new InMemoryStatsStorage();         //Alternative: new FileStatsStorage(File), for saving and loading later
        
        //Attach the StatsStorage instance to the UI: this allows the contents of the StatsStorage to be visualized
        uiServer.attach(statsStorage);

        //Then add the StatsListener to collect this information from the network, as it trains
        model.network.setListeners(new StatsListener(statsStorage));
			*/
        log.info("Train model....");
        for( int i=0; i<epochs; i++ ){
        	log.info("Epoch " + i);
        	model.network.fit(trainIter);
        }
        
        log.info("Evaluate model....");
        Evaluation eval = new Evaluation(model.outputs); //create an evaluation object 
        while(testIter.hasNext()){
            DataSet next = testIter.next();
            INDArray output = model.network.output(next.getFeatureMatrix()); //get the networks prediction
            eval.eval(next.getLabels(), output); //check the prediction against the true class
        }
        log.info(eval.stats());
        log.info("****************Eval finished********************");

        
    }
    
    private static int getOutputSize() {
		if (ownRace == Race.PROTOSS){
			return UnitRepository.protoss.size();
		} else if (ownRace == Race.TERRAN){
			return UnitRepository.terran.size();
		} else if (ownRace == Race.ZERG){
			return UnitRepository.zerg.size();
		}
		return -1;
	}
    
    private static int getNumOfUnits(Race race) {
		if (race == Race.PROTOSS){
			return UnitRepository.protossUnits.size();
		} else if (race == Race.TERRAN){
			return UnitRepository.terranUnits.size();
		} else if (race == Race.ZERG){
			return UnitRepository.zergUnits.size();
		}
		return -1;
	}
    
	private static int getInputSize() {
		if (ownRace == Race.PROTOSS){
			return UnitRepository.protoss.size() * 3 + getNumOfUnits(oppRace) + 3;
		} else if (ownRace == Race.TERRAN){
			return UnitRepository.terran.size() * 3 + getNumOfUnits(oppRace) + 3;
		} else if (ownRace == Race.ZERG){
			return UnitRepository.zerg.size() * 3 + getNumOfUnits(oppRace) + 3;
		}
		return -1;
	}

}
