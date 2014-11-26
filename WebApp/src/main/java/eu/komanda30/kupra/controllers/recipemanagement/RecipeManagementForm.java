package eu.komanda30.kupra.controllers.recipemanagement;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by Ignas on 10/23/2014.
 */
public class RecipeManagementForm {

    @Size(min=1, max=256)
    private String name;

    @NotNull
    private int cookingTime;

    @NotNull
    private boolean publicAccess;

    @Size(min=1, max=256)
    private String description;

    @NotNull
    @Size(min=1, max=512)
    private String processDescription;

    @NotNull
    @Min(1)
    private int servings = 1;

    private boolean publicState;

    private String[] cookingTypesSelection = {"5","10","15"};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public void setCookingTime(int cookingTime) {
        this.cookingTime = cookingTime;
    }

    public boolean isPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProcessDescription() {
        return processDescription;
    }

    public void setProcessDescription(String processDescription) {
        this.processDescription = processDescription;
    }

    public String[] getCookingTypesSelection() {return cookingTypesSelection; }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public boolean isPublicState() {
        return publicState;
    }

    public void setPublicState(boolean publicState) {
        this.publicState = publicState;
    }

}
