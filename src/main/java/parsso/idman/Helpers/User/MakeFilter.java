package parsso.idman.Helpers.User;


import org.springframework.ldap.filter.*;
import org.springframework.stereotype.Service;

@Service
public class MakeFilter {

    public static AndFilter makeUsersFilter(String groupFilter, String searchUid, String searchDisplayName, String userStatus) {

        AndFilter andFilter = new AndFilter();
        andFilter.and(new EqualsFilter("objectclass", "person"));
        if (groupFilter != null && !groupFilter.equals("")) andFilter.and(new EqualsFilter("ou", groupFilter));
        if (searchUid != null && !searchUid.equals("")) andFilter.and(new WhitespaceWildcardsFilter("uid", searchUid));
        if (searchDisplayName != null && !searchDisplayName.equals(""))
            andFilter.and(new WhitespaceWildcardsFilter("displayName", searchDisplayName));

        if (userStatus != null && !userStatus.equals("")) {

            if (userStatus.equalsIgnoreCase("Disable"))
                andFilter.and(new EqualsFilter("pwdAccountLockedTime", "40400404040404.950Z"));

            if (userStatus.equalsIgnoreCase("Lock")) {
                andFilter.and(new PresentFilter("pwdAccountLockedTime"));
                andFilter.and(new NotFilter(new EqualsFilter("pwdAccountLockedTime", "40400404040404.950Z")));
            }

            if (userStatus.equals("Enable"))
                andFilter.and(new NotPresentFilter("pwdAccountLockedTime"));
        }


        return andFilter;
    }
}
