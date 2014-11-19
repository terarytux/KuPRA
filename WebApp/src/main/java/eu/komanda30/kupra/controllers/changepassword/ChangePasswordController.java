package eu.komanda30.kupra.controllers.changepassword;

import eu.komanda30.kupra.entity.UserId;
import eu.komanda30.kupra.services.UserRegistrar;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Lukas on 2014.10.23.
 */
@Controller
@RequestMapping("/changepassword")
public class ChangePasswordController {
    @Resource
    private UserRegistrar userRegistrar;

    @RequestMapping(method = RequestMethod.POST)
    public String submit(@Valid final ChangePasswordForm form,
                         final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "changepassword";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        userRegistrar.changePassword(UserId.forUsername(auth.getName()), form.getPassword());
        return "changepassword";
    }
}