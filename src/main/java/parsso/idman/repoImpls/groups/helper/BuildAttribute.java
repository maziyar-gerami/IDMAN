package parsso.idman.repoImpls.groups.helper;

import parsso.idman.models.groups.Group;

import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;

public class BuildAttribute {
    public Attributes build(Group group) {

        BasicAttribute attr = new BasicAttribute("objectclass");
        attr.add("extensibleObject");
        attr.add("organizationalUnit");
        attr.add("top");
        attr.add("top");

        Attributes attrs = new BasicAttributes();
        attrs.put(attr);
        attrs.put("name", group.getName());
        attrs.put("ou", group.getId());
        if (!group.getDescription().equals(""))
            attrs.put("description", group.getDescription());
        else
            attrs.put("description", " ");

        return attrs;
    }
}
