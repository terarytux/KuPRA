package eu.komanda30.kupra.controllers.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error/")
public class ErrorController {
    @RequestMapping("auth")
    //@ResponseBody
    public String authError() {
        return "authenticationError";
    }

    @RequestMapping("access_denied")
    public String accessDeniedError() {
        return "accessDeniedError";
    }

    @RequestMapping("notfound")
    //@ResponseBody
    public String notFoundError() {
        return "pageNotFoundError";
    }

    @RequestMapping("system")
    public String systemError() {
        return "systemError";
    }

    @RequestMapping("unsupported")
    public String methodNotSupportedError() {
        return "unsupprotedError";
    }

    @RequestMapping("generic")
    public String genericError() {
        return "genericError";
    }
}