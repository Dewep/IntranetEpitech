package net.dewep.intranetepitech;


import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import android.os.Handler;

/*
 * 
 * Class created by Archibald - 08/2013
 * http://www.bigint.fr
 * 
 */


public class Netsoul implements Runnable {
	private static final String clientName = "Android IntranetEpitech";
	private static final double clientVersion = 0.1;
	private String		defaultHost = "ns-server.epita.fr";
	private int			defaultPort = 4242;
	private	Socket		client = null;
	BufferedReader		is = null;
	PrintWriter			pw = null;
	BufferedOutputStream os = null;
	private String		callbackfunc = null;
	private Thread		thread;
	private String		login = "";
	private String		pass = "";
	private String		location = "Android IntranetEpitech";
	private String		data = "Application par Aurélien Maigret - http://www.dewep.net";
	private Boolean		authenticated = false;
	private Boolean		shutdown = false;
	private Handler		handler = null;
	private int save_connect = 0;
	private String save_status = "Prêt";
	static Netsoul _instance = null;

	protected void status(int connect, String str)
	{
		save_connect = connect;
		save_status = str;
		//Log.d(String.valueOf(save_connect), save_status);
		handler.sendMessage(handler.obtainMessage(connect, str));
	}

	public static Netsoul getInstance() {
		if (_instance == null) {
			synchronized (Stock.class) {
				if (_instance == null) {
					//Log.d("instance", "new");
					_instance = new Netsoul();
				}
			}
		}
		return _instance;
	}

	public void getStatus()
	{
		//Log.d(String.valueOf(save_connect), save_status);
		handler.sendMessage(handler.obtainMessage(save_connect, save_status));
	}

	public void init(Handler act) {
		if (handler != null)
		{
			handler = act;
			return ;
		}
		handler = act;
		//Log.d("netsoul", "init");
		status(-1, "Initialisation...");
		System.out.println("INIT");
		callbackfunc = null;
		new Vector<String>();

		System.out.println("target: "+defaultHost+":"+defaultPort);
		
		makeGUI();
		status(-1, "Prêt.");
	}
	
	public void start(String data_login, String data_password) {
		System.out.println("START");
		status(0, "Connexion...");
		if ( thread == null ) {
			login = data_login;
			pass = data_password;
			System.out.println("Create background thread");
			thread = new Thread(this); 
			thread.setDaemon(true);
			thread.start();
		} 
		else {
			status(1, "Connecté.");
//			thread.resume();
//			thread.
		}
	}
	public void stop() {
		System.out.println("STOP");
		status(0, "Arrêt.");
//		thread.suspend();
	}
	public void destroy() {
		status(0, "Arrêt.");
		System.out.println("DESTROY");
		if ( thread != null ) {
			System.out.println("Stopping background thread");
			thread = null;
			authenticated = false;
			BufferedReader _is = is;
			is = null;
			shutdown = true;
			/*try {
				_is.close();
				//_is.notify();
				pw.close();
				//pw.notify();
				os.close();
				//os.notify();
			}
			catch(IOException e) {
				System.err.println("Error closing BufferedReader");
			}*/
//			synchronized (thread) {
				disconnectSocket(_is);
//			}
//			thread.notify();
			
		} 
	}
	

	
	private void makeGUI() {
		/*setLayout(new BorderLayout());
		setBackground(Color.lightGray);
		setVisible(true);
		
		add(new Label("bsoul", Label.CENTER));*/
	}
	
	private void	authenticate() {
		if (login == null || pass == null) {
			System.err.println("no login/pass provided");
			return ;
		}
		else if (authenticated)
			return ;
		connectSocket();
		
		String line = "";
		try {
			if (is != null)
				line = is.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			disconnectSocket(is);
			return ;
		}
		System.out.println("auth line: "+line);
		String[] params = line.split(" +");
		if (!params[0].equals("salut")) {
			System.err.println("protocol error: "+line);
			disconnectSocket(is);
			return ;
		}
		String nfd = params[1];
		String secret = params[2];
		String host = params[3];
		String port = params[4];
		String timestamp = params[5];
		//my ($salut, $nfd, $secret, $host, $port, $timestamp) = split(' ', $chain);
		
		pw.print("auth_ag ext_user none none\n");
		pw.flush();
		System.out.println("sent auth_ag challenge");
		
		try {
			line = is.readLine();
		} catch (IOException e1) {
			System.err.println("challenge read error");
			// TODO Auto-generated catch block
			e1.printStackTrace();
			disconnectSocket(is);
			return ;
		}
		if (!line.equals("rep 002 -- cmd end")) {
			System.err.println("protocol error: "+line);
			disconnectSocket(is);
			return ;
		}
		else
			System.out.println("protocol ok: "+line);
		
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ;
		}
		
