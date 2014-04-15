package sk.upjs.ics.android.markdownr;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class MarkdownPreviewFragment extends Fragment {
	public static final String ENCODING = "utf-8";
	public static final String CONTENT_TYPE = "text/html";
	private static final String DEFAULT_HTML = "<i>No Markdown source</i>";
	private static final String ARG_KEY_HTML_DATA = "htmlData";
	private WebView webView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_markdown_preview, container, false);
		
		webView = (WebView) view.findViewById(R.id.web_view_markdown);
		webView.loadData(getHtmlData(), CONTENT_TYPE, ENCODING);
		return view;
	}
	
	private String getHtmlData() {
		Bundle args = getArguments();
		if(args != null && args.containsKey(ARG_KEY_HTML_DATA)) {
			return args.getString(ARG_KEY_HTML_DATA);
		}
		return DEFAULT_HTML;
	}	
		
	public static MarkdownPreviewFragment newInstance(String htmlData) {
		MarkdownPreviewFragment fragment = new MarkdownPreviewFragment();
		Bundle args = new Bundle();
		args.putString(ARG_KEY_HTML_DATA, htmlData);
		
		fragment.setArguments(args);
		
		return fragment;
	}	
}
