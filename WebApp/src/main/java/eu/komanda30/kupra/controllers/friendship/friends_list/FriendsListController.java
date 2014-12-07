package eu.komanda30.kupra.controllers.friendship.friends_list;

import eu.komanda30.kupra.entity.Friendship;
import eu.komanda30.kupra.entity.KupraUser;
import eu.komanda30.kupra.entity.UserProfile;
import eu.komanda30.kupra.repositories.Friendships;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendsListController {

    private static final Logger LOG = LoggerFactory.getLogger(FriendsListController.class);

    @Resource
    private Friendships friendships;

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public String showFriendsList(final FriendListForm form) {

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedUserId = auth.getName();

        List<Friendship> friendshipsList = friendships.findFriendsOf(loggedUserId);

        for (Friendship friendship : friendshipsList) {
            FriendListUnit friendListUnit = new FriendListUnit();

            KupraUser kupraUser = friendship.getFriendOf(loggedUserId);

            UserProfile userProfile = kupraUser.getUserProfile();

            friendListUnit.setName(userProfile.getName());
            friendListUnit.setSurname(userProfile.getSurname());
            friendListUnit.setSourceId(friendship.getSource().getUserId());
            friendListUnit.setUserId(kupraUser.getUserId());

            form.addFriendListUnit(friendListUnit);
        }
        return "friends_list";
    }
}
