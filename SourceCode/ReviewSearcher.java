package prj;
import java.io.IOException;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
import org.eclipse.swt.widgets.Text;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ReviewSearcher {

	protected Shell shlReviewSearcher;
	private Text restaurantName;
	private Text reviewText;
	private Text dateLow;
	private Text usefulLow;
	private Text funnyLow;
	private Text ratingLow;
	
	//static String index       = "D:\\Temp\\yelp\\reviewIndex\\";	//used in eclipse project
	static String index       = "reviewIndex/";				//used in jar executable
	
	Locale locale = Locale.UK;
	DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, locale);
	

	static IndexReader reader;
	static IndexSearcher searcher;

	private Text usefulHigh;
	private Text ratingHigh;
	private Text funnyHigh;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private Table resultTable;
	private Text dateHigh;
	private Text coolLow;
	private Text coolHigh;



	/**
	 * Launch the application.
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		searcher = new IndexSearcher(reader);
		
		try {
			ReviewSearcher window = new ReviewSearcher();
			window.open();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlReviewSearcher.open();
		shlReviewSearcher.layout();
		while (!shlReviewSearcher.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shlReviewSearcher = new Shell();
		shlReviewSearcher.setSize(1280, 720);
		shlReviewSearcher.setText("Review Searcher");
		
		
		Label lblRestaurantName = new Label(shlReviewSearcher, SWT.NONE);
		lblRestaurantName.setBounds(10, 10, 136, 15);
		lblRestaurantName.setText("Restaurant Name");
		restaurantName = new Text(shlReviewSearcher, SWT.BORDER);
		restaurantName.setBounds(152, 7, 198, 21);
		
		
		Label lblDate = new Label(shlReviewSearcher, SWT.NONE);
		lblDate.setText("Date");
		lblDate.setBounds(10, 100, 55, 15);
		Label lblDateFrom = new Label(shlReviewSearcher, SWT.NONE);
		lblDateFrom.setText("From:");
		lblDateFrom.setBounds(91, 100, 55, 15);
		dateLow = new Text(shlReviewSearcher, SWT.BORDER);
		dateLow.setBounds(152, 97, 198, 21);
		Label lblDateTo = new Label(shlReviewSearcher, SWT.NONE);
		lblDateTo.setText("To:");
		lblDateTo.setBounds(91, 127, 55, 15);
		dateHigh = new Text(shlReviewSearcher, SWT.BORDER);
		dateHigh.setBounds(152, 124, 198, 21);
		
		
		Label lblUseful = new Label(shlReviewSearcher, SWT.NONE);
		lblUseful.setText("Useful count");
		lblUseful.setBounds(10, 223, 70, 15);
		Label lblUsefulFrom = new Label(shlReviewSearcher, SWT.NONE);
		lblUsefulFrom.setText("From:");
		lblUsefulFrom.setBounds(91, 223, 55, 15);
		usefulLow = new Text(shlReviewSearcher, SWT.BORDER);
		usefulLow.setBounds(152, 220, 198, 21);
		Label lblUsefulTo = new Label(shlReviewSearcher, SWT.NONE);
		lblUsefulTo.setText("To:");
		lblUsefulTo.setBounds(91, 250, 55, 15);
		usefulHigh = new Text(shlReviewSearcher, SWT.BORDER);
		usefulHigh.setBounds(152, 247, 198, 21);
		

		Label lblFunny = new Label(shlReviewSearcher, SWT.NONE);
		lblFunny.setText("Funny count");
		lblFunny.setBounds(10, 324, 70, 15);
		Label lblFunnyFrom = new Label(shlReviewSearcher, SWT.NONE);
		lblFunnyFrom.setText("From:");
		lblFunnyFrom.setBounds(91, 324, 55, 15);
		funnyLow = new Text(shlReviewSearcher, SWT.BORDER);
		funnyLow.setBounds(152, 321, 198, 21);
		Label lblFunnyTo = new Label(shlReviewSearcher, SWT.NONE);
		lblFunnyTo.setText("To:");
		lblFunnyTo.setBounds(91, 351, 55, 15);
		funnyHigh = new Text(shlReviewSearcher, SWT.BORDER);
		funnyHigh.setBounds(152, 348, 198, 21);

		
		Label lblCool = new Label(shlReviewSearcher, SWT.NONE);
		lblCool.setText("Cool");
		lblCool.setBounds(10, 413, 55, 15);
		Label lblCoolFrom = new Label(shlReviewSearcher, SWT.NONE);
		lblCoolFrom.setText("From:");
		lblCoolFrom.setBounds(91, 413, 55, 15);
		coolLow = new Text(shlReviewSearcher, SWT.BORDER);
		coolLow.setBounds(152, 407, 198, 21);
		Label lblCoolTo = new Label(shlReviewSearcher, SWT.NONE);
		lblCoolTo.setText("To:");
		lblCoolTo.setBounds(91, 440, 55, 15);		
		coolHigh = new Text(shlReviewSearcher, SWT.BORDER);
		coolHigh.setBounds(152, 434, 198, 21);
		
		

		Label lblRating = new Label(shlReviewSearcher, SWT.NONE);
		lblRating.setText("Rating");
		lblRating.setBounds(10, 496, 55, 15);
		Label lblRatingFrom = new Label(shlReviewSearcher, SWT.NONE);
		lblRatingFrom.setText("From:");
		lblRatingFrom.setBounds(91, 496, 55, 15);
		ratingLow = new Text(shlReviewSearcher, SWT.BORDER);
		ratingLow.setBounds(152, 493, 198, 21);
		Label lblRatingTo = new Label(shlReviewSearcher, SWT.NONE);
		lblRatingTo.setText("To:");
		lblRatingTo.setBounds(91, 532, 55, 15);
		ratingHigh = new Text(shlReviewSearcher, SWT.BORDER);
		ratingHigh.setBounds(152, 529, 198, 21);

			
		Label lblReviewText = new Label(shlReviewSearcher, SWT.NONE);
		lblReviewText.setBounds(10, 590, 107, 15);
		lblReviewText.setText("Review Text");	
		reviewText = new Text(shlReviewSearcher, SWT.BORDER);
		reviewText.setBounds(152, 587, 198, 41);

		
		Button btnSortByUsefulCnt = new Button(shlReviewSearcher, SWT.CHECK);
		btnSortByUsefulCnt.setBounds(381, 587, 136, 16);
		btnSortByUsefulCnt.setText("Sort by useful count");

	
		Button btnSortByDate = new Button(shlReviewSearcher, SWT.CHECK);
		btnSortByDate.setBounds(381, 609, 151, 16);
		btnSortByDate.setText("Sort by review date");

		
		/*************Result table*************/
		
		resultTable = formToolkit.createTable(shlReviewSearcher, SWT.NONE);
		resultTable.setBounds(381, 10, 873, 537);
		formToolkit.paintBordersFor(resultTable);
		resultTable.setHeaderVisible(true);
		resultTable.setLinesVisible(true);

		TableColumn tblclmnRestName = new TableColumn(resultTable, SWT.NONE, 0);
		tblclmnRestName.setWidth(250);
		tblclmnRestName.setText("Restaurant Name");

		TableColumn tblclmnDate = new TableColumn(resultTable, SWT.NONE, 1);
		tblclmnDate.setWidth(180);
		tblclmnDate.setText("Date");
		
		TableColumn tblclmnUseful = new TableColumn(resultTable, SWT.NONE, 2);
		tblclmnUseful.setWidth(50);
		tblclmnUseful.setText("Useful");
		
		TableColumn tblclmnReviewcount = new TableColumn(resultTable, SWT.NONE, 3);
		tblclmnReviewcount.setWidth(50);
		tblclmnReviewcount.setText("Funny");
		
		TableColumn tblclmnCool = new TableColumn(resultTable, SWT.NONE, 4);
		tblclmnCool.setWidth(50);
		tblclmnCool.setText("Cool");
		
		TableColumn tblclmnRating = new TableColumn(resultTable, SWT.NONE, 5);
		tblclmnRating.setWidth(50);
		tblclmnRating.setText("Rating");
		
		TableColumn tblclmnText = new TableColumn(resultTable, SWT.NONE, 6);
		tblclmnText.setWidth(300);
		tblclmnText.setText("Review text");
		
		
		Button btnRepResults = new Button(shlReviewSearcher, SWT.CHECK);
		btnRepResults.setText("Enable representative results functionality");
		btnRepResults.setBounds(696, 566, 256, 16);
		formToolkit.adapt(btnRepResults, true, true);

		Label lblResultsMult = new Label(shlReviewSearcher, SWT.NONE);
		lblResultsMult.setBounds(757, 644, 151, 15);
		
		
		Button btnLimitResults = new Button(shlReviewSearcher, SWT.CHECK);
		btnLimitResults.setText("Limit results to 1000");
		btnLimitResults.setBounds(696, 590, 256, 16);

		resultTable.addListener(SWT.MouseDoubleClick, new Listener() {
			public void handleEvent(Event e) {
				TableItem[] selection = resultTable.getSelection();
				for (int i = 0; i < selection.length; i++) {
					//Program.launch((String) selection[i].getData());	//Open corresponding file in corpus
																		//using system default program
					MessageBox dialog =
					    new MessageBox(shlReviewSearcher, SWT.OK| SWT.CANCEL);
 					dialog.setText("Review summary");
					dialog.setMessage(selection[i].getText(6));


					dialog.open();
				}
			}
		});
		
		
		/*************Perform search and output results*************/
		
		Button btnSearchButton = new Button(shlReviewSearcher, SWT.NONE);
		btnSearchButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				ArrayList<ReviewResult> hits = new ArrayList<ReviewResult>();
				int sortingMethod = 0;	//1 for useful count, 2 for date
				resultTable.removeAll();

				
				
				

				if (btnSortByDate.getSelection() && btnSortByUsefulCnt.getSelection()) {
					System.out.println("Can not sort using both criteria at the same time");

					MessageBox dialog =
					    new MessageBox(shlReviewSearcher, SWT.ICON_WARNING | SWT.OK| SWT.CANCEL);
					dialog.setText("Error message");
					dialog.setMessage("Can not sort using both criteria at the same time.");

					// open dialog and await user selection
					//int selection = dialog.open();
					dialog.open();
					return;
				}else if (btnSortByUsefulCnt.getSelection()) {
					sortingMethod = 1;
				}else if (btnSortByDate.getSelection()) {
					sortingMethod = 2;
				}
				
				
				try {
					hits = SearchReviewFiles.PerformSearch(searcher,
							restaurantName.getText(), 
							dateLow.getText(),   dateHigh.getText(),
							usefulLow.getText(), usefulHigh.getText(),
							funnyLow.getText(),  funnyHigh.getText(),
							coolLow.getText(),   coolHigh.getText(),
							ratingLow.getText(), ratingHigh.getText(),
							reviewText.getText(), sortingMethod,
							btnRepResults.getSelection(), btnLimitResults.getSelection());
				} catch (IOException | ParseException e2) {
					System.out.println(e2.getMessage());
				}
				

				
				int resultSize = Math.min(10000, hits.size());
				lblResultsMult.setText("" + resultSize + " results found");

				
				Date reviewDate = null;
				String formattedDate = null;
				
				for (int i = 0; i < resultSize; i++) {

					try {
						reviewDate = DateTools.stringToDate(hits.get(i).getDate());
						formattedDate = df.format(reviewDate);
					} catch (java.text.ParseException e1) {
						System.out.println("e1");
						e1.printStackTrace();	
					}

					TableItem item = new TableItem(resultTable, SWT.NONE);
					ReviewResult res = hits.get(i);
					item.setText(0, res.getName());
					item.setText(1, formattedDate);
					item.setText(2, res.getUsefulCount());
					item.setText(3, res.getFunnyCount());
					item.setText(4, res.getCoolCount());
					item.setText(5, res.getRating());
					item.setText(6, res.getMatchedText());
					item.setData(res.getPath());
				}
				
			}
		});
		
		btnSearchButton.setBounds(757, 613, 136, 25);
		btnSearchButton.setText("Search Reviews");

	}
}
