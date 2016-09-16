package com.eficksan.whereami.presentation.message;

import com.eficksan.whereami.data.messages.PlacingMessage;
import com.eficksan.whereami.domain.messages.FindMessageInteractor;
import com.eficksan.whereami.domain.users.FindUserInteractor;
import com.eficksan.whereami.domain.votes.DidUserVoteInteractor;
import com.eficksan.whereami.domain.votes.FetchingVotesCountInteractor;
import com.eficksan.whereami.domain.votes.VotingInteractor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import rx.Observable;
import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Aleksei_Ivshin on 9/13/16.
 */
public class MessageDetailsPresenterTest {

    public static String messageId = "messageId";

    FindUserInteractor mockFindUserInteractor;

    FindMessageInteractor mockFindMessageInteractor;

    DidUserVoteInteractor mockDidUserVoteInteractor;

    VotingInteractor mockVotingInteractor;

    FetchingVotesCountInteractor mockVotesCountInteractor;

    MessageDetailsView mockView;

    MessageDetailsPresenter presenter;

    @Before
    public void setUp() throws Exception {
        mockFindMessageInteractor = mock(FindMessageInteractor.class);
        mockFindUserInteractor = mock(FindUserInteractor.class);
        mockDidUserVoteInteractor = mock(DidUserVoteInteractor.class);
        mockVotingInteractor = mock(VotingInteractor.class);
        mockVotesCountInteractor = mock(FetchingVotesCountInteractor.class);
        presenter = new MessageDetailsPresenter(
                mockFindUserInteractor,
                mockFindMessageInteractor,
                mockDidUserVoteInteractor,
                mockVotingInteractor,
                mockVotesCountInteractor);

        mockView = mock(MessageDetailsView.class);
        when(mockView.getVotingForClickEvents()).thenReturn(Observable.<Void>empty());
        when(mockView.getVotingAgainstClickEvents()).thenReturn(Observable.<Void>empty());

        presenter.setView(mockView);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldSubscribeOnClickEvents() {
        // When
        presenter.onCreate(messageId);

        // Then
        verify(mockView, times(1)).getVotingForClickEvents();
        verify(mockView, times(1)).getVotingAgainstClickEvents();
    }

    @Test
    public void shouldRequestMessageContent() {
        // Given
        doNothing().when(mockFindMessageInteractor).execute(anyString(), Matchers.<Subscriber<PlacingMessage>>any());

        // When
        presenter.onCreate(messageId);

        // Then
        verify(mockFindMessageInteractor, times(1)).execute(anyString(), Matchers.<Subscriber<PlacingMessage>>any());
    }

    @Test
    public void shouldHandleMessageContentResponse() {
        // Given
        final PlacingMessage stubPlaceMessage = new PlacingMessage(42, 101, "Test");
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((Subscriber<PlacingMessage>) arguments[1]).onNext(stubPlaceMessage);
                ((Subscriber<PlacingMessage>) arguments[1]).onCompleted();
                return null;
            }
        }).when(mockFindMessageInteractor).execute(anyString(), Matchers.<Subscriber<PlacingMessage>>any());
        doNothing().when(mockView).showMessage(any(PlacingMessage.class));

        // When
        presenter.onCreate(messageId);

        // Then
        verify(mockFindMessageInteractor, times(1)).execute(anyString(), Matchers.<Subscriber<PlacingMessage>>any());
        verify(mockFindMessageInteractor, times(1)).unsubscribe();
        verify(mockView, times(1)).showMessage(any(PlacingMessage.class));
    }

    @Test
    public void shouldHandleMessageContentResponseError() {
        // Given
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((Subscriber<PlacingMessage>) arguments[1]).onError(new Throwable("Error message"));
                return null;
            }
        }).when(mockFindMessageInteractor).execute(anyString(), Matchers.<Subscriber<PlacingMessage>>any());
        doNothing().when(mockView).showMessage(any(PlacingMessage.class));

        // When
        presenter.onCreate(messageId);

        // Then
        verify(mockFindMessageInteractor, times(1)).execute(anyString(), Matchers.<Subscriber<PlacingMessage>>any());
        verify(mockFindMessageInteractor, times(1)).unsubscribe();
        verify(mockView, times(0)).showMessage(any(PlacingMessage.class));
    }

    @Test
    public void shouldUnsubscribeMessageContentObservable() {
        // Given
        doNothing().when(mockFindMessageInteractor).execute(anyString(), Matchers.<Subscriber<PlacingMessage>>any());

        // When
        presenter.onCreate(messageId);
        presenter.onDestroy();

        // Then
        verify(mockFindMessageInteractor, times(1)).unsubscribe();
    }

}
