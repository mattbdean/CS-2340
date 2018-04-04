package com.github.alphabet26.ui;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.TextInputLayout;

import com.github.alphabet26.App;
import com.github.alphabet26.R;
import com.github.alphabet26.ui.DashboardActivity;
import com.github.alphabet26.ui.LoginActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(application = App.class)
public final class LoginActivityTest {
    private LoginActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(LoginActivity.class);
    }

    @Test
    @Ignore
    public void onLogin_shouldStartDashboardActivityWithGoodCreds() {
        setText(R.id.username, "username");
        setText(R.id.password, "password");
        activity.onLogin(null);

        Intent expectedIntent = new Intent(activity, DashboardActivity.class);
        // For whatever reason Intent doesn't override equals() so let's at least make sure we're
        // starting the expected activity
        assertThat(ShadowApplication.getInstance().getNextStartedActivity().getComponent())
                .isEqualTo(expectedIntent.getComponent());
    }

    private void setText(@IdRes int textInputLayoutId, CharSequence content) {
        TextInputLayout layout = activity.findViewById(textInputLayoutId);
        if ((layout == null) || (layout.getEditText() == null)) {
            throw new IllegalStateException("Could not find TextInputLayout with EditText child " +
                    "with provided ID");
        }
        layout.getEditText().setText(content);
    }
}
