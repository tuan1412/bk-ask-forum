package ptpmcn.errorhandling;

public class ForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private static final String Message = "Forbidden action.";

	public ForbiddenException() {
		super(Message);
	}

}
