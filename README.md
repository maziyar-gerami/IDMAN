**IDMAN Overview**

IDMAN is a client for CAS system which is initialized based on CAS WAR overlay that is deployed for managing users using a central CAS system

**prerequisites**

* CAS 6.3.x which is installed on a server and we called this server "CAS Server"
* JDK 11



**Configuration**

You need to change a file, named `application.propperties` before deploying the project.

This file is in the `resources` directory which contains all configuration variables in the project. Now we discuss all variables that you have to change:

1.  Email server configurations

This settings is for setting information about the smtp server. This part is include of below mentioned variable:
*  `spring.mail.host` : SMTP server address
*  `spring.mail.port` : SMTP server port
*  `spring.mail.username` : Username or email address for entering the system
*  `spring.mail.password` : the corresponding password for above mentioned username

2.  IDMAN Settings

this part of configurasion is for configuration about your client
*  `base.url` : client address that connects to CAS server. `Example: https://www.idman.com`
*  `sever.port`: this the IDMAN server port the client port. `Example: 8080`

*NOTE 1*: if you don't specified any ports for the IDMA, don't need to specify `sever.port` variable.

*NOTE 2*: Don't miss `sever.port` variable with CAS server port. Actually this is defined port for IDMAN

3.  CAS Server settings
*  `cas.url.login.path`: CAS login url of the CAS server. Example: `https://cas.server.com/cas/login`
*  `cas.url.logout.path` : CAS logout url of the CAS server. Example: `https://cas.server.com/cas/logout`




**Deploy**

this project uses GRADLE build tool. To build the project, use:

* if you have installed GRADLE SDK tools:
`./gradle clean build`

* else
 `./gradlew[.bat] clean build`

Then you can find the WAR file under this folder:

`\build\libs`




