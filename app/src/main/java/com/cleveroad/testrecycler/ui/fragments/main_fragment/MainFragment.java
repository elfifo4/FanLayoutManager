package com.cleveroad.testrecycler.ui.fragments.main_fragment;

import android.animation.Animator;
import android.os.Bundle;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.cleveroad.fanlayoutmanager.FanLayoutManager;
import com.cleveroad.fanlayoutmanager.FanLayoutManagerSettings;
import com.cleveroad.fanlayoutmanager.callbacks.FanChildDrawingOrderCallback;
import com.cleveroad.testrecycler.R;
import com.cleveroad.testrecycler.SportCardsUtils;
import com.cleveroad.testrecycler.ui.fragments.full_info_fragment.FullInfoTabFragment;


public class MainFragment extends Fragment {

    private FanLayoutManager mFanLayoutManager;

    private SportCardsAdapter mAdapter;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rvCards);

        FanLayoutManagerSettings fanLayoutManagerSettings = FanLayoutManagerSettings
            .newBuilder(getContext())
            .withFanRadius(true)
            .withAngleItemBounce(5)
            .withViewHeightDp(160)
            .withViewWidthDp(120)
            .build();

        mFanLayoutManager = new FanLayoutManager(requireContext(), fanLayoutManagerSettings);

        recyclerView.setLayoutManager(mFanLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new SportCardsAdapter(getContext());
        mAdapter.addAll(SportCardsUtils.generateSportCards());

        mAdapter.setOnItemClickListener((itemPosition, view1) -> {
            if (mFanLayoutManager.getSelectedItemPosition() != itemPosition) {
                mFanLayoutManager.switchItem(recyclerView, itemPosition);
            } else {
                mFanLayoutManager.straightenSelectedItem(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        onClick(view1, mFanLayoutManager.getSelectedItemPosition());
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });
            }
        });

        recyclerView.setAdapter(mAdapter);

        recyclerView.setChildDrawingOrderCallback(new FanChildDrawingOrderCallback(mFanLayoutManager));

        (view.findViewById(R.id.logo)).setOnClickListener(v -> mFanLayoutManager.collapseViews());

    }

    public void onClick(View view, int pos) {
        FullInfoTabFragment fragment = FullInfoTabFragment.newInstance(mAdapter.getModelByPos(pos));

        fragment.setSharedElementEnterTransition(new SharedTransitionSet());
        fragment.setEnterTransition(new Fade());
        setExitTransition(new Fade());
        fragment.setSharedElementReturnTransition(new SharedTransitionSet());

        requireActivity().getSupportFragmentManager()
            .beginTransaction()
            .addSharedElement(view, "shared")
            .replace(R.id.root, fragment)
            .addToBackStack(null)
            .commit();
    }

    public void onClick(int pos) {
        FullInfoTabFragment fragment = FullInfoTabFragment.newInstance(mAdapter.getModelByPos(pos));
        requireActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.root, fragment)
            .addToBackStack(null)
            .commit();
    }

    public boolean deselectIfSelected() {
        if (mFanLayoutManager.isItemSelected()) {
            mFanLayoutManager.deselectItem();
            return true;
        } else {
            return false;
        }
    }

}
