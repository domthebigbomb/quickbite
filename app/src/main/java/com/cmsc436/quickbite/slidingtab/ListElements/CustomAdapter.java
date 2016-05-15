package com.cmsc436.quickbite.slidingtab.ListElements;

import java.util.List;
//import com.cmsc436.quickbite.tabbedview.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmsc436.quickbite.R;

public class CustomAdapter extends BaseAdapter {

	Context context;
	List<RowItem> rowItems;

	CustomAdapter(Context context, List<RowItem> rowItems) {
		this.context = context;
		this.rowItems = rowItems;
	}

	@Override
	public int getCount() {
		return rowItems.size();
	}

	@Override
	public Object getItem(int position) {
		return rowItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return rowItems.indexOf(getItem(position));
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView icon_pic;
		TextView Location_names;
		TextView address;
		TextView distance;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.Location_names = (TextView) convertView
					.findViewById(R.id.Location_names);
			holder.icon_pic = (ImageView) convertView
					.findViewById(R.id.icon_pic);
			holder.address = (TextView) convertView.findViewById(R.id.address);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);

			RowItem row_pos = rowItems.get(position);

			holder.icon_pic.setImageResource(row_pos.geticon_id());
			holder.Location_names.setText(row_pos.getlocation_name());
			holder.address.setText(row_pos.getaddress());
			holder.distance.setText(row_pos.getdistance());

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			//FIX IS HERE SEE http://stackoverflow.com/questions/23995052/repeating-items-in-listview
			RowItem row_pos = rowItems.get(position);
			holder.icon_pic.setImageResource(row_pos.geticon_id());
			holder.Location_names.setText(row_pos.getlocation_name());
			holder.address.setText(row_pos.getaddress());
			holder.distance.setText(row_pos.getdistance());



		}

		return convertView;
	}

}
