package com.vbz.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ArtistViewFragment extends ListFragment {
    private List<ArtistListViewItem> datalist; // holds data retrieved from API

    public ArtistViewFragment() { }

    // DUMMY TEST DATA
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++
    private final String[] data = {"adama", "elisabeth", "kate", "kerri", "nanako"};
    private final String[] pics = new String[] { "http://www.smashingmagazine.com/images/music-cd-covers/67.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/64.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/michael_jackson_dangerous-f.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/43.jpg",
                                                 "http://www.smashingmagazine.com/images/music-cd-covers/zeitgeist.jpg" };
    private static final String LOG_TAG = "SPOTSTREAMER";
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++

    private List<String> search(String query) {
        // TODO: retrieve data from network using spotify API
        Log.d(LOG_TAG, "search called");
        List<String> found = new ArrayList<String>();

        if (! TextUtils.isEmpty(query)) {
            // TODO: get real data from spotify wrapper here
            for (String s : data) {
                if (s.contains(query)) {
                    found.add(s);
                }
            }
        }

        return found;
    }

    public void setListdata(List<String> data) {
        datalist.clear();
        Resources resresolver = getResources(); // KDADEBUG: used to temp resolve local images

        if (data.size() > 0) {
            Log.d(LOG_TAG, "data found: " + data.toString());
            for (String i : data) {
                datalist.add(new ArtistListViewItem(resresolver.getDrawable(R.drawable.ic_launcher), i));
            }
            setListAdapter(new ArtistListViewAdapter(getActivity(), datalist));
//            setListShown(true);
        } else {
            // toast no data found
//            setListShown(false);
//            setEmptyText("no data found...");
            Log.d(LOG_TAG, "no data found");
//            Toast.makeText(getActivity(), "no data found for [" + query + "]", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public void onCreate(Bundle savedinstanceSate) {
        super.onCreate(savedinstanceSate);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        datalist = new ArrayList<ArtistListViewItem>();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        // set array adapter with empty data
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // let the fragment resolve searches
        // we get the theme for this activity so that text is rendered properly
        Context themectx = ((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext();
        SearchView sv = new SearchView(themectx);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                Log.d(LOG_TAG, "submitting query: " + query);
                List<String> data = search(query);
                setListdata(data);
                return true;
            }

            @Override public boolean onQueryTextChange(String newText) {
                Log.d(LOG_TAG, "listening for... " + newText);
                return true;
            }
        });
        sv.setQueryHint("Search for an artist...");
        sv.setOnCloseListener(null);
        sv.setIconifiedByDefault(true);

        // add a search icon to action bar
        inflater.inflate(R.menu.menu_mainfragment, menu);
        MenuItem searchview = menu.findItem(R.id.search);
        searchview.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        searchview.setActionView(sv);
    }

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO: display popular tracks for artist
        ArtistListViewItem item = datalist.get(position);
        Toast.makeText(getActivity(), "Item clicked: " + item.name, Toast.LENGTH_SHORT).show();

        Intent detailsIntent = new Intent(getActivity(), TrackActivity.class);
        detailsIntent.putExtra("tracks", 3);
        if (detailsIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(detailsIntent);
        }
        else {
            Toast.makeText(getActivity(), "could not find Detail Activity", Toast.LENGTH_SHORT).show();
        }
    }
}