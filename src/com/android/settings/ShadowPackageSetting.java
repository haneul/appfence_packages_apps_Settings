

package com.android.settings;
import android.preference.Preference;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import dalvik.system.ShadowPreference;

import android.util.Log;
import android.Manifest;

import java.util.HashSet;

public class ShadowPackageSetting extends PreferenceActivity {
	

	private HashSet<String> perms = new HashSet<String>();
	private HashSet<String> keys = new HashSet<String>();
	private String packageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.shadow_settings);

		Intent i = getIntent();
		packageName = i.getStringExtra("packageName");
		getPreferenceScreen().setTitle(packageName);

		final PackageManager pm = getPackageManager();
		PackageInfo pinfo;
		try {
			pinfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			getPreferenceScreen().removeAll();
			return;
		}

		if(pinfo.requestedPermissions != null)
		{
			for(String perm: pinfo.requestedPermissions)
			{
				perms.add(perm);	
			}

			if(!perms.contains(Manifest.permission.READ_PHONE_STATE))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.IMEI_KEY));
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.PHONE_KEY));
			}
			else
			{
				keys.add(ShadowPreference.IMEI_KEY);
				keys.add(ShadowPreference.PHONE_KEY);
			}
			if(!perms.contains(Manifest.permission.ACCESS_COARSE_LOCATION) &&
			   !perms.contains(Manifest.permission.ACCESS_FINE_LOCATION))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.LOCATION_KEY));
			}
			else
			{
				keys.add(ShadowPreference.LOCATION_KEY);
			}
			if(!perms.contains(Manifest.permission.READ_CONTACTS))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.CONTACTS_KEY));
			}
			else
			{
				keys.add(ShadowPreference.CONTACTS_KEY);
			}
			if(!perms.contains(Manifest.permission.CAMERA) &&
			   !perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.CAMERA_KEY));
			}
			else
			{
				keys.add(ShadowPreference.CAMERA_KEY);
			}
			if(!perms.contains(Manifest.permission.RECORD_AUDIO) &&
			   !perms.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.MIC_KEY));
			}
			else
			{
				keys.add(ShadowPreference.MIC_KEY);
			}
			if(!perms.contains(Manifest.permission.GET_ACCOUNTS) &&
			   !perms.contains(Manifest.permission.ACCOUNT_MANAGER))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.ACCOUNT_KEY));
			}
			else
			{
				keys.add(ShadowPreference.ACCOUNT_KEY);
			}
			if(!perms.contains(Manifest.permission.READ_LOGS))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.LOGS_KEY));
			}
			else
			{
				keys.add(ShadowPreference.LOGS_KEY);
			}
			if(!perms.contains(Manifest.permission.READ_SMS) &&
			   !perms.contains(Manifest.permission.RECEIVE_SMS) &&
			   !perms.contains(Manifest.permission.RECEIVE_MMS))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.SMS_KEY));
			}
			else
			{
				keys.add(ShadowPreference.SMS_KEY);
			}
			if(!perms.contains(Manifest.permission.READ_HISTORY_BOOKMARKS))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.HISTORY_KEY));
			}
			else
			{
				keys.add(ShadowPreference.HISTORY_KEY);
			}
			if(!perms.contains(Manifest.permission.READ_CALENDAR))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.CALENDAR_KEY));
			}
			else
			{
				keys.add(ShadowPreference.CALENDAR_KEY);
			}
			if(!perms.contains(Manifest.permission.SUBSCRIBED_FEEDS_READ))
			{
				getPreferenceScreen().removePreference(findPreference(ShadowPreference.FEEDS_KEY));
			}
			else
			{
				keys.add(ShadowPreference.FEEDS_KEY);
			}
			checkKeys();
		}
		else
		{
			getPreferenceScreen().removeAll();
		}
	}	

	private void checkKeys()
	{
		for(String key: keys)
		{
			if(ShadowPreference.isShadowed(packageName, key))
			{
				Preference pref = findPreference(key);
				((CheckBoxPreference )pref).setChecked(true);
			}
		}
	}

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
            Preference preference) {
		ShadowPreference.shadow(packageName, preference.getKey(), ((CheckBoxPreference )preference).isChecked());
		return false;
	}

}
