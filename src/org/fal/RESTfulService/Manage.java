package org.fal.RESTfulService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.fal.model.Group;
import com.fal.model.User;
import com.rest.DB.DBClass;
import com.rest.util.ToJSON;

@Path("manage")
public class Manage {
	
	//private User currentUser = new User();;

	
	/**
	* Allow a user to write on board.
	* This function is called when we need to store a new message from the user into the database "writes" on the board of the group
	* @param message the message which will be posted on the board
	* @param group the group with the required board
	* @throws SQLException
	* @since 1.0
	*/
	@POST
	@Path("writeOnBoard")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void writeOnBoard(
			@FormParam("email") String email,	
            @FormParam("message") String message,
    		@FormParam("group") String group) throws SQLException
	{
		Connection connection = DBClass.returnConnection();
		PreparedStatement ps = connection.prepareStatement("INSERT INTO board (group_id, message)" + "VALUES(?,?)");
		ps.setString(1,group);
		ps.setString(2,email+ ": " + message);
		ps.executeUpdate();
	}
	
	/**
	* Allow to add a user.
	* This function is called when we need to store a new user into the database from a JSON
	* @param incomingData a JSON Array converts into String
	* @return ok if success, error if not
	* @throws JSONException
	* @since 1.0
	*/
		@POST
		@Path("user_v2")
		@Consumes(MediaType.APPLICATION_JSON)
 		public Response addUser(String incomingData) throws JSONException
		{
			JSONObject jsonObject = new JSONObject(incomingData);
			String email = jsonObject.getString("email");
			String lastname = jsonObject.getString("lastname");
			String firstname = jsonObject.getString("firstname");
			String biography = jsonObject.getString("biography");
			User user = new User(email,lastname,firstname,biography);
			int http_code = InsertUserIntoTheDataBase(user);
			if (http_code==200) {
			return Response.ok().build();
			}
			else
			{
			return Response.serverError().build();
			}
		}
		
		/**
		* Allow to add a group.
		* This function is called when we need to store a new group into the database from a JSON
		* @param incomingData a JSON Array converts into String
		* @return ok if success, error if not
		* @throws JSONException
		* @since 1.0
		*/		@POST
		@Path("group_v2")
		@Consumes(MediaType.APPLICATION_JSON)
		public Response addGroup(String incomingData) throws JSONException
		{
			JSONObject jsonObject = new JSONObject(incomingData);
			String name = jsonObject.getString("name");
			String description = jsonObject.getString("description");
			String admin = jsonObject.getString("admin");
			Group group = new Group(name,description,admin);
			int http_code = InsertGroupIntoTheDataBase(group);
			if (http_code==200) {
			return Response.ok().build();
			}
			else
			{
			return Response.serverError().build();
			}
		}
		
		/**
		* Allow to add a user into the database.
		* This function is called when we need to register the current user's information into the database from its information
		* @param user a user with all the needed information (email, lastname, ...)
		* @return 200 if ok, 400 if not
		* @since 1.0
		*/
		public int InsertUserIntoTheDataBase(User user) 
		{
			Connection connection = DBClass.returnConnection(); 
			PreparedStatement insert;
			try {
				insert = connection.prepareStatement("INSERT INTO user (email,lastname,firstname,biography)" + "VALUES(?,?,?,?)");
				insert.setString(1, user.getEmail());
				insert.setString(2, user.getLastname());
				insert.setString(3, user.getFirstname());
				insert.setString(4, user.getBiography());
				insert.executeUpdate();
				return 200;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 400;
			}
		}
		
		/**
		* Allow to add a group into the database.
		* This function is called when we need to register the group's information into the database from its information
		* @param group a group with all the needed information (name, description, ...)
		* @return 200 if ok, 400 if not
		* @since 1.0
		*/
		public int InsertGroupIntoTheDataBase(Group group) 
		{
			Connection connection = DBClass.returnConnection(); 
			PreparedStatement insert;
			try {
				insert = connection.prepareStatement("INSERT INTO group (name,description,admin)" + "VALUES(?,?,?)");
				insert.setString(1, group.getName());
				insert.setString(2, group.getDescription());
				insert.setString(2, group.getAdmin());
				insert.executeUpdate();
				return 200;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 400;
			}
		}
		