		//byte[]	buffer = new byte[1024];
		//buffer.
		String buffer = secret+"-"+host+"/"+port+pass;
		md5.update(buffer.getBytes());
		byte[] hash_byte = md5.digest();
		
		String hash = "";
		for (int i=0; i < hash_byte.length; i++)
			hash += Integer.toString( ( hash_byte[i] & 0xff ) + 0x100, 16).substring(1);
		
		pw.print("ext_user_log "+login+" "+hash+" "+encode(location)+" "+encode(data)+"\n");
		pw.flush();
		
		System.out.println("sent auth "+"ext_user_log "+login+" '"+hash+"' "+encode(location)+" "+encode(data));
		
		try {
			line = is.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return ;
		}
		
		if (!line.equals("rep 002 -- cmd end")) {
			status(0, "Impossible de s'authentifier.");
			callAct("{\"error\":\"Authentication fail: "+line+"\"}");
			System.err.println("auth error: "+line);
			disconnectSocket(is);
			return ;
		}
		status(1, "Connecté.");
		//callAct("{\"success\":\"Authentication succeed\"}");
		callAct("{" +
				"\"action\":\"loggued\"," +
				"\"user\":{" +
					"\"id\": \""+nfd+"\"" +
				"}" +
			"}");
		System.out.println("auth ok: "+line);
		
