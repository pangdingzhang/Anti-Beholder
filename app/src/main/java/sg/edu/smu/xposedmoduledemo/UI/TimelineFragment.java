package sg.edu.smu.xposedmoduledemo.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.smu.xposedmoduledemo.R;

public class TimelineFragment extends Fragment {
    private RecyclerView rv;
    private TimelineAdapter timelineAdapter;

    public TimelineFragment(){
        super(R.layout.report);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.report, container, false);
        rv = view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        rv.addItemDecoration(new TimelineItemDecoration());
        rv.setAdapter(new TimelineAdapter(getContext()));
        return view;

    }
}
