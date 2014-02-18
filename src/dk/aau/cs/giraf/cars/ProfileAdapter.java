package dk.aau.cs.giraf.cars;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class ProfileAdapter extends BaseAdapter {

	private List<Profile> data;
	private static LayoutInflater inflater=null;

	public ProfileAdapter(Activity activity, List<Profile> data) {
		this.data=data;
		inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(convertView==null)
			view = inflater.inflate(R.layout.profile_list_item, null);

		Profile profile = this.data.get(position);

		TextView name = (TextView) view.findViewById(R.id.profileName);
		name.setText(profile.getFirstname()+" "+profile.getSurname());
		//imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
		return view;
	}
}


