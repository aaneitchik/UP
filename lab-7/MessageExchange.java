import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MessageExchange {

    private JSONParser jsonParser = new JSONParser();

    public String getToken(int index) {
        Integer number = index * 8 + 11;
        return "TN" + number + "EN";
    }

    public int getIndex(String token) {
        return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
    }

    public String getServerResponse(List<Message> messages) {
    	 List<JSONObject> messageList = new ArrayList<JSONObject>();
         for (Message m: messages) {
             JSONObject jsonObject = new JSONObject();
             jsonObject.put("id", m.getId());
             jsonObject.put("userName", m.getUserName());
             jsonObject.put("message", m.getMessage());
             messageList.add(jsonObject);
         }
         JSONObject jsonObject = new JSONObject();
         jsonObject.put("messages", messageList);
         jsonObject.put("token", getToken(messageList.size()));
         return jsonObject.toJSONString();
    }

    public String getClientSendMessageRequest(Message m) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", m.getId());
        jsonObject.put("userName", m.getUserName());
        jsonObject.put("message", m.getMessage());
        return jsonObject.toJSONString();
    }
   
    public Message getClientMessage(InputStream inputStream) throws ParseException {
    	JSONObject obj = getJSONObject(inputStreamToString(inputStream));
    	return new Message(obj);
    }

    public JSONObject getJSONObject(String json) throws ParseException {
        return (JSONObject) jsonParser.parse(json.trim());
    }

    public String inputStreamToString(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(baos.toByteArray());
    }

    public Message getEditedMessage(InputStream in) throws ParseException {
    	return Message.getEditedMessage( getJSONObject( inputStreamToString( in ) ) );
    }
	
}
