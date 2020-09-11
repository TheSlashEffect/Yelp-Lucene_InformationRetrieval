package prj;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;

public class Utils {

	// Get review's text, and return best matching fragment (with highlighted matches)
	static String getHighlightedText(String reviewText, String docPath,
											 Query textQuery, Analyzer analyzer) {
		if (!reviewText.equals("")) {
			final Path docDir = Paths.get(docPath);
			try (InputStream stream = Files.newInputStream(docDir)){;
				BufferedReader fileReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

				//Skip file metadata
				for (int j = 0; j < 8; j++)
					fileReader.readLine();

				//Query reviewQuery = textParser.parse(reviewText);
				Highlighter textHighlighter = new Highlighter(new QueryScorer(textQuery));
				
				String text = fileReader.lines().collect(Collectors.joining());
				String highlightedText = textHighlighter.getBestFragment(analyzer, "reviewText", text);
				if (highlightedText != null) {
					return highlightedText;
				}else {
					return ("No text found");
				}

			}catch (Exception e) {
				//File not found, or other error
				//System.out.println(e.getMessage());
				return ("No text file found, or parse error. Make sure doc directory is in jar's directory.");
			}

		}else
			return ("No matched review text");
	}
	
}

