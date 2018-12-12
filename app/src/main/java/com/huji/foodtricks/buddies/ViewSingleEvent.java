package com.huji.foodtricks.buddies;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.huji.foodtricks.buddies.ui.viewsingleevent.ViewSingleEventFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class ViewSingleEvent extends AppCompatActivity {

    static EventModel curr_event;
    static UserModel curr_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<String> invitees = new ArrayList<>(Arrays.asList("Me"));
        curr_event = new EventModel("Gek with your mom", new Date(), invitees, "stam");
        curr_user = new UserModel("YOYO", "idodush", "ido", "savion");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_event_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ViewSingleEventFragment.newInstance())
                    .commitNow();
        }
        getSupportActionBar().setTitle(curr_event.getTitle());
        FabSpeedDial fabSpeedDial = (FabSpeedDial)findViewById(R.id.fabSpeedDial);
        fabSpeedDial.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Toast.makeText(ViewSingleEvent.this, "Changed RSVP to " +menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onMenuClosed() {

            }
        });

        Bundle prev_screen_bundle = getIntent().getExtras();
//        EventModel curr_event = (EventModel)prev_screen_bundle.getSerializable("value");
//        EventModel curr_event = curr_event;
        updateAllFields(curr_event);
    }

    @SuppressLint("RestrictedApi")
    private void updateAllFields(EventModel curr_event) {
        TextView who_tv = (TextView) findViewById(R.id.who_text);
        who_tv.setText(curr_event.getTitle());
        TextView when_tv = (TextView) findViewById(R.id.when_textView);
//        when_tv.setText(String.valueOf(curr_event.getTime()));
        when_tv.setText("Tomorrow night");
        TextView what_tv = (TextView) findViewById(R.id.what_text);
        what_tv.setText(curr_event.getTitle());
        MapView location_mv = (MapView) findViewById(R.id.mapView);
        if (curr_event.getOrganizerID() != curr_user.getUserFirebaseId())
        {
            FloatingActionButton send_fab = (FloatingActionButton) findViewById(R.id.send_event_fab);
            send_fab.setVisibility(View.GONE);
            FloatingActionButton discard_fab = (FloatingActionButton) findViewById(R.id.discard_event_fab);
            discard_fab.setVisibility(View.GONE);
        }

    }

    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.approvers_popup, null);
        // create the popup window
        PopupWindow pw = new PopupWindow(popupView);
        TextView tv = pw.getContentView().findViewById(R.id.approvers_list);
        tv.setText(String.valueOf(curr_event.nonResponsiveCount()));
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }

    public void send_event_click(View view) {
        curr_event.setEventStatus(EventModel.state.UPCOMING);
    }

    public void discard_event_click(View view) {
        curr_event.setEventStatus(EventModel.state.DISCARDED);

    }
}
