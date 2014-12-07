package eu.komanda30.kupra.entity;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

/**
 * Created by Gintare on 2014-12-07.
 */
@Table(name="menu")
@Entity
@SequenceGenerator(
        name="menuIdSequence",
        sequenceName="menu_seq",
        allocationSize=1

)
public class Menu {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="menuIdSequence")
    private Integer id;


    @Column(nullable = false)
    private int recipe_id;

    @Column(nullable = false)
    private Date date_time;

    public Date getDate_time() {
        return date_time;
    }

    public void setDate_time(Date date_time) {
        this.date_time = date_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

}

