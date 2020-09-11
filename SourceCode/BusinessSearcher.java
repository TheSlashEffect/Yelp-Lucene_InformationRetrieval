package prj;
import java.io.IOException;

import java.nio.file.Paths;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Text;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class BusinessSearcher {

	protected Shell shlRestaurantSearcher;
	private Text restaurantName;
	private Text reviewText;
	private Text neighbourhood;
	private Text address;
	private Text city;
	private Text state;
	private Text postalCode;
	private Text latitudeLow;
	private Text longitudeLow;
	private Text ratingLow;
	
	//static String index       = "D:\\Temp\\yelp\\businessIndex\\";	//used in eclipse project, Windows
	static String index       = "businessIndex/";				//used in jar executable, Linux

	static IndexReader reader;
	static IndexSearcher searcher;

	private Text latitudeHigh;
	private Text ratingHigh;
	private Text longitudeHigh;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table resultTable;

	/**
	 * Launch the application.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		searcher = new IndexSearcher(reader);
		
		try {
			BusinessSearcher window = new BusinessSearcher();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlRestaurantSearcher.open();
		shlRestaurantSearcher.layout();
		while (!shlRestaurantSearcher.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlRestaurantSearcher = new Shell();
		shlRestaurantSearcher.setSize(1280, 720);
		shlRestaurantSearcher.setText("Restaurant Searcher");
		
		
		Label lblRestaurantName = new Label(shlRestaurantSearcher, SWT.NONE);
		lblRestaurantName.setBounds(10, 10, 136, 15);
		lblRestaurantName.setText("Restaurant Name");
		restaurantName = new Text(shlRestaurantSearcher, SWT.BORDER);
		restaurantName.setBounds(152, 7, 198, 21);
		
		
		Label lblCity = new Label(shlRestaurantSearcher, SWT.NONE);
		lblCity.setText("City");
		lblCity.setBounds(10, 51, 55, 15);
		city = new Text(shlRestaurantSearcher, SWT.BORDER);
		city.setBounds(152, 48, 198, 21);		

		
		Label lblAddress = new Label(shlRestaurantSearcher, SWT.NONE);
		lblAddress.setBounds(10, 90, 55, 15);
		lblAddress.setText("Address");
		address = new Text(shlRestaurantSearcher, SWT.BORDER);
		address.setBounds(152, 87, 198, 21);


		Label lblNeighbourhood = new Label(shlRestaurantSearcher, SWT.NONE);
		lblNeighbourhood.setBounds(10, 138, 114, 30);
		lblNeighbourhood.setText("Neighbourhood");
		neighbourhood = new Text(shlRestaurantSearcher, SWT.BORDER);
		neighbourhood.setBounds(152, 135, 198, 21);
		
	
		Label lblState = new Label(shlRestaurantSearcher, SWT.NONE);
		lblState.setText("State");
		lblState.setBounds(10, 196, 55, 15);
		state = new Text(shlRestaurantSearcher, SWT.BORDER);
		state.setBounds(152, 193, 198, 21);
	
		
		Label lblPostalCode = new Label(shlRestaurantSearcher, SWT.NONE);
		lblPostalCode.setText("PostalCode");
		lblPostalCode.setBounds(10, 254, 70, 15);
		postalCode = new Text(shlRestaurantSearcher, SWT.BORDER);
		postalCode.setBounds(152, 251, 198, 21);
	
		
		Label lblLatitude = new Label(shlRestaurantSearcher, SWT.NONE);
		lblLatitude.setText("Latitude");
		lblLatitude.setBounds(10, 315, 55, 25);
		Label lblLatitudeFrom = new Label(shlRestaurantSearcher, SWT.NONE);
		lblLatitudeFrom.setText("From:");
		lblLatitudeFrom.setBounds(91, 315, 55, 15);
		latitudeLow = new Text(shlRestaurantSearcher, SWT.BORDER);
		latitudeLow.setBounds(152, 312, 198, 21);
		Label lblLatitudeTo = new Label(shlRestaurantSearcher, SWT.NONE);
		lblLatitudeTo.setText("To:");
		lblLatitudeTo.setBounds(91, 342, 55, 15);
		latitudeHigh = new Text(shlRestaurantSearcher, SWT.BORDER);
		latitudeHigh.setBounds(152, 339, 198, 21);
		

		Label lblLongitude = new Label(shlRestaurantSearcher, SWT.NONE);
		lblLongitude.setText("Longitude:");
		lblLongitude.setBounds(10, 406, 70, 18);
		Label lblLongitudeFrom = new Label(shlRestaurantSearcher, SWT.NONE);
		lblLongitudeFrom.setText("From:");
		lblLongitudeFrom.setBounds(91, 406, 55, 15);
		longitudeLow = new Text(shlRestaurantSearcher, SWT.BORDER);
		longitudeLow.setBounds(152, 403, 198, 21);
		Label lblLongitudeTo = new Label(shlRestaurantSearcher, SWT.NONE);
		lblLongitudeTo.setText("To:");
		lblLongitudeTo.setBounds(91, 433, 55, 15);
		longitudeHigh = new Text(shlRestaurantSearcher, SWT.BORDER);
		longitudeHigh.setBounds(152, 430, 198, 21);


		Label lblRating = new Label(shlRestaurantSearcher, SWT.NONE);
		lblRating.setText("Rating");
		lblRating.setBounds(10, 496, 55, 15);
		Label lblRatingFrom = new Label(shlRestaurantSearcher, SWT.NONE);
		lblRatingFrom.setText("From:");
		lblRatingFrom.setBounds(91, 496, 55, 15);
		ratingLow = new Text(shlRestaurantSearcher, SWT.BORDER);
		ratingLow.setBounds(152, 493, 198, 21);
		Label lblRatingTo = new Label(shlRestaurantSearcher, SWT.NONE);
		lblRatingTo.setText("To:");
		lblRatingTo.setBounds(91, 532, 55, 15);
		ratingHigh = new Text(shlRestaurantSearcher, SWT.BORDER);
		ratingHigh.setBounds(152, 529, 198, 21);

			
		Label lblReviewText = new Label(shlRestaurantSearcher, SWT.NONE);
		lblReviewText.setBounds(10, 590, 107, 15);
		lblReviewText.setText("Review Text");	
		reviewText = new Text(shlRestaurantSearcher, SWT.BORDER);
		reviewText.setBounds(152, 587, 198, 41);

		
		Button btnSortByRevCnt = new Button(shlRestaurantSearcher, SWT.CHECK);
		btnSortByRevCnt.setBounds(381, 587, 191, 16);
		btnSortByRevCnt.setText("Sort by review count");

	
		Button btnSortByRating = new Button(shlRestaurantSearcher, SWT.CHECK);
		btnSortByRating.setBounds(381, 609, 191, 16);
		btnSortByRating.setText("Sort by business rating");

		
		
		resultTable = formToolkit.createTable(shlRestaurantSearcher, SWT.NONE);
		resultTable.setBounds(381, 10, 873, 537);
		formToolkit.paintBordersFor(resultTable);
		resultTable.setHeaderVisible(true);
		resultTable.setLinesVisible(true);

		TableColumn tblclmnRestName = new TableColumn(resultTable, SWT.NONE, 0);
		tblclmnRestName.setWidth(300);
		tblclmnRestName.setText("Restaurant Name");

		TableColumn tblclmnCity = new TableColumn(resultTable, SWT.NONE, 1);
		tblclmnCity.setWidth(150);
		tblclmnCity.setText("City");
		
		TableColumn tblclmnAddress = new TableColumn(resultTable, SWT.NONE, 2);
		tblclmnAddress.setWidth(150);
		tblclmnAddress.setText("Address");
		
		TableColumn tblclmnReviewcount = new TableColumn(resultTable, SWT.NONE, 3);
		tblclmnReviewcount.setWidth(85);
		tblclmnReviewcount.setText("Review Count");
		
		TableColumn tblclmnRating = new TableColumn(resultTable, SWT.NONE, 4);
		tblclmnRating.setWidth(50);
		tblclmnRating.setText("Rating");
		
		TableColumn tblclmnMatchedText = new TableColumn(resultTable, SWT.NONE, 5);
		tblclmnMatchedText.setWidth(150);
		tblclmnMatchedText.setText("Matched Text");
		
		resultTable.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event e) {
				TableItem[] selection = resultTable.getSelection();
				for (int i = 0; i < selection.length; i++) {
					Program.launch((String) selection[i].getData());
				}
			}
		});
		
		Label lblResultsMult = new Label(shlRestaurantSearcher, SWT.NONE);
		lblResultsMult.setBounds(757, 636, 195, 15);
		
		Button btnRepResults = new Button(shlRestaurantSearcher, SWT.CHECK);
		btnRepResults.setText("Enable representative results functionality");
		btnRepResults.setBounds(696, 566, 282, 16);
		formToolkit.adapt(btnRepResults, true, true);
		
		
		Button btnLimitResults = new Button(shlRestaurantSearcher, SWT.CHECK);
		btnLimitResults.setText("Limit results to 1000");
		btnLimitResults.setSelection(true);
		btnLimitResults.setBounds(696, 587, 256, 16);
		

		Button btnShowMatched = new Button(shlRestaurantSearcher, SWT.CHECK);
		btnShowMatched.setText("Show matched text");
		btnShowMatched.setBounds(1118, 566, 136, 16);

		Button btnSearchButton = new Button(shlRestaurantSearcher, SWT.NONE);
		btnSearchButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				ArrayList<BusinessResult> hits = new ArrayList<BusinessResult>();
				int sortingMethod = 0;	//1 for review count, 2 for rating
				resultTable.removeAll();


				if (btnSortByRating.getSelection() && btnSortByRevCnt.getSelection()) {
					System.out.println("Can not sort using both criteria at the same time");

					MessageBox dialog =
					    new MessageBox(shlRestaurantSearcher, SWT.ICON_WARNING | SWT.OK| SWT.CANCEL);
					dialog.setText("Error message");
					dialog.setMessage("Can not sort using both criteria at the same time\nAlso, life is meaningless");

					
					dialog.open();
					return;
				}else if (btnSortByRevCnt.getSelection()) {
					sortingMethod = 1;
				}else if (btnSortByRating.getSelection()) {
					sortingMethod = 2;
				}
					
				
				
				
				try {
					hits = SearchBusinessFiles.TestSearch(searcher,
							restaurantName.getText(), neighbourhood.getText(), address.getText(),
							city.getText(), state.getText(), postalCode.getText(),
							latitudeLow.getText(),  latitudeHigh.getText(),
							longitudeLow.getText(), longitudeHigh.getText(),
							ratingLow.getText(), ratingHigh.getText(),
							reviewText.getText(), sortingMethod,
							btnRepResults.getSelection(),
							btnLimitResults.getSelection(),
							btnShowMatched.getSelection());
				} catch (IOException | ParseException e2) {
					System.out.println(e2.getMessage());
				}

				
				int resultSize = Math.min(10000, hits.size());
				lblResultsMult.setText("" + resultSize + " results found");
				
				for (int i = 0; i < resultSize; i++) {

					TableItem     item = new TableItem(resultTable, SWT.NONE);
					BusinessResult res = hits.get(i);
					item.setText(0, res.getName());
					item.setText(1, res.getCity());
					item.setText(2, res.getAddress());
					item.setText(3, res.getReviewCount());
					item.setText(4, res.getRating());
					item.setText(5, res.getMatchedText());
					item.setData(   res.getFilePath());
				}
				
			}
		});
		
		
		btnSearchButton.setBounds(757, 605, 136, 25);
		btnSearchButton.setText("Search Documents");
			

	}
}
