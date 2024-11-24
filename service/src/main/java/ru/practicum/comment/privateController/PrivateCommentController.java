package ru.practicum.comment.privateController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CreateCommentDto;
import ru.practicum.comment.service.CommentService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentDto createComment(@PathVariable int userId, @PathVariable int eventId,
                                    @RequestBody @Valid CreateCommentDto createCommentDto) {
        return commentService.createComment(userId, eventId, createCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable int userId, @PathVariable int eventId,
                                    @PathVariable int commentId,
                                    @RequestBody @Valid CreateCommentDto updateComment) {
        return commentService.updateComment(userId, eventId, commentId, updateComment);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable int userId, @PathVariable int eventId, @PathVariable int commentId) {
        commentService.deleteComment(userId, eventId, commentId);
    }
}
