import java.util.Scanner;

public class Main 
{
	private static final String IP_ADDRESS = "172.17.143.177"; 
	private static Scanner scanner;
	private static String resp;
	
	public static void main(String[] args) 
	{
		//	Init scanner
		scanner = new Scanner(System.in);
		
		//	Ask server or client?
		System.out.print("Are you a server? [y/n] ");
		resp = scanner.nextLine();
		
		if(resp.equalsIgnoreCase("y"))
			new Server();
		else 
		{
			//	Verify correct IP
			System.out.print("Is the IP Address: "+ IP_ADDRESS + "? [y/n] ");
			resp = scanner.nextLine();
			
			if(!resp.equalsIgnoreCase("y"))
			{
				//	Ask user for IP
				System.out.print("Enter IP: ");
				resp = scanner.nextLine();
			}
			else resp = IP_ADDRESS;
			
			try 
			{
				new Client(resp);
			}
			catch(Exception e)
			{
				tryAgain(resp);
			}
		}


		scanner.close();
	}

	//	Called by client after connection failure
	public static void tryAgain(String ip)
	{
		System.out.print("Wait for server? ");
		resp = scanner.nextLine();
		
		if(resp.equalsIgnoreCase("y"))
		{
			try 
			{
				new Client(ip);
			}
			catch(Exception e)
			{
				tryAgain(ip);
			}
		}
	}
}
