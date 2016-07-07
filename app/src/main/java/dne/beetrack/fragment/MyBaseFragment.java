package dne.beetrack.fragment;

import android.support.v4.app.Fragment;

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
}
