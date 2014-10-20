package com.example.tag4reeltest;

public class StubSupplier {

	
	
	
	
	public static String queryForCatchesJSON() {
		
		String catchesJSON = "{ \"catches\": [ { \"angler\": \"Jimmy\", \"fish\": \"salmon\"   }, { \"angler\": \"Sawyer\", \"fish\": \"octopus\"   }, { \"angler\": \"Adit\", \"fish\": \"shark\"   }, { \"angler\": \"David\", \"fish\": \"whale\"   }   ], \"success\": 1   }";
		
		return catchesJSON;
	}
	
	public static String queryForCatchesJSONWithSleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return queryForCatchesJSON();
	}
}
