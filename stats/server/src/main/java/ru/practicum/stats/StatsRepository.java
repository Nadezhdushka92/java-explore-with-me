package ru.practicum.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("SELECT e FROM Stats e " +
           "WHERE e.createdDate BETWEEN :start AND :end " +
           "AND  e.uri IN :uris")
    List<Stats> getStatsNotOriginalIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT e FROM Stats e " +
           "WHERE e.createdDate BETWEEN :start AND :end")
    List<Stats> getStatsNotOriginalIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT e FROM Stats e " +
           "WHERE e.id IN (SELECT MIN(e2.id) FROM Stats e2 " +
           "WHERE e2.createdDate BETWEEN :start AND :end " +
           "GROUP BY e2.ip, e2.app) " +
           "AND e.uri IN :uris")
    List<Stats> getStatsOriginalIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT e FROM Stats e " +
           "WHERE e.id IN (SELECT MIN(e2.id) FROM Stats e2 " +
           "WHERE e2.createdDate BETWEEN :start AND :end " +
           "GROUP BY e2.ip, e2.app)")
    List<Stats> getStatsOriginalIp(LocalDateTime start, LocalDateTime end);

}
