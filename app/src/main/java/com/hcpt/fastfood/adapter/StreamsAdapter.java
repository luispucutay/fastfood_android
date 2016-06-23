package com.hcpt.fastfood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcpt.fastfood.R;
import com.hcpt.fastfood.object.Stream;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StreamsAdapter extends BaseAdapter {

	private ArrayList<Stream> mArrStreams;
	private LayoutInflater mInflater;
	private Context ctx;

	public StreamsAdapter(Context context, ArrayList<Stream> arrStreams) {
		this.ctx = context;
		this.mArrStreams = arrStreams;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mArrStreams.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrStreams.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final HolderView holder;
		if (convertView == null) {
			holder = new HolderView();
			convertView = mInflater.inflate(R.layout.row_list_streams, null);
			holder.img_streams = (ImageView) convertView.findViewById(R.id.img_streams);
			holder.tv_title_streams = (TextView) convertView.findViewById(R.id.tv_title_streams);
			holder.tv_title_streams.setSelected(true);
			holder.tv_date_streams = (TextView) convertView.findViewById(R.id.tv_date_streams);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Stream stream = mArrStreams.get(position);
		if (stream != null) {
			Picasso.with(ctx).load(stream.getThumbnail()).placeholder(R.drawable.no_image_available_horizontal).into(holder.img_streams);
			holder.tv_title_streams.setText(stream.getTitle());
			holder.tv_date_streams.setText(stream.getPublishedAt());
		}
		return convertView;
	}

	public class HolderView {
		private ImageView img_streams;
		private TextView tv_title_streams;
		private TextView tv_date_streams;
	}

}