		/**
		* Allow to add a user.
		* This function is called when we need to store a new user into the database from a form
		* @param email the email of the user
		* @param lastname the lastname of the user
		* @param firstname the firstname of the user
		* @param biography the biography of the user		
		* @throws SQLException
		* @throws URISyntaxException
		* @since 1.0
		*/		
		@POST
		@Path("users_v1")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response addUser(
				@FormParam("email") String email,
	            @FormParam("lastname") String lastname,
	            @FormParam("firstname") String firstname,
	            @FormParam("biography") String biography) throws SQLException, URISyntaxException
		{
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement("INSERT INTO user (email,lastname,firstname,biography)" + "VALUES(?,?,?,?)");
			ps.setString(1,email);
			ps.setString(2,lastname);
			ps.setString(3,firstname);
			ps.setString(4,biography);
			
			
			java.net.URI location;
			try {
				ps.executeUpdate();
				location = new java.net.URI("../../profil.html");
				return Response.seeOther(location).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response.status(Status.ACCEPTED).build();
		}
		
		/**
		* Allow to delete an user account.
		* This function is called when we need to delete an user account from the database with a button and a form
		* @param email the email of the user	
		* @throws Exception
		* @return redirection on an other web page
		* @since 1.0
		*/	
		@POST
        @Path("user_dma")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response deleteMyAccount(@FormParam("email") String email) throws Exception
        {
			 Connection connection = DBClass.returnConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM user WHERE email=?"
                    );
            ps.setString(1,email);
            java.net.URI location;
			try {
				ps.executeUpdate();
				location = new java.net.URI("../../index.html");
				return Response.seeOther(location).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				location = new java.net.URI("../../error.html");
				return Response.seeOther(location).build(); 
		}
           
        }
		
		/**
		* Allow to add a group.
		* This function is called when we need to store a new user into the database from a form
		* @param email the email of the current user
		* @param name the name of the group
		* @param description the description of the group
		* @throws SQLException
		* @since 1.0
		*/				
		@POST
		@Path("groups_v1")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response addGroup(
				@FormParam("email") String email,
				@FormParam("name") String name,
	            @FormParam("description") String description
	            ) throws SQLException
		{
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement("INSERT INTO groups (name,description,admin)" + "VALUES(?,?,?)");
			ps.setString(1,name);
			ps.setString(2,description);
			ps.setString(3,email);
			
			java.net.URI location;
			try {
				ps.executeUpdate();
				location = new java.net.URI("../../myGroups.html");
				return Response.seeOther(location).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response.status(Status.ACCEPTED).build();
		}
		
		/**
		* Allow to change the last name of a user.
		* This function is called when we need to update the last name of the current user (from a form)
		* @param email the email of the user
		* @param lastname the last name of the user	
		* @throws Exception
		* @since 1.0
		*/		
		@POST
		@Path("users_v2")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response changeLastnameUser(
				@FormParam("email") String email,
				@FormParam("lastname") String lastname
	            ) throws Exception
		{
			
			Connection connection = DBClass.returnConnection();
			//emailduUser = email du user connecté
			
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE user SET lastname = ? WHERE email = ?");
			ps.setString(1,lastname);
			ps.setString(2,email);			
			 java.net.URI location;
				try {
					ps.executeUpdate();
					location = new java.net.URI("../../profil.html");
					return Response.seeOther(location).build();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					location = new java.net.URI("../../error.html");
					return Response.seeOther(location).build(); 
			}
		}
		
		/**
		* Allow to change the first name of a user.
		* This function is called when we need to update the first name of the current user (from a form)
		* @param email the email of the user
		* @param firstname the first name of the user	
		* @throws Exception
		* @since 1.0
		*/			
		@POST
		@Path("users_v3")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response changeFirstnameUser(
				@FormParam("email") String email,
	            @FormParam("firstname") String firstname
	            ) throws Exception
		{
			
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE user SET firstname = ? WHERE email = ?");
			ps.setString(1,firstname);
			ps.setString(2,email);
			java.net.URI location;
			try {
				ps.executeUpdate();
				location = new java.net.URI("../../profil.html");
				return Response.seeOther(location).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				location = new java.net.URI("../../error.html");
				return Response.seeOther(location).build(); 
		}
		}
		
		/**
		* Allow to change the biography of a user.
		* This function is called when we need to update the biography of the current user (from a form)
		* @param email the email of the user
		* @param biography the lastname of the user	
		* @throws Exception
		* @since 1.0
		*/			
		@POST
		@Path("users_v4")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response changeBiographyUser(
				@FormParam("email") String email,
	            @FormParam("biography") String biography
	            ) throws Exception
		{
			
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE user SET biography = ? WHERE email = ?");
			ps.setString(1,biography);
			ps.setString(2,email);
			java.net.URI location;
			try {
				ps.executeUpdate();
				location = new java.net.URI("../../profil.html");
				return Response.seeOther(location).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				location = new java.net.URI("../../error.html");
				return Response.seeOther(location).build(); 
		}
		}
		
		/**
		* Allow to change the description of a group. 
		* This function is called when we need to update the description of a group (from a form)
		* @param email the email of the user (admin)
		* @param name the name of the group
		* @param description the new description of the group
		* @throws Exception
		* @since 1.0
		*/			
		@POST
        @Path("groups_cgd")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response changeGroupDescription(
                @FormParam("email") String email,
                @FormParam("name") String name,
                @FormParam("description") String newDescription
                ) throws Exception
        {
            
            Connection connection = DBClass.returnConnection();
            
            PreparedStatement ps_admin = connection.prepareStatement(
                    "SELECT admin FROM groups WHERE name= ?"
            );
            ps_admin.setString(1, name);
             ResultSet resultSet = ps_admin.executeQuery();
             ToJSON tojson = new ToJSON();
             JSONArray jsonArray = tojson.toJSONArray(resultSet);
            String adminGroup = jsonArray.toString();
            JSONObject adminObject = jsonArray.getJSONObject(0);
            adminGroup=adminObject.getString("admin");
            java.net.URI location;
            if(adminGroup.equals(email)){
                PreparedStatement ps = connection.prepareStatement(
                        "UPDATE groups SET description= ? WHERE name= ? ");
                ps.setString(1,newDescription);
                ps.setString(2,name);
                
    			try {
    				ps.executeUpdate();
    				location = new java.net.URI("../../myGroups.html");
    				return Response.seeOther(location).build();
    			} catch (URISyntaxException e) {
    				e.printStackTrace();
    				location = new java.net.URI("../../error.html");
    				return Response.seeOther(location).build();

    			}
    			
            }
            
            location = new java.net.URI("../../error.html");
			return Response.seeOther(location).build();

        }
		
		/**
		* Allow a user to join a group. 
		* This function is called when we need to insert a new user in a new group  into the database(from a form)
		* @param name the name of the group
		* @throws Exception
		* @return  
		* @since 1.0
		*/			        
		@POST
		@Path("groups_jg")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response joinGroup(
        		@FormParam("email") String email,
                @FormParam("name") String grName
                ) throws Exception
        {
            Connection connection = DBClass.returnConnection();
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO user_group (user_id,group_id) VALUES(?,?)"
                    );
            ps.setString(1,email);
            ps.setString(2, grName);
            
            java.net.URI location;
			try {
				ps.executeUpdate();
				location = new java.net.URI("../../groups.html");
				return Response.seeOther(location).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				location = new java.net.URI("../../error.html");
				return Response.seeOther(location).build();
			}
		
        }
        
