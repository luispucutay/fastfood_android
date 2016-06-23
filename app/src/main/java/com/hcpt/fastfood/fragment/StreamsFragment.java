package com.hcpt.fastfood.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hcpt.fastfood.BaseFragment;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.activity.StreamsYouTobeActivity;
import com.hcpt.fastfood.adapter.StreamsAdapter;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;
import com.hcpt.fastfood.modelmanager.ParserUtility;
import com.hcpt.fastfood.object.Stream;

import java.io.Serializable;
import java.util.ArrayList;

public class StreamsFragment extends BaseFragment {
    public static final String TAG = "StreamsFragment";
    private ListView lvStreams;

    private ArrayList<Stream> mArrStreams;
    private StreamsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_streams, container, false);
        self = getActivity();
        initUI(view);
        getListStream();
        initControl();
        return view;
    }

    private void initControl() {
        lvStreams.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle sendBundle = new Bundle();
                Intent intent = new Intent(getActivity(), StreamsYouTobeActivity.class);
                sendBundle.putSerializable("streams", (Serializable) mArrStreams.get(position));
                sendBundle.putSerializable("listStreams", (Serializable) removeStreamsFromList(mArrStreams.get(position).getId(), mArrStreams));
                intent.putExtras(sendBundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left2);
            }
        });
    }

    private void initUI(View view) {
        lvStreams = (ListView) view.findViewById(R.id.lvStreams);
        mArrStreams = new ArrayList<>();
        adapter = new StreamsAdapter(self, mArrStreams);
        lvStreams.setAdapter(adapter);
    }

    private ArrayList<Stream> removeStreamsFromList(String id, ArrayList<Stream> array) {
        ArrayList<Stream> list = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            if (!id.equals(array.get(i).getId())) {
                list.add(array.get(i));
            }
        }
        return list;
    }

    private void getListStream() {
        ModelManager.getListStream(getActivity(), true, new ModelManagerListener() {
            @Override
            public void onSuccess(String json) {
                mArrStreams.addAll(ParserUtility.parseListStream(json));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                showToastMessage(getResources().getString(R.string.have_some_error));
            }
        });
    }
}
