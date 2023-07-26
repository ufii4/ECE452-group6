package com.ece452.watfit.data.source.remote;

import com.ece452.watfit.data.models.Suggestion;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.POST;

public interface SuggestionService {
    @POST("v1/chat/completions")
    Flowable<Suggestion> getSuggestions(
            @retrofit2.http.Body ReqBody body
    );

    class ReqBody {
        private String model;
        private List<ChatMessage> messages;

        public ReqBody(String model, List<ChatMessage> messages) {
            this.model = model;
            this.messages = messages;
        }
    }

    class ChatMessage {
        private String role;
        private String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
