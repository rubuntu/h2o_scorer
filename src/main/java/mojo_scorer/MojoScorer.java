package mojo_scorer;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import hex.ModelCategory;
import hex.genmodel.MojoModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.BinomialModelPrediction;
import hex.genmodel.easy.prediction.RegressionModelPrediction;

public class MojoScorer {
	
	private MojoModel model;
	private EasyPredictModelWrapper predictModelWrapper;	
	private ModelCategory modelCategory;
	private String model_name;	
		
	public MojoScorer(String mojo) {
		try {
			model = MojoModel.load(mojo);
			predictModelWrapper =  new EasyPredictModelWrapper(model);			
			modelCategory= predictModelWrapper.getModelCategory();			
			mojo=mojo.replace("\\", "/");		
			String s[]=mojo.split("/|\\.");		
			model_name=s[s.length-2];				
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public MojoModel getModel() {
		return model;
	}

	public String predict(Map<String, String[]> parameters) {		
		double score = 0;
		String error_msg="";
			
		RowData rowData = new RowData();
		
		Set<String> fields = parameters.keySet();

		for (String field:fields) {
			String[] value = parameters.get(field);
			rowData.put(field, value[0]);
			//System.out.println(field + ":" + value[0]);			
		}		
		
		for (String field:fields) {
			try {
				if(modelCategory.equals(ModelCategory.Binomial)) {
					BinomialModelPrediction p = predictModelWrapper.predictBinomial(rowData);
					score = p.classProbabilities[1];
				} else if(modelCategory.equals(ModelCategory.Regression)) {
					RegressionModelPrediction p = predictModelWrapper.predictRegression(rowData);
					score = p.value;					
				}
				if (score > 0) {
					break;
				}
			} catch (PredictException e) {
				if (e.getMessage().contains(field)) {
					error_msg = error_msg.isEmpty() ? e.getMessage() : error_msg + "|" + e.getMessage();
					rowData.remove(field);					
				}
			}
		}		
		
		String ret = "{\"model_name\": \"" + model_name + "\" , \"score\": " + score + " , \"error_msg\": \"" + error_msg + "\" }";
		
		return ret;
	}
}
