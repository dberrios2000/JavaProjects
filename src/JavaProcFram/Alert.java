package JavaProcFram;

import java.io.Serializable;
import java.util.Date;

/**
 * A simple java bean modelling the alert entity.
 * 
 */
public class Alert implements Serializable {

	private static final long serialVersionUID = 201308232212L;
	
	private long id;
	private Date date;
	private int display;
	private String message;
	
	public Alert() {
		
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
