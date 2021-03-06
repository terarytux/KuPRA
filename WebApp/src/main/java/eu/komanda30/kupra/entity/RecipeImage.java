package eu.komanda30.kupra.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="recipe_image")
@SequenceGenerator(
        name="recipeImgIdSequence",
        sequenceName="recipe_image_seq",
        allocationSize=1
)
public class RecipeImage {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipeImgIdSequence")
    private Integer id;

    private String imageUrl;
    private String thumbUrl;

    //For hibernate
    protected RecipeImage() {
    }

    public RecipeImage(String imageUrl, String thumbUrl) {
        this.imageUrl = imageUrl;
        this.thumbUrl = thumbUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }
}
