# MCAuthTester
###Small Tool For Testing Minecraft Authentication on Problem Servers
This is a small tool for testing whether or not a server will properly connect to the Mojang Authentication Servers. If you have a server that is throwing SSL errors while trying to authenticate a user, this tool is something to use. As of right now, it contains the Mojang SSL Certificate but will NOT automatically save it to the keystore (WIP). To create the keystore, you need to run the following commands:<br/><br/>
Extract Mojang SSL Certificate:
```
unzip -p mojang.crt >mojang.crt
```
Create Keystore:
```
keytool -importcert -keystore mojang.ts -storepass [ksPass] -file mojang.crt
```
[ksPass] = Password to secure the keystore
<br/><br/>
#####How To Run MCAuthTester:
```
java -jar MCAuthTester.jar [username] [password] {useKeystore} {ksPass} {store}
```
[username] = Your Minecraft username (unmigrated account) or Mojang Account email (migrated account).<br/>
[password] = Your Minecraft or Mojang Account password. (Does NOT get saved or exported)<br/>
{useKeystore} = Boolean flag whether to use a custom keystore or the default keystore. (Optional)<br/>
{ksPass} = Must be provided if "useKeystore" is designated to create/access the keystore. (Optional)<br/>
{store} = Boolean flag whether to create a custom keystore or not for the Mojang SSL Public Cert. (WIP)


