-include= ~${workspace}/cnf/resources/bnd/feature.props
symbolicName=com.ibm.ws.applibtest-1.0
visibility=public
singleton=true
IBM-App-ForceRestart: install, \
 uninstall
IBM-API-Package: com.ibm.ws.applibtest.api;  type="ibm"
IBM-ShortName: appLibTest-1.0
Subsystem-Name: AppLibTest
-bundles=com.ibm.ws.applibtest; apiJar=false; location:="dev/api/ibm/,lib/"
kind=noship
edition=full
