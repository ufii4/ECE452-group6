package com.ece452.watfit.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ece452.watfit.FitnessSchedulerActivity;
import com.ece452.watfit.MainActivity;
import com.ece452.watfit.R;
import com.ece452.watfit.RecVideoDisplayActivity;
import com.ece452.watfit.RecipeGeneratorActivity;
import com.ece452.watfit.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    // Your Video URL
    String videoId1 = "iCQ2gC4DqJw";
    String videoId2 = "UoC_O3HzsH0";
    String videoId3 = "IT94xC35u6k";
    String videoId4 = "A0BhN-HAkt8";
    String videoId5 = "ixkQaZXVQjs";
    String videoCoverUrl1 = "https://img.youtube.com/vi/";
    String videoCoverUrl2 = "/0.jpg";
    String videoUrl = "https://www.youtube.com/embed/";
    String videoDes1 = "PERFECT 20 MIN FULL BODY WORKOUT FOR BEGINNERS (No Equipment)";
    String videoDes2 = "10 MIN BODYWEIGHT WORKOUT (NO EQUIPMENT HOME WORKOUT!)";
    String videoDes3 = "20 min Fat Burning Workout for TOTAL BEGINNERS (Achievable, No Equipment)";
    String videoDes4 = "20 MINUTE CARDIO AND ABS WORKOUT [FAT MELTING ROUTINE]";
    String videoDes5 = "The PERFECT Beginner Workout (Sets and Reps Included)";
    String SearchURL = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=fitness&type=video&key=AIzaSyDVM_9P55ILrYROYZo7ioyKvyqH8hdAg0E";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //may need to change starts
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //may need to change ends
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ///////The Recipe Generator Button
        Button recipeGeneratorButton = (Button) root.findViewById(R.id.navbar_recipe_generator);
        recipeGeneratorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecipeGeneratorActivity.class);
                startActivity(intent);
            }
        });
        ///////
        ///////The Fitness Scheduler Button
        Button fitnessSchedulerButton = (Button) root.findViewById(R.id.navbar_fitness_generator);
        fitnessSchedulerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FitnessSchedulerActivity.class);
                startActivity(intent);
            }
        });
        ///////

        //Code for Video Desc. starts here
        TextView v1_text = (TextView)root.findViewById(R.id.rec_v1_text);
        TextView v2_text = (TextView)root.findViewById(R.id.rec_v2_text);
        TextView v3_text = (TextView)root.findViewById(R.id.rec_v3_text);
        TextView v4_text = (TextView)root.findViewById(R.id.rec_v4_text);
        TextView v5_text = (TextView)root.findViewById(R.id.rec_v5_text);
        v1_text.setText(videoDes1);
        v2_text.setText(videoDes2);
        v3_text.setText(videoDes3);
        v4_text.setText(videoDes4);
        v5_text.setText(videoDes5);


        //Code for image Covers start here
        ImageView v1 = (ImageView) root.findViewById(R.id.rec_v1);
        ImageView v2 = (ImageView) root.findViewById(R.id.rec_v2);
        ImageView v3 = (ImageView) root.findViewById(R.id.rec_v3);
        ImageView v4 = (ImageView) root.findViewById(R.id.rec_v4);
        ImageView v5 = (ImageView) root.findViewById(R.id.rec_v5);
        URL url = null;
        Bitmap bitmap = null;
        try {
            url = new URL((videoCoverUrl1+videoId1+videoCoverUrl2));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v1.setImageBitmap(bitmap);
        try {
            url = new URL((videoCoverUrl1+videoId2+videoCoverUrl2));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v2.setImageBitmap(bitmap);
        try {
            url = new URL((videoCoverUrl1+videoId3+videoCoverUrl2));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v3.setImageBitmap(bitmap);
        try {
            url = new URL((videoCoverUrl1+videoId4+videoCoverUrl2));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v4.setImageBitmap(bitmap);
        try {
            url = new URL((videoCoverUrl1+videoId5+videoCoverUrl2));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        v5.setImageBitmap(bitmap);

        // Apply OnClickListener  to imageView to
        // switch from one activity to another
        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecVideoDisplayActivity.class);
                Bundle param = new Bundle();
                param.putString("VideoURL", (videoUrl+videoId1));
                intent.putExtras(param);
                startActivity(intent);
            }
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecVideoDisplayActivity.class);
                Bundle param = new Bundle();
                param.putString("VideoURL", (videoUrl+videoId2));
                intent.putExtras(param);
                startActivity(intent);
            }
        });
        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecVideoDisplayActivity.class);
                Bundle param = new Bundle();
                param.putString("VideoURL", (videoUrl+videoId3));
                intent.putExtras(param);
                startActivity(intent);
            }
        });
        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecVideoDisplayActivity.class);
                Bundle param = new Bundle();
                param.putString("VideoURL", (videoUrl+videoId4));
                intent.putExtras(param);
                startActivity(intent);
            }
        });
        v5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecVideoDisplayActivity.class);
                Bundle param = new Bundle();
                param.putString("VideoURL", (videoUrl+videoId5));
                intent.putExtras(param);
                startActivity(intent);
            }
        });
        Button refreshButton = (Button) root.findViewById(R.id.bt_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response = "";
                URL searchUrl = null;
                try {
                    searchUrl = new URL(SearchURL);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                BufferedReader br_refresh;
                try {
                    br_refresh = new BufferedReader(new InputStreamReader(searchUrl.openConnection().getInputStream()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String line;
                while (true){
                    try {
                        if (!((line = br_refresh.readLine()) != null)) break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    response = response + line;
                }

                //response retrieved at last! below we deal with each of the five results
                if (!response.isEmpty()){
                    JSONObject refresh_object;
                    try {
                        refresh_object = new JSONObject(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    JSONArray response_list;
                    try {
                        response_list = refresh_object.getJSONArray("items");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    JSONObject refresh_item;
                    JSONObject refresh_item_id;
                    String temp;
                    URL url_temp = null;
                    Bitmap bitmap_temp = null;
                    //processing for result 0
                    try {
                        refresh_item = response_list.getJSONObject(0);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        refresh_item_id = refresh_item.getJSONObject("id");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        temp = refresh_item_id.getString("videoId");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    videoId1 = temp;
                    try {
                        url_temp = new URL((videoCoverUrl1+videoId1+videoCoverUrl2));
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        bitmap_temp = BitmapFactory.decodeStream(url_temp.openConnection().getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    v1.setImageBitmap(bitmap_temp);
                    try {
                        refresh_item_id = refresh_item.getJSONObject("snippet");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        temp = refresh_item_id.getString("title");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    videoDes1 = temp;
                    v1_text.setText(videoDes1);
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}