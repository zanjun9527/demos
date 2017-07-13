package restfulEasyDemo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("test/")
public class testjava {
	
	@Path("go/{userName}")
	@GET
	public void testRestfulEasy(@PathParam("userName")String userName){
		
		System.out.println(userName);
		
	}
	
}
