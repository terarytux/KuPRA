package eu.komanda30.kupra.controllers.menu;

import eu.komanda30.kupra.controllers.reciperead.LackOfProductsForm;
import eu.komanda30.kupra.controllers.reciperead.LackOfProductsItem;
import eu.komanda30.kupra.entity.KupraUser;
import eu.komanda30.kupra.entity.Menu;
import eu.komanda30.kupra.entity.Recipe;
import eu.komanda30.kupra.entity.RecipeProduct;
import eu.komanda30.kupra.repositories.KupraUsers;
import eu.komanda30.kupra.repositories.Menus;
import eu.komanda30.kupra.repositories.Products;
import eu.komanda30.kupra.repositories.Recipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@RequestMapping("/menu")
@Controller
public class MenuController {

    private final static Logger LOG = LoggerFactory.getLogger(MenuController.class);

    @Resource
    private NewMenuItemFormValidator newMenuItemFormValidator;

    @Resource
    private RecipeCookFormValidator recipeCookFormValidator;

    @Resource
    private KupraUsers kupraUsers;

    @Resource
    private Menus menus;

    @Resource
    private Recipes recipes;

    @Resource
    private Products products;

    @InitBinder("newMenuItemForm")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(newMenuItemFormValidator);
    }

    @InitBinder("recipeCookForm")
    protected void initPrepareBinder(WebDataBinder binder) {
        binder.addValidators(recipeCookFormValidator);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showMenuContent(MenuListForm form,
                                    @RequestParam(value = "dateFrom", required = false) final String newDateFrom) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final KupraUser kupraUser = kupraUsers.findByUsername(auth.getName());

        String templateToRender = "menu";

        Date dateFrom = null;
        if (newDateFrom != null){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                dateFrom = format.parse(newDateFrom);
                templateToRender = "menu :: menuContainer";
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            dateFrom = c.getTime();
        }

        // Hardcoded 4 Collumns now
        for(int i=0; i<4; i++){
            // Adding 24 hour's to from date
            Date dateTo = new Date(dateFrom.getTime()+60*60*24*1000-1);
            Iterable<Menu> menusList = kupraUsers.findMenuByDate(kupraUser, dateFrom, dateTo);

            MenuListDay menuListDay = new MenuListDay();
            menuListDay.setDay(dateFrom);
            for(Menu menuItem : menusList){
                MenuListItem menuListItem = new MenuListItem();
                menuListItem.setDateTime(menuItem.getDateTime());
                menuListItem.setRecipeName(menuItem.getRecipe().getName());
                menuListItem.setMenuItemId(menuItem.getId());
                menuListItem.setCooked(menuItem.isCompleted());
                menuListDay.addMenuListItem(menuListItem);
            }
            form.addMenuListDay(menuListDay);
            dateFrom = new Date(dateFrom.getTime()+60*60*24*1000);
        }

        return templateToRender;
    }

    @Transactional
    @RequestMapping(value = "/calculateProducts", method = RequestMethod.GET)
    public String calculateLackOfProducts(Model model) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final KupraUser kupraUser = kupraUsers.findByUsername(auth.getName());

        ArrayList<RecipeProduct> productsNeeded =  kupraUser.getProductsNeededForMenu();

        ArrayList<RecipeProduct> productsLacking = kupraUser.getLackingProducts(productsNeeded);

        LackOfProductsForm form = new LackOfProductsForm();
        for(RecipeProduct productNeeded : productsLacking){

            LackOfProductsItem item = new LackOfProductsItem();

            item.setName(productNeeded.getProduct().getName());
            item.setProductId(productNeeded.getProduct().getId());
            item.setAmount(productNeeded.getQuantity().stripTrailingZeros());
            item.setUnit(productNeeded.getProduct().getUnit().getAbbreviation());


            form.addLackOfProductsItem(item);
        }


        model.addAttribute("lackOfProductsForm", form);

        return "popups/menuMissingProducts :: lackOfProducts";
    }

    @Transactional
    @RequestMapping(value = "/addProducts", method = RequestMethod.POST)
    public String addProducts(@Valid LackOfProductsForm form, final BindingResult bindingResult) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final KupraUser kupraUser = kupraUsers.findByUsername(auth.getName());

        if (bindingResult.hasErrors()){
            return "popups/menuMissingProducts :: lackOfProducts";
        }

        ArrayList<LackOfProductsItem> items = form.getLackOfProductsItems();
        for(LackOfProductsItem item : items){
            if (item.getAmount() != null && !item.getAmount().equals(new BigDecimal(0))){
                kupraUser.addFridgeItem( products.findOne(item.getProductId()), item.getAmount());
            }
        }

        return "popups/menuMissingProducts :: productsAdded";
    }

    @Transactional(rollbackFor=Exception.class)
    @RequestMapping(value = "/cook/{menuItem}", method = RequestMethod.POST)
    public String openCookModal(@Valid final RecipeCookForm form, final BindingResult bindingResult,
                                @PathVariable Integer menuItem) throws Exception {
        if (bindingResult.hasErrors()) {
            return "popups/cookRecipe :: menuCookModal";
        }
        Menu menu = menus.findOne(menuItem);
        KupraUser kupraUser = kupraUsers.findByMenu(menu);

        menu.setServings(form.getServings());
        //Manage Fridge
        Recipe recipe = menu.getRecipe();
        List<RecipeProduct> recipeProducts = recipe.getProductsNeeded(new BigDecimal(menu.getServings()));


        if (kupraUser.getLackingProducts(recipeProducts).isEmpty()) {
            kupraUser.consumeItemsFromFridge(menu);
            // Manage Menu entity
            menu.setDateTime(form.getDateTime());
            menu.setCompleted(true);
            menu.setScore(form.getScore());


            kupraUsers.save(kupraUser);
        } else {

            form.setMenuItemId(menuItem);
            bindingResult.rejectValue("recipeId","notEnoughProducts");
            return "popups/cookRecipe :: menuCookModal";
        }

        return "popups/cookRecipe :: menuCookedModal";
    }

    @Transactional
    @RequestMapping(value = "/review/{menuItemId}", method = RequestMethod.GET)
    public String openRevuewModal(@PathVariable Integer menuItemId, final RecipeCookForm form) {
        Menu menu = menus.findOne(menuItemId);
        String modalToLoad = "popups/cookRecipe :: menuCookModal";
        form.setName(menu.getRecipe().getName());
        form.setDateTime(menu.getDateTime());
        form.setMenuItemId(menu.getId());
        form.setServings(menu.getServings());
        form.setRecipeId(menu.getRecipe().getRecipeId());
        form.setScore(10);
        form.setCompleted(menu.isCompleted());
        if (menu.isCompleted()){
            modalToLoad = "popups/cookRecipe :: menuCookedModal";
            form.setScore(menu.getScore());
        }

        return modalToLoad;
    }

    @ResponseBody
    @Transactional
    @RequestMapping(value = "/remove/{menuItemId}", method = RequestMethod.POST)
    public String removeMenuItem(@PathVariable Integer menuItemId) {
        Menu menu = menus.findOne(menuItemId);
        KupraUser owner = kupraUsers.findByMenu(menu);
        owner.removeMenuItem(menu);
        kupraUsers.save(owner);

        return "";
    }



    @Transactional
    @RequestMapping(value = "/add/{recipeId}", method = RequestMethod.GET)
    public String openSubmitModal(@PathVariable Integer recipeId, final NewMenuItemForm form) {
        form.setRecipeName(recipes.findOne(recipeId).getName());
        Date currentTime = new Date();

        //Add 5 minutes to prevent submitting to past
        form.setDateTime(new Date(currentTime.getTime()+5*60*1000));

        form.setRecipeId(recipeId);
        form.setServings(1);
        return "add-to-menu-modal :: modalBodyFooter";
    }

    @Transactional
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String submit(@Valid final NewMenuItemForm form, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-to-menu-modal :: modalBodyFooter";
        }
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final KupraUser kupraUser = kupraUsers.findByUsername(auth.getName());

        final eu.komanda30.kupra.entity.Menu newMenuEntity = new Menu();
        newMenuEntity.setRecipe(recipes.findOne(form.getRecipeId()));
        newMenuEntity.setDateTime(form.getDateTime());
        newMenuEntity.setServings(form.getServings());
        kupraUser.addMeniuItem(newMenuEntity);
        kupraUsers.save(kupraUser);

        return "add-to-menu-modal :: modalBodyFooterSuccess";
    }

    @ModelAttribute(value = "shortLocale")
    public String shortLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().equals(new Locale("lt").getLanguage())) {  //TODO: galima bugo vieta (neatitinka localės lt-LT)
            return "lt";
        } else {
            return "en";
        }
    }

}
