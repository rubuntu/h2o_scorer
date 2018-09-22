import org.yaml.snakeyaml.Yaml;

import ai.h2o.com.google.gson.Gson;
import ai.h2o.com.google.gson.JsonArray;
import ai.h2o.com.google.gson.JsonObject;
import ai.h2o.mojos.runtime.MojoPipeline;
import ai.h2o.mojos.runtime.frame.MojoFrame;
import ai.h2o.mojos.runtime.frame.MojoFrameBuilder;
import ai.h2o.mojos.runtime.frame.MojoRowBuilder;
import ai.h2o.mojos.runtime.utils.SimpleCSV;

public class Main {

  public static void main(String[] args) throws Exception {
	
	long ini = System.currentTimeMillis();
	  
    // Load model and csv
    MojoPipeline model = MojoPipeline.loadFrom("pymes_sm.mojo");

    // Get and fill the input columns
    MojoFrameBuilder frameBuilder = model.getInputFrameBuilder();
    MojoRowBuilder rowBuilder = frameBuilder.getMojoRowBuilder();
    
    /*    
    rowBuilder.setValue("AGE", "68");
    rowBuilder.setValue("RACE", "2");
    rowBuilder.setValue("DCAPS", "2");
    rowBuilder.setValue("VOL", "0");
    rowBuilder.setValue("GLEASON", "6");    
     */
    
    
    frameBuilder.addRow(rowBuilder);
    // Create a frame which can be transformed by MOJO pipeline
    MojoFrame iframe = frameBuilder.toMojoFrame();

    // Transform input frame by MOJO pipeline
    MojoFrame oframe = model.transform(iframe);
    // `MojoFrame.debug()` can be used to view the contents of a Frame
    //oframe.debug();
    
	//Yaml yaml = new Yaml();
	Gson gson = new Gson();		
		
	
	//System.out.println(yaml.dump(oframe));
	//System.out.println(gson.toJson(oframe));
    
	//System.out.println("column_name[1]: "+gson.toJson(oframe.getColumnName(1)));
	//System.out.println("column_data[1]: "+gson.toJson(oframe.getColumnData(1)));
	
	//System.out.println(gson.toJson(model));	
	//System.out.println(gson.toJson(iframe));
	//System.out.println(yaml.dump(iframe));
		
	//System.out.println("column_name[1]: "+ oframe.getColumnName(1));
	//System.out.println("column_data[1]: "+ gson.toJson(oframe.getColumnData(1)));
		
	//JsonObject obj = new JsonObject();
	//obj.addProperty(oframe.getColumnName(1), gson.toJson(oframe.getColumnData(1)));

	//System.out.println(obj);
	
	//JsonArray column_data = obj.getAsJsonArray(oframe.getColumnName(1));

	//System.out.println(column_data);
		
    // Output prediction as CSV
    //SimpleCSV outCsv = SimpleCSV.read(oframe);
    //System.out.println(gson.toJson(outCsv));
    
    JsonObject out = new JsonObject();    
    for(int i=0; i< oframe.getNcols();i++) {
    	String column_name=oframe.getColumnName(i).replaceAll("\\.| ", "_");
    	String column_data=gson.toJson(oframe.getColumnData(i)).replaceAll("\\[|\\]", "");
    	out.addProperty(column_name , column_data);    	
    }
    
    System.out.println(out);
    long end = System.currentTimeMillis();
    System.out.println("lapso:"+(end-ini));
    
    
    //outCsv.write(System.out);
  }
}
