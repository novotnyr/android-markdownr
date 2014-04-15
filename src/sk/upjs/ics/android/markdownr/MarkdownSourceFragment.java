package sk.upjs.ics.android.markdownr;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MarkdownSourceFragment extends Fragment {
	
	private static final String PREFERENCE_KEY_MARKDOWN_SOURCE = "markdownSource";

	
	private EditText markdownSourceEditText;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_markdown_source, container, false);
		markdownSourceEditText = (EditText) view.findViewById(R.id.edit_text_markdown_source);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		SharedPreferences preferences = getActivity().getPreferences(Activity.MODE_PRIVATE);
		String htmlSource = preferences.getString(PREFERENCE_KEY_MARKDOWN_SOURCE, "");
		markdownSourceEditText.setText(htmlSource);
	}
	
	@Override
	public void onPause() {
		SharedPreferences preferences = getActivity().getPreferences(Activity.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(PREFERENCE_KEY_MARKDOWN_SOURCE, markdownSourceEditText.getText().toString());
		editor.commit();
		
		super.onPause();
	}
	
}
