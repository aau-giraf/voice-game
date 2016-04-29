package dk.aau.cs.giraf.voicegame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import dk.aau.cs.giraf.activity.GirafActivity;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.gui.GirafInflatableDialog;
import dk.aau.cs.giraf.showcaseview.ShowcaseManager;
import dk.aau.cs.giraf.showcaseview.ShowcaseView;
import dk.aau.cs.giraf.showcaseview.targets.ViewTarget;
import dk.aau.cs.giraf.voicegame.Settings.CalibrationFragment;
import dk.aau.cs.giraf.voicegame.Settings.SpeedGauge;
import dk.aau.cs.giraf.voicegame.game.GameMode;
import dk.aau.cs.giraf.voicegame.game.Enums.SoundMode;
import dk.aau.cs.giraf.voicegame.Settings.GameSettings;
import dk.aau.cs.giraf.voicegame.Settings.SpeedFragment;
import dk.aau.cs.giraf.gui.GColorPicker;
import dk.aau.cs.giraf.gui.GComponent;

public class SettingsActivity extends GirafActivity implements GirafInflatableDialog.OnCustomViewCreatedListener, ShowcaseManager.ShowcaseCapable{
    protected GameSettings gameSettings, initSettings;

    private static final String SAVE_DIALOG_TAG = "SAVE_DIALOG";
    private static final Integer SAVE_DIALOG_ID = 1;
    private GirafInflatableDialog saveDialog;

    GirafButton helpGirafButton, saveSettingsButton;
    ColorButton colorPickButton;

    SpeedFragment speed;
    CalibrationFragment calibration;

    GameMode gameMode;
    SoundMode soundMode;

    String IS_FIRST_RUN_KEY = "IS_FIRST_RUN_KEY_SETTINGS_ACTIVITY";
    ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    ShowcaseManager showcaseManager;
    boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        if(intent.hasExtra("settings")) {
            gameSettings = (GameSettings) intent.getSerializableExtra("settings");
        }
        initSettings = gameSettings;

        View v = LayoutInflater.from(this).inflate(R.layout.activity_settings, null);
        v.setBackgroundColor(GComponent.GetBackgroundColor());
        setContentView(v);

        helpGirafButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_help));

        helpGirafButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick method for helpGirafButton
             * @param v
             */
            @Override
            public void onClick(View v) {
                SettingsActivity.this.toggleShowcase();
            }
        });

        saveSettingsButton = new GirafButton(this, getResources().getDrawable(R.drawable.icon_save));

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick method for saveSettingsButton
             * @param v
             */
            @Override
            public void onClick(View v) {

                saveDialog = GirafInflatableDialog.newInstance(getResources().getString(R.string.save_dialog_settings_title), getResources().getString(R.string.save_dialog_settings_text), R.layout.activity_save_dialog_settings, SAVE_DIALOG_ID);
                saveDialog.show(getSupportFragmentManager(), SAVE_DIALOG_TAG);
            }
        });

        addGirafButtonToActionBar(helpGirafButton, GirafActivity.RIGHT);
        addGirafButtonToActionBar(saveSettingsButton, GirafActivity.RIGHT);

        colorPickButton = (ColorButton) findViewById(R.id.colorPick);
        speed = (SpeedFragment) getFragmentManager().findFragmentById(R.id.speed);
        calibration = (CalibrationFragment)getFragmentManager().findFragmentById(R.id.calibration_fragment);

        speed.setSpeed(gameSettings.GetSpeed());
        speed.setCarColor(gameSettings.GetColor());
        calibration.SetMinVolume(gameSettings.GetMinVolume());
        calibration.SetMaxVolume(gameSettings.GetMaxVolume());
        colorPickButton.SetColor(gameSettings.GetColor());

        initializeGameMode();
        initializeSoundMode();
    }

    //Google analytics - start logging
    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);  // Start logging
    }
    //Google analytics - Stop logging
    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // stop logging
    }

    public void ColorPickClick(View view) {
        final ColorButton button = (ColorButton)view;
        GColorPicker colorPicker = new GColorPicker(view.getContext(), new GColorPicker.OnOkListener() {
            @Override
            public void OnOkClick(GColorPicker diag, int color) {
                button.SetColor(color);
                speed.setCarColor(color);
            }
        });
        colorPicker.SetCurrColor(button.GetColor());
        colorPicker.show();
    }

    @Override
    public void onBackPressed() {
        cancelSettings();
        //TODO Add "There are unsaved changes" dialog
        Intent result = new Intent();
        result.putExtra("settings", gameSettings);
        setResult(Activity.RESULT_OK, result);
        this.finish();
    }

    public void onRadioButtonClicked(View v)
    {
        switch(v.getId())
        {
            case R.id.radioButtonpPickup:
                gameMode = GameMode.pickup;
                break;
            case R.id.radioButtonAvoid:
                gameMode = GameMode.avoid;
                break;
            case R.id.radioButtonSoundUp:
                soundMode = SoundMode.highUp;
                break;
            case R.id.radioButtonSoundDown:
                soundMode = SoundMode.highDown;
                break;
        }
    }

    /**
     * This initiates the gameMode checkbox in settings, so that it shows the current setting
     */
    private void initializeGameMode(){
        if(gameMode == null){
            gameMode = gameSettings.GetGameMode();//Get default
        }
        if (gameMode==GameMode.pickup) {
            ((RadioButton) findViewById(R.id.radioButtonpPickup)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioButtonAvoid)).setChecked(false);
        } else if (gameMode==GameMode.avoid) {
            ((RadioButton) findViewById(R.id.radioButtonAvoid)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioButtonpPickup)).setChecked(false);
        }
    }

    /**
     * This initiates the soundMode checkbox in settings, so that it shows the current setting
     */
    private void initializeSoundMode(){
        if(soundMode == null){
            soundMode = gameSettings.GetSoundMode();//Get default
        }
        if (soundMode == SoundMode.highUp) {
            ((RadioButton) findViewById(R.id.radioButtonSoundUp)).setChecked(true);
            ((RadioButton) findViewById(R.id.radioButtonSoundDown)).setChecked(false);
        } else if (soundMode == SoundMode.highDown) {
            ((RadioButton) findViewById(R.id.radioButtonSoundUp)).setChecked(false);
            ((RadioButton) findViewById(R.id.radioButtonSoundDown)).setChecked(true);
        }
    }

    /**
     * Saves object GameSettings, which implements Serializable to local file.
     * @param gs GameSettings to be saved
     * @param context Context of calling activity
     */
    public static void SaveSettings(GameSettings gs, Context context){
        if(gs == null){
            gs = new GameSettings();//Causes creation of file with default settings
        }
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("vg_settings", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os;
        try {
            os = new ObjectOutputStream(fos);
            os.writeObject(gs);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSettings = GameSettings.LoadSettings(getApplicationContext());
        initSettings = gameSettings;

        // Check if this is the first run of the app
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.isFirstRun = prefs.getBoolean(IS_FIRST_RUN_KEY, true);

        // If it is the first run display ShowcaseView
        if (isFirstRun) {
            this.findViewById(android.R.id.content).getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    showShowcase();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean(IS_FIRST_RUN_KEY, false);
                    editor.commit();

                    synchronized (SettingsActivity.this) {

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            SettingsActivity.this.findViewById(android.R.id.content).getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
                        } else {
                            SettingsActivity.this.findViewById(android.R.id.content).getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
                        }

                        globalLayoutListener = null;
                    }
                }
            });
        }
    }

    /**
     * Cancels unsaved settings by resetting values to before changes; updates view.
     */
    private void cancelSettings(){
        gameSettings = initSettings;
        gameMode = gameSettings.GetGameMode();
        soundMode = gameSettings.GetSoundMode();
        this.colorPickButton.SetColor(gameSettings.GetColor());
        speed.setCarColor(gameSettings.GetColor());
        speed.GetGauge().SetSpeed(gameSettings.GetSpeed());
        initializeGameMode();
        initializeSoundMode();
    }

    /**
     * and override method from implementing GirafInflatableDialog.OnCustomViewCreatedListener
     * Content of the dialog goes in this method
     * @param viewGroup the views inside the dialog, access this when editing views.
     * @param i the id of the dialog
     */
    @Override
    public void editCustomView(final ViewGroup viewGroup, int i) {
        if(i == SAVE_DIALOG_ID ) {

            final GirafButton saveButton = (GirafButton) viewGroup.findViewById(R.id.button_gem);
            GirafButton cancelButton = (GirafButton) viewGroup.findViewById(R.id.button_anuller);

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelSettings();

                    saveDialog.dismiss();
                }
            });

            /**
             * Reads the trackOrganizer from file and overrides it, containing the newly created track aswell.
             */
            saveButton.setOnClickListener(new View.OnClickListener() {

                /**
                 * When clicking the save button, the track will be edited, if the "edit" extra is set to true, and call the appropriate method in the trackOrganizer
                 * @param v the view that was clicked
                 */
                @Override
                public void onClick(View v) {
                    initSettings = new GameSettings(colorPickButton.GetColor(), speed.getSpeed(), calibration.GetMinVolume(),
                            calibration.GetMaxVolume(), gameMode, soundMode);
                    SaveSettings(initSettings, getApplicationContext());

                    saveDialog.dismiss();
                }
            });
        }
        
    }
    @Override
    public synchronized void showShowcase() {

        // Targets for the Showcase
        final ViewTarget saveSettingsButtonTarget = new ViewTarget(saveSettingsButton, 1.5f);
        final ViewTarget colorPickButtonTarget = new ViewTarget(colorPickButton, 1f);
        final ViewTarget calibrationFragmentTarget = new ViewTarget(R.id.calibration_fragment, this, 1f);
        final ViewTarget avoidTarget = new ViewTarget(R.id.radioButtonAvoid, this, 1f);
        final ViewTarget radioButtonSoundDownTarget = new ViewTarget(R.id.radioButtonSoundDown, this, 1f);

        // Create a relative location for the next button
        final RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        final int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        // End button
        final RelativeLayout.LayoutParams stopLps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        stopLps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        stopLps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        final Button stopButton = (Button) LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.showcase_button, null);
        stopButton.setBackgroundColor(getResources().getColor(R.color.giraf_button_fill_end)); // Showcase Button background color
        stopButton.setText("Luk");
        stopLps.setMargins(margin, margin, margin, margin);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.toggleShowcase();
            }
        });

        // Calculate position for the help text
        final int textX = getResources().getDisplayMetrics().widthPixels / 2 + margin;
        final int textY = getResources().getDisplayMetrics().heightPixels / 2 + margin;
        final int textX_left = getResources().getDisplayMetrics().widthPixels / 6;

        showcaseManager = new ShowcaseManager();

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(saveSettingsButtonTarget, true);
                showcaseView.setContentTitle(getString(R.string.save_settings_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.save_settings_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.addView(stopButton, stopLps);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {
                // TODO: Get target of the speedcontainer insted of this quick fix with x/y
                showcaseView.setShowcaseX(getResources().getDisplayMetrics().widthPixels / 3);
                showcaseView.setShowcaseY(getResources().getDisplayMetrics().heightPixels / 3 + margin);
                showcaseView.setContentTitle(getString(R.string.speed_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.speed_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(colorPickButtonTarget, true);
                showcaseView.setContentTitle(getString(R.string.color_pick_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.color_pick_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(calibrationFragmentTarget, true);
                showcaseView.setContentTitle(getString(R.string.calibration_fragment_pick_icon_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.calibration_fragment_pick_icon_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX_left, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(avoidTarget, true);
                showcaseView.setContentTitle(getString(R.string.avoid_pickup_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.avoid_pickup_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                showcaseView.setButtonPosition(lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        showcaseManager.addShowCase(new ShowcaseManager.Showcase() {
            @Override
            public void configShowCaseView(final ShowcaseView showcaseView) {

                showcaseView.setShowcase(radioButtonSoundDownTarget, true);
                showcaseView.setContentTitle(getString(R.string.radioButton_showcase_help_title_text));
                showcaseView.setContentText(getString(R.string.radioButton_showcase_help_content_text));
                showcaseView.setStyle(R.style.GirafCustomShowcaseTheme);
                //showcaseView.setButtonPosition(lps);
                showcaseView.removeView(stopButton);
                showcaseView.addView(stopButton, lps);
                showcaseView.setTextPostion(textX, textY);
            }
        });

        ShowcaseManager.OnDoneListener onDoneCallback = new ShowcaseManager.OnDoneListener() {
            @Override
            public void onDone(ShowcaseView showcaseView) {
                showcaseManager = null;
                isFirstRun = false;
            }
        };
        showcaseManager.setOnDoneListener(onDoneCallback);

        showcaseManager.start(this);
    }

    @Override
    public synchronized void hideShowcase() {
        if (showcaseManager != null) {
            showcaseManager.stop();
            showcaseManager = null;
        }
    }

    @Override
    public synchronized void toggleShowcase() {
        if (showcaseManager != null) {
            hideShowcase();
        } else {
            showShowcase();
        }
    }
}
