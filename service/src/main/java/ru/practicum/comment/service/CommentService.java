package ru.practicum.comment.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentStatusUpdateDto;
import ru.practicum.comment.dto.CreateCommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment (int userId, int eventId, CreateCommentDto createCommentDto);

    CommentDto updateComment (int userId, int eventId, int commentId, CreateCommentDto createCommentDto);

    void deleteComment (int userId, @PathVariable int eventId, int commentId);

    List<CommentDto> updateCommentStatus (int eventId, CommentStatusUpdateDto commentStatusUpdateDto);
}
