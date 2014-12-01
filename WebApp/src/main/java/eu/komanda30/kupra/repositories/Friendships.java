package eu.komanda30.kupra.repositories;

import eu.komanda30.kupra.entity.Friendship;
import eu.komanda30.kupra.entity.UserId;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by Ignas on 11/27/2014.
 */
public interface Friendships extends CrudRepository<Friendship, Integer> {
    @Query("select f from Friendship f join f.source s join f.target t where (s.id = :user_id or t.id = :user_id) and f.friendshipStatus = true")
    List<Friendship> findFriendsOf(@Param("user_id") UserId userId);

    @Query("select f from Friendship f join f.source s join f.target t where (s.id = :user_id or t.id = :user_id) and f.friendshipStatus = false")
    List<Friendship> findNotificationsOf(@Param("user_id") UserId userId);
}
