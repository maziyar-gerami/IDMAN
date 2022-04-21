package parsso.idman.repos;

import org.springframework.http.HttpStatus;

@SuppressWarnings("SameReturnValue")
public interface SystemRefresh {
  HttpStatus userRefresh(String doer);

  HttpStatus captchaRefresh(String doer);

  HttpStatus serviceRefresh(String doer);

  HttpStatus all(String doer);

  void refreshLockedUsers();
}
