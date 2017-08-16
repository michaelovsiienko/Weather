package androidprojectsw.com.weather.contract;

import android.content.Context;

public interface BaseContract {

    interface View {

        Context getContext();

        void showLoading(boolean isShow);

        void showError(String message);

    }

    interface Presenter <V extends View> {

        void attachView(V view);

        void detachView();

    }
}