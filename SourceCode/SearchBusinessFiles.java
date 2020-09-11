package prj;

// lucene/queryparser JAR needed

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.FloatPoint;
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
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;




public class SearchBusinessFiles {

	static SimpleAnalyzer simpleAnalyzer = new SimpleAnalyzer();
	static PorterAnalyzer porterAnalyzer = new PorterAnalyzer();
	static QueryParser nameParser    = new QueryParser("restName",   simpleAnalyzer);
	static QueryParser addressParser = new QueryParser("address",    simpleAnalyzer);
	static QueryParser cityParser    = new QueryParser("city",       simpleAnalyzer);
	static QueryParser textParser    = new QueryParser("reviewText", porterAnalyzer);
	
	public SearchBusinessFiles() {}
	
	public static ArrayList<BusinessResult> TestSearch(IndexSearcher searcher,
			String restaurantName, String neighbourhood, String address, 
			String city, String state, String postalCode,
			String latitudeLow,  String latitudeHigh,
			String longitudeLow, String longitudeHigh,
			String starsLow,     String starsHigh,
			String reviewText,
			int sortingField,
			boolean rep, boolean limitResults,
			boolean showMatched) throws IOException, ParseException{

		BooleanQuery.Builder builder = new BooleanQuery.Builder();

		//Add all non-empty fields to boolean query, but count only reviewText's score

		/**********TERM/PHRASE QUERIES*********/
		
		if (!restaurantName.equals("")) {
			Query nameQuery    = nameParser.parse(restaurantName);
			builder.add(nameQuery, BooleanClause.Occur.FILTER);
		}
		if (!neighbourhood.equals("")) {
			Query hoodQuery    = new TermQuery(new Term(neighbourhood));
			builder.add(hoodQuery, BooleanClause.Occur.FILTER);
		}
		if (!address.equals("")) {
			Query addressQuery = addressParser.parse(address);
			builder.add(addressQuery, BooleanClause.Occur.FILTER);
		}
		if (!city.equals("")) {
			Query cityQuery    = cityParser.parse(city);
			builder.add(cityQuery, BooleanClause.Occur.FILTER);
		}
		if (!state.equals("")) {
			Query stateQuery   = new TermQuery(new Term(state));
			builder.add(stateQuery, BooleanClause.Occur.FILTER);
		}
		if (!postalCode.equals("")) {
			Query postQuery    = new TermQuery(new Term(postalCode));
			builder.add(postQuery, BooleanClause.Occur.FILTER);
		}
		
		
		/**********RANGE QUERIES**********/
				
		// Latitude
		// User provided both limits
		if (!latitudeLow.equals("") && !latitudeHigh.equals("")) {
			Query latitudeRange = DoublePoint.newRangeQuery("latitude",
					Double.parseDouble(latitudeLow), Double.parseDouble(latitudeHigh));
			builder.add(latitudeRange, BooleanClause.Occur.FILTER);
		}
		// User provided only upper limit, set lower limit to -90 degrees
		if (latitudeLow.equals("") && !latitudeHigh.equals("")) {
			Query latitudeRange = DoublePoint.newRangeQuery("latitude",
					0.0, Double.parseDouble(latitudeHigh));
			builder.add(latitudeRange, BooleanClause.Occur.FILTER); 
		}
		// User provided only lower limit, set upper limit to 90 degrees
		if (!latitudeLow.equals("") && latitudeHigh.equals("")) {
			Query latitudeRange = DoublePoint.newRangeQuery("latitude",
					Double.parseDouble(latitudeLow), 90.0);
			builder.add(latitudeRange, BooleanClause.Occur.FILTER); 
		}

		
		// Longitude
		// User provided both limits
		if (!longitudeLow.equals("") && !longitudeHigh.equals("")) {
			Query longitudeRange = DoublePoint.newRangeQuery("longitude",
					Double.parseDouble(longitudeLow), Double.parseDouble(longitudeHigh));
			builder.add(longitudeRange, BooleanClause.Occur.FILTER);
		}
		// User provided only upper limit, set lower limit to -90 degrees
		if (longitudeLow.equals("") && !longitudeHigh.equals("")) {
			Query longitudeRange = DoublePoint.newRangeQuery("longitude",
					0.0, Double.parseDouble(longitudeHigh));
			builder.add(longitudeRange, BooleanClause.Occur.FILTER);
		}
		// User provided only lower limit, set upper limit to 90 degrees
		if (!longitudeLow.equals("") && longitudeHigh.equals("")) {
			Query longitudeRange = DoublePoint.newRangeQuery("longitude",
					Double.parseDouble(longitudeLow), 90.0);
			builder.add(longitudeRange, BooleanClause.Occur.FILTER);
		}

		// Stars
		// User provided both limits
		if (!starsLow.equals("") && !starsHigh.equals("")) {
			Query longitudeRange = FloatPoint.newRangeQuery("stars",
					Float.parseFloat(starsLow), Float.parseFloat(starsHigh));
			builder.add(longitudeRange, BooleanClause.Occur.FILTER);
		}
		// User provided only upper limit, set lower limit to 0.0 rating
		if (starsLow.equals("") && !starsHigh.equals("")) {
			Query longitudeRange = FloatPoint.newRangeQuery("stars",
					0.0f, Float.parseFloat(starsHigh));
			builder.add(longitudeRange, BooleanClause.Occur.FILTER);
		}
		// User provided only lower limit, set upper limit to 5.0 rating
		if (!starsLow.equals("") && starsHigh.equals("")) {
			Query longitudeRange = FloatPoint.newRangeQuery("stars",
					Float.parseFloat(starsLow), 5.0f);
			builder.add(longitudeRange, BooleanClause.Occur.FILTER);
		}

		
		BooleanQuery.Builder highlightBuilder = new BooleanQuery.Builder();	//used for text highlighting
		if (!reviewText.equals("") ) {
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

		
		int searchLimit = 1000;
		//int searchLimit = (limitResults) ? 1000 : 10000;
		
		if       (limitResults  && !rep)
			searchLimit = 1000;
		else if (limitResults  &&  rep)
			searchLimit = 2000;
		else if (!limitResults &&  rep)
			searchLimit = 17000;
		else if (!limitResults && !rep)
			searchLimit = 2000;
		//Max number of retrieved result is set by program design on 10.000 documents.
		//No human user would ever need more than 1.000 results, anyway. This software
		//is not suitable for abnormally picky eaters.

		
		switch(sortingField) {
		
		case(0):		// sort by review Text's content (default)
			searchResults = searcher.search(fullQuery,  searchLimit);
			break;
		case(1):		// sort by review count
			searchResults = searcher.search(fullQuery,  searchLimit, new Sort(new SortField("reviewCntSort", SortField.Type.INT,   true)));
			break;
		case(2):		// sort by star rating
			searchResults = searcher.search(fullQuery,  searchLimit, new Sort(new SortField("starsSort",     SortField.Type.FLOAT, true)));
			break;
		}
		
		if (searchResults != null) hits = searchResults.scoreDocs;
		
		
		
		System.out.println("" + searchResults.totalHits + " results found.");
		long resultSize = Math.min(searchResults.totalHits, searchLimit);

		
		ArrayList<BusinessResult> results = new ArrayList<BusinessResult>((int) resultSize);
		
		if (resultSize == 0) return results;

		if (rep) {

			System.out.println("Before city filtering: " + resultSize);
			
			//Filter by city using dynamic hashing histogram.
			//Map city names (String) to city occurrences (Integer) in results 
			HashMap<String, Integer> hashtogram = new HashMap<String, Integer>();
			//Iterate once to find out all the cities in our results
			for (int i = 0; i < resultSize; i++){
				Document doc = searcher.doc(hits[i].doc);
				
				Integer val = hashtogram.get(doc.get("city"));
				if (val == null)	//first city occurrence
					hashtogram.put(doc.get("city"), 0);
			}
			
			
			// System.out.println("hashtogram size: " + hashtogram.size());
			int limit = (int) (resultSize / hashtogram.size() + 0.5) ;	//Set upper limit of all cities to same value
			
			for (int i = 0; i < resultSize; i++) {
				Document doc = searcher.doc(hits[i].doc);
				
				Integer currentVal = hashtogram.get(doc.get("city"));
				if (currentVal < limit) {
					hashtogram.put(doc.get("city"), ++currentVal);
					
					// Create result
					String resName    = doc.get("restName");
					String resCity    = doc.get("city");
					String resAddress = doc.get("address");
					String resCount   = doc.get("reviewCountVal");
					String resRating  = doc.get("starValue");
					
					if (showMatched)
						results.add(new BusinessResult(resName, resCity, resAddress, resCount, resRating,
						                               Utils.getHighlightedText(reviewText, doc.get("path"),
                                                       highlightBuilder.build(), porterAnalyzer), doc.get("path")));
					else
						results.add(new BusinessResult(resName, resCity, resAddress, resCount, resRating, " ",
						                               doc.get("path")));
				}
			
			}
			
			System.out.println("After city filtering: " + results.size());
			
			/*
			Iterator<Map.Entry<String, Integer>> it = hashtogram.entrySet().iterator();
			while(it.hasNext()) {
			    Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)it.next();
			    System.out.println(pair.getKey() + " = " + pair.getValue());
			    it.remove(); // avoids a ConcurrentModificationException
			}
			*/
			
			
			// Star filtering
			resultSize = results.size();
			int[] histogram  = new int[5];		//Create rating histogram to be used for representative results functionality
			limit = (int) ((resultSize / 5) + 0.5);
			System.out.println("limit: " + limit);
			
			Iterator<BusinessResult> iter = results.iterator();
			while(iter.hasNext()) {
				BusinessResult res = iter.next();
				int rating = (int) Float.parseFloat(res.getRating());

				if (histogram[rating - 1] < limit)
					histogram[rating - 1]++;
				else
					iter.remove();
			}
			
			for (int i = 0; i < 5; i++)
				System.out.println(histogram[i] + " " + (i+1) + " star restaurants");

			System.out.println("After star filtering: " + results.size());

		}else {
			
			for (int i = 0; i < resultSize; i++) {
				
				Document doc  = searcher.doc(hits[i].doc);
				String resName    = doc.get("restName");
				String resCity    = doc.get("city");
				String resAddress = doc.get("address");
				String resCount   = doc.get("reviewCountVal");
				String resRating  = doc.get("starValue");
				
				if (showMatched)
					results.add(new BusinessResult(resName, resCity, resAddress, resCount, resRating,
					                               Utils.getHighlightedText(reviewText, doc.get("path"),
						                           highlightBuilder.build(), porterAnalyzer), doc.get("path")));
				else
					results.add(new BusinessResult(resName, resCity, resAddress, resCount, resRating, " ",
					                               doc.get("path")));

			}
			
		}
		
		return results;

    }
	
	
}


