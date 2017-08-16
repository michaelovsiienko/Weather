package androidprojectsw.com.weather.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidprojectsw.com.weather.Constants;
import androidprojectsw.com.weather.R;


public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Toolbar mToolbar;
    private ListView mListViewCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolbar = findViewById(R.id.toolbar);
        mListViewCities = findViewById(R.id.lv_cities);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.cities));
        mListViewCities.setAdapter(adapter);

        mListViewCities.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CITY_NAME,mListViewCities.getItemAtPosition(i).toString());
        setResult(RESULT_OK,intent);
        finish();
    }
}
