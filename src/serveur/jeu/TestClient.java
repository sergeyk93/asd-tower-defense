package serveur.jeu;

import java.io.IOException;
import java.net.ConnectException;

import org.json.JSONException;
import org.json.JSONObject;

import reseau.CanalTCP;
import reseau.CanalException;

public class TestClient implements ConstantesServeurJeu
{
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		int ID = 0;
		String addr = "localhost";
		if (args.length > 1)
		{
			ID = Integer.parseInt(args[1]);
			addr = args[0];
		}

		new TestClient(addr,ID);
	}

	/**
	 * @param ID
	 */
	public TestClient(String addr, int ID)
	{
		try
		{

			CanalTCP canal = new CanalTCP(addr, 2357, false);
			Thread reception = new Thread(new Reception(canal));
			Thread emission = new Thread(new Emission(canal));

			// Envoi de l'ID du clietn
			canal.envoyerInt(ID);
			// Récéption de la version du serveur
			System.out
					.println("Version du serveur : " + canal.recevoirString());
			// Lancement des thread de dialogues
			reception.start();
			emission.start();

		} catch (ConnectException e)
		{
			e.printStackTrace();
		} catch (CanalException e)
		{
			e.printStackTrace();
		}
	}

	String recevoirMessage(CanalTCP canal) throws JSONException
	{
		String rcp = canal.recevoirString();
		JSONObject json = new JSONObject(rcp);
		return "[" + json.getString("PSEUDO") + "] "
				+ json.getString("MESSAGE");
	}

	void envoyerMessage(CanalTCP canal, String msg) throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("TYPE", MSG);
		JSONObject content = new JSONObject();
		content.put("CIBLE", TO_ALL);
		content.put("MESSAGE", msg);
		json.put("CONTENU", content);

		canal.envoyerString(json.toString());

	}

	private class Emission implements Runnable
	{

		private CanalTCP canal;

		public Emission(CanalTCP canal)
		{
			this.canal = canal;
		}

		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					envoyerMessage(canal, Clavier.lireString());
				} catch (IOException e)
				{
					e.printStackTrace();
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private class Reception implements Runnable
	{
		private CanalTCP canal;

		public Reception(CanalTCP canal)
		{
			this.canal = canal;
		}

		public void run()
		{
			while (true)
			{
				try
				{
					System.out.println(recevoirMessage(canal));
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
