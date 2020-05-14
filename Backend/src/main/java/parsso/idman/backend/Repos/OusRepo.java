package parsso.idman.backend.Repos;

import parsso.idman.backend.Models.OrganizationalUnit;

import java.util.List;

public interface OusRepo {

    public List<OrganizationalUnit> retrieve();
    public String create(OrganizationalUnit ou);
<<<<<<< HEAD
    public String update(String name, OrganizationalUnit ou);
    public String remove(String ou);
    public String remove();
    public OrganizationalUnit retrieveOu(String name);
    public OrganizationalUnit retrieveOu();

=======
    public String update(OrganizationalUnit ou);
    public String remove(String name);
    public OrganizationalUnit retrieveOu(String name);
>>>>>>> a0d0f3a5dec3208e6b55845dd55e395a072dce44

}