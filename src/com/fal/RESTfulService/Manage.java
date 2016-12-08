package com.fal.RESTfulService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import java.io.IOException;
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
	@Path("sign-in")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void signIn(
			@FormParam("email") String email) throws SQLException, IOException
	{
		Connection connection = DBClass.returnConnection();
		PreparedStatement ps = connection.prepareStatement("SELECT email FROM user where email = ?");
		ps.setString(1,email);
		ResultSet rs = ps.executeQuery();
		if(rs!=null){
			
		}
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
			return Response.status(Status.ACCEPTED).build();
		}
		
		
		//Ajouter un groupe dans la bdd à partir d'un formulaire
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
		
		
		//Changement du nom dans la bdd à partir d'un formulaire 
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
		
		
		//Changement du prenom dans la bdd à partir d'un formulaire
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
		
		
		//Changement de la biographie dans la bdd à partir d'un formulaire
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
		
		
		//Changement de la description du groupe via un formulaire// seulement si tu es admin
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
		
		
		// Rejoindre un groupe via un formulaire
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

		
		//Supprimer un groupe via un formulaire + bouton
		@POST
		@Path("groups_v4")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		public void deleteGroup(@FormParam("name") String name) throws SQLException
		{
			
			Connection connection = DBClass.returnConnection();
			
			try {
				PreparedStatement ps0 = connection.prepareStatement("SELECT admin FROM groups WHERE name= ?");
				ps0.setString(1,name);
				ResultSet resultSet = ps0.executeQuery();
				ToJSON tojson = new ToJSON();
				JSONArray jsonArray;
				jsonArray = tojson.toJSONArray(resultSet);
				String output = jsonArray.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			PreparedStatement ps = connection.prepareStatement("DELETE FROM group WHERE name = ?");
			ps.setString(1,name);
			//ps.setString(3,admin); TROUVER UN MOYEN DE TROUVER L'ADMIN AUTOMATIQUEMENT SANS LE RENTRER
			ps.executeUpdate();
			PreparedStatement psbis = connection.prepareStatement("DELETE FROM user_group WHERE id_group = ?");
			psbis.setString(1,name);
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
			 String output = jsonArray.toString();
			 if(output.equals("[]"))
				 return Response.status(412).build();
			 return Response.status(200).entity(output).build();
		 }
		
		/*@GET
		@Path("currency/{id}")
		public String getUserById(@PathParam("id") String id) {
			
			/*List<JsonObject> = 
			
			
		
			HashMap<Integer, String> map = new HashMap<>();
			for (Currency c : userList){
				map.put(c.getId(), c.getName());
			}
			
		   if (map.get(Integer.parseInt(id))==null){
			   return "Erreur : pas de monnaie à cet indice ! ";
		   }
		   return map.get(Integer.parseInt(id));

		}*/
		
		
		
		/*@GET
		@Path("currencies")
		@Produces(MediaType.TEXT_XML)
		public String getCurrenciesXML(@QueryParam("sortedYN") String sortedYN){
			/*if (currencyList.isEmpty()){
				initializeCurrencies();
			}*/
			//List<Currency> listResult = new ArrayList<>();
			/*for(Currency c : currencyList){
				listResult.add(c);
			}*/
			/*if (sortedYN.equals("y")){
				
				Collections.sort(listResult, new java.util.Comparator<Currency>() {

					@Override
					public int compare(Currency c1, Currency c2) {
						
						return c1.getName().compareTo(c2.getName());
					}
				}); 
			}
			String xml="";
			xml="<?xml version=\"1.0\"?>"
					+"<Currencies>";
			for(Currency c : listResult){
				xml+="<Currency>"
						+ "<Country>"+c.getCountry()+"</Country>"
						+ "<Name>" + c.getName()+"</Name>"
						+ "<YearAdopted>"+c.getYearAdopted()+"</YearAdopted>"
						+	"<Id>"+c.getId()+"</Id>"
					+ "</Currency>";
			}
			xml+="</Currencies>";
			System.out.println(xml);
			return xml;
		}
		
		
		/*@GET
		@Path("currencies")
		@Produces(MediaType.APPLICATION_JSON)
		public String getCurrenciesJSON(@QueryParam("sortedYN") String sortedYN){
			/*if (currencyList.isEmpty()){
				initializeCurrencies();
			}
			String json="";
			List<Currency> listResult = new ArrayList<>();
			for(Currency c : currencyList){
				listResult.add(c);
			}
			if (sortedYN.equals("y")){
				
				Collections.sort(listResult, new java.util.Comparator<Currency>() {

					@Override
					public int compare(Currency c1, Currency c2) {
						
						return c1.getName().compareTo(c2.getName());
					}
				}); 
			}
			json+="\"Currencies\":[";
			for(Currency c : listResult){
				json+="{\"Country\":\"" + c.getCountry() + "\", \"Name\":\""+c.getName()+"\",\"YearAdopted\":\""+c.getYearAdopted()+"\", \"Id\":\""+c.getId()+"\"},";
			}
			json=json.substring(0, json.length()-1);
			json+="]";
			System.out.println(json);
			return json;
		}*/
}
