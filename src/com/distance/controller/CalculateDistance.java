package com.distance.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
 
/*
 * author: Pawan Saxena
 * 
 */
 
@RestController
public class CalculateDistance {
	@RequestMapping(value="/calculateDistance",method={RequestMethod.GET,RequestMethod.POST})
	public ModelAndView helloWorld(@RequestParam Map<String,String> requestParams,HttpServletRequest request, 
	        HttpServletResponse response) throws IOException {
		String distance;
		String duration;
		String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+requestParams.get("origin")+"&destinations="+requestParams.get("destination")+"&mode=driving&key=AIzaSyAYVMaJcFvFAqpaCiwjn1tZlgqhY7N3y08";
		System.out.println(url);
			URL object = new URL(url);

			String result=null;
			HttpURLConnection connection = (HttpURLConnection) object
					.openConnection();

			connection.setDoOutput(true);
		
			
			System.out.println("Response Code: " + connection.getResponseCode());
			System.out.println("Response Message: " + connection.getResponseMessage());
			System.out.println("Response Code: " + connection.getContent());
			

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String inputData;
			
			StringBuffer output = new StringBuffer();
			while ((inputData = reader.readLine()) != null) {
				output.append(inputData);
			}
			if(output==null||output.equals(""))
			{
				result="please enter valid addresses";
			}
			else
			{
				JSONObject json=new JSONObject(output.toString());
				System.out.println(json);
					JSONArray rowsArray = json.getJSONArray("rows"); 
					JSONObject locArrObj = rowsArray.getJSONObject(0); 
					JSONArray conferenceLocArr = locArrObj.getJSONArray("elements");
					JSONObject temp=conferenceLocArr.getJSONObject(0);
					JSONObject distanceObject = temp.getJSONObject("distance");
					JSONObject durationObject = temp.getJSONObject("duration");
					distance=distanceObject.getString("text");
					duration=durationObject.getString("text");
					result="<h3>Distance is : </h3>"+distance+" <br><h3>Duration of travelling is : </h3>"+duration;
				
			}
			
			reader.close();
			if(result!=null && !(result.equals("")))
				return new ModelAndView("welcome", "message", result);
			else
				return new ModelAndView("welcome", "message", "please enter valid addresses");
	}
}