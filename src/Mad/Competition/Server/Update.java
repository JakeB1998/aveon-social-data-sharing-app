package Mad.Competition.Server;

public class Update implements Runnable
{

	public Update()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				Thread.sleep(3000);
				//Server.safePrintln(Integer.toString(Server.NumberOfConnectedDevices));
				
				for (int i = 0; i < Server.connectedClients.size(); i++)
				{
					System.out.println(i + " - " +  Server.connectedClients.get(i).toString());
				}
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

}
