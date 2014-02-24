package dk.aau.cs.giraf.cars;

import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import dk.aau.cs.giraf.cars.gamecode.GameObject;
import dk.aau.cs.giraf.cars.gamecode.GameThread;
import dk.aau.cs.giraf.cars.gamecode.GameView;
import dk.aau.cs.giraf.cars.gamecode.MapDivider;
import dk.aau.cs.giraf.cars.gamecode.GameObjects.Car;
import dk.aau.cs.giraf.cars.objects.MicCar;
import dk.aau.cs.giraf.cars.sound.MicTestThread;
import dk.aau.cs.giraf.cars.sound.RecorderThread;
import dk.aau.cs.giraf.cars.sound.SetupStates;

public class MicSetupDialogFragment extends DialogFragment {

    private RecorderThread mRecordThread = new RecorderThread();
    private MicTestThread mMicThread = new MicTestThread();
    private GameView mGameView;
    private GameObject mCar;
    private GameThread mGameThread;
    private static final float GAMEVIEW_WIDTH = 100.0f;
    private static final float GAMEVIEW_HEIGHT = 400.0f;
    private ViewFlipper mFlipper;

    private SetupStates testState = SetupStates.Low;

    public MicSetupDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the Dialog’s UI.
        View view = inflater.inflate(R.layout.dialog_mic_flipviews, container, false);
        // Update the Dialog’s contents.
        final TextView text = (TextView) view.findViewById(R.id.dialog_mic_test_textview);
        text.setText(getString(R.string.mic_test_low));
        final ImageView img = (ImageView) view.findViewById(R.id.dialog_drawable_to_mimic);


        img.setImageDrawable(getResources().getDrawable(R.drawable.bear));


        Button nextButton = (Button) view.findViewById(R.id.next_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        Button retryButton = (Button) view.findViewById(R.id.retry_button);
        mFlipper = (ViewFlipper) view.findViewById(R.id.mictestflip);

        nextButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                switch (testState) {
                    case Low:
                        text.setText(getString(R.string.mic_test_high));
                        img.setImageDrawable(getResources().getDrawable(R.drawable.mus));

                        testState = SetupStates.High;
                        mMicThread.setType(testState);
                        break;
                    case High:
                        testState = SetupStates.Complete;
                        mMicThread.setType(testState);

                        nextView();
                        break;
                }
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        retryButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                switch (testState) {
                    case Low:
                        mMicThread.restartLowFreq();
                        Toast.makeText(getActivity(), "Optagelse genstartet", Toast.LENGTH_SHORT).show();
                        break;
                    case High:
                        mMicThread.restartHighFreq();
                        Toast.makeText(getActivity(), "Optagelse genstartet", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return view;
    }


    public void nextView() {
        mFlipper.showNext();

        View buttonView = mFlipper.getCurrentView();
        BitmapContainer.setResources(getResources());
        BitmapContainer.add(R.drawable.car);

        mGameView = new GameView(getActivity(), getResources(), true);
        mGameView.setZOrderOnTop(true);
        final float scale = getResources().getDisplayMetrics().density;


        int gameViewWidth = (int) (GAMEVIEW_WIDTH * scale + 0.5f);
        int gameViewHeight = (int) (GAMEVIEW_HEIGHT * scale + 0.5f);

        LinearLayout.LayoutParams gameViewParams = new LinearLayout.LayoutParams(gameViewWidth, gameViewHeight);

        LinearLayout openglLayout = (LinearLayout) buttonView.findViewById(R.id.opengl_layout);
        openglLayout.addView(mGameView, gameViewParams);


        Button nextButton = (Button) buttonView.findViewById(R.id.next_button);
        Button retryButton = (Button) buttonView.findViewById(R.id.retry_button);


        final InputTestDialogListener activity = (InputTestDialogListener) getActivity();

        nextButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mMicThread.saveFrequencies();
                dismiss();
            }
        });


        retryButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
                activity.inputTestResult(ResultStates.restart);
            }
        });

        Drawable car = getResources().getDrawable(R.drawable.car);

        int carWidth = car.getIntrinsicWidth();
        int carHeight = car.getIntrinsicHeight();

        System.out.println(carWidth);
        System.out.println(carHeight);
        MapDivider.CalculateConstants(gameViewHeight, 100);

        mCar = new MicCar(gameViewWidth, gameViewHeight, carWidth, carHeight, mMicThread.getLowFreq(), mMicThread.getHighFreq());

        mGameThread = new GameThread((Car) mCar);

        mGameView.SetObjects(mCar);

        mGameThread.start();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecordThread.start();
        mMicThread.setType(SetupStates.Low);
        mMicThread.start();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecordThread.recording = false;
        mMicThread.stopThread();
        mGameThread.stopRunning();
    }

    public interface InputTestDialogListener {
        void inputTestResult(ResultStates resultState);
    }

}
