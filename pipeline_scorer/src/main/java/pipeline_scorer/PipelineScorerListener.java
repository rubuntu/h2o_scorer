package pipeline_scorer;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Application Lifecycle Listener implementation class PipelineScoreListener
 *
 */
public class PipelineScorerListener implements ServletContextListener {
	
	private static final Log log = LogFactory.getLog(PipelineScorerListener.class);

    /**
     * Default constructor. 
     */
    public PipelineScorerListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	try {  	
    		
    		Properties properties = new Properties();
    		InputStream in = getClass().getResourceAsStream("/pipeline_scorer.properties");
    		properties.load(in);
    		in.close();    	
    		
    		String mojo_folder=properties.getProperty("mojo_folder");    		    	  	
    		ServletContext ctx = arg0.getServletContext();
    	  	
    		File folder = new File(mojo_folder);
    		File[] listOfFiles = folder.listFiles();

    		for (int i = 0; i < listOfFiles.length; i++) {
    		  if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith("mojo")) {
    			String path = listOfFiles[i].getPath();
    			String pipeline=listOfFiles[i].getName().replaceFirst("[.][^.]+$", "");
				PipelineScorer scorer = new PipelineScorer(path);
				ctx.setAttribute(pipeline, scorer);
				log.info("Loaded Mojo Pipeline: " + listOfFiles[i].getName());    			  
    		  }
    		}
    	  	
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }


	
}
