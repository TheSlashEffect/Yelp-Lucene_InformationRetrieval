package prj;

public class BusinessResult {
	private String name;
	private String city;
	private String address;
	private String reviewCount;
	private String rating;
	private String matchedText;
	private String filePath;

	public BusinessResult(String name,        String city, 
			              String address,     String reviewCount,
			              String rating,      String matchedText,
			              String filePath) {
		this.name        = name;
		this.city        = city;
		this.address     = address;
		this.reviewCount = reviewCount;
		this.rating      = rating;
		this.matchedText = matchedText;
		this.filePath    = filePath;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getReviewCount() {
		return reviewCount;
	}

	public String getRating() {
		return rating;
	}
	
	public String getMatchedText() {
		return matchedText;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
}
