package com.my.newproject8;

import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.*;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;

public class MainActivity extends AppCompatActivity {
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private DrawerLayout _drawer;
	private HashMap<String, Object> Heders = new HashMap<>();
	private HashMap<String, Object> json = new HashMap<>();
	private String user = "";
	private String user_msg = "";
	private HashMap<String, Object> map = new HashMap<>();
	private String text = "";
	private String Ai = "";
	private HashMap<String, Object> map1 = new HashMap<>();
	private String Response = "";
	
	private ArrayList<HashMap<String, Object>> list_map = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> resmap = new ArrayList<>();
	
	private RecyclerView recyclerview1;
	private ProgressBar progressbar1;
	private LinearLayout linear1;
	private EditText prompt_txt;
	private ImageView imageview3;
	private LinearLayout _drawer_linear1;
	
	private RequestNetwork Net;
	private RequestNetwork.RequestListener _Net_request_listener;
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		_drawer = findViewById(R.id._drawer);
		ActionBarDrawerToggle _toggle = new ActionBarDrawerToggle(MainActivity.this, _drawer, _toolbar, R.string.app_name, R.string.app_name);
		_drawer.addDrawerListener(_toggle);
		_toggle.syncState();
		
		LinearLayout _nav_view = findViewById(R.id._nav_view);
		
		recyclerview1 = findViewById(R.id.recyclerview1);
		progressbar1 = findViewById(R.id.progressbar1);
		linear1 = findViewById(R.id.linear1);
		prompt_txt = findViewById(R.id.prompt_txt);
		imageview3 = findViewById(R.id.imageview3);
		_drawer_linear1 = _nav_view.findViewById(R.id.linear1);
		Net = new RequestNetwork(this);
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				progressbar1.setVisibility(View.VISIBLE);
				Heders = new HashMap<>();
				Heders.put("Authorization", "Bearer ".concat("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"));
				Heders.put("Content-Type", "application/json");
				json.put("model", "text-davinci-003");
				json.put("prompt", prompt_txt.getText().toString());
				json.put("max_tokens", 100);
				json.put("temperature", 0.9);
				Net.setHeaders(Heders);
				Net.setParams(json, RequestNetworkController.REQUEST_BODY);
				Net.startRequestNetwork(RequestNetworkController.POST, "https://api.openai.com/v1/completions", "", _Net_request_listener);
				user = "You";
				user_msg = prompt_txt.getText().toString();
				map1 = new HashMap<>();
				map1.put("User", user);
				map1.put("Text", user_msg);
				list_map.add(map1);
				recyclerview1.setAdapter(new Recyclerview1Adapter(list_map));
				prompt_txt.setText("");
				progressbar1.setVisibility(View.GONE);
			}
		});
		
		_Net_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2, HashMap<String, Object> _param3) {
				final String _tag = _param1;
				final String _response = _param2;
				final HashMap<String, Object> _responseHeaders = _param3;
				progressbar1.setVisibility(View.VISIBLE);
				map = new Gson().fromJson(_response, new TypeToken<HashMap<String, Object>>(){}.getType());
				if (map.containsKey("choices")) {
					Response = (new Gson()).toJson(map.get("choices"), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					resmap = new Gson().fromJson(Response, new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
					if (resmap.get((int)0).containsKey("text")) {
						Ai = "AI Tutor";
						text = resmap.get((int)0).get("text").toString();
						map1 = new HashMap<>();
						map1.put("User", Ai);
						map1.put("Text", text);
						list_map.add(map1);
						recyclerview1.setAdapter(new Recyclerview1Adapter(list_map));
						progressbar1.setVisibility(View.GONE);
					}
					else {
						Ai = "AI Tutor";
						text = "Sorry i did not understand the question!";
						map1 = new HashMap<>();
						map1.put("User", Ai);
						map1.put("Text", text);
						list_map.add(map1);
						recyclerview1.setAdapter(new Recyclerview1Adapter(list_map));
						progressbar1.setVisibility(View.GONE);
					}
				}
				else {
					Ai = "AI Tutor";
					text = "Sorry i did not understand the question!";
					map1 = new HashMap<>();
					map1.put("User", Ai);
					map1.put("Text", text);
					list_map.add(map1);
					recyclerview1.setAdapter(new Recyclerview1Adapter(list_map));
					progressbar1.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
				
			}
		};
	}
	
	private void initializeLogic() {
		i.setClass(getApplicationContext(), SplashActivity.class);
		startActivity(i);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setStackFromEnd(true);
		// layoutManager.setReverseLayout(true);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerview1.setLayoutManager(layoutManager);
		
		prompt_txt.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)15, 0xFFFFFFFF));
		progressbar1.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onBackPressed() {
		if (_drawer.isDrawerOpen(GravityCompat.START)) {
			_drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	public class Recyclerview1Adapter extends RecyclerView.Adapter<Recyclerview1Adapter.ViewHolder> {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Recyclerview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater _inflater = getLayoutInflater();
			View _v = _inflater.inflate(R.layout.cus, null);
			RecyclerView.LayoutParams _lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			_v.setLayoutParams(_lp);
			return new ViewHolder(_v);
		}
		
		@Override
		public void onBindViewHolder(ViewHolder _holder, final int _position) {
			View _view = _holder.itemView;
			
			final LinearLayout linear_gpt = _view.findViewById(R.id.linear_gpt);
			final LinearLayout linearuser = _view.findViewById(R.id.linearuser);
			final androidx.cardview.widget.CardView cardview1 = _view.findViewById(R.id.cardview1);
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final ImageView image_user = _view.findViewById(R.id.image_user);
			final TextView text_username = _view.findViewById(R.id.text_username);
			final TextView text_response = _view.findViewById(R.id.text_response);
			final LinearLayout linear4 = _view.findViewById(R.id.linear4);
			final androidx.cardview.widget.CardView cardview2 = _view.findViewById(R.id.cardview2);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			final TextView textview2 = _view.findViewById(R.id.textview2);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			
			if (_data.get((int)_position).get("User").toString().equals("You")) {
				linearuser.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF00B0FF));
				textview1.setText(list_map.get((int)_position).get("User").toString());
				textview2.setText(list_map.get((int)_position).get("Text").toString());
				linear_gpt.setVisibility(View.GONE);
				linearuser.setVisibility(View.VISIBLE);
			}
			else {
				linear_gpt.setBackground(new GradientDrawable() { public GradientDrawable getIns(int a, int b) { this.setCornerRadius(a); this.setColor(b); return this; } }.getIns((int)10, 0xFF00B0FF));
				text_username.setText(list_map.get((int)_position).get("User").toString());
				text_response.setText(list_map.get((int)_position).get("Text").toString());
				linearuser.setVisibility(View.GONE);
				linear_gpt.setVisibility(View.VISIBLE);
			}
		}
		
		@Override
		public int getItemCount() {
			return _data.size();
		}
		
		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}