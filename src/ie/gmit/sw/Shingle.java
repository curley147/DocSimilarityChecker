package ie.gmit.sw;
/**
 * This is the Shingle class and it contains a constructor and two member variables document ID and the shingle's hashcode
 * It's methods include getters and setters for these variables
 * @author Micheal Curley
 *
 */
public class Shingle {
	private int docId;
	private int shingleHashCode;

	public Shingle(int docId, int shingleHashCode) {
		this.docId = docId;
		this.shingleHashCode = shingleHashCode;
	}
	public Shingle() {
		
	}
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public int getShingleHashCode() {
		return shingleHashCode;
	}
	public void setShingleHashCode(int shingleHashCode) {
		this.shingleHashCode = shingleHashCode;
	}
}
