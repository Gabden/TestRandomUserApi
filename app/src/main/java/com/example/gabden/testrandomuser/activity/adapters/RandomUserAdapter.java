package com.example.gabden.testrandomuser.activity.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.gabden.testrandomuser.R;
import com.example.gabden.testrandomuser.activity.UserDetailActivity;
import com.example.gabden.testrandomuser.activity.models.Result;
import com.example.gabden.testrandomuser.activity.utils.StringUtils;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RandomUserAdapter extends RecyclerView.Adapter<RandomUserAdapter.RandomUserViewHolder> {

    private List<Result> mResultList;

    public RandomUserAdapter(List<Result> resultList) {
        mResultList = resultList;
    }

    @Override
    public RandomUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_row, parent, false);
        return new RandomUserViewHolder((view));
    }

    @Override
    public void onBindViewHolder(RandomUserViewHolder holder, int position) {
        holder.bindRandomUser(mResultList.get(position));
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }

    public class RandomUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_user_image)
        ImageView userImageView;
        @BindView(R.id.tv_user_name)
        TextView userTextView;

        private Result mResult;
        private Gson mGson;

        public RandomUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mGson = new Gson();
        }

        public void bindRandomUser(Result result) {
            mResult = result;

            Glide
                    .with(itemView.getContext())
                    .load(mResult.getPicture().getLarge())
                    .apply(new RequestOptions().centerCrop())
                    .apply(new RequestOptions().override(getUserImageDimension(), getUserImageDimension()))
                    .into(userImageView);

            userTextView.setText(String.format("%s %s", StringUtils.firstCharToUpperCase(mResult.getName().getFirst()), StringUtils.firstCharToUpperCase(mResult.getName().getLast())));
        }

        private int getUserImageDimension() {
            return itemView.getContext().getResources().getDimensionPixelSize(R.dimen.user_image_dimension);
        }

        @OnClick({R.id.iv_user_image, R.id.tv_user_name})
        public void userClicked() {
            Context context = itemView.getContext();
            context.startActivity(UserDetailActivity.newIntent(context, mGson.toJson(mResult)));
        }
    }
}
