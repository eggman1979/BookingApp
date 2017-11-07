package data;

import java.io.Serializable;



public class BoligForening implements Serializable {
	 private static final long serialVersionUID = 1L; 

	private String navn;
	private  int id;

	public BoligForening(String navn,  int id){
		this.navn = navn;
		this.id = id;
	}
	public BoligForening(){}

	public String getNavn() {
		return navn;
	}


	public void setNavn(String navn) {
		this.navn = navn;
	}

	public int getId() {
		return id;
	}

	
}
