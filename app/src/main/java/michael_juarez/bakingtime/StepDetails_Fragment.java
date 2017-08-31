package michael_juarez.bakingtime;


import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import michael_juarez.bakingtime.Controller.RecipeController;
import michael_juarez.bakingtime.Controller.ScreenSizeController;
import michael_juarez.bakingtime.Model.Step;

import static android.content.ContentValues.TAG;
import static michael_juarez.bakingtime.R.id.step_details_next_button;
import static michael_juarez.bakingtime.R.id.step_details_previous_button;

/**
 * Step Pattern:
 * private String mId;
 * private String mShortDescription;
 * private String mVideoUrl;
 * private String mThumbnailUrl;
 */

public class StepDetails_Fragment extends Fragment implements RecipeController.FinishedLoadingRecipeRequest {

    //Assign the fragment's RecyclerView to a variable using ButterKnife Library
    @BindView(R.id.step_details_instruction_tv)
    TextView mInstructionTextView;

    @BindView(R.id.step_details_previous_button)
    Button mPreviousButton;
    @BindView(R.id.step_details_next_button)
    Button mNextButton;
    @BindView(R.id.exo_play)
    ImageButton mExoPlayButton;
    @BindView(R.id.exo_pause)
    ImageButton mExoPauseButton;
    @BindView(R.id.step_details_exoplayerview)
    SimpleExoPlayerView mExoPlayer;
    @BindView(R.id.step_details_recipeImageView)
    ImageView mRecipeImageView;

    private Unbinder unbinder;
    private RecipeController mRecipeController;
    private int mStepPosition;
    private int mStepDetailPosition;
    private Step mStep;
    private int mStepDetailCount;
    private String mRecipeImageURL;

    private int mContainer;
    private boolean mIsTablet;

    private int mResumeWindow;
    private long mResumePosition;
    private SimpleExoPlayer player;

