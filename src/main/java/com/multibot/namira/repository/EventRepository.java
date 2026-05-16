package com.multibot.namira.repository;

import com.multibot.namira.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    // Εδώ θα μπορούμε αργότερα να βρούμε events ανά δημιουργό αν χρειαστεί
}