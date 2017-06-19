import java.io.Serializable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Tauler implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public HashMap<String, Integer> puntuacions  = new HashMap<>();
	public String missatge;
	
	public Tauler(){
		
	}
	
	public void showTauler(){
		Iterator<Entry<String, Integer>> it = puntuacions.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, Integer> pair= (Map.Entry<String, Integer>)it.next();
			 System.out.println("Jugador: "+pair.getKey() + " Intentos: " + pair.getValue());
		        it.remove(); // avoids a ConcurrentModificationException
		}
	}
	
	public void addClients(String s, int i){
		puntuacions.put(s, i);
	}
	
	
}
