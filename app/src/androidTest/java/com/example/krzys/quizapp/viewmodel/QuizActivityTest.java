package com.example.krzys.quizapp.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.krzys.quizapp.EspressoTestUtil;
import com.example.krzys.quizapp.data.dto.quiz.QuizData;
import com.example.krzys.quizapp.data.dto.quizzes.QuizzesItem;
import com.example.krzys.quizapp.ui.QuizActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {
    @Rule
    public ActivityTestRule<QuizActivity> mActivityRule = new ActivityTestRule<>(QuizActivity.class, true, true);

    private QuizViewModel mViewModel;
    //private FragmentBindingAdapters fragmentBindingAdapters;
    private MutableLiveData<QuizData> mQuizData = new MutableLiveData<>();
    private MutableLiveData<QuizzesItem> mQuizzesItem = new MutableLiveData<>();

    @Before
    public void init() throws Throwable {
        //TODO this uses Fragments and we need activity
        EspressoTestUtil.disableAnimations(mActivityRule);
//        UserFragment fragment = UserFragment.create("foo");
        mViewModel = mock(QuizViewModel.class);
//        when(mViewModel)).thenReturn(userData);
/*        when(mViewModel.getAllQuizzesListTypes()).thenReturn(mQuizzesItemList);
        doNothing().when(viewModel).setLogin(anyString());
        navigationController = mock(NavigationController.class);
        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);

        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        fragment.navigationController = navigationController;
        fragment.dataBindingComponent = () -> fragmentBindingAdapters;

        mActivityRule.getActivity().setFragment(fragment);
        mActivityRule.runOnUiThread(() -> fragment.binding.get().repoList.setItemAnimator(null));*/
    }
}
