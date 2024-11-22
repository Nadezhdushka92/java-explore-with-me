package ru.practicum.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.comment.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("SELECT c FROM Comment c WHERE c.event.id IN :eventIds AND c.moderationStatus = 'PUBLISHED'")
    List<Comment> findCommentsByEvents(@Param("eventIds") List<Integer> eventIds);

    List<Comment> findByIdIn(List<Integer> ids);
}
