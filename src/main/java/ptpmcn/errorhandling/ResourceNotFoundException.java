package ptpmcn.errorhandling;

public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private static final String Message = "Resource not found.";
	
	public ResourceNotFoundException() {
		super(Message);
	}
}
