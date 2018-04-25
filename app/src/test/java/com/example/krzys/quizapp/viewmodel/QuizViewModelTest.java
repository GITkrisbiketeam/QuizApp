package com.example.krzys.quizapp.viewmodel;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.rule.ActivityTestRule;

import com.example.krzys.quizapp.repository.QuizAppRepository;
import com.example.krzys.quizapp.ui.QuizActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@RunWith(JUnit4.class)
public class QuizViewModelTest {
/*     @Rule
    public ActivityTestRule<QuizActivity> mActivityRule = new ActivityTestRule<>(QuizActivity.class, true, true);

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private QuizViewModel mQuizViewModel;
    private QuizAppRepository mQuizAppRepository;

    @Before
    public void setup() {
        mQuizAppRepository = mock(QuizAppRepository.class);
        mQuizViewModel = new QuizViewModel(mActivityRule.getActivity().getApplication());
    }

   @Test
    public void testNull() {
        assertThat(userViewModel.getUser(), notNullValue());
        verify(userRepository, never()).loadUser(anyString());
        userViewModel.setLogin("foo");
        verify(userRepository, never()).loadUser(anyString());
    }

    @Test
    public void testCallRepo() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        userViewModel.getUser().observeForever(mock(Observer.class));
        userViewModel.setLogin("abc");
        verify(userRepository).loadUser(captor.capture());
        assertThat(captor.getValue(), is("abc"));
        reset(userRepository);
        userViewModel.setLogin("ddd");
        verify(userRepository).loadUser(captor.capture());
        assertThat(captor.getValue(), is("ddd"));
    }

    @Test
    public void sendResultToUI() {
        MutableLiveData<Resource<User>> foo = new MutableLiveData<>();
        MutableLiveData<Resource<User>> bar = new MutableLiveData<>();
        when(userRepository.loadUser("foo")).thenReturn(foo);
        when(userRepository.loadUser("bar")).thenReturn(bar);
        Observer<Resource<User>> observer = mock(Observer.class);
        userViewModel.getUser().observeForever(observer);
        userViewModel.setLogin("foo");
        verify(observer, never()).onChanged(any(Resource.class));
        User fooUser = TestUtil.createUser("foo");
        Resource<User> fooValue = Resource.success(fooUser);

        foo.setValue(fooValue);
        verify(observer).onChanged(fooValue);
        reset(observer);
        User barUser = TestUtil.createUser("bar");
        Resource<User> barValue = Resource.success(barUser);
        bar.setValue(barValue);
        userViewModel.setLogin("bar");
        verify(observer).onChanged(barValue);
    }

    @Test
    public void loadRepositories() {
        userViewModel.getRepositories().observeForever(mock(Observer.class));
        verifyNoMoreInteractions(repoRepository);
        userViewModel.setLogin("foo");
        verify(repoRepository).loadRepos("foo");
        reset(repoRepository);
        userViewModel.setLogin("bar");
        verify(repoRepository).loadRepos("bar");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void retry() {
        userViewModel.setLogin("foo");
        verifyNoMoreInteractions(repoRepository, userRepository);
        userViewModel.retry();
        verifyNoMoreInteractions(repoRepository, userRepository);
        Observer userObserver = mock(Observer.class);
        userViewModel.getUser().observeForever(userObserver);
        Observer repoObserver = mock(Observer.class);
        userViewModel.getRepositories().observeForever(repoObserver);

        verify(userRepository).loadUser("foo");
        verify(repoRepository).loadRepos("foo");
        reset(userRepository, repoRepository);

        userViewModel.retry();
        verify(userRepository).loadUser("foo");
        verify(repoRepository).loadRepos("foo");
        reset(userRepository, repoRepository);
        userViewModel.getUser().removeObserver(userObserver);
        userViewModel.getRepositories().removeObserver(repoObserver);

        userViewModel.retry();
        verifyNoMoreInteractions(userRepository, repoRepository);
    }

    @Test
    public void nullUser() {
        Observer<Resource<User>> observer = mock(Observer.class);
        userViewModel.setLogin("foo");
        userViewModel.setLogin(null);
        userViewModel.getUser().observeForever(observer);
        verify(observer).onChanged(null);
    }

    @Test
    public void nullRepoList() {
        Observer<Resource<List<Repo>>> observer = mock(Observer.class);
        userViewModel.setLogin("foo");
        userViewModel.setLogin(null);
        userViewModel.getRepositories().observeForever(observer);
        verify(observer).onChanged(null);
    }

    @Test
    public void dontRefreshOnSameData() {
        Observer<String> observer = mock(Observer.class);
        userViewModel.login.observeForever(observer);
        verifyNoMoreInteractions(observer);
        userViewModel.setLogin("foo");
        verify(observer).onChanged("foo");
        reset(observer);
        userViewModel.setLogin("foo");
        verifyNoMoreInteractions(observer);
        userViewModel.setLogin("bar");
        verify(observer).onChanged("bar");
    }

    @Test
    public void noRetryWithoutUser() {
        userViewModel.retry();
        verifyNoMoreInteractions(userRepository, repoRepository);
    }*/
}
