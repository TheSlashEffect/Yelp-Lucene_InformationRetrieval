package prj;

//lucene/queryparser JAR needed

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.FloatPoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;




public class SearchReviewFiles {

	static SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer();
	static PorterAnalyzer porterAnalyzer = new PorterAnalyzer();
	static QueryParser nameParser    = new QueryParser("restName",   simpleAnalyzer);
	static QueryParser addressParser = new QueryParser("address",    simpleAnalyzer);
	static QueryParser cityParser    = new QueryParser("city",       simpleAnalyzer);
	static QueryParser textParser    = new QueryParser("reviewText", porterAnalyzer);
	
	public SearchReviewFiles() {}
	

	public static ArrayList<ReviewResult> PerformSearch(IndexSearcher searcher,
			String restaurantName,
			String dateLow,    String dateHigh,
			String usefulLow,  String usefulHigh,
			String funnyLow,   String funnyHigh,
			String coolLow,    String coolHigh,
			String ratingLow, String ratingHigh,
			String reviewText, 
			int sortingField,
			boolean rep, boolean limitResults) throws IOException, ParseException{

		// Create boolean query using BooleanQuery.Builder
		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		
		
		// Add all non-empty fields to boolean query, but count only reviewText's score

		
		/**********TERM/PHRASE QUERIES*********/
		
		if (!restaurantName.equals("")) {
			Query nameQuery    = nameParser.parse(restaurantName);
			builder.add(nameQuery, BooleanClause.Occur.FILTER);
		}

		
		
		/**********RANGE QUERIES**********/
				
		// Date
		// User provided both limits
		if (!dateLow.equals("") && !dateHigh.equals("")) {
			Query dateRange = new TermRangeQuery("reviewDate",
				new BytesRef(dateLow.replaceAll("-", "")), new BytesRef(dateHigh.replaceAll("-", "")), true, true);
			builder.add(dateRange, BooleanClause.Occur.FILTER);
		}
		// User provided only upper limit
		if (dateLow.equals("") && !dateHigh.equals("")) {
			Query dateRange = new TermRangeQuery("reviewDate",
					new BytesRef("19700101"), new BytesRef(dateHigh.replaceAll("-", "")), true, true);
			builder.add(dateRange, BooleanClause.Occur.FILTER); 
		}
		// User provided only lower limit
		if (!dateLow.equals("") && dateHigh.equals("")) {
			Query dateRange = new TermRangeQuery("reviewDate",
					new BytesRef(dateLow.replaceAll("-", "")), new BytesRef("20200101"), true, true);
			builder.add(dateRange, BooleanClause.Occur.FILTER); 
		}
		
		
		// Useful
		// User provided both limits
		if (!usefulLow.equals("") && !usefulHigh.equals("")) {
			Query usefulRange = IntPoint.newRangeQuery("useful",
					Integer.parseInt(usefulLow), Integer.parseInt(usefulHigh));
			builder.add(usefulRange, BooleanClause.Occur.FILTER);
		}
		// User provided only upper limit, set lower limit to 0
		if (usefulLow .equals("") && !usefulHigh.equals("")) {
			Query usefulRange = IntPoint.newRangeQuery("useful",
					0, Integer.parseInt(usefulHigh));
			builder.add(usefulRange, BooleanClause.Occur.FILTER); 
		}
		// User provided only lower limit, set upper limit to Integer.MAX_VALUE
		if (!usefulLow.equals("") && usefulHigh.equals("")) {
			Query usefulRange = IntPoint.newRangeQuery("useful",
					Integer.parseInt(usefulLow), Integer.MAX_VALUE);
			builder.add(usefulRange, BooleanClause.Occur.FILTER); 
		}

		
		// Funny
		// User provided both limits
		if (!funnyLow.equals("") && !funnyHigh.equals("")) {
			Query funnyRange = IntPoint.newRangeQuery("funny",
					Integer.parseInt(funnyLow), Integer.parseInt(funnyHigh));
			builder.add(funnyRange, BooleanClause.Occur.FILTER);
		}
		// User provided only upper limit, set lower limit to -90 degrees
		if (funnyLow .equals("") && !funnyHigh.equals("")) {
			Query funnyRange = IntPoint.newRangeQuery("funny",
					0, Integer.parseInt(funnyHigh));
			builder.add(funnyRange, BooleanClause.Occur.FILTER);
		}
		// User provided only lower limit, set upper limit to 90 degrees
		if (!funnyLow.equals("") && funnyHigh .equals("")) {
			Query funnyRange = IntPoint.newRangeQuery("funny",
					Integer.parseInt(funnyLow), Integer.MAX_VALUE);
			builder.add(funnyRange, BooleanClause.Occur.FILTER);
		}
		
		
		// Cool
		// User provided both limits
		if (!coolLow.equals("") && !coolHigh.equals("")) {
			Query coolRange = IntPoint.newRangeQuery("cool",
					Integer.parseInt(coolLow), Integer.parseInt(coolHigh));
			builder.add(coolRange, BooleanClause.Occur.FILTER);
		}
		// User provided only upper limit, set lower limit to -90 degrees
		if (coolLow .equals("") && !coolHigh.equals("")) {
			Query coolRange = IntPoint.newRangeQuery("cool",
					0, Integer.parseInt(coolHigh));
			builder.add(coolRange, BooleanClause.Occur.FILTER);
		}
		// User provided only lower limit, set upper limit to 90 degrees
		if (!coolLow.equals("") && coolHigh .equals("")) {
			Query coolRange = IntPoint.newRangeQuery("cool",
					Integer.parseInt(coolLow), Integer.MAX_VALUE);
			builder.add(coolRange, BooleanClause.Occur.FILTER);
		}
		

		// Rating
		// User provided both limits
		if (!ratingLow.equals("") && !ratingHigh.equals("")) {
			Query ratingRange = FloatPoint.newRangeQuery("stars",
					Float.parseFloat(ratingLow), Float.parseFloat(ratingHigh));
			builder.add(ratingRange, BooleanClause.Occur.FILTER);
		}
		// User provided only upper limit, set lower limit to 0.0 rating
		if (ratingLow == "" && !ratingHigh.equals("")) {
			Query ratingRange = FloatPoint.newRangeQuery("stars",
					0.0f, Float.parseFloat(ratingHigh));
			builder.add(ratingRange, BooleanClause.Occur.FILTER);
		}
		// User provided only lower limit, set upper limit to 5.0 rating
		if (!ratingLow.equals("") && ratingHigh.equals("")) {
			Query ratingRange = FloatPoint.newRangeQuery("stars",
					Float.parseFloat(ratingLow), 5.0f);
			builder.add(ratingRange, BooleanClause.Occur.FILTER);
		}
		

		BooleanQuery.Builder highlightBuilder = new BooleanQuery.Builder();	//used for text highlighting
		if (!reviewText.equals("")) {
			// The following 2 lines produce OR type query, meaning lowest ranking results
			// might be highly irrelevant, thus capping the result limits to 1000.
			Query reviewQuery = textParser.parse(reviewText);
			builder.add(reviewQuery, BooleanClause.Occur.MUST);
			highlightBuilder.add(reviewQuery, BooleanClause.Occur.MUST);
			
			// Create phrase Query with each token derived from the porter analyzer stream
	        TokenStream tokenStream  = porterAnalyzer.tokenStream("reviewText", new StringReader(reviewText));
	        PhraseQuery.Builder reviewQueryBuilder = new PhraseQuery.Builder();
	        // OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
	        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

	        tokenStream.reset();
	        while (tokenStream.incrementToken()) {
	            // int startOffset = offsetAttribute.startOffset();
	            // int endOffset = offsetAttribute.endOffset();
	            reviewQueryBuilder.add(new Term("reviewText", charTermAttribute.toString()));
	        }
	        BoostQuery boosted = new BoostQuery(reviewQueryBuilder.build(), 2.0f);
	        builder.add(boosted, BooleanClause.Occur.SHOULD);
	        highlightBuilder.add(boosted, BooleanClause.Occur.SHOULD);
	        tokenStream.close();
		}
		

		
		// Build the query
		BooleanQuery fullQuery = builder.build();
		System.out.println("Searching for: " + fullQuery.toString());
		
		
		// Perform search and return results to front end
		TopDocs    searchResults = null;
		ScoreDoc[] hits          = null;

		
		int searchLimit = (limitResults) ? 1000 : 10000;
		// Max number of retrieved result is set by program design on 10.000 documents.
		// No human user would ever need more than 1.000 results, anyway. This software
		// is not suitable for abnormally picky eaters.

		
		switch(sortingField) {
		
		case(0):		//sort by review text's content (default)
			searchResults = searcher.search(fullQuery, searchLimit);
			break;
		case(1):		//sort by useful count
			searchResults = searcher.search(fullQuery, searchLimit, new Sort(new SortField("usefulCntSort",  SortField.Type.INT,   true)));
			break;
		case(2):		//sort by review date 
			searchResults = searcher.search(fullQuery, searchLimit, new Sort(new SortField("reviewDateSort", SortField.Type.LONG,  true)));
			break;
		}
		
		if (searchResults != null) hits 	= searchResults.scoreDocs;
		
		
		
		System.out.println("" + searchResults.totalHits + " results found.");
		long  resultSize = Math.min(searchResults.totalHits, searchLimit);
		int[] histogram  = new int[5];		//Create rating histogram to be used for representative results functionality
		
		ArrayList<ReviewResult> results = new ArrayList<ReviewResult>((int) resultSize);
		if (resultSize == 0) return results;

		if (rep) {
			int limit;
			
			ArrayList<Document> preResults = new ArrayList<Document>();
			limit = 1;
			
			System.out.println("Before user filtering: " + resultSize);
			
			// Filter by city using dynamic hashing histogram.
			// Map city names (String) to city occurrences (Integer) in results
			HashMap<String, Integer> hashtogram = new HashMap<String, Integer>();
			// Iterate once to find out all the cities in our results
			for (int i = 0; i < resultSize; i++){
				Document doc = searcher.doc(hits[i].doc);
				
				Integer val = hashtogram.get(doc.get("userId"));
				if (val == null) {	//first user occurrence.
					hashtogram.put(doc.get("userId"), 0);
					preResults.add(doc);	//Add reference to document
				}else if (val < limit){		//Update number of user's reviews found.
					hashtogram.put(doc.get("userId"), ++val);
					preResults.add(doc);
				}//User limit reached, don't add document.
			}
			
			
			
			System.out.println("After user filtering: " + preResults.size());
			
			if (resultSize < 100)
				limit = 10;
			else if (resultSize < 1000)
				limit = 20;
			else
				limit = 50;

			for (int i = 0; i < preResults.size(); i++) {
				
				//Document doc  = searcher.doc(hits[i].doc);
				Document doc = preResults.get(i);
				int rating = (int) Float.parseFloat(doc.get("starValue"));
				
				if (histogram[rating - 1] < limit) {
					String resName      = doc.get("restName");
					String resDate      = doc.get("reviewDate");
					String resUsefulCnt = doc.get("usefulCnt");
					String resFunnyCnt  = doc.get("funnyCnt");
					String resCoolCnt  = doc.get("coolCnt");
					String resRating  = doc.get("starValue");
					
					/*
					 * 
					 *Below line returns matched text to user entered review text query.
					 *Not used in demo to Mrs. Pitoura due to extensive corpus size and complexity
					 *(1.096.036 files). Instead, the command below prints a summary of the first 30
					 *words to the user.
					 *
					results.add(new ReviewResult(resName, resDate, resUsefulCnt, resFunnyCnt, resCoolCnt,
												 resRating, getHighlightedText(reviewText, doc.get("path"),
														 					   highlightBuilder.build()), doc.get("path")));
					*/
					results.add(new ReviewResult(resName, resDate, resUsefulCnt, resFunnyCnt, resCoolCnt,
							 resRating, doc.get("preview") + "...", doc.get("path")));					histogram[rating - 1]++;
				}
				
			}
			
			// Developer statistics
			
			for (int i = 0; i < 5; i++)
				System.out.println("" + histogram[i] + " " + (i + 1) + " star reviews");

		}else {
		
			for (int i = 0; i < resultSize; i++) {
				Document doc        = searcher.doc(hits[i].doc);
				String resName      = doc.get("restName");
				String resDate      = doc.get("reviewDate");
				String resUsefulCnt = doc.get("usefulCnt");
				String resFunnyCnt  = doc.get("funnyCnt");
				String resCoolCnt   = doc.get("coolCnt");
				String resRating    = doc.get("starValue");

				
				/*
				 * 
				 *Below line returns matched text to user entered review text query.
				 *Not used in demo to Mrs. Pitoura due to extensive corpus size and complexity
				 *(1.096.036 files). Instead, the command below prints a summary of the first 30
				 *words to the user.
				 *
				results.add(new ReviewResult(resName, resDate, resUsefulCnt, resFunnyCnt, resCoolCnt,
											 resRating, Utils.getHighlightedText(reviewText, doc.get("path"),
													 					   highlightBuilder.build()), doc.get("path")));
				*/
				
				results.add(new ReviewResult(resName, resDate, resUsefulCnt, resFunnyCnt, resCoolCnt,
						 resRating, doc.get("preview") + "...", doc.get("path")));
			}
		}
		return results;
	}

}
