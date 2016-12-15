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
	
	private User currentUser = new User();;

	
	@POST
	@Path("writeOnBoard")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void writeOnBoard(
            @FormParam("message") String message,
    		@FormParam("group") String group) throws SQLException
	{
		Connection connection = DBClass.returnConnection();
		PreparedStatement ps = connection.prepareStatement("INSERT INTO board (group_id, message)" + "VALUES(?,?)");
		ps.setString(1,group);
		ps.setString(2,currentUser.getFirstname() + " " + currentUser.getLastname() + ": " + message);
		ps.executeUpdate();
	}
	
		//Ajoute un utilisateur dans la bdd à partir du json
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
		
		//Ajoute un groupe dans la bdd à partir du json
		@POST
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
		
		//Ajoute un user dans la bdd à patir d'un user
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
		
		//Ajoute un groupe dans la bdd à patir d'un user
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
		
		//Ajouter un user dans la bdd à partir d'un formulaire
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
		
		//Supprimer son propre compte (via bouton)
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
		
		//Ajouter un groupe dans la bdd à partir d'un formulaire
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
		
		//Changement du nom dans la bdd à partir d'un formulaire 
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
		
		//Changement du prenom dans la bdd à partir d'un formulaire
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
		
		//Changement de la biographie dans la bdd à partir d'un formulaire
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
		
		//Changement de la description du groupe via un formulaire// seulement si tu es admin(Fab')
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
		
		//Rejoindre un groupe 
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
		
		//Quitter un groupe via un formulaire + bouton
		@POST
		@Path("groups_v5")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void leaveGroup(@FormParam("name") String name) throws SQLException{
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement("DELETE FROM user_group WHERE group_id = ? AND user_id = ?");
			ps.setString(1,name);
			ps.setString(2,currentUser.getEmail());
			ps.executeUpdate();
		}
		
		//Renvoie sous format json tous les users
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
					
					JSONObject jsonObject = jsonArray.getJSONObject(0);
					String lastname = jsonObject.getString("lastname");
					String firstname = jsonObject.getString("firstname");
					String biography = jsonObject.getString("biography");
					String theEmail = jsonObject.getString("email");
					currentUser.setBiography(biography);
					currentUser.setFirstname(firstname);
					currentUser.setLastname(lastname);
					currentUser.setEmail(mail);
					
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
