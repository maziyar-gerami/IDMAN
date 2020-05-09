package parsso.idman.backend.Repos;

import parsso.idman.backend.Models.OrganizationalUnit;

import java.util.List;

public interface OusRepo {

    public List<OrganizationalUnit> retrieve();
    public String create(OrganizationalUnit ou);
    public String update(OrganizationalUnit ou);
    public String remove(String name);
    public OrganizationalUnit retrieveOu(String name);

}