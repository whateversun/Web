//Shangyu Sun
//2016-5-26
//This program outputs the 5 youngest users with valid us telephone numbers sorted by name
//Assumption: Valid phone number format: ########## or ###-###-####				
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;  
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.*;
import java.util.regex.Pattern;

public class AppSheet {
	public static String detail = "https://appsheettest1.azurewebsites.net/sample/detail/";
	public static int size = 5;
	
	public static void main(String[] args) throws Exception { 
		ArrayList<Integer> haha = getUserId(); // get all user id's
		int[] sortedId = getSortedId(haha); // get the five youngest users' id's who have valid us phone number
		Map<String, String> detailInfo = getInfo(sortedId); //Map the five users' names to their phone numbers
		String[] names = getSortedNames(sortedId);// get the five youngest users' names in sorted order
		for(int i = 0; i < size; i++){ //print out the results in the form of "names : phone numbers"
			System.out.println(names[i] + " : " + detailInfo.get(names[i]));
		}
	}
	
	//Put the user names in sorted order
	public static String[] getSortedNames(int[] id) throws JSONException, Exception {
		String[] names = new String[size];
		for(int i = 0; i < size; i++){ 
			names[i] = getName(getContent(detail + id[i]));
		}
		Arrays.sort(names); 
		return names;
	}
	
	//check if a phone number is valid
	//Valid phone numbers formats:
	// 	########## or ###-###-####
	public static boolean getValidness(String number) {                
		String format1 = "^[0-9]{10}$"; //##########
		String format2 = "^[0-9]{3}\\-?[0-9]{3}\\-?[0-9]{4}$"; //###-###-####
	    return Pattern.matches(format1, number) || Pattern.matches(format2, number);
	}
	
	//Map user names to their phone numbers
	public static Map<String, String> getInfo(int[] id) throws Exception{
		Map<String, String>info = new HashMap<String, String>();
		for(int i = 0; i < id.length; i++){
			info.put(getName(getContent(detail + id[i])), getPhoneNum(getContent(detail + id[i])));
		}
		return info;
	}
	
	//Get 5 youngest users id with valid us phone numbers
	public static int[] getSortedId(ArrayList<Integer> users) throws JSONException, Exception{
		Map<Integer, Integer> iAndA = new HashMap<Integer, Integer>();
		for(int i = 0; i < users.size(); i++){
			if(getValidness(getPhoneNum(getContent(detail + users.get(i))))){
				iAndA.put(getAge(getContent(detail + users.get(i))), users.get(i));
			}
		}
		Integer[] sorted = iAndA.keySet().toArray(new Integer[iAndA.keySet().size()]);
		Arrays.sort(sorted);
		int[] sortedId = new int[size];
		for(int i = 0; i < size; i++){
			sortedId[i] = iAndA.get(sorted[i]);
		}
		return sortedId;
	} 
	
	//Get all user id's
	public static ArrayList<Integer> getUserId() throws Exception{
		ArrayList<Integer> allId = new ArrayList<Integer>();
		String fixedList = "https://appsheettest1.azurewebsites.net/sample/list?token=";
		String list = "https://appsheettest1.azurewebsites.net/sample/list";
		int[] finalV = getTenId(getContent(list));
		for(int i = 0; i < finalV.length; i++){
			allId.add(finalV[i]);
		}
		while (getContent(list).toString().contains("token")){
			String userToken = getToken(getContent(list));
			list = fixedList + userToken; 
			finalV = getTenId(getContent(list));
			for(int i = 0; i < finalV.length; i++){
				allId.add(finalV[i]);
			}
		}
		return allId;
	}
	
	//Get the id of ten users
	public static int[] getTenId (JSONObject user) throws JSONException{
		int length = user.getJSONArray("result").length();
		int[] ids = new int[length];
		for(int i = 0; i < length; i++){
			ids[i] = user.getJSONArray("result").getInt(i);
		}
	    return ids;
	}
	
	//Get the token for another 10 or less users
	public static String getToken (JSONObject user) throws JSONException{
		return user.getString("token");
	}
	
	//Get the age of the user
	public static int getAge (JSONObject user) throws JSONException{
		return user.getInt("age");
	}
	
	//Get the phone number of the user
	public static String getPhoneNum (JSONObject user) throws JSONException{
		return user.getString("number");
	}
	
	//Get the name of the user
	public static String getName (JSONObject user) throws JSONException{
		return user.getString("name");
	}
	
	//Get the content of a URL and store it as a JSON object
	public static JSONObject getContent(String strUrl) throws Exception{
		URL url = new URL(strUrl);  
	    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();  
	    InputStreamReader input = new InputStreamReader(httpConn  
	            .getInputStream(), "utf-8");  
	    BufferedReader bufReader = new BufferedReader(input); 
	    String line = "";  
	    StringBuilder contentBuf = new StringBuilder();  
	    while ((line = bufReader.readLine()) != null) {  
	        contentBuf.append(line);  
	    }
	    String returned = contentBuf.toString();
	    JSONObject content = new JSONObject(returned);
	    
	    return content;
	}
}
