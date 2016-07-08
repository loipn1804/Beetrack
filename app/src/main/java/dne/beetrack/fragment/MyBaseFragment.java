package dne.beetrack.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dne.beetrack.activity.MyBaseActivity;

/**
 * Created by loipn on 7/7/2016.
 */
public class MyBaseFragment extends Fragment {

    public void showProgressDialog(boolean cancelable) {
        ((MyBaseActivity) getActivity()).showProgressDialog(cancelable);
    }

    public void hideProgressDialog() {
        ((MyBaseActivity) getActivity()).hideProgressDialog();
    }

    public void showToastError(String message) {
        ((MyBaseActivity) getActivity()).showToastError(message);
    }

    public void showToastInfo(String message) {
        ((MyBaseActivity) getActivity()).showToastInfo(message);
    }

    public void showToastOk(String message) {
        ((MyBaseActivity) getActivity()).showToastOk(message);
    }

    private boolean isDestroyView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroyView = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDestroyView = true;
    }

    protected boolean isDestroyView() {
        return isDestroyView;
    }
}
