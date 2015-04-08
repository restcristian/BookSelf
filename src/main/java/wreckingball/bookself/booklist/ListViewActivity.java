package wreckingball.bookself.booklist;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.cristianrestituyo.mycameratest.R;

public class ListViewActivity extends Fragment {

    private View context;
    private ListView bookList;
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        ListView bookList = (ListView)findViewById(R.id.listView1);
        dataAdapter da = new dataAdapter();
        bookList.setAdapter(da);
    }
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_list_view, container, false);
        context = v;

        bookList = (ListView)context.findViewById(R.id.listView1);
        dataAdapter da = new dataAdapter();
        bookList.setAdapter(da);
        return v;

    }


}
