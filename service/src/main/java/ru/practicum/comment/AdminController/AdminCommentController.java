package ru.practicum.comment.AdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentStatusUpdateDto;
import ru.practicum.comment.service.CommentService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/events/{eventId}/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @PatchMapping
    public List<CommentDto> updateCommentStatus(@PathVariable int eventId,
                                                @Valid @RequestBody CommentStatusUpdateDto commentUpdateStatus) {
        return commentService.updateCommentStatus(eventId, commentUpdateStatus);
    }
}
