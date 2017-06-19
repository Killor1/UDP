import java.io.Serializable;

public class Jugada implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String nom;
	public int intentos;
	public String jugada;
	
	public Jugada(String nom, int intentos, String jugada){
		this.nom=nom;
		this.intentos=intentos;
		this.jugada=jugada;
	}
}