    public static final String KEY_POSITION = "michael_juarez.baketime.steps_details_fragment.key_position";
    private static final String KEY_EXOPLAYER_RESUME_WINDOW = "michael_juarez.baketime.steps_details_fragment.key_exoplayer_resumewindow";
    private static final String KEY_EXOPLAYER_RESUME_POSITION = "michael_juarez.baketime.steps_details_fragment.key_exoplayer_resumeposition";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.step_details_fragment, container, false);
        setRetainInstance(true);
        unbinder = ButterKnife.bind(this, view);

        //Set up values for tablet-mode
        mIsTablet = ScreenSizeController.getInstance(getActivity(), false, 0).getIsTablet();
        mContainer = ScreenSizeController.getInstance(getActivity(), false, 0).getContainer();

        mRecipeController = RecipeController.getInstance(getActivity(), getResources().getString(R.string.recipe_location), this);

        //Get image url for this step
        mRecipeImageURL = mRecipeController.getRecipeList().get(mStepPosition).getImage();

        //Get Bundle Information that may have been passed in
        mStepPosition = getArguments().getInt(Steps_Fragment.KEY_POSITION);
        mStepDetailPosition = getArguments().getInt(StepDetails_Fragment.KEY_POSITION);
        mResumeWindow = getArguments().getInt(KEY_EXOPLAYER_RESUME_WINDOW);
        mResumePosition = getArguments().getLong(KEY_EXOPLAYER_RESUME_POSITION);

        setUpUi(mStepPosition, mStepDetailPosition);

        return view;
    }

    private void setUpUi(int stepPosition, int StepDetailPosition) {
        //Set up next and previous buttons
        if (mStepPosition < 0)
            showError();

        mStepDetailCount = mRecipeController.getRecipeList().get(mStepPosition).getSteps().size();

        if (mStepDetailPosition == 0)
            mPreviousButton.setVisibility(View.INVISIBLE);
        else if (mStepDetailPosition + 1 == mStepDetailCount)
            mNextButton.setVisibility(View.INVISIBLE);

        mStep = mRecipeController.getRecipeList().get(stepPosition).getSteps().get(StepDetailPosition);
        updateUi();
        hideImageShowVideo();
        setUpExoPlayer();
    }


    private void updateUi() {
        mInstructionTextView.setText(mStep.getDescription());
    }

    private void setUpExoPlayer() {

        Uri mp4VideoUri = Uri.parse(mStep.getVideoUrl());

        //If there is no video, check for image
        if (TextUtils.isEmpty(mp4VideoUri.toString())) {
            String imageURL = Uri.parse(mRecipeImageURL).toString();

            //If there is no thumbnail video, then show a default image
            if (TextUtils.isEmpty(imageURL)) {
                mExoPlayer.setPlayer(null);
                hideVideoShowImage("");
                return;
            }

            hideVideoShowImage(imageURL);
            return;
        }

        boolean needNewPlayer = player == null;

        if (needNewPlayer) {
            // 1. Create a default TrackSelector
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create the player
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "BakingTime"));
            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;
            if (haveResumePosition)
                player.seekTo(mResumeWindow, mResumePosition);

            player.prepare(videoSource);
        }

        // Prepare the player with the source.

        player.setPlayWhenReady(true);

        mExoPlayer.setPlayer(player);

        player.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                hideVideoShowImage("");

                switch (error.type) {
                    case ExoPlaybackException.TYPE_SOURCE:
                        Log.e(TAG, "TYPE_SOURCE: " + error.getSourceException().getMessage());
                        break;

                    case ExoPlaybackException.TYPE_RENDERER:
                        Log.e(TAG, "TYPE_RENDERER: " + error.getRendererException().getMessage());
                        break;

                    case ExoPlaybackException.TYPE_UNEXPECTED:
                        Log.e(TAG, "TYPE_UNEXPECTED: " + error.getUnexpectedException().getMessage());
                        break;
                }
            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showError() {
        Log.d("STEPS_FRAGMENT", "showError() called.");
    }

    @Override
    public void finishedLoadingList(boolean hadError) {
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            setUpExoPlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            updateResumePosition();
            player.release();
            player = null;
        }
    }

    private void updateResumePosition() {
        mResumeWindow = player.getCurrentWindowIndex();
        mResumePosition = Math.max(0, player.getContentPosition());
    }

    @OnClick(step_details_previous_button)
    public void pressedPreviousButton() {
        if (player != null)
            player.release();

        player = null;
        mResumeWindow = C.INDEX_UNSET;
        mResumePosition = 0;

        mStepDetailPosition--;
        mNextButton.setVisibility(View.VISIBLE);
        if (mStepDetailPosition == 0)
            mPreviousButton.setVisibility(View.INVISIBLE);

        setUpUi(mStepPosition, mStepDetailPosition);
    }

    @OnClick(step_details_next_button)
    public void pressedNextButton() {
        if (player != null)
            player.release();

        player = null;
        mResumeWindow = C.INDEX_UNSET;
        mResumePosition = 0;

        mStepDetailPosition++;
        mPreviousButton.setVisibility(View.VISIBLE);
        if ((mStepDetailPosition + 1) == mStepDetailCount)
            mNextButton.setVisibility(View.INVISIBLE);

        setUpUi(mStepPosition, mStepDetailPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateResumePosition();

        //Store the mStepPosition clicked in a new bundle
        Bundle bundle = new Bundle();
        bundle.putInt(Steps_Fragment.KEY_POSITION, mStepPosition);
        bundle.putInt(StepDetails_Fragment.KEY_POSITION, mStepDetailPosition);
        bundle.putInt(KEY_EXOPLAYER_RESUME_WINDOW, mResumeWindow);
        bundle.putLong(KEY_EXOPLAYER_RESUME_POSITION, mResumePosition);

        //Store the bundle inside a new Steps_Fragment
        Fragment stepDetailsFragment = new StepDetails_Fragment();
        stepDetailsFragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out)
                .replace(mContainer, stepDetailsFragment)
                .commit();
    }

    private void hideVideoShowImage(String imageURL) {
        mExoPlayer.setVisibility(View.INVISIBLE);
        mRecipeImageView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(imageURL))
            Picasso.with(getActivity()).load(imageURL).into(mRecipeImageView);
        else
            mRecipeImageView.setImageResource(R.drawable.bakingtimelogo);
    }

    private void hideImageShowVideo() {
        mExoPlayer.setVisibility(View.VISIBLE);
        mRecipeImageView.setVisibility(View.INVISIBLE);
    }
}


