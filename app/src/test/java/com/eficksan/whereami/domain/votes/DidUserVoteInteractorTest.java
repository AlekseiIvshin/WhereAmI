package com.eficksan.whereami.domain.votes;

import com.eficksan.whereami.App;
import com.eficksan.whereami.BuildConfig;
import com.eficksan.whereami.RxJavaTestRunner;
import com.eficksan.whereami.data.votes.FirebaseDatabaseVotesRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.ValueEventListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.annotation.Config;

import rx.observers.TestSubscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Aleksei Ivshin
 * on 10.09.2016.
 */
@RunWith(RxJavaTestRunner.class)
@Config(constants = BuildConfig.class,
        application = App.class)
public class DidUserVoteInteractorTest {

    DidUserVoteInteractor didUserVoteInteractor;
    FirebaseDatabaseVotesRepository mockVotesRepository;

    @Before
    public void setUp() {
        mockVotesRepository = Mockito.mock(FirebaseDatabaseVotesRepository.class);
        didUserVoteInteractor = new DidUserVoteInteractor(mockVotesRepository);
    }

    @After
    public void tearDown() {
        if (didUserVoteInteractor != null) {
            didUserVoteInteractor.unsubscribe();
        }
    }

    @Test
    public void shouldReturnTrueWhenUserVotedBeforeFor() {
        // Given
        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(true);
        doNothing().when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));
        final String testMessageId = "test_message_id";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[1]).onDataChange(snapshot);
                return new Object();
            }
        }).when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // When
        didUserVoteInteractor.execute(testMessageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(true);
    }

    @Test
    public void shouldReturnTrueWhenUserVotedBeforeAgainst() {
        // Given
        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(false);
        doNothing().when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));
        final String testMessageId = "test_message_id";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[1]).onDataChange(snapshot);
                return new Object();
            }
        }).when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();


        // When
        didUserVoteInteractor.execute(testMessageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(true);
    }

    @Test
    public void shouldReturnErrorIfDataIsNotAvailable() {
        // Given
        didUserVoteInteractor = new DidUserVoteInteractor(mockVotesRepository);
        doNothing().when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));
        final String testMessageId = "test_message_id";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[1]).onCancelled(DatabaseError.fromException(new IllegalArgumentException("test")));
                return new Object();
            }
        }).when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // When
        didUserVoteInteractor.execute(testMessageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertError(DatabaseException.class);
    }

    @Test
    public void shouldReturnFalseWhenUserDidNotVoteBefore() {
        // Given
        didUserVoteInteractor = new DidUserVoteInteractor(mockVotesRepository);
        final DataSnapshot snapshot = mock(DataSnapshot.class);
        when(snapshot.getValue(Boolean.class)).thenReturn(null);
        doNothing().when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));
        final String testMessageId = "test_message_id";

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((ValueEventListener) arguments[1]).onDataChange(snapshot);
                return new Object();
            }
        }).when(mockVotesRepository).canVoteMessage(anyString(), any(ValueEventListener.class));

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();

        // When
        didUserVoteInteractor.execute(testMessageId, testSubscriber);

        // Then
        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertCompleted();
        testSubscriber.assertValue(false);
    }
}
