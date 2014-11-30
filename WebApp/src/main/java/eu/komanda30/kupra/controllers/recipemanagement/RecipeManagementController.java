package eu.komanda30.kupra.controllers.recipemanagement;

import eu.komanda30.kupra.entity.UserId;
import eu.komanda30.kupra.repositories.Recipes;
import eu.komanda30.kupra.services.RecipeLibrary;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by Ignas on 10/23/2014.
 */
@Controller
public class RecipeManagementController {
    @Resource
    private RecipeManagementFormValidator recipeManagementFormValidator;

    @Resource
    private RecipeLibrary recipeLibrary;

    @Resource
    private Recipes recipes;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(recipeManagementFormValidator);
    }

    @RequestMapping(value="/recipe_create", method = RequestMethod.GET)
    public String showNewRecipeForm(final RecipeManagementForm form) {
        return "recipe_form";
    }

    @RequestMapping(value="/recipe_create", method = RequestMethod.POST)
    public String createRecipe(@Valid final RecipeManagementForm recipeManagementForm,
                         final BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return "recipe_form";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        recipeLibrary.addRecipe(recipeManagementForm, UserId.forUsername(auth.getName()));

        return "redirect:/recipes";
    }
}
