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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.duver_flament.model.Currency;
import com.duver_flament.model.Office;
import com.fal.model.Group;
import com.fal.model.User;
import com.rest.DB.DBClass;
import com.rest.util.ToJSON;
import com.sun.corba.se.spi.orbutil.fsm.State;
import com.sun.msv.datatype.xsd.Comparator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Path("currencyConverter")
public class Manage {
		
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
		public String addUser(
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
			return "";
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
		 @Path("office")
		 @Produces(MediaType.APPLICATION_JSON)
		 public Response getUser(@QueryParam ("email")String email) throws Exception{
			 Connection connection = DBClass.returnConnection();
			 PreparedStatement ps = connection.prepareStatement(
	                 "SELECT * FROM user WHERE email=?"
	                 );

	         ps.setString(1,email);
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
			   return "Erreur : pas de monnaie � cet indice ! ";
		   }
		   return map.get(Integer.parseInt(id));

		}*/
		
		@GET
		@Path("conversion/{source}/{destination}/{amount}")
		public double convert (@PathParam("source")String source, @PathParam("destination")String destination,
				@PathParam("amount")double amount) {
			 
			double result = 0;
			
			if(source.equals("D")){
				if (destination.equals("E"))
					result = amount * 0.910;
				else if (destination.equals("Y"))
					result = amount * 	104.135;
			}
			
			else if(source.equals("E")){
				if (destination.equals("D"))
					result = amount * 1.098;
				else if (destination.equals("Y"))
					result = amount * 	114.425;
			}

			else if(source.equals("Y")){
				if (destination.equals("D"))
					result = amount * 0.010;
				else if (destination.equals("E"))
					result = amount * 	0.009;
			}
			
			return result;
	}
		
		@GET
		@Path("currencies")
		@Produces(MediaType.TEXT_XML)
		public String getCurrenciesXML(@QueryParam("sortedYN") String sortedYN){
			/*if (currencyList.isEmpty()){
				initializeCurrencies();
			}*/
			List<Currency> listResult = new ArrayList<>();
			/*for(Currency c : currencyList){
				listResult.add(c);
			}*/
			if (sortedYN.equals("y")){
				
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