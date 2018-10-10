package com.example.gabden.testrandomuser.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gabden.testrandomuser.R;
import com.example.gabden.testrandomuser.activity.adapters.RandomUserAdapter;
import com.example.gabden.testrandomuser.activity.models.Example;
import com.example.gabden.testrandomuser.activity.models.Result;
import com.example.gabden.testrandomuser.activity.services.RandomUserService;
import com.example.gabden.testrandomuser.activity.services.RetrofitClient;
import com.example.gabden.testrandomuser.activity.utils.DialogUtils;
import com.example.gabden.testrandomuser.activity.utils.NetworkUtils;
import com.example.gabden.testrandomuser.activity.utils.SharedPreferencesUtils;
import com.example.gabden.testrandomuser.activity.utils.SnackbarUtils;
import com.example.gabden.testrandomuser.activity.views.SimpleDividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UsersActivity extends AppCompatActivity {
    private List<Result> mRandomUsersList;

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.rv_recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                if (!NetworkUtils.isNetworkAvailable(this)) {
                    SnackbarUtils.showErrorSnackbar(this, R.string.network_unavailable);
                } else {
                    getRandomUsers();
                }
                return true;

            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getRandomUsers() {
        final Dialog progressDialog = DialogUtils.showProgressDialog(this);
        final String usersCount = SharedPreferencesUtils.getSavedUsersCount(this);

        RandomUserService service = RetrofitClient.getClient().create(RandomUserService.class);
        Call<Example> resultsCall = service.getRandomUsers(usersCount);

        resultsCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                cancelProgressDialog(progressDialog);

                if (response.isSuccessful()) {
                    mRandomUsersList = response.body().getResults();
                    setupRecyclerView(mRandomUsersList);
                } else {
                    Timber.e(response.message());
                    SnackbarUtils.showErrorSnackbar(UsersActivity.this, R.string.users_download_error);
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Timber.e(t, t.getMessage());
                SnackbarUtils.showErrorSnackbar(UsersActivity.this, R.string.users_download_error);
            }
        });
    }

    private void setupRecyclerView(List<Result> resultList) {
        Timber.d("List size: %s", resultList.size());

        if (!resultList.isEmpty()) {
            mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this, R.drawable.recyclerview_horizontal_divider));

            RandomUserAdapter adapter = new RandomUserAdapter(resultList);
            mRecyclerView.setAdapter(adapter);
        } else {
            SnackbarUtils.showErrorSnackbar(this, R.string.users_download_error);
        }
    }

    private void cancelProgressDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.cancel();
        }
    }
}
