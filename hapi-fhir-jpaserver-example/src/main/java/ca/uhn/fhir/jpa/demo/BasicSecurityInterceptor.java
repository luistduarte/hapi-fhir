package ca.uhn.fhir.jpa.demo;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

public class BasicSecurityInterceptor extends InterceptorAdapter
{

   /**
    * This interceptor implements HTTP Basic Auth, which specifies that
    * a username and password are provided in a header called Authorization.
    */
   @Override
   public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
      
	   String authHeader = theRequest.getHeader("Authorization");
	   System.out.println("ON INTERCEPTOR Header" + authHeader);

      // The format of the header must be:
      if (authHeader == null || authHeader.startsWith("Bearer ") == false) {
         throw new AuthenticationException("Missing or invalid Authorization header");
      }

      //One way to check if token is valid or Not
      //If responde code = 200 token is valid, else token isnt valid

      int responseCode = 0;
      try {
		String url = "http://localhost:8083/v1.0.M1/dataPoints?schema_namespace=omh&schema_name=physical-activity&schema_version=1.0";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Authorization", authHeader);

		System.out.println(con.getHeaderFields().toString());
		responseCode = con.getResponseCode();
		System.out.println("Response Code : " + responseCode);

      } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
      }

      if (responseCode != 200) {
    	  throw new AuthenticationException("Missing or invalid Authorization header");
      }




      // Return true to allow the request to proceed
      return true;
   }


}
