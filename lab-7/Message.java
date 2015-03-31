import org.json.simple.JSONObject;

public class Message {
    	private String id;
    	private String message;
    	private String userName;
    	private static int counter = 0;
	
    	Message() {
    		++counter;
    		userName = "User " + counter;
		}
    	
    	Message(String userName, String message) {
    		this.id = String.valueOf(++counter);
    		this.userName = userName;
    		this.message = message;
    	}
    	
    	Message(String id, String userName, String message) {
    		this.id = id;
    		this.userName = userName;
    		this.message = message;
    	}
	
    	Message(JSONObject json){
            this.id = (String)json.get("id");
            this.userName = (String)json.get("userName");
            this.message = (String)json.get("message");
    	}
    	
    	String getId() {
    		return id;
    	}

    	String getUserName() {
    		return userName;
    	}
    	
    	void setId(int id) {
    		this.id = String.valueOf(id);
    	}

    	void setUserName(String userName) {
    		this.userName = userName;
    	}
    	
    	String getMessage() {
    		return message;
    	}

        Message(String id) {
    		this.id = id;
        }
        
        void setMessage(String message) {
    		this.message = message;
    	}

     	public static Message getEditedMessage(JSONObject object) {
     		Message result = new Message();
     		result.id = (String) object.get("id");
     		result.message = (String) object.get("message");
     		result.userName = (String) object.get("userName");
     		return result;
     	}     	
	}