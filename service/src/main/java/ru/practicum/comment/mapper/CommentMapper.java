package ru.practicum.comment.mapper;

import ru.practicum.adapter.DateTimeAdapter;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CreateCommentDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment createCommentDto (CreateCommentDto createCommentDto, User user, Event event) {
        return Comment.builder()
                .creator(user)
                .event(event)
                .text(createCommentDto.getText())
                .createdOn(LocalDateTime.now())
                .moderationStatus(State.PENDING)
                .build();
    }

    public static CommentDto mapToCommentDto (Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .creator(comment.getCreator().getId())
                .event(comment.getEvent().getId())
                .text(comment.getText())
                .createdOn(DateTimeAdapter.toString(comment.getCreatedOn()))
                .moderationStatus(comment.getModerationStatus().name())
                .build();
    }

    public static Comment updateCommentDto(CreateCommentDto createCommentDto, User user, Event event, int commentId) {
        return Comment.builder()
                .id(commentId)
                .creator(user)
                .event(event)
                .text(createCommentDto.getText())
                .createdOn(LocalDateTime.now())
                .moderationStatus(State.PENDING)
                .build();
    }

}