		/**
		* Allow to delete a group.
		* This function is called when we need to delete a group from the database with a button and a form
		* @param email the email of the user (admin)
		* @param name the name of the group	
		* @throws SQLException
		* @since 1.0
		*/	
        //Supprimer un groupe via un formulaire + bouton
        @POST
		@Path("groups_delete")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        public Response deleteGroup(
		
				@FormParam("email") String email,
				@FormParam("name") String name) throws Exception
		{
			
			Connection connection = DBClass.returnConnection();
			java.net.URI location;
				
				PreparedStatement ps0 = connection.prepareStatement("SELECT admin FROM groups WHERE name = ?");
				ps0.setString(1,name);
				ResultSet resultSet = ps0.executeQuery();
				ToJSON tojson = new ToJSON();
				JSONArray jsonArray;
				jsonArray = tojson.toJSONArray(resultSet);
				String adminGroup = jsonArray.toString();
	            JSONObject adminObject = jsonArray.getJSONObject(0);
	            adminGroup=adminObject.getString("admin");
	            
	            if(adminGroup.equals(email)){
	            	PreparedStatement ps = connection.prepareStatement("DELETE FROM groups WHERE name = ?");
					ps.setString(1,name);
					PreparedStatement psbis = connection.prepareStatement("DELETE FROM user_group WHERE group_id = ?");
					psbis.setString(1,name);
	    			try {
	    				ps.executeUpdate();
	    				psbis.executeUpdate();
	    				location = new java.net.URI("../../myGroups.html");
	    				return Response.seeOther(location).build();
	    			} catch (URISyntaxException e) {
	    				e.printStackTrace();
	    				location = new java.net.URI("../../error.html");
	    				return Response.seeOther(location).build();
	    			}
	            }
	            
	            location = new java.net.URI("../../error.html");
				return Response.seeOther(location).build();			
	         
		}
		
		/**
		* Allow to leave a group.
		* This function is called when we need to pull a user from a group from the database with a button and a form
		* @param email the email of the current user
		* @param name the name of the group	
		* @throws SQLException
		* @since 1.0
		*/	
		@POST
		@Path("groups_v5")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void leaveGroup(@FormParam("email") String email, @FormParam("name") String name) throws SQLException{
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement("DELETE FROM user_group WHERE group_id = ? AND user_id = ?");
			ps.setString(1,name);
			ps.setString(2,email);
			ps.executeUpdate();
		}
		
