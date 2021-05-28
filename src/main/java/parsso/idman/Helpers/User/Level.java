package parsso.idman.Helpers.User;


import lombok.Getter;
import parsso.idman.Models.Users.User;

@Getter
public class Level {

    User doer;

    User user;

    public Level(User doer, User user) {
        this.doer = doer;
        this.user = user;
    }

    public int accessLevel() {

        if (user.getUserId().equalsIgnoreCase("su"))
            return 5;

        else if (levels.valueOf(user.getUsersExtraInfo().getRole()) == levels.USER)
            return 0;

        else if (levels.valueOf(user.getUsersExtraInfo().getRole()) == levels.PRESENTER)
            return 1;

        else if (levels.valueOf(user.getUsersExtraInfo().getRole()) == levels.ADMIN)
            return 2;

        else if (levels.valueOf(user.getUsersExtraInfo().getRole()) == levels.SUPPORTER)
            return 3;
        else
            return 4;

    }

    public boolean hasAccess() {

        if (doer.getUserId().equalsIgnoreCase("su"))
            return true;

        else if (levels.valueOf(doer.getUsersExtraInfo().getRole()).compareTo(levels.valueOf(user.getUsersExtraInfo().getRole())) == 0)
            return false;

        else if (levels.valueOf(doer.getUsersExtraInfo().getRole()).compareTo(levels.valueOf(user.getUsersExtraInfo().getRole())) > 0)
            return false;

        else return levels.valueOf(doer.getUsersExtraInfo().getRole()).compareTo(levels.valueOf(user.getUsersExtraInfo().getRole())) < 0;

    }

    enum levels {USER, PRESENTER, ADMIN, SUPPORTER, SUPERADMIN}
}
