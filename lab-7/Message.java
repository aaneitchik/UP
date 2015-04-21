import org.json.simple.JSONObject;

public class Message {
    	private String id;
    	private String message;
    	private String userName;
    	private String state;
    	private static int counter = 0;
	
    	Message() {
    		++counter;
    		userName = "User " + counter;
		}
    	
    	Message(String id) {
    		this.id = id;
        }
    	
    	Message(String userName, String message) {
    		this.id = String.valueOf(++counter);
    		this.userName = userName;
    		this.message = message;
    		this.state = "standard";
    	}
    	
    	Message(String id, String userName, String message) {
    		this.id = id;
    		this.userName = userName;
    		this.message = message;
    		this.state = "standard";
    	}
    	
    	Message(String id, String userName, String message, String state) {
    		this.id = id;
    		this.userName = userName;
    		this.message = message;
    		this.state = state;
    	}
	
    	Message(JSONObject json){
            this.id = (String)json.get("id");
            this.userName = (String)json.get("userName");
            this.message = (String)json.get("message");
            this.state = (String)json.get("state");
    	}
    	
    	String getId() {
    		return id;
    	}

    	String getUserName() {
    		return userName;
    	}
    	
    	String getState() {
    		return state;
    	}
    	
    	String getMessage() {
    		return message;
    	}
    	
    	void setId(int id) {
    		this.id = String.valueOf(id);
    	}

    	void setUserName(String userName) {
    		this.userName = userName;
    	}
    	
    	void setState(String state) {
    		this.state = state;
    	}
        
        void setMessage(String message) {
    		this.message = message;
    	}

     	public static Message getEditedMessage(JSONObject object) {
     		Message result = new Message();
     		result.id = (String) object.get("id");
     		result.message = (String) object.get("message");
     		result.userName = (String) object.get("userName");
     		result.state = (String) object.get("state");
     		return result;
     	}     	
	}