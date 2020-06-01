package parsso.idman.Repos;

import parsso.idman.Models.OrganizationalUnit;

import java.util.List;

public interface OusRepo {

    public List<OrganizationalUnit> retrieve();
    public String create(OrganizationalUnit ou);
    public String update(String name, OrganizationalUnit ou);
    public String remove(String ou);
    public String remove();
    public OrganizationalUnit retrieveOu(String name);
    public OrganizationalUnit retrieveOu();


}