		pw.print("state actif:"+timestamp+"\n");
		pw.print("user_cmd msg *:www@*"+clientName+"* msg version+"+clientVersion+"\n");
		pw.print("user_cmd who {"+login+"}\n");
		pw.print("user_cmd watch_log_user {"+login+"}\n");
		pw.flush();
		authenticated = true;
	}
	
	public void run() {
		System.out.println("run()");
//		synchronized(mutSend) {
		callAct("{\"status\": \"loaded\"}");
		
		while (true) {
			synchronized (thread) {
				
				authenticate();
				
				try {
					//synchronized (is) {
						try {
							while (authenticated == true && is != null) {
								//System.out.println("waiting for new line");
								//is.wait();
								try {
									String line = is.readLine();
									System.out.println("received line: "+line);
									dispatchReceivedLine(line);
								}
								catch(SocketException e) {
									System.err.println("SocketException ...");
									break ;
								}
								catch(SocketTimeoutException e) {
									System.err.println("Timeout while readline ...");
								}
							}
						}
						//catch(InterruptedException e) {
						//	System.err.println(e);
						//	e.printStackTrace();
						//}
						catch(IOException e) {
							System.err.println("IOException");
							e.printStackTrace();
						}
					//}
				}
				catch (NullPointerException e) {
					
				}
				if (shutdown == true)
					return ;

				try {
					System.out.println("wait for configuration");
					thread.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//System.out.println("---------------------------------");
					if (is != null) {
						System.err.println("configuration never received: "+e);
						e.printStackTrace();
					}
					else
						System.out.println("exiting loop");
					return ;
				}
				
				if (shutdown == true)
					return ;
				//disconnectSocket();
			}
		}
	}
	
	private void	dispatchReceivedLine(String line) {
		String[] args = line.split(" +");
		
		try {
			if (args[0].equals("ping")) {
				System.out.println("pong");
				pw.print("ping "+args[1]+"\n");
				pw.flush();
			}
			else if (args[0].equals("user_cmd")) {
				String identifier = args[1];
				String cmd = args[3];
				String[] ids = identifier.split(":+");
				String[] login_ip = ids[3].split("@+");
				
				if (cmd.equals("who")) {
					String sid = args[4];
					if (sid.equals("rep")) {
						System.out.println("end of command reply");
					}
					else if (sid.equals("state")) {
						String state = args[5];
						System.out.println("state change");
						callAct("{\"action\":\"state\", \"state\":\""+state+"\"}");
					}
					else {
						String source_login = args[5];
						String source_ip = args[6];
						String source_location = args[12];
						String source_promo = args[13];
						String[] source_state_timestamp = args[14].split(":+");
						String source_data = args[15];
						
						String time = timestamp_to_date(source_state_timestamp[1]);
						
//						DateFormat stamp = 
//						DateFormat.getInstance()
						
						if (source_state_timestamp[0].equals("actif"))
							source_state_timestamp[0] = "active";
						//String source_data = args[13];
						System.out.println("user_cmd: info "+identifier+" (state "+source_state_timestamp[0]+", since: "+time+")");
						callAct("{" +
								"\"action\":\"userstatus\"," +
								"\"user\":{" +
									"\"id\": \""+protect(sid)+"\"," +
									"\"login\":\""+protect(source_login)+"\"," +
									"\"location\":\""+protect(decode(source_location))+"\"," +
									"\"ip\":\""+protect(source_ip)+"\"," +
									"\"promo\":\""+protect(source_promo)+"\"," +
									"\"data\":\""+protect(source_data)+"\"" +
								"}," +
								"\"state\":\""+protect(source_state_timestamp[0])+"\"," +
								"\"since\":\""+protect(time)+"\"" +
							"}");
					}
				}
				else if (cmd.equals("msg")) {
					String text = args[4];
					String[] parts = args[5].split("\\*+");
					System.out.println("received "+args[5]+" splitted in "+parts.length);
					String target_login = parts[1].substring(1);
					target_login = target_login.substring(0, target_login.length()-1);
					String target_location = parts[2];
					
					System.out.println("forward message "+ids[0]+" "+target_login+"@"+target_location+" from "+login_ip[0]+"@"+decode(ids[5]));
					callAct("{" +
						"\"action\":\"message\"," +
						"\"user\":{" +
							"\"id\":\""+protect(ids[0])+"\"," +
							"\"login\":\""+protect(login_ip[0])+"\"," +
							"\"ip\":\""+protect(login_ip[1])+"\"," +
							"\"location\":\""+protect(decode(ids[5]))+"\"," +
							"\"promo\":\""+protect(decode(ids[6]))+"\"" +
						"}," +
						"\"message\":{" +
							"\"text\":\""+protect(decode(text))+"\"," +
							"\"login\":\""+protect(target_login)+"\"," +
							"\"location\":\""+protect(decode(target_location))+"\"" +
						"}" +
					"}");
				}
				else if (cmd.equals("login")) {
					System.out.println("user_cmd: login "+identifier+" ");
					callAct("{" +
						"\"action\":\"login\"," +
						"\"user\":{" +
							"\"id\":\""+protect(ids[0])+"\"," +
							"\"login\":\""+protect(login_ip[0])+"\"," +
							"\"location\":\""+protect(decode(ids[5]))+"\"," +
							"\"ip\":\""+protect(login_ip[1])+"\"" +
						"}" +
					"}");
				}
				else if (cmd.equals("logout")) {
					System.out.println("user_cmd: logout "+identifier+" ");
					callAct("{" +
						"\"action\":\"logout\"," +
						"\"user\":{" +
							"\"id\":\""+protect(ids[0])+"\"," +
							"\"login\":\""+protect(login_ip[0])+"\"," +
							"\"location\":\""+protect(decode(ids[5]))+"\"," +
							"\"ip\":\""+protect(login_ip[1])+"\"" +
						"}" +
					"}");
				}
				else if (cmd.equals("state")) {
					String[] state_tm = args[4].split(":+");
					if (state_tm[0].equals("actif"))
						state_tm[0] = "active";
					System.out.println("user_cmd: state "+identifier+" to "+state_tm[0]);
					String time = timestamp_to_date(state_tm[1]);
					callAct("{\"action\":\"state\", \"user\":{\"id\":\""+protect(ids[0])+"\"}, \"state\":\""+protect(state_tm[0])+"\", \"since\":\""+protect(time)+"\"}");
				}
				else
					System.out.println("user_cmd: unrecognized command "+cmd+"");
			}
			else
				System.err.println("Unrecognized message: "+line);
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.err.println("Unparsable message "+e);
		}
	}
	
	private String	timestamp_to_date(String timestamp) {
		long tm = Long.parseLong(timestamp);
		Date t = new Date(tm * 1000);
		@SuppressWarnings("deprecation")
		String time = ""+(1900+t.getYear())+"-"+(t.getMonth()+1 < 10? "0": "")+(t.getMonth()+1)+"-"+(t.getDate() < 10? "0": "")+t.getDate()+" "+(t.getHours() < 10? "0": "")+t.getHours()+":"+(t.getMinutes() < 10? "0": "")+t.getMinutes()+":"+(t.getSeconds() < 10? "0": "")+t.getSeconds();
		return time;
	}

	public boolean	setCredential(String user_login, String user_pass, String user_location, String user_data) {
		System.out.println("setCredential("+user_login+", ****, "+user_location+", "+user_data+")");
		synchronized(thread) {
			System.out.println("thread is synchronized");
			login = user_login;
			pass = user_pass;
			location = user_location;
			data = user_data;
			thread.notify();
			
//			if (is != null)
//				is.notify();
		}
		return true;
	}
	
	
	public boolean	sendMessage(String target_login, String target_location, String message) {
		synchronized (is) {
			//System.out.println("notify thread");
			System.out.println("sending message: '"+encode(message)+"' to "+target_login+"@"+encode(target_location));
			pw.print("user_cmd msg *:"+target_login+"@*"+encode(target_location)+"* msg "+encode(message)+"\n");
			pw.flush();
//				messagesToSend.add(message);
//				if (messagesToSend.size() > 0) {
//					for (int i = 0; i<messagesToSend.size(); i++)
//					System.out.println("sending message: '"+messagesToSend.elementAt(i)+"'");
//				}
//				thread.notify();
		}
		return true;
	}
	public String	appletVersion() {
		return clientName+" "+clientVersion;
	}
	public boolean	watchUser(String target_login) {
		synchronized (is) {
			//System.out.println("notify thread");
			System.out.println("watch login: '"+target_login+"'");
			pw.print("user_cmd watch_log_user {"+target_login+","+login+","+login+"}\n");
			pw.print("user_cmd who {"+target_login+"}\n");
			pw.flush();
		}
		return true;
	}
	public boolean	disconnect() {
		disconnectSocket(is);
		authenticated = false;
		return true;
	}
