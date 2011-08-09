
package com.android.settings;

import android.app.ListActivity;
import android.content.Context;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;

public class ShadowAppList extends ListActivity {
	private ListView listview = null;
	private ArrayList<PackageInfo> packagelist_;
	//private ArrayList<String> packagelist;
	//
	private MyArrayAdapter adapter;

	private Hashtable<String, PackageInfo> ht = new Hashtable<String, PackageInfo>();

	class MyArrayAdapter extends ArrayAdapter {
		Context context;
		private ArrayList<PackageInfo> packagelist;
		
		MyArrayAdapter(Context context, ArrayList<PackageInfo> packagelist) {
			super(context, R.layout.shadow_row, packagelist);
			this.context = context;
			this.packagelist = packagelist;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			View v = convertView;
			if(v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.shadow_row, null);
			}
			PackageInfo p = packagelist.get(position);
			if(p != null) {
				TextView tt = (TextView) v.findViewById(R.id.textView1);
				if(tt != null)
				{
					tt.setText(p.applicationInfo.loadLabel(getPackageManager()));
				}
				ImageView iv = (ImageView) v.findViewById(R.id.imageView1);
				if(iv != null)
				{
					iv.setImageDrawable( p.applicationInfo.loadIcon(getPackageManager()));
				}
			}
			return v;
		}
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shadow_app_list);

		listview = (ListView) findViewById(R.id.list);
		//packagelist = new ArrayList<String>();
		final PackageManager pm = getPackageManager();
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		//ArrayList<PackageInfo> packagelist = new ArrayList<PackageInfo>();
		packagelist_ = new ArrayList<PackageInfo>();
		for( PackageInfo packageInfo : packages )
		{
			Log.d("Skel", "Installed package :" + packageInfo.packageName);

			Log.d("Skel", ""+packageInfo.applicationInfo.flags+" "+packageInfo.applicationInfo.FLAG_SYSTEM);
			//Log.d("Skel", ""+(packageInfo.applicationInfo.flags | ApplicationInfo.FLAG_SYSTEM));
			if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) continue;
			//String name = packageInfo.applicationInfo.name;
			// String name = packageInfo.packageName;

			packagelist_.add(packageInfo);
			//ht.put(name, packageInfo);
		}

		adapter = new MyArrayAdapter(this, packagelist_);

		//adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, packagelist);
		setListAdapter(adapter);
		//registerForContextMenu(getListView());
		//list.setItemsCanFocus(true);
	}

	protected void onListItemClick (ListView l, View v, int position, long id){
		super.onListItemClick(l, v, position, id);

    		Intent showpackage = new Intent(this, ShadowPackageSetting.class);
		PackageInfo pkgInfo = packagelist_.get(position);

		showpackage.putExtra("packageName", pkgInfo.packageName);
		startActivity(showpackage);
	}
}
