package com.example.customdrawer2;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerItem[] mDrawerItems  = {
			new DrawerItem(R.drawable.ic_earth, "Earth"),
			new DrawerItem(R.drawable.ic_jupiter, "Jupiter"),
			new DrawerItem(R.drawable.ic_mars, "Mars"),
			new DrawerItem(R.drawable.ic_mercury, "Mercury"),
			new DrawerItem(R.drawable.ic_neptune, "Neptune"),
			new DrawerItem(R.drawable.ic_saturn, "Saturn"),
			new DrawerItem(R.drawable.ic_uranus, "Uranus"),
			new DrawerItem(R.drawable.ic_venus, "Venus")
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		mDrawerList = (ListView)findViewById(R.id.left_drawer);
		
		// Set a custom shadow that overlays the main content when the drawer opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		// Set up the drawer's list view with items and click listener
		mDrawerList.addHeaderView(LayoutInflater.from(this).inflate(R.layout.drawer_list_header, null));
		mDrawerList.setAdapter(new CustomArrayAdapter(this, mDrawerItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
		// ActionBarのアプリアイコンをドローワー開くボタンとして使用する
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		// ActionBarDrawerToggleを生成
		mDrawerToggle = new ActionBarDrawerToggle(
				this,
				mDrawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close
				) {
			public void onDrawerClosed(View view) {
			}
			
			public void onDrawerOpened(View drawerView) {
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState == null) {
			selectItem(0);
		}
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// ドローワーを開く／閉じるボタンのハンドリング
		// これはActionBarDrawerToggleが処理する
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);	
	}
	
	/* ナビゲーションドローワーのリスナー */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			mDrawerItems[position - 1].countUp();
			selectItem(position);
		}
	}
	
	private void selectItem(int position) {
		// content_frameのFragmentを置き換える
		Fragment fragment = new PlanetFragment();
		Bundle args = new Bundle();
		args.putString(PlanetFragment.ARG_PLANET_STRING, mDrawerItems[position].mTitle.toLowerCase(Locale.getDefault()));
		fragment.setArguments(args);
		
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
		
		// 選択したアイテムとタイトルを更新して、ドローワーを閉じる
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerItems[position].mTitle);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	/**
	 * 	ActionBarDrawerToggleを使用する時は、onPostCreate()とonConfigurationChanged()のオーバーライドが必要
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// onRestoreInstanceStateの後で状態を同期させる
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// configurationが変化した時にもドローワーの状態を同期させる
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * content_farameに表示するFragment
	 */
	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_STRING = "planet_string";
		
		public PlanetFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
			String planet = getArguments().getString(ARG_PLANET_STRING);
			
			int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()), "drawable", getActivity().getPackageName());
			((ImageView)rootView.findViewById(R.id.image)).setImageResource(imageId);
			return rootView;
		}
	}
	
	private class CustomArrayAdapter extends ArrayAdapter<DrawerItem> {
		
		public CustomArrayAdapter(Context context, DrawerItem[] objects) {
			super(context, 0, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Log.d("MainActivity", "getView:" + position);
			View view = convertView;
			// Viewが準備されていなければ新しく生成
			if (view == null) {
				view = LayoutInflater.from(getContext()).inflate(R.layout.drawer_list_item, null);
			}
			
			DrawerItem data = super.getItem(position);
			
			ImageView icon = (ImageView)view.findViewById(R.id.item_icon);
			icon.setImageResource(data.mIconRes);
			
			TextView title = (TextView)view.findViewById(R.id.item_title);
			title.setText(data.mTitle);
			
			TextView counter = (TextView)view.findViewById(R.id.item_counter); 
			if (data.mCounter > 0) {
				counter.setText(String.valueOf(data.mCounter));
				counter.setVisibility(View.VISIBLE);
			} else {
				counter.setVisibility(View.INVISIBLE);
			}
				
			return view;
		}
	}	
}
