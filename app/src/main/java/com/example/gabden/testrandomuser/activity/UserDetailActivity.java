package com.example.gabden.testrandomuser.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gabden.testrandomuser.R;
import com.example.gabden.testrandomuser.activity.models.Result;
import com.example.gabden.testrandomuser.activity.utils.SnackbarUtils;
import com.example.gabden.testrandomuser.activity.utils.StringUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailActivity extends AppCompatActivity {

    private static final String EXTRA_USER = "extra_user";

    @BindView(R.id.tb_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_user_detail_image)
    ImageView userImageView;
    @BindView(R.id.tv_user_name_field)
    TextView nameTextView;
    @BindView(R.id.tv_user_email_field)
    TextView emailTextView;
    @BindView(R.id.tv_user_birthday_field)
    TextView birthdayTextView;
    @BindView(R.id.tv_user_address_field)
    TextView addressTextView;
    @BindView(R.id.tv_user_phone_number_field)
    TextView phoneNumberTextView;

    private Result mUser;


    public static Intent newIntent(Context context, String result) {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(EXTRA_USER, result);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send_user) {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"test@test.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, String.format("%s %s %s", "Information about: ", StringUtils.firstCharToUpperCase(mUser.getName().getFirst()), StringUtils.firstCharToUpperCase(mUser.getName().getLast())));
            emailIntent.putExtra(Intent.EXTRA_TEXT, String.format("%s %s \n %s  %s ", " email: ", mUser.getEmail(), "phone: ", mUser.getPhone()));
            emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(mUser.getPicture().getLarge()));
            emailIntent.setType("application/octet-stream");
            startActivity(Intent.createChooser(emailIntent, "Send Email"));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);


        phoneNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDial = new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("%s %s", "tel: ", mUser.getPhone())));
                startActivity(intentDial);

            }
        });

        emailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mUser.getEmail()});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Message body");
                emailIntent.setType("application/octet-stream");
                startActivity(emailIntent);

            }
        });
        if (getIntent().getExtras() != null && !getIntent().getExtras().isEmpty()) {
            Gson gson = new Gson();
            mUser = gson.fromJson(getIntent().getStringExtra(EXTRA_USER), Result.class);
        } else {
            finish();
            SnackbarUtils.showErrorSnackbar(this, R.string.activity_start_error);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mUser.getName().toString());
        }

        setupUserFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setTitle("Information");
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    private void setupUserFields() {
        Glide.with(this)
                .load(mUser.getPicture().getLarge())
                .apply(new RequestOptions().centerCrop())
                .apply(new RequestOptions().override(getUserImageDimension(), getUserImageDimension()))
                .into(userImageView);

        nameTextView.setText(String.format("%s %s", StringUtils.firstCharToUpperCase(mUser.getName().getFirst()), StringUtils.firstCharToUpperCase(mUser.getName().getLast())));
        emailTextView.setText(mUser.getEmail());
        birthdayTextView.setText(getUserDOB(mUser));
        addressTextView.setText(formatUserAddress(mUser));
        phoneNumberTextView.setText(mUser.getPhone());
    }

    private int getUserImageDimension() {
        return getResources().getDimensionPixelSize(R.dimen.user_detail_image_dimension);
    }

    private String formatUserAddress(Result user) {
        return String.format(getString(R.string.full_address), user.getLocation().getStreet(),
                user.getLocation().getCity(), user.getLocation().getState(),
                user.getLocation().getPostcode());
    }

    private String getUserDOB(Result user) {
        long epochTime = user.getDob().getAge();
        Date birthday = new Date(epochTime * 1000L);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M/dd/yyyy", Locale.US);
        return simpleDateFormat.format(birthday);
    }
}