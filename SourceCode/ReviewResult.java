package prj;

public class ReviewResult {
	private String name;
	private String date;
	private String usefulCount;
	private String funnyCount;
	private String coolCount;
	private String rating;
	private String matchedText;
	private String filePath;

	public ReviewResult(String name, String date, String usefulCount,
			            String funnyCount, String coolCount,
			            String rating, String matchedText,
			            String filePath) {
		
		this.name        = name;
		this.date        = date;
		this.usefulCount = usefulCount;
		this.funnyCount  = funnyCount;
		this.coolCount   = coolCount;
		this.rating      = rating;
		this.matchedText = matchedText;
		this.filePath    = filePath;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getUsefulCount() {
		return usefulCount;
	}
	
	
	public String getFunnyCount() {
		return funnyCount;
	}
	
	public String getCoolCount() {
		return coolCount;
	}	

	public String getRating() {
		return rating;
	}
	
	public String getMatchedText() {
		return matchedText;
	}
	
	public String getPath() {
		return filePath;
	}
	
}
