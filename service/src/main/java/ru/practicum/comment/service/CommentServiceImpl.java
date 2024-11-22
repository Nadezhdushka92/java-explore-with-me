package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentStatusUpdateDto;
import ru.practicum.comment.dto.CreateCommentDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public CommentDto createComment(int userId, int eventId, CreateCommentDto createCommentDto) {
        User user = checkAndReturnUser(userId);
        Event event = checkAndReturnEvent(eventId);

        Comment comment = CommentMapper.createCommentDto(createCommentDto, user, event);
        Comment createComment = commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(createComment);

    }

    @Override
    public CommentDto updateComment(int userId, int eventId, int commentId, CreateCommentDto createCommentDto) {
        User user = checkAndReturnUser(userId);
        Event event = checkAndReturnEvent(eventId);
        Comment comment = CommentMapper.updateCommentDto(createCommentDto, user, event, commentId);

        commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(comment);

    }

    @Override
    public void deleteComment(int userId, int eventId, int commentId) {
        checkAndReturnUser(userId);
        checkAndReturnEvent(eventId);
        commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment not found", ""));

        commentRepository.deleteById(commentId);

    }

    @Override
    public List<CommentDto> updateCommentStatus(int eventId, CommentStatusUpdateDto commentStatusUpdateDto) {
            checkAndReturnEvent(eventId);

            List<Comment> comments = commentRepository.findByIdIn(commentStatusUpdateDto.getCommentsId());
            State status = State.valueOf(commentStatusUpdateDto.getStatus());

            for (Comment comment : comments) {
                if (comment.getModerationStatus() != State.PENDING) {
                    throw new ConflictException("Status should be PENDING", "");
                } else {
                    comment.setModerationStatus(status);
                }
            }

            List<Comment> newComments = commentRepository.saveAll(comments);

            return newComments.stream()
                    .map(CommentMapper::mapToCommentDto)
                    .toList();

    }


    private User checkAndReturnUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User id:" + userId + " not found", ""));
    }

    private Event checkAndReturnEvent(int eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event id:" + eventId + " not found", ""));
    }
}
