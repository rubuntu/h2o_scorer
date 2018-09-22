package pipeline_scorer;

import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import ai.h2o.com.google.gson.Gson;
import ai.h2o.com.google.gson.JsonObject;
import ai.h2o.mojos.runtime.MojoPipeline;
import ai.h2o.mojos.runtime.frame.MojoFrame;
import ai.h2o.mojos.runtime.frame.MojoFrameBuilder;
import ai.h2o.mojos.runtime.frame.MojoRowBuilder;

public class PipelineScorer {
	
	private MojoPipeline model;
	private String pipelineName="";			
	private Gson gson = new Gson();
		
	public PipelineScorer(String pipelineFile) {
		super();
		try {							
			//System.out.println("pipeline_file:"+pipelineFile);				
			model = MojoPipeline.loadFrom(pipelineFile);			
			pipelineFile=pipelineFile.replace("\\", "/");		
			String s[]=pipelineFile.split("/|\\.");		
			pipelineName=s[s.length-2];				
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public MojoPipeline getModel() {
		return model;
	}

	public String predict(Map<String, String[]> parameters) {		
				
	    // Get and fill the input columns
				
	    MojoFrameBuilder frameBuilder = model.getInputFrameBuilder();
	    MojoRowBuilder rowBuilder = frameBuilder.getMojoRowBuilder();		
		Set<String> fields = parameters.keySet();

		String id="0";
		for (String field:fields) {
			String[] value = parameters.get(field);		
			rowBuilder.setValue(field, value[0]);
			if(field.equalsIgnoreCase("id")) {
				id=value[0];
			}
			//System.out.println(field + ":" + value[0]);			
		}						
		frameBuilder.addRow(rowBuilder);
		
	    // Create a frame which can be transformed by MOJO pipeline
	    MojoFrame iframe = frameBuilder.toMojoFrame();

	    // Transform input frame by MOJO pipeline
	    MojoFrame oframe = model.transform(iframe);
	   		
	    String columnName="";
	    String columnData="";
	    String errorMsg="";
	    JsonObject out = new JsonObject();
	    if(!id.equals("0")){
	    	out.addProperty("id", id);	
	    }	    
	    out.addProperty("pipeline", pipelineName);	  	    
	    for(int i=0; i< oframe.getNcols();i++) {
	    	columnName=oframe.getColumnName(i).replaceAll("\\.| ", "_");
	    	columnData=gson.toJson(oframe.getColumnData(i)).replaceAll("\\[|\\]", "");
	    	out.addProperty(columnName , columnData);    	
	    }
	    //Binary classification
	    if(oframe.getNcols()==2) {
	    	out.addProperty("score" , columnData);
	    }
	    out.addProperty("error_msg" , errorMsg);
		return out.toString();
	}
}
