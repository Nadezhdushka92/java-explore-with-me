package ru.practicum.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    @Query("SELECT new ru.practicum.stats.ElementStatsResponseDto(e.app, e.uri, COUNT(e.ip)) " +
           "FROM Stats AS e  " +
           "WHERE e.createdDate BETWEEN :start AND :end " +
           " AND e.uri IN :uris " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(e.ip) DESC")
    List<ElementStatsResponseDto> getStatsNotOriginalIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stats.ElementStatsResponseDto(e.app, e.uri, COUNT( e.ip)) " +
           "FROM Stats AS e  " +
           "WHERE e.createdDate BETWEEN :start AND :end " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT( e.ip) DESC")
    List<ElementStatsResponseDto> getStatsNotOriginalIp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.stats.ElementStatsResponseDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
           "FROM Stats AS e  " +
           "WHERE e.createdDate BETWEEN :start AND :end " +
           "AND e.uri IN :uris " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ElementStatsResponseDto> getStatsOriginalIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.stats.ElementStatsResponseDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
           "FROM Stats AS e  " +
           "WHERE e.createdDate BETWEEN :start AND :end " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT( DISTINCT e.ip) DESC")
    List<ElementStatsResponseDto> getStatsOriginalIp(LocalDateTime start, LocalDateTime end);
}
