package sg.edu.smu.xposedmoduledemo.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.smu.xposedmoduledemo.R;

public class TimelineFragment extends Fragment {
    private RecyclerView rv;
    private Spinner spinner;
    private TimelineAdapter timelineAdapter;
    private String[] permissionArray = {"All Permission", "ACCESS_FINE_LOCATION", "READ_CONTACTS"};

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
        timelineAdapter = new TimelineAdapter(getContext(),"All Permission");
        rv.setAdapter(timelineAdapter);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this.getActivity(),
                R.layout.support_simple_spinner_dropdown_item,permissionArray);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner = view.findViewById(R.id.report_spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                timelineAdapter = new TimelineAdapter(getContext(),permissionArray[i]);
                rv.setAdapter(timelineAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;

    }
}
