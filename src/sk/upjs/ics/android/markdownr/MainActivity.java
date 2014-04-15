package sk.upjs.ics.android.markdownr;

import java.io.StringReader;
import java.io.StringWriter;

import org.tautua.markdownpapers.Markdown;
import org.tautua.markdownpapers.parser.ParseException;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity  {
	private static enum Mode { SOURCE, PREVIEW, BOTH };
	
	private Mode mode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(isSinglePane()) {
			showMarkdownSource();			
		} else {
			mode = Mode.BOTH;
		}
		
	}
	
	public boolean isSinglePane() {
		return findViewById(R.id.layout_root) != null;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem showHtmlMenu = menu.findItem(R.id.show_html_menu);
		MenuItem showHtmlSourceMenu = menu.findItem(R.id.show_source_menu);
		MenuItem refreshMenu = menu.findItem(R.id.refresh_menu);
	    
		if(mode == Mode.SOURCE) {
			showHtmlMenu.setVisible(true);
			showHtmlSourceMenu.setVisible(false);
			refreshMenu.setVisible(false);
		} else if (mode == Mode.PREVIEW) {
			showHtmlMenu.setVisible(false);
			showHtmlSourceMenu.setVisible(true);
			refreshMenu.setVisible(false);
		} else {
			showHtmlMenu.setVisible(false);
			showHtmlSourceMenu.setVisible(false);
			refreshMenu.setVisible(true);
		}
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.show_html_menu:
			showHtml();
			supportInvalidateOptionsMenu();
			return true;
		case R.id.show_source_menu:
			showMarkdownSource();
			supportInvalidateOptionsMenu();
			return true;
		case R.id.refresh_menu:
			refreshPreview();
			return true;
		case R.id.new_file_menu:
			clearMarkdownCode();
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	private void clearMarkdownCode() {
		EditText htmlSourceEditText = (EditText) findViewById(R.id.edit_text_markdown_source);
		htmlSourceEditText.setText("");
	}

	private void refreshPreview() {
		AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				StringReader in = new StringReader(params[0]);
				StringWriter out = new StringWriter();
				try {
					Markdown md = new Markdown();
					md.transform(in, out);
				} catch (ParseException e) {
					return "<i>Syntax error</i>";
				}
				return out.toString();
			}
			
			@Override
			protected void onPostExecute(String result) {
				WebView webView = (WebView) findViewById(R.id.web_view_markdown);
				webView.loadData(result, MarkdownPreviewFragment.CONTENT_TYPE, MarkdownPreviewFragment.ENCODING);
			}
		};
		
		task.execute(getSourceCode());
	}

	private void showMarkdownSource() {
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.layout_root, new MarkdownSourceFragment())
			.commit();
		mode = Mode.SOURCE;
	}
	
	private void showHtml() {
		MarkdownPreviewFragment fragment = MarkdownPreviewFragment.newInstance(getSourceCode());
		
		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.layout_root, fragment)
			.commit();
		mode = Mode.PREVIEW;
		
		refreshPreview();
	}	
	
	protected String getSourceCode() {
		EditText htmlSourceEditText = (EditText) findViewById(R.id.edit_text_markdown_source);
		return htmlSourceEditText.getText().toString();
	}
}
