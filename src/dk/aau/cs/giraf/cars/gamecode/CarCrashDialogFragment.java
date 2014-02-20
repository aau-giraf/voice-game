package dk.aau.cs.giraf.cars.gamecode;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import dk.aau.cs.giraf.cars.R;

public class CarCrashDialogFragment extends DialogFragment {

    //Empty constructor
    public CarCrashDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_car_crash, container, false);

        Button nextButton = (Button) view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameInfo.pause = false;
                dismiss();
            }
        });

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view.setBackgroundColor(Color.WHITE);

        return view;

    }
}


