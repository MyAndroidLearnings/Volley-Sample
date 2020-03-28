package com.tsi.volleyexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.volleyexample.Model.GithubModel;
import com.tsi.volleyexample.NetworkCall.NetworkCall;
import com.tsi.volleyexample.NetworkCall.VolleyResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String GitHubApiURL ="https://api.github.com/users/divakar";
    String GeofenceApiURL ="https://digital-mrkt.tpfsoftware.com/geofence/geodatas/create";

    Button btnVolleyGet, btnVolleyPost,btnAsyncVolleyGet,btnAsyncVolleyPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnVolleyGet = (Button) findViewById(R.id.btnVolleyGet);
        btnVolleyPost = (Button) findViewById(R.id.btnVolleyPost);
        btnAsyncVolleyGet = (Button) findViewById(R.id.btnAsyncVolleyGet);
        btnAsyncVolleyPost = (Button) findViewById(R.id.btnAsyncVolleyPost);

        btnVolleyGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGitHubApi();
            }
        });

        btnVolleyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postGeofenceApi();
            }
        });

        btnAsyncVolleyGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime ="btnAsyncVolleyGet";
                runner.execute(sleepTime);
            }
        });

        btnAsyncVolleyPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = "btnAsyncVolleyPost";
                runner.execute(sleepTime);
            }
        });

    }


    private void postGeofenceApi() {
        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("eventType", "Entry");
            jsonBodyObj.put("_id", "5cde856ca6e2a976f8c9c3c7");
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();
        NetworkCall.postNetworkCallWithParam(GeofenceApiURL, this,requestBody, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("JsonData", message.toString());
            }
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JsonData", response.toString());
            }
        });
    }

    private void getGitHubApi() {
        NetworkCall.getNetworkCallWithoutParam(GitHubApiURL, this, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d("JsonData", message.toString());
            }

            @Override
            public void onResponse(JSONObject response) {
                Log.d("JsonData", response.toString());
                ObjectMapper mapper = new ObjectMapper();
                try {
                    GithubModel githubModel = mapper.readValue(response.toString(),GithubModel.class);
                    Log.d("JsonData", String.valueOf(githubModel));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /*try {
                    GithubModel githubModel = new GithubModel(response.get("login").toString(),Long.parseLong(response.get("id").toString()),response.get("node_id").toString(),response.get("avatar_url").toString(),response.get("gravatar_id").toString(),response.get("url").toString(),response.get("html_url").toString(),response.get("followers_url").toString(),response.get("following_url").toString(),response.get("gists_url").toString(),response.get("starred_url").toString(),response.get("subscriptions_url").toString(),response.get("organizations_url").toString(),response.get("repos_url").toString(),response.get("events_url").toString(),response.get("received_events_url").toString(),response.get("type").toString(),Boolean.getBoolean(response.get("site_admin").toString()),response.get("name").toString(),response.get("company").toString(),response.get("blog").toString(),response.get("location").toString(),response.get("email").toString(),response.get("hireable").toString(),response.get("bio").toString(),Long.parseLong(response.get("public_repos").toString()),Long.parseLong(response.get("public_gists").toString()),Long.parseLong(response.get("followers").toString()),Long.parseLong(response.get("following").toString()),response.get("created_at").toString(),response.get("updated_at").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, Boolean> {
        Boolean result;
        @Override
        protected Boolean doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
            try {
                if(params[0]=="btnAsyncVolleyGet"){
                    getGitHubApi();
                    result = true;
                }else if(params[0]=="btnAsyncVolleyPost"){
                    postGeofenceApi();
                    result = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(String... text) {
        }
    }

}