		/**
		* Allow to have all the users. 
		* This function is called when we need to get all users from the database by using a JSON request
		* @return jsonArray with all the users registered into the database
		* @throws Exception
		* @since 1.0
		*/	
		@GET
		 @Path("users")
		 @Produces(MediaType.APPLICATION_JSON)
		public String getUsers() throws Exception{
			 Connection connection = DBClass.returnConnection();
			 PreparedStatement ps = connection.prepareStatement(
					 "SELECT * FROM user"
					 );
			 ResultSet resultSet = ps.executeQuery();
			 ToJSON tojson = new ToJSON();
			 JSONArray jsonArray = tojson.toJSONArray(resultSet);
			 
			 return jsonArray.toString();
		 }
		 
		//Renvoie le user dont l'email est placé en paramètre dans l'url 
		@GET
		 @Path("user")
		 @Produces(MediaType.APPLICATION_JSON)
		 public Response getUser(@QueryParam ("email")String email) throws Exception{
			 Connection connection = DBClass.returnConnection();
			 String mail = email.replace("%40", "@");
			 PreparedStatement ps = connection.prepareStatement(
	                 "SELECT * FROM user WHERE email=?"
	                 );

	         ps.setString(1,mail);
			 ResultSet resultSet = ps.executeQuery();
			 ToJSON tojson = new ToJSON();
			 JSONArray jsonArray = tojson.toJSONArray(resultSet);
			 
			 java.net.URI location;
			 
			 String output = jsonArray.toString();
			 if(output.equals("[]")){
				 try {
						//location = new java.net.URI("manage/user?email="+email);
						location = new java.net.URI("../../error.html");
						return Response.temporaryRedirect(location).build();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			 }
			 	//return Response.status(412).build(); 
			 
			 try {
				 	
					//location = new java.net.URI("manage/user?email="+email);
					location = new java.net.URI("../../profil.html");
				
					
					return Response.temporaryRedirect(location).build();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			 
			 return Response.status(200).entity(output).build();
		 }
		
		@GET
		 @Path("userjson")
		 @Produces(MediaType.APPLICATION_JSON)
		 public String getUserjson(@QueryParam ("email")String email) throws Exception{
			 Connection connection = DBClass.returnConnection();
			 String mail = email.replace("%40", "@");
			 PreparedStatement ps = connection.prepareStatement(
	                 "SELECT * FROM user WHERE email=?"
	                 );

	         ps.setString(1,mail);
			 ResultSet resultSet = ps.executeQuery();
			 ToJSON tojson = new ToJSON();
			 JSONArray jsonArray = tojson.toJSONArray(resultSet);
			 String output = jsonArray.toString();
			 if(output.equals("[]"))
				 return Response.status(412).build().toString(); 
			 return output;
		 }
		
		@GET
		 @Path("groupsjson")
		 @Produces(MediaType.APPLICATION_JSON)
		 public String getGroupsjson(@QueryParam ("email")String email) throws Exception{
			 Connection connection = DBClass.returnConnection();
			 String mail = email.replace("%40", "@");
			 PreparedStatement ps = connection.prepareStatement(
	                 "SELECT * FROM groups where admin= ?"
	                 );

	         ps.setString(1,mail);
			 ResultSet resultSet = ps.executeQuery();
			 ToJSON tojson = new ToJSON();
			 JSONArray jsonArray = tojson.toJSONArray(resultSet);
			 String output = jsonArray.toString();
			 if(output.equals("[]"))
				 return Response.status(412).build().toString(); 
			 return output;
		 }
		
		@GET
		 @Path("showgroups")
		 @Produces(MediaType.APPLICATION_JSON)
		 public String showgroups() throws Exception{
			 Connection connection = DBClass.returnConnection();
			 PreparedStatement ps = connection.prepareStatement(
	                 "SELECT * FROM groups");
			 ResultSet resultSet = ps.executeQuery();
			 ToJSON tojson = new ToJSON();
			 JSONArray jsonArray = tojson.toJSONArray(resultSet);
			 String output = jsonArray.toString();
			 if(output.equals("[]"))
				 return Response.status(412).build().toString(); 
			 return output;
		 }

		@GET
		 @Path("groupmembers")
		 @Produces(MediaType.APPLICATION_JSON)
		public String getMembers(@QueryParam ("group")String group) throws Exception{
			 Connection connection = DBClass.returnConnection();
			 PreparedStatement ps = connection.prepareStatement(
					 "SELECT user_id FROM user_group WHERE group_id = ?"
					 );
			 ps.setString(1, group);
			 ResultSet resultSet = ps.executeQuery();
			 ToJSON tojson = new ToJSON();
			 JSONArray jsonArray = tojson.toJSONArray(resultSet);
			 
			 return jsonArray.toString();
		 }
}
