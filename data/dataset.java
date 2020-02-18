package com.onedatapoint;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import com.onedatapoint.config.Config;
import com.onedatapoint.model.Question;
import com.onedatapoint.model.XYQuestion;
import com.onedatapoint.views.GraphView;

public class CuringDepressionActivity extends Activity {
    private final static String LOGTAG = "onedatapoint";
    private Iterable<Question> questions;
    private boolean canExit = true;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        questions = Config.getInstance().getQuestionRepository().getQuestions();

//        setupAlarms();
        for (Question question : questions)
            Log.v(LOGTAG, question.toString());

        //questionViews = new Vector<View>();
        //createQuestionViews();
        setContentView(R.layout.home);
    }

    private void setupAlarms() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, LogNotificationReceiver.class), 0);               

        Calendar calendar = Calendar.getInstance();

        // TODO: This is the real code
        //calendar.set(Calendar.HOUR_OF_DAY, 9);
        //calendar.set(Calendar.MINUTE, 00);
        //calendar.set(Calendar.SECOND, 00);

        // Uncomment for testing
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 2);

        // This one sets the alarm for 9am daily
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours
	}

	// Home screen button onClick handlers
    public void openJournal(View v) {
        canExit = false;
        setContentView(R.layout.journal);
    }
    public void openReview(View v) {
        canExit = false;
        setContentView(R.layout.review);
    }
    public void openMedicine(View v) {
        canExit = false;
        setContentView(R.layout.medicine);
    }
    public void openGraphs(View v) {
        canExit = false;
        setContentView(R.layout.graphs);
        GraphView graphView = (GraphView) findViewById(R.id.graphView);
        XYQuestion question = new XYQuestion("#FAF", "Rate your levels of:", "Irritability", "Anxiety");
        graphView.setQuestion(question);
    }

    public void saveAndGoHome(View v) {
        canExit = true;
        setContentView(R.layout.home);
    }

    public void saveAndQuit(View v) {
        moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canExit) {
                moveTaskToBack(true);
                return true;
            }

            canExit = true;
            setContentView(R.layout.home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    private void createQuestionViews() {
        for (Question question : questions) {
            View questionView = new View(this);
            // Create respective View object
            if (question.type.equals("graph"))
                // Instantiate View
                break;
            else if (question.type.equals("slider"))
                // Instantiate View
                break;
            else if (question.type.equals("buttons"))
                // Instantiate View
                break;
            questionViews.add(questionView);
        }
    }
    */

    private void showQuestions() {
        setContentView(R.layout.main);

        TextView text = (TextView)findViewById(R.id.xmlText);
        for (Question question : questions) {
            text.append("\n" + question.toString());
        }
    }
}
package android.support.v4.app;

import android.util.Log;
import android.view.View;
import android.view.Window;
import com.actionbarsherlock.ActionBarSherlock.OnCreatePanelMenuListener;
import com.actionbarsherlock.ActionBarSherlock.OnMenuItemSelectedListener;
import com.actionbarsherlock.ActionBarSherlock.OnPreparePanelListener;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;

/** I'm in ur package. Stealing ur variables. */
public abstract class _ActionBarSherlockTrojanHorse extends FragmentActivity implements OnCreatePanelMenuListener, OnPreparePanelListener, OnMenuItemSelectedListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "_ActionBarSherlockTrojanHorse";

    /** Fragment interface for menu creation callback. */
    public interface OnCreateOptionsMenuListener {
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater);
    }
    /** Fragment interface for menu preparation callback. */
    public interface OnPrepareOptionsMenuListener {
        public void onPrepareOptionsMenu(Menu menu);
    }
    /** Fragment interface for menu item selection callback. */
    public interface OnOptionsItemSelectedListener {
      public boolean onOptionsItemSelected(MenuItem item);
    }

    private ArrayList<Fragment> mCreatedMenus;


    ///////////////////////////////////////////////////////////////////////////
    // Sherlock menu handling
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (DEBUG) Log.d(TAG, "[onCreatePanelMenu] featureId: " + featureId + ", menu: " + menu);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            boolean result = onCreateOptionsMenu(menu);
            if (DEBUG) Log.d(TAG, "[onCreatePanelMenu] activity create result: " + result);

            MenuInflater inflater = getSupportMenuInflater();
            boolean show = false;
            ArrayList<Fragment> newMenus = null;
            if (mFragments.mActive != null) {
                for (int i = 0; i < mFragments.mAdded.size(); i++) {
                    Fragment f = mFragments.mAdded.get(i);
                    if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && f instanceof OnCreateOptionsMenuListener) {
                        show = true;
                        ((OnCreateOptionsMenuListener)f).onCreateOptionsMenu(menu, inflater);
                        if (newMenus == null) {
                            newMenus = new ArrayList<Fragment>();
                        }
                        newMenus.add(f);
                    }
                }
            }

            if (mCreatedMenus != null) {
                for (int i = 0; i < mCreatedMenus.size(); i++) {
                    Fragment f = mCreatedMenus.get(i);
                    if (newMenus == null || !newMenus.contains(f)) {
                        f.onDestroyOptionsMenu();
                    }
                }
            }

            mCreatedMenus = newMenus;

            if (DEBUG) Log.d(TAG, "[onCreatePanelMenu] fragments create result: " + show);
            result |= show;

            if (DEBUG) Log.d(TAG, "[onCreatePanelMenu] returning " + result);
            return result;
        }
        return false;
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (DEBUG) Log.d(TAG, "[onPreparePanel] featureId: " + featureId + ", view: " + view + " menu: " + menu);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            boolean result = onPrepareOptionsMenu(menu);
            if (DEBUG) Log.d(TAG, "[onPreparePanel] activity prepare result: " + result);

            boolean show = false;
            if (mFragments.mActive != null) {
                for (int i = 0; i < mFragments.mAdded.size(); i++) {
                    Fragment f = mFragments.mAdded.get(i);
                    if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && f instanceof OnPrepareOptionsMenuListener) {
                        show = true;
                        ((OnPrepareOptionsMenuListener)f).onPrepareOptionsMenu(menu);
                    }
                }
            }

            if (DEBUG) Log.d(TAG, "[onPreparePanel] fragments prepare result: " + show);
            result |= show;

            result &= menu.hasVisibleItems();
            if (DEBUG) Log.d(TAG, "[onPreparePanel] returning " + result);
            return result;
        }
        return false;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (DEBUG) Log.d(TAG, "[onMenuItemSelected] featureId: " + featureId + ", item: " + item);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            if (onOptionsItemSelected(item)) {
                return true;
            }

            if (mFragments.mActive != null) {
                for (int i = 0; i < mFragments.mAdded.size(); i++) {
                    Fragment f = mFragments.mAdded.get(i);
                    if (f != null && !f.mHidden && f.mHasMenu && f.mMenuVisible && f instanceof OnOptionsItemSelectedListener) {
                        if (((OnOptionsItemSelectedListener)f).onOptionsItemSelected(item)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public abstract boolean onCreateOptionsMenu(Menu menu);

    public abstract boolean onPrepareOptionsMenu(Menu menu);

    public abstract boolean onOptionsItemSelected(MenuItem item);

    public abstract MenuInflater getSupportMenuInflater();
}
package yuku.ambilwarna;

import android.app.AlertDialog;
import android.content.*;
import android.graphics.Color;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class AmbilWarnaDialog {
	private static final String TAG = AmbilWarnaDialog.class.getSimpleName();
	
	public interface OnAmbilWarnaListener {
		void onCancel(AmbilWarnaDialog dialog);
		void onOk(AmbilWarnaDialog dialog, int color);
	}
	
	AlertDialog dialog;
	OnAmbilWarnaListener listener;
	View viewHue;
	AmbilWarnaKotak viewKotak;
	ImageView panah;
	View viewWarnaLama;
	View viewWarnaBaru;
	ImageView viewKeker;
	
	float satudp;
	int warnaLama;
	int warnaBaru;
	float hue;
	float sat;
	float val;
	float ukuranUiDp = 240.f;
	float ukuranUiPx; // diset di constructor
	
	public AmbilWarnaDialog(Context context, int color, OnAmbilWarnaListener listener) {
		this.listener = listener;
		this.warnaLama = color;
		this.warnaBaru = color;
		Color.colorToHSV(color, tmp01);
		hue = tmp01[0];
		sat = tmp01[1];
		val = tmp01[2];
		
		satudp = context.getResources().getDimension(R.dimen.ambilwarna_satudp);
		ukuranUiPx = ukuranUiDp * satudp;
		Log.d(TAG, "satudp = " + satudp + ", ukuranUiPx=" + ukuranUiPx);  //$NON-NLS-1$//$NON-NLS-2$
		
		View view = LayoutInflater.from(context).inflate(R.layout.ambilwarna_dialog, null);
		viewHue = view.findViewById(R.id.ambilwarna_viewHue);
		viewKotak = (AmbilWarnaKotak) view.findViewById(R.id.ambilwarna_viewKotak);
		panah = (ImageView) view.findViewById(R.id.ambilwarna_panah);
		viewWarnaLama = view.findViewById(R.id.ambilwarna_warnaLama);
		viewWarnaBaru = view.findViewById(R.id.ambilwarna_warnaBaru);
		viewKeker = (ImageView) view.findViewById(R.id.ambilwarna_keker);

		letakkanPanah();
		letakkanKeker();
		viewKotak.setHue(hue);
		viewWarnaLama.setBackgroundColor(color);
		viewWarnaBaru.setBackgroundColor(color);

		viewHue.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE 
						|| event.getAction() == MotionEvent.ACTION_DOWN
						|| event.getAction() == MotionEvent.ACTION_UP) {
					
					float y = event.getY(); // dalam px, bukan dp
					if (y < 0.f) y = 0.f;
					if (y > ukuranUiPx) y = ukuranUiPx - 0.001f;
					
					hue = 360.f - 360.f / ukuranUiPx * y;
					if (hue == 360.f) hue = 0.f;
					
					warnaBaru = hitungWarna();
					// update view
					viewKotak.setHue(hue);
					letakkanPanah();
					viewWarnaBaru.setBackgroundColor(warnaBaru);
					
					return true;
				}
				return false;
			}
		});
		viewKotak.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE 
						|| event.getAction() == MotionEvent.ACTION_DOWN
						|| event.getAction() == MotionEvent.ACTION_UP) {
					
					float x = event.getX(); // dalam px, bukan dp
					float y = event.getY(); // dalam px, bukan dp
					
					if (x < 0.f) x = 0.f;
					if (x > ukuranUiPx) x = ukuranUiPx;
					if (y < 0.f) y = 0.f;
					if (y > ukuranUiPx) y = ukuranUiPx;

					sat = (1.f / ukuranUiPx * x);
					val = 1.f - (1.f / ukuranUiPx * y);

					warnaBaru = hitungWarna();
					// update view
					letakkanKeker();
					viewWarnaBaru.setBackgroundColor(warnaBaru);
					
					return true;
				}
				return false;
			}
		});
		
		dialog = new AlertDialog.Builder(context)
		.setView(view)
		.setPositiveButton(R.string.ambilwarna_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (AmbilWarnaDialog.this.listener != null) {
					AmbilWarnaDialog.this.listener.onOk(AmbilWarnaDialog.this, warnaBaru);
				}
			}
		})
		.setNegativeButton(R.string.ambilwarna_cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (AmbilWarnaDialog.this.listener != null) {
					AmbilWarnaDialog.this.listener.onCancel(AmbilWarnaDialog.this);
				}
			}
		})
		.create();
		
	}
	
	@SuppressWarnings("deprecation")
	protected void letakkanPanah() {
		float y = ukuranUiPx - (hue * ukuranUiPx / 360.f);
		if (y == ukuranUiPx) y = 0.f;
		
		AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) panah.getLayoutParams();
		layoutParams.y = (int) (y + 4);
		panah.setLayoutParams(layoutParams);
	}

	@SuppressWarnings("deprecation")
	protected void letakkanKeker() {
		float x = sat * ukuranUiPx;
		float y = (1.f - val) * ukuranUiPx;
		
		AbsoluteLayout.LayoutParams layoutParams = (AbsoluteLayout.LayoutParams) viewKeker.getLayoutParams();
		layoutParams.x = (int) (x + 3);
		layoutParams.y = (int) (y + 3);
		viewKeker.setLayoutParams(layoutParams);
	}

	float[] tmp01 = new float[3];
	private int hitungWarna() {
		tmp01[0] = hue;
		tmp01[1] = sat;
		tmp01[2] = val;
		return Color.HSVToColor(tmp01);
	}

	public void show() {
		dialog.show();
	}
}
/*******************************************************************************
 * Copyright (c) 2011 Jordan Thoms.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package biz.shadowservices.DegreesToolbox;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.quist.app.errorreporter.ExceptionReporter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

public class DataFetcher {
	// This class handles the actual fetching of the data from 2Degrees.
	public double result;
	public static final String LASTMONTHCHARGES = "Your last month's charges";
	private static String TAG = "2DegreesDataFetcher";
	private ExceptionReporter exceptionReporter;
	public enum FetchResult {
		SUCCESS,
		NOTONLINE,
		LOGINFAILED,
		USERNAMEPASSWORDNOTSET,
		NETWORKERROR,
		NOTALLOWED
	}
	public DataFetcher(ExceptionReporter e) {
		exceptionReporter = e;
	}
	public boolean isOnline(Context context) {
		 ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo info = cm.getActiveNetworkInfo();
		 if (info == null) {
			 return false;
		 } else {
			 return info.isConnectedOrConnecting();
		 }
	}
	public boolean isWifi(Context context) {
		 ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		 if (info == null) {
			 return false;
		 } else {
			 return info.isConnectedOrConnecting();
		 }
	}
	public boolean isRoaming(Context context) {
		 ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		 if (info == null) {
			 return false;
		 } else {
			 return info.isRoaming();
		 }
	}
	public boolean isBackgroundDataEnabled(Context context) {
		ConnectivityManager mgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return(mgr.getBackgroundDataSetting());
	}
	public boolean isAutoSyncEnabled() {
		// Get the autosync setting, if on a phone which has one.
		// There are better ways of doing this than reflection, but it's easy in this case
		// since then we can keep linking against the 1.6 SDK.
		if (android.os.Build.VERSION.SDK_INT >= 5) {
			Class<ContentResolver> contentResolverClass = ContentResolver.class;
			try {
				Method m = contentResolverClass.getMethod("getMasterSyncAutomatically", null);
				Log.d(TAG, m.toString());
				Log.d(TAG, m.invoke(null, null).toString());
				boolean bool = ((Boolean)m.invoke(null, null)).booleanValue();
				return bool;
			} catch (Exception e) {
				Log.d(TAG, "could not determine if autosync is enabled, assuming yes");
				return true;
			}
		} else {
			return true;
		}
	}
	public FetchResult updateData(Context context, boolean force) {
		//Open database
		DBOpenHelper dbhelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbhelper.getWritableDatabase();

		// check for internet connectivity
		try {
			if (!isOnline(context)) {
				Log.d(TAG, "We do not seem to be online. Skipping Update.");
				return FetchResult.NOTONLINE;
			}
		} catch (Exception e) {
			exceptionReporter.reportException(Thread.currentThread(), e, "Exception during isOnline()");
		}
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		if (!force) {
			try {
				if (sp.getBoolean("loginFailed", false) == true) {
					Log.d(TAG, "Previous login failed. Skipping Update.");
					DBLog.insertMessage(context, "i", TAG, "Previous login failed. Skipping Update.");
					return FetchResult.LOGINFAILED;
				}
				if(sp.getBoolean("autoupdates", true) == false) {
					Log.d(TAG, "Automatic updates not enabled. Skipping Update.");
					DBLog.insertMessage(context, "i", TAG, "Automatic updates not enabled. Skipping Update.");
					return FetchResult.NOTALLOWED;
				}
				if (!isBackgroundDataEnabled(context) && sp.getBoolean("obeyBackgroundData", true)) {
					Log.d(TAG, "Background data not enabled. Skipping Update.");
					DBLog.insertMessage(context, "i", TAG, "Background data not enabled. Skipping Update.");
					return FetchResult.NOTALLOWED;
				}
				if (!isAutoSyncEnabled() && sp.getBoolean("obeyAutoSync", true) && sp.getBoolean("obeyBackgroundData", true)) {
					Log.d(TAG, "Auto sync not enabled. Skipping Update.");
					DBLog.insertMessage(context, "i", TAG, "Auto sync not enabled. Skipping Update.");
					return FetchResult.NOTALLOWED;
				}
				if (isWifi(context) && !sp.getBoolean("wifiUpdates", true)) {
					Log.d(TAG, "On wifi, and wifi auto updates not allowed. Skipping Update");
					DBLog.insertMessage(context, "i", TAG, "On wifi, and wifi auto updates not allowed. Skipping Update");
					return FetchResult.NOTALLOWED;
				} else if (!isWifi(context)){
					Log.d(TAG, "We are not on wifi.");
					if (!isRoaming(context) && !sp.getBoolean("2DData", true)) {
						Log.d(TAG, "Automatic updates on 2Degrees data not enabled. Skipping Update.");
						DBLog.insertMessage(context, "i", TAG, "Automatic updates on 2Degrees data not enabled. Skipping Update.");
						return FetchResult.NOTALLOWED;
					} else if (isRoaming(context) && !sp.getBoolean("roamingData", false)) {
						Log.d(TAG, "Automatic updates on roaming mobile data not enabled. Skipping Update.");
						DBLog.insertMessage(context, "i", TAG, "Automatic updates on roaming mobile data not enabled. Skipping Update.");
						return FetchResult.NOTALLOWED;
					}

				}
			} catch (Exception e) {
				exceptionReporter.reportException(Thread.currentThread(), e, "Exception while finding if to update.");
			}

		} else {
			Log.d(TAG, "Update Forced");
		}
		
		try {
			String username = sp.getString("username", null);
			String password = sp.getString("password", null);
			if(username == null || password == null) {
				DBLog.insertMessage(context, "i", TAG, "Username or password not set.");
				return FetchResult.USERNAMEPASSWORDNOTSET;				
			}

			// Find the URL of the page to send login data to.
			Log.d(TAG, "Finding Action. ");
			HttpGetter loginPageGet = new HttpGetter("https://secure.2degreesmobile.co.nz/web/ip/login");
			String loginPageString = loginPageGet.execute();
			if (loginPageString != null) {
				Document loginPage = Jsoup.parse(loginPageString, "https://secure.2degreesmobile.co.nz/web/ip/login");
				Element loginForm = loginPage.getElementsByAttributeValue("name", "loginFrm").first();
				String loginAction = loginForm.attr("action");
				// Send login form
				List<NameValuePair> loginValues = new ArrayList <NameValuePair>();
				loginValues.add(new BasicNameValuePair("externalURLRedirect", ""));
				loginValues.add(new BasicNameValuePair("hdnAction", "login"));
				loginValues.add(new BasicNameValuePair("hdnAuthenticationType", "M"));
				loginValues.add(new BasicNameValuePair("hdnlocale", ""));
	
				loginValues.add(new BasicNameValuePair("userid", username));
				loginValues.add(new BasicNameValuePair("password", password));
				Log.d(TAG, "Sending Login ");
				HttpPoster sendLoginPoster = new HttpPoster(loginAction, loginValues);
				// Parse result
	
				Document homePage = Jsoup.parse(sendLoginPoster.execute());
				// Determine if this is a pre-pay or post-paid account.
				boolean postPaid;
				if (homePage.getElementById("p_p_id_PostPaidHomePage_WAR_Homepage_") == null) {
					Log.d(TAG, "Pre-pay account or no account.");
					postPaid = false;
				} else {
					Log.d(TAG, "Post-paid account.");
					postPaid = true;
				}
				
				Element accountSummary = homePage.getElementById("accountSummary");
				if (accountSummary == null) {
					Log.d(TAG, "Login failed.");
					return FetchResult.LOGINFAILED;
				}
				db.delete("cache", "", null);
				/* This code fetched some extra details for postpaid users, but on reflection they aren't that useful.
				 * Might reconsider this.
				 *
				 if (postPaid) {
				 
					Element accountBalanceSummaryTable = accountSummary.getElementsByClass("tableBillSummary").first();
					Elements rows = accountBalanceSummaryTable.getElementsByTag("tr");
					int rowno = 0;
					for (Element row : rows) {
						if (rowno > 1) {
							break;
						}
						//Log.d(TAG, "Starting row");
						//Log.d(TAG, row.html());
						Double value;
						try {
							Element amount = row.getElementsByClass("tableBillamount").first();
							String amountHTML = amount.html();
							Log.d(TAG, amountHTML.substring(1));
							value = Double.parseDouble(amountHTML.substring(1));
						} catch (Exception e) {
							Log.d(TAG, "Failed to parse amount from row.");
							value = null;
						}
						String expiresDetails = "";
						String expiresDate = null;
						String name = null;
						try {
							Element details = row.getElementsByClass("tableBilldetail").first();
							name = details.ownText();
							Element expires = details.getElementsByTag("em").first();
							if (expires != null) {
								 expiresDetails = expires.text();
							} 
							Log.d(TAG, expiresDetails);
							Pattern pattern;
							pattern = Pattern.compile("\\(payment is due (.*)\\)");
							Matcher matcher = pattern.matcher(expiresDetails);
							if (matcher.find()) {
								/*Log.d(TAG, "matched expires");
								Log.d(TAG, "group 0:" + matcher.group(0));
								Log.d(TAG, "group 1:" + matcher.group(1));
								Log.d(TAG, "group 2:" + matcher.group(2)); *
								String expiresDateString = matcher.group(1);
								Date expiresDateObj;
								if (expiresDateString != null) {
									if (expiresDateString.length() > 0) {
										try {
											expiresDateObj = DateFormatters.EXPIRESDATE.parse(expiresDateString);
											expiresDate = DateFormatters.ISO8601DATEONLYFORMAT.format(expiresDateObj);
										} catch (java.text.ParseException e) {
											Log.d(TAG, "Could not parse date: " + expiresDateString);
										}
									}	
								}
							}
						} catch (Exception e) {
							Log.d(TAG, "Failed to parse details from row.");
						}
						String expirev = null;
						ContentValues values = new ContentValues();
						values.put("name", name);
						values.put("value", value);
						values.put("units", "$NZ");
						values.put("expires_value", expirev );
						values.put("expires_date", expiresDate);
						db.insert("cache", "value", values );
						rowno++;
					}
				} */
				Element accountSummaryTable = accountSummary.getElementsByClass("tableAccountSummary").first();
				Elements rows = accountSummaryTable.getElementsByTag("tr");
				for (Element row : rows) {
					// We are now looking at each of the rows in the data table.
					//Log.d(TAG, "Starting row");
					//Log.d(TAG, row.html());
					Double value;
					String units;
					try {
						Element amount = row.getElementsByClass("tableBillamount").first();
						String amountHTML = amount.html();
						//Log.d(TAG, amountHTML);
						String[] amountParts = amountHTML.split("&nbsp;", 2);
						//Log.d(TAG, amountParts[0]);
						//Log.d(TAG, amountParts[1]);
						if (amountParts[0].equals("Included")) {
							value = Values.INCLUDED;
						} else {
							try {
								value = Double.parseDouble(amountParts[0]);	
							} catch (NumberFormatException e) {
								exceptionReporter.reportException(Thread.currentThread(), e, "Decoding value.");
								value = 0.0;
							}
						}
						units = amountParts[1];
					} catch (NullPointerException e) {
						//Log.d(TAG, "Failed to parse amount from row.");
						value = null;
						units = null;
					}
					Element details = row.getElementsByClass("tableBilldetail").first();
					String name = details.getElementsByTag("strong").first().text();
					Element expires = details.getElementsByTag("em").first();
					String expiresDetails = "";
					if (expires != null) {
						 expiresDetails = expires.text();
					} 
					Log.d(TAG, expiresDetails);
					Pattern pattern;
					if (postPaid == false) {
						pattern = Pattern.compile("\\(([\\d\\.]*) ?\\w*? ?expiring on (.*)\\)");
					} else {
						pattern = Pattern.compile("\\(([\\d\\.]*) ?\\w*? ?will expire on (.*)\\)");
					}
					Matcher matcher = pattern.matcher(expiresDetails);
					Double expiresValue = null;
					String expiresDate = null;
					if (matcher.find()) {
						/*Log.d(TAG, "matched expires");
						Log.d(TAG, "group 0:" + matcher.group(0));
						Log.d(TAG, "group 1:" + matcher.group(1));
						Log.d(TAG, "group 2:" + matcher.group(2)); */
						try {
							expiresValue = Double.parseDouble(matcher.group(1));
						} catch (NumberFormatException e) {
							expiresValue = null;
						}
						String expiresDateString = matcher.group(2);
						Date expiresDateObj;
						if (expiresDateString != null) {
							if (expiresDateString.length() > 0) {
								try {
									expiresDateObj = DateFormatters.EXPIRESDATE.parse(expiresDateString);
									expiresDate = DateFormatters.ISO8601DATEONLYFORMAT.format(expiresDateObj);
								} catch (java.text.ParseException e) {
									Log.d(TAG, "Could not parse date: " + expiresDateString);
								}
							}	
						}
					}
					ContentValues values = new ContentValues();
					values.put("name", name);
					values.put("value", value);
					values.put("units", units);
					values.put("expires_value", expiresValue);
					values.put("expires_date", expiresDate);
					db.insert("cache", "value", values );
				}
				
				if(postPaid == false) {
					Log.d(TAG, "Getting Value packs...");
					// Find value packs
					HttpGetter valuePacksPageGet = new HttpGetter("https://secure.2degreesmobile.co.nz/group/ip/prevaluepack");
					String valuePacksPageString = valuePacksPageGet.execute();
					//DBLog.insertMessage(context, "d", "",  valuePacksPageString);
					if(valuePacksPageString != null) {
						Document valuePacksPage = Jsoup.parse(valuePacksPageString);
						Elements enabledPacks = valuePacksPage.getElementsByClass("yellow");
						for (Element enabledPack : enabledPacks) {
							Element offerNameElemt = enabledPack.getElementsByAttributeValueStarting("name", "offername").first();
							String offerName = offerNameElemt.val();
							DBLog.insertMessage(context, "d", "", "Got element: " + offerName);
							ValuePack[] packs = Values.valuePacks.get(offerName);
							if (packs == null) {
								DBLog.insertMessage(context, "d", "", "Offer name: " + offerName + " not matched.");
							} else {
								for (ValuePack pack: packs) {
								//	Cursor csr = db.query("cache", null, "name = '" + pack.type.id + "'", null, null, null, null);
							//		if (csr.getCount() == 1) {
						//				csr.moveToFirst();
										ContentValues values = new ContentValues();
										// Not sure why adding on the previous value?
										//values.put("plan_startamount", csr.getDouble(4) + pack.value);
										//DBLog.insertMessage(context, "d", "", "Pack " + pack.type.id + " start value set to " + csr.getDouble(4) + pack.value);
										values.put("plan_startamount", pack.value);
										values.put("plan_name", offerName);
										DBLog.insertMessage(context, "d", "", "Pack " + pack.type.id + " start value set to " + pack.value);
										db.update("cache", values, "name = '" + pack.type.id + "'", null);
							//		} else {
							//			DBLog.insertMessage(context, "d", "", "Pack " + pack.type.id + " Couldn't find item to add to");
							//		}
								//	csr.close();
								}
							}
						}
					}
				}

				
				SharedPreferences.Editor prefedit = sp.edit();
				Date now = new Date();
				prefedit.putString("updateDate", DateFormatters.ISO8601FORMAT.format(now));
				prefedit.putBoolean("loginFailed", false);
		        prefedit.putBoolean("networkError", false);
				prefedit.commit();
				DBLog.insertMessage(context, "i", TAG, "Update Successful");
				return FetchResult.SUCCESS;

			}
		} catch (ClientProtocolException e) {
			DBLog.insertMessage(context, "w", TAG, "Network error: " + e.getMessage());
			return FetchResult.NETWORKERROR;
		} catch (IOException e) {
			DBLog.insertMessage(context, "w", TAG, "Network error: " + e.getMessage());
			return FetchResult.NETWORKERROR;
		}
		finally {
			db.close();
		}
		return null;
	}
}
/*******************************************************************************
 * Copyright (c) 2011 Jordan Thoms.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package biz.shadowservices.DegreesToolbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import biz.shadowservices.DegreesToolbox.DataFetcher.FetchResult;
import biz.shadowservices.DegreesToolbox.Preferences.BalancePreferencesActivity;
import biz.shadowservices.DegreesToolbox.util.StackTraceUtil;

public class MainActivity extends BaseActivity {
	private static String TAG = "2DegreesPhoneBalanceMainActivity";
	private UpdateReciever reciever;

	ProgressDialog progressDialog = null;
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button buyPackButton = (Button) findViewById(R.id.buyPackButton);
        buyPackButton.setOnClickListener(buyPackListener);
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getSupportMenuInflater();
    	inflater.inflate(R.menu.mainmenu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.refresh_button:
    		forceUpdate();
    		return true;
    	case R.id.openPreferences:
    		Intent openPreferences = new Intent(this, BalancePreferencesActivity.class);
    		startActivityForResult(openPreferences, 2);
    		return true;
    	case R.id.openLog:
    		startActivity(new Intent(this, LogViewActivity.class));
    		return true;
    	case R.id.callCustomerService:
    		startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0800022022")));
    		return true;
    	case R.id.topup:
    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setTitle("Choose a topup method:");
    		builder.setItems(new CharSequence[] {
    				"Voucher topup"
    		}, new DialogInterface.OnClickListener() {
    		    public void onClick(DialogInterface dialog, int item) {
    		    	switch(item) {
    		    	case 0:
    		    		String encodedHash = Uri.encode("#");
    		    		startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:*100*2" + encodedHash)));
    		    		break;
    		    	}
    		    }
    		});
    		AlertDialog alert = builder.create();
    		alert.show();
    		return true;
    	case R.id.openAbout:
    		//Open the about dialog
    		try {
				AboutDialog.create(this).show();
			} catch (NameNotFoundException e) {
				GATracker.getInstance().trackEvent("Exceptions", e.getMessage() + "Creating about dialog", StackTraceUtil.getStackTrace(e), 0);
			}
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
    @Override
    public void onResume() {
    	super.onResume();
    	// Check if username/password is not set.
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this); 
		String username = sp.getString("username", "");
		String password = sp.getString("password", "");
		if (username.equals("") || password.equals("")) {
			// Username/password is not set, launch setup wizard
			Toast.makeText(this, "Username or password empty", 3);
			startActivityForResult(new Intent(this, SetupWizard.class), 1);
		}
    	refreshData();
    	reciever = new UpdateReciever(this);
    	// Register the reciever, so if an update happens while we are in the activity
    	// it will be updated
    	registerReceiver(reciever, new IntentFilter(UpdateWidgetService.NEWDATA));
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == 1) {
    		forceUpdate();
    	} else if (requestCode == 2) {
    		if (resultCode == BalancePreferencesActivity.RESULT_FORCE_UPDATE) {
    			forceUpdate();
    		}
    	}
    }
    private void forceUpdate() {
		progressDialog = ProgressDialog.show(this, null , " Loading. Please wait ... ", true);
		progressDialog.show();
		Intent update = new Intent(this, UpdateWidgetService.class);
		update.putExtra("biz.shadowservices.PhoneBalanceWidget.forceUpdates", true);
		startService(update);
    	GATracker.getInstance().trackEvent("Actions", "Manual Refresh", "MainActivity", 0);
    
    }
    @Override
    public void onPause() {
    	super.onPause();
    	// Unregister the broadcast reciever, since we are now no longer interested in updates happening.
    	unregisterReceiver(reciever);
    	if(progressDialog != null) {
    		progressDialog.dismiss();
    		progressDialog = null;
    	}
    }
    private OnClickListener buyPackListener = new OnClickListener() {
    	public void onClick(View v) {
    		valuePackMenuNodeView(Values.purchaseValuePacks);
    	}
    };
    private OnClickListener refreshListener = new OnClickListener() {
    	public void onClick(View v) {
    		MainActivity.this.forceUpdate();
    	}
    };
    private void valuePackMenuNodeView(final PackTreeNode node) {
    	if (node instanceof PackTreeLeaf) {
    		// We have reached a leaf in the menu, ask for confirmation to send.
    		askToSend((PackTreeLeaf) node);
    		return;
    	}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(node.getQuestionText());
		builder.setItems(node.getChildrenCharSequence(), new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	//Get the selected item's node in the tree
		    	PackTreeNode selectedNode = node.getAt(item);
		    	// Recursively open the node.
		    	valuePackMenuNodeView(selectedNode);
		    }
		});
		AlertDialog alert = builder.create();
		alert.show();
    }
    public void askToSend(final PackTreeLeaf leaf) {
    	AlertDialog.Builder confirmDialog = new AlertDialog.Builder(MainActivity.this);
    	confirmDialog.setMessage(leaf.getConfirmText() + leaf.getTitle() + " by sending '" + leaf.getMessage() + "' to 233?")
    		.setCancelable(false)
    		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					send233SMS(leaf.getMessage());
			    	GATracker.getInstance().trackEvent("Purchase", leaf.getTitle(), "", leaf.getValue());
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {						
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
    	AlertDialog confirm = confirmDialog.create();
    	confirm.show();

    }
    public void refreshData() {
        // Load, display data.
		DBOpenHelper dbhelper = new DBOpenHelper(this);
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		Cursor cursor = db.query("cache", new String[] {"name","value", "units", "expires_value", "expires_date", "plan_startamount", "plan_name"} , null, null,null,null,null);
		cursor.moveToFirst(); 
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.lineslayout);
		layout.removeAllViews();
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		{ 
			RelativeLayout firstLine = new RelativeLayout(this);
			firstLine.setPadding(1,5,5,2);
			TextView col1 = new TextView(this);
			col1.setTypeface(Typeface.DEFAULT_BOLD);
			TextView col3 = new TextView(this);
			col3.setTypeface(Typeface.DEFAULT_BOLD);
			RelativeLayout.LayoutParams col3LayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			col3LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			RelativeLayout.LayoutParams col1LayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			col1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			//col1LayoutParams.addRule(RelativeLayout.LEFT_OF, col3.getId() );
			String updateStatusStr = sp.getString("updateStatus", "SUCCESS");
			FetchResult updateStatus = FetchResult.valueOf(updateStatusStr);
			Log.d(TAG, updateStatus.toString());
			switch(updateStatus) {
			case LOGINFAILED:
			case USERNAMEPASSWORDNOTSET:
				col1.setText("Last Update -- Login failed, set the correct username/password details in menu->preferences and then press back.");
				break;
			case NETWORKERROR:
				col1.setText("Last Update -- Network Error.");
				break;
			case NOTONLINE:
				col1.setText("Last Update -- Not Online");
				break;
			case SUCCESS:
				col1.setText("Last Update");
				String updateDateString = sp.getString("updateDate", "");
				String updateDate = "";
				try {
					Date lastUpdate = DateFormatters.ISO8601FORMAT.parse(updateDateString);
					updateDate = DateFormatters.DATETIME.format(lastUpdate);
				} catch (Exception e) {
					updateDate = "Unknown";
				}
				col3.setText(updateDate);
				break;
			case NOTALLOWED:
				col1.setText("Last Update -- Not allowed by settings.");
				break;
			}				
			firstLine.addView(col1,  col1LayoutParams);
			firstLine.addView(col3, col3LayoutParams);
			layout.addView(firstLine);
			View ruler = new View(this);
			ruler.setBackgroundColor(0xFF9C9C9C);
			layout.addView(ruler, new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, 2));
		}
		for (int i=0; i<cursor.getCount(); i++) {
			RelativeLayout firstLine = new RelativeLayout(this);
			firstLine.setPadding(0,5,5,0);
			TextView col1 = new TextView(this);
			col1.setTypeface(Typeface.DEFAULT_BOLD);
			TextView col3 = new TextView(this);
			col3.setTypeface(Typeface.DEFAULT_BOLD);
			RelativeLayout.LayoutParams col3LayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			col3LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			RelativeLayout.LayoutParams col1LayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			col1LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			//col1LayoutParams.addRule(RelativeLayout.LEFT_OF, col3.getId() );
			col1.setText(cursor.getString(0));
			firstLine.addView(col1,  col1LayoutParams);
			StringBuilder valueStringBuilder = new StringBuilder();
			if(cursor.getString(1) != null) {
				if(Math.abs((cursor.getDouble(1) - Values.INCLUDED)) < 0.01) {
					valueStringBuilder.append("Included");
				} else {
					valueStringBuilder.append(cursor.getString(1));
				}
				valueStringBuilder.append(" ");
			}
			if(cursor.getString(2) != null) {
				valueStringBuilder.append(cursor.getString(2));
			}
			col3.setText(valueStringBuilder.toString());
			firstLine.addView(col3, col3LayoutParams);
			layout.addView(firstLine);
			//if (!cursor.isNull(4)) {
			String planName = "";
			ProgressBar bar = null;
			LayoutParams progressBarParams= null;
			if (!cursor.isNull(5)) {
				Log.d(TAG, cursor.getString(0) + " Not null: " + cursor.getInt(5) );
				double currentVal = cursor.getDouble(1);
				double startVal = cursor.getDouble(5);
				planName = cursor.getString(6) + " Pack: ";
				bar = (ProgressBar)getLayoutInflater().inflate(R.layout.pack_progress_bar, null);
				bar.setMax((int)startVal);
				Log.d(TAG, String.valueOf(startVal - currentVal));
				bar.setProgress((int)(startVal - currentVal));
				progressBarParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			}
			TextView row2 = new TextView(this);
			row2.setPadding(10,0,5,5);
			SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd");
			Date expiryDate;
			String expiresInfo = "";
			String expiresMiddle = " expiring on ";
			if (cursor.getString(0).equals(DataFetcher.LASTMONTHCHARGES)) {
				expiresMiddle = " payment is due ";
			}
			try {
				String date = cursor.getString(4);
				if (date != null) {
					if(date.length() > 0) {
						expiryDate = iso.parse(date);
						SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
						String expiryDateString = output.format(expiryDate);
						expiresInfo = cursor.getString(3);
						if (expiresInfo != null ) {
							if (expiresInfo.length() > 0) {
								expiresInfo += " " + cursor.getString(2) + expiresMiddle + expiryDateString ;
							} else {
								expiresInfo = expiresMiddle + expiryDateString ;
							}
						} else {
							expiresInfo = expiresMiddle  + expiryDateString ;
						}
					}
				}	
			} catch (ParseException e) {
				Log.e(TAG, "Could not parse date from DB.");
			}
			if (expiresInfo.length() > 0) {
				row2.setText(planName + expiresInfo);
				layout.addView(row2);
			}
			if (!cursor.isNull(5)) {
				layout.addView(bar, progressBarParams);
			}
			cursor.moveToNext(); 
			View ruler = new View(this);
			ruler.setBackgroundColor(0xFF9C9C9C);
			layout.addView(ruler, new LinearLayout.LayoutParams( LinearLayout.LayoutParams.FILL_PARENT, 2));
		}
		cursor.close();
		db.close();
    }
       
    public class UpdateReciever extends BroadcastReceiver {

        private MainActivity activity;

        public UpdateReciever(MainActivity activity) {
            this.activity = activity;
        }

        public void onReceive(Context context, Intent intent) {
            if(activity != null) {
            	if(activity.progressDialog != null) {
            		activity.progressDialog.dismiss();
            		activity.progressDialog = null;
            	}
            	activity.refreshData();
            }

        }
    }
    private void send233SMS(String message)
    {                 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage("233", null, message, null, null);
    	Log.d(TAG, "sent message: " + message);
    }    
}
/*******************************************************************************
 * Copyright (c) 2011 Jordan Thoms.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package biz.shadowservices.DegreesToolbox;



import java.io.IOException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

import biz.shadowservices.DegreesToolbox.DataFetcher.FetchResult;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import de.quist.app.errorreporter.ReportingService;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class UpdateWidgetService extends ReportingService implements Runnable {
	// This is the service which handles updating the widgets.
	private static String TAG = "2DegreesUpdateWidgetService";
	public static String NEWDATA = "BalanceWidgetNewDataAvailable12";
	/**
     * Flag if there is an update thread already running. We only launch a new
     * thread if one isn't already running.
     */
    private static boolean isThreadRunning = false;
    private static Object lock = new Object();
    private boolean force = false;
    public class LocalBinder extends Binder {
        UpdateWidgetService getService() {
            return UpdateWidgetService.this;
        }
    }
    private final IBinder mBinder = new LocalBinder();
    static {
    	// Populate the list of widget updaters - in a static initaliser block since it only needs
    	// to happen once.
    	Values.widgetUpdaters.add(new WidgetUpdater1x2());
    	Values.widgetUpdaters.add(new WidgetUpdater2x2());    	
    }
	 // This is the old onStart method that will be called on the pre-2.0
	 // platform.  On 2.0 or later we override onStartCommand() so this
	 // method will not be called.
	 @Override
	 public void onStart(Intent intent, int startId) {
	     handleCommand(intent);
	 }
    public int onStartCommand(Intent intent, int startId) {
    	handleCommand(intent);
    	return START_NOT_STICKY;
    }
    private void handleCommand(Intent intent) {
    	Log.d(TAG, "Starting service");
    	if (intent != null) {
    		force = intent.getBooleanExtra("biz.shadowservices.PhoneBalanceWidget.forceUpdates", false);
    	}
    	// Locking to make sure we only run one thread at a time.
    	synchronized (lock) {
    		if(!isThreadRunning) {
    	    	Log.d(TAG, "Thread not running, starting.");
    			isThreadRunning = true;
    			new Thread(this).start();
    		} else {
    	    	Log.d(TAG, "Thread already running, not doing anything.");
    		}

    	}
    }
	@Override
	public void run() {
		//Build update
    	Log.d(TAG, "Building updates");
		for (AbstractWidgetUpdater updater : Values.widgetUpdaters) {
			updater.widgetLoading(this);
		}
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this); 
		String updateDateString = sp.getString("updateDate", "");
		boolean update = true;
		if (!force) {
			try {
				Date now = new Date();
				Date lastUpdate = DateFormatters.ISO8601FORMAT.parse(updateDateString);
			    long diff = now.getTime() - lastUpdate.getTime();
			    long mins = diff / (1000 * 60);
			    if (mins < Integer.parseInt(sp.getString("freshTime", "30"))) {
			    	update = false;
			    }
			} catch (Exception e) {
				Log.d(TAG, "Failed when deciding whether to update");
			}
		}
    	DataFetcher dataFetcher = new DataFetcher(getExceptionReporter());
    	FetchResult result = null;
		if(update) {
				result = dataFetcher.updateData(this, force);
			    // Login failed - set error for the activity so it can display the information
			    Editor edit = sp.edit();
			    edit.putString("updateStatus", result.toString());
			    edit.commit();
			    Log.d(TAG, "Building updates -- data updated. Result: " +  result.toString());
		} else {
		    Log.d(TAG, "Building updates -- data fresh, not updated");
		    result = FetchResult.SUCCESS;
		}

		for (AbstractWidgetUpdater updater : Values.widgetUpdaters) {
			updater.updateWidgets(this, force, result);
		}

    	Log.d(TAG, "Sent updates");
    	Intent myIntent = new Intent(NEWDATA);
    	sendBroadcast(myIntent);
    	// We now dispatch to GA.
    	// Wrap up in a catch all since this has been having problems
    	try {
    			GATracker.getInstance(getApplication()).incrementActivityCount();
    			GATracker.getInstance().dispatch();
    			GATracker.getInstance().decrementActivityCount();
    	} catch (Exception e) {
    		getExceptionReporter().reportException(Thread.currentThread(), e, "GA Tracking in updateWidgetService");
    	}
    	isThreadRunning = false;
    	// Stop the service. A lot of apps leave their widget update services running, which is completely unnecessary!
    	stopSelf();
	}

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
	
	

}
/*******************************************************************************
 * Copyright (c) 2011 Jordan Thoms.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package biz.shadowservices.DegreesToolbox;

import java.util.ArrayList;
import java.util.List;

import biz.shadowservices.DegreesToolbox.DataFetcher.FetchResult;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetUpdater1x2 extends AbstractWidgetUpdater {
	// Handle updates for the 1x2 widget.
	private static int LINELIMIT = 17;
	private static String TAG = "2DegreesPhoneBalanceWidget2x1";
	@Override
	protected void fillRemoteViews(RemoteViews updateViews, Context context, int widgetId, FetchResult error) {
		Log.d(TAG, "FillRemoteViews, error code: " + error);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		int backgroundId = sp.getInt("widgetSettings[" + widgetId + "][backgroundId]", 0);
		updateViews.setImageViewResource(R.id.widget1x2_background, Values.backgroundIds[backgroundId]);
		Log.d(TAG, "id: " + Values.backgroundIds[backgroundId]);

		if (android.os.Build.VERSION.SDK_INT >= 8) {
			int transparencyPercentage =  sp.getInt("widgetSettings[" + widgetId + "][transparency]", 0);
			float transparencyMultiplier = (100 - transparencyPercentage) / (float) 100;
			updateViews.setInt(R.id.widget1x2_background, "setAlpha", (int) (255 * transparencyMultiplier));
		}

    	switch (error) {
    	case LOGINFAILED:
			updateViews.setTextViewText(R.id.widget1x2_line1, "Login failed");
			break;
    	case USERNAMEPASSWORDNOTSET:
			updateViews.setTextViewText(R.id.widget1x2_line1, "Username or password not set");
			break;
    	default:
    		// Clear widget
    		int[] lineIds = {R.id.widget1x2_line1, R.id.widget1x2_line2, R.id.widget1x2_line3, R.id.widget1x2_right1, R.id.widget1x2_right2 };
    		for (int line : lineIds) {
    			updateViews.setTextViewText(line, "");
    			updateViews.setInt(line, "setTextColor", sp.getInt("widgetSettings[" + widgetId + "][textColor]", 0xffffffff));
    		}
			updateViews.setInt(R.id.widget1x2_lastupdate, "setTextColor", sp.getInt("widgetSettings[" + widgetId + "][textColor]", 0xffffffff));
    		List<Line> lines = buildLines(context);
    		
    		if (lines.size() > 0) {
    			updateViews.setTextViewText(R.id.widget1x2_line1, lines.get(0).getLineContent());
    			if (lines.size() > 1) {
    				updateViews.setTextViewText(R.id.widget1x2_line2, lines.get(1).getLineContent());
    				if(lines.size() > 2) {
    					updateViews.setTextViewText(R.id.widget1x2_line3, lines.get(2).getLineContent());
    					for (int i = 3; i < lines.size(); i++) {
    						if((lines.get(1).getLineContent() + lines.get(3).getLineContent()).length() < LINELIMIT) {
    							updateViews.setTextViewText(R.id.widget1x2_right1, lines.get(i).getLineContent());
    							lines.remove(i);
    						}
    					}
    					for (int i = 3; i < lines.size(); i++) {
    						if((lines.get(2).getLineContent() + lines.get(3).getLineContent()).length() < LINELIMIT) {
    							updateViews.setTextViewText(R.id.widget1x2_right2, lines.get(i).getLineContent());
    						}
    					}
    				}
    			}
    		}
        	break;
    	}

		updateViews.setTextViewText(R.id.widget1x2_lastupdate, getUpdateDateString(context));
		Intent viewIntent = new Intent(context, MainActivity.class);
        PendingIntent openAppPending = PendingIntent.getActivity(context, 0, viewIntent, 0);
        Intent updateIntent = new Intent(context, UpdateWidgetService.class);
        updateIntent.putExtra("biz.shadowservices.PhoneBalanceWidget.forceUpdates", true);
        
        if (sp.getBoolean("press_widget_open_app", true)) {
            updateViews.setOnClickPendingIntent(R.id.widget1x2_widget, openAppPending);
        } else {
            updateViews.setOnClickPendingIntent(R.id.widget1x2_widget, PendingIntent.getService(context, 0, updateIntent, 0));
        }
        
        updateViews.setOnClickPendingIntent(R.id.widget1x2_refreshButton, PendingIntent.getService(context, 0, updateIntent, 0));

        if (!sp.getBoolean("show_refresh_button", true)) {
        	updateViews.setViewVisibility(R.id.widget1x2_refreshButton, View.GONE);
        }
	}
	@Override
	protected int getLayoutId() {
		return R.layout.balance_widget_1x2;
	}
    private List<Line> buildLines(Context context) {
		DBOpenHelper dbhelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		Cursor result = db.query("cache", new String[] {"value", "units"} , null, null,null,null,null);
		List<Line> lines = new ArrayList<Line>();
		try {
			result.moveToFirst();
			for (int i=0; i<result.getCount(); i++) {
				if (result.getString(0) != null) {
					if (result.getInt(0) != 0) {
						if (result.getString(1) != null) {
							if (!result.getString(1).equals("$NZ")) {
								if(!(Math.abs((result.getDouble(0) - Values.INCLUDED)) < 0.001)) {
									lines.add(new Line(Math.round(result.getDouble(0)) + " " + result.getString(1)));
								}
							} else {
								if(result.getDouble(0) > 100) {
									lines.add(new Line("$" + Math.round(result.getDouble(0))));
								} else {
									lines.add(new Line(Util.money.format(result.getDouble(0))));
								}
							}
							Log.d(TAG, result.getString(0) + " " + result.getString(1));
						} else {
							lines.add(new Line(String.valueOf(Math.round(result.getDouble(0)))));
						}
					}
				}
				result.moveToNext();
			}
		} finally {
	        result.close();
	        db.close();
		}
		return lines;
    }
	@Override
	protected ComponentName getComponentName(Context context) {
		return new ComponentName(context, PhoneBalanceWidget.class);
	}
	
	@Override
	protected String getFriendlyName() {
		return "1x2 Widget";
	}
	@Override
	protected void fillRemoteViewsLoading(RemoteViews updateViews,
			Context context) {
		updateViews.setTextViewText(R.id.widget1x2_lastupdate, "Loading");
		Intent viewIntent = new Intent(context, MainActivity.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0, viewIntent, 0);
        updateViews.setOnClickPendingIntent(R.id.widget1x2_widget, pending);
	}

}
package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import loaders.MapLoader;
import loaders.TileLoader;
import test.ChunkData;

/**
 *
 * @author mike
 */
public class Game implements ActionListener {

    JFrame frame;
    DP panel;
    KL keyListener;
    MapLoader loader;
    Timer time;
    TileLoader arb;
    Player player;
    
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();

    BufferedImage map;
    
    int animationFrame;
    int[][] mapTileNumbers;
    Tile[] tiles;
    Image topHud;
    Image bottomHud;
    Image menu;
    int x = 0;
    int y = 0;
    int m = 0;
    int n = 0;
    int incr = 0;
    int t = 0;

    //Just initialize everything - Look at classes for specifics
    public Game() {

        arb = new TileLoader();
        arb.loadTiles();
        frame = new JFrame("2D Game");
        panel = new DP(true);
        keyListener = new KL();
        loader = new MapLoader();
        loader.load();
        player = new Player(0,0, "playerSprites.png");

        mapTileNumbers = loader.getTiles();
        tiles = new Tile[250000];
        topHud = new ImageIcon("src\\res\\tophud-transparent.png").getImage();
        bottomHud = new ImageIcon("src\\res\\bottomhud-transparent.png").getImage();
        menu = new ImageIcon("src\\res\\Menu.png").getImage();

    }

    //create a Game object and call its start method
    public static void main(String[] args) {
        Game g = new Game();
        g.start();
    }

    //sets up the frame
    //generates the map
    //starts the timer
    public void start() {
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().add(BorderLayout.CENTER, panel);
        panel.setFocusable(true);
        panel.addKeyListener(keyListener);
        frame.setVisible(true);
        generateMap();
        for(int i = 0; i < 5; i++) {
            int x = (int) (Math.random() * 450) + 1;
            int y = (int) (Math.random() * 450) + 1;
            enemies.add(new Enemy(x,y,"enemySprites2.png"));
        }
        time = new Timer(5, this);
        time.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int enemyCollisions = 0;
        for(int i = 0; i < enemies.size(); i++) {
            if(player.canMoveEntity(enemies.get(i))) {
                enemyCollisions++;
            }
        }
        //check for a collision with tiles close to the player
        if(player.canMove(tiles,player.getDir()) && enemyCollisions == 0) {
            player.move();
        }
        if(enemies.size() > 0) {
            for(int j = 0; j < enemies.size(); j++) {
                if(!player.canMoveEntity(enemies.get(j))) {
                    enemies.get(j).moveToPlayer(player, tiles, enemies.get(j).getDir());
                } else {
                    enemies.get(j).stopFollow();
                }
            }
        }
        //repaint the screen
        frame.repaint();

    }

    //sets up the map
    //it just populates the tile[] with tiles from the map file
    public void generateMap() {
        //loop through the tile numbers from the map file
        for(int i = 0; i < mapTileNumbers.length*32; i++) {
            //for the actual screen x and y of the tile
            if(x == mapTileNumbers.length*32) {
                x = 0;
                y+=32;
            }
            if(n > mapTileNumbers[m].length -1) {
                n = 0;
                m++;
            }
            //loop and check if any of the tile numbers that were loaded are equal to the increment
            for(int j = 1; j < arb.tiles.length; j++) {
                //if so then set the tile at the current increment to a tile with that tile number
                // also set that tiles x and y
                if(mapTileNumbers[m][n] == j) {
                    tiles[incr] = new Tile(arb.getTile(j));
                    tiles[incr].setX(x);
                    tiles[incr].setY(y);
                    incr++;
                }
            }
            n++;
            x+=32;
        }
        map = new ChunkData().chunk(tiles, incr);

    }

    public class DP extends JPanel {

		private static final long serialVersionUID = 1L;

		public DP(boolean b) {
			super(true);
		}

		//draw stuff
        @Override
        public void paintComponent(Graphics g) {
            g.setFont(new Font("courier new", Font.TYPE1_FONT, 12));
            /*for(int i = 0; i < tiles.length; i++) {
            	if(tiles[i] != null) {
            		g.drawImage(tiles[i].getTile(), tiles[i].getX(), tiles[i].getY(), null);
            	}
            }*/
            g.drawImage(map, 0, 0, null);
            if(animationFrame < 30) {
                g.drawImage(player.getSprite(player.getDir(),1), player.getX(), player.getY(), 24, 32, null);
            } else if(animationFrame > 30 && animationFrame < 79) {
                g.drawImage(player.getSprite(player.getDir(),0), player.getX(), player.getY(), 24, 32, null);
            } else if (animationFrame > 80 && animationFrame < 150) {
                g.drawImage(player.getSprite(player.getDir(),2), player.getX(), player.getY(), 24, 32, null);
            }
            if (animationFrame < 150) {
                animationFrame++;
            } else {
                animationFrame = 0;
            }
            for(int j = 0; j < enemies.size(); j++) {
                if(animationFrame < 30) {
                    g.drawImage(enemies.get(j).getSprite(enemies.get(j).getDir(),1), enemies.get(j).getX(), enemies.get(j).getY(), 24, 32, null);
                    g.drawString("" + enemies.get(j).getDistance(), enemies.get(j).getX(), enemies.get(j).getY() -10);
                } else if(animationFrame > 30 && animationFrame < 79) {
                    g.drawImage(enemies.get(j).getSprite(enemies.get(j).getDir(),1), enemies.get(j).getX(), enemies.get(j).getY(), 24, 32, null);
                    g.drawString("" + enemies.get(j).getDistance(), enemies.get(j).getX(), enemies.get(j).getY() -10);
                } else if (animationFrame > 80 && animationFrame < 150) {
                    g.drawImage(enemies.get(j).getSprite(enemies.get(j).getDir(),1), enemies.get(j).getX(), enemies.get(j).getY(), 24, 32, null);
                    g.drawString("" + enemies.get(j).getDistance(), enemies.get(j).getX(), enemies.get(j).getY() -10);
                }
                if (animationFrame < 150) { 
                    animationFrame++;
                } else {
                    animationFrame = 0;
                }
            }
            g.drawImage(topHud, 5, 5, null);
            g.setFont(new Font("courier new", Font.TYPE1_FONT, 20));
            g.drawString("" +player.getLevel(),40,42);
            //draw background for top hud display bars 
            g.setColor(Color.DARK_GRAY);
            g.fillRect(69,75,51,4);
            g.fillRect(69,81,51,4);
            g.fillRect(69,87,51,4);
            //draw specifics
            //bar width = (cHealth * barWidth)/mHealth
            g.setColor(Color.RED);
            g.fillRect(69,75,(player.getCHealth()*51)/player.getMHealth(),4);
            g.setColor(Color.BLUE);
            g.fillRect(69,81,(player.getCMana()*51)/player.getMMana(),4);
            g.setColor(Color.GREEN);
            g.fillRect(69,87,(player.getCXP()*51)/player.getNextLevelXP(),4);
            //bottom hud
            g.drawImage(bottomHud, 130, 497, null);
            //draw info on top of bottom hud
            g.setColor(Color.BLACK);
            g.setFont(new Font("courier new", Font.TYPE1_FONT, 12));
            g.drawString("HEALTH: " + player.getCHealth() + "/" + player.getMHealth(), 205, 507);
            g.drawString("MANA: " + player.getCMana() + "/" + player.getMMana(), 365, 507);
            g.drawString("XP: " + player.getCXP() + "/" + player.getNextLevelXP(), 490, 507);
            //Draw debug stuff
            g.drawString("X: " + player.getX(), 0, 500);
            g.drawString("DX: " + player.getDX(), 0, 510);
            g.drawString("Y: " + player.getY(), 0, 520);
            g.drawString("DY: " + player.getDY(), 0, 530);
            g.drawString("DIR: " + player.getDir(), 0, 540);
            g.drawString("LAST DIR: " + player.getLastDirection(), 0, 550);
            if(player.getPaused()) {
                g.setFont(new Font("courier new", Font.TYPE1_FONT, 26));
                g.drawImage(menu, 250, 100, null);
                g.drawString("PAUSED", 313, 137);
                
            }
            
            g.dispose();
        }

    }

    //for getting key input
    public class KL extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

    }

}
package clione_sql;

import org.junit.Test;
import tetz42.clione.gen.SQLGenerator;
import tetz42.clione.node.SQLNode;
import tetz42.clione.parsar.SQLParser;

import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static tetz42.clione.SQLManager.params;
import static util.Utils.*;

public class ClioneSQLTest {

    @Test
    public void パラメタなしの普通のSQLをパースする() {
        SQLGenerator gen = new SQLGenerator();
        String sql = gen.genSql(null, new SQLParser("").parse(load("00-normal.sql")));

        assertThat(join(sql), is("select * employee"));
        assertThat(gen.params, is(emptyList()));
    }

    @Test
    public void パラメタ付きのSQLをパースする() {
        SQLNode node = new SQLParser("").parse(load("01-parameter.sql"));
        SQLGenerator gen = new SQLGenerator();
        String sql = gen.genSql(null, node);

        assertThat(join(sql), is("SELECT * FROM BOOK  WHERE AUTHOR = ?  ORDER BY BOOK_ID ASC"));
        assertThat(gen.params, contains(nullValue()));

        sql = gen.genSql(params("author", "AUTHOR"), node);
        assertThat(join(sql), is("SELECT * FROM BOOK  WHERE AUTHOR = ?  ORDER BY BOOK_ID ASC"));
        assertThat(gen.params.get(0), is((Object) "AUTHOR"));
    }

    @Test
    public void 条件パラメタ_$_付きのSQLをパースする() {
        SQLNode node = new SQLParser("").parse(load("13-clione_parameter_$.sql"));
        SQLGenerator gen = new SQLGenerator();
        String sql;

        sql = gen.genSql(params("age", 10).$("pref", "Sendai"), node);
        assertThat(join(sql), is("SELECT * FROM PEOPLE  WHERE    AGE = ?    AND PREFECTURE = ?"));
        assertThat(gen.params, is(asList(10, "Sendai")));

        // 何も指定しないと条件句が消える
        sql = gen.genSql(null, node);
        assertThat(join(sql), is("SELECT * FROM PEOPLE"));
        assertThat(gen.params, is(emptyList()));

        // １つ目の条件を指定しない（ANDが消える）
        sql = gen.genSql(params("pref", "Sendai"), node);
        assertThat(join(sql), is("SELECT * FROM PEOPLE  WHERE    PREFECTURE = ?"));
        assertThat(gen.params, is(asList("Sendai")));
    }

    @Test
    public void 条件パラメタ_アンパサンド_付きのSQLをパースする() {
        SQLNode node = new SQLParser("").parse(load("13-clione_parameter_and.sql"));
        SQLGenerator gen = new SQLGenerator();
        String sql;

        sql = gen.genSql(params("order", true), node);
        assertThat(join(sql), is("SELECT * FROM PEOPLE  ORDER BY NAME"));
        assertThat(gen.params, is(emptyList()));

        // 何も指定しないと条件句が消える
        sql = gen.genSql(null, node);
        assertThat(join(sql), is("SELECT * FROM PEOPLE"));
        assertThat(gen.params, is(emptyList()));
    }

    @Test
    public void 条件パラメタ付き_特殊ケース_のSQLをパースする() {
        SQLNode node = new SQLParser("").parse(load("13-clione_parameter_sp.sql"));
        SQLGenerator gen = new SQLGenerator();
        String sql;

        sql = gen.genSql(params("age", 10), node);
        assertThat(join(sql), is("SELECT * FROM PEOPLE WHERE AGE  =  ?"));
        assertThat(gen.params, is(asList(10)));

        // nullだと IS NULL に展開
        sql = gen.genSql(null, node);
        assertThat(join(sql), is("SELECT * FROM PEOPLE WHERE AGE  IS NULL"));
        assertThat(gen.params, is(emptyList()));

        // Listを指定すると in に展開
        sql = gen.genSql(params("age", asList(10, 20, 30)), node);
        assertThat(join(sql), is("SELECT * FROM PEOPLE WHERE AGE  IN ( ?, ?, ?)"));
        assertThat(gen.params, is(asList(10, 20, 30)));
    }
}
import org.lwjgl.opengl.GL11;


public class Block {
	
	private float xPos;
	private float yPos;
	private float zPos;
	private float width;
	private float height;
	private float depth;
	private float dimention;
	
	/**
	 * used to create a rectangular prism of any size
	 * @param xPos
	 * @param yPos
	 * @param zPos
	 * @param width
	 * @param height
	 * @param depth
	 */
	public Block(float xPos, float yPos, float zPos, float width, float height, float depth){
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	/**
	 * Used for creating a perfect cube of any size
	 * where the width height and depth are the same
	 * @param xPos
	 * @param yPos
	 * @param zPos
	 * @param dimention
	 */
	public Block(float xPos, float yPos, float zPos, float dimention){
		this.xPos = xPos;
		this.yPos = yPos;
		this.zPos = zPos;
		this.dimention = dimention;
		this.width = dimention;
		this.height = dimention;
		this.depth = dimention;
	}
	
	/**
	 * renders the object with the values that it has
	 */
	public void renderObject(){
		
		ImageBank.getBoxTex().bind();
		
		GL11.glBegin(GL11.GL_QUADS);
		//front face
		GL11.glColor3f(1, 0, 0);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(xPos, yPos, zPos);
		GL11.glTexCoord2f(1.0f,0.0f);
		GL11.glVertex3f(xPos+width, yPos, zPos);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(xPos+width, yPos+height, zPos);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(xPos, yPos+height, zPos);
		
		//back face
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(xPos, yPos, zPos-depth);
		GL11.glTexCoord2f(1.0f,0.0f);
		GL11.glVertex3f(xPos+width, yPos, zPos-depth);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(xPos+width, yPos+height, zPos-depth);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(xPos, yPos+height, zPos-depth);
		
		//right face
		GL11.glColor3f(0, 1, 0);
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(xPos+width, yPos, zPos);
		GL11.glTexCoord2f(1.0f,0.0f);
		GL11.glVertex3f(xPos+width, yPos, zPos-depth);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(xPos+width, yPos+height, zPos-depth);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(xPos+width, yPos+height, zPos);
		
		//left face
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(xPos, yPos, zPos);
		GL11.glTexCoord2f(1.0f,0.0f);
		GL11.glVertex3f(xPos, yPos, zPos-depth);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(xPos, yPos+height, zPos-depth);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(xPos, yPos+height, zPos);
		
		//top face
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(xPos, yPos+height, zPos);
		GL11.glTexCoord2f(1.0f,0.0f);
		GL11.glVertex3f(xPos, yPos+height, zPos-depth);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(xPos+width, yPos+height, zPos-depth);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(xPos+width, yPos+height, zPos);
		
		//bottom face
		GL11.glTexCoord2f(0.0f, 0.0f);
		GL11.glVertex3f(xPos, yPos, zPos);
		GL11.glTexCoord2f(1.0f,0.0f);
		GL11.glVertex3f(xPos, yPos, zPos-depth);
		GL11.glTexCoord2f(1.0f, 1.0f);
		GL11.glVertex3f(xPos+width, yPos, zPos-depth);
		GL11.glTexCoord2f(0.0f, 1.0f);
		GL11.glVertex3f(xPos+width, yPos, zPos);
		GL11.glEnd();
	}

	/**
	 * @return the xPos
	 */
	public float getxPos() {
		return xPos;
	}

	/**
	 * @param xPos the xPos to set
	 */
	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	/**
	 * @return the yPos
	 */
	public float getyPos() {
		return yPos;
	}

	/**
	 * @param yPos the yPos to set
	 */
	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	/**
	 * @return the width
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(float width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(float height) {
		this.height = height;
	}

	/**
	 * @return the depth
	 */
	public float getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setDepth(float depth) {
		this.depth = depth;
	}

	/**
	 * @return the dimention
	 */
	public float getDimention() {
		return dimention;
	}

	/**
	 * @param dimention the dimention to set
	 */
	public void setDimention(float dimention) {
		this.dimention = dimention;
	}
}
package nz.gen.geek_central.Compass3D;
/*
    Useful camera-related stuff.

    Copyright 2011 by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.
*/

import android.hardware.Camera;

public class CameraUseful
  {

    public static int NV21DataSize
      (
        int Width,
        int Height
      )
      /* returns the size of a data buffer to hold an NV21-encoded image
        of the specified dimensions. */
      {
        return
                Width * Height
            +
                ((Width + 1) / 2) * ((Height + 1) / 2) * 2;
      } /*NV21DataSize*/

    public static void DecodeNV21
      (
        int SrcWidth, /* dimensions of image before rotation */
        int SrcHeight,
        byte[] Data, /* length = NV21DataSize(SrcWidth, SrcHeight) */
        int Rotate, /* [0 .. 3], angle is 90° * Rotate clockwise */
        int Alpha, /* set as alpha for all decoded pixels */
        int[] Pixels /* length = Width * Height */
      )
      /* decodes NV21-encoded image data, which is the default camera preview image format. */
      {
        final int AlphaMask = Alpha << 24;
      /* Rotation involves accessing either the source or destination pixels in a
        non-sequential fashion. Since the source is smaller, I figure it's less
        cache-unfriendly to go jumping around that. */
        final int DstWidth = (Rotate & 1) != 0 ? SrcHeight : SrcWidth;
        final int DstHeight = (Rotate & 1) != 0 ? SrcWidth : SrcHeight;
        final boolean DecrementRow = Rotate > 1;
        final boolean DecrementCol = Rotate == 1 || Rotate == 2;
        final int LumaRowStride = (Rotate & 1) != 0 ? 1 : SrcWidth;
        final int LumaColStride = (Rotate & 1) != 0 ? SrcWidth : 1;
        final int ChromaRowStride = (Rotate & 1) != 0 ? 2 : SrcWidth;
        final int ChromaColStride = (Rotate & 1) != 0 ? SrcWidth : 2;
        int dst = 0;
        for (int row = DecrementRow ? DstHeight : 0;;)
          {
            if (row == (DecrementRow ? 0 : DstHeight))
                break;
            if (DecrementRow)
              {
                --row;
              } /*if*/
            for (int col = DecrementCol ? DstWidth : 0;;)
              {
                if (col == (DecrementCol ? 0 : DstWidth))
                    break;
                if (DecrementCol)
                  {
                    --col;
                  } /*if*/
                final int Y = 0xff & (int)Data[row * LumaRowStride + col * LumaColStride]; /* [0 .. 255] */
              /* U/V data follows entire luminance block, downsampled to half luminance
                resolution both horizontally and vertically */
              /* decoding follows algorithm shown at
                <http://www.mail-archive.com/android-developers@googlegroups.com/msg14558.html>,
                except it gets red and blue the wrong way round (decoding NV12 rather than NV21) */
              /* see also good overview of YUV-family formats at <http://wiki.videolan.org/YUV> */
                final int Cr =
                    (0xff & (int)Data[SrcHeight * SrcWidth + row / 2 * ChromaRowStride + col / 2 * ChromaColStride]) - 128;
                      /* [-128 .. +127] */
                final int Cb =
                    (0xff & (int)Data[SrcHeight * SrcWidth + row / 2 * ChromaRowStride + col / 2 * ChromaColStride + 1]) - 128;
                      /* [-128 .. +127] */
                Pixels[dst++] =
                        AlphaMask
                    |
                            Math.max
                              (
                                Math.min
                                  (
                                    (int)(
                                            Y
                                        +
                                            Cr
                                        +
                                            (Cr >> 1)
                                        +
                                            (Cr >> 2)
                                        +
                                            (Cr >> 6)
                                    ),
                                    255
                                  ),
                                  0
                              )
                        <<
                            16 /* red */
                    |
                            Math.max
                              (
                                Math.min
                                  (
                                    (int)(
                                            Y
                                        -
                                            (Cr >> 2)
                                        +
                                            (Cr >> 4)
                                        +
                                            (Cr >> 5)
                                        -
                                            (Cb >> 1)
                                        +
                                            (Cb >> 3)
                                        +
                                            (Cb >> 4)
                                        +
                                            (Cb >> 5)
                                    ),
                                    255
                                  ),
                                0
                              )
                        <<
                            8 /* green */
                    |
                        Math.max
                          (
                            Math.min
                              (
                                (int)(
                                        Y
                                    +
                                        Cb
                                    +
                                        (Cb >> 2)
                                    +
                                        (Cb >> 3)
                                    +
                                        (Cb >> 5)
                                ),
                                255
                              ),
                            0
                          ); /* blue */
                if (!DecrementCol)
                  {
                    ++col;
                  } /*if*/
              } /*for*/
            if (!DecrementRow)
              {
                ++row;
              } /*if*/
          } /*for*/
      } /*DecodeNV21*/

    public static android.graphics.Point GetSmallestPreviewSizeAtLeast
      (
        Camera TheCamera,
        int MinWidth,
        int MinHeight
      )
      /* returns smallest supported preview size which is of at least
        the given dimensions. */
      {
        Camera.Size BestSize = null;
        for
          (
            Camera.Size ThisSize : TheCamera.getParameters().getSupportedPreviewSizes()
          )
          {
            if (ThisSize.width >= MinWidth && ThisSize.height >= MinHeight)
              {
                if
                  (
                        BestSize == null
                    ||
                        BestSize.width > ThisSize.width
                    ||
                        BestSize.height > ThisSize.height
                  )
                  {
                    BestSize = ThisSize;
                  } /*if*/
              } /*if*/
          } /*for*/
        if (BestSize == null)
          {
          /* none big enough, pick first, which seems to be biggest */
            BestSize = TheCamera.getParameters().getSupportedPreviewSizes().get(0);
          } /*if*/
        return
            new android.graphics.Point(BestSize.width, BestSize.height);
      } /*GetSmallestPreviewSizeAtLeast*/

    public static android.graphics.Point GetLargestPreviewSizeAtMost
      (
        Camera TheCamera,
        int MaxWidth,
        int MaxHeight
      )
      /* returns largest supported preview size which is of at most
        the given dimensions. */
      {
        Camera.Size BestSize = null;
        for
          (
            Camera.Size ThisSize : TheCamera.getParameters().getSupportedPreviewSizes()
          )
          {
            if (ThisSize.width <= MaxWidth && ThisSize.height <= MaxHeight)
              {
                if
                  (
                        BestSize == null
                    ||
                        BestSize.width < ThisSize.width
                    ||
                        BestSize.height < ThisSize.height
                  )
                  {
                    BestSize = ThisSize;
                  } /*if*/
              } /*if*/
          } /*for*/
        if (BestSize == null)
          {
          /* none big enough, pick last, which seems to be smallest */
            final java.util.List<Camera.Size> PreviewSizes =
                TheCamera.getParameters().getSupportedPreviewSizes();
            BestSize = PreviewSizes.get(PreviewSizes.size() - 1);
          } /*if*/
        return
            new android.graphics.Point(BestSize.width, BestSize.height);
      } /*GetLargestPreviewSizeAtMost*/

  } /*CameraUseful*/
package nz.gen.geek_central.Compass3D;
/*
    Useful EGL-related definitions.

    Copyright 2011 by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.
*/

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL11;

public class EGLUseful
  {
    public static final EGL10 EGL = GetEGL(); /* use this EGL10 instance for making EGL calls */

    public static class EGLException extends RuntimeException
      /* indicates a problem making an EGL call. */
      {

        public EGLException
          (
            String Message
          )
          {
            super(Message);
          } /*EGLException*/

      } /*EGLException*/

    public static EGL10 GetEGL()
      /* you probably don't need to call this. */
      {
        return
            (EGL10)EGLContext.getEGL();
      } /*GetEGL*/

    public static void Fail
      (
        String What
      )
      {
        throw new EGLException
          (
            String.format("EGL %s failed with error %d", What, EGL.eglGetError())
          );
      } /*Fail*/

    public static EGLDisplay NewDisplay()
      /* allocates and initializes a new display. */
      {
        final EGLDisplay Result = EGL.eglGetDisplay((EGLDisplay)EGL10.EGL_DEFAULT_DISPLAY);
          /* as far as I can tell, it makes no difference whether you pass
            EGL_DEFAULT_DISPLAY or EGL_NO_DISPLAY */
        if (Result == null)
          {
            throw new EGLException("failed to allocate a new display");
          } /*if*/
        if (!EGL.eglInitialize(Result, null))
          {
            Fail("initing new display");
          } /*if*/
        return
            Result;
      } /*NewDisplay*/

    public static EGLConfig[] GetConfigs
      (
        EGLDisplay ForDisplay
      )
      /* returns all available configs for the specified display. */
      {
        EGLConfig[] Configs = null;
        final int[] ConfigsNr = new int[1];
        for (;;)
          {
            final boolean Success = EGL.eglGetConfigs
              (
                /*display =*/ ForDisplay,
                /*configs =*/ Configs,
                /*config_size =*/ Configs != null ? Configs.length : 0,
                /*num_config =*/ ConfigsNr
              );
            if (!Success)
              {
                Fail("GetConfigs");
                break;
              } /*if*/
            if (Configs != null)
                break;
            Configs = new EGLConfig[ConfigsNr[0]];
          } /*for*/
        return
            Configs;
      } /*GetConfigs*/

    public static EGLConfig[] GetCompatConfigs
      (
        EGLDisplay ForDisplay,
        int[] MatchingAttribs
      )
      /* returns configs compatible with the specified attributes. */
      {
        EGLConfig[] Configs = null;
        final int[] ConfigsNr = new int[1];
        for (;;)
          {
            final boolean Success = EGL.eglChooseConfig
              (
                /*display =*/ ForDisplay,
                /*attrib =*/ MatchingAttribs,
                /*configs =*/ Configs,
                /*config_size =*/ Configs != null ? Configs.length : 0,
                /*num_config =*/ ConfigsNr
              );
            if (!Success)
              {
                Fail("GetCompatConfigs");
                break;
              } /*if*/
            if (Configs != null)
                break;
            Configs = new EGLConfig[ConfigsNr[0]];
          } /*for*/
        return
            Configs;
      } /*GetCompatConfigs*/

    public static int GetConfigAttrib
      (
        EGLDisplay ForDisplay,
        EGLConfig ForConfig,
        int Attrib
      )
      /* returns the value of the specified attribute of a config. */
      {
        final int[] AttrValue = new int[1];
        final boolean Success = EGL.eglGetConfigAttrib
          (
            /*dpy =*/ ForDisplay,
            /*config =*/ ForConfig,
            /*attribute =*/ Attrib,
            /*value =*/ AttrValue
          );
        if (!Success)
          {
            Fail(String.format("GetConfigAttrib %d", Attrib));
          } /*if*/
        return
            AttrValue[0];
      } /*GetConfigAttrib*/

    public static EGLSurface CreatePbufferSurface
      (
        EGLDisplay ForDisplay,
        EGLConfig WithConfig,
        int Width,
        int Height,
        boolean ExactSize /* false to allow allocating smaller Width/Height */
      )
      /* might return EGL_NO_SURFACE on failure, caller must check for error */
      {
        final int[] Attribs = new int[3 * 2 + 1];
          {
            int i = 0;
            Attribs[i++] = EGL10.EGL_WIDTH;
            Attribs[i++] = Width;
            Attribs[i++] = EGL10.EGL_HEIGHT;
            Attribs[i++] = Height;
            Attribs[i++] = EGL.EGL_LARGEST_PBUFFER;
            Attribs[i++] = ExactSize ? 0 : 1;
            Attribs[i++] = EGL10.EGL_NONE; /* marks end of list */
          }
        return
            EGL.eglCreatePbufferSurface
              (
                /*display =*/ ForDisplay,
                /*config =*/ WithConfig,
                /*attrib_list =*/ Attribs
              );
      } /*CreatePbufferSurface*/

    public static class SurfaceContext
      /* easy management of surface together with context */
      {
        public final EGLDisplay Display;
        public final EGLContext Context;
        public final EGLSurface Surface;

        public SurfaceContext
          (
            EGLDisplay Display,
            EGLContext Context,
            EGLSurface Surface
          )
          {
            this.Display = Display;
            this.Context = Context;
            this.Surface = Surface;
          } /*SurfaceContext*/

        public static SurfaceContext CreatePbuffer
          (
            EGLDisplay ForDisplay,
            EGLConfig[] TryConfigs, /* to be tried in turn */
            int Width,
            int Height,
            boolean ExactSize, /* false to allow allocating smaller Width/Height */
            EGLContext ShareContext /* pass null or EGL_NO_CONTEXT to not share existing context */
          )
          /* creates a Pbuffer surface and Context using one of the available
            configs. */
          {
            EGLSurface TheSurface = null;
            EGLContext UseContext = null;
            for (int i = 0;;)
              {
                if (i == TryConfigs.length)
                  {
                    Fail("creating Pbuffer surface");
                  } /*if*/
                TheSurface = CreatePbufferSurface
                  (
                    /*ForDisplay =*/ ForDisplay,
                    /*WithConfig =*/ TryConfigs[i],
                    /*Width =*/ Width,
                    /*Height =*/ Height,
                    /*ExactSize =*/ ExactSize
                  );
                if (TheSurface != EGL10.EGL_NO_SURFACE)
                  {
                    UseContext = EGL.eglCreateContext
                      (
                        /*display =*/ ForDisplay,
                        /*config =*/ TryConfigs[i],
                        /*share_context =*/ ShareContext == null ? EGL10.EGL_NO_CONTEXT : ShareContext,
                        /*attrib_list =*/ null
                      );
                    if (UseContext == EGL10.EGL_NO_CONTEXT)
                      {
                        Fail("creating context");
                      } /*if*/
                    break;
                  } /*if*/
                ++i;
              } /*for*/
            return
                new SurfaceContext(ForDisplay, UseContext, TheSurface);
          } /*CreatePbuffer*/

        public void SetCurrent()
          /* sets surface and context as current. */
          {
            if (!EGL.eglMakeCurrent(Display, Surface, Surface, Context))
              {
                Fail("setting current context");
              } /*if*/
          } /*SetCurrent*/

        public void ClearCurrent()
          /* clears current surface and context. */
          {
            EGL.eglMakeCurrent
              (
                Display,
                EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_SURFACE,
                EGL10.EGL_NO_CONTEXT
              );
          } /*ClearCurrent*/

        public void Release()
          /* disposes of Surface and Context, but not Display. */
          {
            ClearCurrent(); /* if not already done */
            if (!EGL.eglDestroySurface(Display, Surface))
              {
                Fail("destroying surface");
              } /*if*/;
            if (!EGL.eglDestroyContext(Display, Context))
              {
                Fail("destroying context");
              } /*if*/;
          } /*Release*/

        public GL11 GetGL()
          /* returns an object you can use to make khronos.openglex method
            calls. Preferred method seems to be to use static methods in
            android.opengl.GLESxx instead. */
          {
            return
                (GL11)Context.getGL();
          } /*GetGL*/

      } /*SurfaceContext*/

  } /*EGLUseful*/
package nz.gen.geek_central.GLUseful;
/*
    Easy construction and application of buffers needed for
    OpenGL-ES drawing.

    Copyright 2011 by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.
*/

import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.ByteOrder;
import android.opengl.GLES11;

public class GeomBuilder
  /*
    Helper class for easier construction of geometrical
    objects. Instantiate this and tell it whether each vertex will
    also have a normal vector, a texture-coordinate vector or a
    colour. Then call Add to add vertex definitions (using class
    GeomBuilder.Vec3f to define points, and GeomBuilder.Color to
    define colours), and use the returned vertex indices to construct
    faces with AddTri and AddQuad. Finally, call MakeObj to obtain a
    GeomBuilder.Obj that has a Draw method that will render the
    resulting geometry into a specified GL context.
  */
  {

    public static class Vec3f
      /* 3D vectors/points */
      {
        public final float x, y, z;

        public Vec3f
          (
            float x,
            float y,
            float z
          )
          {
            this.x = x;
            this.y = y;
            this.z = z;
          } /*Vec3f*/

      } /*Vec3f*/

    public static class Color
      /* RGB colours with transparency */
      {
        public final float r, g, b, a;

        public Color
          (
            float r,
            float g,
            float b,
            float a
          )
          {
            this.r = r;
            this.b = b;
            this.g = g;
            this.a = a;
          } /*Color*/

      } /*Color*/

    private final ArrayList<Vec3f> Points;
    private final ArrayList<Vec3f> PointNormals;
    private final ArrayList<Vec3f> PointTexCoords;
    private final ArrayList<Color> PointColors;
    private final ArrayList<Integer> Faces;

    public GeomBuilder
      (
        boolean GotNormals, /* vertices will have normals specified */
        boolean GotTexCoords, /* vertices will have texture coordinates specified */
        boolean GotColors /* vertices will have colours specified */
      )
      {
        Points = new ArrayList<Vec3f>();
        PointNormals = GotNormals ? new ArrayList<Vec3f>() : null;
        PointTexCoords = GotTexCoords ? new ArrayList<Vec3f>() : null;
        PointColors = GotColors ? new ArrayList<Color>() : null;
        Faces = new ArrayList<Integer>();
      } /*GeomBuilder*/

    public int Add
      (
        Vec3f Vertex,
      /* following args are either mandatory or must be null, depending
        on respective flags passed to constructor */
        Vec3f Normal,
        Vec3f TexCoord,
        Color VertexColor
      )
      /* adds a new vertex, and returns its index for use in constructing faces. */
      {
        if
          (
                (PointNormals == null) != (Normal == null)
            ||
                (PointColors == null) != (VertexColor == null)
            ||
                (PointTexCoords == null) != (TexCoord == null)
          )
          {
            throw new RuntimeException("missing or redundant args specified");
          } /*if*/
        final int Result = Points.size();
        Points.add(Vertex);
        if (PointNormals != null)
          {
            PointNormals.add(Normal);
          } /*if*/
        if (PointTexCoords != null)
          {
            PointTexCoords.add(TexCoord);
          } /*if*/
        if (PointColors != null)
          {
            PointColors.add(VertexColor);
          } /*if*/
        return
            Result;
      } /*Add*/

    public void AddTri
      (
        int V1,
        int V2,
        int V3
      )
      /* defines a triangular face. Args are indices as previously returned from calls to Add. */
      {
        Faces.add(V1);
        Faces.add(V2);
        Faces.add(V3);
      } /*AddTri*/

    public void AddQuad
      (
        int V1,
        int V2,
        int V3,
        int V4
      )
      /* Defines a quadrilateral face. Args are indices as previously returned from calls to Add. */
      {
        AddTri(V1, V2, V3);
        AddTri(V4, V1, V3);
      } /*AddQuad*/

    public static class Obj
      /* representation of complete object geometry. */
      {
        private final IntBuffer VertexBuffer;
        private final IntBuffer NormalBuffer;
        private final IntBuffer TexCoordBuffer;
        private final IntBuffer ColorBuffer;
        private final ShortBuffer IndexBuffer;
        final int NrIndexes;

        private Obj
          (
            IntBuffer VertexBuffer,
            IntBuffer NormalBuffer, /* optional */
            IntBuffer TexCoordBuffer, /* optional */
            IntBuffer ColorBuffer, /* optional */
            ShortBuffer IndexBuffer,
            int NrIndexes
          )
          {
            this.VertexBuffer = VertexBuffer;
            this.NormalBuffer = NormalBuffer;
            this.TexCoordBuffer = TexCoordBuffer;
            this.ColorBuffer = ColorBuffer;
            this.IndexBuffer = IndexBuffer;
            this.NrIndexes = NrIndexes;
          } /*Obj*/

        public void Draw()
          /* actually renders the geometry into the specified GL context. */
          {
            GLES11.glEnableClientState(GLES11.GL_VERTEX_ARRAY);
            GLES11.glVertexPointer(3, GLES11.GL_FIXED, 0, VertexBuffer);
            if (NormalBuffer != null)
              {
                GLES11.glEnableClientState(GLES11.GL_NORMAL_ARRAY);
                GLES11.glNormalPointer(GLES11.GL_FIXED, 0, NormalBuffer);
              } /*if*/
            if (TexCoordBuffer != null)
              {
                GLES11.glEnableClientState(GLES11.GL_TEXTURE_COORD_ARRAY);
                GLES11.glTexCoordPointer(3, GLES11.GL_FIXED, 0, TexCoordBuffer);
              } /*if*/
            if (ColorBuffer != null)
              {
                GLES11.glEnableClientState(GLES11.GL_COLOR_ARRAY);
                GLES11.glColorPointer(4, GLES11.GL_FIXED, 0, ColorBuffer);
              } /*if*/
            GLES11.glDrawElements(GLES11.GL_TRIANGLES, NrIndexes, GLES11.GL_UNSIGNED_SHORT, IndexBuffer);
            GLES11.glDisableClientState(GLES11.GL_VERTEX_ARRAY);
            GLES11.glDisableClientState(GLES11.GL_NORMAL_ARRAY);
            GLES11.glDisableClientState(GLES11.GL_TEXTURE_COORD_ARRAY);
            GLES11.glDisableClientState(GLES11.GL_COLOR_ARRAY);
          } /*Draw*/

      } /*Obj*/

    public Obj MakeObj()
      /* constructs and returns the final geometry ready for rendering. */
      {
        final int Fixed1 = 0x10000;
        final int[] Vertices = new int[Points.size() * 3];
        final int[] Normals = PointNormals != null ? new int[Points.size() * 3] : null;
        final int[] TexCoords = PointTexCoords != null ? new int[Points.size() * 3] : null;
        final int[] Colors = PointColors != null ? new int[Points.size() * 4] : null;
        int jv = 0, jn = 0, jt = 0, jc = 0;
        for (int i = 0; i < Points.size(); ++i)
          {
            final Vec3f Point = Points.get(i);
            Vertices[jv++] = (int)(Point.x * Fixed1);
            Vertices[jv++] = (int)(Point.y * Fixed1);
            Vertices[jv++] = (int)(Point.z * Fixed1);
            if (PointNormals != null)
              {
                final Vec3f PointNormal = PointNormals.get(i);
                Normals[jn++] = (int)(PointNormal.x * Fixed1);
                Normals[jn++] = (int)(PointNormal.y * Fixed1);
                Normals[jn++] = (int)(PointNormal.z * Fixed1);
              } /*if*/
            if (PointTexCoords != null)
              {
                final Vec3f Coord = PointTexCoords.get(i);
                TexCoords[jt++] = (int)(Coord.x * Fixed1);
                TexCoords[jt++] = (int)(Coord.y * Fixed1);
                TexCoords[jt++] = (int)(Coord.z * Fixed1);
              } /*if*/
            if (PointColors != null)
              {
                final Color ThisColor = PointColors.get(i);
                Colors[jc++] = (int)(ThisColor.r * Fixed1);
                Colors[jc++] = (int)(ThisColor.g * Fixed1);
                Colors[jc++] = (int)(ThisColor.b * Fixed1);
                Colors[jc++] = (int)(ThisColor.a * Fixed1);
              } /*if*/
          } /*for*/
        final short[] Indices = new short[Faces.size()];
        final int NrIndexes = Indices.length;
        for (int i = 0; i < NrIndexes; ++i)
          {
            Indices[i] = (short)(int)Faces.get(i);
          } /*for*/
      /* Need to use allocateDirect to allocate buffers so garbage
        collector won't move them. Also make sure byte order is
        always native. But direct-allocation and order-setting methods
        are only available for ByteBuffer. Which is why buffers
        are allocated as ByteBuffers and then converted to more
        appropriate types. */
        final IntBuffer VertexBuffer;
        final IntBuffer NormalBuffer;
        final IntBuffer TexCoordBuffer;
        final IntBuffer ColorBuffer;
        final ShortBuffer IndexBuffer;
        VertexBuffer =
            ByteBuffer.allocateDirect(Vertices.length * 4)
            .order(ByteOrder.nativeOrder())
            .asIntBuffer()
            .put(Vertices);
        VertexBuffer.position(0);
        if (PointNormals != null)
          {
            NormalBuffer =
                ByteBuffer.allocateDirect(Normals.length * 4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(Normals);
            NormalBuffer.position(0);
          }
        else
          {
            NormalBuffer = null;
          } /*if*/
        if (PointTexCoords != null)
          {
            TexCoordBuffer =
                ByteBuffer.allocateDirect(TexCoords.length * 4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(TexCoords);
            TexCoordBuffer.position(0);
          }
        else
          {
            TexCoordBuffer = null;
          } /*if*/
        if (PointColors != null)
          {
            ColorBuffer =
                ByteBuffer.allocateDirect(Colors.length * 4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer()
                .put(Colors);
            ColorBuffer.position(0);
          }
        else
          {
            ColorBuffer = null;
          } /*if*/
        IndexBuffer =
            ByteBuffer.allocateDirect(Indices.length * 2)
            .order(ByteOrder.nativeOrder())
            .asShortBuffer()
            .put(Indices);
        IndexBuffer.position(0);
        return
            new Obj
              (
                VertexBuffer,
                NormalBuffer,
                TexCoordBuffer,
                ColorBuffer,
                IndexBuffer,
                NrIndexes
              );
      } /*MakeObj*/

  } /*GeomBuilder*/
package nz.gen.geek_central.GLUseful;
/*
    Easy construction of objects with a single axis of rotational symmetry,
    building on GeomBuilder.

    Copyright 2011 by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.
*/

public class Lathe
  {
    public interface VertexFunc
      {
        public GeomBuilder.Vec3f Get
          (
            int PointIndex
          );
      }

    public interface VectorFunc
      {
        public GeomBuilder.Vec3f Get
          (
            int PointIndex,
            int SectorIndex, /* 0 .. NrSectors - 1 */
            boolean Upper
              /* indicates which of two calls for each point (except for
                start and end points, which only get one call each) to allow
                for discontiguous shading */
          );
      } /*VectorFunc*/

    public interface ColorFunc
      {
        public GeomBuilder.Color Get
          (
            int PointIndex,
            int SectorIndex, /* 0 .. NrSectors - 1 */
            boolean Upper
              /* indicates which of two calls for each point (except for
                start and end points, which only get one call each) to allow
                for discontiguous shading */
          );
      } /*ColorFunc*/

    public static GeomBuilder.Obj Make
      (
        VertexFunc Point,
          /* returns outline of object, must be at least 3 points, all Z coords must be
            zero, all X coords non-negative, and X coord of first and last point
            must be zero. Order the points by decreasing Y coord if you want
            anticlockwise face vertex ordering, or increasing if you want clockwise. */
        int NrPoints, /* valid values for arg to Point.Get are [0 .. NrPoints - 1] */
        VectorFunc Normal, /* optional to compute normal vector at each point */
        VectorFunc TexCoord, /* optional to compute texture coordinate at each point */
        ColorFunc VertexColor, /* optional to compute colour at each point */
        int NrSectors /* must be at least 3 */
      )
      /* rotates Points around Y axis with NrSectors steps, invoking the
        specified callbacks to obtain normal vectors, texture coordinates
        and vertex colours as appropriate, and returns the constructed
        geometry object. */
      {
        final GeomBuilder Geom = new GeomBuilder
          (
            /*GotNormals =*/ Normal != null,
            /*GotTexCoords =*/ TexCoord != null,
            /*GotColors =*/ VertexColor != null
          );
        final int[] PrevInds = new int[NrPoints * 2 - 2];
        final int[] FirstInds = new int[NrPoints * 2 - 2];
        final int[] TheseInds = new int[NrPoints * 2 - 2];
        for (int i = 0;;)
          {
            if (i < NrSectors)
              {
                final float Angle = (float)(2.0 * Math.PI * i / NrSectors);
                final float Cos = android.util.FloatMath.cos(Angle);
                final float Sin = android.util.FloatMath.sin(Angle);
                for (int j = 0; j < NrPoints; ++j)
                  {
                    final GeomBuilder.Vec3f Vertex = Point.Get(j);
                    final GeomBuilder.Vec3f ThisPoint = new GeomBuilder.Vec3f
                      (
                        Vertex.x * Cos,
                        Vertex.y,
                        Vertex.x * Sin
                      );
                    if (j < NrPoints - 1)
                      {
                        final GeomBuilder.Vec3f ThisNormal =
                            Normal != null ?
                                Normal.Get(j, i, true)
                            :
                                null;
                        final GeomBuilder.Vec3f ThisTexCoord =
                            TexCoord != null ?
                                TexCoord.Get(j, i, true)
                            :
                                null;
                        final GeomBuilder.Color ThisColor =
                            VertexColor != null ?
                                VertexColor.Get(j, i, true)
                            :
                                null;
                        TheseInds[j * 2] =
                            Geom.Add(ThisPoint, ThisNormal, ThisTexCoord, ThisColor);
                      } /*if*/
                    if (j > 0)
                      {
                        final GeomBuilder.Vec3f ThisNormal =
                            Normal != null ?
                                Normal.Get(j, i, false)
                            :
                                null;
                        final GeomBuilder.Vec3f ThisTexCoord =
                            TexCoord != null ?
                                TexCoord.Get(j, i, false)
                            :
                                null;
                        final GeomBuilder.Color ThisColor =
                            VertexColor != null ?
                                VertexColor.Get(j, i, false)
                            :
                                null;
                        TheseInds[j * 2 - 1] =
                            Geom.Add(ThisPoint, ThisNormal, ThisTexCoord, ThisColor);
                      } /*if*/
                  } /*for*/
              }
            else
              {
                for (int j = 0; j < TheseInds.length; ++j)
                  {
                    TheseInds[j] = FirstInds[j];
                  } /*for*/
              } /*if*/
            if (i != 0)
              {
                Geom.AddTri(PrevInds[1], TheseInds[0], TheseInds[1]);
                for (int j = 1; j < NrPoints - 1; ++j)
                  {
                    Geom.AddQuad
                      (
                        PrevInds[j * 2 + 1],
                        PrevInds[j * 2],
                        TheseInds[j * 2],
                        TheseInds[j * 2 + 1]
                      );
                  } /*for*/
                Geom.AddTri
                  (
                    PrevInds[TheseInds.length - 2],
                    TheseInds[TheseInds.length - 2],
                    TheseInds[TheseInds.length - 1]
                  );
              }
            else
              {
                for (int j = 0; j < TheseInds.length; ++j)
                  {
                    FirstInds[j] = TheseInds[j];
                  } /*for*/
              } /*if*/
            for (int j = 0; j < TheseInds.length; ++j)
              {
                PrevInds[j] = TheseInds[j];
              } /*for*/
            if (i == NrSectors)
                break;
            ++i;
          } /*for*/
        return 
            Geom.MakeObj();
      } /*Make*/

  } /*Lathe*/
package nz.gen.geek_central.Compass3D;
/*
    Display a 3D compass arrow using OpenGL, composited on a live camera view.

    Copyright 2011, 2012 by Lawrence D'Oliveiro <ldo@geek-central.gen.nz>.

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.
*/

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLDisplay;
import android.opengl.GLES11;
import android.graphics.Matrix;
import java.nio.ByteBuffer;

public class Main extends android.app.Activity
  {
    android.widget.TextView Message1View, Message2View;
    android.view.SurfaceView Graphical;
    android.hardware.SensorManager SensorMan;
    android.hardware.Sensor CompassSensor = null;
    CommonListener Listen;
    final long[] FrameTimes = new long[25];
    boolean Active = false, SurfaceExists = false;
    int NextFrameTime = 0;

    private class CommonListener
        implements
            android.hardware.SensorEventListener,
            android.hardware.Camera.PreviewCallback,
            android.view.SurfaceHolder.Callback
      {
        private final EGLDisplay Display;
        private EGLUseful.SurfaceContext GLContext;
        ByteBuffer GLPixels;
        android.graphics.Bitmap GLBits;
        private android.hardware.Camera TheCamera;
        private CameraSetupExtra TheCameraExtra = null;
        private final Compass Needle;
        private android.graphics.Point PreviewSize, RotatedPreviewSize;
        private int[] ImageBuf;
        private int Rotation;
        private Matrix ImageTransform;

        public CommonListener()
          {
            Display = EGLUseful.NewDisplay();
            Needle = new Compass();
          } /*CommonListener*/

        public void Start()
          {
            AllocateGL();
            Rotation = (5 - Main.this.getWindowManager().getDefaultDisplay().getOrientation()) % 4;
            ImageTransform = new Matrix();
            ImageTransform.preScale
              (
                1, -1,
                0, GLBits.getHeight() / 2.0f
              );
              /* Y-axis goes up for OpenGL, down for 2D Canvas */
            ImageTransform.postRotate
              (
                (Rotation - 1) * 90.0f,
                GLBits.getWidth() / 2.0f,
                GLBits.getHeight() / 2.0f
              );
            StartCompass();
            StartCamera();
          } /*Start*/

        public void Stop()
          {
            StopCamera();
            StopCompass();
            ReleaseGL();
          } /*Stop*/

        public void Finish()
          {
            Stop();
            EGLUseful.EGL.eglTerminate(Display);
          } /*Finish*/

        private void StartCompass()
          {
            if (CompassSensor != null)
              {
                SensorMan.registerListener
                  (
                    this,
                    CompassSensor,
                    android.hardware.SensorManager.SENSOR_DELAY_UI
                  );
              } /*if*/
          } /*StartCompass*/

        private void StopCompass()
          {
            if (CompassSensor != null)
              {
                SensorMan.unregisterListener(this, CompassSensor);
              } /*if*/
          } /*StopCompass*/

        private class CameraSetupExtra
          /* does extra setup specific to Android 3.0 and later. Instantiation
            will fail with NoClassDefFoundError on earlier versions. */
          {
            private final android.graphics.SurfaceTexture DummyTexture;
            private final android.hardware.Camera TheCamera;

            public CameraSetupExtra
              (
                android.hardware.Camera TheCamera
              )
              {
                this.TheCamera = TheCamera;
                int[] ID = new int[1];
                android.opengl.GLES11.glGenTextures(1, ID, 0);
                DummyTexture = new android.graphics.SurfaceTexture(ID[0]);
                TheCamera.setPreviewTexture(DummyTexture);
              } /*CameraSetupExtra*/

            public void Release()
              {
                TheCamera.setPreviewTexture(null);
              /* DummyTexture.release(); */ /* API 14 or later only */
              } /*Release*/

          } /*CameraSetupExtra*/

        private void DumpList
          (
            String Description,
            java.util.List<?> TheList,
            java.io.PrintStream Out
          )
          {
            Out.print(Description);
            if (TheList != null)
              {
                Out.print(" ");
                boolean first = true;
                for (Object val : TheList)
                  {
                    if (!first)
                      {
                        Out.print(", ");
                      } /*if*/
                    Out.print(val);
                    first = false;
                  } /*for*/
              }
            else
              {
                Out.print("(none)");
              } /*if*/
            Out.println();
          } /*DumpList*/

        private void StartCamera()
          {
            TheCamera = android.hardware.Camera.open();
            if (TheCamera != null)
              {
                System.err.printf
                  (
                    "My activity orientation is %d\n",
                    Main.this.getWindowManager().getDefaultDisplay().getOrientation()
                  ); /* debug */
                PreviewSize = CameraUseful.GetLargestPreviewSizeAtMost(TheCamera, Graphical.getWidth(), Graphical.getHeight());
                System.err.printf("Setting preview size to %d*%d (at most %d*%d)\n", PreviewSize.x, PreviewSize.y, Graphical.getWidth(), Graphical.getHeight()); /* debug */
                final android.hardware.Camera.Parameters Parms = TheCamera.getParameters();
                  { /* debug */
                    System.err.println("Main.StartCamera initial params:");
                    System.err.printf
                      (
                        " Scene mode: %s, preview frame rate %d\n",
                        Parms.getSceneMode(),
                        Parms.getPreviewFrameRate()
                      );
                    System.err.printf(" White balance: %s\n", Parms.getWhiteBalance());
                    DumpList
                      (
                        " Supported anti-banding:",
                        Parms.getSupportedAntibanding(),
                        System.err
                      );
                    DumpList
                      (
                        " Supported colour effects:",
                        Parms.getSupportedColorEffects(),
                        System.err
                      );
                    DumpList
                      (
                        " Supported flash modes:",
                        Parms.getSupportedFlashModes(),
                        System.err
                      );
                    DumpList
                      (
                        " Supported picture formats:",
                        Parms.getSupportedPictureFormats(),
                        System.err
                      );
                    DumpList
                      (
                        " Supported preview formats:",
                        Parms.getSupportedPreviewFormats(),
                        System.err
                      );
                    DumpList
                      (
                        " Supported preview frame rates:",
                        Parms.getSupportedPreviewFrameRates(),
                        System.err
                      );
                    DumpList
                      (
                        " Supported scene modes:",
                        Parms.getSupportedSceneModes(),
                        System.err
                      );
                    DumpList
                      (
                        " Supported white balances:",
                        Parms.getSupportedWhiteBalance(),
                        System.err
                      );
                  }
                Parms.setPreviewSize(PreviewSize.x, PreviewSize.y);
                TheCamera.setParameters(Parms);
                RotatedPreviewSize = new android.graphics.Point
                  (
                    (Rotation & 1) != 0 ? PreviewSize.y : PreviewSize.x,
                    (Rotation & 1) != 0 ? PreviewSize.x : PreviewSize.y
                  );
                ImageBuf = new int[PreviewSize.x * PreviewSize.y];
                TheCamera.setPreviewCallback(this);
              /* Note I don't call TheCamera.setPreviewDisplay, even though the docs
                say this is necessary. I don't want to do that, because I don't want
                any preview to appear on-screen. I got away with that on an HTC Desire
                (Android 2.2), but it apears the Samsung Galaxy Nexus (Android 4.0) is
                not so forgiving. Luckily Honeycomb and later offer setPreviewTexture
                as an alternative. So I set a dummy one of these. However, this seems
                to make for a horrible frame rate. I'll have to see how to remedy
                that later (fixme!). */
                try
                  {
                    TheCameraExtra = new CameraSetupExtra(TheCamera);
                  }
                catch (NoClassDefFoundError TooOld)
                  {
                  } /*try*/
                TheCamera.startPreview();
              }
            else
              {
                Message2View.setText("Failed to open camera");
              } /*if*/
          } /*StartCamera*/

        private void StopCamera()
          {
            if (TheCamera != null)
              {
                TheCamera.stopPreview();
                if (TheCameraExtra != null)
                  {
                    TheCameraExtra.Release();
                    TheCameraExtra = null;
                  } /*if*/
                TheCamera.setPreviewCallback(null);
                TheCamera.release();
                TheCamera = null;
                ImageBuf = null;
              } /*if*/
          } /*StopCamera*/

        private void AllocateGL()
          {
            final int Width = Graphical.getWidth();
            final int Height = Graphical.getHeight();
            GLContext = EGLUseful.SurfaceContext.CreatePbuffer
              (
                /*ForDisplay =*/ Display,
                /*TryConfigs =*/
                    EGLUseful.GetCompatConfigs
                      (
                        /*ForDisplay =*/ Display,
                        /*MatchingAttribs =*/
                            new int[]
                                {
                                    EGL10.EGL_RED_SIZE, 8,
                                    EGL10.EGL_GREEN_SIZE, 8,
                                    EGL10.EGL_BLUE_SIZE, 8,
                                    EGL10.EGL_ALPHA_SIZE, 8,
                                    EGL10.EGL_DEPTH_SIZE, 16,
                                    EGL10.EGL_SURFACE_TYPE, EGL10.EGL_PBUFFER_BIT,
                                    EGL10.EGL_CONFIG_CAVEAT, EGL10.EGL_NONE,
                                    EGL10.EGL_NONE /* marks end of list */
                                }
                      ),
                /*Width =*/ Width,
                /*Height =*/ Height,
                /*ExactSize =*/ true,
                /*ShareContext =*/ null
              );
            GLContext.SetCurrent();
            Needle.Setup(Width, Height);
            GLContext.ClearCurrent();
            GLPixels = ByteBuffer.allocateDirect
              (
                Width * Height * 4
              ).order(java.nio.ByteOrder.nativeOrder());
            GLBits = android.graphics.Bitmap.createBitmap
              (
                /*width =*/ Width,
                /*height =*/ Height,
                /*config =*/ android.graphics.Bitmap.Config.ARGB_8888
              );
          } /*AllocateGL*/

        private void ReleaseGL()
          {
            if (GLContext != null)
              {
                GLContext.Release();
                GLContext = null;
              } /*if*/
            if (GLBits != null)
              {
                GLBits.recycle();
                GLBits = null;
              } /*if*/
          } /*ReleaseGL*/

        private float Azi, Elev, Roll;

        private void Draw()
          /* (re)draws the complete composited display. */
          {
            final android.graphics.Canvas Display = Graphical.getHolder().lockCanvas();
            if (Display != null)
              {
                Display.drawColor(0, android.graphics.PorterDuff.Mode.SRC);
                  /* initialize all pixels to fully transparent */
                if (ImageBuf != null)
                  {
                    Display.drawBitmap
                      (
                        /*colors =*/ ImageBuf,
                        /*offset =*/ 0,
                        /*stride =*/ RotatedPreviewSize.x,
                        /*x =*/ (Graphical.getWidth() - RotatedPreviewSize.x) / 2,
                        /*y =*/ (Graphical.getHeight() - RotatedPreviewSize.y) / 2,
                        /*width =*/ RotatedPreviewSize.x,
                        /*height =*/ RotatedPreviewSize.y,
                        /*hasAlpha =*/ true,
                        /*paint =*/ null
                      );
                  } /*if*/
                if (GLContext != null)
                  {
                    GLContext.SetCurrent();
                    Needle.Draw(Azi, Elev, Roll);
                      { /* debug */
                        final int EGLError = EGLUseful.EGL.eglGetError();
                        if (EGLError != EGL10.EGL_SUCCESS)
                          {
                            System.err.printf
                              (
                                "Compass3D.Main EGL error 0x%04x\n", EGLError
                              );
                          } /*if*/
                      }
                    GLES11.glFinish();
                    GLES11.glReadPixels
                      (
                        /*x =*/ 0,
                        /*y =*/ 0,
                        /*width =*/ GLBits.getWidth(),
                        /*height =*/ GLBits.getHeight(),
                        /*format =*/ GLES11.GL_RGBA,
                        /*type =*/ GLES11.GL_UNSIGNED_BYTE,
                        /*pixels =*/ GLPixels
                      );
                    GLContext.ClearCurrent();
                    GLBits.copyPixelsFromBuffer(GLPixels);
                    Display.drawBitmap(GLBits, ImageTransform, null);
                  } /*if*/
                Graphical.getHolder().unlockCanvasAndPost(Display);
              }
            else
              {
                System.err.println("Graphical surface not ready");
              } /*if*/
          } /*Draw*/

      /* SensorEventListener methods: */

        public void onAccuracyChanged
          (
            android.hardware.Sensor TheSensor,
            int NewAccuracy
          )
          {
          /* ignore for now */
          } /*onAccuracyChanged*/

        private long LastSensorUpdate = 0;

        public void onSensorChanged
          (
            android.hardware.SensorEvent Event
          )
          {
            final java.io.ByteArrayOutputStream MessageBuf = new java.io.ByteArrayOutputStream();
            final java.io.PrintStream Msg = new java.io.PrintStream(MessageBuf);
            Msg.printf
              (
                "Sensor event at %.6f accuracy %d\nValues(%d): (",
                Event.timestamp / Math.pow(10.0d, 9),
                Event.accuracy,
                Event.values.length
              );
            for (int i = 0; i < Event.values.length; ++i)
              {
                if (i != 0)
                  {
                    Msg.print(", ");
                  } /*if*/
                Msg.printf("%.0f°", Event.values[i]);
              } /*for*/
            Msg.print(")\n");
            Msg.flush();
            Message1View.setText(MessageBuf.toString());
            Azi = Event.values[0];
            Elev = Event.values[1];
            Roll = Event.values[2];
            final long Now = System.currentTimeMillis();
            if (Now - LastSensorUpdate >= 250)
              /* throttle sensor updates because they seem to cause contention
                with camera preview updates */
              {
                LastSensorUpdate = Now;
                Draw();
              } /*if*/
          } /*onSensorChanged*/

      /* Camera.PreviewCallback methods: */

        public void onPreviewFrame
          (
            byte[] Data,
            android.hardware.Camera TheCamera
          )
          {
            FrameTimes[NextFrameTime] = System.currentTimeMillis();
            NextFrameTime = (NextFrameTime + 1) % FrameTimes.length;
            final float FrameRate =
                    FrameTimes.length * 1000f
                /
                    (
                        FrameTimes[(NextFrameTime + FrameTimes.length - 1) % FrameTimes.length]
                    -
                        FrameTimes[NextFrameTime]
                    );
            if (NextFrameTime == 0)
              {
                Message2View.setText(String.format("Camera fps %.2f", FrameRate));
              } /*if*/
            CameraUseful.DecodeNV21
              (
                /*SrcWidth =*/ PreviewSize.x,
                /*SrcHeight =*/ PreviewSize.y,
                /*Data =*/ Data,
                /*Rotate =*/ Rotation,
                /*Alpha =*/ 255,
                /*Pixels =*/ ImageBuf
              );
            Draw();
          } /*onPreviewFrame*/

      /* SurfaceHolder.Callback methods: */

        public void surfaceChanged
          (
            android.view.SurfaceHolder TheHolder,
            int Format,
            int Width,
            int Height
          )
          {
            System.err.println("Compass3D.Main surfaceChanged"); /* debug */
            Stop();
            SurfaceExists = true;
            if (Active)
              {
                Start();
              } /*if*/
          } /*surfaceChanged*/

        public void surfaceCreated
          (
            android.view.SurfaceHolder TheHolder
          )
          {
          /* do everything in surfaceChanged */
            System.err.println("Compass3D.Main surfaceCreated"); /* debug */
          } /*surfaceCreated*/

        public void surfaceDestroyed
          (
            android.view.SurfaceHolder TheHolder
          )
          {
            SurfaceExists = false;
            Stop();
            System.err.println("Compass3D.Main surfaceDestroyed"); /* debug */
          } /*surfaceDestroyed*/

      } /*CommonListener*/

    @Override
    public void onCreate
      (
        android.os.Bundle savedInstanceState
      )
      {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Message1View = (android.widget.TextView)findViewById(R.id.message1);
        Message2View = (android.widget.TextView)findViewById(R.id.message2);
        Graphical = (android.view.SurfaceView)findViewById(R.id.display);
        SensorMan = (android.hardware.SensorManager)getSystemService(SENSOR_SERVICE);
        CompassSensor = SensorMan.getDefaultSensor(android.hardware.Sensor.TYPE_ORIENTATION);
        if (CompassSensor == null)
          {
            Message1View.setText("No compass hardware present");
          } /*if*/
        Listen = new CommonListener();
        Graphical.getHolder().addCallback(Listen);
      } /*onCreate*/

    @Override
    public void onDestroy()
      {
        Listen.Finish();
        super.onDestroy();
      } /*onDestroy*/

    @Override
    public void onPause()
      {
        Listen.Stop();
        Active = false;
        super.onPause();
      } /*onPause*/

    @Override
    public void onResume()
      {
        super.onResume();
        Active = true;
        if (SurfaceExists)
          {
            Listen.Start();
          } /*if*/
      } /*onResume*/

  } /*Main*/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dnehsics;

import java.applet.Applet;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;

import java.util.List;

import java.util.concurrent.CopyOnWriteArrayList;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransparencyAttributes;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;

import com.sun.j3d.utils.geometry.ColorCube;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

/**
 *
 * @author henry
 */
public class Main extends Applet {
	public static final long serialVersionUID = 293841832419834234L;
    private static BoundingSphere myBounds = new BoundingSphere();
    private List<Body> bodies = new CopyOnWriteArrayList<Body>();
    public Main() {
        setLayout(new BorderLayout());
        GraphicsConfiguration config =
           SimpleUniverse.getPreferredConfiguration();

        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);

        BranchGroup scene = createSceneGraph();

        // SimpleUniverse is a Convenience Utility class
        SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

        // This will move the ViewPlatform back a bit so the
        // objects in the scene can be viewed.
        simpleU.getViewingPlatform().setNominalViewingTransform();
        OrbitBehavior orbit =
            new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0,0,0),100));
        ViewingPlatform vp = simpleU.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
        simpleU.addBranchGraph(scene);
    } // end of HelloJava3Da (constructor)
    public BranchGroup createSceneGraph() {
        // Create the root of the branch graph
        BranchGroup objRoot = new BranchGroup();

       /* new Body(new Vector3f(0,0.01f,.3f), new Vector3f(.0f,0,-.0003f),.02f,(float)Body.ELECTRON_CHARGE*1E10f,objRoot,bodies);
        new Body(new Vector3f(0,-.01f,-.3f), new Vector3f(.0f,.00f,.0003f),.02f,(float)Body.ELECTRON_CHARGE*1E10f,objRoot,bodies);*/
        
       /* new Atom(new Vector3f(0,0.01f,.3f), new Vector3f(.0f,0,-.0009f),.02f,2,1,(float)Body.ELECTRON_MASS,new Color3f(0,0,0),objRoot,bodies);
        new Atom(new Vector3f(0,-.01f,-.3f), new Vector3f(.0f,.00f,.0009f),.02f,2,1,(float)Body.ELECTRON_MASS,new Color3f(0,0,0),objRoot,bodies);*/
        int count = 0;
        for(double i = -.3; i<=.3; i+=.1)
            if(count%2 == 0)
            {
                new Atom(new Vector3f(0,0.03f,(float)i), new Vector3f(.0f,0,0),.02f,18,17,(float)Body.ELECTRON_MASS,new Color3f(0,0,0),objRoot,bodies);
                count++;
            }
            else
            {
                new Atom(new Vector3f(0,0.03f,(float)i), new Vector3f(.0f,0,0),.02f,10,11,(float)Body.ELECTRON_MASS,new Color3f(255,0,0),objRoot,bodies);
                count++;
            }
        for(double i = -.3; i<=.3; i+=.1)
            if(count%2 == 1)
            {
                new Atom(new Vector3f(0,-0.03f,(float)i), new Vector3f(.0f,0,0),.02f,18,17,(float)Body.ELECTRON_MASS,new Color3f(0,0,0),objRoot,bodies);
                count++;
            }
            else
            {
                new Atom(new Vector3f(0,-0.03f,(float)i), new Vector3f(.0f,0,0),.02f,10,11,(float)Body.ELECTRON_MASS,new Color3f(255,0,0),objRoot,bodies);
                count++;
            }
        for(double i = -.3; i<=.3; i+=.1)
            if(count%2 == 0)
            {
                new Atom(new Vector3f(.03f,0.03f,(float)i), new Vector3f(.0f,0,0),.02f,18,17,(float)Body.ELECTRON_MASS,new Color3f(0,0,0),objRoot,bodies);
                count++;
            }
            else
            {
                new Atom(new Vector3f(.03f,0.03f,(float)i), new Vector3f(.0f,0,0),.02f,10,11,(float)Body.ELECTRON_MASS,new Color3f(255,0,0),objRoot,bodies);
                count++;
            }
        for(double i = -.3; i<=.3; i+=.1)
            if(count%2 == 1)
            {
                new Atom(new Vector3f(.03f,-0.03f,(float)i), new Vector3f(.0f,0,0),.02f,18,17,(float)Body.ELECTRON_MASS,new Color3f(0,0,0),objRoot,bodies);
                count++;
            }
            else
            {
                new Atom(new Vector3f(.03f,-0.03f,(float)i), new Vector3f(.0f,0,0),.02f,10,11,(float)Body.ELECTRON_MASS,new Color3f(255,0,0),objRoot,bodies);
                count++;
            }

        Appearance a = new Appearance();
        a.setTransparencyAttributes(new TransparencyAttributes(TransparencyAttributes.NICEST,.70f));
        PolygonAttributes pa = new PolygonAttributes();
        pa.setCullFace(PolygonAttributes.CULL_NONE);
        a.setPolygonAttributes(pa);
        Shape3D s = new ColorCube(.5f);
       s.setAppearance(a);
        objRoot.addChild(s);
        objRoot.addChild(lightScene());
        objRoot.addChild(createAxis());

        return objRoot;
    } // end of CreateSceneGraph method of HelloJava3Da

    /**
     * @param args the command line arguments
     */
            private BranchGroup lightScene()
  // One ambient light, 2 directional lights
  {
        BranchGroup sceneBG = new BranchGroup();
    Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    // Set up the ambient light
    AmbientLight ambientLightNode = new AmbientLight(white);
    ambientLightNode.setInfluencingBounds(myBounds);
    sceneBG.addChild(ambientLightNode);

    // Set up the directional lights
    Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
       // left, down, backwards
    Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
       // right, down, forwards

    DirectionalLight light1 =
            new DirectionalLight(white, light1Direction);
    light1.setInfluencingBounds(myBounds);
    sceneBG.addChild(light1);

    DirectionalLight light2 =
        new DirectionalLight(white, light2Direction);
    light2.setInfluencingBounds(myBounds);
    sceneBG.addChild(light2);
    return sceneBG;
  }  // end of lightScene()
       public BranchGroup createAxis()
        {
                //the branchgroup to store the lines
                BranchGroup b = new BranchGroup();

                //the positive x line
                Point3d[] points = new Point3d[]{new Point3d(1,0,0)};//a point at (1,0,0)
                GeometryArray g = new LineArray(points.length*2,GeometryArray.COORDINATES);//make a linearray
                g.setCoordinates(0, points);//set the verticies of the array the the point
                Appearance a = new Appearance();//set the appearance to red and thikness (size 5) and solid
                a.setColoringAttributes(new ColoringAttributes(255,0,0,ColoringAttributes.NICEST));
                a.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_SOLID, false));
                Shape3D s = new Shape3D(g, a);//create a shape with a certain shape and appearance
                b.addChild(s);//add it to b

                //the positive y line
                points = new Point3d[]{new Point3d(0,1,0)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a2 = new Appearance();
                a2.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_SOLID, false));
                a2.setColoringAttributes(new ColoringAttributes(0,255,0,ColoringAttributes.NICEST));
                s = new Shape3D(g, a2);
                b.addChild(s);

                //the positive z line
                points = new Point3d[]{new Point3d(0,0,1)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a3 = new Appearance();
                a3.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_SOLID, false));
                a3.setColoringAttributes(new ColoringAttributes(0,0,255,ColoringAttributes.NICEST));
                s = new Shape3D(g, a3);
                b.addChild(s);

                //the negative x line
                points = new Point3d[]{new Point3d(-1,0,0)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a4 = new Appearance();
                a4.setColoringAttributes(new ColoringAttributes(255,0,0,ColoringAttributes.NICEST));
                a4.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_DASH, false));
                s = new Shape3D(g, a4);
                b.addChild(s);

                //the negative y line
                points = new Point3d[]{new Point3d(0,-1,0)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a5 = new Appearance();
                a5.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_DASH, false));
                a5.setColoringAttributes(new ColoringAttributes(0,255,0,ColoringAttributes.NICEST));
                s = new Shape3D(g, a5);
                b.addChild(s);

                //the negative z line
                points = new Point3d[]{new Point3d(0,0,-1)};
                g = new LineArray(points.length*2,GeometryArray.COORDINATES);
                g.setCoordinates(0, points);
                Appearance a6 = new Appearance();
                a6.setLineAttributes(new LineAttributes(5, LineAttributes.PATTERN_DASH, false));
                a6.setColoringAttributes(new ColoringAttributes(0,0,255,ColoringAttributes.NICEST));
                s = new Shape3D(g, a6);
                b.addChild(s);
                return b;
        }

    public static void main(String[] args) {
         new MainFrame(new Main(), 800, 800);
    }

}
package com.graphbuilder.math;

import com.graphbuilder.struc.Stack;

/**
<p>Expression string parser.  The parser returns an Expression object which can be evaluated.

<p>The elements of an expression fall into two categories, operators and terms.  Terms include
variables, functions and values.  Although values do not subclass TermNode, they are processed
in a similar way during the parsing.

<p>The operators are exponent (^), times (*), divide (/), plus (+) and minus (-).  The exponent has
the highest precedence, then times and divide (which have an equal level of precedence) and then
plus and minus (which have an equal level of precedence).  For those operators that have an equal
level of precedence, the operator that comes first (reading from left to right) gets the higher
level of precedence.  For example, a-b+c becomes ((a-b)+c)

<p>During the parsing, the string is scanned from left to right.  Either a term or an operator will
come next.  If it is a term, then it may be preceded by an optional + or - sign.  For example,
For example +a/-b/+c is valid, and becomes ((a/(-b))/c).  If a term is not explicitly signed, then
by default it is positive.

<p>If the first character of a term is a decimal (.) or a digit (0-9) then the term marks the
beginning of a value.  The first character that is found that is <b>not</b> a digit or a decimal marks the
end of the value, except if the character is an 'e' or an 'E'.  In this case, the first digit immediately
following can be a plus sign, minus sign or digit.  E.g. 1.23E+4.  At this point, the first character that
is not a digit marks the end of the value.

<p>If the first character of a term is <b>not</b> a decimal, digit, open bracket '(', a close bracket ')', a comma ',',
a whitespace character ' ', '\t', '\n', or an operator, then the character marks the beginning of a variable or
function name.  The first character found that is an operator, whitespace, open bracket, close bracket or comma
marks the end of the name.  If the first non-whitespace character after a name is an open bracket, then it marks
the beginning of the function parameters, otherwise the term is marked as a variable.

<p>Functions that accept more than one parameter will have the parameters separated by commas.  E.g.
f(x,y).  Since the parameters of a function are also expressions, when a comma is detected that has
exactly one non-balanced open bracket to the left, a recursive call is made passing in the substring.
E.g. f(g(x,y),z), a recursive call will occur passing in the substring "g(x,y)".

<p>Miscellaneous Notes

<ul>
<li>Names cannot begin with a decimal or a digit since those characters mark the beginning of a value.</li>
<li>In general, whitespace is ignored or marks the end of a name or value.</li>
<li>Evaluation of values is done using the Double.parseDouble(...) method.</li>
<li>Constants can be represented as functions that take no parameters.  E.g. pi() or e()</li>
<li>Round brackets '(' and ')' are the only brackets that have special meaning.</li>
<li>The brackets in the expression must balance otherwise a ExpressionParseException is thrown.</li>
<li>An ExpressionParseException is thrown in all cases where the expression string is invalid.</li>
<li>All terms must be separated by an operator.  E.g. 2x is <b>not</b> valid, but 2*x is.</li>
<li>In cases where simplification is possible, simplification is <b>not</b> done.  E.g. 2^4 is <b>not</b>
simplified to 16.</li>
<li>Computerized scientific notation is supported for values.  E.g. 3.125e-4</li>
<li>Scoped negations are not permitted. E.g. -(a) is <b>not</b> valid, but -1*(a) is.</li>
</ul>

@see com.graphbuilder.math.Expression
*/
public class ExpressionTree {

	private ExpressionTree() {}

	/**
	Returns an expression-tree that represents the expression string.  Returns null if the string is empty.

	@throws ExpressionParseException If the string is invalid.
	*/
	public static Expression parse(String s) throws ExpressionParseException {
		if (s == null)
			throw new ExpressionParseException("Expression string cannot be null.", -1);

		return build(s, 0);
	}

	private static Expression build(String s, int indexErrorOffset) throws ExpressionParseException {

		// do not remove (required condition for functions with no parameters, e.g. Pi())
		if (s.trim().length() == 0)
			return null;

		Stack s1 = new Stack(); // contains expression nodes
		Stack s2 = new Stack(); // contains open brackets ( and operators ^,*,/,+,-

		boolean term = true; // indicates a term should come next, not an operator
		boolean signed = false; // indicates if the current term has been signed
		boolean negate = false; // indicates if the sign of the current term is negated

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == ' ' || c == '\t' || c == '\n')
				continue;

			if (term) {
				if (c == '(') {
					if (negate)
						throw new ExpressionParseException("Open bracket found after negate.", i);

					s2.push("(");
				}
				else if (!signed && (c == '+' || c == '-')) {
					signed = true;
					if (c == '-') negate = true; // by default negate is false
				}
				else if (c >= '0' && c <= '9' || c == '.') {

					int j = i + 1;
					while (j < s.length()) {
						c = s.charAt(j);
						if (c >= '0' && c <= '9' || c == '.') j++;

						// code to account for "computerized scientific notation"
						else if (c == 'e' || c == 'E') {
							j++;

							if (j < s.length()) {
								c = s.charAt(j);

								if (c != '+' && c != '-' && (c < '0' || c > '9'))
									throw new ExpressionParseException("Expected digit, plus sign or minus sign but found: " + String.valueOf(c), j + indexErrorOffset);

								j++;
							}

							while (j < s.length()) {
								c = s.charAt(j);
								if (c < '0' || c > '9')
									break;
								j++;
							}
							break;
						}
						else break;
					}

					double d = 0;
					String _d = s.substring(i, j);

					try {
						d = Double.parseDouble(_d);
					} catch (Throwable t) {
						throw new ExpressionParseException("Improperly formatted value: " + _d, i + indexErrorOffset);
					}

					if (negate) d = -d;
					s1.push(new ValNode(d));
					i = j - 1;

					negate = false;
					term = false;
					signed = false;
				}
				else if (c != ',' && c != ')' && c != '^' && c != '*' && c != '/' && c != '+' && c != '-') {
					int j = i + 1;
					while (j < s.length()) {
						c = s.charAt(j);
						if (c != ',' && c != ' ' && c != '\t' && c != '\n' && c != '(' && c != ')' && c != '^' && c != '*' && c != '/' && c != '+' && c != '-')
							j++;
						else break;
					}

					if (j < s.length()) {
						int k = j;
						while (c == ' ' || c == '\t' || c == '\n') {
							k++;
							if (k == s.length()) break;
							c = s.charAt(k);
						}

						if (c == '(') {
							FuncNode fn = new FuncNode(s.substring(i, j), negate);
							int b = 1;
							int kOld = k + 1;
							while (b != 0) {
								k++;

								if (k >= s.length()) {
									throw new ExpressionParseException("Missing function close bracket.", i + indexErrorOffset);
								}

								c = s.charAt(k);

								if (c == ')') {
									b--;
								}
								else if (c == '(') {
									b++;
								}
								else if (c == ',' && b == 1) {
									Expression o = build(s.substring(kOld, k), kOld);
									if (o == null) {
										throw new ExpressionParseException("Incomplete function.", kOld + indexErrorOffset);
									}
									fn.add(o);
									kOld = k + 1;
								}
							}
							Expression o = build(s.substring(kOld, k), kOld);
							if (o == null) {
								if (fn.numChildren() > 0) {
									throw new ExpressionParseException("Incomplete function.", kOld + indexErrorOffset);
								}
							}
							else {
								fn.add(o);
							}
							s1.push(fn);
							i = k;
						}
						else {
							s1.push(new VarNode(s.substring(i, j), negate));
							i = k - 1;
						}
					}
					else {
						s1.push(new VarNode(s.substring(i, j), negate));
						i = j - 1;
					}

					negate = false;
					term = false;
					signed = false;
				}
				else {
					throw new ExpressionParseException("Unexpected character: " + String.valueOf(c), i + indexErrorOffset);
				}
			}
			else {
				if (c == ')') {
					Stack s3 = new Stack();
					Stack s4 = new Stack();
					while (true) {
						if (s2.isEmpty()) {
							throw new ExpressionParseException("Missing open bracket.", i + indexErrorOffset);
						}
						Object o = s2.pop();
						if (o.equals("(")) break;
						s3.addToTail(s1.pop());
						s4.addToTail(o);
					}
					s3.addToTail(s1.pop());

					s1.push(build(s3, s4));
				}
				else if (c == '^' || c == '*' || c == '/' || c == '+' || c == '-') {
					term = true;
					s2.push(String.valueOf(c));
				}
				else {
					throw new ExpressionParseException("Expected operator or close bracket but found: " + String.valueOf(c), i + indexErrorOffset);
				}
			}
		}

		if (s1.size() != s2.size() + 1) {
			throw new ExpressionParseException("Incomplete expression.", indexErrorOffset + s.length());
		}

		return build(s1, s2);
	}

	private static Expression build(Stack s1, Stack s2) throws ExpressionParseException {
		Stack s3 = new Stack();
		Stack s4 = new Stack();

		while (!s2.isEmpty()) {
			Object o = s2.removeTail();
			Object o1 = s1.removeTail();
			Object o2 = s1.removeTail();

			if (o.equals("^")) {
				s1.addToTail(new PowNode((Expression) o1, (Expression) o2));
			}
			else {
				s1.addToTail(o2);
				s4.push(o);
				s3.push(o1);
			}
		}

		s3.push(s1.pop());

		while (!s4.isEmpty()) {
			Object o = s4.removeTail();
			Object o1 = s3.removeTail();
			Object o2 = s3.removeTail();

			if (o.equals("*")) {
				s3.addToTail(new MultNode((Expression) o1, (Expression) o2));
			}
			else if (o.equals("/")) {
				s3.addToTail(new DivNode((Expression) o1, (Expression) o2));
			}
			else {
				s3.addToTail(o2);
				s2.push(o);
				s1.push(o1);
			}
		}

		s1.push(s3.pop());

		while (!s2.isEmpty()) {
			Object o = s2.removeTail();
			Object o1 = s1.removeTail();
			Object o2 = s1.removeTail();

			if (o.equals("+")) {
				s1.addToTail(new AddNode((Expression) o1, (Expression) o2));
			}
			else if (o.equals("-")) {
				s1.addToTail(new SubNode((Expression) o1, (Expression) o2));
			}
			else {
				// should never happen
				throw new ExpressionParseException("Unknown operator: " + o, -1);
			}
		}

		return (Expression) s1.pop();
	}
}
package com.object.disoriented;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class ThreePSActivity extends Activity {
	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	/** Called when the activity is first created. */
	private Button btnBuy;
	private Button btnReceipt;
	private String user = "1";
	private String sess_id;
	private String TAG = "3PS Buyer Screen";
	private ArrayList<String> qrContents;
	public static ItemList itemList = new ItemList();
	@Override

	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.buyer_start);
		
		
		qrContents = new ArrayList<String>();
		btnBuy = (Button) findViewById(R.id.btnBuy);
		btnBuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.v(TAG,"Buy button pressed");
				Toast.makeText(ThreePSActivity.this, "Buy Button clicked!", Toast.LENGTH_SHORT).show();

				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				startActivityForResult(intent, 0);
			}
		});

		btnReceipt = (Button) findViewById(R.id.btnReceipt);
		btnReceipt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				
				Log.v(TAG,"Receipt button pressed");
				Toast.makeText(ThreePSActivity.this, "Receipt Button clicked!", Toast.LENGTH_SHORT).show();		
				Intent intent = new Intent(ThreePSActivity.this,ReceiptActivity.class);
				startActivity(intent);
			}
		});
        
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
            	Log.v(TAG, "gets here");
                String contents = intent.getStringExtra("SCAN_RESULT");
                String[] QRvals = contents.split(";");
                String sess_id = "";
                StringTokenizer st = new StringTokenizer(contents,";");
                
                Log.v(TAG, "gets here2");
                
                while(st.hasMoreTokens()){
                	qrContents.add(st.nextToken());
                }
                
                try{
                	String url = "http://128.61.105.48/session.php";
                	String charset = "UTF-8";
                	String param1 = contents;
                	String param2 = user;
                	String query = "QRinput=" + param1 + "&user_id=" + param2;
                	URLConnection con = new URL(url +"?" + query).openConnection();
                	con.setRequestProperty("Accept-Charset", charset);

                	
                	InputStream sessionStream = con.getInputStream();
                	int d = 0;
                	while (d != -1){
                		d = sessionStream.read();
                		if(d != -1){
                			sess_id += (char)d;
                		}
                	}
                	Log.v(TAG, sess_id);
                	Log.v(TAG, url + "?" + query);
                	

                } catch (SQLException e) {
                	e.printStackTrace();
                } catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
                Log.v(TAG,contents);
                // Handle successful scan
                
                Intent i = new Intent(this,PurchaseActivity.class);
                i.putStringArrayListExtra("qr_contents", qrContents);
                i.putExtra("sess_id", sess_id);
                startActivity(i);
                
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }
    public String genSessId(){
    	String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    	Random rnd = new Random();

   	   	StringBuilder sb = new StringBuilder( 40 );
   	   	for( int i = 0; i < 40; i++)
   	   		sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
   	   	return sb.toString();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.buyer_start_menu, menu);
        return true;
    }	
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_HOME);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	startActivity(intent);
    	return true;
    }

}package net.threescale.api;

import net.threescale.api.cache.ApiCache;
import net.threescale.api.cache.ConfiguredCacheImpl;
import net.threescale.api.cache.DefaultCacheImpl;
import net.threescale.api.v2.Api2;
import net.threescale.api.v2.Api2Impl;
import net.threescale.api.v2.HttpSender;
import net.threescale.api.v2.HttpSenderImpl;

/**
 * Factory class to create 3scale Api objects.
 */
public class ApiFactory {
    /** Default URL of 3Scale Provider {@value}*/
    public static String DEFAULT_3SCALE_PROVIDER_API_URL = "http://su1.3scale.net";

    /**
     * Creates a new Version 2 Api object.
     *
     * @param url                  URL of the server to connect to. e.g. http://su1.3scale.net.
     * @param provider_private_key The Providers private key obtained from 3scale.
     * @return A new Api object.
     */
    public static net.threescale.api.v2.Api2 createV2Api(String url, String provider_private_key) {
        return new Api2Impl(url, provider_private_key);
    }

    /**
     * Creates a new Version 2 Api object using <code>DEFAULT_3SCALE_PROVIDER_API_URL</code>
     *
     * @param provider_private_key The Providers private key obtained from 3scale.
     * @return A new Api object.
     */
    public static Api2 createV2Api(String provider_private_key) {
        return createV2Api(DEFAULT_3SCALE_PROVIDER_API_URL, provider_private_key);
    }

    /**
     * Creates a new Server Api object, with out any caching.
     *
     * @param url                  URL of the server to connect to. e.g. http://server.3scale.net.
     * @param provider_private_key The Providers private key obtained from 3scale.
     * @param sender               The HttpSender object to be used for communication with the server.
     * @return A new Server Api object.
     */
    public static net.threescale.api.v2.Api2 createV2Api(String url, String provider_private_key,
                                                         net.threescale.api.v2.HttpSender sender) {
        return new Api2Impl(url, provider_private_key, sender);
    }

    /**
     * Creates a new Cached Server Api object with a user specified cache and http sender.
     *
     * @param url                  URL of the server to connect to. e.g. http://server.3scale.net.
     * @param provider_private_key The Providers private key obtained from 3scale.
     * @param sender               The HttpSender object to be used for communication with the server.
     * @param cache                The cache to use.
     * @return A new Server Api object.
     */
    public static Api2 createV2ApiWithCache(String url, String provider_private_key,
                                            net.threescale.api.v2.HttpSender sender, ApiCache cache) {
        return new Api2Impl(url, provider_private_key, sender, cache);
    }

    /**
     * Creates a new Cached Server Api object using the default local cache parameters in etc/default.xml and
     * a user specified http sender (mainly for testing).
     *
     * @param url                  URL of the server to connect to. e.g. http://server.3scale.net.
     * @param provider_private_key The Providers private key obtained from 3scale.
     * @param sender               The HttpSender object to be used for communication with the server.
     * @return A new Server Api object.
     */
    public static Api2 createV2ApiWithLocalCache(String url, String provider_private_key,
                                                 net.threescale.api.v2.HttpSender sender) {
        return new Api2Impl(url, provider_private_key, sender, new DefaultCacheImpl(url, provider_private_key, sender));
    }

    /**
     * Creates a new Cached Server Api object using the default local cache parameters in etc/default.xml
     * and default http sender.
     * @param url                  URL of the server to connect to. e.g. http://server.3scale.net.
     * @param provider_private_key The Providers private key obtained from 3scale.
     * @return A new Server Api object.
     */
    public static Api2 createV2ApiWithLocalCache(String url, String provider_private_key) {
        HttpSender sender = new HttpSenderImpl();
        return createV2ApiWithLocalCache(url, provider_private_key, sender);
    }

    /**
     * Creates a Cached Server Api object with cache paramter file specificed by the user.

     * @param url                  URL of the server to connect to. e.g. http://server.3scale.net.
     * @param provider_private_key The Providers private key obtained from 3scale.
     * @param path_to_config       Path to the cache config xml file.
     * @return A new Server Api object.
     */
    public static Api2 createV2ApiWithRemoteCache(String url, String provider_private_key,
                                                  String path_to_config) {
        return new Api2Impl(url, provider_private_key, new ConfiguredCacheImpl(path_to_config, url, provider_private_key, new net.threescale.api.v2.HttpSenderImpl()));
    }

}
package com.supinfo.geekquote;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.supinfo.geekquote.adapter.QuoteAdapter;
import com.supinfo.geekquote.dao.DatabaseHelper;
import com.supinfo.geekquote.model.Quote;

public class QuoteListActivity extends Activity {

	private static final int QUOTE_ACTIVITY_REQUEST_CODE = 1;

	private Button button;

	private EditText editText;

	private ListView listView;

	private ArrayList<Quote> quotes;

	private QuoteAdapter quoteAdapter;

	private DatabaseHelper databaseHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		databaseHelper = new DatabaseHelper(this);

		if (savedInstanceState != null) {
			quotes = (ArrayList<Quote>) savedInstanceState
					.getSerializable("quotes");
		} else {

			quotes = new ArrayList<Quote>();

			SQLiteDatabase db = databaseHelper.getReadableDatabase();

			String[] columns = { "id", "strquote", "rating", "creation_date" };
			Cursor cursor = db.query("quotes", columns, null, null, null, null,
					null);
			
			cursor.moveToFirst();

			while (!cursor.isAfterLast()) {

				Quote quote = new Quote(cursor.getLong(0), cursor.getString(1),
						cursor.getInt(2), null);
				
				Date date = new Date(cursor.getLong(3));
				quote.setDate(date);

				quotes.add(quote);
				
				cursor.moveToNext();
			}

			db.close();
		}

		button = (Button) findViewById(R.id.button);
		editText = (EditText) findViewById(R.id.edit_text);
		listView = (ListView) findViewById(R.id.list);

		initButton();
		initList();
	}

	private void initButton() {
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String strQuote = editText.getText().toString();
				editText.setText("");

				if (strQuote == null || strQuote.trim().isEmpty()) {
					return;
				}

				addQuote(strQuote);
			}
		});
	}

	private void initList() {
		quoteAdapter = new QuoteAdapter(this, quotes);
		listView.setAdapter(quoteAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View row,
					int position, long index) {

				Quote quote = quoteAdapter.getItem(position);

				Intent intent = new Intent(QuoteListActivity.this,
						QuoteActivity.class);
				intent.putExtra("quote", quote);
				intent.putExtra("quote_index", index);

				startActivityForResult(intent, QUOTE_ACTIVITY_REQUEST_CODE);
			}
		});

	}

	public void addQuote(String strQuote) {

		Quote quote = new Quote(strQuote, 0);

		ContentValues contentValues = new ContentValues();
		contentValues.put("strquote", quote.getStrQuote());
		contentValues.put("creation_date", quote.getDate().getTime());
		contentValues.put("rating", quote.getRating());

		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		long id = db.insert("quotes", null, contentValues);
		db.close();

		if (id == 0) {
			Toast.makeText(this, "Erreur lors de l'ajour de la quote", 5000)
					.show();
			return;
		}

		quote.setId(id);
		quotes.add(quote);
		quoteAdapter.notifyDataSetChanged();

		Toast.makeText(this, "Quote ajoutée", 5000).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == QUOTE_ACTIVITY_REQUEST_CODE) {

			if (resultCode == RESULT_OK) {

				Quote quote = (Quote) data.getExtras().getSerializable("quote");
				long index = data.getExtras().getLong("quote_index");

				quotes.set((int) index, quote);
				quoteAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("quotes", quotes);
		super.onSaveInstanceState(outState);
	}
}
package com.threetaps.client;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.threetaps.util.Constants;
import com.windriver.gson.extension.GeneralObjectDeserializer;

public abstract class Client {

	protected static Logger logger = Logger.getLogger(Client.class.getName());

	private static String DEFAULT_URL = Constants.DEFAULT_API_URL;
	private static int DEFAULT_PORT = Constants.DEFAULT_API_PORT;

	protected String baseURL;
	protected int port;

	private HttpClient httpClient = new DefaultHttpClient();

	protected Gson gson = new GsonBuilder().setDateFormat(Constants.DATE_FORMAT)
		.excludeFieldsWithModifiers(Modifier.TRANSIENT)
		.registerTypeAdapter(Map.class, new GeneralObjectDeserializer()).create();

	protected Client() {
		this(DEFAULT_URL, DEFAULT_PORT);
	}

	protected Client(String url, int port) {

		this.baseURL = url;
		this.port = port;
	}

	protected HttpResponse execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		logger.info(request.getURI().toString());
		return httpClient.execute(request);
	}

	protected HttpResponse executeGet(String endpoint) throws ClientProtocolException, IOException {
		return executeGet(endpoint, null);
	}

	protected HttpResponse executeGet(String endpoint, Map<String, String> params) throws ClientProtocolException,
			IOException {

		final List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair(ThreetapsClient.AUTH_ID_KEY, ThreetapsClient.getInstance().getAuthID()));
		
		if (params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
			}
		}

		final HttpGet request = new HttpGet(baseURL + endpoint + "?" + URLEncodedUtils.format(qparams, "UTF-8"));
		return execute(request);
	}

	protected HttpResponse executePost(String endpoint, Map<String, String> params) throws ClientProtocolException,
			IOException {

		final List<NameValuePair> qparams = new ArrayList<NameValuePair>();
		qparams.add(new BasicNameValuePair(ThreetapsClient.AUTH_ID_KEY, ThreetapsClient.getInstance().getAuthID()));
		
		if (params != null) {
			for (Map.Entry<String, String> param : params.entrySet()) {
				qparams.add(new BasicNameValuePair(param.getKey(), param.getValue()));
				logger.info(param.getKey() + "=" + param.getValue());
			}
		}

		final HttpPost request = new HttpPost(baseURL + endpoint);
		request.setEntity(new UrlEncodedFormEntity(qparams, HTTP.UTF_8));
		
		return execute(request);
	}
}
package net.rcode.npedit;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

/**
 * A nine patch image loaded into memory
 * @author stella
 *
 */
public class NinePatchImage {
	public static final int MODE_SCALEX=0;
	public static final int MODE_SCALEY=1;
	public static final int MODE_PADDINGX=2;
	public static final int MODE_PADDINGY=3;
	
	public static final String NINE_PATCH_SUFFIX=".9.png";
	public static final String PNG_SUFFIX=".png";
	
	public File source;
	public boolean isNinePatch;
	public File plainFile;
	public File ninePatchFile;
	public BufferedImage image;
	
	public NinePatchImage() {
	}
	
	public void ensureNinePatch() {
		if (isNinePatch) return;
		
		// Otherwise, we need to add a border
		BufferedImage newImage=new BufferedImage(image.getWidth()+2, image.getHeight()+2, image.getType());
		newImage.getRaster().setRect(1, 1, image.getRaster());
		image=newImage;
		isNinePatch=true;
	}
	
	public void ensurePlain() {
		if (!isNinePatch) return;
		
		// Otherwise, we need to remove 2 rows and 2 columns
		BufferedImage newImage=new BufferedImage(image.getWidth()-2, image.getHeight()-2, image.getType());
		Raster src=image.getRaster().createChild(1, 1, newImage.getWidth(), newImage.getHeight(), 
				0, 0, null);
		newImage.getRaster().setRect(src);
		image=newImage;
		isNinePatch=false;
	}
	
	public static NinePatchImage load(File source) throws IOException {
		NinePatchImage ret=new NinePatchImage();
		ret.source=source;
		
		ImageReader reader=ImageUtil.getPngReader();	
		ImageInputStream in=ImageIO.createImageInputStream(source);
		reader.setInput(in);
		ret.image=reader.read(0);
		reader.dispose();
		in.close();

		// Detect if nine patch or not
		String name=source.getName();
		if (name.toLowerCase().endsWith(NINE_PATCH_SUFFIX)) {
			ret.isNinePatch=true;
			ret.ninePatchFile=source;
			name=name.substring(0, name.length()-NINE_PATCH_SUFFIX.length()) + PNG_SUFFIX;
			ret.plainFile=new File(source.getParentFile(), name);
		} else {
			ret.isNinePatch=false;
			ret.plainFile=source;
			name=name.substring(0, name.length()-PNG_SUFFIX.length()) + NINE_PATCH_SUFFIX;
			ret.ninePatchFile=new File(source.getParentFile(), name);
		}
		
		return ret;
	}

	public File getOutputFile() {
		File outputFile;
		if (isNinePatch) outputFile=ninePatchFile;
		else outputFile=plainFile;
		return outputFile;
	}
	
	public void save(File outputFile) throws IOException {
		ImageOutputStream out=ImageIO.createImageOutputStream(outputFile);
		try {
			ImageWriter writer=ImageUtil.getPngWriter();
			writer.setOutput(out);
			writer.write(image);
			writer.dispose();
		} finally {
			out.close();
		}
	}

	public static boolean validatePixel(int pixel) {
		int alpha=pixel >>> 24;
		return alpha==0 || alpha==0xff;
	}

	public static boolean isSetPixel(int pixel) {
		int alpha=pixel >>> 24;
		if (alpha==0) return false;
		else if (alpha==0xff) return true;
		else return false;
	}
	
	public int[] getMarkers(int mode) {
		if (!isNinePatch) return new int[0];
		
		int w=image.getWidth();
		int h=image.getHeight();
		int[] markers;
		int count=0;
		if (mode==MODE_SCALEX || mode==MODE_PADDINGX) {
			int y;
			if (mode==MODE_SCALEX) y=0;
			else y=h-1;
			markers=new int[w];
			
			for (int x=0; x<w; x++) {
				if (isSetPixel(image.getRGB(x, y))) {
					markers[count++]=x;
				}
			}
		} else if (mode==MODE_SCALEY || mode==MODE_PADDINGY) {
			int x;
			if (mode==MODE_SCALEY) x=0;
			else x=w-1;
			markers=new int[h];
			
			for (int y=0; y<h; y++) {
				if (isSetPixel(image.getRGB(x, y))) {
					markers[count++]=y;
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
		
		int[] ret=new int[count];
		System.arraycopy(markers, 0, ret, 0, count);
		
		return ret;
	}
	
	/**
	 * Convert image to 9-patch and expand it per the scale bars
	 * @param targetWidth
	 * @param targetHeight
	 */
	public void expand(int targetWidth, int targetHeight) {
		ensureNinePatch();
		
		int addCols=targetWidth - image.getWidth();
		int addRows=targetWidth - image.getHeight();
		
		if (addCols<0 || addRows<0) {
			throw new IllegalArgumentException("Attempt to expand image by a negative amount");
		}
		
		int[] colMarkers=getMarkers(MODE_SCALEX);
		int[] rowMarkers=getMarkers(MODE_SCALEY);
		BufferedImage dest=new BufferedImage(targetWidth, targetHeight, image.getType());
		
		int colLoops=0, colRem=0;
		if (colMarkers.length>0) {
			colLoops=addCols/colMarkers.length;
			colRem=addCols%colMarkers.length;
		}
		
		int rowLoops=0, rowRem=0;
		if (rowMarkers.length>0) {
			rowLoops=addRows/rowMarkers.length;
			rowRem=addRows%rowMarkers.length;
		}

		//System.out.format("Adding colLoops=%s, colRem=%s, rowLoops=%s, rowRem=%s\n", colLoops, colRem, rowLoops, rowRem);
		
		int index;
		
		// Copy rows
		int ydest=0;
		int ysrc=0;
		for (index=0; index<rowMarkers.length; index++) {
			int y=rowMarkers[index];
			// Copy fixed rows
			for (; ysrc<y; ysrc++) {
				copyRow(image, ysrc, dest, ydest++);
			}
			
			// Copy this row up to loop times
			for (int c=0; c<rowLoops; c++) {
				copyRow(image, y, dest, ydest++);
			}
			
			// Copy one more if there is a remainder
			if (rowRem>0) {
				copyRow(image, y, dest, ydest++);
				rowRem--;
			}
			
			ysrc=y;
		}
		
		// Copy remaining rows
		for (; ysrc<image.getHeight(); ysrc++) {
			copyRow(image, ysrc, dest, ydest++);
		}
		
		// Copy columns (backwards)
		int xdest=targetWidth-1;
		int xsrc=image.getWidth()-1;
		for (index=colMarkers.length-1; index>=0; index--) {
			int x=colMarkers[index];
			
			// Copy fixed cols
			for (; xsrc>x; xsrc--) {
				copyCol(dest, xsrc, dest, xdest--);
			}
			
			// Copy this col up to loop times
			for (int c=0; c<colLoops; c++) {
				copyCol(dest, x, dest, xdest--);
			}
			
			// Copy one more for remainder
			if (colRem>0) {
				copyCol(dest, x, dest, xdest--);
				colRem--;
			}
			
			xsrc=x;
		}
		
		// Copy remaining cols
		for (; xsrc>=0; xsrc--) {
			copyCol(dest, xsrc, dest, xdest--);
		}
		
		this.image=dest;
	}

	private void copyCol(BufferedImage src, int xsrc, BufferedImage dest,
			int xdest) {
		//System.out.println("copyCol(" + xsrc + " -> " + xdest + ")");
		
		Raster srcRaster=src.getRaster().createChild(xsrc, 0, 1, Math.min(src.getHeight(), dest.getHeight()), 
				0, 0, null);
		dest.getRaster().setDataElements(xdest, 0, srcRaster);
	}

	private void copyRow(BufferedImage src, int ysrc, BufferedImage dest,
			int ydest) {
		Raster srcRaster=src.getRaster().createChild(0, ysrc, Math.min(src.getWidth(), dest.getWidth()), 
				1, 0, 0, null);
		dest.getRaster().setDataElements(0, ydest, srcRaster);
	}
}
package de.minestar.sixteenblocks.Core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import de.minestar.minestarlibrary.commands.CommandList;
import de.minestar.minestarlibrary.utils.ConsoleUtils;
import de.minestar.sixteenblocks.Commands.cmdAdmin;
import de.minestar.sixteenblocks.Commands.cmdBan;
import de.minestar.sixteenblocks.Commands.cmdChat;
import de.minestar.sixteenblocks.Commands.cmdChatRadius;
import de.minestar.sixteenblocks.Commands.cmdCreateRow;
import de.minestar.sixteenblocks.Commands.cmdDeleteArea;
import de.minestar.sixteenblocks.Commands.RealStop;
import de.minestar.sixteenblocks.Commands.cmdGive;
import de.minestar.sixteenblocks.Commands.cmdHideChat;
import de.minestar.sixteenblocks.Commands.cmdHome;
import de.minestar.sixteenblocks.Commands.cmdInfo;
import de.minestar.sixteenblocks.Commands.cmdKick;
import de.minestar.sixteenblocks.Commands.cmdMe;
import de.minestar.sixteenblocks.Commands.cmdMessage;
import de.minestar.sixteenblocks.Commands.cmdMute;
import de.minestar.sixteenblocks.Commands.cmdNumberize;
import de.minestar.sixteenblocks.Commands.cmdRebuild;
import de.minestar.sixteenblocks.Commands.cmdReload;
import de.minestar.sixteenblocks.Commands.cmdReloadFilter;
import de.minestar.sixteenblocks.Commands.cmdReply;
import de.minestar.sixteenblocks.Commands.cmdReset;
import de.minestar.sixteenblocks.Commands.cmdRow;
import de.minestar.sixteenblocks.Commands.cmdSaveArea;
import de.minestar.sixteenblocks.Commands.cmdSay;
import de.minestar.sixteenblocks.Commands.cmdSlots;
import de.minestar.sixteenblocks.Commands.cmdSpawn;
import de.minestar.sixteenblocks.Commands.cmdStartAuto;
import de.minestar.sixteenblocks.Commands.cmdStartHere;
import de.minestar.sixteenblocks.Commands.cmdStop;
import de.minestar.sixteenblocks.Commands.cmdSupport;
import de.minestar.sixteenblocks.Commands.cmdSupporter;
import de.minestar.sixteenblocks.Commands.cmdTP;
import de.minestar.sixteenblocks.Commands.cmdTicket;
import de.minestar.sixteenblocks.Commands.cmdURL;
import de.minestar.sixteenblocks.Commands.cmdUnban;
import de.minestar.sixteenblocks.Commands.cmdVip;
import de.minestar.sixteenblocks.Listener.ActionListener;
import de.minestar.sixteenblocks.Listener.BaseListener;
import de.minestar.sixteenblocks.Listener.ChatListener;
import de.minestar.sixteenblocks.Listener.CommandListener;
import de.minestar.sixteenblocks.Listener.LoginListener;
import de.minestar.sixteenblocks.Listener.MovementListener;
import de.minestar.sixteenblocks.Mail.MailHandler;
import de.minestar.sixteenblocks.Manager.AreaDatabaseManager;
import de.minestar.sixteenblocks.Manager.AreaManager;
import de.minestar.sixteenblocks.Manager.ChannelManager;
import de.minestar.sixteenblocks.Manager.NumberManager;
import de.minestar.sixteenblocks.Manager.StructureManager;
import de.minestar.sixteenblocks.Manager.WorldManager;
import de.minestar.sixteenblocks.Threads.AFKThread;
import de.minestar.sixteenblocks.Threads.BlockThread;
import de.minestar.sixteenblocks.Threads.BroadcastThread;
import de.minestar.sixteenblocks.Threads.ChatThread;
import de.minestar.sixteenblocks.Threads.DayThread;
import de.minestar.sixteenblocks.Threads.JSONThread;
import de.minestar.sixteenblocks.Units.ChatFilter;

public class Core extends JavaPlugin {
    private static Core instance;

    // LISTENER
    private Listener baseListener, blockListener, movementListener, loginListener, commandListener;
    private ChatListener chatListener;

    // MANAGER
    private AreaDatabaseManager areaDatabaseManager;
    private AreaManager areaManager;
    private WorldManager worldManager;
    private StructureManager structureManager;
    private ChannelManager channelManager;
    private NumberManager numberManager;
    private MailHandler mHandler;
    private ChatFilter filter;

    // THREADS
    private BlockThread blockThread;
    private AFKThread afkThread;
    private BroadcastThread bThread;
    private ChatThread chatThread;

    private Timer timer = new Timer(), broadcastTimer = new Timer(), chatTimer = new Timer();

    private CommandList commandList;

    // GLOBAL VARIABLES

    public final static String NAME = "16Blocks";

    public static boolean shutdownServer = false;
    public static boolean isShutDown = false;

    @Override
    public void onDisable() {
        this.areaDatabaseManager.closeConnection();
//        this.ticketDatabaseManager.closeConnection();
//        checkTread.saveCheckTickets();
        Settings.saveSettings(this.getDataFolder());
        timer.cancel();
        broadcastTimer.cancel();
        chatTimer.cancel();
        TextUtils.logInfo("Disabled!");
    }

    @Override
    public void onEnable() {
        // INIT INSTANCE
        instance = this;

        // CREATE SKIN-DIR
        new File(this.getDataFolder(), "/skins/").mkdirs();

        // INIT SETTINGS
        Settings.init(this.getDataFolder());

        // SET NAME
        TextUtils.setPluginName("YAM");

        // SUPER EXTENSION-THREAD
        this.blockThread = new BlockThread(Bukkit.getWorlds().get(0));
        this.blockThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this.blockThread, 0L, Settings.getTicksBetweenReplace()));

        // AFK THREAD
        this.afkThread = new AFKThread();

        // STARTUP
        this.createManager();

        this.areaManager.updateThread(this.blockThread);

        // INIT AREAMANAGER
        this.areaManager.init();

        // LOAD SUPPORTER (NOT ALL ADMINS ARE OPS)
        loadSupporter();

        this.registerListeners();

        // FINAL INTITIALIZATION
        this.areaManager.checkForZoneExtension();
        createThreads(Bukkit.getScheduler());

        // INIT COMMANDS
        this.initCommands();

        // UPDATE SPAWN
        Bukkit.getWorlds().get(0).setSpawnLocation(Settings.getSpawnVector().getBlockX(), Settings.getSpawnVector().getBlockY(), Settings.getSpawnVector().getBlockZ());

        // INFO
        TextUtils.logInfo("Enabled!");

        for (OfflinePlayer oPlayer : Bukkit.getOperators())
            operator.add(oPlayer.getName().toLowerCase());
    }

    /**
     * @return the structureManager
     */
    public StructureManager getStructureManager() {
        return structureManager;
    }

    private void createManager() {
        this.areaDatabaseManager = new AreaDatabaseManager(this.getDescription().getName(), this.getDataFolder());
        // this.ticketDatabaseManager = new TicketDatabaseManager(NAME,
        // getDataFolder());
        this.structureManager = new StructureManager();
        this.worldManager = new WorldManager();
        this.areaManager = new AreaManager(this.areaDatabaseManager, this.worldManager, this.structureManager);
        this.mHandler = new MailHandler(getDataFolder());
        this.filter = new ChatFilter(getDataFolder());
        this.numberManager = new NumberManager();
        this.channelManager = new ChannelManager();
    }

    private void registerListeners() {
        // CREATE LISTENERS
        this.baseListener = new BaseListener(this.channelManager);
        this.blockListener = new ActionListener(this.areaManager, this.afkThread, this.channelManager);
        this.chatListener = new ChatListener(this.filter, this.afkThread, this.channelManager);
        this.movementListener = new MovementListener(this.worldManager, this.afkThread);
        this.loginListener = new LoginListener(this.afkThread);
        this.commandListener = new CommandListener();

        // REGISTER LISTENERS
        Bukkit.getPluginManager().registerEvents(this.baseListener, this);
        Bukkit.getPluginManager().registerEvents(this.blockListener, this);
        Bukkit.getPluginManager().registerEvents(this.chatListener, this);
        Bukkit.getPluginManager().registerEvents(this.movementListener, this);
        Bukkit.getPluginManager().registerEvents(this.loginListener, this);
        Bukkit.getPluginManager().registerEvents(this.commandListener, this);
    }

    private void initCommands() {
        /* @formatter:off */
        // Empty permission because permissions are handeld in the commands
        Map<Player, Player> recipients = new HashMap<Player, Player>(256);
        commandList = new CommandList(Core.NAME,

                        new cmdSpawn        ("/spawn",      "",                         ""),
                        new cmdInfo         ("/info",       "",                         ""),
                        new cmdStartAuto    ("/start",      "",                         "", this.areaManager),
                        new cmdStartAuto    ("/startauto",  "",                         "", this.areaManager),
                        new cmdStartHere    ("/starthere",  "",                         "", this.areaManager),
                        new cmdHome         ("/home",       "[Playername]",             "", this.areaManager),
                        new cmdSaveArea     ("/save",       "<StructureName>",          "", this.areaManager, this.structureManager),

                        // TELEPORT
                        new cmdCreateRow    ("/createrow",  "<Number>",                 ""),
                        new cmdChatRadius   ("/chatradius", "<Number>",                 ""),

                        new cmdRow          ("/row",        "<Number>",                 ""),
                        new cmdRow          ("/jump",       "<Number>",                 ""),
                        new cmdTP           ("/tp",         "<Player>",                 ""),
                        new cmdRebuild      ("/rebuild",    "",                         "", this.areaManager),
                        new cmdReset        ("/reset",    "",                           "", this.areaManager),
                        new cmdNumberize    ("/numberize",    "",                       "", this.numberManager, this.areaManager),

                        // STOP RELOAD
                        new cmdStop         ("/shutdown",        "",                    ""),
                        new cmdReload       ("/rel",        "",                         ""),

                        // MESSAGE SYSTEM
                        new cmdMessage      ("/m",          "<PlayerName> <Message>",   "", recipients),
                        new cmdMessage      ("/w",          "<PlayerName> <Message>",   "", recipients),
                        new cmdReply        ("/r",          "<Message>",                "", recipients),
                        new cmdMe           ("/me",         "<Message>",                ""),
                        new cmdMute         ("/mute",       "<Player>",                 "", this.channelManager),

                        // PUNISHMENTS
                        new cmdBan          ("/ban",        "<Playername>",             ""),
                        new cmdUnban        ("/unban",      "<Playername>",             ""),
                        new cmdKick         ("/kick",       "<Playername> [Message]",   ""),
                        new cmdDeleteArea   ("/delete",     "[Playername]",             "", this.areaManager),
                        new cmdSupporter    ("/supporter",  "<Playername>",             ""),
                        new cmdVip          ("/vip",        "<Playername>",             ""),

                        // BUG REPORTS
                        new cmdTicket       ("/ticket",     "<Text>",                   "", mHandler),
                        new cmdTicket       ("/bug",        "<Text>",                   "", mHandler),
                        new cmdTicket       ("/report",     "<Text>",                   "", mHandler),

                        // SET SLOTS
                        new cmdSlots        ("/slots",      "<Number>",                 ""),

                        // BROADCASTS
                        new cmdSay          ("/say",        "<Message>",                ""),
                        new cmdSay          ("/cast",       "<Message>",                ""),
                        new cmdSay          ("/broadcast",  "<Message>",                ""),

                        // LOOKING FOR SUPPORTER
                        new cmdAdmin        ("/admins",     "",                         "", this.channelManager),

                        // RELOAD CHATFILTER
                        new cmdReloadFilter ("/filter",     "",                         "", this.filter, bThread),

                        new cmdGive         ("/give",       "<Player> <Item[:SubID]> [Amount]", ""),

                        // URL TO LIVE MAP
                        new cmdURL          ("/livemap",    "[Player]",                 "", this.areaManager),

                        // CHAT HANDELING
                        new cmdChat         ("/chat",       "",                         "", this.channelManager),
                        new cmdHideChat     ("/hidechat",   "",                         "", this.channelManager),
                        new cmdHideChat     ("/nochat",     "",                         "", this.channelManager),
                        new cmdSupport      ("/support",    "",                         "", this.channelManager),

                        // MASTER OF DESASTER COMMAND
                        new RealStop ("/realstop",          "",                         ""),
                        new RealStop ("/realreload",        "",                         "")

        );
        /* @formatter:on */
    }
    private void createThreads(BukkitScheduler scheduler) {
        // Keep always day time
        scheduler.scheduleSyncRepeatingTask(this, new DayThread(Bukkit.getWorlds().get(0), Settings.getTime()), 0, 1);

        // Check tickets
        // checkTread = new CheckTicketThread(this.ticketDatabaseManager,
        // getDataFolder());
        // scheduler.scheduleSyncRepeatingTask(this, checkTread, 20L * 60L, 20L
        // * 60L * 10L);

        // Writing JSON with online player
        timer.schedule(new JSONThread(this.areaManager), 1000L * 5L, 1000L * 5L);

        // Broadcasting information to player
        bThread = new BroadcastThread(this.getDataFolder(), this.areaManager);
        broadcastTimer.schedule(bThread, 1000L * 60L, 1000L * 60L * Settings.getJAMES_INTERVAL());

        this.chatThread = new ChatThread(this.chatListener, this.channelManager);
        this.chatTimer.schedule(this.chatThread, 1000L * 30L, 1000L * 30L);

        // AFK Thread
        scheduler.scheduleSyncRepeatingTask(this, this.afkThread, 20L * 10L, 20L * 30L);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        commandList.handleCommand(sender, label, args);
        return true;
    }

    public static Core getInstance() {
        return instance;
    }

    /**
     * @return the areaManager
     */
    public AreaManager getAreaManager() {
        return areaManager;
    }

    // SUPPORT AND VIP HANDELING

    private static Set<String> supporter, vips;

    public void addSupporter(String playerName) {
        Core.supporter.add(playerName.toLowerCase());
    }

    public void removeSupporter(String playerName) {
        Core.supporter.remove(playerName.toLowerCase());
    }

    public boolean toggleSupporter(String playerName) {
        playerName = playerName.toLowerCase();
        this.removeVip(playerName);
        if (!Core.isSupporter(playerName)) {
            this.addSupporter(playerName);
        } else {
            this.removeSupporter(playerName);
        }
        this.saveSupporter();
        return Core.isSupporter(playerName);
    }

    public void addVip(String playerName) {
        Core.vips.add(playerName.toLowerCase());
    }

    public void removeVip(String playerName) {
        Core.vips.remove(playerName.toLowerCase());
    }

    public boolean toggleVip(String playerName) {
        playerName = playerName.toLowerCase();
        this.removeSupporter(playerName);
        if (!Core.isVip(playerName)) {
            this.addVip(playerName);
        } else {
            this.removeVip(playerName);
        }
        this.saveVips();
        return Core.isVip(playerName);
    }

    public static Set<String> getSupporter() {
        return Core.supporter;
    }

    public static Set<String> getVips() {
        return Core.vips;
    }

    private void saveSupporter() {
        this.saveUsers(Core.supporter, "supporter.txt");
        this.saveUsers(Core.vips, "vips.txt");
    }

    private void saveVips() {
        this.saveUsers(Core.supporter, "supporter.txt");
        this.saveUsers(Core.vips, "vips.txt");
    }

    private void saveUsers(Set<String> set, String fileName) {
        File f = new File(getDataFolder(), fileName);
        // IF FILE EXISTS: DELETE IT
        if (f.exists()) {
            f.delete();
        }

        try {
            f.createNewFile();
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(f));
            int count = 0;
            for (String name : set) {
                bWriter.write(name + System.getProperty("line.separator"));
                count++;
            }
            ConsoleUtils.printInfo(NAME, "Saved " + count + " users in '" + fileName + "'!");
            bWriter.flush();
            bWriter.close();
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Error while saving file '" + fileName + "'!");
        }
    }

    private void loadSupporter() {
        Core.supporter = new HashSet<String>(16);
        Core.vips = new HashSet<String>(16);
        this.loadUsers(Core.supporter, "supporter.txt");
        this.loadUsers(Core.vips, "vips.txt");
    }

    private void loadUsers(Set<String> set, String fileName) {
        File f = new File(getDataFolder(), fileName);
        // CHECK: FILE EXISTS
        if (!f.exists()) {
            ConsoleUtils.printError(NAME, "File '" + fileName + "' not found! No Users were loaded.");
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            BufferedReader bReader = new BufferedReader(new FileReader(f));
            String line = "";
            while ((line = bReader.readLine()) != null) {
                line = line.trim().replace(" ", "");
                if (!line.isEmpty()) {
                    set.add(line.toLowerCase());
                }
            }
            ConsoleUtils.printInfo(NAME, "Loaded " + set.size() + " users from '" + fileName + "'!");
        } catch (Exception e) {
            ConsoleUtils.printException(e, NAME, "Error while reading file: '" + fileName + "'");
        }
    }

    public static boolean isVip(Player player) {
        return Core.isVip(player.getName());
    }

    public static boolean isVip(String playerName) {
        return vips.contains(playerName.toLowerCase());
    }

    private static Set<String> operator = new HashSet<String>(64);

    public static boolean isSupporter(Player player) {
        return Core.isSupporter(player.getName());
    }

    public static boolean isSupporter(String playerName) {
        String name = playerName.toLowerCase();
        return operator.contains(name) || supporter.contains(name) || Core.isVip(name);
    }

    public static int getAllowedMaxPlayer() {
        return Bukkit.getMaxPlayers() - Settings.getSupporterBuffer();
    }

    /**
     * @return the extendThread
     */
    public BlockThread getExtendThread() {
        return blockThread;
    }
}
package de.minestar.sixteenblocks.Core;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import de.minestar.sixteenblocks.Listener.ChatListener;

public class Settings {
    private static int AREA_SIZE_X = 32, AREA_SIZE_Z = 32;
    private static int MINIMUM_BUILD_Y = 6, MAXIMUM_BUILD_Y = 50;
    private static int BASE_Y = 4;

    private static int CHAT_PAUSE_IN_SECONDS = 5;

    private static int SKINS_LEFT = 10;
    private static int SKINS_RIGHT = 10;

    private static int OLD_SKINS_LEFT = 10;
    private static int OLD_SKINS_RIGHT = 10;

    private static long TIME = 6000;

    private static long SUPPORT_TIME = 2 * 60 * 1000L;

    private static int MAX_BLOCKS_REPLACE_AT_ONCE = 100;
    private static long TICKS_BETWEEN_REPLACE = 5L;
    private static int CREATE_ROWS_AT_ONCE = 2;

    private static int CHAT_RADIUS = 150;

    private static long JAMES_INTERVAL = 2L;

    private static ChatColor colorNormal = ChatColor.GREEN;
    private static ChatColor colorSupporter = ChatColor.RED;
    private static ChatColor colorVips = ChatColor.GREEN;

    /**
     * @return the colorNormal
     */
    public static ChatColor getColorNormal() {
        return colorNormal;
    }

    /**
     * @return the colorSupporter
     */
    public static ChatColor getColorSupporter() {
        return colorSupporter;
    }

    /**
     * @return the colorVips
     */
    public static ChatColor getColorVips() {
        return colorVips;
    }

    /**
     * @param cHAT_RADIUS
     *            the cHAT_RADIUS to set
     */
    public static void setChatRadius(int cHAT_RADIUS) {
        CHAT_RADIUS = cHAT_RADIUS;
    }

    // Player who can join the server when server is full
    private static int SUPPORTER_BUFFER = 30;

    private static Vector SPAWN_VECTOR = new Vector(0, 4, 0), INFOWALL_VECTOR = new Vector(0, 4, 0);

    private static String JSON_PATH = "stats.json";

    public static void init(File dataFolder) {
        try {
            File file = new File(dataFolder, "config.yml");
            if (!file.exists()) {
                saveSettings(dataFolder);
                return;
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            TIME = config.getLong("general.dayTime", TIME);
            BASE_Y = config.getInt("general.baseLevel", BASE_Y);
            CHAT_PAUSE_IN_SECONDS = config.getInt("general.chatPauseInSeconds", CHAT_PAUSE_IN_SECONDS);
            CHAT_RADIUS = config.getInt("general.chatRadius", CHAT_RADIUS);

            AREA_SIZE_X = config.getInt("Zone.sizeX", AREA_SIZE_X);
            AREA_SIZE_Z = config.getInt("Zone.sizeZ", AREA_SIZE_Z);
            MINIMUM_BUILD_Y = config.getInt("Zone.minimumBuildLevel", MINIMUM_BUILD_Y);
            MAXIMUM_BUILD_Y = config.getInt("Zone.maximumBuildLevel", MAXIMUM_BUILD_Y);

            SKINS_LEFT = config.getInt("Skins.left", SKINS_LEFT);
            SKINS_RIGHT = config.getInt("Skins.right", SKINS_RIGHT);

            SUPPORT_TIME = config.getLong("Channel.supportTime", SUPPORT_TIME);

            OLD_SKINS_LEFT = config.getInt("Skins.oldLeft", SKINS_LEFT);
            OLD_SKINS_RIGHT = config.getInt("Skins.oldRight", SKINS_RIGHT);

            colorNormal = ChatColor.getByChar(config.getString("Color.normal", String.valueOf(colorNormal.getChar())));
            colorSupporter = ChatColor.getByChar(config.getString("Color.supporter", String.valueOf(colorSupporter.getChar())));
            colorVips = ChatColor.getByChar(config.getString("Color.vips", String.valueOf(colorVips.getChar())));

            MAX_BLOCKS_REPLACE_AT_ONCE = config.getInt("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            TICKS_BETWEEN_REPLACE = config.getLong("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);
            CREATE_ROWS_AT_ONCE = config.getInt("Threads.Structures.createRowsAtOnce", CREATE_ROWS_AT_ONCE);

            SPAWN_VECTOR = config.getVector("Locations.spawn", SPAWN_VECTOR);
            INFOWALL_VECTOR = config.getVector("Locations.infoWall", INFOWALL_VECTOR);

            JSON_PATH = config.getString("general.JSON", JSON_PATH);

            SUPPORTER_BUFFER = config.getInt("general.supportBuffer", SUPPORTER_BUFFER);

            JAMES_INTERVAL = config.getLong("general.jamesInterval", JAMES_INTERVAL);

            if (CHAT_RADIUS < 1) {
                ChatListener.radiusOff = true;
            } else {
                ChatListener.radiusOff = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            saveSettings(dataFolder);
        }
    }

    public static void saveSettings(File dataFolder) {
        try {
            File file = new File(dataFolder, "config.yml");
            boolean fileExists = file.exists();

            YamlConfiguration config = new YamlConfiguration();
            if (fileExists)
                config.load(file);

//            config.set("general.dayTime", TIME);
            config.set("general.baseLevel", BASE_Y);
            config.set("general.chatPauseInSeconds", CHAT_PAUSE_IN_SECONDS);
            config.set("general.chatRadius", CHAT_RADIUS);

            config.set("Zone.sizeX", AREA_SIZE_X);
            config.set("Zone.sizeZ", AREA_SIZE_Z);
            config.set("Zone.minimumBuildLevel", MINIMUM_BUILD_Y);
            config.set("Zone.maximumBuildLevel", MAXIMUM_BUILD_Y);

            config.set("Skins.left", SKINS_LEFT);
            config.set("Skins.right", SKINS_RIGHT);

            config.set("Skins.oldLeft", SKINS_LEFT);
            config.set("Skins.oldRight", SKINS_RIGHT);

            config.set("Channel.supportTime", SUPPORT_TIME);

            // SAVE COLORCODES
            config.set("ChatColor.AQUA", String.valueOf(ChatColor.AQUA.getChar()));
            config.set("ChatColor.BLACK", String.valueOf(ChatColor.BLACK.getChar()));
            config.set("ChatColor.BLUE", String.valueOf(ChatColor.BLUE.getChar()));
            config.set("ChatColor.DARK_AQUA", String.valueOf(ChatColor.DARK_AQUA.getChar()));
            config.set("ChatColor.DARK_BLUE", String.valueOf(ChatColor.DARK_BLUE.getChar()));
            config.set("ChatColor.DARK_GRAY", String.valueOf(ChatColor.DARK_GRAY.getChar()));
            config.set("ChatColor.DARK_GREEN", String.valueOf(ChatColor.DARK_GREEN.getChar()));
            config.set("ChatColor.DARK_PURPLE", String.valueOf(ChatColor.DARK_PURPLE.getChar()));
            config.set("ChatColor.DARK_RED", String.valueOf(ChatColor.DARK_RED.getChar()));
            config.set("ChatColor.GOLD", String.valueOf(ChatColor.GOLD.getChar()));
            config.set("ChatColor.GRAY", String.valueOf(ChatColor.GRAY.getChar()));
            config.set("ChatColor.GREEN", String.valueOf(ChatColor.GREEN.getChar()));
            config.set("ChatColor.LIGHT_PURPLE", String.valueOf(ChatColor.LIGHT_PURPLE.getChar()));
            config.set("ChatColor.RED", String.valueOf(ChatColor.RED.getChar()));
            config.set("ChatColor.WHITE", String.valueOf(ChatColor.WHITE.getChar()));
            config.set("ChatColor.YELLOW", String.valueOf(ChatColor.YELLOW.getChar()));

            config.set("Threads.Structures.MaxReplaceAtOnce", MAX_BLOCKS_REPLACE_AT_ONCE);
            config.set("Threads.Structures.ticksBetweenReplace", TICKS_BETWEEN_REPLACE);
            config.set("Threads.Structures.createRowsAtOnce", CREATE_ROWS_AT_ONCE);

            config.set("Locations.spawn", SPAWN_VECTOR);
            config.set("Locations.infoWall", INFOWALL_VECTOR);

            config.set("general.JSON", JSON_PATH);

            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getAreaSizeX() {
        return AREA_SIZE_X;
    }

    public static int getAreaSizeZ() {
        return AREA_SIZE_Z;
    }

    public static long getTime() {
        return TIME;
    }

    public static long getSupportTime() {
        return SUPPORT_TIME;
    }

    public static int getMinimumBuildY() {
        return MINIMUM_BUILD_Y;
    }

    public static int getMaximumBuildY() {
        return MAXIMUM_BUILD_Y;
    }

    public static int getBaseY() {
        return BASE_Y;
    }

    public static int getMaxBlocksReplaceAtOnce() {
        return MAX_BLOCKS_REPLACE_AT_ONCE;
    }

    public static long getTicksBetweenReplace() {
        return TICKS_BETWEEN_REPLACE;
    }

    public static Vector getSpawnVector() {
        return SPAWN_VECTOR;
    }

    public static Vector getInfoWallVector() {
        return INFOWALL_VECTOR;
    }

    public static int getSkinsLeft() {
        return SKINS_LEFT;
    }

    public static int getSkinsRight() {
        return SKINS_RIGHT;
    }

    public static int getSkinsLeftOld() {
        return OLD_SKINS_LEFT;
    }

    public static int getSkinsRightOld() {
        return OLD_SKINS_RIGHT;
    }

    public static int getChatPauseTimeInSeconds() {
        return CHAT_PAUSE_IN_SECONDS;
    }

    public static int getCreateRowsAtOnce() {
        return CREATE_ROWS_AT_ONCE;
    }

    public static int getChatRadius() {
        return CHAT_RADIUS;
    }

    public static String getJSONPath() {
        return JSON_PATH;
    }

    public static int getSupporterBuffer() {
        return SUPPORTER_BUFFER;
    }

    public static long getJAMES_INTERVAL() {
        return JAMES_INTERVAL;
    }
}
package de.minestar.sixteenblocks.Listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;

import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Manager.ChannelManager;

public class BaseListener implements Listener {

    private ChannelManager channelManager;

    public BaseListener(ChannelManager channelManager) {
        this.channelManager = channelManager;
    }

    // ////////////////////////////////////////////////
    //
    // ENTITY-LISTENER
    //
    // ////////////////////////////////////////////////

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityBlockChange(EntityChangeBlockEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemSpawnEvent(ItemSpawnEvent event) {
        event.setCancelled(true);
    }

    // ////////////////////////////////////////////////
    //
    // WORLD-LISTENER
    //
    // ////////////////////////////////////////////////

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        event.getWorld().setSpawnLocation(0, 6, 0);
    }

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        event.getWorld().setSpawnLocation(0, 6, 0);
    }

    // ////////////////////////////////////////////////
    //
    // PLAYER-LISTENER
    //
    // ////////////////////////////////////////////////

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().setGameMode(GameMode.CREATIVE);
        event.setJoinMessage("");
        this.channelManager.updatePlayer(event.getPlayer(), this.channelManager.getChannelByChannelName("Hidden"));
        TextUtils.sendInfo(event.getPlayer(), ChatColor.RED + "WELCOME TO YOUAREMINECRAFT!");
        TextUtils.sendInfo(event.getPlayer(), ChatColor.LIGHT_PURPLE + "To get into the chat, type /chat");
        TextUtils.sendInfo(event.getPlayer(), ChatColor.LIGHT_PURPLE + "To get into the support chat, type /support");
        TextUtils.sendInfo(event.getPlayer(), ChatColor.LIGHT_PURPLE + "To get back here, type /nochat or /hidechat"); 
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        this.channelManager.removePlayerFromChannel(event.getPlayer());
    }

    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage("");
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        event.setAmount(0);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        event.setCancelled(true);
    }

    // ////////////////////////////////////////////////
    //
    // WEATHER-LISTENER
    //
    // ////////////////////////////////////////////////
    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.toThunderState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    // ////////////////////////////////////////////////
    //
    // BLOCK-LISTENER
    //
    // ////////////////////////////////////////////////

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() != IgniteCause.FLINT_AND_STEEL) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }
}
package de.minestar.sixteenblocks.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.server.Packet130UpdateSign;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.minestar.sixteenblocks.Core.Core;
import de.minestar.sixteenblocks.Core.Settings;
import de.minestar.sixteenblocks.Core.TextUtils;
import de.minestar.sixteenblocks.Enums.EnumDirection;
import de.minestar.sixteenblocks.Enums.EnumStructures;
import de.minestar.sixteenblocks.Threads.AreaDeletionThread;
import de.minestar.sixteenblocks.Threads.BlockThread;
import de.minestar.sixteenblocks.Units.StructureBlock;
import de.minestar.sixteenblocks.Units.ZoneXZ;

public class AreaManager {
    private Map<String, SkinArea> usedAreaList = new HashMap<String, SkinArea>();
    private Map<String, SkinArea> unusedAreaList = new HashMap<String, SkinArea>();

    private Set<String> blockedAreas = new HashSet<String>();

    private StructureManager structureManager;
    private AreaDatabaseManager databaseManager;
    private WorldManager worldManager;

    private int lastRow = 0;

    private BlockThread blockThread;

    // ////////////////////////////////////////////////
    //
    // CONSTRUCTOR
    //
    // ////////////////////////////////////////////////

    public AreaManager(AreaDatabaseManager databaseManager, WorldManager worldManager, StructureManager structureManager) {
        this.worldManager = worldManager;
        this.databaseManager = databaseManager;
        this.structureManager = structureManager;
    }

    public void init() {
        this.createNotExistingAreas();
        this.loadAreas();
        this.initMaximumZ();
        this.checkForZoneExtension();
    }

    public void updateThread(BlockThread thread) {
        this.blockThread = thread;
    }

    public ArrayList<StructureBlock> getChangedBlocks(ZoneXZ thisZone) {
        ArrayList<StructureBlock> blockList = new ArrayList<StructureBlock>();
        int baseX = thisZone.getBaseX();
        int baseZ = thisZone.getBaseZ();
        World world = Bukkit.getWorlds().get(0);
        for (int y = Settings.getMaximumBuildY(); y >= Settings.getMinimumBuildY(); y--) {
            for (int x = 0; x < Settings.getAreaSizeX(); x++) {
                for (int z = 0; z < Settings.getAreaSizeZ(); z++) {
                    if (world.getBlockTypeIdAt(baseX + x, y, baseZ + z) != Material.AIR.getId()) {
                        blockList.add(new StructureBlock(x, y, z, 0));
                    }
                }
            }
        }
        return blockList;
    }

    private void createNotExistingAreas() {
        if (Settings.getSkinsLeft() == Settings.getSkinsLeftOld() && Settings.getSkinsRight() == Settings.getSkinsRightOld())
            return;

        ArrayList<SkinArea> newSkins = this.databaseManager.createNotExistingAreas();
        System.out.println("Create new areas: " + newSkins.size());
        ZoneXZ thisZone;
        int maxZ = Integer.MIN_VALUE;
        int currentRow = -1;
        World thisWorld = Bukkit.getWorlds().get(0);
        for (SkinArea thisArea : newSkins) {
            thisZone = thisArea.getZoneXZ();

            if (maxZ < thisZone.getZ())
                maxZ = thisZone.getZ();

            if (currentRow != thisZone.getZ()) {
                int baseXLeft = ZoneXZ.getBaseX(Settings.getSkinsLeftOld() + 1, thisZone.getZ());
                int baseXRight = ZoneXZ.getBaseX(-(Settings.getSkinsRightOld()), thisZone.getZ()) - 1;

                if (thisZone.getZ() % 2 != 0) {
                    baseXLeft -= (Settings.getAreaSizeX());
                }

                // REBASE ZONES
                int baseZ = thisZone.getBaseZ();
                Block thisBlock;
                for (int z = 0; z < Settings.getAreaSizeZ(); z++) {
                    for (int x = 0; x < Settings.getAreaSizeX() * 2; x++) {
                        thisWorld.getBlockAt(baseXLeft + x, 2, baseZ + z).setTypeId(Material.DIRT.getId(), false);
                        thisWorld.getBlockAt(baseXLeft + x, 3, baseZ + z).setTypeId(Material.GRASS.getId(), false);
                        thisBlock = thisWorld.getBlockAt(baseXLeft + x, 4, baseZ + z);
                        if (thisBlock.getTypeId() == 44) {
                            thisBlock.setTypeId(Material.AIR.getId(), false);
                        }

                        thisWorld.getBlockAt(baseXRight - x, 2, baseZ + z).setTypeId(Material.DIRT.getId(), false);
                        thisWorld.getBlockAt(baseXRight - x, 3, baseZ + z).setTypeId(Material.GRASS.getId(), false);
                        thisBlock = thisWorld.getBlockAt(baseXRight - x, 4, baseZ + z);
                        if (thisBlock.getTypeId() == 44) {
                            thisBlock.setTypeId(Material.AIR.getId(), false);
                        }
                    }
                }
                currentRow = thisZone.getZ();
                System.out.println("Current row: " + currentRow);
            }
            this.createSingleZone(thisZone);
        }
    }

    // ////////////////////////////////////////////////
    //
    // AREA CREATION
    //
    // ////////////////////////////////////////////////

    public void createRow(int z) {
        for (int x = -Settings.getSkinsRight() + (z % 2 == 0 ? 0 : 1); x <= Settings.getSkinsLeft(); x++) {
            this.createUnusedArea(new SkinArea(x, z, ""), true);
            if (z % 2 != 0) {
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, z - 1, blockThread);
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x - 1, z - 1, blockThread);
            } else {
                if (x > -Settings.getSkinsRight()) {
                    this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, z - 1, blockThread);
                }
            }
        }
        if (z == 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_CORNER).createStructure(-Settings.getSkinsRight(), z - 1, this.blockThread);
            this.structureManager.getStructure(EnumStructures.STREETS_CORNER).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, z - 1, blockThread);

            this.structureManager.getStructure(EnumStructures.INFO_WALL_1).createStructure(1, z - 1, blockThread);
            this.structureManager.getStructure(EnumStructures.INFO_WALL_2).createStructure(0, z - 1, blockThread);

        } else if (z % 2 != 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(-Settings.getSkinsRight() - 1, z - 1, blockThread);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, z - 1, blockThread);
        } else {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(-Settings.getSkinsRight(), z - 1, blockThread);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, z - 1, blockThread);
        }
    }

    public void createSingleZone(ZoneXZ thisZone) {
        BlockThread extendThread = Core.getInstance().getExtendThread();

        int x = thisZone.getX();
        int row = thisZone.getZ();
        int baseX = thisZone.getBaseX();
        if (row % 2 != 0) {
            baseX -= Settings.getAreaSizeX();
        }
        int baseZ = thisZone.getBaseZ();

        // CREATE BACK-STREETS
        if (row % 2 != 0) {
            // UNEVEN ROWS
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).getBlocksForExtension(baseX + Settings.getAreaSizeX(), baseZ));

            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(ZoneXZ.getBaseX(x - 1, row - 1), ZoneXZ.getBaseZ(row - 1)));
            if (thisZone.getX() >= Settings.getSkinsLeft()) {
                extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(ZoneXZ.getBaseX(x, row - 1), ZoneXZ.getBaseZ(row - 1)));
            }
        } else {
            // EVEN ROWS
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).getBlocksForExtension(baseX, baseZ));

            if (row != 0 || (thisZone.getX() >= -Settings.getSkinsRight())) {
                extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(baseX, baseZ - Settings.getAreaSizeZ()));
            }
        }

        // CREATE SIDE-STREETS
        if (row == 0) {
            // ROW 0
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_CORNER).getBlocksForExtension(ZoneXZ.getBaseX(-Settings.getSkinsRight() - 1, row - 1), ZoneXZ.getBaseZ(row - 1)));
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_CORNER).getBlocksForExtension(EnumDirection.FLIP_X, ZoneXZ.getBaseX(Settings.getSkinsLeft(), row - 1), ZoneXZ.getBaseZ(row - 1)));
        }

        if (row % 2 != 0) {
            // UNEVEN ROWS
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).getBlocksForExtension(ZoneXZ.getBaseX(-Settings.getSkinsRight() - 1, row), ZoneXZ.getBaseZ(row)));
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).getBlocksForExtension(EnumDirection.FLIP_X, ZoneXZ.getBaseX(Settings.getSkinsLeft(), row), ZoneXZ.getBaseZ(row)));
            // FIX WRONG BLOCKS
            if (x == -Settings.getSkinsRightOld()) {
                int extendX = ZoneXZ.getBaseX(x, row - 1);
                int extendZ = ZoneXZ.getBaseZ(row);
                extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(extendX, extendZ));
            }
            if (x - 1 == Settings.getSkinsLeftOld()) {
                extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).getBlocksForExtension(ZoneXZ.getBaseX(x - 1, row - 1), ZoneXZ.getBaseZ(row)));
            }

        } else {
            // EVEN ROWS
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).getBlocksForExtension(ZoneXZ.getBaseX(-Settings.getSkinsRight() - 1, row), ZoneXZ.getBaseZ(row)));
            extendThread.addBlockList(this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).getBlocksForExtension(EnumDirection.FLIP_X, ZoneXZ.getBaseX(Settings.getSkinsLeft() + 1, row), ZoneXZ.getBaseZ(row)));
        }
    }

    public void createRowStructures(int row) {
        for (int x = -Settings.getSkinsRight() + (row % 2 == 0 ? 0 : 1); x <= Settings.getSkinsLeft(); x++) {
            if (row % 2 != 0) {
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, row - 1, blockThread);
                this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x - 1, row - 1, blockThread);
            } else {
                if (x > -Settings.getSkinsRight()) {
                    this.structureManager.getStructure(EnumStructures.ZONE_STREETS_BACK).createStructure(x, row - 1, blockThread);
                }
            }
            this.structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).createStructure(x, row, blockThread);
        }

        if (row == 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_CORNER).createStructure(-Settings.getSkinsRight(), row - 1, blockThread);
            this.structureManager.getStructure(EnumStructures.STREETS_CORNER).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, row - 1, blockThread);

            this.structureManager.getStructure(EnumStructures.INFO_WALL_1).createStructure(1, row - 1, blockThread);
            this.structureManager.getStructure(EnumStructures.INFO_WALL_2).createStructure(0, row - 1, blockThread);

        } else if (row % 2 != 0) {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(-Settings.getSkinsRight() - 1, row - 1, blockThread);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_1).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, row - 1, blockThread);
        } else {
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(-Settings.getSkinsRight(), row - 1, blockThread);
            this.structureManager.getStructure(EnumStructures.STREETS_SIDE_2).createStructure(EnumDirection.FLIP_X, Settings.getSkinsLeft() + 1, row - 1, blockThread);
        }
    }

    // ////////////////////////////////////////////////
    //
    // PERSISTENCE
    //
    // ////////////////////////////////////////////////

    private void loadAreas() {
        List<SkinArea> loadedAreas = databaseManager.loadZones();
        for (SkinArea thisArea : loadedAreas) {
            if (thisArea.getAreaOwner().equalsIgnoreCase(""))
                this.unusedAreaList.put(thisArea.getZoneXZ().toString(), thisArea);
            else
                this.usedAreaList.put(thisArea.getZoneXZ().toString(), thisArea);

        }
        TextUtils.logInfo(this.unusedAreaList.size() + " unused Areas loaded.");
        TextUtils.logInfo(this.usedAreaList.size() + " used Areas loaded.");
    }

    private void saveArea(SkinArea thisArea) {
        this.databaseManager.saveZone(thisArea);
    }

    private void updateAreaOwner(SkinArea thisArea) {
        this.databaseManager.updateAreaOwner(thisArea);
    }

    // ////////////////////////////////////////////////
    //
    // BLOCK AREAS
    //
    // ////////////////////////////////////////////////

    public void blockArea(ZoneXZ thisZone) {
        this.blockArea(thisZone.toString());
    }

    public void unblockArea(ZoneXZ thisZone) {
        this.unblockArea(thisZone.toString());
    }

    public boolean isAreaBlocked(ZoneXZ thisZone) {
        return this.isAreaBlocked(thisZone.toString());
    }

    public void blockArea(String zoneString) {
        this.blockedAreas.add(zoneString);
    }

    public void unblockArea(String zoneString) {
        this.blockedAreas.remove(zoneString);
    }

    public boolean isAreaBlocked(String zoneString) {
        return this.blockedAreas.contains(zoneString);
    }

    // ////////////////////////////////////////////////
    //
    // MODIFY & GET AREAS
    //
    // ////////////////////////////////////////////////

    public boolean createUnusedArea(SkinArea skinArea, boolean createStructures) {
        if (this.containsUnusedArea(skinArea.getZoneXZ()))
            return false;
        // UPDATE DATABASE
        if (!this.usedAreaList.containsKey(skinArea.getZoneXZ().toString())) {
            this.saveArea(skinArea);
        } else {
            this.updateAreaOwner(skinArea);
        }
        // UPDATE LISTS
        this.unusedAreaList.put(skinArea.getZoneXZ().toString(), skinArea);
        this.usedAreaList.remove(skinArea.getZoneXZ().toString());
        if (createStructures) {
            this.structureManager.getStructure(EnumStructures.ZONE_STREETS_AND_SOCKET).createStructure(skinArea.getZoneXZ().getX(), skinArea.getZoneXZ().getZ(), blockThread);
        }

        return true;
    }

    public SkinArea getUnusedArea(ZoneXZ thisZone) {
        return this.unusedAreaList.get(thisZone.toString());
    }

    public boolean containsUnusedArea(ZoneXZ thisZone) {
        return this.containsUnusedArea(thisZone.toString());
    }

    public boolean containsUnusedArea(String coordinateString) {
        return this.unusedAreaList.containsKey(coordinateString);
    }

    public int getUsedAreaCount() {
        return usedAreaList.size();
    }

    // ///////////////////////////
    // DELETE AREA
    // ///////////////////////////

    public void deletePlayerArea(SkinArea thisArea, Player player) {
        // BLOCK AREA
        this.blockArea(thisArea.getZoneXZ());
        // CREATE THREAD AND START IT
        World world = Bukkit.getWorlds().get(0);
        ZoneXZ thisZone = thisArea.getZoneXZ();
        AreaDeletionThread thisThread = new AreaDeletionThread(world, thisZone.getX() * Settings.getAreaSizeX() - (thisZone.getZ() % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0), thisZone.getZ() * Settings.getAreaSizeZ(), thisZone, player.getName());
        thisThread.initTask(Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getInstance(), thisThread, 0, Settings.getTicksBetweenReplace()));
    }

    // ///////////////////////////
    // USED AREAS
    // ///////////////////////////

    public boolean hasPlayerArea(Player player) {
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.isAreaOwner(player)) {
                return true;
            }
        }
        return false;
    }

    public SkinArea getPlayerArea(Player player) {
        return this.getPlayerArea(player.getName());
    }

    public SkinArea getPlayerArea(String playerName) {
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.isAreaOwner(playerName)) {
                return thisArea;
            }
        }

        // SEARCH FOR THE BEGINNING OF THE NAME
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.getAreaOwner().toLowerCase().startsWith(playerName.toLowerCase())) {
                return thisArea;
            }
        }

        return null;
    }

    public SkinArea getExactPlayerArea(String playerName) {
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.isAreaOwner(playerName)) {
                return thisArea;
            }
        }
        return null;
    }

    public SkinArea getPlayerArea(ZoneXZ thisZone) {
        return this.usedAreaList.get(thisZone.toString());
    }

    public boolean containsPlayerArea(ZoneXZ thisZone) {
        return this.containsPlayerArea(thisZone.toString());
    }

    public boolean containsPlayerArea(String coordinateString) {
        return this.usedAreaList.containsKey(coordinateString);
    }

    public boolean createPlayerArea(SkinArea skinArea, boolean createStructures, Player player) {
        if (this.containsPlayerArea(skinArea.getZoneXZ()))
            return false;

        // UPDATE DATABASE
        if (!this.unusedAreaList.containsKey(skinArea.getZoneXZ().toString())) {
            this.saveArea(skinArea);
        } else {
            this.updateAreaOwner(skinArea);
        }

        // UPDATE LISTS
        this.usedAreaList.put(skinArea.getZoneXZ().toString(), skinArea);
        this.unusedAreaList.remove(skinArea.getZoneXZ().toString());
        if (createStructures) {
            this.structureManager.getStructure(EnumStructures.ZONE_STEVE).createStructureWithSign(skinArea.getZoneXZ().getX(), skinArea.getZoneXZ().getZ(), player, blockThread);
            this.createPlayerSign(player, skinArea.getZoneXZ());
        }

        this.checkForZoneExtension();
        return true;
    }

    public void createPlayerSign(Player player, ZoneXZ thisZone) {
        World world = Bukkit.getWorlds().get(0);

        // PLACE SIGN
        int x = thisZone.getX() * Settings.getAreaSizeX() - (thisZone.getZ() % 2 != 0 ? (Settings.getAreaSizeX() >> 1) : 0) + (Settings.getAreaSizeX() >> 1);
        int z = thisZone.getZ() * Settings.getAreaSizeZ() + 12;
        world.getBlockAt(x, Settings.getMinimumBuildY() - 1, z).setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 0x2, true);

        // UPDATE LINE
        Sign signBlock = (Sign) (world.getBlockAt(x, Settings.getMinimumBuildY() - 1, z).getState());
        String[] playerName = new String[3];

        signBlock.setLine(0, "Built by:");
        playerName[0] = player.getName();
        playerName[1] = "";
        playerName[2] = "";
        if (player.getName().length() > 15) {
            playerName[0] = player.getName().substring(0, 15);
            if (player.getName().length() > 30) {
                playerName[1] = player.getName().substring(15, 30);
                if (player.getName().length() > 45) {
                    playerName[2] = player.getName().substring(30, 45);
                } else {
                    playerName[2] = player.getName().substring(30);
                }
            } else {
                playerName[1] = player.getName().substring(15);
            }
        }

        signBlock.setLine(1, playerName[0]);
        signBlock.setLine(2, playerName[1]);
        signBlock.setLine(3, playerName[2]);
        signBlock.update(true);

        // SEND UPDATE => NEED HELP OF ORIGINAL MC-SERVERSOFTWARE
        CraftPlayer cPlayer = (CraftPlayer) player;
        Packet130UpdateSign signPacket = null;
        signPacket = new Packet130UpdateSign(signBlock.getX(), signBlock.getY(), signBlock.getZ(), signBlock.getLines());
        cPlayer.getHandle().netServerHandler.sendPacket(signPacket);
    }

    public boolean removePlayerArea(ZoneXZ thisZone) {
        if (!this.containsPlayerArea(thisZone.toString()))
            return false;

        this.unusedAreaList.put(thisZone.toString(), new SkinArea(thisZone.getX(), thisZone.getZ(), ""));
        this.usedAreaList.remove(thisZone.toString());
        this.databaseManager.deleteAreaOwner(thisZone);
        return true;
    }

    public boolean removePlayerArea(SkinArea thisArea) {
        return this.removePlayerArea(thisArea.getZoneXZ());
    }

    public boolean removePlayerArea(Player player) {
        SkinArea toDelete = null;
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.isAreaOwner(player)) {
                toDelete = thisArea;
                break;
            }
        }
        // DELETE IF FOUND
        if (toDelete != null)
            return this.removePlayerArea(toDelete);
        return false;
    }

    // ////////////////////////////////////////////////
    //
    // IS IN AREA
    //
    // ////////////////////////////////////////////////

    public boolean isInArea(Player player, Location location) {
        return this.isInArea(player, location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public boolean isInArea(Player player, Block block) {
        return this.isInArea(player, block.getX(), block.getY(), block.getZ());
    }

    public boolean isInArea(Player player, int x, int y, int z) {
        if (y < Settings.getMinimumBuildY() || y > Settings.getMaximumBuildY())
            return false;

        ZoneXZ thisZone = ZoneXZ.fromPoint(x, z);
        SkinArea thisArea = this.getPlayerArea(thisZone);
        if (thisArea == null) {
            return false;
        }
        return thisArea.isAreaOwner(player);
    }

    public void checkForZoneExtension() {
        if (this.unusedAreaList.size() <= (Settings.getSkinsLeft() + Settings.getSkinsRight()) * Settings.getCreateRowsAtOnce()) {
            while (true) {
                if (this.unusedAreaList.containsKey(lastRow + ":0") || this.usedAreaList.containsKey(lastRow + ":0")) {
                    ++lastRow;
                    this.worldManager.setMaxZ((lastRow + 1) * (Settings.getAreaSizeZ() + 1));
                    continue;
                } else {
                    for (int i = 0; i < Settings.getCreateRowsAtOnce(); i++) {
                        this.createRow(lastRow + i);
                    }
                    this.worldManager.setMaxZ((lastRow + Settings.getCreateRowsAtOnce()) * (Settings.getAreaSizeZ()));
                    return;
                }
            }
        }
    }

    private void initMaximumZ() {
        int i = 0;
        while (true) {
            if (this.unusedAreaList.containsKey(i + ":0") || this.usedAreaList.containsKey(i + ":0")) {
                ++i;
                this.worldManager.setMaxZ((i + 1) * (Settings.getAreaSizeZ() + 1));
                continue;
            } else {
                return;
            }
        }
    }

    public SkinArea getRandomUnusedArea() {
        ZoneXZ thisZone;
        int minX = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        for (SkinArea thisArea : this.unusedAreaList.values()) {
            thisZone = thisArea.getZoneXZ();
            if (!this.isAreaBlocked(thisZone)) {
                if (thisZone.getZ() <= minZ) {
                    if (thisZone.getZ() < minZ)
                        minX = Integer.MAX_VALUE;
                    minZ = thisZone.getZ();
                    if (thisZone.getX() < minX)
                        minX = thisZone.getX();
                }
            }
        }

        if (this.unusedAreaList.containsKey(minZ + ":" + minX))
            return this.unusedAreaList.get(minZ + ":" + minX);
        else
            return (SkinArea) this.unusedAreaList.values().toArray()[0];
    }

    /**
     * @return the lastRow
     */
    public int getLastRow() {
        int last = Integer.MIN_VALUE;

        // UNUSED AREAS
        for (SkinArea thisArea : this.unusedAreaList.values()) {
            if (thisArea.getZoneXZ().getZ() > last)
                last = thisArea.getZoneXZ().getZ();
        }

        // USED AREAS
        for (SkinArea thisArea : this.usedAreaList.values()) {
            if (thisArea.getZoneXZ().getZ() > last)
                last = thisArea.getZoneXZ().getZ();
        }

        return last;
    }
}
package info.staticfree.android.twentyfourhour;
/*
 * Copyright (C) 2011 Steve Pomeroy <steve@staticfree.info>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Some portions Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * A widget that displays the time as a 12-at-the-top 24 hour analog clock. By
 * default, it will show the current time in the current timezone. The displayed
 * time can be set using {@link #setTime(long)} and and
 * {@link #setTimezone(TimeZone)}.
 *
 * @author <a href="mailto:steve@staticfree.info">Steve Pomeroy</a>
 *
 */
public class Analog24HClock extends View {

	private boolean mShowNow = true;
	private boolean mShowSeconds = true;

	private static final int UPDATE_INTERVAL = 1000 * 15;

	private Calendar mCalendar;
	private Drawable mFace;
	private Drawable mHour;
	private Drawable mMinute;

	private int mDialWidth;
	private int mDialHeight;

	private float mHourRot;
	private float mMinRot;

	private boolean mKeepon = false;
	private int mBottom;
	private int mTop;
	private int mLeft;
	private int mRight;
	private boolean mSizeChanged;
	private boolean mUseLargeFace = false;

	private final ArrayList<DialOverlay> mDialOverlay = new ArrayList<DialOverlay>();

	public Analog24HClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Analog24HClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Analog24HClock(Context context) {
		super(context);

		init();
	}

	private void init() {
		final Resources r = getResources();
		mFace = r.getDrawable(mUseLargeFace ? R.drawable.clock_face_large : R.drawable.clock_face);
		mHour = r.getDrawable(mUseLargeFace ? R.drawable.hour_hand_large : R.drawable.hour_hand);
		mMinute = r.getDrawable(mUseLargeFace ? R.drawable.minute_hand_large
				: R.drawable.minute_hand);

		mCalendar = Calendar.getInstance();

		mDialHeight = mFace.getIntrinsicHeight();
		mDialWidth = mFace.getIntrinsicWidth();
	}

	/**
	 * Sets the currently displayed time in {@link System#currentTimeMillis()}
	 * time. This will clear {@link #setShowNow(boolean)}.
	 *
	 * @param time
	 *            the time to display on the clock
	 */
	public void setTime(long time) {
		mShowNow = false;
		mCalendar.setTimeInMillis(time);

		updateHands();
		invalidate();
	}

	/**
	 * When set, the current time in the current timezone will be displayed.
	 *
	 * @param showNow
	 */
	public void setShowNow(boolean showNow) {
		mShowNow = showNow;
	}

	/**
	 * When set, the minute hand will move slightly based on the current number
	 * of seconds. If false, the minute hand will snap to the minute ticks.
	 * Note: there is no second hand, this only affects the minute hand.
	 *
	 * @param showSeconds
	 */
	public void setShowSeconds(boolean showSeconds) {
		mShowSeconds = showSeconds;
	}

	/**
	 * Sets the timezone to use when displaying the time.
	 *
	 * @param timezone
	 */
	public void setTimezone(TimeZone timezone) {
		mCalendar = Calendar.getInstance(timezone);
	}

	@Override
	protected void onAttachedToWindow() {
		mKeepon = true;
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		mKeepon = false;
		super.onDetachedFromWindow();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		final boolean prevUseLargeFace = mUseLargeFace;

		mUseLargeFace = w > mDialWidth || h > mDialHeight;

		// reinitialize if we need to switch face images
		if (prevUseLargeFace != mUseLargeFace) {
			init();
		}

		mSizeChanged = true;
	}

	// some parts from AnalogClock.java
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		final boolean sizeChanged = mSizeChanged;
		mSizeChanged = false;

		if (mShowNow) {
			mCalendar.setTimeInMillis(System.currentTimeMillis());

			updateHands();

			if (mKeepon) {
				postInvalidateDelayed(UPDATE_INTERVAL);
			}
		}

		final int availW = mRight - mLeft;
		final int availH = mBottom - mTop;

		final int cX = availW / 2;
		final int cY = availH / 2;

		int w = mDialWidth;
		int h = mDialHeight;

		boolean scaled = false;

		if (availW < w || availH < h) {
			scaled = true;
			final float scale = Math.min((float) availW / (float) w,
					(float) availH / (float) h);
			canvas.save();
			canvas.scale(scale, scale, cX, cY);
		}

		if (sizeChanged) {
			mFace.setBounds(cX - (w / 2), cY - (h / 2), cX + (w / 2), cY
					+ (h / 2));
		}

		mFace.draw(canvas);

		for (final DialOverlay overlay : mDialOverlay){
			overlay.onDraw(canvas, cX, cY, w, h, mCalendar);
		}

		canvas.save();
		canvas.rotate(mHourRot, cX, cY);

		if (sizeChanged) {
			w = mHour.getIntrinsicWidth();
			h = mHour.getIntrinsicHeight();
			mHour.setBounds(cX - (w / 2), cY - (h / 2), cX + (w / 2), cY
					+ (h / 2));
		}
		mHour.draw(canvas);
		canvas.restore();

		canvas.save();
		canvas.rotate(mMinRot, cX, cY);

		if (sizeChanged) {
			w = mMinute.getIntrinsicWidth();
			h = mMinute.getIntrinsicHeight();
			mMinute.setBounds(cX - (w / 2), cY - (h / 2), cX + (w / 2), cY
					+ (h / 2));
		}
		mMinute.draw(canvas);
		canvas.restore();

		if (scaled) {
			canvas.restore();
		}
	}

	// from AnalogClock.java
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		float hScale = 1.0f;
		float vScale = 1.0f;

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
			hScale = (float) widthSize / (float) mDialWidth;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
			vScale = (float) heightSize / (float) mDialHeight;
		}

		final float scale = Math.min(hScale, vScale);

		setMeasuredDimension(
				getDefaultSize((int) (mDialWidth * scale), widthMeasureSpec),
				getDefaultSize((int) (mDialHeight * scale), heightMeasureSpec));
	}

	@Override
	protected int getSuggestedMinimumHeight() {
		return mDialHeight;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		return mDialWidth;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		// because we don't have access to the actual protected fields
		mRight = right;
		mLeft = left;
		mTop = top;
		mBottom = bottom;
	}

	private void updateHands() {


		final int h = mCalendar.get(Calendar.HOUR_OF_DAY);
		final int m = mCalendar.get(Calendar.MINUTE);
		final int s = mCalendar.get(Calendar.SECOND);

		mHourRot = getHourHandAngle(h, m);
		mMinRot = (m / 60.0f) * 360
				+ (mShowSeconds ? ((s / 60.0f) * 360 / 60.0f) : 0);
	}

	public static float getHourHandAngle(int h, int m){
		return ((12 + h) / 24.0f * 360) % 360 + (m / 60.0f) * 360 / 24.0f;
	}

	public void addDialOverlay (DialOverlay dialOverlay){
		mDialOverlay.add(dialOverlay);
	}

	public void removeDialOverlay (DialOverlay dialOverlay){
		mDialOverlay.remove(dialOverlay);
	}

	public interface DialOverlay {

		public abstract void onDraw(Canvas canvas, int cX, int cY, int w, int h, Calendar calendar);

	}
}
package Client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import java.awt.List;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;

public class ChatClient2 extends JApplet implements Runnable 
{
	/* Constants
	private final int SIZE_OF_HAND = TOTAL_CARDS/NO_OF_PLAYERS;
	private final String [] names = {"North", "East", "South", "West"};
	*/
	private final int TOTAL_RANKS = 13, TOTAL_SUITS= 4, NO_OF_CARD_BACKS= 2;
	static String Thread_Switcher = "kicker";
	
	private final int card_spacing = 25, dist = 30, margin = 12;
	private final int extra_width = 300, frame_width = 1024, frame_height = 800, frame_height_actual = 768;
	private final int button_width = 100, button_height = 30;
	private final int new_card_width = 150, new_card_height = 215;
	//private final int card_width = new_card_width, card_height = new_card_height;
	private final int card_width = 72, card_height = 96;
	private final int player_position = 2; // Default is South position
	private final String directory = "images/";
	//network
	Socket sock;
	DataInputStream dis;
	PrintStream ps;
	Thread kicker = null;

	Socket sock2;
	ObjectOutputStream os;
	ObjectInputStream is;
	Thread finisher = null;

	private int TOTAL_CARDS = 0, NO_OF_PLAYERS = 0, SIZE_OF_HAND = 0;  
	private String [] names = null;
	private int player_id = -1;
	private int pos = -1;
	private int trumpCardPos;
	private int whoHasSetTrump = -1;
	private int [][]pos_change = null;
	private int []init_array =null;
	private int []card_array = null;


	// JAPPLET Stuff....
	JPanel p1, p2;
	private JLabel[] cur_hand;
	private JLabel[] player_label;
	private JLabel[][] handLbl; 
	private JLabel background, scoreLbl, background2, trump_label;
	JTextField inputField;
	JTextArea outputArea;
	JButton B1,B2,B3,B4,B5,B6;
	JButton [] bidButtons = new JButton[7];
	JScrollPane scrollPane;
	
	ImageIcon[]Image_Icons = new ImageIcon[52];
	ImageIcon[]card_backs = new ImageIcon[NO_OF_CARD_BACKS];
	ImageIcon card_back;
	ImageIcon trumpImageIcon;

	String name, theHost;
	int thePort;
	private final SymAction lSymAction = new SymAction();

	public boolean setTrumpAction;

	public void init() 
	{	
		try {
		/* first, assign a BorderLayout and add the two Panels */
		p2 = new JPanel();
		p2.setLayout(null);
		p2.setBounds(frame_width,0,extra_width,frame_height_actual);
		//p2.setBackground(java.awt.Color.black);
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBounds(0,0,frame_width,frame_height_actual);
		setSize(frame_width+extra_width,frame_height_actual);
		add(p1);
		add(p2);
		
		setTrumpAction = false;
		String temp;
		temp = getParameter("host");
		if( temp == null)
			theHost = "localhost";
		else
			theHost = temp;
		temp = getParameter("port");

		try 
		{
			thePort = Integer.valueOf(temp).intValue();
		}
		catch(Exception e)
		{
			thePort = 31123;
		}
		
		loadImageIcons();

		setPanel2();
		register_buttons();
		
		setBackground();
		
		}
		catch (NullPointerException e)
		{
			
		}
	}

	public void run() {
		
		int count = 0;
		if (Thread_Switcher.equals("finisher"))
		{
			run2();
			return;	
		}
		while (sock == null && kicker != null )
		{
			try
			{
				sock = new Socket(theHost,thePort);
				dis = new DataInputStream( sock.getInputStream() );
				ps = new PrintStream( sock.getOutputStream() );		
			}
			catch (Exception e)
			{
				System.out.println("Unable to contact host.");
				if (count < 10)
					outputArea.append("Unable to contact host. Retrying...\n" + e);
				sock = null;
				sleep_time(5000);
			}
			sleep_time(100);
			count++;
		}

		output("login||"+name);
		outputArea.append("Logged in to server successfully.\n");
		B2.setEnabled(true);
		B1.setEnabled(false);
		B5.setEnabled(true);
		inputField.addActionListener(lSymAction);
		//setIconsJLabels();
		while (sock != null && dis != null && kicker != null) 
		{
			try 
			{
				String str = dis.readLine();
				System.out.println("Got: "+str);
				if(str != null) 
					if(str.indexOf("||") != -1)
					{
						StringTokenizer st = new StringTokenizer(str,"||");
						String cmd = st.nextToken();
						String val = st.nextToken();
						if(cmd.equals("list"))
						{ 
						}
						else
							if(cmd.equals("logout")) 
							{
								outputArea.append(val+"\n");
								validate();
							}
							else
								if(cmd.equals("login")) 
								{
									outputArea.append(st.nextToken()+"\n");
								}
								else
									outputArea.append( val + "\n" );
					}
					else
						outputArea.append(str + "\n");
			}
			catch (IOException e)
			{
				System.out.println("Connection lost.");
				kickerstop();
				kicker.stop();
			}
		}	
	}

	public void run2()
	{
		int count = 0;	
		while (sock2 == null && finisher != null )
		{
			try
			{
				sock2 = new Socket(theHost, thePort+1);
				os = new ObjectOutputStream(sock2.getOutputStream());
				os.flush();
				is=new ObjectInputStream(sock2.getInputStream());		
			}
			catch (Exception e)
			{
				System.out.println("not able to connect to the game");
				if (count < 10)
					outputArea.append("not able to connect to the game.\n" + e);
				sock2 = null;
				sleep_time(5000);
			}
			sleep_time(100);
			count++;
		}
		
		outputArea.append("Logged in to game successfully.\n");
		try {

			os.writeObject(name);	
			os.flush();
			while (sock2 != null && is != null && finisher != null) 
			{
				String str = (String) is.readObject();
				System.out.println("Got: "+str);
				if(str != null) 
				{
					if(str.equals("init"))
					{
						init_array = (int [])is.readObject();
						outputArea.append("Received init " + init_array[0]+" "+init_array[1]+" "+init_array[2]+" "+"\n");
						String [] temp = (String [])is.readObject();
						outputArea.append("Received name" +temp[0]+temp[1]+temp[2]+temp[3]+"\n");
						NO_OF_PLAYERS=init_array[0];
						TOTAL_CARDS=init_array[1];
						player_id=init_array[2];
						SIZE_OF_HAND = TOTAL_CARDS/NO_OF_PLAYERS;
						card_array = new int[SIZE_OF_HAND];
						assign_names(temp);
						initJLabels();
					}
					else if(str.equals("cards"))
					{
						int [] temp = (int [])is.readObject();
						//outputArea.append("\nReceived cards"); 
						for (int i = 0; i < SIZE_OF_HAND; i++)
						{
							card_array[i] = temp[i];
						//	outputArea.append(" "+card_array[i]);
						}
						setIconsJLabels();

					}
					else if(str.equals("first"))
					{
						setIconsJLabels_player(card_array, 1);
					}
					else if(str.equals("second"))
					{
						setIconsJLabels_player(card_array, 2);
					}
					else if(str.equals("bid"))
					{
						B3.setEnabled(true);
						B4.setEnabled(true);
						//Put sound here	
					}
					else if(str.equals("pos"))
					{
						int cur_player= (Integer)is.readObject();
						int pos = (Integer)is.readObject();
						int idx = (Integer)is.readObject();
						after_other_players_card(cur_player, pos, idx);
						/*String done = "done";
						os.writeObject(done);
						os.flush();*/
					}
					else if(str.equals("play"))
					{
						add_mouse_player();	
						B3.setEnabled(false);
						B4.setEnabled(false);
						if (whoHasSetTrump == player_id)
							B6.setEnabled(true);
						//play sound here too
					}
					else if(str.equals("valid"))
					{
						int valid = (Integer)is.readObject();
						if (valid == 1)
						{
							after_my_card();
						}
					}
					else if(str.equals("round"))
					{
						removeIconCurrentHand();
						int score = (Integer)is.readObject();
						scoreLbl.setText("Round:"+score);
					}
					else if(str.equals("trumpset"))
					{
						int cur_player= (Integer)is.readObject();
						int pos = (Integer)is.readObject();
						setTrumpLabel(cur_player, pos);
					}
					else if (str.equals("settrump"))
					{
						setTrumpAction = true;
						add_mouse_player();	
					}
					else if(str.equals("game"))
					{
						int score = (Integer)is.readObject();
						scoreLbl.setText("Game:"+score);
						add_remove_background(false);
						add_JLabels_to_pane();
						setIconsJLabels();
						add_remove_background(true);
						p1.repaint();
					}
				}
			}	
		}
		catch (Exception e)
		{
			System.out.println("Connection lost.");
			finisherstop();
			//finisher.stop();	
		}
	}

	public void stop() 
	{
		output("logout||"+name);
		try
		{
			dis.close();
			ps.close();
			sock.close();
			is.close();
			os.close();
			sock2.close();
		}
		catch (Exception e)
		{
		}
		sock = null;
		sock2 = null;
		outputArea.append("Logged out from server.\n");
		kicker = null;
		finisher = null;
	}

	public void kickerstop()
	{
		output("logout||"+name);
		try
		{
			dis.close();
			ps.close();
			sock.close();
		}
		catch (Exception e)
		{
		}
		sock = null;
		outputArea.append("Logged out from server.\n");
		kicker = null;

		/* reset our affected GUI components */
		B2.setEnabled(false);
		B1.setEnabled(true);
		inputField.setText(name);
		inputField.addMouseListener(lSymAction);
		inputField.removeActionListener(lSymAction);
		p2.layout();
	}	
	

	public void finisherstop()
	{
		try
		{
			is.close();
			os.close();
			sock2.close();
		}
		catch (Exception e)
		{
		}
		sock2 = null;
		outputArea.append("Disconnected from game.\n");

		removeIconsJLabels();
		finisher = null;
	}

	public boolean output(String str) 
	{
		try
		{
			ps.println(str);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	class SymAction implements java.awt.event.ActionListener, MouseListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == B1)
				login_actionPerformed(event);
			else if (object == B2)
				logout_actionPerformed(event);
			else if (object == B3)
				bid_actionPerformed(event);
			else if (object == B4)
				pass_actionPerformed(event);
			else if (object == B5)
				iamready_actionPerformed(event);
			else if (object == B6)
				reveal_trump_actionPerformed(event);
			else if (object == inputField)
				say_actionPerformed(event);
				
		}
		public void mouseClicked(MouseEvent e) 
		{
			Object object = e.getSource();
			if (object == inputField)
			{
				inputField.setText("");
				inputField.removeMouseListener(lSymAction);
				return;
			}
			if (object == trump_label)
			{
				if ((ImageIcon)trump_label.getIcon() == card_back)
					trump_label.setIcon(trumpImageIcon);
				else
					trump_label.setIcon(card_back);
				return;
			}
			/*if (object == background)
			{
				after_one_round();	
				if (game_over == true)
				{
					after_one_game();
					getContentPane().remove(background);
					add_JLabels_to_pane();
					getContentPane().add(background);
					getContentPane().repaint();
					
				}
				remove_mouse_event(background);
				scoreLbl.setText(null);
				return;
			}
			*/
			pos = getIndexOfCardPressed(object);
			if ( pos >= 0 )
			{
				sendClickedCard(pos);
				if (setTrumpAction)
				{
					whoHasSetTrump = player_id;
					trumpCardPos = pos;
					setTrumpLabel(player_id, pos);
					setTrumpAction = false;
					add_mouse_event(trump_label);
					remove_mouse_player();
				}
			}
		}	
		/* Ignore all  the following events	*/
		public void mouseExited(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved   (MouseEvent e) {} 
		public void mouseEntered(MouseEvent e) {}  
		public void mouseReleased(MouseEvent e) {}  
		public void mousePressed(MouseEvent e) {}  

	}

	public boolean handleEvent(Event evt) 
	{
		/* if the Event is not one of the ones we can handle, we should pass it along the chain-of-command to our super-class */
		return super.handleEvent(evt);
	}
	
	void register_buttons()
	{
		B1.addActionListener(lSymAction);
		B2.addActionListener(lSymAction);
		B3.addActionListener(lSymAction);
		B4.addActionListener(lSymAction);
		B5.addActionListener(lSymAction);
		B6.addActionListener(lSymAction);
	}

	public boolean login_actionPerformed(java.awt.event.ActionEvent event)	
	{
		Thread_Switcher = "kicker";
		outputArea.append("Logging in...\n");
		name = inputField.getText(); /* find out what alias the user wants to use */
		inputField.setText("");

		/* create a new Thread and start it */
		kicker = new Thread(this);
		kicker.start();
		return true;
	}

	public boolean logout_actionPerformed(java.awt.event.ActionEvent event)	
	{
		outputArea.append("Logging out...\n");
		/* stop the Thread (which will disconnect) */
		kickerstop();
		kicker.stop();
		return true;
	}
	
	public boolean say_actionPerformed(java.awt.event.ActionEvent event)
	{
		if (inputField.getText() != null)
		{
			output("say||"+inputField.getText());
			inputField.selectAll();
			inputField.setText("");
		}
		return true;
	}

	public boolean bid_actionPerformed(java.awt.event.ActionEvent event)
	{
		try 
		{
			int bid = 14;
			if (inputField.getText() != null)
			{
				String temp = inputField.getText();
				bid = Integer.parseInt(temp);
				inputField.setText("");
			}
			if (bid < 14 && bid > 28)
			{
				bid = 14;	
			}
			os.writeObject(bid);	
			os.flush();
		}	
		catch(Exception e)
		{

		}
		
		B3.setEnabled(false);
		B4.setEnabled(false);
		return true;
	}

	public boolean pass_actionPerformed(java.awt.event.ActionEvent event)
	{
		try 
		{
			int bid = 0;
			os.writeObject(bid);	
			os.flush();
		}	
		catch(Exception e)
		{

		}
		B3.setEnabled(false);
		B4.setEnabled(false);
		return true;
	}

	public boolean reveal_trump_actionPerformed(java.awt.event.ActionEvent event)
	{
		try 
		{
			// Send the trump to all the players or request the trump to 
			//os.writeObject(bid);	
			//os.flush();
			reveal_trump();
		}	
		catch(Exception e)
		{

		}
		return true;
	}

	public boolean whisper_actionPerformed(java.awt.event.ActionEvent event)
	{
		outputArea.append("You whisper to "+name+ ": "+inputField.getText()+"\n");
		output("whisper||"+inputField.getText()+"||"+name );
		inputField.selectAll();
		return true;
	}

	public boolean iamready_actionPerformed(java.awt.event.ActionEvent event)
	{
		Thread_Switcher = "finisher";
		outputArea.append("Getting ready for the game\n");
		inputField.selectAll();
		outputArea.append("Iam Ready for the game\n");
		B5.setEnabled(false);
		finisher = new Thread(this);
		finisher.start();
		return true;
	}

	public void reveal_trump()
	{
		B6.setEnabled(false);
		handLbl[player_position][trumpCardPos].setIcon(trumpImageIcon);
		p1.add(handLbl[player_position][trumpCardPos]);
		repaint();
		add_mouse_event(handLbl[player_position][trumpCardPos]);
	}
	void setBackground()
	{
		//Set Background 
		background = new JLabel();
		p1.add(background);
		String imageFile = directory + "background.gif";
		ImageIcon backImage = new ImageIcon( getImage( getCodeBase(), imageFile ) );
		//URL imageURL = cldr.getResource(imageFile);
		//ImageIcon backImage = new ImageIcon(imageURL);
		background.setBounds(0,0,frame_width, frame_height_actual);
		background.setIcon(backImage);
	}

	void setPanel2()
	{
		/* next create the Field used for input.  For fun, make it 80 columns wide.  Add it to the south Panel */
		inputField = new JTextField(20);
		inputField.setBounds(frame_width,(frame_height_actual/2),extra_width ,30);
		Font font = new Font("Verdana",Font.ITALIC, 12);
		Font font2 = new Font("Verdana",Font.ITALIC, 6);
		inputField.setBackground(Color.LIGHT_GRAY);
		//inputField.setOpaque(false);
		inputField.setFont(font);
		inputField.setText("<Enter Name and Press Login>");
		inputField.addMouseListener(lSymAction);
		p2.add( inputField );

		/* create the output Area.  Make it 10 rows by 60 columns.  Add it to north Panel  don't let the user edit the contents, and make the background color Cyan - because it looks nice */
		outputArea = new JTextArea(10, 20);
		outputArea.setBounds(frame_width,((frame_height_actual/2) + 30),extra_width,((frame_height_actual/2) - 30));
		outputArea.setEditable(false);
		outputArea.setFont(font);
		outputArea.setOpaque(false);
		scrollPane = new JScrollPane(outputArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(frame_width,((frame_height_actual/2) + 30),extra_width,((frame_height_actual/2) - 30));
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque( false );
		p2.add(scrollPane);

		/* now for the Buttons.  Make the first Button to let the user "login" */
		B1 = new JButton("Chat");
		int buttonPos = frame_width + (extra_width - (2 * button_width))/2;
		B1.setBounds(buttonPos, (frame_height_actual/6), button_width, button_height);  
		p2.add(B1);

		/* The second Button allows the user to "logout", but is initially disabled */
		B2 = new JButton("logout");
		B2.setBounds(buttonPos+button_width, (frame_height_actual/6), button_width, button_height);  
		p2.add(B2);
		B2.setEnabled(false);

		/* third Button to say Iam Ready */
		B3 = new JButton("bid");
		B3.setBounds(buttonPos, ((frame_height_actual/2) - (frame_height_actual/6) - button_height), button_width, button_height);  
		B3.setEnabled(false);
		p2.add(B3);
		
		/* Fourth Button to send message */
		B4 = new JButton("pass");
		B4.setBounds(buttonPos + button_width, ((frame_height_actual/2) - (frame_height_actual/6) - button_height), button_width, button_height);  
		B4.setEnabled(false);
		p2.add(B4);

		/* Fifth Button to join */
		B5 = new JButton("Join");
		B5.setBounds(buttonPos, ((frame_height_actual/2) - (frame_height_actual/12) - button_height/2), 2 * button_width, button_height);  
		B5.setEnabled(false);
		p2.add(B5);

		B6 = new JButton("Reveal");
		B6.setBounds(margin, margin+card_height+30, card_width, button_height);  
		B6.setEnabled(false);
		p2.add(B6);

		setBackground2();
	}
	
	void setBackground2()
	{
		//Set Background2 
		background2 = new JLabel();
		p2.add(background2);
		String imageFile = directory + "background2.gif";
		ImageIcon backImage = new ImageIcon( getImage( getCodeBase(), imageFile ) );
		//URL imageURL = cldr.getResource(imageFile);
		//ImageIcon backImage = new ImageIcon(imageURL);
		background2.setBounds(frame_width,0,extra_width,frame_height_actual);
		background2.setIcon(backImage);
	}
	
	void initJLabels()
	{
		add_remove_background(false);
		cur_hand = new JLabel[NO_OF_PLAYERS];
		player_label = new JLabel[NO_OF_PLAYERS];
		trump_label = new JLabel();
		handLbl = new JLabel[NO_OF_PLAYERS][ SIZE_OF_HAND ]; 
		int xPos = 0, yPos = 0;
		int plxPos = 0, plyPos = 0, plw =40, plh = 30;
		int chxPos = 0, chyPos = 0;
		
		trump_label.setOpaque(false);
		trump_label.setForeground(java.awt.Color.black);
		trump_label.setBounds(margin,margin,card_width,card_height + 20);
		trump_label.setIcon(null);
		trump_label.setText(null);
		p1.add(trump_label);

		for ( int k = 0 ; k < NO_OF_PLAYERS ;k++)
		{
			player_label[k] = new JLabel();
			cur_hand[k] = new JLabel();
			switch (k)
			{
				case 0:
					{
						xPos = ((frame_width-card_width)/2) +((SIZE_OF_HAND-1)/2 * card_spacing);
						yPos = margin;
						plxPos = xPos+(margin/2)+card_width;
						plyPos = yPos+(card_height/2) ;
						chxPos = (frame_width/2) - (card_width/2);
						chyPos = (frame_height_actual/2) - dist - card_height;
						break;
					}
				case 3:
					{
						xPos = margin;
						yPos = ((frame_height-card_height)/2) +((SIZE_OF_HAND-1)/2 * card_spacing);
						plxPos = xPos + (card_width/2);
						plyPos = yPos +(margin/2)+card_height;
						chxPos = (frame_width/2) - dist - card_width;
						chyPos = (frame_height/2) - (card_height/2);
						break;
					}
				case 2:
					{
						xPos = ((frame_width-card_width)/2) +((SIZE_OF_HAND-1)/2 * card_spacing);
						yPos = frame_height_actual - margin - card_height;
						plxPos = xPos+(margin/2)+card_width;
						plyPos = yPos+(card_height/2) ;
						chxPos = (frame_width/2) - (card_width/2);
						chyPos = (frame_height_actual/2) + (2 *dist);
						break;
					}
				case 1:
					{
						xPos = frame_width - margin - card_width;
						yPos = ((frame_height-card_height)/2) +((SIZE_OF_HAND-1)/2 * card_spacing);
						plxPos = xPos + (card_width/2);
						plyPos = yPos+(margin/2)+card_height;
						chxPos = (frame_width/2) + dist;
						chyPos = (frame_height/2) - (card_height/2);
						break;
					}
			}
			
			player_label[k].setForeground(java.awt.Color.black);
			player_label[k].setFont(new Font("Dialog", Font.BOLD, 10));
			player_label[k].setBounds(plxPos,plyPos,plw,plh);
			player_label[k].setText(null);
			p1.add(player_label[k]);

			cur_hand[k].setOpaque(false);
			cur_hand[k].setForeground(java.awt.Color.black);
			cur_hand[k].setBounds(chxPos,chyPos,card_width,card_height);
			cur_hand[k].setIcon(null);
			p1.add(cur_hand[k]);

			for ( int i = 0 ; i < SIZE_OF_HAND; i++ ) 
			{
				handLbl[k][i] = new JLabel();
				handLbl[k][i].setOpaque(false);
				handLbl[k][i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				handLbl[k][i].setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
				handLbl[k][i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				//handLbl[k][i].setText("Card");
				handLbl[k][i].setToolTipText("This is a card.");
				p1.add(handLbl[k][i]);
				handLbl[k][i].setForeground(java.awt.Color.black);
				handLbl[k][i].setFont(new Font("Dialog", Font.BOLD, 10));
				handLbl[k][i].setBounds(xPos,yPos,card_width,card_height);
				if (k % 2 == 0)
					xPos-=card_spacing;
				else
					yPos-=card_spacing;
			}
		}

		//Initiate Score Label
		scoreLbl = new JLabel();
		scoreLbl.setToolTipText("Sum of all cards");
		scoreLbl.setForeground(java.awt.Color.black);
		scoreLbl.setBounds(636,156,204,29);
		scoreLbl.setText(null);
		p1.add(scoreLbl);

		//setBackground();
		add_remove_background(true);
		p1.repaint();
	}

	void setIconsJLabels()
	{
		int temp = player_position;
		int temp2 = player_id;
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			for (int i = 0; i < SIZE_OF_HAND; i++)
			{
				if (k <2)
					handLbl[k][SIZE_OF_HAND - i - 1].setIcon(card_back);
				else
					handLbl[k][i].setIcon(card_back);
				//handLbl[k][i].setIcon(Image_Icons[k][i]);
				//p1.repaint();
				//sleep_time(15);
			}
			player_label[temp].setText(names[temp2]);
			temp++;
			temp= temp%NO_OF_PLAYERS;
			temp2++;
			temp2 = temp2 % NO_OF_PLAYERS;
		}
		p1.repaint();
	}
	
	void setTrumpLabel(int cur_player, int card_position)
	{
		int pos = (cur_player + player_position - player_id +NO_OF_PLAYERS) % NO_OF_PLAYERS;	
		trumpImageIcon = (ImageIcon)handLbl[pos][card_position].getIcon();
		trump_label.setIcon(card_back);
		trump_label.setText("Set by "+names[cur_player]);
		handLbl[pos][card_position].setIcon(null);
		p1.remove(handLbl[pos][card_position]);
		p1.repaint();

	}
	void setIconsJLabels_player(int [] cards, int option)
	{
		int k = player_position;
		int start, end;
		if (option == 1)	
		{
			start = 0;
			end = SIZE_OF_HAND/2;
		}
		else
		{
			start = SIZE_OF_HAND/2;
			end = SIZE_OF_HAND;

		}
		for (int i = start; i < end; i++)
		{
			handLbl[k][i].setIcon(Image_Icons[cards[i]]);
			p1.repaint();
			sleep_time(15);
		}
	}

	void removeIconsJLabels()
	{
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			for (int i = 0; i < SIZE_OF_HAND; i++)
			{
				handLbl[k][i].setIcon(null);
			}
		player_label[k].setText(null);

		}
		p1.repaint();
	}

	void loadImageIcons()
	{
		String cards = "a23456789tjqk";
		String suit = "cshd";
		for (int k = 0; k < 4; k++)
		{
			for (int i = 0; i < 13; i++)
			{
				String imageFile = directory + cards.charAt(i) + suit.charAt(k) +".gif";
				/*Toolkit toolkit = Toolkit.getDefaultToolkit();
				Image image = toolkit.getImage(imageFile);
				Image scaledImage = image.getScaledInstance(new_card_width, new_card_height, Image.SCALE_DEFAULT);   
				Image_Icons[k*13 +i]=new ImageIcon(scaledImage); */
				Image_Icons[k*13 +i] = new ImageIcon( getImage( getCodeBase(), imageFile ) );
			}

		}
		
		for (int j = 0; j< NO_OF_CARD_BACKS; j++)
		{
			String imageFile = directory + "cardback"+(j+1)+".gif";
			//String imageFile = directory + "chelcardback.gif";
			card_backs[j]= new ImageIcon( getImage( getCodeBase(), imageFile ) );
		}
		card_back = card_backs[0];
	}

	void assign_names(String [] temp)
	{
		names = new String[NO_OF_PLAYERS];
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			if ( k == player_id)	
			{
				names[k] = "You";	
			}
			else
				names[k] = temp[k];
		}
	}
	void sleep_time(int time)
	{
		try 
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			
		}
	}

	void add_remove_background(boolean choice)
	{
		if (choice == true)
			p1.add(background);	
		else
			p1.remove(background);	
	}

	// player
	void add_mouse_player()
	{
		for ( int i = 0; i < SIZE_OF_HAND; i++ ) 
		{
			handLbl[player_position][i].addMouseListener(lSymAction);
		}
	}

	void remove_mouse_player()
	{
		for ( int i = 0; i < SIZE_OF_HAND; i++ ) 
		{
			handLbl[player_position][i].removeMouseListener(lSymAction);
		}
	}

	int getIndexOfCardPressed(Object o1)
	{
		for ( int i = 0 ; i < SIZE_OF_HAND; i++ ) 
		{
			if (o1 == handLbl[player_position][i])
			{
				return i;
			}
		}	 			 
		return -1;
	}
	
	void after_other_players_card(int cur_player, int cardPos, int cardIndex)
	{
		int pos = (cur_player + +player_position - player_id +NO_OF_PLAYERS) % NO_OF_PLAYERS;	
		handLbl[pos][cardPos].setIcon(null);	
		p1.remove(handLbl[pos][cardPos]);	
		cur_hand[pos].setIcon(Image_Icons[cardIndex]);	
		p1.repaint();	
	}
	
	void sendClickedCard(int pos)
	{
		try 
		{
			os.writeObject(pos);
			os.flush();
		}
		catch (Exception e)
		{

		}
	}

	//Single Label
	void add_mouse_event(JLabel j1)
	{
		j1.addMouseListener(lSymAction);
	}

	void remove_mouse_event(JLabel j1)
	{
		j1.removeMouseListener(lSymAction);
	}

	void add_JLabels_to_pane()
	{  
		for ( int k = 0; k < NO_OF_PLAYERS; k++ ) 
		{
			for ( int i = 0; i < SIZE_OF_HAND; i++ ) 
			{
				p1.add(handLbl[k][i]);
			}
		}
	
	}

	void removeIconCurrentHand()
	{
		for ( int k = 0; k < NO_OF_PLAYERS; k++ ) 
		{
			cur_hand[k].setIcon(null);
		}

	}

	void after_my_card()
	{
		int k = player_position;
		int i = pos;
		if ( pos >=0 )
		{
			ImageIcon temp = (ImageIcon) handLbl[k][i].getIcon();
			handLbl[k][i].setIcon(null);
			remove_mouse_event(handLbl[k][i]);
			p1.remove(handLbl[k][i]);	
			cur_hand[k].setIcon(Image_Icons[card_array[pos]]);
			p1.repaint();	
		}
		remove_mouse_player();
	}
}
package Client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import java.awt.List;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;

public class ClientGUI extends JApplet implements Runnable 
{
	/* Constants
	private final int TOTAL_CARDS = 52, NO_OF_PLAYERS = 4;
	private final int SIZE_OF_HAND = TOTAL_CARDS/NO_OF_PLAYERS;
	private final String [] names = {"North", "East", "South", "West"};
	*/
	
	private final int card_spacing = 25, dist = 30, margin = 12;
	private final int extra_width = 300, frame_width = 1024, frame_height = 800, frame_height_actual = 768;
	private final int button_width = 100, button_height = 30;
	private final int card_width = 72, card_height = 96;
	private final String directory = "images/";

	private int TOTAL_CARDS = 0, NO_OF_PLAYERS = 0, SIZE_OF_HAND = 0;  
	private String [] names = null;
	private int player_position = -1;
	private int []card_array = null;
	private int [][]pos_change = null;
	private int []init_array =null;

	//network
	Socket sock;
	ObjectOutputStream os;
	ObjectInputStream is;
	Thread finisher = null;

	// JAPPLET Stuff....
	JPanel p1, p2;
	private JLabel[] cur_hand = new JLabel[NO_OF_PLAYERS];
	private JLabel[] player_label = new JLabel[NO_OF_PLAYERS];
	private JLabel[][] handLbl = new JLabel[NO_OF_PLAYERS][ SIZE_OF_HAND ]; 
	private JLabel background, scoreLbl, background2;
	JTextField inputField;
	JTextArea outputArea;
	JButton B1,B2,B3,B4;
	JScrollPane scrollPane;
	
	ImageIcon[][] Image_Icons = new ImageIcon[NO_OF_PLAYERS][SIZE_OF_HAND];
	ImageIcon card_back;
	
	String name, host;
	int port;

	ChatClient1  child;
	private final SymAction lSymAction = new SymAction();

	public void init() 
	{	
		try {
			/* first, assign a BorderLayout and add the two Panels */
			p2 = new JPanel();
			p2.setLayout(null);
			p2.setBounds(frame_width,0,extra_width,frame_height_actual);
			//p2.setBackground(java.awt.Color.black);
			p1 = new JPanel();
			p1.setLayout(null);
			p1.setBounds(0,0,frame_width,frame_height_actual);
			setSize(frame_width+extra_width,frame_height_actual);
			add(p1);
			add(p2);
			
			String temp;
			temp = getParameter("host");
			if( temp == null)
				host = "localhost";
			else
				host = temp;
			temp = getParameter("port");

			try 
			{
				port = Integer.valueOf(temp).intValue();
			}
			catch(Exception e)
			{
				port = 31123;
			}
			
			loadImageIcons();

			setPanel2();
			register_buttons();
			
			initJLabels();

			setBackground();
			
		}
		catch (NullPointerException e)
		{
			
		}
	}

	public void run() 
	{
		int count = 0;	
		while (sock == null && finisher != null )
		{
			try
			{
				sock = new Socket(host, port+1);
				os = new ObjectOutputStream(sock.getOutputStream());
				os.flush();
				is=new ObjectInputStream(sock.getInputStream());		
			}
			catch (Exception e)
			{
				System.out.println("not able to connect to the game");
				if (count < 10)
					outputArea.append("notable to connect to the game.\n" + e);
				sock = null;
			}
			sleep_time( 5000 );
			count++;
		}

		while (sock != null && is != null && finisher != null) 
		{
			try 
			{
				String str = (String) is.readObject();
				System.out.println("Got: "+str);
				if(str != null) 
				{
					if(str.equals("init"))
					{
						init_array = (int [])is.readObject();
						NO_OF_PLAYERS=init_array[0];
						TOTAL_CARDS=init_array[1];
						player_position=init_array[2];
						SIZE_OF_HAND = TOTAL_CARDS/NO_OF_PLAYERS;
					}
					else if(str.equals("cards"))
					{
						card_array = (int [])is.readObject();
						
					}
					else if(str.equals("card_change"))
					{
						pos_change = (int [][])is.readObject();

					}
					else if(str.equals("your_turn"))
					{

					}
				}
			}
			catch (Exception e)
			{
				System.out.println("Connection lost.");
				stop();	
			}
		}	
	}
	
	public void finisher_stop()
	{
		try
		{
			is.close();
			os.close();
			sock.close();
		}
		catch (Exception e)
		{
			
		}
		sock = null;
	}
	
	public void stop() 
	{
		child.output("logout||"+name);
		outputArea.append("Logged out from server.\n");
		B2.setEnabled(false);
		B1.setEnabled(true);
		inputField.setText("<Enter Name and Press Login>");
		inputField.addMouseListener(lSymAction);
		p2.layout();
		removeIconsJLabels();
	}

	class SymAction implements java.awt.event.ActionListener, MouseListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == B1)
				login_actionPerformed(event);
			else if (object == B2)
				logout_actionPerformed(event);
			else if (object == B3)
				say_actionPerformed(event);
			else if (object == B4)
				whisper_actionPerformed(event);
		}
		public void mouseClicked(MouseEvent e) 
		{
			Object object = e.getSource();
			if (object == inputField)
			{
				inputField.setText("");
				inputField.removeMouseListener(lSymAction);
			}
			/*if (object == background)
			{
				after_one_round();	
				if (game_over == true)
				{
					after_one_game();
					getContentPane().remove(background);
					add_JLabels_to_pane();
					getContentPane().add(background);
					getContentPane().repaint();
					
				}
				remove_mouse_event(background);
				scoreLbl.setText(null);
				return;
			}
			int pos = card_key_map_for_JLabel(object, cur_player);
			if ( pos >= 0 )
			{
				Card card = cardDeck.getCard(pos);	
				if (card != null)
				{
					int k = cur_player;
					int i = pos % SIZE_OF_HAND;	
					handLbl[k][i].setIcon(null);	
					//remove_mouse_event(handLbl[k][i]);
					getContentPane().remove(handLbl[k][i]);	
					getContentPane().repaint();	
					cur_hand[k].setIcon(card.getCardImage());
					after_one_card(card);
					//cur_hand[k].setText( card.toString() );
				}
			}
			*/
		}	
		/* Ignore all  the following events	*/
		public void mouseExited(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved   (MouseEvent e) {} 
		public void mouseEntered(MouseEvent e) {}  
		public void mouseReleased(MouseEvent e) {}  
		public void mousePressed(MouseEvent e) {}  

	}

	public boolean handleEvent(Event evt) 
	{
		/* if the Event is not one of the ones we can handle, we should pass it along the chain-of-command to our super-class */
		return super.handleEvent(evt);
	}
	
	void register_buttons()
	{
		B1.addActionListener(lSymAction);
		B2.addActionListener(lSymAction);
		B3.addActionListener(lSymAction);
		B4.addActionListener(lSymAction);
	}

	public boolean login_actionPerformed(java.awt.event.ActionEvent event)	
	{
		outputArea.append("Logging in...\n");
		name = inputField.getText(); /* find out what alias the user wants to use */
		inputField.setText("");

		child = new ChatClient1(this, name, host, port);
		child.start();
		return true;
	}

	public boolean logout_actionPerformed(java.awt.event.ActionEvent event)	
	{
		outputArea.append("Logging out...\n");
		/* stop the Thread (which will disconnect) */
		child.kicker_stop();
		stop();
		return true;
	}
	
	public boolean say_actionPerformed(java.awt.event.ActionEvent event)
	{
		if (inputField.getText() != null)
		{
			child.output("say||"+inputField.getText());
			inputField.selectAll();
			inputField.setText("");
		}
		return true;
	}


	public boolean whisper_actionPerformed(java.awt.event.ActionEvent event)
	{
		outputArea.append("You whisper to "+name+ ": "+inputField.getText()+"\n");
		child.output("whisper||"+inputField.getText()+"||"+name );
		inputField.selectAll();
		return true;
	}

	void setBackground()
	{
		//Set Background 
		background = new JLabel();
		p1.add(background);
		String imageFile = directory + "background.gif";
		ImageIcon backImage = new ImageIcon( getImage( getCodeBase(), imageFile ) );
		//URL imageURL = cldr.getResource(imageFile);
		//ImageIcon backImage = new ImageIcon(imageURL);
		background.setBounds(0,0,frame_width, frame_height_actual);
		background.setIcon(backImage);
	}

	void setPanel2()
	{
		/* next create the Field used for input.  For fun, make it 80 columns wide.  Add it to the south Panel */
		inputField = new JTextField(20);
		inputField.setBounds(frame_width,(frame_height_actual/2),extra_width ,30);
		Font font = new Font("Verdana",Font.ITALIC, 12);
		Font font2 = new Font("Verdana",Font.ITALIC, 6);
		inputField.setBackground(Color.LIGHT_GRAY);
		//inputField.setOpaque(false);
		inputField.setFont(font);
		inputField.setText("<Enter Name and Press Login>");
		inputField.addMouseListener(lSymAction);
		p2.add( inputField );

		/* create the output Area.  Make it 10 rows by 60 columns.  Add it to north Panel  don't let the user edit the contents, and make the background color Cyan - because it looks nice */
		outputArea = new JTextArea(10, 60);
		outputArea.setBounds(frame_width,((frame_height_actual/2) + 30),extra_width,((frame_height_actual/2) - 30));
		outputArea.setEditable(false);
		outputArea.setFont(font);
		outputArea.setOpaque(false);
		scrollPane = new JScrollPane(outputArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy (ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(frame_width,((frame_height_actual/2) + 30),extra_width,((frame_height_actual/2) - 30));
		scrollPane.setOpaque(false);
		p2.add(scrollPane);

		/* now for the Buttons.  Make the first Button to let the user "login" */
		B1 = new JButton("login");
		int buttonPos = frame_width + (extra_width - (2 * button_width))/2;
		B1.setBounds(buttonPos, (frame_height_actual/6), button_width, button_height);  
		p2.add(B1);

		/* The second Button allows the user to "logout", but is initially disabled */
		B2 = new JButton("logout");
		B2.setBounds(buttonPos+button_width, (frame_height_actual/6), button_width, button_height);  
		p2.add(B2);
		B2.setEnabled(false);

		/* third Button to say Iam Ready */
		B3 = new JButton("say");
		B3.setBounds(buttonPos, ((frame_height_actual/2) - (frame_height_actual/6) - button_height), button_width, button_height);  
		B3.setEnabled(false);
		p2.add(B3);
		
		/* Fourth Button to send message */
		B4 = new JButton("whisper");
		B4.setBounds(buttonPos + button_width, ((frame_height_actual/2) - (frame_height_actual/6) - button_height), button_width, button_height);  
		B4.setEnabled(false);
		p2.add(B4);

		setBackground2();
	}
	
	void setBackground2()
	{
		//Set Background2 
		background2 = new JLabel();
		p2.add(background2);
		String imageFile = directory + "background2.gif";
		ImageIcon backImage = new ImageIcon( getImage( getCodeBase(), imageFile ) );
		//URL imageURL = cldr.getResource(imageFile);
		//ImageIcon backImage = new ImageIcon(imageURL);
		background2.setBounds(frame_width,0,extra_width,frame_height_actual);
		background2.setIcon(backImage);
	}
	
	void initJLabels()
	{
		int xPos = 0, yPos = 0;
		int plxPos = 0, plyPos = 0, plw =40, plh = 30;
		int chxPos = 0, chyPos = 0;
		
		for ( int k = 0 ; k < NO_OF_PLAYERS ;k++)
		{
			player_label[k] = new JLabel();
			cur_hand[k] = new JLabel();
			switch (k)
			{
				case 0:
					{
						xPos = ((frame_width-card_width)/2) +(6 * card_spacing);
						yPos = margin;
						plxPos = xPos+6+card_width;
						plyPos = yPos+(card_height/2) ;
						chxPos = (frame_width/2) - (card_width/2);
						chyPos = (frame_height_actual/2) - dist - card_height;
						break;
					}
				case 3:
					{
						xPos = margin;
						yPos = ((frame_height-card_height)/2) +(6 * card_spacing);
						plxPos = xPos + (card_width/2);
						plyPos = yPos +6+card_height;
						chxPos = (frame_width/2) - dist - card_width;
						chyPos = (frame_height/2) - (card_height/2);
						break;
					}
				case 2:
					{
						xPos = ((frame_width-card_width)/2) +(6 * card_spacing);
						yPos = frame_height_actual - margin - card_height;
						plxPos = xPos+6+card_width;
						plyPos = yPos+(card_height/2) ;
						chxPos = (frame_width/2) - (card_width/2);
						chyPos = (frame_height_actual/2) + (2 *dist);
						break;
					}
				case 1:
					{
						xPos = frame_width - margin - card_width;
						yPos = ((frame_height-card_height)/2) +(6 * card_spacing);
						plxPos = xPos + (card_width/2);
						plyPos = yPos+6+card_height;
						chxPos = (frame_width/2) + dist;
						chyPos = (frame_height/2) - (card_height/2);
						break;
					}
			}
			
			p1.add(player_label[k]);
			player_label[k].setForeground(java.awt.Color.black);
			player_label[k].setFont(new Font("Dialog", Font.BOLD, 10));
			player_label[k].setBounds(plxPos,plyPos,plw,plh);

			p1.add(cur_hand[k]);
			cur_hand[k].setForeground(java.awt.Color.black);
			cur_hand[k].setBounds(chxPos,chyPos,card_width,card_height);

			for ( int i = 0 ; i < SIZE_OF_HAND; i++ ) 
			{
				handLbl[k][i] = new JLabel();
				handLbl[k][i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				handLbl[k][i].setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
				handLbl[k][i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				//handLbl[k][i].setText("Card");
				handLbl[k][i].setOpaque(false);
				handLbl[k][i].setToolTipText("This is a card.");
				p1.add(handLbl[k][i]);
				handLbl[k][i].setForeground(java.awt.Color.black);
				handLbl[k][i].setFont(new Font("Dialog", Font.BOLD, 10));
				handLbl[k][i].setBounds(xPos,yPos,card_width,card_height);
				if (k % 2 == 0)
					xPos-=card_spacing;
				else
					yPos-=card_spacing;
			}
		}

		//Initiate Score Label
		/*scoreLbl = new JLabel();
		scoreLbl.setToolTipText("Sum of all cards");
		p1.add(scoreLbl);
		scoreLbl.setForeground(java.awt.Color.black);
		scoreLbl.setBounds(636,156,204,29);

		*/
	}

	void setIconsJLabels()
	{
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			for (int i = 0; i < SIZE_OF_HAND; i++)
			{
				if (k <2)
					handLbl[k][SIZE_OF_HAND - i - 1].setIcon(card_back);
				else
					handLbl[k][i].setIcon(card_back);
				//handLbl[k][i].setIcon(Image_Icons[k][i]);
				p1.repaint();
				sleep_time(15);
			}
		player_label[k].setText(names[k]);

		}
	}

	void removeIconsJLabels()
	{
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			for (int i = 0; i < SIZE_OF_HAND; i++)
			{
				handLbl[k][i].setIcon(null);
			}
		player_label[k].setText(null);

		}
		p1.repaint();
	}
	void loadImageIcons()
	{
		String cards = "a23456789tjqk";
		String suit = "cshd";
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			for (int i = 0; i < SIZE_OF_HAND; i++)
			{
				String imageFile = directory + cards.charAt(i) + suit.charAt(k) +".gif";
				Image_Icons[k][i] = new ImageIcon( getImage( getCodeBase(), imageFile ) );
			}

		}
		
		String imageFile = directory + "cardback.gif";
		//String imageFile = directory + "chelcardback.gif";
		card_back = new ImageIcon( getImage( getCodeBase(), imageFile ) );
	}

	void sleep_time(int time)
	{
		try 
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			
		}
	}
}
package Client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import java.awt.List;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.net.*;
import java.lang.*;
import java.io.*;
import java.util.*;

public class ChatClient extends JApplet implements Runnable 
{
	// Constants
	private final int TOTAL_CARDS = 52, NO_OF_PLAYERS = 4;
	private final int SIZE_OF_HAND = TOTAL_CARDS/NO_OF_PLAYERS;
	private final int card_spacing = 25, dist = 30, margin = 12;
	private final int extra_width = 300, frame_width = 1024, frame_height = 800, frame_height_actual = 768;
	private final int button_width = 100, button_height = 30;
	private final int card_width = 72, card_height = 96;
	private final String [] names = {"North", "East", "South", "West"};
	private final String directory = "images/";
	//network
	Socket sock;
	DataInputStream dis;
	PrintStream ps;
	Thread kicker = null;
	
	// JAPPLET Stuff....
	JPanel p1, p2;
	private JLabel[] cur_hand = new JLabel[NO_OF_PLAYERS];
	private JLabel[] player_label = new JLabel[NO_OF_PLAYERS];
	private JLabel[][] handLbl = new JLabel[NO_OF_PLAYERS][ SIZE_OF_HAND ]; 
	private JLabel background, scoreLbl, background2;
	JTextField inputField;
	JTextArea outputArea;
	JButton B1,B2,B3,B4;
	JScrollPane scrollPane;
	
	ImageIcon[][] Image_Icons = new ImageIcon[NO_OF_PLAYERS][SIZE_OF_HAND];
	ImageIcon card_back;

	String name, theHost;
	int thePort;
	private final SymAction lSymAction = new SymAction();

	public void init() 
	{	
		try {
		/* first, assign a BorderLayout and add the two Panels */
		p2 = new JPanel();
		p2.setLayout(null);
		p2.setBounds(frame_width,0,extra_width,frame_height_actual);
		//p2.setBackground(java.awt.Color.black);
		p1 = new JPanel();
		p1.setLayout(null);
		p1.setBounds(0,0,frame_width,frame_height_actual);
		setSize(frame_width+extra_width,frame_height_actual);
		add(p1);
		add(p2);

		String temp;
		temp = getParameter("host");
		if( temp == null)
			theHost = "localhost";
		else
			theHost = temp;
		temp = getParameter("port");

		try 
		{
			thePort = Integer.valueOf(temp).intValue();
		}
		catch(Exception e)
		{
			thePort = 31123;
		}
		
		loadImageIcons();

		setPanel2();
		register_buttons();
		
		initJLabels();

		setBackground();
		
		//setIconsJLabels();

		}
		catch (NullPointerException e)
		{
			
		}
	}

	public void run() {
		
		int count = 0;

		while (sock == null && kicker != null )
		{
			try
			{
				sock = new Socket(theHost,thePort);
				dis = new DataInputStream( sock.getInputStream() );
				ps = new PrintStream( sock.getOutputStream() );		
			}
			catch (Exception e)
			{
				System.out.println("Unable to contact host.");
				if (count < 10)
					outputArea.append("Unable to contact host. Retrying...\n" + e);
				sock = null;
			}
			try
			{
				Thread.sleep( 5000 );
			}
			catch(Exception e)
			{
			}
			count++;
		}

		output("login||"+name);
		outputArea.append("Logged in to server successfully.\n");
		B2.setEnabled(true);
		B1.setEnabled(false);
		B3.setEnabled(true);
		B4.setEnabled(true);
		setIconsJLabels();
		while (sock != null && dis != null && kicker != null) 
		{
			try 
			{
				String str = dis.readLine();
				System.out.println("Got: "+str);
				if(str != null) 
					if(str.indexOf("||") != -1)
					{
						StringTokenizer st = new StringTokenizer(str,"||");
						String cmd = st.nextToken();
						String val = st.nextToken();
						if(cmd.equals("list"))
						{ 
						}
						else
							if(cmd.equals("logout")) 
							{
								outputArea.append(val+"\n");
								validate();
							}
							else
								if(cmd.equals("login")) 
								{
									outputArea.append(st.nextToken()+"\n");
								}
								else
									outputArea.append( val + "\n" );
					}
					else
						outputArea.append(str + "\n");
			}
			catch (IOException e)
			{
				System.out.println("Connection lost.");
				kicker.stop();
			}
		}	
	}

	public void stop() 
	{
		output("logout||"+name);
		try
		{
			dis.close();
			ps.close();
			sock.close();
		}
		catch (Exception e)
		{
		}
		sock = null;
		outputArea.append("Logged out from server.\n");

		/* reset our affected GUI components */
		B2.setEnabled(false);
		B1.setEnabled(true);
		inputField.setText("<Enter Name and Press Login>");
		inputField.addMouseListener(lSymAction);
		p2.layout();
		removeIconsJLabels();

		kicker = null;
	}

	public boolean output(String str) 
	{
		try
		{
			ps.println(str);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

	class SymAction implements java.awt.event.ActionListener, MouseListener
	{
		public void actionPerformed(java.awt.event.ActionEvent event)
		{
			Object object = event.getSource();
			if (object == B1)
				login_actionPerformed(event);
			else if (object == B2)
				logout_actionPerformed(event);
			else if (object == B3)
				say_actionPerformed(event);
			else if (object == B4)
				whisper_actionPerformed(event);
		}
		public void mouseClicked(MouseEvent e) 
		{
			Object object = e.getSource();
			if (object == inputField)
			{
				inputField.setText("");
				inputField.removeMouseListener(lSymAction);
			}
			/*if (object == background)
			{
				after_one_round();	
				if (game_over == true)
				{
					after_one_game();
					getContentPane().remove(background);
					add_JLabels_to_pane();
					getContentPane().add(background);
					getContentPane().repaint();
					
				}
				remove_mouse_event(background);
				scoreLbl.setText(null);
				return;
			}
			int pos = card_key_map_for_JLabel(object, cur_player);
			if ( pos >= 0 )
			{
				Card card = cardDeck.getCard(pos);	
				if (card != null)
				{
					int k = cur_player;
					int i = pos % SIZE_OF_HAND;	
					handLbl[k][i].setIcon(null);	
					//remove_mouse_event(handLbl[k][i]);
					getContentPane().remove(handLbl[k][i]);	
					getContentPane().repaint();	
					cur_hand[k].setIcon(card.getCardImage());
					after_one_card(card);
					//cur_hand[k].setText( card.toString() );
				}
			}
			*/
		}	
		/* Ignore all  the following events	*/
		public void mouseExited(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved   (MouseEvent e) {} 
		public void mouseEntered(MouseEvent e) {}  
		public void mouseReleased(MouseEvent e) {}  
		public void mousePressed(MouseEvent e) {}  

	}

	public boolean handleEvent(Event evt) 
	{
		/* if the Event is not one of the ones we can handle, we should pass it along the chain-of-command to our super-class */
		return super.handleEvent(evt);
	}
	
	void register_buttons()
	{
		B1.addActionListener(lSymAction);
		B2.addActionListener(lSymAction);
		B3.addActionListener(lSymAction);
		B4.addActionListener(lSymAction);
	}

	public boolean login_actionPerformed(java.awt.event.ActionEvent event)	
	{
		outputArea.append("Logging in...\n");
		name = inputField.getText(); /* find out what alias the user wants to use */
		inputField.setText("");

		/* create a new Thread and start it */
		kicker = new Thread(this);
		kicker.start();
		return true;
	}

	public boolean logout_actionPerformed(java.awt.event.ActionEvent event)	
	{
		outputArea.append("Logging out...\n");
		/* stop the Thread (which will disconnect) */
		stop();
		kicker.stop();
		return true;
	}
	
	public boolean say_actionPerformed(java.awt.event.ActionEvent event)
	{
		if (inputField.getText() != null)
		{
			output("say||"+inputField.getText());
			inputField.selectAll();
			inputField.setText("");
		}
		return true;
	}


	public boolean whisper_actionPerformed(java.awt.event.ActionEvent event)
	{
		//outputArea.append("You whisper to "+name+ ": "+inputField.getText()+"\n");
		output("whisper||"+inputField.getText()+"||"+name );
		inputField.selectAll();
		return true;
	}

	void setBackground()
	{
		//Set Background 
		background = new JLabel();
		p1.add(background);
		String imageFile = directory + "background.gif";
		ImageIcon backImage = new ImageIcon( getImage( getCodeBase(), imageFile ) );
		//URL imageURL = cldr.getResource(imageFile);
		//ImageIcon backImage = new ImageIcon(imageURL);
		background.setBounds(0,0,frame_width, frame_height_actual);
		background.setIcon(backImage);
	}

	void setPanel2()
	{
		/* next create the Field used for input.  For fun, make it 80 columns wide.  Add it to the south Panel */
		inputField = new JTextField(20);
		inputField.setBounds(frame_width,(frame_height_actual/2),extra_width ,30);
		Font font = new Font("Verdana",Font.ITALIC, 12);
		Font font2 = new Font("Verdana",Font.ITALIC, 6);
		inputField.setBackground(Color.LIGHT_GRAY);
		//inputField.setOpaque(false);
		inputField.setFont(font);
		inputField.setText("<Enter Name and Press Login>");
		inputField.addMouseListener(lSymAction);
		p2.add( inputField );

		/* create the output Area.  Make it 10 rows by 60 columns.  Add it to north Panel  don't let the user edit the contents, and make the background color Cyan - because it looks nice */
		outputArea = new JTextArea(10, 60);
		outputArea.setBounds(frame_width,((frame_height_actual/2) + 30),extra_width,((frame_height_actual/2) - 30));
		p2.add(outputArea);
		outputArea.setEditable(false);
		outputArea.setFont(font);
		outputArea.setOpaque(false);

		/* now for the Buttons.  Make the first Button to let the user "login" */
		B1 = new JButton("login");
		int buttonPos = frame_width + (extra_width - (2 * button_width))/2;
		B1.setBounds(buttonPos, (frame_height_actual/6), button_width, button_height);  
		p2.add(B1);

		/* The second Button allows the user to "logout", but is initially disabled */
		B2 = new JButton("logout");
		B2.setBounds(buttonPos+button_width, (frame_height_actual/6), button_width, button_height);  
		p2.add(B2);
		B2.setEnabled(false);

		/* third Button to say Iam Ready */
		B3 = new JButton("say");
		B3.setBounds(buttonPos, ((frame_height_actual/2) - (frame_height_actual/6) - button_height), button_width, button_height);  
		B3.setEnabled(false);
		p2.add(B3);
		
		/* Fourth Button to send message */
		B4 = new JButton("whisper");
		B4.setBounds(buttonPos + button_width, ((frame_height_actual/2) - (frame_height_actual/6) - button_height), button_width, button_height);  
		B4.setEnabled(false);
		p2.add(B4);

		setBackground2();
	}
	
	void setBackground2()
	{
		//Set Background2 
		background2 = new JLabel();
		p2.add(background2);
		String imageFile = directory + "background2.gif";
		ImageIcon backImage = new ImageIcon( getImage( getCodeBase(), imageFile ) );
		//URL imageURL = cldr.getResource(imageFile);
		//ImageIcon backImage = new ImageIcon(imageURL);
		background2.setBounds(frame_width,0,extra_width,frame_height_actual);
		background2.setIcon(backImage);
	}
	
	void initJLabels()
	{
		int xPos = 0, yPos = 0;
		int plxPos = 0, plyPos = 0, plw =40, plh = 30;
		int chxPos = 0, chyPos = 0;
		
		for ( int k = 0 ; k < NO_OF_PLAYERS ;k++)
		{
			player_label[k] = new JLabel();
			cur_hand[k] = new JLabel();
			switch (k)
			{
				case 0:
					{
						xPos = ((frame_width-card_width)/2) +(6 * card_spacing);
						yPos = margin;
						plxPos = xPos+6+card_width;
						plyPos = yPos+(card_height/2) ;
						chxPos = (frame_width/2) - (card_width/2);
						chyPos = (frame_height_actual/2) - dist - card_height;
						break;
					}
				case 3:
					{
						xPos = margin;
						yPos = ((frame_height-card_height)/2) +(6 * card_spacing);
						plxPos = xPos + (card_width/2);
						plyPos = yPos +6+card_height;
						chxPos = (frame_width/2) - dist - card_width;
						chyPos = (frame_height/2) - (card_height/2);
						break;
					}
				case 2:
					{
						xPos = ((frame_width-card_width)/2) +(6 * card_spacing);
						yPos = frame_height_actual - margin - card_height;
						plxPos = xPos+6+card_width;
						plyPos = yPos+(card_height/2) ;
						chxPos = (frame_width/2) - (card_width/2);
						chyPos = (frame_height_actual/2) + (2 *dist);
						break;
					}
				case 1:
					{
						xPos = frame_width - margin - card_width;
						yPos = ((frame_height-card_height)/2) +(6 * card_spacing);
						plxPos = xPos + (card_width/2);
						plyPos = yPos+6+card_height;
						chxPos = (frame_width/2) + dist;
						chyPos = (frame_height/2) - (card_height/2);
						break;
					}
			}
			
			p1.add(player_label[k]);
			player_label[k].setForeground(java.awt.Color.black);
			player_label[k].setFont(new Font("Dialog", Font.BOLD, 10));
			player_label[k].setBounds(plxPos,plyPos,plw,plh);

			p1.add(cur_hand[k]);
			cur_hand[k].setForeground(java.awt.Color.black);
			cur_hand[k].setBounds(chxPos,chyPos,card_width,card_height);

			for ( int i = 0 ; i < SIZE_OF_HAND; i++ ) 
			{
				handLbl[k][i] = new JLabel();
				handLbl[k][i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
				handLbl[k][i].setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
				handLbl[k][i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
				//handLbl[k][i].setText("Card");
				handLbl[k][i].setOpaque(false);
				handLbl[k][i].setToolTipText("This is a card.");
				p1.add(handLbl[k][i]);
				handLbl[k][i].setForeground(java.awt.Color.black);
				handLbl[k][i].setFont(new Font("Dialog", Font.BOLD, 10));
				handLbl[k][i].setBounds(xPos,yPos,card_width,card_height);
				if (k % 2 == 0)
					xPos-=card_spacing;
				else
					yPos-=card_spacing;
			}
		}

		//Initiate Score Label
		/*scoreLbl = new JLabel();
		scoreLbl.setToolTipText("Sum of all cards");
		p1.add(scoreLbl);
		scoreLbl.setForeground(java.awt.Color.black);
		scoreLbl.setBounds(636,156,204,29);

		*/
	}

	void setIconsJLabels()
	{
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			for (int i = 0; i < SIZE_OF_HAND; i++)
			{
				if (k <2)
					handLbl[k][SIZE_OF_HAND - i - 1].setIcon(card_back);
				else
					handLbl[k][i].setIcon(card_back);
				//handLbl[k][i].setIcon(Image_Icons[k][i]);
				p1.repaint();
				sleep_time(15);
			}
		player_label[k].setText(names[k]);

		}
	}

	void removeIconsJLabels()
	{
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			for (int i = 0; i < SIZE_OF_HAND; i++)
			{
				handLbl[k][i].setIcon(null);
			}
		player_label[k].setText(null);

		}
		p1.repaint();
	}
	void loadImageIcons()
	{
		String cards = "a23456789tjqk";
		String suit = "cshd";
		for (int k = 0; k < NO_OF_PLAYERS; k++)
		{
			for (int i = 0; i < SIZE_OF_HAND; i++)
			{
				String imageFile = directory + cards.charAt(i) + suit.charAt(k) +".gif";
				Image_Icons[k][i] = new ImageIcon( getImage( getCodeBase(), imageFile ) );
			}

		}
		
		String imageFile = directory + "cardback.gif";
		//String imageFile = directory + "chelcardback.gif";
		card_back = new ImageIcon( getImage( getCodeBase(), imageFile ) );
	}

	void sleep_time(int time)
	{
		try 
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			
		}
	}
}
package common;

import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;

public class CraftrMap
{
	public CraftrChunk[] chunks;
	public int cachesize;
	public int genMode = 0;
	public static Random rand = new Random();
	public String saveDir;
	public String mapName = "map";
	public boolean multiplayer;
	public boolean maplock;
	public boolean modlock;
	
	public boolean isServer;
	
	// just inferfaces for the client / server specifics -GM
	public CraftrGameShim game;
	public CraftrNetShim net;
	public CraftrServerShim se;
	
	public CraftrPhysics physics;
	Random randc = new Random();
	// CONSTRUCTORS

	public CraftrMap(boolean _isServer, int _cachesize)
	{
		this(_isServer,_cachesize,false);
	}

	public CraftrMap(boolean _isServer, int _cachesize, boolean multiMode)
	{
		this(_isServer,_cachesize,multiMode,"map");
	}

	public CraftrMap(boolean _isServer, int _cachesize, String name)
	{
		this(_isServer,_cachesize,false,name);
	}

	public CraftrMap(boolean _isServer, int _cachesize, boolean multiMode, String name)
	{
		mapName=name;
		isServer = _isServer;
		System.out.println("[MAP] Initializing '" + name + "'...");
		physics = new CraftrPhysics(isServer);
		multiplayer = multiMode;
		chunks = new CraftrChunk[_cachesize];
		cachesize = _cachesize;
		saveDir = "";
		for(int i=0;i<cachesize;i++)
		{
			chunks[i] = new CraftrChunk(0,0,false);
			chunks[i].isUsed = false;
			chunks[i].isSet = false;
		}
	}
	
	public void kill(int id)
	{
		if(isServer)
			synchronized(se)
			{
				se.kill(id);
			}
		else if (id==255)
			game.kill();
	}

	public void resizeChunks(int _cachesize)
	{
		chunks = new CraftrChunk[_cachesize];
		cachesize = _cachesize;
		for(int i=0;i<cachesize;i++)
		{
			chunks[i] = new CraftrChunk(0,0,false);
			chunks[i].isUsed = false;
			chunks[i].isSet = false;
		}
	}
	
	// CHUNK LOCATING	
	public CraftrChunk grabChunk(int x, int y) throws NoChunkMemException
	{
		// Is the chunk cached?
		for (int i=0;i<cachesize;i++) if(chunks[i].xpos == x && chunks[i].ypos == y && chunks[i].isSet == true) { chunks[i].isUsed = true; return chunks[i]; }
		
		// No, is there any empty chunk?
		for (int i=0;i<cachesize;i++) if(!chunks[i].isSet) { chunks[i] = new CraftrChunk(x,y,true); chunks[i].isUsed = true; chunks[i].loadByte(readChunkFile(x,y,true)); return chunks[i]; }
		
		// No, is there any chunk that isn't in the closest area of players'?
		for (int i=0;i<cachesize;i++) if(!chunks[i].isUsed) { chunks[i] = new CraftrChunk(x,y,true); chunks[i].isUsed = true; chunks[i].loadByte(readChunkFile(x,y,true)); return chunks[i]; }
		
		if(isServer)
		{
			// Use up the first chunk you see 
			int i = randc.nextInt(cachesize);
			saveChunkFile(i);
			chunks[i] = new CraftrChunk(x,y,true);
			chunks[i].isUsed = true;
			chunks[i].loadByte(readChunkFile(x,y,true));
			return chunks[i];
		} else {
			// Are you kidding me!? 9 chunks are (should be) used and you can't find a free space in SIXTY-FOUR!?
			// I throw an exception right now.
			throw new NoChunkMemException(x,y);
		}
	}

	public void wipeChunks()
	{
		for (int i=0;i<cachesize;i++)
		{
			chunks[i].isSet=false;
			chunks[i].isUsed=false;
		}
	}

	public CraftrChunk findCachedChunk(int x, int y)
	{
		// Is the chunk cached?
		for (int i=0;i<cachesize;i++) if(chunks[i].xpos == x && chunks[i].ypos == y && chunks[i].isSet == true) return chunks[i];
	
		// No, who cares.
		return null;
	}
	
	public int findChunkID(int x, int y)
	{
		for (int i=0;i<cachesize;i++) if(chunks[i].xpos == x && chunks[i].ypos == y && chunks[i].isSet == true) return i;
		return -1;
	}

	public int findNewChunkID(int x, int y)
	{
		// Is the chunk cached?
		for (int i=0;i<cachesize;i++) if(chunks[i].xpos == x && chunks[i].ypos == y && chunks[i].isSet == true) return i;
	
		// No, is there any empty chunk?
		for (int i=0;i<cachesize;i++) if(!chunks[i].isSet) { chunks[i] = new CraftrChunk(x,y,true); return i; }
		
		// No, is there any chunk that isn't in the closest area of players'?
		for (int i=0;i<cachesize;i++) if(!chunks[i].isUsed) { chunks[i] = new CraftrChunk(x,y,true); return i; }

		// No, who cares.
		return -1;
	}
	
	// HANDLING isUsed
	public void setUsed(int x, int y)
	{
		for (int i=0;i<cachesize;i++) if(chunks[i].xpos == x && chunks[i].ypos == y) { chunks[i].isReUsed = true;}
	}
	
	public void clearAllUsed()
	{
		for (int i=0;i<cachesize;i++)
		{
			if(chunks[i].isSet && chunks[i].isUsed == true && chunks[i].isReUsed == false)
			{
				saveChunkFile(i);
				chunks[i].isUsed = false;
			} else if (chunks[i].isReUsed == true && chunks[i].isSet)
			{
				chunks[i].isUsed = true;
				chunks[i].isReUsed = false;
			}
		}
	}
	
	// NEW CHUNK GENERATION
	public CraftrChunk generateChunk(int cx, int cy, boolean used)
	{
		CraftrChunk out = new CraftrChunk(0,0,used);
		out.generate(genMode);
		return out;
	}
	
	// LOADING/SAVING - UTILITIES
	public void checkDirs(int x, int y)
	{
		File tmf = new File(saveDir + mapName);
		if(!tmf.exists()) tmf.mkdir();
		tmf = new File(saveDir + mapName + "/" + y);
		if(!tmf.exists()) tmf.mkdir();
	}
	
	// LOADING
	public CraftrChunk readChunkFromFile(int x, int y)
	{
		byte[] tmp = readChunkFile(x,y,false);
		if(tmp != null) {
			CraftrChunk n = new CraftrChunk(x,y,false);
			n.loadByte(tmp);
			return n;
		}
		else return null;
	}
	public void readChunkFile(int x, int y, int cid)
	{
		if (cid >= 0)
		{
			chunks[cid] = new CraftrChunk(x,y,true);
			chunks[cid].loadByte(readChunkFile(x,y,true));
		}
	}
	public byte[] readChunkFile(int x, int y, boolean genNew)
	{
		if(multiplayer)
		{
			System.out.println("[MAP] Chunk request: " + x + ", " + y);
			if(!isServer)
				net.chunkRequest(x,y);
			CraftrChunk nout = new CraftrChunk(x,y,true);
			return nout.saveByte();
		}
		else
		{
		// Init variables
		FileInputStream in = null;
		GZIPInputStream gin = null;
		byte[] out = new byte[16384];
		try	// The code proper
		{
			// Load file
			in = new FileInputStream(saveDir + mapName + "/" + CraftrChunk.getFilename(x,y));
			gin = new GZIPInputStream(in);
			// Create buffer, check version
			byte[] buf = new byte[256];
			gin.read(buf,0,1);
			int i = 1;
			int hdrsize = CraftrChunk.hdrsize;
			switch(buf[0])
			{
				case 5:
					out = new byte[1+(4096*11)+hdrsize];
					while(i<(1+(4096*11)+hdrsize) && i>-1) i += gin.read(out,i,out.length-i);
					gin.close();
					for(int ri=0;ri<4096;ri++)
					{
						if(out[1+ri+hdrsize]==5 && (0x80&(int)out[1+ri+hdrsize+4096])>0)
						{
							out[1+ri+hdrsize+4096]=(byte)1;
							physics.addBlockToCheck(new CraftrBlockPos(x*64+(ri&63),y*64+(ri>>6)));
						}
						else if(physics.isReloaded(out[1+ri+hdrsize]) || out[1+ri+(4096*2)+hdrsize] != 0)
							physics.addBlockToCheck(new CraftrBlockPos(x*64+(ri&63),y*64+(ri>>6)));
					}
					return out;
				default:
					System.out.println("[MAP] ReadChunkFile: unknown version: " + buf[0]);
					break;
			}
		}
		catch (FileNotFoundException e)
		{
			// FileInputStream - file was not found.
			CraftrChunk nout = null;
			if(genNew) nout = generateChunk(x, y,false);
			else return null;
			saveChunkFile(x,y,nout.saveByte());
			return nout.saveByte();
		}
		catch (Exception e)
		{
			// Something else happened!
			System.out.println("[MAP] ReadChunkFile: exception: " + e.getMessage());
			return out;
		}
		finally
		{
			try
			{
				if(gin != null && in != null) {gin.close(); in.close();}
			}
			catch (Exception e) { System.out.println("ReadChunkFile: warning - file in streams didn't close"); }
		}
		return out;
		}
	}
	
	// SAVING
	public void saveChunkFile(int cid)
	{
		CraftrChunk cin = chunks[cid];
		if (cin != null)
		{
			saveChunkFile(cin.xpos,cin.ypos,cin.saveByte());
		}
	}
	public void saveChunkFile(int x, int y)
	{
		CraftrChunk cin = findCachedChunk(x,y);
		if (cin != null)
		{
			saveChunkFile(x,y,cin.saveByte());
		}
	}
	public void saveChunkFile(int x, int y, byte[] data)
	{
		if(multiplayer) return;
		FileOutputStream fout = null;
		GZIPOutputStream gout = null;
		try
		{
			checkDirs(x,y);
			fout = new FileOutputStream(saveDir + mapName + "/" + CraftrChunk.getFilename(x,y));
			gout = new GZIPOutputStream(fout);
			gout.write(data,0,data.length);
		}
		catch (Exception e)
		{
			System.out.println("SaveChunkFile: exception: " + e.toString());
		}
		finally
		{
			try
			{
				if(gout != null && fout != null) {gout.finish(); gout.close(); fout.close();}
			}
			catch (Exception e) { System.out.println("SaveChunkFile: warning - file out streams didn't close"); }
		}
	}
	
	public CraftrBlock getBlock(int x, int y)
	{
		try
		{ 
			return new CraftrBlock(x,y,grabChunk((x>>6),(y>>6)).getBlock(x&63,y&63));
		}
		catch(NoChunkMemException e)
		{
			System.out.println("getBlock: exception: no chunk memory found. Odd...");
			return null;
 		}
	}
 	
 	// returns true if it needs to pull the player along with it
 	public boolean pushAttempt(int lolx, int loly, int lolvx, int lolvy)
 	{
 		CraftrBlock dc = getBlock(lolx,loly);
 		CraftrBlock dt = getBlock(lolx+lolvx,loly+lolvy);
		
 		if(dc.isPushable() && dt.isEmpty())
 		{
 			if(!multiplayer)
 			{
 				synchronized(this)
 				{
 					setPushable(lolx,loly,(byte)0,(byte)0);
 					setPushable(lolx+lolvx,loly+lolvy,dc.getChar(),dc.getColor());
 				}
				for(int i=0;i<4;i++)
				{
					physics.addBlockToCheck(new CraftrBlockPos(lolx+xMovement[i],loly+yMovement[i]));
					physics.addBlockToCheck(new CraftrBlockPos(lolx+lolvx+xMovement[i],loly+lolvy+yMovement[i]));
				}
 			}
 			return true;
 		} else {
 			return false;
 		}
 	}
 	
	public void setPushable(int x, int y, int aChr, int aCol)
	{
		setPushable(x,y,(byte)aChr,(byte)aCol);
	}

 	public void setPushable(int x, int y, byte aChr, byte aCol)
 	{
 		try
 		{ 
 			int px = x&63;
 			int py = y&63;
 			grabChunk((x>>6),(y>>6)).placePushable(px,py,aChr,aCol);
 		}
		catch(Exception e)
		{
 			System.out.println("setPushable: exception!");
			e.printStackTrace();
 			if(!multiplayer) System.exit(1);
		}
	}
 	public void setBullet(int x, int y, byte aType)
	{
		setBullet(x,y,aType,(byte)0);
	}
 	public void setBullet(int x, int y, byte aType, byte aPar)
 	{
 		try
 		{ 
 			int px = x&63;
 			int py = y&63;
 			grabChunk((x>>6),(y>>6)).placeBullet(px,py,aType, aPar);
 		}
		catch(Exception e)
		{
 			System.out.println("setBullet: exception!");
			e.printStackTrace();
 			if(!multiplayer) System.exit(1);
		}
	}
	public void setBulletNet(int x, int y, byte aType, byte aPar)
	{
		setBulletNet(x,y,aType);
	}
	public void setBulletNet(int x, int y, byte aType)
	{
		try
		{
			se.out.writeByte(0x70);
			se.out.writeInt(x);
			se.out.writeInt(y);
			se.out.writeByte(aType);
			byte[] t = se.getPacket();
			se.sendAllOnMap(t,t.length,mapName);
		}
		catch(Exception e) { System.out.println("setBulletNet sending error!"); }
	}
	
	public void setPlayerNet(int x, int y, int on)
	{
		try
		{
			se.out.writeByte(0x2A|on);
			se.out.writeInt(x);
			se.out.writeInt(y);
			byte[] t = se.getPacket();
			se.sendAllOnMap(t,t.length,mapName);
		}
		catch(Exception e) { System.out.println("setPlayerNet sending error!"); }
	}
	public void setPlayer(int x, int y, int on)
	{
		CraftrBlock block = getBlock(x,y);
		switch(block.getType())
		{
			case 5:
				int d15 = (block.getParam()&0x7F)|(on<<7);
				int t15 = block.getParam()&0x80;
				if(t15==0 && on>0)
				{
					d15=d15&0x80;
					playSample(x,y,1);
				}
				else if(t15>0 && on==0) playSample(x,y,0);
				while(maplock) { try{ Thread.sleep(1); } catch(Exception e) {} }
				modlock=true;
				setBlock(x,y,block.getType(),(byte)d15,block.getBlockChar(),block.getBlockColor());
				physics.addBlockToCheck(new CraftrBlockPos(x,y));
				for(int i=0;i<4;i++)
				{
					physics.addBlockToCheck(new CraftrBlockPos(x+xMovement[i],y+yMovement[i]));
				}
				
				if(isServer)
					setPlayerNet(x,y,on);
				
				modlock=false;
				break;
			default:
				break;
		}
	}
	
	public void pushMultiple(int x, int y, int xs, int ys, int dx, int dy)
	{
		pushMultiple(x,y,xs,ys,dx,dy,false);
	}

	// this was tricky to merge, so it might still be a bit buggy --GM
	public void pushMultiple(int x, int y, int xsize, int ysize, int dx, int dy, boolean pull)
	{
		synchronized(this)
		{
			if(isServer)
			{
				try
				{
					if(pull) se.out.writeByte(0xE2);
					else se.out.writeByte(0xE1);
					se.out.writeInt(x);
					se.out.writeInt(y);
					se.out.writeShort(xsize);
					se.out.writeShort(ysize);
					se.out.writeByte(dx);
					se.out.writeByte(dy);
					byte[] t = se.getPacket();
					se.sendAllOnMap(t,t.length,mapName);
				}
				catch(Exception e){ System.out.println("Failed to send pushMultiple packet!"); }
			}
			CraftrBlock[] blocks = new CraftrBlock[xsize*ysize];
			for(int ty=0;ty<ysize;ty++)
			{
				int iy = ty*dy;
				for(int tx=0;tx<xsize;tx++)
				{
					int ix = tx*dx;
					blocks[(ty*xsize)+tx] = getBlock(x+ix,y+iy);
					setBlock(x+ix,y+iy,(byte)0,(byte)0,(byte)0,(byte)0);
					setPushable(x+ix,y+iy,(byte)0,(byte)0);
					setPlayer(x+ix,y+iy,0);
				}
			}
			for(int ty=0;ty<ysize;ty++)
			{
				int iy = ty*dy;
				for(int tx=0;tx<xsize;tx++)
				{
					int ix = tx*dx;
					int arrayPos = (ty*xsize)+tx;
					int ox = x+ix;
					int oy = y+iy;
					if(pull)
					{
						ox-=dx;
						oy-=dy;
					} else {
						ox+=dx;
						oy+=dy;
					}
					if(blocks[arrayPos].isPushable()) setPushable(ox,oy,
		                                                          (byte)blocks[arrayPos].getChar(),
		                                                          (byte)blocks[arrayPos].getColor());
					if(blocks[arrayPos].isPlaceable())
					{
						setBlock(ox,oy,blocks[arrayPos].getBlockData());
						setPlayer(ox,oy,1);
					}
					for(int moveDir=0;moveDir<4;moveDir++)
					{
						physics.addBlockToCheck(new CraftrBlockPos(ox+xMovement[moveDir],oy+yMovement[moveDir]));
					}
				}
			}
		}
	}

	public void tryPushM(int x, int y, int dx, int dy, byte chr, byte col)
	{
		if((dx!=0 && dy!=0) || (dx==0 && dy==0)) return; // don't do diagonals.
		if(dx>1 || dx<-1 || dy>1 || dy<-1) return; // no.
		if(col==0) return; // we do not want non-colored pushables
		if(getBlock(x+dx,y+dy).isEmpty()) // can we not push?
		{
			setPushable(x+dx,y+dy,chr,col);
			if(isServer)
				setPushableNet(x+dx,y+dy,chr,col);
			return;
		}
		int posx = x+dx;
		int posy = y+dy;
		// we'll have to push unless we see a wall and until we have pushiums
		while(getBlock(posx,posy).isPushable())
		{
			posx+=dx;
			posy+=dy;
		}
		if(!getBlock(posx,posy).isEmpty()) return;
		int tx = posx-(x+dx);
		int ty = posy-(y+dy);
		if(tx<0) tx=-tx;
		if(ty<0) ty=-ty;
		if(tx==0) tx=1;
		if(ty==0) ty=1;
		pushMultiple(x+dx,y+dy,tx,ty,dx,dy);
		setPushable(x+dx,y+dy,chr,col);
		if(isServer)
			setPushableNet(x+dx,y+dy,chr,col);
	}

	public boolean piston(int x, int y, int dx, int dy, boolean pull)
	{
		if((dx!=0 && dy!=0) || (dx==0 && dy==0)) return false; // don't do diagonals.
		if(dx>1 || dx<-1 || dy>1 || dy<-1) return false; // no.
		if(!getBlock(x+dx,y+dy).isPistonable()) return false;
		int posx = x+dx;
		int posy = y+dy;
		// we'll have to push unless we see a wall and until we have pushiums
		while(getBlock(posx,posy).isPistonable())
		{
			posx+=dx;
			posy+=dy;
		}
		if(!getBlock(posx,posy).isPistonEmpty()) return false;
		int tx = posx-(x+dx);
		int ty = posy-(y+dy);
		if(tx<0) tx=-tx;
		if(ty<0) ty=-ty;
		if(tx==0) tx=1;
		if(ty==0) ty=1;
		pushMultiple(x+dx,y+dy,tx,ty,dx,dy,pull);
		return true;
	}

	public void playSample(int x, int y, int id)
	{
		playSound(x,y,id+256);
	}
	public void playSound(int x, int y, int id)
	{
		if(isServer)
		{
			se.playSound(x,y,id,this);
		} else {
			game.playSound(x,y,id);
		}
	}
	public static final int[] xMovement = { -1, 1, 0, 0 };
	public static final int[] yMovement = { 0, 0, -1, 1 };
	private static final int[] wiriumChar = { 197,179,179,179,196,218,192,195,196,191,217,180,196,194,193,197};

	public int updateLook(CraftrBlock block)
	{
		if(block.getType()==4) return 206; // default char for Crossuh blocks
		// NOTE: server used getChar() here.
		// That bug has gone unnoticed because nobody bothered to put a pushium on a P-NAND. --GM
		if(block.getType()==3 && (block.getBlockChar()<24 || block.getBlockChar()>=28)) return 25; // default char for P-NANDs
		if(block.getType()!=2) return block.getBlockChar();
		CraftrBlock surroundingBlock;
		int wiriumNeighbourInfo = 0;
		for(int moveDir=0;moveDir<4;moveDir++)
		{
			surroundingBlock=getBlock(block.x+xMovement[moveDir],block.y+yMovement[moveDir]);
			if(surroundingBlock.isWiriumNeighbour()) wiriumNeighbourInfo|=(1<<(3-moveDir));
		}
		return wiriumChar[wiriumNeighbourInfo];
	}

	public void clearBlock(int x, int y)
	{
		try
		{ 
			int px = x&63;
			int py = y&63;
			grabChunk((x>>6),(y>>6)).clear(px, py);
		}
		catch(NoChunkMemException e)
		{
			System.out.println("Map cache too small!");
			if(!multiplayer) System.exit(1);
		}
		catch(NullPointerException e)
		{
			if(!multiplayer) System.exit(1);
		}
	}

	public void setBlock(int x, int y, byte[] data)
	{
		try
		{ 
			if(data.length>5 && data[5]!=0) setPushable(x,y,data[4],data[5]);
			int px = x&63;
			int py = y&63;
			//System.out.println("setBlock at chunk " + (x>>6) + "," + (y>>6) + ", pos " + px + "," + py);
			grabChunk((x>>6),(y>>6)).place(px,py,data[0],data[2],data[3],data[1]);
		}
		catch(NoChunkMemException e)
		{
			System.out.println("Map cache too small!");
			if(!multiplayer) System.exit(1);
		}
		catch(NullPointerException e)
		{
			if(!multiplayer) System.exit(1);
		}
	}

	public void setBlock(int x, int y, int t2, int p2, int ch2, int co2)
	{
		try
		{
			t2 &= 0xFF;
			if(t2==0xFF) setPushable(x,y,ch2,co2);
			int px = x&63;
			int py = y&63;
			//System.out.println("setBlock at chunk " + (x>>6) + "," + (y>>6) + ", pos " + px + "," + py);
			grabChunk((x>>6),(y>>6)).place(px,py,(byte)t2,(byte)ch2,(byte)co2,(byte)p2);
		}
		catch(NoChunkMemException e)
		{
			System.out.println("Map cache too small!");
			if(!multiplayer) System.exit(1);
		}
		catch(NullPointerException e)
		{
			//System.out.println("setBlock: no cached chunk near player found. ODD.");
			if(!multiplayer) System.exit(1);
		}
	}
	public void setBlock(int x, int y, byte t1, byte p1, byte ch1, byte co1)
	{
		try
		{ 
			if(t1==-1) setPushable(x,y,ch1,co1);
			int px = x&63;
			int py = y&63;
			grabChunk((x>>6),(y>>6)).place(px,py,t1,ch1,co1,p1);
		}
		catch(NoChunkMemException e)
		{
			System.out.println("Map cache too small!");
			if(!multiplayer) System.exit(1);
		}
		catch(NullPointerException e)
		{
			if(!multiplayer) System.exit(1);
		}
	}
	public void clearBlockNet(int x, int y)
	{
		try
		{ 
			int px = x&63;
			int py = y&63;
			se.out.writeByte(0x34);
			se.out.writeInt(x);
			se.out.writeInt(y);
			byte[] t = se.getPacket();
			se.sendAllOnMap(t,t.length,mapName);
		}
		catch(Exception e)
		{
			System.out.println("[MAP] setBlockNet exception!");
			e.printStackTrace();
		}
	}
	public void setBlockNet(int x, int y, byte t1, byte ch1, byte co1)
	{
		try
		{ 
			int px = x&63;
			int py = y&63;
			se.out.writeByte(0x33);
			se.out.writeByte((byte)255);
			se.out.writeInt(x);
			se.out.writeInt(y);
			se.out.writeByte(t1);
			se.out.writeByte(ch1);
			se.out.writeByte(co1);
			byte[] t = se.getPacket();
			se.sendAllOnMap(t,t.length,mapName);
		}
		catch(Exception e)
		{
			System.out.println("[MAP] setBlockNet exception!");
			e.printStackTrace();
		}
	}
	public void setPushableNet(int x, int y, byte ch1, byte co1)
	{
		setBlockNet(x,y,(byte)-1,ch1,co1);
	}
}
package server;
import common.*;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import java.util.concurrent.*;

public class CraftrClient implements Runnable
{
	public Socket socket;
	public DataOutputStream out;
	public ByteArrayOutputStream out2;
	private DataInputStream in;
	public int x, y, loginStage;
	public byte chr, col;
	public String nick;
	public CraftrMap map;
	public int id, version, dc;
	public boolean isRequestingChunk;
	public int rcX, rcY;
	public int rcP;
	public GZIPOutputStream rcin;
	public CraftrServer serv;
	public CraftrNetSender ns;
	public int ncol=0;
	public int health;
	public boolean op = false;
	public long frames = 0;
	public int pingsWaiting = 0;
	private CraftrAuth auth;
	public boolean passWait=false;
	public CraftrCopier cc;
	public CraftrWorld world;
	public boolean isCopying = false;
	public boolean isPasting = false;
	public int copyStage = 0;
	public int cx,cy,deaths;

	public CraftrClient(Socket s, CraftrMap m, int iz, CraftrServer se)
	{
		try
		{
			socket = s;
			serv = se;
			socket.setTcpNoDelay(true);
			map = m;
			id = iz;
			dc = 0;
			nick = "anonymous"; // ah yeah, default values
			chr = 2;
			col = 31;
			health = 5;
			x = 0;
			y = 0;
			loginStage = 0;
			in = new DataInputStream(socket.getInputStream());
			out2 = new ByteArrayOutputStream(65536);
			out = new DataOutputStream(out2);
			ns = new CraftrNetSender(socket.getOutputStream());
			Thread tns = new Thread(ns);
			tns.start();
			cc=new CraftrCopier();
			System.out.println("New user with ID " + id + " connected!");
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrServer client thread init error!");
			System.exit(1);
		} 
	}
	
	private int min(int a1, int a2)
	{
		if(a1>a2)
		{
			return a2;
		} else return a1;
	}

	public void kill()
	{
		if(!world.isPvP) return;
		health--;
		if(health<=0)
		{
			health = 5;
			deaths++;
			sendChatMsgAll("&c" + nick + "&c was killed!");
			teleport(world.spawnX,world.spawnY);
		}
		setHealth(health);
	}
	public void resetPvP()
	{
		deaths=0;
	}

	public void playSound(int tx,int ty, int val)
	{
		int ax=x-tx;
		int ay=y-ty;
		if(ax>=-128 && ax<128 && ay>=-128 && ay<128)
		{
			synchronized(out)
			{
				try
				{
					out.writeByte(0x60);
					out.writeByte((byte)ax);
					out.writeByte((byte)ay);
					out.writeByte((byte)val);
				}
				catch(Exception e){};
				sendPacket();
			}
		}
	}
	
	public void changeMap(CraftrMap nmap) // it's a friendly smile, on an open port
	{
		try
		{
			map.setPlayer(x,y,0);
			map.physics.players[id] = null;
			despawnPlayer();
			despawnOthers();
			map=nmap;
			world=serv.findWorld(map.mapName);
			x=world.spawnX;
			y=world.spawnY;
			map.physics.players[id] = new CraftrPlayer(x,y,chr,col,nick);
			map.setPlayer(x,y,1);
			spawnPlayer();
			spawnOthers();
			synchronized(out)
			{
				out.writeByte(0x80);	
				out.writeInt(x);
				out.writeInt(y);
				sendPacket();
			}
			setRaycasting(world.isRaycasted);
			setPvP(world.isPvP);
			sendChatMsgSelf("&aMap changed to &f" + nmap.mapName);
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal changeMap error! (i hope)");
			e.printStackTrace();
		}
	}

	public void changeNickname(String newn)
	{
		try
		{
			nick=newn;
			synchronized(out)
			{
				out.writeByte(0x26);
				out.writeByte(255);
				writeString(newn);
				sendPacket();
				out.writeByte(0x26);
				out.writeByte((byte)id);
				writeString(newn);
				serv.sendOthersOnMap(id,getPacket());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal changeNickname error!");
		}
	}
	
	public void sendChatMsgSelf(String m)
	{
		try
		{
			String m2 = m;
			synchronized(out)
			{
				while(m2.length()>40)
				{
					out.writeByte(0x41);
					out.writeByte((byte)id);
					writeString(m2.substring(0,40));
					m2=m2.substring(40);
				}
				out.writeByte(0x41);
				out.writeByte((byte)id);
				writeString(m2);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal sendChatMsgSelf error!");
		}
	}

	public void sendChatMsgID(String m, int i)
	{
		try
		{
			String m2 = m;
			synchronized(out)
			{
				while(m2.length()>40)
				{
					out.writeByte(0x41);
					out.writeByte((byte)i);
					writeString(m2.substring(0,40));
					m2=m2.substring(40);
				}
				out.writeByte(0x41);
				out.writeByte((byte)i);
				writeString(m2);
				serv.clients[i].sendPacket(getPacket());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal sendChatMsgSelf error!");
		}
	}
	
	public void sendChatMsgAll(String m)
	{
		try
		{
			synchronized(out)
			{
				String m2=m;
				while(m2.length()>40)
				{
					out.writeByte(0x41);
					out.writeByte((byte)id);
					writeString(m2.substring(0,40));
					m2=m2.substring(40);
				}
				out.writeByte(0x41);
				out.writeByte((byte)id);
				writeString(m2);
				serv.sendAll(getPacket());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal sendChatMsgAll error!");
		}
	}
	
	public void kick()
	{
		kick("Kicked!");
	}
	
	public void kick(String msg)
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0xF5);
				writeString(msg);
				sendPacket();
			}
			Thread.sleep(100);
			disconnect();
			System.out.println("User " + id + " was kicked! (" + msg + ")");
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrKick error!");
			disconnect();
		}
	}
	public String readString()
	{
		try
		{
			int la = in.readUnsignedByte();
			byte[] t = new byte[la];
			in.read(t,0,la);
			return new String(t);
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal CraftrServer readString error!");
			return "";
		} 
	}
	
	public void teleport(int tx, int ty)
	{
		try
		{
			x=tx;
			y=ty;
			map.physics.players[id].px=x;
			map.physics.players[id].py=y;
			synchronized(out)
			{
				out.writeByte(0x24);
				out.writeByte(255);
				out.writeInt(x);
				out.writeInt(y);
				sendPacket();
				out.writeByte(0x24);
				out.writeByte((byte)id);
				out.writeInt(x);
				out.writeInt(y);
				serv.sendOthersOnMap(id,getPacket());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal teleport error!");
			e.printStackTrace();
		}
	}
	
	public void writeString(String s)
	{
		try
		{
			byte[] t = s.getBytes();
			synchronized(out)
			{
				out.writeByte(s.length());
				out.write(t,0,s.length());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal CraftrServer writeString error!");
			try
			{
				synchronized(out){out.writeByte(0x00);}
			}
			catch(Exception ee)
			{
				System.out.println("Fatal CraftrServer writeString error!");
				disconnect();
			}
		} 
	}
	
	public void sendPacket()
	{
		sendPacket(getPacket());
	}
	
	public void sendPacket(byte[] t)
	{
		try
		{
			synchronized(ns.packets)
			{
				ns.packets.offer(t);
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrServer sendPacket(byte[]) error!");
			System.exit(1);
		} 
	}
	
	public byte[] getPacket()
	{
		byte[] t = out2.toByteArray();
		out2.reset();
		return t;
	}

	public void sendOpPacket(int val)
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x28);
				out.writeShort((short)val*42);
				sendPacket();
			}
		}
		catch(Exception e){}
	}
	public int abs(int a)
	{
		if(a<0) return -a; else return a;
	}
	public void disconnect()
	{
		System.out.println("User " + id + " has disconnected!");
		dc = 1;
		ns.isRunning=false;
		try
		{
			map.setPlayer(x,y,0);
			despawnPlayer();
			sendChatMsgAll(nick + " has left.");
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal CraftrServer client thread DC packet send error!");
			e.printStackTrace();
		}
	}
	public void setRaycasting(boolean r)
	{
		try
		{
			synchronized(out)
			{
				if(r) out.writeByte(0x82);
				else out.writeByte(0x81);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal setRaycasting error!");


		}
	}

	public void despawnPlayer()
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x22);
				out.writeByte((byte)id);
				serv.sendOthersOnMap(id,getPacket());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal despawnPlayer error!");
		}
	}

	public void spawnPlayer()
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x20);
				out.writeByte(id);
				writeString(nick);
				out.writeInt(x);
				out.writeInt(y);
				out.writeByte(chr);
				out.writeByte(col);
				out.writeByte(ncol); 
				serv.sendOthersOnMap(id,getPacket());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal spawnPlayer error");
		}
	}

	public void spawnOthers()
	{
		try
		{
			for(int pli=0;pli<255;pli++)
			{
				if(pli != id && serv.clients[pli] != null && serv.clients[pli].dc == 0 && serv.clients[pli].map == map)
				{
					synchronized(out)
					{
						out.writeByte(0x20);
						out.writeByte(serv.clients[pli].id);
						writeString(serv.clients[pli].nick);
						out.writeInt(serv.clients[pli].x);
						out.writeInt(serv.clients[pli].y);
						out.writeByte(serv.clients[pli].chr);
						out.writeByte(serv.clients[pli].col);
						out.writeByte(serv.clients[pli].ncol);
						sendPacket();
					}
				}	
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal spawnOthers error");
		}
	}

	public void setHealth(int h)
	{
		health = h;
		try
		{
			synchronized(out)
			{
				out.writeByte(0x91);
				out.writeByte((byte)h);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal setHealth error");
		}
	}

	public void setPvP(boolean mpvp)
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x92);
				if(mpvp) out.writeByte(1);
				else out.writeByte(0);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal setPvP error");
		}
	}

	public void despawnOthers()
	{
		try
		{
			for(int pli=0;pli<255;pli++)
			{
				if(pli != id && serv.clients[pli] != null && serv.clients[pli].dc == 0 && serv.clients[pli].map == map)
				{
					synchronized(out)
					{
						out.writeByte(0x22);
						out.writeByte(serv.clients[pli].id);
						sendPacket();
					}
				}	
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal spawnOthers error");
		}
	}
	// This game is a TEMPORARY MEASURE!!! -asie
	public void run()
	{
		// Inner loop.
		try
		{
			while(dc == 0)
			{
				int len = 1;
				int packets = 0;
				while(len>0 && dc==0)
				{
					byte[] buf = new byte[1];
					if(!socket.isConnected() || !socket.isBound() || !ns.isRunning) disconnect();
					else if(in.available() > 0) len = in.read(buf,0,1);
					else len = 0;
					if(len > 0 && packets < 32)
					{
						packets++;
						switch((int)(buf[0]&0xFF))
						{
							case 0x0F:
								if(loginStage>=1)
						
		{
									readString();
									readString();
									in.readByte();
									in.readByte();
									in.readInt();
									in.readByte();
									in.readByte();
									break;
								} else {
									loginStage = 1;
									nick = readString();
									readString();
									in.readByte();
									in.readByte();
									version = in.readInt();
									chr = in.readByte();
									col = in.readByte();
									x=serv.spawnX;
									y=serv.spawnY;
									if(serv.anonMode) nick = "User"+id;
									System.out.println("User " + id + " (IP " + socket.getInetAddress().getHostAddress() + ") connected!");
									if(nick.length()>20)
									{
										kick("Invalid nickname!");
									}
									else if(version!=CraftrVersion.getProtocolVersion())
									{
										kick("Invalid protocol/game version!");
									}
									else if(serv.isBanned(socket.getInetAddress().getHostAddress()))
									{
										kick("You're banned!");
									}
									else
									{
										if(serv.isOp(socket.getInetAddress().getHostAddress()))
										{
											op=true;
											System.out.println("User " + id + " is an Op!");
										}
										synchronized(out)
										{
											out.writeByte(0x01);
											out.writeInt(x);
											out.writeInt(y);
											writeString(nick);
											out.writeShort(op?42:0);
	
											sendPacket();
										}
										if(serv.passOn)
										{
											auth = new CraftrAuth(serv.pass);
											byte[] ae = auth.encrypt();
											synchronized(out)
											{
												out.writeByte(0x50);
												out.write(ae,0,32);
												sendPacket();
											}
											passWait=true;
										}
										sendChatMsgAll(nick + " has joined.");
										setRaycasting(world.isRaycasted);
										map.physics.players[id] = new CraftrPlayer(x,y,chr,col,nick);
										map.setPlayer(x,y,1);
										spawnPlayer();
										spawnOthers();
									}
								}
								break;
							case 0x10:
								rcX = in.readInt();
								rcY = in.readInt();
								System.out.println("[ID " + id + "] sending chunk " + rcX + "," + rcY);
								byte[] t = new byte[16384];
								int z10;
								synchronized(map.chunks)
								{
									t = map.grabChunk(rcX,rcY).saveByteNet();
									z10 = map.findChunkID(rcX,rcY);
								}
								ByteArrayOutputStream bain = new ByteArrayOutputStream();
								rcin = new GZIPOutputStream(bain);
								rcin.write(t,0,t.length);
								rcin.finish();
								rcin.close();
								byte[] t2 = bain.toByteArray();
								int pl = t2.length;
								int pp = t2.length;
								synchronized(out)
								{
									out.writeByte(0x11);
									out.writeByte(0x01); // type
									out.writeInt(rcX);
									out.writeInt(rcY);
									out.writeInt(t2.length);
									sendPacket();
								}
								while(pl>0)
								{
									int pls = 1000;
									if(pl<pls) pls=pl;
									synchronized(out)
									{
										out.writeByte(0x12);
										out.writeShort(pls);
										out.write(t2,pp-pl,pls);
										sendPacket();
										Thread.sleep(1);
									}
									pl -= pls;
								}
								synchronized(map.chunks) { if(map.chunks[z10].xpos == rcX && map.chunks[z10].ypos == rcY) map.chunks[z10].isUsed=false; }
								synchronized(out)
								{
									out.writeByte(0x13);
									sendPacket();
								}
								break;
							case 0x23:
								byte[] ta = new byte[4];
								ta[0] = 0x21;
								ta[1] = (byte)id;
								ta[2] = in.readByte();
								ta[3] = in.readByte();
								if(passWait) break;
								if(abs(ta[2])>4 || abs(ta[3])>4)
								{
									kick("Invalid movement!");
								}
								serv.sendOthersOnMap(id,ta,4);
								map.setPlayer(x,y,0);
								x+=ta[2];
								y+=ta[3];
								map.physics.players[id].px=x;
								map.physics.players[id].py=y;
								map.setPlayer(x,y,1);
								break;
							case 0x25:
								if(world.isPvP) break;
								map.setPlayer(x,y,0);
								if(map==serv.map)
								{
									map.setPlayer(serv.spawnX,serv.spawnY,1);
									teleport(serv.spawnX,serv.spawnY);
								} else {
									map.setPlayer(world.spawnX,world.spawnY,1);
									teleport(world.spawnX,world.spawnY);
								}
								break;
							case 0x28:
								int x29 = in.readInt();
								int y29 = in.readInt();
								if(passWait) break;
								if(abs(x29-x)<=16 || abs(y29-y)<=16)
								{
									x=x29;
									y=y29;
									map.physics.players[id].px=x;
									map.physics.players[id].py=y;
									synchronized(out)
									{
										out.writeByte(0x24);
										out.writeByte(id);
										out.writeInt(x29);
										out.writeInt(y29);
										serv.sendOthersOnMap(id,getPacket());
									}
								}
								break;
							case 0x2A:
								disconnect();
								break;
							case 0x2C:
							case 0x2D:
							case 0x2E:
							case 0x2F:
								int dir2f = buf[0]&0x03;
								int dx2f = map.xMovement[dir2f];
								int dy2f = map.yMovement[dir2f];
								byte[] ta2f = new byte[4];
								ta2f[0] = buf[0];
								ta2f[1] = (byte)id;
								if(passWait) break;
								serv.sendOthersOnMap(id,ta2f,2);
								map.setPlayer(x,y,0);
								x+=dx2f;
								y+=dy2f;
								map.physics.players[id].px=x;
								map.physics.players[id].py=y;
								map.setPlayer(x,y,1);
								break;
							case 0x30:
								int ax = in.readInt();
								int ay = in.readInt();
								byte at = in.readByte();
								byte ach = in.readByte();
								byte aco = in.readByte();
								if(passWait) break;
								byte[] zc = map.getBlock(ax,ay).getBlockData();
								if((op && (isCopying || isPasting)) || (serv.mapLock && !op))
								{
									out.writeByte(0x31);
									out.writeByte((byte)id);
									out.writeInt(ax);
									out.writeInt(ay);
									out.writeByte(zc[0]);
									out.writeByte(zc[2]);
									out.writeByte(zc[3]);
									sendPacket();
									if(isCopying)
									{
										if(copyStage == 0)
										{
											sendChatMsgSelf("Click on the other corner.");
											cx=ax;
											cy=ay;
											copyStage++;
										}
										else if(copyStage == 1)
										{
											isCopying=false;
											int tcx = ax;
											int tcy = ay;
											int ts = 0;
											if(tcx<cx) { ts=tcx;tcx=cx;cx=ts; }
											if(tcy<cy) { ts=tcy;tcy=cy;cy=ts; }
											int tcxs = abs(tcx-cx)+1;
											int tcys = abs(tcy-cy)+1;
											if(tcxs>0 && tcxs<=128 && tcys>0 && tcys<=128)
											{
												cc.copy(map,cx,cy,tcxs,tcys);
												sendChatMsgSelf("Copied.");
											} else sendChatMsgSelf("Copy error: Invalid size (" + tcxs + ", " + tcys + ")");
										}
									}
									else if(isPasting)
									{
										cc.paste(map,ax,ay);
										sendChatMsgSelf("Pasted.");
										System.out.println("[ID " + id + "] Pasted at " + ax + "," + ay + "!");
										isPasting=false;
									}
								}
								else
								{
									int tat = (int)(at&0xFF);
									if((tat > CraftrBlock.maxType && at != -1) || !CraftrBlock.isPlaceable(tat))
									{
										kick("Invalid block type!");
									}
									byte[] t33 = new byte[4];
									t33[0] = at;
									t33[3] = aco;
									t33[2] = ach;
									t33[1] = (byte)CraftrBlock.getParam(tat);
									while(map.maplock) { try{ Thread.sleep(1); } catch(Exception e) {} }
									map.modlock=true;
	 								synchronized(map)
	 								{
	 									if(at == -1)
	 									{
	 										map.setPushable(ax,ay,ach,aco);
	 									} else {
	 										map.setBlock(ax,ay,t33[0],t33[1],t33[2],t33[3]);
	 										map.setPushable(ax,ay,(byte)0,(byte)0);
	 									}
	 								}
									map.physics.addBlockToCheck(new CraftrBlockPos(ax,ay));
									for(int i=0;i<4;i++)
									{
										map.physics.addBlockToCheck(new CraftrBlockPos(ax+map.xMovement[i],ay+map.yMovement[i]));
									}
									synchronized(out)
									{
										out.writeByte(0x31);
										out.writeByte((byte)id);
										out.writeInt(ax);
										out.writeInt(ay);
										out.writeByte(t33[0]);
										out.writeByte(t33[2]);
										out.writeByte(t33[3]);
										serv.sendOthersOnMap(id,getPacket());
	 									if(at != -1 && zc[5] != 0)
	 									{
	 										out.writeByte(0x31);
	 										out.writeByte((byte)id);
	 										out.writeInt(ax);
	 										out.writeInt(ay);
	 										out.writeByte(-1);
	 										out.writeByte(0);
	 										out.writeByte(0);
	 										serv.sendOthersOnMap(id,getPacket());
	 									}
									}
									map.modlock=false;
								}
								break;
							case 0x40:
								String al = readString();
								System.out.println("<" + nick + "> " + al);
								String alt = serv.parseMessage(al,id);
								if(alt.equals("$N") && !al.equals("")) sendChatMsgAll("<" + nick + "> " + al);
								else if (!alt.equals("")) sendChatMsgSelf(alt);
								break;
							case 0x51:
								byte[] t51 = new byte[32];
								in.read(t51,0,32);
								if(!auth.testDecrypt(t51))
								{
									kick("Incorrect password!");
								}
								else passWait=false;
 								break;
							case 0x70:
								int xb = in.readInt();
								int yb = in.readInt();
								byte bt = in.readByte();
								map.setBullet(xb,yb,bt);
								map.setBulletNet(xb,yb,bt);
								map.physics.addBlockToCheck(new CraftrBlockPos(xb,yb));
								for(int i=0;i<4;i++) map.physics.addBlockToCheck(new CraftrBlockPos(xb+map.xMovement[i],yb+map.yMovement[i]));
								break;
 							case 0xE0:
 								{
 									int lolx = this.x;
 									int loly = this.y;
 									int lolvx = in.readByte();
 									int lolvy = in.readByte();

 									if((lolvx != 0 && lolvy != 0) || (lolvx == 0 && lolvy == 0))
 										kick("Invalid touch distance!");
 									else if(lolvx < -1 || lolvx > 1 || lolvy < -1 || lolvy > 1)
 										kick("Invalid touch distance!");
 									else {
 										byte[] dq;
 										boolean pa;
 										synchronized(map)
 										{
 											dq = map.getBlock(lolx+lolvx,loly+lolvy).getBlockData();
 											pa = map.pushAttempt(lolx+lolvx,loly+lolvy,lolvx,lolvy);
 										}
 										if(pa)
 										{
 											map.setPlayer(x,y,0);
 											x = lolx+lolvx;
 											y = loly+lolvy;
 											synchronized(out)
 											{
 												out.writeByte(0x32);
 												out.writeByte(255);
 												out.writeInt(lolx+lolvx);
 												out.writeInt(loly+lolvy);
 												out.writeByte((byte)lolvx);
 												out.writeByte((byte)lolvy);
 												out.writeByte(dq[4]);
 												out.writeByte(dq[5]);
 												sendPacket();
 												out.writeByte(0x32);
 												out.writeByte(id);
 												out.writeInt(lolx+lolvx);
 												out.writeInt(loly+lolvy);
 												out.writeByte((byte)lolvx);
 												out.writeByte((byte)lolvy);
 												out.writeByte(dq[4]);
 												out.writeByte(dq[5]);
 												serv.sendOthersOnMap(id,getPacket());
 												map.setPlayer(this.x,this.y,1);
 												map.setPlayer(this.x+lolvx,this.y+lolvy,1);
 											}
 										}
 									}
 								}
								break;
							case 0xF0:
								synchronized(out)
								{
									out.writeByte(0xF1);
									sendPacket();
								}
								break;
							case 0xF1:
								pingsWaiting--;
								break;
							default:
								System.out.println("Unknown packet " + (int)(buf[0]&0xFF) + "!");
								break; // Ignore.
						}
					}
				}
				Thread.sleep(5);
				frames++;
				if(frames%625==0) // every 10 seconds
				{
					synchronized(out)
					{
						out.writeByte(0xF0);
						sendPacket();
					}
					pingsWaiting++;
					if(pingsWaiting>20) disconnect();
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrServer client thread loop error!");
			e.printStackTrace();
			dc = 1;
			ns.isRunning=false;
		}
	}
	// for people who didn't get the earlier comment, it's an inside joke of sorts
}
package server;
import common.*;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.*;
import java.text.*;

public class CraftrServer extends CraftrServerShim
{
	public ServerSocket servsock;
	public CraftrClient[] clients;
	public String name = "64pixels server";
	public boolean run; 
	public CraftrMap map;
	public CraftrInput ci;
	public ByteArrayOutputStream out2;
	public boolean anonMode;
	public String[] op_ips;
	public String[] ban_ips;
	public String[] world_names;
	public ArrayList<CraftrWorld> worlds;
	public int spawnX=0;
	public int privmode=0;
	public int spawnY=0;
	public CraftrConfig config;
	public int nagle=0;
	public int map_tps=10;
	public int map_save_duration=10;
	public int map_cache_size=128;
	public int tpforall=0;
	public boolean passOn = false;
	public String pass;
	public boolean opPassOn = false;
	public String opPass;
	public CraftrWarps warps;
	public int po = 25566;
	public boolean mapLock = false;
	public CraftrWorld world;

	public int countPlayers()
	{
		int t = 0;
		for(int i=0;i<255;i++)
		{
			if(clients[i]!=null && clients[i].dc==0) t++;
		}
		return t;
	}

	public void changeMainSpawnXY(int x, int y)
	{
		spawnX=x; world.spawnX=x;
		spawnY=y; world.spawnY=y;
	}

	public void kill(int pid)
	{
		int tX=clients[pid].world.spawnX;
		int tY=clients[pid].world.spawnY;
		if(pid>=0 && pid<256 && clients[pid]!=null && clients[pid].dc==0 && (clients[pid].x!=tX || clients[pid].y!=tY))
		{
			clients[pid].kill();
		}
	}

	public CraftrWorld findWorld(String name)
	{
		for(int i=0;i<worlds.size();i++)
		{
			if(worlds.get(i).name.equalsIgnoreCase(name))
			{
				return worlds.get(i);
			}
		}
		return null;
	}

	public boolean isOp(String ip)
	{
		for(String s: op_ips)
		{
			if(s.equals(ip.toLowerCase())) return true;
		}
		return false;
	}
	
	public boolean isBanned(String ip)
	{
		for(String s: ban_ips)
		{
			if(s.equals(ip.toLowerCase())) return true;
		}
		return false;
	}
	
	public void addOp(String ip)
	{
		ArrayList<String> t = new ArrayList<String>(op_ips.length+1);
		for(String s:op_ips)
		{
			if(s.equals(ip.toLowerCase())) return;
			t.add(s);
		}
		t.add(ip.toLowerCase());
		String[] z = new String[t.size()];
		int i = 0;
		for(String s : t)
		{
			z[i++]=s.toLowerCase();
		}
		op_ips = z;
	}
	
	public void removeOp(String ip)
	{
		ArrayList<String> t = new ArrayList<String>(op_ips.length+1);
		for(String s:op_ips)
		{
			t.add(s);
		}
		t.remove(ip.toLowerCase());
		String[] z = new String[t.size()];
		int i = 0;
		for(String s : t)
		{
			z[i++]=s.toLowerCase();
		}
		op_ips = z;
	}
	
	public void addWorld(String world)
	{
		ArrayList<String> t = new ArrayList<String>(world_names.length+1);
		for(String s:world_names)
		{
			if(s.equals(world.toLowerCase())) return;
			t.add(s);
		}
		t.add(world.toLowerCase());
		String[] z = new String[t.size()];
		int i = 0;
		for(String s : t)
		{
			z[i++]=s.toLowerCase();
		}
		world_names = z;
	}
	
	public void removeWorld(String world)
	{
		ArrayList<String> t = new ArrayList<String>(world_names.length+1);
		for(String s:world_names)
		{
			t.add(s);
		}
		t.remove(world.toLowerCase());
		String[] z = new String[t.size()];
		int i = 0;
		for(String s : t)
		{
			z[i++]=s.toLowerCase();
		}
		world_names = z;
	}
	
	public void ban(String ip)
	{
		ArrayList<String> t = new ArrayList<String>(ban_ips.length+1);
		for(String s:ban_ips)
		{
			if(s.equals(ip.toLowerCase())) return;
			t.add(s);
		}
		t.add(ip);
		String[] z = new String[t.size()];
		int i = 0;
		for(String s : t)
		{
			z[i++]=s.toLowerCase();
		}
		ban_ips = z;
	}
	
	public void unban(String ip)
	{
		ArrayList<String> t = new ArrayList<String>(op_ips.length+1);
		for(String s:ban_ips)
		{
			t.add(s);
		}
		t.remove(ip.toLowerCase());
		String[] z = new String[t.size()];
		int i = 0;
		for(String s : t)
		{
			z[i++]=s.toLowerCase();
		}
		ban_ips = z;
	}
	
	
	public void saveWorldConfig(CraftrWorld world)
	{
		try
		{
			NumberFormat nf = NumberFormat.getNumberInstance();
			BufferedWriter out = new BufferedWriter(new FileWriter(map.saveDir + world.name + "/config.txt"));
			String s = "" ;
			s = "map-ticks=" + world.tickSpeed;
			out.write(s,0,s.length());
			out.newLine();
			s = "spawn-x=" + world.spawnX;
			out.write(s,0,s.length());
			out.newLine();
			s = "spawn-y=" + world.spawnY;
			out.write(s,0,s.length());
			out.close();	
		}
		catch (Exception e)
		{
			System.out.println("Error writing config data! " + e.getMessage());
		}
	}

	public void loadWorldConfig(CraftrWorld world)
	{
		try
		{
			NumberFormat nf = NumberFormat.getNumberInstance();
			CraftrConfig nc = new CraftrConfig(map.saveDir + world.name + "/config.txt");
			for(int i=0;i<nc.keys;i++)
			{
				String key = nc.key[i];
				String val = nc.value[i];
				System.out.println("Config key found: " + key);
				if(key.contains("spawn-x"))
				{
					world.spawnX = nf.parse(val).intValue();
				}
				else if(key.contains("spawn-y"))
				{
					world.spawnY = nf.parse(val).intValue();
				}
				else if(key.contains("map-ticks"))
				{
					world.changeTickSpeed(nf.parse(val).intValue());
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Error reading config data! " + e.getMessage());
		}
	}

	public void saveConfig()
	{
		try
		{
			NumberFormat nf = NumberFormat.getNumberInstance();
			BufferedWriter out = new BufferedWriter(new FileWriter(map.saveDir + "config.txt"));
			String s = "";
			if(CraftrNetSender.alg>0)
			{
				s = "send-algorithm=" + CraftrNetSender.alg;
				out.write(s,0,s.length());
				out.newLine();
			}
			if(nagle>0)
			{
				s = "use-nagle=" + nagle;
				out.write(s,0,s.length());
				out.newLine();
			}
			if(passOn)
			{
				s = "password=" + pass;
				out.write(s,0,s.length());
				out.newLine();
			}
			if(opPassOn)
			{
				s = "op-password=" + opPass;
				out.write(s,0,s.length());
				out.newLine();
			}
			if(anonMode)
			{
				s = "anonymous-mode=1";
				out.write(s,0,s.length());
				out.newLine();
			}
			if(map_cache_size!=128)
			{
				s = "map-cache-size="+map_cache_size;
				out.write(s,0,s.length());
				out.newLine();
			}
			s = "map-save-duration="+map_save_duration;
			out.write(s,0,s.length());
			out.newLine();
			s = "map-ticks=" + map_tps;
			out.write(s,0,s.length());
			out.newLine();
			s = "tp-for-all=" + tpforall;
			out.write(s,0,s.length());
			out.newLine();
			s = "port=" + po;
			out.write(s,0,s.length());
			out.newLine();
			s = "private-mode=" + privmode;
			out.write(s,0,s.length());
			out.newLine();
			s = "name=" + name;
			out.write(s,0,s.length());
			out.newLine();
			s = "spawn-x=" + spawnX;
			out.write(s,0,s.length());
			out.newLine();
			s = "spawn-y=" + spawnY;
			out.write(s,0,s.length());
			out.close();	
		}
		catch (Exception e)
		{
			System.out.println("Error writing config data! " + e.getMessage());
		}
	}
	
	public void loadConfig()
	{
		try
		{
			NumberFormat nf = NumberFormat.getNumberInstance();
			for(int i=0;i<config.keys;i++)
			{
				String key = config.key[i];
				String val = config.value[i];
				System.out.println("Config key found: " + key);
				if(key.contains("spawn-x"))
				{
					spawnX = nf.parse(val).intValue();
				}
				else if(key.contains("spawn-y"))
				{
					spawnY = nf.parse(val).intValue();
				}
				else if(key.contains("send-algorithm"))
				{
					CraftrNetSender.alg = nf.parse(val).intValue();
				}
				else if(key.contains("use-nagle"))
				{
					nagle = nf.parse(val).intValue();
				}
				else if(key.contains("map-ticks"))
				{
					map_tps=nf.parse(val).intValue();
				}
				else if(key.contains("tp-for-all"))
				{
					tpforall=nf.parse(val).intValue();
				}
				else if(key.contains("op-password"))
				{
					opPassOn=true;
					opPass=val;
				}
				else if(key.contains("password"))
				{
					passOn=true;
					pass=val;
				}
				else if(key.contains("anonymous-mode"))
				{
					if(nf.parse(val).intValue()>0) anonMode=true;
				}
				else if(key.contains("port"))
				{
					po = nf.parse(val).intValue();
				}
				else if(key.contains("map-cache-size"))
				{
					map_cache_size=nf.parse(val).intValue();
				}
				else if(key.contains("name"))
				{
					name=val;
				}
				else if(key.contains("private-mode"))
				{
					privmode=nf.parse(val).intValue();
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Error reading config data! " + e.getMessage());
		}
	}
	
	public String parseMessage(String al, int id)
	{
		if(al.length()<2) return "$N"; 
		if(!al.substring(0,1).equals("/")) return "$N";
		String[] cmdz = al.substring(1).split(" ");
		String[] cmd = new String[cmdz.length];
		for(int i=0;i<cmdz.length;i++)
		{
			cmd[i]=cmdz[i].toLowerCase();
		}
		if(cmd[0].equals("who") || cmd[0].equals("players") || cmd[0].equals("playerlist") || cmd[0].equals("list") || cmd[0].equals("users"))
		{
			String lol = "";
			int ap = 0;
			for(int i=0;i<255;i++)
			{
				if(clients[i] != null && clients[i].dc == 0)
				{
					if(ap>0)
					{
						lol+=", ";
					}
					ap++;
					lol+=clients[i].nick;
				}
			}
			return "&c" + ap + "/255&f - " + lol;
		}
		else if((cmd[0].equals("tp") || cmd[0].equals("teleport")) && id!=255 && (tpforall>0 || clients[id].op))
		{
			int t = findByNick(cmd[1]);
			if(t<0 || t>255)
			{
				return "No such nickname!";
			}
			else
			{
				if(clients[id].map!=clients[t].map) clients[id].changeMap(clients[t].map);
				clients[id].teleport(clients[t].x,clients[t].y);
				return "";
			}
		}
		else if(cmd[0].equals("warp") && id!=255)
		{
			int t = clients[id].world.warps.findWarpID(cmd[1]);
			if(t>=0)
			{
				CraftrWarp w = clients[id].world.warps.warps.get(t);
				clients[id].teleport(w.x,w.y);
				return "";
			}
			else
			{
				return "Warp not found!";
			}
		}
		else if(cmd[0].equals("warps"))
		{
			String wt = "Warps (map " + clients[id].world.name + "): ";
			for(int i=0;i<clients[id].world.warps.warps.size();i++)
			{
				if(clients[id].world.warps.warps.get(i)!=null)
				{
					if(i>0) wt+= ", ";
					wt += clients[id].world.warps.warps.get(i).name;
				}
			}
			return wt;
		}
		else if(cmd[0].equals("worlds") || cmd[0].equals("maps"))
		{
			String wt = "Worlds: ";
			for(int i=0;i<world_names.length;i++)
			{
				if(!world_names[i].startsWith("$"))
				{
					wt += world_names[i];
					wt += ", ";
				}
			}
			return wt.substring(0,wt.length()-2);
		}
		else if((cmd[0].equals("id") || cmd[0].equals("identify")) && id!=255)
		{
			if(!opPassOn) return "Identify disabled!";
			if(cmdz.length>1 && cmdz[1].equals(opPass))
			{
				clients[id].op=true;
				clients[id].sendOpPacket(1);
				return "You're opped now!";
			} else return "Incorrect op password! :(";
		}
		else if(cmd[0].equals("cmds") || cmd[0].equals("help"))
		{
			if(id == 255)
			{
				return "Commands: who warps kick nick deop save ban unban delwarp lock unlock worlds addworld delworld msg";
			}
			else if(clients[id].op)
			{

				return "Commands: who tp warp warps me kick fetch copy paste setspawn say nick op deop save ban unban setwarp delwarp id import export pvp lock unlock worlds addworld delworld load return msg raycast";
			}
			else
			{
				return "Commands: who " + ((tpforall!=0)?"tp ":"") + "warp warps me id worlds load return msg";
			}
		}
		else if(cmd[0].equals("me") && id!=255)
		{
			String st = "* " + clients[id].nick + " ";
			for(int i=1;i<cmdz.length;i++)
			{
				if(i>1) st += " ";
				st=st+cmdz[i];
			}
			while(st.length()>38)
			{
				String st2 = st.substring(0,38);
				st=st.substring(38,st.length());
				clients[id].sendChatMsgAll("&5"+st2);
			}
			clients[id].sendChatMsgAll("&5"+st);
			return "";
		}
		else if((cmd[0].equals("load") || cmd[0].equals("goto") || cmd[0].equals("l") || cmd[0].equals("join") || cmd[0].equals("world")) && id!=255)
		{ 
			CraftrWorld tm = findWorld(cmdz[1]);
			if(tm==null) return "No such world!";
			else
			{
				clients[id].changeMap(tm.map);
				if (cmd[1].startsWith("$")) clients[id].sendChatMsgAll("&e" + clients[id].nick + " loaded a secret map!");
				else if(!cmd[1].equals("map")) clients[id].sendChatMsgAll("&e" + clients[id].nick + " loaded map &f'" + cmdz[1] + "'!"); 
				else clients[id].sendChatMsgAll("&e" + clients[id].nick + " loaded the main map!");
			}
		}
		else if(cmd[0].equals("return") && id!=255)
		{
			clients[id].changeMap(map);
			clients[id].sendChatMsgAll("&e" + clients[id].nick + " loaded the main map!"); 
		}
		else if(cmd[0].equals("m") || cmd[0].equals("msg"))
		{
			if(cmd.length<3) return "No nickname/message specified!";
			int t = findByNick(cmd[1]);
			if(t<0 || t>255)
			{
				return "No such nickname!";
			}
			else
			{
				String msg = "";
				for(int i=2;i<cmdz.length;i++)
				{
					if(i>2) msg+=" ";
					msg+=cmdz[i];
				}
				String msg2 = "&a[PM] >&f" + clients[id].nick + ": " + msg;
				clients[id].sendChatMsgID(msg2,t);
				return msg2;
			}
		}
		else
		{
			if(id != 255 && !clients[id].op) return "$N";
			if(cmd[0].equals("kick"))
			{
				int t = findByNick(cmd[1]);
				if(t<0 || t>255)
				{
					return "No such nickname!";
				}
				else
				{
					clients[t].kick();
					if(id!=255) System.out.println("[KICK] user " + clients[t].nick + ", by user " + clients[id].nick);
					return clients[t].nick + " has been kicked.";
				}
			}
			else if(cmd[0].equals("fetch") && id !=255)
			{
				for(int i=1;i<cmd.length;i++)
				{
					int t = findByNick(cmd[i]);
					if(t<0 || t>255)
					{
						return "No such nickname!";
					}
					else
					{
						if(clients[id].map!=clients[t].map) clients[t].changeMap(clients[id].map);
						clients[t].teleport(clients[id].x,clients[id].y);
						clients[t].sendChatMsgSelf("Fetched by " + clients[id].nick + "!");
						return "User fetched!";
					}
				}
			}
			else if(cmd[0].equals("adddungeon"))
			{
				if(cmd.length<4) return "Usage: /adddungeon [world] [width] [height]";
				else if (findWorld(cmdz[1])==null)
				{
					return "World '" + cmdz[1] + "' does not exist.";
				}
				CraftrWorld w = findWorld(cmdz[1]);
				CraftrDungeonGenerator cdg = new CraftrDungeonGenerator();
				NumberFormat nf = NumberFormat.getNumberInstance();
				try
				{
					CraftrDungeonThread cdt = new CraftrDungeonThread(this,id,cdg,w.map,nf.parse(cmdz[2]).intValue(),nf.parse(cmdz[3]).intValue());
					Thread dt = new Thread(cdt);
					dt.start();
				}
				catch(Exception e)
				{
					return "&cERROR: &fInvalid arguments!";
				}
				return "Creating dungeon...";
			}
			else if(cmd[0].equals("raycast") && id!=255)
			{
				String tmap = "(map " + clients[id].world.name + ")";
				if(clients[id].map==map) tmap = "(main map)";
				if(clients[id].world.isRaycasted)
				{
					clients[id].world.isRaycasted=false;
					clients[id].sendChatMsgAll("&eVisibility raycasting OFF &f" + tmap);
					for(int i=0;i<255;i++)
					{
						if(clients[i] != null && clients[i].dc == 0 && clients[i].map == clients[id].map)
						{
							clients[i].setRaycasting(false);
						}
					}
				}
				else
				{
					clients[id].world.isRaycasted=true;
					clients[id].sendChatMsgAll("&eVisibility raycasting ON! &f" + tmap);
					for(int i=0;i<255;i++)
					{
						if(clients[i] != null && clients[i].dc == 0 && clients[i].map == clients[id].map)
						{
						    clients[i].setRaycasting(true);
						}
					}
				}
				return "";
			}
			else if(cmd[0].equals("pvp") && id!=255)
			{
				String tmap = "(map " + clients[id].world.name + ")";
				if(clients[id].map==map) tmap = "(main map)";
				else if (clients[id].world.name.startsWith("$")) tmap = "(secret map)";
				if(clients[id].world.isPvP)
				{
					clients[id].world.isPvP=false;
					clients[id].sendChatMsgAll("&ePvP mode OFF &f" + tmap);
					clients[id].sendChatMsgAll("&cDEATHS:");
					for(int i=0;i<255;i++)
					{
						if(clients[i] != null && clients[i].dc == 0 && clients[i].map == clients[id].map)
						{
							clients[id].sendChatMsgAll("&c" + clients[i].nick + "&7 - &e" + clients[i].deaths + " times");
							clients[i].setPvP(false);
						}
					}
				}
				else
				{
					clients[id].world.isPvP=true;
					clients[id].sendChatMsgAll("&ePvP mode ON! &f" + tmap);
					for(int i=0;i<255;i++)
					{
						if(clients[i] != null && clients[i].dc == 0)
						{
							clients[i].resetPvP();
							if(clients[i].map == clients[id].map) clients[i].setPvP(true);
						}
					}
				}
				return "";
			}
			else if(cmd[0].equals("lock"))
			{
				mapLock=true;
				clients[id].sendChatMsgAll("&cMap locked.");
				return "";
			}
			else if(cmd[0].equals("unlock"))
			{
				mapLock=false;
				clients[id].sendChatMsgAll("&aMap unlocked!");
				return "";
			}
			else if(cmd[0].equals("copy") && id!=255)
			{
				clients[id].copyStage=0;
				clients[id].isCopying=true;
				clients[id].isPasting=false;
				return "Click on the first corner.";
			}
			else if(cmd[0].equals("import") && id!=255)
			{
				return clients[id].cc.load(cmd[1]);
				
			}
			else if(cmd[0].equals("export") && id!=255)
			{
				clients[id].cc.save(cmd[1]);
				return "Exported! (i hope)";
			}
			else if(cmd[0].equals("paste") && id!=255)
			{
				clients[id].isCopying=false;
				clients[id].isPasting=true;
				return "Click on the top-left destination corner.";
			}
			else if(cmd[0].equals("setspawn") && id!=255)
			{
				if(clients[id].map!=map)
				{
					clients[id].world.spawnX = clients[id].x;
					clients[id].world.spawnY = clients[id].y;
				}
	 			else
				{
					changeMainSpawnXY(clients[id].x,clients[id].y);
				}
				return "New spawn set at [" + clients[id].x + "," + clients[id].y + "].";
			}
			else if(cmd[0].equals("say") && id!=255)
			{
				String st = "";
				for(int i=1;i<cmdz.length;i++)
				{
					if(i>1) st += " ";
					st=st+cmdz[i];
				}
				while(st.length()>38)
				{
					String st2 = st.substring(0,38);
					st=st.substring(38,st.length());
					clients[id].sendChatMsgAll("&7"+st2);
				}
				clients[id].sendChatMsgAll("&7"+st);
				return "";
			}
			else if(cmd[0].equals("nick"))
			{
				if(cmd.length>2)
				{
					int t = findByNick(cmd[1]);
					if(t<0 || t>255)
					{
						return "No such nickname!";
					}
					else
					{
						String tt = clients[t].nick;
						clients[t].changeNickname(cmd[2]);
						if(id!=255) clients[id].sendChatMsgAll("User " + tt + " is now known as " + cmd[2]);
						return "Nickname of user " + tt + " changed.";
					}
				}
				else if(cmd.length>1 && id!=255)
				{
					String tt = clients[id].nick;
					clients[id].changeNickname(cmd[1]);
					clients[id].sendChatMsgAll("User " + tt + " is now known as " + cmd[1]);
					return "You're now known as " + cmd[1];	
				}
				else return "Not enough parameters!";
			}
			else if(cmd[0].equals("op"))
			{
				for(int i=1;i<cmd.length;i++)
				{
					int t = findByNick(cmd[i]);
					if(t<0 || t>255)
					{
						return "No such nickname!";
					}
					else
					{
						addOp(clients[t].socket.getInetAddress().getHostAddress());
						clients[t].sendChatMsgSelf("You're opped now!");
						clients[t].op=true;
						clients[t].sendOpPacket(1);
						saveNamesFile(op_ips,"ops.txt");
					}
				}
			}
			else if(cmd[0].equals("addworld"))
			{
				if(cmd[1].equalsIgnoreCase("map"))
				{
					return "This map name cannot be used.";
				}
				else if (findWorld(cmdz[1])!=null)
				{
					return "World '" + cmdz[1] + "' already exists.";
				}
				addWorld(cmdz[1]);
				CraftrMap tm = new CraftrMap(true,map_cache_size,cmdz[1]);
				tm.checkDirs(0,0);
				tm.se = this;
				CraftrWorld w = new CraftrWorld(cmdz[1],tm,map_tps, new CraftrWarps());
				loadWorldConfig(w);
				worlds.add(w);
				saveNamesFile(world_names,"worlds.txt");
				return "World '" + cmdz[1] + "' added.";
			}
			else if(cmd[0].equals("delworld"))
			{
				if(cmd[1].equalsIgnoreCase("map"))
				{
					return "This map cannot be deleted.";
				}
				else if (findWorld(cmdz[1])==null)
				{
					return "World '" + cmdz[1] + "' doesn't exist.";
				}
				removeWorld(cmdz[1]);
				saveNamesFile(world_names,"worlds.txt");
				for(int i=0;i<255;i++)
				{
					if(clients[i]!=null && clients[i].dc==0 && clients[i].map==findWorld(cmdz[1]).map)
					{
						clients[i].changeMap(map);
					}
				}
				if(findWorld(cmdz[1])!=null)
				{
					saveWorldConfig(findWorld(cmdz[1]));
					worlds.remove(findWorld(cmdz[1]));
				}
				return "World '" + cmdz[1] + "' deleted.";
			}
			else if(cmd[0].equals("deop"))
			{
				for(int i=1;i<cmd.length;i++)
				{
					int t = findByNick(cmd[i]);
					if(t<0 || t>255)
					{
						return "No such nickname!";
					}
					else
					{
						removeOp(clients[t].socket.getInetAddress().getHostAddress());
						clients[t].sendChatMsgSelf("You're not opped anymore.");
						clients[t].op=false;
						clients[t].sendOpPacket(0);
						saveNamesFile(op_ips,"ops.txt");
					}
				}
			}
			else if(cmd[0].equals("save") || cmd[0].equals("savemap"))
			{
				saveMap();
				System.out.println("[ID " + id + "] Map saved by user " + clients[id].nick + "!");
				return "Map saved!";
			}
			else if(cmd[0].equals("ban"))
			{
				for(int i=1;i<cmd.length;i++)
				{
					int t = findByNick(cmd[i]);
					if(t<0 || t>255)
					{
						return "No such nickname!";
					}
					else
					{
						ban(clients[t].socket.getInetAddress().getHostAddress());
						saveNamesFile(ban_ips,"bans.txt");
						clients[t].kick("Banned!");
						if(id!=255) System.out.println("[BAN] user " + clients[t].nick + ", by user " + clients[id].nick);
					}
				}
			}
			else if(cmd[0].equals("unban"))
			{
				for(String s: ban_ips)
				{
					if(s.equals(cmd[1]))
					{
						unban(s);
						saveNamesFile(ban_ips,"bans.txt");
						return "Person unbanned!";
					}
				}
				return "IP not found!";
			}
			else if(cmd[0].equals("setwarp") && id!=255)
			{
				int t = clients[id].world.warps.findWarpID(cmd[1]);
				if(t>=0)
				{
					clients[id].world.warps.warps.get(t).x=clients[id].x;
					clients[id].world.warps.warps.get(t).y=clients[id].y;
					clients[id].world.saveWarps();
					return "Warp location changed.";
				}
				else
				{
					clients[id].world.warps.warps.add(new CraftrWarp(clients[id].x,clients[id].y,cmd[1]));
					clients[id].world.saveWarps();
					return "New warp added.";
				}
			}
			else if(cmd[0].equals("delwarp"))
			{
				int t = clients[id].world.warps.findWarpID(cmd[1]);
				if(t>=0)
				{
					clients[id].world.warps.warps.remove(t);
					clients[id].world.saveWarps();
					return "Warp removed.";
				}
				else
				{
					return "Warp not found!";
				}
			}
			else
			{
				return "No such command!";
			}
		}
		return "";
	}
	
	public String[] readNamesFile(String name)
	{
		try
		{
			FileInputStream fis = new FileInputStream(name);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			ArrayList<String> a = new ArrayList<String>(64);
			String t = "";
			while(true)
			{
				t = br.readLine();
				if(t==null)break;
				a.add(t);
			}
			String[] z = new String[a.size()];
			int i = 0;
			for(String s : a)
			{
				z[i++]=s.toLowerCase();
			}
			fis.close();
			return z;
		}
		catch(Exception e)
		{
			System.out.println("Couldn't read " + name + "!");
			e.printStackTrace();
			String[] z = new String[0];
			return z;
		}
	}
	

	public void saveNamesFile(String[] data, String name)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(name);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			for(String s:data)
			{
				bw.write(s,0,s.length());
				bw.newLine();
			}
			bw.close();
			fos.close();
		}
		catch(Exception e)
		{
			System.out.println("Couldn't write " + name + "!");
			e.printStackTrace();
		}
	}
	
	public CraftrServer(String[] args)
	{
		try
		{
			config = new CraftrConfig("config.txt");
			loadConfig();
			op_ips=readNamesFile("ops.txt");
			System.out.println(op_ips.length + " op IPs!");
			ban_ips=readNamesFile("bans.txt");
			System.out.println(ban_ips.length + " banned IPs!");
			map = new CraftrMap(true,map_cache_size,"map");
			map.se = this;
			warps = new CraftrWarps();
			world = new CraftrWorld("map",map,map_tps,warps);
			map.checkDirs(0,0);
			worlds=new ArrayList<CraftrWorld>();
			world_names=readNamesFile("worlds.txt");
			System.out.println(world_names.length + " worlds!");
			worlds.add(world);
			for(String wn : world_names)
			{
				CraftrMap tm = new CraftrMap(true,map_cache_size,wn);
				tm.se = this;
				tm.checkDirs(0,0);
				CraftrWorld w = new CraftrWorld(wn,tm,map_tps,new CraftrWarps());
				loadWorldConfig(w);
				worlds.add(w);
			}
			if(args.length>0)
			{
				for(int i=0;i<args.length;i++)
				{
					if(args[i].equals("/a"))
					{
						anonMode=true;
					}
					else if(args[i].equals("/h"))
					{
						System.out.println("64px-srvr\nUsage: 64px-srvr [params]\n\nparams - parameters:\n    /a - anonymous mode (default nicknames) (off by default)\n    /h - show help\n    /p port - change port (25566 is default)");
						System.exit(0);
					}
					else if(args[i].equals("/p"))
					{
						i++;
						po = new Integer(args[i]).intValue();
					}
				}
			}
			servsock = new ServerSocket(po);
			clients = new CraftrClient[255];
			out2 = new ByteArrayOutputStream(2048);
			out = new DataOutputStream(out2);
		}
		catch(Exception e)
		{

			System.out.println("Fatal CraftrServer init error!");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public byte[] getPacket()
	{
		byte[] t = out2.toByteArray();
		out2.reset();
		return t;
	}
	
	public int findByNick(String nick)
	{
		int curr_id = -1;
		int curr_id_prob = 255; 
		for(int i=0;i<255;i++)
		{
			if(clients[i] != null && clients[i].nick.toLowerCase().startsWith(nick.toLowerCase()) && clients[i].dc == 0)
			{
				int t = clients[i].nick.toLowerCase().compareTo(nick.toLowerCase());
				if(t<0) t=-t;
				if(t<curr_id_prob)
				{
					curr_id = i;
					curr_id_prob = t;
				}
			}
		}
		return curr_id;
	}
	public int findByIP(String ip)
	{
		for(int i=0;i<255;i++)
		{
			if(clients[i] != null && clients[i].socket.getInetAddress().getHostAddress().toLowerCase().equals(ip) && clients[i].dc == 0)
			{
				return i;
			}
		}
		return -1;
	}
	
	public void sendOthers(int a, byte[] arr, int len)
	{
		try
		{
			for(int i=0;i<255;i++)
			{
				if(clients[i] != null && clients[i].id != a && clients[i].dc == 0)
				{
					clients[i].sendPacket(arr);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrServer sendOthers error!");
			System.exit(1);
		}
	}

	public void sendOthersOnMap(int a, byte[] arr, int len)
	{
		try
		{
			for(int i=0;i<255;i++)
			{
				if(clients[i] != null && clients[i].id != a && clients[i].dc == 0 && clients[i].map == clients[a].map)
				{
					clients[i].sendPacket(arr);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrServer sendOthers error!");
			System.exit(1);
		}
	}
	
	public void sendAll(byte[] arr)
	{
		sendAll(arr,arr.length);
	}
	public void sendOthers(int a, byte[] arr)
	{
		sendOthers(a,arr,arr.length);
	}
	public void sendOthersOnMap(int a, byte[] arr)
	{
		sendOthersOnMap(a,arr,arr.length);
	}
	public void sendAll(byte[] arr, int len)
	{
		sendOthers(256,arr,arr.length);
	}
	public void sendAllOnMap(byte[] arr, String map_name)
	{
		sendAllOnMap(arr,arr.length,map_name);
	}
	public void sendAllOnMap(byte[] arr, int len, String map_name)
	{
		try
		{
			for(int i=0;i<255;i++)
			{
				if(clients[i] != null && clients[i].dc == 0 && clients[i].map.mapName.equalsIgnoreCase(map_name))
				{
					clients[i].sendPacket(arr);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrServer sendAllOnMap error!");
			System.exit(1);
		}
	}
	
	public void playSound(int x, int y, int id, CraftrMap mymap)
	{
		for(int i=0;i<255;i++)
		{
			if(clients[i] != null && clients[i].dc == 0 && clients[i].map==mymap) 
			{
				// This screws up meloders.
				// Adrian, see me after class. --GM
				//if(id>256) clients[i].playSound(x,y,(id%4)+252);
				//else clients[i].playSound(x,y,id);
				
				// samples are played with on/off messages
				// we don't need to make the explicit sound --GM
				if(id<256) clients[i].playSound(x,y,id);
			}
		}
	}

	public void writeString(String s, DataOutputStream out)
	{
		try
		{
			byte[] t = s.getBytes();
			synchronized(out)
			{
				out.writeByte(s.length());
				out.write(t,0,s.length());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal CraftrServer writeString error!");
			try
			{
				synchronized(out){out.writeByte(0x00);}
			}
			catch(Exception ee)
			{
				System.out.println("Fatal CraftrServer writeString error!");
			}
		} 
	}
	
	public void start()
	{
		System.out.println("64px-srvr version " + CraftrVersion.getVersionName());
		System.out.println("Svetlana, I'm sorry.");
		System.out.println("Also let's create servers with Haruhi Suzumiya! (yeah, haruhiism plug FTW)");
		System.out.print("Initializing: #");
		run = true;
		ci = new CraftrInput(this);
		Thread ti = new Thread(ci);
		ti.start();
		System.out.print("#");
		CraftrAutoSaver cas = new CraftrAutoSaver(this);
		cas.mapspeed=(map_save_duration*60);
		Thread ti2 = new Thread(cas);
		ti2.start();
		System.out.print("#");
		Thread ti4 = new Thread(new CraftrHeartThread(this));
		if(privmode==0) ti4.start();
		System.out.print("#");
		System.out.println("READY!");
		while(run)
		{
			try
			{
				boolean accepted = false;
				Socket t = servsock.accept();
				for(int i=0;i<255;i++)
				{
					if(clients[i] == null || clients[i].dc > 0)
					{
						clients[i] = new CraftrClient(t,map,i,this);
						clients[i].world = findWorld("map");
						Thread t1 = new Thread(clients[i]);
						t1.start();
						accepted = true;
						break;
					}
				}
				if(!accepted)
				{
					DataOutputStream to = new DataOutputStream(t.getOutputStream());
					to.writeByte(0xF5);
					writeString("Too many players!",to);
				}
				Thread.sleep(10);
			}
			catch(Exception e)
			{
				System.out.println("Fatal CraftrServer loop error!");
				System.exit(1);
			}
		}
	}

	public boolean mapBeSaved=false;

	public void saveMap()
	{
		if(mapBeSaved) return;
		mapBeSaved=true;
		for(int i=0;i<map.chunks.length;i++)
		{
			if(map.chunks[i].isSet || map.chunks[i].isUsed)
			{
				map.saveChunkFile(i);
			}
		}
		for(CraftrWorld w : worlds)
		{
			for(int i=0;i<w.map.chunks.length;i++)
			{
				if(w.map.chunks[i].isSet || w.map.chunks[i].isUsed)
				{
					w.map.saveChunkFile(i);
				}
			}
		}
		mapBeSaved=false;
	}
	public void end()
	{
		saveMap();
		while(mapBeSaved) { try{Thread.sleep(10);}catch(Exception e){} } // you never know
		saveConfig();
		for(CraftrWorld w : worlds)
		{
			saveWorldConfig(w);
		}
	}
}
package client;
import common.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.util.*;
import java.util.zip.*;
import javax.swing.*;
import java.lang.*;
import java.text.*;
import java.net.*;

public class CraftrGame extends JComponent
implements MouseListener, MouseMotionListener, KeyListener, ComponentListener, FocusListener, CraftrGameShim
{
	public CraftrCanvas canvas;
	public JFrame window;
	public boolean gameOn;
	public static Random rand = new Random();

	public CraftrMap map;
	public CraftrPlayer players[] = new CraftrPlayer[256];
	
	public boolean hasShot;
	public boolean blockChange = false;
	public boolean playerChange = false;
	public boolean mouseChange = false;
	public boolean netChange = false;
	public boolean multiplayer;
	public boolean isKick = false;
	public CraftrMapThread cmt;
	public int cmtsp=30;
	public int overhead=0;
	public int health=5;
	public CraftrConfig config;
	public CraftrNet net;
	public CraftrGameScreen gs;
	public CraftrInScreen is;
	public CraftrSound audio;
	public boolean advMouseMode = false;
	public int netThreadRequest = 0;
	public int kim = 0;
	public String isKickS;
	public boolean skipConfig = false;
	public boolean muted = false;
	public boolean raycasting = false;

	private int fps = 0;
	private long frame = 0;
	private long fold = 0;
	private int waitTime = 0;
	private int nagle=0;
	private int chrArrowWaiter = 0;
	private boolean[] keyHeld;
	private long wpso = 0;
	private Date overdate;
	private Date told = new Date();
	private Date tnew;
	private int ix = 0;
	private int iy = 0;
	private int mx = -1;
	private int my = -1;
	private int oldmx = -1;
	private int oldmy = -1;
	private int lpx = 0;
	private int lpy = 0;
	private boolean canMousePress;
	private boolean isShift = false;
	private int mb = 0;
	private int oldmb = 0;
	private int ev_no,ev_1,ev_2,ev_3;
	private int key_up = KeyEvent.VK_UP;
	private int key_left = KeyEvent.VK_LEFT;
	private int key_right = KeyEvent.VK_RIGHT;
	private int key_down = KeyEvent.VK_DOWN;
	private CraftrGameThread gt;

	public void setHealth(int h)
	{
		health = h;
		gs.health = health;
	}
	public void kill()
	{
		if(multiplayer) return;
		setHealth(health-1);
		if(health==0)
		{
			gs.addChatMsg("&cYou were killed!");
			health=5;
			gs.health=5;
			map.setPlayer(players[255].px,players[255].py,0);
			map.setPlayer(0,0,1);
			oldmx=-1;
			oldmy=-1;
			players[255].move(0,0);
			playerChange = true;
		}
	}
	public void playSound(int tx, int ty, int val)
	{
		if(muted) return;
		if(val>=256)
		{
			playSample(tx,ty,val-256);
			return;
		}		
		int x=players[255].px-tx;
		int y=players[255].py-ty;
		audio.playNote(x,y,val,1.0);
	}
	public void playSample(int tx, int ty, int val)
	{
		if(muted) return;
		int x=players[255].px-tx;
		int y=players[255].py-ty;
		audio.playSampleByNumber(x,y,val,1.0);
	}
	public void changeKeyMode(int t)
	{
		kim=t;
		if(t==0)
		{
			key_up = KeyEvent.VK_UP;
			key_left = KeyEvent.VK_LEFT;
			key_right = KeyEvent.VK_RIGHT;
			key_down = KeyEvent.VK_DOWN;
		} else {
			key_up = KeyEvent.VK_W;
			key_down = KeyEvent.VK_S;
			key_left = KeyEvent.VK_A;
			key_right = KeyEvent.VK_D;
		}
	}
	public static String getVersion()
	{
		return CraftrVersion.getVersionName();
	}
	public CraftrGame()
	{
		audio = new CraftrSound();
		File sdchk = new File(System.getProperty("user.home") + "/.64pixels");
		if(!sdchk.exists()) sdchk.mkdir();
		window = new JFrame("64pixels^2 " + getVersion());
		gameOn = true;
		map = new CraftrMap(false,64);
		map.game = this;
		map.saveDir = System.getProperty("user.home") + "/.64pixels/";
		players[255] = new CraftrPlayer(0,0);
		canMousePress = true;
		config = new CraftrConfig(map.saveDir + "config.txt");
		cmt = new CraftrMapThread(map);
		gs = new CraftrGameScreen(null);	
		loadConfig();
		canvas = new CraftrCanvas();
		gs.setCanvas(canvas);
		canvas.cs = (CraftrScreen)gs;
		if(cmtsp>0) cmt.speed=(1000/cmtsp);
		else cmt.speed=0;
		keyHeld = new boolean[4];
	}
	public void kickOut(String why)
	{
		isKick=true;
		isKickS = why;
	}
	public String escapeSlashes(String orig)
	{
		char[] temp = orig.toCharArray();
		String newS = "";
		for(int i=0;i<temp.length;i++)
		{
			if(temp[i]=='\\') i++;
			if(i<temp.length) newS+=temp[i];
		}
		return newS;
	}
	public boolean fetchSList()
	{
		URL u1;
		InputStream is = null;
		FileOutputStream fos;
		try
		{
			u1 = new URL("http://admin.64pixels.org/serverlist.php?asie=1");
			is = u1.openStream();
			fos = new FileOutputStream(map.saveDir + "slist.txt");
			int count = 1;
			while(count>0)
			{
				byte[] t = new byte[64];
				count=is.read(t,0,64);
				if(count>0)
				{
					System.out.println("read " + count + " bytes");
					fos.write(t,0,count);
				}
			}
		}
		catch(Exception e) { e.printStackTrace(); return false;}
		finally { try{is.close();}catch(Exception e){} }
		return true;
	}

	public void realKickOut()
	{
		CraftrKickScreen cks=new CraftrKickScreen(canvas,isKickS);
		canvas.cs=(CraftrScreen)cks;
		while(true)
		{
			canvas.draw(mx,my);
			try { Thread.sleep(100); } catch(Exception e) { }
		}
	}
	public void focusLost(FocusEvent ce)
	{
		keyHeld[0]=false;
		keyHeld[1]=false;
		keyHeld[2]=false;
		keyHeld[3]=false;
	}
	public void focusGained(FocusEvent ce) {}
	
	public void componentHidden(ComponentEvent ce) {}
	public void componentShown(ComponentEvent ce) {}
	public void componentMoved(ComponentEvent ce) {}
	public void componentResized(ComponentEvent ce)
	{
		canvas.scale(window.getRootPane().getWidth(),window.getRootPane().getHeight());
	}

	public String getPassword()
	{
		return getData("Input password:",16);
	}

	public String getData(String name,int len)
	{
		is = new CraftrInScreen(canvas,1,name);
		canvas.cs = (CraftrScreen)is;
		is.maxLen=len;
		is.minLen=1;
		loopInScreen();
		String t = is.inString;
		canvas.cs = (CraftrScreen)gs;
		is = null;
		return t;
	}
	
	public void saveConfig()
	{
		try
		{
			NumberFormat nf = NumberFormat.getNumberInstance();
			BufferedWriter out = new BufferedWriter(new FileWriter(map.saveDir + "config.txt"));
			String s = "";
			for(int i=0;i<256;i++)
			{
				if(gs.drawChrA[i]!=1)
				{
					s = "dch|" + i + "=" + gs.drawChrA[i];
					out.write(s,0,s.length());
					out.newLine();
				}
				if(gs.drawColA[i]!=15)
				{
					s = "dco|" + i + "=" + gs.drawColA[i];
					out.write(s,0,s.length());
					out.newLine();
				}
			}
			s = "player-char=" + players[255].pchr;
			out.write(s,0,s.length());
			out.newLine();
			s = "player-color=" + players[255].pcol;
			out.write(s,0,s.length());
			out.newLine();
			if(map.cachesize != 64)
			{
				s = "map-cache-size=" + map.cachesize;
				out.write(s,0,s.length());
				out.newLine();
			}
			s = "player-name=" + players[255].name;
			out.write(s,0,s.length());
			out.newLine();
			if(map.genMode != 0)
			{
				s = "mapgen-mode=" + map.genMode;
				out.write(s,0,s.length());
				out.newLine();
			}
			s = "wsad-mode=" + kim;
			out.write(s,0,s.length());
			out.newLine();
			if(nagle>0)
			{
				s = "use-nagle=" + nagle;
				out.write(s,0,s.length());
				out.newLine();
			}
			if(cmtsp!=30)
			{
				s = "physics-speed=";
				if(cmtsp==0) s += "max";
				else s += cmtsp;
				out.write(s,0,s.length());
				out.newLine();
			}
			s = "drawn-type=" + gs.drawType;
			out.write(s,0,s.length());
			out.newLine();
			if(!multiplayer)
			{
				lpx = players[255].px;
				lpy = players[255].py;
			}
			if(gs.hideousPrompts)
			{
				s = "hideous-prompts=1";
				out.write(s,0,s.length());
				out.newLine();
			}
			if(!canvas.resizePlayfield)
			{
				s = "resize-playfield=false";
				out.write(s,0,s.length());
				out.newLine();
			}
			s = "player-x=" + lpx;
			out.write(s,0,s.length());
			out.newLine();
			s = "player-y=" + lpy;
			out.write(s,0,s.length());
			out.close();	
		}
		catch (Exception e)
		{
			System.out.println("Error saving config data! " + e.getMessage());
		}
	}
	
	public void loadConfig()
	{
		try
		{
			NumberFormat nf = NumberFormat.getNumberInstance();
			for(int i=0;i<config.keys;i++)
			{
				String key = config.key[i].toLowerCase();
				String val = config.value[i];
				if(key.contains("drawn-type"))
				{
					gs.drawType = nf.parse(val).byteValue();
					if(gs.drawType>CraftrBlock.maxType || gs.drawType<-1 || !CraftrBlock.isPlaceable(gs.drawType)) gs.drawType = 0;
				}
				else if(key.contains("player-char"))
				{
					players[255].pchr = nf.parse(val).byteValue();
				}
				else if(key.contains("mapgen-mode"))
				{
					map.genMode = nf.parse(val).byteValue();
				}
				else if(key.contains("player-name"))
				{
					players[255].name = val;
				}
				else if(key.contains("player-color"))
				{
					players[255].pcol = nf.parse(val).byteValue();
				}
				else if(key.contains("map-cache-length"))
				{
					map.resizeChunks(nf.parse(val).intValue());
				}
				if(key.contains("player-x"))
				{
					players[255].px = nf.parse(val).intValue();
					lpx = players[255].px;
				}
				else if(key.contains("player-y"))
				{
					players[255].py = nf.parse(val).intValue();
					lpy = players[255].py;
				}
				else if(key.contains("wsad-mode"))
				{
					kim = nf.parse(val).intValue();
					changeKeyMode(kim);
				}
				else if(key.contains("use-nagle"))
				{
					nagle = nf.parse(val).intValue();
				}
				else if(key.contains("physics-speed"))
				{
					if(val.equals("max"))
					{
						cmtsp = 0;
					}
					else
					{
						cmtsp = nf.parse(val).intValue();
						if(cmtsp<0) cmtsp=30;
						else if(cmtsp>1000) cmtsp=0;
					}
				}
				else if(key.contains("dch|"))
				{
					String[] dchi = key.split("\\|");
					/* as | is a special character here, we need to escape it with \. *
					 *  but \ is also special so we escape THAT with another \	 */
					if(dchi.length==2)
					{
						gs.drawChrA[nf.parse(dchi[1]).intValue()]=nf.parse(val).intValue();
					}
				}
				else if(key.contains("dco|"))
				{
					String[] dcoi = key.split("\\|");
					if(dcoi.length==2)
					{
						gs.drawColA[nf.parse(dcoi[1]).intValue()]=nf.parse(val).intValue();
					}
				}
				else if(key.contains("hideous-prompts"))
				{
					if(nf.parse(val).intValue()>0) gs.hideousPrompts=true;
				}
				else if(key.contains("resize-playfield"))
				{
					if(val.contains("false")) canvas.resizePlayfield = false;
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Error reading config data! " + e.getMessage());
		}
	}

	public void mouseEntered(MouseEvent ev) {}
	public void mouseExited(MouseEvent ev) {}
	public void mouseClicked(MouseEvent ev) {}

	public boolean isDragging = false;
	public boolean isConfig = true;
	public int dragX = 0;
	public int dragY = 0;
	public int dragID = 0;
	public void mousePressed(MouseEvent ev)
	{
		mb = ev.getButton();
		updateMouse(ev.getX(),ev.getY());
		ev_no = ev.NOBUTTON;
		ev_1 = ev.BUTTON1;
		ev_2 = ev.BUTTON2;
		ev_3 = ev.BUTTON3;
		advMouseMode = ev.isControlDown();
		if(isConfig)
		{
			processWindows();
			return;
		}
		if(isKick) return;
		mouseChange = gs.mousePressed(ev);
		processWindows();
		processMouse();
	}
	public void mouseReleased(MouseEvent ev) { mb = ev_no; canMousePress = true; advMouseMode = false;}

	int confChr = 0;
	int confCol = 0; 
	public void processWindows()
	{
		ArrayList<CraftrWindow> w;
		if(isConfig) w=is.windows;
		else w=gs.windows;
		try
		{
			synchronized(w)
			{
				if(w.size()>0)
					for(CraftrWindow cw : w)
					{
						if(!isConfig && gs.obstructedWindow(cw,mx,my)) { }
						else if(isConfig && is.obstructedWindow(cw,mx,my)) { }
						else if(insideRect(mx,my,(cw.x+cw.w-1)<<3,cw.y<<3,8,8))
						{
							// close button, any window
							if(isConfig) is.toggleWindow(cw.type);
							else gs.toggleWindow(cw.type);
							canMousePress = false;
						} else if(insideRect(mx,my,(cw.x+1)<<3,(cw.y+1)<<3,(cw.w-2)<<3,(cw.h-2)<<3))
						{
							switch(cw.type)
							{
								case 1: // char selecting, char window only
									confChr = (((mx-((cw.x+1)<<3))>>3)%(cw.w-2)) + ((((my>>3)-(cw.y+1))%(cw.h-2))*(cw.w-2));
									if(confChr<=255)
									{
										gs.sdrawChr(confChr);
										gs.chrBarOff = confChr-8;
										if(gs.chrBarOff<0) gs.chrBarOff+=256;
										if(isConfig) is.getWindow(1).charChosen = confChr;
									}				
									break;
								case 2:
									confCol = (((mx-((cw.x+1)<<3))>>3)&15) | (((my-((cw.y+1)<<3))<<1)&240);
									gs.sdrawCol(confCol);
									if(isConfig) is.getWindow(2).colorChosen = confCol;
									break;
								case 3:
									if(insideRect(mx,my,(cw.x+2)<<3,(cw.y+2)<<3,(cw.w-4)<<3,(cw.h-4)<<3))
									{
										int ix = (mx-((cw.x+2)<<3))>>4;
										int iy = (my-((cw.y+2)<<3))>>4;
										int ip = ix+iy*4;
										gs.drawType = cw.recBlockType[ip];
										gs.sdrawChr(cw.recBlockChr[ip]);
										gs.chrBarOff = gs.gdrawChr()-8;
										if(gs.chrBarOff<0) gs.chrBarOff+=256;
										gs.sdrawCol(cw.recBlockCol[ip]);
										gs.toggleWindow(3);
										canMousePress = false;
										mouseChange = true;
									}
									break;
								case 4:
									gs.drawType=CraftrWindow.getBlockType((my-((cw.y+1)<<3))>>3);
									break;
								default:
									break;
							}
						} else if(insideRect(mx,my,cw.x<<3,cw.y<<3,cw.w<<3,cw.h<<3))
						{ // DRAGGING WINDOWS! :D
							dragX = (mx-(cw.x<<3))>>3;
							dragY = (my-(cw.y<<3))>>3;
							dragID = w.indexOf(cw);
							isDragging = true;
						}
					}
			}
		}
		catch(Exception e) { }
	}
	public void mouseMoved(MouseEvent ev) {
		updateMouse(ev.getX(),ev.getY());
		advMouseMode = ev.isControlDown();
	}
	public void mouseDragged(MouseEvent ev) { updateMouse(ev.getX(),ev.getY()); advMouseMode = ev.isControlDown(); } // this can be quite handy
	
	public void updateMouse(int umx, int umy)
	{
		mx=(int)(umx/canvas.scaleX);
		my=(int)(umy/canvas.scaleY);
		if(!isConfig && (mx >= 0 && mx < canvas.WIDTH && my >= 0 && my < (canvas.GRID_H<<4)))
		{
			int tx = (players[255].px+(mx>>4))-(canvas.FULLGRID_W/2)+1;
			int ty = (players[255].py+(my>>4))-(canvas.FULLGRID_H/2)+1;
			gs.hov_type=map.getBlock(tx,ty).getTypeWithVirtual();
		}
		if(isDragging)
		{
			ArrayList<CraftrWindow> w = gs.windows;
			if(isConfig) w = is.windows;
			synchronized(w)
			{
				CraftrWindow dcw = w.get(dragID);
				int dragRX = (mx-((dcw.x+dragX)<<3))>>3;
				int dragRY = (my-((dcw.y+dragY)<<3))>>3;
				dcw.x+=dragRX;
				dcw.y+=dragRY;
				w.set(dragID,dcw);
			}
			if(mb != ev_1) isDragging = false;
		}
	}	

	public void processMouse()
	{
		if(mb != ev_no && canMousePress && !isDragging)
		{
			if(insideRect(mx,my,0,0,canvas.WIDTH,(canvas.GRID_H<<4)) && !gs.inWindow(mx,my))
			{
				CraftrWindow cw = gs.getWindow(3);
				for(int i=0;i<256;i++)
				{
					if(gs.players[i] != null && gs.players[i].px == mx>>4 && gs.players[i].py == my>>4) return;
				}
				byte[] tmparr = new byte[4];
				CraftrBlock capturedBlock = map.getBlock(players[255].px-(canvas.FULLGRID_W/2)+1+(mx>>4),players[255].py-(canvas.FULLGRID_H/2)+1+(my>>4));
				if(!capturedBlock.isPlaceable()) return;
				if(mb == ev_1)
				{
					tmparr[0] = (byte)gs.drawType;
					tmparr[1] = (byte)CraftrBlock.getParam(gs.drawType);
					tmparr[2] = (byte)gs.gdrawChr();
					tmparr[3] = (byte)gs.gdrawCol();
				}
				if(mb == ev_2)
				{
					if(capturedBlock.isPlaceable() && !advMouseMode) gs.drawType = capturedBlock.getTypeWithVirtual();
 					gs.sdrawChr(capturedBlock.getChar());
 					gs.sdrawCol(capturedBlock.getColor());
					gs.chrBarOff = gs.gdrawChr()-8;
					if(gs.chrBarOff<0) gs.chrBarOff+=256;
					if(cw!=null) cw.addRecBlock((byte)gs.drawType,(byte)gs.gdrawChr(),(byte)gs.gdrawCol());
				}
				else if(oldmb != mb || (mx>>4 != oldmx>>4 || my>>4 != oldmy>>4))
				{
					mouseChange = true;
					oldmx = mx;
					oldmy = my;
					oldmb = mb;
					int ttx = players[255].px-(canvas.FULLGRID_W/2)+1+(mx>>4);
					int tty = players[255].py-(canvas.FULLGRID_H/2)+1+(my>>4);
					if(!multiplayer) synchronized(map.physics)
					{
						map.physics.addBlockToCheck(new CraftrBlockPos(ttx,tty));
						for(int i=0;i<4;i++)
						{
							map.physics.addBlockToCheck(new CraftrBlockPos(ttx+map.xMovement[i],tty+map.yMovement[i]));
						}
					}
					if(mb == ev_1 && advMouseMode) tmparr[0] = (byte)map.getBlock(ttx,tty).getTypeWithVirtual();
					if(gs.drawType==2 && mb == ev_1)
					{
						tmparr[3]=(byte)(tmparr[3]&7);
						tmparr[0]=(byte)2;
					}
					blockChange=true;
					if(tmparr[0]==-1)
					{
						map.setPushable(ttx,tty,tmparr[2],tmparr[3]);
						map.setBlock(ttx,tty,(byte)0,(byte)0,(byte)0,(byte)0);
					} else {
 						map.setPushable(ttx,tty,(byte)0,(byte)0);
 						if(cw!=null) cw.addRecBlock(tmparr[0],tmparr[2],tmparr[3]);
 						map.setBlock(ttx,tty,tmparr);
 						if(mb == ev_1 || mb == ev_3)
						{
							CraftrBlock blockPlaced = map.getBlock(ttx,tty);
							byte[] bPdata = blockPlaced.getBlockData();
							map.setBlock(ttx,tty,bPdata[0],bPdata[1],(byte)map.updateLook(blockPlaced),bPdata[3]);
							for(int i=0;i<4;i++)
							{
								blockPlaced = map.getBlock(ttx+map.xMovement[i],tty+map.yMovement[i]);
								bPdata = blockPlaced.getBlockData();
								map.setBlock(ttx+map.xMovement[i],tty+map.yMovement[i],bPdata[0],bPdata[1],(byte)map.updateLook(blockPlaced),bPdata[3]);
							}
						}
					}
					if(multiplayer)
					{
						net.sendBlock(ttx,tty,tmparr[0],tmparr[2],tmparr[3]);
					}
					updateMouse((int)(mx*canvas.scaleX),(int)(my*canvas.scaleY));
				}
			}
		}
	}
	public boolean insideRect(int mx, int my, int x, int y, int w, int h)
	{
		if(mx >= x && my >= y && mx < x+w && my < y+h)
		{
			return true;
		} else
		{
			return false;
		}
	}

	public void shoot(int dir)
	{
		int sx=players[255].px+map.xMovement[dir];
		int sy=players[255].py+map.yMovement[dir];
		map.setBullet(sx,sy,(byte)(dir+1));
		blockChange=true;
		if(multiplayer)
		{
			net.shoot(sx,sy,(dir+1));
		}
		else
		{
			map.physics.addBlockToCheck(new CraftrBlockPos(sx,sy));
			for(int i=0;i<4;i++) map.physics.addBlockToCheck(new CraftrBlockPos(sx+map.xMovement[i],sy+map.yMovement[i]));
		}
	}

	public void keyTyped(KeyEvent ev) {} // this one sucks even more
	public void keyPressed(KeyEvent ev)
	{
		if(isKick) return;
		if(is != null)
		{
			is.parseKey(ev);
			return;
		}
		int kc = ev.getKeyCode();
		isShift = ev.isShiftDown();
		if(is == null && gs.barType == 0)
		{
			if(kc==key_left)
				keyHeld[1] = true;
			else if(kc==key_right)
				keyHeld[2] = true;
			else if(kc==key_up)
				keyHeld[0] = true;
			else if(kc==key_down)
				keyHeld[3] = true;
			if (gs.getWindow(1)!=null) {
				char chr = ev.getKeyChar();
				if(chr >= 32 && chr <= 127)
				{
					gs.sdrawChr(0xFF&(int)((byte)chr));
					gs.chrBarOff = gs.gdrawChr()-8;
					mouseChange=true;
				}
			}
			else
			{
				switch(kc)
				{
					case KeyEvent.VK_P:
						System.out.println("player pos: x = " + players[255].px + ", y = " + players[255].py + ".");
						waitTime=2;
						break;
					case KeyEvent.VK_T:
						if(multiplayer)
						{
							gs.barType = 1;
							gs.chatMsg="";
							mouseChange = true;
						}
						break;
					case KeyEvent.VK_R:
						if(multiplayer && (players[255].px != 0 || players[255].py != 0))
						{
							net.respawnRequest();
						}
						break;
					case KeyEvent.VK_F:
						if(!multiplayer||net.isOp)
						{
							mouseChange=true;
							gs.viewFloorsMode=!gs.viewFloorsMode;
						}
						break;
					case KeyEvent.VK_B:
						gs.toggleWindow(3);
						mouseChange=true;
						break;
					default:
							break;
				}
			}
		} else if(gs.barType==1) {
			if(kc == KeyEvent.VK_BACK_SPACE && gs.chatMsg.length() > 0)
			{
				gs.chatMsg = gs.chatMsg.substring(0,gs.chatMsg.length()-1);
				mouseChange = true;
			} else if(kc == KeyEvent.VK_ENTER)
			{
				if(!gs.chatMsg.trim().equals(""))
				{
					net.sendChatMsg(gs.chatMsg);
				}
				gs.chatMsg = "";
				mouseChange = true;
				gs.barType=0;
			} else if(kc == KeyEvent.VK_ESCAPE)
			{
				gs.barType = 0;
				mouseChange = true;
			}
			else if(ev.isControlDown())
			{
				if(kc==KeyEvent.VK_V)
				{
					try
					{
						// Ctrl+V pressed, let's-a paste!
						Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
						if(clip.isDataFlavorAvailable(DataFlavor.stringFlavor))
						{
							gs.chatMsg += (String)clip.getData(DataFlavor.stringFlavor);
							mouseChange = true;
						}
					}
					catch(Exception e)
					{
						System.out.println("Clipboard pasting failed!");
						e.printStackTrace();
					}
				}
				else if(kc==KeyEvent.VK_C)
				{
					try
					{
						Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
						if(clip.isDataFlavorAvailable(DataFlavor.stringFlavor))
						{
							clip.setContents(new StringSelection(gs.chatMsg),null);
						}
					}
					catch(Exception e)
					{
						System.out.println("Clipboard copying failed!");
						e.printStackTrace();
					}
				}
			}
			else if (gs.chatMsg.length()<120)
			{
				char chr = ev.getKeyChar();
				if(chr >= 32 && chr <= 127) gs.chatMsg += chr;
				mouseChange = true;
			}
		}
		if(kc==KeyEvent.VK_F7)
		{
			muted=!muted;
			if(muted) gs.addChatMsg("&6Sound muted.");
			else gs.addChatMsg("&6Sound unmuted.");
		}
	}
	public void keyReleased(KeyEvent ev)
	{
		int kc=ev.getKeyCode();
		if(kc==key_left)
				keyHeld[1] = false;
		else if(kc==key_right)
				keyHeld[2] = false;
		else if(kc==key_up)
				keyHeld[0] = false;
		else if(kc==key_down)
				keyHeld[3] = false;
	}
	public int movePlayer(int dpx, int dpy)
	{
		if(hasShot) return waitTime;
		int px = players[255].px+dpx;
		int py = players[255].py+dpy;
		CraftrBlock blockMoveTo=map.getBlock(px,py);
		if(isShift && blockMoveTo.isEmpty())
		{
			for(int i=0;i<4;i++)
			{
				int tx = players[255].px+map.xMovement[i];
				int ty = players[255].py+map.yMovement[i];
				if(tx==px && ty==py)
				{
					shoot(i);
					hasShot = true;
					waitTime=9;
				}
			}
		}
		else if(map.pushAttempt(px,py,dpx,dpy))
		{
			if(multiplayer)
			{
				net.playerPush(dpx,dpy);
			} else {
				map.setPlayer(players[255].px,players[255].py,0);
				map.setPlayer(px,py,1);
				map.setPlayer(px+dpx,py+dpy,1);
				oldmx=-1;
				oldmy=-1;
				players[255].move(px,py);
				playerChange = true;
			}
			return 2;
		}
		else if(blockMoveTo.isEmpty())
		{
			if(multiplayer) net.playerMove(dpx,dpy);
			else
			{
				map.setPlayer(players[255].px,players[255].py,0);
				map.setPlayer(px,py,1);
			}
			oldmx=-1;
			oldmy=-1;
			players[255].move(px,py);
			playerChange = true;
			return 2;
 		}
		return waitTime;
	}
	public void spawnPlayer(int cx, int cy, int id)
	{
		CraftrChunk pc;
		try
		{
			pc = map.grabChunk(cx,cy);
		}
		catch(Exception e)
		{
			System.out.println("spawnPlayer: no chunk memory found, most likely");
			return;
		}
		for(int i=0;i<64;i++)
		{
			for(int j=0;j<64;j++)
			{
				if(pc.getBlockType(i,j) == 0) { players[id].px = cx+i; players[id].py = cy+j; return; }
			}
		}
	}
	
	public void render()
	{
		int px = players[255].px;
		int py = players[255].py;
		int sx = px-(canvas.FULLGRID_W/2)+1;
		int sy = py-(canvas.FULLGRID_H/2)+1;
		CraftrBlock t;
		try
		{
			if (!raycasting)
			{
				for(int iy=0;iy<canvas.FULLGRID_H;iy++)
				{
					for(int ix=0;ix<canvas.FULLGRID_W;ix++)
					{
						gs.blocks[(iy*canvas.FULLGRID_W)+ix] = map.getBlock(ix+sx,iy+sy);
					}
				}
			}
			else
			{
				for(int iy=0;iy<canvas.FULLGRID_H;iy++)
				{
					for(int ix=0;ix<canvas.FULLGRID_W;ix++)
					{
						gs.blocks[(iy*canvas.FULLGRID_W)+ix] = null;
					}
				}
				// this is the recursive route.
				gs.blocks[(((canvas.FULLGRID_H/2)-1)*canvas.FULLGRID_W)+(canvas.FULLGRID_W/2)-1] = map.getBlock(px,py);
				castRayPillars(px,py,-1, 0,-1,-1,-1, 1,(canvas.FULLGRID_W/2)+2);
				castRayPillars(px,py, 1, 0, 1,-1, 1, 1,(canvas.FULLGRID_W/2)+2);
				castRayPillars(px,py, 0,-1,-1,-1, 1,-1,(canvas.FULLGRID_H/2)+2);
				castRayPillars(px,py, 0, 1,-1, 1, 1, 1,(canvas.FULLGRID_H/2)+2);
			}
			for (int i=0;i<256;i++)
			{
				if(players[i] == null)
				{
					gs.removePlayer(i);
					continue;
				}
				int tx = (players[i].px-players[255].px)+(canvas.FULLGRID_W/2)-1;
				int ty = (players[i].py-players[255].py)+(canvas.FULLGRID_H/2)-1;
				gs.removePlayer(i);
				if(tx>=0 && ty>=0 && tx<canvas.FULLGRID_W && ty<canvas.FULLGRID_H && gs.blocks[(ty*canvas.FULLGRID_W)+tx] != null)
				{
					CraftrBlock blockAtPlayer = map.getBlock(players[i].px,players[i].py);
					if(blockAtPlayer.getType()!=8) gs.addPlayer(i,tx,ty,players[i].name,players[i].pchr,players[i].pcol);
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("[SEVERE] render exception: " + e.toString() + " | " + e.getMessage() + " | " + e.getCause());
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void castRayPillars(int sx, int sy, int dx, int dy, int x1, int y1, int x2, int y2, int maxtrace)
	{
		assert(x1 <= x2 && y1 <= y2);
		
		// TODO: make it aim for the block corners.
		//       that way we can get the FULL visibility,
		//       rather than a slightly clipped one.
		
		int ox1 = x1;
		int oy1 = y1;
		int ox2 = x2;
		int oy2 = y2;
		
		
		int adx = (dx < 0 ? -dx : dx);
		int ady = (dy < 0 ? -dy : dy);
		
		while(maxtrace > 0)
		{
			//System.out.printf("maxtrace %d %d %d: %d %d -> %d %d\n", maxtrace, dx, dy, x1, y1, x2, y2);
			boolean hitone = false;
			boolean hittingone = false;
			int x = x1, y = y1;
			
			// AFAIK this is pretty similar to Bresenham's thing.
			while(x <= x2 && y <= y2)
			{
				// RANGE CHECK!
				if(x >= 0-(canvas.FULLGRID_W/2)+1 && x < canvas.FULLGRID_W-((canvas.FULLGRID_W/2)-1) && y >= 0-(canvas.FULLGRID_H/2)+1 && y < canvas.FULLGRID_H-((canvas.FULLGRID_H/2)-1))
				{
					CraftrBlock t = map.getBlock(x+sx,y+sy);

					// first check: block behind is empty
					// second check: block is aligned with the axis
					// third check: block right/left is empty
					boolean antidiagcheck = 
						   map.getBlock(x+sx-dx,y+sy-dy).isEmpty()
						|| x == 0 || y == 0
						|| map.getBlock(x+sx-(x < 0 ? -ady : ady),y+sy-(y < 0 ? -adx : adx)).isEmpty();

					//if(antidiagcheck || !t.isEmpty()) // no corners for you - TODO: fix the "flicker"
					if(antidiagcheck)
						gs.blocks[((y+((canvas.FULLGRID_H/2)-1))*canvas.FULLGRID_W)+x+((canvas.FULLGRID_W/2)-1)] = t;
					
					if(!(t.isEmpty() && antidiagcheck))
					{
						if(!hittingone)
						{
							hitone = true;
							hittingone = true;
							// we must split this.
							if(x1 != x || y1 != y)
								castRayPillars(sx,sy,dx,dy,x1,y1,x-ady,y-adx,maxtrace);
						}
					} else if(hittingone) {
						hittingone = false;
						x1 = x;
						y1 = y;
					}
				}
				x += ady;
				y += adx;
			}
			
			// touch walls if necessary
			x = x1;
			y = y1;
			{
				CraftrBlock t2 = map.getBlock(x+sx,y+sy);

				if(t2.isEmpty())
				{
					x -= ady;
					y -= adx;
					if(x >= 0-(canvas.FULLGRID_W/2)+1 && x < canvas.FULLGRID_W-((canvas.FULLGRID_W/2)-1) && y >= 0-(canvas.FULLGRID_H/2)+1 && y < canvas.FULLGRID_H-((canvas.FULLGRID_H/2)-1))
					{
						CraftrBlock t = map.getBlock(x+sx,y+sy);

						if(!t.isEmpty())
							gs.blocks[((y+((canvas.FULLGRID_H/2)-1))*canvas.FULLGRID_W)+x+((canvas.FULLGRID_W/2)-1)] = t;		
					}
				}
			}
			
			x = x2;
			y = y2;
			{
				CraftrBlock t2 = map.getBlock(x+sx,y+sy);

				if(t2.isEmpty())
				{
					x += ady;
					y += adx;
					if(x >= 0-(canvas.FULLGRID_W/2)+1 && x < canvas.FULLGRID_W-((canvas.FULLGRID_W/2)-1) && y >= 0-(canvas.FULLGRID_H/2)+1 && y < canvas.FULLGRID_H-((canvas.FULLGRID_H/2)-1))
					{
						CraftrBlock t = map.getBlock(x+sx,y+sy);

						if(!t.isEmpty())
							gs.blocks[((y+((canvas.FULLGRID_H/2)-1))*canvas.FULLGRID_W)+x+((canvas.FULLGRID_W/2)-1)] = t;		
					}
				}
			}
			
			if(hitone)
			{
				if(!hittingone)
				{
					castRayPillars(sx,sy,dx,dy,x1,y1,x2,y2,maxtrace);
				}
				
				return;
			}
			
			if(dy == 0)
			{
				x1 += dx;
				x2 += dx;
			} else {
				y1 += dy;
				y2 += dy;
			}
			
			if(dy == 0)
			{
				y1 = (int)(oy1*x1/ox1);
				y2 = (int)(oy2*x2/ox2);
			} else {
				x1 = (int)(ox1*y1/oy1);
				x2 = (int)(ox2*y2/oy2);
			}
			
			maxtrace--;
		}
	}
	
	public void init()
	{
		window.add(canvas);
		window.pack(); // makes everything a nice size
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		tnew = new Date(told.getTime() + 1000L);
	}
	
	public void loopInScreen()
	{
		try
		{
			while(is.isRunning)
			{
				canvas.draw(mx,my);
				if(waitTime>0) waitTime--;
				if(mx != oldmx || my != oldmy || mb != oldmb)
				{
					mouseChange = true;
					oldmx = mx;
					oldmy = my;
					oldmb = mb;
				}
				Thread.sleep(33);
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal loopInScreen error!");
			System.exit(1);
		}
	}

	public String configure()
	{
		boolean inconf = true;
		is = new CraftrInScreen(canvas,2,"Main menu");
		String[] modes = new String[7];
		modes[0] = "Singleplayer";
		modes[1] = "Multiplayer";
		modes[4] = "Change player char ->";
		modes[5] = "Change player color ->";
		String ostr = "";
		while(inconf)
		{
			is = new CraftrInScreen(canvas,2,"Main menu");
			is.isRunning=true;
			modes[2] = "Key mode: " + ((kim>0)?"WSAD":"Arrows");
			modes[3] = "Hideous prompts: " + ((gs.hideousPrompts)?"On":"Off");
			modes[6] = "Resize mode: " + ((canvas.resizePlayfield)?"Playfield":"Scale");
			is.addStrings(modes);
			canvas.cs = (CraftrScreen)is;
			loopInScreen();
			switch(is.inSel)
			{
				case 0:
					multiplayer = false;
					inconf = false;
					break;
				case 1:
					multiplayer = true;
					boolean doCustom = true;
					CraftrKickScreen cks = new CraftrKickScreen(canvas,"Loading serverlist...");
					cks.mName="DON'T PANIC";
					cks.bgcolor = 0x808080;
					canvas.cs = (CraftrScreen) cks;
					canvas.draw(mx,my);
					System.out.print("fetching... ");
					if(fetchSList())
					{
						System.out.println("fetched!");
						doCustom=false; // for now
						CraftrConfig csl = new CraftrConfig();
						csl.load(map.saveDir + "slist.txt");
						// by now csl stores the serverlist D:
						String[] csll = new String[csl.keys+2];
						csll[0]="Custom address";
						for(int i=1;i<=csl.keys;i++)
						{
							csll[i]=escapeSlashes(csl.keyo[i-1]);
						}
						csll[csll.length-1]="<- Back";
						is = new CraftrInScreen(canvas,2,"Choose server");
						is.addStrings(csll);
						canvas.cs= (CraftrScreen) is;
						loopInScreen();
						if(is.inSel==0) doCustom=true;
						else if(is.inSel==(csll.length-1)) { inconf = true; break; }
						else ostr=csl.value[is.inSel-1];
					}
					else
					{
						System.out.println("not fetched (probably means glados)");
						cks.bgcolor = 0xAA0000;
						cks.mName="SERVERLIST NOT FOUND";
						cks.name="PLEASE DON'T PANIC, ONE SECOND...";
						canvas.draw(mx,my);
						try{Thread.sleep(1800);}catch(Exception e){}
					}
					if(doCustom)
					{
						is = new CraftrInScreen(canvas,1,"Input address:");
						is.minLen=0;
						is.maxLen=60;
						canvas.cs = (CraftrScreen)is;
						loopInScreen();
						ostr = is.inString;
					}
					is = new CraftrInScreen(canvas,1,"Enter nickname:");
					is.minLen=1;
					is.maxLen=16;
					if(players[255].name != "You") is.inString = players[255].name;
					canvas.cs = (CraftrScreen)is;
					loopInScreen();
					net.nick = is.inString;
					inconf = false;
					break;
				case 2:
					changeKeyMode(1-(kim%2));
					break;
				case 3:
					gs.hideousPrompts=!gs.hideousPrompts;
					break;
				case 4:
					is.toggleWindow(1);
					while(is.getWindow(1)!=null)
					{
						canvas.draw(mx,my);
						try{ Thread.sleep(33); } catch(Exception e){}
					}
					if(confChr!=0) players[255].pchr = (byte)confChr;
					break;
				case 5:
					is.toggleWindow(2);
					while(is.getWindow(2)!=null)
					{
						canvas.draw(mx,my);
						try{ Thread.sleep(33); } catch(Exception e){}
					}
					if(confCol!=0) players[255].pcol = (byte)confCol;
					break;
				case 6:
					canvas.resizePlayfield=!canvas.resizePlayfield;
					break;
			}
		}
		canvas.cs = (CraftrScreen)gs;
		is = null;
		return ostr;
	}
	
	public void parseNTR()
	{
		switch(netThreadRequest)
		{
			case 1:
				net.sendDecrypt(getPassword());
				break;
			default:
				break;
		}
		netThreadRequest=0;
	}
	public void start(String[] args)
	{
		ev_1=65535;
		window.getRootPane().addComponentListener(this);
		window.addFocusListener(this);
		window.addKeyListener(this);
		addKeyListener(this);
		addComponentListener(this);
		net = new CraftrNet();
		gt = new CraftrGameThread(this);
		window.getRootPane().addMouseListener(this);
		window.getRootPane().addMouseMotionListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		String thost = "127.0.0.1";
		if (skipConfig) multiplayer=false;
		else thost = configure();
		isConfig=false;
		health = 5;
		gs.health = 5;
		if(!multiplayer)
		{
			gs.addChatMsg("you're running 64pixels^2 " + getVersion());
			gs.addChatMsg("created by asiekierka and GreaseMonkey.");
			Thread tmap = new Thread(cmt);
			tmap.start();
		}
		else
		{
			System.out.println("Connecting...");
			CraftrKickScreen cks = new CraftrKickScreen(canvas,"Wait a second...");
			cks.mName="CONNECTING...";
			cks.bgcolor = 0x808080;
			canvas.cs = (CraftrScreen)cks;
			net.connect(CraftrConvert.getHost(thost),CraftrConvert.getPort(thost), nagle);
			System.out.println("Connected! Logging in...");
			canvas.cs = (CraftrScreen)gs;
			gs.showHealthBar = false;
			map.net = net;
			net.gaa = this;
			map.multiplayer = true;
			Thread t1 = new Thread(net);
			t1.start();
			net.chunkRequest(0,0);
		}
		Thread t2 = new Thread(gt);
		t2.start();
		gs.setCanvas(canvas);
	}
	public void runOnce()
	{
		hasShot = false;
		if(isKick) { gt.isRunning=false; realKickOut(); }
		if(waitTime==0)
		{
			if(keyHeld[0]==true)
			{
				waitTime=movePlayer(0,-1);
			}
			if(keyHeld[1]==true)
			{
				waitTime=movePlayer(-1,0);
			}
			if(keyHeld[2]==true)
			{
				waitTime=movePlayer(1,0);
			}
			if(keyHeld[3]==true)
			{
				waitTime=movePlayer(0,1);
			}
		}
		else waitTime--;
		while(overhead>33)
		{
			if(waitTime>0) waitTime--;
			if(chrArrowWaiter>0) chrArrowWaiter--;
			overhead-=33;
		}
		if(chrArrowWaiter>0) chrArrowWaiter--;
		else if(mb == ev_1) {
			if(insideRect(mx,my,12*16+8,gs.BARPOS_Y+1,8,14))
			{
				gs.chrBarOff -= 1;
				chrArrowWaiter=2;
				mouseChange=true;
				if(gs.chrBarOff<0) gs.chrBarOff = 255;
			}
			else if(insideRect(mx,my,29*16,gs.BARPOS_Y+1,8,14))
			{
				gs.chrBarOff += 1;
				chrArrowWaiter=2;
				mouseChange=true;
				if(gs.chrBarOff>255) gs.chrBarOff=0;
			}
		}
		if(multiplayer)
		{
			for(int i=0;i<256;i++)
			{
				if(players[i] != null && players[i].posChanged)
				{
					playerChange = true;
					players[i].posChanged = false;
				}
			}
			if(netThreadRequest>0) parseNTR();
		}
		else
		{
			playerChange = players[255].posChanged;
			players[255].posChanged = false;
		}
		if(playerChange)
			map.physics.players[255] = players[255];
		if(!multiplayer || net.loginStage > 0)
		{
			processMouse();
			if(mx != oldmx || my != oldmy || mb != oldmb)
			{
				mouseChange = true;
				oldmx = mx;

				oldmy = my;
				oldmb = mb;
			}
			render();
			canvas.draw(mx,my);
		}
		frame++;
		overdate = new Date();
		overhead+=overdate.getTime()-told.getTime()-33;
		told = overdate;
		if(told.compareTo(tnew)>=0)
		{
			tnew = new Date(told.getTime() + 1000L);
			fold = frame;
			fps = (int)gt.fps;
			gt.fps=0;
			System.out.println(fps + " fps, physics runs at " + (cmt.wps-wpso) + "checks a second");
			wpso = cmt.wps;
		}
		gt.isRunning=gameOn;
	}
	public void stop()
	{
		gt.isRunning=false;
		gameOn = false;
	}
	public void end()
	{
		System.out.print("Saving... ");
		gt.isRunning=false;
		gameOn=false;
		if(map.saveDir != "")
		{
			if(!multiplayer)
			{
			for(int i=0;i<map.chunks.length;i++)
			{
				if(map.chunks[i].isSet || map.chunks[i].isUsed)
				{
					map.saveChunkFile(i);
				}
			}
			}
			else
			{
				net.sockClose();
			}
			saveConfig();
		}
		audio.kill();
		System.out.println("Done!");
	}
	public void finalize()
	{
		end();
	}
}
package client;
import common.*;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.awt.event.*;

public class CraftrGameScreen extends CraftrScreen
{
	public int CHATBOTTOM_X = 11;
	public int CHATBOTTOM_Y;
	public int BARPOS_Y;
	public int drawType;
	public int[] drawChrA = new int[256];
	public int[] drawColA = new int[256];
	public int barselMode;
	public int barType = 0;
	public int chrBarOff;
	public String chatMsg;
	public boolean viewFloorsMode;
	public boolean hideousPrompts=false;
	public int camX = 0;
	public int camY = 0;
	public int frames = 0;
	public int health = 3;
	public boolean showHealthBar = true;
	public int mx = 0;
	public int my = 0;
	public ArrayList<CraftrWindow> windows;
	public boolean isSticky;

	public CraftrBlock[] blocks;

	public CraftrChatMsg[] chatarr;
	public int chatlen;
	public CraftrCanvas c;
	public int hov_type = 0;
	public int hov_par = 0;
	
	public CraftrPlayer players[] = new CraftrPlayer[256];

	public CraftrGameScreen(CraftrCanvas cc)
	{
		c = cc;
		windows = new ArrayList<CraftrWindow>();
		if(c!=null)
		{
			blocks = new CraftrBlock[c.FULLGRID_W*c.FULLGRID_H];
			CHATBOTTOM_Y = (c.GRID_H*16)-17;
			BARPOS_Y = (c.GRID_H*16);
		}
		chatarr = new CraftrChatMsg[20];
		chatlen = 0;
		for(int i=0;i<256;i++) drawColA[i] = 15;
		for(int i=0;i<256;i++) drawChrA[i] = 1;
		drawType = 0;
		barselMode = 1; // char
		chatMsg = "";
		barType = 0;
	}

	public void invokeHelp(String t)
	{
		toggleWindow(5);
		getWindow(5).text=t;
		getWindow(5).resize();
	}

	public boolean mousePressed(MouseEvent ev)
	{
		int mb = ev.getButton();
		if (insideRect(mx,my,7*16+8,BARPOS_Y,8,8)) // type, up
		{
			if(mb==ev.BUTTON3) { invokeHelp("Choose the type."); return false; }
			drawType-=1;
			while(!CraftrBlock.isPlaceable(drawType)) drawType-=1;
			if(drawType < -1) drawType = CraftrBlock.maxType;
			while(!CraftrBlock.isPlaceable(drawType)) drawType-=1;
		} else if (insideRect(mx,my,7*16+8,BARPOS_Y+8,8,8)) // type, down
		{
			if(mb==ev.BUTTON3) { invokeHelp("Choose the type."); return false; }
			drawType+=1;
			while(!CraftrBlock.isPlaceable(drawType)) drawType+=1;
			if(drawType > CraftrBlock.maxType) drawType = -1;
			while(!CraftrBlock.isPlaceable(drawType)) drawType+=1;
		}
		else if (insideRect(mx,my,7*16,BARPOS_Y+8,8,8)) // T
		{
			if(mb==ev.BUTTON3) { invokeHelp("Open type selection window."); return false; }
			toggleWindow(4);
		}
		 else if (insideRect(mx,my,8*16+8,BARPOS_Y,24,8)) // mode, chr
		{
			if(mb==ev.BUTTON3) { invokeHelp("Set the bar mode to choosing characters."); return false; }
			barselMode = 1;
		} else if (insideRect(mx,my,8*16+8,BARPOS_Y+8,24,8)) // mode, col
		{
			if(mb==ev.BUTTON3) { invokeHelp("Set the bar mode to choosing colors."); return false; }
			barselMode = 2;
		} else if(insideRect(mx,my,30*16,BARPOS_Y,16,16))
		{
			if(mb==ev.BUTTON3) { invokeHelp("Open the character selection window."); return false; }
			toggleWindow(1);
		}
		else if(insideRect(mx,my,31*16,BARPOS_Y,16,16))
		{
			if(mb==ev.BUTTON3) { invokeHelp("Open the color selection window."); return false; }
			toggleWindow(2);
		}
		else if (drawType == 17)
		{
			if(insideRect(mx,my,12*16+8,BARPOS_Y,48,8))
			{
				if(mb==ev.BUTTON3) { invokeHelp("Unstickify piston."); return false; }
				isSticky=false;
			}
			else if(insideRect(mx,my,12*16+8,BARPOS_Y+8,48,8))
			{
				if(mb==ev.BUTTON3) { invokeHelp("Stickify piston."); return false; }
				isSticky=true;
			}
		}
		else if (drawType == 2)
		{
			if(insideRect(mx,my,12*16+8,BARPOS_Y,128,16))
			{
				if(mb==ev.BUTTON3) { invokeHelp("Select color."); return false; }
				sdrawCol((mx-(12*16+8))>>4);
			}
		}
		else if (barselMode == 1 && (drawType == 3 || drawType == 20)) // p-nand dir
		{
			if(insideRect(mx,my,12*16+8,BARPOS_Y,64,16))
			{
				if(mb==ev.BUTTON3) { invokeHelp("Select block output direction."); return false; }
				sdrawChr(24+((mx-(12*16+8))>>4));
			}
		}
		else if (barselMode == 1 && drawType == 15) // extend dir
		{
			if(insideRect(mx,my,12*16+8,BARPOS_Y,64,16))
			{
				if(mb==ev.BUTTON3) { invokeHelp("Select block output direction."); return false; }
				sdrawChr(CraftrBlock.extendDir[((mx-(12*16+8))>>4)]);
			}
		}
		else if (barselMode == 1) // checkings, chr
		{
			if(insideRect(mx,my,13*16,BARPOS_Y,256,16))
			{
				if(mb==ev.BUTTON3) { invokeHelp("Select character."); return false; }
				sdrawChr(((mx-(13*16))>>4)+chrBarOff);
			}
			else if(mb==ev.BUTTON3 && insideRect(mx,my,12*16+8,BARPOS_Y+1,8,14))
			{
				chrBarOff -= 16;
				if(chrBarOff<0) chrBarOff += 256;
				return true;
			}
			else if(mb==ev.BUTTON3 && insideRect(mx,my,29*16,BARPOS_Y+1,8,14))
			{
				chrBarOff += 16;
				if(chrBarOff>255) chrBarOff -= 256;
				return true;
			}
		} else if (barselMode == 2) // checkings, col
		{
			if(insideRect(mx,my,12*16+8,BARPOS_Y,128,16))
			{
				if(mb==ev.BUTTON3) { invokeHelp("Select colour."); return false; }
				int colChoose = (mx-(12*16+8))>>3;
				int colMode = my-BARPOS_Y;
				if(colMode>7) // FG
				{
					sdrawCol((gdrawCol()&240)|(colChoose&15));
				} else // BG
				{
					sdrawCol((gdrawCol()&15)|((colChoose&15)<<4));
				}
			}
		}
		return false;
	}

	public boolean insideRect(int mx, int my, int x, int y, int w, int h)
	{
		if(mx >= x && my >= y && mx < x+w && my < y+h)
		{
			return true;
		} else
		{
			return false;
		}
	}

	public boolean obstructedWindow(CraftrWindow w, int mx, int my)
	{
		synchronized(windows)
		{
			for(int wi = windows.size()-1;wi>windows.indexOf(w);wi--)
			{
				CraftrWindow cw = windows.get(wi);
				if(insideRect(mx,my,cw.x<<3,cw.y<<3,cw.w<<3,cw.h<<3)) return true;
			}
		}
		return false;
	}

	public boolean inWindow(int x, int y)
	{
		synchronized(windows)
		{
			for(CraftrWindow cw : windows)
				if(insideRect(mx,my,cw.x<<3,cw.y<<3,cw.w<<3,cw.h<<3)) return true;
		}
		return false;
	}

	public void setCanvas(CraftrCanvas canvas)
	{
		c=canvas;
		if(c!=null)
		{
			blocks = new CraftrBlock[c.FULLGRID_W*c.FULLGRID_H];
			CHATBOTTOM_Y = (c.GRID_H*16)-17;
			BARPOS_Y = (c.GRID_H*16);
		}
	}

	public CraftrWindow getWindow(int type)
	{
		synchronized(windows)
		{
			for(CraftrWindow cw: windows)
			{
				if(cw.type==type) return cw;
			}
		}
		return null;
	}

	public void toggleWindow(int type)
	{
		synchronized(windows)
		{
			int app = -1;
			for(CraftrWindow cw : windows)
			{
				if(cw.type == type) app = windows.indexOf(cw);
			}
			if(app>=0) windows.remove(app);
			else windows.add(new CraftrWindow(type,4)); // UID chosen by fair dice roll. Guaranteed to be unique.
		}
	}
	public int gdrawChr()
	{
		return drawChrA[drawType<0?(0xFF&(int)((byte)drawType)):drawType];
	}
	public int gdrawCol()
	{
		return drawColA[drawType<0?(0xFF&(int)((byte)drawType)):drawType];
	}
	public void sdrawChr(int c)
	{
		drawChrA[drawType<0?(0xFF&(int)((byte)drawType)):drawType] = c;
	}
	public void sdrawCol(int c)
	{
		drawColA[drawType<0?(0xFF&(int)((byte)drawType)):drawType] = c;
	}
	public void paint(int mmx, int mmy)
	{
		mx = mmx;
		my = mmy;
		c.FillRect(0x000000,0,0,c.sizeX,c.sizeY);
		CraftrBlock t;
		for(int iy=0;iy<c.FULLGRID_H-1;iy++)
		{
			for(int ix=0;ix<c.FULLGRID_W;ix++)
			{
				t = blocks[ix+(iy*c.FULLGRID_W)];
				if(t != null)
				{
					c.DrawChar(ix<<4,iy<<4,(byte)t.getDrawnChar(),(byte)t.getDrawnColor());
					if(t.isBullet())
						switch(t.getBullet())
						{
							case 1:
							case 2:
							case 3:
							case 4:
								c.DrawChar(ix<<4,iy<<4,(byte)248,(byte)15);
								break;
						}
				}else{
					c.DrawChar(ix<<4,iy<<4,(byte)177,(byte)0x08);
				}
			}
		}
		for (int i=0;i<256;i++)
		{
			if(players[i]!=null)
			{
				c.FillRect(0x000000,players[i].px<<4,players[i].py<<4,16,16);
				c.DrawChar(players[i].px<<4,players[i].py<<4,players[i].pchr,players[i].pcol);
			}
		}
		switch(barType)
		{
			case 0: DrawBar(); break;
			case 1: DrawChatBar(); break;
		}
		DrawMouse();
		DrawChatMsg();
		DrawHealthBar();
		for(int i=0;i<256;i++)
		{
			if(players[i] != null)
			{
				if(mx>>4 == players[i].px && my>>4 == players[i].py && !inWindow(mx,my))
				{
					writeChat((players[i].px*16+8)-((players[i].name.length()*8)>>1),players[i].py*16-10,new CraftrChatMsg(players[i].name));
				}
			}
		}
		synchronized(windows)
		{
			for(CraftrWindow cw : windows)
			{
				cw.typeChosen = drawType;
				cw.charChosen = gdrawChr();
				cw.colorChosen = gdrawCol();
				cw.isMelodium=(drawType==7);
				cw.render(c);
			}
		}
		frames++;
	}

	
	// chat processing
	
	public void addChatMsg(String msg)
	{
		// This fixes the case that there is TOO MUCH CHAT GOING ON BRO
		System.arraycopy(chatarr,0,chatarr,1,19);
		chatarr[0] = new CraftrChatMsg(msg);
		if(chatlen<20) chatlen++;
		
	}
	public void writeChat(int x, int y, CraftrChatMsg msg)
	{
		int tx = x+1;
		int ty = y+1;
		int offs = 0;
		byte col = (byte)15;
		c.FillRect(0x000000,x-1,y-1,1,10);
		for(int i=0; i<msg.msglen; i++)
		{
			c.FillRect(0x000000,x+((i-offs)<<3),y-1,9,10);
			String z;
			char[] t = new char[1];
			if((i+2)<msg.msglen)
			{
				z = msg.message[i+1]+"";
				t=z.toUpperCase().toCharArray();
			}
			if(msg.message[i]=='&' && ((i+2)<msg.msglen) && ((t[0]>'0' && t[0]<='9') || (t[0]>='A' && t[0]<='F')))
			{
				col=(byte)(t[0]-'0');
				if(col>9) col=(byte)((t[0]-'A')+10);
				i+=2;
				offs+=2;
			} 
			c.DrawChar1x(tx+((i-offs)<<3),ty,(byte)msg.message[i],col);
		}
	}
	public void DrawHealthBar()
	{
		if(!showHealthBar) return;
		int startX = c.WIDTH-42;
		int startY = BARPOS_Y-9;
		for(int i=0;i<5;i++)
			if(health>i)
				c.DrawChar1x(startX+(i<<3),startY,(byte)3,(byte)0x0C);
			else
				c.DrawChar1x(startX+(i<<3),startY,(byte)3,(byte)0x08);
	}
	public void DrawChatMsg()
	{
		if(chatlen>0)
		{
			Date now = new Date();
			int ix=CHATBOTTOM_X;
			for(int i=0;i<chatlen;i++)
			{
				if ((now.compareTo(chatarr[i].expirytime) < 0) || barType==1)
				{
					writeChat(ix,CHATBOTTOM_Y-(i*10),chatarr[i]);
				}
			}
		}
	}

	// painting handlers
	
	public void DrawMouse()
	{
		if(mx >= 0 && mx < c.WIDTH && my >= 0 && my < (c.GRID_H<<4))
		{
			String tstr=CraftrBlock.getName(hov_type);
			c.DrawRect(0xAAAAAA,(mx&(~15)),(my&(~15)),15,15);
			if(viewFloorsMode) writeChat(mx-(tstr.length()<<2),my-10,new CraftrChatMsg(tstr));
		}
	}
	
	public void DrawChatBar()
	{
		c.FillRect(0x000000,0,BARPOS_Y,c.WIDTH,16);
		c.DrawString1x(0,BARPOS_Y,">" + chatMsg, 15);
	}
	
	public boolean isArrow()
	{
		return (drawType==3 || drawType==15 || drawType==20);
	}

	public void DrawBar()
	{
		c.FillRect(0x000000,0,BARPOS_Y,c.WIDTH,16);
		if(hideousPrompts) c.DrawString1x(0,BARPOS_Y+16,"      Type       ",9);
		c.DrawString(0,BARPOS_Y,CraftrBlock.getName(drawType),15);
		c.DrawChar1x(7*16,BARPOS_Y+8,(byte)'T',(byte)10);
		c.DrawChar1x(7*16+8,BARPOS_Y,(byte)30,(byte)14);
		c.DrawChar1x(7*16+8,BARPOS_Y+8,(byte)31,(byte)14);
		c.DrawChar1x(8*16,BARPOS_Y,(byte)179,(byte)15);
		c.DrawChar1x(8*16,BARPOS_Y+8,(byte)179,(byte)15);
		c.DrawChar1x(10*16,BARPOS_Y,(byte)179,(byte)15);
		c.DrawChar1x(10*16,BARPOS_Y+8,(byte)179,(byte)15);
		if(drawType==2) c.DrawChar(10*16+12,BARPOS_Y,(byte)197,(byte)((gdrawCol()&7)+8));
		else c.DrawChar(10*16+12,BARPOS_Y,(byte)gdrawChr(),(byte)gdrawCol());
		c.DrawChar1x(12*16,BARPOS_Y,(byte)179,(byte)15);
		c.DrawChar1x(12*16,BARPOS_Y+8,(byte)179,(byte)15);
		if(drawType == 4) barselMode=2;
		int bsmt = barselMode;
		if(drawType == 2) bsmt=3;
		else if(isArrow() && barselMode == 1) bsmt=4;
		else if(drawType == 17) bsmt=5;
		else if(drawType >= 21 && drawType <= 22) bsmt=0;
		if((drawType == 3 || drawType ==20)&& (gdrawChr()<24 || gdrawChr()>=28)) sdrawChr(25);
		else if(drawType == 15 && !(gdrawChr()==30 || gdrawChr()==31 || gdrawChr()==16 || gdrawChr()==17)) sdrawChr(31);
		else if(drawType==4) sdrawChr(206);
		else if (drawType == 17)
		{
			if(isSticky) sdrawCol(0x2E);
			else sdrawCol(0x7F);
			sdrawChr(177);
		}
		else if (drawType == 21)
		{
			sdrawCol(0x06);
			sdrawChr(153);
		} else if (drawType == 22)
		{
			sdrawCol(0x0B);
			sdrawChr(227);
		}
		switch(bsmt)
		{
			case 1: // char
				if(hideousPrompts) c.DrawString1x(12*16+8,BARPOS_Y+16,"               Char               ",9);
				c.DrawString1x(8*16+8,BARPOS_Y,"Chr",240);
				c.DrawString1x(8*16+8,BARPOS_Y+8,"Col",15);
				c.DrawChar1x(12*16+8,BARPOS_Y+4,(byte)17,(byte)14);
				for(int j=0;j<16;j++)
				{
					c.DrawChar(13*16+(j<<4),BARPOS_Y,(byte)(chrBarOff+j),(byte)gdrawCol());
				}
				c.DrawChar1x(29*16,BARPOS_Y+4,(byte)16,(byte)14);
				c.DrawChar1x(29*16+8,BARPOS_Y,(byte)179,(byte)15);
				c.DrawChar1x(29*16+8,BARPOS_Y+8,(byte)179,(byte)15);
				if(gdrawChr() >= chrBarOff && gdrawChr() < chrBarOff+16)
				{
					c.DrawRect(0xAAAAAA,13*16+((gdrawChr()-chrBarOff)*16),BARPOS_Y,15,15);
				}
				break;
			case 2: // color
				if(hideousPrompts) c.DrawString1x(12*16+8,BARPOS_Y+16,"      Color       ",9);
				if(isArrow()) c.DrawString1x(8*16+8,BARPOS_Y,"Dir",15);
				else if (drawType != 4) c.DrawString1x(8*16+8,BARPOS_Y,"Chr",15);
				c.DrawString1x(8*16+8,BARPOS_Y+8,"Col",240);
				for(int j=0;j<16;j++)
				{
					c.DrawChar1x(12*16+8+(j<<3),BARPOS_Y,(byte)254,(byte)((j<<4)|(gdrawCol()&15)));
					c.DrawChar1x(12*16+8+(j<<3),BARPOS_Y+8,(byte)254,(byte)(j|(gdrawCol()&240)));
				}
				c.DrawChar1x(20*16+10,BARPOS_Y,(byte)'B',(byte)15);
				c.DrawChar1x(20*16+10,BARPOS_Y+8,(byte)'F',(byte)15);
				c.DrawChar1x(21*16+2,BARPOS_Y,(byte)'G',(byte)15);
				c.DrawChar1x(21*16+2,BARPOS_Y+8,(byte)'G',(byte)15);
				c.DrawChar1x(21*16+10,BARPOS_Y,(byte)179,(byte)15);
				c.DrawChar1x(21*16+10,BARPOS_Y+8,(byte)179,(byte)15);
				c.DrawRect(0xAAAAAA,12*16+8+((gdrawCol()>>4)<<3),BARPOS_Y,7,7);
				c.DrawRect(0xAAAAAA,12*16+8+((gdrawCol()&15)<<3),BARPOS_Y+8,7,7);
				break;
			case 3: // wirium color
				for(int j=0;j<8;j++)
				{
					c.DrawChar(12*16+8+(j<<4),BARPOS_Y,(byte)254,(byte)j);
				}
				c.DrawString1x(20*16+12,BARPOS_Y,"Wirium",15);
				c.DrawString1x(20*16+12,BARPOS_Y+8,"Colors",15);
				c.DrawChar1x(24*16,BARPOS_Y,(byte)179,(byte)15);
				c.DrawChar1x(24*16,BARPOS_Y+8,(byte)179,(byte)15);
				c.DrawRect(0xAAAAAA,12*16+8+((gdrawCol()&7)<<4),BARPOS_Y,15,15);
				break;
			case 4: // p-nand direction
				c.DrawString1x(8*16+8,BARPOS_Y,"Dir",240);
				c.DrawString1x(8*16+8,BARPOS_Y+8,"Col",15);
				if(drawType==3 || drawType==20)
				{
					for(int j=24;j<28;j++)
					{
						c.DrawChar(12*16+8+((j-24)<<4),BARPOS_Y,(byte)j,(byte)15);
					}
					c.DrawRect(0xAAAAAA,12*16+8+((gdrawChr()-24)<<4),BARPOS_Y,15,15);
				}
				else if(drawType==15)
				{
					c.DrawChar(12*16+8,BARPOS_Y,(byte)30,(byte)15);
					c.DrawChar(12*16+24,BARPOS_Y,(byte)31,(byte)15);
					c.DrawChar(12*16+40,BARPOS_Y,(byte)16,(byte)15);
					c.DrawChar(12*16+56,BARPOS_Y,(byte)17,(byte)15);
					switch(gdrawChr())
					{
						case 30:
						case 31:
							c.DrawRect(0xAAAAAA,12*16+8+((gdrawChr()-30)<<4),BARPOS_Y,15,15);
							break;
						case 16:
						case 17:
							c.DrawRect(0xAAAAAA,12*16+40+((gdrawChr()-16)<<4),BARPOS_Y,15,15);
							break;
					}
				}
				c.DrawString1x(16*16+12,BARPOS_Y+4,"Direction",15);
				c.DrawChar1x(21*16+8,BARPOS_Y,(byte)179,(byte)15);
				c.DrawChar1x(21*16+8,BARPOS_Y+8,(byte)179,(byte)15);
				break;
			case 5: // pusher info
				if(isSticky)
				{
					c.DrawString1x(12*16+8,BARPOS_Y,"Normal",15);
					c.DrawString1x(12*16+8,BARPOS_Y+8,"Sticky",240);
				} else
				{
					c.DrawString1x(12*16+8,BARPOS_Y,"Normal",240);
					c.DrawString1x(12*16+8,BARPOS_Y+8,"Sticky",15);
				}
				break;
			default:
				break;
		}
		c.DrawChar(30*16,BARPOS_Y,(byte)1,(byte)12);
		c.DrawChar(31*16,BARPOS_Y,(byte)'C',(byte)9);
	}
	
	public int addPlayer(int id, int scrx, int scry, String name, byte ch, byte co)
	{
		players[id] = new CraftrPlayer(scrx,scry,ch,co,name);
		return id;
	}

	public void removePlayer(int id)
	{
		players[id] = null;
	}
}
package client;
import common.*;

import java.lang.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import java.util.concurrent.*;

public class CraftrNet implements Runnable, CraftrNetShim
{
	public Socket socket;
	public DataOutputStream out;
	public ByteArrayOutputStream out2;
	private DataInputStream in;
	private byte[] cbuffer;
	public String nick;
	public int loginStage = 0;
	public boolean isLoadingChunk = false;
	public int loadChunkID, lcX, lcY, lcP;
	public int chunkPacketsLeft, chunkSize, chunkType;
	public CraftrNetSender ns;
	public boolean isOp=false;
	public int frames=0;
	public int pingsWaiting=0;
	public CraftrAuth auth;
	private byte[] msgenc = new byte[32];
	
	public CraftrNet()
	{
		// Here we initalize the socket.
		try
		{ 
			cbuffer = new byte[65536];
		} catch(Exception e)
		{
			System.out.println("Fatal CraftrNet error!");
			System.exit(1);
		}
	}
	public void connect(String host, int port, int nagle)
	{
		try
		{
			socket = new Socket(host,port);
			while(!socket.isConnected())
			{
				Thread.sleep(10);
			}
			socket.setTcpNoDelay(true);
			in = new DataInputStream(socket.getInputStream());
			out2 = new ByteArrayOutputStream(65536);
			out = new DataOutputStream(out2);
			ns = new CraftrNetSender(socket.getOutputStream());
			Thread tns = new Thread(ns);
			tns.start();
		} catch(Exception e)
		{
			System.out.println("Fatal CraftrNet error!");
			System.exit(1);
		}
	}
	public void sendPacket()
	{
		sendPacket(getPacket());
	}

	public void sendPacket(byte[] t)
	{
		try
		{
			synchronized(ns.packets)
			{
				ns.packets.offer(t);
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrServer sendPacket(byte[]) error!");
			System.exit(1);
		} 
	}
	
	public void sockClose()
	{
		try
		{
			if(socket.isConnected())
			{
				synchronized(out)
				{
					out.writeByte(0x2A);
				}
				sendPacket();
			}
			socket.close();
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal CraftrServer sockClose error!");
		} 
	}
	
	
	public byte[] getPacket()
	{
		byte[] t = out2.toByteArray();
		out2.reset();
		return t;
	}
	public String readString()
	{
		try
		{
			int la = in.readUnsignedByte();
			byte[] t = new byte[la];
			in.read(t,0,la);
			return new String(t);
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal CraftrServer readString error!");
			return "";
		} 
	}
	
	public void writeString(String s)
	{
		try
		{
			byte[] t = s.getBytes();
			synchronized(out)
			{
				out.writeByte(s.length());
				out.write(t,0,s.length());
			}
		}
		catch(Exception e)
		{
			System.out.println("Non-fatal CraftrServer writeString error!");
			try
			{
				synchronized(out){out.writeByte(0x00);}
			}
			catch(Exception ee)
			{
				System.out.println("Fatal CraftrServer writeString error!");
				System.exit(1);
			}
		} 
	}
	
	public void chunkRequest(int x, int y)
	{
		try
		{
			System.out.println("request-net: " + x + " " + y);
			synchronized(out)
			{
				out.writeByte(0x10);
				out.writeInt(x);
				out.writeInt(y);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal craftrNet chunkRequest Error!");
			System.exit(1);
		}
	}
	public void shoot(int x, int y, int dir)
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x70);
				out.writeInt(x);
				out.writeInt(y);
				out.writeByte((byte)dir);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal craftrNet shoot Error!");
			System.exit(1);
		}
	}

 	public void playerPush(int dx, int dy)
 	{
 		try
 		{
 			synchronized(out)
 			{
 				out.writeByte(0xE0);
 				out.writeByte((byte)dx);
 				out.writeByte((byte)dy);
 				sendPacket();
 			}
 		}
 		catch(Exception e)
		{
 			System.out.println("Fatal craftrNet playerPush Error!");
 			System.exit(1);
 		}
 	}

	public void sendChatMsg(String msg)
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x40);
				writeString(msg);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal craftrNet sendChatMsg Error!");
			System.exit(1);
		}
	}
	
	public void sendBlock(int dx, int dy, byte t, byte ch, byte co)
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x30);
				out.writeInt(dx);
				out.writeInt(dy);
				out.writeByte(t);
				out.writeByte(ch);
				out.writeByte(co);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal craftrNet sendBlock Error!");
			System.exit(1);
		}
	}
	public void playerMove(int dx, int dy)
	{
		try
		{
			int i = 0;
			for(i=0;i<4;i++)
			{
				if(CraftrMap.xMovement[i]==dx && CraftrMap.yMovement[i]==dy)
				{
					i+=0x2C;
					break;
				}
			}
			synchronized(out)
			{
				if(i>=0x2C)
				{
					out.writeByte((byte)i);
				}
				else
				{
					out.writeByte(0x23);
					out.writeByte((byte)dx);
					out.writeByte((byte)dy);
				}
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal craftrNet playerMove Error!");
			System.exit(1);
		}
	}
	
	CraftrGame gaa;
	
	public void run()
	{
		try
		{
			while(loginStage != 255 && socket.isConnected())
			{
				loop(gaa);
				Thread.sleep(5);
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal CraftrNet thread run() error!");
			System.exit(1);
		}
	}
	
	public void respawnRequest()
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x25);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal craftrNet playerMove Error!");
			System.exit(1);
		}
	}
	
	public void sendDecrypt(String pass)
	{
		try
		{
			synchronized(out)
			{
				out.writeByte(0x51);
				auth = new CraftrAuth(pass);
				byte[] dec = auth.decryptClient(msgenc);
				out.write(dec,0,32);
				sendPacket();
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal craftrNet sendDecrypt Error!");
			System.exit(1);
		}
	}
	public void loop(CraftrGame game)
	{
		try
		{
			if(loginStage == 0)
			{
				synchronized(out)
				{
					out.writeByte(0x0F);
					writeString(nick);
					writeString("eeeeh");
					out.writeByte(0x00);
					out.writeByte(0x7F); // compatibility purposes, NEVER REMOVE. NEVER. NEVER!!!
					out.writeInt(CraftrVersion.getProtocolVersion());
					out.writeByte(game.players[255].pchr);
					out.writeByte(game.players[255].pcol);
					sendPacket();
				}
				loginStage = 1;
			}
			else
			{
				if(game.isKick) { ns.isRunning=false; }
				int len = 1;
				if(!socket.isConnected() || !ns.isRunning)
				{
					if(!game.isKick) game.kickOut("Disconnected!");
					return;
				}
				while(len>0)
				{
					byte[] buf = new byte[1];
					if(in.available() > 0) len = in.read(buf,0,1);
					else len=0;
					if(len>0)
					{
						switch((int)(buf[0]&0xFF))
						{
							case 0x01:
							{
								if(loginStage>=2)
								{
									in.readInt();
									in.readInt();
									readString();
									in.readShort();
								} else {
									loginStage=2;
									game.players[255].px=in.readInt();
									game.players[255].py=in.readInt();
									int tx = game.players[255].px>>6;
									int ty = game.players[255].py>>6;
									chunkRequest(tx,ty);
									chunkRequest(tx+1,ty);
									chunkRequest(tx,ty+1);
									chunkRequest(tx+1,ty+1);
									nick = readString();
									isOp=in.readUnsignedShort()==42;
									game.players[255].name = nick;
								}
								System.out.println("Logged in!");
								break;
							}
							case 0x11:
								chunkType = in.readUnsignedByte();
								isLoadingChunk=true;
								lcX = in.readInt();
								lcY = in.readInt();
								lcP = 0;
								System.out.println("getting chunk " + lcX + "," + lcY);
								synchronized(game.map)
								{
									loadChunkID = game.map.findNewChunkID(lcX,lcY);
								}
								if(loadChunkID < 0) isLoadingChunk = false; // haha! take that, silly servers
								chunkPacketsLeft=in.readInt();
								if(chunkPacketsLeft<0 || chunkPacketsLeft>131072)
								{
									isLoadingChunk=false;
									break;
								}
								chunkSize=chunkPacketsLeft;
								cbuffer = new byte[chunkPacketsLeft];
								break;
							case 0x12:
								int tp = in.readUnsignedShort();
								if(isLoadingChunk)
								{
									in.readFully(cbuffer,lcP,tp);
									lcP+=tp;
									chunkPacketsLeft-=tp;
								}
								else
								{
									for(int i=0;i<tp;i++) in.readByte();
								}
								break;
							case 0x13:
								if(isLoadingChunk)
								{
									isLoadingChunk=false;
									if(chunkType==1)
									{
										GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(cbuffer,0,lcP));
										byte[] tmp1 = new byte[65536];
										int ttp = 0;
										int rp = 0;
										while(rp >= 0)
										{
											ttp+=rp;
											rp = gin.read(tmp1,ttp,65536-ttp);
										}
										synchronized(game.map)
										{
											game.map.chunks[loadChunkID].loadByteNet(tmp1);
										}
									}
									game.netChange=true;
								}
								break;
							case 0x20:
								int t1 = in.readUnsignedByte();
								String tmp2 = readString();
								int px = in.readInt();
								int py = in.readInt();
								byte chr = in.readByte();
								byte col = in.readByte();
								game.players[t1] = new CraftrPlayer(px,py,chr,col,tmp2);
								game.players[t1].posChanged = true;
								game.players[t1].ncol = in.readByte();
								break;
							case 0x21:
								int t2 = in.readUnsignedByte();
								int dx1 = in.readByte();
								int dy1 = in.readByte();
								if(game.players[t2] != null)
								{
									game.players[t2].moveDelta(dx1,dy1);
								}
								break;
							case 0x22:
								int t22 = in.readUnsignedByte();
								if(t22==255) break;
								game.players[t22] = null;
								break;
							case 0x24:
								int ta1 = in.readUnsignedByte();
								int dx2 = in.readInt();
								int dy2 = in.readInt();
								if(game.players[ta1] != null)
								{
									game.players[ta1].move(dx2,dy2);
								}
								game.netChange=true;
								break;
							case 0x26:
								int ta25 = in.readUnsignedByte();
								if(game.players[ta25]!=null) game.players[ta25].name = readString();
								break;
							case 0x27:
								game.netThreadRequest = 1;
								break;
							case 0x28:
								int t28=in.readByte();
								isOp=t28==42?true:false;
								break;
							case 0x2A:
							case 0x2B:
								int bx2c=in.readInt();
								int by2c=in.readInt();
								byte[] d2c;
								synchronized(game.map)
								{
									d2c = game.map.getBlock(bx2c,by2c).getBlockData();
								}
								int t2c = buf[0]&0x01;	
								int t22c = 0x80&(int)d2c[1];
								int t32c = -1;
								if(t22c!=0 && t2c==0) t32c=0;
								if(t22c==0 && t2c!=0) t32c=1;
								d2c[1] = (byte)((d2c[1]&0x7f) | (t2c<<7));
								synchronized(game.map)
								{
									game.map.setBlock(bx2c,by2c,d2c);
								}
								if(t32c>=0)
								{
									switch(d2c[0])
									{
										case 5:
											game.playSample(bx2c,by2c,t32c);
											break;
										case 6:
											game.playSample(bx2c,by2c,t32c+2);
											break;
										default:
											break;
									}
								}  
								break;
							case 0x2C:
							case 0x2D:
							case 0x2E:
							case 0x2F:
								int id2f = in.readUnsignedByte();
								int dir2f = buf[0]&0x03;
								int dx2f = CraftrMap.xMovement[dir2f];
								int dy2f = CraftrMap.yMovement[dir2f];
								if(game.players[id2f] != null)
								{
									game.players[id2f].moveDelta(dx2f,dy2f);
								}
								break;
							case 0x31:
							case 0x33:
								in.readUnsignedByte();
								int bx1 = in.readInt();
								int by1 = in.readInt();
								byte t3 = in.readByte();
								byte ch1 = in.readByte();
								byte co1 = in.readByte();
 								if(t3 == -1)
 								{
 									synchronized(game.map)
 									{
 										game.map.setPushable(bx1,by1,ch1,co1);
 									}
 								} else
								{
 									synchronized(game.map)
 									{
 										game.map.setBlock(bx1,by1,t3,(byte)0,ch1,co1);
 										if(buf[0]!=0x33) game.map.setPushable(bx1,by1,(byte)0,(byte)0);
										for(int i=0;i<4;i++)
										{
											CraftrBlock t = game.map.getBlock(bx1+game.map.xMovement[i],by1+game.map.yMovement[i]);
											game.map.setBlock(bx1+game.map.xMovement[i],by1+game.map.yMovement[i],t.getType(),t.getParam(),game.map.updateLook(t),t.getBlockColor());
										}
 									}
 								}
								game.blockChange=true;
								break;
 							case 0x32:
								int pid = in.readUnsignedByte();
								int lolx = in.readInt();
								int loly = in.readInt();
								byte lolvx = in.readByte();
								byte lolvy = in.readByte();
								byte nchr = in.readByte();
								byte ncol = in.readByte();
								synchronized(game.map)
								{
									game.map.setPushable(lolx,loly,(byte)0,(byte)0);
									game.map.setPushable(lolx+lolvx,loly+lolvy,nchr,ncol);
								}
								if(game.players[pid] != null)
								{
									if(pid==255)
									{
										synchronized(out)
										{
											out.writeByte(0x28);
											out.writeInt(lolx);
											out.writeInt(loly);
											sendPacket();
										}
									}
									game.players[pid].move(lolx,loly);
								}
								game.blockChange=true;
								break;
							case 0x34:
								int lol2x = in.readInt();
								int lol2y = in.readInt();
								game.map.clearBlock(lol2x,lol2y);
								break;
							case 0x41:
								int ta41 = in.readUnsignedByte();
								String tmp3 = readString();
								game.gs.addChatMsg(tmp3);
								game.netChange = true;
								break;
							case 0x50:
								in.read(msgenc,0,32);
								game.netThreadRequest = 1;
								break;
							case 0x60: // sound GET
								int ta60x = in.readByte();
								int ta60y = in.readByte();
								int ta60v = in.readUnsignedByte();
								// best not to screw up meloders --GM
								if(!game.muted) game.audio.playNote(ta60x,ta60y,ta60v,1.0);
								break;
							case 0x70:
								int x70 = in.readInt();
								int y70 = in.readInt();
								byte t70 = in.readByte();
								synchronized(game.map)
 								{
 									game.map.setBullet(x70,y70,t70);
 								}
								break;
							case 0x80: // reload map
							{
								game.players[255].px=in.readInt();
								game.players[255].py=in.readInt();
								int tx = game.players[255].px>>6;
								int ty = game.players[255].py>>6;
								chunkRequest(tx,ty);
								chunkRequest(tx+1,ty);
								chunkRequest(tx,ty+1);
								chunkRequest(tx+1,ty+1);
								synchronized(game.map)
								{
									game.map.wipeChunks();
								}
								break;
							}
							case 0x81:
							{
								game.raycasting=false;
								break;
							}
							case 0x82:
							{
								game.raycasting=true;
								break;
							}
							case 0x90: // die
							{
								game.kill();
								break;
							}
							case 0x91: // set health
							{
								game.setHealth(in.readUnsignedByte()%6);
								break;
							}
							case 0x92: // toggle health bar
							{
								game.gs.showHealthBar = (in.readByte()==1);
								break;
							}
							case 0xE1: // push me
							case 0xE2: // push me
								int e1x = in.readInt();
								int e1y = in.readInt();
								int e1xs = in.readShort();
								int e1ys = in.readShort();
								int e1dx = in.readByte();
								int e1dy = in.readByte();
								game.map.pushMultiple(e1x,e1y,e1xs,e1ys,e1dx,e1dy,((int)(buf[0]&0xFF)==0xE2));
								break;
							case 0xF0:
								synchronized(out)
								{
									out.writeByte(0xF1);
									sendPacket();
								}
								break;
							case 0xF1:
								pingsWaiting--;
								break;
							case 0xF5:
								String tmp4 = readString();
								game.kickOut(tmp4);
								break;
						}
					}
				}
				frames++;
				if(frames%130==0 && game.players[255]!=null) // every 2 seconds, less twempowawy measuwe
				{
					synchronized(out)
					{
						out.writeByte(0x28);
						out.writeInt(game.players[255].px);
						out.writeInt(game.players[255].py);
						sendPacket();
					}
				}
				if(frames%625==0) // every 10 seconds
				{
					synchronized(out)
					{
						out.writeByte(0xF0);
						sendPacket();
					}
					pingsWaiting++;
					if(pingsWaiting>10)
					{
						System.out.println("The server probably went down!");
						System.exit(1);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Fatal craftrNet Loop Error!");
			e.printStackTrace();
			sockClose();
			System.exit(1);
		}
	}
}
// $ANTLR 3.2 Sep 23, 2009 12:02:23 Company.g 2012-05-22 14:37:42

package org.softlang.parser;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CompanyLexer extends Lexer {
    public static final int WS=6;
    public static final int T__12=12;
    public static final int T__11=11;
    public static final int T__14=14;
    public static final int T__13=13;
    public static final int T__10=10;
    public static final int FLOAT=5;
    public static final int EOF=-1;
    public static final int T__9=9;
    public static final int T__8=8;
    public static final int T__7=7;
    public static final int STRING=4;

    // delegates
    // delegators

    public CompanyLexer() {;} 
    public CompanyLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public CompanyLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "Company.g"; }

    // $ANTLR start "T__7"
    public final void mT__7() throws RecognitionException {
        try {
            int _type = T__7;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:7:6: ( 'company' )
            // Company.g:7:8: 'company'
            {
            match("company"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__7"

    // $ANTLR start "T__8"
    public final void mT__8() throws RecognitionException {
        try {
            int _type = T__8;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:8:6: ( '{' )
            // Company.g:8:8: '{'
            {
            match('{'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__8"

    // $ANTLR start "T__9"
    public final void mT__9() throws RecognitionException {
        try {
            int _type = T__9;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:9:6: ( '}' )
            // Company.g:9:8: '}'
            {
            match('}'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__9"

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:10:7: ( 'department' )
            // Company.g:10:9: 'department'
            {
            match("department"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:11:7: ( 'manager' )
            // Company.g:11:9: 'manager'
            {
            match("manager"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:12:7: ( 'employee' )
            // Company.g:12:9: 'employee'
            {
            match("employee"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:13:7: ( 'address' )
            // Company.g:13:9: 'address'
            {
            match("address"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:14:7: ( 'salary' )
            // Company.g:14:9: 'salary'
            {
            match("salary"); 


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "STRING"
    public final void mSTRING() throws RecognitionException {
        try {
            int _type = STRING;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:44:9: ( '\"' (~ '\"' )* '\"' )
            // Company.g:44:13: '\"' (~ '\"' )* '\"'
            {
            match('\"'); 
            // Company.g:44:17: (~ '\"' )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\u0000' && LA1_0<='!')||(LA1_0>='#' && LA1_0<='\uFFFF')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // Company.g:44:18: ~ '\"'
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match('\"'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "STRING"

    // $ANTLR start "FLOAT"
    public final void mFLOAT() throws RecognitionException {
        try {
            int _type = FLOAT;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:45:9: ( ( '0' .. '9' )+ ( '.' ( '0' .. '9' )+ )? )
            // Company.g:45:13: ( '0' .. '9' )+ ( '.' ( '0' .. '9' )+ )?
            {
            // Company.g:45:13: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // Company.g:45:14: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt2 >= 1 ) break loop2;
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);

            // Company.g:45:25: ( '.' ( '0' .. '9' )+ )?
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='.') ) {
                alt4=1;
            }
            switch (alt4) {
                case 1 :
                    // Company.g:45:26: '.' ( '0' .. '9' )+
                    {
                    match('.'); 
                    // Company.g:45:30: ( '0' .. '9' )+
                    int cnt3=0;
                    loop3:
                    do {
                        int alt3=2;
                        int LA3_0 = input.LA(1);

                        if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                            alt3=1;
                        }


                        switch (alt3) {
                    	case 1 :
                    	    // Company.g:45:31: '0' .. '9'
                    	    {
                    	    matchRange('0','9'); 

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt3 >= 1 ) break loop3;
                                EarlyExitException eee =
                                    new EarlyExitException(3, input);
                                throw eee;
                        }
                        cnt3++;
                    } while (true);


                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "FLOAT"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // Company.g:46:9: ( ( ' ' | ( '\\r' )? '\\n' | '\\t' )+ )
            // Company.g:46:13: ( ' ' | ( '\\r' )? '\\n' | '\\t' )+
            {
            // Company.g:46:13: ( ' ' | ( '\\r' )? '\\n' | '\\t' )+
            int cnt6=0;
            loop6:
            do {
                int alt6=4;
                switch ( input.LA(1) ) {
                case ' ':
                    {
                    alt6=1;
                    }
                    break;
                case '\n':
                case '\r':
                    {
                    alt6=2;
                    }
                    break;
                case '\t':
                    {
                    alt6=3;
                    }
                    break;

                }

                switch (alt6) {
            	case 1 :
            	    // Company.g:46:14: ' '
            	    {
            	    match(' '); 

            	    }
            	    break;
            	case 2 :
            	    // Company.g:46:18: ( '\\r' )? '\\n'
            	    {
            	    // Company.g:46:18: ( '\\r' )?
            	    int alt5=2;
            	    int LA5_0 = input.LA(1);

            	    if ( (LA5_0=='\r') ) {
            	        alt5=1;
            	    }
            	    switch (alt5) {
            	        case 1 :
            	            // Company.g:46:18: '\\r'
            	            {
            	            match('\r'); 

            	            }
            	            break;

            	    }

            	    match('\n'); 

            	    }
            	    break;
            	case 3 :
            	    // Company.g:46:29: '\\t'
            	    {
            	    match('\t'); 

            	    }
            	    break;

            	default :
            	    if ( cnt6 >= 1 ) break loop6;
                        EarlyExitException eee =
                            new EarlyExitException(6, input);
                        throw eee;
                }
                cnt6++;
            } while (true);

            skip();

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    public void mTokens() throws RecognitionException {
        // Company.g:1:8: ( T__7 | T__8 | T__9 | T__10 | T__11 | T__12 | T__13 | T__14 | STRING | FLOAT | WS )
        int alt7=11;
        switch ( input.LA(1) ) {
        case 'c':
            {
            alt7=1;
            }
            break;
        case '{':
            {
            alt7=2;
            }
            break;
        case '}':
            {
            alt7=3;
            }
            break;
        case 'd':
            {
            alt7=4;
            }
            break;
        case 'm':
            {
            alt7=5;
            }
            break;
        case 'e':
            {
            alt7=6;
            }
            break;
        case 'a':
            {
            alt7=7;
            }
            break;
        case 's':
            {
            alt7=8;
            }
            break;
        case '\"':
            {
            alt7=9;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt7=10;
            }
            break;
        case '\t':
        case '\n':
        case '\r':
        case ' ':
            {
            alt7=11;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 7, 0, input);

            throw nvae;
        }

        switch (alt7) {
            case 1 :
                // Company.g:1:10: T__7
                {
                mT__7(); 

                }
                break;
            case 2 :
                // Company.g:1:15: T__8
                {
                mT__8(); 

                }
                break;
            case 3 :
                // Company.g:1:20: T__9
                {
                mT__9(); 

                }
                break;
            case 4 :
                // Company.g:1:25: T__10
                {
                mT__10(); 

                }
                break;
            case 5 :
                // Company.g:1:31: T__11
                {
                mT__11(); 

                }
                break;
            case 6 :
                // Company.g:1:37: T__12
                {
                mT__12(); 

                }
                break;
            case 7 :
                // Company.g:1:43: T__13
                {
                mT__13(); 

                }
                break;
            case 8 :
                // Company.g:1:49: T__14
                {
                mT__14(); 

                }
                break;
            case 9 :
                // Company.g:1:55: STRING
                {
                mSTRING(); 

                }
                break;
            case 10 :
                // Company.g:1:62: FLOAT
                {
                mFLOAT(); 

                }
                break;
            case 11 :
                // Company.g:1:68: WS
                {
                mWS(); 

                }
                break;

        }

    }


 

}package controller;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.CampusView;
import view.CampusViewStateUpdater;
import view.CampusView.CampusViewMessageListener;

import model.CampusModel;


public class CampusViewController implements KeyListener, MouseListener, CampusViewMessageListener {
	
	private static String DEFAULT_SELECTOR_TEXT = "Select building";
	
	private CampusView view;
	private CampusModel model;
	
	private JComboBox start_select,end_select;
	private JButton start_button;
	private JTextArea output_text;
	private JCheckBox disable_rotation;
	private JScrollPane output_pane;
	
	private Map<String,String> long_to_short = new HashMap<String,String>();
	
	
	public CampusViewController(CampusView view, CampusModel model) {
		this.view = view;
		this.model = model;
		Set<String> shorts = model.get_all_short();
		for (String s : shorts) {
			long_to_short.put(model.get_short_long(s), s);
		}
	}
	
	public void add_controls(JPanel panel) {
		panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
		
		JPanel start_sel_panel = new JPanel(new BorderLayout());
		start_sel_panel.add(new JLabel("Start:"),BorderLayout.WEST);
		start_select = make_combobox_selector(model);
		start_sel_panel.add(start_select,BorderLayout.SOUTH);
		panel.add(start_sel_panel);
		
		JPanel end_sel_panel = new JPanel(new BorderLayout());
		end_sel_panel.add(new JLabel("End:"),BorderLayout.WEST);
		end_select = make_combobox_selector(model);
		end_sel_panel.add(end_select,BorderLayout.SOUTH);
		panel.add(end_sel_panel);
		
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel start_button_panel = new JPanel(new BorderLayout());
		start_button_panel.setSize(end_sel_panel.getSize());
		disable_rotation = new JCheckBox("Disable autorotation");
		start_button_panel.add(disable_rotation,BorderLayout.WEST);
		start_button = new JButton("Go");
		start_button_panel.add(start_button,BorderLayout.EAST);
		panel.add(start_button_panel);
		
		panel.add(Box.createRigidArea(new Dimension(0,10)));
		
		JPanel text_output_panel = new JPanel(new BorderLayout());
		output_text = new JTextArea();
		output_text.setEditable(false);
		output_pane = new JScrollPane(output_text, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		output_pane.setPreferredSize(new Dimension(350,230));
		text_output_panel.add(output_pane);
		panel.add(text_output_panel);
		
		
		start_button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				view.canvas.requestFocus();
				start_button_click();
			}
		});
		
		disable_rotation.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (!disable_rotation.isSelected()) {
					CampusViewStateUpdater.DEMO_ROTATE_CAMERA = 0.001f;
				} else {
					CampusViewStateUpdater.DEMO_ROTATE_CAMERA = 0.0f;
				}
			}
		});
		
		start_button_enable(false);
	}
	
	protected void start_button_click() {
		String start = start_select.getSelectedItem().toString();
		String end = end_select.getSelectedItem().toString();
		
		if (start.equals(DEFAULT_SELECTOR_TEXT) && !end.equals(DEFAULT_SELECTOR_TEXT)) {
			start = end;
		} else if (!start.equals(DEFAULT_SELECTOR_TEXT) && end.equals(DEFAULT_SELECTOR_TEXT)) {
			end = start;
		} else if (start.equals(DEFAULT_SELECTOR_TEXT) && end.equals(DEFAULT_SELECTOR_TEXT)) {
			return;
		}
		
		if (start.equals(end)) {
			output_message(String.format("\nGoing to %s...", start));
		} else {
			output_message(String.format("\nWalking from %s to %s...", start,end));
		}
		
		
		start = long_to_short.get(start);
		end = long_to_short.get(end);
		
		List<Double> coords = model.get_shortest_path(start,end);
		view.clear_markers();
		
		for(int i = 0; i < coords.size(); i+=3) {
			view.add_marker(coords.get(i).floatValue(), coords.get(i+1).floatValue());
		}
		view.start_follow();
	}
	
	public void output_message(String msg) {
		output_text.append(msg+"\n");
		output_text.setCaretPosition(output_text.getDocument().getLength());
	}
	
	public void start_button_enable(boolean status) {
		start_button.setEnabled(status);
	}
	
	private static JComboBox make_combobox_selector(CampusModel model) {
		Set<String> shorts = model.get_all_short();
		String[] opts = new String[shorts.size()];
		
		int ct = 0;
		for (String s : shorts) {
			opts[ct] = model.get_short_long(s);
			//opts[ct] = s;
			ct++;
		}
		Arrays.sort(opts);
		String[] defaults = {DEFAULT_SELECTOR_TEXT};
		String[] add_opts = new String[shorts.size()+1];
		System.arraycopy(defaults, 0, add_opts, 0, 1);
		System.arraycopy(opts, 0, add_opts, 1, shorts.size());
		JComboBox selector = new JComboBox(add_opts);
		return selector;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			view.rotate_camera(-0.05f);
			disable_rotation.setSelected(true);
			CampusViewStateUpdater.DEMO_ROTATE_CAMERA = 0.0f;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			view.rotate_camera(0.05f);
			disable_rotation.setSelected(true);
			CampusViewStateUpdater.DEMO_ROTATE_CAMERA = 0.0f;
		} else if (e.getKeyCode() == KeyEvent.VK_UP) {
			view.move(0.1f);
			disable_rotation.setSelected(true);
			CampusViewStateUpdater.DEMO_ROTATE_CAMERA = 0.0f;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			view.move(-0.1f);
			disable_rotation.setSelected(true);
			CampusViewStateUpdater.DEMO_ROTATE_CAMERA = 0.0f;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {};

	@Override
	public void keyTyped(KeyEvent arg0) {};

	@Override
	public void get_message(String msg) {
		output_message(msg);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		float clickx = arg0.getX();
		float clicky = arg0.getY();
		view.click_move(clickx,clicky);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {};

	@Override
	public void mouseExited(MouseEvent arg0) {};

	@Override
	public void mousePressed(MouseEvent arg0) {};

	@Override
	public void mouseReleased(MouseEvent arg0) {};

}
/*
 * Copyright (c) 2001-2006 Caucho Technology, Inc.  All rights reserved.
 *
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Caucho Technology (http://www.caucho.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Hessian", "Resin", and "Caucho" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@caucho.com.
 *
 * 5. Products derived from this software may not be called "Resin"
 *    nor may "Resin" appear in their names without prior written
 *    permission of Caucho Technology.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL CAUCHO TECHNOLOGY OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Scott Ferguson 
 * 
 */

package com.caucho.hessian.micro;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import com.vodafone360.people.utils.LogUtils;
import com.vodafone360.people.utils.ResetableBufferedInputStream;

/**
 * Input stream for Hessian requests, compatible with microedition Java. It only
 * uses classes and types available to J2ME. In particular, it does not have any
 * support for the &lt;double> type.
 * <p>
 * MicroHessianInput does not depend on any classes other than in J2ME, so it
 * can be extracted independently into a smaller package.
 * <p>
 * MicroHessianInput is unbuffered, so any client needs to provide its own
 * buffering.
 * 
 * <pre>
 * InputStream is = ...; // from http connection
 * MicroHessianInput in = new MicroHessianInput(is);
 * String value;
 * in.startReply();         // read reply header
 * value = in.readString(); // read string value
 * in.completeReply();      // read reply footer
 * </pre>
 */
public class MicroHessianInput {
    protected DataInputStream is;
    /** 
     * Using a special BufferedInputstream, which is constructed once and then reused.
     * Saves a lot of GC time.
     */
    protected ResetableBufferedInputStream rbis;
    
    /** Using fast but not thread safe StringBuilder for constructing strings */
    private StringBuilder mStringBuilder = new StringBuilder(255);
    
    /**
     * Creates a new Hessian input stream, initialized with an underlying input
     * stream.
     * 
     * @param is the underlying input stream.
     */
    public MicroHessianInput(InputStream is) {
        init(is);
    }

    /**
     * Creates an uninitialized Hessian input stream.
     */
    public MicroHessianInput() {
    }

    /**
     * Initialize the hessian stream with the underlying input stream.
     */
    public void init(InputStream is) {
        //use the reusable resetablebufferedinputstream here
        if (rbis==null){
            // create it only once
            rbis=new ResetableBufferedInputStream(is,2048);
        }else{
            // then reuse it
            rbis.reset(is);
        }
        
        this.is = new DataInputStream(rbis);

    }

    /**
     * Starts reading the reply
     * <p>
     * A successful completion will have a single value:
     * 
     * <pre>
     * r x01 x00
     * </pre>
     */
    public void startReply() throws IOException {
        int tag = is.read();

        if (tag != 'r')
            throw protocolException("expected hessian reply");

        // remove some bits from the input stream
        is.skip(2);
    }

    /**
     * Completes reading the call
     * <p>
     * A successful completion will have a single value:
     * 
     * <pre>
     * z
     * </pre>
     */
    public void completeReply() throws IOException {
        int tag = is.read();

        if (tag != 'z')
            throw protocolException("expected end of reply");
    }

    /**
     * Reads a boolean
     * 
     * <pre>
     * T
     * F
     * </pre>
     */
    public boolean readBoolean() throws IOException {
        int tag = is.read();

        switch (tag) {
            case 'T':
                return true;
            case 'F':
                return false;
            default:
                throw expect("boolean", tag);
        }
    }

    /**
     * Reads an integer
     * 
     * <pre>
     * I b32 b24 b16 b8
     * </pre>
     */
    public int readInt() throws IOException {
        int tag = is.read();
        return readInt(tag);
    }

    public int readInt(int tag) throws IOException {
        if (tag != 'I')
            throw expect("integer", tag);

        return is.readInt();
    }

    /**
     * Reads a long
     * 
     * <pre>
     * L b64 b56 b48 b40 b32 b24 b16 b8
     * </pre>
     */
    public long readLong() throws IOException {
        int tag = is.read();
        return readLong(tag);
    }

    private long readLong(int tag) throws IOException {
        if (tag != 'L')
            throw protocolException("expected long");

        return is.readLong();
    }

    /**
     * Reads a date.
     * 
     * <pre>
     * T b64 b56 b48 b40 b32 b24 b16 b8
     * </pre>
     */
    public long readUTCDate() throws IOException {
        int tag = is.read();

        if (tag != 'd')
            throw protocolException("expected date");

        return is.readLong();
    }

    /**
     * Reads a byte array
     * 
     * @return byte[] array extracted from Hessian stream, NULL if 'N' specified
     *         in data.
     * @throws IOException.
     */
    public byte[] readBytes() throws IOException {
        int tag = is.read();
        return readBytes(tag);
    }

    private byte[] readBytes(int tag) throws IOException {
        if (tag == 'N')
            return null;

        if (tag != 'B')
            throw expect("bytes", tag);

        int b16 = is.read();
        int b8 = is.read();

        int len = (b16 << 8) + b8;

        byte[] bytes = new byte[len];
        is.read(bytes);
        return bytes;
    }

    /**
     * Reads an arbitrary object the input stream.
     */
    public Object readObject(Class<?> expectedClass) throws IOException {
        int tag = is.read();

        switch (tag) {
            case 'N':
                return null;

            case 'T':
                return true;

            case 'F':
                return false;

            case 'I': {
                return readInt(tag);
            }

            case 'L': {
                return readLong(tag);
            }

            case 'd': {
                return new Date(is.readLong());
            }

            case 'S':
            case 'X': {
                int b16 = is.read();
                int b8 = is.read();

                int len = (b16 << 8) + b8;

                return readStringImpl(len);
            }

            case 'B': {
                return readBytes(tag);
            }
            default:
                throw new IOException("unknown code:" + (char)tag);
        }
    }

    public Vector<Object> readVector() throws IOException {
        int tag = is.read();
        return readVector(tag);
    }

    private Vector<Object> readVector(int tag) throws IOException {
        if (tag == 'N')
            return null;

        if (tag != 'V')
            throw expect("vector", tag);

        Vector<Object> v = new Vector<Object>();
        Object o = decodeTag();

        if (o instanceof End)
            return v;

        if (o instanceof Type)
            o = decodeTag();

        if (o instanceof End)
            return v;

        int len = 0;
        if (o instanceof Integer) {
            len = ((Integer)o);
            o = decodeTag();
        }

        for (int i = 0; i < len; i++) {
            v.addElement(o);
            o = decodeTag();
        }
        return v;
    }

    public Fault readFault() throws IOException {
        decodeTag();
        int tag = is.read();
        if (tag == 'S') {
            return new Fault(readString(tag));
        }
        return null;
    }

    public Object decodeTag() throws IOException {
        int tag = is.read();
        // HessianUtils.printTagValue(tag);
        return decodeType(tag);
    }

    public Object decodeType(int tag) throws IOException {
        // LogUtils.logD("HessianDecoder.decodeType() tag["+tag+"]");
        switch (tag) {
            case 't': // tag
                is.skip(2);
                Type type = new Type();
                return type;
            case 'l': // length
                int i = 0;

                i += (is.read() << 24);
                i += (is.read() << 16);
                i += (is.read() << 8);
                i += is.read();

                Integer len = i;
                return len;
            case 'z': // end
                End end = new End();
                return end;
            case 'N': // null
                return null;
            case 'r':
                // reply startReply should have retrieved this?
                return null;
            case 'M':
                return readHashMap(tag);
            case 'V': // array/Vector
                return readVector(tag);
            case 'T': // boolean true
                return true;
            case 'F': // boolean false
                return false;
            case 'I': // integer
                return readInt(tag);
            case 'L': // read long
                return readLong(tag);
            case 'd': // UTC date
                return null;
            case 'S': // String
                return readString(tag);
            case 'B': // read byte array
                return readBytes(tag);
            case 'f':
                return readFault();
            default:
                LogUtils.logE("HessianDecoder.decodeType() Unknown type");
                return null;
        }
    }

    /**
     * Reads a string
     * 
     * <pre>
     * S b16 b8 string value
     * </pre>
     */
    public String readString() throws IOException {
        int tag = is.read();
        return readString(tag);
    }

    private String readString(int tag) throws IOException {
        if (tag == 'N')
            return null;

        if (tag != 'S')
            throw expect("string", tag);

        int b16 = is.read();
        int b8 = is.read();

        int len = (b16 << 8) + b8;

        return readStringImpl(len);
    }

    /**
     * Reads a string from the underlying stream.
     */
    private String readStringImpl(int length) throws IOException {

        // reset the StringBuilder. Recycling is better than making always a
        // new one.
        mStringBuilder.setLength(0);

        for (int i = 0; i < length; i++) {
            int ch = is.read();

            if (ch < 0x80)
                mStringBuilder.append((char)ch);

            else if ((ch & 0xe0) == 0xc0) {
                int ch1 = is.read();
                int v = ((ch & 0x1f) << 6) + (ch1 & 0x3f);
                mStringBuilder.append((char)v);
            } else if ((ch & 0xf0) == 0xe0) {
                int ch1 = is.read();
                int ch2 = is.read();
                int v = ((ch & 0x0f) << 12) + ((ch1 & 0x3f) << 6) + (ch2 & 0x3f);
                mStringBuilder.append((char)v);
            } else if ((ch & 0xff) >= 0xf0 && (ch & 0xff) <= 0xf4) { // UTF-4
                final byte[] b = new byte[4];
                b[0] = (byte)ch;
                b[1] = (byte)is.read();
                b[2] = (byte)is.read();
                b[3] = (byte)is.read();
                mStringBuilder.append(new String(b, "utf-8"));
                i++;
            } else
                throw new IOException("bad utf-8 encoding");
        }

        return mStringBuilder.substring(0, mStringBuilder.length());
    }

    public Hashtable<String, Object> readHashMap() throws IOException {
        // read map type
        int tag = is.read();
        return readHashMap(tag);
    }

    public Hashtable<String, Object> readHashMap(int tag) throws IOException {
        // read map type

        if (tag == 'N')
            return null;

        if (tag != 'M')
            throw expect("map", tag);

        Hashtable<String, Object> ht = new Hashtable<String, Object>();
        Object obj = decodeTag();
        if (obj instanceof Type) {
            // get following object
            obj = decodeTag();
        }

        Object obj1 = null;

        while (obj != null && !(obj instanceof End)) // 'z' = list-end
        {
            obj1 = decodeTag();
            ht.put(obj.toString(), obj1);
            obj = decodeTag();
        }
        return ht;
    }

    protected IOException expect(String expect, int ch) {
        if (ch < 0)
            return protocolException("expected " + expect + " at end of file");
        else
            return protocolException("expected " + expect + " at " + (char)ch);
    }

    protected IOException protocolException(String message) {
        return new IOException(message);
    }

    /**
     * Place-holder class for End tag 'z'
     */
    private static class End {
    }

    /**
     * Place-holder class for Type tag 't'
     */
    private static class Type {
    }

    /**
     * Class holding error string returned during Hessian decoding
     */
    public static class Fault {
        private String mErrString = null;

        private Fault(String eString) {
            mErrString = eString;
        }

        public String errString() {
            return mErrString;
        }
    }
}
/*
 * Copyright (c) 2001-2006 Caucho Technology, Inc.  All rights reserved.
 *
 * The Apache Software License, Version 1.1
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Caucho Technology (http://www.caucho.com/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Hessian", "Resin", and "Caucho" must not be used to
 *    endorse or promote products derived from this software without prior
 *    written permission. For written permission, please contact
 *    info@caucho.com.
 *
 * 5. Products derived from this software may not be called "Resin"
 *    nor may "Resin" appear in their names without prior written
 *    permission of Caucho Technology.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL CAUCHO TECHNOLOGY OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Scott Ferguson
 */

package com.caucho.hessian.micro;

import java.io.*;
import java.util.*;

/**
 * Output stream for Hessian requests, compatible with microedition Java. It
 * only uses classes and types available to J2ME. In particular, it does not
 * have any support for the &lt;double> type.
 * <p>
 * MicroHessianOutput does not depend on any classes other than in J2ME, so it
 * can be extracted independently into a smaller package.
 * <p>
 * MicroHessianOutput is unbuffered, so any client needs to provide its own
 * buffering.
 * 
 * <pre>
 * OutputStream os = ...; // from http connection
 * MicroHessianOutput out = new MicroHessianOutput(os);
 * String value;
 * out.startCall(&quot;hello&quot;);  // start hello call
 * out.writeString(&quot;arg1&quot;); // write a string argument
 * out.completeCall();      // complete the call
 * </pre>
 */
public class MicroHessianOutput {
    protected OutputStream os;
    
    /** Max Chunk size = 64K. */
    public final static int MAX_CHUNK = 65535;
    
    /** The mask to be used to get the lower byte in an int */
    private final static int LOW_BYTE_MASK = 0xff;

    /**
     * Creates a new Hessian output stream, initialized with an underlying
     * output stream.
     * 
     * @param os the underlying output stream.
     */
    public MicroHessianOutput(OutputStream os) {
        init(os);
    }

    /**
     * Creates an uninitialized Hessian output stream.
     */
    public MicroHessianOutput() {
    }

    public void init(OutputStream os) {
        this.os = os;
    }

    /**
     * Writes the method call: <code><pre>
     * c major minor
     * m b16 b8 method-namek
     * </pre></code>
     * 
     * @param method the method name to call.
     */
    public void startCall(String method) throws IOException {
        os.write('c');
        os.write(0);
        os.write(1);

        os.write('m');
        int len = method.length();
        os.write(len >> 8);
        os.write(len);
        printString(method, 0, len);
    }

    /**
     * Writes the method call: <code><pre>
     * z
     * </pre></code>
     */
    public void completeCall() throws IOException {
        os.write('z');
    }

    /**
     * Writes a boolean value to the stream. The boolean will be written with
     * the following syntax: <code><pre>
     * T
     * F
     * </pre></code>
     * 
     * @param value the boolean value to write.
     */
    public void writeBoolean(boolean value) throws IOException {
        if (value)
            os.write('T');
        else
            os.write('F');
    }

    /**
     * Writes an integer value to the stream. The integer will be written with
     * the following syntax: <code><pre>
     * I b32 b24 b16 b8
     * </pre></code>
     * 
     * @param value the integer value to write.
     */
    public void writeInt(int value) throws IOException {
        os.write('I');
        os.write(value >> 24);
        os.write(value >> 16);
        os.write(value >> 8);
        os.write(value);
    }

    /**
     * Writes a long value to the stream. The long will be written with the
     * following syntax: <code><pre>
     * L b64 b56 b48 b40 b32 b24 b16 b8
     * </pre></code>
     * 
     * @param value the long value to write.
     */
    public void writeLong(long value) throws IOException {
        os.write('L');
        os.write((byte)(value >> 56));
        os.write((byte)(value >> 48));
        os.write((byte)(value >> 40));
        os.write((byte)(value >> 32));
        os.write((byte)(value >> 24));
        os.write((byte)(value >> 16));
        os.write((byte)(value >> 8));
        os.write((byte)(value));
    }

    /**
     * Writes a date to the stream. <code><pre>
     * T  b64 b56 b48 b40 b32 b24 b16 b8
     * </pre></code>
     * 
     * @param time the date in milliseconds from the epoch in UTC
     */
    public void writeUTCDate(long time) throws IOException {
        os.write('d');
        os.write((byte)(time >> 56));
        os.write((byte)(time >> 48));
        os.write((byte)(time >> 40));
        os.write((byte)(time >> 32));
        os.write((byte)(time >> 24));
        os.write((byte)(time >> 16));
        os.write((byte)(time >> 8));
        os.write((byte)(time));
    }

    /**
     * Writes a null value to the stream. The null will be written with the
     * following syntax <code><pre>
     * N
     * </pre></code>
     * 
     * @param value the string value to write.
     */
    public void writeNull() throws IOException {
        os.write('N');
    }

    /**
     * Writes a string value to the stream using UTF-8 encoding. The string will
     * be written with the following syntax: <code><pre>
     * s b16 b8 string-value S b16 b8 string-value
     * </pre></code> If the value is null, it will be written as <code><pre>
     * N
     * </pre></code>
     *
     * @param value the string value to write.
     * @throws  IOException     if the stream fails.
     */
    public void writeString(final String value) throws IOException {
        if (value == null) {
            os.write('N');
            return;
        }
        final int length = value.length();
        final int numChunks = length / MAX_CHUNK;
        int i = 0;
        int offset = 0;
        for ( ; i < numChunks; i++) {
            printStringLen(MAX_CHUNK, false);
            printString(value, offset, MAX_CHUNK);
            offset += MAX_CHUNK;
        }
        printStringLen(length % MAX_CHUNK, true);
        printString(value, offset, (length % MAX_CHUNK));
    }
    
    /**
     * Prints the string length plus the 'S'/'s' prefix.
     *
     * @param   length      the length of the string.
     * @param   isFinalChunk  true for 'S', false for 's'.
     * @throws  IOException   if the stream fails.
     */
    protected void printStringLen(final int length, final boolean isFinalChunk) throws IOException {
        os.write(isFinalChunk ? 'S' : 's');
        os.write(length >> 8 & LOW_BYTE_MASK);
        os.write(length & LOW_BYTE_MASK);
    }

    /**
     * Writes a byte array to the stream. The array will be written with the
     * following syntax: <code><pre>
     * B b16 b18 bytes
     * </pre></code> If the value is null, it will be written as <code><pre>
     * N
     * </pre></code>
     * 
     * @param value the string value to write.
     */
    public void writeBytes(byte[] buffer) throws IOException {
        if (buffer == null)
            os.write('N');
        else
            writeBytes(buffer, 0, buffer.length);
    }

    /**
     * Writes a byte array to the stream. The array will be written with the
     * following syntax: <code><pre>
     * b b16 b8 bytes B b16 b8 bytes
     * </pre></code> If the value is null, it will be written as <code><pre>
     * N
     * </pre></code>
     *
     * @param buffer the string value to write.
     * @param   offset      the first byte of the byte array to write.
     * @param   length      the number of bytes of the byte array to write.
     * @throws  IOException if the stream fails.
     */
    public void writeBytes(final byte[] buffer, final int offset, final int length) throws IOException {
        if (buffer == null) {
            os.write('N');
            return;
        }
        
        int i = 0;
        final int numChunks = length / MAX_CHUNK;
        int chunkOffset = 0;
        for ( ; i < numChunks; i++) {
            printBytesLen(MAX_CHUNK, false);
            os.write(buffer, offset + chunkOffset, MAX_CHUNK);
            chunkOffset += MAX_CHUNK;
        }
        printBytesLen(length % MAX_CHUNK, true);
        os.write(buffer, offset + chunkOffset, (length % MAX_CHUNK));

    }
    
    /**
     * Prints the byte array length plus the 'B'/'b' prefix.
     *
     * @param   length      the length of the byte array.
     * @param   isFinalChunk           true for 'B', false for 'b'.
     * @throws  IOException     if the stream fails.
     */
    protected void printBytesLen(final int length, final boolean isFinalChunk) throws IOException {
        this.os.write(isFinalChunk ? 'B' : 'b');
        this.os.write(length >> 8 & LOW_BYTE_MASK);
        this.os.write(length & LOW_BYTE_MASK);
    }

    /**
     * Writes a reference. <code><pre>
     * R b32 b24 b16 b8
     * </pre></code>
     * 
     * @param value the integer value to write.
     */
    public void writeRef(int value) throws IOException {
        os.write('R');
        os.write(value >> 24);
        os.write(value >> 16);
        os.write(value >> 8);
        os.write(value);
    }

    /**
     * Writes a generic object to the output stream.
     */
    public void writeObject(Object object) throws IOException {
        if (object == null)
            writeNull();
        else if (object instanceof String)
            writeString((String)object);
        else if (object instanceof Boolean)
            writeBoolean(((Boolean)object).booleanValue());
        else if (object instanceof Integer)
            writeInt(((Number)object).intValue());
        else if (object instanceof Long)
            writeLong(((Number)object).longValue());
        else if (object instanceof Date)
            writeUTCDate(((Date)object).getTime());
        else if (object instanceof byte[]) {
            byte[] data = (byte[])object;
            writeBytes(data, 0, data.length);
        } else if (object instanceof Vector<?>) {
            Vector<?> vector = (Vector<?>)object;

            int size = vector.size();
            writeListBegin(size, null);
            for (int i = 0; i < size; i++)
                writeObject(vector.get(i));

            writeListEnd();
        } else if (object instanceof Hashtable<?,?>) {
            @SuppressWarnings("unchecked")
            Hashtable<Object, Object> hashtable = (Hashtable<Object, Object>)object;

            writeMapBegin(null);
            Enumeration<Object> e = hashtable.keys();
            while (e.hasMoreElements()) {
                Object key = e.nextElement();
                Object value = hashtable.get(key);

                writeObject(key);
                writeObject(value);
            }
            writeMapEnd();
        } else
            writeCustomObject(object);
    }

    /**
     * Applications which override this can do custom serialization.
     * 
     * @param object the object to write.
     */
    public void writeCustomObject(Object object) throws IOException {
        throw new IOException("unexpected object: " + object);
    }

    /**
     * Writes the list header to the stream. List writers will call
     * <code>writeListBegin</code> followed by the list contents and then call
     * <code>writeListEnd</code>. <code><pre>
     * &lt;list>
     *   &lt;type>java.util.ArrayList&lt;/type>
     *   &lt;length>3&lt;/length>
     *   &lt;int>1&lt;/int>
     *   &lt;int>2&lt;/int>
     *   &lt;int>3&lt;/int>
     * &lt;/list>
     * </pre></code>
     */
    public void writeListBegin(int length, String type) throws IOException {
        os.write('V');
        os.write('t');
        printLenString(type);

        os.write('l');
        os.write(length >> 24);
        os.write(length >> 16);
        os.write(length >> 8);
        os.write(length);
    }

    /**
     * Writes the tail of the list to the stream.
     */
    public void writeListEnd() throws IOException {
        os.write('z');
    }

    /**
     * Writes the map header to the stream. Map writers will call
     * <code>writeMapBegin</code> followed by the map contents and then call
     * <code>writeMapEnd</code>. <code><pre>
     * Mt b16 b8 type (<key> <value>)z
     * </pre></code>
     */
    public void writeMapBegin(String type) throws IOException {
        os.write('M');
        os.write('t');
        printLenString(type);
    }

    /**
     * Writes the tail of the map to the stream.
     */
    public void writeMapEnd() throws IOException {
        os.write('z');
    }

    /**
     * Writes a remote object reference to the stream. The type is the type of
     * the remote interface. <code><pre>
     * 'r' 't' b16 b8 type url
     * </pre></code>
     */
    public void writeRemote(String type, String url) throws IOException {
        os.write('r');
        os.write('t');
        printLenString(type);
        os.write('S');
        printLenString(url);
    }

    /**
     * Prints a string to the stream, encoded as UTF-8 with preceeding length
     * 
     * @param v the string to print.
     */
    public void printLenString(String v) throws IOException {
        if (v == null) {
            os.write(0);
            os.write(0);
        } else {
            int len = v.length();
            os.write(len >> 8);
            os.write(len);

            printString(v, 0, len);
        }
    }

    /**
     * Prints a string to the stream, encoded as UTF-8
     * 
     * @param v the string to print.
     */
    public void printString(String v) throws IOException {
        printString(v, 0, v.length());
    }

    /**
     * Prints a string to the stream, encoded as UTF-8
     * 
     * @param v the string to print.
     */
    public void printString(String v, int offset, int length) throws IOException {
        for (int i = 0; i < length; i++) {
            char ch = v.charAt(i + offset);

            if (ch < 0x80)
                os.write(ch);
            else if (ch < 0x800) {
                os.write(0xc0 + ((ch >> 6) & 0x1f));
                os.write(0x80 + (ch & 0x3f));
            } else if (ch >= 0xd800 && ch <= 0xdf00) {
                final char[] chars = new char[2];
                chars[0] = ch;
                chars[1] = v.charAt(i + offset + 1);
                os.write(String.valueOf(chars).getBytes("utf-8"));
                i++;
            } else {
                os.write(0xe0 + ((ch >> 12) & 0xf));
                os.write(0x80 + ((ch >> 6) & 0x3f));
                os.write(0x80 + (ch & 0x3f));
            }
        }
    }
}
package com.actionbarsherlock;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.internal.ActionBarSherlockCompat;
import com.actionbarsherlock.internal.ActionBarSherlockNative;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

/**
 * <p>Helper for implementing the action bar design pattern across all versions
 * of Android.</p>
 *
 * <p>This class will manage interaction with a custom action bar based on the
 * Android 4.0 source code. The exposed API mirrors that of its native
 * counterpart and you should refer to its documentation for instruction.</p>
 *
 * @author Jake Wharton <jakewharton@gmail.com>
 * @version 4.0.0
 */
public abstract class ActionBarSherlock {
    protected static final String TAG = "ActionBarSherlock";
    protected static final boolean DEBUG = false;

    private static final Class<?>[] CONSTRUCTOR_ARGS = new Class[] { Activity.class, int.class };
    private static final HashMap<Implementation, Class<? extends ActionBarSherlock>> IMPLEMENTATIONS =
            new HashMap<Implementation, Class<? extends ActionBarSherlock>>();

    static {
        //Register our two built-in implementations
        registerImplementation(ActionBarSherlockCompat.class);
        registerImplementation(ActionBarSherlockNative.class);
    }


    /**
     * <p>Denotes an implementation of ActionBarSherlock which provides an
     * action bar-enhanced experience.</p>
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Implementation {
        static final int DEFAULT_API = -1;
        static final int DEFAULT_DPI = -1;

        int api() default DEFAULT_API;
        int dpi() default DEFAULT_DPI;
    }


    /** Activity interface for menu creation callback. */
    public interface OnCreatePanelMenuListener {
        public boolean onCreatePanelMenu(int featureId, Menu menu);
    }
    /** Activity interface for menu creation callback. */
    public interface OnCreateOptionsMenuListener {
        public boolean onCreateOptionsMenu(Menu menu);
    }
    /** Activity interface for menu item selection callback. */
    public interface OnMenuItemSelectedListener {
        public boolean onMenuItemSelected(int featureId, MenuItem item);
    }
    /** Activity interface for menu item selection callback. */
    public interface OnOptionsItemSelectedListener {
        public boolean onOptionsItemSelected(MenuItem item);
    }
    /** Activity interface for menu preparation callback. */
    public interface OnPreparePanelListener {
        public boolean onPreparePanel(int featureId, View view, Menu menu);
    }
    /** Activity interface for menu preparation callback. */
    public interface OnPrepareOptionsMenuListener {
        public boolean onPrepareOptionsMenu(Menu menu);
    }
    /** Activity interface for action mode finished callback. */
    public interface OnActionModeFinishedListener {
        public void onActionModeFinished(ActionMode mode);
    }
    /** Activity interface for action mode started callback. */
    public interface OnActionModeStartedListener {
        public void onActionModeStarted(ActionMode mode);
    }


    /**
     * If set, the logic in these classes will assume that an {@link Activity}
     * is dispatching all of the required events to the class. This flag should
     * only be used internally or if you are creating your own base activity
     * modeled after one of the included types (e.g., {@code SherlockActivity}).
     */
    public static final int FLAG_DELEGATE = 1;


    /**
     * Register an ActionBarSherlock implementation.
     *
     * @param implementationClass Target implementation class which extends
     * {@link ActionBarSherlock}. This class must also be annotated with
     * {@link Implementation}.
     */
    public static void registerImplementation(Class<? extends ActionBarSherlock> implementationClass) {
        if (!implementationClass.isAnnotationPresent(Implementation.class)) {
            throw new IllegalArgumentException("Class " + implementationClass.getSimpleName() + " is not annotated with @Implementation");
        } else if (IMPLEMENTATIONS.containsValue(implementationClass)) {
            if (DEBUG) Log.w(TAG, "Class " + implementationClass.getSimpleName() + " already registered");
            return;
        }

        Implementation impl = implementationClass.getAnnotation(Implementation.class);
        if (DEBUG) Log.i(TAG, "Registering " + implementationClass.getSimpleName() + " with qualifier " + impl);
        IMPLEMENTATIONS.put(impl, implementationClass);
    }

    /**
     * Unregister an ActionBarSherlock implementation. <strong>This should be
     * considered very volatile and you should only use it if you know what
     * you are doing.</strong> You have been warned.
     *
     * @param implementationClass Target implementation class.
     * @return Boolean indicating whether the class was removed.
     */
    public static boolean unregisterImplementation(Class<? extends ActionBarSherlock> implementationClass) {
        return IMPLEMENTATIONS.values().remove(implementationClass);
    }

    /**
     * Wrap an activity with an action bar abstraction which will enable the
     * use of a custom implementation on platforms where a native version does
     * not exist.
     *
     * @param activity Activity to wrap.
     * @return Instance to interact with the action bar.
     */
    public static ActionBarSherlock wrap(Activity activity) {
        return wrap(activity, 0);
    }

    /**
     * Wrap an activity with an action bar abstraction which will enable the
     * use of a custom implementation on platforms where a native version does
     * not exist.
     *
     * @param activity Owning activity.
     * @param flags Option flags to control behavior.
     * @return Instance to interact with the action bar.
     */
    public static ActionBarSherlock wrap(Activity activity, int flags) {
        //Create a local implementation map we can modify
        HashMap<Implementation, Class<? extends ActionBarSherlock>> impls =
                new HashMap<Implementation, Class<? extends ActionBarSherlock>>(IMPLEMENTATIONS);
        boolean hasQualfier;

        /* DPI FILTERING */
        hasQualfier = false;
        for (Implementation key : impls.keySet()) {
            //Only honor TVDPI as a specific qualifier
            if (key.dpi() == DisplayMetrics.DENSITY_TV) {
                hasQualfier = true;
                break;
            }
        }
        if (hasQualfier) {
            final boolean isTvDpi = activity.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_TV;
            for (Iterator<Implementation> keys = impls.keySet().iterator(); keys.hasNext(); ) {
                int keyDpi = keys.next().dpi();
                if ((isTvDpi && keyDpi != DisplayMetrics.DENSITY_TV)
                        || (!isTvDpi && keyDpi == DisplayMetrics.DENSITY_TV)) {
                    keys.remove();
                }
            }
        }

        /* API FILTERING */
        hasQualfier = false;
        for (Implementation key : impls.keySet()) {
            if (key.api() != Implementation.DEFAULT_API) {
                hasQualfier = true;
                break;
            }
        }
        if (hasQualfier) {
            final int runtimeApi = Build.VERSION.SDK_INT;
            int bestApi = 0;
            for (Iterator<Implementation> keys = impls.keySet().iterator(); keys.hasNext(); ) {
                int keyApi = keys.next().api();
                if (keyApi > runtimeApi) {
                    keys.remove();
                } else if (keyApi > bestApi) {
                    bestApi = keyApi;
                }
            }
            for (Iterator<Implementation> keys = impls.keySet().iterator(); keys.hasNext(); ) {
                if (keys.next().api() != bestApi) {
                    keys.remove();
                }
            }
        }

        if (impls.size() > 1) {
            throw new IllegalStateException("More than one implementation matches configuration.");
        }
        if (impls.isEmpty()) {
            throw new IllegalStateException("No implementations match configuration.");
        }
        Class<? extends ActionBarSherlock> impl = impls.values().iterator().next();
        if (DEBUG) Log.i(TAG, "Using implementation: " + impl.getSimpleName());

        try {
            Constructor<? extends ActionBarSherlock> ctor = impl.getConstructor(CONSTRUCTOR_ARGS);
            return ctor.newInstance(activity, flags);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    /** Activity which is displaying the action bar. Also used for context. */
    protected final Activity mActivity;
    /** Whether delegating actions for the activity or managing ourselves. */
    protected final boolean mIsDelegate;

    /** Reference to our custom menu inflater which supports action items. */
    protected MenuInflater mMenuInflater;



    protected ActionBarSherlock(Activity activity, int flags) {
        if (DEBUG) Log.d(TAG, "[<ctor>] activity: " + activity + ", flags: " + flags);

        mActivity = activity;
        mIsDelegate = (flags & FLAG_DELEGATE) != 0;
    }


    /**
     * Get the current action bar instance.
     *
     * @return Action bar instance.
     */
    public abstract ActionBar getActionBar();


    ///////////////////////////////////////////////////////////////////////////
    // Lifecycle and interaction callbacks when delegating
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Notify action bar of a configuration change event. Should be dispatched
     * after the call to the superclass implementation.
     *
     * <blockquote><pre>
     * @Override
     * public void onConfigurationChanged(Configuration newConfig) {
     *     super.onConfigurationChanged(newConfig);
     *     mSherlock.dispatchConfigurationChanged(newConfig);
     * }
     * </pre></blockquote>
     *
     * @param newConfig The new device configuration.
     */
    public void dispatchConfigurationChanged(Configuration newConfig) {}

    /**
     * Notify the action bar that the activity has finished its resuming. This
     * should be dispatched after the call to the superclass implementation.
     *
     * <blockquote><pre>
     * @Override
     * protected void onPostResume() {
     *     super.onPostResume();
     *     mSherlock.dispatchPostResume();
     * }
     * </pre></blockquote>
     */
    public void dispatchPostResume() {}

    /**
     * Notify the action bar that the activity is pausing. This should be
     * dispatched before the call to the superclass implementation.
     *
     * <blockquote><pre>
     * @Override
     * protected void onPause() {
     *     mSherlock.dispatchPause();
     *     super.onPause();
     * }
     * </pre></blockquote>
     */
    public void dispatchPause() {}

    /**
     * Notify the action bar that the activity is stopping. This should be
     * called before the superclass implementation.
     *
     * <blockquote><p>
     * @Override
     * protected void onStop() {
     *     mSherlock.dispatchStop();
     *     super.onStop();
     * }
     * </p></blockquote>
     */
    public void dispatchStop() {}

    /**
     * Indicate that the menu should be recreated by calling
     * {@link OnCreateOptionsMenuListener#onCreateOptionsMenu(com.actionbarsherlock.view.Menu)}.
     */
    public abstract void dispatchInvalidateOptionsMenu();

    /**
     * Notify the action bar that it should display its overflow menu if it is
     * appropriate for the device. The implementation should conditionally
     * call the superclass method only if this method returns {@code false}.
     *
     * <blockquote><p>
     * @Override
     * public void openOptionsMenu() {
     *     if (!mSherlock.dispatchOpenOptionsMenu()) {
     *         super.openOptionsMenu();
     *     }
     * }
     * </p></blockquote>
     *
     * @return {@code true} if the opening of the menu was handled internally.
     */
    public boolean dispatchOpenOptionsMenu() {
        return false;
    }

    /**
     * Notify the action bar that it should close its overflow menu if it is
     * appropriate for the device. This implementation should conditionally
     * call the superclass method only if this method returns {@code false}.
     *
     * <blockquote><pre>
     * @Override
     * public void closeOptionsMenu() {
     *     if (!mSherlock.dispatchCloseOptionsMenu()) {
     *         super.closeOptionsMenu();
     *     }
     * }
     * </pre></blockquote>
     *
     * @return {@code true} if the closing of the menu was handled internally.
     */
    public boolean dispatchCloseOptionsMenu() {
        return false;
    }

    /**
     * Notify the class that the activity has finished its creation. This
     * should be called after the superclass implementation.
     *
     * <blockquote><pre>
     * @Override
     * protected void onPostCreate(Bundle savedInstanceState) {
     *     mSherlock.dispatchPostCreate(savedInstanceState);
     *     super.onPostCreate(savedInstanceState);
     * }
     * </pre></blockquote>
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle
     *                           contains the data it most recently supplied in
     *                           {@link Activity#}onSaveInstanceState(Bundle)}.
     *                           <strong>Note: Otherwise it is null.</strong>
     */
    public void dispatchPostCreate(Bundle savedInstanceState) {}

    /**
     * Notify the action bar that the title has changed and the action bar
     * should be updated to reflect the change. This should be called before
     * the superclass implementation.
     *
     * <blockquote><pre>
     *  @Override
     *  protected void onTitleChanged(CharSequence title, int color) {
     *      mSherlock.dispatchTitleChanged(title, color);
     *      super.onTitleChanged(title, color);
     *  }
     * </pre></blockquote>
     *
     * @param title New activity title.
     * @param color New activity color.
     */
    public void dispatchTitleChanged(CharSequence title, int color) {}

    /**
     * Notify the action bar the user has created a key event. This is used to
     * toggle the display of the overflow action item with the menu key and to
     * close the action mode or expanded action item with the back key.
     *
     * <blockquote><pre>
     * @Override
     * public boolean dispatchKeyEvent(KeyEvent event) {
     *     if (mSherlock.dispatchKeyEvent(event)) {
     *         return true;
     *     }
     *     return super.dispatchKeyEvent(event);
     * }
     * </pre></blockquote>
     *
     * @param event Description of the key event.
     * @return {@code true} if the event was handled.
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        return false;
    }

    /**
     * Notify the action bar that the Activity has triggered a menu creation
     * which should happen on the conclusion of {@link Activity#onCreate}. This
     * will be used to gain a reference to the native menu for native and
     * overflow binding as well as to indicate when compatibility create should
     * occur for the first time.
     *
     * @param menu Activity native menu.
     * @return {@code true} since we always want to say that we have a native
     */
    public abstract boolean dispatchCreateOptionsMenu(android.view.Menu menu);

    /**
     * Notify the action bar that the Activity has triggered a menu preparation
     * which usually means that the user has requested the overflow menu via a
     * hardware menu key. You should return the result of this method call and
     * not call the superclass implementation.
     *
     * <blockquote><p>
     * @Override
     * public final boolean onPrepareOptionsMenu(android.view.Menu menu) {
     *     return mSherlock.dispatchPrepareOptionsMenu(menu);
     * }
     * </p></blockquote>
     *
     * @param menu Activity native menu.
     * @return {@code true} if menu display should proceed.
     */
    public abstract boolean dispatchPrepareOptionsMenu(android.view.Menu menu);

    /**
     * Notify the action bar that a native options menu item has been selected.
     * The implementation should return the result of this method call.
     *
     * <blockquote><p>
     * @Override
     * public final boolean onOptionsItemSelected(android.view.MenuItem item) {
     *     return mSherlock.dispatchOptionsItemSelected(item);
     * }
     * </p></blockquote>
     *
     * @param item Options menu item.
     * @return @{code true} if the selection was handled.
     */
    public abstract boolean dispatchOptionsItemSelected(android.view.MenuItem item);

    /**
     * Notify the action bar that the overflow menu has been opened. The
     * implementation should conditionally return {@code true} if this method
     * returns {@code true}, otherwise return the result of the superclass
     * method.
     *
     * <blockquote><p>
     * @Override
     * public final boolean onMenuOpened(int featureId, android.view.Menu menu) {
     *     if (mSherlock.dispatchMenuOpened(featureId, menu)) {
     *         return true;
     *     }
     *     return super.onMenuOpened(featureId, menu);
     * }
     * </p></blockquote>
     *
     * @param featureId Window feature which triggered the event.
     * @param menu Activity native menu.
     * @return {@code true} if the event was handled by this method.
     */
    public boolean dispatchMenuOpened(int featureId, android.view.Menu menu) {
        return false;
    }

    /**
     * Notify the action bar that the overflow menu has been closed. This
     * method should be called before the superclass implementation.
     *
     * <blockquote><p>
     * @Override
     * public void onPanelClosed(int featureId, android.view.Menu menu) {
     *     mSherlock.dispatchPanelClosed(featureId, menu);
     *     super.onPanelClosed(featureId, menu);
     * }
     * </p></blockquote>
     *
     * @param featureId
     * @param menu
     */
    public void dispatchPanelClosed(int featureId, android.view.Menu menu) {}


    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Internal method to trigger the menu creation process.
     *
     * @return {@code true} if menu creation should proceed.
     */
    protected final boolean callbackCreateOptionsMenu(Menu menu) {
        if (DEBUG) Log.d(TAG, "[callbackCreateOptionsMenu] menu: " + menu);

        boolean result = true;
        if (mActivity instanceof OnCreatePanelMenuListener) {
            OnCreatePanelMenuListener listener = (OnCreatePanelMenuListener)mActivity;
            result = listener.onCreatePanelMenu(Window.FEATURE_OPTIONS_PANEL, menu);
        } else if (mActivity instanceof OnCreateOptionsMenuListener) {
            OnCreateOptionsMenuListener listener = (OnCreateOptionsMenuListener)mActivity;
            result = listener.onCreateOptionsMenu(menu);
        }

        if (DEBUG) Log.d(TAG, "[callbackCreateOptionsMenu] returning " + result);
        return result;
    }

    /**
     * Internal method to trigger the menu preparation process.
     *
     * @return {@code true} if menu preparation should proceed.
     */
    protected final boolean callbackPrepareOptionsMenu(Menu menu) {
        if (DEBUG) Log.d(TAG, "[callbackPrepareOptionsMenu] menu: " + menu);

        boolean result = true;
        if (mActivity instanceof OnPreparePanelListener) {
            OnPreparePanelListener listener = (OnPreparePanelListener)mActivity;
            result = listener.onPreparePanel(Window.FEATURE_OPTIONS_PANEL, null, menu);
        } else if (mActivity instanceof OnPrepareOptionsMenuListener) {
            OnPrepareOptionsMenuListener listener = (OnPrepareOptionsMenuListener)mActivity;
            result = listener.onPrepareOptionsMenu(menu);
        }

        if (DEBUG) Log.d(TAG, "[callbackPrepareOptionsMenu] returning " + result);
        return result;
    }

    /**
     * Internal method for dispatching options menu selection to the owning
     * activity callback.
     *
     * @param item Selected options menu item.
     * @return {@code true} if the item selection was handled in the callback.
     */
    protected final boolean callbackOptionsItemSelected(MenuItem item) {
        if (DEBUG) Log.d(TAG, "[callbackOptionsItemSelected] item: " + item.getTitleCondensed());

        boolean result = false;
        if (mActivity instanceof OnMenuItemSelectedListener) {
            OnMenuItemSelectedListener listener = (OnMenuItemSelectedListener)mActivity;
            result = listener.onMenuItemSelected(Window.FEATURE_OPTIONS_PANEL, item);
        } else if (mActivity instanceof OnOptionsItemSelectedListener) {
            OnOptionsItemSelectedListener listener = (OnOptionsItemSelectedListener)mActivity;
            result = listener.onOptionsItemSelected(item);
        }

        if (DEBUG) Log.d(TAG, "[callbackOptionsItemSelected] returning " + result);
        return result;
    }


    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////


    /**
     * Query for the availability of a certain feature.
     *
     * @param featureId The feature ID to check.
     * @return {@code true} if feature is enabled, {@code false} otherwise.
     */
    public abstract boolean hasFeature(int featureId);

    /**
     * Enable extended screen features. This must be called before
     * {@code setContentView()}. May be called as many times as desired as long
     * as it is before {@code setContentView()}. If not called, no extended
     * features will be available. You can not turn off a feature once it is
     * requested.
     *
     * @param featureId The desired features, defined as constants by Window.
     * @return Returns true if the requested feature is supported and now
     * enabled.
     */
    public abstract boolean requestFeature(int featureId);

    /**
     * Set extra options that will influence the UI for this window.
     *
     * @param uiOptions Flags specifying extra options for this window.
     */
    public abstract void setUiOptions(int uiOptions);

    /**
     * Set extra options that will influence the UI for this window. Only the
     * bits filtered by mask will be modified.
     *
     * @param uiOptions Flags specifying extra options for this window.
     * @param mask Flags specifying which options should be modified. Others
     *             will remain unchanged.
     */
    public abstract void setUiOptions(int uiOptions, int mask);

    /**
     * Set the content of the activity inside the action bar.
     *
     * @param layoutResId Layout resource ID.
     */
    public abstract void setContentView(int layoutResId);

    /**
     * Set the content of the activity inside the action bar.
     *
     * @param view The desired content to display.
     */
    public void setContentView(View view) {
        if (DEBUG) Log.d(TAG, "[setContentView] view: " + view);

        setContentView(view, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }

    /**
     * Set the content of the activity inside the action bar.
     *
     * @param view The desired content to display.
     * @param params Layout parameters to apply to the view.
     */
    public abstract void setContentView(View view, ViewGroup.LayoutParams params);

    /**
     * Variation on {@link #setContentView(android.view.View, android.view.ViewGroup.LayoutParams)}
     * to add an additional content view to the screen. Added after any
     * existing ones on the screen -- existing views are NOT removed.
     *
     * @param view The desired content to display.
     * @param params Layout parameters for the view.
     */
    public abstract void addContentView(View view, ViewGroup.LayoutParams params);

    /**
     * Change the title associated with this activity.
     */
    public abstract void setTitle(CharSequence title);

    /**
     * Change the title associated with this activity.
     */
    public void setTitle(int resId) {
        if (DEBUG) Log.d(TAG, "[setTitle] resId: " + resId);

        setTitle(mActivity.getString(resId));
    }

    /**
     * Sets the visibility of the progress bar in the title.
     * <p>
     * In order for the progress bar to be shown, the feature must be requested
     * via {@link #requestWindowFeature(int)}.
     *
     * @param visible Whether to show the progress bars in the title.
     */
    public abstract void setProgressBarVisibility(boolean visible);

    /**
     * Sets the visibility of the indeterminate progress bar in the title.
     * <p>
     * In order for the progress bar to be shown, the feature must be requested
     * via {@link #requestWindowFeature(int)}.
     *
     * @param visible Whether to show the progress bars in the title.
     */
    public abstract void setProgressBarIndeterminateVisibility(boolean visible);

    /**
     * Sets whether the horizontal progress bar in the title should be indeterminate (the circular
     * is always indeterminate).
     * <p>
     * In order for the progress bar to be shown, the feature must be requested
     * via {@link #requestWindowFeature(int)}.
     *
     * @param indeterminate Whether the horizontal progress bar should be indeterminate.
     */
    public abstract void setProgressBarIndeterminate(boolean indeterminate);

    /**
     * Sets the progress for the progress bars in the title.
     * <p>
     * In order for the progress bar to be shown, the feature must be requested
     * via {@link #requestWindowFeature(int)}.
     *
     * @param progress The progress for the progress bar. Valid ranges are from
     *            0 to 10000 (both inclusive). If 10000 is given, the progress
     *            bar will be completely filled and will fade out.
     */
    public abstract void setProgress(int progress);

    /**
     * Sets the secondary progress for the progress bar in the title. This
     * progress is drawn between the primary progress (set via
     * {@link #setProgress(int)} and the background. It can be ideal for media
     * scenarios such as showing the buffering progress while the default
     * progress shows the play progress.
     * <p>
     * In order for the progress bar to be shown, the feature must be requested
     * via {@link #requestWindowFeature(int)}.
     *
     * @param secondaryProgress The secondary progress for the progress bar. Valid ranges are from
     *            0 to 10000 (both inclusive).
     */
    public abstract void setSecondaryProgress(int secondaryProgress);

    /**
     * Get a menu inflater instance which supports the newer menu attributes.
     *
     * @return Menu inflater instance.
     */
    public MenuInflater getMenuInflater() {
        if (DEBUG) Log.d(TAG, "[getMenuInflater]");

        // Make sure that action views can get an appropriate theme.
        if (mMenuInflater == null) {
            if (getActionBar() != null) {
                mMenuInflater = new MenuInflater(getThemedContext());
            } else {
                mMenuInflater = new MenuInflater(mActivity);
            }
        }
        return mMenuInflater;
    }

    protected abstract Context getThemedContext();

    /**
     * Start an action mode.
     *
     * @param callback Callback that will manage lifecycle events for this
     *                 context mode.
     * @return The ContextMode that was started, or null if it was canceled.
     * @see ActionMode
     */
    public abstract ActionMode startActionMode(ActionMode.Callback callback);
}
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.actionbarsherlock.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.SpinnerAdapter;

/**
 * A window feature at the top of the activity that may display the activity title, navigation
 * modes, and other interactive items.
 * <p>Beginning with Android 3.0 (API level 11), the action bar appears at the top of an
 * activity's window when the activity uses the system's {@link
 * android.R.style#Theme_Holo Holo} theme (or one of its descendant themes), which is the default.
 * You may otherwise add the action bar by calling {@link
 * android.view.Window#requestFeature requestFeature(FEATURE_ACTION_BAR)} or by declaring it in a
 * custom theme with the {@link android.R.styleable#Theme_windowActionBar windowActionBar} property.
 * <p>By default, the action bar shows the application icon on
 * the left, followed by the activity title. If your activity has an options menu, you can make
 * select items accessible directly from the action bar as "action items". You can also
 * modify various characteristics of the action bar or remove it completely.</p>
 * <p>From your activity, you can retrieve an instance of {@link ActionBar} by calling {@link
 * android.app.Activity#getActionBar getActionBar()}.</p>
 * <p>In some cases, the action bar may be overlayed by another bar that enables contextual actions,
 * using an {@link android.view.ActionMode}. For example, when the user selects one or more items in
 * your activity, you can enable an action mode that offers actions specific to the selected
 * items, with a UI that temporarily replaces the action bar. Although the UI may occupy the
 * same space, the {@link android.view.ActionMode} APIs are distinct and independent from those for
 * {@link ActionBar}.
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about how to use the action bar, including how to add action items, navigation
 * modes and more, read the <a href="{@docRoot}guide/topics/ui/actionbar.html">Action
 * Bar</a> developer guide.</p>
 * </div>
 */
public abstract class ActionBar {
    /**
     * Standard navigation mode. Consists of either a logo or icon
     * and title text with an optional subtitle. Clicking any of these elements
     * will dispatch onOptionsItemSelected to the host Activity with
     * a MenuItem with item ID android.R.id.home.
     */
    public static final int NAVIGATION_MODE_STANDARD = android.app.ActionBar.NAVIGATION_MODE_STANDARD;

    /**
     * List navigation mode. Instead of static title text this mode
     * presents a list menu for navigation within the activity.
     * e.g. this might be presented to the user as a dropdown list.
     */
    public static final int NAVIGATION_MODE_LIST = android.app.ActionBar.NAVIGATION_MODE_LIST;

    /**
     * Tab navigation mode. Instead of static title text this mode
     * presents a series of tabs for navigation within the activity.
     */
    public static final int NAVIGATION_MODE_TABS = android.app.ActionBar.NAVIGATION_MODE_TABS;

    /**
     * Use logo instead of icon if available. This flag will cause appropriate
     * navigation modes to use a wider logo in place of the standard icon.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_USE_LOGO = android.app.ActionBar.DISPLAY_USE_LOGO;

    /**
     * Show 'home' elements in this action bar, leaving more space for other
     * navigation elements. This includes logo and icon.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_HOME = android.app.ActionBar.DISPLAY_SHOW_HOME;

    /**
     * Display the 'home' element such that it appears as an 'up' affordance.
     * e.g. show an arrow to the left indicating the action that will be taken.
     *
     * Set this flag if selecting the 'home' button in the action bar to return
     * up by a single level in your UI rather than back to the top level or front page.
     *
     * <p>Setting this option will implicitly enable interaction with the home/up
     * button. See {@link #setHomeButtonEnabled(boolean)}.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_HOME_AS_UP = android.app.ActionBar.DISPLAY_HOME_AS_UP;

    /**
     * Show the activity title and subtitle, if present.
     *
     * @see #setTitle(CharSequence)
     * @see #setTitle(int)
     * @see #setSubtitle(CharSequence)
     * @see #setSubtitle(int)
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_TITLE = android.app.ActionBar.DISPLAY_SHOW_TITLE;

    /**
     * Show the custom view if one has been set.
     * @see #setCustomView(View)
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public static final int DISPLAY_SHOW_CUSTOM = android.app.ActionBar.DISPLAY_SHOW_CUSTOM;

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.
     *
     * @param view Custom navigation view to place in the ActionBar.
     */
    public abstract void setCustomView(View view);

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * <p>Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.</p>
     *
     * <p>The display option {@link #DISPLAY_SHOW_CUSTOM} must be set for
     * the custom view to be displayed.</p>
     *
     * @param view Custom navigation view to place in the ActionBar.
     * @param layoutParams How this custom view should layout in the bar.
     *
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setCustomView(View view, LayoutParams layoutParams);

    /**
     * Set the action bar into custom navigation mode, supplying a view
     * for custom navigation.
     *
     * <p>Custom navigation views appear between the application icon and
     * any action buttons and may use any space available there. Common
     * use cases for custom navigation views might include an auto-suggesting
     * address bar for a browser or other navigation mechanisms that do not
     * translate well to provided navigation modes.</p>
     *
     * <p>The display option {@link #DISPLAY_SHOW_CUSTOM} must be set for
     * the custom view to be displayed.</p>
     *
     * @param resId Resource ID of a layout to inflate into the ActionBar.
     *
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setCustomView(int resId);

    /**
     * Set the icon to display in the 'home' section of the action bar.
     * The action bar will use an icon specified by its style or the
     * activity icon by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param resId Resource ID of a drawable to show as an icon.
     *
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setIcon(int resId);

    /**
     * Set the icon to display in the 'home' section of the action bar.
     * The action bar will use an icon specified by its style or the
     * activity icon by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param icon Drawable to show as an icon.
     *
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setIcon(Drawable icon);

    /**
     * Set the logo to display in the 'home' section of the action bar.
     * The action bar will use a logo specified by its style or the
     * activity logo by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param resId Resource ID of a drawable to show as a logo.
     *
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setLogo(int resId);

    /**
     * Set the logo to display in the 'home' section of the action bar.
     * The action bar will use a logo specified by its style or the
     * activity logo by default.
     *
     * Whether the home section shows an icon or logo is controlled
     * by the display option {@link #DISPLAY_USE_LOGO}.
     *
     * @param logo Drawable to show as a logo.
     *
     * @see #setDisplayUseLogoEnabled(boolean)
     * @see #setDisplayShowHomeEnabled(boolean)
     */
    public abstract void setLogo(Drawable logo);

    /**
     * Set the adapter and navigation callback for list navigation mode.
     *
     * The supplied adapter will provide views for the expanded list as well as
     * the currently selected item. (These may be displayed differently.)
     *
     * The supplied OnNavigationListener will alert the application when the user
     * changes the current list selection.
     *
     * @param adapter An adapter that will provide views both to display
     *                the current navigation selection and populate views
     *                within the dropdown navigation menu.
     * @param callback An OnNavigationListener that will receive events when the user
     *                 selects a navigation item.
     */
    public abstract void setListNavigationCallbacks(SpinnerAdapter adapter,
            OnNavigationListener callback);

    /**
     * Set the selected navigation item in list or tabbed navigation modes.
     *
     * @param position Position of the item to select.
     */
    public abstract void setSelectedNavigationItem(int position);

    /**
     * Get the position of the selected navigation item in list or tabbed navigation modes.
     *
     * @return Position of the selected item.
     */
    public abstract int getSelectedNavigationIndex();

    /**
     * Get the number of navigation items present in the current navigation mode.
     *
     * @return Number of navigation items.
     */
    public abstract int getNavigationItemCount();

    /**
     * Set the action bar's title. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param title Title to set
     *
     * @see #setTitle(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setTitle(CharSequence title);

    /**
     * Set the action bar's title. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param resId Resource ID of title string to set
     *
     * @see #setTitle(CharSequence)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setTitle(int resId);

    /**
     * Set the action bar's subtitle. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set. Set to null to disable the
     * subtitle entirely.
     *
     * @param subtitle Subtitle to set
     *
     * @see #setSubtitle(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setSubtitle(CharSequence subtitle);

    /**
     * Set the action bar's subtitle. This will only be displayed if
     * {@link #DISPLAY_SHOW_TITLE} is set.
     *
     * @param resId Resource ID of subtitle string to set
     *
     * @see #setSubtitle(CharSequence)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setSubtitle(int resId);

    /**
     * Set display options. This changes all display option bits at once. To change
     * a limited subset of display options, see {@link #setDisplayOptions(int, int)}.
     *
     * @param options A combination of the bits defined by the DISPLAY_ constants
     *                defined in ActionBar.
     */
    public abstract void setDisplayOptions(int options);

    /**
     * Set selected display options. Only the options specified by mask will be changed.
     * To change all display option bits at once, see {@link #setDisplayOptions(int)}.
     *
     * <p>Example: setDisplayOptions(0, DISPLAY_SHOW_HOME) will disable the
     * {@link #DISPLAY_SHOW_HOME} option.
     * setDisplayOptions(DISPLAY_SHOW_HOME, DISPLAY_SHOW_HOME | DISPLAY_USE_LOGO)
     * will enable {@link #DISPLAY_SHOW_HOME} and disable {@link #DISPLAY_USE_LOGO}.
     *
     * @param options A combination of the bits defined by the DISPLAY_ constants
     *                defined in ActionBar.
     * @param mask A bit mask declaring which display options should be changed.
     */
    public abstract void setDisplayOptions(int options, int mask);

    /**
     * Set whether to display the activity logo rather than the activity icon.
     * A logo is often a wider, more detailed image.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param useLogo true to use the activity logo, false to use the activity icon.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayUseLogoEnabled(boolean useLogo);

    /**
     * Set whether to include the application home affordance in the action bar.
     * Home is presented as either an activity icon or logo.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showHome true to show home, false otherwise.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowHomeEnabled(boolean showHome);

    /**
     * Set whether home should be displayed as an "up" affordance.
     * Set this to true if selecting "home" returns up by a single level in your UI
     * rather than back to the top level or front page.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showHomeAsUp true to show the user that selecting home will return one
     *                     level up rather than to the top level of the app.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayHomeAsUpEnabled(boolean showHomeAsUp);

    /**
     * Set whether an activity title/subtitle should be displayed.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showTitle true to display a title/subtitle if present.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowTitleEnabled(boolean showTitle);

    /**
     * Set whether a custom view should be displayed, if set.
     *
     * <p>To set several display options at once, see the setDisplayOptions methods.
     *
     * @param showCustom true if the currently set custom view should be displayed, false otherwise.
     *
     * @see #setDisplayOptions(int)
     * @see #setDisplayOptions(int, int)
     */
    public abstract void setDisplayShowCustomEnabled(boolean showCustom);

    /**
     * Set the ActionBar's background. This will be used for the primary
     * action bar.
     *
     * @param d Background drawable
     * @see #setStackedBackgroundDrawable(Drawable)
     * @see #setSplitBackgroundDrawable(Drawable)
     */
    public abstract void setBackgroundDrawable(Drawable d);

    /**
     * Set the ActionBar's stacked background. This will appear
     * in the second row/stacked bar on some devices and configurations.
     *
     * @param d Background drawable for the stacked row
     */
    public void setStackedBackgroundDrawable(Drawable d) { }

    /**
     * Set the ActionBar's split background. This will appear in
     * the split action bar containing menu-provided action buttons
     * on some devices and configurations.
     * <p>You can enable split action bar with {@link android.R.attr#uiOptions}
     *
     * @param d Background drawable for the split bar
     */
    public void setSplitBackgroundDrawable(Drawable d) { }

    /**
     * @return The current custom view.
     */
    public abstract View getCustomView();

    /**
     * Returns the current ActionBar title in standard mode.
     * Returns null if {@link #getNavigationMode()} would not return
     * {@link #NAVIGATION_MODE_STANDARD}.
     *
     * @return The current ActionBar title or null.
     */
    public abstract CharSequence getTitle();

    /**
     * Returns the current ActionBar subtitle in standard mode.
     * Returns null if {@link #getNavigationMode()} would not return
     * {@link #NAVIGATION_MODE_STANDARD}.
     *
     * @return The current ActionBar subtitle or null.
     */
    public abstract CharSequence getSubtitle();

    /**
     * Returns the current navigation mode. The result will be one of:
     * <ul>
     * <li>{@link #NAVIGATION_MODE_STANDARD}</li>
     * <li>{@link #NAVIGATION_MODE_LIST}</li>
     * <li>{@link #NAVIGATION_MODE_TABS}</li>
     * </ul>
     *
     * @return The current navigation mode.
     */
    public abstract int getNavigationMode();

    /**
     * Set the current navigation mode.
     *
     * @param mode The new mode to set.
     * @see #NAVIGATION_MODE_STANDARD
     * @see #NAVIGATION_MODE_LIST
     * @see #NAVIGATION_MODE_TABS
     */
    public abstract void setNavigationMode(int mode);

    /**
     * @return The current set of display options.
     */
    public abstract int getDisplayOptions();

    /**
     * Create and return a new {@link Tab}.
     * This tab will not be included in the action bar until it is added.
     *
     * <p>Very often tabs will be used to switch between {@link Fragment}
     * objects.  Here is a typical implementation of such tabs:</p>
     *
     * {@sample development/samples/ApiDemos/src/com/example/android/apis/app/FragmentTabs.java
     *      complete}
     *
     * @return A new Tab
     *
     * @see #addTab(Tab)
     */
    public abstract Tab newTab();

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be added at the end of the list.
     * If this is the first tab to be added it will become the selected tab.
     *
     * @param tab Tab to add
     */
    public abstract void addTab(Tab tab);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be added at the end of the list.
     *
     * @param tab Tab to add
     * @param setSelected True if the added tab should become the selected tab.
     */
    public abstract void addTab(Tab tab, boolean setSelected);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be inserted at
     * <code>position</code>. If this is the first tab to be added it will become
     * the selected tab.
     *
     * @param tab The tab to add
     * @param position The new position of the tab
     */
    public abstract void addTab(Tab tab, int position);

    /**
     * Add a tab for use in tabbed navigation mode. The tab will be insterted at
     * <code>position</code>.
     *
     * @param tab The tab to add
     * @param position The new position of the tab
     * @param setSelected True if the added tab should become the selected tab.
     */
    public abstract void addTab(Tab tab, int position, boolean setSelected);

    /**
     * Remove a tab from the action bar. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param tab The tab to remove
     */
    public abstract void removeTab(Tab tab);

    /**
     * Remove a tab from the action bar. If the removed tab was selected it will be deselected
     * and another tab will be selected if present.
     *
     * @param position Position of the tab to remove
     */
    public abstract void removeTabAt(int position);

    /**
     * Remove all tabs from the action bar and deselect the current tab.
     */
    public abstract void removeAllTabs();

    /**
     * Select the specified tab. If it is not a child of this action bar it will be added.
     *
     * <p>Note: If you want to select by index, use {@link #setSelectedNavigationItem(int)}.</p>
     *
     * @param tab Tab to select
     */
    public abstract void selectTab(Tab tab);

    /**
     * Returns the currently selected tab if in tabbed navigation mode and there is at least
     * one tab present.
     *
     * @return The currently selected tab or null
     */
    public abstract Tab getSelectedTab();

    /**
     * Returns the tab at the specified index.
     *
     * @param index Index value in the range 0-get
     * @return
     */
    public abstract Tab getTabAt(int index);

    /**
     * Returns the number of tabs currently registered with the action bar.
     * @return Tab count
     */
    public abstract int getTabCount();

    /**
     * Retrieve the current height of the ActionBar.
     *
     * @return The ActionBar's height
     */
    public abstract int getHeight();

    /**
     * Show the ActionBar if it is not currently showing.
     * If the window hosting the ActionBar does not have the feature
     * {@link Window#FEATURE_ACTION_BAR_OVERLAY} it will resize application
     * content to fit the new space available.
     */
    public abstract void show();

    /**
     * Hide the ActionBar if it is currently showing.
     * If the window hosting the ActionBar does not have the feature
     * {@link Window#FEATURE_ACTION_BAR_OVERLAY} it will resize application
     * content to fit the new space available.
     */
    public abstract void hide();

    /**
     * @return <code>true</code> if the ActionBar is showing, <code>false</code> otherwise.
     */
    public abstract boolean isShowing();

    /**
     * Add a listener that will respond to menu visibility change events.
     *
     * @param listener The new listener to add
     */
    public abstract void addOnMenuVisibilityListener(OnMenuVisibilityListener listener);

    /**
     * Remove a menu visibility listener. This listener will no longer receive menu
     * visibility change events.
     *
     * @param listener A listener to remove that was previously added
     */
    public abstract void removeOnMenuVisibilityListener(OnMenuVisibilityListener listener);

    /**
     * Enable or disable the "home" button in the corner of the action bar. (Note that this
     * is the application home/up affordance on the action bar, not the systemwide home
     * button.)
     *
     * <p>This defaults to true for packages targeting &lt; API 14. For packages targeting
     * API 14 or greater, the application should call this method to enable interaction
     * with the home/up affordance.
     *
     * <p>Setting the {@link #DISPLAY_HOME_AS_UP} display option will automatically enable
     * the home button.
     *
     * @param enabled true to enable the home button, false to disable the home button.
     */
    public void setHomeButtonEnabled(boolean enabled) { }

    /**
     * Returns a {@link Context} with an appropriate theme for creating views that
     * will appear in the action bar. If you are inflating or instantiating custom views
     * that will appear in an action bar, you should use the Context returned by this method.
     * (This includes adapters used for list navigation mode.)
     * This will ensure that views contrast properly against the action bar.
     *
     * @return A themed Context for creating views
     */
    public Context getThemedContext() { return null; }

    /**
     * Listener interface for ActionBar navigation events.
     */
    public interface OnNavigationListener {
        /**
         * This method is called whenever a navigation item in your action bar
         * is selected.
         *
         * @param itemPosition Position of the item clicked.
         * @param itemId ID of the item clicked.
         * @return True if the event was handled, false otherwise.
         */
        public boolean onNavigationItemSelected(int itemPosition, long itemId);
    }

    /**
     * Listener for receiving events when action bar menus are shown or hidden.
     */
    public interface OnMenuVisibilityListener {
        /**
         * Called when an action bar menu is shown or hidden. Applications may want to use
         * this to tune auto-hiding behavior for the action bar or pause/resume video playback,
         * gameplay, or other activity within the main content area.
         *
         * @param isVisible True if an action bar menu is now visible, false if no action bar
         *                  menus are visible.
         */
        public void onMenuVisibilityChanged(boolean isVisible);
    }

    /**
     * A tab in the action bar.
     *
     * <p>Tabs manage the hiding and showing of {@link Fragment}s.
     */
    public static abstract class Tab {
        /**
         * An invalid position for a tab.
         *
         * @see #getPosition()
         */
        public static final int INVALID_POSITION = -1;

        /**
         * Return the current position of this tab in the action bar.
         *
         * @return Current position, or {@link #INVALID_POSITION} if this tab is not currently in
         *         the action bar.
         */
        public abstract int getPosition();

        /**
         * Return the icon associated with this tab.
         *
         * @return The tab's icon
         */
        public abstract Drawable getIcon();

        /**
         * Return the text of this tab.
         *
         * @return The tab's text
         */
        public abstract CharSequence getText();

        /**
         * Set the icon displayed on this tab.
         *
         * @param icon The drawable to use as an icon
         * @return The current instance for call chaining
         */
        public abstract Tab setIcon(Drawable icon);

        /**
         * Set the icon displayed on this tab.
         *
         * @param resId Resource ID referring to the drawable to use as an icon
         * @return The current instance for call chaining
         */
        public abstract Tab setIcon(int resId);

        /**
         * Set the text displayed on this tab. Text may be truncated if there is not
         * room to display the entire string.
         *
         * @param text The text to display
         * @return The current instance for call chaining
         */
        public abstract Tab setText(CharSequence text);

        /**
         * Set the text displayed on this tab. Text may be truncated if there is not
         * room to display the entire string.
         *
         * @param resId A resource ID referring to the text that should be displayed
         * @return The current instance for call chaining
         */
        public abstract Tab setText(int resId);

        /**
         * Set a custom view to be used for this tab. This overrides values set by
         * {@link #setText(CharSequence)} and {@link #setIcon(Drawable)}.
         *
         * @param view Custom view to be used as a tab.
         * @return The current instance for call chaining
         */
        public abstract Tab setCustomView(View view);

        /**
         * Set a custom view to be used for this tab. This overrides values set by
         * {@link #setText(CharSequence)} and {@link #setIcon(Drawable)}.
         *
         * @param layoutResId A layout resource to inflate and use as a custom tab view
         * @return The current instance for call chaining
         */
        public abstract Tab setCustomView(int layoutResId);

        /**
         * Retrieve a previously set custom view for this tab.
         *
         * @return The custom view set by {@link #setCustomView(View)}.
         */
        public abstract View getCustomView();

        /**
         * Give this Tab an arbitrary object to hold for later use.
         *
         * @param obj Object to store
         * @return The current instance for call chaining
         */
        public abstract Tab setTag(Object obj);

        /**
         * @return This Tab's tag object.
         */
        public abstract Object getTag();

        /**
         * Set the {@link TabListener} that will handle switching to and from this tab.
         * All tabs must have a TabListener set before being added to the ActionBar.
         *
         * @param listener Listener to handle tab selection events
         * @return The current instance for call chaining
         */
        public abstract Tab setTabListener(TabListener listener);

        /**
         * Select this tab. Only valid if the tab has been added to the action bar.
         */
        public abstract void select();

        /**
         * Set a description of this tab's content for use in accessibility support.
         * If no content description is provided the title will be used.
         *
         * @param resId A resource ID referring to the description text
         * @return The current instance for call chaining
         * @see #setContentDescription(CharSequence)
         * @see #getContentDescription()
         */
        public abstract Tab setContentDescription(int resId);

        /**
         * Set a description of this tab's content for use in accessibility support.
         * If no content description is provided the title will be used.
         *
         * @param contentDesc Description of this tab's content
         * @return The current instance for call chaining
         * @see #setContentDescription(int)
         * @see #getContentDescription()
         */
        public abstract Tab setContentDescription(CharSequence contentDesc);

        /**
         * Gets a brief description of this tab's content for use in accessibility support.
         *
         * @return Description of this tab's content
         * @see #setContentDescription(CharSequence)
         * @see #setContentDescription(int)
         */
        public abstract CharSequence getContentDescription();
    }

    /**
     * Callback interface invoked when a tab is focused, unfocused, added, or removed.
     */
    public interface TabListener {
        /**
         * Called when a tab enters the selected state.
         *
         * @param tab The tab that was selected
         * @param ft A {@link FragmentTransaction} for queuing fragment operations to execute
         *        during a tab switch. The previous tab's unselect and this tab's select will be
         *        executed in a single transaction. This FragmentTransaction does not support
         *        being added to the back stack.
         */
        public void onTabSelected(Tab tab, FragmentTransaction ft);

        /**
         * Called when a tab exits the selected state.
         *
         * @param tab The tab that was unselected
         * @param ft A {@link FragmentTransaction} for queuing fragment operations to execute
         *        during a tab switch. This tab's unselect and the newly selected tab's select
         *        will be executed in a single transaction. This FragmentTransaction does not
         *        support being added to the back stack.
         */
        public void onTabUnselected(Tab tab, FragmentTransaction ft);

        /**
         * Called when a tab that is already selected is chosen again by the user.
         * Some applications may use this action to return to the top level of a category.
         *
         * @param tab The tab that was reselected.
         * @param ft A {@link FragmentTransaction} for queuing fragment operations to execute
         *        once this method returns. This FragmentTransaction does not support
         *        being added to the back stack.
         */
        public void onTabReselected(Tab tab, FragmentTransaction ft);
    }

    /**
     * Per-child layout information associated with action bar custom views.
     *
     * @attr ref android.R.styleable#ActionBar_LayoutParams_layout_gravity
     */
    public static class LayoutParams extends MarginLayoutParams {
        /**
         * Gravity for the view associated with these LayoutParams.
         *
         * @see android.view.Gravity
         */
        @ViewDebug.ExportedProperty(mapping = {
            @ViewDebug.IntToString(from =  -1,                       to = "NONE"),
            @ViewDebug.IntToString(from = Gravity.NO_GRAVITY,        to = "NONE"),
            @ViewDebug.IntToString(from = Gravity.TOP,               to = "TOP"),
            @ViewDebug.IntToString(from = Gravity.BOTTOM,            to = "BOTTOM"),
            @ViewDebug.IntToString(from = Gravity.LEFT,              to = "LEFT"),
            @ViewDebug.IntToString(from = Gravity.RIGHT,             to = "RIGHT"),
            @ViewDebug.IntToString(from = Gravity.CENTER_VERTICAL,   to = "CENTER_VERTICAL"),
            @ViewDebug.IntToString(from = Gravity.FILL_VERTICAL,     to = "FILL_VERTICAL"),
            @ViewDebug.IntToString(from = Gravity.CENTER_HORIZONTAL, to = "CENTER_HORIZONTAL"),
            @ViewDebug.IntToString(from = Gravity.FILL_HORIZONTAL,   to = "FILL_HORIZONTAL"),
            @ViewDebug.IntToString(from = Gravity.CENTER,            to = "CENTER"),
            @ViewDebug.IntToString(from = Gravity.FILL,              to = "FILL")
        })
        public int gravity = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(int gravity) {
            this(WRAP_CONTENT, FILL_PARENT, gravity);
        }

        public LayoutParams(LayoutParams source) {
            super(source);

            this.gravity = source.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
package com.actionbarsherlock.app;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.ActionBarSherlock.OnActionModeFinishedListener;
import com.actionbarsherlock.ActionBarSherlock.OnActionModeStartedListener;
import com.actionbarsherlock.ActionBarSherlock.OnCreatePanelMenuListener;
import com.actionbarsherlock.ActionBarSherlock.OnMenuItemSelectedListener;
import com.actionbarsherlock.ActionBarSherlock.OnPreparePanelListener;
import com.actionbarsherlock.internal.view.menu.MenuItemMule;
import com.actionbarsherlock.internal.view.menu.MenuMule;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public abstract class SherlockFragmentActivity extends FragmentActivity implements OnCreatePanelMenuListener, OnPreparePanelListener, OnMenuItemSelectedListener, OnActionModeStartedListener, OnActionModeFinishedListener {
    static final boolean DEBUG = false;
    private static final String TAG = "SherlockFragmentActivity";

    private ActionBarSherlock mSherlock;
    private boolean mIgnoreNativeCreate = false;
    private boolean mIgnoreNativePrepare = false;
    private boolean mIgnoreNativeSelected = false;
    private Boolean mOverrideNativeCreate = null;

    protected final ActionBarSherlock getSherlock() {
        if (mSherlock == null) {
            mSherlock = ActionBarSherlock.wrap(this, ActionBarSherlock.FLAG_DELEGATE);
        }
        return mSherlock;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Action bar and mode
    ///////////////////////////////////////////////////////////////////////////

    public ActionBar getSupportActionBar() {
        return getSherlock().getActionBar();
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return getSherlock().startActionMode(callback);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {}

    @Override
    public void onActionModeFinished(ActionMode mode) {}


    ///////////////////////////////////////////////////////////////////////////
    // General lifecycle/callback dispatching
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getSherlock().dispatchConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getSherlock().dispatchPostResume();
    }

    @Override
    protected void onPause() {
        getSherlock().dispatchPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        getSherlock().dispatchStop();
        super.onStop();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        getSherlock().dispatchPostCreate(savedInstanceState);
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        getSherlock().dispatchTitleChanged(title, color);
        super.onTitleChanged(title, color);
    }

    @Override
    public final boolean onMenuOpened(int featureId, android.view.Menu menu) {
        if (getSherlock().dispatchMenuOpened(featureId, menu)) {
            return true;
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void onPanelClosed(int featureId, android.view.Menu menu) {
        getSherlock().dispatchPanelClosed(featureId, menu);
        super.onPanelClosed(featureId, menu);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (getSherlock().dispatchKeyEvent(event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Native menu handling
    ///////////////////////////////////////////////////////////////////////////

    public MenuInflater getSupportMenuInflater() {
        if (DEBUG) Log.d(TAG, "[getSupportMenuInflater]");

        return getSherlock().getMenuInflater();
    }

    public void invalidateOptionsMenu() {
        if (DEBUG) Log.d(TAG, "[invalidateOptionsMenu]");

        getSherlock().dispatchInvalidateOptionsMenu();
    }

    protected void supportInvalidateOptionsMenu() {
        if (DEBUG) Log.d(TAG, "[supportInvalidateOptionsMenu]");

        invalidateOptionsMenu();
    }

    @Override
    public final boolean onCreatePanelMenu(int featureId, android.view.Menu menu) {
        if (DEBUG) Log.d(TAG, "[onCreatePanelMenu] featureId: " + featureId + ", menu: " + menu);

        if (featureId == Window.FEATURE_OPTIONS_PANEL && !mIgnoreNativeCreate) {
            mIgnoreNativeCreate = true;
            boolean result = getSherlock().dispatchCreateOptionsMenu(menu);
            mIgnoreNativeCreate = false;

            if (DEBUG) Log.d(TAG, "[onCreatePanelMenu] returning " + result);
            return result;
        }
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public final boolean onCreateOptionsMenu(android.view.Menu menu) {
        return (mOverrideNativeCreate != null) ? mOverrideNativeCreate.booleanValue() : true;
    }

    @Override
    public final boolean onPreparePanel(int featureId, View view, android.view.Menu menu) {
        if (DEBUG) Log.d(TAG, "[onPreparePanel] featureId: " + featureId + ", view: " + view + ", menu: " + menu);

        if (featureId == Window.FEATURE_OPTIONS_PANEL && !mIgnoreNativePrepare) {
            mIgnoreNativePrepare = true;
            boolean result = getSherlock().dispatchPrepareOptionsMenu(menu);
            mIgnoreNativePrepare = false;

            if (DEBUG) Log.d(TAG, "[onPreparePanel] returning " + result);
            return result;
        }
        return super.onPreparePanel(featureId, view, menu);
    }

    @Override
    public final boolean onPrepareOptionsMenu(android.view.Menu menu) {
        return true;
    }

    @Override
    public final boolean onMenuItemSelected(int featureId, android.view.MenuItem item) {
        if (DEBUG) Log.d(TAG, "[onMenuItemSelected] featureId: " + featureId + ", item: " + item);

        if (featureId == Window.FEATURE_OPTIONS_PANEL && !mIgnoreNativeSelected) {
            mIgnoreNativeSelected = true;
            boolean result = getSherlock().dispatchOptionsItemSelected(item);
            mIgnoreNativeSelected = false;

            if (DEBUG) Log.d(TAG, "[onMenuItemSelected] returning " + result);
            return result;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public final boolean onOptionsItemSelected(android.view.MenuItem item) {
        return false;
    }

    @Override
    public void openOptionsMenu() {
        if (!getSherlock().dispatchOpenOptionsMenu()) {
            super.openOptionsMenu();
        }
    }

    @Override
    public void closeOptionsMenu() {
        if (!getSherlock().dispatchCloseOptionsMenu()) {
            super.closeOptionsMenu();
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // Sherlock menu handling
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (DEBUG) Log.d(TAG, "[onCreatePanelMenu] featureId: " + featureId + ", menu: " + menu);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            boolean result = onCreateOptionsMenu(menu);

            //Dispatch to parent panel creation for fragment dispatching
            if (DEBUG) Log.d(TAG, "[onCreatePanelMenu] dispatching to native with mule");
            mOverrideNativeCreate = result;
            boolean fragResult = super.onCreatePanelMenu(featureId, new MenuMule(menu));
            mOverrideNativeCreate = null;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                result |= menu.hasVisibleItems();
            } else {
                result |= fragResult;
            }

            return result;
        }
        return false;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (DEBUG) Log.d(TAG, "[onPreparePanel] featureId: " + featureId + ", view: " + view + " menu: " + menu);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            boolean result = onPrepareOptionsMenu(menu);

            //Dispatch to parent panel preparation for fragment dispatching
            if (DEBUG) Log.d(TAG, "[onPreparePanel] dispatching to native with mule");
            super.onPreparePanel(featureId, view, new MenuMule(menu));

            return result;
        }
        return false;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (DEBUG) Log.d(TAG, "[onMenuItemSelected] featureId: " + featureId + ", item: " + item);

        if (featureId == Window.FEATURE_OPTIONS_PANEL) {
            boolean result = onOptionsItemSelected(item);

            //Dispatch to parent panel selection for fragment dispatching
            if (DEBUG) Log.d(TAG, "[onMenuItemSelected] dispatching to native with mule");
            result |= super.onMenuItemSelected(featureId, new MenuItemMule(item));

            return result;
        }
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }


    ///////////////////////////////////////////////////////////////////////////
    // Content
    ///////////////////////////////////////////////////////////////////////////

    @Override
    public void addContentView(View view, LayoutParams params) {
        getSherlock().addContentView(view, params);
    }

    @Override
    public void setContentView(int layoutResId) {
        getSherlock().setContentView(layoutResId);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        getSherlock().setContentView(view, params);
    }

    @Override
    public void setContentView(View view) {
        getSherlock().setContentView(view);
    }

    public void requestWindowFeature(long featureId) {
        getSherlock().requestFeature((int)featureId);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Progress Indication
    ///////////////////////////////////////////////////////////////////////////

    public void setSupportProgress(int progress) {
        getSherlock().setProgress(progress);
    }

    public void setSupportProgressBarIndeterminate(boolean indeterminate) {
        getSherlock().setProgressBarIndeterminate(indeterminate);
    }

    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
        getSherlock().setProgressBarIndeterminateVisibility(visible);
    }

    public void setSupportProgressBarVisibility(boolean visible) {
        getSherlock().setProgressBarVisibility(visible);
    }

    public void setSupportSecondaryProgress(int secondaryProgress) {
        getSherlock().setSecondaryProgress(secondaryProgress);
    }
}
package edu.colorado.cs.cirrus.android;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;
import edu.colorado.cs.cirrus.domain.TendrilException;
import edu.colorado.cs.cirrus.domain.intf.ITendril;
import edu.colorado.cs.cirrus.domain.intf.TendrilUrls;
import edu.colorado.cs.cirrus.domain.model.AccessGrant;
import edu.colorado.cs.cirrus.domain.model.CostAndConsumption;
import edu.colorado.cs.cirrus.domain.model.Device;
import edu.colorado.cs.cirrus.domain.model.DeviceData;
import edu.colorado.cs.cirrus.domain.model.Devices;
import edu.colorado.cs.cirrus.domain.model.ExternalAccountId;
import edu.colorado.cs.cirrus.domain.model.MeterReadings;
import edu.colorado.cs.cirrus.domain.model.PricingProgram;
import edu.colorado.cs.cirrus.domain.model.PricingSchedule;
import edu.colorado.cs.cirrus.domain.model.Resolution;
import edu.colorado.cs.cirrus.domain.model.SetThermostatDataRequest;
import edu.colorado.cs.cirrus.domain.model.User;
import edu.colorado.cs.cirrus.domain.model.UserProfile;
import edu.colorado.cs.cirrus.domain.model.fetchThermostatDataRequest;

public class TendrilTemplate implements ITendril {
    private static final String TAG = "TendrilTemplate";
    private static TendrilTemplate instance;

    public static TendrilTemplate get() {
        if (instance == null) {
            instance = new TendrilTemplate();
        }
        return instance;
    }

    private HttpEntity<?> requestEntity;
    private HttpHeaders requestHeaders;
    private final RestTemplate restTemplate;
    private AccessGrant accessGrant = null;
    private User user = null;
    private UserProfile userProfile = null;
    private ExternalAccountId externalAccountId = null;
    private Devices devices = null;

    private Device tstat = null;

    private TendrilTemplate() {
        System.err.println("Initializing TendrilTemplate");

        this.restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        // The HttpComponentsClientHttpRequestFactory uses the
        // org.apache.http package to make network requests
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpUtils.getNewHttpClient()));
    }

    public String logIn(String userName, String password) throws TendrilException {
        Log.i(TAG, "logIn attempt: username: " + userName + ", password: " + password);
        String accessToken="";
        try{
        	accessToken = authorize(false, userName, password);
        	setRequestEntity();
        } catch(Exception e){
        	throw new TendrilAndroidException(e);
        }
        Log.i(TAG, "login successful! access token: " + accessGrant.getAccess_token());
        return accessToken;
    }

    private void setRequestEntity() {
        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_XML);

        requestHeaders = new HttpHeaders();
        requestHeaders.setAccept(acceptableMediaTypes);
        requestHeaders.setContentType(MediaType.APPLICATION_XML);
        requestHeaders.set("access_token", this.accessGrant.getAccess_token());
        requestEntity = new HttpEntity<Object>(requestHeaders);
    }

    public void useAccessToken(String accessToken) {
        accessGrant = new AccessGrant();
        accessGrant.setAccess_token(accessToken);
        setRequestEntity();
    }

    private String authorize(boolean refresh, String userName, String password) {
        DateTime expiration = new DateTime();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.set("Accept", "application/json");
        params.set("Content-Type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<String, String>();
        formData.add("scope", TendrilUrls.SCOPE);

        if (refresh) {
            formData.add("grant_type", "refresh_token");
            formData.add("refresh_token", accessGrant.getRefresh_token());
        }
        else {
            formData.add("client_id", TendrilUrls.APP_KEY);
            formData.add("client_secret", TendrilUrls.APP_SECRET);
            formData.add("grant_type", "password");
            formData.add("username", userName);
            formData.add("password", password);
        }
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestHeaders.set("Accept", "application/json");
        // Populate the MultiValueMap being serialized and headers in an
        // HttpEntity object to use for the request
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(
                formData, requestHeaders);

        ResponseEntity<AccessGrant> response = restTemplate.exchange(TendrilUrls.ACCESS_TOKEN_URL, HttpMethod.POST,
                requestEntity, AccessGrant.class);

        System.err.println(response);
        accessGrant = response.getBody();
        accessGrant.setExpirationDateTime(expiration.plusSeconds((int) accessGrant.getExpires_in()));
        System.err.println(accessGrant);
        return accessGrant.getAccess_token();
    }

    // TENDRIL's API is not working well- some date ranges return 500 error for no known reason
    public CostAndConsumption fetchCostAndConsumption(Resolution resolution, DateTime from, DateTime to,
            int limitToLatest) throws TendrilException {
        String fromString = from.toString(ISODateTimeFormat.dateTimeNoMillis());
        String toString = to.toString(ISODateTimeFormat.dateTimeNoMillis());
        System.err.println(fromString);
        System.err.println(toString);

        Object[] vars = { resolution.name(), fromString, toString, limitToLatest };
        ResponseEntity<CostAndConsumption> costAndConsumption;

        try {
            costAndConsumption = restTemplate.exchange(TendrilUrls.GET_HISTORICAL_COST_AND_CONSUMPTION_URL,
                    HttpMethod.GET, requestEntity, CostAndConsumption.class, vars);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }
        return costAndConsumption.getBody();

    }

    public CostAndConsumption fetchCostAndConsumptionRange(DateTime from, DateTime to) throws TendrilException {
        return fetchCostAndConsumption(Resolution.RANGE, from, to, 1);

    }

    public Devices fetchDevices() throws TendrilException {
        ResponseEntity<Devices> devices;
        try {
            devices = restTemplate.exchange(TendrilUrls.GET_DEVICE_LIST_URL, HttpMethod.GET, requestEntity,
                    Devices.class);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }

        return devices.getBody();
    }

    public ExternalAccountId fetchExternalAccountId() throws TendrilException {

        ResponseEntity<ExternalAccountId> response;

        try {
            response = restTemplate.exchange(TendrilUrls.GET_USER_EXTERNAL_ACCOUNT_ID_URL, HttpMethod.GET,
                    requestEntity, ExternalAccountId.class);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }
        // System.err.println(response.getBody());
        this.externalAccountId = response.getBody();
        return externalAccountId;
    }

    // Tendril's API is not working consistently enough to test this
    private MeterReadings fetchMeterReadings(DateTime from, DateTime to, Integer limitToLatest, Source source)
            throws TendrilException {

        String fromString = from.toString(ISODateTimeFormat.dateTimeNoMillis());
        String toString = to.toString(ISODateTimeFormat.dateTimeNoMillis());

        Object[] vars = { getExternalAccountId(), fromString, toString, limitToLatest, "ACTUAL" };
        System.err.println("vars: ");
        for (Object o : vars) {
            System.err.println(o.toString());
        }

        ResponseEntity<MeterReadings> meterReadings;
        try {
            meterReadings = restTemplate.exchange(TendrilUrls.GET_METER_READINGS_URL, HttpMethod.GET, requestEntity,
                    MeterReadings.class, vars);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }

        System.err.println(meterReadings.getBody());
        return meterReadings.getBody();
    }

    public MeterReadings fetchMeterReadingsRange(DateTime from, DateTime to) throws TendrilException {
        return fetchMeterReadings(from, to, 100, Source.ACTUAL);
    }

    public PricingProgram fetchPricingProgram() throws TendrilException {

        ResponseEntity<PricingProgram> pricingSchedule;
        try {
            pricingSchedule = restTemplate.exchange(TendrilUrls.GET_PRICING_PROGRAM_URL, HttpMethod.GET, requestEntity,
                    PricingProgram.class);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }
        return pricingSchedule.getBody();

    }

    // This does not currently work- API documentation is inconsistent
    public PricingSchedule fetchPricingSchedule(DateTime from, DateTime to) throws TendrilException {
        Object[] vars = { from.toString(ISODateTimeFormat.dateTimeNoMillis()),
                to.toString(ISODateTimeFormat.dateTimeNoMillis()) };

        ResponseEntity<PricingSchedule> response;
        try {
            response = restTemplate.exchange(TendrilUrls.GET_PRICING_SCHEDULE_URL, HttpMethod.GET, requestEntity,
                    PricingSchedule.class, vars);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }
        // System.err.println(response.getBody());
        return response.getBody();
    }

    private String fetchThermostatDeviceRequestId() throws TendrilException {
        fetchThermostatDataRequest gtdr = new fetchThermostatDataRequest();
        gtdr.setDeviceId(getTstat().getDeviceId());
        gtdr.setLocationId(getLocationId());
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        String gtdrString = "<getThermostatDataRequest xmlns=\"http://platform.tendrilinc.com/tnop/extension/ems\""
                + " deviceId=\"001db700000246f5\"" + " locationId=\"74\">" + "</getThermostatDataRequest>";

        System.err.println("gtdr: " + gtdrString);
        HttpEntity<String> requestEntity = new HttpEntity<String>(gtdrString, requestHeaders);

        fetchThermostatDataRequest gtdrResponse = null;
        try {
            ResponseEntity<fetchThermostatDataRequest> response = restTemplate.exchange(
                    TendrilUrls.POST_DEVICE_ACTION_URL, HttpMethod.POST, requestEntity,
                    fetchThermostatDataRequest.class);

            gtdrResponse = response.getBody();
            System.err.println(gtdrResponse);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }
        return gtdrResponse.getRequestId();
    }

    public User fetchUser() throws TendrilException {
        ResponseEntity<User> response;
        try {
            response = restTemplate.exchange(TendrilUrls.GET_USER_INFO_URL, HttpMethod.GET, requestEntity, User.class);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }
        user = response.getBody();
        return user;
    }

    // throws a 404 even for current-user. See Tendril's "try it" page
    public UserProfile fetchUserProfile() throws TendrilException {

        ResponseEntity<UserProfile> response;
        try {
            response = restTemplate.exchange(TendrilUrls.GET_USER_PROFILE_URL, HttpMethod.GET, requestEntity,
                    UserProfile.class);
        }
        catch (Exception e) {
            throw new TendrilAndroidException(e);
        }
        System.err.println(response.getBody());
        userProfile = response.getBody();
        return userProfile;
    }

    public Devices getDevices() throws TendrilException {
        if (devices == null) devices = fetchDevices();
        return devices;
    }

    public String getExternalAccountId() throws TendrilException {
        if (this.externalAccountId == null) fetchExternalAccountId();
        return externalAccountId.getExternalAccountId();
    }

    private String getLocationId() throws TendrilException {
        if (user == null) fetchUser();
        return user.getId();
    }

    public fetchThermostatDataRequest getThermostatData() throws TendrilException {
        Object[] vars = { fetchThermostatDeviceRequestId() };
        fetchThermostatDataRequest gtdrResponse = null;
        String requestState = "In Progress";
        boolean firstTry = true;
        while (requestState.equals("In Progress")) {
            try {
                if (!firstTry) {
                    System.err.println("Waiting 2 seconds for device status..");
                    Thread.sleep(2000);
                }
                firstTry = false;
                ResponseEntity<fetchThermostatDataRequest> response = restTemplate.exchange(
                        TendrilUrls.GET_DEVICE_ACTION_DATA, HttpMethod.GET, requestEntity,
                        fetchThermostatDataRequest.class, vars);

                gtdrResponse = response.getBody();
                System.err.println(response.getBody());
                requestState = gtdrResponse.getRequestState();
            }

            catch (HttpClientErrorException e) {
                throw new TendrilAndroidException(e);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return gtdrResponse;
    }

    public Device getTstat() throws TendrilException {
        if (tstat == null) {
            for (Device d : getDevices().getDevice()) {
                if (d.getCategory().equalsIgnoreCase(TendrilUrls.THERMOSTAT_CATEGORY)) {
                    tstat = d;
                    System.err.println("tstat: " + tstat);
                    break;
                }
            }
        }
        return tstat;
    }

    public User getUser() throws TendrilException {
        if (user == null) fetchUser();
        return user;
    }

    public UserProfile getUserProfile() throws TendrilException {
        if (this.userProfile == null) {
            this.userProfile = fetchUserProfile();
        }
        return this.userProfile;
    }

    public static void logOut() {
        // TODO: actually log out of Tendril server
        instance = null;
    }

    public SetThermostatDataRequest setTstatSetpoint(Float setpoint) throws TendrilException {

        SetThermostatDataRequest stdr = new SetThermostatDataRequest();
        stdr.setDeviceId(getTstat().getDeviceId());
        stdr.setLocationId(getLocationId());
        stdr.setRequestId("none");
        DeviceData data = new DeviceData();
        data.setMode("Heat");
        data.setSetpoint(setpoint.toString());
        data.setTemperatureScale("Fahrenheit");

        stdr.setData(data);

        HttpEntity<SetThermostatDataRequest> requestEntity = new HttpEntity<SetThermostatDataRequest>(stdr,
                requestHeaders);

        SetThermostatDataRequest stdrResponse = null;
        try {

            ResponseEntity<SetThermostatDataRequest> response = restTemplate.exchange(
                    TendrilUrls.POST_DEVICE_ACTION_URL, HttpMethod.POST, requestEntity, SetThermostatDataRequest.class);

            stdrResponse = response.getBody();
            System.err.println(stdrResponse);
        }
        catch (HttpClientErrorException e) {
            e.printStackTrace();
            System.err.println(e.getResponseBodyAsString());
        }
        return stdrResponse;
    }

    protected RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
package edu.colorado.cs.cirrus.cirrus;

import org.joda.time.DateTime;
import edu.colorado.cs.cirrus.android.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import edu.colorado.cs.cirrus.domain.TendrilException;
import edu.colorado.cs.cirrus.domain.model.CostAndConsumption;
import edu.colorado.cs.cirrus.domain.model.Resolution;
import edu.colorado.cs.cirrus.domain.model.SetThermostatDataRequest;
import edu.colorado.cs.cirrus.domain.model.fetchThermostatDataRequest;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CirrusActivity extends AbstractAsyncTendrilActivity implements
        ActionBar.TabListener {
	
	private LinearLayout costLayout;
	private LinearLayout consumptionLayout;
	private LinearLayout thermostatLayout;
	
	private ActionBar.Tab costTab;
	private ActionBar.Tab consumptionTab;
	private ActionBar.Tab thermostatTab;
	
	private TextView smartHeatText;
	private TextView modeText;
	private EditText setpointText;
	private TextView currentTempText;
	
	private Button setThermostatButton;
	private Button refreshButton;
	
	private DateTime startOf2011 = new DateTime();
	private DateTime endOf2011 = new DateTime();
	
	private PreferenceUtils cirrusPrefs;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startOf2011 = startOf2011.withYear(2011).withMonthOfYear(1)
		        .withDayOfMonth(1).withTimeAtStartOfDay();
		endOf2011 = endOf2011.withYear(2012).withMonthOfYear(1)
		        .withDayOfMonth(1).withTimeAtStartOfDay().minusMillis(1);
		
		cirrusPrefs = new PreferenceUtils(this);
		
		tendril = TendrilTemplate.get();
		
		// Grab layouts
		setContentView(R.layout.cirrus_all_layout);
		costLayout = (LinearLayout) findViewById(R.id.cost);
		consumptionLayout = (LinearLayout) findViewById(R.id.consumption);
		thermostatLayout = (LinearLayout) findViewById(R.id.thermostat);
		modeText = (TextView) findViewById(R.id.thermostat_mode);
		setpointText = (EditText) findViewById(R.id.thermostat_setpoint);
		currentTempText = (TextView) findViewById(R.id.thermostat_current_temp);
		smartHeatText = (TextView) findViewById(R.id.thermostat_smart_heat);
		
		setThermostatButton = (Button) findViewById(R.id.setButton);
		refreshButton = (Button) findViewById(R.id.refreshButton);
		
		// Force changing the setpoint to take numbers
		setpointText.setKeyListener(DigitsKeyListener.getInstance(false, true));
		
		// Hide the keyboard when enter is pressed
		setpointText.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)){
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if(mgr != null){
						mgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
						return true;
					}
				}
				return false;
			}
		});
		
		// Setup actionbar
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		// costTab setup
		costTab = getSupportActionBar().newTab();
		costTab.setText("$$$");
		costTab.setTabListener(this);
		getSupportActionBar().addTab(costTab);
		
		// consumptionTab setup
		consumptionTab = getSupportActionBar().newTab();
		consumptionTab.setText("kWh");
		consumptionTab.setTabListener(this);
		getSupportActionBar().addTab(consumptionTab);
		
		// thermostatTab setup
		thermostatTab = getSupportActionBar().newTab();
		thermostatTab.setText("\u00B0F");
		thermostatTab.setTabListener(this);
		getSupportActionBar().addTab(thermostatTab);
		
		// Set on click listener for buttons
		setThermostatButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				showLoadingProgressDialog();
				try {
					SetThermostatDataRequest stdr = tendril
					        .setTstatSetpoint(Float.parseFloat(setpointText
					                .getEditableText().toString()));
					modeText.setText(stdr.getData().getMode());
					setpointText.setText(stdr.getData().getSetpoint());
					cirrusPrefs.setSmartHeat(false);
					smartHeatText.setText("Disabled");
					
					dismissProgressDialog();
					
					ToastFactory
					        .showToast(getApplicationContext(),
					                "Thermostat has been set to "
					                        + setpointText.getEditableText()
					                                .toString());
					
				} catch (TendrilException e) {
					ToastFactory.showToast(getApplicationContext(), e
					        .getTendrilResponse().getReason());
					Log.e(TAG, e.getTendrilResponse().toString());
				} catch (Exception e) {
					ToastFactory.showToast(getApplicationContext(),
					        "Something went horribly wrong...");
				}
				
				finally {
					dismissProgressDialog();
				
				}
				
			}
		});
		
		refreshButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				thermostatTab.select();
			}
		});
		
		// Set Consumption Layout as default
		consumptionTab.select();
		
		// Start the background service
		startService(new Intent(this, TendrilLocationService.class));
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		tendril.useAccessToken(cirrusPrefs.getAccessToken());
	}
	
	public void onTabReselected(Tab tab, FragmentTransaction transaction) {
	}
	
	public void onTabSelected(Tab tab, FragmentTransaction transaction) {
		// Make all Layouts invisible
		// 8 visibility = gone
		// 4 visibility = invisible
		// 0 visibility = visible
		costLayout.setVisibility(8);
		consumptionLayout.setVisibility(8);
		thermostatLayout.setVisibility(8);
		
		showLoadingProgressDialog();
		// Determine which layout to make visible
		try {
			if (tab.equals(costTab)) {
				costLayout.setVisibility(0);
				
				CostAndConsumption CnC = tendril.fetchCostAndConsumption(
				        Resolution.MONTHLY, startOf2011, endOf2011, 12);
				costLayout.addView(BarGraph.getYearlyCostView(this,
				        CnC.getComponentList(), "Energy Costs for 2011"));
				
			} else if (tab.equals(consumptionTab)) {
				consumptionLayout.setVisibility(0);
				
				CostAndConsumption CnC = tendril.fetchCostAndConsumption(
				        Resolution.MONTHLY, startOf2011, endOf2011, 12);
				consumptionLayout.addView(BarGraph.getYearlyConsumptionView(
				        this, CnC.getComponentList(),
				        "Energy Consumption for 2011"));
				
			} else if (tab.equals(thermostatTab)) {
				thermostatLayout.setVisibility(0);
				
				fetchThermostatDataRequest gtdr = tendril.getThermostatData();
				
				modeText.setText(gtdr.getResult().getMode().toString());
				setpointText.setText(gtdr.getResult().getSetpoint());
				currentTempText.setText(gtdr.getResult().getCurrentTemp());
				if (cirrusPrefs.getSmartHeat() == true)
					smartHeatText.setText("Enabled");
				else
					smartHeatText.setText("Disabled");
				
			}
		} catch (TendrilException e) {
			ToastFactory.showToast(this, e.getTendrilResponse().getReason());
			Log.e(TAG, e.getTendrilResponse().toString());
		} catch (Exception e) {
			ToastFactory.showToast(this, "Something went horribly wrong...");
		}
		
		dismissProgressDialog();
	}
	
	public void onTabUnselected(Tab tab, FragmentTransaction transaction) {
		// Stop any async tasks.... If we can even do that
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Add icon for getting to the settings menu
		menu.add("Settings").setIcon(R.drawable.settingsicon)
		        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Open the settings activity
		if (item.getTitle() == "Settings") {
			Intent intent = new Intent(this, CirrusPreferenceActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, 0);
			return true;
			
		}
		return false;
		
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
			if (resultCode == -1) {
				// We are logged out, return to main menu
				finish();
			}
		}
		
	}
	
}
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera.protocol.server.adapters.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.abdera.Abdera;
import org.apache.abdera.i18n.templates.Template;
import org.apache.abdera.i18n.text.Normalizer;
import org.apache.abdera.i18n.text.Sanitizer;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Link;
import org.apache.abdera.protocol.server.ProviderHelper;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.ResponseContext;
import org.apache.abdera.protocol.server.Target;
import org.apache.abdera.protocol.server.provider.managed.FeedConfiguration;
import org.apache.abdera.protocol.server.provider.managed.ManagedCollectionAdapter;

/**
 * Simple Filesystem Adapter that uses a local directory to store Atompub collection entries. As an extension of the
 * ManagedCollectionAdapter class, the Adapter is intended to be used with implementations of the ManagedProvider and
 * are configured using /abdera/adapter/*.properties files. The *.properties file MUST specify the fs.root property to
 * specify the root directory used by the Adapter.
 */
public class FilesystemAdapter extends ManagedCollectionAdapter {

    private final File root;
    private final static FileSorter sorter = new FileSorter();
    private final static Template paging_template = new Template("?{-join|&|count,page}");

    public FilesystemAdapter(Abdera abdera, FeedConfiguration config) {
        super(abdera, config);
        this.root = getRoot();
    }

    private File getRoot() {
        try {
            String root = (String)config.getProperty("fs.root");
            File file = new File(root);
            if (!file.exists())
                file.mkdirs();
            if (!file.isDirectory())
                throw new RuntimeException("Root must be a directory");
            return file;
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException)e;
            throw new RuntimeException(e);
        }
    }

    private Entry getEntry(File entryFile) {
        if (!entryFile.exists() || !entryFile.isFile())
            throw new RuntimeException();
        try {
            FileInputStream fis = new FileInputStream(entryFile);
            Document<Entry> doc = abdera.getParser().parse(fis);
            Entry entry = doc.getRoot();
            return entry;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addPagingLinks(RequestContext request, Feed feed, int currentpage, int count) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("count", count);
        params.put("page", currentpage + 1);
        String next = paging_template.expand(params);
        next = request.getResolvedUri().resolve(next).toString();
        feed.addLink(next, "next");
        if (currentpage > 0) {
            params.put("page", currentpage - 1);
            String prev = paging_template.expand(params);
            prev = request.getResolvedUri().resolve(prev).toString();
            feed.addLink(prev, "previous");
        }
        params.put("page", 0);
        String current = paging_template.expand(params);
        current = request.getResolvedUri().resolve(current).toString();
        feed.addLink(current, "current");
    }

    private void getEntries(RequestContext request, Feed feed, File root) {
        File[] files = root.listFiles();
        Arrays.sort(files, sorter);
        int length = ProviderHelper.getPageSize(request, "count", 25);
        int offset = ProviderHelper.getOffset(request, "page", length);
        String _page = request.getParameter("page");
        int page = (_page != null) ? Integer.parseInt(_page) : 0;
        addPagingLinks(request, feed, page, length);
        if (offset > files.length)
            return;
        for (int n = offset; n < offset + length && n < files.length; n++) {
            File file = files[n];
            Entry entry = getEntry(file);
            feed.addEntry((Entry)entry.clone());
        }
    }

    public ResponseContext getFeed(RequestContext request) {
        Feed feed = abdera.newFeed();
        feed.setId(config.getServerConfiguration().getServerUri() + "/" + config.getFeedId());
        feed.setTitle(config.getFeedTitle());
        feed.addAuthor(config.getFeedAuthor());
        feed.addLink(config.getFeedUri());
        feed.addLink(config.getFeedUri(), "self");
        feed.setUpdated(new Date());
        getEntries(request, feed, root);
        return ProviderHelper.returnBase(feed.getDocument(), 200, null);
    }

    public ResponseContext deleteEntry(RequestContext request) {
        Target target = request.getTarget();
        String key = target.getParameter("entry");
        File file = getFile(key, false);
        if (file.exists())
            file.delete();
        return ProviderHelper.nocontent();
    }

    public ResponseContext getEntry(RequestContext request) {
        Target target = request.getTarget();
        String key = target.getParameter("entry");
        File file = getFile(key, false);
        Entry entry = getEntry(file);
        if (entry != null)
            return ProviderHelper.returnBase(entry.getDocument(), 200, null);
        else
            return ProviderHelper.notfound(request);
    }

    public ResponseContext postEntry(RequestContext request) {
        if (request.isAtom()) {
            try {
                Entry entry = (Entry)request.getDocument().getRoot().clone();
                String key = createKey(request);
                setEditDetail(request, entry, key);
                File file = getFile(key);
                FileOutputStream out = new FileOutputStream(file);
                entry.writeTo(out);
                String edit = entry.getEditLinkResolvedHref().toString();
                return ProviderHelper.returnBase(entry.getDocument(), 201, null).setLocation(edit);
            } catch (Exception e) {
                return ProviderHelper.badrequest(request);
            }
        } else {
            return ProviderHelper.notsupported(request);
        }
    }

    private void setEditDetail(RequestContext request, Entry entry, String key) throws IOException {
        Target target = request.getTarget();
        String feed = target.getParameter("feed");
        String id = key;
        entry.setEdited(new Date());
        Link link = entry.getEditLink();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("feed", feed);
        params.put("entry", id);
        String href = request.absoluteUrlFor("entry", params);
        if (link == null) {
            entry.addLink(href, "edit");
        } else {
            link.setHref(href);
        }
    }

    private File getFile(String key) {
        return getFile(key, true);
    }

    private File getFile(String key, boolean post) {
        File file = new File(root, key);
        if (post && file.exists())
            throw new RuntimeException("File exists");
        return file;
    }

    private String createKey(RequestContext request) throws IOException {
        String slug = request.getSlug();
        if (slug == null) {
            slug = ((Entry)request.getDocument().getRoot()).getTitle();
        }
        return Sanitizer.sanitize(slug, "", true, Normalizer.Form.D);
    }

    public ResponseContext putEntry(RequestContext request) {
        if (request.isAtom()) {
            try {
                Entry entry = (Entry)request.getDocument().getRoot().clone();
                String key = request.getTarget().getParameter("entry");
                setEditDetail(request, entry, key);
                File file = getFile(key, false);
                FileOutputStream out = new FileOutputStream(file);
                entry.writeTo(out);
                String edit = entry.getEditLinkResolvedHref().toString();
                return ProviderHelper.returnBase(entry.getDocument(), 200, null).setLocation(edit);
            } catch (Exception e) {
                return ProviderHelper.badrequest(request);
            }
        } else {
            return ProviderHelper.notsupported(request);
        }
    }

    private static class FileSorter implements Comparator<File> {
        public int compare(File o1, File o2) {
            return o1.lastModified() > o2.lastModified() ? -1 : o1.lastModified() < o2.lastModified() ? 1 : 0;
        }
    }
}
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera.protocol.client;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.TrustManager;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Base;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.protocol.EntityProvider;
import org.apache.abdera.protocol.Response.ResponseType;
import org.apache.abdera.protocol.client.cache.Cache;
import org.apache.abdera.protocol.client.cache.CacheFactory;
import org.apache.abdera.protocol.client.cache.CachedResponse;
import org.apache.abdera.protocol.client.cache.LRUCache;
import org.apache.abdera.protocol.client.cache.Cache.Disposition;
import org.apache.abdera.protocol.client.util.BaseRequestEntity;
import org.apache.abdera.protocol.client.util.EntityProviderRequestEntity;
import org.apache.abdera.protocol.client.util.MethodHelper;
import org.apache.abdera.protocol.client.util.MultipartRelatedRequestEntity;
import org.apache.abdera.protocol.client.util.SimpleSSLProtocolSocketFactory;
import org.apache.abdera.protocol.error.Error;
import org.apache.abdera.protocol.error.ProtocolException;
import org.apache.abdera.protocol.util.CacheControlUtil;
import org.apache.abdera.util.EntityTag;
import org.apache.abdera.util.Version;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;

/**
 * An Atom Publishing Protocol client.
 */
@SuppressWarnings( {"unchecked", "deprecation"})
public class AbderaClient {

    public static final String DEFAULT_USER_AGENT = Version.APP_NAME + "/" + Version.VERSION;

    public static int DEFAULT_MAX_REDIRECTS = 10;

    protected final Abdera abdera;
    protected final Cache cache;
    private final HttpClient client;

    public AbderaClient() {
        this(new Abdera(), DEFAULT_USER_AGENT);
    }

    /**
     * Create an AbderaClient instance using the specified useragent name
     * 
     * @param useragent
     */
    public AbderaClient(String useragent) {
        this(new Abdera(), useragent);
    }

    /**
     * Create an AbderaClient instance using the specified Abdera instance and useragent name
     * 
     * @param abdera
     * @param useragent
     */
    public AbderaClient(Abdera abdera, String useragent) {
        this(abdera, useragent, initCache(abdera));
    }

    /**
     * Create an AbderaClient instance using the specified Abdera instance and useragent name
     * 
     * @param abdera
     * @param useragent
     */
    public AbderaClient(Abdera abdera, String useragent, Cache cache) {
        this.abdera = abdera;
        this.cache = cache;
        MultiThreadedHttpConnectionManager connManager = new MultiThreadedHttpConnectionManager();
        client = new HttpClient(connManager);
        client.getParams().setParameter(HttpClientParams.USER_AGENT, useragent);
        client.getParams().setBooleanParameter(HttpClientParams.USE_EXPECT_CONTINUE, true);
        client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        setAuthenticationSchemeDefaults();
        setMaximumRedirects(DEFAULT_MAX_REDIRECTS);
    }

    /**
     * Create an Abdera using a preconfigured HttpClient object
     * 
     * @param client An Apache HttpClient object
     */
    public AbderaClient(HttpClient client) {
        this(new Abdera(), client);
    }

    /**
     * Create an Abdera using a preconfigured HttpClient object
     * 
     * @param abdera
     * @param client An Apache HttpClient object
     */
    public AbderaClient(Abdera abdera, HttpClient client) {
        this(abdera, client, initCache(abdera));
    }

    /**
     * Create an Abdera using a preconfigured HttpClient object
     * 
     * @param abdera
     * @param client An Apache HttpClient object
     */
    public AbderaClient(Abdera abdera, HttpClient client, Cache cache) {
        this.abdera = abdera;
        this.cache = cache;
        this.client = client;
        setAuthenticationSchemeDefaults();
        setMaximumRedirects(DEFAULT_MAX_REDIRECTS);
    }

    /**
     * Create an AbderaClient instance using the specified Abdera instance
     * 
     * @param abdera
     */
    public AbderaClient(Abdera abdera) {
        this(abdera, DEFAULT_USER_AGENT);
    }

    /**
     * Create an AbderaClient instance using the specified Abdera instance
     * 
     * @param abdera
     */
    public AbderaClient(Abdera abdera, Cache cache) {
        this(abdera, DEFAULT_USER_AGENT, cache);
    }

    /**
     * Returns the client HTTP cache instance
     */
    public Cache getCache() {
        return cache;
    }

    /**
     * @deprecated The CacheFactory interface is no longer used.
     */
    public Cache initCache(CacheFactory factory) {
        return initCache(abdera);
    }

    /**
     * Initializes the client HTTP cache
     */
    public static Cache initCache(Abdera abdera) {
        return new LRUCache(abdera);
    }

    /**
     * Sends an HTTP HEAD request to the specified URI.
     * 
     * @param uri The request URI
     * @param options The request options
     */
    public ClientResponse head(String uri, RequestOptions options) {
        return execute("HEAD", uri, (RequestEntity)null, options);
    }

    /**
     * Sends an HTTP GET request to the specified URI.
     * 
     * @param uri The request URI
     * @param options The request options
     */
    public ClientResponse get(String uri, RequestOptions options) {
        return execute("GET", uri, (RequestEntity)null, options);
    }

    /**
     * Sends an HTTP POST request to the specified URI.
     * 
     * @param uri The request URI
     * @param provider An EntityProvider implementation providing the payload of the request
     * @param options The request options
     */
    public ClientResponse post(String uri, EntityProvider provider, RequestOptions options) {
        return post(uri, new EntityProviderRequestEntity(abdera, provider, options.isUseChunked()), options);
    }

    /**
     * Sends an HTTP POST request to the specified URI.
     * 
     * @param uri The request URI
     * @param entity A RequestEntity object providing the payload of the request
     * @param options The request options
     */
    public ClientResponse post(String uri, RequestEntity entity, RequestOptions options) {
        return execute("POST", uri, entity, options);
    }

    /**
     * Sends an HTTP POST request to the specified URI.
     * 
     * @param uri The request URI
     * @param in An InputStream providing the payload of the request
     * @param options The request options
     */
    public ClientResponse post(String uri, InputStream in, RequestOptions options) {
        return execute("POST", uri, new InputStreamRequestEntity(in), options);
    }

    /**
     * Sends an HTTP POST request to the specified URI.
     * 
     * @param uri The request URI
     * @param base An Abdera FOM Document or Element object providing the payload of the request
     * @param options The request options
     */
    public ClientResponse post(String uri, Base base, RequestOptions options) {
        if (base instanceof Document) {
            Document d = (Document)base;
            if (options.getSlug() == null && d.getSlug() != null)
                options.setSlug(d.getSlug());
        }
        return execute("POST", uri, new BaseRequestEntity(base, options.isUseChunked()), options);
    }

    /**
     * Sends an HTTP POST request to the specified URI. It uses the media and entry parameters to create a
     * multipart/related object. If the contentType is not provided this method tries to get it from the type attribute
     * of the entry content.
     * 
     * @param uri The request URI
     * @param entry The entry that will be sent as the first element of the multipart/related object
     * @param media The media object that will be sent as the second element of the multipart/related object
     */
    public ClientResponse post(String uri, Entry entry, InputStream media) {
        return post(uri, entry, media, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP POST request to the specified URI. It uses the media and entry parameters to create a
     * multipart/related object. If the contentType is not provided this method tries to get it from the type attribute
     * of the entry content.
     * 
     * @param uri The request URI
     * @param entry The entry that will be sent as the first element of the multipart/related object
     * @param media The media object that will be sent as the second element of the multipart/related object
     * @param options The request options
     */
    public ClientResponse post(String uri, Entry entry, InputStream media, RequestOptions options) {
        return post(uri, entry, media, null, options);
    }

    /**
     * Sends an HTTP POST request to the specified URI. It uses the media and entry parameters to create a
     * multipart/related object.
     * 
     * @param uri The request URI
     * @param entry The entry that will be sent as the first element of the multipart/related object
     * @param media The media object that will be sent as the second element of the multipart/related object
     * @param contentType the content type of the media object
     * @param options The request options
     */
    public ClientResponse post(String uri, Entry entry, InputStream media, String contentType, RequestOptions options) {
        return execute("POST", uri, new MultipartRelatedRequestEntity(entry, media, contentType), options);
    }

    /**
     * Sends an HTTP PUT request to the specified URI.
     * 
     * @param uri The request URI
     * @param provider An EntityProvider implementation providing the payload of the request
     * @param options The request options
     */
    public ClientResponse put(String uri, EntityProvider provider, RequestOptions options) {
        if (options == null)
            options = getDefaultRequestOptions();
        if (options.isConditionalPut()) {
            EntityTag etag = provider.getEntityTag();
            if (etag != null)
                options.setIfMatch(etag);
            else {
                Date lm = provider.getLastModified();
                if (lm != null)
                    options.setIfUnmodifiedSince(lm);
            }
        }
        return put(uri, new EntityProviderRequestEntity(abdera, provider, options.isUseChunked()), options);
    }

    /**
     * Sends an HTTP PUT request to the specified URI.
     * 
     * @param uri The request URI
     * @param entity A RequestEntity object providing the payload of the request
     * @param options The request options
     */
    public ClientResponse put(String uri, RequestEntity entity, RequestOptions options) {
        return execute("PUT", uri, entity, options);
    }

    /**
     * Sends an HTTP PUT request to the specified URI.
     * 
     * @param uri The request URI
     * @param in An InputStream providing the payload of the request
     * @param options The request options
     */
    public ClientResponse put(String uri, InputStream in, RequestOptions options) {
        return execute("PUT", uri, new InputStreamRequestEntity(in), options);
    }

    /**
     * Sends an HTTP PUT request to the specified URI.
     * 
     * @param uri The request URI
     * @param base A FOM Document or Element providing the payload of the request
     * @param options The request options
     */
    public ClientResponse put(String uri, Base base, RequestOptions options) {
        if (options == null)
            options = getDefaultRequestOptions();
        if (base instanceof Document) {
            Document d = (Document)base;
            if (options.getSlug() == null && d.getSlug() != null)
                options.setSlug(d.getSlug());

            if (options.isConditionalPut()) {
                if (d.getEntityTag() != null)
                    options.setIfMatch(d.getEntityTag());
                else if (d.getLastModified() != null)
                    options.setIfUnmodifiedSince(d.getLastModified());
            }
        }
        return execute("PUT", uri, new BaseRequestEntity(base, options.isUseChunked()), options);
    }

    /**
     * Sends an HTTP DELETE request to the specified URI.
     * 
     * @param uri The request URI
     * @param options The request options
     */
    public ClientResponse delete(String uri, RequestOptions options) {
        return execute("DELETE", uri, (RequestEntity)null, options);
    }

    /**
     * Sends an HTTP HEAD request to the specified URI using the default options
     * 
     * @param uri The request URI
     */
    public ClientResponse head(String uri) {
        return head(uri, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP GET request to the specified URI using the default options
     * 
     * @param uri The request URI
     */
    public ClientResponse get(String uri) {
        return get(uri, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP POST request to the specified URI using the default options
     * 
     * @param uri The request URI
     * @param provider An EntityProvider implementation providing the payload the request
     */
    public ClientResponse post(String uri, EntityProvider provider) {
        return post(uri, provider, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP POST request to the specified URI using the default options
     * 
     * @param uri The request URI
     * @param entity A RequestEntity object providing the payload of the request
     */
    public ClientResponse post(String uri, RequestEntity entity) {
        return post(uri, entity, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP POST request to the specified URI using the default options
     * 
     * @param uri The request URI
     * @param in An InputStream providing the payload of the request
     */
    public ClientResponse post(String uri, InputStream in) {
        return post(uri, in, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP POST request to the specified URI using the default options
     * 
     * @param uri The request URI
     * @param base A FOM Document or Element providing the payload of the request
     */
    public ClientResponse post(String uri, Base base) {
        return post(uri, base, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP PUT request to the specified URI using the default options
     * 
     * @param uri The request URI
     * @param provider An EntityProvider implementation providing the payload of the request
     */
    public ClientResponse put(String uri, EntityProvider provider) {
        return put(uri, provider, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP PUT request to the specified URI using the default options
     * 
     * @param uri The request URI
     * @param entity A RequestEntity object providing the payload of the request
     */
    public ClientResponse put(String uri, RequestEntity entity) {
        return put(uri, entity, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP PUT request to the specified URI using the default options
     * 
     * @param uri The request URI
     * @param in An InputStream providing the payload of the request
     */
    public ClientResponse put(String uri, InputStream in) {
        return put(uri, in, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP PUT request to the specified URI using the default options
     * 
     * @param uri The request URI
     * @param base A FOM Document or Element providing the payload of the request
     */
    public ClientResponse put(String uri, Base base) {
        return put(uri, base, getDefaultRequestOptions());
    }

    /**
     * Sends an HTTP DELETE request to the specified URI using the default options
     * 
     * @param uri The request URI
     */
    public ClientResponse delete(String uri) {
        return delete(uri, getDefaultRequestOptions());
    }

    /**
     * Register a new authentication scheme.
     * 
     * @param name
     * @param scheme
     */
    public static void registerScheme(String name, Class<? extends AuthScheme> scheme) {
        AuthPolicy.registerAuthScheme(name, scheme);
    }

    /**
     * Unregister a specific authentication scheme
     * 
     * @param name The name of the authentication scheme (e.g. "basic", "digest", etc)
     */
    public static void unregisterScheme(String name) {
        AuthPolicy.unregisterAuthScheme(name);
    }

    /**
     * Unregister multiple HTTP authentication schemes
     */
    public static void unregisterScheme(String... names) {
        for (String name : names)
            unregisterScheme(name);
    }

    /**
     * Register the specified TrustManager for SSL support on the default port (443)
     * 
     * @param trustManager The TrustManager implementation
     */
    public static void registerTrustManager(TrustManager trustManager) {
        registerTrustManager(trustManager, 443);
    }

    /**
     * Register the default TrustManager for SSL support on the default port (443)
     */
    public static void registerTrustManager() {
        registerTrustManager(443);
    }

    /**
     * Register the specified TrustManager for SSL support on the specified port
     * 
     * @param trustManager The TrustManager implementation
     * @param port The port
     */
    public static void registerTrustManager(TrustManager trustManager, int port) {
        SimpleSSLProtocolSocketFactory f = new SimpleSSLProtocolSocketFactory(trustManager);
        registerFactory(f, port);
    }

    /**
     * Register the default trust manager on the specified port
     * 
     * @param port The port
     */
    public static void registerTrustManager(int port) {
        SimpleSSLProtocolSocketFactory f = new SimpleSSLProtocolSocketFactory();
        registerFactory(f, port);
    }

    /**
     * Register the specified secure socket factory on the specified port
     */
    public static void registerFactory(SecureProtocolSocketFactory factory, int port) {
        Protocol.registerProtocol("https", new Protocol("https", (ProtocolSocketFactory)factory, port));
    }

    /**
     * Configure the client to use preemptive authentication (HTTP Basic Authentication only)
     */
    public AbderaClient usePreemptiveAuthentication(boolean val) {
        client.getParams().setAuthenticationPreemptive(val);
        return this;
    }

    private boolean useCache(String method, RequestOptions options) {
        return (CacheControlUtil.isIdempotent(method)) && !options.isNoCache()
            && !options.isNoStore()
            && options.getUseLocalCache();
    }

    private boolean mustRevalidate(RequestOptions options, CachedResponse response) {
        if (options.getRevalidateWithAuth()) {
            if (options.getAuthorization() != null)
                return true;
            if (client.getParams().getBooleanParameter(HttpClientParams.PREEMPTIVE_AUTHENTICATION, false))
                return true;
            if (response != null) {
                if (response.isPublic())
                    return false;
            }
        }
        return false;
    }

    /**
     * Sends the specified method request to the specified URI. This can be used to send extension HTTP methods to a
     * server (e.g. PATCH, LOCK, etc)
     * 
     * @param method The HTTP method
     * @param uri The request URI
     * @param base A FOM Document and Element providing the payload for the request
     * @param options The Request Options
     */
    public ClientResponse execute(String method, String uri, Base base, RequestOptions options) {
        return execute(method, uri, new BaseRequestEntity(base), options);
    }

    /**
     * Sends the specified method request to the specified URI. This can be used to send extension HTTP methods to a
     * server (e.g. PATCH, LOCK, etc)
     * 
     * @param method The HTTP method
     * @param uri The request URI
     * @param provider An EntityProvider implementation providing the payload of the request
     * @param options The Request Options
     */
    public ClientResponse execute(String method, String uri, EntityProvider provider, RequestOptions options) {
        if (options == null)
            options = getDefaultRequestOptions();
        return execute(method, uri, new EntityProviderRequestEntity(abdera, provider, options.isUseChunked()), options);
    }

    /**
     * Sends the specified method request to the specified URI. This can be used to send extension HTTP methods to a
     * server (e.g. PATCH, LOCK, etc)
     * 
     * @param method The HTTP method
     * @param uri The request URI
     * @param in An InputStream providing the payload of the request
     * @param options The Request Options
     */
    public ClientResponse execute(String method, String uri, InputStream in, RequestOptions options) {
        RequestEntity re = null;
        try {
            if (options.getContentType() != null) {
                re = new InputStreamRequestEntity(in, options.getContentType().toString());
            } else {
                re = new InputStreamRequestEntity(in);
            }
        } catch (Exception e) {
            re = new InputStreamRequestEntity(in);
        }
        return execute(method, uri, re, options);
    }

    private Disposition getCacheDisposition(boolean usecache,
                                            String uri,
                                            RequestOptions options,
                                            CachedResponse cached_response) {
        Disposition disp = (usecache) ? cache.disposition(uri, options) : Disposition.TRANSPARENT;
        disp =
            (!disp.equals(Disposition.TRANSPARENT) && mustRevalidate(options, cached_response)) ? Disposition.STALE
                : disp;
        return disp;
    }

    /**
     * Sends the specified method request to the specified URI. This can be used to send extension HTTP methods to a
     * server (e.g. PATCH, LOCK, etc)
     * 
     * @param method The HTTP method
     * @param uri The request URI
     * @param entity A RequestEntity object providing the payload for the request
     * @param options The Request Options
     */
    public ClientResponse execute(String method, String uri, RequestEntity entity, RequestOptions options) {
        boolean usecache = useCache(method, options);
        options = options != null ? options : getDefaultRequestOptions();
        try {
            Cache cache = getCache();
            CachedResponse cached_response = cache.get(uri);
            switch (getCacheDisposition(usecache, uri, options, cached_response)) {
                case FRESH: // CACHE HIT: FRESH
                    if (cached_response != null)
                        return checkRequestException(cached_response, options);
                case STALE: // CACHE HIT: STALE
                    // revalidate the cached entry
                    if (cached_response != null) {
                        if (cached_response.getEntityTag() != null)
                            options.setIfNoneMatch(cached_response.getEntityTag().toString());
                        else if (cached_response.getLastModified() != null)
                            options.setIfModifiedSince(cached_response.getLastModified());
                        else
                            options.setNoCache(true);
                    }
                default: // CACHE MISS
                    HttpMethod httpMethod = MethodHelper.createMethod(method, uri, entity, options);
                    client.executeMethod(httpMethod);
                    if (usecache && (httpMethod.getStatusCode() == 304 || httpMethod.getStatusCode() == 412)
                        && cached_response != null)
                        return cached_response;
                    ClientResponse response = new CommonsResponse(abdera, httpMethod);
                    response =
                        options.getUseLocalCache() ? response = cache.update(uri, options, response, cached_response)
                            : response;
                    return checkRequestException(response, options);
            }
        } catch (RuntimeException r) {
            throw r;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private ClientResponse checkRequestException(ClientResponse response, RequestOptions options) {
        if (response == null)
            return response;
        ResponseType type = response.getType();
        if ((type.equals(ResponseType.CLIENT_ERROR) && options.is4xxRequestException()) || (type
            .equals(ResponseType.SERVER_ERROR) && options.is5xxRequestException())) {
            try {
                Document<Element> doc = response.getDocument();
                org.apache.abdera.protocol.error.Error error = null;
                if (doc != null) {
                    Element root = doc.getRoot();
                    if (root instanceof Error) {
                        error = (Error)root;
                    }
                }
                if (error == null)
                    error =
                        org.apache.abdera.protocol.error.Error.create(abdera, response.getStatus(), response
                            .getStatusText());
                error.throwException();
            } catch (ProtocolException pe) {
                throw pe;
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return response;
    }

    /**
     * Get a copy of the default request options
     */
    public RequestOptions getDefaultRequestOptions() {
        return MethodHelper.createDefaultRequestOptions();
    }

    /**
     * Add authentication credentials
     */
    public AbderaClient addCredentials(String target, String realm, String scheme, Credentials credentials)
        throws URISyntaxException {
        String host = AuthScope.ANY_HOST;
        int port = AuthScope.ANY_PORT;
        if (target != null) {
            URI uri = new URI(target);
            host = uri.getHost();
            port = uri.getPort();
        }
        AuthScope scope =
            new AuthScope(host, port, (realm != null) ? realm : AuthScope.ANY_REALM, (scheme != null) ? scheme
                : AuthScope.ANY_SCHEME);
        client.getState().setCredentials(scope, credentials);
        return this;
    }

    /**
     * Configure the client to use the default authentication scheme settings
     */
    public AbderaClient setAuthenticationSchemeDefaults() {
        List authPrefs = AuthPolicy.getDefaultAuthPrefs();
        client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
        return this;
    }

    /**
     * When multiple authentication schemes are supported by a server, the client will automatically select a scheme
     * based on the configured priority. For instance, to tell the client to prefer "digest" over "basic", set the
     * priority by calling setAuthenticationSchemePriority("digest","basic")
     */
    public AbderaClient setAuthenticationSchemePriority(String... scheme) {
        List authPrefs = java.util.Arrays.asList(scheme);
        client.getParams().setParameter(AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
        return this;
    }

    /**
     * Returns the current listing of preferred authentication schemes, in order of preference
     * 
     * @see setAuthenticationSchemePriority
     */
    public String[] getAuthenticationSchemePriority() {
        List list = (List)client.getParams().getParameter(AuthPolicy.AUTH_SCHEME_PRIORITY);
        return (String[])list.toArray(new String[list.size()]);
    }

    /**
     * <p>
     * Per http://jakarta.apache.org/commons/httpclient/performance.html
     * </p>
     * <blockquote> Generally it is recommended to have a single instance of HttpClient per communication component or
     * even per application. However, if the application makes use of HttpClient only very infrequently, and keeping an
     * idle instance of HttpClient in memory is not warranted, it is highly recommended to explicitly shut down the
     * multithreaded connection manager prior to disposing the HttpClient instance. This will ensure proper closure of
     * all HTTP connections in the connection pool. </blockquote>
     */
    public AbderaClient teardown() {
        ((MultiThreadedHttpConnectionManager)client.getHttpConnectionManager()).shutdown();
        return this;
    }

    /**
     * Set the maximum number of connections allowed for a single host
     */
    public AbderaClient setMaxConnectionsPerHost(int max) {
        Map<HostConfiguration, Integer> m = new HashMap<HostConfiguration, Integer>();
        m.put(HostConfiguration.ANY_HOST_CONFIGURATION, max);
        client.getHttpConnectionManager().getParams().setParameter(HttpConnectionManagerParams.MAX_HOST_CONNECTIONS, m);
        return this;
    }

    /**
     * Return the maximum number of connections allowed for a single host
     */
    public int getMaxConnectionsPerHost() {
        Map<HostConfiguration, Integer> m =
            (Map<HostConfiguration, Integer>)client.getHttpConnectionManager().getParams()
                .getParameter(HttpConnectionManagerParams.MAX_HOST_CONNECTIONS);
        if (m == null)
            return MultiThreadedHttpConnectionManager.DEFAULT_MAX_HOST_CONNECTIONS;
        Integer i = m.get(HostConfiguration.ANY_HOST_CONFIGURATION);
        return i != null ? i.intValue() : MultiThreadedHttpConnectionManager.DEFAULT_MAX_HOST_CONNECTIONS;
    }

    /**
     * Return the maximum number of connections allowed for the client
     */
    public AbderaClient setMaxConnectionsTotal(int max) {
        client.getHttpConnectionManager().getParams()
            .setIntParameter(HttpConnectionManagerParams.MAX_TOTAL_CONNECTIONS, max);
        return this;
    }

    /**
     * Return the maximum number of connections allowed for the client
     */
    public int getMaxConnectionsTotal() {
        return client.getHttpConnectionManager().getParams()
            .getIntParameter(HttpConnectionManagerParams.MAX_TOTAL_CONNECTIONS,
                             MultiThreadedHttpConnectionManager.DEFAULT_MAX_TOTAL_CONNECTIONS);
    }

    /**
     * Configure the client to use the specified proxy
     */
    public AbderaClient setProxy(String host, int port) {
        client.getHostConfiguration().setProxy(host, port);
        return this;
    }

    /**
     * Specify the auth credentials for the proxy server
     */
    public AbderaClient setProxyCredentials(String host, int port, Credentials credentials) {
        setProxyCredentials(host, port, null, null, credentials);
        return this;
    }

    /**
     * Specify the auth credentials for the proxy server
     */
    public AbderaClient setProxyCredentials(String host, int port, String realm, String scheme, Credentials credentials) {
        host = host != null ? host : AuthScope.ANY_HOST;
        port = port > -1 ? port : AuthScope.ANY_PORT;
        AuthScope scope =
            new AuthScope(host, port, realm != null ? realm : AuthScope.ANY_REALM, scheme != null ? scheme
                : AuthScope.ANY_SCHEME);
        client.getState().setProxyCredentials(scope, credentials);
        return this;
    }

    /**
     * Manually add cookies
     */
    public AbderaClient addCookie(String domain, String name, String value) {
        Cookie cookie = new Cookie(domain, name, value);
        client.getState().addCookie(cookie);
        return this;
    }

    /**
     * Manually add cookies
     */
    public AbderaClient addCookie(String domain, String name, String value, String path, Date expires, boolean secure) {
        Cookie cookie = new Cookie(domain, name, value, path, expires, secure);
        client.getState().addCookie(cookie);
        return this;
    }

    /**
     * Manually add cookies
     */
    public AbderaClient addCookie(String domain, String name, String value, String path, int maxAge, boolean secure) {
        Cookie cookie = new Cookie(domain, name, value, path, maxAge, secure);
        client.getState().addCookie(cookie);
        return this;
    }

    /**
     * Manually add cookies
     */
    public AbderaClient addCookies(Cookie cookie) {
        client.getState().addCookie(cookie);
        return this;
    }

    /**
     * Manually add cookies
     */
    public AbderaClient addCookies(Cookie... cookies) {
        client.getState().addCookies(cookies);
        return this;
    }

    /**
     * Get all the cookies
     */
    public Cookie[] getCookies() {
        return client.getState().getCookies();
    }

    /**
     * Get the cookies for a specific domain and path
     */
    public Cookie[] getCookies(String domain, String path) {
        Cookie[] cookies = getCookies();
        List<Cookie> list = new ArrayList<Cookie>();
        for (Cookie cookie : cookies) {
            String test = cookie.getDomain();
            if (test.startsWith("."))
                test = test.substring(1);
            if ((domain.endsWith(test) || test.endsWith(domain)) && (path == null || cookie.getPath().startsWith(path))) {
                list.add(cookie);
            }
        }
        return list.toArray(new Cookie[list.size()]);
    }

    /**
     * Get the cookies for a specific domain
     */
    public Cookie[] getCookies(String domain) {
        return getCookies(domain, null);
    }

    /**
     * Clear the cookies
     */
    public AbderaClient clearCookies() {
        client.getState().clearCookies();
        return this;
    }

    /**
     * Sets the timeout until a connection is etablished. A value of zero means the timeout is not used. The default
     * value is zero.
     */
    public AbderaClient setConnectionTimeout(int timeout) {
        client.getHttpConnectionManager().getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, timeout);
        return this;
    }

    /**
     * Sets the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data. A timeout
     * value of zero is interpreted as an infinite timeout.
     */
    public AbderaClient setSocketTimeout(int timeout) {
        client.getParams().setSoTimeout(timeout);
        return this;
    }

    /**
     * Sets the timeout in milliseconds used when retrieving an HTTP connection from the HTTP connection manager.
     */
    public void setConnectionManagerTimeout(long timeout) {
        client.getParams().setLongParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, timeout);
    }

    /**
     * Return the timeout until a connection is etablished, in milliseconds. A value of zero means the timeout is not
     * used. The default value is zero.
     */
    public int getConnectionTimeout() {
        return client.getHttpConnectionManager().getParams()
            .getIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 0);
    }

    /**
     * Return the socket timeout for the connection in milliseconds A timeout value of zero is interpreted as an
     * infinite timeout.
     */
    public int getSocketTimeout() {
        return client.getParams().getSoTimeout();
    }

    /**
     * Returns the timeout in milliseconds used when retrieving an HTTP connection from the HTTP connection manager.
     */
    public long getConnectionManagerTimeout() {
        return client.getParams().getLongParameter(HttpClientParams.CONNECTION_MANAGER_TIMEOUT, 0);
    }

    /**
     * Determines whether Nagle's algorithm is to be used. The Nagle's algorithm tries to conserve bandwidth by
     * minimizing the number of segments that are sent. When applications wish to decrease network latency and increase
     * performance, they can disable Nagle's algorithm (that is enable TCP_NODELAY). Data will be sent earlier, at the
     * cost of an increase in bandwidth consumption.
     */
    public void setTcpNoDelay(boolean enable) {
        client.getHttpConnectionManager().getParams().setBooleanParameter(HttpConnectionParams.TCP_NODELAY, enable);
    }

    /**
     * Tests if Nagle's algorithm is to be used.
     */
    public boolean getTcpNoDelay() {
        return client.getHttpConnectionManager().getParams().getBooleanParameter(HttpConnectionParams.TCP_NODELAY,
                                                                                 false);
    }

    /**
     * Return the HttpConnectionManagerParams object of the underlying HttpClient. This enables you to configure options
     * not explicitly exposed by the AbderaClient
     */
    public HttpConnectionManagerParams getHttpConnectionManagerParams() {
        return client.getHttpConnectionManager().getParams();
    }

    /**
     * Return the HttpClientParams object of the underlying HttpClient. This enables you to configure options not
     * explicitly exposed by the AbderaClient
     */
    public HttpClientParams getHttpClientParams() {
        return client.getParams();
    }

    /**
     * Set the maximum number of redirects
     */
    public AbderaClient setMaximumRedirects(int redirects) {
        client.getParams().setIntParameter(HttpClientParams.MAX_REDIRECTS, redirects);
        return this;
    }

    /**
     * Get the maximum number of redirects
     */
    public int getMaximumRedirects() {
        return client.getParams().getIntParameter(HttpClientParams.MAX_REDIRECTS, DEFAULT_MAX_REDIRECTS);
    }

    /**
     * Clear all credentials (including proxy credentials)
     */
    public AbderaClient clearCredentials() {
        client.getState().clearCredentials();
        clearProxyCredentials();
        return this;
    }

    /**
     * Clear proxy credentials
     */
    public AbderaClient clearProxyCredentials() {
        client.getState().clearProxyCredentials();
        return this;
    }

    /**
     * Clear all state (cookies, credentials, etc)
     */
    public AbderaClient clearState() {
        client.getState().clear();
        return this;
    }
}
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera.protocol.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.activation.MimeType;

import org.apache.abdera.i18n.rfc4646.Lang;
import org.apache.abdera.i18n.text.Localizer;
import org.apache.abdera.i18n.text.Rfc2047Helper;
import org.apache.abdera.i18n.text.UrlEncoding;
import org.apache.abdera.i18n.text.CharUtils.Profile;
import org.apache.abdera.protocol.Request;
import org.apache.abdera.protocol.util.AbstractRequest;
import org.apache.abdera.protocol.util.CacheControlUtil;
import org.apache.abdera.util.EntityTag;
import org.apache.commons.httpclient.util.DateParseException;
import org.apache.commons.httpclient.util.DateUtil;

/**
 * The RequestOptions class allows a variety of options affecting the execution of the request to be modified.
 */
public class RequestOptions extends AbstractRequest implements Request {

    private boolean noLocalCache = false;
    private boolean revalidateAuth = false;
    private boolean useChunked = false;
    private boolean usePostOverride = false;
    private boolean requestException4xx = false;
    private boolean requestException5xx = false;
    private boolean useExpectContinue = true;
    private boolean useConditional = true;
    private boolean followRedirects = true;

    private final Map<String, String[]> headers = new HashMap<String, String[]>();

    public RequestOptions() {
    }

    /**
     * Create the RequestOptions object with the specified If-Modified-Since header value
     * 
     * @param ifModifiedSince
     */
    public RequestOptions(Date ifModifiedSince) {
        this();
        setIfModifiedSince(ifModifiedSince);
    }

    /**
     * Create the RequestOptions object with the specified If-None-Match header value
     * 
     * @param IfNoneMatch
     */
    public RequestOptions(String ifNoneMatch) {
        this();
        setIfNoneMatch(ifNoneMatch);
    }

    /**
     * Create the RequestOptions object with the specified If-None-Match header value
     * 
     * @param IfNoneMatch
     */
    public RequestOptions(String... ifNoneMatch) {
        this();
        setIfNoneMatch(ifNoneMatch);
    }

    /**
     * Create the RequestOptions object with the specified If-Modified-Since and If-None-Match header values
     * 
     * @param ifModifiedSince
     * @param IfNoneMatch
     */
    public RequestOptions(Date ifModifiedSince, String ifNoneMatch) {
        this();
        setIfModifiedSince(ifModifiedSince);
        setIfNoneMatch(ifNoneMatch);
    }

    /**
     * Create the RequestOptions object with the specified If-Modified-Since and If-None-Match header values
     * 
     * @param ifModifiedSince
     * @param IfNoneMatch
     */
    public RequestOptions(Date ifModifiedSince, String... ifNoneMatch) {
        this();
        setIfModifiedSince(ifModifiedSince);
        setIfNoneMatch(ifNoneMatch);
    }

    /**
     * Create the RequestOptions object
     * 
     * @param no_cache True if the request will indicate that cached responses should not be returned
     */
    public RequestOptions(boolean no_cache) {
        this();
        setNoCache(no_cache);
    }

    private Map<String, String[]> getHeaders() {
        return headers;
    }

    private String combine(String... values) {
        StringBuilder v = new StringBuilder();
        for (String val : values) {
            if (v.length() > 0)
                v.append(", ");
            v.append(val);
        }
        return v.toString();
    }

    /**
     * The difference between this and getNoCache is that this only disables the local cache without affecting the
     * Cache-Control header.
     */
    public boolean getUseLocalCache() {
        return !noLocalCache;
    }

    /**
     * True if the local client cache should be used
     */
    public RequestOptions setUseLocalCache(boolean use_cache) {
        this.noLocalCache = !use_cache;
        return this;
    }

    /**
     * Set the value of the HTTP Content-Type header
     */
    public RequestOptions setContentType(String value) {
        return setHeader("Content-Type", value);
    }

    public RequestOptions setContentLocation(String iri) {
        return setHeader("Content-Location", iri);
    }

    /**
     * Set the value of the HTTP Content-Type header
     */
    public RequestOptions setContentType(MimeType value) {
        return setHeader("Content-Type", value.toString());
    }

    /**
     * Set the value of the HTTP Authorization header
     */
    public RequestOptions setAuthorization(String auth) {
        return setHeader("Authorization", auth);
    }

    /**
     * Set the value of a header using proper encoding of non-ascii characters
     */
    public RequestOptions setEncodedHeader(String header, String charset, String value) {
        return setHeader(header, Rfc2047Helper.encode(value, charset));
    }

    /**
     * Set the values of a header using proper encoding of non-ascii characters
     */
    public RequestOptions setEncodedHeader(String header, String charset, String... values) {
        if (values != null && values.length > 0) {
            for (int n = 0; n < values.length; n++) {
                values[n] = Rfc2047Helper.encode(values[n], charset);
            }
            getHeaders().put(header, new String[] {combine(values)});
        } else {
            removeHeaders(header);
        }
        return this;
    }

    /**
     * Set the value of the specified HTTP header
     */
    public RequestOptions setHeader(String header, String value) {
        return value != null ? setHeader(header, new String[] {value}) : removeHeaders(header);
    }

    /**
     * Set the value of the specified HTTP header
     */
    public RequestOptions setHeader(String header, String... values) {
        if (values != null && values.length > 0) {
            getHeaders().put(header, new String[] {combine(values)});
        } else {
            removeHeaders(header);
        }
        return this;
    }

    /**
     * Set the date value of the specified HTTP header
     */
    public RequestOptions setDateHeader(String header, Date value) {
        return value != null ? setHeader(header, DateUtil.formatDate(value)) : removeHeaders(header);
    }

    /**
     * Similar to setEncodedHeader, but allows for multiple instances of the specified header
     */
    public RequestOptions addEncodedHeader(String header, String charset, String value) {
        return addHeader(header, Rfc2047Helper.encode(value, charset));
    }

    /**
     * Similar to setEncodedHeader, but allows for multiple instances of the specified header
     */
    public RequestOptions addEncodedHeader(String header, String charset, String... values) {
        if (values == null || values.length == 0)
            return this;
        for (int n = 0; n < values.length; n++) {
            values[n] = Rfc2047Helper.encode(values[n], charset);
        }
        List<String> list = Arrays.asList(getHeaders().get(header));
        String value = combine(values);
        if (list != null) {
            if (!list.contains(value))
                list.add(value);
        } else {
            setHeader(header, new String[] {value});
        }
        return this;
    }

    /**
     * Similar to setHeader but allows for multiple instances of the specified header
     */
    public RequestOptions addHeader(String header, String value) {
        if (value != null)
            addHeader(header, new String[] {value});
        return this;
    }

    /**
     * Similar to setHeader but allows for multiple instances of the specified header
     */
    public RequestOptions addHeader(String header, String... values) {
        if (values == null || values.length == 0)
            return this;
        String[] headers = getHeaders().get(header);
        List<String> list = headers != null ? Arrays.asList(headers) : new ArrayList<String>();
        String value = combine(values);
        if (list != null) {
            if (!list.contains(value))
                list.add(value);
        } else {
            setHeader(header, new String[] {value});
        }
        return this;
    }

    /**
     * Similar to setDateHeader but allows for multiple instances of the specified header
     */
    public RequestOptions addDateHeader(String header, Date value) {
        if (value == null)
            return this;
        return addHeader(header, DateUtil.formatDate(value));
    }

    /**
     * Returns the text value of the specified header
     */
    public String getHeader(String header) {
        String[] list = getHeaders().get(header);
        return (list != null && list.length > 0) ? list[0] : null;
    }

    /**
     * Return a listing of text values for the specified header
     */
    public String[] getHeaders(String header) {
        return getHeaders().get(header);
    }

    /**
     * Returns the date value of the specified header
     */
    public Date getDateHeader(String header) {
        String val = getHeader(header);
        try {
            return (val != null) ? DateUtil.parseDate(val) : null;
        } catch (DateParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a listing of header names
     */
    public String[] getHeaderNames() {
        Set<String> names = getHeaders().keySet();
        return names.toArray(new String[names.size()]);
    }

    /**
     * Sets the value of the HTTP If-Match header
     */
    public RequestOptions setIfMatch(String entity_tag) {
        return setIfMatch(new EntityTag(entity_tag));
    }

    /**
     * Sets the value of the HTTP If-Match header
     */
    public RequestOptions setIfMatch(EntityTag entity_tag) {
        return setHeader("If-Match", entity_tag.toString());
    }

    /**
     * Sets the value of the HTTP If-Match header
     */
    public RequestOptions setIfMatch(EntityTag... entity_tags) {
        return setHeader("If-Match", EntityTag.toString(entity_tags));
    }

    /**
     * Sets the value of the HTTP If-Match header
     */
    public RequestOptions setIfMatch(String... entity_tags) {
        return setHeader("If-Match", EntityTag.toString(entity_tags));
    }

    /**
     * Sets the value of the HTTP If-None-Match header
     */
    public RequestOptions setIfNoneMatch(String entity_tag) {
        return setIfNoneMatch(new EntityTag(entity_tag));
    }

    /**
     * Sets the value of the HTTP If-None-Match header
     */
    public RequestOptions setIfNoneMatch(EntityTag entity_tag) {
        return setHeader("If-None-Match", entity_tag.toString());
    }

    /**
     * Sets the value of the HTTP If-None-Match header
     */
    public RequestOptions setIfNoneMatch(EntityTag... entity_tags) {
        return setHeader("If-None-Match", EntityTag.toString(entity_tags));
    }

    /**
     * Sets the value of the HTTP If-None-Match header
     */
    public RequestOptions setIfNoneMatch(String... entity_tags) {
        return setHeader("If-None-Match", EntityTag.toString(entity_tags));
    }

    /**
     * Sets the value of the HTTP If-Modified-Since header
     */
    public RequestOptions setIfModifiedSince(Date date) {
        return setDateHeader("If-Modified-Since", date);
    }

    /**
     * Sets the value of the HTTP If-Unmodified-Since header
     */
    public RequestOptions setIfUnmodifiedSince(Date date) {
        return setDateHeader("If-Unmodified-Since", date);
    }

    /**
     * Sets the value of the HTTP Accept header
     */
    public RequestOptions setAccept(String accept) {
        return setAccept(new String[] {accept});
    }

    /**
     * Sets the value of the HTTP Accept header
     */
    public RequestOptions setAccept(String... accept) {
        return setHeader("Accept", combine(accept));
    }

    public RequestOptions setAcceptLanguage(Locale locale) {
        return setAcceptLanguage(Lang.fromLocale(locale));
    }

    public RequestOptions setAcceptLanguage(Locale... locales) {
        String[] langs = new String[locales.length];
        for (int n = 0; n < locales.length; n++)
            langs[n] = Lang.fromLocale(locales[n]);
        setAcceptLanguage(langs);
        return this;
    }

    /**
     * Sets the value of the HTTP Accept-Language header
     */
    public RequestOptions setAcceptLanguage(String accept) {
        return setAcceptLanguage(new String[] {accept});
    }

    /**
     * Sets the value of the HTTP Accept-Language header
     */
    public RequestOptions setAcceptLanguage(String... accept) {
        return setHeader("Accept-Language", combine(accept));
    }

    /**
     * Sets the value of the HTTP Accept-Charset header
     */
    public RequestOptions setAcceptCharset(String accept) {
        return setAcceptCharset(new String[] {accept});
    }

    /**
     * Sets the value of the HTTP Accept-Charset header
     */
    public RequestOptions setAcceptCharset(String... accept) {
        return setHeader("Accept-Charset", combine(accept));
    }

    /**
     * Sets the value of the HTTP Accept-Encoding header
     */
    public RequestOptions setAcceptEncoding(String accept) {
        return setAcceptEncoding(new String[] {accept});
    }

    /**
     * Sets the value of the HTTP Accept-Encoding header
     */
    public RequestOptions setAcceptEncoding(String... accept) {
        return setHeader("Accept-Encoding", combine(accept));
    }

    /**
     * Sets the value of the Atom Publishing Protocol Slug header
     */
    public RequestOptions setSlug(String slug) {
        if (slug.indexOf((char)10) > -1 || slug.indexOf((char)13) > -1)
            throw new IllegalArgumentException(Localizer.get("SLUG.BAD.CHARACTERS"));
        return setHeader("Slug", UrlEncoding.encode(slug, Profile.ASCIISANSCRLF.filter()));
    }

    /**
     * Sets the value of the HTTP Cache-Control header
     */
    public RequestOptions setCacheControl(String cc) {
        CacheControlUtil.parseCacheControl(cc, this);
        return this;
    }

    /**
     * Remove the specified HTTP header
     */
    public RequestOptions removeHeaders(String name) {
        getHeaders().remove(name);
        return this;
    }

    /**
     * Return the value of the Cache-Control header
     */
    public String getCacheControl() {
        return CacheControlUtil.buildCacheControl(this);
    }

    /**
     * Configure the AbderaClient Side cache to revalidate when using Authorization
     */
    public boolean getRevalidateWithAuth() {
        return revalidateAuth;
    }

    /**
     * Configure the AbderaClient Side cache to revalidate when using Authorization
     */
    public RequestOptions setRevalidateWithAuth(boolean revalidateAuth) {
        this.revalidateAuth = revalidateAuth;
        return this;
    }

    /**
     * Should the request use chunked encoding?
     */
    public boolean isUseChunked() {
        return useChunked;
    }

    /**
     * Set whether the request should use chunked encoding.
     */
    public RequestOptions setUseChunked(boolean useChunked) {
        this.useChunked = useChunked;
        return this;
    }

    /**
     * Set whether the request should use the X-HTTP-Method-Override option
     */
    public RequestOptions setUsePostOverride(boolean useOverride) {
        this.usePostOverride = useOverride;
        return this;
    }

    /**
     * Return whether the request should use the X-HTTP-Method-Override option
     */
    public boolean isUsePostOverride() {
        return this.usePostOverride;
    }

    /**
     * Set whether or not to throw a RequestExeption on 4xx responses
     */
    public RequestOptions set4xxRequestException(boolean v) {
        this.requestException4xx = v;
        return this;
    }

    /**
     * Return true if a RequestException should be thrown on 4xx responses
     */
    public boolean is4xxRequestException() {
        return this.requestException4xx;
    }

    /**
     * Set whether or not to throw a RequestExeption on 5xx responses
     */
    public RequestOptions set5xxRequestException(boolean v) {
        this.requestException5xx = v;
        return this;
    }

    /**
     * Return true if a RequestException should be thrown on 5xx responses
     */
    public boolean is5xxRequestException() {
        return this.requestException5xx;
    }

    /**
     * Set whether or not to use the HTTP Expect-Continue mechanism (enabled by default)
     */
    public RequestOptions setUseExpectContinue(boolean useExpect) {
        this.useExpectContinue = useExpect;
        return this;
    }

    /**
     * Return true if Expect-Continue should be used
     */
    public boolean isUseExpectContinue() {
        return this.useExpectContinue;
    }

    /**
     * True if HTTP Conditional Requests should be used automatically. This only has an effect when putting a Document
     * that has an ETag or Last-Modified date present
     */
    public boolean isConditionalPut() {
        return this.useConditional;
    }

    /**
     * True if HTTP Conditinal Request should be used automatically. This only has an effect when putting a Document
     * that has an ETag or Last-Modified date present
     */
    public RequestOptions setConditionalPut(boolean conditional) {
        this.useConditional = conditional;
        return this;
    }

    /**
     * True if the client should follow redirects automatically
     */
    public boolean isFollowRedirects() {
        return followRedirects;
    }

    /**
     * True if the client should follow redirects automatically
     */
    public RequestOptions setFollowRedirects(boolean followredirects) {
        this.followRedirects = followredirects;
        return this;
    }
}
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera.factory;

import javax.activation.MimeType;
import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.Base;
import org.apache.abdera.model.Categories;
import org.apache.abdera.model.Category;
import org.apache.abdera.model.Collection;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Control;
import org.apache.abdera.model.DateTime;
import org.apache.abdera.model.Div;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.model.Generator;
import org.apache.abdera.model.IRIElement;
import org.apache.abdera.model.Link;
import org.apache.abdera.model.Person;
import org.apache.abdera.model.Service;
import org.apache.abdera.model.Source;
import org.apache.abdera.model.Text;
import org.apache.abdera.model.Workspace;
import org.apache.abdera.parser.Parser;

/**
 * The Factory interface is the primary means by which Feed Object Model instances are built. Factories are specific to
 * parser implementations. Users will generally not have to know anything about the Factory implementation, which will
 * be automatically selected based on the Abdera configuration options.
 */
public interface Factory {

    /**
     * Create a new Parser instance.
     * 
     * @return A new instance of the Parser associated with this Factory
     */
    Parser newParser();

    /**
     * Create a new Document instance with a root Element of type T.
     * 
     * @return A new instance of a Document
     */
    <T extends Element> Document<T> newDocument();

    /**
     * Create a new Service element.
     * 
     * @return A newly created Service element
     */
    Service newService();

    /**
     * Create a new Service element as a child of the given Base.
     * 
     * @param parent The element or document to which the new Service should be added as a child
     * @return A newly created Service element
     */
    Service newService(Base parent);

    /**
     * Create a new Workspace element.
     * 
     * @return A newly created Workspace element
     */
    Workspace newWorkspace();

    /**
     * Create a new Workspace element as a child of the given Element.
     * 
     * @param parent The element to which the new Workspace should be added as a child
     * @return A newly created Workspace element
     */
    Workspace newWorkspace(Element parent);

    /**
     * Create a new Collection element.
     * 
     * @return A newly created Collection element
     */
    Collection newCollection();

    /**
     * Create a new Collection element as a child of the given Element.
     * 
     * @param parent The element to which the new Collection should be added as a child
     * @return A newly created Collection element
     */
    Collection newCollection(Element parent);

    /**
     * Create a new Feed element. A new Document containing the Feed will be created automatically
     * 
     * @return A newly created Feed element.
     */
    Feed newFeed();

    /**
     * Create a new Feed element as a child of the given Base.
     * 
     * @param parent The element or document to which the new Feed should be added as a child
     * @return A newly created Feed element
     */
    Feed newFeed(Base parent);

    /**
     * Create a new Entry element. A new Document containing the Entry will be created automatically
     * 
     * @return A newly created Entry element
     */
    Entry newEntry();

    /**
     * Create a new Entry element as a child of the given Base.
     * 
     * @param parent The element or document to which the new Entry should be added as a child
     * @return A newly created Entry element
     */
    Entry newEntry(Base parent);

    /**
     * Create a new Category element.
     * 
     * @return A newly created Category element
     */
    Category newCategory();

    /**
     * Create a new Category element as a child of the given Element.
     * 
     * @param parent The element to which the new Category should be added as a child
     * @return A newly created Category element
     */
    Category newCategory(Element parent);

    /**
     * Create a new Content element.
     * 
     * @return A newly created Content element with type="text"
     */
    Content newContent();

    /**
     * Create a new Content element of the given Content.Type.
     * 
     * @param type The Content.Type for the newly created Content element.
     * @return A newly created Content element using the specified type
     */
    Content newContent(Content.Type type);

    /**
     * Create a new Content element of the given Content.Type as a child of the given Element.
     * 
     * @param type The Content.Type for the newly created Content element.
     * @param parent The element to which the new Content should be added as a child
     * @return A newly created Content element using the specified type
     */
    Content newContent(Content.Type type, Element parent);

    /**
     * Create a new Content element of the given MediaType.
     * 
     * @param mediaType The MIME media type to be specified by the type attribute
     * @return A newly created Content element using the specified MIME type
     */
    Content newContent(MimeType mediaType);

    /**
     * Create a new Content element of the given MediaType as a child of the given Element.
     * 
     * @param mediaType The MIME media type to be specified by the type attribute
     * @param parent The element to which the new Content should be added as a child
     * @return A newly created Content element using the specified mediatype.
     */
    Content newContent(MimeType mediaType, Element parent);

    /**
     * Create a new published element.
     * 
     * @return A newly created atom:published element
     */
    DateTime newPublished();

    /**
     * Create a new published element as a child of the given Element.
     * 
     * @param parent The element to which the new Published element should be added as a child
     * @return A newly created atom:published element
     */
    DateTime newPublished(Element parent);

    /**
     * Create a new updated element.
     * 
     * @return A newly created atom:updated element
     */
    DateTime newUpdated();

    /**
     * create a new updated element as a child of the given Element.
     * 
     * @param parent The element to which the new Updated element should be added as a child
     * @return A newly created atom:updated element
     */
    DateTime newUpdated(Element parent);

    /**
     * Create a new app:edited element. The app:edited element is defined by the Atom Publishing Protocol specification
     * for use in atom:entry elements created and edited using that protocol. The element should only ever appear as a
     * child of atom:entry.
     * 
     * @return A newly created app:edited element
     */
    DateTime newEdited();

    /**
     * Create a new app:edited element. The app:edited element is defined by the Atom Publishing Protocol specification
     * for use in atom:entry elements created and edited using that protocol. The element should only ever appear as a
     * child of atom:entry.
     * 
     * @param parent The element to which the new Edited element should be added as a child
     * @return A newly created app:edited element
     */
    DateTime newEdited(Element parent);

    /**
     * Create a new DateTime element with the given QName as a child of the given Element. RFC4287 provides the abstract
     * Atom Date Construct as a reusable component. Any extension element whose value is a Date/Time SHOULD reuse this
     * construct to maintain consistency with the base specification.
     * 
     * @param qname The XML QName of the Atom Date element to create
     * @param parent The element to which the new Atom Date element should be added as a child
     * @return The newly created Atom Date Construct element
     */
    DateTime newDateTime(QName qname, Element parent);

    /**
     * Create a new Generator with Abdera's default name and version.
     * 
     * @return A newly created and pre-populated atom:generator element
     */
    Generator newDefaultGenerator();

    /**
     * Create a new Generator using Abdera's default name and version as a child of the given Element.
     * 
     * @param parent The element to which the new Generator element should be added as a child
     * @return A newly created and pre-populated atom:generator element
     */
    Generator newDefaultGenerator(Element parent);

    /**
     * Create a new Generator element.
     * 
     * @return A newly created atom:generator element
     */
    Generator newGenerator();

    /**
     * Create a new Generator element as a child of the given Element.
     * 
     * @param parent The element to which the new Generator element should be added as a child
     * @return A newly creatd atom:generator element
     */
    Generator newGenerator(Element parent);

    /**
     * Create a new id element.
     * 
     * @return A newly created atom:id element
     */
    IRIElement newID();

    /**
     * Create a new id element as a child of the given Element.
     * 
     * @param parent The element to which the new ID element should be added as a child
     * @return A newly created atom:id element
     */
    IRIElement newID(Element parent);

    /**
     * Create a new icon element.
     * 
     * @return A newly created atom:icon element
     */
    IRIElement newIcon();

    /**
     * Create a new icon element as a child of the given Element.
     * 
     * @param parent The element to which the new Icon element should be added as a child
     * @return A newly created atom:icon element
     */
    IRIElement newIcon(Element parent);

    /**
     * Create a new logo element.
     * 
     * @return A newly created atom:logo element
     */
    IRIElement newLogo();

    /**
     * Create a new logo element as a child of the given Element.
     * 
     * @param parent The element to which the new Logo element should be added as a child
     * @return A newly created atom:logo element
     */
    IRIElement newLogo(Element parent);

    /**
     * Create a new uri element.
     * 
     * @return A newly created atom:uri element
     */
    IRIElement newUri();

    /**
     * Create a new uri element as a child of the given Element.
     * 
     * @param parent The element to which the new URI element should be added as a child
     * @return A newly created atom:uri element
     */
    IRIElement newUri(Element parent);

    /**
     * Create a new IRI element with the given QName as a child of the given Element.
     * 
     * @param qname The XML QName of the new IRI element
     * @param parent The element to which the new generic IRI element should be added as a child
     * @return A newly created element whose text value can be an IRI
     */
    IRIElement newIRIElement(QName qname, Element parent);

    /**
     * Create a new Link element.
     * 
     * @return A newly created atom:link element
     */
    Link newLink();

    /**
     * Create a new Link element as a child of the given Element.
     * 
     * @param parent The element to which the new Link element should be added as a child
     * @return A newly created atom:uri element
     */
    Link newLink(Element parent);

    /**
     * Create a new author element.
     * 
     * @return A newly created atom:author element
     */
    Person newAuthor();

    /**
     * Create a new author element as a child of the given Element.
     * 
     * @param parent The element to which the new Author element should be added as a child
     * @return A newly created atom:author element
     */
    Person newAuthor(Element parent);

    /**
     * Create a new contributor element.
     * 
     * @return A newly created atom:contributor element
     */
    Person newContributor();

    /**
     * Create a new contributor element as a child of the given Element.
     * 
     * @param parent The element to which the new Contributor element should be added as a child
     * @return A newly created atom:contributor element
     */
    Person newContributor(Element parent);

    /**
     * Create a new Person element with the given QName as a child of the given Element. RFC4287 provides the abstract
     * Atom Person Construct to represent people and other entities within an Atom Document. Extensions that wish to
     * represent people SHOULD reuse this construct.
     * 
     * @param qname The XML QName of the newly created Person element
     * @param parent The element to which the new Person element should be added as a child
     * @return A newly created Atom Person Construct element
     */
    Person newPerson(QName qname, Element parent);

    /**
     * Create a new Source element.
     * 
     * @return A newly created atom:source element
     */
    Source newSource();

    /**
     * Create a new Source element as a child of the given Element.
     * 
     * @param parent The element to which the new Source element should be added as a child
     * @return A newly created atom:source element
     */
    Source newSource(Element parent);

    /**
     * Create a new Text element with the given QName and Text.Type. RFC4287 provides the abstract Text Construct to
     * represent simple Text, HTML or XHTML within a document. This construct is used by Atom core elements like
     * atom:title, atom:summary, atom:rights, atom:subtitle, etc and SHOULD be reused by extensions that need a way of
     * embedding text in a document.
     * 
     * @param qname The XML QName of the Text element to create
     * @param type The type of text (plain text, HTML or XHTML)
     * @return A newly created Atom Text Construct element
     */
    Text newText(QName qname, Text.Type type);

    /**
     * Create a new Text element with the given QName and Text.Type as a child of the given Element.
     * 
     * @param qname The XML QName of the Text element to create
     * @param type The type of text (plain text, HTML or XHTML)
     * @param parent The element to which the new Updated element should be added as a child
     * @return A newly created Atom Text Construct element
     */
    Text newText(QName qname, Text.Type type, Element parent);

    /**
     * Create a new title element.
     * 
     * @return A newly created atom:title element
     */
    Text newTitle();

    /**
     * Create a new title element as a child of the given Element.
     * 
     * @param parent The element to which the new Title element should be added as a child
     * @return A newly created atom:title element
     */
    Text newTitle(Element parent);

    /**
     * Create a new title element with the given Text.Type.
     * 
     * @param type The type of text used in the title (plain text, HTML, XHTML)
     * @return A newly created atom:title element
     */
    Text newTitle(Text.Type type);

    /**
     * Create a new title element with the given Text.Type as a child of the given Element.
     * 
     * @param type The type of text used in the title (plain text, HTML, XHTML)
     * @param parent The element to which the new Updated element should be added as a child
     * @return A newly created atom:title element
     */
    Text newTitle(Text.Type type, Element parent);

    /**
     * Create a new subtitle element.
     * 
     * @return A newly created atom:subtitle element
     */
    Text newSubtitle();

    /**
     * Create a new subtitle element as a child of the given Element.
     * 
     * @param parent The element to which the new Subtitle element should be added as a child
     * @return A newly created atom:subtitle element
     */
    Text newSubtitle(Element parent);

    /**
     * Create a new subtitle element with the given Text.Type.
     * 
     * @param type The type of text used in the subtitle (plain text, HTML, XHTML)
     * @return A newly created atom:subtitle element
     */
    Text newSubtitle(Text.Type type);

    /**
     * Create a new subtitle element with the given Text.Type as a child of the given Element.
     * 
     * @param type The type of text used i the subtitle (plain text, HTML, XHTML)
     * @param parent The element to which the new Subtitle element should be added as a child
     * @return A newly created atom:subtitle element
     */
    Text newSubtitle(Text.Type type, Element parent);

    /**
     * Create a new summary element.
     * 
     * @return A newly created atom:summary element
     */
    Text newSummary();

    /**
     * Create a new summary element as a child of the given Element.
     * 
     * @param parent The element to which the new Summary element should be added as a child
     * @return A newly created atom:summary element
     */
    Text newSummary(Element parent);

    /**
     * Create a new summary element with the given Text.Type.
     * 
     * @param type The type of text used in the summary (plain text, HTML, XHTML)
     * @return A newly created atom:summary element
     */
    Text newSummary(Text.Type type);

    /**
     * Create a new summary element with the given Text.Type as a child of the given Element.
     * 
     * @param type The type of text used in the summary (plain text, HTML, XHTML)
     * @param parent The element to which the new Summary element should be added as a child
     * @return A newly created atom:summary element
     */
    Text newSummary(Text.Type type, Element parent);

    /**
     * Create a new rights element.
     * 
     * @return A newly created atom:rights element
     */
    Text newRights();

    /**
     * Create a new rights element as a child of the given Element.
     * 
     * @param parent The element to which the new Rights element should be added as a child
     * @return A newly created atom:rights element
     */
    Text newRights(Element parent);

    /**
     * Create a new rights element with the given Text.Type.
     * 
     * @param type The type of text used in the Rights (plain text, HTML, XHTML)
     * @return A newly created atom:rights element
     */
    Text newRights(Text.Type type);

    /**
     * Create a new rights element with the given Text.Type as a child of the given Element.
     * 
     * @param type The type of text used in the Rights (plain text, HTML, XHTML)
     * @param parent The element to which the new Rights element should be added as a child
     * @return A newly created atom:rights element
     */
    Text newRights(Text.Type type, Element parent);

    /**
     * Create a new name element.
     * 
     * @return A newly created atom:name element
     */
    Element newName();

    /**
     * Create a new name element as a child of the given Element.
     * 
     * @param parent The element to which the new Name element should be added as a child
     * @return A newly created atom:summary element
     */
    Element newName(Element parent);

    /**
     * Create a new email element.
     * 
     * @return A newly created atom:email element
     */
    Element newEmail();

    /**
     * Create a new email element as a child of the given Element.
     * 
     * @param parent The element to which the new Email element should be added as a child
     * @return A newly created atom:email element
     */
    Element newEmail(Element parent);

    /**
     * Create a new Element with the given QName.
     * 
     * @return A newly created element
     */
    <T extends Element> T newElement(QName qname);

    /**
     * Create a new Element with the given QName as a child of the given Base.
     * 
     * @param qname The XML QName of the element to create
     * @param parent The element or document to which the new element should be added as a child
     * @return A newly created element
     */
    <T extends Element> T newElement(QName qname, Base parent);

    /**
     * Create a new extension element with the given QName.
     * 
     * @param qname The XML QName of the element to create
     * @return A newly created element
     */
    <T extends Element> T newExtensionElement(QName qname);

    /**
     * Create a new extension element with the given QName as a child of the given Base.
     * 
     * @param qname The XML QName of the element to create
     * @param parent The element or document to which the new element should be added as a child
     * @return A newly created element
     */
    <T extends Element> T newExtensionElement(QName qname, Base parent);

    /**
     * Create a new Control element. The app:control element is introduced by the Atom Publishing Protocol as a means of
     * allowing publishing clients to provide metadata to a server affecting the way an entry is published. The control
     * element SHOULD only ever appear as a child of the atom:entry and MUST only ever appear once.
     * 
     * @return A newly app:control element
     */
    Control newControl();

    /**
     * Create a new Control element as a child of the given Element.
     * 
     * @param parent The element to which the new Control element should be added as a child
     * @return A newly app:control element
     */
    Control newControl(Element parent);

    /**
     * Create a new Div element.
     * 
     * @return A newly xhtml:div element
     */
    Div newDiv();

    /**
     * Create a new Div element as a child of the given Base.
     * 
     * @param parent The element or document to which the new XHTML div element should be added as a child
     * @return A newly xhtml:div element
     */
    Div newDiv(Base parent);

    /**
     * Registers an extension factory for this Factory instance only
     * 
     * @param extensionFactory An ExtensionFactory instance
     */
    Factory registerExtension(ExtensionFactory extensionFactory);

    /**
     * Create a new Categories element. The app:categories element is introduced by the Atom Publishing Protocol as a
     * means of providing a listing of atom:category's that can be used by entries in a collection.
     * 
     * @return A newly app:categories element
     */
    Categories newCategories();

    /**
     * Create a new Categories element. The app:categories element is introduced by the Atom Publishing Protocol as a
     * means of providing a listing of atom:category's that can be used by entries in a collection.
     * 
     * @param parent The element or document to which the new Categories element should be added as a child
     * @return A newly app:categories element
     */
    Categories newCategories(Base parent);

    /**
     * Generate a new random UUID URI
     */
    String newUuidUri();

    /**
     * Get the Abdera instance for this factory
     */
    Abdera getAbdera();

    /**
     * Get the mime type for the specified extension element / document
     */
    <T extends Base> String getMimeType(T base);

    /**
     * Returns a listing of extension factories registered
     */
    String[] listExtensionFactories();
}
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera.factory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.channels.WritableByteChannel;
import java.util.Date;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.i18n.rfc4646.Lang;
import org.apache.abdera.model.Base;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Content.Type;
import org.apache.abdera.util.AbstractStreamWriter;

/**
 * StreamBuilder is a special implementation of the StreamWriter interface that can be used to create Feed Object Model
 * instances using the StreamWriter interface. StreamBuilder provides an additional method (getBase) for returning the
 * FOM Base element that was built. The StreamWriter methods indent(), flush(), close(), setWriter(), setInputStream,
 * setAutoclose(), setAutoflush(), setAutoIndent(), and setChannel() have no effect on this StreamWriter implementation
 * 
 * <pre>
 * StreamBuilder sw = new StreamBuilder();
 * Entry entry =
 *     sw.startElement(Constants.ENTRY).writeBase(&quot;http://example.org&quot;).writeLanguage(&quot;en-US&quot;)
 *         .writeId(&quot;http://example.org&quot;).writeTitle(&quot;testing&quot;).writeUpdated(new Date()).endElement().getBase();
 * entry.writeTo(System.out);
 * </pre>
 */
@SuppressWarnings("unchecked")
public class StreamBuilder extends AbstractStreamWriter {

    private final Abdera abdera;
    private Base root = null;
    private Base current = null;

    public StreamBuilder() {
        this(Abdera.getInstance());
    }

    public StreamBuilder(Abdera abdera) {
        super(abdera, "fom");
        this.abdera = abdera;
    }

    public <T extends Base> T getBase() {
        return (T)root;
    }

    public StreamBuilder startDocument(String xmlversion, String charset) {
        if (root != null)
            throw new IllegalStateException("Document already started");
        root = abdera.getFactory().newDocument();
        ((Document)root).setCharset(charset);
        current = root;
        return this;
    }

    public StreamBuilder startDocument(String xmlversion) {
        return startDocument(xmlversion, "UTF-8");
    }

    private static QName getQName(String name, String namespace, String prefix) {
        if (prefix != null)
            return new QName(namespace, name, prefix);
        else if (namespace != null)
            return new QName(namespace, name);
        else
            return new QName(name);
    }

    public StreamBuilder startElement(String name, String namespace, String prefix) {
        current = abdera.getFactory().newElement(getQName(name, namespace, prefix), current);
        if (root == null)
            root = current;
        return this;
    }

    public StreamBuilder endElement() {
        current = current instanceof Element ? ((Element)current).getParentElement() : null;
        return this;
    }

    public StreamBuilder writeAttribute(String name, String namespace, String prefix, String value) {
        if (!(current instanceof Element))
            throw new IllegalStateException("Not currently an element");
        ((Element)current).setAttributeValue(getQName(name, namespace, prefix), value);
        return this;
    }

    public StreamBuilder writeComment(String value) {
        current.addComment(value);
        return this;
    }

    public StreamBuilder writeElementText(String value) {
        if (!(current instanceof Element))
            throw new IllegalStateException("Not currently an element");
        Element element = (Element)current;
        String text = element.getText();
        element.setText(text + value);
        return this;
    }

    public StreamBuilder writeId() {
        return writeId(abdera.getFactory().newUuidUri());
    }

    public StreamBuilder writePI(String value) {
        return writePI(value, null);
    }

    public StreamBuilder writePI(String value, String target) {
        if (!(current instanceof Document))
            throw new IllegalStateException("Not currently a document");
        ((Document)current).addProcessingInstruction(target != null ? target : "", value);
        return this;
    }

    public void close() throws IOException {
    }

    public StreamBuilder flush() {
        // non-op
        return this;
    }

    public StreamBuilder indent() {
        // non-op
        return this;
    }

    public StreamBuilder setOutputStream(OutputStream out) {
        // non-op
        return this;
    }

    public StreamBuilder setOutputStream(OutputStream out, String charset) {
        // non-op
        return this;
    }

    public StreamBuilder setWriter(Writer writer) {
        // non-op
        return this;
    }

    public StreamBuilder endAuthor() {
        return (StreamBuilder)super.endAuthor();
    }

    public StreamBuilder endCategories() {
        return (StreamBuilder)super.endCategories();
    }

    public StreamBuilder endCategory() {
        return (StreamBuilder)super.endCategory();
    }

    public StreamBuilder endCollection() {
        return (StreamBuilder)super.endCollection();
    }

    public StreamBuilder endContent() {
        return (StreamBuilder)super.endContent();
    }

    public StreamBuilder endContributor() {
        return (StreamBuilder)super.endContributor();
    }

    public StreamBuilder endControl() {
        return (StreamBuilder)super.endControl();
    }

    public StreamBuilder endDocument() {
        return (StreamBuilder)super.endDocument();
    }

    public StreamBuilder endEntry() {
        return (StreamBuilder)super.endEntry();
    }

    public StreamBuilder endFeed() {
        return (StreamBuilder)super.endFeed();
    }

    public StreamBuilder endGenerator() {
        return (StreamBuilder)super.endGenerator();
    }

    public StreamBuilder endLink() {
        return (StreamBuilder)super.endLink();
    }

    public StreamBuilder endPerson() {
        return (StreamBuilder)super.endPerson();
    }

    public StreamBuilder endService() {
        return (StreamBuilder)super.endService();
    }

    public StreamBuilder endSource() {
        return (StreamBuilder)super.endSource();
    }

    public StreamBuilder endText() {
        return (StreamBuilder)super.endText();
    }

    public StreamBuilder endWorkspace() {
        return (StreamBuilder)super.endWorkspace();
    }

    public StreamBuilder setAutoclose(boolean auto) {
        return (StreamBuilder)super.setAutoclose(auto);
    }

    public StreamBuilder setAutoflush(boolean auto) {
        return (StreamBuilder)super.setAutoflush(auto);
    }

    public StreamBuilder setAutoIndent(boolean indent) {
        return (StreamBuilder)super.setAutoIndent(indent);
    }

    public StreamBuilder setChannel(WritableByteChannel channel, String charset) {
        return (StreamBuilder)super.setChannel(channel, charset);
    }

    public StreamBuilder setChannel(WritableByteChannel channel) {
        return (StreamBuilder)super.setChannel(channel);
    }

    public StreamBuilder startAuthor() {
        return (StreamBuilder)super.startAuthor();
    }

    public StreamBuilder startCategories() {
        return (StreamBuilder)super.startCategories();
    }

    public StreamBuilder startCategories(boolean fixed, String scheme) {
        return (StreamBuilder)super.startCategories(fixed, scheme);
    }

    public StreamBuilder startCategories(boolean fixed) {
        return (StreamBuilder)super.startCategories(fixed);
    }

    public StreamBuilder startCategory(String term, String scheme, String label) {
        return (StreamBuilder)super.startCategory(term, scheme, label);
    }

    public StreamBuilder startCategory(String term, String scheme) {
        return (StreamBuilder)super.startCategory(term, scheme);
    }

    public StreamBuilder startCategory(String term) {
        return (StreamBuilder)super.startCategory(term);
    }

    public StreamBuilder startCollection(String href) {
        return (StreamBuilder)super.startCollection(href);
    }

    public StreamBuilder startContent(String type, String src) {
        return (StreamBuilder)super.startContent(type, src);
    }

    public StreamBuilder startContent(String type) {
        return (StreamBuilder)super.startContent(type);
    }

    public StreamBuilder startContent(Type type, String src) {
        return (StreamBuilder)super.startContent(type, src);
    }

    public StreamBuilder startContent(Type type) {
        return (StreamBuilder)super.startContent(type);
    }

    public StreamBuilder startContributor() {
        return (StreamBuilder)super.startContributor();
    }

    public StreamBuilder startControl() {
        return (StreamBuilder)super.startControl();
    }

    public StreamBuilder startDocument() {
        return (StreamBuilder)super.startDocument();
    }

    public StreamBuilder startElement(QName qname) {
        return (StreamBuilder)super.startElement(qname);
    }

    public StreamBuilder startElement(String name, String namespace) {
        return (StreamBuilder)super.startElement(name, namespace);
    }

    public StreamBuilder startElement(String name) {
        return (StreamBuilder)super.startElement(name);
    }

    public StreamBuilder startEntry() {
        return (StreamBuilder)super.startEntry();
    }

    public StreamBuilder startFeed() {
        return (StreamBuilder)super.startFeed();
    }

    public StreamBuilder startGenerator(String version, String uri) {
        return (StreamBuilder)super.startGenerator(version, uri);
    }

    public StreamBuilder startLink(String iri, String rel, String type, String title, String hreflang, long length) {
        return (StreamBuilder)super.startLink(iri, rel, type, title, hreflang, length);
    }

    public StreamBuilder startLink(String iri, String rel, String type) {
        return (StreamBuilder)super.startLink(iri, rel, type);
    }

    public StreamBuilder startLink(String iri, String rel) {
        return (StreamBuilder)super.startLink(iri, rel);
    }

    public StreamBuilder startLink(String iri) {
        return (StreamBuilder)super.startLink(iri);
    }

    public StreamBuilder startPerson(QName qname) {
        return (StreamBuilder)super.startPerson(qname);
    }

    public StreamBuilder startPerson(String name, String namespace, String prefix) {
        return (StreamBuilder)super.startPerson(name, namespace, prefix);
    }

    public StreamBuilder startPerson(String name, String namespace) {
        return (StreamBuilder)super.startPerson(name, namespace);
    }

    public StreamBuilder startPerson(String name) {
        return (StreamBuilder)super.startPerson(name);
    }

    public StreamBuilder startService() {
        return (StreamBuilder)super.startService();
    }

    public StreamBuilder startSource() {
        return (StreamBuilder)super.startSource();
    }

    public StreamBuilder startText(QName qname, org.apache.abdera.model.Text.Type type) {
        return (StreamBuilder)super.startText(qname, type);
    }

    public StreamBuilder startText(String name, String namespace, String prefix, org.apache.abdera.model.Text.Type type) {
        return (StreamBuilder)super.startText(name, namespace, prefix, type);
    }

    public StreamBuilder startText(String name, String namespace, org.apache.abdera.model.Text.Type type) {
        return (StreamBuilder)super.startText(name, namespace, type);
    }

    public StreamBuilder startText(String name, org.apache.abdera.model.Text.Type type) {
        return (StreamBuilder)super.startText(name, type);
    }

    public StreamBuilder startWorkspace() {
        return (StreamBuilder)super.startWorkspace();
    }

    public StreamBuilder writeAccepts(String... accepts) {
        return (StreamBuilder)super.writeAccepts(accepts);
    }

    public StreamBuilder writeAcceptsEntry() {
        return (StreamBuilder)super.writeAcceptsEntry();
    }

    public StreamBuilder writeAcceptsNothing() {
        return (StreamBuilder)super.writeAcceptsNothing();
    }

    public StreamBuilder writeAttribute(QName qname, Date value) {
        return (StreamBuilder)super.writeAttribute(qname, value);
    }

    public StreamBuilder writeAttribute(QName qname, double value) {
        return (StreamBuilder)super.writeAttribute(qname, value);
    }

    public StreamBuilder writeAttribute(QName qname, int value) {
        return (StreamBuilder)super.writeAttribute(qname, value);
    }

    public StreamBuilder writeAttribute(QName qname, long value) {
        return (StreamBuilder)super.writeAttribute(qname, value);
    }

    public StreamBuilder writeAttribute(QName qname, String value) {
        return (StreamBuilder)super.writeAttribute(qname, value);
    }

    public StreamBuilder writeAttribute(String name, Date value) {
        return (StreamBuilder)super.writeAttribute(name, value);
    }

    public StreamBuilder writeAttribute(String name, double value) {
        return (StreamBuilder)super.writeAttribute(name, value);
    }

    public StreamBuilder writeAttribute(String name, int value) {
        return (StreamBuilder)super.writeAttribute(name, value);
    }

    public StreamBuilder writeAttribute(String name, long value) {
        return (StreamBuilder)super.writeAttribute(name, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, Date value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, double value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, int value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, long value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, String prefix, Date value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, prefix, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, String prefix, double value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, prefix, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, String prefix, int value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, prefix, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, String prefix, long value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, prefix, value);
    }

    public StreamBuilder writeAttribute(String name, String namespace, String value) {
        return (StreamBuilder)super.writeAttribute(name, namespace, value);
    }

    public StreamBuilder writeAttribute(String name, String value) {
        return (StreamBuilder)super.writeAttribute(name, value);
    }

    public StreamBuilder writeAuthor(String name, String email, String uri) {
        return (StreamBuilder)super.writeAuthor(name, email, uri);
    }

    public StreamBuilder writeAuthor(String name) {
        return (StreamBuilder)super.writeAuthor(name);
    }

    public StreamBuilder writeBase(IRI iri) {
        return (StreamBuilder)super.writeBase(iri);
    }

    public StreamBuilder writeBase(String iri) {
        return (StreamBuilder)super.writeBase(iri);
    }

    public StreamBuilder writeCategory(String term, String scheme, String label) {
        return (StreamBuilder)super.writeCategory(term, scheme, label);
    }

    public StreamBuilder writeCategory(String term, String scheme) {
        return (StreamBuilder)super.writeCategory(term, scheme);
    }

    public StreamBuilder writeCategory(String term) {
        return (StreamBuilder)super.writeCategory(term);
    }

    public StreamBuilder writeContent(String type, String value) {
        return (StreamBuilder)super.writeContent(type, value);
    }

    public StreamBuilder writeContent(Type type, DataHandler value) throws IOException {
        return (StreamBuilder)super.writeContent(type, value);
    }

    public StreamBuilder writeContent(Type type, InputStream value) throws IOException {
        return (StreamBuilder)super.writeContent(type, value);
    }

    public StreamBuilder writeContent(Type type, String value) {
        return (StreamBuilder)super.writeContent(type, value);
    }

    public StreamBuilder writeContributor(String name, String email, String uri) {
        return (StreamBuilder)super.writeContributor(name, email, uri);
    }

    public StreamBuilder writeContributor(String name) {
        return (StreamBuilder)super.writeContributor(name);
    }

    public StreamBuilder writeDate(QName qname, Date date) {
        return (StreamBuilder)super.writeDate(qname, date);
    }

    public StreamBuilder writeDate(QName qname, String date) {
        return (StreamBuilder)super.writeDate(qname, date);
    }

    public StreamBuilder writeDate(String name, Date date) {
        return (StreamBuilder)super.writeDate(name, date);
    }

    public StreamBuilder writeDate(String name, String namespace, Date date) {
        return (StreamBuilder)super.writeDate(name, namespace, date);
    }

    public StreamBuilder writeDate(String name, String namespace, String prefix, Date date) {
        return (StreamBuilder)super.writeDate(name, namespace, prefix, date);
    }

    public StreamBuilder writeDate(String name, String namespace, String prefix, String date) {
        return (StreamBuilder)super.writeDate(name, namespace, prefix, date);
    }

    public StreamBuilder writeDate(String name, String namespace, String date) {
        return (StreamBuilder)super.writeDate(name, namespace, date);
    }

    public StreamBuilder writeDate(String name, String date) {
        return (StreamBuilder)super.writeDate(name, date);
    }

    public StreamBuilder writeDraft(boolean draft) {
        return (StreamBuilder)super.writeDraft(draft);
    }

    public StreamBuilder writeEdited(Date date) {
        return (StreamBuilder)super.writeEdited(date);
    }

    public StreamBuilder writeEdited(String date) {
        return (StreamBuilder)super.writeEdited(date);
    }

    public StreamBuilder writeElementText(DataHandler value) throws IOException {
        return (StreamBuilder)super.writeElementText(value);
    }

    public StreamBuilder writeElementText(Date value) {
        return (StreamBuilder)super.writeElementText(value);
    }

    public StreamBuilder writeElementText(double value) {
        return (StreamBuilder)super.writeElementText(value);
    }

    public StreamBuilder writeElementText(InputStream value) throws IOException {
        return (StreamBuilder)super.writeElementText(value);
    }

    public StreamBuilder writeElementText(int value) {
        return (StreamBuilder)super.writeElementText(value);
    }

    public StreamBuilder writeElementText(long value) {
        return (StreamBuilder)super.writeElementText(value);
    }

    public StreamBuilder writeElementText(String format, Object... args) {
        return (StreamBuilder)super.writeElementText(format, args);
    }

    public StreamBuilder writeGenerator(String version, String uri, String value) {
        return (StreamBuilder)super.writeGenerator(version, uri, value);
    }

    public StreamBuilder writeIcon(IRI iri) {
        return (StreamBuilder)super.writeIcon(iri);
    }

    public StreamBuilder writeIcon(String iri) {
        return (StreamBuilder)super.writeIcon(iri);
    }

    public StreamBuilder writeId(IRI iri) {
        return (StreamBuilder)super.writeId(iri);
    }

    public StreamBuilder writeId(String iri) {
        return (StreamBuilder)super.writeId(iri);
    }

    public StreamBuilder writeIRIElement(QName qname, IRI iri) {
        return (StreamBuilder)super.writeIRIElement(qname, iri);
    }

    public StreamBuilder writeIRIElement(QName qname, String iri) {
        return (StreamBuilder)super.writeIRIElement(qname, iri);
    }

    public StreamBuilder writeIRIElement(String name, IRI iri) {
        return (StreamBuilder)super.writeIRIElement(name, iri);
    }

    public StreamBuilder writeIRIElement(String name, String namespace, IRI iri) {
        return (StreamBuilder)super.writeIRIElement(name, namespace, iri);
    }

    public StreamBuilder writeIRIElement(String name, String namespace, String prefix, IRI iri) {
        return (StreamBuilder)super.writeIRIElement(name, namespace, prefix, iri);
    }

    public StreamBuilder writeIRIElement(String name, String namespace, String prefix, String iri) {
        return (StreamBuilder)super.writeIRIElement(name, namespace, prefix, iri);
    }

    public StreamBuilder writeIRIElement(String name, String namespace, String iri) {
        return (StreamBuilder)super.writeIRIElement(name, namespace, iri);
    }

    public StreamBuilder writeIRIElement(String name, String iri) {
        return (StreamBuilder)super.writeIRIElement(name, iri);
    }

    public StreamBuilder writeLanguage(Lang lang) {
        return (StreamBuilder)super.writeLanguage(lang);
    }

    public StreamBuilder writeLanguage(Locale locale) {
        return (StreamBuilder)super.writeLanguage(locale);
    }

    public StreamBuilder writeLanguage(String lang) {
        return (StreamBuilder)super.writeLanguage(lang);
    }

    public StreamBuilder writeLink(String iri, String rel, String type, String title, String hreflang, long length) {
        return (StreamBuilder)super.writeLink(iri, rel, type, title, hreflang, length);
    }

    public StreamBuilder writeLink(String iri, String rel, String type) {
        return (StreamBuilder)super.writeLink(iri, rel, type);
    }

    public StreamBuilder writeLink(String iri, String rel) {
        return (StreamBuilder)super.writeLink(iri, rel);
    }

    public StreamBuilder writeLink(String iri) {
        return (StreamBuilder)super.writeLink(iri);
    }

    public StreamBuilder writeLogo(IRI iri) {
        return (StreamBuilder)super.writeLogo(iri);
    }

    public StreamBuilder writeLogo(String iri) {
        return (StreamBuilder)super.writeLogo(iri);
    }

    public StreamBuilder writePerson(QName qname, String name, String email, String uri) {
        return (StreamBuilder)super.writePerson(qname, name, email, uri);
    }

    public StreamBuilder writePerson(String localname,
                                     String namespace,
                                     String prefix,
                                     String name,
                                     String email,
                                     String uri) {
        return (StreamBuilder)super.writePerson(localname, namespace, prefix, name, email, uri);
    }

    public StreamBuilder writePerson(String localname, String namespace, String name, String email, String uri) {
        return (StreamBuilder)super.writePerson(localname, namespace, name, email, uri);
    }

    public StreamBuilder writePerson(String localname, String name, String email, String uri) {
        return (StreamBuilder)super.writePerson(localname, name, email, uri);
    }

    public StreamBuilder writePersonEmail(String email) {
        return (StreamBuilder)super.writePersonEmail(email);
    }

    public StreamBuilder writePersonName(String name) {
        return (StreamBuilder)super.writePersonName(name);
    }

    public StreamBuilder writePersonUri(String uri) {
        return (StreamBuilder)super.writePersonUri(uri);
    }

    public StreamBuilder writePublished(Date date) {
        return (StreamBuilder)super.writePublished(date);
    }

    public StreamBuilder writePublished(String date) {
        return (StreamBuilder)super.writePublished(date);
    }

    public StreamBuilder writeRights(String value) {
        return (StreamBuilder)super.writeRights(value);
    }

    public StreamBuilder writeRights(org.apache.abdera.model.Text.Type type, String value) {
        return (StreamBuilder)super.writeRights(type, value);
    }

    public StreamBuilder writeSubtitle(String value) {
        return (StreamBuilder)super.writeSubtitle(value);
    }

    public StreamBuilder writeSubtitle(org.apache.abdera.model.Text.Type type, String value) {
        return (StreamBuilder)super.writeSubtitle(type, value);
    }

    public StreamBuilder writeSummary(String value) {
        return (StreamBuilder)super.writeSummary(value);
    }

    public StreamBuilder writeSummary(org.apache.abdera.model.Text.Type type, String value) {
        return (StreamBuilder)super.writeSummary(type, value);
    }

    public StreamBuilder writeText(QName qname, org.apache.abdera.model.Text.Type type, String value) {
        return (StreamBuilder)super.writeText(qname, type, value);
    }

    public StreamBuilder writeText(String name,
                                   String namespace,
                                   String prefix,
                                   org.apache.abdera.model.Text.Type type,
                                   String value) {
        return (StreamBuilder)super.writeText(name, namespace, prefix, type, value);
    }

    public StreamBuilder writeText(String name, String namespace, org.apache.abdera.model.Text.Type type, String value) {
        return (StreamBuilder)super.writeText(name, namespace, type, value);
    }

    public StreamBuilder writeText(String name, org.apache.abdera.model.Text.Type type, String value) {
        return (StreamBuilder)super.writeText(name, type, value);
    }

    public StreamBuilder writeTitle(String value) {
        return (StreamBuilder)super.writeTitle(value);
    }

    public StreamBuilder writeTitle(org.apache.abdera.model.Text.Type type, String value) {
        return (StreamBuilder)super.writeTitle(type, value);
    }

    public StreamBuilder writeUpdated(Date date) {
        return (StreamBuilder)super.writeUpdated(date);
    }

    public StreamBuilder writeUpdated(String date) {
        return (StreamBuilder)super.writeUpdated(date);
    }

    public StreamBuilder setPrefix(String prefix, String uri) {
        if (!(current instanceof Element))
            throw new IllegalStateException("Not currently an element");
        ((Element)current).declareNS(uri, prefix);
        return this;
    }

    public StreamBuilder writeNamespace(String prefix, String uri) {
        return setPrefix(prefix, uri);
    }

}
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera.i18n.iri;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.abdera.i18n.text.CharUtils;
import org.apache.abdera.i18n.text.CharUtils.Profile;
import org.apache.abdera.i18n.text.InvalidCharacterException;
import org.apache.abdera.i18n.text.Nameprep;
import org.apache.abdera.i18n.text.Normalizer;
import org.apache.abdera.i18n.text.UrlEncoding;
import org.apache.abdera.i18n.text.data.UnicodeCharacterDatabase;

public final class IRI implements Serializable, Cloneable {

    private static final long serialVersionUID = -4530530782760282284L;
    protected Scheme _scheme;
    private String scheme;
    private String authority;
    private String userinfo;
    private String host;
    private int port = -1;
    private String path;
    private String query;
    private String fragment;

    private String a_host;
    private String a_fragment;
    private String a_path;
    private String a_query;
    private String a_userinfo;
    private String a_authority;

    public IRI(java.net.URL url) {
        this(url.toString());
    }

    public IRI(java.net.URI uri) {
        this(uri.toString());
    }

    public IRI(String iri) {
        parse(CharUtils.stripBidi(iri));
        init();
    }

    public IRI(String iri, Normalizer.Form nf) throws IOException {
        this(Normalizer.normalize(CharUtils.stripBidi(iri), nf).toString());
    }

    public IRI(String scheme, String userinfo, String host, int port, String path, String query, String fragment) {
        this.scheme = scheme;
        this._scheme = SchemeRegistry.getInstance().getScheme(scheme);
        this.userinfo = userinfo;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
        StringBuilder buf = new StringBuilder();
        buildAuthority(buf, userinfo, host, port);
        this.authority = (buf.length() != 0) ? buf.toString() : null;
        init();
    }

    public IRI(String scheme, String authority, String path, String query, String fragment) {
        this.scheme = scheme;
        this._scheme = SchemeRegistry.getInstance().getScheme(scheme);
        this.authority = authority;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
        parseAuthority();
        init();
    }

    public IRI(String scheme, String host, String path, String fragment) {
        this(scheme, null, host, -1, path, null, fragment);
    }

    IRI(Scheme _scheme,
        String scheme,
        String authority,
        String userinfo,
        String host,
        int port,
        String path,
        String query,
        String fragment) {
        this._scheme = _scheme;
        this.scheme = scheme;
        this.authority = authority;
        this.userinfo = userinfo;
        this.host = host;
        this.port = port;
        this.path = path;
        this.query = query;
        this.fragment = fragment;
        init();
    }

    private void init() {
        if (host != null && host.startsWith("[")) {
            a_host = host;
        } else {
            a_host = IDNA.toASCII(host);
        }
        a_fragment = UrlEncoding.encode(fragment, Profile.FRAGMENT.filter());
        a_path = UrlEncoding.encode(path, Profile.PATH.filter());
        a_query = UrlEncoding.encode(query, Profile.QUERY.filter(), Profile.PATH.filter());
        a_userinfo = UrlEncoding.encode(userinfo, Profile.USERINFO.filter());
        a_authority = buildASCIIAuthority();
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((authority == null) ? 0 : authority.hashCode());
        result = PRIME * result + ((fragment == null) ? 0 : fragment.hashCode());
        result = PRIME * result + ((host == null) ? 0 : host.hashCode());
        result = PRIME * result + ((path == null) ? 0 : path.hashCode());
        result = PRIME * result + port;
        result = PRIME * result + ((query == null) ? 0 : query.hashCode());
        result = PRIME * result + ((scheme == null) ? 0 : scheme.hashCode());
        result = PRIME * result + ((userinfo == null) ? 0 : userinfo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final IRI other = (IRI)obj;
        if (authority == null) {
            if (other.authority != null)
                return false;
        } else if (!authority.equals(other.authority))
            return false;
        if (fragment == null) {
            if (other.fragment != null)
                return false;
        } else if (!fragment.equals(other.fragment))
            return false;
        if (host == null) {
            if (other.host != null)
                return false;
        } else if (!host.equals(other.host))
            return false;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        if (port != other.port)
            return false;
        if (query == null) {
            if (other.query != null)
                return false;
        } else if (!query.equals(other.query))
            return false;
        if (scheme == null) {
            if (other.scheme != null)
                return false;
        } else if (!scheme.equals(other.scheme))
            return false;
        if (userinfo == null) {
            if (other.userinfo != null)
                return false;
        } else if (!userinfo.equals(other.userinfo))
            return false;
        return true;
    }

    public String getAuthority() {
        return (authority != null && authority.length() > 0) ? authority : null;
    }

    public String getFragment() {
        return fragment;
    }

    public String getHost() {
        return (host != null && host.length() > 0) ? host : null;
    }

    public IDNA getIDN() {
        return new IDNA(host);
    }

    public String getASCIIHost() {
        return (a_host != null && a_host.length() > 0) ? a_host : null;
    }

    public String getPath() {
        return path;
    }

    public int getPort() {
        return port;
    }

    public String getQuery() {
        return query;
    }

    public String getScheme() {
        return (scheme != null) ? scheme.toLowerCase() : null;
    }

    public String getSchemeSpecificPart() {
        return buildSchemeSpecificPart(authority, path, query, fragment);
    }

    public String getUserInfo() {
        return userinfo;
    }

    void buildAuthority(StringBuilder buf, String aui, String ah, int port) {
        if (aui != null && aui.length() != 0) {
            buf.append(aui);
            buf.append('@');
        }
        if (ah != null && ah.length() != 0) {
            buf.append(ah);
        }
        if (port != -1) {
            buf.append(':');
            buf.append(port);
        }
    }

    private String buildASCIIAuthority() {
        if (_scheme instanceof HttpScheme) {
            StringBuilder buf = new StringBuilder();
            String aui = getASCIIUserInfo();
            String ah = getASCIIHost();
            int port = getPort();
            buildAuthority(buf, aui, ah, port);
            return buf.toString();
        } else {
            return UrlEncoding.encode(authority, Profile.AUTHORITY.filter());
        }
    }

    public String getASCIIAuthority() {
        return (a_authority != null && a_authority.length() > 0) ? a_authority : null;
    }

    public String getASCIIFragment() {
        return a_fragment;
    }

    public String getASCIIPath() {
        return a_path;
    }

    public String getASCIIQuery() {
        return a_query;
    }

    public String getASCIIUserInfo() {
        return a_userinfo;
    }

    public String getASCIISchemeSpecificPart() {
        return buildSchemeSpecificPart(a_authority, a_path, a_query, a_fragment);
    }

    private String buildSchemeSpecificPart(String authority, String path, String query, String fragment) {
        StringBuilder buf = new StringBuilder();
        if (authority != null) {
            buf.append("//");
            buf.append(authority);
        }
        if (path != null && path.length() != 0) {
            buf.append(path);
        }
        if (query != null) {
            buf.append('?');
            buf.append(query);
        }
        if (fragment != null) {
            buf.append('#');
            buf.append(fragment);
        }
        return buf.toString();
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return new IRI(toString()); // not going to happen, but we have to
                                        // catch it
        }
    }

    public boolean isAbsolute() {
        return scheme != null;
    }

    public boolean isOpaque() {
        return path == null;
    }

    public static IRI relativize(IRI b, IRI c) {
        if (c.isOpaque() || b.isOpaque())
            return c;
        if ((b.scheme == null && c.scheme != null) || (b.scheme != null && c.scheme == null)
            || (b.scheme != null && c.scheme != null && !b.scheme.equalsIgnoreCase(c.scheme)))
            return c;
        String bpath = normalize(b.getPath());
        String cpath = normalize(c.getPath());
        bpath = (bpath != null) ? bpath : "/";
        cpath = (cpath != null) ? cpath : "/";
        if (!bpath.equals(cpath)) {
            if (bpath.charAt(bpath.length() - 1) != '/')
                bpath += "/";
            if (!cpath.startsWith(bpath))
                return c;
        }
        IRI iri =
            new IRI(null, null, null, null, null, -1, normalize(cpath.substring(bpath.length())), c.getQuery(), c
                .getFragment());
        return iri;
    }

    public IRI relativize(IRI iri) {
        return relativize(this, iri);
    }

    public boolean isPathAbsolute() {
        String path = getPath();
        return (path != null) && path.length() > 0 && path.charAt(0) == '/';
    }

    public boolean isSameDocumentReference() {
        return scheme == null && authority == null
            && (path == null || path.length() == 0 || path.equals("."))
            && query == null;
    }

    public static IRI resolve(IRI b, String c) throws IOException {
        return resolve(b, new IRI(c));
    }

    public static IRI resolve(IRI b, IRI c) {

        if (c == null)
            return null;
        if ("".equals(c.toString()) || "#".equals(c.toString())
            || ".".equals(c.toString())
            || "./".equals(c.toString()))
            return b;
        if (b == null)
            return c;

        if (c.isOpaque() || b.isOpaque())
            return c;
        if (c.isSameDocumentReference()) {
            String cfragment = c.getFragment();
            String bfragment = b.getFragment();
            if ((cfragment == null && bfragment == null) || (cfragment != null && cfragment.equals(bfragment))) {
                return (IRI)b.clone();
            } else {
                return new IRI(b._scheme, b.getScheme(), b.getAuthority(), b.getUserInfo(), b.getHost(), b.getPort(),
                               normalize(b.getPath()), b.getQuery(), cfragment);
            }
        }
        if (c.isAbsolute())
            return c;

        Scheme _scheme = b._scheme;
        String scheme = b.scheme;
        String query = c.getQuery();
        String fragment = c.getFragment();
        String userinfo = null;
        String authority = null;
        String host = null;
        int port = -1;
        String path = null;
        if (c.getAuthority() == null) {
            authority = b.getAuthority();
            userinfo = b.getUserInfo();
            host = b.getHost();
            port = b.getPort();
            path = c.isPathAbsolute() ? normalize(c.getPath()) : resolve(b.getPath(), c.getPath());
        } else {
            authority = c.getAuthority();
            userinfo = c.getUserInfo();
            host = c.getHost();
            port = c.getPort();
            path = normalize(c.getPath());
        }
        return new IRI(_scheme, scheme, authority, userinfo, host, port, path, query, fragment);
    }

    public IRI normalize() {
        return normalize(this);
    }

    public static String normalizeString(String iri) {
        return normalize(new IRI(iri)).toString();
    }

    public static IRI normalize(IRI iri) {
        if (iri.isOpaque() || iri.getPath() == null)
            return iri;
        IRI normalized = null;
        if (iri._scheme != null)
            normalized = iri._scheme.normalize(iri);
        return (normalized != null) ? normalized : new IRI(iri._scheme, iri.getScheme(), iri.getAuthority(), iri
            .getUserInfo(), iri.getHost(), iri.getPort(), normalize(iri.getPath()), UrlEncoding.encode(UrlEncoding
            .decode(iri.getQuery()), Profile.IQUERY.filter()), UrlEncoding
            .encode(UrlEncoding.decode(iri.getFragment()), Profile.IFRAGMENT.filter()));
    }

    protected static String normalize(String path) {
        if (path == null || path.length() == 0)
            return "/";
        String[] segments = path.split("/");
        if (segments.length < 2)
            return path;
        StringBuilder buf = new StringBuilder("/");
        for (int n = 0; n < segments.length; n++) {
            String segment = segments[n].intern();
            if (segment == ".") {
                segments[n] = null;
            } else if (segment == "..") {
                segments[n] = null;
                int i = n;
                while (--i > -1) {
                    if (segments[i] != null)
                        break;
                }
                if (i > -1)
                    segments[i] = null;
            }
        }
        for (int n = 0; n < segments.length; n++) {
            if (segments[n] != null) {
                if (buf.length() > 1)
                    buf.append('/');
                buf.append(UrlEncoding.encode(UrlEncoding.decode(segments[n]), Profile.IPATHNODELIMS_SEG.filter()));
            }
        }
        if (path.endsWith("/") || path.endsWith("/."))
            buf.append('/');
        return buf.toString();
    }

    private static String resolve(String bpath, String cpath) {
        if (bpath == null && cpath == null)
            return null;
        if (bpath == null && cpath != null) {
            return (!cpath.startsWith("/")) ? "/" + cpath : cpath;
        }
        if (bpath != null && cpath == null)
            return bpath;
        StringBuilder buf = new StringBuilder("");
        int n = bpath.lastIndexOf('/');
        if (n > -1)
            buf.append(bpath.substring(0, n + 1));
        if (cpath.length() != 0)
            buf.append(cpath);
        if (buf.charAt(0) != '/')
            buf.insert(0, '/');
        return normalize(buf.toString());
    }

    public IRI resolve(IRI iri) {
        return resolve(this, iri);
    }

    public IRI resolve(String iri) {
        return resolve(this, new IRI(iri));
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        String scheme = getScheme();
        if (scheme != null && scheme.length() != 0) {
            buf.append(scheme);
            buf.append(':');
        }
        buf.append(getSchemeSpecificPart());
        return UrlEncoding.encode(buf.toString(), Profile.SCHEMESPECIFICPART.filter());
    }

    public String toASCIIString() {
        StringBuilder buf = new StringBuilder();
        String scheme = getScheme();
        if (scheme != null && scheme.length() != 0) {
            buf.append(scheme);
            buf.append(':');
        }
        buf.append(getASCIISchemeSpecificPart());
        return buf.toString();
    }

    public String toBIDIString() {
        return CharUtils.wrapBidi(toString(), CharUtils.LRE);
    }

    public java.net.URI toURI() throws URISyntaxException {
        return new java.net.URI(toASCIIString());
    }

    public java.net.URL toURL() throws MalformedURLException, URISyntaxException {
        return toURI().toURL();
    }

    private void parseAuthority() {
        if (authority != null) {
            Matcher auth = AUTHORITYPATTERN.matcher(authority);
            if (auth.find()) {
                userinfo = auth.group(1);
                host = auth.group(2);
                if (auth.group(3) != null)
                    port = Integer.parseInt(auth.group(3));
                else
                    port = -1;
            }
            try {
                CharUtils.verify(userinfo, Profile.IUSERINFO);
                CharUtils.verify(host, Profile.IHOST);
            } catch (InvalidCharacterException e) {
                throw new IRISyntaxException(e);
            }
        }
    }

    private void parse(String iri) {
        try {
            SchemeRegistry reg = SchemeRegistry.getInstance();
            Matcher irim = IRIPATTERN.matcher(iri);
            if (irim.find()) {
                scheme = irim.group(1);
                _scheme = reg.getScheme(scheme);
                authority = irim.group(2);
                path = irim.group(3);
                query = irim.group(4);
                fragment = irim.group(5);
                parseAuthority();
                try {
                    CharUtils.verify(scheme, Profile.SCHEME);
                    CharUtils.verify(path, Profile.IPATH);
                    CharUtils.verify(query, Profile.IQUERY);
                    CharUtils.verify(fragment, Profile.IFRAGMENT);
                } catch (InvalidCharacterException e) {
                    throw new IRISyntaxException(e);
                }
            } else {
                throw new IRISyntaxException("Invalid Syntax");
            }
        } catch (IRISyntaxException e) {
            throw e;
        } catch (Exception e) {
            throw new IRISyntaxException(e);
        }
    }

    private static final Pattern IRIPATTERN =
        Pattern.compile("^(?:([^:/?#]+):)?(?://([^/?#]*))?([^?#]*)(?:\\?([^#]*))?(?:#(.*))?");

    private static final Pattern AUTHORITYPATTERN =
        Pattern.compile("^(?:(.*)?@)?((?:\\[.*\\])|(?:[^:]*))?(?::(\\d+))?");

    public static void preinit() {
        UnicodeCharacterDatabase.getCanonicalClass(1);
        Nameprep.prep("");
    }

    /**
     * Returns a new IRI with a trailing slash appended to the path, if necessary
     */
    public IRI trailingSlash() {
        return new IRI(_scheme, scheme, authority, userinfo, host, port, path.endsWith("/") ? path : path + "/", query,
                       fragment);
    }

}

