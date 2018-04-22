package programs;

import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;

public class JenkinsTest {

		public static void main(String[] args) {
			
			//java jenkins api programming examples
			
			/*System.out.println("Check 0: List Jobs");
			List<String> jobList = listJobs("http://192.168.43.215:8080");
			System.out.println("First Job:"+jobList.get(0));
			
			System.out.println("Check 4: Create AA_TEST_JOB4 by using the xml configuration from the first job (similar to copyJob)");
			String configXML = readJob("http://192.168.43.215:8080", jobList.get(0));
			createJob("http://192.168.43.215:8080", "Test1", configXML);

			System.out.println("Check 2: Create AA_TEST_JOB2 by copying first job");
			copyJob("http://192.168.43.215:8080", "Test2", jobList.get(0));

			System.out.println("Check 3: Create AA_TEST_JOB3 by using a generic xml configuration");
			createJob("http://192.168.43.215:8080", "Test3", "<project><builders/><publishers/><buildWrappers/></project>");

			System.out.println("Check 1: Delete AA_TEST_JOB1 (created manually)");
			deleteJob("http://my.jenkins.com", "AA_TEST_JOB1");*/
			
			Client client = Client.create();
			WebResource webResource = client.resource("http://192.168.43.215:8080/computer/api/json/");
			ClientResponse response = webResource.get(ClientResponse.class);
			String jsonResponse = response.getEntity(String.class);
			client.destroy();
			
      		System.out.println("Response copyJob:::::"+jsonResponse);
      		

		}
		
		public static List<String> listJobs(String url) {
			Client client = Client.create();
//			client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME, PASSWORD));
			WebResource webResource = client.resource(url+"/api/xml");
			ClientResponse response = webResource.get(ClientResponse.class);
			String jsonResponse = response.getEntity(String.class);
			client.destroy();
			System.out.println("Response listJobs:::::"+jsonResponse);
			
			// Assume jobs returned are in xml format, TODO using an XML Parser would be better here
			// Get name from <job><name>...
			List<String> jobList = new ArrayList<String>();
			String[] jobs = jsonResponse.split("job>"); // 1, 3, 5, 7, etc will contain jobs
			for(String job: jobs){
				String[] names = job.split("name>");
				if(names.length == 3) {
					String name = names[1];
					name = name.substring(0,name.length()-2); // Take off </ for the closing name tag: </name>
					jobList.add(name);
//					System.out.println("name:"+name);
				}
//				System.out.println("job:"+job);
//				for(String name: names){
//					System.out.println("name:"+name);
//				}
			}
			return jobList;
		}

		public static String deleteJob(String url, String jobName) {
			Client client = Client.create();
//			client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME, PASSWORD));
			WebResource webResource = client.resource(url+"/job/"+jobName+"/doDelete");
			ClientResponse response = webResource.post(ClientResponse.class);
			String jsonResponse = response.getEntity(String.class);
			client.destroy();
//			System.out.println("Response deleteJobs:::::"+jsonResponse);
			return jsonResponse;
		}
		
		public static String copyJob(String url, String newJobName, String oldJobName){
			Client client = Client.create();
//			client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME, PASSWORD));
			WebResource webResource = client.resource(url+"/createItem?name="+newJobName+"&mode=copy&from="+oldJobName);
			ClientResponse response = webResource.type("application/xml").get(ClientResponse.class);
			String jsonResponse = response.getEntity(String.class);
			client.destroy();
//			System.out.println("Response copyJob:::::"+jsonResponse);
			return jsonResponse;
		}
		
		public static String createJob(String url, String newJobName, String configXML){
			Client client = Client.create();
//			client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME, PASSWORD));
			WebResource webResource = client.resource(url+"/createItem?name="+newJobName);
			ClientResponse response = webResource.type("application/xml").post(ClientResponse.class, configXML);
			String jsonResponse = response.getEntity(String.class);
			client.destroy();
			System.out.println("Response createJob:::::"+jsonResponse);
			return jsonResponse;
		}

		public static String readJob(String url, String jobName){
			Client client = Client.create();
//			client.addFilter(new com.sun.jersey.api.client.filter.HTTPBasicAuthFilter(USERNAME, PASSWORD));
			WebResource webResource = client.resource(url+"/job/"+jobName+"/config.xml");
			ClientResponse response = webResource.get(ClientResponse.class);
			String jsonResponse = response.getEntity(String.class);
			client.destroy();
//			System.out.println("Response readJob:::::"+jsonResponse);
			return jsonResponse;
		
	}

}
