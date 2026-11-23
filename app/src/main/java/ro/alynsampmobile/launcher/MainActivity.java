package ro.alynsampmobile.launcher;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.google.android.material.textview.MaterialTextView;
import com.joom.paranoid.Obfuscate;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import ro.alynsampmobile.launcher.ui.fragment.FavoriteServersFragment;
import ro.alynsampmobile.launcher.ui.fragment.ServersPageFragment;
import ro.alynsampmobile.launcher.ui.fragment.SettingsPageFragment;
import ro.alynsampmobile.launcher.utils.Utils;

@Obfuscate
public class MainActivity extends AppCompatActivity implements MaxAdListener, MaxAdViewAdListener {
    private MaxInterstitialAd interstitialAd = null;

    private int retryAttempt;
    private int selectedTab = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        if (!getIntent().getStringExtra("extra_check").equals("alynsampmobile1337")) {
            Log.e("MainActivity", "Not joined from launcher!");
            finish();
            return;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (interstitialAd == null && !Utils.isTester(this)) {
                createInterstitialAd();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error creating interstitialAd: " + e.getMessage());
        }

        ((MaterialTextView) findViewById(R.id.ahahaha)).setText(Utils.copyright);

        final LinearLayout playLayout = findViewById(R.id.playLayout);
        final LinearLayout settingsLayout = findViewById(R.id.settingsLayout);

        final ImageView playImage = findViewById(R.id.playImage);
        final ImageView settingsImage = findViewById(R.id.settingsImage);

        final TextView playTxt = findViewById(R.id.playTxt);
        final TextView settingsTxt = findViewById(R.id.settingsTxt);

        if (Utils.isTester(this)) {
            new AlertDialog.Builder(this).setTitle("Tester")
                    .setMessage("You are a tester! You have access to the latest features and updates.")
                    .setPositiveButton("Ok", null).setCancelable(false).show();
        }

        // set play fragment (favorite servers) by default
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragmentContainer, FavoriteServersFragment.class, null)
                .commit();

        selectedTab = 1;
        playTxt.setVisibility(View.VISIBLE);
        settingsTxt.setVisibility(View.GONE);

        playImage.setImageResource(R.drawable.play_selected_icon);
        settingsImage.setImageResource(R.drawable.settings_icon);

        playLayout.setBackgroundResource(R.drawable.round_back_navbg);
        settingsLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.transparent));

        playLayout.setOnClickListener(v -> {
            if (selectedTab != 1) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, FavoriteServersFragment.class, null)
                        .commit();

                settingsTxt.setVisibility(View.GONE);
                settingsImage.setImageResource(R.drawable.settings_icon);
                settingsLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.transparent));

                playTxt.setVisibility(View.VISIBLE);
                playImage.setImageResource(R.drawable.play_selected_icon);
                playLayout.setBackgroundResource(R.drawable.round_back_navbg);

                ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                playLayout.startAnimation(scaleAnimation);

                selectedTab = 1;
            }
        });

        settingsLayout.setOnClickListener(v -> {
            if (selectedTab != 2) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragmentContainer, SettingsPageFragment.class, null)
                        .commit();

                playTxt.setVisibility(View.GONE);
                playImage.setImageResource(R.drawable.play_icon);
                playLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.transparent));

                settingsTxt.setVisibility(View.VISIBLE);
                settingsImage.setImageResource(R.drawable.settings_selected_icon);
                settingsLayout.setBackgroundResource(R.drawable.round_back_navbg);

                ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                scaleAnimation.setDuration(200);
                scaleAnimation.setFillAfter(true);
                settingsLayout.startAnimation(scaleAnimation);

                selectedTab = 2;
            }
        });
    }

    void createInterstitialAd() {
        interstitialAd = new MaxInterstitialAd("YOUR_AD_UNIT_ID", this);
        interstitialAd.setListener(this);

        // Load the first ad
        interstitialAd.loadAd();
    }

    // MAX Ad Listener
    @Override
    public void onAdLoaded(final MaxAd maxAd) {
        // Interstitial ad is ready to be shown. interstitialAd.isReady() will now return 'true'

        // Reset retry attempt
        retryAttempt = 0;
    }

    @Override
    public void onAdLoadFailed(final String adUnitId, final MaxError error) {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)

        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

        new Handler().postDelayed(() -> interstitialAd.loadAd(), delayMillis);
    }

    @Override
    public void onAdDisplayFailed(final MaxAd maxAd, final MaxError error) {
        // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
        interstitialAd.loadAd();
    }

    @Override
    public void onAdDisplayed(final MaxAd maxAd) {
    }

    @Override
    public void onAdClicked(final MaxAd maxAd) {
    }

    @Override
    public void onAdHidden(final MaxAd maxAd) {
        // Interstitial ad is hidden. Pre-load the next ad
        interstitialAd.loadAd();
    }

    @Override
    public void onAdExpanded(final MaxAd maxAd) {
    }

    @Override
    public void onAdCollapsed(final MaxAd maxAd) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            if (interstitialAd.isReady()) {
                interstitialAd.showAd();
            } else {
                Log.e("interstitialAd", "interstitialAd not ready!");
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error showing interstitialAd: " + e.getMessage());
        }

        quitApp();
    }

    private long quit_time = 0;

    public void quitApp() {
        if ((System.currentTimeMillis() - quit_time) > 2000) {
            Toast.makeText(this, "Press again to quit.", Toast.LENGTH_LONG).show();
            quit_time = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}