//	public boolean	unwatchUser(String target_login) {
//		synchronized (is) {
//			//System.out.println("notify thread");
//			System.out.println("watch login: '"+target_login+"'");
//			pw.print("user_cmd watch_log_user {"+target_login+","+login+","+login+"}\n");
//			pw.print("user_cmd who {"+target_login+"}\n");
//			pw.flush();
//		}
//		return true;
//	}

	private static String encode(String str) {
		try {
			return URLEncoder.encode(str, "ISO-8859-1").replace((CharSequence)"+", (CharSequence)"%20");
		}
		catch (UnsupportedEncodingException e) {
			System.out.println(e);
			return null;
		}
	}
	private static String protect(String str) {
		return str.replace((CharSequence)"\\", (CharSequence)"\\\\").replace((CharSequence)"\"", (CharSequence)"\\\"");
	}
	
	private static String decode(String str) {
		try {
			return URLDecoder.decode(str, "ISO-8859-1");
		}
		catch (UnsupportedEncodingException e) {
			System.out.println(e);
			return null;
		}
	}
	
	private	void callAct(String json) {
		if (callbackfunc == null)
			return ;
		/*try {
			//getAppletContext().showDocument(new URL("javascript:"
					//+ callbackfunc + "(" + json + (callbackparam != null? ", "+callbackparam: "") + ")"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		}*/
	}

	private boolean connectSocket() {
		if (client == null || !client.isConnected() || client.isClosed() || client.isInputShutdown() || client.isOutputShutdown()) {
//			System.out.println("Client is not connected");
			try {
//				System.out.println("Connecting to client");
				client = new Socket(defaultHost, defaultPort);
				is = new BufferedReader(new InputStreamReader(client.getInputStream()));
				os = new BufferedOutputStream(client.getOutputStream());
				pw = new PrintWriter(os, false);
				client.setSoTimeout(10000);
//				System.out.println("Connected to "+targetURL.getHost()+":"+port);
//				Status.setText("Connecté à "+this.Host+":"+this.Port);
				callAct("{\"success\":\"Connected to "+this.defaultHost+":"+this.defaultPort+"\"}");
			}
			catch (IOException e) {
				callAct("{\"error\":\"Cant connect to "+this.defaultHost+":"+this.defaultPort+"\"}");
				System.err.println("Error: Can't connect to "+this.defaultHost+":"+this.defaultPort);
				return false;
			}
		}
//		else
//			System.out.println("Client is already connected");
		return true;
	}
	private void disconnectSocket(BufferedReader _is) {
		try {
			try {
				if (client != null) {
					client.close();
					client.notify();
				}
				if (pw != null) {
					pw.close();
					pw.notify();
				}
				if (_is != null) {
					_is.close();
					_is.notify();
				}
				
			}
			catch(IOException e) {
				System.err.println("Failed to close socket "+e);
			}
			client = null;
			pw = null;
			_is = null;
			is = null;

//			if (client != null) {
//				System.out.println("Closing connection");
//				client.close();
//			}
		}
		catch (Exception e) {
			System.err.println("Error: while closing socket");
		}
		client = null;
	}


}
