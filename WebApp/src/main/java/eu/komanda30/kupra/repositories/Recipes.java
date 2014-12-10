package eu.komanda30.kupra.repositories;

import eu.komanda30.kupra.entity.KupraUser;
import eu.komanda30.kupra.entity.Product;
import eu.komanda30.kupra.entity.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Ignas on 10/23/2014.
 */
public interface Recipes extends CrudRepository<Recipe, Integer> {
    @Query("from Recipe where publicAccess = true")
    Iterable<Recipe> findByPublic();

    @Query("from Recipe where author = :user")
    List<Recipe> findByUser(@Param("user") KupraUser user, Pageable pageable);

    @Query("from Recipe where author = :user and publicAccess = true")
    List<Recipe> findByUserPublic(@Param("user") KupraUser user, Pageable pageable);

    @Query("select r from Friendship f join f.target.recipes r where f.source = :user and f.friendshipStatus = true")
    List<Recipe> findFriendsRecipes(@Param("user") KupraUser user, Pageable pageable);

    @Query("select distinct r from Recipe r left join r.author.friendships f where r.author = :user or f.target = :user or r.publicAccess = true")
    List<Recipe> findAllAccessible(@Param("user") KupraUser user, Pageable pageable);

    @Query("select (count(*) > 0) from RecipeProduct r where r.product = :product")
    Boolean isUsedProduct(@Param("product") Product product);
}
