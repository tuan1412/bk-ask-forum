package ptpmcn.errorhandling;

public class FileUploadException extends RuntimeException {
private static final long serialVersionUID = 1L;
	
	private static final String Message = "Dont upload file";
	
	public FileUploadException() {
		super(Message);
	}
}
