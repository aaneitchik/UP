import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.parser.ParseException;
import java.util.Calendar;      
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.io.*;    
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Server implements HttpHandler {
    private List<Message> history = new ArrayList<Message>();
    private MessageExchange messageExchange = new MessageExchange();

    static PrintWriter outServer, outClient;
    static Calendar calendar;   
    static String s;
    static Date now;
    static DateFormat formatter;
    static FileWriter fileWriter;    
    static String fileName = "serverlog.txt";

    public static void main(String[] args) {
    	try {
        	outServer = new PrintWriter(new FileWriter(new File("serverlog.txt")));
    	} 
        catch (Exception exception) {
        	exception.printStackTrace();
        } finally {
        	if (outServer != null) {
				outServer.close();
			}
        }        
    	if (args.length != 1) {
            myPrint("Usage: java Server port");
    	}	
        else {
            try {
                myPrint("Server is starting...");
                Integer port = Integer.parseInt(args[0]);
                HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
                myPrint("Server started.");
                String serverHost = InetAddress.getLocalHost().getHostAddress();
                myPrint("Get list of messages: GET http://" + serverHost + ":" + port + "/chat?token={token}");
     	        myPrint("Post message: POST http://" + serverHost + ":" + port + "/chat provide body json in format {\"message\" : \"{message}\"} ");
                myPrint("Deleted message by ID: DELETE http://" + serverHost + ":" + port + "/chat?id={id}");
                myPrint("Edit message by ID: PUT http://" + serverHost + ":" + port + "/chat?id={id}" + " provide body json in format {\"message\" : \"{message}\"} " );
                server.createContext("/chat", new Server());
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
            	myPrint(s + "Error creating http server: " + e);
            }     
        }
    }

    public static void myPrint(String S) {
    	System.out.println(S);
    	now = new Date();    
    	formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
     	s = formatter.format(now);
     	try {
     		FileWriter sw = new FileWriter(fileName, true); 
       		sw.write(s + " " + S + "\n");   
	        sw.close();
        } catch(Exception e){ 
	        System.out.print(e.getMessage());  
        }       
    }


    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        if ("GET".equals(httpExchange.getRequestMethod())) {
            response = doGet(httpExchange);
        } else if ("POST".equals(httpExchange.getRequestMethod())) {
            doPost(httpExchange);
        } else if ("DELETE".equals(httpExchange.getRequestMethod())) {
        	doDel(httpExchange);
        } else if ("PUT".equals(httpExchange.getRequestMethod())) {
        	doPut(httpExchange);
        } else {
            response = "Unsupported http method: " + httpExchange.getRequestMethod();
        }
        sendResponse(httpExchange, response);
    }

    private String doGet(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null) {
            Map<String, String> map = queryToMap(query);
            String token = map.get("token");
            if (token != null && !"".equals(token)) {
                int index = messageExchange.getIndex(token);
                return messageExchange.getServerResponse(history.subList(index, history.size()));
            } else {
                return "Token query parameter is absent in url: " + query;
            }
        }
        return  "Absent query in url";
    }

    private void doPost(HttpExchange httpExchange) {
        try {
            Message message = messageExchange.getClientMessage(httpExchange.getRequestBody());
            if (!message.getMessage().equals("")) {
                message.setId(history.size() + 1);
                myPrint("GetMessage from " + message.getUserName() + ": " + message.getMessage());
                history.add(message);
            }
        } catch (ParseException e) {
            System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
        }
    }

    private void doPut(HttpExchange httpExchange) {
    	String query = httpExchange.getRequestURI().getQuery();
    	if (query != null) {
    		Map<String, String> map = queryToMap(query);
            String id = map.get("id");
            myPrint("PUT request: id = "+id+" received");
            if (id != null && !"".equals(id)) {
            	int index = Integer.parseInt(id);
                int ind = 0;
                while (ind < history.size() && Integer.parseInt(history.get(ind).getId()) != index) {
                	++ind;
                }
                if(ind < history.size()) {
                	try {          
                		Message message = messageExchange.getClientMessage(httpExchange.getRequestBody());   
                		if (history.get(ind).getMessage().length() == 0) {
                			myPrint("This message can't be edited, it was deleted");
                			return;    
                		}
                		history.get(ind).setMessage(message.getMessage());     
                		myPrint("Message was successfully edited");
                	} catch (ParseException e) {
                		System.err.println("Invalid user message: " + httpExchange.getRequestBody() + " " + e.getMessage());
                	}	
                } else {
                	myPrint("Message ID is out of bounds");
                }
            }
        }
    }
 
     private void doDel(HttpExchange httpExchange) {
    	String query= httpExchange.getRequestURI().getQuery();
    	if (query != null) {
            Map<String, String> map = queryToMap(query);
            String id = map.get("id");
            myPrint("DELETE request: id = "+id+" received");
            if (id != null && !"".equals(id)) {
                int index = Integer.parseInt(id);
                int ind = 0;
                while (ind < history.size() && Integer.parseInt(history.get(ind).getId()) != index) {
                	++ind;
                }
                if(ind < history.size() ) {
                	//history.remove(index - 1);
                	//history.add(new Message(Integer.toString(index + 1), "", ""));
                	history.remove(ind);
                	history.add(new Message(Integer.toString(index), "", ""));
                	myPrint("Message was successfully deleted");
                } else {
                	myPrint("Message ID is out of bounds");
    			}
            }
        } 
    }
 
    private void sendResponse(HttpExchange httpExchange, String response) {
         try {
             byte[] bytes = response.getBytes();
             Headers headers = httpExchange.getResponseHeaders();
             headers.add("Access-Control-Allow-Origin","*");
             httpExchange.sendResponseHeaders(200, bytes.length);
             OutputStream os = httpExchange.getResponseBody();
             os.write( bytes);
             os.flush();
             os.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
 
     
    private Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length > 1) {
                result.put(pair[0], pair[1]);
            } else {
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
