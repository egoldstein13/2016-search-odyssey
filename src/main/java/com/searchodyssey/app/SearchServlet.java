package com.searchodyssey.app;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Iterator;

import com.searchodyssey.app.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.servlet.RequestDispatcher;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.BufferedReader;

import java.net.URLEncoder;

import redis.clients.jedis.Jedis;
 
@WebServlet("/Search")
public class SearchServlet extends HttpServlet {
 
    protected void doPost(HttpServletRequest request,
			  HttpServletResponse response) throws ServletException, IOException{

        String search = request.getParameter("search");
      	// make a JedisIndex
       	Jedis jedis = JedisMaker.make();
        JedisIndex index = new JedisIndex(jedis);
	
        WikiSearch search1 = WikiSearch.search(search, index);
        
        List<Entry<String, Integer>> entries = search1.sort();

        String json = listmap_to_json_string(entries);

        request.setAttribute("json",json);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/SearchResults");
          requestDispatcher.forward(request, response);
        
    }
    
    public String listmap_to_json_string(List<Entry<String, Integer>> list)
    {       
        JSONArray json_arr=new JSONArray();
        int i=1;
        if(list==null || list.size()==0){
            JSONObject json_obj=new JSONObject();
            try {
                json_obj.put("count", "");
                json_obj.put("text","No results! :(");
            } catch (JSONException e) {
                e.printStackTrace();
            }
             json_arr.put(json_obj);
            return json_arr.toString();
        }
        for (Entry<String, Integer> entry : list) {
            JSONObject json_obj=new JSONObject();
            String key = entry.getKey();
                Integer value = entry.getValue();
            try {
                json_obj.put("count",value);
                json_obj.put("text",key);
            } catch (JSONException e) {
                e.printStackTrace();
            }                           
            json_arr.put(json_obj);
            if(++i>10)
            break;
        }
        return json_arr.toString();
    }
 
}
