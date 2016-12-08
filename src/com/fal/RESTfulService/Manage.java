package com.fal.RESTfulService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
import com.sun.corba.se.spi.orbutil.fsm.State;
import com.sun.msv.datatype.xsd.Comparator;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Path("manage")
public class Manage {
	
	private User currentUser = new User();
	
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
		
		@POST
		@Path("users_v1")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public Response addUser(
				@FormParam("email") String email,
	            @FormParam("lastname") String lastname,
	            @FormParam("firstname") String firstname,
	            @FormParam("biography") String biography) throws SQLException
		{
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement("INSERT INTO user (email,lastname,firstname,biography)" + "VALUES(?,?,?,?)");
			ps.setString(1,email);
			ps.setString(2,lastname);
			ps.setString(3,firstname);
			ps.setString(4,biography);
			ps.executeUpdate();
			
			java.net.URI location;
			try {
				//location = new java.net.URI("manage/user?email="+email);
				location = new java.net.URI("../../index.html");
				return Response.seeOther(location).build();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //return Response.temporaryRedirect(location).build();
			try {
				getUser(email);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response.status(Status.ACCEPTED).build();
		}
		
		@POST
		@Path("groups_v1")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public String addGroup(
				@FormParam("name") String name,
	            @FormParam("description") String description
	            ) throws SQLException
		{
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement("INSERT INTO group (name,description)" + "VALUES(?,?)");
			ps.setString(1,name);
			ps.setString(2,description);
			//ps.setString(3,admin); TROUVER UN MOYEN DE TROUVER L'ADMIN AUTOMATIQUEMENT SANS LE RENTRER
			ps.executeUpdate();
			return "";
		}
		
		@PUT
		@Path("users_v2")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void changeLastnameUser(
				@FormParam("lastname") String lastname
	            ) throws Exception
		{
			
			Connection connection = DBClass.returnConnection();
			//emailduUser = email du user connecté
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE user" 
					+ "SET lastname=?"
					+ "WHERE email=?");
			ps.setString(1,lastname);
			//ps.setString(2,emailduUser);
			ps.executeUpdate();
			//return "";
		}
		
		@PUT
		@Path("users_v3")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void changeFirstnameUser(
	            @FormParam("firstname") String firstname
	            ) throws Exception
		{
			
			Connection connection = DBClass.returnConnection();
			//emailduUser = email du user connecté
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE user" 
					+ "SET firstname=?"
					+ "WHERE email=?");
			ps.setString(1,firstname);
			//ps.setString(2,emailduUser);
			ps.executeUpdate();
			//return "";
		}
		
		@PUT
		@Path("users_v4")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void changeBiographyUser(
	            @FormParam("biography") String biography
	            ) throws Exception
		{
			
			Connection connection = DBClass.returnConnection();
			//emailduUser = email du user connecté
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE user" 
					+ "SET biography=?"
					+ "WHERE email=?");
			ps.setString(1,biography);
			//ps.setString(2,emailduUser);
			ps.executeUpdate();
			//return "";
		}
		
		@POST
		@Path("groups_v2")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public String changeGroupDescription(
				@FormParam("name") String name,
	            @FormParam("description") String newDescription
	            ) throws Exception
		{
			
			Connection connection = DBClass.returnConnection();
			
			PreparedStatement ps_admin = connection.prepareStatement(
					"SELECT admin FROM group WHERE name="+name
			);
			 ResultSet resultSet = ps_admin.executeQuery();
			 ToJSON tojson = new ToJSON();
			 JSONArray jsonArray = tojson.toJSONArray(resultSet);
			 /*boolean isAdmin;
			 for (int i = 0; i < jsonArray.length(); i++) {
				   if(jsonArray.get(i).equals(name))
					   isAdmin=true;
			}*/
			 
			String adminGroup = jsonArray.toString();
			//if(adminGroup == à l utilisateur connecté) utiliser la fonction getUserLog()
			PreparedStatement ps = connection.prepareStatement(
					"UPDATE group" 
					+ "SET description=?"
					+ "WHERE name=?");
			ps.setString(1,newDescription);
			ps.setString(2,name);
			ps.executeUpdate();
			return "";
		}
		
		@POST
		@Path("groups_v3")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public String joinGroup(
				@FormParam("groupe_name") String grName
	            ) throws Exception
		{
			Connection connection = DBClass.returnConnection();
			String uName = null; //utiliser la fonction getUserLogin()
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO group (members)" + 
					"VALUES(?)" + 
					"WHERE name=" + grName);
			ps.setString(1,uName);
			return "";
		}

		public void deleteGroup(@FormParam("name") String name) throws SQLException
		{
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement("DELETE FROM group WHERE name = ?");
			ps.setString(1,name);
			//ps.setString(3,admin); TROUVER UN MOYEN DE TROUVER L'ADMIN AUTOMATIQUEMENT SANS LE RENTRER
			ps.executeUpdate();
			PreparedStatement psbis = connection.prepareStatement("DELETE FROM user_group WHERE id_group = ?");
			ps.setString(1,name);
			//ps.setString(3,admin); TROUVER UN MOYEN DE TROUVER L'ADMIN AUTOMATIQUEMENT SANS LE RENTRER
			psbis.executeUpdate();
		}
		
		public void leaveGroup(@FormParam("name") String name) throws SQLException{
			Connection connection = DBClass.returnConnection();
			PreparedStatement ps = connection.prepareStatement("DELETE FROM user_group WHERE id_group = ? AND id_user= ?");
			ps.setString(1,name);
			//ps.setString(2,admin); TROUVER UN MOYEN DE TROUVER L'ADMIN AUTOMATIQUEMENT SANS LE RENTRER
			ps.executeUpdate();
		}
		
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
			 String output = jsonArray.toString();
			 if(output.equals("[]"))
				 return Response.status(412).build();
			 return Response.status(200).entity(output).build();
		 }

}
