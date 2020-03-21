package com.sajjad.info;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sajjad.info.Adapter.PersonInfo;
import com.sajjad.info.Adapter.PersonRecyclerAdapter;
import com.sajjad.info.Adapter.SliderPagerAdapter;
import com.sajjad.info.Adapter.ZoomTransaction;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class InfoActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    RecyclerView infoRecyclerView;
    PersonRecyclerAdapter personRecyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<PersonInfo> personInfos, personSubList;
    SQLiteHelper sqLiteHelper;
    //
    TabLayout sliderTabLayout;
    public ViewPager sliderPager;
    public SliderPagerAdapter sliderPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setUpViews();
        //
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                InfoActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (sliderPager.getCurrentItem() < personSubList.size() - 1) {
                            sliderPager.setCurrentItem(sliderPager.getCurrentItem() + 1);
                        } else {
                            sliderPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 2000, 2000);
    }

    private void setUpViews() {
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);
        // views
        infoRecyclerView = findViewById(R.id.infoRecyclerView);
        // get all person information
        sqLiteHelper = new SQLiteHelper(this);
        personInfos = sqLiteHelper.getAll();
        personSubList = sqLiteHelper.getLastThree();
        // recycler view settings
        layoutManager = new LinearLayoutManager(this);
        infoRecyclerView.setLayoutManager(layoutManager);
        personRecyclerAdapter = new PersonRecyclerAdapter(this, personInfos);
        infoRecyclerView.setAdapter(personRecyclerAdapter);
        // slider settings
        sliderTabLayout = findViewById(R.id.sliderTabLayout);
        sliderPager = findViewById(R.id.sliderPager);
        sliderTabLayout.setupWithViewPager(sliderPager);

        sliderPagerAdapter = new SliderPagerAdapter(personSubList);
        sliderPager.setAdapter(sliderPagerAdapter);
        ZoomTransaction zoomTransaction = new ZoomTransaction();
        sliderPager.setPageTransformer(true, zoomTransaction);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem searchItem=menu.findItem(R.id.searchItem);

        SearchView searchView= (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                personRecyclerAdapter.searchPerson(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addItem) {
            AddUpdateFragment addUpdateFragment = AddUpdateFragment.newInstance(-1);
            addUpdateFragment.show(getSupportFragmentManager(), null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        personRecyclerAdapter.notifyDataSetChanged();
    }
}