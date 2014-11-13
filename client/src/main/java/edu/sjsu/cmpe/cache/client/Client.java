package edu.sjsu.cmpe.cache.client;

import java.util.List;
import java.util.ArrayList;
public class Client {

    public static void main(String[] args) throws Exception {
    	ArrayList<String> servers = new ArrayList<String>();
    	String serverA = "http://localhost:3000";
    	String serverB = "http://localhost:3001";
    	String serverC = "http://localhost:3002";
    	 servers.add(serverA);
         servers.add(serverB);
         servers.add(serverC);
        for( int i=0; i<servers.size(); i++ ) {
        	System.out.println("Server "+(i+1)+" : "+servers.get(i));
        }
        
        CacheServiceInterface cacheServer = new DistributedCacheService(
        		servers);
        String[] value = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        System.out.println("");
        System.out.println("Insert key value");
        System.out.println("{key => value}");
        for(int i=1; i<=value.length; i++) {
        	System.out.println("{ key : "+i+" => value : "+value[i-1]+" }");
        	cacheServer.put(i, value[i-1]);
        }
        
        System.out.println("");
        System.out.println("Get the key and value");
        System.out.println("{key => value}");

        for(int i=1; i<=value.length; i++) {
        	String outputValue = cacheServer.get(i);
        	System.out.println(i +" => " + outputValue);
        }
        System.out.println("Existing Cache Client...");
    }

}
