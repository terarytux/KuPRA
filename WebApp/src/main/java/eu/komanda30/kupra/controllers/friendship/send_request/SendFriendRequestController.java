package eu.komanda30.kupra.controllers.friendship.send_request;

import eu.komanda30.kupra.entity.Friendship;
import eu.komanda30.kupra.repositories.Friendships;
import eu.komanda30.kupra.repositories.KupraUsers;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/friends")
public class SendFriendRequestController {
    private static final Logger LOG = LoggerFactory.getLogger(SendFriendRequestController.class);
    @Resource
    private Friendships friendships;
    @Resource
    private KupraUsers kupraUsers;

    @ResponseBody
    @Transactional
    @RequestMapping(value="/sendRequest", method = RequestMethod.POST)
    public String sendRequest(@RequestParam("source_id") String targetId){
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final String username = auth.getName();

        Friendship friendship = new Friendship();
        friendship.setTarget(kupraUsers.findOne(targetId));
        friendship.setSource(kupraUsers.findByUsername(username));
        friendship.setFriendshipStatus(false);
        friendships.save(friendship);

        return "true";
    }
}
