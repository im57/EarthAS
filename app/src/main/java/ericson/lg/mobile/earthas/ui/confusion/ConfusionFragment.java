package ericson.lg.mobile.earthas.ui.confusion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import ericson.lg.mobile.earthas.R;
import ericson.lg.mobile.earthas.ui.collection.CollectionFragment;

public class ConfusionFragment extends Fragment implements Parsing{

    private Button btnSearch;
    private EditText etItem;
    private String item;

    private RecyclerView recyclerConfusion;
    private LinearLayoutManager layoutManager;

    private ConfusionAdapter adapter;

    private String func;
    private String region;
    private String type;
    private String body;

    private View root;

    private String apiAddress;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_confusion, container, false);

        recyclerConfusion = root.findViewById(R.id.recycler_search);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerConfusion.setLayoutManager(layoutManager);

        adapter = new ConfusionAdapter(root.getContext(), this);
        recyclerConfusion.setAdapter(adapter);

        parsingList();

        etItem = root.findViewById(R.id.edit_item);
        btnSearch = root.findViewById(R.id.button_search);
        btnSearch.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                item = etItem.getText().toString();

                parsingFind();
            }
        });

        return root;
    }

    public void parsingOpen(String type){
        this.type = type.equals("general")? "garbage" : type;

        func = "open";
        apiAddress = root.getResources().getString(R.string.url) + root.getResources().getString(R.string.url_box_open);
        region = "seoul";
        try {
            new RestAPITask().execute(apiAddress + URLEncoder.encode(region, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void parsingList() {
        func = "list";
        apiAddress = root.getResources().getString(R.string.url) + root.getResources().getString(R.string.url_confusion_list);

        try {
            new RestAPITask().execute(apiAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void parsingFind() {
        func = "find";
        apiAddress = root.getResources().getString(R.string.url) + root.getResources().getString(R.string.url_confusion_find);

        try {
            new RestAPITask().execute(apiAddress + URLEncoder.encode(item, "UTF-8"));
           // new RestAPITask().execute(root.getResources().getString(R.string.url) + root.getResources().getString(R.string.url_confusion_find) + item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RestAPITask extends AsyncTask<String, Void, String> {

        //수행 전
        @Override
        protected void onPreExecute() {
            if(func.equals("open")){
                try {
                    JSONObject json = new JSONObject();
                    json.put("type", type);
                    body = json.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                adapter.clearItem();
            }
        }

        @Override
        protected String doInBackground(String... Strings) {
            String result = null;
            Log.d("urllllllllllllll", Strings[0]);
            try {
                result = downloadContents(Strings[0]);

            }
            catch (Exception e) {
                // Error calling the rest api
                Log.e("REST_API", "GET method failed: " + e.getMessage());
                e.printStackTrace();
            }

            return result;
        }

        //작업 완료
        @Override
        protected void onPostExecute(String result) {
            if(func.equals("open")){
                Toast.makeText(root.getContext(), type + " box open success", Toast.LENGTH_SHORT).show();
            } else {
                parse(result);
                adapter.notifyDataSetChanged();
            }
        }
    }

    /* 주소(address)에 접속하여 문자열 데이터를 수신한 후 반환 */
    protected String downloadContents(String address) {
        HttpURLConnection conn = null;
        InputStream stream = null;
        String result = null;

        try {
            URL url = new URL(address);
            conn = (HttpURLConnection)url.openConnection();
            stream = getNetworkConnection(conn);
            result = readStreamToString(stream);
            if (stream != null) stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    // URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환
    private InputStream getNetworkConnection(HttpURLConnection conn) throws Exception {
        // 클라이언트 아이디 및 시크릿 그리고 요청 URL 선언
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        conn.setDoInput(true);
        conn.setRequestProperty("content-type", "application/json");

        if(!func.equals("open")){
            conn.setRequestMethod("GET");
        } else{
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            writeStream(conn);
        }

        if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + conn.getResponseCode());
        }

        return conn.getInputStream();
    }

    protected void writeStream(HttpURLConnection conn) {
        try {
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(body); //json 형식의 메세지 전달
            wr.flush();
            wr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* InputStream을 전달받아 문자열로 변환 후 반환 */
    protected String readStreamToString(InputStream stream){
        StringBuilder result = new StringBuilder();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(stream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String readLine = bufferedReader.readLine();

            while (readLine != null) {
                result.append(readLine + "\n");
                readLine = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    //json parsing
    public void parse(String json){
        try{
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("Items");
            JSONObject jsonConfusion;
            Confusion confusion;

            for(int i = 0; i< array.length(); i++) {
                jsonConfusion = array.getJSONObject(i);
                confusion = new Confusion();

                confusion.setName(jsonConfusion.getString("name"));
                confusion.setType(jsonConfusion.getString("type"));

                adapter.addItem(confusion);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}