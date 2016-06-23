package com.hcpt.fastfood.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.object.PromotionObject;
import com.hcpt.fastfood.utility.DateUtil;

public class PromotionAdapter extends BaseAdapter {

	private ArrayList<PromotionObject> listPromotion;
	private LayoutInflater mInflater;
	public boolean isAssignScreen = false;
	private Context context;
	private AQuery lstAq;

	public PromotionAdapter(Context activity,
			ArrayList<PromotionObject> listPromotion) {
		this.listPromotion = listPromotion;
		this.mInflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = activity;
		lstAq = new AQuery(activity);
	}

	public ArrayList<PromotionObject> getListOrderHistory() {
		return listPromotion;
	}

	public void setListOrderHistory(ArrayList<PromotionObject> listPromotion) {
		this.listPromotion = listPromotion;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return mArrCategory.size();
		return listPromotion.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listPromotion.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final HolderView holder;
		if (convertView == null) {
			holder = new HolderView();
			convertView = mInflater.inflate(R.layout.row_promotion_list_item,
					null);
			holder.lblTitle = (TextView) convertView
					.findViewById(R.id.lblTitle);
			holder.lblTitle.setSelected(true);
			holder.lblDate = (TextView) convertView.findViewById(R.id.lblDate);
			holder.lblDate.setSelected(true);
			holder.lblContent = (TextView) convertView
					.findViewById(R.id.lblContent);
			holder.imgImage = (ImageView) convertView
					.findViewById(R.id.imgImage);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		PromotionObject item = listPromotion.get(position);
		if (item != null) {
			holder.lblTitle.setText(item.getTitle());
			String date = context.getString(R.string.lbl_from)
					+ " "
					+ DateUtil.convertTimeStampToDate(item.getStartDate(),
							"dd MMM/yyyy")
					+ " "
					+ context.getString(R.string.lbl_to)
					+ " "
					+ DateUtil.convertTimeStampToDate(item.getEndDate(),
							"dd MMM/yyyy");
			holder.lblDate.setText(date);
			holder.lblContent.setText(item.getDescription());

			// Load Image
			AQuery aq = lstAq.recycle(convertView);
			aq.id(holder.imgImage)
					.progress(holder.progess)
					.image(item.getImage(), true, true, 0, R.drawable.no_image,
							new BitmapAjaxCallback() {
								@SuppressLint("NewApi")
								@Override
								public void callback(String url, ImageView iv,
										Bitmap bm, AjaxStatus status) {
									if (bm != null) {
										Drawable d = new BitmapDrawable(context
												.getResources(), bm);
										holder.imgImage
												.setBackgroundDrawable(d);
									} else {
										holder.imgImage
												.setBackgroundResource(R.drawable.no_image);
									}
								}
							});
		}
		return convertView;
	}

	public class HolderView {
		private TextView lblTitle, lblDate, lblContent;
		private ImageView imgImage;
		private ProgressBar progess;
	}

}
