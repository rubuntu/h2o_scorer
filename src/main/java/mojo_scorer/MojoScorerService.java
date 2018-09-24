package mojo_scorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yaml.snakeyaml.Yaml;

import ai.h2o.com.google.gson.Gson;

/**
 * Servlet implementation class ModelScorerServlet
 */
public class MojoScorerService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private static final Log log = LogFactory.getLog(ModelScorerService.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MojoScorerService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings({ "unchecked"})
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Yaml yaml = new Yaml();
		Gson gson = new Gson();		
		String respuesta="{}";
		Map<String, String[]>  parameterMap = request.getParameterMap();
		String jsonParameters=gson.toJson(parameterMap).replaceAll("\\[|\\]","");
		//log.info("RequestURI:"+request.getRequestURI());		
		//log.info("QueryString:"+request.getQueryString());
		//log.info("Parameters:" + jsonParameters);
		
		String s[]=request.getRequestURI().split("/");		
		String model=s[s.length-1];
		
		String path=getServletContext().getRealPath("/")+ model +".zip";
		//log.info("Path:" + path);		
		//String filePath="/opt/tomcat7/webapps/mojo_scorer/"+model+".zip";
				
		File f = new File(path);		
		if(f.isFile()) {
			
			//if( !(new File(filePath)).isFile() ) {
			//	copyFile(path,filePath);
			//}
			
			MojoScorer scorer = (MojoScorer) getServletContext().getAttribute(path);
			if(scorer == null) {
				scorer = new MojoScorer(path);
				getServletContext().setAttribute(path, scorer);
			}			
			
			if(!jsonParameters.equals("{}")) { 				
				respuesta = jsonParameters.replace("}",",") + scorer.predict(parameterMap).substring(1);
			} else {
				String yamlString=yaml.dump(scorer.getModel()).replace("!!","_algorithm: " );				
				respuesta=gson.toJson(yaml.load(yamlString));
			}
		}
		
		//log.info("Response:"+respuesta);
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
