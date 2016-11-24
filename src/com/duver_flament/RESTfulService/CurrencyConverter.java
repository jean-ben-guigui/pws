package com.duver_flament.RESTfulService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
public class CurrencyConverter {
	public static String version="1";
	private static List<Currency> currencyList = new ArrayList<Currency>();
	
	/*------------------------Code Block Number 1-----------------------*/
	@GET
	@Path("version")
	public String version()
	{
	return "The current version is " +this.version;
	}
	
	private static void initializeCurrencies()
	{
	currencyList.add(new Currency("Japan", "Yen",1945,1));
	currencyList.add(new Currency("USA", "Dollar", 1800,2));
	currencyList.add(new Currency("EU","Euro",2000,3));
	}
	
	@POST
	@Path("offices_v2")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addOffice_v2(String incomingData) throws JSONException
	{
		JSONObject jsonObject = new JSONObject(incomingData);
		String city = jsonObject.getString("city");
		String manager = jsonObject.getString("manager_name");
		String email = jsonObject.getString("email");
		int year = jsonObject.getInt("year_founded");
		Office offist = new Office(city,manager,year,email);
		int http_code = InsertOfficeIntoTheDataBase(offist);
		if (http_code==200) {
		return Response.ok().build();
		}
		else
		{
		return Response.serverError().build();
		}
	}
	
	public int InsertOfficeIntoTheDataBase(Office office) 
	{
		Connection connection = DBClass.returnConnection(); 
		PreparedStatement insert;
		try {
			insert = connection.prepareStatement("INSERT INTO office (city, manager_name,email, year_founded)" + "VALUES(?,?,?,?)");
			insert.setString(1, office.getCity());
			insert.setString(2, office.getManager_name());
			insert.setString(3, office.getEmail());
			insert.setInt(4, office.getYear_founded());
			insert.executeUpdate();
			return 200;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 400;
		}
	}


	
	@POST
	@Path("offices")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public String addOffice(@FormParam("city") String city,
            @FormParam("manager") String manager,
            @FormParam("email") String email,
            @FormParam("year founded") int year) throws SQLException
	{
		Connection connection = DBClass.returnConnection();
		PreparedStatement ps = connection.prepareStatement("INSERT INTO office " + "(city,manager_name,email,year_founded)" + "VALUES (?, ?, ?, ?)");
		ps.setString(1,city);
		ps.setString(2,manager);
		ps.setString(3,email);
		ps.setFloat(4,year);
		ps.executeUpdate();
		return "";
	}

	
	 @GET
	 @Path("offices")
	 @Produces(MediaType.APPLICATION_JSON)
	 public String getOffices() throws Exception{
		 Connection connection = DBClass.returnConnection();
		 PreparedStatement ps = connection.prepareStatement(
				 "SELECT * FROM office"
				 );
		 ResultSet resultSet = ps.executeQuery();
		 ToJSON tojson = new ToJSON();
		 JSONArray jsonArray = tojson.toJSONArray(resultSet);
		 
		 return jsonArray.toString();
	 }
	 
	 @GET
	 @Path("office")
	 @Produces(MediaType.APPLICATION_JSON)
	 public Response getOffice(@QueryParam ("city")String city) throws Exception{
		 Connection connection = DBClass.returnConnection();
		 PreparedStatement ps = connection.prepareStatement(
                 "SELECT * FROM office WHERE city=?"
                 );

         ps.setString(1,city);
		 ResultSet resultSet = ps.executeQuery();
		 ToJSON tojson = new ToJSON();
		 JSONArray jsonArray = tojson.toJSONArray(resultSet);
		 String output = jsonArray.toString();
		 if(output.equals("[]"))
			 return Response.status(412).build();
		 return Response.status(200).entity(output).build();
	 }
	
	 
	
	@GET
	@Path("currency/{id}")
	public String getUserById(@PathParam("id") String id) {
		if (currencyList.isEmpty()){
			initializeCurrencies();
		}
		HashMap<Integer, String> map = new HashMap<>();
		for (Currency c : currencyList){
			map.put(c.getId(), c.getName());
		}
		
	   if (map.get(Integer.parseInt(id))==null){
		   return "Erreur : pas de monnaie à cet indice ! ";
	   }
	   return map.get(Integer.parseInt(id));

	}
	
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
		if (currencyList.isEmpty()){
			initializeCurrencies();
		}
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
	
	
	@GET
	@Path("currencies")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCurrenciesJSON(@QueryParam("sortedYN") String sortedYN){
		if (currencyList.isEmpty()){
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
	}
}
