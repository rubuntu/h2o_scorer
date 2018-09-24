package pipeline_scorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ai.h2o.com.google.gson.Gson;

/**
 * Servlet implementation class PipelineScorerService
 */
public class PipelineScorerService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(PipelineScorerService.class);
       
	private Properties properties = new Properties();
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PipelineScorerService() {
    	
		try {			
			InputStream in = getClass().getResourceAsStream("/pipeline_scorer.properties");
			properties.load(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}    

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "unchecked"})
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();		
		String respuesta="{}";
		Map<String, String[]>  parameterMap = request.getParameterMap();
		String jsonParameters=gson.toJson(parameterMap).replaceAll("\\[|\\]","");
		log.info("RequestURI:"+request.getRequestURI());		
		log.info("Parameters:" + jsonParameters);
		
		String pipeline=request.getPathInfo().substring(1);
		
		String path=getServletContext().getRealPath("/")+ pipeline +".mojo";
			
		String filePath=properties.getProperty("mojo_folder", getServletContext().getRealPath("/"))+pipeline+".mojo";				
		File f = new File(path);		
		if(f.isFile()) {			
			if( !(new File(filePath)).isFile() ) {
				copyFile(path,filePath);
			}
			
			PipelineScorer scorer = (PipelineScorer) getServletContext().getAttribute(pipeline);
			if(scorer == null) {
				scorer = new PipelineScorer(path);
				getServletContext().setAttribute(pipeline, scorer);
			}			
			
			if(!jsonParameters.equals("{}") && scorer != null) { 						
				respuesta = scorer.predict(parameterMap);
			}
		}		
		log.info("Response:"+respuesta);
		response.setContentType("application/json");		
		response.getWriter().append(respuesta);		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}
	
		
	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		service(request, response);
	}


	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	/** 
	 * @param source
	 * @param dest
	 * @throws IOException
	 */
	private void copyFile(String source, String dest) throws IOException {
	    FileChannel sourceChannel = null;
	    FileChannel destChannel = null;
	    try {
	        sourceChannel = new FileInputStream(source).getChannel();
	        destChannel = new FileOutputStream(dest).getChannel();
	        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
	       }finally{
	           sourceChannel.close();
	           destChannel.close();
	   }
	}	

}
