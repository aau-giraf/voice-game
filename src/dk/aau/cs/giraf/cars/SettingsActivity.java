package dk.aau.cs.giraf.cars;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import dk.aau.cs.giraf.cars.MicSetupDialogFragment.InputTestDialogListener;
import dk.aau.cs.giraf.cars.R.id;
import dk.aau.cs.giraf.cars.Settings1Fragment.ListClickListener;
import dk.aau.cs.giraf.cars.gamecode.GameInfo.GameInfo;


public class SettingsActivity extends Activity implements InputTestDialogListener, ListClickListener, OnCheckedChangeListener {
    RadioGroup carSpeed;
    RadioGroup obstacleCount;
    private long mChildId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mChildId = getIntent().getExtras().getLong("currentChildId");
        carSpeed = (RadioGroup) findViewById(R.id.car_speed);
        obstacleCount = (RadioGroup) findViewById(R.id.obstacle_count);

        carSpeed.setOnCheckedChangeListener(this);
        obstacleCount.setOnCheckedChangeListener(this);

        loadGameInfo();
    }


    public void loadGameInfo() {
        findViewById(R.id.color1).setBackgroundColor(GameInfo.color1);
        findViewById(R.id.color2).setBackgroundColor(GameInfo.color2);
        findViewById(R.id.color3).setBackgroundColor(GameInfo.color3);


        if (GameInfo.carSpeed == 1.00) {
            carSpeed.check(R.id.speed100pct);
        } else if (GameInfo.carSpeed == 0.75) {
            carSpeed.check(R.id.speed75pct);
        } else if (GameInfo.carSpeed == 0.50) {
            carSpeed.check(R.id.speed50pct);
        } else if (GameInfo.carSpeed == 0.25) {
            carSpeed.check(R.id.speed25pct);
        }

        if (GameInfo.numberOfObstacles == 4) {
            obstacleCount.check(R.id.obstacles4);
        } else if (GameInfo.numberOfObstacles == 3) {
            obstacleCount.check(R.id.obstacles3);
        } else if (GameInfo.numberOfObstacles == 2) {
            obstacleCount.check(R.id.obstacles2);
        } else if (GameInfo.numberOfObstacles == 1) {
            obstacleCount.check(R.id.obstacles1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    public void createTestDialog() {
        MicSetupDialogFragment micTest = new MicSetupDialogFragment();

        micTest.setCancelable(false);

        micTest.show(getFragmentManager(), "micTestDialog");

    }

    public void showMicTestDialog(View v) {
        createTestDialog();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.getId() == R.id.car_speed) {
            switch (checkedId) {
                case R.id.speed100pct:
                    GameInfo.carSpeed = 1.00f;
                    break;
                case R.id.speed75pct:
                    GameInfo.carSpeed = 0.75f;
                    break;
                case R.id.speed50pct:
                    GameInfo.carSpeed = 0.50f;
                    break;
                case R.id.speed25pct:
                    GameInfo.carSpeed = 0.25f;
                    break;
            }
        } else if (group.getId() == R.id.obstacle_count) {
            switch (checkedId) {
                case R.id.obstacles1:
                    GameInfo.numberOfObstacles = 1;
                    break;
                case R.id.obstacles2:
                    GameInfo.numberOfObstacles = 2;
                    break;
                case R.id.obstacles3:
                    GameInfo.numberOfObstacles = 3;
                    break;
                case R.id.obstacles4:
                    GameInfo.numberOfObstacles = 4;
                    break;
            }
        }
    }

    public void changeColors(View view) {
        Dialog color_dialog = new Dialog(this);
        color_dialog.setContentView(R.layout.color_dialog);
        color_dialog.setTitle(R.string.chosecolor);
        color_dialog.setCanceledOnTouchOutside(false);
        color_dialog.show();

        setOnClick(color_dialog, view, color_dialog.findViewById(id.mcolor1));
        setOnClick(color_dialog, view, color_dialog.findViewById(id.mcolor2));
        setOnClick(color_dialog, view, color_dialog.findViewById(id.mcolor3));
        setOnClick(color_dialog, view, color_dialog.findViewById(id.mcolor4));
        setOnClick(color_dialog, view, color_dialog.findViewById(id.mcolor5));
        setOnClick(color_dialog, view, color_dialog.findViewById(id.mcolor6));
        setOnClick(color_dialog, view, color_dialog.findViewById(id.mcolor7));
        setOnClick(color_dialog, view, color_dialog.findViewById(id.mcolor8));
    }

    private void setOnClick(final Dialog color_dialog, final View parent, View child) {
        child.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                parent.setBackgroundDrawable(v.getBackground());
                color_dialog.dismiss();
            }

        });
    }

    @Override
    public void inputTestResult(ResultStates resultState) {
        // TODO Auto-generated method stub
        switch (resultState) {
            case restart:
                createTestDialog();
            case complete:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void saveColors() {
        View view;
        Drawable temp;
        ColorDrawable temp2;

        view = findViewById(R.id.color1);
        temp = view.getBackground();
        temp2 = (ColorDrawable) temp;
        GameInfo.color1 = temp2.getColor();

        view = findViewById(R.id.color2);
        temp = view.getBackground();
        temp2 = (ColorDrawable) temp;
        GameInfo.color2 = temp2.getColor();

        view = findViewById(R.id.color3);
        temp = view.getBackground();
        temp2 = (ColorDrawable) temp;
        GameInfo.color3 = temp2.getColor();
    }


    @Override
    public void loadProfile(long userId) {
        saveColors();
        Settings.save(mChildId, getBaseContext());

        mChildId = userId;

        Settings.load(mChildId, getBaseContext());
        loadGameInfo();

    }
}

